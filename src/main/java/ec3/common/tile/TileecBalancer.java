package ec3.common.tile;

import java.util.UUID;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import ec3.api.EnumStructureType;
import ec3.api.IStructurePiece;

public class TileecBalancer extends TileEntity implements IStructurePiece, ITickable {
	public TileecController controller;
	public UUID uuid = UUID.randomUUID();
	
	@Override
	public EnumStructureType getStructure() {
		return EnumStructureType.MRUCUEnrichmentChamber;
	}

	@Override
	public TileEntity structureController() {
		return controller;
	}

	@Override
	public void setStructureController(TileEntity tile, EnumStructureType structure) {
		if(tile instanceof TileecController && structure == getStructure())
			controller = (TileecController)tile;
	}
	
	@Override
	public void update() {
		if(controller != null && controller.getMRUCU() != null)
			controller.getMRUCU().setFlag(true);
	}
}
