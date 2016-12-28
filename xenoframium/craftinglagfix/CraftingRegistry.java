package xenoframium.craftinglagfix;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import xenoframium.craftinglagfix.datastructures.ListeningArrayList;
import xenoframium.craftinglagfix.datastructures.PriorityCache;
import xenoframium.craftinglagfix.datastructures.ShapedOreTrie;
import xenoframium.craftinglagfix.datastructures.ShapelessOreTrie;
import xenoframium.craftinglagfix.datastructures.Trie;
import xenoframium.craftinglagfix.wrappers.InventoryCraftingWrapper;
import xenoframium.craftinglagfix.wrappers.ItemStackWrapper;
import xenoframium.craftinglagfix.wrappers.OreRecipeKeyGenerator;
import xenoframium.craftinglagfix.wrappers.RecipeMatchVerifier;
import xenoframium.craftinglagfix.wrappers.ShapedInventoryCrafting;
import xenoframium.craftinglagfix.wrappers.ShapedOreRecipeWrapper;
import xenoframium.craftinglagfix.wrappers.ShapedRecipeWrapper;
import xenoframium.craftinglagfix.wrappers.ShapelessInventoryCrafting;
import xenoframium.craftinglagfix.wrappers.ShapelessOreRecipeWrapper;
import xenoframium.craftinglagfix.wrappers.ShapelessRecipeWrapper;
import xenoframium.craftinglagfix.wrappers.WildCardKeyGenerator;

public class CraftingRegistry {
	private static int MAX_ELEMENTS;

	public static List<IRecipe> recipes = new ListeningArrayList();

	private static Map<ItemStackWrapper, Set<String>> oreDictNames = new HashMap<ItemStackWrapper, Set<String>>();
	private static Trie<IRecipe> recipeTrie = new Trie<IRecipe>();
	private static ShapedOreTrie shapedOreRecipeTrie = new ShapedOreTrie();
	private static ShapelessOreTrie shapelessOreRecipeTrie = new ShapelessOreTrie();
	private static List<IRecipe> unsupportedRecipeList = new ArrayList<IRecipe>();
	private static Set<IRecipe> oreRecipesSet = new HashSet<IRecipe>();
	private static PriorityCache<Object, IRecipe> cache = null;
	private static IRecipe lastRecipe = null;

	private static WildCardKeyGenerator wildCardKeyGenerator = new WildCardKeyGenerator();
	private static OreRecipeKeyGenerator oreRecipeKeyGenerator = new OreRecipeKeyGenerator();
	private static boolean isSafeToAccessOreDictionary = false;

	public static Set<String> getOreDictEntries(ItemStackWrapper stack) {
		return oreDictNames.get(stack);
	}

	public static void processRecipes() {
		isSafeToAccessOreDictionary = true;
		for (String name : OreDictionary.getOreNames()) {
			for (ItemStack ore : OreDictionary.getOres(name)) {
				ItemStackWrapper wrapper = new ItemStackWrapper(ore);
				Set<String> names = oreDictNames.get(wrapper);
				if (names != null) {
					names.add(name);
				} else {
					names = new HashSet<String>();
					names.add(name);
					oreDictNames.put(wrapper, names);
				}
			}
		}
		for (IRecipe recipe : recipes) {
			registerRecipe(recipe);
		}
	}

	public static void registerRecipe(IRecipe recipe) {
		if (isSafeToAccessOreDictionary) {
			if (recipe instanceof ShapelessRecipes) {
				ShapelessRecipeWrapper wrapper = new ShapelessRecipeWrapper((ShapelessRecipes) recipe);
				recipeTrie.addValue(wrapper, wrapper);
			} else if (recipe instanceof ShapedRecipes) {
				ShapedRecipeWrapper wrapper = new ShapedRecipeWrapper((ShapedRecipes) recipe);
				recipeTrie.addValue(wrapper, wrapper);
			} else if (recipe instanceof ShapedOreRecipe) {
				ShapedOreRecipe shaped = (ShapedOreRecipe) recipe;
				ShapedOreRecipeWrapper wrapper = new ShapedOreRecipeWrapper(shaped, false);
				shapedOreRecipeTrie.addValue(wrapper);
				Field mirrored = null;
				boolean isMirrorable = false;
				try {
					mirrored = shaped.getClass().getDeclaredField("mirrored");
					mirrored.setAccessible(true);
					isMirrorable = (Boolean) mirrored.get(shaped);
				} catch (Exception e) {
				}

				if (isMirrorable) {
					wrapper = new ShapedOreRecipeWrapper(shaped, true);
					shapedOreRecipeTrie.addValue(wrapper);
				}
				oreRecipesSet.add(wrapper);
			} else if (recipe instanceof ShapelessOreRecipe) {
				ShapelessOreRecipeWrapper wrapper = new ShapelessOreRecipeWrapper((ShapelessOreRecipe) recipe);
				shapelessOreRecipeTrie.addValue(wrapper);
				oreRecipesSet.add(wrapper);
			} else {
				unsupportedRecipeList.add(recipe);
			}
		}
	}

