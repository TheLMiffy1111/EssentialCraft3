package ec3.common.item;

import DummyCore.Client.IModelRegisterer;
import ec3.common.world.WorldGenMRUTower;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;

public class TestItem_EC extends Item implements IModelRegisterer {

	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(!worldIn.isRemote)
		{
			new WorldGenMRUTower().generate(worldIn, worldIn.rand, pos);
			worldIn.markBlockRangeForRenderUpdate(pos.getX()-32, Math.max(Loader.isModLoaded("cubicchunks") ? Integer.MIN_VALUE : 0, pos.getY()-32), pos.getZ()-32, pos.getX()+32, Math.min(Loader.isModLoaded("cubicchunks") ? Integer.MAX_VALUE : 255, pos.getY()+32), pos.getZ()+32);
		}
		return EnumActionResult.PASS;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("minecraft:blaze_rod", "inventory"));
	}
}
