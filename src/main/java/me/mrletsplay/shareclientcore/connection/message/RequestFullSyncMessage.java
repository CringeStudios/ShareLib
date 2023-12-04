package me.mrletsplay.shareclientcore.connection.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public record RequestFullSyncMessage(int siteID, String documentPath) implements AddressableMessage {

	@Override
	public MessageType getType() {
		return MessageType.REQUEST_FULL_SYNC;
	}

	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeInt(siteID);
		out.writeBoolean(documentPath != null);
		if(documentPath != null) out.writeUTF(documentPath);
	}

	public static RequestFullSyncMessage deserialize(DataInputStream in) throws IOException {
		return new RequestFullSyncMessage(in.readInt(), in.readBoolean() ? in.readUTF() : null);
	}

}
