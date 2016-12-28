package xenoframium.craftinglagfix.wrappers;

import java.util.Arrays;

import net.minecraft.inventory.InventoryCrafting;

public class InventoryCraftingWrapper {
	int hash;
	ItemStackWrapper[] wrapper;
	public InventoryCraftingWrapper(InventoryCrafting inventoryCrafting) {
		wrapper = new ItemStackWrapper[inventoryCrafting.getSizeInventory()];
		
		for (int i = 0; i < inventoryCrafting.getSizeInventory(); i++) {
			wrapper[i] = new ItemStackWrapper(inventoryCrafting.getStackInSlot(i));
		}
		
		hash = Arrays.hashCode(wrapper);
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
		if (!(obj instanceof InventoryCraftingWrapper)) {
			return false;
		}
		InventoryCraftingWrapper other = (InventoryCraftingWrapper) obj;
		if (!Arrays.equals(wrapper, other.wrapper)) {
			return false;
		}
		return true;
	}
	
	
}
