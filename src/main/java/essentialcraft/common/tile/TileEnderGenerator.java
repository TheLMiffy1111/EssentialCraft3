package essentialcraft.common.tile;

import java.util.List;

import essentialcraft.api.ApiCore;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

public class TileEnderGenerator extends TileMRUGeneric {

	public static int cfgMaxMRU = ApiCore.GENERATOR_MAX_MRU_GENERIC;
	public static float cfgBalance = -1F;
	public static int mruGenerated = 500;
	public static int endermenCatchRadius = 8;

	private boolean firstTick = true;

	public TileEnderGenerator() {
		super(cfgMaxMRU);
		slot0IsBoundGem = false;
	}

	@Override
	public void update() {
		if(firstTick) {
			if(cfgBalance < 0) {
				mruStorage.setBalance(getWorld().rand.nextFloat()*2);
			}
			else {
				mruStorage.setBalance(cfgBalance);
			}
		}
		super.update();
		firstTick = false;
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			AxisAlignedBB endermenTPRadius = new AxisAlignedBB(pos.getX()-endermenCatchRadius, pos.getY()-endermenCatchRadius, pos.getZ()-endermenCatchRadius, pos.getX()+endermenCatchRadius+1, pos.getY()+endermenCatchRadius+1, pos.getZ()+endermenCatchRadius+1);
			List<EntityEnderman> l = getWorld().getEntitiesWithinAABB(EntityEnderman.class, endermenTPRadius);
			if(Loader.isModLoaded("hardcoreenderexpansion")) {
				try {
					l.addAll(getWorld().getEntitiesWithinAABB((Class<? extends EntityEnderman>)Class.forName("chylex.hee.entity.mob.EntityMobAngryEnderman"), endermenTPRadius));
					l.addAll(getWorld().getEntitiesWithinAABB((Class<? extends EntityEnderman>)Class.forName("chylex.hee.entity.mob.EntityMobBabyEnderman"), endermenTPRadius));
					l.addAll(getWorld().getEntitiesWithinAABB((Class<? extends EntityEnderman>)Class.forName("chylex.hee.entity.mob.EntityMobEndermage"), endermenTPRadius));
					l.addAll(getWorld().getEntitiesWithinAABB((Class<? extends EntityEnderman>)Class.forName("chylex.hee.entity.mob.EntityMobEnderman"), endermenTPRadius));
					l.addAll(getWorld().getEntitiesWithinAABB((Class<? extends EntityEnderman>)Class.forName("chylex.hee.entity.mob.EntityMobParalyzedEnderman"), endermenTPRadius));
				}
				catch(ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			if(!l.isEmpty()) {
				for(int i = 0; i < l.size(); ++i) {
					l.get(i).setPositionAndRotation(pos.getX()+0.5D, pos.getY()+1D, pos.getZ()+0.5D, 0, 0);
				}
			}
			AxisAlignedBB endermanAttackRad = new AxisAlignedBB(pos.getX()-2, pos.getY()-2, pos.getZ()-2, pos.getX()+2, pos.getY()+2, pos.getZ()+2);
			List<EntityEnderman> l1 = getWorld().getEntitiesWithinAABB(EntityEnderman.class, endermanAttackRad);
			if(Loader.isModLoaded("hardcoreenderexpansion")) {
				try {
					l1.addAll(getWorld().getEntitiesWithinAABB((Class<? extends EntityEnderman>)Class.forName("chylex.hee.entity.mob.EntityMobAngryEnderman"), endermanAttackRad));
					l1.addAll(getWorld().getEntitiesWithinAABB((Class<? extends EntityEnderman>)Class.forName("chylex.hee.entity.mob.EntityMobBabyEnderman"), endermanAttackRad));
					l1.addAll(getWorld().getEntitiesWithinAABB((Class<? extends EntityEnderman>)Class.forName("chylex.hee.entity.mob.EntityMobEndermage"), endermanAttackRad));
					l1.addAll(getWorld().getEntitiesWithinAABB((Class<? extends EntityEnderman>)Class.forName("chylex.hee.entity.mob.EntityMobEnderman"), endermanAttackRad));
					l1.addAll(getWorld().getEntitiesWithinAABB((Class<? extends EntityEnderman>)Class.forName("chylex.hee.entity.mob.EntityMobParalyzedEnderman"), endermanAttackRad));
				}
				catch(ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			if(!l1.isEmpty()) {
				for(int i = 0; i < l1.size(); ++i) {
					if(l1.get(i).attackEntityFrom(DamageSource.MAGIC, 1)) {
						mruStorage.addMRU(mruGenerated, true);
					}
				}
			}
		}
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.endergenerator";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.GENERATOR_MAX_MRU_GENERIC).setMinValue(1).getInt();
			cfgBalance = (float)cfg.get(category, "Balance", -1D, "Default balance, -1 is random").setMinValue(-1D).setMaxValue(2D).getDouble();
			mruGenerated = cfg.get(category, "MRUGenerated", 500, "MRU generated per hit").setMinValue(0).getInt();
			endermenCatchRadius = cfg.get(category, "Radius", 8, "Radius of Endermen detection").setMinValue(0).getInt();
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
