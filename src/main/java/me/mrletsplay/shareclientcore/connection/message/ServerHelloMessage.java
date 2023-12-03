package me.mrletsplay.shareclientcore.connection.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public record ServerHelloMessage(int protocolVersion, int siteID) implements Message {

	@Override
	public MessageType getType() {
		return MessageType.SERVER_HELLO;
	}

	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeInt(protocolVersion);
		out.writeInt(siteID);
	}

	public static ServerHelloMessage deserialize(DataInputStream in) throws IOException {
		return new ServerHelloMessage(in.readInt(), in.readInt());
	}

}
