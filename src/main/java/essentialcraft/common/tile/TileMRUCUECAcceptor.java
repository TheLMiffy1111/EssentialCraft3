package essentialcraft.common.tile;

import DummyCore.Utils.MiscUtils;
import essentialcraft.api.EnumStructureType;
import essentialcraft.api.IMRUHandler;
import essentialcraft.api.IStructurePiece;
import essentialcraft.api.IWorldUpdatable;
import essentialcraft.common.capabilities.mru.CapabilityMRUHandler;
import essentialcraft.common.item.ItemsCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class TileMRUCUECAcceptor extends TileEntity implements IStructurePiece, IInventory, ITickable {
	public TileMRUCUECController controller;
	public int syncTick;
	public ItemStack[] items = {ItemStack.EMPTY};
	public ControllerMRUStorageInputOnlyWrapper mruStorageWrapper = new ControllerMRUStorageInputOnlyWrapper();

	@Override
	public void readFromNBT(NBTTagCompound i) {
		super.readFromNBT(i);
		MiscUtils.loadInventory(this, i);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound i) {
		super.writeToNBT(i);
		MiscUtils.saveInventory(this, i);
		return i;
	}

	@Override
	public EnumStructureType getStructure() {
		return EnumStructureType.MRUCUEC;
	}

	@Override
	public TileEntity structureController() {
		return controller;
	}

	@Override
	public void setStructureController(TileEntity tile, EnumStructureType structure) {
		if(tile instanceof TileMRUCUECController && structure == getStructure())
			controller = (TileMRUCUECController)tile;
	}

	@Override
	public void update() {
		if(controller != null) {
			mruStorageWrapper.update(getPos(), getWorld(), getStackInSlot(0));
		}
		if(syncTick == 0) {
			if(!getWorld().isRemote)
				MiscUtils.sendPacketToAllAround(getWorld(), getUpdatePacket(), pos.getX(), pos.getY(), pos.getZ(), getWorld().provider.getDimension(), 16);
			syncTick = 10;
		}
		else
			--syncTick;
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
		if(!items[par1].isEmpty()) {
			ItemStack itemstack = items[par1];
			items[par1] = ItemStack.EMPTY;
			return itemstack;
		}
		else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	public void setInventorySlotContents(int par1, ItemStack stack) {
		items[par1] = stack;

		if(!stack.isEmpty() && stack.getCount() > getInventoryStackLimit())
			stack.setCount(getInventoryStackLimit());
	}

	@Override
	public String getName() {
		return "essentialcraft.container.mruAcceptor";
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
	public void openInventory(EntityPlayer p) {}

	@Override
	public void closeInventory(EntityPlayer p) {}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return stack.getItem() == ItemsCore.bound_gem;
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
	public void clear() {
		for(int i = 0; i < getSizeInventory(); i++)
			setInventorySlotContents(i, ItemStack.EMPTY);
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
			capability == CapabilityMRUHandler.MRU_HANDLER_CAPABILITY ? (T)mruStorageWrapper :
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

	public class ControllerMRUStorageInputOnlyWrapper implements IMRUHandler, IWorldUpdatable<ItemStack> {

		@Override
		public int getMaxMRU() {
			if(controller != null) {
				return controller.mruStorage.getMaxMRU();
			}
			return 0;
		}

		@Override
		public void setMaxMRU(int amount) {
			if(controller != null) {
				controller.mruStorage.setMaxMRU(amount);
			}
		}

		@Override
		public int getMRU() {
			if(controller != null) {
				return controller.mruStorage.getMRU();
			}
			return 0;
		}

		@Override
		public void setMRU(int amount) {
			if(controller != null) {
				controller.mruStorage.setMRU(amount);
			}
		}

		@Override
		public int addMRU(int amount, boolean doAdd) {
			if(controller != null) {
				return controller.mruStorage.addMRU(amount, doAdd);
			}
			return amount;
		}

		@Override
		public int extractMRU(int amount, boolean doExtract) {
			return 0;
		}

		@Override
		public float getBalance() {
			if(controller != null) {
				controller.mruStorage.getBalance();
			}
			return 1F;
		}

		@Override
		public void setBalance(float balance) {
			if(controller != null) {
				controller.mruStorage.setBalance(balance);
			}
		}

		@Override
		public boolean getShade() {
			if(controller != null) {
				return controller.mruStorage.getShade();
			}
			return false;
		}

		@Override
		public void setShade(boolean shade) {
			if(controller != null) {
				controller.mruStorage.setShade(shade);
			}
		}

		@Override
		public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
			if(controller != null) {
				controller.mruStorage.writeToNBT(nbt);
			}
			return nbt;
		}

		@Override
		public void readFromNBT(NBTTagCompound nbt) {
			if(controller != null) {
				controller.mruStorage.readFromNBT(nbt);
			}
		}

		@Override
		public void update(BlockPos pos, World world, ItemStack boundGem) {
			if(controller != null) {
				controller.mruStorage.update(pos, world, boundGem);
			}
		}
	}
}
