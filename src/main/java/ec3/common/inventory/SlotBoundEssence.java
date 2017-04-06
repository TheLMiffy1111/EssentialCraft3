package ec3.common.inventory;

import ec3.common.item.ItemBoundGem;
import ec3.common.item.ItemDrop;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class SlotBoundEssence extends Slot {
	public SlotBoundEssence(IInventory par2IInventory, int par3, int par4, int par5) {
		super(par2IInventory, par3, par4, par5);
		setBackgroundName("essentialcraft:items/elemental/drop_air");
	}
	
	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {
		return par1ItemStack.getItem() instanceof ItemBoundGem;
	}
}
