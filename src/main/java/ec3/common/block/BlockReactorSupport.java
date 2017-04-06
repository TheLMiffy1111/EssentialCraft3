package ec3.common.block;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.EnumLayer;
import ec3.common.tile.TileMRUReactor;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.model.ModelLoader;

public class BlockReactorSupport extends Block implements IModelRegisterer {

	public static final PropertyEnum<EnumLayer> LAYER = PropertyEnum.<EnumLayer>create("layer", EnumLayer.class, EnumLayer.LAYERTHREE);
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool EAST = PropertyBool.create("east");
	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool WEST = PropertyBool.create("west");

	public BlockReactorSupport() {
		super(Material.ROCK);
		setDefaultState(blockState.getBaseState().withProperty(SOUTH, false).withProperty(EAST, false).withProperty(NORTH, false).withProperty(WEST, false).withProperty(LAYER, EnumLayer.BOTTOM));
	}

	public boolean isOpaqueCube(IBlockState s)
	{
		return false;
	}

	public boolean isFullCube(IBlockState s)
	{
		return false;
	}

	public EnumBlockRenderType getRenderType(IBlockState s)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, LAYER, SOUTH, EAST, NORTH, WEST);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		Block b0 = world.getBlockState(pos.down()).getBlock();
		Block b1 = world.getBlockState(pos.down(2)).getBlock();
		TileEntity ste = world instanceof ChunkCache ? ((ChunkCache)world).getTileEntity(pos.south(), Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos.south());
		TileEntity ete = world instanceof ChunkCache ? ((ChunkCache)world).getTileEntity(pos.east(), Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos.east());
		TileEntity nte = world instanceof ChunkCache ? ((ChunkCache)world).getTileEntity(pos.north(), Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos.north());
		TileEntity wte = world instanceof ChunkCache ? ((ChunkCache)world).getTileEntity(pos.west(), Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos.west());
		boolean s = (ste != null && ste instanceof TileMRUReactor && ((TileMRUReactor)ste).isStructureCorrect);
		boolean e = (ete != null && ete instanceof TileMRUReactor && ((TileMRUReactor)ete).isStructureCorrect);
		boolean n = (nte != null && nte instanceof TileMRUReactor && ((TileMRUReactor)nte).isStructureCorrect);
		boolean w = (wte != null && wte instanceof TileMRUReactor && ((TileMRUReactor)wte).isStructureCorrect);

		return state.
				withProperty(LAYER, b0 == this ? b1 == this ? EnumLayer.fromIndexThree(2) : EnumLayer.fromIndexThree(1) : EnumLayer.fromIndexThree(0)).
				withProperty(SOUTH, s).withProperty(EAST, e).withProperty(NORTH, n).withProperty(WEST, w);
	}

	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		switch(rot)
		{
		case CLOCKWISE_180:
			return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(EAST, state.getValue(WEST)).withProperty(SOUTH, state.getValue(NORTH)).withProperty(WEST, state.getValue(EAST));
		case COUNTERCLOCKWISE_90:
			return state.withProperty(NORTH, state.getValue(EAST)).withProperty(EAST, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(WEST)).withProperty(WEST, state.getValue(NORTH));
		case CLOCKWISE_90:
			return state.withProperty(NORTH, state.getValue(WEST)).withProperty(EAST, state.getValue(NORTH)).withProperty(SOUTH, state.getValue(EAST)).withProperty(WEST, state.getValue(SOUTH));
		default:
			return state;
		}
	}

	public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
	{
		switch (mirrorIn)
		{
		case LEFT_RIGHT:
			return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(NORTH));
		case FRONT_BACK:
			return state.withProperty(EAST, state.getValue(WEST)).withProperty(WEST, state.getValue(EAST));
		default:
			return super.withMirror(state, mirrorIn);
		}
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:reactorSupport", "inventory"));
	}
}
