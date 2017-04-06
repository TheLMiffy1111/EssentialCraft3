package ec3.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import DummyCore.Utils.Notifier;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraft.item.ItemStack;

public class RadiatingChamberRecipes {

	public static final Hashtable<String,List<RadiatingChamberRecipe>> recipes = new Hashtable<String, List<RadiatingChamberRecipe>>();
	public static final List<String> craftMatrixByID = new ArrayList<String>();

	public static RadiatingChamberRecipe getRecipeByResult(ItemStack result) {
		for(List<RadiatingChamberRecipe> recList : recipes.values()) {
			for(RadiatingChamberRecipe rec : recList) {
				if(rec.result.isItemEqual(result))
					return rec;
			}
		}
		return null;
	}

	public static List<RadiatingChamberRecipe> getRecipeByCP(ItemStack[] craftingPair) {
		ItemStack[] req = new ItemStack[craftingPair.length];
		for(int i = 0; i < req.length;++i)
		{
			if(craftingPair[i] != null)
				req[i] = craftingPair[i].copy();
			else
				req[i] = null;
		}
		for(int i = 0; i < req.length; ++i)
		{
			if(req[i] != null)
				req[i].stackSize = 0;
		}
		String searchStr = Arrays.toString(req);
		req = null;
		return recipes.get(searchStr);
	}

	public static RadiatingChamberRecipe getRecipeByCPAndBalance(ItemStack[] craftingPair, float balance) {
		List<RadiatingChamberRecipe> lst = getRecipeByCP(craftingPair);
		if(lst != null && !lst.isEmpty()) {
			for(int i = 0; i < lst.size(); ++i) {
				RadiatingChamberRecipe rec = lst.get(i);
				if(balance < rec.upperBalanceLine && balance > rec.lowerBalanceLine) {
					return rec;
				}
			}
		}
		return null;
	}

	public static boolean addRecipe(RadiatingChamberRecipe rec) {
		try {
			ItemStack[] req = new ItemStack[rec.recipeItems.length];
			for(int i = 0; i < req.length;++i) {
				if(rec.recipeItems[i] != null)
					req[i] = rec.recipeItems[i].copy();
				else
					req[i] = null;
			}
			for(int i = 0; i < req.length; ++i) {
				if(req[i] != null)
					req[i].stackSize = 0;
			}
			List<RadiatingChamberRecipe> lst = null;
			if(recipes.containsKey(Arrays.toString(req)))
				lst = recipes.get(Arrays.toString(req));
			else
				lst = new ArrayList<RadiatingChamberRecipe>();
			lst.add(rec);
			recipes.put(Arrays.toString(req), lst);
			ItemStack result = rec.result.copy();
			result.stackSize = 0;
			result = null;
			craftMatrixByID.add(Arrays.toString(req));
			req = null;
			return true;
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+rec+" on side "+side);
			return false;
		}
	}

	public static boolean addRecipeIS(ItemStack[] craftingPair, ItemStack result, int mruRequired, float[] balanceBounds, int size) {
		try {
			RadiatingChamberRecipe addedRecipe = new RadiatingChamberRecipe(craftingPair, result, mruRequired, balanceBounds, size);
			return addRecipe(addedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+Arrays.toString(craftingPair)+" with the result "+result+" on side "+side);
			return false;
		}
	}

	public static boolean addRecipeIS(ItemStack[] craftingPair, ItemStack result, int mruRequired, float[] balanceBounds, float m, int size) {
		try {
			RadiatingChamberRecipe addedRecipe = new RadiatingChamberRecipe(craftingPair, result, mruRequired, balanceBounds, m, size);
			return addRecipe(addedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+Arrays.toString(craftingPair)+" with the result "+result+" on side "+side);
			return false;
		}
	}

	public static boolean addRecipeIS(ItemStack[] craftingPair, ItemStack result, int mruRequired, float[] balanceBounds) {
		try {
			RadiatingChamberRecipe addedRecipe = new RadiatingChamberRecipe(craftingPair, result, mruRequired, balanceBounds);
			return addRecipe(addedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+Arrays.toString(craftingPair)+" with the result "+result+" on side "+side);
			return false;
		}
	}

	public static boolean addRecipeIS(ItemStack[] craftingPair, ItemStack result, int mruRequired, float[] balanceBounds, float m) {
		try {
			RadiatingChamberRecipe addedRecipe = new RadiatingChamberRecipe(craftingPair, result, mruRequired, balanceBounds, m);
			return addRecipe(addedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+Arrays.toString(craftingPair)+" with the result "+result+" on side "+side);
			return false;
		}
	}
	
	public static boolean removeRecipe(RadiatingChamberRecipe rec) {
		try {
			ItemStack[] req = new ItemStack[rec.recipeItems.length];
			for(int i = 0; i < req.length;++i) {
				if(rec.recipeItems[i] != null)
					req[i] = rec.recipeItems[i].copy();
				else
					req[i] = null;
			}
			for(int i = 0; i < req.length; ++i) {
				if(req[i] != null)
					req[i].stackSize = 0;
			}
			recipes.remove(Arrays.toString(req));
			ItemStack result = rec.result.copy();
			result.stackSize = 0;
			result = null;
			craftMatrixByID.remove(Arrays.toString(req));
			req = null;
			return true;
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to remove recipe "+rec+" on side "+side);
			return false;
		}
	}
	
	public static boolean removeRecipeCP(ItemStack[] craftingPair) {
		try {
			for(RadiatingChamberRecipe removedRecipe : getRecipeByCP(craftingPair)) {
				removeRecipe(removedRecipe);
			}
			return true;
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to remove recipe "+Arrays.toString(craftingPair)+" on side "+side);
			return false;
		}
	}
	
	public static boolean removeRecipeCP(ItemStack[] craftingPair, float balance) {
		try {
			RadiatingChamberRecipe removedRecipe = getRecipeByCPAndBalance(craftingPair, balance);
			return removeRecipe(removedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to remove recipe "+Arrays.toString(craftingPair)+" on side "+side);
			return false;
		}
	}
	
	public static boolean removeRecipeIS(ItemStack result) {
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
