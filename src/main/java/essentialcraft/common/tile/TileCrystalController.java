package essentialcraft.common.tile;

import essentialcraft.api.ApiCore;
import essentialcraft.common.item.ItemEssence;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.common.config.Configuration;

public class TileCrystalController extends TileMRUGeneric {

	public static int cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;
	public static int mruUsage = 100;
	public static double chanceToUseMRU = 0.05D;
	public static double mutateModifier = 0.001D;

	public TileCrystalController() {
		super(cfgMaxMRU);
		setSlotsNum(2);
	}

	@Override
	public void update() {
		super.update();
		mruStorage.update(getPos(), getWorld(), getStackInSlot(0));

		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			if(!getWorld().isRemote && getWorld().rand.nextDouble() < chanceToUseMRU && mruStorage.getMRU() >= mruUsage) {
				mruStorage.extractMRU(mruUsage, true);
			}
		}
		spawnParticles();
		if(!getWorld().isRemote && getWorld().isBlockIndirectlyGettingPowered(pos) == 0)
			mutateToElement();
	}

	public void spawnParticles() {
		if(world.isRemote && mruStorage.getMRU() > 0 && getCrystal() != null) {
			for(int o = 0; o < 2; ++o) {
				getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+0.3D + getWorld().rand.nextDouble()/2, pos.getY()+0.3F + (float)o/2, pos.getZ()+0.3D + getWorld().rand.nextDouble()/2D, -1.0D, 1.0D, 0.0D);
			}
		}
	}

	public void mutateToElement() {
		if(!getStackInSlot(1).isEmpty() && getStackInSlot(1).getItem() instanceof ItemEssence && mruStorage.getMRU() > mruUsage*10 && getCrystal() != null && getCrystal().size < 100) {
			ItemStack e = getStackInSlot(1);
			TileElementalCrystal c = getCrystal();
			int rarity = (int)((double)e.getItemDamage() / 4);
			double chance = mutateModifier * (rarity + 1);
			if(getWorld().rand.nextDouble() < chance) {
				mruStorage.extractMRU(mruUsage*10, true);
				int type = e.getItemDamage()%4;
				c.mutate(type, getWorld().rand.nextInt((rarity + 1) * 2));
				decrStackSize(1, 1);
			}
		}
	}

	public TileElementalCrystal getCrystal() {
		TileElementalCrystal t = null;
		if(hasCrystalOnEast())
			t = (TileElementalCrystal)getWorld().getTileEntity(pos.east());
		if(hasCrystalOnWest())
			t = (TileElementalCrystal)getWorld().getTileEntity(pos.west());
		if(hasCrystalOnSouth())
			t = (TileElementalCrystal)getWorld().getTileEntity(pos.south());
		if(hasCrystalOnNorth())
			t = (TileElementalCrystal)getWorld().getTileEntity(pos.north());
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
			String category = "tileentities.crystalcontroller";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.DEVICE_MAX_MRU_GENERIC).setMinValue(1).getInt();
			mruUsage = cfg.get(category, "MRUUsage", 100).setMinValue(0).setMaxValue(cfgMaxMRU).getInt();
			chanceToUseMRU = cfg.get(category, "UseMRUChance", 0.05D).setMinValue(0D).setMaxValue(1D).getDouble();
			mutateModifier = cfg.get(category, "MutationChanceModifier", 0.001D).setMinValue(0D).getDouble();
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
