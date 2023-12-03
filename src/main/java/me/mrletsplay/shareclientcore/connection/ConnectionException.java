package me.mrletsplay.shareclientcore.connection;

public class ConnectionException extends Exception {

	private static final long serialVersionUID = -6133726852202889620L;

	public ConnectionException() {
		super();
	}

	public ConnectionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConnectionException(String message) {
		super(message);
	}

	public ConnectionException(Throwable cause) {
		super(cause);
	}



}
