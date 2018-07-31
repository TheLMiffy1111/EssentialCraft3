package essentialcraft.common.tile;

import essentialcraft.api.ApiCore;
import essentialcraft.api.IMRUHandlerItem;
import essentialcraft.common.capabilities.mru.CapabilityMRUHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.config.Configuration;

public class TileChargingChamber extends TileMRUGeneric {

	public static Capability<IMRUHandlerItem> MRU_HANDLER_ITEM_CAPABILITY = CapabilityMRUHandler.MRU_HANDLER_ITEM_CAPABILITY;
	public static int cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;
	public static double reqMRUModifier = 1D;

	public TileChargingChamber() {
		super(cfgMaxMRU);
		setSlotsNum(2);
	}

	@Override
	public void update() {
		super.update();
		mruStorage.update(getPos(), getWorld(), getStackInSlot(0));
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0)
			tryChargeTools();
	}

	public void tryChargeTools(){
		ItemStack stack = getStackInSlot(1);
		if(!stack.isEmpty()) {
			if(stack.hasCapability(MRU_HANDLER_ITEM_CAPABILITY, null)) {
				IMRUHandlerItem mruHandler = stack.getCapability(MRU_HANDLER_ITEM_CAPABILITY, null);
				int mru = mruHandler.getMRU();
				int maxMRU = mruHandler.getMaxMRU();
				int p = (int)((double)maxMRU/20);
				if(mru < maxMRU) {
					if(mru+p < maxMRU) {
						int amount = (int)(mruStorage.extractMRU((int)(p*reqMRUModifier), true)/reqMRUModifier);
						mruHandler.addMRU(amount, true);
					}
					else {
						int k = maxMRU - mru;
						int amount = (int)(mruStorage.extractMRU((int)(k*reqMRUModifier), true)/reqMRUModifier);
						mruHandler.addMRU(amount, true);
					}
				}
			}
		}
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.chargingchamber";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.DEVICE_MAX_MRU_GENERIC).setMinValue(1).getInt();
			reqMRUModifier = cfg.get(category, "ChargeCostModifier", 1D).setMinValue(Double.MIN_NORMAL).getDouble();
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
