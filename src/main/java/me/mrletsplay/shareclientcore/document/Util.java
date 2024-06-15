package me.mrletsplay.shareclientcore.document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {

	// Source for a lot of the code: https://digitalfreepen.com/2017/10/06/simple-real-time-collaborative-text-editor.html

	public static final int BASE = 10;

	public static int comparePositions(Identifier[] a, Identifier[] b) {
		for(int i = 0; i < Math.min(a.length, b.length); i++) {
			int tmp;
			if((tmp = a[i].compareTo(b[i])) != 0) return tmp;
		}

		return Integer.compare(a.length, b.length);
	}

	public static int compareChars(Char a, Char b) {
		int pos = comparePositions(a.position(), b.position());
		if(pos != 0) return pos;
		return Integer.compare(a.lamport(), b.lamport());
	}

	public static Identifier[] generatePositionBetween(Identifier[] before, Identifier[] after, int site) {
		if(comparePositions(before, after) != -1) throw new IllegalArgumentException("before must be strictly less than after");

		List<Identifier> newPosition = new ArrayList<>();

		for(int i = 0; i < Math.max(before.length, after.length) + 1; i++) {
			Identifier c1 = i >= before.length ? new Identifier(0, site) : before[i];
			Identifier c2 = i >= after.length ? new Identifier(BASE - 1, site) : after[i];

			// Modification to the original source because otherwise it can lead to cases where positions can't be generated and this seems to work
			if(i >= before.length || c1.digit() != c2.digit()) {
				int[] incremented = getIncremented(before, after, i);
				return constructPosition(incremented, before, after, site);
			}

			if(c1.site() == c2.site()) {
				newPosition.add(c1);
				continue; // Identifiers are equal, compare next digit
			}

			if(c1.site() < c2.site()) {
				// Anything starting with before will be sorted before after
				newPosition.add(c1);
				newPosition.add(new Identifier(1, site));
				return newPosition.toArray(Identifier[]::new);
			}

			System.err.println("Got invalid state");
			System.err.println(c1 + "/" + c2);
			System.err.println(Arrays.toString(before));
			System.err.println(Arrays.toString(after));
			throw new RuntimeException("Invalid site order");
		}

		return newPosition.toArray(Identifier[]::new);
	}

	public static int[] getIncremented(Identifier[] before, Identifier[] after, int offset) {
		// TODO: can potentially be optimized by just calculating the index of the first non-zero digit (e.g. don't return array and discard it -> just return the index)
		int[] delta = subtract(after, before, offset);
		int firstNonZero = 0;
		for(int i = 0; i < delta.length; i++) {
			if(delta[i] != 0) {
				firstNonZero = i;
				break;
			}
		}

		// Because of math, (firstNonZero + 1) >= i1.length (I think)
		// then make the array 1 longer so we have space for the increment (which is one order of magnitude smaller)
		int[] incremented = new int[offset + firstNonZero + 2];
		for(int i = 0; i < incremented.length; i++) {
			incremented[i] = i < before.length ? before[i].digit() : 0;
		}

		add1AtIndex(incremented, offset + firstNonZero + 1); // last digit might be zero, which is ambigious, inc again
		if(incremented[incremented.length - 1] == 0) add1AtIndex(incremented, offset + firstNonZero + 1);
		return incremented;
	}

	public static Identifier[] constructPosition(int[] num, Identifier[] before, Identifier[] after, int site) {
		// Implements rules according to constructPosition from https://inria.hal.science/inria-00432368/document

		Identifier[] ident = new Identifier[num.length];

		for(int i = 0; i < num.length; i++) {
			int digit = num[i];

			if(i == num.length - 1) {
				ident[i] = new Identifier(digit, site);
			}else if(i < before.length && digit == before[i].digit()) {
				ident[i] = before[i];
			}else if(i < after.length && digit == after[i].digit()) {
				ident[i] = after[i];
			}else {
				ident[i] = new Identifier(digit, site);
			}
		}

		return ident;
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
