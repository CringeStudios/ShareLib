package me.mrletsplay.shareclientcore.connection;

import java.io.IOException;

/**
 * Represents a connection to a remote user or server
 */
public interface RemoteConnection {

	public void connect() throws IOException, InterruptedException;

	public int retrieveSiteID();

	public void send(Change... changes);

	public void addListener(RemoteListener listener);

	public void removeListener(RemoteListener listener);

}
