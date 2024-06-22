package me.mrletsplay.shareclientcore.connection.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import me.mrletsplay.shareclientcore.connection.Change;
import me.mrletsplay.shareclientcore.connection.ChangeType;
import me.mrletsplay.shareclientcore.document.Char;

public record ChangeMessage(String documentPath, Change[] changes) implements Message {

	@Override
	public MessageType getType() {
		return MessageType.CHANGE;
	}

	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeUTF(documentPath);
		out.writeInt(changes.length);
		for(int i = 0; i < changes.length; i++) {
			out.writeUTF(changes[i].type().name());
			changes[i].character().serialize(out);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(changes);
		result = prime * result + Objects.hash(documentPath);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChangeMessage other = (ChangeMessage) obj;
		return Arrays.equals(changes, other.changes) && Objects.equals(documentPath, other.documentPath);
	}

	public static ChangeMessage deserialize(DataInputStream in) throws IOException {
		try {
			String documentPath = in.readUTF();
			Change[] changes = new Change[in.readInt()];
			for(int i = 0; i < changes.length; i++) {
				changes[i] = new Change(ChangeType.valueOf(in.readUTF()), Char.deserialize(in));
			}
			return new ChangeMessage(documentPath, changes);
		}catch(IllegalArgumentException e) {
			throw new IOException("Invalid change type", e);
		}
	}

}
