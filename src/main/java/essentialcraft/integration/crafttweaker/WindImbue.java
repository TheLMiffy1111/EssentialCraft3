package essentialcraft.integration.crafttweaker;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import essentialcraft.api.WindImbueRecipe;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.essentialcraft.WindImbue")
public class WindImbue {

	public static List<IAction> addActions = Lists.newArrayList();
	public static List<IAction> removeActions = Lists.newArrayList();

	@ZenMethod
	public static void addRecipe(IItemStack input, IItemStack output, int energy) {
		if(input == null || output == null) {
			CraftTweakerAPI.logError("Cannot turn "+input+" into a Wind Imbue Recipe");
			return;
		}
		addActions.add(new ActionAddWindImbueRecipe(input, output, energy));
	}

	@ZenMethod
	public static void removeRecipe(IItemStack input, @Optional IItemStack output) {
		if(input == null) {
			CraftTweakerAPI.logError("Cannot remove "+input+" from Wind Imbue Recipes");
			return;
		}
		removeActions.add(new ActionRemoveWindImbueRecipe(input, output));
	}

	private static class ActionAddWindImbueRecipe implements IAction {
		IItemStack input;
		IItemStack output;
		int energy;
		WindImbueRecipe rec;

		public ActionAddWindImbueRecipe(IItemStack input, IItemStack output, int energy) {
			this.input = input;
			this.output = output;
			this.energy = energy;
		}

		@Override
		public void apply() {
			rec = new WindImbueRecipe(CraftTweakerMC.getItemStack(input), CraftTweakerMC.getItemStack(output), energy);
		}

		@Override
		public String describe() {
			return "Adding Wind Imbue Recipe for "+output.getDisplayName();
		}
	}

	private static class ActionRemoveWindImbueRecipe implements IAction {
		IItemStack input;
		IItemStack output;

		public ActionRemoveWindImbueRecipe(IItemStack input, IItemStack output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public void apply() {
			ArrayList<WindImbueRecipe> toRemove = new ArrayList<WindImbueRecipe>();
			WindImbueRecipe.RECIPES.stream().
			filter(entry->input.contains(CraftTweakerMC.getIIngredient(entry.input)) && (output == null || output.matches(CraftTweakerMC.getIItemStack(entry.result)))).
			forEach(entry->toRemove.add(entry));
			if(toRemove.isEmpty())
				CraftTweakerAPI.logWarning("No recipe for "+input.toString());
			else {
				for(WindImbueRecipe entry : toRemove) {
					WindImbueRecipe.removeRecipe(entry);
				}
			}
		}

		@Override
		public String describe() {
			return "Removing Wind Imbue Recipes for "+input.toString();
		}
	}
}
