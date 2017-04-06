package ec3.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerRightClicker extends Container {
	
	private IInventory inv;
	private int sizeInventory;
	public ContainerRightClicker(InventoryPlayer par1InventoryPlayer, TileEntity par2) {
		inv = (IInventory)par2;
		addSlotToContainer(new SlotBoundEssence(inv, 0, 26, 41));
		
		if(par2.getBlockMetadata() == 0 || par2.getBlockMetadata() == 1) {
			addSlotToContainer(new SlotGeneric(inv, 1, 80, 24, "minecraft:items/stick"));
			sizeInventory = 3;
		}
		else {
			for(int i = 0; i < 9; ++i) {
				addSlotToContainer(new SlotGeneric(inv, 1 + i, 62 + (i%3*18), 6 + (i/3*18), "minecraft:items/stick"));
			}
			sizeInventory = 11;
		}
		addSlotToContainer(new SlotGeneric(inv, 10, 156, 4, "minecraft:items/dye_powder_gray"));
		int i;
		
		for(i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(par1InventoryPlayer, j + i*9 + 9, 8 + j*18, 84 + i*18));
			}
		}
		
		for(i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(par1InventoryPlayer, i, 8 + i*18, 142));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return inv.isUsableByPlayer(p_75145_1_);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {
		ItemStack itemstack = null;
		Slot slot = (Slot)inventorySlots.get(p_82846_2_);
		
		if(slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if(p_82846_2_ < sizeInventory) {
				if(!mergeItemStack(itemstack1, sizeInventory, 36+sizeInventory, true)) {
					if(itemstack1.stackSize == 0)
						slot.putStack((ItemStack)null);
					slot.onSlotChange(itemstack1, itemstack);
					return null;
				}
			}
			else if(p_82846_2_ >= sizeInventory) {
				for(int i = 0; i < sizeInventory; ++i) {
					if(mergeItemStack(itemstack1, i, i+1, false)) {
						if(itemstack1.stackSize == 0)
							slot.putStack((ItemStack)null);
						return null;
					}
				}
			}
			
			if(p_82846_2_ >= sizeInventory && p_82846_2_ < 27+sizeInventory) {
				if(!mergeItemStack(itemstack1, 27+sizeInventory, 36+sizeInventory, false)) {
					if(itemstack1.stackSize == 0)
						slot.putStack((ItemStack)null);
					return null;
				}
			}
			else if(p_82846_2_ >= 27+sizeInventory && p_82846_2_ < 36+sizeInventory && !mergeItemStack(itemstack1, sizeInventory, 27+sizeInventory, false)) {
				if(itemstack1.stackSize == 0)
					slot.putStack((ItemStack)null);
				return null;
			}
			
			if(itemstack.stackSize == 0)
				slot.putStack((ItemStack)null);
			
			if(itemstack1.stackSize == 0)
				slot.putStack((ItemStack)null);
			else
				slot.onSlotChanged();
			
			if(itemstack1.stackSize == itemstack.stackSize)
				return null;
			
			slot.onPickupFromSlot(p_82846_1_, itemstack1);
		}
		
		return itemstack;
	}
}
