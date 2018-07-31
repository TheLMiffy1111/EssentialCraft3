package essentialcraft.integration.crafttweaker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import DummyCore.Utils.UnformedItemStack;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import essentialcraft.api.MagicianTableRecipe;
import essentialcraft.api.MagicianTableRecipes;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.essentialcraft.MagicianTable")
public class MagicianTable {

	@ZenMethod
	public static void addRecipe(IIngredient[] ingredients, IItemStack output, int mru) {
		if(ingredients == null || ingredients.length > 5 || output == null) {
			CraftTweakerAPI.logError("Cannot turn "+Arrays.toString(ingredients)+" into a Magician Table Recipe");
			return;
		}

		boolean allNull = true;
		UnformedItemStack[] input = new UnformedItemStack[ingredients.length];
		for(int i = 0; i < ingredients.length; i++) {
			if(ingredients[i] != null)
				allNull = false;
			input[i] = CraftTweakerUtils.toUnformedIS(ingredients[i]);
		}

		if(allNull) {
			CraftTweakerAPI.logError("Cannot turn "+Arrays.toString(ingredients)+" into a Magician Table Recipe");
			return;
		}

		if(MagicianTableRecipes.CRAFT_MATRIX_LIST.contains(Arrays.asList(input)))
			CraftTweakerAPI.logWarning("Recipe already exists!");
		else
			CraftTweakerAPI.apply(new AddRecipeAction(new MagicianTableRecipe(input, CraftTweakerMC.getItemStack(output), mru)));
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

		ArrayList<MagicianTableRecipe> toRemove = new ArrayList<MagicianTableRecipe>();

		for(MagicianTableRecipe entry : MagicianTableRecipes.RECIPES.values()) {
			if(entry.requiredItems.length <= ingredients.length) {
				boolean flag = true;
				for(int i = 0; i < entry.requiredItems.length; i++) {
					if(entry.requiredItems[i] == null || entry.requiredItems[i].possibleStacks.isEmpty())
						continue;
					if(!ingredients[i].matches(CraftTweakerUtils.getIItemStack(entry.requiredItems[i])))
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
		else
			CraftTweakerAPI.apply(new RemoveRecipeAction(toRemove));
	}



	private static class AddRecipeAction implements IAction {
		MagicianTableRecipe rec;

		public AddRecipeAction(MagicianTableRecipe rec) {
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

	private static class RemoveRecipeAction implements IAction {
		List<MagicianTableRecipe> rec;

		public RemoveRecipeAction(List<MagicianTableRecipe> rec) {
			this.rec = rec;
		}

		@Override
		public void apply() {
			for(MagicianTableRecipe entry : rec) {
				MagicianTableRecipes.removeRecipe(entry);
			}
		}

		@Override
		public String describe() {
			return "Removing "+rec.size()+" Magician Table Recipe(s)";
		}
	}
}
