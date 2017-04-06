package ec3.utils.common;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import DummyCore.Utils.MathUtils;
import DummyCore.Utils.MiscUtils;
import baubles.api.BaublesApi;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import ec3.api.ApiCore;
import ec3.api.IWindResistance;
import ec3.common.item.ItemComputerArmor;
import ec3.common.item.ItemGenericArmor;
import ec3.common.item.ItemsCore;
import ec3.common.registry.PotionRegistry;

public class PlayerTracker{

	@SubscribeEvent
	public void onPlayerLogOn(PlayerEvent event)
	{
	}

	@SubscribeEvent
	public void onDescrAdded(ItemTooltipEvent event)
	{
		ItemStack stk = event.getItemStack();
		Item i = stk.getItem();
		if(ApiCore.reductionsTable.containsKey(i))
		{
			ArrayList<Float> fltLst = ApiCore.reductionsTable.get(i);
			event.getToolTip().add(TextFormatting.GOLD+"+"+((int)(fltLst.get(0)*100))+"% "+TextFormatting.DARK_PURPLE+"to MRUCorruption resistance");
			event.getToolTip().add(TextFormatting.GOLD+"+"+((int)(fltLst.get(1)*100))+"% "+TextFormatting.DARK_PURPLE+"to MRURadiation resistance");
			event.getToolTip().add(TextFormatting.GOLD+"-"+((int)(fltLst.get(2)*100))+"% "+TextFormatting.DARK_PURPLE+"to Corruption affection");
		}
	}

	@SubscribeEvent
	public void onPlayerJump(LivingJumpEvent event)
	{
		if(event.getEntityLiving() instanceof EntityPlayer && !event.getEntityLiving().getEntityWorld().isRemote && event.getEntityLiving().getEntityWorld().rand.nextFloat() < 0.0003F)
		{
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			boolean addBuff = true;
			if(ECUtils.getData(player).isWindbound())
			{
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
					event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST,100,12));
				player.sendMessage(new TextComponentString(TextFormatting.DARK_AQUA+""+TextFormatting.ITALIC+"The wind pushes you upwards..."));

				WindRelations.increasePlayerWindRelations(player, 1000);
			}

		}
		if(event.getEntityLiving() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			if(player.inventory.armorInventory[1] != null && player.inventory.armorInventory[1].getItem() instanceof ItemGenericArmor)
			{
				player.motionY += 0.2D;
			}
			if(player.inventory.armorInventory[1] != null && player.inventory.armorInventory[1].getItem() instanceof ItemComputerArmor)
			{
				player.motionY += 0.3D;
			}
		}

	}

	@SubscribeEvent
	public void onPlayerHurt(LivingHurtEvent event)
	{
		if(event.getEntityLiving() instanceof EntityPlayer && !event.getEntityLiving().getEntityWorld().isRemote)
		{
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			ItemStack chestplate = player.inventory.armorInventory[2];
			if(chestplate != null && chestplate.getItem() == ItemsCore.magicArmorItems[5] && event.getEntityLiving().getEntityWorld().rand.nextFloat() <= 0.2F)
			{
				event.setAmount(0F);
				for(int i = 0; i < 100; ++i)
				{
					MiscUtils.spawnParticlesOnServer("redstone", (float)(player.posX+MathUtils.randomDouble(event.getEntityLiving().getEntityWorld().rand)/2), (float)(player.posY+1+MathUtils.randomDouble(event.getEntityLiving().getEntityWorld().rand)), (float)(player.posZ+MathUtils.randomDouble(event.getEntityLiving().getEntityWorld().rand)/2), -1, 0, 0);
				}
				return;
			}
			if(chestplate != null && chestplate.getItem() == ItemsCore.computer_chestplate)
			{
				if(event.getSource() != null && (event.getSource() == DamageSource.wither || event.getSource() == DamageSource.magic || event.getSource() == DamageSource.starve))
					event.setCanceled(true);
				return;
			}
			ItemStack boots = player.inventory.armorInventory[0];
			if(boots != null && boots.getItem() == ItemsCore.magicArmorItems[7])
			{
				if(event.getSource() == DamageSource.fall)
				{
					event.setAmount(event.getAmount() - event.getAmount()*0.9F);
				}
			}
			if(boots != null && boots.getItem() == ItemsCore.computer_boots)
			{
				if(event.getSource() == DamageSource.fall)
				{
					event.setAmount(0);
					return;
				}
			}
			if(player.getActivePotionEffect(PotionRegistry.chaosInfluence) != null)
			{
				int amplifier = player.getActivePotionEffect(PotionRegistry.chaosInfluence).getAmplifier();
				event.setAmount(event.getAmount() * (1+amplifier));
			}
			if(player.getActivePotionEffect(PotionRegistry.frozenMind) != null)
			{
				ECUtils.calculateAndAddPE(player, MobEffects.SLOWNESS, 400,100);
				ECUtils.calculateAndAddPE(player, MobEffects.WEAKNESS, 400,100);
				ECUtils.calculateAndAddPE(player, MobEffects.MINING_FATIGUE, 400,100);
			}
		}
	}
}
