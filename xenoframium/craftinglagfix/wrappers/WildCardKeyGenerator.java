package xenoframium.craftinglagfix.wrappers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import xenoframium.craftinglagfix.datastructures.PrefixKeyGenerator;

public class WildCardKeyGenerator implements PrefixKeyGenerator {

	@Override
	public Object[][] generateKeys(Object[] prefixArray) {
		ItemStackWrapper[] itemStackArray = (ItemStackWrapper[]) prefixArray;
		Object[][] keys = new Object[itemStackArray.length][];

		for (int i = 0; i < itemStackArray.length; i++) {
			ItemStackWrapper wrapper = itemStackArray[i];
			if (wrapper.getDamage() != OreDictionary.WILDCARD_VALUE) {
				keys[i] = new Object[] { wrapper, wrapper.getWithWildcardDamage() };
			} else {
				keys[i] = new Object[] { wrapper };
			}
		}

		return keys;
	}

}
