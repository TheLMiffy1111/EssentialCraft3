package ec3.common.tile;

import java.util.List;

import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.config.Configuration;
import ec3.api.ApiCore;
import ec3.common.entity.EntitySolarBeam;

public class TileSunRayAbsorber extends TileMRUGeneric {
	public static float cfgMaxMRU = ApiCore.GENERATOR_MAX_MRU_GENERIC*10;
	public static float cfgBalance = 2F;
	public static float mruGenerated = 500;
	
	public TileSunRayAbsorber() {
		super();
		maxMRU = (int)cfgMaxMRU;
		slot0IsBoundGem = false;
	}
	
	public boolean canGenerateMRU() {
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void update() {
		super.update();
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			balance = cfgBalance;
			List<EntitySolarBeam> l = getWorld().getEntitiesWithinAABB(EntitySolarBeam.class, new AxisAlignedBB(pos.getX()-1, pos.getY()-1, pos.getZ()-1, pos.getX()+2, pos.getY()+2, pos.getZ()+2));
			if(!l.isEmpty()) {
				if(!getWorld().isRemote) {
					setMRU((int)(getMRU() + mruGenerated));
					if(getMRU() > getMaxMRU())
						setMRU(getMaxMRU());
				}
			}
		}
	}
	
	public static void setupConfig(Configuration cfg) {
		try {
			cfg.load();
			String[] cfgArrayString = cfg.getStringList("SunRayAbsorberSettings", "tileentities", new String[] {
					"Max MRU:"+ApiCore.GENERATOR_MAX_MRU_GENERIC*10,
					"Default balance:2",
					"MRU generated per tick:500",
			}, "");
			String dataString = "";
			
			for(int i = 0; i < cfgArrayString.length; ++i)
				dataString += "||" + cfgArrayString[i];
			
			DummyData[] data = DataStorage.parseData(dataString);
			
			cfgMaxMRU = Float.parseFloat(data[0].fieldValue);
			cfgBalance = Float.parseFloat(data[1].fieldValue);
			mruGenerated = Float.parseFloat(data[2].fieldValue);
			
			cfg.save();
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
