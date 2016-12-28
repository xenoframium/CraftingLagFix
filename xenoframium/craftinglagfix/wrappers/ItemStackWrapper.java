package xenoframium.craftinglagfix.wrappers;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemStackWrapper implements Comparable<ItemStackWrapper> {
	private ItemStack itemStack;
	private int hash = 1;

	public ItemStackWrapper(ItemStack itemStack) {
		this.itemStack = itemStack;

		if (itemStack == null) {
			hash = 0;
			return;
		}

		hash = 31 * hash + itemStack.getItem().delegate.name().hashCode();
		hash = 31 * hash + itemStack.getItemDamage();
	}

	public int getDamage() {
		if (itemStack == null) {
			return OreDictionary.WILDCARD_VALUE;
		}
		return itemStack.getItemDamage();
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public ItemStackWrapper getWithWildcardDamage() {
		if (itemStack == null) {
			return this;
		}

		return new ItemStackWrapper(new ItemStack(itemStack.getItem(), 1, OreDictionary.WILDCARD_VALUE));
	}

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (!(obj instanceof ItemStackWrapper)) {
			return false;
		}

		ItemStackWrapper other = (ItemStackWrapper) obj;
		if (itemStack == null || other.itemStack == null) {
			if (itemStack == other.itemStack) {
				return true;
			} else {
				return false;
			}
		} else if (!itemStack.getItem().delegate.name().equals(other.itemStack.getItem().delegate.name())) {
			return false;
		} else if (itemStack.getItemDamage() != other.itemStack.getItemDamage()) {
			if (itemStack.getItemDamage() != OreDictionary.WILDCARD_VALUE) {
				if (other.itemStack.getItemDamage() != OreDictionary.WILDCARD_VALUE) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public int compareTo(ItemStackWrapper other) {
		if (other == null) {
			return 1;
		}
		
		if (this.itemStack == null) {
			if (other.itemStack == null) {
				return 0;
			} else {
				return -1;
			}
		} else if (other.itemStack == null) {
			return 1;
		}

		int itemCompareResult = itemStack.getItem().delegate.name()
				.compareTo(other.itemStack.getItem().delegate.name());
		if (itemCompareResult == 0) {
			return this.itemStack.getItemDamage() - other.itemStack.getItemDamage();
		} else {
			return itemCompareResult;
		}
	}
	
	@Override
	public String toString() {
		if (itemStack == null) {
			return "Item: null";
		}
		return "Item: " + itemStack.getItem().delegate.name() + " Damage: " + itemStack.getItemDamage();
	}
}
