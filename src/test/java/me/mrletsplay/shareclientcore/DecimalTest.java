package me.mrletsplay.shareclientcore;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import me.mrletsplay.shareclientcore.document.Identifier;
import me.mrletsplay.shareclientcore.document.Util;

public class DecimalTest {

	@Test
	public void testSimpleSubtraction() {
		Identifier[] a = new Identifier[] { new Identifier(1, 0), new Identifier(2, 0), new Identifier(3, 0) };
		Identifier[] b = new Identifier[] { new Identifier(1, 0), new Identifier(1, 0), new Identifier(3, 0) };
		assertArrayEquals(new int[] { 0, 1, 0 }, Util.subtract(a, b, 0));
	}

	@Test
	public void testCarrySubtraction() {
		Identifier[] a = new Identifier[] { new Identifier(2, 0), new Identifier(0, 0), new Identifier(4, 0) };
		Identifier[] b = new Identifier[] { new Identifier(1, 0), new Identifier(0, 0), new Identifier(5, 0) };
		assertArrayEquals(new int[] { 0, Util.BASE - 1, Util.BASE - 1 }, Util.subtract(a, b, 0));
	}

	@Test
	public void testOffsetSubtraction() {
		Identifier[] a = new Identifier[] { new Identifier(1, 0), new Identifier(0, 0), new Identifier(5, 0) };
		Identifier[] b = new Identifier[] { new Identifier(1, 0), new Identifier(0, 0), new Identifier(4, 0) };
		assertArrayEquals(new int[] { 0, 1 }, Util.subtract(a, b, 1));
	}

	@Test
	public void testReversedInputFails() {
		Identifier[] a = new Identifier[] { new Identifier(1, 0), new Identifier(1, 0), new Identifier(3, 0) };
		Identifier[] b = new Identifier[] { new Identifier(1, 0), new Identifier(2, 0), new Identifier(3, 0) };
		assertThrows(RuntimeException.class, () -> Util.subtract(a, b, 0));
	}

	@Test
	public void testSimpleAdd1() {
		int[] a = new int[] { 0, 0 };
		Util.add1AtIndex(a, 1);
		assertArrayEquals(new int[] { 0, 1 }, a);
	}

	@Test
	public void testCarryAdd1() {
		int[] a = new int[] { 0, Util.BASE - 1 };
		Util.add1AtIndex(a, 1);
		assertArrayEquals(new int[] { 1, 0 }, a);
	}

	@Test
	public void testCarryAdd1_2() {
		int[] a = new int[] { 0, Util.BASE - 1, Util.BASE - 1 };
		Util.add1AtIndex(a, 2);
		assertArrayEquals(new int[] { 1, 0, 0 }, a);
	}

	@Test
	public void testCarryAdd1_3() {
		int[] a = new int[] { 0, Util.BASE - 1, Util.BASE - 1 };
		Util.add1AtIndex(a, 1);
		assertArrayEquals(new int[] { 1, 0, Util.BASE - 1 }, a);
	}

}
