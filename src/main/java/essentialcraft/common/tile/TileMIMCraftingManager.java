package essentialcraft.common.tile;

import java.util.ArrayList;

import essentialcraft.common.inventory.InventoryCraftingFrame;
import essentialcraft.common.item.ItemCraftingFrame;
import net.minecraft.item.ItemStack;

public class TileMIMCraftingManager extends TileMRUGeneric {

	public TileMIM parent;
	int tickTime;
	public ArrayList<CraftingPattern> allCrafts = new ArrayList<CraftingPattern>();

	public static class CraftingPattern {
		public ItemStack result = ItemStack.EMPTY;
		public ItemStack[] input = {};
		public boolean isOreDict;
		public ItemStack crafter = ItemStack.EMPTY;

		public CraftingPattern(ItemStack par1, ItemStack par2, ItemStack[] par3, boolean par4) {
			result = par1;
			crafter = par2;
			input = par3;
			isOreDict = par4;
		}

		public CraftingPattern(ItemStack crafting) {
			if(crafting != null && crafting.getItem() instanceof ItemCraftingFrame && crafting.hasTagCompound()) {
				InventoryCraftingFrame frame = new InventoryCraftingFrame(crafting);
				if(frame != null && !frame.getStackInSlot(9).isEmpty() && !frame.isEmpty()) {
					ItemStack[] is = new ItemStack[]{frame.getStackInSlot(0),frame.getStackInSlot(1),frame.getStackInSlot(2),frame.getStackInSlot(3),frame.getStackInSlot(4),frame.getStackInSlot(5),frame.getStackInSlot(6),frame.getStackInSlot(7),frame.getStackInSlot(8)};
					result = frame.getStackInSlot(9);
					crafter = crafting;
					input = is;
					isOreDict = !crafting.getTagCompound().getBoolean("ignoreOreDict");
				}
			}
		}

		public boolean isValidRecipe() {
			return !result.isEmpty() && crafter != null && input.length == 9 && !isEmpty();
		}

		public boolean isResultTheSame(ItemStack is) {
			if(is.isEmpty() || !isValidRecipe())
				return false;

			return is.isItemEqual(result) && ItemStack.areItemStackTagsEqual(is, result);
		}

		public boolean isEmpty() {
			boolean ret = true;
			for(ItemStack stk : input) {
				ret &= stk.isEmpty();
			}
			return ret;
		}
	}

	@Override
	public void setInventorySlotContents(int par1, ItemStack stack) {
		super.setInventorySlotContents(par1, stack);
		syncTick = 0;
	}

	public TileMIMCraftingManager() {
		super(0);
		setSlotsNum(54);
		slot0IsBoundGem = false;
	}

	@Override
	public void update() {
		if(syncTick == 20)
			rebuildRecipes();

		super.update();

		if(tickTime == 0) {
			tickTime = 20;
			if(parent != null)
				if(!parent.isParent(this))
					parent = null;
		}
		else
			--tickTime;
	}

	@Override
	public int[] getOutputSlots() {
		return new int[0];
	}

	public void rebuildRecipes() {
		allCrafts.clear();
		for(int i = 0; i < getSizeInventory(); ++i) {
			ItemStack is = getStackInSlot(i);
			if(!is.isEmpty()) {
				CraftingPattern p = new CraftingPattern(is);
				if(p.isValidRecipe()) {
					allCrafts.add(p);
					continue;
				}
				else
					p = null;
			}
		}
	}

	public ItemStack findCraftingFrameByRecipe(ItemStack result) {
		for(int i = 0; i < allCrafts.size(); ++i) {
			if(allCrafts.get(i).isResultTheSame(result))
				return allCrafts.get(i).crafter;
		}

		return ItemStack.EMPTY;
	}

	public ItemStack[] findCraftingComponentsByRecipe(ItemStack result) {
		for(int i = 0; i < allCrafts.size(); ++i) {
			if(allCrafts.get(i).isResultTheSame(result))
				return allCrafts.get(i).input;
		}

		return null;
	}

	public ItemStack findResultByCraftingFrame(ItemStack frame) {
		for(int i = 0; i < allCrafts.size(); ++i) {
			if(ItemStack.areItemStacksEqual(allCrafts.get(i).crafter, frame) && ItemStack.areItemStackTagsEqual(allCrafts.get(i).crafter, frame))
				return allCrafts.get(i).result;
		}

		return ItemStack.EMPTY;
	}

	public int craft(ItemStack result, int times) {
		ItemStack stk = findCraftingFrameByRecipe(result);

		if(stk.isEmpty())
			return 0;

		int crafted = 0;

		for(int i = 0; i < (times+result.getCount()-1)/result.getCount(); ++i) {
			ItemStack[] required = findCraftingComponentsByRecipe(result);
			if(canCraft(required,stk)) {
				if(craft(required,stk)) {
					parent.addItemStackToSystem(result.copy());
					crafted+=result.getCount();
				}
			}
		}

		return crafted;
	}

	public boolean canCraft(ItemStack[] components, ItemStack crafter) {
		if(crafter == null)
			return false;

		if(!crafter.hasTagCompound())
			return false;

		for(int i = 0; i < components.length; ++i) {
			if(parent.retrieveItemStackFromSystem(components[i], !crafter.getTagCompound().getBoolean("ignoreOreDict"), false) > 0)
				return false;
		}

		return true;
	}

	public boolean craft(ItemStack[] components, ItemStack crafter) {
		if(crafter == null)
			return false;

		if(!crafter.hasTagCompound())
			return false;

		for(int i = 0; i < components.length; ++i) {
			if(parent.retrieveItemStackFromSystem(components[i], !crafter.getTagCompound().getBoolean("ignoreOreDict"), true) > 0) {
				for(int j = 0; j < i; ++j) {
					parent.addItemStackToSystem(components[j]);
				}
				return false;
			}
		}

		return true;
	}

	public ArrayList<CraftingPattern> getAllRecipes() {
		return allCrafts;
	}

	public ArrayList<ItemStack> getAllResults() {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

		for(int i = 0; i < allCrafts.size(); ++i) {
			CraftingPattern par = allCrafts.get(i);
			if(par.isValidRecipe())
				ret.add(par.result);
		}

		return ret;
	}

	public boolean hasRecipe(ItemStack is) {
		if(is.isEmpty())
			return false;

		for(int i = 0; i < getSizeInventory(); ++i) {
			if(!getStackInSlot(i).isEmpty() && getStackInSlot(i).getItem() instanceof ItemCraftingFrame) {
				InventoryCraftingFrame inv = new InventoryCraftingFrame(getStackInSlot(i));
				if(inv != null) {
					if(!inv.getStackInSlot(9).isEmpty() && inv.getStackInSlot(9).isItemEqual(is) && ItemStack.areItemStackTagsEqual(is, inv.getStackInSlot(9)))
						return true;
				}
			}
		}

		return false;
	}
}
