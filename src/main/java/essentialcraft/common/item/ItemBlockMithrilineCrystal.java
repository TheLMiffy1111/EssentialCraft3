package essentialcraft.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockMithrilineCrystal extends ItemBlock {

	public ItemBlockMithrilineCrystal(Block block) {
		super(block);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public int getMetadata(int meta) {
		return meta;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		int meta = stack.getItemDamage()/3;
		String added = "mithriline";
		if(meta == 1)
			added = "pale";
		if(meta == 2)
			added = "void";
		if(meta == 3)
			added = "demonic";
		if(meta == 4)
			added = "shade";
		return super.getUnlocalizedName(stack)+"."+added;
	}
}
