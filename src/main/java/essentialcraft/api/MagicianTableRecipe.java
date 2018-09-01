package essentialcraft.api;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry.Impl;

public class MagicianTableRecipe extends Impl<IRecipe> implements IRecipe {

	public Ingredient[] requiredItems = {Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY};
	public ItemStack result = ItemStack.EMPTY;
	public int mruRequired;

	public MagicianTableRecipe(Ingredient[] requiredItems, ItemStack result, int mruRequired) {
		for(int i = 0; i < 5 && i < requiredItems.length; ++i) {
			Ingredient ing = requiredItems[i];
			this.requiredItems[i] = ing == null ? Ingredient.EMPTY : ing;
		}
		this.result = result;
		this.mruRequired = mruRequired;
	}

	public MagicianTableRecipe(MagicianTableRecipe recipeByResult) {
		this.requiredItems = recipeByResult.requiredItems.clone();
		this.result = recipeByResult.result.copy();
		this.mruRequired = recipeByResult.mruRequired;
	}

	public boolean matches(ItemStack[] input) {
		if(input.length < 5) {
			return false;
		}
		for(int i = 0; i < 5; ++i) {
			if(!this.requiredItems[i].apply(input[i])) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		String retStr = super.toString();
		for(int i = 0; i < this.requiredItems.length; ++i) {
			retStr+="||item_"+i+":"+requiredItems[i];
		}
		retStr+="||output:"+result;
		retStr+="||mru:"+mruRequired;
		return retStr;
	}

	@Override
	public boolean matches(InventoryCrafting invCrafting, World world) {
		if(invCrafting.getSizeInventory() >= 5) {
			boolean ret = true;
			for(int i = 0; i < 5; ++i) {
				if(!requiredItems[i].apply(invCrafting.getStackInSlot(i))) {
					ret = false;
				}
			}
			return ret;
		}
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		return result;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width*height>=5;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return result;
	}
}
