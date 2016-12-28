package xenoframium.craftinglagfix.wrappers;

import java.util.ArrayList;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import xenoframium.craftinglagfix.datastructures.PrefixKeyGenerator;

public class OreRecipeKeyGenerator implements PrefixKeyGenerator {
	@Override
	public Object[][] generateKeys(Object[] prefixArray) {
		Object[][] result = new Object[prefixArray.length][];
		int i = 0;
		for (Object entry : prefixArray) {
			ArrayList<ItemStackWrapper> oreItems = (ArrayList<ItemStackWrapper>) entry;
			StringBuilder op = new StringBuilder();
			if (oreItems.size() != 0) {
				result[i] = new Object[oreItems.size()];
				int j = 0;
				for (ItemStackWrapper itemStack : oreItems) {
					result[i][j] = itemStack;
					j++;
				}
			} else {
				result[i] = new Object[]{new ItemStackWrapper(null)};
			}
			i++;
		}
		return result;
	}

}
