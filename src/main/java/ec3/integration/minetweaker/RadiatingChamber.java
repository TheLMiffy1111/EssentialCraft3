package ec3.integration.minetweaker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import DummyCore.Utils.UnformedItemStack;
import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.data.DataFloat;
import minetweaker.api.data.DataInt;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import ec3.api.RadiatingChamberRecipe;
import ec3.api.RadiatingChamberRecipes;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.essentialcraft.RadiatingChamber")
public class RadiatingChamber {
	
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
			MineTweakerAPI.logError("Cannot turn "+Arrays.toString(ingredients)+" into a Radiating Chamber Recipe");
			return;
		}
		
		boolean allNull = true;
		
		ItemStack[] input = new ItemStack[2];
		for(int i = 0; i < ingredients.length; i++) {
			if(ingredients[i] != null)
				allNull = false;
			input[i] = MineTweakerMC.getItemStack(ingredients[i]);
		}
		
		if(allNull) {
			MineTweakerAPI.logError("Cannot turn "+Arrays.toString(ingredients)+" into a Radiating Chamber Recipe");
			return;
		}
		
		if(RadiatingChamberRecipes.craftMatrixByID.contains(Arrays.toString(input)))
			MineTweakerAPI.logWarning("Recipe already exists!");
		else
			MineTweakerAPI.apply(new AddRecipeAction(new RadiatingChamberRecipe(input, MineTweakerMC.getItemStack(output), mru, new float[] {upperBalance, lowerBalance}, modifier)));
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
			MineTweakerAPI.logError("Cannot remove "+Arrays.toString(ingredients)+" from Radiating Chamber Recipes");
			return;
		}
		
		boolean hasNull = false;
		for(int i = 0; i < ingredients.length; i++) {
			if(ingredients[i] == null)
				hasNull = true;
		}
		
		if(hasNull) {
			MineTweakerAPI.logError("Cannot remove "+Arrays.toString(ingredients)+" from Radiating Chamber Recipes");
			return;
		}
		
		ArrayList<RadiatingChamberRecipe> toRemove = new ArrayList<RadiatingChamberRecipe>();
		for(List<RadiatingChamberRecipe> entry : RadiatingChamberRecipes.recipes.values()) {
			for(RadiatingChamberRecipe rec : entry) {
				if(
						ingredients[0].matches(MineTweakerMC.getIItemStack(rec.recipeItems[0])) &&
						(rec.recipeItems[1] == null || (ingredients.length == 2 && ingredients[1].matches(MineTweakerMC.getIItemStack(rec.recipeItems[2]))) &&
						(output == null || output.matches(MineTweakerMC.getIItemStack(rec.result))) && 
						(Float.isNaN(balance) || (balance <= rec.upperBalanceLine && balance >= rec.lowerBalanceLine))))
					toRemove.add(rec);
			}
		}
		
		if(toRemove.isEmpty())
			MineTweakerAPI.logWarning("No recipe for "+Arrays.toString(ingredients));
		else
			MineTweakerAPI.apply(new RemoveRecipeAction(toRemove));
	}
	
	private static class AddRecipeAction implements IUndoableAction {
		RadiatingChamberRecipe rec;
		
		public AddRecipeAction(RadiatingChamberRecipe rec) {
			this.rec = rec;
		}
		
		public void apply() {
			RadiatingChamberRecipes.addRecipe(rec);
		}
		
		public boolean canUndo() {
			return true;
		}
		
		public void undo() {
			RadiatingChamberRecipes.removeRecipe(rec);
		}
		
		public String describe() {
			return "Adding Radiating Chamber Recipe for "+rec.result.getDisplayName();
		}
		
		public String describeUndo() {
			return "Removing Radiating Chamber Recipe for "+rec.result.getDisplayName();
		}
		
		public Object getOverrideKey() {
			return null;
		}
	}
	
	private static class RemoveRecipeAction implements IUndoableAction {
		List<RadiatingChamberRecipe> rec;
		
		public RemoveRecipeAction(List<RadiatingChamberRecipe> rec) {
			this.rec = rec;
		}
		
		public void apply() {
			for(RadiatingChamberRecipe entry : rec) {
				RadiatingChamberRecipes.removeRecipe(entry);
			}
		}
		
		public boolean canUndo() {
			return true;
		}
		
		public void undo() {
			for(RadiatingChamberRecipe entry : rec) {
				RadiatingChamberRecipes.addRecipe(entry);
			}
		}
		
		public String describe() {
			return "Removing "+rec.size()+" Radiating Chamber Recipe";
		}
		
		public String describeUndo() {
			return "Restoring "+rec.size()+" Radiating Chamber Recipe";
		}
		
		public Object getOverrideKey() {
			return null;
		}
	}
}
