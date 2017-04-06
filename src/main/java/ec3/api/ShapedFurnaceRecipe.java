package ec3.api;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class ShapedFurnaceRecipe implements IRecipe {

	public ItemStack result;
	public ItemStack smelted;

	public ShapedFurnaceRecipe(ItemStack i, ItemStack i_1)
	{
		smelted = i;
		result = i_1;
	}

	@Override
	public boolean matches(InventoryCrafting p_77569_1_, World p_77569_2_) {
		return p_77569_1_.getStackInSlot(0).isItemEqual(smelted) && p_77569_1_.getStackInSlot(1).isItemEqual(result);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
		return result;
	}

	@Override
	public int getRecipeSize() {
		return 2;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return result;
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inv) {
		return new ItemStack[1];
	}
}
