package ec3.common.block;

import java.util.List;

import DummyCore.Client.IModelRegisterer;
import ec3.api.IColdBlock;
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
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockFancy extends Block implements IColdBlock, IModelRegisterer {

	public static final String[] overlays = new String[]{"ancientTile","bigTile","brick","fancyTile","pressuredTile","smallTiles","temple","tiles","futuristicTile","machine","runic","netherStar","plate","packedPlate","doublePlate","gem"};
	public String modelName = "";

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
		setDefaultState(blockState.getBaseState().withProperty(TYPE, FancyBlockType.ANCIENTTILE));
	}

	public BlockFancy setModelName(String name) {
		modelName = name;
		return this;
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

	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List<ItemStack> p_149666_3_)
	{
		for(int i = 0; i < overlays.length; ++i)
			p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
	}

	@Override
	public float getColdModifier(World w, int x, int y, int z,int meta)  {
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

	@Override
	public void registerModels() {
		ModelLoader.setCustomStateMapper(this, new FancyBlockStateMapper());
		for(int i = 0; i < 16; i++) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i, new ModelResourceLocation("essentialcraft:" + getRegistryName().getResourcePath().replace('.', '/'), "type=" + FancyBlockType.fromIndex(i).getName()));
		}
	}
	
	public static enum FancyBlockType implements IStringSerializable {
		ANCIENTTILE("ancient_tile"),
		BIGTILE("big_tile"),
		BRICK("brick"),
		FANCYTILE("fancy_tile"),
		PRESSUREDTILE("pressured_tile"),
		SMALLTILES("small_tiles"),
		TEMPLE("temple"),
		TILES("tiles"),
		FUTURISTICTILE("futuristic_tile"),
		MACHINE("machine"),
		RUNIC("runic"),
		NETHERSTAR("nether_star"),
		PLATE("plate"),
		PACKEDPLATE("packed_plate"),
		DOUBLEPLATE("double_plate"),
		GEM("gem");
		
		private int index;
		private String name;
		
		private FancyBlockType(String s) {
			index = ordinal();
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
