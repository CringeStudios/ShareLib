package me.mrletsplay.shareclientcore.connection;

import me.mrletsplay.shareclientcore.connection.message.Message;
import me.mrletsplay.shareclientcore.debug.DebugValues;

/**
 * Represents a connection to a remote user or server
 */
public interface RemoteConnection {

	public static final String
		DEBUG_MESSAGES_SENT = "messagesSent",
		DEBUG_MESSAGES_RECEIVED = "messagesReceived",
		DEBUG_CHANGES_SENT = "changesSent",
		DEBUG_CHANGES_RECEIVED = "changesReceived";

	public static final int PROTOCOL_VERSION = 1;

	public void connect(String sessionID) throws ConnectionException;

	public void disconnect();

	public int getSiteID();

	public void send(Message message) throws ConnectionException;

	public void addListener(MessageListener listener);

	public void removeListener(MessageListener listener);

	public void setDisconnectListener(DisconnectListener listener);

	public DebugValues getDebugValues();

}
