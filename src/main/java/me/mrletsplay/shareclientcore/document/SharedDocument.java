package me.mrletsplay.shareclientcore.document;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.mrletsplay.shareclientcore.connection.Change;
import me.mrletsplay.shareclientcore.connection.ChangeType;
import me.mrletsplay.shareclientcore.connection.ConnectionException;
import me.mrletsplay.shareclientcore.connection.MessageListener;
import me.mrletsplay.shareclientcore.connection.RemoteConnection;
import me.mrletsplay.shareclientcore.connection.message.ChangeMessage;
import me.mrletsplay.shareclientcore.connection.message.Message;

public class SharedDocument implements MessageListener {

	private RemoteConnection connection;
	private CharBag charBag;
	private String path;
	private int site;
	private int lamport;
	private Set<DocumentListener> listeners;

	public SharedDocument(RemoteConnection connection, String path, String initialContents) {
		this.connection = connection;
		this.charBag = new ArrayCharBag();
		this.path = path;
		this.site = connection.getSiteID();
		this.listeners = new HashSet<>();

		charBag.add(Char.START_OF_DOCUMENT);
		charBag.add(Char.END_OF_DOCUMENT);
		if(initialContents != null && !initialContents.isEmpty()) {
			insert(0, initialContents, Identifier.DEFAULT_SITE);
		}
	}

	public SharedDocument(RemoteConnection connection, String path) {
		this(connection, path, null);
	}

	private Change[] insert(int index, String str, int site) {
		if(index < 0 || index >= charBag.size() - 1) throw new IllegalArgumentException("Index out of bounds");

		Char charBefore = charBag.get(index);
		Char charAfter = charBag.get(index +1);

		char[] chars = str.toCharArray();
		Change[] changes = new Change[chars.length];
		for(int i = 0; i < chars.length; i++) {
			Identifier[] newPos = Util.generatePositionBetween(charBefore.position(), charAfter.position(), site);
			lamport++;
			Char ch = new Char(newPos, lamport, chars[i]);
			charBag.add(ch);
			changes[i] = new Change(path, ChangeType.ADD, ch);
			charBefore = ch;
		}

		return changes;
	}

	/**
	 * Inserts characters into the document at the specified index
	 * @param index The index to insert at
	 * @param str The string to insert
	 */
	public void localInsert(int index, String str) {
		Change[] changes = insert(index, str, site);

		for(Change c : changes) {
			try {
				connection.send(new ChangeMessage(c));
			} catch (ConnectionException e) {
				e.printStackTrace(); // TODO: throw error
			}
		}
	}

	/**
	 * Deletes n characters from a document, starting at the specified index
	 * @param index The index to start deleting at
	 * @param n The number of characters to delete
	 */
	public void localDelete(int index, int n) {
		if(index < 0 || index + n >= charBag.size() - 1) throw new IllegalArgumentException("Index out of bounds");
		if(n == 0) return;

		Change[] changes = new Change[n];
		while(n-- > 0) {
			// TODO: more efficient implementation (e.g. range delete in CharBag)
			Char toRemove = charBag.get(index + 1);
			changes[n] = new Change(path, ChangeType.REMOVE, toRemove);
			charBag.remove(toRemove);
		}


		for(Change c : changes) {
			try {
				connection.send(new ChangeMessage(c));
			} catch (ConnectionException e) {
				e.printStackTrace(); // TODO: throw error
			}
		}
	}

	/**
	 * Inserts a remote change into the document and updates internal parameters accordingly
	 * @param c The character to insert
	 * @return The index of the inserted character, or -1 if it was not inserted because it already exists
	 */
	public int remoteInsert(Char c) {
		lamport = Math.max(c.lamport(), lamport) + 1;
		int index = charBag.add(c);
		if(index == -1) return -1;
		listeners.forEach(l -> l.onInsert(index - 1, c.value()));
		return index;
	}

	/**
	 * Removes a character from the document and updates internal parameters accordingly
	 * @param c The character to delete
	 * @return The index the character was located at, or -1 if it was not contained in the document
	 */
	public int remoteDelete(Char c) {
		lamport = Math.max(c.lamport(), lamport) + 1;
		int index = charBag.remove(c);
		if(index == -1) return -1;
		listeners.forEach(l -> l.onDelete(index - 1));
		return index;
	}

	public CharBag getCharBag() {
		return charBag;
	}

	/**
	 * Collects the chars in this document ordered by their position into a list.<br>
	 * This differs from {@link CharBag#toList()} in that this method does not include the {@link Char#START_OF_DOCUMENT} and {@link Char#END_OF_DOCUMENT} chars.
	 * @return
	 */
	public List<Char> toList() {
		List<Char> chars = charBag.toList();
		return chars.subList(1, chars.size() - 1);
	}

	public String getContents() {
		String contents = charBag.toString();
		return contents.substring(1, contents.length() - 1);
	}

	public void addListener(DocumentListener listener) {
		listeners.add(listener);
	}

	public void removeListener(DocumentListener listener) {
		listeners.add(listener);
	}

	@Override
	public void onMessage(Message message) {
		if(message instanceof ChangeMessage change) {
			Change c = change.change();
			if(!c.documentPath().equals(path)) return;

			System.out.println("Change: " + c + " | " + Arrays.toString(c.character().position()));
			switch(c.type()) {
				case ADD -> remoteInsert(c.character());
				case REMOVE -> remoteDelete(c.character());
			}
		}
	}

}
