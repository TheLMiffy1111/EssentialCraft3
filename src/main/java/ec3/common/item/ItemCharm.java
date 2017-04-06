package ec3.common.item;

import java.util.List;
import java.util.Locale;

import DummyCore.Client.IModelRegisterer;
import baubles.api.BaubleType;
import baubles.api.IBauble;
import ec3.utils.common.ECUtils;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.ModelLoader;

public class ItemCharm extends ItemStoresMRUInNBT implements IBauble, IModelRegisterer {

	public String[] name = new String[] {"Fire", "Water", "Earth", "Air", "Steam", "Magma", "Lightning", "Life", "Rain", "Dust", "None"};
	public ItemCharm() {
		super();
		setMaxMRU(5000);
		setMaxDamage(0);
		maxStackSize = 1;
		bFull3D = false;
		setHasSubtypes(true);
	}

	public void onWornTick(ItemStack s, EntityLivingBase entity) {
		if(entity instanceof EntityPlayer) {
			EntityPlayer e = (EntityPlayer) entity;
			int dam = s.getItemDamage();
			switch(dam) {
			case 0:
				updateFire(e, s);
				break;
			case 1:
				updateWater(e, s);
				break;
			case 2:
				updateEarth(e, s);
				break;
			case 3:
				updateAir(e, s);
				break;
			case 4:
				updateSteam(e, s);
				break;
			case 5:
				updateMagma(e, s);
				break;
			case 6:
				updateLightning(e, s);
				break;
			case 7:
				updateLife(e, s);
				break;
			case 8:
				updateRain(e, s);
				break;
			case 9:
				updateDust(e, s);
				break;
			}
		}
	}

	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		for(int var4 = 0; var4 < 10; ++var4) {
			ItemStack min = new ItemStack(par1, 1, var4);
			ECUtils.initMRUTag(min, maxMRU);
			ItemStack max = new ItemStack(par1, 1, var4);
			ECUtils.initMRUTag(max, maxMRU);
			ECUtils.getStackTag(max).setInteger("mru", ECUtils.getStackTag(max).getInteger("maxMRU"));
			par3List.add(min);
			par3List.add(max);
		}
	}

	public void updateFire(EntityPlayer e, ItemStack s) {
		if(e.isBurning() && !e.isPotionActive(MobEffects.FIRE_RESISTANCE) && (ECUtils.tryToDecreaseMRUInStorage(e, -50) || setMRU(s, -50))) {
			e.extinguish();
			e.getEntityWorld().playSound(e, e.posX, e.posY, e.posZ, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.PLAYERS, 1, 1);
		}
	}

	public void updateWater(EntityPlayer e, ItemStack s) {
		if(e.getAir() < 10 && e.isInWater() && (ECUtils.tryToDecreaseMRUInStorage(e, -100) || setMRU(s, -100))) {
			e.setAir(100);
			e.getEntityWorld().playSound(e, e.posX, e.posY, e.posZ, SoundEvents.ENTITY_PLAYER_BREATH, SoundCategory.PLAYERS, 1, 1);
		}
	}

	public void updateEarth(EntityPlayer e, ItemStack s) {
		if(e.hurtTime > 0 && !e.isPotionActive(MobEffects.RESISTANCE) && (ECUtils.tryToDecreaseMRUInStorage(e, -200) || setMRU(s, -200)))
			e.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 100, 0));
	}

	public void updateAir(EntityPlayer e, ItemStack s) {
		if(e.isSprinting() && !e.isPotionActive(MobEffects.SPEED) && (ECUtils.tryToDecreaseMRUInStorage(e, -10) || setMRU(s, -10))) {
			e.addPotionEffect(new PotionEffect(MobEffects.SPEED, 20, 1));
			e.getEntityWorld().playSound(e, e.posX, e.posY, e.posZ, SoundEvents.ENTITY_PLAYER_BREATH, SoundCategory.PLAYERS, 1, .01F);
		}
	}

	public void updateSteam(EntityPlayer e, ItemStack s) {
		if(e.getHealth() < 5 && !e.isPotionActive(MobEffects.SPEED) && (ECUtils.tryToDecreaseMRUInStorage(e, -200) || setMRU(s, -200))) {
			e.addPotionEffect(new PotionEffect(MobEffects.SPEED, 100, 5));
			e.getEntityWorld().playSound(e, e.posX, e.posY, e.posZ, SoundEvents.ENTITY_PLAYER_BREATH, SoundCategory.PLAYERS, 1, .01F);
			e.getEntityWorld().playSound(e, e.posX, e.posY, e.posZ, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.PLAYERS, 1, .01F);
		}
	}

	public void updateMagma(EntityPlayer e, ItemStack s) {
		Material m = e.getEntityWorld().getBlockState(new BlockPos((int)e.posX-1, (int)e.posY-1, (int)e.posZ)).getMaterial();
		Material m1 = e.getEntityWorld().getBlockState(new BlockPos((int)e.posX-1, (int)e.posY, (int)e.posZ)).getMaterial();
		if((m == Material.LAVA || m1 == Material.LAVA) && !e.isPotionActive(MobEffects.FIRE_RESISTANCE) && (ECUtils.tryToDecreaseMRUInStorage(e, -100) || setMRU(s, -100))) {
			e.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 100, 0));
			e.getEntityWorld().playSound(e, e.posX, e.posY, e.posZ, SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.PLAYERS, 1, 10F);
		}
	}

	public void updateLightning(EntityPlayer e, ItemStack s) {
		if(e.getEntityWorld().isThundering()&& !e.isPotionActive(MobEffects.STRENGTH) && (ECUtils.tryToDecreaseMRUInStorage(e, -100) || setMRU(s, -100))) {
			e.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 100, 0));
			e.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 600, 0));
			e.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 100, 0));
			e.getEntityWorld().playSound(e, e.posX, e.posY, e.posZ, SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.PLAYERS, 1, 1F);
		}
	}

	public void updateLife(EntityPlayer e, ItemStack s) {
		if(e.getHealth() < 5 && !e.isPotionActive(MobEffects.REGENERATION) && (ECUtils.tryToDecreaseMRUInStorage(e, -200) || setMRU(s, -200))) {
			e.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 50, 1));
			e.getEntityWorld().playSound(e, e.posX, e.posY, e.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1,  10F);
		}
		if(e.getHealth() < 20 && !e.isPotionActive(MobEffects.REGENERATION) && (ECUtils.tryToDecreaseMRUInStorage(e, -50) || setMRU(s, -50))) {
			e.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 50, 0));
			e.getEntityWorld().playSound(e, e.posX, e.posY, e.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1, 10F);
		}
	}

	public void updateRain(EntityPlayer e, ItemStack s) {
		if(e.getEntityWorld().isRaining() && !e.isPotionActive(MobEffects.HASTE) && (ECUtils.tryToDecreaseMRUInStorage(e, -50) || setMRU(s, -50))) {
			e.addPotionEffect(new PotionEffect(MobEffects.HASTE, 100, 0));
			e.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 100, 2));
			e.addPotionEffect(new PotionEffect(MobEffects.SPEED, 100, 0));
			e.getEntityWorld().playSound(e, e.posX, e.posY, e.posZ, SoundEvents.ENTITY_PLAYER_SPLASH, SoundCategory.PLAYERS, 1, 1F);
		}
	}

	public void updateDust(EntityPlayer e, ItemStack s) {
		Material m = e.getEntityWorld().getBlockState(new BlockPos((int)e.posX-1, (int)e.posY-1, (int)e.posZ)).getMaterial();
		if(m == Material.SAND && !e.isPotionActive(MobEffects.RESISTANCE) && (ECUtils.tryToDecreaseMRUInStorage(e, -100) || setMRU(s, -100))) {
			e.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 100, 0));
			e.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 0));
		}
	}

	public String getItemDisplayName(ItemStack par1ItemStack) {
		return "Charm Of "+name[Math.min(par1ItemStack.getItemDamage(), name.length-1)];
	}

	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.AMULET;
	}

	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {}

	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {}

	public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}

	public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}

	@Override
	public void registerModels() {
		for(int i = 0; i < name.length-1; i++)
			ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation("essentialcraft:item/charm", "type=" + name[i].toLowerCase(Locale.ENGLISH)));
	}
}
