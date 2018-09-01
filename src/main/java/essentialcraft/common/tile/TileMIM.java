package essentialcraft.common.tile;

import java.util.ArrayList;
import java.util.HashMap;

import essentialcraft.api.ApiCore;
import essentialcraft.common.item.ItemBoundGem;
import essentialcraft.common.tile.TileMIMCraftingManager.CraftingPattern;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.config.Configuration;

public class TileMIM extends TileMRUGeneric {

	public static int cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;

	public ArrayList<ItemStack> current = new ArrayList<ItemStack>();
	public ArrayList<CraftingPattern> crafts = new ArrayList<CraftingPattern>();
	public ArrayList<TileMIMScreen> screens = new ArrayList<TileMIMScreen>();
	public ArrayList<TileMIMCraftingManager> managers = new ArrayList<TileMIMCraftingManager>();
	int tickTime;
	boolean exporting;

	public TileMIM() {
		super(cfgMaxMRU);
		setSlotsNum(55);
	}

	@Override
	public int[] getOutputSlots() {
		return new int[0];
	}

	@Override
	public void update() {
		super.update();
		mruStorage.update(getPos(), getWorld(), getStackInSlot(0));
		rebuildAllItems();
		if(tickTime == 0) {
			tickTime = 20;
			manageScreens();
			manageCrafters();
			rebuildAllCrafts();
		}
		else {
			if(tickTime == 9) {
				for(int i = 35; i < 53; ++i) {
					ItemStack gem = getStackInSlot(i);
					if(gem != null && gem.getItem() instanceof ItemBoundGem && gem.getTagCompound() != null && gem.getTagCompound().hasKey("pos") && gem.getTagCompound().getInteger("dim") == getWorld().provider.getDimension()) {
						int[] c = ItemBoundGem.getCoords(gem);
						if(getWorld().isBlockLoaded(new BlockPos(c[0], c[1], c[2]))) {
							TileEntity t = getWorld().getTileEntity(new BlockPos(c[0], c[1], c[2]));
							if(t != null && t instanceof TileMIMExportNode)
								((TileMIMExportNode)t).exportAllPossibleItems(this);
						}
					}
				}
			}
			if(tickTime == 10) {
				for(int i = 17; i < 35; ++i) {
					ItemStack gem = getStackInSlot(i);
					if(gem != null && gem.getItem() instanceof ItemBoundGem && gem.getTagCompound() != null && gem.getTagCompound().hasKey("pos") && gem.getTagCompound().getInteger("dim") == getWorld().provider.getDimension()) {
						int[] c = ItemBoundGem.getCoords(gem);
						if(getWorld().isBlockLoaded(new BlockPos(c[0], c[1], c[2]))) {
							TileEntity t = getWorld().getTileEntity(new BlockPos(c[0], c[1], c[2]));
							if(t != null && t instanceof TileMIMImportNode)
								((TileMIMImportNode)t).importAllPossibleItems(this);
						}
					}
				}
			}
			--tickTime;
		}
	}

	public void manageScreens() {
		screens.clear();
		for(int i = 7; i < 11; ++i) {
			ItemStack gem = getStackInSlot(i);
			if(gem != null && gem.getItem() instanceof ItemBoundGem && gem.getTagCompound() != null && gem.getTagCompound().hasKey("pos") && gem.getTagCompound().getInteger("dim") == getWorld().provider.getDimension()) {
				int[] c = ItemBoundGem.getCoords(gem);
				if(getWorld().isBlockLoaded(new BlockPos(c[0], c[1], c[2]))) {
					TileEntity t = getWorld().getTileEntity(new BlockPos(c[0], c[1], c[2]));
					if(t != null && t instanceof TileMIMScreen) {
						((TileMIMScreen)t).parent = this;
						screens.add((TileMIMScreen)t);
					}
				}
			}
		}
	}

	public void manageCrafters() {
		managers.clear();
		for(int i = 11; i < 17; ++i) {
			ItemStack gem = getStackInSlot(i);
			if(gem != null && gem.getItem() instanceof ItemBoundGem && gem.getTagCompound() != null && gem.getTagCompound().hasKey("pos") && gem.getTagCompound().getInteger("dim") == getWorld().provider.getDimension()) {
				int[] c = ItemBoundGem.getCoords(gem);
				if(getWorld().isBlockLoaded(new BlockPos(c[0], c[1], c[2]))) {
					TileEntity t = getWorld().getTileEntity(new BlockPos(c[0], c[1], c[2]));
					if(t != null && t instanceof TileMIMCraftingManager) {
						((TileMIMCraftingManager)t).parent = this;
						managers.add((TileMIMCraftingManager)t);
					}
				}
			}
		}
	}

