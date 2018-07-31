package essentialcraft.common.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class SlotGeneric extends Slot {
	public int slot;

	public SlotGeneric(IInventory inv, int slot, int xPos, int yPos) {
		super(inv, slot, xPos, yPos);
		this.slot = slot;
	}

	public SlotGeneric(IInventory inv, int slot, int xPos, int yPos, ResourceLocation background) {
		super(inv, slot, xPos, yPos);
		this.slot = slot;
		this.setBackgroundName(background.toString());
	}

	public SlotGeneric(IInventory inv, int slot, int xPos, int yPos, String background) {
		super(inv, slot, xPos, yPos);
		this.slot = slot;
		this.setBackgroundName(background);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return this.inventory.isItemValidForSlot(this.slot, stack);
	}
}
