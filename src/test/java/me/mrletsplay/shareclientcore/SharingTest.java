package me.mrletsplay.shareclientcore;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
	}

}
