package me.mrletsplay.shareclientcore.document;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ArrayCharBag implements CharBag {

	private List<Char> chars;

	public ArrayCharBag() {
		this.chars = new ArrayList<>();
	}

	public ArrayCharBag(List<Char> chars) {
		this.chars = new ArrayList<>(chars);
	}

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
		chars.remove(i);
		return i;
	}

	@Override
	public Char get(int index) {
		return chars.get(index);
	}

	@Override
	public void clear() {
		chars.clear();
	}

	@Override
	public int size() {
		return chars.size();
	}

	@Override
	public List<Char> toList() {
		return new ArrayList<>(chars);
	}

	@Override
	public byte[] getContents() {
		byte[] bytes = new byte[chars.size()];
		for(int i = 0; i < bytes.length; i++) {
			bytes[i] = chars.get(i).value();
		}
		return bytes;
	}

	@Override
	public String getContentsAsString() {
		return new String(getContents(), StandardCharsets.UTF_8);
	}

}
