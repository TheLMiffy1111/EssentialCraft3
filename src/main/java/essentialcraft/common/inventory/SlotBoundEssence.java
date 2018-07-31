package essentialcraft.common.inventory;

import essentialcraft.common.item.ItemBoundGem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBoundEssence extends Slot {
	public SlotBoundEssence(IInventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
		setBackgroundName("essentialcraft:items/elemental/drop_air");
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.getItem() instanceof ItemBoundGem;
	}
}
