package essentialcraft.common.tile;

import essentialcraft.api.ApiCore;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public class TileMIMScreen extends TileMRUGeneric {

	public TileMIM parent;
	int tickTime;

	public static int cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;
	public static int mruForIns = 500;
	public static int mruForOut = 10;

	public TileMIMScreen() {
		super(cfgMaxMRU);
		setSlotsNum(2);
	}

	@Override
	public int[] getOutputSlots() {
		return new int[] {1};
	}

	@Override
	public void update() {
		super.update();
		mruStorage.update(getPos(), getWorld(), getStackInSlot(0));
		if(tickTime == 0) {
			tickTime = 20;
			if(parent != null)
				if(!parent.isParent(this))
					parent = null;
		}
		else
			--tickTime;

		if(parent != null) {
			if(!getStackInSlot(1).isEmpty()) {
				if(mruStorage.getMRU() >= mruForIns) {
					mruStorage.extractMRU(mruForIns, true);
					if(parent.addItemStackToSystem(getStackInSlot(1)))
						setInventorySlotContents(1, ItemStack.EMPTY);

					syncTick = 0;
				}
			}
		}
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.magicalinventorymanagerscreen";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.DEVICE_MAX_MRU_GENERIC).setMinValue(1).getInt();
			mruForIns = cfg.get(category, "MRUUsageIn", 500, "MRU Per Inserted Item").setMinValue(0).setMaxValue(cfgMaxMRU).getInt();
			mruForOut = cfg.get(category, "MRUUsageOut", 10, "MRU Per Requested Item (for 1)").setMinValue(0).setMaxValue(cfgMaxMRU).getInt();
		}
		catch(Exception e) {
			return;
		}
	}
}
