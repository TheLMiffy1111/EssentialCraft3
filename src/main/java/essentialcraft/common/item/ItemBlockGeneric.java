package essentialcraft.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlockGeneric extends ItemBlock {

	public ItemBlockGeneric(Block block) {
		super(block);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public int getMetadata(int par1) {
		return par1;
	}
}
