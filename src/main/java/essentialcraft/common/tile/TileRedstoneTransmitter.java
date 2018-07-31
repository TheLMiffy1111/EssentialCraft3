package essentialcraft.common.tile;

import essentialcraft.common.block.BlockRedstoneTransmitter;
import essentialcraft.common.item.ItemBoundGem;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TileRedstoneTransmitter extends TileMRUGeneric {
	public int prevPower = 0;

	public TileRedstoneTransmitter() {
		super(0);
		setSlotsNum(1);
	}

	public int getRedstonePower() {
		if(getStackInSlot(0).getItem() instanceof ItemBoundGem) {
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

	@Override
	public void update() {
		super.update();
		if(getRedstonePower() != prevPower) {
			getWorld().notifyNeighborsRespectDebug(pos, blockType, false);
			getWorld().notifyNeighborsOfStateChange(pos, blockType, false);
			prevPower = getRedstonePower();
		}
	}

	@Override
	public int[] getOutputSlots() {
		return new int[0];
	}
}
