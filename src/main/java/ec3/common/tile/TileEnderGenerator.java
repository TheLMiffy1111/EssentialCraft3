package ec3.common.tile;

import java.util.List;

import net.minecraftforge.fml.common.Loader;
import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.config.Configuration;
import ec3.api.ApiCore;

public class TileEnderGenerator extends TileMRUGeneric {
	
	public static float cfgMaxMRU = ApiCore.GENERATOR_MAX_MRU_GENERIC;
	public static float cfgBalance = -1F;
	public static float mruGenerated = 500;
	public static int endermenCatchRadius = 8;
	
	public TileEnderGenerator() {
		super();
		maxMRU = (int)cfgMaxMRU;
		balance = cfgBalance;
		slot0IsBoundGem = false;
	}
	
	public boolean canGenerateMRU() {
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void update() {
		super.update();
		if(balance == -1)
			balance = getWorld().rand.nextFloat()*2;
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			AxisAlignedBB endermenTPRadius = new AxisAlignedBB(pos.getX()-endermenCatchRadius, pos.getY()-endermenCatchRadius, pos.getZ()-endermenCatchRadius, pos.getX()+endermenCatchRadius+1, pos.getY()+endermenCatchRadius+1, pos.getZ()+endermenCatchRadius+1);
			List<EntityEnderman> l = getWorld().getEntitiesWithinAABB(EntityEnderman.class, endermenTPRadius);
			if(Loader.isModLoaded("HardcoreEnderExpansion")) {
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
			if(Loader.isModLoaded("HardcoreEnderExpansion")) {
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
				if(!getWorld().isRemote) {
					for(int i = 0; i < l1.size(); ++i) {
						if(l1.get(i).attackEntityFrom(DamageSource.magic, 1)) {
							setMRU((int)(getMRU() + mruGenerated));
							if(getMRU() > getMaxMRU())
								setMRU(getMaxMRU());
						}
					}
				}
			}
		}
	}
	
	public static void setupConfig(Configuration cfg) {
		try {
			cfg.load();
			String[] cfgArrayString = cfg.getStringList("EnderGeneratorSettings", "tileentities", new String[] {
					"Max MRU:" + ApiCore.GENERATOR_MAX_MRU_GENERIC,
					"Default balance(-1 is random):-1.0",
					"MRU generated per hit:500",
					"Radius of Endermen detection:8"
			}, "");
			String dataString = "";
			
			for(int i = 0; i < cfgArrayString.length; ++i)
				dataString += "||" + cfgArrayString[i];
			
			DummyData[] data = DataStorage.parseData(dataString);
			
			cfgMaxMRU = Float.parseFloat(data[0].fieldValue);
			cfgBalance = Float.parseFloat(data[1].fieldValue);
			mruGenerated = Float.parseFloat(data[2].fieldValue);
			endermenCatchRadius = Integer.parseInt(data[3].fieldValue);
			
			cfg.save();
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
