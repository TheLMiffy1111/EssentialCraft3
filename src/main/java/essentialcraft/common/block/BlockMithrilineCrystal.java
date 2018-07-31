package essentialcraft.common.block;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Client.ModelUtils;
import DummyCore.Utils.EnumLayer;
import essentialcraft.common.tile.TileMithrilineCrystal;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMithrilineCrystal extends BlockContainer implements IModelRegisterer {

	public static final PropertyEnum<CrystalType> TYPE = PropertyEnum.<CrystalType>create("type", CrystalType.class);
	public static final PropertyEnum<EnumLayer> LAYER = PropertyEnum.<EnumLayer>create("layer", EnumLayer.class, EnumLayer.LAYERTHREE);

	public static final AxisAlignedBB BLOCK_AABB = new AxisAlignedBB(0.25D,0D,0.25D,0.75D,1D,0.75D);

	public BlockMithrilineCrystal() {
		super(Material.ROCK);
		this.setSoundType(SoundType.GLASS);
		setDefaultState(blockState.getBaseState().withProperty(TYPE, CrystalType.MITHRILINE).withProperty(LAYER, EnumLayer.BOTTOM));
	}

	@Override
	public int damageDropped(IBlockState s)
	{
		return s.getValue(TYPE).getIndex()*3;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return new ItemStack(this, 1, state.getValue(TYPE).getIndex()*3);
	}

	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.getValue(TYPE).getMapColor();
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
	{
		list.add(new ItemStack(this, 1, 0));
		list.add(new ItemStack(this, 1, 3));
		list.add(new ItemStack(this, 1, 6));
		list.add(new ItemStack(this, 1, 9));
		list.add(new ItemStack(this, 1, 12));
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
		super.breakBlock(world, pos, blockstate);
		int par6 = blockstate.getValue(LAYER).getIndexThree();
		if(par6 == 0)
		{
			world.setBlockToAir(pos.up());
			world.setBlockToAir(pos.up(2));
		}
		if(par6 == 1)
		{
			world.setBlockToAir(pos.down());
			world.setBlockToAir(pos.up());
		}
		if(par6 == 2)
		{
			world.setBlockToAir(pos.down());
			world.setBlockToAir(pos.down(2));
		}
	}

	@Override
	public void onBlockAdded(World w, BlockPos p, IBlockState s)
	{
		super.onBlockAdded(w, p, s);
		int meta = getMetaFromState(s);
		if(meta%3 == 0)
		{
			w.setBlockState(p.up(), getStateFromMeta(meta+1), 3);
			w.setBlockState(p.up(2), getStateFromMeta(meta+1), 3);
		}
	}

	@Override
	public boolean canPlaceBlockAt(World p_149742_1_, BlockPos p_149742_2_)
	{
		return p_149742_1_.getBlockState(p_149742_2_).getBlock().isReplaceable(p_149742_1_, p_149742_2_) && p_149742_1_.getBlockState(p_149742_2_.up()).getBlock().isReplaceable(p_149742_1_, p_149742_2_.up()) && p_149742_1_.getBlockState(p_149742_2_.up(2)).getBlock().isReplaceable(p_149742_1_, p_149742_2_.up(2));
	}


	@Override
	public boolean isOpaqueCube(IBlockState s)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState s)
	{
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState s) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return meta%3 == 0 ? new TileMithrilineCrystal(meta/3) : null;
	}

	@Override
	public float getEnchantPowerBonus(World world, BlockPos pos)
	{
		if(world.getBlockState(pos).getValue(LAYER) == EnumLayer.BOTTOM) {
			int meta = world.getBlockState(pos).getValue(TYPE).getIndex();
			return meta == 0 ? 7.5F : meta == 1 ? 15 : meta == 2 ? 30 : meta == 3 ? 60 : meta == 4 ? 120 : 0;
		}
		else
			return 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(TYPE, CrystalType.fromIndex(meta%15/3)).withProperty(LAYER, EnumLayer.fromIndexThree(meta%3));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(TYPE).getIndex()*3+state.getValue(LAYER).getIndexThree();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE, LAYER);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BLOCK_AABB;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(LAYER).build());
		if(!Loader.isModLoaded("codechickenlib")) {
			for(int i = 0; i < 5; i++) {
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i*3, new ModelResourceLocation("essentialcraft:mithrilinecrystalinv", "type=" + CrystalType.fromIndex(i).getName()));
			}
		}
		else {
			ModelUtils.setItemModelSingleIcon(Item.getItemFromBlock(this), "essentialcraft:mithrilinecrystal");
		}
	}

	public static enum CrystalType implements IStringSerializable {
		MITHRILINE(0, "mithriline", MapColor.GREEN),
		PALE(1, "pale", MapColor.LAPIS),
		VOID(2, "void", MapColor.BLACK),
		DEMONIC(3, "demonic", MapColor.RED),
		SHADE(4, "shade", MapColor.GRAY);

		private final int index;
		private final String name;
		private final MapColor mapColor;

		private CrystalType(int index, String name, MapColor mapColor) {
			this.index = index;
			this.name = name;
			this.mapColor = mapColor;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return name;
		}

		public int getIndex() {
			return index;
		}

		public MapColor getMapColor() {
			return mapColor;
		}

		public static CrystalType fromIndex(int i) {
			return values()[i];
		}
	}
}
