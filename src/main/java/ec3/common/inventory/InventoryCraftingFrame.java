package ec3.common.inventory;

import java.util.UUID;

import DummyCore.Utils.MiscUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;

public class InventoryCraftingFrame implements IInventory {
	
	public ItemStack[] inventory = new ItemStack[10];
	public UUID randomUUID;
	public ItemStack filterStack;
	
	public InventoryCraftingFrame(ItemStack filter) {
		if(!filter.hasTagCompound()) {
			NBTTagCompound theTag = MiscUtils.getStackTag(filter);
			randomUUID = UUID.randomUUID();
			theTag.setString("uniqueID", randomUUID.toString());
		}
		readFromNBTTagCompound(MiscUtils.getStackTag(filter));
		filterStack = filter;
	}
	
	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if(inventory[slot] == null)
			return null;
		ItemStack returnStack;
		if(inventory[slot].stackSize > amount)
			returnStack = inventory[slot].splitStack(amount);
		else {
			returnStack = inventory[slot];
			inventory[slot] = null;
		}
		markDirty();
		return returnStack;
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inventory[slot] = stack;
	}
	
	@Override
	public String getName() {
		return "ec3.inventory.craftingFrame";
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
	public void markDirty() {}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer p_70300_1_) {
		return true;
	}
	
	@Override
	public void openInventory(EntityPlayer p) {}
	
	@Override
	public void closeInventory(EntityPlayer p) {}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return true;
	}
	
	public void readFromNBTTagCompound(NBTTagCompound tag) {
		NBTTagCompound inventoryTag = ((NBTTagCompound)tag.getTag("inventory"));
		if(inventoryTag == null)
			return;
		
		if(randomUUID == null) {
			randomUUID = UUID.fromString(tag.getString("uniqueID"));
			//Not actually sure if this can happen, but it is Java, so the more null checks, the better!
			if(randomUUID == null)
				randomUUID = UUID.randomUUID();
		}
		
		NBTTagList actualInventory = inventoryTag.getTagList("items", 10);
		for(int i = 0; i < actualInventory.tagCount() && i < inventory.length; i++) {
			NBTTagCompound indexTag = (NBTTagCompound) actualInventory.getCompoundTagAt(i);
			int index = indexTag.getInteger("index");
			try {
				inventory[index] = ItemStack.loadItemStackFromNBT(indexTag);
			} 
			catch(Exception e) {
				inventory[index] = null;
			}
		}
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		NBTTagList items = new NBTTagList();
		
		for(int i = 0; i < inventory.length; i++) {
			if(inventory[i] != null) {
				NBTTagCompound indexTag = new NBTTagCompound();
				items.appendTag(indexTag);
				indexTag.setInteger("index", i);
				inventory[i].writeToNBT(indexTag);
			}
		}
		NBTTagCompound inventoryTag = new NBTTagCompound();
		inventoryTag.setTag("items", items);
		tag.setTag("inventory", inventoryTag);
		//Can this ever happen? I guess it only happens on server, so should be impossible, but just in case - here is a NPE check.
		if(randomUUID == null)
			randomUUID = UUID.randomUUID();
		tag.setString("uniqueID", randomUUID.toString());
		return tag;
	}

	@Override
	public ITextComponent getDisplayName() {
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack returnStack = getStackInSlot(index);
		setInventorySlotContents(index, null);
		return returnStack;
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
}
