package ec3.common.block;

import java.util.Random;

import com.google.common.cache.LoadingCache;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Client.ModelUtils;
import DummyCore.Utils.DummyPortalHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

//Might make this extend a class in DummyCore
public class BlockPortal extends net.minecraft.block.BlockPortal implements IModelRegisterer {

	public BlockPortal() {
		super();
	}

	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {}

	public void onEntityCollidedWithBlock(World p_149670_1_, BlockPos p_149670_2_, IBlockState s, Entity p_149670_5_) {
		if(!p_149670_1_.isRemote) {
			DummyPortalHandler.transferEntityToDimension(p_149670_5_);
		}
	}

	public void randomDisplayTick(IBlockState state, World worldIn, BlockPos pos, Random rand) {}

	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		EnumFacing.Axis enumfacing$axis = (EnumFacing.Axis)state.getValue(AXIS);

		if (enumfacing$axis == EnumFacing.Axis.X) {
			BlockPortal.Size blockportal$size = new BlockPortal.Size(worldIn, pos, EnumFacing.Axis.X);

			if(!blockportal$size.isValid() || blockportal$size.portalBlockCount < blockportal$size.width * blockportal$size.height) {
				worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
			}
		}
		else if (enumfacing$axis == EnumFacing.Axis.Z) {
			BlockPortal.Size blockportal$size1 = new BlockPortal.Size(worldIn, pos, EnumFacing.Axis.Z);

			if(!blockportal$size1.isValid() || blockportal$size1.portalBlockCount < blockportal$size1.width * blockportal$size1.height) {
				worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
			}
		}
	}

	public BlockPattern.PatternHelper createPatternHelper(World worldIn, BlockPos p_181089_2_) {
		EnumFacing.Axis enumfacing$axis = EnumFacing.Axis.Z;
		BlockPortal.Size blockportal$size = new BlockPortal.Size(worldIn, p_181089_2_, EnumFacing.Axis.X);
		LoadingCache<BlockPos, BlockWorldState> loadingcache = BlockPattern.createLoadingCache(worldIn, true);

		if(!blockportal$size.isValid()) {
			enumfacing$axis = EnumFacing.Axis.X;
			blockportal$size = new BlockPortal.Size(worldIn, p_181089_2_, EnumFacing.Axis.Z);
		}

		if(!blockportal$size.isValid()) {
			return new BlockPattern.PatternHelper(p_181089_2_, EnumFacing.NORTH, EnumFacing.UP, loadingcache, 1, 1, 1);
		}
		else {
			int[] aint = new int[EnumFacing.AxisDirection.values().length];
			EnumFacing enumfacing = blockportal$size.rightDir.rotateYCCW();
			BlockPos blockpos = blockportal$size.bottomLeft.up(blockportal$size.getHeight() - 1);

			for(EnumFacing.AxisDirection enumfacing$axisdirection : EnumFacing.AxisDirection.values()) {
				BlockPattern.PatternHelper blockpattern$patternhelper = new BlockPattern.PatternHelper(enumfacing.getAxisDirection() == enumfacing$axisdirection ? blockpos : blockpos.offset(blockportal$size.rightDir, blockportal$size.getWidth() - 1), EnumFacing.getFacingFromAxis(enumfacing$axisdirection, enumfacing$axis), EnumFacing.UP, loadingcache, blockportal$size.getWidth(), blockportal$size.getHeight(), 1);

				for(int i = 0; i < blockportal$size.getWidth(); ++i) {
					for(int j = 0; j < blockportal$size.getHeight(); ++j) {
						BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(i, j, 1);

						if(blockworldstate.getBlockState() != null && blockworldstate.getBlockState().getMaterial() != Material.AIR) {
							++aint[enumfacing$axisdirection.ordinal()];
						}
					}
				}
			}

			EnumFacing.AxisDirection enumfacing$axisdirection1 = EnumFacing.AxisDirection.POSITIVE;

			for(EnumFacing.AxisDirection enumfacing$axisdirection2 : EnumFacing.AxisDirection.values()) {
				if(aint[enumfacing$axisdirection2.ordinal()] < aint[enumfacing$axisdirection1.ordinal()]) {
					enumfacing$axisdirection1 = enumfacing$axisdirection2;
				}
			}

			return new BlockPattern.PatternHelper(enumfacing.getAxisDirection() == enumfacing$axisdirection1 ? blockpos : blockpos.offset(blockportal$size.rightDir, blockportal$size.getWidth() - 1), EnumFacing.getFacingFromAxis(enumfacing$axisdirection1, enumfacing$axis), EnumFacing.UP, loadingcache, blockportal$size.getWidth(), blockportal$size.getHeight(), 1);
		}
	}

	public boolean trySpawnPortal(World worldIn, BlockPos pos) {
		BlockPortal.Size blockportal$size = new BlockPortal.Size(worldIn, pos, EnumFacing.Axis.X);

		if(blockportal$size.isValid() && blockportal$size.portalBlockCount == 0) {
			blockportal$size.placePortalBlocks();
			return true;
		}
		else {
			BlockPortal.Size blockportal$size1 = new BlockPortal.Size(worldIn, pos, EnumFacing.Axis.Z);

			if(blockportal$size1.isValid() && blockportal$size1.portalBlockCount == 0) {
				blockportal$size1.placePortalBlocks();
				return true;
			}
			else {
				return false;
			}
		}
	}

	@Override
	public void registerModels() {
		ModelUtils.setItemModelSingleIcon(Item.getItemFromBlock(this), "essentialcraft:portal");
	}

	public static class Size {
		private final World world;
		private final EnumFacing.Axis axis;
		private final EnumFacing rightDir;
		private final EnumFacing leftDir;
		private int portalBlockCount;
		private BlockPos bottomLeft;
		private int height;
		private int width;

		public Size(World worldIn, BlockPos p_i45694_2_, EnumFacing.Axis p_i45694_3_) {
			world = worldIn;
			axis = p_i45694_3_;

			if(p_i45694_3_ == EnumFacing.Axis.X) {
				leftDir = EnumFacing.EAST;
				rightDir = EnumFacing.WEST;
			}
			else {
				leftDir = EnumFacing.NORTH;
				rightDir = EnumFacing.SOUTH;
			}

			for(BlockPos blockpos = p_i45694_2_; p_i45694_2_.getY() > blockpos.getY() - 21 && p_i45694_2_.getY() > 0 && isEmptyBlock(worldIn.getBlockState(p_i45694_2_.down()).getBlock()); p_i45694_2_ = p_i45694_2_.down()) {}

			int i = getDistanceUntilEdge(p_i45694_2_, leftDir) - 1;

			if(i >= 0) {
				bottomLeft = p_i45694_2_.offset(leftDir, i);
				width = getDistanceUntilEdge(bottomLeft, rightDir);

				if(width < 2 || width > 21) {
					bottomLeft = null;
					width = 0;
				}
			}

			if(bottomLeft != null) {
				height = calculatePortalHeight();
			}
		}

		protected int getDistanceUntilEdge(BlockPos p_180120_1_, EnumFacing p_180120_2_) {
			int i;

			for(i = 0; i < 22; ++i) {
				BlockPos blockpos = p_180120_1_.offset(p_180120_2_, i);

				if(!isEmptyBlock(world.getBlockState(blockpos).getBlock()) || world.getBlockState(blockpos.down()).getBlock() != Blocks.OBSIDIAN) {
					break;
				}
			}

			Block block = world.getBlockState(p_180120_1_.offset(p_180120_2_, i)).getBlock();
			return block == Blocks.OBSIDIAN ? i : 0;
		}

		public int getHeight() {
			return height;
		}

		public int getWidth() {
			return width;
		}

		protected int calculatePortalHeight() {
			{
				label24: for (height = 0; height < 21; ++height) {
					for(int i = 0; i < width; ++i) {
						BlockPos blockpos = bottomLeft.offset(rightDir, i).up(height);
						Block block = world.getBlockState(blockpos).getBlock();

						if(!isEmptyBlock(block)) {
							break label24;
						}

						if(block == BlocksCore.portal) {
							++portalBlockCount;
						}

						if(i == 0) {
							block = world.getBlockState(blockpos.offset(leftDir)).getBlock();

							if(block != Blocks.OBSIDIAN) {
								break label24;
							}
						}
						else if(i == width - 1) {
							block = world.getBlockState(blockpos.offset(rightDir)).getBlock();

							if(block != Blocks.OBSIDIAN) {
								break label24;
							}
						}
					}
				}
			}

			for(int j = 0; j < width; ++j) {
				if(world.getBlockState(bottomLeft.offset(rightDir, j).up(height)).getBlock() != Blocks.OBSIDIAN) {
					height = 0;
					break;
				}
			}

			if(height <= 21 && height >= 3) {
				return height;
			}
			else {
				bottomLeft = null;
				width = 0;
				height = 0;
				return 0;
			}
		}

		protected boolean isEmptyBlock(Block blockIn) {
			return blockIn.getMaterial(blockIn.getDefaultState()) == Material.AIR || /*blockIn == BlocksCore.portalActivator ||*/ blockIn == BlocksCore.portal;
		}

		public boolean isValid() {
			return bottomLeft != null && width >= 2 && width <= 21 && height >= 3 && height <= 21;
		}

		public void placePortalBlocks() {
			for (int i = 0; i < width; ++i) {
				BlockPos blockpos = bottomLeft.offset(rightDir, i);

				for(int j = 0; j < height; ++j) {
					world.setBlockState(blockpos.up(j), BlocksCore.portal.getDefaultState().withProperty(BlockPortal.AXIS, axis), 2);
				}
			}
		}
	}
}
