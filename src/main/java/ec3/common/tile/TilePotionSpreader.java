package ec3.common.tile;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.config.Configuration;
import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.MathUtils;
import DummyCore.Utils.MiscUtils;
import ec3.api.ApiCore;
import ec3.utils.common.ECUtils;

public class TilePotionSpreader extends TileMRUGeneric {
	public int potionID = -1;
	public int potionDuration = -1;
	public int potionAmplifier = -1;
	public int potionUseTime = -1;
	public static float cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;
	public static boolean generatesCorruption = false;
	public static int genCorruption = 5;
	public static int mruUsage = 250;
	public static int potionGenUseTime = 16;
	
	public TilePotionSpreader() {
		super();
		maxMRU = (int)cfgMaxMRU;
		setSlotsNum(9);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void update() {
		super.update();
		ECUtils.manage(this, 0);
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			if(potionID == -1)
				for(int i = 1; i < 9; ++i) {
					ItemStack stk = getStackInSlot(i);
					if(stk != null && stk.getItem() instanceof ItemPotion) {
						List<PotionEffect> lst = PotionUtils.getEffectsFromStack(stk);
						if(!lst.isEmpty()) {
							PotionEffect effect = lst.get(0);
							potionID = Potion.getIdFromPotion(effect.getPotion());
							potionAmplifier = effect.getAmplifier();
							potionDuration = effect.getDuration();
							potionUseTime = potionGenUseTime;
							setInventorySlotContents(i, null);
							break;
						}
					}
				}
			else {
				Potion actualPotion = Potion.getPotionById(potionID);
				List<EntityLivingBase> lst = getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos.getX()-8, pos.getY()-8, pos.getZ()-8, pos.getX()+9, pos.getY()+9, pos.getZ()+9));
				if(!lst.isEmpty() && !getWorld().isRemote) {
					boolean haveUsedPotion = false;
					for(int i = 0; i < lst.size(); ++i) {
						EntityLivingBase base = lst.get(i);
						boolean shouldUsePotion = false;
						PotionEffect effect = new PotionEffect(actualPotion,potionDuration,potionAmplifier);
						if(actualPotion == MobEffects.INSTANT_HEALTH) {
							float healAmount = (float)Math.max(4 << effect.getAmplifier(), 0);
							shouldUsePotion = (!base.isEntityUndead() && base.getHealth()+healAmount <= base.getMaxHealth()) || (base.isEntityUndead() && base.hurtResistantTime == 0 && base.hurtTime == 0);
						}
						else if(actualPotion == MobEffects.INSTANT_DAMAGE) {
							float damageAmount = (float)(6 << effect.getAmplifier());
							shouldUsePotion = (base.isEntityUndead() && base.getHealth()+damageAmount <= base.getMaxHealth()) || (!base.isEntityUndead() && base.hurtResistantTime == 0 && base.hurtTime == 0);
						}
						else {
							shouldUsePotion = !base.isPotionActive(actualPotion);
						}
						if(shouldUsePotion && getMRU() >= mruUsage) {
							setMRU(getMRU()-mruUsage);
							haveUsedPotion = true;
							base.addPotionEffect(effect);
							int j = actualPotion.getLiquidColor();
							float f = 0F;
							float f1 = 0F;
							float f2 = 0F;
					        f += (float)(j >> 16 & 255) / 255.0F;
					        f1 += (float)(j >> 8 & 255) / 255.0F;
					        f2 += (float)(j >> 0 & 255) / 255.0F;
					        for(int i1 = 0; i1 < 100; ++i1)
					        	MiscUtils.spawnParticlesOnServer("spell_mob", (float)(base.posX + MathUtils.randomFloat(getWorld().rand)), (float)(base.posY+1 + MathUtils.randomFloat(getWorld().rand)), (float)(base.posZ + MathUtils.randomFloat(getWorld().rand)), f, f1, f2);
						}
						if(generatesCorruption)
							ECUtils.increaseCorruptionAt(getWorld(), pos.getX(), pos.getY(), pos.getZ(), getWorld().rand.nextInt(genCorruption));
					}
					if(haveUsedPotion)
						--potionUseTime;
				}
				if(potionUseTime <= 0) {
					potionID = -1;
					potionAmplifier = -1;
					potionDuration = -1;
				}
				if(potionID != -1) {
					int j = actualPotion.getLiquidColor();
					float f = 0F;
					float f1 = 0F;
					float f2 = 0F;
					f += (float)(j >> 16 & 255) / 255.0F;
					f1 += (float)(j >> 8 & 255) / 255.0F;
					f2 += (float)(j >> 0 & 255) / 255.0F;
					getWorld().spawnParticle(EnumParticleTypes.SPELL_MOB, pos.getX()+0.5F, pos.getY()+0.5F, pos.getZ()+0.5F, f, f1, f2);
				}
			}
		}
	}
    
	@Override
	public void readFromNBT(NBTTagCompound i) {
		potionID = i.getInteger("potionID");
		potionDuration = i.getInteger("potionDuration");
		potionAmplifier = i.getInteger("potionAmplifier");
		potionUseTime = i.getInteger("potionUseTime");
		super.readFromNBT(i);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound i) {
		i.setInteger("potionID", potionID);
		i.setInteger("potionDuration", potionDuration);
		i.setInteger("potionAmplifier", potionAmplifier);
		i.setInteger("potionUseTime", potionUseTime);
		return super.writeToNBT(i);
	}
	
	public static void setupConfig(Configuration cfg) {
		try {
			cfg.load();
			String[] cfgArrayString = cfg.getStringList("PotionSpreaderSettings", "tileentities", new String[] {
					"Max MRU:" + ApiCore.DEVICE_MAX_MRU_GENERIC,
					"MRU Usage Per Mob:250",
					"Can this device actually generate corruption:false",
					"The amount of corruption generated each tick(do not set to 0!):5",
					"Amount of times one potion can be spreaded:16"
			}, "");
			String dataString = "";
			
			for(int i = 0; i < cfgArrayString.length; ++i)
				dataString += "||" + cfgArrayString[i];
			
			DummyData[] data = DataStorage.parseData(dataString);
			
			mruUsage = Integer.parseInt(data[1].fieldValue);
			cfgMaxMRU = Float.parseFloat(data[0].fieldValue);
			generatesCorruption = Boolean.parseBoolean(data[2].fieldValue);
			genCorruption = Integer.parseInt(data[3].fieldValue);
			potionGenUseTime = Integer.parseInt(data[4].fieldValue);
			
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
	
	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return p_94041_1_ == 0 ? isBoundGem(p_94041_2_) : p_94041_2_.getItem() instanceof ItemPotion;
	}
}
