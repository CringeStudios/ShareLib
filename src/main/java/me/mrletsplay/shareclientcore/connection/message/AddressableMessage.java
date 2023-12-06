package me.mrletsplay.shareclientcore.connection.message;

public interface AddressableMessage extends Message {

	public static final int BROADCAST_SITE_ID = -1;

	public int siteID();

}
