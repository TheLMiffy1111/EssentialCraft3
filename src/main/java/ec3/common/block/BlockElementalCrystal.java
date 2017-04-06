package ec3.common.block;

import java.util.List;
import java.util.Random;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Client.ModelUtils;
import DummyCore.Utils.MiscUtils;
import ec3.common.tile.TileElementalCrystal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;

public class BlockElementalCrystal extends BlockContainer implements IModelRegisterer {

	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	public BlockElementalCrystal(Material p_i45394_1_) {
		super(p_i45394_1_);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN));
	}

	public BlockElementalCrystal() {
		super(Material.ROCK);
	}

	public boolean isOpaqueCube(IBlockState s) {
		return false;
	}

	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		ItemStack is = new ItemStack(this, 1);
		TileElementalCrystal c = (TileElementalCrystal)world.getTileEntity(pos);
		if(c != null) {
			MiscUtils.getStackTag(is).setFloat("size", c.size);
			MiscUtils.getStackTag(is).setFloat("fire", c.fire);
			MiscUtils.getStackTag(is).setFloat("water", c.water);
			MiscUtils.getStackTag(is).setFloat("earth", c.earth);
			MiscUtils.getStackTag(is).setFloat("air", c.air);
		}
		return is;
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.SOLID;
	}

	public boolean isFullCube(IBlockState s)
	{
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileElementalCrystal();
	}

	@Override
	public IBlockState getStateForPlacement(World w, BlockPos p, EnumFacing side, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(FACING, side);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		ItemStack is = new ItemStack(this, 1);
		TileElementalCrystal c = (TileElementalCrystal)world.getTileEntity(pos);
		if(c != null) {
			MiscUtils.getStackTag(is).setFloat("size", c.size);
			MiscUtils.getStackTag(is).setFloat("fire", c.fire);
			MiscUtils.getStackTag(is).setFloat("water", c.water);
			MiscUtils.getStackTag(is).setFloat("earth", c.earth);
			MiscUtils.getStackTag(is).setFloat("air", c.air);
		}

		spawnAsEntity(world, pos, is);

		world.removeTileEntity(pos);
	}

	public int getLightValue(IBlockState s, IBlockAccess world, BlockPos pos) {
		try {
			Block block = s.getBlock();
			if(block == this) {
				TileElementalCrystal tile = (TileElementalCrystal)world.getTileEntity(pos);
				float size = tile.size;

				float floatSize = size/100F;

				int lightSize = (int) (floatSize*15);

				return lightSize;
			}
			return getLightValue(s);
		}
		catch(Exception e){
			return getLightValue(s);
		}
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta%6));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(FACING).build());
		if(!Loader.isModLoaded("codechickenlib") && !Loader.isModLoaded("CodeChickenLib")) {
			ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(this), new MeshDefinitionElementalCrystal());
			for(int i = 0; i <= 20; i++) {
				ModelBakery.registerItemVariants(Item.getItemFromBlock(this), new ModelResourceLocation("essentialcraft:elementalCrystalInv", "size=" + i));
			}
		}
		else {
			ModelUtils.setItemModelSingleIcon(Item.getItemFromBlock(this), "essentialcraft:elementalCrystal");
		}
	}

	public static class MeshDefinitionElementalCrystal implements ItemMeshDefinition {
		@Override
		public ModelResourceLocation getModelLocation(ItemStack stack) {
			float size = 0;
			if(MiscUtils.getStackTag(stack) != null) {
				size = MiscUtils.getStackTag(stack).getFloat("size");
			}

			return new ModelResourceLocation("essentialcraft:elementalCrystalInv", "size=" + MathHelper.floor(size)/5);
		}
	}
}
