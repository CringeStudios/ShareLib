package me.mrletsplay.shareclientcore.document;

import java.nio.charset.StandardCharsets;
import java.util.List;

public interface CharBag {

	/**
	 * Adds a character to the bag and returns the index it was inserted at, or -1 if it was not inserted because it already exists
	 * @param character The character to add
	 * @return The index it was inserted at
	 */
	public int add(Char character);

	/**
	 * Removes a character from the bag and returns the index it was located at, or -1 if it was not contained in the bag
	 * @param character The character to remove
	 * @return The index it was located at
	 */
	public int remove(Char character);

	/**
	 * Finds the character at the given index and returns it
	 * @param index The index of the character
	 * @return The character
	 */
	public Char get(int index);

	/**
	 * Removes all chars from this bag
	 */
	public void clear();

	/**
	 * Returns the number of chars in this bag
	 * @return The number of chars
	 */
	public int size();

	/**
	 * Collects the chars in this bag ordered by their position into a list
	 * @return
	 */
	public List<Char> toList();

	/**
	 * Collects the chars in this bag ordered by their position into a byte array
	 * @return A byte array containing the contents of this bag
	 */
	public byte[] getContents();

	/**
	 * Collects the chars in this bag ordered by their position into a string using the {@link StandardCharsets#UTF_8 UTF-8} charset.<br>
	 * Note: This will not work properly for binary contents, make sure to only call this on bags containing text!
	 * @return A string containing the contents of this bag
	 */
	public String getContentsAsString();

}
