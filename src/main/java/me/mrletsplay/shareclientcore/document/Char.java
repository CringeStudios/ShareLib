package me.mrletsplay.shareclientcore.document;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import me.mrletsplay.shareclientcore.connection.SerializableObject;

public record Char(Identifier[] position, int lamport, char value) implements SerializableObject {

	public static final Char START_OF_DOCUMENT = new Char(new Identifier[] { new Identifier(1, 0) }, 0, '^');
	public static final Char END_OF_DOCUMENT = new Char(new Identifier[] { new Identifier(Util.BASE - 1, 0) }, 0, '$');

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(position);
		result = prime * result + Objects.hash(lamport, value);
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
		Char other = (Char) obj;
		return lamport == other.lamport && Arrays.equals(position, other.position) && value == other.value;
	}

	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeInt(position.length);
		for(int i = 0; i < position.length; i++) {
			Identifier id = position[i];
			out.writeInt(id.digit());
			out.writeInt(id.site());
		}

		out.writeInt(lamport);
		out.writeChar(value);
	}

	public static Char deserialize(DataInputStream in) throws IOException {
		Identifier[] pos = new Identifier[in.readInt()];
		for(int i = 0; i < pos.length; i++) {
			pos[i] = new Identifier(in.readInt(), in.readInt());
		}

		int lamport = in.readInt();
		char value = in.readChar();
		return new Char(pos, lamport, value);
	}

}
