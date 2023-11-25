package me.mrletsplay.shareclientcore.document;

import java.util.Arrays;
import java.util.Objects;

public record Char(Identifier[] position, int lamport, char value) {

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

}
