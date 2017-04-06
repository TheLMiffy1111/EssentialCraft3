package ec3.common.block;

import java.util.ArrayList;
import java.util.Random;

import DummyCore.Client.IModelRegisterer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDoublePlant.EnumPlantType;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.IShearable;

public class BlockModTallGrass extends BlockBush implements IGrowable, IShearable, IBlockColor, IItemColor, IModelRegisterer {

	public BlockModTallGrass() {
		super(Material.VINE);
		this.setSoundType(SoundType.PLANT);
	}

	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		float f = 0.4F;
		return new AxisAlignedBB(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.8F, 0.5F + f).offset(pos);
	}

	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
		Block b = state.getBlock();
		if(b != null && b instanceof BlockModTallGrass)
			return true;
		return false;
	}

	@Override
	public int colorMultiplier(IBlockState s, IBlockAccess p_149720_1_, BlockPos p_149720_2_, int tint) {
		return BiomeColorHelper.getGrassColorAtPos(p_149720_1_, p_149720_2_);
	}

	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex) {
		return ColorizerGrass.getGrassColor(0.5D, 1.0D);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this,1,0);
	}

	public Item getItemDropped(IBlockState p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return null;
	}

	/**
	 * Returns the usual quantity dropped by the block plus a bonus of 1 to 'i' (inclusive).
	 */
	public int quantityDroppedWithBonus(int p_149679_1_, Random p_149679_2_)
	{
		return 1 + p_149679_2_.nextInt(p_149679_1_ * 2 + 1);
	}

	@Override
	public ArrayList<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState meta, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		if(world instanceof World ? ((World)world).rand.nextInt(8) != 0 : RANDOM.nextInt(8) != 0)
			return ret;
		ItemStack seed = ForgeHooks.getGrassSeed(world instanceof World ? ((World)world).rand : RANDOM, fortune);
		if(seed != null) ret.add(seed);
		return ret;
	}

	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Override
	public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(this, 1, getMetaFromState(world.getBlockState(pos))));
		return ret;
	}

	public boolean canGrow(World p_149851_1_, BlockPos p_149851_2_, IBlockState state, boolean p_149851_5_) {
		return true;
	}

	public boolean canUseBonemeal(World p_149852_1_, Random p_149852_2_, BlockPos p_149852_3_, IBlockState state) {
		return true;
	}

	public void grow(World p_149853_1_, Random p_149853_2_, BlockPos p_149853_3_, IBlockState state) {
		int l = getMetaFromState(state);
		byte b0 = 2;

		if(l == 2) {
			b0 = 3;
		}

		if(Blocks.DOUBLE_PLANT.canPlaceBlockAt(p_149853_1_, p_149853_3_)) {
			Blocks.DOUBLE_PLANT.placeAt(p_149853_1_, p_149853_3_, EnumPlantType.byMetadata(b0), 2);
		}
	}

	protected boolean canSustainBush(IBlockState p_149854_1_)
	{
		return p_149854_1_.getBlock() == Blocks.GRASS || p_149854_1_.getBlock() == Blocks.DIRT || p_149854_1_.getBlock() == Blocks.FARMLAND || p_149854_1_.getBlock() instanceof BlockModTallGrass;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:tallGrass", "inventory"));
	}
}
