package ec3.common.tile;

import ec3.common.block.BlockRedstoneTransmitter;
import ec3.common.item.ItemBoundGem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TileRedstoneTransmitter extends TileMRUGeneric {
	public int prevPower = 0;
	
	public TileRedstoneTransmitter() {
		super();
		setSlotsNum(1);
		setMaxMRU(0);
	}
	
	public int getRedstonePower() {
		if(getStackInSlot(0) != null && getStackInSlot(0).getItem() instanceof ItemBoundGem) {
			int[] c = ItemBoundGem.getCoords(getStackInSlot(0));

			if(c != null && c.length > 0) {
				BlockPos other = new BlockPos(c[0], c[1], c[2]);

				for(EnumFacing facing : EnumFacing.VALUES) {
					if(getWorld().getBlockState(other.offset(facing)).getBlock() instanceof BlockRedstoneTransmitter) {
						return 0;
					}
				}
				
				if(getWorld().getBlockState(other).getBlock() instanceof BlockRedstoneTransmitter) {
					return getWorld().isBlockIndirectlyGettingPowered(other);
				}
			}
		}
		
		return 0;
	}
	
	public void update() {
		super.update();
		if(getRedstonePower() != prevPower) {
			getWorld().notifyBlockOfStateChange(pos, blockType);
			getWorld().notifyNeighborsOfStateChange(pos, blockType);
			prevPower = getRedstonePower();
		}
	}
	
	@Override
	public int[] getOutputSlots() {
		return new int[0];
	}
}
