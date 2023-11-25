package me.mrletsplay.shareclientcore.connection;

import java.io.IOException;

public class DummyConnection implements RemoteConnection {

	@Override
	public void connect() throws IOException, InterruptedException {

	}

	@Override
	public int retrieveSiteID() {
		return 0;
	}

	@Override
	public void send(Change... changes) {

	}

	@Override
	public void addListener(RemoteListener listener) {

	}

	@Override
	public void removeListener(RemoteListener listener) {

	}

}
