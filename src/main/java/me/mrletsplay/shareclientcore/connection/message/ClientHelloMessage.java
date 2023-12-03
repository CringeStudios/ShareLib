package me.mrletsplay.shareclientcore.connection.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public record ClientHelloMessage(String username, String sessionID) implements Message {

	@Override
	public MessageType getType() {
		return MessageType.CLIENT_HELLO;
	}

	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeUTF(username);
		out.writeUTF(sessionID);
	}

	public static ClientHelloMessage deserialize(DataInputStream in) throws IOException {
		return new ClientHelloMessage(in.readUTF(), in.readUTF());
	}

}
