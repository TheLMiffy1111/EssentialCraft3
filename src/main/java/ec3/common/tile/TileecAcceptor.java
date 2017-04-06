package ec3.common.tile;

import java.util.UUID;

import DummyCore.Utils.MiscUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import ec3.api.EnumStructureType;
import ec3.api.IStructurePiece;
import ec3.api.ITERequiresMRU;
import ec3.common.item.ItemBoundGem;
import ec3.common.item.ItemsCore;
import ec3.utils.common.ECUtils;

public class TileecAcceptor extends TileEntity implements ITERequiresMRU, IStructurePiece, IInventory, ITickable {
	public TileecController controller;
	public UUID uuid = UUID.randomUUID();
	public int syncTick;
	public ItemStack[] items = new ItemStack[1];
	
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
	public int getMRU() {
		return controller != null ? controller.getMRU() : 0;
	}
	
	@Override
	public int getMaxMRU() {
		return controller != null ? controller.getMaxMRU() : 0;
	}
	
	@Override
	public boolean setMRU(int i) {
		return controller != null && i >= getMRU() ? controller.setMRU(i) : false;
	}
	
	@Override
	public float getBalance() {
		return controller != null ? controller.getBalance() : 1;
	}
	
	@Override
	public boolean setBalance(float f) {
		return controller != null ? controller.setBalance(f) : false;
	}
	
	@Override
	public UUID getUUID() {
		return uuid;
	}
	
	@Override
	public EnumStructureType getStructure() {
		return EnumStructureType.MRUCUEnrichmentChamber;
	}
	
	@Override
	public TileEntity structureController() {
		return controller;
	}
	
	@Override
	public void setStructureController(TileEntity tile, EnumStructureType structure) {
		if(tile instanceof TileecController && structure == getStructure())
			controller = (TileecController)tile;
	}
	
	@Override
	public void update() {
		if(structureController() != null)
			ECUtils.mruIn(this, 0);
		ECUtils.spawnMRUParticles(this,0);
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
		if(items[par1] != null) {
			ItemStack itemstack;
			
			if(items[par1].stackSize <= par2) {
				itemstack = items[par1];
				items[par1] = null;
				return itemstack;
			}
			else {
				itemstack = items[par1].splitStack(par2);
				
				if(items[par1].stackSize == 0)
					items[par1] = null;
				
				return itemstack;
			}
		}
		else
			return null;
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
		
		if (par2ItemStack != null && par2ItemStack.stackSize > getInventoryStackLimit())
			par2ItemStack.stackSize = getInventoryStackLimit();
	}
	
	@Override
	public String getName() {
		return "ec3.container.mruAcceptor";
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
		return getWorld().getTileEntity(pos) == this && player.dimension == getWorld().provider.getDimension();
	}
	
	@Override
	public void openInventory(EntityPlayer p) {}
	
	@Override
	public void closeInventory(EntityPlayer p) {}
	
	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return p_94041_2_.getItem() == ItemsCore.bound_gem;
	}
	
	@Override
	public boolean setMaxMRU(float f) {
		return controller != null ? controller.setMaxMRU(f) : false;
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
	        setInventorySlotContents(i, null);
	}
	
	public IItemHandler itemHandler = new InvWrapper(this);

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)itemHandler : super.getCapability(capability, facing);
	}
}
