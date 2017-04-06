package ec3.common.block;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.MiscUtils;
import ec3.common.item.ItemBoundGem;
import ec3.common.mod.EssentialCraftCore;
import ec3.common.tile.TileRedstoneTransmitter;
import ec3.utils.cfg.Config;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockRedstoneTransmitter extends BlockContainer implements IModelRegisterer {

	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	public BlockRedstoneTransmitter(Material p_i45394_1_) {
		super(p_i45394_1_);
	}

	public AxisAlignedBB getBoundingBox(IBlockState s, IBlockAccess p_149719_1_, BlockPos p_149719_2_) {
		int metadata = s.getValue(FACING).getIndex();
		if(metadata == 0) {
			return new AxisAlignedBB(0.35F, 0F, 0.35F, 0.65F, 0.5F, 0.65F);
		}
		else if(metadata == 1) {
			return new AxisAlignedBB(0.35F, 0.45F, 0.35F, 0.65F, 1F, 0.65F);
		}
		else if(metadata == 2) {
			return new AxisAlignedBB(0.35F, 0.35F, 0F, 0.65F, 0.65F, 0.5F);
		}
		else if(metadata == 3) {
			return new AxisAlignedBB(0.35F, 0.35F, 0.5F, 0.65F, 0.65F, 1F);
		}
		else if(metadata == 4) {
			return new AxisAlignedBB(0F, 0.35F, 0.35F, 0.5F, 0.65F, 0.65F);
		}
		else if(metadata == 5) {
			return new AxisAlignedBB(0.5F, 0.35F, 0.35F, 1F, 0.65F, 0.65F);
		}
		return super.getBoundingBox(s, p_149719_1_, p_149719_2_);
	}

	public BlockRedstoneTransmitter() {
		super(Material.ROCK);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN));
	}

	@Override
	public void breakBlock(World par1World, BlockPos par2Pos, IBlockState par3State) {
		MiscUtils.dropItemsOnBlockBreak(par1World, par2Pos.getX(), par2Pos.getY(), par2Pos.getZ(), par3State.getBlock(), 0);
		super.breakBlock(par1World, par2Pos, par3State);
	}

	public boolean isOpaqueCube(IBlockState s) {
		return false;
	}

	public boolean isFullCube(IBlockState s) {
		return false;
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	public EnumBlockRenderType getRenderType(IBlockState s) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int metadata) {
		return new TileRedstoneTransmitter();
	}

	@Override
	public IBlockState getStateForPlacement(World w, BlockPos p, EnumFacing side, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(FACING, side.getOpposite());
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

	public boolean canProvidePower(IBlockState s) {
		return true;
	}

	public boolean canConnectRedstone(IBlockState s, IBlockAccess w, BlockPos p, EnumFacing side)
	{
		TileRedstoneTransmitter trt = (TileRedstoneTransmitter)w.getTileEntity(p);
		return trt.getStackInSlot(0) != null;
	}

	public int getWeakPower(IBlockState s, IBlockAccess w, BlockPos p, EnumFacing side) {
		TileRedstoneTransmitter trt = (TileRedstoneTransmitter) w.getTileEntity(p);

		if(trt.getStackInSlot(0) != null && trt.getStackInSlot(0).getItem() instanceof ItemBoundGem) {
			int[] c = ItemBoundGem.getCoords(trt.getStackInSlot(0));

			if(c != null && c.length > 0) {
				if(w.getBlockState(new BlockPos(c[0], c[1], c[2])) == this) {
					return trt.getRedstonePower(c);
				}
			}
		}
		return 0;
	}

	public void neighborChanged(IBlockState s, World w, BlockPos p, Block n) {
		TileRedstoneTransmitter trt = (TileRedstoneTransmitter) w.getTileEntity(p);
		if(trt.getStackInSlot(0) != null && trt.getStackInSlot(0).getItem() instanceof ItemBoundGem) {
			int[] c = ItemBoundGem.getCoords(trt.getStackInSlot(0));
			if(c != null && c.length > 0) {
				if(w.getBlockState(new BlockPos(c[0], c[1], c[2])).getBlock() == this) {
					w.notifyNeighborsOfStateChange(new BlockPos(c[0], c[1], c[2]), this);
				}
			}
		}
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
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:redstoneTransmitter", "inventory"));
	}
}
