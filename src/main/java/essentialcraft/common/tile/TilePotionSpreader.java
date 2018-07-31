package essentialcraft.common.tile;

import java.util.List;

import DummyCore.Utils.MathUtils;
import DummyCore.Utils.MiscUtils;
import essentialcraft.api.ApiCore;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.config.Configuration;

public class TilePotionSpreader extends TileMRUGeneric {
	public ResourceLocation potionID = null;
	public int potionDuration = -1;
	public int potionAmplifier = -1;
	public int potionUseTime = -1;
	public static int cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;
	public static boolean generatesCorruption = false;
	public static int genCorruption = 5;
	public static int mruUsage = 250;
	public static int potionGenUseTime = 16;

	public TilePotionSpreader() {
		super(cfgMaxMRU);
		setSlotsNum(9);
	}

	@Override
	public void update() {
		super.update();
		mruStorage.update(getPos(), getWorld(), getStackInSlot(0));
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			if(potionID == null)
				for(int i = 1; i < 9; ++i) {
					ItemStack stk = getStackInSlot(i);
					if(!stk.isEmpty() && stk.getItem() instanceof ItemPotion) {
						List<PotionEffect> lst = PotionUtils.getEffectsFromStack(stk);
						if(!lst.isEmpty()) {
							PotionEffect effect = lst.get(0);
							potionID = effect.getPotion().getRegistryName();
							potionAmplifier = effect.getAmplifier();
							potionDuration = effect.getDuration();
							potionUseTime = potionGenUseTime;
							setInventorySlotContents(i, ItemStack.EMPTY);
							break;
						}
					}
				}
			else {
				Potion actualPotion = Potion.REGISTRY.getObject(potionID);
				List<EntityLivingBase> lst = getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos.getX()-8, pos.getY()-8, pos.getZ()-8, pos.getX()+9, pos.getY()+9, pos.getZ()+9));
				if(!lst.isEmpty() && !getWorld().isRemote) {
					boolean haveUsedPotion = false;
					for(int i = 0; i < lst.size(); ++i) {
						EntityLivingBase base = lst.get(i);
						boolean shouldUsePotion = false;
						PotionEffect effect = new PotionEffect(actualPotion,potionDuration,potionAmplifier,true,true);
						if(actualPotion == MobEffects.INSTANT_HEALTH) {
							float healAmount = Math.max(4 << effect.getAmplifier(), 0);
							shouldUsePotion = !base.isEntityUndead() && base.getHealth()+healAmount <= base.getMaxHealth() || base.isEntityUndead() && base.hurtResistantTime == 0 && base.hurtTime == 0;
						}
						else if(actualPotion == MobEffects.INSTANT_DAMAGE) {
							float damageAmount = 6 << effect.getAmplifier();
							shouldUsePotion = base.isEntityUndead() && base.getHealth()+damageAmount <= base.getMaxHealth() || !base.isEntityUndead() && base.hurtResistantTime == 0 && base.hurtTime == 0;
						}
						else {
							shouldUsePotion = !base.isPotionActive(actualPotion);
						}
						if(shouldUsePotion && mruStorage.getMRU() >= mruUsage) {
							mruStorage.extractMRU(mruUsage, true);
							haveUsedPotion = true;
							base.addPotionEffect(effect);
							int j = actualPotion.getLiquidColor();
							float f = 0F;
							float f1 = 0F;
							float f2 = 0F;
							f += (j >> 16 & 255) / 255.0F;
							f1 += (j >> 8 & 255) / 255.0F;
							f2 += (j >> 0 & 255) / 255.0F;
							for(int i1 = 0; i1 < 100; ++i1)
								MiscUtils.spawnParticlesOnServer("spell_mob", (float)(base.posX + MathUtils.randomFloat(getWorld().rand)), (float)(base.posY+1 + MathUtils.randomFloat(getWorld().rand)), (float)(base.posZ + MathUtils.randomFloat(getWorld().rand)), f, f1, f2);
						}
						if(generatesCorruption)
							ECUtils.randomIncreaseCorruptionAt(getWorld(), pos, getWorld().rand, (genCorruption));
					}
					if(haveUsedPotion)
						--potionUseTime;
				}
				if(potionUseTime <= 0) {
					potionID = null;
					potionAmplifier = -1;
					potionDuration = -1;
				}
				if(potionID != null) {
					int j = actualPotion.getLiquidColor();
					float f = 0F;
					float f1 = 0F;
					float f2 = 0F;
					f += (j >> 16 & 255) / 255.0F;
					f1 += (j >> 8 & 255) / 255.0F;
					f2 += (j >> 0 & 255) / 255.0F;
					getWorld().spawnParticle(EnumParticleTypes.SPELL_MOB, pos.getX()+0.5F, pos.getY()+0.5F, pos.getZ()+0.5F, f, f1, f2);
				}
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound i) {
		potionID = new ResourceLocation(i.getString("potionID"));
		potionDuration = i.getInteger("potionDuration");
		potionAmplifier = i.getInteger("potionAmplifier");
		potionUseTime = i.getInteger("potionUseTime");
		super.readFromNBT(i);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound i) {
		i.setString("potionID", potionID.toString());
		i.setInteger("potionDuration", potionDuration);
		i.setInteger("potionAmplifier", potionAmplifier);
		i.setInteger("potionUseTime", potionUseTime);
		return super.writeToNBT(i);
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.potionspreader";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.DEVICE_MAX_MRU_GENERIC).setMinValue(1).getInt();
			mruUsage = cfg.get(category, "MRUUsage", 250, "MRU Usage Per Mob").setMinValue(0).setMaxValue(cfgMaxMRU).getInt();
			generatesCorruption = cfg.get(category, "GenerateCorruption", false).getBoolean();
			genCorruption = cfg.get(category, "MaxCorruptionGen", 5, "Max amount of corruption generated per tick").setMinValue(0).getInt();
			potionGenUseTime = cfg.get(category, "MaxSpreadAmount", 16, "Amount of times one potion can be spreaded").setMinValue(0).getInt();
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
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot == 0 ? isBoundGem(stack) : stack.getItem() instanceof ItemPotion;
	}
}
