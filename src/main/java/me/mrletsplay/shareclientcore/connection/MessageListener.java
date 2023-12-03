package me.mrletsplay.shareclientcore.connection;

import me.mrletsplay.shareclientcore.connection.message.Message;

/**
 * A message listener that can receive remote messages
 */
public interface MessageListener {

	public void onMessage(Message message);

}
