package essentialcraft.integration.crafttweaker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import essentialcraft.api.RadiatingChamberRecipe;
import essentialcraft.api.RadiatingChamberRecipes;
import net.minecraft.item.crafting.Ingredient;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.essentialcraft.RadiatingChamber")
public class RadiatingChamber {

	public static List<IAction> addActions = Lists.newArrayList();
	public static List<IAction> removeActions = Lists.newArrayList();

	@ZenMethod
	public static void addRecipe(IItemStack[] ingredients, IItemStack output, int mru) {
		addRecipe(ingredients, output, mru, Float.MAX_VALUE, -Float.MAX_VALUE, 1F);
	}

	@ZenMethod
	public static void addRecipe(IItemStack[] ingredients, IItemStack output, int mru, float modifier) {
		addRecipe(ingredients, output, mru, Float.MAX_VALUE, -Float.MAX_VALUE, modifier);
	}

	@ZenMethod
	public static void addRecipe(IItemStack[] ingredients, IItemStack output, int mru, float upperBalance, float lowerBalance) {
		addRecipe(ingredients, output, mru, upperBalance, lowerBalance, 1F);
	}

	@ZenMethod
	public static void addRecipe(IItemStack[] ingredients, IItemStack output, int mru, float upperBalance, float lowerBalance, float modifier) {
		if(ingredients == null || ingredients.length > 2 || output == null) {
			CraftTweakerAPI.logError("Cannot turn "+Arrays.toString(ingredients)+" into a Radiating Chamber Recipe");
			return;
		}

		boolean allNull = true;

		Ingredient[] input = new Ingredient[ingredients.length];
		for(int i = 0; i < ingredients.length; i++) {
			if(ingredients[i] != null)
				allNull = false;
			input[i] = CraftTweakerUtils.toIngredient(ingredients[i]);
		}

		if(allNull) {
			CraftTweakerAPI.logError("Cannot turn "+Arrays.toString(ingredients)+" into a Magician Table Recipe");
			return;
		}

		addActions.add(new ActionAddRadiatingChamberRecipe(new RadiatingChamberRecipe(input, CraftTweakerMC.getItemStack(output), mru, upperBalance, lowerBalance, modifier)));
	}

	@ZenMethod
	public static void removeRecipe(IIngredient[] ingredients, float balance) {
		removeRecipe(ingredients, null, balance);
	}

	@ZenMethod
	public static void removeRecipe(IIngredient[] ingredients, @Optional IIngredient output) {
		removeRecipe(ingredients, output, Float.NaN);
	}

	@ZenMethod
	public static void removeRecipe(IIngredient[] ingredients, IIngredient output, float balance) {
		if(ingredients == null || ingredients.length > 2) {
			CraftTweakerAPI.logError("Cannot remove "+Arrays.toString(ingredients)+" from Radiating Chamber Recipes");
			return;
		}

		boolean hasNull = false;
		for(int i = 0; i < ingredients.length; i++) {
			if(ingredients[i] == null)
				hasNull = true;
		}

		if(hasNull) {
			CraftTweakerAPI.logError("Cannot remove "+Arrays.toString(ingredients)+" from Radiating Chamber Recipes");
			return;
		}

		removeActions.add(new ActionRemoveRadiatingChamberRecipe(ingredients, output, balance));
	}

	private static class ActionAddRadiatingChamberRecipe implements IAction {
		RadiatingChamberRecipe rec;

		public ActionAddRadiatingChamberRecipe(RadiatingChamberRecipe rec) {
			this.rec = rec;
		}

		@Override
		public void apply() {
			RadiatingChamberRecipes.addRecipe(rec);
		}

		@Override
		public String describe() {
			return "Adding Radiating Chamber Recipe for "+rec.result.getDisplayName();
		}
	}

	private static class ActionRemoveRadiatingChamberRecipe implements IAction {
		IIngredient[] ingredients;
		IIngredient output;
		float balance;

		public ActionRemoveRadiatingChamberRecipe(IIngredient[] ingredients, IIngredient output, float balance) {
			this.ingredients = ingredients;
			this.output = output;
			this.balance = balance;
		}

		@Override
		public void apply() {
			ArrayList<RadiatingChamberRecipe> toRemove = new ArrayList<RadiatingChamberRecipe>();
			for(RadiatingChamberRecipe rec : RadiatingChamberRecipes.RECIPES) {
				if(
						ingredients[0].contains(CraftTweakerMC.getIIngredient(rec.recipeItems[0])) &&
						(rec.recipeItems[1] == Ingredient.EMPTY || ingredients.length == 2 && ingredients[1].contains(CraftTweakerMC.getIIngredient(rec.recipeItems[2])) &&
						(output == null || output.matches(CraftTweakerMC.getIItemStack(rec.result))) &&
						(Float.isNaN(balance) || balance <= rec.upperBalanceLine && balance >= rec.lowerBalanceLine)))
					toRemove.add(rec);
			}

			if(toRemove.isEmpty())
				CraftTweakerAPI.logWarning("No recipe for "+Arrays.toString(ingredients));
			else {
				for(RadiatingChamberRecipe entry : toRemove) {
					RadiatingChamberRecipes.removeRecipe(entry);
				}
			}
		}

		@Override
		public String describe() {
			return "Removing Radiating Chamber Recipes for "+Arrays.toString(ingredients);
		}
	}
}
