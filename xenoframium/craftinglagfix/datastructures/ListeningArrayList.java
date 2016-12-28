package xenoframium.craftinglagfix.datastructures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

import net.minecraft.item.crafting.IRecipe;
import xenoframium.craftinglagfix.CraftingRegistry;

public class ListeningArrayList extends ArrayList<IRecipe> {
	
	@Override
	public boolean add(IRecipe e) {
		CraftingRegistry.registerRecipe(e);
		return super.add(e);
	}

	@Override
	public void add(int index, IRecipe element) {
		CraftingRegistry.registerRecipe(element);
		super.add(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends IRecipe> c) {
		for (IRecipe recipe : c) {
			CraftingRegistry.registerRecipe(recipe);
		}

		return super.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends IRecipe> c) {
		for (IRecipe recipe : c) {
			CraftingRegistry.registerRecipe(recipe);
		}

		return super.addAll(index, c);
	}

	@Override
	public void clear() {
		for (IRecipe recipe : this) {
			CraftingRegistry.removeRecipe(recipe);
		}

		super.clear();
	}

	@Override
	public IRecipe remove(int index) {
		IRecipe removed = super.remove(index);
		
		if (removed != null) {
			CraftingRegistry.removeRecipe(removed);
		}
		
		return removed;
	}
	
	@Override
	public boolean remove(Object o) {
		if (super.remove(o)) {
			CraftingRegistry.removeRecipe((IRecipe) o);
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		for (Object o : c) {
			if (o instanceof IRecipe) {
				CraftingRegistry.removeRecipe((IRecipe) o);
			}
		}
		
		return super.removeAll(c);
	}
	
	@Override
	public boolean retainAll(Collection<?> c) {
		for (IRecipe recipe : this) {
			if (!c.contains(recipe)) {
				CraftingRegistry.removeRecipe(recipe);
			}
		}
		
		return super.retainAll(c);
	}
	
	@Override
	public IRecipe set(int index, IRecipe element) {
		IRecipe oldValue = super.set(index, element);
		CraftingRegistry.removeRecipe(oldValue);
		CraftingRegistry.registerRecipe(element);
		
		return oldValue;
	}
	
}
