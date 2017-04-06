package ec3.common.item;

import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Client.ModelUtils;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.IItemOverlayElement;
import DummyCore.Utils.MathUtils;
import DummyCore.Utils.MiscUtils;
import ec3.api.GunRegistry;
import ec3.api.GunRegistry.GunMaterial;
import ec3.api.GunRegistry.GunType;
import ec3.api.GunRegistry.LenseMaterial;
import ec3.api.GunRegistry.ScopeMaterial;
import ec3.common.entity.EntityMRURay;
import ec3.common.registry.SoundRegistry;
import ec3.utils.common.ECUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemGun extends ItemStoresMRUInNBT implements IModelRegisterer {
	Random rnd = new Random();

	public String gunType;

	public ItemGun(String s)
	{
		super();
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
		this.setFull3D();
		this.setMaxDamage(0);
		gunType = s;
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int indexInInventory, boolean isCurrentItem)
	{
		if(isCurrentItem && this.gunType.equalsIgnoreCase("gatling"))
		{
			if(entity instanceof EntityLivingBase)
			{
				EntityLivingBase.class.cast(entity).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS,3,3,true,true));
			}
		}
		if(MiscUtils.getStackTag(itemStack).hasKey("cool"))
		{
			MiscUtils.getStackTag(itemStack).setFloat("cool", MiscUtils.getStackTag(itemStack).getFloat("cool")-1);
			if(MiscUtils.getStackTag(itemStack).getFloat("cool") <= 0)
				MiscUtils.getStackTag(itemStack).removeTag("cool");
		}
		if(MiscUtils.getStackTag(itemStack).hasKey("gunShots") && isCurrentItem)
		{
			float current = MiscUtils.getStackTag(itemStack).getFloat("gunShots");
			float max = MiscUtils.getStackTag(itemStack).getCompoundTag("stats").getFloat("shots");
			if(current+1 >= max)
			{
				Vec3d look = entity.getLookVec();
				look.rotatePitch(-0.5F);
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, entity.posX+look.xCoord, entity.posY-0.5D+look.yCoord, entity.posZ+look.zCoord, 0, 0, 0);
			}
		}
		super.onUpdate(itemStack, world, entity, indexInInventory, isCurrentItem);
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List, boolean par4) 
	{
		if(MiscUtils.getStackTag(par1ItemStack).hasKey("stats") && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
		{
			NBTTagCompound stats = MiscUtils.getStackTag(par1ItemStack).getCompoundTag("stats");
			par3List.add(I18n.translateToLocal("ec3.gun.txt.damage")+" "+MathHelper.floor(stats.getFloat("damage")));
			par3List.add(I18n.translateToLocal("ec3.gun.txt.durability")+" "+MathHelper.floor(stats.getFloat("durability")));
			par3List.add(I18n.translateToLocal("ec3.gun.txt.reload")+" "+MathHelper.floor(stats.getFloat("reload")));
			par3List.add(I18n.translateToLocal("ec3.gun.txt.knockback")+" "+MathHelper.floor(stats.getFloat("knockback")));
			par3List.add(I18n.translateToLocal("ec3.gun.txt.speed")+" "+MathHelper.floor(stats.getFloat("speed")));
			par3List.add(I18n.translateToLocal("ec3.gun.txt.spread")+" "+MathHelper.floor(stats.getFloat("spread")));
			par3List.add(I18n.translateToLocal("ec3.gun.txt.shots")+" "+MathHelper.floor(stats.getFloat("shots")));
			par3List.add(I18n.translateToLocal("ec3.gun.txt.zoom")+" "+MathHelper.floor(stats.getFloat("zoom")));
			par3List.add(I18n.translateToLocal("ec3.gun.txt.balance_"+""+MathHelper.floor(stats.getFloat("balance"))));
		}else if(MiscUtils.getStackTag(par1ItemStack).hasKey("stats"))
		{
			par3List.add(TextFormatting.BLUE+""+TextFormatting.ITALIC+I18n.translateToLocal("ec3.txt.viewInfoHotkey"));
		}
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
	}

	public ActionResult<ItemStack> onItemRightClick(ItemStack gun, World w, EntityPlayer p, EnumHand h)
	{
		if(!gun.getTagCompound().hasKey("base") && !w.isRemote)
		{
			createRandomGun(gun);
			return new ActionResult(EnumActionResult.PASS,gun);
		}
		if(p.isHandActive())
			return new ActionResult(EnumActionResult.PASS,gun);
		if(this.gunType.equalsIgnoreCase("rifle") || this.gunType.equalsIgnoreCase("gatling"))
		{
			p.setActiveHand(h);
		}
		else
		{
			if(gun.getTagCompound().hasKey("base"))
			{
				float balance = 0;
				if(gun.getTagCompound().hasKey("lense"))
				{
					String lenseID = gun.getTagCompound().getString("lense");
					LenseMaterial lense = GunRegistry.getLenseFromID(lenseID);
					for(GunType gt : GunType.values()) {
						if(lense != null && lense.materialData.containsKey(gt))
						{
							for(DummyData d : lense.materialData.get(gt))
							{
								if(d.fieldName.equalsIgnoreCase("balance"))
									balance = (int)Float.parseFloat(d.fieldValue);
							}
						}
					}
				}
				if(MiscUtils.getStackTag(gun).hasKey("stats"))
				{
					NBTTagCompound stats = MiscUtils.getStackTag(gun).getCompoundTag("stats");
					if(!MiscUtils.getStackTag(gun).hasKey("gunDamage"))
					{
						MiscUtils.getStackTag(gun).setFloat("gunDamage", 0);
					}
					if(!MiscUtils.getStackTag(gun).hasKey("gunShots"))
					{
						MiscUtils.getStackTag(gun).setFloat("gunShots", 0);
					}
					if(MiscUtils.getStackTag(gun).hasKey("cool"))
					{
						return new ActionResult(EnumActionResult.PASS,gun);
					}
					if(MiscUtils.getStackTag(gun).getFloat("gunShots")+1 <= stats.getFloat("shots"))
						MiscUtils.getStackTag(gun).setFloat("gunShots", MiscUtils.getStackTag(gun).getFloat("gunShots")+1);
					else
					{
						p.setActiveHand(h);
						return new ActionResult(EnumActionResult.PASS,gun);
					}
					if(ECUtils.tryToDecreaseMRUInStorage(p, (int) -stats.getFloat("damage")*10) || this.setMRU(gun, (int) -stats.getFloat("damage")*10))
					{
						if(MiscUtils.getStackTag(gun).getFloat("gunDamage")+1 <= stats.getFloat("durability"))
							MiscUtils.getStackTag(gun).setFloat("gunDamage", MiscUtils.getStackTag(gun).getFloat("gunDamage")+1);
						else
						{
							if(!w.isRemote && w.rand.nextFloat() <= 0.25F)
							{
								w.playSound(p.posX, p.posY, p.posZ, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1, 1, false);
								MiscUtils.getStackTag(gun).setFloat("gunShots", stats.getFloat("shots"));
							}
						}


						MiscUtils.getStackTag(gun).setFloat("cool", stats.getFloat("speed")*2);
						w.playSound(p.posX, p.posY, p.posZ, SoundRegistry.gunBeam, SoundCategory.PLAYERS, 0.1F+stats.getFloat("damage")/100, 2-stats.getFloat("damage")/50, false);
						EntityMRURay ray = new EntityMRURay(w,p,stats.getFloat("damage"),stats.getFloat("spread")/2,balance);
						if(!w.isRemote)
							w.spawnEntity(ray);
						p.rotationPitch -= stats.getFloat("knockback");
					}
				}
			}
		}
		return new ActionResult(EnumActionResult.PASS,gun);
	}

	public static void createRandomGun(ItemStack gun)
	{
		Random rand = new Random();
		NBTTagCompound tag = MiscUtils.getStackTag(gun);
		tag.setString("base", GunRegistry.gunMaterials.get(rand.nextInt(GunRegistry.gunMaterials.size())).id);
		tag.setString("device", GunRegistry.gunMaterials.get(rand.nextInt(GunRegistry.gunMaterials.size())).id);
		tag.setString("handle", GunRegistry.gunMaterials.get(rand.nextInt(GunRegistry.gunMaterials.size())).id);
		tag.setString("lense", GunRegistry.lenseMaterials.get(rand.nextInt(GunRegistry.lenseMaterials.size())).id);
		ItemGun g = (ItemGun)gun.getItem();
		if(g.gunType.equalsIgnoreCase("sniper"))
		{
			tag.setString("scope", GunRegistry.scopeMaterialsSniper.get(rand.nextInt(GunRegistry.scopeMaterialsSniper.size())).id);
		}
		else if(!g.gunType.equalsIgnoreCase("gatling"))
		{
			tag.setString("scope", GunRegistry.scopeMaterials.get(rand.nextInt(GunRegistry.scopeMaterials.size())).id);
		}
		gun.setTagCompound(tag);
		calculateGunStats(gun);
	}

	public static void calculateGunStats(ItemStack gun)
	{
		NBTTagCompound gunTag = MiscUtils.getStackTag(gun);
		if(gunTag.hasKey("stats"))
			gunTag.removeTag("stats");

		NBTTagCompound stats = new NBTTagCompound();
		GunMaterial base = null;
		GunMaterial handle = null;
		GunMaterial device = null;
		LenseMaterial lense = null;
		ScopeMaterial scope = null;
		ItemGun iGun = ItemGun.class.cast(gun.getItem());

		float damage = 0;
		float durability = 0;
		float reload = 0;
		float knockback = 0;
		float speed = 0;
		float spread = 0;
		float shots = 0;
		float zoom = 0;
		float balance = 0;
		GunType gt = 
				iGun.gunType.equalsIgnoreCase("pistol") ? GunType.PISTOL :
					iGun.gunType.equalsIgnoreCase("rifle") ? GunType.RIFLE :
						iGun.gunType.equalsIgnoreCase("sniper") ? GunType.SNIPER :
							iGun.gunType.equalsIgnoreCase("gatling") ? GunType.GATLING :
								GunType.PISTOL;


		if(gunTag.hasKey("base"))
		{
			base = GunRegistry.getGunFromID(gunTag.getString("base"));
		}

		if(gunTag.hasKey("device"))
		{
			device = GunRegistry.getGunFromID(gunTag.getString("device"));
		}

		if(gunTag.hasKey("handle"))
		{
			handle = GunRegistry.getGunFromID(gunTag.getString("handle"));
		}

		if(gunTag.hasKey("lense"))
		{
			lense = GunRegistry.getLenseFromID(gunTag.getString("lense"));
		}

		if(gunTag.hasKey("scope"))
		{
			if(ItemGun.class.cast(gun.getItem()).gunType.equalsIgnoreCase("sniper"))
			{
				scope = GunRegistry.getScopeSniperFromID(gunTag.getString("scope"));
			}
			else
			{
				scope = GunRegistry.getScopeFromID(gunTag.getString("scope"));
			}
		}

		if(base != null && base.materialData.containsKey(gt))
		{
			for(DummyData d : base.materialData.get(gt))
			{
				if(d.fieldName.equalsIgnoreCase("durability"))
					durability += Float.parseFloat(d.fieldValue)/3;
				if(d.fieldName.equalsIgnoreCase("damage"))
					damage += Float.parseFloat(d.fieldValue)/3;
				if(d.fieldName.equalsIgnoreCase("reload"))
					reload += Float.parseFloat(d.fieldValue);  
				if(d.fieldName.equalsIgnoreCase("knockback"))
					knockback += Float.parseFloat(d.fieldValue)/3;  
				if(d.fieldName.equalsIgnoreCase("spread"))
					spread += Float.parseFloat(d.fieldValue)/3;  
				if(d.fieldName.equalsIgnoreCase("speed"))
					speed += Float.parseFloat(d.fieldValue);  
				if(d.fieldName.equalsIgnoreCase("shots"))
					shots += Float.parseFloat(d.fieldValue);  
			}
		}

		if(handle != null && handle.materialData.containsKey(gt))
		{
			for(DummyData d : handle.materialData.get(gt))
			{
				if(d.fieldName.equalsIgnoreCase("durability"))
					durability += Float.parseFloat(d.fieldValue);
				if(d.fieldName.equalsIgnoreCase("damage"))
					damage += Float.parseFloat(d.fieldValue)/3;
				if(d.fieldName.equalsIgnoreCase("reload"))
					reload += Float.parseFloat(d.fieldValue)/3;  
				if(d.fieldName.equalsIgnoreCase("knockback"))
					knockback += Float.parseFloat(d.fieldValue);  
				if(d.fieldName.equalsIgnoreCase("spread"))
					spread += Float.parseFloat(d.fieldValue)/3;  
				if(d.fieldName.equalsIgnoreCase("speed"))
					speed += Float.parseFloat(d.fieldValue)/3;  
				if(d.fieldName.equalsIgnoreCase("shots"))
					shots += Float.parseFloat(d.fieldValue)/3;   
			}
		}

		if(device != null && device.materialData.containsKey(gt))
		{
			for(DummyData d : device.materialData.get(gt))
			{
				if(d.fieldName.equalsIgnoreCase("durability"))
					durability += Float.parseFloat(d.fieldValue)/3;
				if(d.fieldName.equalsIgnoreCase("damage"))
					damage += Float.parseFloat(d.fieldValue);
				if(d.fieldName.equalsIgnoreCase("reload"))
					reload += Float.parseFloat(d.fieldValue)/3;  
				if(d.fieldName.equalsIgnoreCase("knockback"))
					knockback += Float.parseFloat(d.fieldValue)/3;  
				if(d.fieldName.equalsIgnoreCase("spread"))
					spread += Float.parseFloat(d.fieldValue);  
				if(d.fieldName.equalsIgnoreCase("speed"))
					speed += Float.parseFloat(d.fieldValue)/3;  
				if(d.fieldName.equalsIgnoreCase("shots"))
					shots += Float.parseFloat(d.fieldValue)/3;   
			}
		}

		if(lense != null && lense.materialData.containsKey(gt))
		{
			for(DummyData d : lense.materialData.get(gt))
			{
				if(d.fieldName.equalsIgnoreCase("durability"))
					durability += Float.parseFloat(d.fieldValue)/3;
				if(d.fieldName.equalsIgnoreCase("damage"))
					damage += Float.parseFloat(d.fieldValue)/3;
				if(d.fieldName.equalsIgnoreCase("reload"))
					reload += Float.parseFloat(d.fieldValue)/3;
				if(d.fieldName.equalsIgnoreCase("knockback"))
					knockback += Float.parseFloat(d.fieldValue)/3;
				if(d.fieldName.equalsIgnoreCase("spread"))
					spread += Float.parseFloat(d.fieldValue)/3;
				if(d.fieldName.equalsIgnoreCase("speed"))
					speed += Float.parseFloat(d.fieldValue)/3;
				if(d.fieldName.equalsIgnoreCase("shots"))
					shots += Float.parseFloat(d.fieldValue)/3;
				if(d.fieldName.equalsIgnoreCase("balance"))
					balance = (int)Float.parseFloat(d.fieldValue);
			}
		}

		if(scope != null && scope.materialData.containsKey(gt))
		{
			for(DummyData d : scope.materialData.get(gt))
			{
				if(d.fieldName.equalsIgnoreCase("durability"))
					durability += Float.parseFloat(d.fieldValue)/3;
				if(d.fieldName.equalsIgnoreCase("damage"))
					damage += Float.parseFloat(d.fieldValue)/3;
				if(d.fieldName.equalsIgnoreCase("reload"))
					reload += Float.parseFloat(d.fieldValue)/3;  
				if(d.fieldName.equalsIgnoreCase("knockback"))
					knockback += Float.parseFloat(d.fieldValue)/3;  
				if(d.fieldName.equalsIgnoreCase("spread"))
					spread += Float.parseFloat(d.fieldValue)/3;  
				if(d.fieldName.equalsIgnoreCase("speed"))
					speed += Float.parseFloat(d.fieldValue)/3;  
				if(d.fieldName.equalsIgnoreCase("shots"))
					shots += Float.parseFloat(d.fieldValue)/3;   
				if(d.fieldName.equalsIgnoreCase("scope.zoom"))
					zoom += Float.parseFloat(d.fieldValue);   
			}
		}

		if(gt == GunType.GATLING)
		{
			speed = 0;
			spread *= 4.5F;
			shots *= 20;
		}

		if(gt == GunType.RIFLE)
		{
			speed = 0;
			spread *= 1.3F;
			shots *= 6;
		}

		if(gt == GunType.SNIPER)
		{
			speed *= 9;
			spread /= 10;
			damage *= 2;
		}

		if(gt == GunType.PISTOL)
		{
			reload /= 2;
		}

		stats.setFloat("damage", damage);
		stats.setFloat("durability", durability);
		stats.setFloat("reload", reload);
		stats.setFloat("knockback", knockback);
		stats.setFloat("speed", speed);
		stats.setFloat("spread", spread);
		stats.setFloat("shots", shots);
		stats.setFloat("zoom", zoom);
		stats.setFloat("balance", balance);

		gunTag.setTag("stats", stats);
	}

	public ItemStack onItemUseFinish(ItemStack gun, World w, EntityLivingBase p)
	{
		if(gun.hasTagCompound())
		{
			NBTTagCompound tag = MiscUtils.getStackTag(gun);
			if(tag.hasKey("stats") && tag.getFloat("gunShots")+1 >= tag.getCompoundTag("stats").getFloat("shots"))
			{
				tag.setFloat("gunShots", 0);
				tag.setFloat("cool", 60);
			}
		}
		return gun;
	}

	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
	{
		if(!(player instanceof EntityPlayer))
			return;
		ItemStack gun = stack;
		EntityPlayer p = (EntityPlayer)player;
		World w = p.getEntityWorld();
		if(this.getItemUseAction(stack) == EnumAction.BLOCK)
		{
			if(count % 20 == 0)
				player.getEntityWorld().playSound(player.posX, player.posY, player.posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 0.4F, 1+MathUtils.randomFloat(player.getEntityWorld().rand), false);
			return;
		}
		else
		{
			if(this.gunType.equalsIgnoreCase("rifle") && count % 3 == 0)
			{
				if(gun.getTagCompound().hasKey("base"))
				{
					float balance = 0;
					if(gun.getTagCompound().hasKey("lense"))
					{
						String lenseID = gun.getTagCompound().getString("lense");
						LenseMaterial lense = GunRegistry.getLenseFromID(lenseID);
						for(GunType gt : GunType.values()) {
							if(lense != null && lense.materialData.containsKey(gt))
							{
								for(DummyData d : lense.materialData.get(gt))
								{
									if(d.fieldName.equalsIgnoreCase("balance"))
										balance = (int)Float.parseFloat(d.fieldValue);
								}
							}
						}
					}
					if(MiscUtils.getStackTag(gun).hasKey("stats"))
					{
						NBTTagCompound stats = MiscUtils.getStackTag(gun).getCompoundTag("stats");
						if(!MiscUtils.getStackTag(gun).hasKey("gunDamage"))
						{
							MiscUtils.getStackTag(gun).setFloat("gunDamage", 0);
						}
						if(!MiscUtils.getStackTag(gun).hasKey("gunShots"))
						{
							MiscUtils.getStackTag(gun).setFloat("gunShots", 0);
						}
						if(MiscUtils.getStackTag(gun).getFloat("gunShots")+1 <= stats.getFloat("shots"))
							MiscUtils.getStackTag(gun).setFloat("gunShots", MiscUtils.getStackTag(gun).getFloat("gunShots")+1);
						else
						{
							p.stopActiveHand();
							return;
						}
						if(ECUtils.tryToDecreaseMRUInStorage(p, (int) -stats.getFloat("damage")*3) || this.setMRU(gun, (int) -stats.getFloat("damage")*3))
						{
							if(MiscUtils.getStackTag(gun).getFloat("gunDamage")+1 <= stats.getFloat("durability"))
								MiscUtils.getStackTag(gun).setFloat("gunDamage", MiscUtils.getStackTag(gun).getFloat("gunDamage")+1);
							else
							{
								if(!w.isRemote && w.rand.nextFloat() <= 0.25F)
								{
									w.playSound(p.posX, p.posY, p.posZ, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1, 1, false);
									MiscUtils.getStackTag(gun).setFloat("gunShots", stats.getFloat("shots"));
								}
							}

							MiscUtils.getStackTag(gun).setFloat("cool", stats.getFloat("speed")*2);
							w.playSound(p.posX, p.posY, p.posZ, SoundRegistry.gunBeam, SoundCategory.PLAYERS, 0.1F+stats.getFloat("damage")/100, 2-stats.getFloat("damage")/50, false);
							EntityMRURay ray = new EntityMRURay(w,p,stats.getFloat("damage"),stats.getFloat("spread")/2,balance);
							if(!w.isRemote)
								w.spawnEntity(ray);
						}
					}
				}
			}
			if(this.gunType.equalsIgnoreCase("gatling"))
			{
				int usingTicks = 10000-count;
				if(count >= 10000-60 && count % 5 == 0)
				{
					w.playSound(p.posX, p.posY, p.posZ, SoundEvents.ENTITY_MINECART_INSIDE, SoundCategory.PLAYERS, 0.1F, 0F+(float)usingTicks/30F, false);
				}
				if(usingTicks >= 60)
				{
					ECUtils.playSoundToAllNearby(p.posX, p.posY, p.posZ, "essentialcraft:sound.beam", 1F, 2, 16, p.dimension);
					if(gun.getTagCompound().hasKey("base"))
					{
						float balance = 0;
						if(gun.getTagCompound().hasKey("lense"))
						{
							String lenseID = gun.getTagCompound().getString("lense");
							LenseMaterial lense = GunRegistry.getLenseFromID(lenseID);
							for(GunType gt : GunType.values()) {
								if(lense != null && lense.materialData.containsKey(gt))
								{
									for(DummyData d : lense.materialData.get(gt))
									{
										if(d.fieldName.equalsIgnoreCase("balance"))
											balance = (int)Float.parseFloat(d.fieldValue);
									}
								}
							}
						}
						if(MiscUtils.getStackTag(gun).hasKey("stats"))
						{
							NBTTagCompound stats = MiscUtils.getStackTag(gun).getCompoundTag("stats");
							if(!MiscUtils.getStackTag(gun).hasKey("gunDamage"))
							{
								MiscUtils.getStackTag(gun).setFloat("gunDamage", 0);
							}
							if(!MiscUtils.getStackTag(gun).hasKey("gunShots") && !w.isRemote)
							{
								MiscUtils.getStackTag(gun).setFloat("gunShots", 0);
							}
							if(MiscUtils.getStackTag(gun).getFloat("gunShots")+1 <= stats.getFloat("shots"))
							{
								if(!w.isRemote)
									MiscUtils.getStackTag(gun).setFloat("gunShots", MiscUtils.getStackTag(gun).getFloat("gunShots")+1);
							}
							else
							{
								p.stopActiveHand();
								return;
							}
							if(ECUtils.tryToDecreaseMRUInStorage(p, (int) -stats.getFloat("damage")*2) || this.setMRU(gun, (int) -stats.getFloat("damage")*2))
							{
								if(MiscUtils.getStackTag(gun).getFloat("gunDamage")+1 <= stats.getFloat("durability"))
									MiscUtils.getStackTag(gun).setFloat("gunDamage", MiscUtils.getStackTag(gun).getFloat("gunDamage")+1);
								else
								{
									if(!w.isRemote && w.rand.nextFloat() <= 0.25F)
									{
										w.playSound(p.posX, p.posY, p.posZ, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1, 1, false);
										MiscUtils.getStackTag(gun).setFloat("gunShots", stats.getFloat("shots"));
									}
								}
								//w.playSound(p.posX, p.posY, p.posZ, "essentialcraft:sound.beam", 0.1F+stats.getFloat("damage")/100, 2-stats.getFloat("damage")/50, false);
								EntityMRURay ray = new EntityMRURay(w,p,stats.getFloat("damage"),stats.getFloat("spread")/2,balance);
								if(!w.isRemote)
									w.spawnEntity(ray);
							}
						}
					}
				}
			}
		}
	}

	public boolean showDurabilityBar(ItemStack stack)
	{
		return stack.hasTagCompound() && MiscUtils.getStackTag(stack).hasKey("stats");
	}

	public double getDurabilityForDisplay(ItemStack stack)
	{
		if(stack.hasTagCompound())
		{
			NBTTagCompound gunTag = MiscUtils.getStackTag(stack);
			if(gunTag.hasKey("stats"))
			{
				float currentDamage = gunTag.getFloat("gunDamage");
				float maxDamage = gunTag.getCompoundTag("stats").getFloat("durability");

				return currentDamage / maxDamage;
			}
		}
		return (double)stack.getItemDamage() / (double)stack.getMaxDamage();
	}

	public EnumAction getItemUseAction(ItemStack p_77661_1_)
	{
		if(p_77661_1_.hasTagCompound())
		{
			NBTTagCompound tag = MiscUtils.getStackTag(p_77661_1_);
			if(tag.hasKey("stats"))
			{
				float current = tag.getFloat("gunShots")+1;
				float max = tag.getCompoundTag("stats").getFloat("shots");
				if(current >= max)
					return EnumAction.BLOCK;
			}

		}
		return EnumAction.BOW;
	}

	public int getMaxItemUseDuration(ItemStack p_77626_1_)
	{
		if(p_77626_1_.hasTagCompound())
		{
			NBTTagCompound tag = MiscUtils.getStackTag(p_77626_1_);
			if(tag.hasKey("stats"))
			{
				float current = tag.getFloat("gunShots")+1;
				float max = tag.getCompoundTag("stats").getFloat("shots");
				if(current >= max)
					return MathHelper.floor(tag.getCompoundTag("stats").getFloat("reload")*20);
			}
		}
		return 10000;
	}

	@Override
	public void registerModels() {
		ModelUtils.setItemModelSingleIcon(this, "essentialcraft:item/"+getRegistryName().getResourcePath(), "internal");
		ModelBakery.registerItemVariants(this, new ModelResourceLocation("essentialcraft:item/"+getRegistryName().getResourcePath(), "inventory"));
		if(this == ItemsCore.sniper)
			ModelBakery.registerItemVariants(this, new ModelResourceLocation("essentialcraft:item/blank", "inventory"));
		MiscUtils.addItemOverlayElement(this, ItemOverlayGun.INSTANCE);
	}

	public static class ItemOverlayGun implements IItemOverlayElement {
		public static final ItemOverlayGun INSTANCE = new ItemOverlayGun();

		@Override
		public void renderItemOverlayIntoGUI(FontRenderer fr, ItemStack item, int x, int y, String text) {
			double health = 0;
			if(item.hasTagCompound()) {
				NBTTagCompound tag = MiscUtils.getStackTag(item);
				if(tag.hasKey("stats")) {
					float current = tag.getFloat("gunShots");
					float max = tag.getCompoundTag("stats").getFloat("shots");
					if(current > max)
						current = max;
					health = current/max;
				}
			}
			int j = (int)Math.round(13.0D - health * 13.0D);
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			GlStateManager.disableTexture2D();
			GlStateManager.disableAlpha();
			GlStateManager.disableBlend();
			Tessellator tessellator = Tessellator.getInstance();
			VertexBuffer vertexbuffer = tessellator.getBuffer();
			this.draw(vertexbuffer, x + 14, y, 2, 14, 0, 0, 0, 255);
			this.draw(vertexbuffer, x + 14, y, 1, 13, 51, 51, 51, 255);
			this.draw(vertexbuffer, x + 14, y + j, 1, 13-j, 186, 0, 0, 255);
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();
			GlStateManager.enableTexture2D();
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
		}

		private void draw(VertexBuffer renderer, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
			renderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
			renderer.pos((double)(x + 0), (double)(y + 0), 0.0D).color(red, green, blue, alpha).endVertex();
			renderer.pos((double)(x + 0), (double)(y + height), 0.0D).color(red, green, blue, alpha).endVertex();
			renderer.pos((double)(x + width), (double)(y + height), 0.0D).color(red, green, blue, alpha).endVertex();
			renderer.pos((double)(x + width), (double)(y + 0), 0.0D).color(red, green, blue, alpha).endVertex();
			Tessellator.getInstance().draw();
		}
	}
}
