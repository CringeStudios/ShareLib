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

	public static int[] getIncremented(Identifier[] i1, Identifier[] i2, int offset) {
		// TODO: can potentially be optimized by just calculating the index of the first non-zero digit (e.g. don't return array and discard it -> just return the index)
		int[] delta = subtract(i2, i1, offset);
		int firstNonZero = 0;
		for(int i = 0; i < delta.length; i++) {
			if(delta[i] != 0) {
				firstNonZero = i;
				break;
			}
		}

		// Because of math, (firstNonZero + 1) >= i1.length (I think)
		// then make the array 1 longer so we have space for the increment (which is one order of magnitude smaller)
		int[] incremented = new int[firstNonZero + 2];
		for(int i = 0; i < incremented.length; i++) {
			incremented[i] = i <= i1.length ? i1[i].digit() : 0;
		}

		add1AtIndex(incremented, firstNonZero + 1); // last digit might be zero, which is ambigious, inc again
		if(incremented[incremented.length - 1] == 0) add1AtIndex(incremented, firstNonZero + 1);
		return incremented;
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

		if(carry == 1) throw new RuntimeException("Can't subtract numbers (are you sure a > b?)");

		return diff;
	}

	public static void add1AtIndex(int[] a, int index) {
		// Technically, some of the overflow preventions are probably overkill since the biggest carry we can get is 1 (we're only adding a 1 somewhere after all)

		int carry = 0;
		for(int i = a.length - 1; i >= 0; i--) {
			int dA = a[i];
			int dB = (index == i ? 1 : 0) + carry;

			carry = dB - (BASE - dA) + 1; // Calculate carry and be overflow-safe
			if(carry < 0) carry = 0;

			a[i] = carry != 0 ? -BASE + dA + dB : dA + dB; // Calculate sum of digits and be overflow-safe
		}

		if(carry == 1) throw new RuntimeException("Can't add numbers: Too large");
	}

}
