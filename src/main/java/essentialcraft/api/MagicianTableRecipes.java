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

public class MagicianTableRecipes {

	public static final HashMap<List<UnformedItemStack>,MagicianTableRecipe> RECIPES = new HashMap<List<UnformedItemStack>,MagicianTableRecipe>();
	public static final List<List<UnformedItemStack>> CRAFT_MATRIX_LIST = new ArrayList<List<UnformedItemStack>>();

	public static List<MagicianTableRecipe> getRecipesByComponent(ItemStack component) {
		List<MagicianTableRecipe> retLst = new ArrayList<MagicianTableRecipe>();
		for(List<UnformedItemStack> lst : CRAFT_MATRIX_LIST) {
			for(UnformedItemStack stk : lst) {
				if(!stk.isEmpty() && !component.isEmpty() && stk.itemStackMatches(component))
					retLst.add(RECIPES.get(lst));
			}
		}
		return retLst;
	}

	public static MagicianTableRecipe getRecipeByResult(ItemStack result) {
		for(MagicianTableRecipe rec : RECIPES.values()) {
			if(rec.result.isItemEqual(result)) {
				return rec;
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


	public static MagicianTableRecipe getRecipeByCP(ItemStack[] craftingPair) {
		List<UnformedItemStack> lst = getUnformedStkByItemStacks(craftingPair);
		return RECIPES.get(lst);
	}

	public static boolean addRecipe(MagicianTableRecipe rec) {
		try {
			UnformedItemStack[] req = new UnformedItemStack[rec.requiredItems.length];
			for(int i = 0; i < req.length;++i) {
				if(!rec.requiredItems[i].isEmpty())
					req[i] = rec.requiredItems[i].copy();
				else
					req[i] = UnformedItemStack.EMPTY;
			}
			RECIPES.put(Arrays.<UnformedItemStack>asList(req), rec);
			CRAFT_MATRIX_LIST.add(Arrays.<UnformedItemStack>asList(req));
			return true;
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+rec+" on side "+side);
			return false;
		}
	}

	public static boolean addRecipe(ItemStack[] craftingPair, ItemStack result, int mruRequired) {
		try {
			UnformedItemStack[] unformedStacks = new UnformedItemStack[craftingPair.length];
			for(int i = 0; i < craftingPair.length; ++i) {
				unformedStacks[i] = new UnformedItemStack(craftingPair[i]);
			}
			MagicianTableRecipe addedRecipe = new MagicianTableRecipe(unformedStacks, result, mruRequired);
			return addRecipe(addedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+Arrays.toString(craftingPair)+" with the result "+result+" on side "+side);
			return false;
		}
	}

	public static boolean addRecipe(UnformedItemStack[] craftingPair, ItemStack result, int mruRequired) {
		try {
			MagicianTableRecipe addedRecipe = new MagicianTableRecipe(craftingPair, result, mruRequired);
			return addRecipe(addedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+Arrays.toString(craftingPair)+" with the result "+result+" on side "+side);
			return false;
		}
	}

	public static boolean addRecipe(String[] craftingPair, ItemStack result, int mruRequired) {
		try {
			UnformedItemStack[] unformedStacks = new UnformedItemStack[craftingPair.length];
			for(int i = 0; i < unformedStacks.length; ++i) {
				unformedStacks[i] = new UnformedItemStack(craftingPair[i]);
			}
			MagicianTableRecipe addedRecipe = new MagicianTableRecipe(unformedStacks, result, mruRequired);
			return addRecipe(addedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+Arrays.toString(craftingPair)+" with the result "+result+" on side "+side);
			return false;
		}
	}

	public static boolean addRecipe(Object[] craftingPair, ItemStack result, int mruRequired) {
		try {
			UnformedItemStack[] unformedStacks = new UnformedItemStack[craftingPair.length];
			for(int i = 0; i < unformedStacks.length; ++i) {
				if(craftingPair[i] instanceof UnformedItemStack)
					unformedStacks[i] = (UnformedItemStack)craftingPair[i];
				else
					unformedStacks[i] = new UnformedItemStack(craftingPair[i]);
			}
			MagicianTableRecipe addedRecipe = new MagicianTableRecipe(unformedStacks, result, mruRequired);
			return addRecipe(addedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to add recipe "+Arrays.toString(craftingPair)+" with the result "+result+" on side "+side);
			return false;
		}
	}

	public static boolean removeRecipe(MagicianTableRecipe rec) {
		try {
			UnformedItemStack[] req = new UnformedItemStack[rec.requiredItems.length];
			for(int i = 0; i < req.length;++i) {
				if(rec.requiredItems[i] != null)
					req[i] = rec.requiredItems[i].copy();
				else
					req[i] = UnformedItemStack.EMPTY;
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

	public static boolean removeRecipeIS(ItemStack result) {
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

	public static boolean removeRecipeCP(ItemStack[] craftingPair) {
		try {
			MagicianTableRecipe removedRecipe = getRecipeByCP(craftingPair);
			return removeRecipe(removedRecipe);
		}
		catch(Exception e) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Notifier.notifyCustomMod("EssentialCraftAPI","Unable to remove recipe "+Arrays.toString(craftingPair)+" on side "+side);
			return false;
		}
	}
}
