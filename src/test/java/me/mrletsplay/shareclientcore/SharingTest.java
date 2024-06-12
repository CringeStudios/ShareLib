package me.mrletsplay.shareclientcore;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;

import org.junit.jupiter.api.Test;

import me.mrletsplay.shareclientcore.connection.DummyConnection;
import me.mrletsplay.shareclientcore.document.SharedDocument;

public class SharingTest {

	@Test
	public void testSharedDocument() {
		DummyConnection a = new DummyConnection(0);
		DummyConnection b = new DummyConnection(1);

		a.setSendMessageHandler(m -> b.receive(m));
		b.setSendMessageHandler(m -> a.receive(m));

		SharedDocument sharedA = new SharedDocument(a, "doc");
		SharedDocument sharedB = new SharedDocument(b, "doc");

		a.addListener(sharedA);
		b.addListener(sharedB);

		sharedA.localInsert(0, "Hello World!");

		assertEquals("Hello World!", sharedA.getContentsAsString());
		assertEquals("Hello World!", sharedB.getContentsAsString());

		sharedB.localInsert(0, "This is a test!");

		assertEquals("This is a test!Hello World!", sharedA.getContentsAsString());
		assertEquals("This is a test!Hello World!", sharedB.getContentsAsString());

		sharedA.localDelete(10, 11);

		assertEquals("This is a World!", sharedA.getContentsAsString());
		assertEquals("This is a World!", sharedB.getContentsAsString());

		sharedB.localDelete(7, 8);

		assertEquals("This is!", sharedA.getContentsAsString());
		assertEquals("This is!", sharedB.getContentsAsString());
	}

	@Test
	public void testSharedDocument2() {
		DummyConnection a = new DummyConnection(0);
		DummyConnection b = new DummyConnection(1);

		a.setSendMessageHandler(m -> b.receive(m));
		b.setSendMessageHandler(m -> a.receive(m));

		SharedDocument sharedA = new SharedDocument(a, "doc");
		SharedDocument sharedB = new SharedDocument(b, "doc");

		a.addListener(sharedA);
		b.addListener(sharedB);

		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890";

		Random r = new Random(0);
		for(int i = 0; i < 1000; i++) {
			switch(r.nextInt(sharedA.getContents().length == 0 ? 2 : 4)) {
				case 0: {
					// Insert A
					char[] insert = new char[r.nextInt(16)];
					for(int j = 0; j < insert.length; j++) insert[j] = chars.charAt(r.nextInt(chars.length()));
					sharedA.localInsert(r.nextInt(sharedA.getContents().length + 1), String.valueOf(insert));
				}
				case 1: {
					// Insert B
					char[] insert = new char[r.nextInt(16)];
					for(int j = 0; j < insert.length; j++) insert[j] = chars.charAt(r.nextInt(chars.length()));
					sharedB.localInsert(r.nextInt(sharedB.getContents().length + 1), String.valueOf(insert));
				}
				case 2: {
					// Delete A
					int len = sharedA.getContents().length;
					int idx = r.nextInt(len);
					int n = r.nextInt(len - idx);
					sharedA.localDelete(idx, n);
				}
				case 3: {
					// Delete B
					int len = sharedB.getContents().length;
					int idx = r.nextInt(len);
					int n = r.nextInt(len - idx + 1);
					sharedB.localDelete(idx, n);
				}
			}

			System.out.println("A: " + sharedA.getContentsAsString());
			System.out.println("B: " + sharedB.getContentsAsString());
			assertEquals(sharedA.getContentsAsString(), sharedB.getContentsAsString());
		}
	}

}
