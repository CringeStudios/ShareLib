package me.mrletsplay.shareclientcore.debug;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DebugValues {

	private Set<String> keys;
	private Map<String, Integer> values;

	public DebugValues(Collection<String> keys) {
		this.keys = Collections.unmodifiableSet(new LinkedHashSet<>(keys));
		this.values = new HashMap<>(this.keys.size());
	}

	public DebugValues(String... keys) {
		this(Arrays.asList(keys));
	}

	public Set<String> getKeys() {
		return keys;
	}

	private void checkKey(String key) throws IllegalArgumentException {
		if(!keys.contains(key)) throw new IllegalArgumentException("Not a valid key");
	}

	public void set(String key, int value) throws IllegalArgumentException {
		checkKey(key);
		values.put(key, value);
	}

	public int get(String key) throws IllegalArgumentException {
		checkKey(key);
		return values.getOrDefault(key, 0);
	}

	public void reset(String key) throws IllegalArgumentException {
		checkKey(key);
		values.remove(key);
	}

	public void increment(String key) throws IllegalArgumentException {
		checkKey(key);
		values.put(key, values.getOrDefault(key, 0) + 1);
	}

	public void decrement(String key) throws IllegalArgumentException {
		checkKey(key);
		values.put(key, values.getOrDefault(key, 0) + 1);
	}

	@Override
	public String toString() {
		return keys.stream()
			.map(key -> key + ": " + get(key))
			.collect(Collectors.joining("\n"));
	}

}
