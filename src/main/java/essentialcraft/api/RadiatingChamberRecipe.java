package essentialcraft.api;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry.Impl;

public class RadiatingChamberRecipe extends Impl<IRecipe> implements IRecipe {

	public Ingredient[] recipeItems = {Ingredient.EMPTY, Ingredient.EMPTY};
	public ItemStack result = ItemStack.EMPTY;
	public int mruRequired;
	public float upperBalanceLine,lowerBalanceLine;
	public float costModifier;

	public RadiatingChamberRecipe(Ingredient[] ingred, ItemStack res, int mruReq, float balancePoint1, float balancePoint2) {
		for(int i = 0; i < 2 && i < ingred.length; ++i) {
			Ingredient ing = ingred[i];
			this.recipeItems[i] = ing == null ? Ingredient.EMPTY : ing;
		}
		result = res;
		mruRequired = mruReq;
		upperBalanceLine = Math.max(balancePoint1, balancePoint2);
		lowerBalanceLine = Math.min(balancePoint1, balancePoint2);
		costModifier = 1.0F;
	}

	public RadiatingChamberRecipe(Ingredient[] ingred, ItemStack res, int mruReq, float balancePoint1, float balancePoint2, float modifier) {
		for(int i = 0; i < 2 && i < ingred.length; ++i) {
			Ingredient ing = ingred[i];
			this.recipeItems[i] = ing == null ? Ingredient.EMPTY : ing;
		}
		result = res;
		mruRequired = mruReq;
		upperBalanceLine = Math.max(balancePoint1, balancePoint2);
		lowerBalanceLine = Math.min(balancePoint1, balancePoint2);
		costModifier = modifier;
	}

	public RadiatingChamberRecipe(RadiatingChamberRecipe other) {
		recipeItems = other.recipeItems;
		result = other.result;
		mruRequired = other.mruRequired;
		upperBalanceLine = other.upperBalanceLine;
		lowerBalanceLine = other.lowerBalanceLine;
		costModifier = other.costModifier;
	}

	public boolean matches(ItemStack[] input, float balance) {
		if(input.length < 2) {
			return false;
		}
		if(balance < this.lowerBalanceLine || balance > this.upperBalanceLine) {
			return false;
		}
		for(int i = 0; i < 2; ++i) {
			if(!this.recipeItems[i].apply(input[i])) {
				return false;
			}
		}
		return true;
	}

	public boolean matches(ItemStack[] input) {
		if(input.length < 2) {
			return false;
		}
		for(int i = 0; i < 2; ++i) {
			if(!this.recipeItems[i].apply(input[i])) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		String retStr = super.toString();
		for(int i = 0; i < this.recipeItems.length; ++i)
		{
			retStr+="||item_"+i+":"+recipeItems[i];
		}
		retStr+="||output:"+result;
		retStr+="||mru:"+mruRequired;
		retStr+="||upperBalance:"+upperBalanceLine;
		retStr+="||lowerBalance:"+lowerBalanceLine;
		return retStr;
	}

	@Override
	public boolean matches(InventoryCrafting invCrafting, World world) {
		if(invCrafting.getSizeInventory() >= 2) {
			boolean ret = true;
			for(int i = 0; i < 2; ++i) {
				if(!recipeItems[i].apply(invCrafting.getStackInSlot(i))) {
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
		return width*height>=2;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return result;
	}
}
