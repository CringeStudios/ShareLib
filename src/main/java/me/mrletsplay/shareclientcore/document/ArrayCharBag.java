package me.mrletsplay.shareclientcore.document;

import java.util.ArrayList;
import java.util.List;

public class ArrayCharBag implements CharBag {

	private List<Char> chars = new ArrayList<>();

	@Override
	public void add(Char character) {
		int i = 0;
		while(Util.comparePositions(chars.get(i).position(), character.position()) < 0) i++;
		chars.add(i, character);
	}

	@Override
	public void remove(Char character) {
		chars.remove(character);
	}

}
