package ec3.common.tile;

import java.util.UUID;

import DummyCore.Utils.MathUtils;
import DummyCore.Utils.MiscUtils;
import DummyCore.Utils.Notifier;
import DummyCore.Utils.TileStatTracker;
import ec3.api.ITERequiresMRU;
import ec3.common.item.ItemBoundGem;
import ec3.common.mod.EssentialCraftCore;
import ec3.utils.common.ECUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
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

public abstract class TileMRUGeneric extends TileEntity implements ITERequiresMRU, IInventory, ISidedInventory, ITickable {

	public TileMRUGeneric() {
		super();
		tracker = new TileStatTracker(this);
	}

	public int syncTick = 10;
	int mru;
	int maxMRU = 100;
	UUID uuid = UUID.randomUUID();
	float balance;
	public int innerRotation;
	private ItemStack[] items = new ItemStack[1];
	private TileStatTracker tracker;
	public boolean slot0IsBoundGem = true;
	public boolean requestSync = true;

	public abstract int[] getOutputSlots();

	public void setSlotsNum(int i) {
		items = new ItemStack[i];
	}

	@Override
	public void readFromNBT(NBTTagCompound i) {
		super.readFromNBT(i);
		ECUtils.loadMRUState(this, i);
		MiscUtils.loadInventory(this, i);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound i) {
		super.writeToNBT(i);
		ECUtils.saveMRUState(this, i);
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
			syncTick = 60;
		}
		else
			--syncTick;

		if(requestSync && getWorld().isRemote) {
			requestSync = false;
			ECUtils.requestScheduledTileSync(this, EssentialCraftCore.proxy.getClientPlayer());
		}
	}

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
	public int getMRU() {
		return mru;
	}

	@Override
	public int getMaxMRU() {
		return maxMRU;
	}

	@Override
	public boolean setMRU(int i) {
		mru = i;
		return true;
	}

	@Override
	public float getBalance() {
		return balance;
	}

	@Override
	public boolean setBalance(float f) {
		balance = f;
		return true;
	}

	@Override
	public boolean setMaxMRU(float f) {
		maxMRU = (int)f;
		return true;
	}

	@Override
	public UUID getUUID() {
		return uuid;
	}

	@Override
	public int getSizeInventory() {
		return items.length;
	}

	@Override
	public ItemStack getStackInSlot(int par1) {
		return items[par1];
	}

	@Override
	public ItemStack decrStackSize(int par1, int par2) {
		if(items[par1] != null) {
			ItemStack itemstack;

			if(items[par1].stackSize <= par2) {
				itemstack = items[par1];
				items[par1] = null;
				return itemstack;
			}
			else {
				itemstack = items[par1].splitStack(par2);

				if(items[par1].stackSize == 0) {
					items[par1] = null;
				}

				return itemstack;
			}
		}
		else {
			return null;
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int par1) {
		if(items[par1] != null) {
			ItemStack itemstack = items[par1];
			items[par1] = null;
			return itemstack;
		}
		else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
		items[par1] = par2ItemStack;

		if (par2ItemStack != null && par2ItemStack.stackSize > getInventoryStackLimit()) {
			par2ItemStack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return getWorld().getTileEntity(pos) == this && player.dimension == getWorld().provider.getDimension();
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return slot0IsBoundGem && p_94041_1_ == 0 ? isBoundGem(p_94041_2_) : true;
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
	public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, EnumFacing p_102007_3_) {
		return isItemValidForSlot(p_102007_1_, p_102007_2_);
	}

	@Override
	public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, EnumFacing p_102008_3_) {
		return MathUtils.arrayContains(getOutputSlots(), p_102008_1_);
	}

	public boolean isBoundGem(ItemStack stack) {
		return stack.getItem() instanceof ItemBoundGem;
	}

	@Override
	public void clear() {
		for(int i = 0; i < getSizeInventory(); i++)
			setInventorySlotContents(i, null);
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
		return "ec3.container.generic";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	public IItemHandler itemHandler = new SidedInvWrapper(this, EnumFacing.DOWN);

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)itemHandler : super.getCapability(capability, facing);
	}
}
