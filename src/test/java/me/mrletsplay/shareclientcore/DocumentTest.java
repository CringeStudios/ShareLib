package me.mrletsplay.shareclientcore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import me.mrletsplay.shareclientcore.document.Document;

public class DocumentTest {

	@Test
	public void testLocalInsert() {
		Document doc = new Document(1);
		doc.localInsert(0, "Hello");
		assertEquals("Hello", doc.getContents());
		doc.localInsert(5, " World");
		assertEquals("Hello World", doc.getContents());
		doc.localInsert(5, " Test");
		assertEquals("Hello Test World", doc.getContents());
	}

	@Test
	public void testLocalInsertInvalidIndexFails() {
		Document doc = new Document(1);
		doc.localInsert(0, "Hello");
		assertThrows(IllegalArgumentException.class, () -> doc.localInsert(-1, "Test"));
		assertThrows(IllegalArgumentException.class, () -> doc.localInsert(6, "Test"));
	}

	@Test
	public void testLocalDelete() {
		Document doc = new Document(1);
		doc.localInsert(0, "Hello World!");
		doc.localDelete(5, 6);
		assertEquals("Hello!", doc.getContents());
	}

	@Test
	public void testLocalDeleteInvalidIndexFails() {
		Document doc = new Document(1);
		doc.localInsert(0, "Hello World!");
		assertThrows(IllegalArgumentException.class, () -> doc.localDelete(-1, 10));
		assertThrows(IllegalArgumentException.class, () -> doc.localDelete(12, 1));
		assertThrows(IllegalArgumentException.class, () -> doc.localDelete(0, 13));
	}

}
