package ec3.common.block;

import java.util.List;

import ec3.common.block.BlockFancy.FancyBlockType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
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
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class BlockFancyMimic extends BlockMimic {

	public static final PropertyEnum<FancyBlockType> TYPE = PropertyEnum.<FancyBlockType>create("type", FancyBlockType.class);
	
	public BlockFancyMimic() {
		super();
		setDefaultState(blockState.getBaseState().withProperty(TYPE, FancyBlockType.ANCIENTTILE));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(TYPE).getIndex();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(TYPE, FancyBlockType.fromIndex(meta));
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
		for(int i = 0; i < 16; i++) {
			list.add(new ItemStack(itemIn, 1, i));
		}
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this,1,state.getValue(TYPE).getIndex());
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new ExtendedBlockState(this, new IProperty[] {TYPE}, new IUnlistedProperty[] {STATE, WORLD, POS});
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return getBoundingBox(blockState, worldIn, pos);
	}

	@Override
	public void registerModels() {
		for(int i = 0; i < 16; i++) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i, new ModelResourceLocation("essentialcraft:fancyBlock/mimicInv", "type=" + FancyBlockType.fromIndex(i).getName()));
			ModelBakery.registerItemVariants(Item.getItemFromBlock(this), new ModelResourceLocation("essentialcraft:fancyBlock/mimic", "type=" + FancyBlockType.fromIndex(i).getName()), new ModelResourceLocation("essentialcraft:fancyBlock/mimic", "internal=" + FancyBlockType.fromIndex(i).getName()));
		}
		ModelLoader.setCustomStateMapper(this, new FancyMimicStateMapper());
	}
	
	public static class FancyMimicStateMapper extends StateMapperBase {
		@Override
		protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
			return new ModelResourceLocation("essentialcraft:fancyBlock/mimic", "internal=" + state.getValue(TYPE).getName());
		}
	}
}
