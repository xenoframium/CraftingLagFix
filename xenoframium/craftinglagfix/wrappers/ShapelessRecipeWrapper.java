package xenoframium.craftinglagfix.wrappers;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;
import xenoframium.craftinglagfix.datastructures.PrefixRepresentable;

public class ShapelessRecipeWrapper implements IRecipe, PrefixRepresentable{
	
	private ShapelessRecipes recipe;
	private ItemStackWrapper[] requiredItems;
	private int hash = 1;
	
	public ShapelessRecipeWrapper(ShapelessRecipes recipe) {
		this.recipe = recipe;
		
		int nullCount = 0;
		for (Object currentItemStack : recipe.recipeItems) {
			if (currentItemStack == null) {
				nullCount++;
			}
		}
		
		requiredItems = new ItemStackWrapper[recipe.recipeItems.size() - nullCount];
		int i = 0;
		Iterator it = recipe.recipeItems.iterator();
		while (it.hasNext()) {
			Object nextStack = it.next();
			if (nextStack != null) {
				requiredItems[i] = new ItemStackWrapper((ItemStack) nextStack);
				i++;
			}
		}
		Arrays.sort(requiredItems);
		
		for (ItemStackWrapper itemStack : requiredItems) {
			hash = 31 * hash + itemStack.hashCode();
		}
	}

	@Override
	public Object[] getPrefixes() {
		return requiredItems;
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
		
		if (!(obj instanceof ShapelessRecipeWrapper)) {
			return false;
		}
		
		ShapelessRecipeWrapper other = (ShapelessRecipeWrapper) obj;
		if (!Arrays.equals(requiredItems, other.requiredItems)) {
			return false;
		}
		return true;
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