	/**
	 * Tries to insert the given ItemStack to all available inventories.
	 * @param is - the itemstack to insert. Can be null, false will then be returned
	 * @return true if the operation was sucessfull, and the Inserter MUST set the ItemStack to null. False otherwise. Note, that the ItemStack may have been changed(the stacksize)
	 */
	public boolean addItemStackToSystem(ItemStack is) {
		if(is == null)
			return false;

		for(int i = 1; i < 7; ++i) {
			ItemStack gem = getStackInSlot(i);
			if(gem != null && gem.getItem() instanceof ItemBoundGem && gem.getTagCompound() != null && gem.getTagCompound().hasKey("pos") && gem.getTagCompound().getInteger("dim") == getWorld().provider.getDimension()) {
				int[] c = ItemBoundGem.getCoords(gem);
				if(getWorld().isBlockLoaded(new BlockPos(c[0], c[1], c[2]))) {
					TileEntity t = getWorld().getTileEntity(new BlockPos(c[0], c[1], c[2]));
					if(t != null && t instanceof TileMIMInventoryStorage) {
						if(((TileMIMInventoryStorage)t).insertItemStack(is))
							return true;
						else
							continue;
					}
				}
			}
		}
		return false;
	}

	public ArrayList<CraftingPattern> getAllCrafts() {
		return crafts;
	}

	public ArrayList<CraftingPattern> getCraftsByName(String namePart) {
		ArrayList<CraftingPattern> retLst = new ArrayList<CraftingPattern>();

		for(int i = 0; i < crafts.size(); ++i) {
			ItemStack stk = crafts.get(i).result;
			if(stk.getDisplayName().toLowerCase().contains(namePart.toLowerCase()))
				retLst.add(crafts.get(i));
		}

		return retLst;
	}

	public int craftFromTheSystem(ItemStack is, int times) {
		if(is == null || times < 1)
			return 0;

		for(int i = 11; i < 17; ++i) {
			ItemStack gem = getStackInSlot(i);
			if(gem != null && gem.getItem() instanceof ItemBoundGem && gem.getTagCompound() != null && gem.getTagCompound().hasKey("pos") && gem.getTagCompound().getInteger("dim") == getWorld().provider.getDimension()) {
				int[] c = ItemBoundGem.getCoords(gem);
				if(getWorld().isBlockLoaded(new BlockPos(c[0], c[1], c[2]))) {
					TileEntity t = getWorld().getTileEntity(new BlockPos(c[0], c[1], c[2]));
					if(t != null && t instanceof TileMIMCraftingManager) {
						TileMIMCraftingManager cm = (TileMIMCraftingManager)t;
						int crafted = cm.craft(is, times);
						times -= crafted;

						if(times <= 0)
							break;
					}
				}
			}
		}

		return times;
	}

	public int retrieveItemStackFromSystem(ItemStack is, boolean oreDict,boolean doRetrieve) {
		if(is.isEmpty())
			return 0;

		int left = is.getCount();
		int oldSize = is.getCount();

		for(int i = 1; i < 7; ++i) {
			ItemStack gem = getStackInSlot(i);
			if(gem != null && gem.getItem() instanceof ItemBoundGem && gem.getTagCompound() != null && gem.getTagCompound().hasKey("pos") && gem.getTagCompound().getInteger("dim") == getWorld().provider.getDimension()) {
				int[] c = ItemBoundGem.getCoords(gem);
				if(getWorld().isBlockLoaded(new BlockPos(c[0], c[1], c[2]))) {
					TileEntity t = getWorld().getTileEntity(new BlockPos(c[0], c[1], c[2]));
					if(t != null && t instanceof TileMIMInventoryStorage) {
						int newLeft = ((TileMIMInventoryStorage)t).retrieveStack(is,oreDict,doRetrieve);
						if(newLeft == 0) {
							if(!doRetrieve)
								is.setCount(oldSize);
							return 0;
						}
						else {
							left = newLeft;
							is.setCount(newLeft);
							continue;
						}
					}
				}
			}
		}

		return left;
	}

	public boolean isParent(TileMIMScreen screen) {
		return screens.contains(screen);
	}

	public boolean isParent(TileMIMCraftingManager manager) {
		return managers.contains(manager);
	}

	public ArrayList<ItemStack> getItemsByName(String namePart) {
		ArrayList<ItemStack> retLst = new ArrayList<ItemStack>();

		for(int i = 0; i < current.size(); ++i) {
			ItemStack stk = current.get(i);
			if(stk.getDisplayName().toLowerCase().contains(namePart.toLowerCase()))
				retLst.add(stk);
		}

		return retLst;
	}

	public ArrayList<ItemStack> getAllItems() {
		ArrayList<ItemStack> retLst = new ArrayList<ItemStack>();
		retLst.addAll(current);
		return retLst;
	}

