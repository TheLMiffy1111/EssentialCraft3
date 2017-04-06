package ec3.common.block;

import java.util.Random;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.MathUtils;
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

public class BlockMRULevitator extends Block implements IModelRegisterer {

	public BlockMRULevitator(Material p_i45394_1_) {
		super(p_i45394_1_);
		this.setTickRandomly(true);
	}

	public void randomDisplayTick(IBlockState s, World p_149734_1_, BlockPos p_149734_2_, Random p_149734_5_) {
		for(int i = 0; i < 10; ++i) {
			EssentialCraftCore.proxy.spawnParticle("mruFX", p_149734_2_.getX()+0.5F+MathUtils.randomFloat(p_149734_5_)/5, p_149734_2_.getY()+0.5F, p_149734_2_.getZ()+0.5F+MathUtils.randomFloat(p_149734_5_)/5, 0, -5-MathUtils.randomFloat(p_149734_5_)*5, 0);
			Vec3d rotateVec = new Vec3d(1, 0, 1);
			rotateVec.rotateYaw(i*36);
			for(int i1 = 0; i1 < 3; ++i1)
				EssentialCraftCore.proxy.spawnParticle("mruFX", p_149734_2_.getX()+0.5F+MathUtils.randomFloat(p_149734_5_)/5, p_149734_2_.getY()+0.25F, p_149734_2_.getZ()+0.5F+MathUtils.randomFloat(p_149734_5_)/5, rotateVec.xCoord, 0, rotateVec.zCoord);
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
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:levitator", "inventory"));
	}
}
