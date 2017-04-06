package ec3.common.item;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.MathUtils;
import DummyCore.Utils.MiscUtils;
import baubles.api.BaubleType;
import baubles.api.IBauble;
import ec3.api.ApiCore;
import ec3.api.IMRUStorage;
import ec3.api.IUBMRUGainModifier;
import ec3.api.IWindModifier;
import ec3.api.IWindResistance;
import ec3.utils.cfg.Config;
import ec3.utils.common.RadiationManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.model.ModelLoader;

public class BaublesModifier extends Item implements IBauble, IUBMRUGainModifier, IMRUStorage, IWindResistance, IWindModifier, IModelRegisterer {

	public static String[] names = new String[] {
			"portableMD",//0
			"ringOfStability",//1
			"ringOfExperience",//2
			"ringOfResistance",//3
			"lifePendant",//4
			"windCatcher",//5
			"windMagesTalisman",//6
			"doctorsBelt",//7
			"alphaRing",//8
			"betaRing",//9
			"gammaRing",//10
			"deltaRing",//11
			"epsilonBelt",//12
			"dzetaAmulet",//13
			"etaAmulet",//14
			"thetaBelt",//15
			"iotaRing",//16
			"kappaRing",//17
			"lambdaRing",//18
			"muRing",//19
			"nuAmulet",//20
			"xiBelt",//21
			"omicronAmulet",//22
			"piBelt",//23
			"rhoRing",//24
			"sigmaRing",//25
			"tauRing",//26
			"upsilonRing",//27
			"phiRing",//28
			"chiRing",//29
			"psiRing",//30
			"omegaRing",//31
			"unknown",//fallback
	};

	public BaubleType[] btALST = new BaubleType[] {
			BaubleType.AMULET,
			BaubleType.RING,
			BaubleType.RING,
			BaubleType.RING,
			BaubleType.AMULET,
			BaubleType.AMULET,
			BaubleType.AMULET,
			BaubleType.BELT,
			BaubleType.RING,
			BaubleType.RING,
			BaubleType.RING,
			BaubleType.RING,
			BaubleType.BELT,
			BaubleType.AMULET,
			BaubleType.AMULET,
			BaubleType.BELT,
			BaubleType.RING,
			BaubleType.RING,
			BaubleType.RING,
			BaubleType.RING,
			BaubleType.AMULET,
			BaubleType.BELT,
			BaubleType.AMULET,
			BaubleType.BELT,
			BaubleType.RING,
			BaubleType.RING,
			BaubleType.RING,
			BaubleType.RING,
			BaubleType.RING,
			BaubleType.RING,
			BaubleType.RING,
			BaubleType.RING,
			BaubleType.RING,
	};

	public BaublesModifier() {
		setMaxDamage(0);
		setHasSubtypes(true);
		setMaxStackSize(1);
	}

	public BaubleType getBaubleType(ItemStack itemstack) {
		return btALST.length > itemstack.getItemDamage() ? btALST[itemstack.getItemDamage()] : BaubleType.RING;
	}

