package me.mrletsplay.shareclientcore.document;

import java.util.ArrayList;
import java.util.List;

public class Util {

	public static final int BASE = 10;

	public static int comparePositions(Identifier[] a, Identifier[] b) {
		for(int i = 0; i < Math.min(a.length, b.length); i++) {
			int tmp;
			if((tmp = a[i].compareTo(b[i])) != 0) return tmp;
		}

		return Integer.compare(a.length, b.length);
	}

	public static Identifier[] generatePositionBetween(Identifier[] before, Identifier[] after, int site) {
		List<Identifier> newPosition = new ArrayList<>();

		for(int i = 0; i < Math.min(before.length, after.length) + 1; i++) {
			Identifier c1 = i >= before.length ? new Identifier(0, site) : before[i];
			Identifier c2 = i >= after.length ? new Identifier(BASE, site) : after[i];

			if(c1.digit() != c2.digit()) {
				// TODO: generate delta, then pick a value between
			}

			if(c1.site() == c2.site()) {
				newPosition.add(c1);
				continue; // Identifiers are equal, compare next digit
			}

			if(c1.site() < c2.site()) {
				// Anything starting with before will be sorted before after
				newPosition.add(new Identifier(BASE, site));
				return newPosition.toArray(Identifier[]::new);
			}

			throw new RuntimeException("Invalid site order");
		}

		return null;
	}

	public static void getIncrement(Identifier[] i1, Identifier[] i2, int offset) {
	}

	/**
	 * Subtract b from a, where a > b
	 */
	public static int[] subtract(Identifier[] a, Identifier[] b, int offset) {
		int carry = 0;
		int[] diff = new int[Math.max(a.length, b.length) - offset];

		for(int i = diff.length - 1; i >= 0; i--) {
			int idx = i + offset;
			int dA = (idx >= a.length ? 0 : a[idx].digit()) - carry;
			int dB = idx >= b.length ? 0 : b[idx].digit();
			if(dA < dB) {
				carry = 1;
				diff[i] = dA - dB + BASE;
			}else {
				carry = 0;
				diff[i] = dA - dB;
			}
		}

		return diff;
	}

}
