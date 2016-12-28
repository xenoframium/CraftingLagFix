package xenoframium.craftinglagfix.wrappers;

import java.util.Arrays;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.world.World;
import xenoframium.craftinglagfix.datastructures.PrefixRepresentable;

public class ShapedRecipeWrapper implements IRecipe, PrefixRepresentable{
	private ShapedRecipes recipe;
	private ItemStackWrapper[] requiredItems;
	private int hash = 1;
	private int height;
	private int top;
	private int left;

	public ShapedRecipeWrapper(ShapedRecipes recipe) {
		this.recipe = recipe;
		
		top = 3;
		left = 3;
		int bottom = -1, right = -1;
		
		//Trim recipe for hashing
		for (int row = 0; row < recipe.recipeHeight; row++) {
			for (int column = 0; column < recipe.recipeWidth; column++) {
				if (recipe.recipeItems[row * recipe.recipeWidth + column] != null) {
					top = Math.min(top, row);
					bottom = Math.max(bottom, row);
					left = Math.min(left, column);
					right = Math.max(right, column);
				}
			}
		}

		bottom++;
		right++;
		
		height = recipe.recipeHeight;
		
		if (top == 3) {
			return;
		}
		
		int width = right - left;
		int height = bottom - top;
		
		requiredItems = new ItemStackWrapper[width * height];
		
		for (int r = top; r < bottom; r++) {
			for (int c = left; c < right; c++) {
				ItemStack res = recipe.recipeItems[r * width + c];
				requiredItems[(r - top) * width + (c - left)] = new ItemStackWrapper(res);
			}
		}
		
		for (ItemStackWrapper itemStack : requiredItems) {
			hash = 31 * hash + itemStack.hashCode();
		}
		
		hash = 31 * hash + recipe.recipeHeight;
		hash = 31 * hash + recipe.recipeWidth;
		hash = 31 * hash + top;
		hash = 31 * hash + left;
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
		
		if (!(obj instanceof ShapedRecipeWrapper)) {
			return false;
		}
		
		ShapedRecipeWrapper other = (ShapedRecipeWrapper) obj;
		if (recipe.recipeHeight != other.recipe.recipeHeight) {
			return false;
		}
		if (recipe.recipeWidth != other.recipe.recipeWidth) {
			return false;
		}
		if (height != other.height) {
			
		}
		if (top != other.top) {
			return false;
		}
		if (left != other.left) {
			return false;
		}
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
