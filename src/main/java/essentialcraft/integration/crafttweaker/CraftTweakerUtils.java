package essentialcraft.integration.crafttweaker;

import java.util.stream.Collectors;

import DummyCore.Utils.IngredientUtils;
import DummyCore.Utils.UnformedItemStack;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class CraftTweakerUtils {

	public static void apply() {
		DemonTrading.addActions.forEach(CraftTweakerAPI::apply);
		MagicianTable.addActions.forEach(CraftTweakerAPI::apply);
		MithrilineFurnace.addActions.forEach(CraftTweakerAPI::apply);
		RadiatingChamber.addActions.forEach(CraftTweakerAPI::apply);
		WindImbue.addActions.forEach(CraftTweakerAPI::apply);
		DemonTrading.removeActions.forEach(CraftTweakerAPI::apply);
		MagicianTable.removeActions.forEach(CraftTweakerAPI::apply);
		MithrilineFurnace.removeActions.forEach(CraftTweakerAPI::apply);
		RadiatingChamber.removeActions.forEach(CraftTweakerAPI::apply);
		WindImbue.removeActions.forEach(CraftTweakerAPI::apply);
	}

	public static UnformedItemStack toUnformedIS(IIngredient ingredient) {
		if(ingredient == null)
			return null;
		else {
			if(ingredient instanceof IOreDictEntry) {
				return new UnformedItemStack(((IOreDictEntry)ingredient).getName());
			}
			else if(ingredient instanceof IItemStack) {
				return new UnformedItemStack(CraftTweakerMC.getItemStack((IItemStack)ingredient));
			}
			else
				return null;
		}
	}

	public static IItemStack getIItemStack(UnformedItemStack uis) {
		return CraftTweakerMC.getIItemStack(uis.getISToDraw(0));
	}

	public static Ingredient toIngredient(IIngredient ingredient) {
		if(ingredient instanceof IOreDictEntry) {
			return IngredientUtils.getIngredient(((IOreDictEntry)ingredient).getName());
		}
		else if(ingredient instanceof IItemStack) {
			ItemStack stack = CraftTweakerMC.getItemStack(ingredient);
			if(stack.hasTagCompound()) {
				return IngredientUtils.getIngredientNBT(stack);
			}
			return IngredientUtils.getIngredient(stack);
		}
		else {
			try {
				return IngredientUtils.getIngredientNBT(
						ingredient.getItems().stream().map(CraftTweakerMC::getItemStack).collect(Collectors.toList()));
			}
			catch(Exception e) {}
		}
		return Ingredient.EMPTY;
	}
}
