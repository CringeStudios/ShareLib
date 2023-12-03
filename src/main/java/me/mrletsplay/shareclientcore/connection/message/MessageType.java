package me.mrletsplay.shareclientcore.connection.message;

public enum MessageType {

	/**
	 * Client hello containing user information
	 */
	CLIENT_HELLO,

	/**
	 * Server hello containing protocol information
	 */
	SERVER_HELLO,

	/**
	 * Peer has joined
	 */
	PEER_JOIN,

	/**
	 * Peer has left
	 */
	PEER_LEAVE,

	/**
	 * Full synchronization message, containing the full contents of a file
	 */
	FULL_SYNC,

	/**
	 * Request for the synchronization of a particular file or all shared files
	 */
	REQUEST_FULL_SYNC,

	/**
	 * A single change made by one peer
	 */
	CHANGE,

	/**
	 * Request for the checksum of a file
	 */
	REQUEST_CHECKSUM,

	/**
	 * Checksum of a file
	 */
	CHECKSUM,

	/**
	 * Creation of a file
	 */
	CREATE_FILE,

	/**
	 * Deletion of a file
	 */
	DELETE_FILE,
	;

}
