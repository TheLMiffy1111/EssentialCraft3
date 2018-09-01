package essentialcraft.common.tile;

import essentialcraft.api.ApiCore;
import essentialcraft.common.capabilities.mru.CapabilityMRUHandler;
import essentialcraft.common.capabilities.mru.MRUTileStorage;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileCreativeMRUSource extends TileEntity implements ITickable {

	public static int cfgMaxMRU = ApiCore.GENERATOR_MAX_MRU_GENERIC*100;
	protected MRUTileStorage mruStorage = new MRUTileStorage(cfgMaxMRU);

	public TileCreativeMRUSource() {
		super();
	}

	@Override
	public void update()  {
		mruStorage.setMRU(mruStorage.getMaxMRU());
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.creativemrusource";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.GENERATOR_MAX_MRU_GENERIC*100).setMinValue(1).getInt();
		}
		catch(Exception e) {
			return;
		}
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityMRUHandler.MRU_HANDLER_CAPABILITY ||
				super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == CapabilityMRUHandler.MRU_HANDLER_CAPABILITY ? (T)mruStorage :
			super.getCapability(capability, facing);
	}
}
