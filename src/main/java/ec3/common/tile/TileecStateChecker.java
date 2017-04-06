package ec3.common.tile;

import java.util.UUID;

import net.minecraft.tileentity.TileEntity;
import ec3.api.EnumStructureType;
import ec3.api.IStructurePiece;
import ec3.api.ITERequiresMRU;

public class TileecStateChecker extends TileEntity implements IStructurePiece, ITERequiresMRU {
	public TileecController controller;
	public UUID uuid = UUID.randomUUID();
	
	@Override
	public int getMRU() {
		return controller != null ? controller.getMRU() : 0;
	}
	
	@Override
	public int getMaxMRU() {
		return controller != null ? controller.getMaxMRU() : 0;
	}
	
	@Override
	public boolean setMRU(int i) {
		return false;
	}
	
	@Override
	public float getBalance() {
		return controller != null ? controller.getBalance() : 1;
	}
	
	@Override
	public boolean setBalance(float f) {
		return false;
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
			controller = (TileecController)tile;
	}

	@Override
	public boolean setMaxMRU(float f) {
		return false;
	}
}
