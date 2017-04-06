package ec3.common.block;

import DummyCore.Client.IModelRegisterer;
import ec3.common.tile.TileSolarPrism;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockSolarPrism extends BlockContainer implements IModelRegisterer {

	public static final AxisAlignedBB BLOCK_AABB = new AxisAlignedBB(-1,0.4F,-1,2,0.6F,2);

	protected BlockSolarPrism(Material p_i45394_1_) {
		super(p_i45394_1_);
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
		return BlockRenderLayer.TRANSLUCENT;
	}
	
	public EnumBlockRenderType getRenderType(IBlockState s)
	{
		return EnumBlockRenderType.MODEL;
	}
	
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return BLOCK_AABB;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileSolarPrism();
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:solarPrism", "inventory"));
	}
}
