package essentialcraft.common.tile;

import essentialcraft.api.ApiCore;
import net.minecraftforge.common.config.Configuration;

public class TileMoonWell extends TileMRUGeneric {

	public static int cfgMaxMRU = ApiCore.GENERATOR_MAX_MRU_GENERIC;
	public static float cfgBalance = 1F;
	public static int maxHeight = 80;
	public static double mruGenerated = 60;

	public TileMoonWell() {
		super(cfgMaxMRU);
		mruStorage.setBalance(cfgBalance);
		slot0IsBoundGem = false;
	}

	public boolean canGenerateMRU() {
		return getWorld().provider.getMoonPhase(getWorld().getWorldTime()) != 4 && !getWorld().isDaytime() && getWorld().canBlockSeeSky(pos.up());
	}

	@Override
	public void update() {
		super.update();
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			int moonPhase = getWorld().provider.getMoonPhase(getWorld().getWorldTime());
			double moonFactor = Math.abs(1D-moonPhase*0.25D);
			double mruGen = mruGenerated;
			mruGen *= moonFactor;
			double heightFactor = 1D;
			if(pos.getY() > maxHeight)
				heightFactor = 0D;
			else {
				heightFactor = 1D - (double)pos.getY()/maxHeight;
				mruGen *= heightFactor;
			}
			if((int)mruGen > 0 && canGenerateMRU()) {
				mruStorage.addMRU((int)mruGen, true);
			}
		}
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.moonwell";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.GENERATOR_MAX_MRU_GENERIC).setMinValue(1).getInt();
			cfgBalance = (float)cfg.get(category, "Balance", 1D).setMinValue(0D).setMaxValue(2D).getDouble();
			mruGenerated = cfg.get(category, "MaxMRUGenerated", 60D).setMinValue(0D).getDouble();
			maxHeight = cfg.get(category, "MaxHeight", 80).setMinValue(0).getInt();
		}
		catch(Exception e) {
			return;
		}
	}

	@Override
	public int[] getOutputSlots() {
		return new int[0];
	}
}
