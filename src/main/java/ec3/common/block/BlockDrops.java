package ec3.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import DummyCore.Client.IModelRegisterer;
import ec3.api.EnumDropType;
import ec3.common.item.ItemsCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockDrops extends Block implements IModelRegisterer {

	public static final PropertyEnum<EnumDropType> TYPE = PropertyEnum.<EnumDropType>create("type", EnumDropType.class, EnumDropType.CANBEFARMED);
	public static final AxisAlignedBB BLOCK_AABB = new AxisAlignedBB(0,0,0,1,0.1F,1);

	protected BlockDrops(Material p_i45394_1_) {
		super(p_i45394_1_);
		setDefaultState(blockState.getBaseState().withProperty(TYPE, EnumDropType.AIR));
	}

	public boolean isOpaqueCube(IBlockState s)
	{
		return false;
	}

	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return null;
	}

	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
	{
		return BLOCK_AABB.offset(pos);
	}

	public boolean isFullCube(IBlockState s)
	{
		return false;
	}

	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List<ItemStack> p_149666_3_)
	{
		for(int i = 0; i < 4; ++i)
			p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
	}

	@Override
	public int damageDropped(IBlockState p_149692_1_)
	{
		return p_149692_1_.getValue(TYPE).getIndex();
	}

	@Override
	public Item getItemDropped(IBlockState p_149650_1_, Random p_149650_2_, int p_149650_3_)
	{
		return ItemsCore.drops;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this,1,state.getValue(TYPE).getIndex());
	}

	@Override
	public int quantityDropped(Random p_149745_1_)
	{
		return 1+p_149745_1_.nextInt(6);
	}

	public ArrayList<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

		int count = quantityDropped(state, fortune, world instanceof World ? ((World)world).rand : RANDOM);
		for(int i = 0; i < count; i++)
		{
			Item item = getItemDropped(state, world instanceof World ? ((World)world).rand : RANDOM, fortune);
			if(item != null)
			{
				ret.add(new ItemStack(item, 1, damageDropped(state)));
			}
		}
		return ret;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(TYPE, EnumDropType.fromIndex(meta%4));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(TYPE).getIndex();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE);
	}

	@Override
	public void registerModels() {
		for(int i = 0; i < EnumDropType.CANBEFARMED.length; i++) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i, new ModelResourceLocation("essentialcraft:Drops", "type=" + EnumDropType.CANBEFARMED[i].getName()));
		}
	}
}
