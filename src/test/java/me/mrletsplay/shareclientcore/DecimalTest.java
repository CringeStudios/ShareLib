package me.mrletsplay.shareclientcore;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

import me.mrletsplay.shareclientcore.document.Identifier;
import me.mrletsplay.shareclientcore.document.Util;

public class DecimalTest {

	@Test
	public void testSimpleSubtraction() {
		Identifier[] a = new Identifier[] { new Identifier(1, 0), new Identifier(2, 0), new Identifier(3, 0) };
		Identifier[] b = new Identifier[] { new Identifier(1, 0), new Identifier(1, 0), new Identifier(3, 0) };
		assertArrayEquals(Util.subtract(a, b, 0), new int[] { 0, 1, 0 });
	}

	@Test
	public void testCarrySubtraction() {
		Identifier[] a = new Identifier[] { new Identifier(2, 0), new Identifier(0, 0), new Identifier(4, 0) };
		Identifier[] b = new Identifier[] { new Identifier(1, 0), new Identifier(0, 0), new Identifier(5, 0) };
		assertArrayEquals(Util.subtract(a, b, 0), new int[] { 0, 9, 9 });
	}

	@Test
	public void testOffsetSubtraction() {
		Identifier[] a = new Identifier[] { new Identifier(1, 0), new Identifier(0, 0), new Identifier(5, 0) };
		Identifier[] b = new Identifier[] { new Identifier(1, 0), new Identifier(0, 0), new Identifier(4, 0) };
		assertArrayEquals(Util.subtract(a, b, 1), new int[] { 0, 1 });
	}

}
