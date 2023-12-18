package me.mrletsplay.shareclientcore.document;

public record Identifier(int digit, int site) implements Comparable<Identifier> {

	public static final int DEFAULT_SITE = 0;

	@Override
	public int compareTo(Identifier o) {
		int tmp;
		if((tmp = Integer.compare(digit, o.digit)) != 0) return tmp;
		return Integer.compare(site, o.site);
	}

}
