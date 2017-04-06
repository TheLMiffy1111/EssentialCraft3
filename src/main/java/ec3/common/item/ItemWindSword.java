package ec3.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemWindSword extends ItemSword_Mod {

	public ItemWindSword(ToolMaterial m) 
	{
		super(m);
	}

	public EnumActionResult onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, BlockPos p_77648_4_, EnumHand p_77648_6_, EnumFacing p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
	{
		return ItemsCore.wind_elemental_hoe.onItemUse(p_77648_1_, p_77648_2_, p_77648_3_, p_77648_4_, p_77648_6_, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_);
	}
}
