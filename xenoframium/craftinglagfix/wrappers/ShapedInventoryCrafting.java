package xenoframium.craftinglagfix.wrappers;

import java.util.Arrays;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import xenoframium.craftinglagfix.datastructures.PrefixRepresentable;

public class ShapedInventoryCrafting implements PrefixRepresentable{
	private ItemStackWrapper[] itemStacks;
	private int hash;

	public ShapedInventoryCrafting(InventoryCrafting inventoryCrafting) {
		int top = 3, bottom = -1, left = 3, right = -1, size = -1;
		
		if (inventoryCrafting.getSizeInventory() == 9) {
			size = 3;
		} else {
			size = 2;
		}
		
		//Trim recipe for hashing
		for (int currentRow = 0; currentRow < size; currentRow++) {
			for (int currentColumn = 0; currentColumn < size; currentColumn++) {
				if (inventoryCrafting.getStackInSlot(currentRow * size + currentColumn) != null) {
					top = Math.min(top, currentRow);
					bottom = Math.max(bottom, currentRow);
					left = Math.min(left, currentColumn);
					right = Math.max(right, currentColumn);
				}
			}
		}

		bottom++;
		right++;
		
		if (top == 3) {
			itemStacks = new ItemStackWrapper[0];
			return;
		}
		
		int width = right - left;
		int height = bottom - top;
		
		itemStacks = new ItemStackWrapper[width * height];
		
		for (int currentRow = top; currentRow < bottom; currentRow++) {
			for (int currentColumn = left; currentColumn < right; currentColumn++) {
				ItemStack res = inventoryCrafting.getStackInSlot(currentRow * size + currentColumn);
				itemStacks[(currentRow - top) * width + (currentColumn - left)] = new ItemStackWrapper(res);
			}
		}
		hash = Arrays.hashCode(itemStacks);
	}
	
	@Override
	public Object[] getPrefixes() {
		return itemStacks;
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
		if (!(obj instanceof ShapedInventoryCrafting)) {
			return false;
		}
		ShapedInventoryCrafting other = (ShapedInventoryCrafting) obj;
		if (!java.util.Arrays.equals(itemStacks, other.itemStacks)) {
			return false;
		}
		return true;
	}

}
