package essentialcraft.common.block;

import DummyCore.Client.IModelRegisterer;
import essentialcraft.api.EnumDropType;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockCompressedDrops extends Block implements IModelRegisterer {

	public static final PropertyEnum<EnumDropType> TYPE = PropertyEnum.<EnumDropType>create("type", EnumDropType.class);

	public BlockCompressedDrops() {
		super(Material.ROCK);
		setSoundType(SoundType.METAL);
		setDefaultState(blockState.getBaseState().withProperty(TYPE, EnumDropType.ELEMENTAL));
	}

	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.getValue(TYPE).getMapColor();
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		for(int i = 0; i < 6; ++i)
			list.add(new ItemStack(this, 1, i));
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(TYPE).getIndex();
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this,1,state.getValue(TYPE).getIndex());
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(TYPE, EnumDropType.fromIndex(meta));
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
		for(int i = 0; i < EnumDropType.values().length; i++) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i, new ModelResourceLocation("essentialcraft:compressed", "type=" + EnumDropType.fromIndex(i)));
		}
	}
}
