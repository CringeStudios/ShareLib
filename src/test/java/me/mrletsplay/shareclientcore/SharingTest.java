package me.mrletsplay.shareclientcore;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import me.mrletsplay.shareclientcore.connection.DummyConnection;
import me.mrletsplay.shareclientcore.connection.message.Message;
import me.mrletsplay.shareclientcore.document.SharedDocument;

public class SharingTest {

	private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890";

	@Test
	public void testSharedDocumentSimple() {
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

	private void performRandomEdit(Random r, SharedDocument document) {
		if(document.getContents().length == 0 || r.nextBoolean()) {
			char[] insert = new char[r.nextInt(16)];
			for(int j = 0; j < insert.length; j++) insert[j] = CHARS.charAt(r.nextInt(CHARS.length()));
			document.localInsert(r.nextInt(document.getContents().length + 1), String.valueOf(insert));
		}else {
			int len = document.getContents().length;
			int idx = r.nextInt(len);
			int n = r.nextInt(Math.min(16, len - idx)) + 1;
			document.localDelete(idx, n);
		}
	}

	@Test
	public void testSharedDocumentRandom() {
		DummyConnection a = new DummyConnection(0);
		DummyConnection b = new DummyConnection(1);

		a.setSendMessageHandler(m -> b.receive(m));
		b.setSendMessageHandler(m -> a.receive(m));

		SharedDocument sharedA = new SharedDocument(a, "doc");
		SharedDocument sharedB = new SharedDocument(b, "doc");

		a.addListener(sharedA);
		b.addListener(sharedB);

		Random r = new Random(0);
		for(int i = 0; i < 10_000; i++) {
			performRandomEdit(r, r.nextBoolean() ? sharedA : sharedB);
			assertEquals(sharedA.getContentsAsString(), sharedB.getContentsAsString());
		}
	}

	@Test
	public void testSharedDocumentInterleaving() {
		List<Message> messagesA = new ArrayList<>();
		List<Message> messagesB = new ArrayList<>();

		DummyConnection a = new DummyConnection(0);
		DummyConnection b = new DummyConnection(1);

		a.setSendMessageHandler(messagesA::add);
		b.setSendMessageHandler(messagesB::add);

		SharedDocument sharedA = new SharedDocument(a, "doc");
		SharedDocument sharedB = new SharedDocument(b, "doc");

		a.addListener(sharedA);
		b.addListener(sharedB);

		Random r = new Random(2);
		for(int i = 0; i < 1000; i++) {
			// Perform some random edits
			for(int j = 0; j < 100; j++) {
				boolean bEdit = r.nextBoolean();
				performRandomEdit(r, bEdit ? sharedA : sharedB);
			}

			// Randomly interleave messages
			while(!messagesA.isEmpty() || !messagesB.isEmpty()) {
				if(messagesB.isEmpty() || (!messagesA.isEmpty() && r.nextBoolean())) {
					b.receive(messagesA.remove(0));
				}else {
					a.receive(messagesB.remove(0));
				}
			}

			// At this point, both documents should be in sync again
			assertEquals(sharedA.getContentsAsString(), sharedB.getContentsAsString());
			assertEquals(sharedA.getCharBag().toList(), sharedB.getCharBag().toList());
		}
	}

	@Test
	public void testShareDocumentDeleteAndReinsert() {
		List<Message> messagesA = new ArrayList<>();
		List<Message> messagesB = new ArrayList<>();

		DummyConnection a = new DummyConnection(0);
		DummyConnection b = new DummyConnection(1);

		a.setSendMessageHandler(messagesA::add);
		b.setSendMessageHandler(messagesB::add);

		SharedDocument sharedA = new SharedDocument(a, "doc");
		SharedDocument sharedB = new SharedDocument(b, "doc");

		a.addListener(sharedA);
		b.addListener(sharedB);

		sharedA.localInsert(0, "Test");
		messagesA.forEach(b::receive);
		messagesA.clear();
		assertEquals("Test", sharedA.getContentsAsString());
		assertEquals("Test", sharedB.getContentsAsString());

		sharedA.localDelete(3, 1);
		sharedA.localInsert(3, "t");
		assertEquals("Test", sharedA.getContentsAsString());

		messagesA.forEach(b::receive);
		messagesA.clear();

		assertEquals("Test", sharedA.getContentsAsString());
	}

}
