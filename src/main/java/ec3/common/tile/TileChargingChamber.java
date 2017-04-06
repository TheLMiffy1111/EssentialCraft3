package ec3.common.tile;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import ec3.api.ApiCore;
import ec3.api.IItemRequiresMRU;
import ec3.utils.common.ECUtils;

public class TileChargingChamber extends TileMRUGeneric {
	
	public static float cfgMaxMRU = (int)ApiCore.DEVICE_MAX_MRU_GENERIC;
	public static float reqMRUModifier = 1.0F;
	
	public TileChargingChamber() {
		super();
		maxMRU = (int) cfgMaxMRU;
		setSlotsNum(2);
	}
	
	@Override
	public void update() {
		super.update();
		ECUtils.manage(this, 0);
		
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0)
			tryChargeTools();
	}
	
	public void tryChargeTools(){
		ItemStack _gen_var_0 = getStackInSlot(1);
		if(_gen_var_0 != null && !getWorld().isRemote) {
			if(_gen_var_0.getItem() instanceof IItemRequiresMRU) {
				IItemRequiresMRU item = (IItemRequiresMRU) _gen_var_0.getItem();
				int mru = item.getMRU(_gen_var_0);
				int maxMRU = item.getMaxMRU(_gen_var_0);
				int p = (int)((float)maxMRU/100 * 5);
				if(mru < maxMRU) {
					if(mru+p < maxMRU) {
						if(getMRU() > p*reqMRUModifier) {
							System.out.println(item.setMRU(_gen_var_0, p));
							if(item.setMRU(_gen_var_0,p))
								setMRU((int) (getMRU() - p*reqMRUModifier));
						}
						else if(getMRU() > 0) {
							if(item.setMRU(_gen_var_0,getMRU()))
								setMRU(0);
						}
					}
					else {
						int k = maxMRU - mru;
						if(getMRU() > k*reqMRUModifier) {
							if(item.setMRU(_gen_var_0, k))
								setMRU((int)(getMRU() - k*reqMRUModifier));
						}
						else if(getMRU() > 0) {
							if(item.setMRU(_gen_var_0, getMRU()))
								setMRU(0);
						}
					}
				}
			}
		}
	}
	
	public static void setupConfig(Configuration cfg) {
		try {
			cfg.load();
			String[] cfgArrayString = cfg.getStringList("ChargingChamberSettings", "tileentities", new String[] {
					"Max MRU:" + ApiCore.DEVICE_MAX_MRU_GENERIC,
					"Charge cost Modifier:1.0"
			}, "Settings of the given Device.");
			String dataString = "";
			
			for(int i = 0; i < cfgArrayString.length; ++i)
				dataString += "||" + cfgArrayString[i];
			
			DummyData[] data = DataStorage.parseData(dataString);
			cfgMaxMRU = (int)Float.parseFloat(data[0].fieldValue);
			reqMRUModifier=Float.parseFloat(data[1].fieldValue);
			cfg.save();
		}
		catch(Exception e) {
			return;
		}
	}
	
	@Override
	public int[] getOutputSlots() {
		return new int[] {1};
	}
}