	@SuppressWarnings("unchecked")
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
		if(itemstack.getItemDamage() == 0 && player instanceof EntityPlayer) {
			int ubmrucu = ApiCore.getPlayerData((EntityPlayer)player).getPlayerUBMRU();
			if(ubmrucu >= 1 && setMRU(itemstack, 10)) {
				--ubmrucu;
				ApiCore.getPlayerData((EntityPlayer)player).modifyUBMRU(ubmrucu);
			}
		}	
		if(itemstack.getItemDamage() == 17 && player instanceof EntityPlayer) {
			EntityPlayer p = (EntityPlayer)player;
			p.addExhaustion(0.01F);
			if(p.ticksExisted % 10 == 0)
				ApiCore.getPlayerData((EntityPlayer)player).modifyUBMRU(ApiCore.getPlayerData((EntityPlayer)player).getPlayerUBMRU()+1);
		}
		if(itemstack.getItemDamage() == 18 && player instanceof EntityPlayer) {
			EntityPlayer p = (EntityPlayer)player;
			if(p.getEntityWorld().provider != null && p.getEntityWorld().provider.getDimension()!=Config.dimensionID)
				RadiationManager.increasePlayerRadiation(p, -3);
		}
		if(itemstack.getItemDamage() == 19 && player instanceof EntityPlayer) {
			EntityPlayer p = (EntityPlayer)player;
			if(p.getEntityWorld().provider != null && p.getEntityWorld().provider.getDimension()==Config.dimensionID && !p.capabilities.isCreativeMode)
				RadiationManager.increasePlayerRadiation(p, 1);
		}
		if(itemstack.getItemDamage() == 7 && player instanceof EntityPlayer) {
			EntityPlayer p = (EntityPlayer)player;
			PotionEffect[] peArray = new PotionEffect[p.getActivePotionEffects().size()];
			peArray = (PotionEffect[]) p.getActivePotionEffects().toArray(peArray);
			for(int i = 0; i < peArray.length; ++i) {
				PotionEffect effect = p.getActivePotionEffect(peArray[i].getPotion());
				if(isPotionBad(peArray[i].getPotion())) {
					try {
						Class<PotionEffect> cz = PotionEffect.class;
						Field duration = cz.getDeclaredFields()[1];
						duration.setAccessible(true);
						int d = duration.getInt(effect);
						duration.setInt(effect, --d);
						duration.setAccessible(false);
						continue;
					}
					catch(Exception e) {
						continue;
					}
				}
			}
		}
	}

	public static boolean isPotionBad(Potion p) {
		return p == MobEffects.WITHER || p == MobEffects.POISON || p == MobEffects.HUNGER || p == MobEffects.BLINDNESS || p == MobEffects.NAUSEA || p == MobEffects.MINING_FATIGUE || p == MobEffects.SLOWNESS || p == MobEffects.WEAKNESS;
	}

	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {}

	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {}

	public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}

	public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}

	public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List<ItemStack> p_150895_3_) {
		for(int i = 0; i < names.length-1; ++i) {
			p_150895_3_.add(new ItemStack(p_150895_1_, 1, i));
		}
	}

	public String getUnlocalizedName(ItemStack p_77667_1_) {
		return getUnlocalizedName() + "." + names[Math.min(p_77667_1_.getItemDamage(), names.length-1)];
	}

	@SuppressWarnings("unchecked")
	public float getModifiedValue(float original, ItemStack mod, Random rng, EntityPlayer p) {
		if(mod.getItemDamage() == 31)
			return original*2;
		if(mod.getItemDamage() == 29)
			return original/2;
		if(mod.getItemDamage() == 20) {
			float divide = original/100*25;
			original -= divide;

			double x = p.posX;
			double y = p.posY;
			double z = p.posZ;

			List<EntityMob> mobs = p.getEntityWorld().getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB(x-0.5D, y-0.5D, z-0.5D, x+0.5D, y+0.5D, z+0.5D).expand(6, 3, 6));
			for(int i = 0; i < mobs.size(); ++i) {
				EntityMob mob = mobs.get(i);
				mob.attackEntityFrom(DamageSource.causePlayerDamage(p), 3);
			}

			return original;
		}
		if(mod.getItemDamage() == 1)
			return original + rng.nextInt(100);
		if(mod.getItemDamage() == 2) {
			float divide10 = original/10;
			original -= divide10;
			EntityXPOrb orb = new EntityXPOrb(p.getEntityWorld(), p.posX+MathUtils.randomFloat(rng), p.posY+MathUtils.randomFloat(rng), p.posZ+MathUtils.randomFloat(rng), MathHelper.floor(divide10/4));
			orb.delayBeforeCanPickup = 100;
			if(!p.getEntityWorld().isRemote)
				p.getEntityWorld().spawnEntity(orb);

			return original;
		}
		if(mod.getItemDamage() == 4) {
			float divide30 = original/100*30;
			original -= divide30;

			p.heal(divide30/30);

			return original;
		}
		if(mod.getItemDamage() == 7)
			return 0;
		return original;
	}

	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean par4) {
		super.addInformation(stack, player, list, par4);
		if(stack.getItemDamage() == 0)
			list.add(getMRU(stack) + "/" + getMaxMRU(stack) + " MRU");

		list.addAll(buildHelpList(I18n.translateToLocal("ec3.txt.help.baubles."+stack.getItemDamage())));
	}

	public boolean setMRU(ItemStack stack, int amount) {
		if(getMRU(stack) + amount >= 0 && getMRU(stack) + amount <= getMaxMRU(stack)) {
			MiscUtils.getStackTag(stack).setInteger("mru", MiscUtils.getStackTag(stack).getInteger("mru")+amount);
			return true;
		}
		return false;
	}

	public int getMRU(ItemStack stack) {
		return stack.getItemDamage() == 0 ? MiscUtils.getStackTag(stack).getInteger("mru") : 0;
	}

	public int getMaxMRU(ItemStack stack) {
		return stack.getItemDamage() == 0 ? 5000 : 0;
	}

	public boolean resistWind(EntityPlayer p, ItemStack stk) {
		return stk.getItemDamage() == 3;
	}

	public float getModifier(ItemStack stk, EntityPlayer p) {
		if(stk.getItemDamage() == 5) {
			if(!p.getEntityWorld().isRemote && p.getEntityWorld().rand.nextFloat() <= 0.15F) {
				ItemStack windKeeper = new ItemStack(ItemsCore.windKeeper);
				if(!p.inventory.addItemStackToInventory(windKeeper))
					p.dropItem(windKeeper, true);
				p.sendMessage(new TextComponentString(TextFormatting.DARK_RED+""+TextFormatting.ITALIC+"The wind catcher catches some wind..."));
			}
			return -0.1F;
		}	
		if(stk.getItemDamage() == 6)
			return 0.3F;
		return 0;
	}

	public List<String> buildHelpList(String s) {
		List<String> ret = new ArrayList<String>();
		String addedString = "";
		while(s.indexOf("|") != -1) {
			int index = s.indexOf("|");
			String charType = s.substring(index+1, index+2);
			s = s.substring(0,index)+s.substring(index+2);
			if(charType.equals("n")) {
				int nextIndex = s.indexOf("|n");
				if(nextIndex != -1) {
					addedString = s.substring(0,index);
					ret.add(addedString);
					addedString = "";
					s = s.substring(index);
				}
				else {
					addedString = s.substring(0,index);
					ret.add(addedString);
					addedString = "";
					s = s.substring(index);

				}
			}
			else if(charType.equals("r")) 
				s = s.substring(0,index)+TextFormatting.RESET+s.substring(index); 
			else 
				s = s.substring(0,index)+findByChar(charType.charAt(0))+s.substring(index); 
		}
		addedString += s;
		ret.add(addedString);
		return ret;
	}

	public TextFormatting findByChar(char c) {
		for(int i = 0; i < TextFormatting.values().length; ++i) {
			TextFormatting ecf = TextFormatting.values()[i];
			char w = ecf.toString().charAt(1);
			if(w == c)
				return ecf;
		}
		return TextFormatting.RESET;
	}

	@Override
	public void registerModels() {
		for(int i = 0; i < names.length-1; i++)
			ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation("essentialcraft:item/baublesCore", "type=" + names[i].toLowerCase(Locale.ENGLISH)));
	}
}
