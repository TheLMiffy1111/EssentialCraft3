package essentialcraft.common.tile;

import java.util.List;

import essentialcraft.api.ApiCore;
import essentialcraft.common.entity.EntitySolarBeam;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.config.Configuration;

public class TileSunRayAbsorber extends TileMRUGeneric {
	public static int cfgMaxMRU = ApiCore.GENERATOR_MAX_MRU_GENERIC*10;
	public static float cfgBalance = 2F;
	public static int mruGenerated = 500;

	public TileSunRayAbsorber() {
		super(cfgMaxMRU);
		mruStorage.setBalance(cfgBalance);
		slot0IsBoundGem = false;
	}

	@Override
	public void update() {
		super.update();
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			List<EntitySolarBeam> l = getWorld().getEntitiesWithinAABB(EntitySolarBeam.class, new AxisAlignedBB(pos.getX()-1, pos.getY()-1, pos.getZ()-1, pos.getX()+2, pos.getY()+2, pos.getZ()+2));
			if(!l.isEmpty()) {
				mruStorage.addMRU(mruGenerated, true);
			}
		}
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.sunrayabsorber";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.GENERATOR_MAX_MRU_GENERIC*10).setMinValue(1).getInt();
			cfgBalance = cfg.get(category, "Balance", 2D).setMinValue(0D).setMaxValue(2D).getInt();
			mruGenerated = cfg.get(category, "MRUGenerated", 500).setMinValue(0).getInt();
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
