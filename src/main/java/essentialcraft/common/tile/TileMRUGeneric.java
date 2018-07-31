package essentialcraft.common.tile;

import java.util.Arrays;

import DummyCore.Utils.MathUtils;
import DummyCore.Utils.MiscUtils;
import DummyCore.Utils.Notifier;
import DummyCore.Utils.TileStatTracker;
import essentialcraft.common.capabilities.mru.CapabilityMRUHandler;
import essentialcraft.common.capabilities.mru.MRUTileStorage;
import essentialcraft.common.item.ItemBoundGem;
import essentialcraft.common.mod.EssentialCraftCore;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public abstract class TileMRUGeneric extends TileEntity implements ISidedInventory, ITickable {

	public TileMRUGeneric() {
		super();
		tracker = new TileStatTracker(this);
		mruStorage = new MRUTileStorage();
	}

	public TileMRUGeneric(int maxMRU) {
		super();
		tracker = new TileStatTracker(this);
		mruStorage = new MRUTileStorage(maxMRU);
	}

	public int syncTick = 10;
	public int innerRotation;
	private ItemStack[] items = {ItemStack.EMPTY};
	private TileStatTracker tracker;
	public boolean slot0IsBoundGem = true;
	public boolean requestSync = true;
	protected MRUTileStorage mruStorage;

	public abstract int[] getOutputSlots();

	public void setSlotsNum(int i) {
		items = new ItemStack[i];
		Arrays.fill(items, ItemStack.EMPTY);
	}

	@Override
	public void readFromNBT(NBTTagCompound i) {
		super.readFromNBT(i);
		mruStorage.readFromNBT(i);
		MiscUtils.loadInventory(this, i);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound i) {
		super.writeToNBT(i);
		mruStorage.writeToNBT(i);
		MiscUtils.saveInventory(this, i);
		return i;
	}

	@Override
	public void update() {
		++innerRotation;
		//Sending the sync packets to the CLIENT.
		if(syncTick == 0) {
			if(tracker == null)
				Notifier.notifyCustomMod("EssentialCraft", "[WARNING][SEVERE]TileEntity " + this + " at pos " + pos.getX() + "," + pos.getY() + ","  + pos.getZ() + " tries to sync itself, but has no TileTracker attached to it! SEND THIS MESSAGE TO THE DEVELOPER OF THE MOD!");
			else if(!getWorld().isRemote && tracker.tileNeedsSyncing()) {
				MiscUtils.sendPacketToAllAround(getWorld(), getUpdatePacket(), pos.getX(), pos.getY(), pos.getZ(), getWorld().provider.getDimension(), 32);
			}
			syncTick = 20;
		}
		else {
			--syncTick;
		}

		if(requestSync && getWorld().isRemote) {
			requestSync = false;
			ECUtils.requestScheduledTileSync(this, EssentialCraftCore.proxy.getClientPlayer());
		}
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		writeToNBT(nbttagcompound);
		return new SPacketUpdateTileEntity(pos, -10, nbttagcompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		if(pkt.getTileEntityType() == -10)
			readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public int getSizeInventory() {
		return items.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return items[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if(!items[slot].isEmpty()) {
			ItemStack itemstack;

			if(items[slot].getCount() <= amount) {
				itemstack = items[slot];
				items[slot] = ItemStack.EMPTY;
				return itemstack;
			}
			else {
				itemstack = items[slot].splitStack(amount);

				if(items[slot].getCount() == 0) {
					items[slot] = ItemStack.EMPTY;
				}

				return itemstack;
			}
		}
		else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int slot) {
		if(!items[slot].isEmpty()) {
			ItemStack itemstack = items[slot];
			items[slot] = ItemStack.EMPTY;
			return itemstack;
		}
		else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		items[slot] = stack;

		if (!stack.isEmpty() && stack.getCount() > getInventoryStackLimit()) {
			stack.setCount(getInventoryStackLimit());
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return getWorld().getTileEntity(getPos()) == this && player.dimension == getWorld().provider.getDimension() && getPos().distanceSqToCenter(player.posX, player.posY, player.posZ) <= 64D;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot0IsBoundGem && slot == 0 ? isBoundGem(stack) : true;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing face) {
		int[] ret = new int[getSizeInventory()];
		for(int i = 0; i < ret.length; i++) {
			ret[i] = i;
		}
		return ret;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing facing) {
		return isItemValidForSlot(slot, stack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing facing) {
		return MathUtils.arrayContains(getOutputSlots(), slot);
	}

	public boolean isBoundGem(ItemStack stack) {
		return stack.getItem() instanceof ItemBoundGem;
	}

	@Override
	public void clear() {
		for(int i = 0; i < getSizeInventory(); i++) {
			setInventorySlotContents(i, ItemStack.EMPTY);
		}
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public String getName() {
		return "essentialcraft.container.generic";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	public IItemHandler itemHandler = new SidedInvWrapper(this, EnumFacing.DOWN);

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ||
				capability == CapabilityMRUHandler.MRU_HANDLER_CAPABILITY ||
				super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)itemHandler :
			capability == CapabilityMRUHandler.MRU_HANDLER_CAPABILITY ? (T)mruStorage :
				super.getCapability(capability, facing);
	}

	@Override
	public boolean isEmpty() {
		boolean ret = true;
		for(ItemStack stk : items) {
			ret &= stk.isEmpty();
		}
		return ret;
	}
}
