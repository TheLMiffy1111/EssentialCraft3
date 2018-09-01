package essentialcraft.common.tile;

import essentialcraft.common.block.BlockAdvBlockBreaker;
import essentialcraft.common.inventory.InventoryMagicFilter;
import essentialcraft.common.item.ItemFilter;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileAdvancedBlockBreaker extends TileMRUGeneric {

	public TileAdvancedBlockBreaker() {
		super(0);
		this.slot0IsBoundGem = false;
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

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return stack.getItem() instanceof ItemFilter;
	}

	public void breakBlocks() {
		for(int i = 1; i < 13; ++i) {
			BlockPos p = pos.offset(getRotation(), i);
			Block b = getWorld().getBlockState(p).getBlock();
			if(b != null && !b.isAir(getWorld().getBlockState(p), getWorld(), p)) {
				ItemStack fromBlock = b.getItem(getWorld(), p, getWorld().getBlockState(p));
				World w = getWorld();
				if(getStackInSlot(0).isEmpty() || !(getStackInSlot(0).getItem() instanceof ItemFilter)) {
					b.breakBlock(w, p, w.getBlockState(p));
					b.onBlockDestroyedByPlayer(w, p, w.getBlockState(p));
					b.dropBlockAsItem(w, p, w.getBlockState(p), 0);
					w.setBlockToAir(p);
				}
				else if(ECUtils.canFilterAcceptItem(new InventoryMagicFilter(getStackInSlot(0)), fromBlock, getStackInSlot(0))) {
					b.breakBlock(w, p, w.getBlockState(p));
					b.onBlockDestroyedByPlayer(w, p, w.getBlockState(p));
					b.dropBlockAsItem(w, p, w.getBlockState(p), 0);
					w.setBlockToAir(p);
				}
			}
		}
	}
}
