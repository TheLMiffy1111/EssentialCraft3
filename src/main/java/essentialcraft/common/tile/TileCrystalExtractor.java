package essentialcraft.common.tile;

import essentialcraft.api.ApiCore;
import essentialcraft.common.item.ItemsCore;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.common.config.Configuration;

public class TileCrystalExtractor extends TileMRUGeneric {

	public int progressLevel;
	public static int cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;
	public static int mruUsage = 100;
	public static int requiredTime = 1000;

	public TileCrystalExtractor() {
		super(cfgMaxMRU);
		setSlotsNum(13);
	}

	@Override
	public void update() {
		super.update();
		mruStorage.update(getPos(), getWorld(), getStackInSlot(0));

		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0)
			doWork();
		spawnParticles();

	}

	public void doWork() {
		if(canWork()) {
			if(mruStorage.getMRU() >= mruUsage) {
				mruStorage.extractMRU(mruUsage, true);
				++progressLevel;
				if(progressLevel >= requiredTime) {
					progressLevel = 0;
					createItems();
				}
			}
		}
	}

	public void createItems() {
		TileElementalCrystal t = getCrystal();
		double f = t.fire;
		double w = t.water;
		double e = t.earth;
		double a = t.air;
		double s = t.size * 3000;
		double[] baseChance = {f, w, e, a};
		int[] essenceChance = {37500, 50000, 75000, 150000};
		int[] getChance = new int[16];
		for(int i = 0; i < 16; ++i) {
			getChance[i] = (int)(s*baseChance[i%4]/essenceChance[i/4]);
		}
		for(int i = 1; i < 13; ++i) {
			ItemStack st = new ItemStack(ItemsCore.essence,1,getWorld().rand.nextInt(16));
			if(getWorld().rand.nextInt(100) < getChance[st.getItemDamage()]) {
				int sts = getWorld().rand.nextInt(1 + getChance[st.getItemDamage()]/4);
				st.setCount(sts);
				if(st.getCount() <= 0) {
					st.setCount(1);
				}
				setInventorySlotContents(i, st);
			}
		}
	}

	public boolean canWork() {
		for(int i = 1; i < 13; ++i) {
			if(!getStackInSlot(i).isEmpty()) {
				return false;
			}
		}
		if(getCrystal() == null)
			return false;
		return true;
	}

	public boolean hasItemInSlots() {
		for(int i = 1; i < 13; ++i) {
			if(!getStackInSlot(i).isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public void spawnParticles() {
		if(world.isRemote && canWork() && mruStorage.getMRU() > 0) {
			TileElementalCrystal t = getCrystal();
			if(t != null) {
				for(int o = 0; o < 10; ++o) {
					getWorld().spawnParticle(EnumParticleTypes.PORTAL, pos.getX() + getWorld().rand.nextDouble(), t.getPos().getY() + getWorld().rand.nextDouble(), pos.getZ() + getWorld().rand.nextDouble(), t.getPos().getX()-pos.getX(), 0.0D, t.getPos().getZ()-pos.getZ());
				}
			}
		}
	}

	public TileElementalCrystal getCrystal() {
		TileElementalCrystal t = null;
		if(hasCrystalOnEast()) {
			t = (TileElementalCrystal)getWorld().getTileEntity(pos.east());
		}
		if(hasCrystalOnWest()) {
			t = (TileElementalCrystal)getWorld().getTileEntity(pos.west());
		}
		if(hasCrystalOnSouth()) {
			t = (TileElementalCrystal)getWorld().getTileEntity(pos.south());
		}
		if(hasCrystalOnNorth()) {
			t = (TileElementalCrystal)getWorld().getTileEntity(pos.north());
		}
		return t;
	}

	public boolean hasCrystalOnEast() {
		TileEntity t = getWorld().getTileEntity(pos.east());
		return t instanceof TileElementalCrystal;
	}

	public boolean hasCrystalOnWest() {
		TileEntity t = getWorld().getTileEntity(pos.west());
		return t instanceof TileElementalCrystal;
	}

	public boolean hasCrystalOnSouth() {
		TileEntity t = getWorld().getTileEntity(pos.south());
		return t instanceof TileElementalCrystal;
	}

	public boolean hasCrystalOnNorth() {
		TileEntity t = getWorld().getTileEntity(pos.north());
		return t instanceof TileElementalCrystal;
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.crystalextractor";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.DEVICE_MAX_MRU_GENERIC).setMinValue(1).getInt();
			mruUsage = cfg.get(category, "MRUUsage", 100).setMinValue(0).setMaxValue(cfgMaxMRU).getInt();
			requiredTime = cfg.get(category, "RequiredTicks", 1000, "Ticks required to get an essence").setMinValue(0).getInt();
		}
		catch(Exception e) {
			return;
		}
	}

	@Override
	public int[] getOutputSlots() {
		return new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot == 0 && isBoundGem(stack);
	}
}
