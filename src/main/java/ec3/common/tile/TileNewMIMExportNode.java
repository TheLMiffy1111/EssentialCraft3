package ec3.common.tile;

import java.util.ArrayList;

import ec3.common.inventory.InventoryMagicFilter;
import ec3.common.item.ItemFilter;
import ec3.common.item.ItemsCore;
import ec3.utils.common.ECUtils;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TileNewMIMExportNode extends TileMRUGeneric {

	public TileNewMIMExportNode() {
		setMaxMRU(0);
		setSlotsNum(1);
		slot0IsBoundGem = false;
	}
	
	public EnumFacing getRotation() {
		int metadata = this.getBlockMetadata();
		metadata %= 6;
		return EnumFacing.getFront(metadata);
	}
	
	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return p_94041_2_.getItem() == ItemsCore.filter;
	}
	
	@Override
	public int[] getOutputSlots() {
		return new int[0];
	}
	
	public ISidedInventory getConnectedInventory() {
		EnumFacing side = getRotation();
		if(getWorld().getTileEntity(pos.offset(side)) != null) {
			TileEntity tile = getWorld().getTileEntity(pos.offset(side));
			if(tile instanceof ISidedInventory)
				return (ISidedInventory)tile;
		}
		
		return null;
	}
	
	public IInventory getConnectedInventoryInefficent() {
		EnumFacing side = getRotation();
		if(getWorld().getTileEntity(pos.offset(side)) != null) {
			TileEntity tile = getWorld().getTileEntity(pos.offset(side));
			if(tile instanceof IInventory)
				return (IInventory) tile;
		}
		
		return null;
	}
	
	public int[] getAccessibleSlots() {
		return getConnectedInventory().getSlotsForFace(getRotation().getOpposite());
	}
	
	public void exportAllPossibleItems(TileNewMIM parent) {
		if(getWorld().isBlockIndirectlyGettingPowered(pos) > 0)
			return;
		
		ISidedInventory inv = getConnectedInventory();
		if(inv != null) {
			ArrayList<ItemStack> itemsToExport = parent.getAllItems();
			int[] slots = getAccessibleSlots();
			
			if(slots.length <= 0)
				return;
			
			for(int i = 0; i < itemsToExport.size(); ++i) {
				for(int j = 0; j < slots.length; ++j) {
					if(inv.canInsertItem(slots[j], itemsToExport.get(i), getRotation().getOpposite())) {
						if(inv.getStackInSlot(slots[j]) == null || (inv.getStackInSlot(slots[j]).isItemEqual(itemsToExport.get(i)) && ItemStack.areItemStackTagsEqual(inv.getStackInSlot(slots[j]), itemsToExport.get(i)))) {
							if(getStackInSlot(0) == null || !(getStackInSlot(0).getItem() instanceof ItemFilter)) {
								if(inv.getStackInSlot(slots[j]) == null) {
									ItemStack copied = itemsToExport.get(i).copy();
									if(copied.stackSize >= inv.getInventoryStackLimit())
										copied.stackSize = inv.getInventoryStackLimit();
									
									if(parent.retrieveItemStackFromSystem(copied, false, true) == 0)
										inv.setInventorySlotContents(slots[j], copied);
									
								}
								else {
									ItemStack copied = itemsToExport.get(i).copy();
									if(copied.stackSize >= inv.getInventoryStackLimit())
										copied.stackSize = inv.getInventoryStackLimit();
									
									if(inv.getStackInSlot(slots[j]).stackSize + copied.stackSize <= inv.getInventoryStackLimit() && inv.getStackInSlot(slots[j]).stackSize + copied.stackSize <= copied.getMaxStackSize()) {
										if(parent.retrieveItemStackFromSystem(copied, false, true) == 0)
											inv.getStackInSlot(slots[j]).stackSize += copied.stackSize;
									}
									else {
										int reduceBy = copied.stackSize - inv.getStackInSlot(slots[j]).stackSize;
										if(reduceBy > 0) {
											copied.stackSize = reduceBy;
											if(parent.retrieveItemStackFromSystem(copied, false, true) == 0)
												inv.getStackInSlot(slots[j]).stackSize += reduceBy;
										}
									}
								}
							}
							else {
								ItemStack copied = itemsToExport.get(i).copy();
								if(ECUtils.canFilterAcceptItem(new InventoryMagicFilter(getStackInSlot(0)), copied, getStackInSlot(0))) {
									if(copied.stackSize >= inv.getInventoryStackLimit())
										copied.stackSize = inv.getInventoryStackLimit();
									
									if(inv.getStackInSlot(slots[j]) == null) {
										if(parent.retrieveItemStackFromSystem(copied, false, true) == 0)
											inv.setInventorySlotContents(slots[j], copied);
										
									}
									else if(inv.getStackInSlot(slots[j]).stackSize + copied.stackSize <= inv.getInventoryStackLimit() && inv.getStackInSlot(slots[j]).stackSize + copied.stackSize <= copied.getMaxStackSize())
									{
										if(parent.retrieveItemStackFromSystem(copied, false, true) == 0)
											inv.getStackInSlot(slots[j]).stackSize += copied.stackSize;
									}
									else {
										int reduceBy = copied.stackSize - inv.getStackInSlot(slots[j]).stackSize;
										if(reduceBy > 0) {
											copied.stackSize = reduceBy;
											if(parent.retrieveItemStackFromSystem(copied, false, true) == 0)
												inv.getStackInSlot(slots[j]).stackSize += reduceBy;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		else {
			IInventory iinv = getConnectedInventoryInefficent();
			
			ArrayList<ItemStack> itemsToExport = parent.getAllItems();
			
			if(iinv.getSizeInventory() <= 0)
				return;
			
			for(int i = 0; i < itemsToExport.size(); ++i) {
				for(int j = 0; j < iinv.getSizeInventory(); ++j) {
					if(iinv.isItemValidForSlot(j, itemsToExport.get(i))) {
						if(iinv.getStackInSlot(j) == null || (iinv.getStackInSlot(j).isItemEqual(itemsToExport.get(i)) && ItemStack.areItemStackTagsEqual(iinv.getStackInSlot(j), itemsToExport.get(i)))) {
							if(getStackInSlot(0) == null || !(getStackInSlot(0).getItem() instanceof ItemFilter)) {
								if(iinv.getStackInSlot(j) == null) {
									ItemStack copied = itemsToExport.get(i).copy();
									if(copied.stackSize >= iinv.getInventoryStackLimit())
										copied.stackSize = iinv.getInventoryStackLimit();
									
									if(parent.retrieveItemStackFromSystem(copied, false, true) == 0)
										iinv.setInventorySlotContents(j, copied);
									
								}
								else {
									ItemStack copied = itemsToExport.get(i).copy();
									if(copied.stackSize >= iinv.getInventoryStackLimit())
										copied.stackSize = iinv.getInventoryStackLimit();
									
									if(iinv.getStackInSlot(j).stackSize + copied.stackSize <= iinv.getInventoryStackLimit() && iinv.getStackInSlot(j).stackSize + copied.stackSize <= copied.getMaxStackSize()) {
										if(parent.retrieveItemStackFromSystem(copied, false, true) == 0)
											iinv.getStackInSlot(j).stackSize += copied.stackSize;
									}
									else {
										int reduceBy = copied.stackSize - iinv.getStackInSlot(j).stackSize;
										if(reduceBy > 0) {
											copied.stackSize = reduceBy;
											if(parent.retrieveItemStackFromSystem(copied, false, true) == 0)
												iinv.getStackInSlot(j).stackSize += reduceBy;
										}
									}
								}
							}
							else {
								ItemStack copied = itemsToExport.get(i).copy();
								if(ECUtils.canFilterAcceptItem(new InventoryMagicFilter(getStackInSlot(0)), copied, getStackInSlot(0))) {
									if(copied.stackSize >= iinv.getInventoryStackLimit())
										copied.stackSize = iinv.getInventoryStackLimit();
									
									if(iinv.getStackInSlot(j) == null) {
										if(parent.retrieveItemStackFromSystem(copied, false, true) == 0)
											iinv.setInventorySlotContents(j, copied);
										
									}
									else if(iinv.getStackInSlot(j).stackSize + copied.stackSize <= iinv.getInventoryStackLimit() && iinv.getStackInSlot(j).stackSize + copied.stackSize <= copied.getMaxStackSize()) {
										if(parent.retrieveItemStackFromSystem(copied, false, true) == 0)
											iinv.getStackInSlot(j).stackSize += copied.stackSize;
									}
									else {
										int reduceBy = copied.stackSize - iinv.getStackInSlot(j).stackSize;
										if(reduceBy > 0) {
											copied.stackSize = reduceBy;
											if(parent.retrieveItemStackFromSystem(copied, false, true) == 0)
												iinv.getStackInSlot(j).stackSize += reduceBy;	
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
