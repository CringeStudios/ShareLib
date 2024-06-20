package me.mrletsplay.shareclientcore.document;

import java.util.Iterator;
import java.util.List;

import me.mrletsplay.shareclientcore.debug.DebugValues;
import me.mrletsplay.shareclientcore.util.AVLOrderTree;

public class BinaryTreeCharBag implements CharBag {

	private AVLOrderTree<Char> chars;

	public BinaryTreeCharBag() {
		this.chars = new AVLOrderTree<>(Util::compareChars);
	}

	@Override
	public int add(Char character) {
		return chars.addIndex(character);
	}

	@Override
	public int remove(Char character) {
		return chars.removeIndex(character);
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
		return chars.stream().toList();
	}

	@Override
	public byte[] getContents() {
		byte[] bytes = new byte[chars.size()];
		int i = 0;
		Iterator<Char> it = chars.iterator();
		while(it.hasNext()) {
			Char c = it.next();
			bytes[i++] = c.value();
		}
		return bytes;
	}

	@Override
	public String getContentsAsString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DebugValues getDebugValues() {
		// TODO Auto-generated method stub
		return null;
	}

}
