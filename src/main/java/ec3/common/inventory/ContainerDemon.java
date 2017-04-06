package ec3.common.inventory;

import ec3.common.entity.EntityDemon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerDemon extends Container {

	public EntityDemon trader;
	
	public ContainerDemon(EntityPlayer p, EntityDemon inv) {
		trader = inv;
		
		addSlotToContainer(new SlotGeneric(inv, 0, 80, 30));
		int i;
		
		for(i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(p.inventory, j + i*9 + 9, 8 + j*18, 84 + i*18));
			}
		}
		
		for(i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(p.inventory, i, 8 + i*18, 142));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer p) {
		return trader.isUsableByPlayer(p);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {
		ItemStack itemstack = null;
		Slot slot = (Slot)inventorySlots.get(p_82846_2_);
		
		if(slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if(p_82846_2_ < trader.getSizeInventory()) {
				if(!mergeItemStack(itemstack1, trader.getSizeInventory(), 36+trader.getSizeInventory(), true)) {
					if(itemstack1.stackSize == 0)
						slot.putStack((ItemStack)null);
					slot.onSlotChange(itemstack1, itemstack);
					return null;
				}
			}
			else if(p_82846_2_ >= trader.getSizeInventory()) {
				for(int i = 0; i < trader.getSizeInventory(); ++i) {
					if(mergeItemStack(itemstack1, i, i+1, false)) {
						if(itemstack1.stackSize == 0)
							slot.putStack((ItemStack)null);
						return null;
					}
				}
			}
			
			if(p_82846_2_ >= trader.getSizeInventory() && p_82846_2_ < 27+trader.getSizeInventory()) {
				if(!mergeItemStack(itemstack1, 27+trader.getSizeInventory(), 36+trader.getSizeInventory(), false)) {
					if(itemstack1.stackSize == 0)
						slot.putStack((ItemStack)null);
					return null;
				}
			}
			else if(p_82846_2_ >= 27+trader.getSizeInventory() && p_82846_2_ < 36+trader.getSizeInventory() && !mergeItemStack(itemstack1, trader.getSizeInventory(), 27+trader.getSizeInventory(), false)) {
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
