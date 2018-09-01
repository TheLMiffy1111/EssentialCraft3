package essentialcraft.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemWindSword extends ItemSwordEC {

	public ItemWindSword(ToolMaterial m)
	{
		super(m);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand p_77648_6_, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return ItemsCore.wind_elemental_hoe.onItemUse(player, world, pos, p_77648_6_, facing, hitX, hitY, hitZ);
	}
}
