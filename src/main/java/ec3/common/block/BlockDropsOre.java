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
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockDropsOre extends Block implements IModelRegisterer {

	private Random rand = new Random(System.currentTimeMillis());
	public static final PropertyEnum<EnumDropType> TYPE = PropertyEnum.<EnumDropType>create("type", EnumDropType.class, EnumDropType.NORMAL);
	public static final PropertyEnum<OreDimensionType> DIMENSION = PropertyEnum.<OreDimensionType>create("dimension", OreDimensionType.class);

	@Override
	public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune)
	{
		return MathHelper.getInt(rand, 0, 2);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this,1,state.getValue(DIMENSION).getIndex()*5+state.getValue(TYPE).getIndexOre());
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		return true;
	}

	public BlockDropsOre() {
		super(Material.ROCK);
		setDefaultState(blockState.getBaseState().withProperty(TYPE, EnumDropType.ELEMENTAL).withProperty(DIMENSION, OreDimensionType.OVERWORLD));
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List<ItemStack> p_149666_3_)
	{
		for(int i = 0; i < 15; ++i)
			p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		int value = state.getValue(TYPE).getIndexOre();
		return value == 0 ? 4 : value-1;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return ItemsCore.drops;
	}

	public ArrayList<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

		int count = rand.nextInt(2*(fortune+1))+1;
		for(int i = 0; i < count; i++)
		{
			Item item = getItemDropped(state, rand, fortune);
			if(item != null)
			{
				ret.add(new ItemStack(item, 1, damageDropped(state)));
			}
		}
		return ret;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(DIMENSION, OreDimensionType.fromIndex(meta%15/5)).withProperty(TYPE, EnumDropType.fromIndexOre(meta%5));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(DIMENSION).getIndex()*5+state.getValue(TYPE).getIndexOre();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE, DIMENSION);
	}

	@Override
	public void registerModels() {
		for(int i = 0; i < OreDimensionType.values().length; i++) {
			for(int j = 0; j < EnumDropType.values().length; j++) {
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i*EnumDropType.values().length+j, new ModelResourceLocation("essentialcraft:oreDrops", "dimension=" + OreDimensionType.fromIndex(i).getName() + "," + "type=" + EnumDropType.fromIndexOre(j).getName()));
			}
		}
	}

	public static enum OreDimensionType implements IStringSerializable {
		OVERWORLD(0, "overworld"),
		NETHER(1, "nether"),
		END(2, "end");

		private int index;
		private String name;

		private OreDimensionType(int i, String s) {
			index = i;
			name = s;
		}

		public String getName() {
			return name;
		}

		public String toString() {
			return name;
		}

		public int getIndex() {
			return index;
		}

		public static OreDimensionType fromIndex(int i) {
			return values()[i%3];
		}
	}
}
