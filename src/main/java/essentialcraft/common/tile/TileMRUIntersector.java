package essentialcraft.common.tile;

import DummyCore.Utils.MiscUtils;
import DummyCore.Utils.Notifier;
import DummyCore.Utils.TileStatTracker;
import essentialcraft.api.ApiCore;
import essentialcraft.common.capabilities.mru.CapabilityMRUHandler;
import essentialcraft.common.capabilities.mru.MRUTileStorage;
import essentialcraft.common.item.ItemBoundGem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class TileMRUIntersector extends TileEntity implements IInventory, ITickable {

	public static int cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;

	public int syncTick;
	protected MRUTileStorage mruStorage = new MRUTileStorage(cfgMaxMRU);
	public int innerRotation;
	public ItemStack[] items = {ItemStack.EMPTY, ItemStack.EMPTY};
	private TileStatTracker tracker;

	public TileMRUIntersector() {
		super();
		tracker = new TileStatTracker(this);
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
		mruStorage.update(getPos(), getWorld(), getStackInSlot(0));
		mruStorage.update(getPos(), getWorld(), getStackInSlot(1));
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
	public ItemStack getStackInSlot(int par1) {
		return items[par1];
	}

	@Override
	public ItemStack decrStackSize(int par1, int par2) {
		if(!items[par1].isEmpty()) {
			ItemStack itemstack;

			if(items[par1].getCount() <= par2) {
				itemstack = items[par1];
				items[par1] = ItemStack.EMPTY;
				return itemstack;
			}
			else {
				itemstack = items[par1].splitStack(par2);

				if(items[par1].getCount() == 0)
					items[par1] = ItemStack.EMPTY;

				return itemstack;
			}
		}
		else
			return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStackFromSlot(int par1) {
		if (!items[par1].isEmpty()) {
			ItemStack itemstack = items[par1];
			items[par1] = ItemStack.EMPTY;
			return itemstack;
		}
		else
			return ItemStack.EMPTY;
	}

	@Override
	public void setInventorySlotContents(int par1, ItemStack stack) {
		items[par1] = stack;

		if(!stack.isEmpty() && stack.getCount() > getInventoryStackLimit())
			stack.setCount(getInventoryStackLimit());
	}

	@Override
	public String getName() {
		return "essentialcraft.container.mruIntersector";
	}

	@Override
	public boolean hasCustomName() {
		return false;
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

	public IItemHandler itemHandler = new InvWrapper(this);

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

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.mruintersector";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.DEVICE_MAX_MRU_GENERIC).setMinValue(1).getInt();
		}
		catch(Exception e) {
			return;
		}
	}
}
