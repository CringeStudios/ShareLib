package me.mrletsplay.shareclientcore;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import me.mrletsplay.shareclientcore.connection.Change;
import me.mrletsplay.shareclientcore.connection.ChangeType;
import me.mrletsplay.shareclientcore.connection.message.ChangeMessage;
import me.mrletsplay.shareclientcore.connection.message.ChecksumMessage;
import me.mrletsplay.shareclientcore.connection.message.ClientHelloMessage;
import me.mrletsplay.shareclientcore.connection.message.FullSyncMessage;
import me.mrletsplay.shareclientcore.connection.message.Message;
import me.mrletsplay.shareclientcore.connection.message.PeerJoinMessage;
import me.mrletsplay.shareclientcore.connection.message.PeerLeaveMessage;
import me.mrletsplay.shareclientcore.connection.message.RequestChecksumMessage;
import me.mrletsplay.shareclientcore.connection.message.RequestFullSyncMessage;
import me.mrletsplay.shareclientcore.connection.message.ServerHelloMessage;
import me.mrletsplay.shareclientcore.document.Char;
import me.mrletsplay.shareclientcore.document.Identifier;

public class MessageTest {

	private static byte[] serialize(Message m) throws IOException {
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		DataOutputStream dOut = new DataOutputStream(bOut);
		dOut.writeUTF(m.getType().name());
		m.serialize(dOut);
		return bOut.toByteArray();
	}

	private static Message deserialize(byte[] bytes) throws IOException {
		return Message.deserialize(ByteBuffer.wrap(bytes));
	}

	@Test
	public void testChangeMessage() throws IOException {
		Change[] changes = {
				new Change(ChangeType.ADD, new Char(new Identifier[] {new Identifier(0, 1), new Identifier(1, 3)}, 42, (byte) 'e')),
				new Change(ChangeType.REMOVE, new Char(new Identifier[] {new Identifier(2, 1), new Identifier(1, 4)}, 314, (byte) 'q')),

		};
		ChangeMessage m = new ChangeMessage("Project:src/test.txt", changes);
		assertEquals(deserialize(serialize(m)), m, "Deserialized message must equal message");
	}

	@Test
	public void testClientHelloMessage() throws IOException {
		ClientHelloMessage m = new ClientHelloMessage("User123", "abcdefg");
		assertEquals(deserialize(serialize(m)), m, "Deserialized message must equal message");
	}

	@Test
	public void testPeerJoinMessage() throws IOException {
		PeerJoinMessage m = new PeerJoinMessage("User234", 2);
		assertEquals(deserialize(serialize(m)), m, "Deserialized message must equal message");
	}

	@Test
	public void testPeerLeaveMessage() throws IOException {
		PeerLeaveMessage m = new PeerLeaveMessage(2);
		assertEquals(deserialize(serialize(m)), m, "Deserialized message must equal message");
	}

	@Test
	public void testServerHelloMessage() throws IOException {
		ServerHelloMessage m = new ServerHelloMessage(42, 3);
		assertEquals(deserialize(serialize(m)), m, "Deserialized message must equal message");
	}

	@Test
	public void testChecksumMessage() throws IOException {
		ChecksumMessage m = new ChecksumMessage(2, "Project:src/test.txt", new byte[] {1, 2, 3, 4, 5, 6});
		assertEquals(deserialize(serialize(m)), m, "Deserialized message must equal message");
	}

	@Test
	public void testFullSyncMessage() throws IOException {
		List<Char> chars = Arrays.asList(
			new Char(new Identifier[] {new Identifier(0, 1), new Identifier(1, 3)}, 42, (byte) 'e'),
			new Char(new Identifier[] {new Identifier(0, 1), new Identifier(1, 3), new Identifier(1, 4)}, 333, (byte) 'f')
		);
		FullSyncMessage m = new FullSyncMessage(2, "Project:src/test.txt", chars);
		assertEquals(deserialize(serialize(m)), m, "Deserialized message must equal message");
	}

	@Test
	public void testRequestChecksumMessage() throws IOException {
		RequestChecksumMessage m = new RequestChecksumMessage(2, "Project:src/test.txt");
		assertEquals(deserialize(serialize(m)), m, "Deserialized message must equal message");
	}

	@Test
	public void testRequestFullSyncMessage() throws IOException {
		RequestFullSyncMessage m = new RequestFullSyncMessage(2, "Project:src/test.txt");
		assertEquals(deserialize(serialize(m)), m, "Deserialized message must equal message");
	}

}
