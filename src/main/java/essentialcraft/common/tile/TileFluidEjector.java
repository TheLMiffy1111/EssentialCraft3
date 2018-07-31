package essentialcraft.common.tile;

import net.minecraft.util.EnumFacing;

public class TileFluidEjector extends TileMRUGeneric {

	public EnumFacing getRotation() {
		int metadata = this.getBlockMetadata();
		metadata %= 6;
		return EnumFacing.getFront(metadata);
	}

	public TileFluidEjector() {
		super(0);
		setSlotsNum(0);
	}

	@Override
	public int[] getOutputSlots() {
		return new int[0];
	}
}
