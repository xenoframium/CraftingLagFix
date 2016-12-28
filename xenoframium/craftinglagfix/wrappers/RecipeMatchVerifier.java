package xenoframium.craftinglagfix.wrappers;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import xenoframium.craftinglagfix.datastructures.MatchVerifier;

public class RecipeMatchVerifier implements MatchVerifier{
	private InventoryCrafting inventoryCrafting;
	private World world;

	public RecipeMatchVerifier(InventoryCrafting inventoryCrafting, World world) {
		this.inventoryCrafting = inventoryCrafting;
		this.world = world;
	}
	
	@Override
	public boolean doesMatch(Object obj) {
		return ((IRecipe) obj).matches(inventoryCrafting, world);
	}
	
}
