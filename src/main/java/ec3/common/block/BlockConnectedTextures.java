package ec3.common.block;

import DummyCore.Client.IModelRegisterer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.oredict.OreDictionary;

public class BlockConnectedTextures extends Block implements IBlockColor, IModelRegisterer {

	//The blockstate files for these blocks are large.

	public static final String[] field_150921_b = new String[] {"white", "red", "green", "brown", "blue", "purple", "cyan", "lightgray", "gray", "pink", "lime", "yellow", "lightblue", "magenta", "orange", "black"};
	public static final int[] field_150922_c = new int[] {15790320, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 11250603, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844,1973019};
	public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);
	public static final PropertyBool DOWN = PropertyBool.create("down");
	public static final PropertyBool UP = PropertyBool.create("up");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool EAST = PropertyBool.create("east");
	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool WEST = PropertyBool.create("west");

	public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tint)
	{
		int metadata = state.getValue(COLOR).getDyeDamage();
		if(metadata == 0)
			metadata = 15;
		else if(metadata == 15)
			metadata = 0;
		if(metadata == OreDictionary.WILDCARD_VALUE)
			metadata = 0;
		return field_150922_c[metadata];
	}

	public String iconPath;
	public String textureName;

	protected BlockConnectedTextures(Material p_i45394_1_) {
		super(p_i45394_1_);
		setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE).withProperty(DOWN, false).withProperty(UP, false).withProperty(SOUTH, false).withProperty(NORTH, false).withProperty(EAST, false).withProperty(WEST, false));
	}

	public boolean isOpaqueCube(IBlockState s)
	{
		return this.blockMaterial != Material.GLASS;
	}

	public boolean isFullCube(IBlockState s)
	{
		return this.blockMaterial != Material.GLASS;
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return this.blockMaterial == Material.GLASS ? BlockRenderLayer.TRANSLUCENT : BlockRenderLayer.SOLID;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return blockAccess.getBlockState(pos.offset(side)).getBlock() == this ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}

	public BlockConnectedTextures setTexturePath(String p)
	{
		iconPath = p;
		return this;
	}

	public BlockConnectedTextures setBlockTextureName(String p) {
		textureName = p;
		return this;
	}

	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack is, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(is != null && OreDictionary.getOreIDs(is).length > 0 && !(is.getItem() instanceof ItemBlock))
		{
			for(int i = 0; i < OreDictionary.getOreIDs(is).length; ++i)
			{
				String oreDisctName = OreDictionary.getOreName(OreDictionary.getOreIDs(is)[i]);
				if(oreDisctName != null && !oreDisctName.isEmpty() && !oreDisctName.equalsIgnoreCase("unknown"))
				{
					int color = -1;
					for(int i1 = 0; i1 < field_150921_b.length; ++i1)
					{
						String dyeName = "dye"+field_150921_b[i1];
						if(oreDisctName.equalsIgnoreCase(dyeName))
						{
							color = i1;
							break;
						}
					}
					if(color != -1)
					{
						if(color == 0)
							color = 15;
						else if(color == 15)
							color = 0;
						if(player.isSneaking())
						{
							for(int dx = -2; dx <= 2; ++dx)
							{
								for(int dy = -2; dy <= 2; ++dy)
								{
									for(int dz = -2; dz <= 2; ++dz)
									{
										Block b = world.getBlockState(pos.add(dx, dy, dz)).getBlock();
										if(b == this)
										{
											b.recolorBlock(world, pos.add(dx, dy, dz), side, EnumDyeColor.byDyeDamage(color));
											world.markBlocksDirtyVertical(pos.getX(), pos.getZ(), pos.getY()-2, pos.getY()+2);
										}
									}
								}
							}
						}else
						{
							recolorBlock(world, pos, side, EnumDyeColor.byDyeDamage(color));
							world.markBlocksDirtyVertical(pos.getX(), pos.getZ(), pos.getY()-2, pos.getY()+2);
						}
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean recolorBlock(World world, BlockPos pos, EnumFacing side, EnumDyeColor color)
	{
		int meta = this.getMetaFromState(world.getBlockState(pos));
		if(meta == 0)
			meta = 15;
		else if(meta == 15)
			meta = 0;
		if (meta != color.getDyeDamage())
		{
			meta = color.getDyeDamage();
			if(meta == 0)
				meta = 15;
			else if(meta == 15)
				meta = 0;
			world.setBlockState(pos, this.getStateFromMeta(meta), 3);
			return true;
		}
		return false;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		int color = meta;
		if(color == 0)
			color = 15;
		else if(color == 15)
			color = 0;
		return getDefaultState().withProperty(COLOR, EnumDyeColor.byDyeDamage(color));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int color = state.getValue(COLOR).getDyeDamage();
		if(color == 0)
			color = 15;
		else if(color == 15)
			color = 0;
		return color;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, COLOR, DOWN, UP, SOUTH, EAST, NORTH, WEST);
	}

	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.
				withProperty(DOWN, worldIn.getBlockState(pos.down()).getBlock() == this).
				withProperty(UP, worldIn.getBlockState(pos.up()).getBlock() == this).
				withProperty(SOUTH,  worldIn.getBlockState(pos.south()).getBlock() == this).
				withProperty(EAST,  worldIn.getBlockState(pos.east()).getBlock() == this).
				withProperty(NORTH,  worldIn.getBlockState(pos.north()).getBlock() == this).
				withProperty(WEST,  worldIn.getBlockState(pos.west()).getBlock() == this);
	}

	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		switch(rot)
		{
		case CLOCKWISE_180:
			return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(EAST, state.getValue(WEST)).withProperty(SOUTH, state.getValue(NORTH)).withProperty(WEST, state.getValue(EAST));
		case COUNTERCLOCKWISE_90:
			return state.withProperty(NORTH, state.getValue(EAST)).withProperty(EAST, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(WEST)).withProperty(WEST, state.getValue(NORTH));
		case CLOCKWISE_90:
			return state.withProperty(NORTH, state.getValue(WEST)).withProperty(EAST, state.getValue(NORTH)).withProperty(SOUTH, state.getValue(EAST)).withProperty(WEST, state.getValue(SOUTH));
		default:
			return state;
		}
	}

	public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
	{
		switch (mirrorIn)
		{
		case LEFT_RIGHT:
			return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(NORTH));
		case FRONT_BACK:
			return state.withProperty(EAST, state.getValue(WEST)).withProperty(WEST, state.getValue(EAST));
		default:
			return super.withMirror(state, mirrorIn);
		}
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(COLOR).build());
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:" + getRegistryName().getResourcePath(), "inventory"));
	}
}
