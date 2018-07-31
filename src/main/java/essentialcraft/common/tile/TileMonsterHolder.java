package essentialcraft.common.tile;

import java.util.List;

import DummyCore.Utils.Coord3D;
import DummyCore.Utils.DummyDistance;
import essentialcraft.api.ApiCore;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.config.Configuration;

public class TileMonsterHolder extends TileMRUGeneric {
	public static double rad = 12D;
	public static int cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;
	public static boolean generatesCorruption = false;
	public static int genCorruption = 1;
	public static int mruUsage = 1;

	public TileMonsterHolder() {
		super(cfgMaxMRU);
		setSlotsNum(1);
	}

	@Override
	public void update() {
		super.update();
		mruStorage.update(getPos(), getWorld(), getStackInSlot(0));
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			List<EntityLivingBase> lst = getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos.getX()-32, pos.getY()-32, pos.getZ()-32, pos.getX()+33, pos.getY()+33, pos.getZ()+33));
			if(!lst.isEmpty()) {
				for(int i = 0; i < lst.size(); ++i)
				{
					EntityLivingBase e = lst.get(i);
					if(!(e instanceof EntityPlayer)) {
						if(mruStorage.getMRU() >= mruUsage) {
							mruStorage.extractMRU(mruUsage, true);
							Coord3D tilePos = new Coord3D(pos.getX()+0.5D,pos.getY()+0.5D,pos.getZ()+0.5D);
							Coord3D mobPosition = new Coord3D(e.posX,e.posY,e.posZ);
							DummyDistance dist = new DummyDistance(tilePos,mobPosition);
							if(dist.getDistance() < rad && dist.getDistance() >= rad - 3) {
								Vec3d posVector = new Vec3d(tilePos.x-mobPosition.x,tilePos.y-mobPosition.y ,tilePos.z-mobPosition.z);
								e.setPositionAndRotation(tilePos.x-posVector.x/1.1D, tilePos.y-posVector.y/1.1D, tilePos.z-posVector.z/1.1D, e.rotationYaw, e.rotationPitch);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.monsterholder";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.DEVICE_MAX_MRU_GENERIC).setMinValue(1).getInt();
			mruUsage = cfg.get(category, "MRUUsage", 1, "MRU Usage Per Mob").setMinValue(0).setMaxValue(cfgMaxMRU).getInt();
			generatesCorruption = cfg.get(category, "GenerateCorruption", false).getBoolean();
			genCorruption = cfg.get(category, "MaxCorruptionGen", 1, "Max amount of corruption generated per tick").setMinValue(0).getInt();
			rad = cfg.get(category, "Radius", 12D).setMinValue(0D).getDouble();
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
