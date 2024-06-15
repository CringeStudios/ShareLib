package me.mrletsplay.shareclientcore.document;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.mrletsplay.shareclientcore.debug.DebugValues;

public class ArrayCharBag implements CharBag {

	private List<Char> chars;

	private DebugValues debugValues;

	public ArrayCharBag(List<Char> chars) {
		this.chars = new ArrayList<>(chars);
		this.debugValues = new DebugValues(
			DEBUG_INSERTIONS,
			DEBUG_INSERTIONS_DROPPED,
			DEBUG_DELETIONS,
			DEBUG_DELETIONS_DROPPED
		);
	}

	public ArrayCharBag() {
		this(Collections.emptyList());
	}

	@Override
	public int add(Char character) {
		int i = 0;
		// TODO: use binary search
		while(i < chars.size() && Util.compareChars(chars.get(i), character) < 0) i++;
		if(i < chars.size() && Util.compareChars(chars.get(i), character) == 0) {
			debugValues.increment(DEBUG_INSERTIONS_DROPPED);
			return -1;
		}
		chars.add(i, character);
		debugValues.increment(DEBUG_INSERTIONS);
		return i;
	}

	@Override
	public int remove(Char character) {
		int i = 0;
		// TODO: use binary search
		while(i < chars.size() && Util.compareChars(chars.get(i), character) < 0) i++;
		if(i == chars.size() || Util.compareChars(chars.get(i), character) != 0) {
			debugValues.increment(DEBUG_DELETIONS_DROPPED);
			return -1;
		}
		chars.remove(i);
		debugValues.increment(DEBUG_DELETIONS);
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

	@Override
	public DebugValues getDebugValues() {
		return debugValues;
	}

}
