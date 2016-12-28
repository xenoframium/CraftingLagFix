package xenoframium.craftinglagfix.datastructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

//Amortized O(logN) insert
//TODO: maybe replace with heap backed by hashmap
public class PriorityCache<KeyType, ValueType> {
	
	class Wrapper implements Comparable<Wrapper>{
		int accessCount = 0;
		KeyType key;
		ValueType value;
		
		Wrapper(KeyType key, ValueType value) {
			this.value = value;
		}
		
		@Override
		public int compareTo(Wrapper o) {
			if (this.accessCount > o.accessCount) {
				return 1;
			} else if (this.accessCount < o.accessCount) {
				return -1;
			}
			
			return 0;
		}
	}
	
	private final int maxElements;
	private final int numToDelete;
	HashMap<KeyType, Wrapper> values = new HashMap<KeyType, Wrapper>();
	
	public PriorityCache(int maxElements) {
		this.maxElements = maxElements;
		this.numToDelete = bitShiftLog2(maxElements);
	}
	
	private int bitShiftLog2(int num) {
		int result = 0;
		while (num != 0) {
			num >>= 1;
		}
		return result;
	}
	
	private void shrink() {
		List<Wrapper> elements = new ArrayList<Wrapper>(values.values());
		Collections.sort(elements);
		values.clear();
		for (int i = numToDelete; i < elements.size(); i++) {
			Wrapper wrapper = elements.get(i);
			values.put(wrapper.key, wrapper);
		}
	}
	
	public void add(KeyType key, ValueType value) {
		if (values.size() == maxElements) {
			shrink();
		}
		values.put(key, new Wrapper(key, value));
	}
	
	public void increment(KeyType key) {
		if (values.containsKey(key)) {
			values.get(key).accessCount++;
		}
	}
	
	public ValueType get(KeyType key) {
		if (values.containsKey(key)) {
			return values.get(key).value;
		}
		return null;
	}
}
