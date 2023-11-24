package me.mrletsplay.shareclientcore.document;

import java.util.ArrayList;
import java.util.List;

public class ArrayCharBag implements CharBag {

	private List<Char> chars = new ArrayList<>();

	@Override
	public int add(Char character) {
		int i = 0;
		// TODO: use binary search
		while(i < chars.size() && Util.comparePositions(chars.get(i).position(), character.position()) < 0) i++;
		if(i < chars.size() && Util.comparePositions(chars.get(i).position(), character.position()) == 0) return -1;
		chars.add(i, character);
		return i;
	}

	@Override
	public int remove(Char character) {
		int i = 0;
		// TODO: use binary search
		while(i < chars.size() && Util.comparePositions(chars.get(i).position(), character.position()) < 0) i++;
		if(i == chars.size() || Util.comparePositions(chars.get(i).position(), character.position()) != 0) return -1;
		chars.remove(character);
		return i;
	}

	@Override
	public Char get(int index) {
		return chars.get(index);
	}

	@Override
	public int size() {
		return chars.size();
	}

	@Override
	public String toString() {
		return chars.stream()
				.map(c -> c.value())
				.reduce(new StringBuilder(), (b, s) -> b.append(s), (a, b) -> a.append(b)).toString();
	}

}
