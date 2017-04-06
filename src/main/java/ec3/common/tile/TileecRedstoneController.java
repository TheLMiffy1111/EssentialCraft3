package ec3.common.tile;

import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import ec3.api.EnumStructureType;
import ec3.api.IStructurePiece;

public class TileecRedstoneController extends TileEntity implements IStructurePiece, ITickable {
	public TileecController controller;
	public UUID uuid = UUID.randomUUID();
	public int setting;
	public int tickTimer;
	
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		setting = p_145839_1_.getInteger("setting");
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound p_145841_1_) {
		super.writeToNBT(p_145841_1_);
		p_145841_1_.setInteger("setting", setting);
		return p_145841_1_;
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
	public void update() {
		++tickTimer;
		if(tickTimer >= 20) {
			tickTimer = 0;
			getWorld().notifyBlockOfStateChange(pos, getWorld().getBlockState(pos).getBlock());
		}
	}
	
	public boolean outputRedstone() {
		if(controller != null && controller.getMRUCU() != null && ((float)controller.getMRU()/(float)controller.getMaxMRU())*10 >= setting)
			return true;
		
		return false;
	}
}
