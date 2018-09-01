package essentialcraft.common.item;

import essentialcraft.common.block.BlockRedstoneDeviceNotSided;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockRDNS extends ItemBlock{

	public ItemBlockRDNS(Block block) {
		super(block);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public int getMetadata(int par1) {
		return par1;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName(stack)+"."+BlockRedstoneDeviceNotSided.NAMES[stack.getItemDamage()];
	}
}
