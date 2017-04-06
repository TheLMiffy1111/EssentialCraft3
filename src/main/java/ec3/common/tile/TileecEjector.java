package ec3.common.tile;

import java.util.UUID;

import net.minecraft.tileentity.TileEntity;
import ec3.api.EnumStructureType;
import ec3.api.IStructurePiece;
import ec3.api.ITEStoresMRU;

public class TileecEjector extends TileEntity implements ITEStoresMRU, IStructurePiece {
	public TileecController controller;
	public UUID uuid = UUID.randomUUID();
	
	@Override
	public int getMRU() {
		if(controller!=null)
			return controller.getMRU();
		return 0;
	}

	@Override
	public int getMaxMRU() {
		return 60000;
	}

	@Override
	public boolean setMRU(int i) {
		return controller != null && i < getMRU() ? controller.setMRU(i) : false;
	}

	@Override
	public float getBalance() {
		return controller != null ? controller.getBalance() : 1;
	}

	@Override
	public boolean setBalance(float f) {
		return controller != null ? controller.setBalance(f) : false;
	}

	@Override
	public UUID getUUID() {
		return uuid;
	}

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
			controller = (TileecController) tile;
	}

	@Override
	public boolean setMaxMRU(float f) {
		return controller != null ? controller.setMaxMRU(f) : false;
	}
}
