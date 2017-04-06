package ec3.common.tile;

import ec3.common.block.BlockAdvBlockBreaker;
import ec3.common.inventory.InventoryMagicFilter;
import ec3.common.item.ItemFilter;
import ec3.utils.common.ECUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileAdvancedBlockBreaker extends TileMRUGeneric {

	public TileAdvancedBlockBreaker() {
		setMaxMRU(0);
		setSlotsNum(1);
	}
	
	public EnumFacing getRotation() {
		int metadata = getWorld().getBlockState(pos).getValue(BlockAdvBlockBreaker.FACING).getIndex();
		if(metadata > 5)
			metadata %= 6;
		return EnumFacing.getFront(metadata);
	}
	
	@Override
	public int[] getOutputSlots() {
		return new int[] {0};
	}
	
	public void breakBlocks() {
		for(int i = 1; i < 13; ++i) {
			BlockPos p = new BlockPos(pos.getX() + getRotation().getFrontOffsetX()*i, pos.getY() + getRotation().getFrontOffsetY()*i, pos.getZ() + getRotation().getFrontOffsetZ()*i);
			Block b = getWorld().getBlockState(p).getBlock();
			if(b != null && !b.isAir(getWorld().getBlockState(p), getWorld(), p)) {
				ItemStack fromBlock = new ItemStack(b,1,getWorld().getBlockState(p).getValue(BlockAdvBlockBreaker.FACING).getIndex());
				World w = getWorld();
				int dX = pos.getX() + getRotation().getFrontOffsetX()*i;
				int dY = pos.getY() + getRotation().getFrontOffsetY()*i;
				int dZ = pos.getZ() + getRotation().getFrontOffsetZ()*i;
				BlockPos dP = new BlockPos(dX, dY, dZ);
				if(getStackInSlot(0) == null || !(getStackInSlot(0).getItem() instanceof ItemFilter)) {
					b.breakBlock(w, dP, w.getBlockState(dP));
					b.onBlockDestroyedByPlayer(w, dP, w.getBlockState(dP));
					b.dropBlockAsItem(w, dP, w.getBlockState(dP), 0);
					w.setBlockState(dP, Blocks.AIR.getDefaultState(), 2);
				}
				else {
					if(ECUtils.canFilterAcceptItem(new InventoryMagicFilter(getStackInSlot(0)), fromBlock, getStackInSlot(0))) {
						b.breakBlock(w, dP, w.getBlockState(dP));
						b.onBlockDestroyedByPlayer(w, dP, w.getBlockState(dP));
						b.dropBlockAsItem(w, dP, w.getBlockState(dP), 0);
						w.setBlockState(dP, Blocks.AIR.getDefaultState(), 2);
					}
				}
			}
		}
	}
}
