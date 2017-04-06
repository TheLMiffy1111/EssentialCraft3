package ec3.integration.minetweaker;

import java.util.ArrayList;
import java.util.List;

import DummyCore.Utils.UnformedItemStack;
import ec3.api.MagicianTableRecipe;
import ec3.api.MagicianTableRecipes;
import ec3.api.RadiatingChamberRecipe;
import ec3.api.RadiatingChamberRecipes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import scala.actors.threadpool.Arrays;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.oredict.IOreDictEntry;

public class MineTweakerUtils {
	
	public static UnformedItemStack toUnformedIS(IIngredient ingredient) {
		if(ingredient == null)
			return null;
		else {
			if(ingredient instanceof IOreDictEntry) {
				return new UnformedItemStack(((IOreDictEntry)ingredient).getName());
			}
			else if(ingredient instanceof IItemStack) {
				return new UnformedItemStack(MineTweakerMC.getItemStack((IItemStack)ingredient));
			}
			else
				return null;
		}
	}
	
	public static IItemStack getIItemStack(UnformedItemStack uis) {
		return MineTweakerMC.getIItemStack(uis.getISToDraw(0));
	}
}
