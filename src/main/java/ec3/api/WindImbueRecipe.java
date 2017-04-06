package ec3.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class WindImbueRecipe implements IRecipe {

	public ItemStack result;
	public ItemStack transforming;
	public int enderEnergy;

	public static final List<WindImbueRecipe> recipes = new ArrayList<WindImbueRecipe>();

	public static WindImbueRecipe findRecipeByComponent(ItemStack c) {
		if(c == null)
			return null;

		for(int i = 0; i < recipes.size(); ++i) {
			if(c.isItemEqual(recipes.get(i).transforming))
				return recipes.get(i);
		}

		return null;
	}

	public static WindImbueRecipe findRecipeByResult(ItemStack r) {
		if(r == null)
			return null;

		for(int i = 0; i < recipes.size(); ++i)
		{
			if(r.isItemEqual(recipes.get(i).result))
				return recipes.get(i);
		}

		return null;
	}

	public WindImbueRecipe(ItemStack s1, ItemStack s2, int i) {
		transforming = s1;
		result = s2;
		enderEnergy = i;
		recipes.add(this);
	}

	public static void removeRecipe(WindImbueRecipe rec) {
		recipes.remove(rec);
	}

	public static void removeRecipeByComponent(ItemStack c) {
		removeRecipe(findRecipeByComponent(c));
	}

	public static void removeRecipeByResult(ItemStack r) {
		removeRecipe(findRecipeByResult(r));
	}

	public static void removeRecipe(ItemStack s, ItemStack r) {
		for(int i = 0; i < recipes.size(); ++i) {
			WindImbueRecipe rec = recipes.get(i);
			if(s.isItemEqual(rec.transforming) && r.isItemEqual(rec.result)) {
				removeRecipe(rec);
				return;
			}
		}
	}

	@Override
	public boolean matches(InventoryCrafting c, World w) {
		return c.getSizeInventory() > 0 && c.getSizeInventory() < 3 && c.getStackInSlot(0) != null && c.getStackInSlot(0).isItemEqual(transforming);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting c) {
		return result;
	}

	@Override
	public int getRecipeSize() {
		return 1;
	}

	@Override
	public ItemStack getRecipeOutput()  {
		return result;
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inv) {
		return new ItemStack[1];
	}
}
