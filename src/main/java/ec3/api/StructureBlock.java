package ec3.api;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class StructureBlock {

	public Block blk;
	public int metadata;
	public int x,y,z;

	public StructureBlock(Block b, int meta, int i, int j, int k) {
		blk = b;
		metadata = meta;
		x = i;
		y = j;
		z = k;
	}

	public StructureBlock(Block b, int meta, BlockPos p) {
		this(b,meta,p.getX(),p.getY(),p.getZ());
	}
}
