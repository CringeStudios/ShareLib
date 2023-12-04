package me.mrletsplay.shareclientcore.connection.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public record FullSyncMessage(int siteID, String documentPath, byte[] content) implements AddressableMessage {

	@Override
	public MessageType getType() {
		return MessageType.FULL_SYNC;
	}

	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeInt(siteID);
		out.writeUTF(documentPath);
		out.writeInt(content.length);
		out.write(content);
	}

	public static FullSyncMessage deserialize(DataInputStream in) throws IOException {
		try {
			return new FullSyncMessage(in.readInt(), in.readUTF(), in.readNBytes(in.readInt()));
		}catch(IllegalArgumentException e) {
			throw new IOException("Invalid content length", e);
		}
	}

}
