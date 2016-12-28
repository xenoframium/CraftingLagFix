package xenoframium.craftinglagfix.wrappers;

import java.util.Arrays;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import xenoframium.craftinglagfix.datastructures.PrefixRepresentable;

public class ShapelessInventoryCrafting implements PrefixRepresentable{
	
	private ItemStackWrapper[] itemStacks;
	int hash;
	
	public ShapelessInventoryCrafting(InventoryCrafting inventoryCrafting) {
		int numberOfItemStacks = 0;
		int size = inventoryCrafting.getSizeInventory();
		for (int i = 0; i < size; i++) {
			if (inventoryCrafting.getStackInSlot(i) != null) {
				numberOfItemStacks++;
			}
		}
		
		int numberOfItemStacksProcessed = 0;
		itemStacks = new ItemStackWrapper[numberOfItemStacks];
		for (int i = 0; numberOfItemStacksProcessed < numberOfItemStacks; i++) {
			ItemStack stack = inventoryCrafting.getStackInSlot(i);
			if (stack != null) {
				itemStacks[numberOfItemStacksProcessed] = new ItemStackWrapper(stack);
				numberOfItemStacksProcessed++;
			}
		}
		Arrays.sort(itemStacks);
		hash = Arrays.hashCode(itemStacks);
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
		if (!(obj instanceof ShapelessInventoryCrafting)) {
			return false;
		}
		ShapelessInventoryCrafting other = (ShapelessInventoryCrafting) obj;
		if (!Arrays.equals(itemStacks, other.itemStacks)) {
			return false;
		}
		return true;
	}

	@Override
	public Object[] getPrefixes() {
		return itemStacks;
	}
	
}
