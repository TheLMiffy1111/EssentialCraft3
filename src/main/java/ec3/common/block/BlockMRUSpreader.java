package ec3.common.block;

import java.util.Random;

import DummyCore.Client.IModelRegisterer;
import ec3.common.mod.EssentialCraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockMRUSpreader extends Block implements IModelRegisterer {

	public BlockMRUSpreader(Material p_i45394_1_) {
		super(p_i45394_1_);
		this.setTickRandomly(true);
		this.setLightLevel(1.0F);
	}

	public void randomDisplayTick(IBlockState s, World p_149734_1_, BlockPos p_149734_2_, Random p_149734_5_) {
		for(int i = 0; i < 5; ++i) {
			Vec3d rotateVec = new Vec3d(1, 1, 1);
			rotateVec.rotatePitch(p_149734_5_.nextFloat()*360F);
			rotateVec.rotateYaw(p_149734_5_.nextFloat()*360F);
			rotateVec.rotatePitch(p_149734_5_.nextFloat()*360F);
			rotateVec.rotateYaw(p_149734_5_.nextFloat()*360F);
			for(int i1 = 0; i1 < 10; ++i1)
				EssentialCraftCore.proxy.spawnParticle("mruFX", p_149734_2_.getX()+0.5F, p_149734_2_.getY()+1F, p_149734_2_.getZ()+0.5F, rotateVec.xCoord*10, rotateVec.yCoord*10, rotateVec.zCoord*10);
			rotateVec = null;
		}

	}

	public boolean isOpaqueCube(IBlockState s)
	{
		return false;
	}

	public boolean isFullCube(IBlockState s)
	{
		return false;
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.SOLID;
	}

	public EnumBlockRenderType getRenderType(IBlockState s)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:spreader", "inventory"));
	}
}
