package ec3.common.block;

import java.util.List;

import DummyCore.Blocks.Properties.UnlistedPropertyObject;
import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.MiscUtils;
import ec3.common.mod.EssentialCraftCore;
import ec3.common.tile.TileRightClicker;
import ec3.utils.cfg.Config;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class BlockRightClicker extends BlockContainer implements IModelRegisterer {

	public static final PropertyEnum<ActivatorType> TYPE = PropertyEnum.<ActivatorType>create("type", ActivatorType.class);
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	public static final UnlistedPropertyObject<IBlockState> STATE = new UnlistedPropertyObject<IBlockState>("state", IBlockState.class);
	public static final UnlistedPropertyObject<IBlockAccess> WORLD = new UnlistedPropertyObject<IBlockAccess>("world", IBlockAccess.class);
	public static final UnlistedPropertyObject<BlockPos> POS = new UnlistedPropertyObject<BlockPos>("pos", BlockPos.class);

	public BlockRightClicker() {
		super(Material.ROCK);
		setDefaultState(blockState.getBaseState().withProperty(TYPE, ActivatorType.NORMAL).withProperty(FACING, EnumFacing.NORTH));
	}
	
	public EnumBlockRenderType getRenderType(IBlockState s)
	{
		return EnumBlockRenderType.MODEL;
	}
	
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
    {
        return true;
    }
	
	public boolean isOpaqueCube(IBlockState s)
	{
		return false;
	}

	public int damageDropped(IBlockState meta)
	{
		return meta.getValue(TYPE).getIndex();
	}

	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List<ItemStack> p_149666_3_) {
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 0));
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 1));
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 2));
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 3));
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 4));
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 5));
	}

	@Override
	public void breakBlock(World par1World, BlockPos par2Pos, IBlockState par3State) {
		MiscUtils.dropItemsOnBlockBreak(par1World, par2Pos.getX(), par2Pos.getY(), par2Pos.getZ(), par3State.getBlock(), 0);
		super.breakBlock(par1World, par2Pos, par3State);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int metadata) {
		return new TileRightClicker();
	}

	public void getStateForPlacementBy(World w, BlockPos p, IBlockState s, EntityLivingBase placer, ItemStack p_149689_6_)
	{
		int l = BlockPistonBase.getFacingFromEntity(p, placer).getIndex();
		TileEntity tile = w.getTileEntity(p);
		if(tile != null && tile instanceof TileRightClicker)
			((TileRightClicker)tile).rotation = l;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new ExtendedBlockState(this, new IProperty[] {TYPE, FACING}, new IUnlistedProperty[] {STATE, WORLD, POS});
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
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(TYPE, ActivatorType.fromIndex(meta%6));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(TYPE).getIndex();
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity tile = world instanceof ChunkCache ? ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
		if(tile != null && tile instanceof TileRightClicker)
			return state.withProperty(FACING, ((TileRightClicker)tile).getRotation());
		return state;
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		state = ((IExtendedBlockState)getActualState(state, world, pos)).withProperty(WORLD, world).withProperty(POS, pos);
		TileEntity tile = world.getTileEntity(pos);
		if(tile != null && tile instanceof TileRightClicker)
			return ((IExtendedBlockState)state).withProperty(STATE, ((TileRightClicker)tile).getState());
		return state;
	}

	@Override
	public void registerModels() {
		for(int i = 0; i < 6; i++) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i, new ModelResourceLocation("essentialcraft:rightClicker", "facing=north,type=" + ActivatorType.fromIndex(i)));
			for(int j = 0; j < 6; j++) {
				if(j == 2)
					continue;
				ModelBakery.registerItemVariants(Item.getItemFromBlock(this), new ModelResourceLocation("essentialcraft:rightClicker", "facing="+ EnumFacing.getFront(j).getName() +",type=" + ActivatorType.fromIndex(i)));
			}
		}
		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(FACING, TYPE).build());
	}

	public static enum ActivatorType implements IStringSerializable {
		NORMAL("normal"),
		NORMALSNEAKY("normal_sneaky"),
		EXTENDED("extended"),
		EXTENDEDSNEAKY("extended_sneaky"),
		ADVANCED("advanced"),
		ADVANCEDSNEAKY("advanced_sneaky");

		private int index;
		private String name;

		private ActivatorType(String s) {
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

		public static ActivatorType fromIndex(int i) {
			return values()[i%6];
		}
	}
}
