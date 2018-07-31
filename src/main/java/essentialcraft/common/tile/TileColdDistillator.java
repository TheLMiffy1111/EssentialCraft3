package essentialcraft.common.tile;

import java.util.List;

import essentialcraft.api.ApiCore;
import essentialcraft.api.IColdBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.config.Configuration;

public class TileColdDistillator extends TileMRUGeneric {

	public static float balanceProduced = 0F;
	public static int cfgMaxMRU = ApiCore.GENERATOR_MAX_MRU_GENERIC*10;
	public static double mruGenModifier = 1;
	public static boolean harmEntities = true;

	public TileColdDistillator() {
		super(cfgMaxMRU);
		slot0IsBoundGem = false;
	}

	public boolean canGenerateMRU() {
		return false;
	}

	@Override
	public void update() {
		super.update();
		mruStorage.setBalance(balanceProduced);
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			int mruGenerated = (int)(getMRU()*mruGenModifier);
			mruStorage.addMRU(mruGenerated, true);
			if(mruGenerated > 0 && !getWorld().isRemote && harmEntities) {
				damageAround();
			}
		}
	}

	public void damageAround() {
		List<EntityLivingBase> l = getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos).grow(3D, 3D, 3D));
		if(!l.isEmpty()) {
			EntityLivingBase e = l.get(getWorld().rand.nextInt(l.size()));
			if(e instanceof EntityPlayer && !((EntityPlayer)e).capabilities.isCreativeMode) {
				e.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 3000, 2));
				e.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 3000, 2));
				e.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 3000, 2));
				if(getWorld().rand.nextFloat() < 0.2F) {
					e.attackEntityFrom(DamageSource.STARVE, 1);
				}
			}
			else if(!(e instanceof EntityPlayer)) {
				e.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 3000, 2));
				e.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 3000, 2));
				e.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 3000, 2));
				if(getWorld().rand.nextFloat() < 0.2F) {
					e.attackEntityFrom(DamageSource.STARVE, 1);
				}
			}
		}
	}

	public int getMRU() {
		double i = 0;
		for(int x = -3; x <= 3; ++x) {
			for(int z = -3; z <= 3; ++z) {
				for(int y = -3; y <= 3; ++y) {
					if(getWorld().getBlockState(pos.add(x, y, z)).getBlock() == Blocks.ICE) {
						i += 0.15D;
					}
					if(getWorld().getBlockState(pos.add(x, y, z)).getBlock() == Blocks.SNOW) {
						i += 0.2D;
					}
					if(getWorld().getBlockState(pos.add(x, y, z)).getBlock() == Blocks.SNOW_LAYER) {
						i += 0.05D;
					}
					if(getWorld().getBlockState(pos.add(x, y, z)).getBlock() == Blocks.PACKED_ICE) {
						i += 0.3D;
					}
					Block b = getWorld().getBlockState(pos.add(x, y, z)).getBlock();
					if(b != null && b instanceof IColdBlock) {
						i += ((IColdBlock)b).getColdModifier(getWorld(), pos.add(x, y, z));
					}
				}
			}
		}
		return (int)i;
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.colddistillator";
			balanceProduced = (float)cfg.get(category, "Balance", 0D).setMinValue(0D).setMaxValue(2D).getDouble();
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.GENERATOR_MAX_MRU_GENERIC*10).setMinValue(1).getInt();
			mruGenModifier = cfg.get(category, "MRUGenModifier", 1D).setMinValue(0D).getDouble();
			harmEntities = cfg.get(category, "DamageEntitiesAround", true).getBoolean();
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
