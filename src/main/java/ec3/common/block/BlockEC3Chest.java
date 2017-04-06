package ec3.common.block;

import java.util.List;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.MiscUtils;
import ec3.common.mod.EssentialCraftCore;
import ec3.common.tile.TileMagicalChest;
import ec3.utils.cfg.Config;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockEC3Chest extends BlockContainer implements IModelRegisterer {

	public static final PropertyEnum<ChestType> TYPE = PropertyEnum.<ChestType>create("type", ChestType.class);

	public BlockEC3Chest() {
		super(Material.ROCK);
	}

	public boolean isOpaqueCube(IBlockState s)
	{
		return false;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this,1,state.getValue(TYPE).getIndex());
	}

	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List<ItemStack> p_149666_3_)
	{
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 0));
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 1));
	}

	@Override
	public void breakBlock(World par1World, BlockPos par2Pos, IBlockState par3State) {
		MiscUtils.dropItemsOnBlockBreak(par1World, par2Pos.getX(), par2Pos.getY(), par2Pos.getZ(), par3State.getBlock(), 0);
		super.breakBlock(par1World, par2Pos, par3State);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int metadata) {
		return new TileMagicalChest(metadata);
	}

	public void getStateForPlacementBy(World w, BlockPos p, IBlockState s, EntityLivingBase p_149689_5_, ItemStack p_149689_6_)
	{
		int l = determineOrientation(w, p.getX(), p.getY(), p.getZ(), p_149689_5_);
		TileMagicalChest tile = TileMagicalChest.class.cast(w.getTileEntity(p));
		tile.rotation = l;
		tile.ownerName = p_149689_5_.getCommandSenderEntity().getName();
	}

	public static int determineOrientation(World p_150071_0_, int p_150071_1_, int p_150071_2_, int p_150071_3_, EntityLivingBase p_150071_4_)
	{
		int l = MathHelper.floor((double)(p_150071_4_.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		return l;
	}

	@Override
	public boolean onBlockActivated(World par1World, BlockPos par2, IBlockState par3, EntityPlayer par4EntityPlayer, EnumHand par5, ItemStack par6, EnumFacing par7, float par8, float par9, float par10) {
		if(par1World.isRemote) {
			return true;
		}
		else {
			if(!par4EntityPlayer.isSneaking()) {
				par4EntityPlayer.openGui(EssentialCraftCore.core, Config.guiID[0], par1World, par2.getX(), par2.getY(), par2.getZ());
				return true;
			}
			else {
				return false;
			}
		}
	}

	@Override
	public boolean isFullCube(IBlockState s)
	{
		return false;
	}

	public int damageDropped(IBlockState s)
	{
		return s.getValue(TYPE).getIndex();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(TYPE, ChestType.fromIndex(meta%2));
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
		for(int i = 0; i < ChestType.values().length; i++) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i, new ModelResourceLocation("essentialcraft:chestInv", "type="+ChestType.fromIndex(i).getName()));
		}
	}

	public static enum ChestType implements IStringSerializable {
		MAGICAL(0, "magical"),
		VOID(1, "void");

		private int index;
		private String name;

		private ChestType(int i, String s) {
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

		public static ChestType fromIndex(int i) {
			return values()[i%2];
		}
	}
}
