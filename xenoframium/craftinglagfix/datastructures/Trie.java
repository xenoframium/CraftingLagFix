package xenoframium.craftinglagfix.datastructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;
import xenoframium.craftinglagfix.wrappers.ItemStackWrapper;
import xenoframium.craftinglagfix.wrappers.OreRecipeKeyGenerator;
import xenoframium.craftinglagfix.wrappers.ShapedOreRecipeWrapper;
import xenoframium.craftinglagfix.wrappers.ShapelessOreRecipeWrapper;

public class Trie<ValueType> {

	Object t = null;
	ItemStackWrapper z = new ItemStackWrapper(new ItemStack(Blocks.planks, 1, 32767));
	ItemStack zz = new ItemStack(Blocks.planks);

	private HashSet<TrieNode> visited;

	private class TrieNode {
		private Map<Object, TrieNode> children = new HashMap<Object, TrieNode>();
		private Set<ValueType> values = new HashSet<ValueType>();

		boolean isEmpty() {
			return !hasChildren() && !hasValues();
		}

		boolean hasChildren() {
			return !children.isEmpty();
		}

		int numChildren() {
			return children.size();
		}

		TrieNode getChild(Object key) {
			return children.get(key);
		}

		TrieNode createChild(Object key) {
			TrieNode childNode = children.get(key);
			if (childNode == null) {
				childNode = new TrieNode();
				children.put(key, childNode);
			}

			return childNode;
		}

		void removeChild(Object key) {
			children.remove(key);
		}

		boolean hasValues() {
			return !values.isEmpty();
		}

		void addValue(ValueType value) {
			values.add(value);
		}

		boolean removeValue(ValueType value) {
			return values.remove(value);
		}

		Set<ValueType> getValues() {
			return values;
		}
	}

	private TrieNode root = new TrieNode();
	
	private static PrefixKeyGenerator defaultKeyGenerator = new PrefixKeyGenerator() {
		@Override
		public Object[][] generateKeys(Object[] prefixArray) {
			Object[][] keys = new Object[prefixArray.length][];

			for (int i = 0; i < prefixArray.length; i++) {
				keys[i] = new Object[] { prefixArray[i] };
			}

			return keys;
		}
	};

	private ValueType findNode(Object[][] keys, int depth, TrieNode currentNode, MatchVerifier matchVerifier) {
		if (currentNode == root) {
			visited = new HashSet<TrieNode>();
		}

		if (depth == keys.length) {
			for (ValueType possibleMatch : currentNode.getValues()) {
				if (matchVerifier.doesMatch(possibleMatch)) {
					return possibleMatch;
				}
			}
			
			return null;
		}

		if (!currentNode.hasChildren()) {
			return null;
		}

		for (int j = 0; j < keys[depth].length; j++) {
			TrieNode childToSearch = currentNode.getChild(keys[depth][j]);
			if (childToSearch != null && !visited.contains(childToSearch)) {
				ValueType foundValue = findNode(keys, depth + 1, childToSearch, matchVerifier);

				if (foundValue != null) {
					return foundValue;
				}
				visited.add(childToSearch);
			}
		}

		return null;
	}

	private Object[][] generateKeys(PrefixRepresentable objectToGetKeyFrom, PrefixKeyGenerator keyGenerator) {
		Object[] prefixRepresentation = objectToGetKeyFrom.getPrefixes();
		Object[][] keys = keyGenerator.generateKeys(prefixRepresentation);

		return keys;
	}

	public ValueType getValue(PrefixRepresentable objectToFind, MatchVerifier matchVerifier) {
		return getValue(objectToFind, defaultKeyGenerator, matchVerifier);
	}

	public ValueType getValue(PrefixRepresentable objectToFind, PrefixKeyGenerator keyGenerator, MatchVerifier matchVerifier) {
		ValueType match = findNode(generateKeys(objectToFind, keyGenerator), 0, root, matchVerifier);
		return match;
	}

	public void addValue(PrefixRepresentable objectToAddTo, ValueType valueToAdd) {
		Object[] prefixes = objectToAddTo.getPrefixes();
		TrieNode currentNode = root;
		for (Object prefix : prefixes) {
			currentNode = currentNode.createChild(prefix);
		}
		currentNode.addValue(valueToAdd);
	}

	private boolean removeValues(ValueType valueToRemove, Object[][] keys, int depth, TrieNode currentNode) {
		if (currentNode == root) {
			visited = new HashSet<TrieNode>();
		}

		if (depth == keys.length) {
			return !currentNode.hasChildren() && !currentNode.hasValues();
		}

		if (!currentNode.hasChildren()) {
			return !currentNode.hasValues();
		}

		for (Object key : keys[depth]) {
			TrieNode childToSearch = currentNode.getChild(key);
			if (childToSearch != null) {
				if (!visited.contains(childToSearch)) {
					visited.add(childToSearch);
					if (removeValues(valueToRemove, keys, depth + 1, childToSearch)) {
						currentNode.removeChild(key);
					}
				} else if (childToSearch.isEmpty()) {
					currentNode.removeChild(key);
				}
			}
		}
		return !currentNode.hasChildren() && !currentNode.hasValues();
	}

	public void removeValues(PrefixRepresentable objectToRemoveFrom, ValueType valueToRemove,
			PrefixKeyGenerator keyGenerator) {
		removeValues(valueToRemove, keyGenerator.generateKeys(objectToRemoveFrom.getPrefixes()), 0, root);
	}

	public void removeValue(PrefixRepresentable objectToRemoveFrom, ValueType valueToRemove) {
		removeValues(objectToRemoveFrom, valueToRemove, defaultKeyGenerator);
	}
}
