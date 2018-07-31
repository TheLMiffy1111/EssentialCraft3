package essentialcraft.common.tile;

import essentialcraft.api.EnumStructureType;
import essentialcraft.api.IStructurePiece;
import net.minecraft.tileentity.TileEntity;

public class TileMRUCUECHoldingChamber extends TileEntity implements IStructurePiece {
	public TileMRUCUECController controller;

	@Override
	public EnumStructureType getStructure() {
		return EnumStructureType.MRUCUEC;
	}

	@Override
	public TileEntity structureController() {
		return controller;
	}

	@Override
	public void setStructureController(TileEntity tile, EnumStructureType structure) {
		if(tile instanceof TileMRUCUECController && structure == getStructure())
			controller = (TileMRUCUECController) tile;
	}
}
