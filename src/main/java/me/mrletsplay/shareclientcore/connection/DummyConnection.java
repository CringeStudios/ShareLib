package me.mrletsplay.shareclientcore.connection;

import me.mrletsplay.shareclientcore.connection.message.Message;

public class DummyConnection implements RemoteConnection {

	@Override
	public void connect(String sessionID) throws ConnectionException {

	}

	@Override
	public int getSiteID() {
		return 0;
	}

	@Override
	public void send(Message message) {

	}

	@Override
	public void addListener(MessageListener listener) {

	}

	@Override
	public void removeListener(MessageListener listener) {

	}

}
