package essentialcraft.common.block;

import DummyCore.Client.IModelRegisterer;
import essentialcraft.api.IColdBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFancy extends Block implements IColdBlock, IModelRegisterer {

	public static final String[] overlays = {"ancientTile","bigTile","brick","fancyTile","pressuredTile","smallTiles","temple","tiles","futuristicTile","machine","runic","netherStar","plate","packedPlate","doublePlate","gem"};

	public static final PropertyEnum<FancyBlockType> TYPE = PropertyEnum.<FancyBlockType>create("type", FancyBlockType.class);

	public BlockFancy(Material material) {
		super(material);
		if(material == Material.ROCK)
			this.setSoundType(SoundType.STONE);
		if(material == Material.ANVIL)
			this.setSoundType(SoundType.ANVIL);
		if(material == Material.CLAY || material == Material.GROUND)
			this.setSoundType(SoundType.GROUND);
		if(material == Material.CLOTH)
			this.setSoundType(SoundType.CLOTH);
		if(material == Material.CRAFTED_SNOW)
			this.setSoundType(SoundType.SNOW);
		if(material == Material.WOOD)
			this.setSoundType(SoundType.WOOD);
		if(material == Material.GLASS)
			this.setSoundType(SoundType.GLASS);
		setDefaultState(blockState.getBaseState().withProperty(TYPE, FancyBlockType.ANCIENT_TILE));
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState s)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this,1,state.getValue(TYPE).getIndex());
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
	{
		for(int i = 0; i < 16; ++i)
			list.add(new ItemStack(this, 1, i));
	}

	@Override
	public float getColdModifier(IBlockAccess w, BlockPos pos)  {
		return this == BlocksCore.fancyBlocks.get(3) ? 0.5F : 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(TYPE, FancyBlockType.fromIndex(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(TYPE).getIndex();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelLoader.setCustomStateMapper(this, new FancyBlockStateMapper());
		for(int i = 0; i < 16; i++) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i, new ModelResourceLocation("essentialcraft:" + getRegistryName().getResourcePath().replace('.', '/'), "type=" + FancyBlockType.fromIndex(i).getName()));
		}
	}

	public static enum FancyBlockType implements IStringSerializable {
		ANCIENT_TILE("ancient_tile"),
		BIG_TILE("big_tile"),
		BRICK("brick"),
		FANCY_TILE("fancy_tile"),
		PRESSURED_TILE("pressured_tile"),
		SMALL_TILES("small_tiles"),
		TEMPLE("temple"),
		TILES("tiles"),
		FUTURISTIC_TILE("futuristic_tile"),
		MACHINE("machine"),
		RUNIC("runic"),
		NETHER_STAR("nether_star"),
		PLATE("plate"),
		PACKED_PLATE("packed_plate"),
		DOUBLE_PLATE("double_plate"),
		GEM("gem");

		private int index;
		private String name;

		private FancyBlockType(String s) {
			index = ordinal();
			name = s;
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

		public static FancyBlockType fromIndex(int i) {
			return values()[i%16];
		}
	}

	public static class FancyBlockStateMapper extends StateMapperBase {
		@Override
		protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
			return new ModelResourceLocation("essentialcraft:" + state.getBlock().getRegistryName().getResourcePath().replace('.', '/'), "type=" + state.getValue(TYPE).getName());
		}
	}
}
