package essentialcraft.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

import DummyCore.Utils.Notifier;
import DummyCore.Utils.UnformedItemStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class RadiatingChamberRecipes {

	public static final HashMap<List<UnformedItemStack>,List<RadiatingChamberRecipe>> RECIPES = new HashMap<List<UnformedItemStack>, List<RadiatingChamberRecipe>>();
	public static final List<List<UnformedItemStack>> CRAFT_MATRIX_LIST = new ArrayList<List<UnformedItemStack>>();

	public static RadiatingChamberRecipe getRecipeByResult(ItemStack result) {
		for(List<RadiatingChamberRecipe> recList : RECIPES.values()) {
			for(RadiatingChamberRecipe rec : recList) {
				if(rec.result.isItemEqual(result)) {
					return rec;
				}
			}
		}
		return null;
	}

	public static List<UnformedItemStack> getUnformedStkByItemStacks(ItemStack[] pair) {
		if(pair == null)
			return new ArrayList<UnformedItemStack>();

		List<ItemStack> l = Lists.<ItemStack>newArrayList(pair);
		l.removeIf(is->is==null||is.isEmpty());
		pair = l.toArray(new ItemStack[0]);

		if(pair.length<=0)
			return new ArrayList<UnformedItemStack>();

		ForLST:for(List<UnformedItemStack> lst : CRAFT_MATRIX_LIST) {
			if(lst != null && lst.size() == pair.length) {
				for(int i = 0; i < lst.size(); ++i) {
					UnformedItemStack stack = lst.get(i);
					if(stack.isEmpty() && pair[i].isEmpty()) {
						continue;
					}
					if(!stack.isEmpty() && !stack.itemStackMatches(pair[i])) {
						continue ForLST;
					}
				}
				return lst;
			}
		}
		return new ArrayList<UnformedItemStack>();
	}

	public static List<RadiatingChamberRecipe> getRecipeByCP(ItemStack[] craftingPair) {
		List<UnformedItemStack> lst = getUnformedStkByItemStacks(craftingPair);
		return RECIPES.get(lst);
	}

	public static RadiatingChamberRecipe getRecipeByCPAndBalance(ItemStack[] craftingPair, float balance) {
		List<RadiatingChamberRecipe> lst = getRecipeByCP(craftingPair);
		if(lst != null) {
			for(RadiatingChamberRecipe rec : lst) {
				if(balance < rec.upperBalanceLine && balance > rec.lowerBalanceLine) {
					return rec;
				}
			}
		}
		return null;
	}

	public static boolean addRecipe(RadiatingChamberRecipe rec) {
		try {
			UnformedItemStack[] req = new UnformedItemStack[rec.recipeItems.length];
			for(int i = 0; i < req.length;++i) {
				if(!rec.recipeItems[i].isEmpty())
					req[i] = rec.recipeItems[i].copy();
				else
					req[i] = UnformedItemStack.EMPTY;
			}
			List<RadiatingChamberRecipe> lst = null;
			if(RECIPES.keySet().stream().anyMatch(l->l.toString().equals(Arrays.asList(req).toString())))
				lst = RECIPES.entrySet().stream().filter(e->e.getKey().toString().equals(Arrays.asList(req).toString())).findAny().get().getValue();
			else
				lst = new ArrayList<RadiatingChamberRecipe>();
			lst.add(rec);
			RECIPES.put(Arrays.asList(req), lst);
			CRAFT_MATRIX_LIST.add(Arrays.asList(req));
			return true;
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+rec+" on side "+side);
			return false;
		}
	}

	public static boolean addRecipe(ItemStack[] craftingPair, ItemStack result, int mruRequired, float[] balanceBounds) {
		try {
			UnformedItemStack[] unformedStacks = new UnformedItemStack[craftingPair.length];
			for(int i = 0; i < craftingPair.length; ++i) {
				unformedStacks[i] = new UnformedItemStack(craftingPair[i]);
			}
			RadiatingChamberRecipe addedRecipe = new RadiatingChamberRecipe(unformedStacks, result, mruRequired, balanceBounds);
			return addRecipe(addedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+Arrays.toString(craftingPair)+" with the result "+result+" on side "+side);
			return false;
		}
	}

	public static boolean addRecipe(ItemStack[] craftingPair, ItemStack result, int mruRequired, float[] balanceBounds, float modifier) {
		try {
			UnformedItemStack[] unformedStacks = new UnformedItemStack[craftingPair.length];
			for(int i = 0; i < craftingPair.length; ++i) {
				unformedStacks[i] = new UnformedItemStack(craftingPair[i]);
			}
			RadiatingChamberRecipe addedRecipe = new RadiatingChamberRecipe(unformedStacks, result, mruRequired, balanceBounds, modifier);
			return addRecipe(addedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+Arrays.toString(craftingPair)+" with the result "+result+" on side "+side);
			return false;
		}
	}

	public static boolean addRecipe(UnformedItemStack[] craftingPair, ItemStack result, int mruRequired, float[] balanceBounds) {
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

	public static boolean addRecipe(UnformedItemStack[] craftingPair, ItemStack result, int mruRequired, float[] balanceBounds, float modifier) {
		try {
			RadiatingChamberRecipe addedRecipe = new RadiatingChamberRecipe(craftingPair, result, mruRequired, balanceBounds, modifier);
			return addRecipe(addedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+Arrays.toString(craftingPair)+" with the result "+result+" on side "+side);
			return false;
		}
	}

	public static boolean addRecipe(String[] craftingPair, ItemStack result, int mruRequired, float[] balanceBounds) {
		try {
			UnformedItemStack[] unformedStacks = new UnformedItemStack[craftingPair.length];
			for(int i = 0; i < craftingPair.length; ++i) {
				unformedStacks[i] = new UnformedItemStack(craftingPair[i]);
			}
			RadiatingChamberRecipe addedRecipe = new RadiatingChamberRecipe(unformedStacks, result, mruRequired, balanceBounds);
			return addRecipe(addedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+Arrays.toString(craftingPair)+" with the result "+result+" on side "+side);
			return false;
		}
	}

	public static boolean addRecipe(String[] craftingPair, ItemStack result, int mruRequired, float[] balanceBounds, float modifier) {
		try {
			UnformedItemStack[] unformedStacks = new UnformedItemStack[craftingPair.length];
			for(int i = 0; i < craftingPair.length; ++i) {
				unformedStacks[i] = new UnformedItemStack(craftingPair[i]);
			}
			RadiatingChamberRecipe addedRecipe = new RadiatingChamberRecipe(unformedStacks, result, mruRequired, balanceBounds, modifier);
			return addRecipe(addedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+Arrays.toString(craftingPair)+" with the result "+result+" on side "+side);
			return false;
		}
	}

	public static boolean addRecipe(Object[] craftingPair, ItemStack result, int mruRequired, float[] balanceBounds) {
		try {
			UnformedItemStack[] unformedStacks = new UnformedItemStack[craftingPair.length];
			for(int i = 0; i < unformedStacks.length; ++i) {
				if(craftingPair[i] instanceof UnformedItemStack)
					unformedStacks[i] = (UnformedItemStack)craftingPair[i];
				else
					unformedStacks[i] = new UnformedItemStack(craftingPair[i]);
			}
			RadiatingChamberRecipe addedRecipe = new RadiatingChamberRecipe(unformedStacks, result, mruRequired, balanceBounds);
			return addRecipe(addedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+Arrays.toString(craftingPair)+" with the result "+result+" on side "+side);
			return false;
		}
	}

	public static boolean addRecipe(Object[] craftingPair, ItemStack result, int mruRequired, float[] balanceBounds, float modifier) {
		try {
			UnformedItemStack[] unformedStacks = new UnformedItemStack[craftingPair.length];
			for(int i = 0; i < unformedStacks.length; ++i) {
				if(craftingPair[i] instanceof UnformedItemStack)
					unformedStacks[i] = (UnformedItemStack)craftingPair[i];
				else
					unformedStacks[i] = new UnformedItemStack(craftingPair[i]);
			}
			RadiatingChamberRecipe addedRecipe = new RadiatingChamberRecipe(unformedStacks, result, mruRequired, balanceBounds, modifier);
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
			UnformedItemStack[] req = new UnformedItemStack[rec.recipeItems.length];
			for(int i = 0; i < req.length;++i) {
				if(rec.recipeItems[i] != null)
					req[i] = rec.recipeItems[i].copy();
				else
					req[i] = null;
			}
			RECIPES.remove(Arrays.<UnformedItemStack>asList(req));
			CRAFT_MATRIX_LIST.remove(Arrays.<UnformedItemStack>asList(req));
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
