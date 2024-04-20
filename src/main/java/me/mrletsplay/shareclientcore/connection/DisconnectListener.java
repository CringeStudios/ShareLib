package me.mrletsplay.shareclientcore.connection;

public interface DisconnectListener {

	public void onDisconnect(String reason, boolean remote);

}
