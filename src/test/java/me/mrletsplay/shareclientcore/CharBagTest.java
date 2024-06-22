package me.mrletsplay.shareclientcore;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import me.mrletsplay.shareclientcore.document.ArrayCharBag;
import me.mrletsplay.shareclientcore.document.Char;
import me.mrletsplay.shareclientcore.document.CharBag;
import me.mrletsplay.shareclientcore.document.Identifier;

public class CharBagTest {

	private static List<CharBag> getBags() {
		return List.of(new ArrayCharBag()/*, new BinaryTreeCharBag() TODO implement BinaryTreeCharBag */);
	}

	@ParameterizedTest
	@MethodSource("getBags")
	public void testCharBagAdd(CharBag bag) {
		Char c = new Char(new Identifier[] { new Identifier(0, 0) }, 0, (byte) 0);
		assertEquals(0, bag.add(c));
		assertEquals(List.of(c), bag.toList());
	}

	@ParameterizedTest
	@MethodSource("getBags")
	public void testCharBagAddBetween(CharBag bag) {
		Char c = new Char(new Identifier[] { new Identifier(0, 0) }, 0, (byte) 0);
		Char c2 = new Char(new Identifier[] { new Identifier(1, 0) }, 1, (byte) 0);
		Char cBetween = new Char(new Identifier[] { new Identifier(0, 1) }, 2, (byte) 0);

		bag.add(c);
		bag.add(c2);

		assertEquals(List.of(c, c2), bag.toList());

		assertEquals(1, bag.add(cBetween));

		assertEquals(List.of(c, cBetween, c2), bag.toList());
	}

	@ParameterizedTest
	@MethodSource("getBags")
	public void testCharBagRemove(CharBag bag) {
		Char c = new Char(new Identifier[] { new Identifier(0, 0) }, 0, (byte) 0);
		bag.add(c);
		assertEquals(0, bag.remove(c));
		assertEquals(Collections.emptyList(), bag.toList());
	}

	@ParameterizedTest
	@MethodSource("getBags")
	public void testCharBagGet(CharBag bag) {
		Char c = new Char(new Identifier[] { new Identifier(0, 0) }, 0, (byte) 0);
		Char c2 = new Char(new Identifier[] { new Identifier(0, 1) }, 2, (byte) 0);
		Char c3 = new Char(new Identifier[] { new Identifier(1, 0) }, 1, (byte) 0);

		bag.add(c);
		bag.add(c3);
		bag.add(c2);

		assertEquals(List.of(c, c2, c3), bag.toList());
		assertEquals(c2, bag.get(1));
	}

	@ParameterizedTest
	@MethodSource("getBags")
	public void testCharBagClear(CharBag bag) {
		Char c = new Char(new Identifier[] { new Identifier(0, 0) }, 0, (byte) 0);
		Char c2 = new Char(new Identifier[] { new Identifier(0, 1) }, 2, (byte) 0);
		Char c3 = new Char(new Identifier[] { new Identifier(1, 0) }, 1, (byte) 0);

		bag.add(c);
		bag.add(c3);
		bag.add(c2);

		assertEquals(List.of(c, c2, c3), bag.toList());

		bag.clear();

		assertEquals(Collections.emptyList(), bag.toList());
	}

}
