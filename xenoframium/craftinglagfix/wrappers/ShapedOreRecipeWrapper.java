package xenoframium.craftinglagfix.wrappers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import xenoframium.craftinglagfix.datastructures.PrefixRepresentable;

public class ShapedOreRecipeWrapper implements IRecipe, PrefixRepresentable {

	public ShapedOreRecipe recipe;
	private int hash = 1;
	public Object[] prefixes;
	//private List<List<List<ItemStackWrapper>>> allSlots = new ArrayList<List<List<ItemStackWrapper>>>();
	private int recipeWidth;
	private int recipeHeight;
	private int top;
	private int left;

	public ShapedOreRecipeWrapper(ShapedOreRecipe recipe, boolean isMirrored) {
		recipeWidth = 0;
		recipeHeight = 0;
		// I'm sorry... I need these...
		try {
			Class<?> currentClass = recipe.getClass();
			while (currentClass != ShapedOreRecipe.class) {
				currentClass = currentClass.getSuperclass();
			}

			Field field = currentClass.getDeclaredField("width");
			field.setAccessible(true);
			recipeWidth = (Integer) field.get(recipe);
			field = currentClass.getDeclaredField("height");
			field.setAccessible(true);
			recipeHeight = (Integer) field.get(recipe);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Object[] recipeInput = recipe.getInput();

		if (isMirrored) {
			Object[] mirrored = new Object[recipeWidth * recipeHeight];
			for (int row = 0; row < recipeHeight; row++) {
				for (int column = 0; column < recipeWidth; column++) {
					mirrored[row * recipeWidth + recipeWidth - column - 1] = recipeInput[row * recipeWidth + column];
				}
			}
			recipeInput = mirrored;
		}

		top = 3;
		left = 3;
		int bottom = -1, right = -1;

		for (int row = 0; row < recipeHeight; row++) {
			for (int column = 0; column < recipeWidth; column++) {
				Object element = recipeInput[row * recipeWidth + column];
				if (element != null) {
					if (element instanceof ArrayList && ((ArrayList) element).size() == 0) {
						continue;
					}

					top = Math.min(top, row);
					bottom = Math.max(bottom, row);
					left = Math.min(left, column);
					right = Math.max(right, column);
				}
			}
		}

		bottom++;
		right++;
		int width = right - left;
		int height = bottom - top;

		this.recipe = recipe;
		prefixes = new Object[width * height];
		
		for (int row = top; row < bottom; row++) {
			for (int column = left; column < right; column++) {
				
				Object recipeElement = recipeInput[row * recipeWidth + column];
				
				if (recipeElement instanceof ItemStack || recipeElement == null) {
					prefixes[(row - top) * width + (column - left)] = new ItemStackWrapper((ItemStack) recipeElement);
				} else if (recipeElement instanceof ArrayList) {
					
					ArrayList<ItemStack> validItemStacks = (ArrayList<ItemStack>) recipeElement;
					if (validItemStacks.size() != 0) {
						
						OreDictionary.getOreNames();
						int[] oreDictionaryIDs = OreDictionary.getOreIDs(validItemStacks.get(0));
						
						for (int id : oreDictionaryIDs) {
							if (OreDictionary.getOres(id) == recipeElement) {
								prefixes[(row - top) * width + (column - left)] = OreDictionary.getOreName(id);
								break;
							}
						}
						
					} else {
						prefixes[(row - top) * width + (column - left)] = new ItemStackWrapper(null);
					}
				}
			}
		}

		/*for (int row = 0; row < recipeHeight; row++) {
			allSlots.add(new ArrayList<List<ItemStackWrapper>>(recipeWidth));
			
			for (int column = 0; column < recipeWidth; column++) {
				
				Object recipeElement = recipeInput[row * width + column];
				if (recipeElement instanceof ItemStack || recipeElement == null) {

					ArrayList<ItemStackWrapper> entry = new ArrayList<ItemStackWrapper>(1);
					entry.add(new ItemStackWrapper((ItemStack) recipeElement));
					allSlots.get(row).add(entry);

				} else if (recipeElement instanceof ArrayList) {

					ArrayList<ItemStack> validItemStacks = (ArrayList<ItemStack>) recipeElement;
					if (validItemStacks.size() != 0) {

						ArrayList<ItemStackWrapper> entry = new ArrayList<ItemStackWrapper>(validItemStacks.size());
						for (ItemStack stack : validItemStacks) {
							entry.add(new ItemStackWrapper(stack));
						}
						allSlots.get(row).add(entry);

					} else {
						
						ArrayList<ItemStackWrapper> entry = new ArrayList<ItemStackWrapper>(1);
						entry.add(new ItemStackWrapper(null));
						allSlots.get(row).add(entry);
						
					}
				}
			}
		}*/
		
		hash = Arrays.hashCode(prefixes);
		hash = 31 * hash + recipeWidth;
		hash = 31 * hash + recipeHeight;
		hash = 31 * hash + top;
		hash = 31 * hash + left;
		//hash = 31 * allSlots.hashCode();
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
		if (!(obj instanceof ShapedOreRecipeWrapper)) {
			return false;
		}
		ShapedOreRecipeWrapper other = (ShapedOreRecipeWrapper) obj;
		if (recipe == null) {
			if (other.recipe != null) {
				return false;
			} else {
				return true;
			}
		}
		if (recipeHeight != other.recipeHeight) {
			return false;
		}
		if (recipeWidth != other.recipeWidth) {
			return false;
		}
		if (top != other.top) {
			return false;
		}
		if (left != other.left) {
			return false;
		}
		/*if (allSlots.equals(other.allSlots)) {
			return false;
		}*/
		if (!Arrays.equals(prefixes, other.prefixes)) {
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
