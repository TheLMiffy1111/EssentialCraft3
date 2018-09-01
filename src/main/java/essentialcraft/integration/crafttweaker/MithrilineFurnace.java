package essentialcraft.integration.crafttweaker;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import essentialcraft.api.MithrilineFurnaceRecipe;
import essentialcraft.api.MithrilineFurnaceRecipes;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.essentialcraft.MithrilineFurnace")
public class MithrilineFurnace {

	public static List<IAction> addActions = Lists.newArrayList();
	public static List<IAction> removeActions = Lists.newArrayList();

	@ZenMethod
	public static void addRecipe(IIngredient input, IItemStack output, float enderpower) {
		if(input == null || output == null) {
			CraftTweakerAPI.logError("Cannot turn "+input+" into a Mithriline Furnace Recipe");
			return;
		}
		addActions.add(new ActionAddMithrilineFurnaceRecipe(input, output, enderpower));
	}

	@ZenMethod
	public static void removeRecipe(IIngredient input, @Optional IItemStack output) {
		if(input == null) {
			CraftTweakerAPI.logError("Cannot remove "+input+" from Mithriline Furnace Recipes");
			return;
		}
		removeActions.add(new ActionRemoveMithrilineFurnaceRecipe(input, output));
	}

	private static class ActionAddMithrilineFurnaceRecipe implements IAction {
		IIngredient input;
		IItemStack output;
		float enderpower;

		public ActionAddMithrilineFurnaceRecipe(IIngredient input, IItemStack output, float enderpower) {
			this.input = input;
			this.output = output;
			this.enderpower = enderpower;
		}

		@Override
		public void apply() {
			boolean flag = true;
			for(MithrilineFurnaceRecipe rec : MithrilineFurnaceRecipes.RECIPES) {
				if(input.contains(CraftTweakerMC.getIIngredient(rec.input))) {
					flag = false;
				}
			}

			if(flag)
				MithrilineFurnaceRecipes.addRecipe(new MithrilineFurnaceRecipe(CraftTweakerUtils.toIngredient(input), CraftTweakerMC.getItemStack(output), enderpower, input.getAmount()));
			else
				CraftTweakerAPI.logWarning("Recipe already exists!");
		}

		@Override
		public String describe() {
			return "Adding Mithriline Furnace Recipe for "+output.getDisplayName();
		}
	}

	private static class ActionRemoveMithrilineFurnaceRecipe implements IAction {
		IIngredient input;
		IItemStack output;

		public ActionRemoveMithrilineFurnaceRecipe(IIngredient input, IItemStack output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public void apply() {
			ArrayList<MithrilineFurnaceRecipe> toRemove = new ArrayList<MithrilineFurnaceRecipe>();
			MithrilineFurnaceRecipes.RECIPES.stream().
			filter(entry->input.contains(CraftTweakerMC.getIIngredient(entry.input)) && (output == null || output.matches(CraftTweakerMC.getIItemStack(entry.result)))).
			forEach(entry->toRemove.add(entry));

			if(toRemove.isEmpty())
				CraftTweakerAPI.logWarning("No recipe for "+input.toString());
			else {
				for(MithrilineFurnaceRecipe entry : toRemove) {
					MithrilineFurnaceRecipes.removeRecipe(entry);
				}
			}
		}

		@Override
		public String describe() {
			return "Removing Mithriline Furnace Recipes for "+input.toString();
		}
	}
}
