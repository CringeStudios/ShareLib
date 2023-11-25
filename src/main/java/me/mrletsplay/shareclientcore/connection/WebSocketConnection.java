package me.mrletsplay.shareclientcore.connection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import me.mrletsplay.shareclientcore.document.Char;
import me.mrletsplay.shareclientcore.document.Identifier;

public class WebSocketConnection implements RemoteConnection {

	private WSClient client;
	private Set<RemoteListener> listeners;

	public WebSocketConnection(URI uri, Map<String, String> httpHeaders) {
		this.client = new WSClient(uri, httpHeaders);
		this.listeners = new HashSet<>();
	}

	public WebSocketConnection(URI uri) {
		this(uri, null);
	}

	@Override
	public void connect() throws IOException, InterruptedException {
		if(!client.connectBlocking()) throw new IOException("Failed to connect to WebSocket server");
	}

	@Override
	public int retrieveSiteID() {
		// TODO: implement
		return new Random().nextInt();
	}

	@Override
	public void send(Change... changes) {
		for(Change c : changes) {
			client.send(serialize(c));
		}
	}

	@Override
	public void addListener(RemoteListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(RemoteListener listener) {
		listeners.remove(listener);
	}

	private byte[] serialize(Change change) {
		try {
			ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			DataOutputStream dOut = new DataOutputStream(bOut);
			dOut.writeInt(change.document());
			dOut.writeUTF(change.type().name());

			Char ch = change.character();
			dOut.writeInt(ch.position().length);
			for(int i = 0; i < ch.position().length; i++) {
				Identifier id = ch.position()[i];
				dOut.writeInt(id.digit());
				dOut.writeInt(id.site());
			}

			dOut.writeInt(ch.lamport());
			dOut.writeChar(ch.value());
			return bOut.toByteArray();
		}catch(IOException e) {
			throw new RuntimeException("Something went very wrong", e);
		}
	}

	private Change deserialize(byte[] bytes) {
		try {
			DataInputStream dIn = new DataInputStream(new ByteArrayInputStream(bytes));
			int document = dIn.readInt();
			ChangeType type = ChangeType.valueOf(dIn.readUTF());

			Identifier[] pos = new Identifier[dIn.readInt()];
			for(int i = 0; i < pos.length; i++) {
				pos[i] = new Identifier(dIn.readInt(), dIn.readInt());
			}

			int lamport = dIn.readInt();
			char value = dIn.readChar();
			return new Change(document, type, new Char(pos, lamport, value));
		}catch(IllegalArgumentException e) {
			throw new IllegalArgumentException("Failed to deserialize change", e);
		}catch(IOException e) {
			throw new RuntimeException("Something went very wrong", e);
		}
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
			// TODO: request site id
		}

		@Override
		public void onMessage(String message) {
			System.out.println("Got text message: " + message);
		}

		@Override
		public void onMessage(ByteBuffer bytes) {
			byte[] bytesArray = new byte[bytes.remaining()];
			bytes.get(bytesArray);
			listeners.forEach(l -> l.onRemoteChange(deserialize(bytesArray)));
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
