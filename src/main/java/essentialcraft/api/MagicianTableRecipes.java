package essentialcraft.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

import DummyCore.Utils.IngredientUtils;
import DummyCore.Utils.Notifier;
import DummyCore.Utils.UnformedItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreIngredient;

public class MagicianTableRecipes {

	public static final List<MagicianTableRecipe> RECIPES = Lists.newArrayList();

	public static List<MagicianTableRecipe> getRecipesByComponent(ItemStack component) {
		List<MagicianTableRecipe> retLst = new ArrayList<MagicianTableRecipe>();
		for(MagicianTableRecipe rec : RECIPES) {
			for(Ingredient ing : rec.requiredItems) {
				if(ing.apply(component)) {
					retLst.add(rec);
				}
			}
		}
		return retLst;
	}

	public static MagicianTableRecipe getRecipeByResult(ItemStack result) {
		for(MagicianTableRecipe rec : RECIPES) {
			if(rec.result.isItemEqual(result)) {
				return rec;
			}
		}
		return null;
	}

	public static MagicianTableRecipe getRecipeByInput(ItemStack[] input) {
		for(MagicianTableRecipe rec : RECIPES) {
			if(rec.matches(input)) {
				return rec;
			}
		}
		return null;
	}

	public static boolean addRecipe(MagicianTableRecipe rec) {
		try {
			RECIPES.add(rec);
			return true;
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+rec+" on side "+side);
			return false;
		}
	}

	public static boolean addRecipe(Ingredient[] input, ItemStack result, int mruRequired) {
		try {
			MagicianTableRecipe addedRecipe = new MagicianTableRecipe(input, result, mruRequired);
			return addRecipe(addedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+Arrays.toString(input)+" with the result "+result+" on side "+side);
			return false;
		}
	}

	public static boolean addRecipe(ItemStack[] input, ItemStack result, int mruRequired) {
		try {
			Ingredient[] ingredients = new Ingredient[input.length];
			for(int i = 0; i < input.length; ++i) {
				ingredients[i] = Ingredient.fromStacks(input[i]);
			}
			MagicianTableRecipe addedRecipe = new MagicianTableRecipe(ingredients, result, mruRequired);
			return addRecipe(addedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+Arrays.toString(input)+" with the result "+result+" on side "+side);
			return false;
		}
	}

	public static boolean addRecipe(UnformedItemStack[] input, ItemStack result, int mruRequired) {
		try {
			Ingredient[] ingredients = new Ingredient[input.length];
			for(int i = 0; i < input.length; ++i) {
				ingredients[i] = IngredientUtils.getIngredient(input[i]);
			}
			MagicianTableRecipe addedRecipe = new MagicianTableRecipe(ingredients, result, mruRequired);
			return addRecipe(addedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+Arrays.toString(input)+" with the result "+result+" on side "+side);
			return false;
		}
	}

	public static boolean addRecipe(String[] input, ItemStack result, int mruRequired) {
		try {
			Ingredient[] ingredients = new Ingredient[input.length];
			for(int i = 0; i < ingredients.length; ++i) {
				ingredients[i] = new OreIngredient(input[i]);
			}
			MagicianTableRecipe addedRecipe = new MagicianTableRecipe(ingredients, result, mruRequired);
			return addRecipe(addedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+Arrays.toString(input)+" with the result "+result+" on side "+side);
			return false;
		}
	}

	public static boolean addRecipe(Object[] input, ItemStack result, int mruRequired) {
		try {
			Ingredient[] ingredients = new Ingredient[input.length];
			for(int i = 0; i < ingredients.length; ++i) {
				ingredients[i] = IngredientUtils.getIngredient(input[i]);
			}
			MagicianTableRecipe addedRecipe = new MagicianTableRecipe(ingredients, result, mruRequired);
			return addRecipe(addedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+Arrays.toString(input)+" with the result "+result+" on side "+side);
			return false;
		}
	}

	public static boolean removeRecipe(MagicianTableRecipe rec) {
		try {
			RECIPES.remove(rec);
			return true;
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to remove recipe "+rec+" on side "+side);
			return false;
		}
	}

	public static boolean removeRecipeByResult(ItemStack result) {
		try {
			MagicianTableRecipe removedRecipe = getRecipeByResult(result);
			return removeRecipe(removedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to remove recipe with the result "+result+" on side "+side);
			return false;
		}
	}

	public static boolean removeRecipeByInput(ItemStack[] input) {
		try {
			MagicianTableRecipe removedRecipe = getRecipeByInput(input);
			return removeRecipe(removedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to remove recipe "+Arrays.toString(input)+" on side "+side);
			return false;
		}
	}
}
