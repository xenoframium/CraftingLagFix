package xenoframium.craftinglagfix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;

public class CraftingLagFix extends DummyModContainer {
	
	public CraftingLagFix() {
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = "craftingLagFix";
		meta.name = "Crafting Lag Fix";
		meta.version = "1.0.0";
		meta.authorList = Arrays.asList("xenoframium");
	}
	
	@Subscribe
	public void serverStarting(FMLServerStartingEvent event) {
		System.out.println("Starting crafting recipe initialisation...");
		CraftingRegistry.processRecipes();
	}
	
	@Subscribe
	public void oreRegisterEvent(OreRegisterEvent event) {
		CraftingRegistry.reregisterOreDictRecipes(event.Name, event.Ore);
	}
	
	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}

}