package essentialcraft.api;

import java.util.ArrayList;
import java.util.List;

import DummyCore.Utils.IngredientUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

public class MithrilineFurnaceRecipes {

	public static final List<MithrilineFurnaceRecipe> RECIPES = new ArrayList<MithrilineFurnaceRecipe>();

	public static void addRecipe(Ingredient input, ItemStack result, float cost, int req) {
		addRecipe(new MithrilineFurnaceRecipe(input, result, cost, req));
	}

	public static void addRecipe(ItemStack input, ItemStack result, float cost, int req) {
		Ingredient is = Ingredient.fromStacks(input.copy());
		addRecipe(new MithrilineFurnaceRecipe(is, result, cost, req));
	}

	public static void addRecipe(String input, ItemStack result, float cost, int req) {
		Ingredient is = new OreIngredient(input);
		addRecipe(new MithrilineFurnaceRecipe(is, result, cost, req));
	}

	public static void addRecipe(Object input, ItemStack result, float cost, int req) {
		Ingredient is = IngredientUtils.getIngredient(input);
		addRecipe(new MithrilineFurnaceRecipe(is, result, cost, req));
	}

	public static void addRecipe(MithrilineFurnaceRecipe rec) {
		RECIPES.add(rec);
	}

	public static void removeRecipeByInput(ItemStack component) {
		removeRecipe(getRecipeByInput(component));
	}

	public static void removeRecipeByInput(String oreDictName) {
		try {
			removeRecipe(getRecipeByInput(OreDictionary.getOres(oreDictName).get(0)));
		}
		catch(Exception e) {}
	}

	public static void removeRecipeByResult(ItemStack result) {
		removeRecipe(getRecipeByResult(result));
	}

	public static void removeRecipe(ItemStack input, ItemStack result) {
		for(int i = 0; i < RECIPES.size(); ++i) {
			MithrilineFurnaceRecipe rec = RECIPES.get(i);
			if(rec != null && rec.input.apply(input) && rec.result.isItemEqual(result)) {
				removeRecipe(rec);
				return;
			}
		}
	}

	public static void removeRecipe(String input, ItemStack result) {
		try {
			for(int i = 0; i < RECIPES.size(); ++i) {
				MithrilineFurnaceRecipe rec = RECIPES.get(i);
				if(rec != null && rec.input.apply(OreDictionary.getOres(input).get(0)) && rec.result.isItemEqual(result)) {
					removeRecipe(rec);
					return;
				}
			}
		}
		catch(Exception e) {}
	}

	public static void removeRecipe(MithrilineFurnaceRecipe rec) {
		RECIPES.remove(rec);
	}

	public static MithrilineFurnaceRecipe getRecipeByInput(ItemStack is) {
		return RECIPES.stream().filter(recipe->recipe.input.apply(is)).findAny().orElse(null);
	}

	public static MithrilineFurnaceRecipe getRecipeByResult(ItemStack is) {
		return RECIPES.stream().filter(recipe->recipe.result.isItemEqual(is)).findAny().orElse(null);
	}
}
