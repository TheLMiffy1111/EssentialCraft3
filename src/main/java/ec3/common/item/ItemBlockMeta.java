package ec3.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockMeta extends ItemBlockGeneric {

	public ItemBlockMeta(Block block) {
		super(block);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName(stack)+"."+stack.getItemDamage();
	}
}
