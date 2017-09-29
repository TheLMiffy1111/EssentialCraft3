package ec3.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerPotionSpreader extends Container {

	private IInventory inv;
	public ContainerPotionSpreader(InventoryPlayer par1InventoryPlayer, TileEntity par2) {
		inv = (IInventory)par2;
		addSlotToContainer(new SlotBoundEssence(inv, 0, 155, 23));
		for(int j = 0; j < 4; ++j) {
			for(int k = 0; k < 2; ++k) {
				addSlotToContainer(new SlotGeneric(inv, j + k*4 + 1, 26 + j*18, 23 + k*18));
			}
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
		Slot slot = inventorySlots.get(p_82846_2_);

		if(slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if(p_82846_2_ < inv.getSizeInventory()) {
				if(!mergeItemStack(itemstack1, inv.getSizeInventory(), 36+inv.getSizeInventory(), true)) {
					if(itemstack1.stackSize == 0)
						slot.putStack((ItemStack)null);
					slot.onSlotChange(itemstack1, itemstack);
					return null;
				}
			}
			else if(p_82846_2_ >= inv.getSizeInventory()) {
				for(int i = 0; i < inv.getSizeInventory(); ++i) {
					if(mergeItemStack(itemstack1, i, i+1, false)) {
						if(itemstack1.stackSize == 0)
							slot.putStack((ItemStack)null);
						return null;
					}
				}
			}

			if(p_82846_2_ >= inv.getSizeInventory() && p_82846_2_ < 27+inv.getSizeInventory()) {
				if(!mergeItemStack(itemstack1, 27+inv.getSizeInventory(), 36+inv.getSizeInventory(), false)) {
					if(itemstack1.stackSize == 0)
						slot.putStack((ItemStack)null);
					return null;
				}
			}
			else if(p_82846_2_ >= 27+inv.getSizeInventory() && p_82846_2_ < 36+inv.getSizeInventory() && !mergeItemStack(itemstack1, inv.getSizeInventory(), 27+inv.getSizeInventory(), false)) {
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