package me.mrletsplay.shareclientcore.connection.message;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import me.mrletsplay.shareclientcore.connection.SerializableObject;

public interface Message extends SerializableObject {

	public MessageType getType();

	public static Message deserialize(ByteBuffer buffer) throws IOException {
		byte[] bytesArray = new byte[buffer.remaining()];
		buffer.get(bytesArray);

		try {
			DataInputStream dIn = new DataInputStream(new ByteArrayInputStream(bytesArray));
			MessageType type = MessageType.valueOf(dIn.readUTF());

			Message m;
			switch(type) {
				case CLIENT_HELLO -> m = ClientHelloMessage.deserialize(dIn);
				case SERVER_HELLO -> m = ServerHelloMessage.deserialize(dIn);
				case PEER_JOIN -> m = PeerJoinMessage.deserialize(dIn);
				case PEER_LEAVE -> m = PeerLeaveMessage.deserialize(dIn);
				case CHANGE -> m = ChangeMessage.deserialize(dIn);
				case REQUEST_FULL_SYNC -> m = RequestFullSyncMessage.deserialize(dIn);
				case REQUEST_CHECKSUM -> m = RequestChecksumMessage.deserialize(dIn);
				case FULL_SYNC -> m = FullSyncMessage.deserialize(dIn);
				case CHECKSUM -> m = ChecksumMessage.deserialize(dIn);
				default -> m = new BasicMessage(type);// TODO: implement missing message types and delete BasicMessage
			}

			return m;
		}catch(IllegalArgumentException e) {
			throw new IOException("Invalid message type", e);
		}
	}

}
