package ec3.integration.minetweaker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import DummyCore.Utils.UnformedItemStack;
import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.data.DataInt;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import ec3.api.MagicianTableRecipe;
import ec3.api.MagicianTableRecipes;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.essentialcraft.MagicianTable")
public class MagicianTable {

	@ZenMethod
	public static void addRecipe(IIngredient[] ingredients, IItemStack output, int mru) {
		if(ingredients == null || ingredients.length > 5 || output == null) {
			MineTweakerAPI.logError("Cannot turn "+Arrays.toString(ingredients)+" into a Magician Table Recipe");
			return;
		}

		boolean allNull = true;
		UnformedItemStack[] input = new UnformedItemStack[ingredients.length];
		for(int i = 0; i < ingredients.length; i++) {
			if(ingredients[i] != null)
				allNull = false;
			input[i] = MineTweakerUtils.toUnformedIS(ingredients[i]);
		}

		if(allNull) {
			MineTweakerAPI.logError("Cannot turn "+Arrays.toString(ingredients)+" into a Magician Table Recipe");
			return;
		}

		if(MagicianTableRecipes.craftMatrixByID.contains(Arrays.asList(input)))
			MineTweakerAPI.logWarning("Recipe already exists!");
		else
			MineTweakerAPI.apply(new AddRecipeAction(new MagicianTableRecipe(input, MineTweakerMC.getItemStack(output), mru)));
	}

	@ZenMethod
	public static void removeRecipe(IIngredient[] ingredients, @Optional IIngredient output) {
		if(ingredients == null || ingredients.length > 5) {
			MineTweakerAPI.logError("Cannot remove "+Arrays.toString(ingredients)+" from Magician Table Recipes");
			return;
		}

		boolean hasNull = false;
		for(int i = 0; i < ingredients.length; i++) {
			if(ingredients[i] == null)
				hasNull = true;
		}

		if(hasNull) {
			MineTweakerAPI.logError("Cannot remove "+Arrays.toString(ingredients)+" from Magician Table Recipes");
			return;
		}

		ArrayList<MagicianTableRecipe> toRemove = new ArrayList<MagicianTableRecipe>();

		for(MagicianTableRecipe entry : MagicianTableRecipes.recipes.values()) {
			if(entry.requiredItems.length <= ingredients.length) {
				boolean flag = true;
				for(int i = 0; i < entry.requiredItems.length; i++) {
					if(entry.requiredItems[i] == null || entry.requiredItems[i].possibleStacks.isEmpty())
						continue;
					if(!ingredients[i].matches(MineTweakerUtils.getIItemStack(entry.requiredItems[i])))
						flag = false;
				}
				if(flag) {
					if(output == null || output.matches(MineTweakerMC.getIItemStack(entry.result)))
						toRemove.add(entry);
				}
			}
		};

		if(toRemove.isEmpty())
			MineTweakerAPI.logWarning("No recipe for "+Arrays.toString(ingredients));
		else
			MineTweakerAPI.apply(new RemoveRecipeAction(toRemove));
	}



	private static class AddRecipeAction implements IUndoableAction {
		MagicianTableRecipe rec;

		public AddRecipeAction(MagicianTableRecipe rec) {
			this.rec = rec;
		}

		public void apply() {
			MagicianTableRecipes.addRecipe(rec);
		}

		public boolean canUndo() {
			return true;
		}

		public void undo() {
			MagicianTableRecipes.removeRecipe(rec);
		}

		public String describe() {
			return "Adding Magician Table Recipe for "+rec.result.getDisplayName();
		}

		public String describeUndo() {
			return "Removing Magician Table Recipe for "+rec.result.getDisplayName();
		}

		public Object getOverrideKey() {
			return null;
		}
	}

	private static class RemoveRecipeAction implements IUndoableAction {
		List<MagicianTableRecipe> rec;

		public RemoveRecipeAction(List<MagicianTableRecipe> rec) {
			this.rec = rec;
		}

		public void apply() {
			for(MagicianTableRecipe entry : rec) {
				MagicianTableRecipes.removeRecipe(entry);
			}
		}

		public boolean canUndo() {
			return true;
		}

		public void undo() {
			for(MagicianTableRecipe entry : rec) {
				MagicianTableRecipes.addRecipe(entry);
			}
		}

		public String describe() {
			return "Removing "+rec.size()+" Magician Table Recipe(s)";
		}

		public String describeUndo() {
			return "Restoring "+rec.size()+" Magician Table Recipe(s)";
		}

		public Object getOverrideKey() {
			return null;
		}
	}
}
