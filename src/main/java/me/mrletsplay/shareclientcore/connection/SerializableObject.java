package me.mrletsplay.shareclientcore.connection;

import java.io.DataOutputStream;
import java.io.IOException;

public interface SerializableObject {

	public void serialize(DataOutputStream out) throws IOException;

}
