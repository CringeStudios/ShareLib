package me.mrletsplay.shareclientcore.connection.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public record ChecksumMessage(int siteID, String documentPath, byte[] checksum) implements AddressableMessage {

	@Override
	public MessageType getType() {
		return MessageType.CHECKSUM;
	}

	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeInt(siteID);
		out.writeUTF(documentPath);
		out.writeInt(checksum.length);
		out.write(checksum);
	}

	public static ChecksumMessage deserialize(DataInputStream in) throws IOException {
		try {
			return new ChecksumMessage(in.readInt(), in.readUTF(), in.readNBytes(in.readInt()));
		}catch(IllegalArgumentException e) {
			throw new IOException("Invalid checksum length", e);
		}
	}

}
