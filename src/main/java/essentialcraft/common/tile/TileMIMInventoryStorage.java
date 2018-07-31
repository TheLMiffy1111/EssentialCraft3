package essentialcraft.common.tile;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.tuple.Pair;

import DummyCore.Utils.MiscUtils;
import essentialcraft.common.item.ItemInventoryGem;
import essentialcraft.common.item.ItemsCore;
import essentialcraft.common.mod.EssentialCraftCore;
import essentialcraft.network.PacketNBT;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class TileMIMInventoryStorage extends TileMRUGeneric {

	public int updateTime = 0;
	ArrayList<Pair<BlockPos,IItemHandler>> counted = new ArrayList<Pair<BlockPos,IItemHandler>>();
	ArrayList<IItemHandler> countedT = new ArrayList<IItemHandler>();
	public ArrayList<ItemStack> items = new ArrayList<ItemStack>();
	ArrayList<EntityPlayerMP> plrs = new ArrayList<EntityPlayerMP>();
	boolean requireSync = false;
	final Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

	public TileMIMInventoryStorage() {
		super(0);
		setSlotsNum(54);
		slot0IsBoundGem = false;
	}

	@Override
	public int[] getOutputSlots() {
		return new int[0];
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return stack.getItem() == ItemsCore.inventoryGem;
	}

	/**
	 *
	 * @return A full list of all Inventories available for the device. Will only return valid tiles of existing blocks
	 */
	public ArrayList<IItemHandler> getInventories() {
		ArrayList<IItemHandler> retLst = new ArrayList<IItemHandler>();

		for(Pair<BlockPos,IItemHandler> p : counted) {
			if(getWorld().isBlockLoaded(new BlockPos(p.getLeft())) && getWorld().getTileEntity(new BlockPos(p.getLeft())).hasCapability(ITEM_HANDLER_CAPABILITY, null))
				retLst.add(p.getRight());
		}

		return retLst;
	}

	/**
	 * Decreases the stack sizes in possible inventories, until either the stacksize was satisfied, or there are no more stacks of that type in inventories. Also calls a full rebuild on inventories and stacks. Has a null check for inventories. Returns a stk.stackSize if the desired stack was not found.
	 * @param stk the stack to pull. Stacksize unbound.
	 * @return 0 if the pull was successful, anything greater than 0, but lower than stk.stackSize if the request was not fully satisfied.
	 */
	public int retrieveStack(ItemStack stk, boolean oreDict, boolean actuallyRetrieve) {
		int index = -1;
		for(int i = 0; i < items.size(); ++i) {
			ItemStack is = items.get(i);
			if(!is.isEmpty()) {
				if(is.isItemEqual(stk) && ItemStack.areItemStackTagsEqual(stk, is) || oreDict && ECUtils.oreDictionaryCompare(stk, is)) {
					index = i;
					break;
				}
			}
		}
		if(stk.getCount() == 0)
			stk.setCount(1);

		int ret = stk.getCount();
		if(index != -1) {
			fG:for(int i = 0; i < countedT.size(); ++i) {
				if(countedT.get(i) == null)
					continue;

				for(int j = 0; j < countedT.get(i).getSlots(); ++j) {
					ItemStack s = countedT.get(i).getStackInSlot(j).copy();
					if(!s.isEmpty() && s.isItemEqual(stk) && ItemStack.areItemStackTagsEqual(stk, s) || oreDict && ECUtils.oreDictionaryCompare(stk, s)) {
						if(ret >= s.getCount()) {
							if(actuallyRetrieve)
								countedT.get(i).extractItem(j, ret, false);

							ret -= s.getCount();

							if(ret < 1)
								break fG;

							continue;
						}
						else {
							if(actuallyRetrieve) {
								countedT.get(i).extractItem(j, ret, false);
							}

							ret = 0;
							break fG;
						}
					}
				}
			}
		}

		updateTime = 0;
		requireSync = true;
		return ret;
	}

	/**
	 *
	 * @return A list of all items there are.
	 */
	public ArrayList<ItemStack> getAllItems() {
		return items;
	}

	/**
	 *
	 * @param namePart - a name part to search the items. Not case-sensitive!
	 * @return A list of items matching the name.
	 */
	public ArrayList<ItemStack> getItemsByName(String namePart) {
		ArrayList<ItemStack> retLst = new ArrayList<ItemStack>();

		for(int i = 0; i < items.size(); ++i) {
			ItemStack stk = items.get(i);
			if(stk.getDisplayName().contains(namePart.toLowerCase()))
				retLst.add(stk);
		}

		return retLst;
	}

	/**
	 * Tries to add a given ItemStack to all linked inventories. Will not try to increase sizes first - sadly optimization is more important here.
	 * @param is - the itemstack to insert. Can be null, will return false then.
	 * @return true if the ItemStack was inserted, false if not, or practically not.
	 */
	public boolean insertItemStack(ItemStack is) {
		if(is.isEmpty())
			return false;

		for(int i = 0; i < counted.size(); ++i) {
			BlockPos coords = counted.get(i).getLeft();
			IItemHandler inv = counted.get(i).getRight();

			if(!getWorld().isBlockLoaded(new BlockPos(coords)))
				continue;

			if(inv == null)
				continue;

			for(int j = 0; j < inv.getSlots(); ++j) {
				ItemStack stk = inv.getStackInSlot(j);
				if(!stk.isEmpty()) {
					if(ItemHandlerHelper.canItemStacksStack(stk, is)) {
						if(stk.getCount() < stk.getMaxStackSize()) {
							if(stk.getCount()+is.getCount() <= stk.getMaxStackSize()) {
								inv.insertItem(j, is.copy(), false);
								return true;
							}
							else {
								is = inv.insertItem(j, is.copy(), false).copy();
								continue;
							}
						}
					}
				}
			}
			for(int j = 0; j < inv.getSlots(); ++j) {
				ItemStack stk = inv.getStackInSlot(j);
				if(stk.isEmpty() && inv.insertItem(j, is, true).isEmpty()) {
					inv.insertItem(j, is.copy(), false);
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Re-calculates all the items there are. I wish there would be a better way to do this. Especially, if I didn't have to do this every tick, since it is pretty resource-intensive. However, if I do not do this every tick then dupes are possible.
	 */
	public void rebuildItems() {
		HashMap<String,Integer> found = new HashMap<String,Integer>();
		HashMap<String,ItemStack> foundByID = new HashMap<String,ItemStack>();
		ArrayList<String> ids = new ArrayList<String>();
		ArrayList<ItemStack> oldCopy = new ArrayList<ItemStack>();
		oldCopy.addAll(items);

		items.clear();
		for(int i = 0; i < countedT.size(); ++i) {
			if(countedT.get(i) == null)
				continue;

			for(int j = 0; j < countedT.get(i).getSlots(); ++j) {
				ItemStack stk = countedT.get(i).getStackInSlot(j);
				if(!stk.isEmpty() && stk.getCount() > 0) {
					String id = stk.getItem().getRegistryName().toString() + "@" + stk.getItemDamage();
					if(stk.getTagCompound() == null || stk.getTagCompound().hasNoTags()) {
						if(found.containsKey(id))
							found.put(id, found.get(id) + stk.getCount());
						else {
							found.put(id, stk.getCount());
							foundByID.put(id, stk);
							ids.add(id);
						}
					}
					else
						items.add(stk.copy());
				}
			}
		}

		for(int i = 0; i < ids.size(); ++i) {
			ItemStack cID = foundByID.get(ids.get(i)).copy();
			cID.setCount(found.get(ids.get(i)));
			items.add(cID);
		}

		found.clear();
		foundByID.clear();
		ids.clear();

		if(items.size() == oldCopy.size()) {
			for(int i = 0; i < oldCopy.size(); ++i) {
				if(items.get(i).isEmpty() && oldCopy.get(i).isEmpty() || items.get(i).isItemEqual(oldCopy.get(i)) && ItemStack.areItemStackTagsEqual(items.get(i), oldCopy.get(i)) && items.get(i).getCount() == oldCopy.get(i).getCount())
					continue;
				else
					requireSync = true;
			}

		}
		else
			requireSync = true;

		packets(false);
	}

	/**
	 * Re-create the list of all inventories there are.
	 */
	public void rebuildInventories() {
		updateTime = 20;
		counted.clear();
		countedT.clear();
		for(int i = 0; i < 6*9; ++i) {
			ItemStack stk = getStackInSlot(i);
			if(stk.getItem() instanceof ItemInventoryGem && stk.hasTagCompound()) {
				int[] coords = ItemInventoryGem.getCoords(stk);
				if(coords != null && coords.length > 0) {
					int x = coords[0];
					int y = coords[1];
					int z = coords[2];
					int dim = MiscUtils.getStackTag(stk).getInteger("dim");
					if(dim == getWorld().provider.getDimension()) {
						if(getWorld().isBlockLoaded(new BlockPos(x, y, z)) && getWorld().getBlockState(new BlockPos(x, y, z)).getBlock() instanceof ITileEntityProvider) {
							TileEntity tile = getWorld().getTileEntity(new BlockPos(x, y, z));
							if(tile.hasCapability(ITEM_HANDLER_CAPABILITY, null)) {
								IItemHandler handler = tile.getCapability(ITEM_HANDLER_CAPABILITY, null);
								countedT.add(handler);
								counted.add(Pair.<BlockPos, IItemHandler>of(new BlockPos(x, y, z), handler));
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void openInventory(EntityPlayer p)  {
		if(!getWorld().isRemote) {
			plrs.add((EntityPlayerMP)p);
			requireSync = true;
		}
	}

	@Override
	public void closeInventory(EntityPlayer p)  {
		if(!getWorld().isRemote) {
			plrs.remove(p);
		}
	}

	@Override
	public void update() {
		super.update();

		if(updateTime <= 0)
			rebuildInventories();
		else
			--updateTime;

		if(!getWorld().isRemote)
			rebuildItems();
	}

	/**
	 * I wish there would be a better way of doing this, however, vanilla won't sync items in inventories unless they are not opened.
	 */
	public void packets(boolean force) {
		for(int i = 0; i < plrs.size(); ++i) {
			EntityPlayerMP player = plrs.get(i);
			if(player == null || player.isDead || player.dimension != getWorld().provider.getDimension())
				plrs.remove(i);
		}


		if(!requireSync && !force)
			return;

		if(!plrs.isEmpty()) {
			NBTTagCompound sentTag = new NBTTagCompound();
			NBTTagList lst = new NBTTagList();
			for(int i = 0; i < items.size(); ++i) {
				NBTTagCompound itmTag = new NBTTagCompound();
				items.get(i).writeToNBT(itmTag);
				itmTag.setInteger("stackSize", items.get(i).getCount());
				lst.appendTag(itmTag);
			}
			sentTag.setTag("items", lst);
			sentTag.setInteger("x", pos.getX());
			sentTag.setInteger("y", pos.getY());
			sentTag.setInteger("z", pos.getZ());

			PacketNBT packet = new PacketNBT(sentTag).setID(3);

			for(int i = 0; i < plrs.size(); ++i) {
				EssentialCraftCore.network.sendTo(packet, plrs.get(i));
			}
		}
		requireSync = false;
	}

	@Override
	public void setInventorySlotContents(int par1, ItemStack stack) {
		super.setInventorySlotContents(par1, stack);
		updateTime = 0;
		requireSync = true;
	}
}
