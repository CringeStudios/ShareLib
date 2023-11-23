package me.mrletsplay.shareclientcore.document;

public class Document {

	private CharBag charBag;
	private int site;
	private int lamport;

	public Document(int site) {
		this.charBag = new ArrayCharBag();
		charBag.add(Char.START_OF_DOCUMENT);
		charBag.add(Char.END_OF_DOCUMENT);

		this.site = site;
	}

	public void localInsert(int index, String str) {
		if(index < 0 || index >= charBag.size() - 1) throw new IllegalArgumentException("Index out of bounds");

		Char charBefore = charBag.get(index);
		Char charAfter = charBag.get(index +1);

		for(char c : str.toCharArray()) {
			Identifier[] newPos = Util.generatePositionBetween(charBefore.position(), charAfter.position(), site);
			lamport++;
			Char ch = new Char(newPos, lamport, c);
			charBag.add(ch);
			charBefore = ch;
		}
	}

	public CharBag getCharBag() {
		return charBag;
	}

	public String getContents() {
		String contents = charBag.toString();
		return contents.substring(1, contents.length() - 1);
	}

}
