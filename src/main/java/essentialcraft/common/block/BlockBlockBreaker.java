package essentialcraft.common.block;

import DummyCore.Client.IModelRegisterer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockBlockBreaker extends Block implements IModelRegisterer {

	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	public BlockBlockBreaker() {
		super(Material.ROCK);
		setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN));
	}

	@Override
	public boolean canProvidePower(IBlockState s) {
		return true;
	}

	@Override
	public void neighborChanged(IBlockState s, World w, BlockPos p, Block n, BlockPos fp) {
		if(!w.isRemote && w.isBlockIndirectlyGettingPowered(p) > 0) {
			EnumFacing d = w.getBlockState(p).getValue(FACING);
			Block broken = w.getBlockState(p.offset(d)).getBlock();
			if(!broken.isAir(w.getBlockState(p.offset(d)), w, p.offset(d))) {
				float hardness = broken.getBlockHardness(w.getBlockState(p.offset(d)), w, p.offset(d));
				if(hardness >= 0 && hardness <= 10) {
					for(int i = 1; i < 13; ++i) {
						BlockPos dP = p.offset(d, i);
						Block b = w.getBlockState(dP).getBlock();
						if(b.getBlockHardness(w.getBlockState(dP), w, dP) == hardness) {
							b.breakBlock(w, dP, w.getBlockState(dP));
							b.onBlockDestroyedByPlayer(w, dP, w.getBlockState(dP));
							b.dropBlockAsItem(w, dP, w.getBlockState(dP), 0);
							w.setBlockToAir(dP);
						}
						else {
							break;
						}
					}
				}
			}
		}
	}

	@Override
	public IBlockState getStateForPlacement(World w, BlockPos p, EnumFacing side, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(FACING, side);
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
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:blockbreaker", "inventory"));
	}
}