	// Just in case some sneaky person feels like registering an ore after
	// loading is complete ; )
	public static void reregisterOreDictRecipes(String name, ItemStack itemStack) {
		if (isSafeToAccessOreDictionary) {
			System.out.println("New ore registered, reregistering recipes...");
			oreDictNames.get(new ItemStackWrapper(itemStack)).add(name);
			for (IRecipe recipe : oreRecipesSet) {
				removeRecipe(recipe);
				registerRecipe(recipe);
			}
		}
	}

	public static void removeRecipe(IRecipe recipe) {
		if (isSafeToAccessOreDictionary) {
			if (recipe instanceof ShapelessRecipes) {
				ShapelessRecipeWrapper wrapper = new ShapelessRecipeWrapper((ShapelessRecipes) recipe);
				recipeTrie.removeValue(wrapper, wrapper);
			} else if (recipe instanceof ShapedRecipes) {
				ShapedRecipeWrapper wrapper = new ShapedRecipeWrapper((ShapedRecipes) recipe);
				recipeTrie.removeValue(wrapper, wrapper);
			} else if (recipe instanceof ShapedOreRecipe) {
				ShapedOreRecipeWrapper wrapper = new ShapedOreRecipeWrapper((ShapedOreRecipe) recipe, true);
				shapedOreRecipeTrie.removeValue(wrapper);

				Field mirrored = null;
				boolean isMirrorable = false;
				try {
					mirrored = recipe.getClass().getDeclaredField("mirrored");
					mirrored.setAccessible(true);
					isMirrorable = (Boolean) mirrored.get(recipe);
				} catch (Exception e) {
				}

				if (isMirrorable) {
					wrapper = new ShapedOreRecipeWrapper((ShapedOreRecipe) recipe, false);
					shapedOreRecipeTrie.removeValue(wrapper);
				}
			} else if (recipe instanceof ShapelessOreRecipe) {
				ShapelessOreRecipeWrapper wrapper = new ShapelessOreRecipeWrapper((ShapelessOreRecipe) recipe);
				shapelessOreRecipeTrie.removeValue(wrapper);
			} else {
				unsupportedRecipeList.remove(recipe);
			}
		}
	}

	static long lastCraftTime = 0;
	static long totalTime = 0;
	public static IRecipe getRecipe(InventoryCrafting inventoryCrafting, World world) {
		if (lastRecipe != null) {
			if (lastRecipe.matches(inventoryCrafting, world)) {
				return lastRecipe;
			}
		}

		RecipeMatchVerifier matchVerifier = new RecipeMatchVerifier(inventoryCrafting, world);

		ShapelessInventoryCrafting shapeless = new ShapelessInventoryCrafting(inventoryCrafting);
		ShapedInventoryCrafting shaped = new ShapedInventoryCrafting(inventoryCrafting);

		if (cache == null) {
			cache = new PriorityCache<Object, IRecipe>(MAX_ELEMENTS);
		} else {
			IRecipe result = cache.get(shapeless);
			if (result != null && result.matches(inventoryCrafting, world)) {
				cache.increment(shapeless);
				lastRecipe = result;
				return result;
			}
			result = cache.get(shaped);
			if (result != null && result.matches(inventoryCrafting, world)) {
				cache.increment(shaped);
				lastRecipe = result;
				return result;
			}
		}

		boolean areAllSlotsEmpty = true;
		for (int i = 0; i < inventoryCrafting.getSizeInventory(); i++) {
			if (inventoryCrafting.getStackInSlot(i) != null) {
				areAllSlotsEmpty = false;
				break;
			}
		}

		if (areAllSlotsEmpty) {
			return null;
		}
		
		IRecipe result = recipeTrie.getValue(shapeless, wildCardKeyGenerator, matchVerifier);
		if (result != null) {
			cache.add(shapeless, result);
			lastRecipe = result;
			return result;
		}

		result = recipeTrie.getValue(shaped, wildCardKeyGenerator, matchVerifier);
		if (result != null) {
			cache.add(shaped, result);
			lastRecipe = result;
			return result;
		}

		result = shapedOreRecipeTrie.getValue(shaped, matchVerifier);
		if (result != null) {
			cache.add(shaped, result);
			lastRecipe = result;
			return result;
		}

		result = shapelessOreRecipeTrie.getValue(shapeless, matchVerifier);
		if (result != null) {
			cache.add(shapeless, result);
			lastRecipe = result;
			return result;
		}

		InventoryCraftingWrapper cacheKey = new InventoryCraftingWrapper(inventoryCrafting);

		for (IRecipe recipe : unsupportedRecipeList) {
			if (recipe.matches(inventoryCrafting, world)) {
				cache.add(cacheKey, recipe);
				lastRecipe = recipe;
				return recipe;
			}
		}

		return null;
	}
}
