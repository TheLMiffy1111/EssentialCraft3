package essentialcraft.common.tile;

import essentialcraft.api.ApiCore;
import essentialcraft.common.capabilities.mru.MRUTileCrossDimStorage;
import essentialcraft.common.capabilities.mru.MRUTileRangelessStorage;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public class TileMRUDimensionalTransciever extends TileMRUGeneric {

	public static int cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;
	public static boolean allowDimensionalTransfer = true;

	public TileMRUDimensionalTransciever() {
		super(cfgMaxMRU);
		mruStorage = allowDimensionalTransfer ? new MRUTileCrossDimStorage(cfgMaxMRU) : new MRUTileRangelessStorage(cfgMaxMRU);
		setSlotsNum(1);
	}

	@Override
	public void update() {
		super.update();
		mruStorage.update(getPos(), getWorld(), getStackInSlot(0));
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return isBoundGem(stack);
	}

	@Override
	public int[] getOutputSlots() {
		return new int[0];
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.mrudimensionaltransciever";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.DEVICE_MAX_MRU_GENERIC).setMinValue(1).getInt();
			allowDimensionalTransfer = cfg.get(category, "AllowDimensionalTransfer", true).getBoolean();
		}
		catch(Exception e) {
			return;
		}
	}
}
