package me.mrletsplay.shareclientcore.connection;

/**
 * Represents something that can receive remote changes
 */
public interface RemoteListener {

	public void onRemoteChange(Change... changes);

}
