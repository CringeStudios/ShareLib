package me.mrletsplay.shareclientcore.document;

public interface CharBag {

//	public Character find(PositionIdentifier position);
//	public Character findBefore(PositionIdentifier position);
//	public Character findAfter(PositionIdentifier position);

	/**
	 * Adds a character to the bag and returns the index it was inserted at
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

}
