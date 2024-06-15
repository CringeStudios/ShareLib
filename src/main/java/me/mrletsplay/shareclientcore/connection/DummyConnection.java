package me.mrletsplay.shareclientcore.connection;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import me.mrletsplay.shareclientcore.connection.message.Message;
import me.mrletsplay.shareclientcore.debug.DebugValues;

public class DummyConnection implements RemoteConnection {

	private int siteID;
	private Consumer<Message> sendMessageHandler;
	private List<MessageListener> listeners;

	public DummyConnection(int siteID, Consumer<Message> sendMessageHandler) {
		this.siteID = siteID;
		this.sendMessageHandler = sendMessageHandler;
		this.listeners = new ArrayList<>();
	}

	public DummyConnection(int siteID) {
		this(siteID, null);
	}

	public void setSendMessageHandler(Consumer<Message> sendMessageHandler) {
		this.sendMessageHandler = sendMessageHandler;
	}

	@Override
	public void connect(String sessionID) throws ConnectionException {

	}

	@Override
	public void disconnect() {

	}

	@Override
	public int getSiteID() {
		return siteID;
	}

	@Override
	public void send(Message message) {
		if(sendMessageHandler != null) {
			sendMessageHandler.accept(message);
		}
	}

	public void receive(Message message) {
		listeners.forEach(l -> l.onMessage(message));
	}

	@Override
	public void addListener(MessageListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(MessageListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void setDisconnectListener(DisconnectListener listener) {

	}

	@Override
	public DebugValues getDebugValues() {
		return null;
	}

}
