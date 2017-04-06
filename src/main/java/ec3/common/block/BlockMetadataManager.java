package ec3.common.block;

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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockMetadataManager extends Block implements IModelRegisterer {

	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	public BlockMetadataManager() {
		super(Material.ROCK);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN));
	}

	public boolean canProvidePower()
	{
		return true;
	}

	public void neighborChanged(IBlockState s, World w, BlockPos p, Block n) 
	{
		for(int i = 0; i < 6; ++i)
		{
			EnumFacing d = EnumFacing.getFront(i);
			Block b = w.getBlockState(p.offset(d)).getBlock();
			if(b != n && b != this && n != this)
			{
				b.neighborChanged(w.getBlockState(p.offset(d)), w, p.offset(d), this);
			}
		}
	}

	public boolean canConnectRedstone(IBlockState s, IBlockAccess world, BlockPos p, EnumFacing side)
	{
		return side != s.getValue(FACING);
	}

	public int getWeakPower(IBlockState s, IBlockAccess w, BlockPos p, EnumFacing side)
	{
		EnumFacing d = s.getValue(FACING);
		return w.getBlockState(p.offset(d)).getBlock().getMetaFromState(w.getBlockState(p.offset(d)));
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
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:metadataManager", "inventory"));
	}
}
