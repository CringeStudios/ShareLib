package me.mrletsplay.shareclientcore.connection.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public record PeerJoinMessage(String peerName, int peerSiteID) implements Message {

	@Override
	public MessageType getType() {
		return MessageType.PEER_JOIN;
	}

	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeUTF(peerName);
		out.writeInt(peerSiteID);
	}

	public static PeerJoinMessage deserialize(DataInputStream in) throws IOException {
		return new PeerJoinMessage(in.readUTF(), in.readInt());
	}

}
