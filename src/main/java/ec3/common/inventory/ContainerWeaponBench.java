package ec3.common.inventory;

import ec3.common.tile.TileWeaponMaker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerWeaponBench extends Container {
	
	private IInventory inv;
	private int sizeInventory;
    public ContainerWeaponBench(InventoryPlayer par1InventoryPlayer, TileEntity par2) {
    	TileWeaponMaker maker = (TileWeaponMaker)par2;
    	inv = (IInventory)par2;
    	
        addSlotToContainer(new SlotGeneric(inv, 0, 152, 37));
        
        if(maker.index == 0) {
        	//Elemental Core
        	addSlotToContainer(new SlotGeneric(inv, 1, 40, 37));
        	//Soul Sphere
        	addSlotToContainer(new SlotGeneric(inv, 2, 60, 57));
        	//Lens
        	addSlotToContainer(new SlotGeneric(inv, 3, 20, 17));
        	//Scope
        	addSlotToContainer(new SlotGeneric(inv, 4, 60, 17));
        	//Base
        	addSlotToContainer(new SlotGeneric(inv, 5, 40, 17));
        	addSlotToContainer(new SlotGeneric(inv, 6, 60, 37));
        	addSlotToContainer(new SlotGeneric(inv, 7, 80, 37));
        	//Mechanism
        	addSlotToContainer(new SlotGeneric(inv, 8, 80, 17));
        	//Handle
        	addSlotToContainer(new SlotGeneric(inv, 9, 80, 57));
        	sizeInventory = 10;
        }
        
        if(maker.index == 1) {
        	//Elemental Core
        	addSlotToContainer(new SlotGeneric(inv, 1, 40, 37));
        	//Soul Sphere
        	addSlotToContainer(new SlotGeneric(inv, 2, 60, 37));
        	//Lens
        	addSlotToContainer(new SlotGeneric(inv, 3, 20, 37));
        	//Scope
        	addSlotToContainer(new SlotGeneric(inv, 4, 40, 17));
        	//Base
        	addSlotToContainer(new SlotGeneric(inv, 5, 20, 17));
        	addSlotToContainer(new SlotGeneric(inv, 6, 60, 17));
        	addSlotToContainer(new SlotGeneric(inv, 7, 80, 37));
        	addSlotToContainer(new SlotGeneric(inv, 8, 60, 57));
        	//Mechanism
        	addSlotToContainer(new SlotGeneric(inv, 9, 80, 17));
        	//Handle
        	addSlotToContainer(new SlotGeneric(inv, 10, 20, 57));
        	addSlotToContainer(new SlotGeneric(inv, 11, 40, 57));
        	addSlotToContainer(new SlotGeneric(inv, 12, 80, 57));
        	sizeInventory = 13;
        }
        
        if(maker.index == 2) {
        	//Elemental Core
        	addSlotToContainer(new SlotGeneric(inv, 1, 60, 37));
        	//Soul Sphere
        	addSlotToContainer(new SlotGeneric(inv, 2, 40, 57));
        	//Lens
        	addSlotToContainer(new SlotGeneric(inv, 3, 20, 37));
        	//Scope
        	addSlotToContainer(new SlotGeneric(inv, 4, 60, 17));
        	addSlotToContainer(new SlotGeneric(inv, 5, 40, 17));
        	addSlotToContainer(new SlotGeneric(inv, 6, 80, 17));
        	//Base
        	addSlotToContainer(new SlotGeneric(inv, 7, 40, 37));
        	addSlotToContainer(new SlotGeneric(inv, 8, 80, 37));
        	addSlotToContainer(new SlotGeneric(inv, 9, 100, 37));
        	//Mechanism
        	addSlotToContainer(new SlotGeneric(inv, 10, 100, 17));
        	addSlotToContainer(new SlotGeneric(inv, 11, 60, 57));
        	//Handle
        	addSlotToContainer(new SlotGeneric(inv, 12, 80, 57));
        	addSlotToContainer(new SlotGeneric(inv, 13, 100, 57));
        	sizeInventory = 14;
        }
        
        if(maker.index == 3) {
        	//Elemental Core
        	addSlotToContainer(new SlotGeneric(inv, 1, 80, 37));
        	//Soul Sphere
        	addSlotToContainer(new SlotGeneric(inv, 2, 40, 57));
        	//Lens
        	addSlotToContainer(new SlotGeneric(inv, 3, 20, 37));
        	addSlotToContainer(new SlotGeneric(inv, 4, 20, 17));
        	addSlotToContainer(new SlotGeneric(inv, 5, 20, 57));
        	//Scope
        	//No scope for the Gatling
        	//Base
        	addSlotToContainer(new SlotGeneric(inv, 6, 40, 17));
        	addSlotToContainer(new SlotGeneric(inv, 7, 60, 17));
        	addSlotToContainer(new SlotGeneric(inv, 8, 80, 17));
        	addSlotToContainer(new SlotGeneric(inv, 9, 100, 17));
        	addSlotToContainer(new SlotGeneric(inv, 10, 120, 17));
        	addSlotToContainer(new SlotGeneric(inv, 11, 40, 37));
        	addSlotToContainer(new SlotGeneric(inv, 12, 120, 37));
        	//Mechanism
        	addSlotToContainer(new SlotGeneric(inv, 13, 60, 37));
        	addSlotToContainer(new SlotGeneric(inv, 14, 100, 37));
        	//Handle
        	addSlotToContainer(new SlotGeneric(inv, 15, 60, 57));
        	addSlotToContainer(new SlotGeneric(inv, 16, 80, 57));
        	addSlotToContainer(new SlotGeneric(inv, 17, 100, 57));
        	addSlotToContainer(new SlotGeneric(inv, 18, 120, 57));
        	sizeInventory = 19;
        }
        
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
