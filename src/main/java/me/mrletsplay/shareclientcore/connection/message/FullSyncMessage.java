package me.mrletsplay.shareclientcore.connection.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.shareclientcore.document.Char;

public record FullSyncMessage(int siteID, String documentPath, List<Char> content) implements AddressableMessage {

	@Override
	public MessageType getType() {
		return MessageType.FULL_SYNC;
	}

	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeInt(siteID);
		out.writeUTF(documentPath);

		out.writeInt(content.size());
		for(Char c : content) {
			c.serialize(out);
		}
	}

	public static FullSyncMessage deserialize(DataInputStream in) throws IOException {
		try {
			int siteID = in.readInt();
			String documentPath = in.readUTF();

			int contentSize = in.readInt();
			List<Char> content = new ArrayList<>(contentSize);
			for(int i = 0; i < contentSize; i++) {
				content.add(Char.deserialize(in));
				System.out.println(content.get(content.size() - 1));
			}

			return new FullSyncMessage(siteID, documentPath, content);
		}catch(IllegalArgumentException e) {
			throw new IOException("Invalid content length", e);
		}
	}

}
