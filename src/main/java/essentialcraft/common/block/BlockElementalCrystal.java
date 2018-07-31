package essentialcraft.common.block;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Client.ModelUtils;
import DummyCore.Utils.MiscUtils;
import essentialcraft.common.tile.TileElementalCrystal;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockElementalCrystal extends BlockContainer implements IModelRegisterer {

	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	public BlockElementalCrystal() {
		super(Material.ROCK);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN));
	}

	@Override
	public boolean isOpaqueCube(IBlockState s) {
		return false;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		ItemStack is = new ItemStack(this, 1);
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileElementalCrystal) {
			TileElementalCrystal crystal = (TileElementalCrystal)te;
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setDouble("size", crystal.size);
			nbt.setDouble("fire", crystal.fire);
			nbt.setDouble("water", crystal.water);
			nbt.setDouble("earth", crystal.earth);
			nbt.setDouble("air", crystal.air);
			is.setTagCompound(nbt);
		}
		return is;
	}

	@Override
	public boolean isFullCube(IBlockState s) {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileElementalCrystal();
	}

	@Override
	public IBlockState getStateForPlacement(World w, BlockPos p, EnumFacing side, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(FACING, side);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileElementalCrystal) {
			double size = 0;
			double fire = 0;
			double water = 0;
			double earth = 0;
			double air = 0;
			NBTTagCompound nbt = stack.getTagCompound();
			if(nbt != null) {
				size = nbt.getDouble("size");
				fire = nbt.getDouble("fire");
				water = nbt.getDouble("water");
				earth = nbt.getDouble("earth");
				air = nbt.getDouble("air");
			}
			TileElementalCrystal crystal = (TileElementalCrystal)te;
			crystal.size = size;
			crystal.fire = fire;
			crystal.water = water;
			crystal.earth = earth;
			crystal.air = air;
		}
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {
		player.addStat(StatList.getBlockStats(this));
		player.addExhaustion(0.005F);
		if(world.isRemote) {
			return;
		}
		ArrayList<ItemStack> items = Lists.<ItemStack>newArrayList();
		ItemStack is = new ItemStack(this, 1);
		if(te instanceof TileElementalCrystal) {
			TileElementalCrystal crystal = (TileElementalCrystal)te;
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setDouble("size", crystal.size);
			nbt.setDouble("fire", crystal.fire);
			nbt.setDouble("water", crystal.water);
			nbt.setDouble("earth", crystal.earth);
			nbt.setDouble("air", crystal.air);
			is.setTagCompound(nbt);
		}
		items.add(is);
		ForgeEventFactory.fireBlockHarvesting(items, world, pos, state, 0, 1F, true, player);
		for(ItemStack item : items) {
			spawnAsEntity(world, pos, item);
		}
	}

	@Override
	public int getLightValue(IBlockState s, IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileElementalCrystal) {
			TileElementalCrystal crystal = (TileElementalCrystal)te;
			double size = crystal.size;
			double floatSize = size/100;
			int light = (int)(floatSize*15);
			return light;
		}
		return getLightValue(s);
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
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		list.add(new ItemStack(this, 1));
		for(int i = 0; i < 5; ++i) {
			for(int j = 0; j < 4; ++j) {
				ItemStack crystalStack = new ItemStack(this, 1);
				NBTTagCompound tag = new NBTTagCompound();
				tag.setDouble("size", i*25);
				double[] elements = {0, 0, 0, 0};
				elements[j] = 100F;
				tag.setDouble("fire", elements[0]);
				tag.setDouble("water", elements[1]);
				tag.setDouble("earth", elements[2]);
				tag.setDouble("air", elements[3]);
				crystalStack.setTagCompound(tag);
				list.add(crystalStack);
			}{
				ItemStack crystalStack = new ItemStack(this, 1);
				NBTTagCompound tag = new NBTTagCompound();
				tag.setDouble("size", i*25);
				double[] elements = {100, 100, 100, 100};
				tag.setDouble("fire", elements[0]);
				tag.setDouble("water", elements[1]);
				tag.setDouble("earth", elements[2]);
				tag.setDouble("air", elements[3]);
				crystalStack.setTagCompound(tag);
				list.add(crystalStack);
			}{
				ItemStack crystalStack = new ItemStack(this, 1);
				NBTTagCompound tag = new NBTTagCompound();
				tag.setDouble("size", i*25);
				double[] elements = {0, 0, 0, 0};
				tag.setDouble("fire", elements[0]);
				tag.setDouble("water", elements[1]);
				tag.setDouble("earth", elements[2]);
				tag.setDouble("air", elements[3]);
				crystalStack.setTagCompound(tag);
				list.add(crystalStack);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(FACING).build());
		if(!Loader.isModLoaded("codechickenlib")) {
			ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(this), new MeshDefinitionElementalCrystal());
			for(int i = 0; i <= 20; i++) {
				ModelBakery.registerItemVariants(Item.getItemFromBlock(this), new ModelResourceLocation("essentialcraft:elementalcrystalinv", "size=" + i));
			}
		}
		else {
			ModelUtils.setItemModelSingleIcon(Item.getItemFromBlock(this), "essentialcraft:elementalcrystal");
		}
	}

	public static class MeshDefinitionElementalCrystal implements ItemMeshDefinition {
		@Override
		public ModelResourceLocation getModelLocation(ItemStack stack) {
			float size = 0;
			if(MiscUtils.getStackTag(stack) != null) {
				size = MiscUtils.getStackTag(stack).getFloat("size");
			}

			return new ModelResourceLocation("essentialcraft:elementalcrystalinv", "size=" + MathHelper.floor(size)/5);
		}
	}
}
