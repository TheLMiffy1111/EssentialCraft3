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
import essentialcraft.api.MagicianTableRecipe;
import essentialcraft.api.MagicianTableRecipes;
import net.minecraft.item.crafting.Ingredient;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.essentialcraft.MagicianTable")
public class MagicianTable {

	public static List<IAction> addActions = Lists.newArrayList();
	public static List<IAction> removeActions = Lists.newArrayList();

	@ZenMethod
	public static void addRecipe(IIngredient[] ingredients, IItemStack output, int mru) {
		if(ingredients == null || ingredients.length > 5 || output == null) {
			CraftTweakerAPI.logError("Cannot turn "+Arrays.toString(ingredients)+" into a Magician Table Recipe");
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

		addActions.add(new ActionAddMagicianTableRecipe(new MagicianTableRecipe(input, CraftTweakerMC.getItemStack(output), mru)));
	}

	@ZenMethod
	public static void removeRecipe(IIngredient[] ingredients, @Optional IIngredient output) {
		if(ingredients == null || ingredients.length > 5) {
			CraftTweakerAPI.logError("Cannot remove "+Arrays.toString(ingredients)+" from Magician Table Recipes");
			return;
		}

		boolean hasNull = false;
		for(int i = 0; i < ingredients.length; i++) {
			if(ingredients[i] == null)
				hasNull = true;
		}

		if(hasNull) {
			CraftTweakerAPI.logError("Cannot remove "+Arrays.toString(ingredients)+" from Magician Table Recipes");
			return;
		}
		removeActions.add(new ActionRemoveMagicianTableRecipe(ingredients, output));
	}

	private static class ActionAddMagicianTableRecipe implements IAction {
		MagicianTableRecipe rec;

		public ActionAddMagicianTableRecipe(MagicianTableRecipe rec) {
			this.rec = rec;
		}

		@Override
		public void apply() {
			MagicianTableRecipes.addRecipe(rec);
		}

		@Override
		public String describe() {
			return "Adding Magician Table Recipe for "+rec.result.getDisplayName();
		}
	}

	private static class ActionRemoveMagicianTableRecipe implements IAction {
		IIngredient[] ingredients;
		IIngredient output;

		public ActionRemoveMagicianTableRecipe(IIngredient[] ingredients, IIngredient output) {
			this.ingredients = ingredients;
			this.output = output;
		}

		@Override
		public void apply() {
			ArrayList<MagicianTableRecipe> toRemove = new ArrayList<MagicianTableRecipe>();

			for(MagicianTableRecipe entry : MagicianTableRecipes.RECIPES) {
				if(entry.requiredItems.length <= ingredients.length) {
					boolean flag = true;
					for(int i = 0; i < entry.requiredItems.length; i++) {
						if(entry.requiredItems[i] == null || entry.requiredItems[i] == Ingredient.EMPTY)
							continue;
						if(!ingredients[i].contains(CraftTweakerMC.getIIngredient(entry.requiredItems[i])))
							flag = false;
					}
					if(flag) {
						if(output == null || output.matches(CraftTweakerMC.getIItemStack(entry.result)))
							toRemove.add(entry);
					}
				}
			}

			if(toRemove.isEmpty())
				CraftTweakerAPI.logWarning("No recipe for "+Arrays.toString(ingredients));
			else {
				for(MagicianTableRecipe entry : toRemove) {
					MagicianTableRecipes.removeRecipe(entry);
				}
			}
		}

		@Override
		public String describe() {
			return "Removing Magician Table Recipes for "+Arrays.toString(ingredients);
		}
	}
}