	public void openAllStorages(EntityPlayer p) {
		for(int i = 1; i < 7; ++i) {
			ItemStack gem = getStackInSlot(i);
			if(gem != null && gem.getItem() instanceof ItemBoundGem && gem.getTagCompound() != null && gem.getTagCompound().hasKey("pos") && gem.getTagCompound().getInteger("dim") == getWorld().provider.getDimension()) {
				int[] c = ItemBoundGem.getCoords(gem);
				if(getWorld().isBlockLoaded(new BlockPos(c[0], c[1], c[2]))) {
					TileEntity t = getWorld().getTileEntity(new BlockPos(c[0], c[1], c[2]));
					if(t != null && t instanceof TileMIMInventoryStorage) {
						((TileMIMInventoryStorage)t).openInventory(p);
					}
				}
			}
		}
	}

	public void closeAllStorages(EntityPlayer p) {
		for(int i = 1; i < 7; ++i) {
			ItemStack gem = getStackInSlot(i);
			if(gem != null && gem.getItem() instanceof ItemBoundGem && gem.getTagCompound() != null && gem.getTagCompound().hasKey("pos") && gem.getTagCompound().getInteger("dim") == getWorld().provider.getDimension()) {
				int[] c = ItemBoundGem.getCoords(gem);
				if(getWorld().isBlockLoaded(new BlockPos(c[0], c[1], c[2]))) {
					TileEntity t = getWorld().getTileEntity(new BlockPos(c[0], c[1], c[2]));
					if(t != null && t instanceof TileMIMInventoryStorage)
						((TileMIMInventoryStorage)t).closeInventory(p);
				}
			}
		}
	}

	public void rebuildAllItems() {
		current.clear();
		HashMap<String,Integer> allItems = new HashMap<String,Integer>();
		HashMap<String,ItemStack> foundByID = new HashMap<String,ItemStack>();
		ArrayList<String> ids = new ArrayList<String>();

		for(int i = 1; i < 7; ++i) {
			ItemStack gem = getStackInSlot(i);
			if(gem != null && gem.getItem() instanceof ItemBoundGem && gem.getTagCompound() != null && gem.getTagCompound().hasKey("pos") && gem.getTagCompound().getInteger("dim") == getWorld().provider.getDimension()) {
				int[] c = ItemBoundGem.getCoords(gem);
				if(getWorld().isBlockLoaded(new BlockPos(c[0], c[1], c[2]))) {
					TileEntity t = getWorld().getTileEntity(new BlockPos(c[0], c[1], c[2]));
					if(t != null && t instanceof TileMIMInventoryStorage) {
						ArrayList<ItemStack> items = ((TileMIMInventoryStorage)t).getAllItems();
						for(int j = 0; j < items.size(); ++j) {
							ItemStack itm = items.get(j);
							if(itm != null) {
								String id = itm.getItem().getRegistryName().toString() + "@" + itm.getItemDamage();
								if(itm.getTagCompound() == null || itm.getTagCompound().hasNoTags()) {
									if(allItems.containsKey(id))
										allItems.put(id, allItems.get(id)+itm.getCount());
									else {
										allItems.put(id, itm.getCount());
										foundByID.put(id, itm);
										ids.add(id);
									}
								}
								else
									current.add(itm.copy());
							}
						}
					}
				}
			}
		}

		for(int i = 0; i < ids.size(); ++i) {
			ItemStack cID = foundByID.get(ids.get(i)).copy();
			cID.setCount(allItems.get(ids.get(i)));
			current.add(cID);
		}

		allItems.clear();
		foundByID.clear();
		ids.clear();

		allItems = null;
		foundByID = null;
		ids = null;
	}

	public void rebuildAllCrafts() {
		crafts.clear();
		for(int i = 11; i < 17; ++i) {
			ItemStack gem = getStackInSlot(i);
			if(gem != null && gem.getItem() instanceof ItemBoundGem && gem.getTagCompound() != null && gem.getTagCompound().hasKey("pos") && gem.getTagCompound().getInteger("dim") == getWorld().provider.getDimension()) {
				int[] c = ItemBoundGem.getCoords(gem);
				if(getWorld().isBlockLoaded(new BlockPos(c[0], c[1], c[2]))) {
					TileEntity t = getWorld().getTileEntity(new BlockPos(c[0], c[1], c[2]));
					if(t != null && t instanceof TileMIMCraftingManager)
						crafts.addAll(((TileMIMCraftingManager)t).getAllRecipes());
				}
			}
		}
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.magicalinventorymanager";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.DEVICE_MAX_MRU_GENERIC).setMinValue(1).getInt();
		}
		catch(Exception e) {
			return;
		}
	}
}
