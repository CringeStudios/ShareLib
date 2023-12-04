package me.mrletsplay.shareclientcore.connection.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public record RequestChecksumMessage(int siteID, String documentPath) implements AddressableMessage {

	@Override
	public MessageType getType() {
		return MessageType.REQUEST_CHECKSUM;
	}

	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeInt(siteID);
		out.writeUTF(documentPath);
	}

	public static RequestChecksumMessage deserialize(DataInputStream in) throws IOException {
		return new RequestChecksumMessage(in.readInt(), in.readUTF());
	}

}
