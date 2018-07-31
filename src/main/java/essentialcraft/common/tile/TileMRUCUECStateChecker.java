package essentialcraft.common.tile;

import essentialcraft.api.EnumStructureType;
import essentialcraft.api.IMRUDisplay;
import essentialcraft.api.IMRUHandler;
import essentialcraft.api.IStructurePiece;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileMRUCUECStateChecker extends TileEntity implements IStructurePiece, IMRUDisplay {
	public TileMRUCUECController controller;
	public ControllerMRUStorageReadOnlyWrapper mruStorageWrapper = new ControllerMRUStorageReadOnlyWrapper();

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
			controller = (TileMRUCUECController)tile;
	}

	@Override
	public IMRUHandler getMRUHandler() {
		return mruStorageWrapper;
	}
	
	public class ControllerMRUStorageReadOnlyWrapper implements IMRUHandler {

		@Override
		public int getMaxMRU() {
			if(controller != null) {
				return controller.mruStorage.getMaxMRU();
			}
			return 0;
		}

		@Override
		public void setMaxMRU(int amount) {}

		@Override
		public int getMRU() {
			if(controller != null) {
				return controller.mruStorage.getMRU();
			}
			return 0;
		}

		@Override
		public void setMRU(int amount) {}

		@Override
		public int addMRU(int amount, boolean doAdd) {
			return amount;
		}

		@Override
		public int extractMRU(int amount, boolean doExtract) {
			return 0;
		}

		@Override
		public float getBalance() {
			if(controller != null) {
				controller.mruStorage.getBalance();
			}
			return 1F;
		}

		@Override
		public void setBalance(float balance) {}

		@Override
		public boolean getShade() {
			if(controller != null) {
				return controller.mruStorage.getShade();
			}
			return false;
		}

		@Override
		public void setShade(boolean shade) {}

		@Override
		public NBTTagCompound writeToNBT(NBTTagCompound nbt) {return nbt;}

		@Override
		public void readFromNBT(NBTTagCompound nbt) {}
	}
}
