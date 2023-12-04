package me.mrletsplay.shareclientcore.connection;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ServerHandshake;

import me.mrletsplay.shareclientcore.connection.message.ClientHelloMessage;
import me.mrletsplay.shareclientcore.connection.message.Message;
import me.mrletsplay.shareclientcore.connection.message.ServerHelloMessage;

public class WebSocketConnection implements RemoteConnection {

	private WSClient client;
	private String username;
	private Set<MessageListener> listeners;
	private int siteID;

	private Object wait = new Object();
	private boolean helloReceived;
	private ConnectionException connectException;

	public WebSocketConnection(URI uri, String username, Map<String, String> httpHeaders) {
		this.client = new WSClient(uri, httpHeaders);
		this.username = username;
		this.listeners = new HashSet<>();
	}

	public WebSocketConnection(URI uri, String username) {
		this(uri, username, null);
	}

	@Override
	public void connect(String sessionID) throws ConnectionException {
		try {
			if(!client.connectBlocking(30, TimeUnit.SECONDS)) throw new IOException("Failed to connect to WebSocket server");
			send(new ClientHelloMessage(username, sessionID));
			synchronized(wait) { wait.wait(30_000L); }
			if(!helloReceived) {
				client.close();
				throw new ConnectionException("Server did not send hello");
			}
			if(connectException != null) throw connectException;
		} catch (InterruptedException | IOException e) {
			throw new ConnectionException("Failed to establish connection", e);
		}
	}

	@Override
	public void disconnect() {
		client.close(CloseFrame.NORMAL);
	}

	@Override
	public int getSiteID() {
		return siteID;
	}

	@Override
	public void send(Message message) throws ConnectionException {
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		DataOutputStream dOut = new DataOutputStream(bOut);

		try {
			dOut.writeUTF(message.getType().name());
			message.serialize(dOut);
		} catch (IOException e) {
			throw new ConnectionException("Failed to serialize message", e);
		}

		client.send(bOut.toByteArray());
	}

	@Override
	public void addListener(MessageListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(MessageListener listener) {
		listeners.remove(listener);
	}

	private class WSClient extends WebSocketClient {

		public WSClient(URI serverUri) {
			super(serverUri);
		}

		public WSClient(URI serverUri, Map<String, String> httpHeaders) {
			super(serverUri, httpHeaders);
		}

		@Override
		public void onOpen(ServerHandshake handshakedata) {

		}

		@Override
		public void onMessage(String message) {
			System.out.println("Got text message: " + message);
		}

		@Override
		public void onMessage(ByteBuffer bytes) {
			Message m;
			try {
				m = Message.deserialize(bytes);
			}catch(IOException e) {
				e.printStackTrace(); // TODO: custom logging (e.g. via error callback)
				return;
			}

			if(m instanceof ServerHelloMessage hello) {
				helloReceived = true;
				siteID = hello.siteID();

				if(hello.protocolVersion() != PROTOCOL_VERSION) {
					connectException = new ConnectionException(String.format("Protocol version mismatch: (Server has %s, Client has %s)", hello.protocolVersion(), PROTOCOL_VERSION));
					close();
				}

				synchronized(wait) { wait.notifyAll(); }
				return;
			}

			listeners.forEach(l -> l.onMessage(m));
		}

		@Override
		public void onClose(int code, String reason, boolean remote) {
			// TODO: handle
		}

		@Override
		public void onError(Exception ex) {
			// TODO: handle
		}

	}

}
