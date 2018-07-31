package essentialcraft.common.tile;

import essentialcraft.api.ApiCore;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.common.config.Configuration;

public class TileMagicalRepairer extends TileMRUGeneric {

	public static int cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;
	public static boolean generatesCorruption = true;
	public static int genCorruption = 3;
	public static int mruUsage = 70;

	public TileMagicalRepairer() {
		super(cfgMaxMRU);
		setSlotsNum(2);
	}

	@Override
	public void update() {
		super.update();
		mruStorage.update(getPos(), getWorld(), getStackInSlot(0));
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0)
			repare();
		spawnParticles();
	}

	public void repare() {
		ItemStack repareItem = getStackInSlot(1);
		if(canRepare(repareItem)) {
			if(mruStorage.getMRU() >= mruUsage) {
				mruStorage.extractMRU(mruUsage, true);
				repareItem.setItemDamage(repareItem.getItemDamage() - 1);
				if(generatesCorruption)
					ECUtils.randomIncreaseCorruptionAt(getWorld(), pos, getWorld().rand, (genCorruption));
			}
		}
	}

	public boolean canRepare(ItemStack s) {
		return !s.isEmpty() && s.getItemDamage() != 0 && s.getItem().isRepairable() && mruStorage.getMRU() >= mruUsage;
	}

	public void spawnParticles() {
		if(world.isRemote && canRepare(getStackInSlot(1)) && mruStorage.getMRU() > 0) {
			for(int o = 0; o < 10; ++o) {
				getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+0.25D + getWorld().rand.nextDouble()/2.2D, pos.getY()+0.25D+(float)o/20, pos.getZ()+0.25D + getWorld().rand.nextDouble()/2.2D, 1.0D, 0.0D, 1.0D);
			}
		}
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.magicalrepairer";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.DEVICE_MAX_MRU_GENERIC).setMinValue(1).getInt();
			mruUsage = cfg.get(category, "MRUUsage", 500).setMinValue(70).setMaxValue(cfgMaxMRU).getInt();
			generatesCorruption = cfg.get(category, "GenerateCorruption", true).getBoolean();
			genCorruption = cfg.get(category, "MaxCorruptionGen", 3, "Max amount of corruption generated per tick").setMinValue(0).getInt();
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
