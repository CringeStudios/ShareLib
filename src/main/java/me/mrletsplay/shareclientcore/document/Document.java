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

	/**
	 * Inserts characters into the document at the specified index
	 * @param index The index to insert at
	 * @param str The string to insert
	 */
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

	/**
	 * Deletes n characters from a document, starting at the specified index
	 * @param index The index to start deleting at
	 * @param n The number of characters to delete
	 */
	public void localDelete(int index, int n) {
		if(index < 0 || index + n >= charBag.size() - 1) throw new IllegalArgumentException("Index out of bounds");

		while(n-- > 0) {
			// TODO: more efficient implementation (e.g. range delete in CharBag)
			charBag.remove(charBag.get(index + 1));
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
