package essentialcraft.common.item;

import java.util.List;

import essentialcraft.common.block.BlockFancy;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockFancy extends ItemBlock {

	public ItemBlockFancy(Block block) {
		super(block);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public int getMetadata(int par1)
	{
		return par1;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World player, List<String> list, ITooltipFlag par4)
	{
		super.addInformation(stack, player, list, par4);
		list.add(I18n.translateToLocal("essentialcraft.desc.fancy."+BlockFancy.overlays[stack.getItemDamage()]));
	}
}
