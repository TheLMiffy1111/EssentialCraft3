package essentialcraft.common.tile;

import java.util.List;

import essentialcraft.api.ApiCore;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.config.Configuration;

public class TileAnimalSeparator extends TileMRUGeneric {

	public static int cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;
	public static int mruUsage = 100;
	public static double radius = 24D;
	public static double radiusIgnore = 5D;
	
	public TileAnimalSeparator() {
		super(cfgMaxMRU);
		setSlotsNum(1);
	}

	@Override
	public int[] getOutputSlots() {
		return new int[0];
	}

	@Override
	public void update() {
		super.update();
		mruStorage.update(getPos(), getWorld(), getStackInSlot(0));
	}

	public void separate(boolean b) {
		AxisAlignedBB toTeleport = new AxisAlignedBB(pos).grow(radius, radius, radius);
		AxisAlignedBB noTeleport = new AxisAlignedBB(pos).grow(radiusIgnore, radiusIgnore, radiusIgnore);
		List<EntityAgeable> tp = getWorld().getEntitiesWithinAABB(EntityAgeable.class, toTeleport, entity->!getWorld().getEntitiesWithinAABB(EntityAgeable.class, noTeleport).contains(entity));
		for(EntityAgeable e : tp) {
			if(!e.isDead && !(e instanceof IMob) && (b && e.isChild() || !b && !e.isChild()) && mruStorage.getMRU() >= 100) {
				mruStorage.extractMRU(mruUsage, true);
				e.setPositionAndRotation(pos.getX()+0.5D, pos.getY()+1.5D, pos.getZ()+0.5D, 0, 0);
			}
		}
	}
	
	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.animalseparator";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.DEVICE_MAX_MRU_GENERIC).setMinValue(1).getInt();
			mruUsage = cfg.get(category, "MRUUsage", 100).setMinValue(0).setMaxValue(cfgMaxMRU).getInt();
			radius = cfg.get(category, "Radius", 24D).setMinValue(0).getDouble();
			radiusIgnore = cfg.get(category, "RadiusIgnore", 5D).setMinValue(0).setMaxValue(radius).getDouble();
		}
		catch(Exception e) {
			return;
		}
	}
}
