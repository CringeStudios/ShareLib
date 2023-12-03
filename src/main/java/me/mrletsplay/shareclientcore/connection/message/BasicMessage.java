package me.mrletsplay.shareclientcore.connection.message;

import java.io.DataOutputStream;
import java.io.IOException;

public record BasicMessage(MessageType type) implements Message {

	@Override
	public MessageType getType() {
		return type;
	}

	@Override
	public void serialize(DataOutputStream out) throws IOException {}

}
