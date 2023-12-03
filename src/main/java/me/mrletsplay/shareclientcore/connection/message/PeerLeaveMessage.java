package me.mrletsplay.shareclientcore.connection.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public record PeerLeaveMessage(int peerSiteID) implements Message {

	@Override
	public MessageType getType() {
		return MessageType.PEER_LEAVE;
	}

	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeInt(peerSiteID);
	}

	public static PeerLeaveMessage deserialize(DataInputStream in) throws IOException {
		return new PeerLeaveMessage(in.readInt());
	}

}
