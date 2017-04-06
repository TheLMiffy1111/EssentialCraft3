package ec3.common.block;

import java.util.List;

import DummyCore.Blocks.Properties.UnlistedPropertyObject;
import DummyCore.Client.IModelRegisterer;
import ec3.common.tile.TileMimic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class BlockMimic extends BlockContainer implements IModelRegisterer {

	public static final UnlistedPropertyObject<IBlockState> STATE = new UnlistedPropertyObject<IBlockState>("state", IBlockState.class);
	public static final UnlistedPropertyObject<IBlockAccess> WORLD = new UnlistedPropertyObject<IBlockAccess>("world", IBlockAccess.class);
	public static final UnlistedPropertyObject<BlockPos> POS = new UnlistedPropertyObject<BlockPos>("pos", BlockPos.class);

	public BlockMimic() {
		super(Material.ROCK);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileMimic();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[] {STATE, WORLD, POS});
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		state = ((IExtendedBlockState)state).withProperty(WORLD, world).withProperty(POS, pos);
		TileEntity tile = world.getTileEntity(pos);
		if(tile != null && tile instanceof TileMimic)
			return ((IExtendedBlockState)state).withProperty(STATE, ((TileMimic)tile).getState());
		return state;
	}

	public boolean isOpaqueCube(IBlockState s) {
		return false;
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return true;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntity tile = worldIn.getTileEntity(pos);

		if(worldIn.isRemote)
			return true;
		if(heldItem != null && heldItem.getItem() instanceof ItemBlock && tile instanceof TileMimic && !playerIn.isSneaking()) {
			TileMimic mimic = (TileMimic)tile;
			IBlockState changeState = ((ItemBlock)heldItem.getItem()).getBlock().getStateForPlacement(worldIn, pos, side, hitX, hitY, hitZ, heldItem.getItemDamage(), playerIn);

			if(isValidBlock(changeState) && changeState.getBlock() != this) {
				mimic.setState(changeState);
				worldIn.notifyBlockUpdate(pos, state, state, 2);
				return true;
			}
		}
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		TileEntity te = source.getTileEntity(pos);
		if(te instanceof TileMimic && ((TileMimic)te).getState() != null) {
			return ((TileMimic)te).getState().getBoundingBox(new FakeBlockAccess(source), pos);
		}
		return super.getBoundingBox(state, source, pos);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	public boolean isFullCube(IBlockState state) {
		return false;
	}

	public static boolean isValidBlock(IBlockState state) {
		return state.getRenderType() == EnumBlockRenderType.MODEL && state.getMaterial() != Material.AIR;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:mimic", "inventory"));
	}
	
	private static class FakeBlockAccess implements IBlockAccess {

		private final IBlockAccess compose;

		private FakeBlockAccess(IBlockAccess compose) {
			this.compose = compose;
		}

		@Override
		public TileEntity getTileEntity(BlockPos pos) {
			return compose.getTileEntity(pos);
		}

		@Override
		public int getCombinedLight(BlockPos pos, int lightValue) {
			return 15 << 20 | 15 << 4;
		}

		@Override
		public IBlockState getBlockState(BlockPos pos) {
			IBlockState state = compose.getBlockState(pos);
			if(state.getBlock() instanceof BlockMimic) {
				state = ((TileMimic)compose.getTileEntity(pos)).getState();
			}
			return state == null ? Blocks.AIR.getDefaultState() : state;
		}

		@Override
		public boolean isAirBlock(BlockPos pos) {
			return compose.isAirBlock(pos);
		}

		@Override
		public Biome getBiome(BlockPos pos) {
			return compose.getBiome(pos);
		}

		@Override
		public int getStrongPower(BlockPos pos, EnumFacing direction) {
			return compose.getStrongPower(pos, direction);
		}

		@Override
		public WorldType getWorldType() {
			return compose.getWorldType();
		}

		@Override
		public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
			return compose.isSideSolid(pos, side, _default);
		}
	}
}
