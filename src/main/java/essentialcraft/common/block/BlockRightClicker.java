package essentialcraft.common.block;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.UnlistedPropertyObject;
import essentialcraft.common.mod.EssentialCraftCore;
import essentialcraft.common.tile.TileRightClicker;
import essentialcraft.utils.cfg.Config;
import net.minecraft.block.BlockContainer;
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
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

	@Override
	public EnumBlockRenderType getRenderType(IBlockState s) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return true;
	}

	@Override
	public boolean isOpaqueCube(IBlockState s) {
		return false;
	}

	@Override
	public int damageDropped(IBlockState meta) {
		return meta.getValue(TYPE).getIndex();
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		list.add(new ItemStack(this, 1, 0));
		list.add(new ItemStack(this, 1, 1));
		list.add(new ItemStack(this, 1, 2));
		list.add(new ItemStack(this, 1, 3));
		list.add(new ItemStack(this, 1, 4));
		list.add(new ItemStack(this, 1, 5));
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
		IInventory inv = (IInventory)world.getTileEntity(pos);
		InventoryHelper.dropInventoryItems(world, pos, inv);
		super.breakBlock(world, pos, blockstate);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileRightClicker();
	}

	@Override
	public void onBlockPlacedBy(World w, BlockPos p, IBlockState s, EntityLivingBase placer, ItemStack p_149689_6_) {
		int l = EnumFacing.getDirectionFromEntityLiving(p, placer).getIndex();
		TileEntity tile = w.getTileEntity(p);
		if(tile != null && tile instanceof TileRightClicker)
			((TileRightClicker)tile).rotation = l;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new ExtendedBlockState(this, new IProperty[] {TYPE, FACING}, new IUnlistedProperty[] {STATE, WORLD, POS});
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos par2, IBlockState par3, EntityPlayer player, EnumHand par5, EnumFacing par7, float par8, float par9, float par10) {
		if(player.isSneaking()) {
			return false;
		}
		if(!world.isRemote) {
			player.openGui(EssentialCraftCore.core, Config.guiID[0], world, par2.getX(), par2.getY(), par2.getZ());
			return true;
		}
		return true;
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

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		for(int i = 0; i < 6; i++) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i, new ModelResourceLocation("essentialcraft:rightclicker", "facing=north,type=" + ActivatorType.fromIndex(i)));
			for(int j = 0; j < 6; j++) {
				if(j == 2)
					continue;
				ModelBakery.registerItemVariants(Item.getItemFromBlock(this), new ModelResourceLocation("essentialcraft:rightclicker", "facing="+ EnumFacing.getFront(j).getName() +",type=" + ActivatorType.fromIndex(i)));
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

		public static ActivatorType fromIndex(int i) {
			return values()[i%6];
		}
	}
}
