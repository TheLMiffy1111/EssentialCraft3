package ec3.utils.common;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import ec3.api.IWindModifier;
import ec3.api.IWindResistance;
import ec3.common.item.BaublesModifier;
import ec3.common.item.ItemsCore;
import ec3.common.registry.PotionRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.FakePlayer;

public class WindRelations {

	public static int getPlayerWindRelations(EntityPlayer player)
	{
		if(player instanceof FakePlayer)
			return 0;
		return ECUtils.getData(player).getPlayerWindPoints();
	}

	public static float getPlayerWindRevModifier(EntityPlayer player)
	{
		float retFlt = 0.1F;
		for(int i = 0; i < player.inventory.armorInventory.length; ++i)
		{
			ItemStack armor = player.inventory.armorInventory[i];
			if(armor != null)
			{
				if(armor.getItem() == ItemsCore.magicArmorItems[12] || armor.getItem() == ItemsCore.magicArmorItems[13] || armor.getItem() == ItemsCore.magicArmorItems[14] || armor.getItem() == ItemsCore.magicArmorItems[15])
				{
					retFlt += 0.23F;
				}
				if(armor.getItem() instanceof IWindModifier)
				{
					retFlt += ((IWindModifier)armor.getItem()).getModifier(armor, player);
				}
			}
		}
		if(BaublesApi.getBaublesHandler(player) != null)
			for(int i = 0; i < BaublesApi.getBaublesHandler(player).getSlots(); ++i)
			{
				ItemStack armor = BaublesApi.getBaublesHandler(player).getStackInSlot(i);
				if(armor != null)
				{
					if(armor.getItem() instanceof IWindModifier)
					{
						retFlt += ((IWindModifier)armor.getItem()).getModifier(armor, player);
					}
				}
			}
		return retFlt;
	}

	public static void setPlayerWindRelations(EntityPlayer player, int amount)
	{
		if(!(player instanceof FakePlayer))
			ECUtils.getData(player).modifyWindpoints(amount);
	}

	public static void increasePlayerWindRelations(EntityPlayer e, int amount)
	{
		int current = getPlayerWindRelations(e);
		amount *= getPlayerWindRevModifier(e);
		setPlayerWindRelations(e, current+amount);
		e.addPotionEffect(new PotionEffect(PotionRegistry.windTouch,1000,0,true,true));
	}

	public static void playerTick(EntityPlayer player)
	{
		if(player instanceof FakePlayer) return;
		if(!ECUtils.getData(player).isWindbound()) return;

		int mod = 1;
		if(player.getActivePotionEffect(PotionRegistry.paranormalLightness) != null) 
			mod = player.getActivePotionEffect(PotionRegistry.paranormalLightness).getAmplifier()+1;

		if(!player.getEntityWorld().isRemote && player.getEntityWorld().rand.nextDouble() < (0.0002F)*mod)
		{
			increasePlayerWindRelations(player,1000);
			boolean elemental = false;
			IBaublesItemHandler b = BaublesApi.getBaublesHandler(player);
			if(b != null)
			{
				for(int i = 0; i < b.getSlots(); ++i)
				{
					ItemStack is = b.getStackInSlot(i);
					if(is != null && is.getItem() != null && is.getItem() instanceof BaublesModifier && is.getItemDamage() == 9)
						elemental = true;
				}
			}
			if(!elemental)
				player.sendMessage(new TextComponentString(TextFormatting.DARK_AQUA+""+TextFormatting.ITALIC+"The wind timidly touches your hair..."));
			else
			{
				int rndID = player.getEntityWorld().rand.nextInt(4);
				//Fire
				if(rndID == 0)
				{
					player.sendMessage(new TextComponentString(TextFormatting.DARK_AQUA+""+TextFormatting.ITALIC+"You feel a warm touch of the wind..."));
					player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH,6*60*20,1,true,true));
				}
				//Water
				if(rndID == 1)
				{
					player.sendMessage(new TextComponentString(TextFormatting.DARK_AQUA+""+TextFormatting.ITALIC+"You feel a chilling touch of the wind..."));
					player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION,2*60*20,0,true,true));
				}
				//Earth
				if(rndID == 2)
				{
					player.sendMessage(new TextComponentString(TextFormatting.DARK_AQUA+""+TextFormatting.ITALIC+"You feel a strong push of the wind..."));
					player.addPotionEffect(new PotionEffect(MobEffects.SATURATION,2*60*20,0,true,true));
				}
				//Air
				if(rndID == 2)
				{
					player.sendMessage(new TextComponentString(TextFormatting.DARK_AQUA+""+TextFormatting.ITALIC+"You feel a strong blow of the wind..."));
					player.addPotionEffect(new PotionEffect(MobEffects.HASTE,6*60*20,1,true,true));
				}
			}
		}
		if(!player.getEntityWorld().isRemote && player.getEntityWorld().rand.nextDouble() < 0.001F*mod)
		{
			if(player.isSprinting())
			{
				player.sendMessage(new TextComponentString(TextFormatting.DARK_AQUA+""+TextFormatting.ITALIC+"The wind pushes you in the back..."));
				increasePlayerWindRelations(player,1000);
				boolean addBuff = true;

				if(BaublesApi.getBaublesHandler(player) != null)
				{
					for(int i = 0; i < BaublesApi.getBaublesHandler(player).getSlots(); ++i)
					{
						if(BaublesApi.getBaublesHandler(player).getStackInSlot(i) != null)
						{
							if(BaublesApi.getBaublesHandler(player).getStackInSlot(i).getItem() != null)
							{
								if(BaublesApi.getBaublesHandler(player).getStackInSlot(i).getItem() instanceof IWindResistance)
								{
									if(addBuff)
										addBuff = !((IWindResistance)BaublesApi.getBaublesHandler(player).getStackInSlot(i).getItem()).resistWind(player, BaublesApi.getBaublesHandler(player).getStackInSlot(i));
								}
							}
						}
					}
				}

				if(player.inventory != null)
				{
					for(int i = 0; i < player.inventory.armorInventory.length; ++i)
					{
						if(player.inventory.armorInventory[i] != null)
						{
							if(player.inventory.armorInventory[i].getItem() != null)
							{
								if(player.inventory.armorInventory[i].getItem() instanceof IWindResistance)
								{
									if(addBuff)
										addBuff = !((IWindResistance)player.inventory.armorInventory[i].getItem()).resistWind(player, BaublesApi.getBaublesHandler(player).getStackInSlot(i));
								}
							}
						}
					}
				}

				if(addBuff)
					player.addPotionEffect(new PotionEffect(MobEffects.SPEED,20,31));
			}
		}
	}
}
