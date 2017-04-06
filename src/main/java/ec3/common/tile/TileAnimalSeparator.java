package ec3.common.tile;

import java.util.List;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.math.AxisAlignedBB;
import ec3.api.ApiCore;
import ec3.utils.common.ECUtils;

public class TileAnimalSeparator extends TileMRUGeneric {

	public TileAnimalSeparator() {
		setSlotsNum(1);
		setMaxMRU(ApiCore.DEVICE_MAX_MRU_GENERIC);
	}
	
	@Override
	public int[] getOutputSlots() {
		return new int[0];
	}
	
	@Override
	public void update() {
		super.update();
		ECUtils.manage(this, 0);
	}
	
	@SuppressWarnings("unchecked")
	public void separate(boolean b) {
		AxisAlignedBB toTeleport = new AxisAlignedBB(pos).expand(24, 24, 24);
		AxisAlignedBB noTeleport = new AxisAlignedBB(pos).expand(5, 5, 5);
		List<EntityAgeable> tp = getWorld().getEntitiesWithinAABB(EntityAgeable.class, toTeleport);
		List<EntityAgeable> ntp = getWorld().getEntitiesWithinAABB(EntityAgeable.class, noTeleport);
		for(EntityAgeable e : tp) {
			if(!e.isDead && !(e instanceof IMob) && ((b && e.isChild()) || (!b && !e.isChild())) && getMRU() >= 100 && !ntp.contains(e) && setMRU(getMRU() - 100)) {
				e.setPositionAndRotation(pos.getX()+0.5D, pos.getY()+1.5D, pos.getZ()+0.5D, 0, 0);
			}
		}
	}
}
