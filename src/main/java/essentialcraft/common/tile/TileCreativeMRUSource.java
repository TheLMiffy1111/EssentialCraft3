package essentialcraft.common.tile;

import essentialcraft.api.ApiCore;
import net.minecraftforge.common.config.Configuration;

public class TileCreativeMRUSource extends TileMRUGeneric {

	public static int cfgMaxMRU = ApiCore.GENERATOR_MAX_MRU_GENERIC*100;
	
	public TileCreativeMRUSource() {
		super(cfgMaxMRU);
		setSlotsNum(0);
	}

	@Override
	public int[] getOutputSlots() {
		return new int[0];
	}

	@Override
	public void update()  {
		mruStorage.setMRU(mruStorage.getMaxMRU());
		super.update();
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
}
