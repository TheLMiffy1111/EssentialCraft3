package essentialcraft.common.block;

import java.util.ArrayList;
import java.util.Random;

import DummyCore.Client.IModelRegisterer;
import essentialcraft.common.block.BlockDropsOre.OreDimensionType;
import essentialcraft.common.item.ItemsCore;
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
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockMithrilineOre extends Block implements IModelRegisterer {

	public static final PropertyEnum<OreDimensionType> DIMENSION = PropertyEnum.<OreDimensionType>create("dimension", OreDimensionType.class);

	public BlockMithrilineOre() {
		super(Material.ROCK);
		setDefaultState(blockState.getBaseState().withProperty(DIMENSION, OreDimensionType.OVERWORLD));
	}

	@Override
	public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune)
	{
		return MathHelper.getInt(RANDOM, 0, 2);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this,1,state.getValue(DIMENSION).getIndex());
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		return true;
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		for(int i = 0; i < 3; ++i)
			list.add(new ItemStack(this, 1, i));
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 51;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return ItemsCore.genericItem;
	}

	@Override
	public ArrayList<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

		int count = this.RANDOM.nextInt(8*(fortune+1))+3;
		for(int i = 0; i < count; i++) {
			Item item = getItemDropped(state, RANDOM, fortune);
			if(item != null) {
				ret.add(new ItemStack(item, 1, damageDropped(state)));
			}
		}
		return ret;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(DIMENSION, OreDimensionType.fromIndex(meta%3));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(DIMENSION).getIndex();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, DIMENSION);
	}

	@Override
	public void registerModels() {
		for(int i = 0; i < OreDimensionType.values().length; i++) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i, new ModelResourceLocation("essentialcraft:oremithriline", "dimension=" + OreDimensionType.fromIndex(i).getName()));
		}
	}
}
