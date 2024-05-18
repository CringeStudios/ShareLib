package me.mrletsplay.shareclientcore.connection.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(checksum);
		result = prime * result + Objects.hash(documentPath, siteID);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChecksumMessage other = (ChecksumMessage) obj;
		return Arrays.equals(checksum, other.checksum) && Objects.equals(documentPath, other.documentPath)
				&& siteID == other.siteID;
	}

	public static ChecksumMessage deserialize(DataInputStream in) throws IOException {
		try {
			return new ChecksumMessage(in.readInt(), in.readUTF(), in.readNBytes(in.readInt()));
		}catch(IllegalArgumentException e) {
			throw new IOException("Invalid checksum length", e);
		}
	}

}
