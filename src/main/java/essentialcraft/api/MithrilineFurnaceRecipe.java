package essentialcraft.api;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class MithrilineFurnaceRecipe {

	public final Ingredient input;
	public final ItemStack result;
	public final float energy;
	public final int stackSize;

	public MithrilineFurnaceRecipe(Ingredient input, ItemStack result, float energy, int stackSize) {
		this.input = input;
		this.result = result;
		this.energy = energy;
		this.stackSize = stackSize;
	}
}
