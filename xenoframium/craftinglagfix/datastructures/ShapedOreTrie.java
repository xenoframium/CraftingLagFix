package xenoframium.craftinglagfix.datastructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.item.crafting.IRecipe;
import xenoframium.craftinglagfix.CraftingRegistry;
import xenoframium.craftinglagfix.wrappers.ItemStackWrapper;
import xenoframium.craftinglagfix.wrappers.ShapedInventoryCrafting;
import xenoframium.craftinglagfix.wrappers.ShapedOreRecipeWrapper;

//TODO: extend Trie instead
public class ShapedOreTrie {

	private HashSet<TrieNode> visited;

	private class TrieNode {
		private Map<Object, TrieNode> children = new HashMap<Object, TrieNode>();
		private Set<IRecipe> values = new HashSet<IRecipe>();

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

		void addValue(ShapedOreRecipeWrapper value) {
			values.add(value);
		}

		boolean removeValue(ShapedOreRecipeWrapper value) {
			return values.remove(value);
		}

		Set<IRecipe> getValues() {
			return values;
		}
	}

	private TrieNode root = new TrieNode();

	private IRecipe findValue(ItemStackWrapper[] prefixes, int depth, TrieNode currentNode, MatchVerifier verifier) {
		if (prefixes.length == 0) {
			return null;
		}

		if (currentNode == root) {
			visited = new HashSet<TrieNode>();
		}

		if (depth == prefixes.length) {
			for (IRecipe possibleMatch : currentNode.getValues()) {
				if (verifier.doesMatch(possibleMatch)) {
					return possibleMatch;
				}
			}

			return null;
		}

		if (!currentNode.hasChildren()) {
			return null;
		}

		ArrayList<Object> keys = new ArrayList<Object>();
		Set<String> oreDictEntries = CraftingRegistry.getOreDictEntries(prefixes[depth]);
		if (oreDictEntries != null) {
			Set<String> wildcardEntries = CraftingRegistry.getOreDictEntries(prefixes[depth].getWithWildcardDamage());
			if (wildcardEntries != null) {
				oreDictEntries.addAll(wildcardEntries);
			}
		} else {
			oreDictEntries = CraftingRegistry.getOreDictEntries(prefixes[depth].getWithWildcardDamage());
		}

		// Needed for bounded O(N)
		if (oreDictEntries != null) {
			if (oreDictEntries.size() + 2 > currentNode.numChildren()) {
				for (Object key : currentNode.children.keySet()) {
					if (oreDictEntries.contains(key)) {
						keys.add(key);
					}
				}
			} else {
				keys = new ArrayList<Object>(oreDictEntries);
			}
		}

		keys.add(prefixes[depth]);
		keys.add(prefixes[depth].getWithWildcardDamage());

		for (Object key : keys) {
			TrieNode childToSearch = currentNode.getChild(key);
			if (childToSearch != null && !visited.contains(childToSearch)) {
				IRecipe foundValue = findValue(prefixes, depth + 1, childToSearch, verifier);

				if (foundValue != null) {
					return foundValue;
				}
				visited.add(childToSearch);
			}
		}

		return null;
	}

	public IRecipe getValue(ShapedInventoryCrafting objectToFind, MatchVerifier verifier) {
		IRecipe matchingNode = findValue((ItemStackWrapper[]) objectToFind.getPrefixes(), 0, root, verifier);
		return matchingNode;
	}

	public void addValue(ShapedOreRecipeWrapper valueToAdd) {
		Object[] prefixes = valueToAdd.getPrefixes();
		TrieNode currentNode = root;
		for (Object prefix : prefixes) {
			currentNode = currentNode.createChild(prefix);
		}
		currentNode.addValue(valueToAdd);
	}

	private boolean removeValue(ShapedOreRecipeWrapper valueToRemove, Object[] prefixes, int depth,
			TrieNode currentNode) {
		if (currentNode == root) {
			visited = new HashSet<TrieNode>();
		}

		if (depth == prefixes.length) {
			currentNode.removeValue(valueToRemove);
			return !currentNode.hasChildren() && !currentNode.hasValues();
		}

		if (!currentNode.hasChildren()) {
			return !currentNode.hasValues();
		}

		Object key = prefixes[depth];
		TrieNode childToSearch = currentNode.getChild(key);
		if (childToSearch != null) {
			if (removeValue(valueToRemove, prefixes, depth + 1, childToSearch)) {
				currentNode.removeChild(key);
			}
		}

		return !currentNode.hasChildren() && !currentNode.hasValues();
	}

	public void removeValue(ShapedOreRecipeWrapper valueToRemove) {
		removeValue(valueToRemove, valueToRemove.getPrefixes(), 0, root);
	}
}
