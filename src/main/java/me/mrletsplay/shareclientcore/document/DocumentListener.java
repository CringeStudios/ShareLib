package me.mrletsplay.shareclientcore.document;

/**
 * Represents something that can receive local changes
 */
public interface DocumentListener {

	public void onInsert(int index, char character);

	public void onDelete(int index);

}
