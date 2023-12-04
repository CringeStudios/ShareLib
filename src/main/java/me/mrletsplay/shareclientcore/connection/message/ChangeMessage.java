package me.mrletsplay.shareclientcore.connection.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import me.mrletsplay.shareclientcore.connection.Change;
import me.mrletsplay.shareclientcore.connection.ChangeType;
import me.mrletsplay.shareclientcore.document.Char;

public record ChangeMessage(Change change) implements Message {

	@Override
	public MessageType getType() {
		return MessageType.CHANGE;
	}

	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeUTF(change.documentPath());
		out.writeUTF(change.type().name());
		change.character().serialize(out);
	}

	public static ChangeMessage deserialize(DataInputStream in) throws IOException {
		try {
			return new ChangeMessage(new Change(in.readUTF(), ChangeType.valueOf(in.readUTF()), Char.deserialize(in)));
		}catch(IllegalArgumentException e) {
			throw new IOException("Invalid change type", e);
		}
	}

}
