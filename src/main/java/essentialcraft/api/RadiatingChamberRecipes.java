package essentialcraft.api;

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

public class RadiatingChamberRecipes {

	public static final List<RadiatingChamberRecipe> RECIPES = Lists.newArrayList();

	public static RadiatingChamberRecipe getRecipeByResult(ItemStack result) {
		for(RadiatingChamberRecipe rec : RECIPES) {
			if(rec.result.isItemEqual(result)) {
				return rec;
			}
		}
		return null;
	}

	public static List<RadiatingChamberRecipe> getRecipesByInput(ItemStack[] input) {
		List<RadiatingChamberRecipe> ret = Lists.newArrayList();
		for(RadiatingChamberRecipe rec : RECIPES) {
			if(rec.matches(input)) {
				ret.add(rec);
			}
		}
		return ret;
	}

	public static RadiatingChamberRecipe getRecipeByInputAndBalance(ItemStack[] input, float balance) {
		for(RadiatingChamberRecipe rec : RECIPES) {
			if(rec.matches(input, balance)) {
				return rec;
			}
		}
		return null;
	}

	public static boolean addRecipe(RadiatingChamberRecipe rec) {
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

	public static boolean addRecipe(Ingredient[] input, ItemStack result, int mruRequired, float balanceBound1, float balanceBound2) {
		try {
			RadiatingChamberRecipe addedRecipe = new RadiatingChamberRecipe(input, result, mruRequired, balanceBound1, balanceBound2);
			return addRecipe(addedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+Arrays.toString(input)+" with the result "+result+" on side "+side);
			return false;
		}
	}

	public static boolean addRecipe(Ingredient[] input, ItemStack result, int mruRequired, float balanceBound1, float balanceBound2, float modifier) {
		try {
			RadiatingChamberRecipe addedRecipe = new RadiatingChamberRecipe(input, result, mruRequired, balanceBound1, balanceBound2, modifier);
			return addRecipe(addedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+Arrays.toString(input)+" with the result "+result+" on side "+side);
			return false;
		}
	}

	public static boolean addRecipe(ItemStack[] input, ItemStack result, int mruRequired, float balanceBound1, float balanceBound2) {
		try {
			Ingredient[] ingredients = new Ingredient[input.length];
			for(int i = 0; i < input.length; ++i) {
				ingredients[i] = Ingredient.fromStacks(input[i].copy());
			}
			RadiatingChamberRecipe addedRecipe = new RadiatingChamberRecipe(ingredients, result, mruRequired, balanceBound1, balanceBound2);
			return addRecipe(addedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+Arrays.toString(input)+" with the result "+result+" on side "+side);
			return false;
		}
	}

	public static boolean addRecipe(ItemStack[] input, ItemStack result, int mruRequired, float balanceBound1, float balanceBound2, float modifier) {
		try {
			Ingredient[] ingredients = new Ingredient[input.length];
			for(int i = 0; i < input.length; ++i) {
				ingredients[i] = Ingredient.fromStacks(input[i].copy());
			}
			RadiatingChamberRecipe addedRecipe = new RadiatingChamberRecipe(ingredients, result, mruRequired, balanceBound1, balanceBound2, modifier);
			return addRecipe(addedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+Arrays.toString(input)+" with the result "+result+" on side "+side);
			return false;
		}
	}

	public static boolean addRecipe(UnformedItemStack[] input, ItemStack result, int mruRequired, float balanceBound1, float balanceBound2) {
		try {
			Ingredient[] ingredients = new Ingredient[input.length];
			for(int i = 0; i < input.length; ++i) {
				ingredients[i] = IngredientUtils.getIngredient(input[i]);
			}
			RadiatingChamberRecipe addedRecipe = new RadiatingChamberRecipe(ingredients, result, mruRequired, balanceBound1, balanceBound2);
			return addRecipe(addedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+Arrays.toString(input)+" with the result "+result+" on side "+side);
			return false;
		}
	}

	public static boolean addRecipe(UnformedItemStack[] input, ItemStack result, int mruRequired, float balanceBound1, float balanceBound2, float modifier) {
		try {
			Ingredient[] ingredients = new Ingredient[input.length];
			for(int i = 0; i < input.length; ++i) {
				ingredients[i] = IngredientUtils.getIngredient(input[i]);
			}
			RadiatingChamberRecipe addedRecipe = new RadiatingChamberRecipe(ingredients, result, mruRequired, balanceBound1, balanceBound2, modifier);
			return addRecipe(addedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+Arrays.toString(input)+" with the result "+result+" on side "+side);
			return false;
		}
	}

	public static boolean addRecipe(String[] input, ItemStack result, int mruRequired, float balanceBound1, float balanceBound2) {
		try {
			Ingredient[] ingredients = new Ingredient[input.length];
			for(int i = 0; i < input.length; ++i) {
				ingredients[i] = new OreIngredient(input[i]);
			}
			RadiatingChamberRecipe addedRecipe = new RadiatingChamberRecipe(ingredients, result, mruRequired, balanceBound1, balanceBound2);
			return addRecipe(addedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+Arrays.toString(input)+" with the result "+result+" on side "+side);
			return false;
		}
	}

	public static boolean addRecipe(String[] input, ItemStack result, int mruRequired, float balanceBound1, float balanceBound2, float modifier) {
		try {
			Ingredient[] ingredients = new Ingredient[input.length];
			for(int i = 0; i < input.length; ++i) {
				ingredients[i] = new OreIngredient(input[i]);
			}
			RadiatingChamberRecipe addedRecipe = new RadiatingChamberRecipe(ingredients, result, mruRequired, balanceBound1, balanceBound2, modifier);
			return addRecipe(addedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+Arrays.toString(input)+" with the result "+result+" on side "+side);
			return false;
		}
	}

	public static boolean addRecipe(Object[] input, ItemStack result, int mruRequired, float balanceBound1, float balanceBound2) {
		try {
			Ingredient[] ingredients = new Ingredient[input.length];
			for(int i = 0; i < input.length; ++i) {
				ingredients[i] = IngredientUtils.getIngredient(input[i]);
			}
			RadiatingChamberRecipe addedRecipe = new RadiatingChamberRecipe(ingredients, result, mruRequired, balanceBound1, balanceBound2);
			return addRecipe(addedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+Arrays.toString(input)+" with the result "+result+" on side "+side);
			return false;
		}
	}

	public static boolean addRecipe(Object[] input, ItemStack result, int mruRequired, float balanceBound1, float balanceBound2, float modifier) {
		try {
			Ingredient[] ingredients = new Ingredient[input.length];
			for(int i = 0; i < input.length; ++i) {
				ingredients[i] = IngredientUtils.getIngredient(input[i]);
			}
			RadiatingChamberRecipe addedRecipe = new RadiatingChamberRecipe(ingredients, result, mruRequired, balanceBound1, balanceBound2, modifier);
			return addRecipe(addedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+Arrays.toString(input)+" with the result "+result+" on side "+side);
			return false;
		}
	}

	public static boolean removeRecipe(RadiatingChamberRecipe rec) {
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

	public static boolean removeRecipeByInput(ItemStack[] input) {
		try {
			for(RadiatingChamberRecipe removedRecipe : getRecipesByInput(input)) {
				removeRecipe(removedRecipe);
			}
			return true;
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to remove recipe "+Arrays.toString(input)+" on side "+side);
			return false;
		}
	}

	public static boolean removeRecipeByInput(ItemStack[] input, float balance) {
		try {
			RadiatingChamberRecipe removedRecipe = getRecipeByInputAndBalance(input, balance);
			return removeRecipe(removedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to remove recipe "+Arrays.toString(input)+" on side "+side);
			return false;
		}
	}

	public static boolean removeRecipeByResult(ItemStack result) {
		try {
			RadiatingChamberRecipe removedRecipe = getRecipeByResult(result);
			return removeRecipe(removedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to remove recipe with result"+ result +"on side "+side);
			return false;
		}
	}
}
