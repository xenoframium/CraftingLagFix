package xenoframium.craftinglagfix.wrappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import xenoframium.craftinglagfix.datastructures.PrefixRepresentable;

public class ShapelessOreRecipeWrapper implements IRecipe, PrefixRepresentable {
	public ShapelessOreRecipe recipe;
	private Object[] prefixes;
	private int hash;

	public ShapelessOreRecipeWrapper(ShapelessOreRecipe recipe) {
		this.recipe = recipe;

		ArrayList<Object> recipeItems = recipe.getInput();
		ArrayList<String> oreNames = new ArrayList<String>();
		ArrayList<ItemStackWrapper> itemStacks = new ArrayList<ItemStackWrapper>();
		
		for (int i = 0; i < recipeItems.size(); i++) {
			Object entry = recipeItems.get(i);

			if (entry instanceof ItemStack) {
				itemStacks.add(new ItemStackWrapper((ItemStack) entry));
			} else {
				ArrayList<ItemStack> items = (ArrayList<ItemStack>) entry;
				
				if (items.size() != 0) {
					OreDictionary.getOreNames();
					int[] oreDictionaryIDs = OreDictionary.getOreIDs(items.get(0));
					for (int id : oreDictionaryIDs) {
						if (OreDictionary.getOres(id) == entry) {
							oreNames.add(OreDictionary.getOreName(id));
							break;
						}
					}
				}
			}
		}
		prefixes = new Object[oreNames.size() + itemStacks.size()];
		Collections.sort(oreNames);
		Collections.sort(itemStacks);
		int i = 0;
		for (Object prefix : oreNames) {
			prefixes[i] = prefix;
			i++;
		}
		
		for (Object prefix : itemStacks) {
			prefixes[i] = prefix;
			i++;
		}

		for (Object object : prefixes) {
			hash = 31 * hash + object.hashCode();
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ShapelessOreRecipeWrapper)) {
			return false;
		}
		ShapelessOreRecipeWrapper other = (ShapelessOreRecipeWrapper) obj;
		if (recipe == null) {
			if (other.recipe != null) {
				return false;
			} else {
				return true;
			}
		} else if (!Arrays.equals(prefixes, other.prefixes)) {
			return false;
		}
		return true;
	}
	
	@Override
	public Object[] getPrefixes() {
		return prefixes;
	}

	@Override
	public boolean matches(InventoryCrafting p_77569_1_, World p_77569_2_) {
		return recipe.matches(p_77569_1_, p_77569_2_);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
		return recipe.getCraftingResult(p_77572_1_);
	}

	@Override
	public int getRecipeSize() {
		return recipe.getRecipeSize();
	}

	@Override
	public ItemStack getRecipeOutput() {
		return recipe.getRecipeOutput();
	}

}
