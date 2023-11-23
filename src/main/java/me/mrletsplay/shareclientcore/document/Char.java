package me.mrletsplay.shareclientcore.document;

public record Char(Identifier[] position, int lamport, char value) {

	public static final Char START_OF_DOCUMENT = new Char(new Identifier[] { new Identifier(1, 0) }, 0, '^');
	public static final Char END_OF_DOCUMENT = new Char(new Identifier[] { new Identifier(Util.BASE - 1, 0) }, 0, '$');

}
