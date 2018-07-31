package essentialcraft.utils.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.logging.log4j.Level;

import DummyCore.Events.DummyEvent_OnPacketRecieved;
import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.MathUtils;
import DummyCore.Utils.MiscUtils;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import essentialcraft.api.ApiCore;
import essentialcraft.api.GunRegistry;
import essentialcraft.api.GunRegistry.GunMaterial;
import essentialcraft.api.GunRegistry.GunType;
import essentialcraft.api.GunRegistry.ScopeMaterial;
import essentialcraft.api.IUBMRUGainModifyHandler;
import essentialcraft.api.IWorldEvent;
import essentialcraft.api.WorldEventRegistry;
import essentialcraft.client.gui.GuiResearchBook;
import essentialcraft.common.capabilities.mru.CapabilityMRUHandler;
import essentialcraft.common.item.ItemBaublesSpecial;
import essentialcraft.common.item.ItemGun;
import essentialcraft.common.item.ItemShadeSword;
import essentialcraft.common.item.ItemWindAxe;
import essentialcraft.common.item.ItemWindHoe;
import essentialcraft.common.item.ItemWindPickaxe;
import essentialcraft.common.item.ItemWindShovel;
import essentialcraft.common.item.ItemWindSword;
import essentialcraft.common.item.ItemsCore;
import essentialcraft.common.mod.EssentialCraftCore;
import essentialcraft.common.registry.ResearchRegistry;
import essentialcraft.common.tile.TilePlayerPentacle;
import essentialcraft.common.tile.TileWeaponMaker;
import essentialcraft.common.world.biome.BiomeChaosCorruption;
import essentialcraft.common.world.biome.BiomeFrozenCorruption;
import essentialcraft.common.world.biome.BiomeMagicCorruption;
import essentialcraft.proxy.ClientProxy;
import essentialcraft.utils.cfg.Config;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ECEventHandler {

	public String lastTickLanguage;
	public static boolean shouldLoadLoot;

	@SubscribeEvent
	public void lootTableEvent(LootTableLoadEvent event) {
		if(shouldLoadLoot && event.getName().equals(LootTableList.CHESTS_SIMPLE_DUNGEON)) {
			LootPool main = event.getTable().getPool("main");
			for(int i = 0; i < ItemBaublesSpecial.names.length-1; ++i) {
				main.addEntry(new LootEntryItem(ItemsCore.baublesCore, 3, 0, new LootFunction[] {new SetMetadata(new LootCondition[0], new RandomValueRange(i))}, new LootCondition[0], "essentialcraft:baublesCore"+i));
			}
			shouldLoadLoot = false;
		}
	}

	@SubscribeEvent
	public void serverTickEvent(ServerTickEvent event)
	{
		ECUtils.actionsTick();
	}

	@SubscribeEvent
	public void anvilEvent(AnvilUpdateEvent event) {
		if(event.getLeft().getItem() instanceof ItemGun && !event.getRight().isEmpty()) {
			NBTTagCompound tag = MiscUtils.getStackTag(event.getLeft());
			if(tag.hasKey("base")) {
				GunMaterial material = null;
				for(int i = 0; i < GunRegistry.GUN_MATERIALS.size(); ++i) {
					GunMaterial gm = GunRegistry.GUN_MATERIALS.get(i);
					if(gm.recipe.isItemEqual(event.getRight())) {
						material = gm;
						break;
					}
				}
				if(material != null) {
					ItemStack result = event.getLeft().copy();
					MiscUtils.getStackTag(result).setFloat("gunDamage", 0);
					event.setCost(4);
					event.setMaterialCost(1);
					event.setOutput(result);
				}
			}
		}
	}

	public ItemStack findShearItem(Entity e) {
		int rN = 1+e.getEntityWorld().rand.nextInt(3);
		if(e instanceof EntityCow)
			return new ItemStack(Items.LEATHER,rN,0);
		if(e instanceof EntityChicken)
			return new ItemStack(Items.FEATHER,rN,0);
		if(e instanceof EntitySquid)
			return new ItemStack(Items.DYE,rN,0);
		if(e instanceof EntitySpider)
			return new ItemStack(Items.STRING,rN,0);
		if(e instanceof EntityCreeper)
			return new ItemStack(Items.GUNPOWDER,rN,0);
		if(e instanceof EntitySkeleton)
			return new ItemStack(Items.DYE,rN,15);
		if(e instanceof EntityMagmaCube)
			return new ItemStack(Items.MAGMA_CREAM,rN,0);
		if(e instanceof EntitySlime)
			return new ItemStack(Items.SLIME_BALL,rN,0);
		if(e instanceof EntityZombie)
			return new ItemStack(Items.ROTTEN_FLESH,rN,0);
		if(e instanceof EntitySnowman)
			return new ItemStack(Blocks.SNOW,rN,0);
		return ItemStack.EMPTY;
	}

	@SubscribeEvent
	public void attackTargetEvent(LivingSetAttackTargetEvent event) {
		if(event.getTarget() != null) {
			if(event.getTarget() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)event.getTarget();
				boolean changeTarget = false;
				IBaublesItemHandler b = BaublesApi.getBaublesHandler(player);
				if(b != null) {
					for(int i = 0; i < b.getSlots(); ++i) {
						ItemStack is = b.getStackInSlot(i);
						if(is.getItem() instanceof ItemBaublesSpecial && is.getItemDamage() == 30) {
							changeTarget = true;
							break;
						}
					}
				}
				if(changeTarget) {
					List<EntityLivingBase> entities = event.getEntityLiving().getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(event.getEntityLiving().posX-0.5D, event.getEntityLiving().posY-0.5D, event.getEntityLiving().posZ-0.5D, event.getEntityLiving().posX+0.5D, event.getEntityLiving().posY+0.5D, event.getEntityLiving().posZ+0.5D).grow(6, 3, 6));
					for(int i = 0; i < entities.size(); ++i) {
						EntityLivingBase base = entities.get(i);
						if(base == event.getEntityLiving() || base == event.getTarget()) {
							entities.remove(i);
							--i;
						}
					}
					if(!entities.isEmpty()) {
						EntityLivingBase ba = entities.get(event.getEntityLiving().getEntityWorld().rand.nextInt(entities.size()));
						if(ba != event.getEntityLiving() && !(ba instanceof EntityPlayer)) {
							event.getEntityLiving().setRevengeTarget(ba);
							event.getEntityLiving().setLastAttackedEntity(ba);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void xpEvent(PlayerPickupXpEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		boolean edouble = false;
		IBaublesItemHandler b = BaublesApi.getBaublesHandler(player);
		if(b != null) {
			for(int i = 0; i < b.getSlots(); ++i) {
				ItemStack is = b.getStackInSlot(i);
				if(is.getItem() instanceof ItemBaublesSpecial && is.getItemDamage() == 29) {
					edouble = true;
				}
			}
		}
		if(edouble) {
			event.getOrb().xpValue*=2;
		}
	}

	@SubscribeEvent
	public void shearEvent(EntityInteract event) {
		EntityPlayer player = event.getEntityPlayer();
		if(player != null) {
			boolean shear = false;
			IBaublesItemHandler b = BaublesApi.getBaublesHandler(player);
			if(b != null) {
				for(int i = 0; i < b.getSlots(); ++i) {
					ItemStack is = b.getStackInSlot(i);
					if(is.getItem() instanceof ItemBaublesSpecial && is.getItemDamage() == 28) {
						shear = true;
					}
				}
			}
			ItemStack stk = event.getItemStack();
			int cost = 1000;
			shear = shear && stk.getItem() instanceof ItemShears && ApiCore.getPlayerData(player).getPlayerUBMRU() >= cost && !player.isSwingInProgress;
			if(shear) {
				Entity base = event.getTarget();
				stk.damageItem(32, player);
				ItemStack is = this.findShearItem(base);
				if(!is.isEmpty()) {
					EntityItem ent = new EntityItem(base.getEntityWorld(),base.posX,base.posY,base.posZ,is);
					Random rand = base.getEntityWorld().rand;
					ent.motionY += rand.nextFloat() * 0.05F;
					ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
					ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
					player.getEntityWorld().playSound(player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.PLAYERS, 1, 1, false);
					if(!player.getEntityWorld().isRemote)
						player.getEntityWorld().spawnEntity(ent);
					player.swingArm(EnumHand.MAIN_HAND);
					ApiCore.getPlayerData(player).modifyUBMRU(ApiCore.getPlayerData(player).getPlayerUBMRU()-cost);
				}
			}
		}
	}

	@SubscribeEvent
	public void enderTeleport(EnderTeleportEvent event) {
		if(event.getEntityLiving() != null) {
			if(event.getEntityLiving() instanceof EntityEnderman) {
				EntityEnderman ender = (EntityEnderman) event.getEntityLiving();
				EntityPlayer player = ender.getEntityWorld().getClosestPlayerToEntity(ender, 16D);
				if(player != null) {
					boolean stopTeleportation = false;
					IBaublesItemHandler b = BaublesApi.getBaublesHandler(player);
					if(b != null) {
						for(int i = 0; i < b.getSlots(); ++i) {
							ItemStack is = b.getStackInSlot(i);
							if(is.getItem() instanceof ItemBaublesSpecial && is.getItemDamage() == 27)
								stopTeleportation = true;
						}
					}
					if(stopTeleportation) {
						event.setCanceled(true);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void livingDeath(LivingDropsEvent event) {
		if(event.getSource() != null) {
			if(event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
				boolean increaseDrops = false;
				IBaublesItemHandler b = BaublesApi.getBaublesHandler(player);
				if(b != null) {
					for(int i = 0; i < b.getSlots(); ++i) {
						ItemStack is = b.getStackInSlot(i);
						if(is.getItem() instanceof ItemBaublesSpecial && is.getItemDamage() == 26)
							increaseDrops = true;
					}
				}
				if(increaseDrops && ApiCore.getPlayerData(player).getPlayerUBMRU() >= 1000) {
					ApiCore.getPlayerData(player).modifyUBMRU(ApiCore.getPlayerData(player).getPlayerUBMRU()-1000);
					for(int i = 0; i < event.getDrops().size(); ++i) {
						EntityItem ei = event.getDrops().get(i);
						if(ei != null) {
							ItemStack is = ei.getItem();
							if(!is.isEmpty()) {
								is.grow(player.getEntityWorld().rand.nextInt(3));
							}
						}
					}
				}
			}
		}
	}


	@SubscribeEvent
	public void breakBlock(BlockEvent.BreakEvent event) {
		boolean gainXP = false;
		boolean xpToU = false;
		if(event.getPlayer() != null) {
			IBaublesItemHandler b = BaublesApi.getBaublesHandler(event.getPlayer());
			if(b != null) {
				for(int i = 0; i < b.getSlots(); ++i) {
					ItemStack is = b.getStackInSlot(i);
					if(is.getItem() instanceof ItemBaublesSpecial && is.getItemDamage() == 23)
						gainXP = true;
					if(is.getItem() instanceof ItemBaublesSpecial && is.getItemDamage() == 24)
						xpToU = true;
				}
			}
			if(gainXP) {
				event.setExpToDrop(event.getExpToDrop()*3);
				if(event.getExpToDrop() <= 0 && event.getWorld().rand.nextFloat() < 0.05F) {
					event.setExpToDrop(1);
				}
			}
			if(xpToU) {
				int xp = event.getExpToDrop();
				ApiCore.getPlayerData(event.getPlayer()).modifyUBMRU(ApiCore.getPlayerData(event.getPlayer()).getPlayerUBMRU() + xp*1000);
				event.setExpToDrop(0);
			}
		}
	}


	@SubscribeEvent
	public void harvestDrops(HarvestDropsEvent event) {
		if(event.getHarvester() != null) {
			boolean increaseFortune = false;

			IBaublesItemHandler b = BaublesApi.getBaublesHandler(event.getHarvester());
			if(b != null) {
				for(int i = 0; i < b.getSlots(); ++i) {
					ItemStack is = b.getStackInSlot(i);
					if(is.getItem() instanceof ItemBaublesSpecial && is.getItemDamage() == 22)
						increaseFortune = true;
				}
			}
			if(increaseFortune && ApiCore.getPlayerData(event.getHarvester()).getPlayerUBMRU() >= 500) {
				ECUtils.getData(event.getHarvester()).modifyUBMRU(ECUtils.getData(event.getHarvester()).getPlayerUBMRU() - 500);
				for(int i = 0; i < event.getDrops().size(); ++i) {
					ItemStack is = event.getDrops().get(i);
					if(!is.isEmpty() && is.getItem() != null && !(is.getItem() instanceof ItemBlock)) {
						is.grow(1);
					}
				}
			}
		}
	}


	@SubscribeEvent
	public void damageEvent(LivingHurtEvent event)
	{
		if(event.getSource() != null)
		{
			if(event.getSource().getTrueSource() != null)
			{
				if(event.getSource().getTrueSource() instanceof EntityPlayer)
				{
					EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
					boolean reset = false;
					boolean dd = false;
					boolean damageScrewup = false;
					boolean radiation = false;

					IBaublesItemHandler b = BaublesApi.getBaublesHandler(player);
					if(b != null)
					{
						for(int i = 0; i < b.getSlots(); ++i)
						{
							ItemStack is = b.getStackInSlot(i);
							if(is.getItem() instanceof ItemBaublesSpecial && is.getItemDamage() == 13)
								reset = true;
							if(is.getItem() instanceof ItemBaublesSpecial && is.getItemDamage() == 14)
								dd = true;
							if(is.getItem() instanceof ItemBaublesSpecial && is.getItemDamage() == 16)
								damageScrewup = true;
							if(is.getItem() instanceof ItemBaublesSpecial && is.getItemDamage() == 21)
								radiation = true;
						}
					}
					if(dd)
					{
						event.setAmount(event.getAmount()*2);
					}
					if(reset)
					{
						if(ApiCore.getPlayerData(player).getPlayerUBMRU() >= 300)
						{
							ApiCore.getPlayerData(player).modifyUBMRU(ApiCore.getPlayerData(player).getPlayerUBMRU()-300);
							player.resetCooldown();
							event.getEntity().hurtResistantTime = 0;
							event.getEntityLiving().hurtTime = 0;
						}
					}
					if(damageScrewup)
					{
						if(event.getEntityLiving().hurtResistantTime <= 0 || event.getEntityLiving().hurtResistantTime <= 0)
						{
							float percentage = event.getAmount()/10;
							event.setAmount(event.getAmount() - percentage);
							event.getEntityLiving().attackEntityFrom(DamageSource.MAGIC, percentage);
							event.getEntity().hurtResistantTime = 0;
							event.getEntityLiving().hurtTime = 0;
						}
					}
					if(radiation)
					{
						if(event.getEntityLiving() instanceof EntityPlayer)
						{
							RadiationManager.increasePlayerRadiation((EntityPlayer) event.getEntityLiving(), (int) (event.getAmount()*1000*5));
						}else
						{
							event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier(SharedMonsterAttributes.MAX_HEALTH.getName(), -event.getAmount(), 0));
						}
					}
				}
			}

		}
		if(event.getEntityLiving() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			boolean dd = false;
			boolean saveFromDeath = false;
			boolean radiation = false;
			int timesToReflect = 0;
			IBaublesItemHandler b = BaublesApi.getBaublesHandler(player);
			if(b != null)
			{
				for(int i = 0; i < b.getSlots(); ++i)
				{
					ItemStack is = b.getStackInSlot(i);
					if(is.getItem() instanceof ItemBaublesSpecial && is.getItemDamage() == 14)
						dd = true;
					if(is.getItem() instanceof ItemBaublesSpecial && is.getItemDamage() == 15)
						saveFromDeath = true;
					if(is.getItem() instanceof ItemBaublesSpecial && is.getItemDamage() == 21)
						radiation = true;
					if(is.getItem() instanceof ItemBaublesSpecial && is.getItemDamage() == 25)
						++timesToReflect;
				}
			}
			if(dd)
			{
				event.setAmount(event.getAmount() * 2);
			}
			for(int i = 0; i < timesToReflect; ++i)
			{
				if(!player.getEntityWorld().isRemote && event.getSource()!=null&&event.getSource().getTrueSource()!=null&&player.getEntityWorld().rand.nextFloat()<0.1F)
				{
					event.getSource().getTrueSource().attackEntityFrom(DamageSource.causePlayerDamage(player), event.getAmount());
					event.setAmount(0F);
					break;
				}
			}
			if(saveFromDeath)
			{
				if(player.getHealth() - event.getAmount() <= 0)
				{
					if(ApiCore.getPlayerData(player).getPlayerUBMRU() >= event.getAmount()*5000)
					{
						ApiCore.getPlayerData(player).modifyUBMRU((int) (ApiCore.getPlayerData(player).getPlayerUBMRU() - event.getAmount()*5000));
						event.setAmount(0);
					}
				}
			}
			if(radiation)
			{
				RadiationManager.increasePlayerRadiation(player, (int) (event.getAmount()*1000));
			}
		}else
		{

		}
		if(event.getSource() != null && event.getSource().getTrueSource() instanceof EntityLivingBase)
		{
			EntityLivingBase elb = (EntityLivingBase)event.getSource().getTrueSource();
			if(!elb.getHeldItemMainhand().isEmpty() && elb.getHeldItemMainhand().getItem() instanceof ItemShadeSword)
			{
				ItemShadeSword iss = (ItemShadeSword)elb.getHeldItemMainhand().getItem();
				iss.hitEntity(event.getEntityLiving().getHeldItemMainhand(), event.getEntityLiving(), elb);
			}
			else if(!elb.getHeldItemOffhand().isEmpty() && elb.getHeldItemOffhand().getItem() instanceof ItemShadeSword)
			{
				ItemShadeSword iss = (ItemShadeSword)elb.getHeldItemOffhand().getItem();
				iss.hitEntity(event.getEntityLiving().getHeldItemOffhand(), event.getEntityLiving(), elb);
			}
		}
	}

	@SubscribeEvent
	public void configSync(ConfigChangedEvent.OnConfigChangedEvent event) {
		if(event.getModID().equals("essentialcraft")) {
			Config.config.save();
			Config.instance.load(Config.config);
		}
	}

	@SubscribeEvent
	public void guiButtonPressed(DummyCore.Events.DummyEvent_OnClientGUIButtonPress event) {
		if(event.client_ParentClassPath.equalsIgnoreCase("essentialcraft.client.gui.GuiFilter")) {
			EntityPlayer p = event.presser;
			ItemStack is = p.getHeldItemMainhand();
			NBTTagCompound itemTag = MiscUtils.getStackTag(is);
			if(event.buttonID == 0) {
				if(itemTag.getBoolean("ignoreMeta"))
					itemTag.setBoolean("ignoreMeta", false);
				else
					itemTag.setBoolean("ignoreMeta", true);
			}
			if(event.buttonID == 1) {
				if(itemTag.getBoolean("ignoreNBT"))
					itemTag.setBoolean("ignoreNBT", false);
				else
					itemTag.setBoolean("ignoreNBT", true);
			}
			if(event.buttonID == 2) {
				if(itemTag.getBoolean("ignoreOreDict"))
					itemTag.setBoolean("ignoreOreDict", false);
				else
					itemTag.setBoolean("ignoreOreDict", true);
			}
			is.setTagCompound(itemTag);
		}
		if(event.client_ParentClassPath.equalsIgnoreCase("essentialcraft.client.gui.GuiCraftingFrame")) {
			EntityPlayer p = event.presser;
			ItemStack is = p.getHeldItemMainhand();
			NBTTagCompound itemTag = MiscUtils.getStackTag(is);
			if(event.buttonID == 0) {
				if(itemTag.getBoolean("ignoreOreDict"))
					itemTag.setBoolean("ignoreOreDict", false);
				else
					itemTag.setBoolean("ignoreOreDict", true);
			}
			is.setTagCompound(itemTag);
		}
		if(event.client_ParentClassPath.equalsIgnoreCase("essentialcraft.client.gui.GuiWeaponBench") && event.buttonID == 0) {
			TileWeaponMaker maker = (TileWeaponMaker)event.presser.getEntityWorld().getTileEntity(new BlockPos(event.x, event.y, event.z));
			maker.makeWeapon();
		}
		if(event.client_ParentClassPath.equalsIgnoreCase("essentialcraft.client.gui.GuiPlayerPentacle")) {
			TilePlayerPentacle pentacle = (TilePlayerPentacle) event.presser.getEntityWorld().getTileEntity(new BlockPos(event.x, event.y, event.z));

			int reqToConsume = ECUtils.getData(event.presser).getEffects().get(event.buttonID).getStickiness();
			if(event.additionalData[0].fieldName.equalsIgnoreCase("isCreative") && event.additionalData[0].fieldValue.equalsIgnoreCase("true") || pentacle.consumeEnderstarEnergy(reqToConsume)) {
				ECUtils.getData(event.presser).getEffects().remove(event.buttonID);
				ECUtils.requestSync(event.presser);
			}
		}
	}

	@SubscribeEvent
	public void onBlockSpeedCheck(PlayerEvent.BreakSpeed event)
	{
		//I usually do not comment my code.
		//However I think that this is an exception
		//I do not know why it took me so long to figure all this out
		//But this code took me a good 4-6 hours

		//Getting the current player
		EntityPlayer p = event.getEntityPlayer();

		//Getting the player's tool in hand
		ItemStack currentTool = p.getHeldItemMainhand();

		//A usual check - we are on a server, the tool exists and the tool is a Wind type of tool.
		if(!event.getEntityPlayer().getEntityWorld().isRemote && (currentTool.getItem() instanceof ItemWindAxe || currentTool.getItem() instanceof ItemWindPickaxe || currentTool.getItem() instanceof ItemWindShovel || currentTool.getItem() instanceof ItemWindHoe || currentTool.getItem() instanceof ItemWindSword))
		{
			//Setting the current tool type via
			//Hard-coded tool check. Hmmm....
			String currentToolClass = "";
			if(currentTool.getItem() instanceof ItemPickaxe)
				currentToolClass = "pickaxe";
			if(currentTool.getItem() instanceof ItemAxe)
				currentToolClass = "axe";
			if(currentTool.getItem() instanceof ItemSpade)
				currentToolClass = "shovel";
			if(currentTool.getItem() instanceof ItemHoe)
				currentToolClass = "hoe";
			if(currentTool.getItem() instanceof ItemSword)
				currentToolClass = "sword";

			//If the player is using an improper tool, or the current stack is a sword
			if (!ForgeHooks.isToolEffective(event.getEntityPlayer().getEntityWorld(),event.getPos(),currentTool) || currentToolClass.equalsIgnoreCase("sword"))
			{
				//Getting the proper tool type
				String clazz = event.getState().getBlock().getHarvestTool(event.getState());
				if(clazz == null || clazz.isEmpty())
				{
					//If the block and it's materials exist. Who knows, that mod calls this event...
					if(event.getState()!=null && event.getState()!=null)
					{
						//Bunch of material checks to make sure lazy modders like me will still get their blocks recognized properly
						if(event.getState().getMaterial()==Material.ANVIL)
							clazz = "pickaxe";
						if(event.getState().getMaterial()==Material.CACTUS)
							clazz = "axe";
						if(event.getState().getMaterial()==Material.CLAY)
							clazz = "shovel";
						if(event.getState().getMaterial()==Material.CORAL)
							clazz = "pickaxe";
						if(event.getState().getMaterial()==Material.CRAFTED_SNOW)
							clazz = "shovel";
						if(event.getState().getMaterial()==Material.GLASS)
							clazz = "pickaxe";
						if(event.getState().getMaterial()==Material.GOURD)
							clazz = "axe";
						if(event.getState().getMaterial()==Material.GRASS)
							clazz = "shovel";
						if(event.getState().getMaterial()==Material.GROUND)
							clazz = "shovel";
						if(event.getState().getMaterial()==Material.ICE)
							clazz = "pickaxe";
						if(event.getState().getMaterial()==Material.IRON)
							clazz = "pickaxe";
						if(event.getState().getMaterial()==Material.PACKED_ICE)
							clazz = "pickaxe";
						if(event.getState().getMaterial()==Material.PISTON)
							clazz = "axe";
						if(event.getState().getMaterial()==Material.ROCK)
							clazz = "pickaxe";
						if(event.getState().getMaterial()==Material.SAND)
							clazz = "shovel";
						if(event.getState().getMaterial()==Material.SNOW)
							clazz = "shovel";
						if(event.getState().getMaterial()==Material.SPONGE)
							clazz = "shovel";
						if(event.getState().getMaterial()==Material.TNT)
							clazz = "sword";
						if(event.getState().getMaterial()==Material.WEB)
							clazz = "sword";
						if(event.getState().getMaterial()==Material.WOOD)
							clazz = "axe";
					}
				}
				//If the proper tool type exists
				//Also a dumb sword check
				if(clazz != null && !clazz.isEmpty() && clazz != currentToolClass)
				{
					//Initializing 2 blank NBTs

					//This one will store all the info about the current tool - ID, Meta, Amount and things like enchantments.
					//Should NEVER store other stored items tags!!!
					NBTTagCompound toolTag = new NBTTagCompound();

					//And this one will ONLY store tags of other tools
					NBTTagCompound genericTag = new NBTTagCompound();

					//Filling in the first tag. At this point it is a huge mess of everything, so we need to clean it up.
					currentTool.writeToNBT(toolTag);

					//If our tag actually stores anything apart from ID, Amount and Meta
					if(toolTag.hasKey("tag"))
					{
						//copy the generic tag
						genericTag = toolTag.getCompoundTag("tag").copy();

						//Nullify all other tools data if any
						toolTag.getCompoundTag("tag").removeTag("pickaxe");
						toolTag.getCompoundTag("tag").removeTag("axe");
						toolTag.getCompoundTag("tag").removeTag("shovel");
						toolTag.getCompoundTag("tag").removeTag("hoe");
						toolTag.getCompoundTag("tag").removeTag("sword");

						//Safety check, should not happen, but shit happens.
						toolTag.getCompoundTag("tag").removeTag("tag");
					}

					//Now, since our genericTag is full of every data types, like Enchantments, and we do not want that to happen we need to clean it up.
					//I'm doing it by getting every tag there is, writing it down into an ArrayList, that way we are escaping the nasty ConurrentModification
					//Removing all the necessary data keys from the list, and then removing every element, that matches any of the keys left in the list.
					//That way I make sure, that there is nothing, apart from the tool NBT in the genericTag

					//All KEYS of the tags in the genericData
					Set<String> tags = genericTag.getKeySet();

					//Blank list
					List<String> tagKeyLst = new ArrayList<String>();

					//Getting the Iterator, since this is a Set, and we do not want the StackOverflow exception
					Iterator<String> $i = tags.iterator();

					//Iterating through each KEY and putting it into our blank list
					while($i.hasNext())
					{
						tagKeyLst.add($i.next());
					}

					//Removing all the tool tags from the list.
					//Before removing making sure, that that element actually exists.
					if(tagKeyLst.indexOf("pickaxe") != -1)
						tagKeyLst.remove("pickaxe");
					if(tagKeyLst.indexOf("axe") != -1)
						tagKeyLst.remove("axe");
					if(tagKeyLst.indexOf("shovel") != -1)
						tagKeyLst.remove("shovel");
					if(tagKeyLst.indexOf("hoe") != -1)
						tagKeyLst.remove("hoe");
					if(tagKeyLst.indexOf("sword") != -1)
						tagKeyLst.remove("sword");

					//Iterating through our list, which should have everything, except for our tools in it.
					//In other words we worked as a gc - we've collected everything we DO NOT need, and now we are deleting it.
					for(int i = 0; i < tagKeyLst.size(); ++i)
					{
						genericTag.removeTag(tagKeyLst.get(i));
					}

					//Initializing our blank ItemStack, that will replace the current one.
					ItemStack efficent = ItemStack.EMPTY;

					//If the tool we are switching to was found in the tag, and, therefore, was stored
					if(genericTag.hasKey(clazz))
					{
						//Preparing the generic NBT
						NBTTagCompound loadFrom = genericTag.getCompoundTag(clazz).copy();
						//And removing that tool from NBT = we do not want NBT duplicates.
						genericTag.removeTag(clazz);
						//Loading our tool from the NBT
						efficent = new ItemStack(loadFrom);
					}else //Or, if the tool was not found, we are creating a new blank one
					{
						//Another hard-coded tool initialization.
						if(clazz.equalsIgnoreCase("pickaxe"))
							efficent = new ItemStack(ItemsCore.wind_elemental_pick,1,currentTool.getItemDamage());
						if(clazz.equalsIgnoreCase("shovel"))
							efficent = new ItemStack(ItemsCore.wind_elemental_shovel,1,currentTool.getItemDamage());
						if(clazz.equalsIgnoreCase("hoe")) //Will that ever happen?
							efficent = new ItemStack(ItemsCore.wind_elemental_hoe,1,currentTool.getItemDamage());
						if(clazz.equalsIgnoreCase("sword"))
							efficent = new ItemStack(ItemsCore.wind_elemental_sword,1,currentTool.getItemDamage());
						if(clazz.equalsIgnoreCase("axe"))
							efficent = new ItemStack(ItemsCore.wind_elemental_axe,1,currentTool.getItemDamage());
					}

					//If our tool got replaced. Also, if our tool is a valid item. Should be valid all the times, but...
					if(efficent != null && efficent.getItem() != null)
					{
						//Getting the tag of our new-made item.
						//IMPORTANT! This is already a 'tag' tag. I mean, println the efficent's NBT, and you will see
						//{id:6666s,Damage:0s,Count:1b,tag:{}}
						//That tag{} == this tag!

						//Also, the most creative name ever award goes to me, I guess.
						NBTTagCompound anotherTag = MiscUtils.getStackTag(efficent);

						//Setting our tags, if needed
						//I mean, saving our tools into the NBT data! Yea, that sounds better!
						if(genericTag.hasKey("pickaxe"))
							anotherTag.setTag("pickaxe", genericTag.getTag("pickaxe"));
						if(genericTag.hasKey("axe"))
							anotherTag.setTag("axe", genericTag.getTag("axe"));
						if(genericTag.hasKey("shovel"))
							anotherTag.setTag("shovel", genericTag.getTag("shovel"));
						if(genericTag.hasKey("hoe"))
							anotherTag.setTag("hoe", genericTag.getTag("hoe"));
						if(genericTag.hasKey("sword"))
							anotherTag.setTag("sword", genericTag.getTag("sword"));

						//Also setting our current tool into the NBT.
						//I mean saving, sure.
						if(toolTag != null)
							anotherTag.setTag(currentToolClass, toolTag);

						//And giving our player the new, awesome tool!
						event.getEntityPlayer().inventory.setInventorySlotContents(event.getEntityPlayer().inventory.currentItem, efficent);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void worldLoadEvent(WorldEvent.Load event) {
		if(event.getWorld() != null && !event.getWorld().isRemote && event.getWorld().provider != null && event.getWorld().provider.getDimension() == 0) {
			File f = event.getWorld().getSaveHandler().getWorldDirectory();
			if(f != null) {
				try {
					String fPath = f.getAbsolutePath();
					File worldSaveFile = new File(fPath+"//EC3Data.dat");
					if(worldSaveFile.isDirectory()) {
						throw new IOException("File is a directory! Please, delete the EC3Data.dat(???) folder in your save and launch the game again!");
					}
					if(!worldSaveFile.exists()) {
						FMLLog.log(Level.WARN, "[EC3]*Server save file not found. This is completely normal if this is your first launch of the save. Otherwise please, report this to the author!");
						worldSaveFile.createNewFile();
					}
					FileInputStream iStream = new FileInputStream(worldSaveFile);
					try {
						ECUtils.ec3WorldTag = CompressedStreamTools.readCompressed(iStream);
					}
					catch(Exception ex) {
						//...
					}
					finally {
						iStream.close();
					}
				}
				catch(Exception e) {
					//...
				}
			}
		}
	}

	@SubscribeEvent
	public void worldSaveEvent(WorldEvent.Save event) {
		if(event.getWorld() != null && !event.getWorld().isRemote && event.getWorld().provider != null && event.getWorld().provider.getDimension() == 0) {
			File f = event.getWorld().getSaveHandler().getWorldDirectory();
			if(f != null) {
				try {
					String fPath = f.getAbsolutePath();
					File worldSaveFile = new File(fPath+"//EC3Data.dat");
					if(worldSaveFile.isDirectory()) {
						throw new IOException("File is a directory! Please, delete the EC3Data.dat(???) folder in your save and launch the game again!");
					}
					if(!worldSaveFile.exists()) {
						worldSaveFile.createNewFile();
					}
					FileOutputStream oStream = new FileOutputStream(worldSaveFile);
					try {
						CompressedStreamTools.writeCompressed(ECUtils.ec3WorldTag, oStream);
					}
					catch(Exception ex) {
						//...
					}
					finally {
						oStream.close();
					}
				}
				catch(Exception e) {
					//...
				}
			}
		}
	}

	@SubscribeEvent
	public void worldTick(WorldTickEvent event)
	{
		if(!event.world.isRemote && event.world != null && event.world.provider != null && event.world.provider.getDimension() == Config.dimensionID && event.phase == Phase.END)
		{
			if(ECUtils.hasActiveEvent())
			{
				if(WorldEventRegistry.currentEvent == null)
				{
					WorldEventRegistry.currentEvent = WorldEventRegistry.gettEffectByID(ECUtils.getActiveEvent());
					WorldEventRegistry.currentEventDuration = ECUtils.getActiveEventDuration();
				}else
				{
					ECUtils.ec3WorldTag.setInteger("currentEventDuration", ECUtils.getActiveEventDuration()-1);
					if(ECUtils.getActiveEventDuration() <= 0)
					{
						WorldEventRegistry.currentEvent.onEventEnd(event.world);
						WorldEventRegistry.currentEvent = null;
						WorldEventRegistry.currentEventDuration = -1;
						ECUtils.ec3WorldTag.removeTag("currentEventDuration");
						ECUtils.ec3WorldTag.removeTag("currentEvent");
						ECUtils.requestCurrentEventSync();
					}else
						WorldEventRegistry.currentEventDuration = ECUtils.getActiveEventDuration();
				}
			}else
			{
				if(event.world.getWorldTime() % 20 == 0)
				{
					IWorldEvent wevent = WorldEventRegistry.selectRandomEffect(event.world);
					if(wevent != null)
					{
						wevent.onEventBeginning(event.world);
						ECUtils.ec3WorldTag.setString("currentEvent", wevent.getEventID());
						ECUtils.ec3WorldTag.setInteger("currentEventDuration", wevent.getEventDuration(event.world));
						ECUtils.requestCurrentEventSync();
					}
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onAimZoom(FOVUpdateEvent event)
	{
		EntityPlayer p = event.getEntity();
		if(p.isSneaking() && p.getHeldItemMainhand().getItem() instanceof ItemGun && p.getHeldItemMainhand().getTagCompound() != null && p.getHeldItemMainhand().getTagCompound().hasKey("scope"))
		{
			String id = p.getHeldItemMainhand().getTagCompound().getString("scope");
			ScopeMaterial s = null;
			if(((ItemGun)p.getHeldItemMainhand().getItem()).gunType.equalsIgnoreCase("sniper"))
			{
				s = GunRegistry.getScopeSniperFromID(id);
			}
			else
			{
				s = GunRegistry.getScopeFromID(id);
			}
			if(s != null)
			{
				GunType g = null;
				if(((ItemGun)p.getHeldItemMainhand().getItem()).gunType.equalsIgnoreCase("sniper"))
					g = GunType.SNIPER;
				if(((ItemGun)p.getHeldItemMainhand().getItem()).gunType.equalsIgnoreCase("pistol"))
					g = GunType.PISTOL;
				if(((ItemGun)p.getHeldItemMainhand().getItem()).gunType.equalsIgnoreCase("rifle"))
					g = GunType.RIFLE;
				if(((ItemGun)p.getHeldItemMainhand().getItem()).gunType.equalsIgnoreCase("gatling"))
					g = GunType.GATLING;
				if(g != null)
				{
					ArrayList<DummyData> ls = s.materialData.get(g);
					for(int i = 0; i < ls.size(); ++i)
					{
						DummyData dt = ls.get(i);
						if(dt != null && dt.fieldName.equalsIgnoreCase("scope.zoom"))
						{
							float value = Float.parseFloat(dt.fieldValue)*1F;
							event.setNewfov(1/value);
							return;
						}
					}
				}
			}
		}
	}

	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void denyFlowerGen(DecorateBiomeEvent.Decorate event) {
		if(event.getWorld().provider.getDimension() == Config.dimensionID) {
			if(event.getType() == EventType.FLOWERS || event.getType() == EventType.TREE) {
				event.setResult(Result.DENY);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void getBiomeWaterColor(BiomeEvent.GetWaterColor event)
	{
		EntityPlayer player = EssentialCraftCore.proxy.getClientPlayer();
		if(player != null)
		{
			int dimID = player.dimension;
			if(dimID == Config.dimensionID)
			{
				event.setNewColor(0xff6a58);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void clientTick(TickEvent.ClientTickEvent event)
	{
		if(event.phase == TickEvent.Phase.START) {}
		else
		{
			if(lastTickLanguage != null && !lastTickLanguage.isEmpty())
			{
				if(!lastTickLanguage.equalsIgnoreCase(FMLClientHandler.instance().getCurrentLanguage()))
				{
					ResearchRegistry.init();
					if(GuiResearchBook.currentCategory != null && GuiResearchBook.currentDiscovery != null)
					{
						String id = GuiResearchBook.currentDiscovery.id;
						for(int i = 0; i < GuiResearchBook.currentCategory.discoveries.size(); ++i)
						{
							if(GuiResearchBook.currentCategory.discoveries.get(i).id.equals(id))
							{
								GuiResearchBook.currentDiscovery=GuiResearchBook.currentCategory.discoveries.get(i);
								break;
							}
						}
					}
				}
			}
			lastTickLanguage = FMLClientHandler.instance().getCurrentLanguage();
		}
	}



	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void getBiomeFolliageColor(BiomeEvent.GetFoliageColor event)
	{
		EntityPlayer player = EssentialCraftCore.proxy.getClientPlayer();
		if(player != null)
		{
			int dimID = player.dimension;
			if(dimID == Config.dimensionID)
			{
				if(!(event.getBiome() instanceof BiomeChaosCorruption) && !(event.getBiome() instanceof BiomeFrozenCorruption) && !(event.getBiome() instanceof BiomeMagicCorruption))
				{
					event.setNewColor(0x886a58);
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void getBiomeGrassColor(BiomeEvent.GetGrassColor event)
	{
		EntityPlayer player = EssentialCraftCore.proxy.getClientPlayer();
		if(player != null)
		{
			int dimID = player.dimension;
			if(dimID == Config.dimensionID)
			{
				if(!(event.getBiome() instanceof BiomeChaosCorruption) && !(event.getBiome() instanceof BiomeFrozenCorruption) && !(event.getBiome() instanceof BiomeMagicCorruption))
				{
					event.setNewColor(0x886a58);
				}
			}
		}
	}

	@SubscribeEvent
	public void onKillEntity(LivingDeathEvent event)
	{
		EntityLivingBase base = event.getEntityLiving();
		DamageSource src = event.getSource();
		if(src != null && src.getTrueSource() != null)
		{
			Entity e = src.getTrueSource();
			if(e instanceof EntityPlayer && !(e instanceof FakePlayer))
			{
				EntityPlayer player = (EntityPlayer) e;
				int addedEnergy = 0;
				if(base instanceof EntityPlayer && !(base instanceof FakePlayer))
				{
					int currentEnergy_int = ECUtils.getData((EntityPlayer)base).getPlayerUBMRU();
					addedEnergy += currentEnergy_int;
				}
				else
				{
					float maxHp = base.getMaxHealth();
					ItemStack helmet = player.inventory.armorInventory.get(3);
					if(!helmet.isEmpty() && helmet.getItem() == ItemsCore.magicArmorItems[4])
					{
						player.getFoodStats().addStats((int)(maxHp/10F), 1F);
					}
					addedEnergy += (20+MathUtils.randomFloat(e.getEntityWorld().rand)*10)*maxHp;
				}
				if(BaublesApi.getBaublesHandler(player) != null && addedEnergy > 0)
					for(int i = 0; i < 7; ++i)
					{
						ItemStack bStk = BaublesApi.getBaublesHandler(player).getStackInSlot(i);
						if(bStk.getItem() instanceof IUBMRUGainModifyHandler)
						{
							IUBMRUGainModifyHandler gmod = (IUBMRUGainModifyHandler)bStk.getItem();
							addedEnergy = MathHelper.floor(gmod.getModifiedValue(addedEnergy, bStk, player.getEntityWorld().rand,player));
						}
					}
				int currentEnergy_int = ECUtils.getData(player).getPlayerUBMRU();
				currentEnergy_int += addedEnergy;
				ECUtils.getData(player).modifyUBMRU(currentEnergy_int);
				boolean canDropEmber = false;
				IBaublesItemHandler b = BaublesApi.getBaublesHandler(player);
				if(b != null)
				{
					for(int i = 0; i < b.getSlots(); ++i)
					{
						ItemStack is = b.getStackInSlot(i);
						if(is.getItem() instanceof ItemBaublesSpecial && is.getItemDamage() == 12)
							canDropEmber = true;
					}
				}
				if(canDropEmber)
				{
					if(player.getEntityWorld().rand.nextFloat() < 0.05F)
					{
						ItemStack emberStack = new ItemStack(ItemsCore.ember,1,player.getEntityWorld().rand.nextInt(8));
						EntityItem emberItem = new EntityItem(base.getEntityWorld(), base.posX, base.posY, base.posZ, emberStack);
						if(!base.getEntityWorld().isRemote)
							base.getEntityWorld().spawnEntity(emberItem);
					}
				}
			}
		}
		if(base instanceof EntityPlayer)
		{
			if(!(base instanceof FakePlayer))
				ECUtils.getData((EntityPlayer) base).modifyUBMRU(0);
		}
	}

	@SubscribeEvent
	public void onPacketRecieved(DummyEvent_OnPacketRecieved event) {
		DummyData[] packetData = DataStorage.parseData(event.recievedData);
		if(packetData != null && packetData.length > 0) {
			try  {
				DummyData modData = packetData[0];
				if(modData.fieldName.equalsIgnoreCase("mod") && modData.fieldValue.equalsIgnoreCase("essentialcraft.particle.item")) {
					double sX = Double.parseDouble(packetData[1].fieldValue);
					double sY = Double.parseDouble(packetData[2].fieldValue);
					double sZ = Double.parseDouble(packetData[3].fieldValue);
					double mX = Double.parseDouble(packetData[4].fieldValue);
					double mY = Double.parseDouble(packetData[5].fieldValue);
					double mZ = Double.parseDouble(packetData[6].fieldValue);
					if(event.effectiveSide == Side.CLIENT)
						EssentialCraftCore.proxy.ItemFX(sX,sY,sZ,mX,mY,mZ);
				}
				else if(modData.fieldName.equalsIgnoreCase("mod") && modData.fieldValue.equalsIgnoreCase("essentialcraft.item.wings")) {
					double sX = Double.parseDouble(packetData[1].fieldValue);
					double sY = Double.parseDouble(packetData[2].fieldValue);
					double sZ = Double.parseDouble(packetData[3].fieldValue);
					EntityPlayer player = MiscUtils.getPlayerFromUUID(packetData[4].fieldValue);
					player.setPosition(sX, sY, sZ);
					ItemStack wings = BaublesApi.getBaublesHandler(player).getStackInSlot(3);
					if(!wings.isEmpty()) {
						if(ECUtils.tryToDecreaseMRUInStorage(player, 1)){}
						else if(wings.getCapability(CapabilityMRUHandler.MRU_HANDLER_ITEM_CAPABILITY, null).getMRU() > 0) {
							wings.getCapability(CapabilityMRUHandler.MRU_HANDLER_ITEM_CAPABILITY, null).extractMRU(1, true);
						}
					}
				}
				if(modData.fieldName.equalsIgnoreCase("mod") && modData.fieldValue.equalsIgnoreCase("essentialcraft.player.position")) {
					EssentialCraftCore.proxy.handlePositionChangePacket(packetData);
				}
				else if(modData.fieldName.equalsIgnoreCase("mod") && modData.fieldValue.equalsIgnoreCase("essentialcraft.sound")) {
					EssentialCraftCore.proxy.handleSoundPlay(packetData);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}

	public static ArrayList<ResourceLocation> textures = new ArrayList<ResourceLocation>();

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onTextureStitch(TextureStitchEvent.Pre event) {
		TextureMap map = event.getMap();
		ClientProxy.mruIcon = map.registerSprite(new ResourceLocation("essentialcraft:blocks/mru"));
		ClientProxy.chaosIcon = map.registerSprite(new ResourceLocation("essentialcraft:blocks/chaos"));
		ClientProxy.frozenIcon = map.registerSprite(new ResourceLocation("essentialcraft:blocks/frozen"));
		ClientProxy.mruParticleIcon = map.registerSprite(new ResourceLocation("essentialcraft:items/particles/particle_mru_dummy_icon"));
		ClientProxy.fogIcon = map.registerSprite(new ResourceLocation("essentialcraft:items/particles/fog"));
		for(int i = 0; i < 4; ++i)
			ClientProxy.c_spell_particle_array[i] = map.registerSprite(new ResourceLocation("essentialcraft:items/particles/c_spell_"+i));
		for(ResourceLocation rl : textures)
			map.registerSprite(rl);
		map.registerSprite(new ResourceLocation("essentialcraft:blocks/null"));
		map.registerSprite(new ResourceLocation("essentialcraft:special/whitebox"));
	}
}
