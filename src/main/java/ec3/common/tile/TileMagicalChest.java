package ec3.common.tile;

import DummyCore.Utils.MiscUtils;
import DummyCore.Utils.Notifier;
import DummyCore.Utils.TileStatTracker;
import ec3.common.mod.EssentialCraftCore;
import ec3.utils.common.ECUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public class TileMagicalChest extends TileEntity implements IInventory, ISidedInventory, ITickable {
	public float lidAngle;
	public float prevLidAngle;
	public int numUsingPlayers;
	private int ticksSinceSync;

	private ItemStack[] inventory;

	public String ownerName = "none";
	public int syncTick = 10;
	public int rotation;
	private TileStatTracker tracker;
	public boolean requestSync = true;
	public int metadata;

	public TileMagicalChest(int metaData) {
		this();
		metadata = metaData;
		setSlotsNum(metadata);
	}

	public TileMagicalChest() {
		super();
		tracker = new TileStatTracker(this);
	}
	
	public void setSlotsNum(int meta) {
		inventory = new ItemStack[meta == 0 ? 54 : 117];
	}

	@Override
	public void readFromNBT(NBTTagCompound i) {
		super.readFromNBT(i);
		metadata = i.getInteger("metadata");
		setSlotsNum(metadata);
		rotation = i.getInteger("rotation");
		ownerName = i.getString("ownerName");
		MiscUtils.loadInventory(this, i);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound i) {
		super.writeToNBT(i);
		i.setInteger("metadata", metadata);
		i.setInteger("rotation", rotation);
		i.setString("ownerName", ownerName);
		MiscUtils.saveInventory(this, i);
		return i;
	}

	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slotIndex) {
		return inventory[slotIndex];
	}

	@Override
	public ItemStack decrStackSize(int slotIndex, int decrementAmount) {
		ItemStack itemStack = getStackInSlot(slotIndex);
		if (itemStack != null) {
			if (itemStack.stackSize <= decrementAmount) {
				setInventorySlotContents(slotIndex, null);
			}
			else {
				itemStack = itemStack.splitStack(decrementAmount);
				if (itemStack.stackSize == 0) {
					setInventorySlotContents(slotIndex, null);
				}
			}
		}

		return itemStack;
	}

	@Override
	public ItemStack removeStackFromSlot(int slotIndex) {
		if (inventory[slotIndex] != null) {
			ItemStack itemStack = inventory[slotIndex];
			inventory[slotIndex] = null;
			return itemStack;
		}
		else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int slotIndex, ItemStack itemStack) {
		inventory[slotIndex] = itemStack;

		if(itemStack != null && itemStack.stackSize > getInventoryStackLimit()) {
			itemStack.stackSize = getInventoryStackLimit();
		}

		markDirty();
	}

	@Override
	public String getName() {
		return "magicalChest";
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
	public boolean isUsableByPlayer(EntityPlayer entityplayer) {
		return getWorld().getTileEntity(pos) == this && pos.distanceSqToCenter(entityplayer.posX, entityplayer.posY, entityplayer.posZ) <= 64D;
	}

	@Override
	public void openInventory(EntityPlayer p) {
		++numUsingPlayers;
		getWorld().addBlockEvent(pos, getWorld().getBlockState(pos).getBlock(), 1, numUsingPlayers);
	}

	@Override
	public void closeInventory(EntityPlayer p) {
		--numUsingPlayers;
		getWorld().addBlockEvent(pos, getWorld().getBlockState(pos).getBlock(), 1, numUsingPlayers);
	}

	@Override
	public boolean isItemValidForSlot(int slotIndex, ItemStack itemStack) {
		return true;
	}

	@Override
	public void update() {
		if(syncTick == 0) {
			if(tracker == null)
				Notifier.notifyCustomMod("EssentialCraft", "[WARNING][SEVERE]TileEntity " + this + " at pos " + pos.getX() + "," + pos.getY() + "," + pos.getZ() + " tries to sync itself, but has no TileTracker attached to it! SEND THIS MESSAGE TO THE DEVELOPER OF THE MOD!");
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

		if(++ticksSinceSync % 20 * 4 == 0)
			getWorld().addBlockEvent(pos, getWorld().getBlockState(pos).getBlock(), 1, numUsingPlayers);

		prevLidAngle = lidAngle;
		float angleIncrement = 0.1F;

		if(numUsingPlayers > 0 && lidAngle == 0.0F)
			getWorld().playSound(null, pos, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, getWorld().rand.nextFloat()*0.1F + 0.9F);

		if(numUsingPlayers == 0 && lidAngle > 0.0F || numUsingPlayers > 0 && lidAngle < 1.0F) {
			float var8 = lidAngle;

			if(numUsingPlayers > 0)
				lidAngle += angleIncrement;
			else
				lidAngle -= angleIncrement;

			if(lidAngle > 1.0F)
				lidAngle = 1.0F;

			if(lidAngle < 0.5F && var8 >= 0.5F)
				getWorld().playSound(null, pos, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, getWorld().rand.nextFloat() * 0.1F + 0.9F);

			if(lidAngle < 0.0F)
				lidAngle = 0.0F;
		}
	}

	/**
	 * Called when a client event is received with the event number and argument, see World.sendClientEvent
	 */
	@Override
	public boolean receiveClientEvent(int eventID, int numUsingPlayers) {
		if(eventID == 1) {
			this.numUsingPlayers = numUsingPlayers;
			return true;
		}
		else {
			return super.receiveClientEvent(eventID, numUsingPlayers);
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
	public int[] getSlotsForFace(EnumFacing side) {
		int[] ret = new int[inventory.length];

		for(int i = 0; i < ret.length; ++i)
			ret[i] = i;

		return ret;
	}

	@Override
	public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, EnumFacing p_102007_3_) {
		return true;
	}

	@Override
	public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, EnumFacing p_102008_3_) {
		return true;
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
