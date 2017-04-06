package ec3.common.tile;

import net.minecraft.util.math.BlockPos;

public class TileRedstoneTransmitter extends TileMRUGeneric {
	public int updateTime = 0;
	
	public TileRedstoneTransmitter() {
		super();
		setSlotsNum(1);
		setMaxMRU(0);
	}
	
	public int getRedstonePower(int[] c) {
		return getWorld().getStrongPower(new BlockPos(c[0], c[1], c[2]));
	}
	
	public void update() {
		super.update();
		if(updateTime == 0) {
			updateTime = 20;
			getWorld().notifyBlockOfStateChange(pos, blockType);
		}
		else
			--updateTime;
	}
	
	@Override
	public int[] getOutputSlots() {
		return new int[0];
	}
}
