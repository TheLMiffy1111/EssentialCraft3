package ec3.common.block;

import java.util.List;
import java.util.Random;

import DummyCore.Client.IModelRegisterer;
import ec3.common.tile.TileCorruption;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockCorruption_Light extends BlockContainer implements IModelRegisterer {

	public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 7);
	public static final PropertyBool DOWN = PropertyBool.create("down");
	public static final PropertyBool UP = PropertyBool.create("up");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool EAST = PropertyBool.create("east");
	public static final PropertyBool WEST = PropertyBool.create("west");
	public String textureName;

	protected BlockCorruption_Light(Material p_i45394_1_) {
		super(p_i45394_1_);
		setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, 0).withProperty(DOWN, false).withProperty(UP, false).withProperty(SOUTH, false).withProperty(NORTH, false).withProperty(EAST, false).withProperty(WEST, false));
	}

	public boolean isOpaqueCube(IBlockState s)
	{
		return false;
	}

	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}

	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return null;
	}

	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	public EnumBlockRenderType getRenderType(IBlockState s)
	{
		return EnumBlockRenderType.MODEL;
	}

	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List<ItemStack> p_149666_3_)
	{
		for(int i = 0; i < 8; ++i)
			p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this,1,state.getValue(LEVEL));
	}

	@Override
	public int damageDropped(IBlockState p_149692_1_)
	{
		return p_149692_1_.getValue(LEVEL);
	}

	@Override
	public Item getItemDropped(IBlockState p_149650_1_, Random p_149650_2_, int p_149650_3_)
	{
		return null;
	}

	@Override
	public int quantityDropped(Random p_149745_1_)
	{
		return 0;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileCorruption();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(LEVEL, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(LEVEL);
	}

	public BlockCorruption_Light setBlockTextureName(String string) {
		textureName = string;
		return this;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, LEVEL, DOWN, UP, SOUTH, NORTH, EAST, WEST);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.
				withProperty(DOWN, world.getBlockState(pos.down()).isBlockNormalCube()).
				withProperty(UP, world.getBlockState(pos.up()).isBlockNormalCube()).
				withProperty(SOUTH, world.getBlockState(pos.south()).isBlockNormalCube()).
				withProperty(NORTH, world.getBlockState(pos.north()).isBlockNormalCube()).
				withProperty(EAST, world.getBlockState(pos.east()).isBlockNormalCube()).
				withProperty(WEST, world.getBlockState(pos.west()).isBlockNormalCube());
	}

	@Override
	public void registerModels() {
		for(int i = 0; i < 8; i++) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i, new ModelResourceLocation("essentialcraft:" + getRegistryName().getResourcePath() + "Inv", "level=" + i));
		}
	}
}
