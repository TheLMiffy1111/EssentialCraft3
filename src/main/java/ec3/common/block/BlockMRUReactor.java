package ec3.common.block;

import DummyCore.Client.IModelRegisterer;
import ec3.common.tile.TileMRUReactor;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockMRUReactor extends BlockContainer implements IModelRegisterer {

	protected BlockMRUReactor(Material p_i45386_1_) {
		super(p_i45386_1_);
	}

	public BlockMRUReactor() {
		super(Material.ROCK);
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

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileMRUReactor();
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:reactor", "inventory"));
	}
}
