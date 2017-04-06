package ec3.integration.minetweaker;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.data.DataInt;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import ec3.api.MagicianTableRecipe;
import ec3.api.MagicianTableRecipes;
import ec3.api.WindImbueRecipe;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.essentialcraft.WindImbue")
public class WindImbue {

	@ZenMethod
	public static void addRecipe(IItemStack input, IItemStack output, int energy) {
		if(input == null || output == null) {
			MineTweakerAPI.logError("Cannot turn "+input+" into a Wind Imbue Recipe");
			return;
		}

		boolean flag = true;
		for(WindImbueRecipe rec : WindImbueRecipe.recipes) {
			if(input.matches(MineTweakerMC.getIItemStack(rec.transforming)))
				flag = false;
		}

		if(flag)
			MineTweakerAPI.apply(new AddRecipeAction(MineTweakerMC.getItemStack(input),MineTweakerMC.getItemStack(output),energy));
		else
			MineTweakerAPI.logWarning("Recipe already exists!");
	}

	@ZenMethod
	public static void removeRecipe(IItemStack input, @Optional IItemStack output) {
		if(input == null) {
			MineTweakerAPI.logError("Cannot remove "+input+" from Wind Imbue Recipes");
			return;
		}

		final ArrayList<WindImbueRecipe> toRemove = new ArrayList<WindImbueRecipe>();
		WindImbueRecipe.recipes.stream().filter((entry) -> {
			return input.matches(MineTweakerMC.getIItemStack(entry.transforming)) && (output == null || output.matches(MineTweakerMC.getIItemStack(entry.result)));
		}).forEach((entry) -> {
			toRemove.add(entry);
		});

		if(toRemove.isEmpty())
			MineTweakerAPI.logWarning("No recipe for "+input.toString());
		else
			MineTweakerAPI.apply(new RemoveRecipeAction(toRemove));
	}

	private static class AddRecipeAction implements IUndoableAction {
		ItemStack input;
		ItemStack output;
		int energy;
		WindImbueRecipe rec;

		public AddRecipeAction(ItemStack input, ItemStack output, int energy) {
			this.input = input;
			this.output = output;
			this.energy = energy;
		}

		public void apply() {
			rec = new WindImbueRecipe(input, output, energy);
		}

		public boolean canUndo() {
			return true;
		}

		public void undo() {
			WindImbueRecipe.removeRecipe(rec);
		}

		public String describe() {
			return "Adding Wind Imbue Recipe for "+output.getDisplayName();
		}

		public String describeUndo() {
			return "Removing Wind Imbue Recipe for "+output.getDisplayName();
		}

		public Object getOverrideKey() {
			return null;
		}
	}

	private static class RemoveRecipeAction implements IUndoableAction {
		List<WindImbueRecipe> rec;

		public RemoveRecipeAction(List<WindImbueRecipe> rec) {
			this.rec = rec;
		}

		public void apply() {
			for(WindImbueRecipe entry : rec) {
				WindImbueRecipe.removeRecipe(entry);
			}
		}

		public boolean canUndo() {
			return true;
		}

		public void undo() {
			for(WindImbueRecipe entry : rec) {
				new WindImbueRecipe(entry.transforming, entry.result, entry.enderEnergy);
			}
		}

		public String describe() {
			return "Removing "+rec.size()+" Wind Imbue Recipes";
		}

		public String describeUndo() {
			return "Restoring "+rec.size()+" Wind Imbue Recipes";
		}

		public Object getOverrideKey() {
			return null;
		}
	}
}
