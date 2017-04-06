package ec3.utils.common;

import java.util.ArrayList;
import java.util.List;

import DummyCore.Utils.UnformedItemStack;
import net.minecraft.block.material.Material;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import ec3.api.CorruptionEffectLibrary;
import ec3.api.EnumCorruptionEffect;
import ec3.api.ICorruptionEffect;
import ec3.common.registry.PotionRegistry;

public class CorruptionEffectEC3NBTBased implements ICorruptionEffect{

	public EnumCorruptionEffect type = EnumCorruptionEffect.BODY;
	public int cost = 0;
	public int meta = 0;
	
	public static final ResourceLocation loc(String s)
	{
		return new ResourceLocation("essentialcraft","textures/special/corruptionEffects/"+s+".png");
	}
	
	public ResourceLocation[] allLocs = new ResourceLocation[]
	{
		loc("body_hands"),
		loc("body_hands"),
		loc("body_hands"),
		loc("body_legs"),
		loc("body_legs"),
		loc("mind_white"),
		loc("mind_black"),
		loc("mind_purple"),
		loc("mind_red"),
		loc("mind_blue"),
		loc("mind_teal"),
		loc("matrix_damage"),
		loc("matrix_end"),
		loc("matrix_explosion"),
		loc("matrix_lava"),
		loc("matrix_lightning"),
		loc("matrix_matrix"),
		loc("matrix_moon"),
		loc("matrix_mru"),
		loc("matrix_nether"),
		loc("matrix_overworld"),
		loc("matrix_rain"),
		loc("matrix_sun"),
		loc("matrix_water"),
		loc("matrix_wind")
	};
	
	//0 - Hands damaged, --Break Speed
	//1 - Hands damaged, --Damage
	//2 - Hands damaged, damage upon swing
	//3 - Legs damaged, --Move speed
	//4 - Legs damaged, ++Fall damage
	//5 - Mind damaged, Parad0x(white)
	//6 - Mind damaged, Hallucinations(black)
	//7 - Mind damaged, Overlay(purple)
	//8 - Mind damaged, Inventory messup(Red)
	//9 - Mind damaged, Throw current item(Blue)
	//10 - Mind damaged, Random right-click(Teal)
	//11 - Matrix damaged, more damage(_damage)
	//12 - Matrix damaged, periodic damage in the end(_end)
	//13 - Matrix damaged, spontaneous explosions(_explosions)
	//14 - Matrix damaged, random fire(_lava)
	//15 - Matrix damaged, spontaneous lightnings(_lightning)
	//16 - Matrix damaged, more matrix damage(_matrix)
	//17 - Matrix damaged, periodic damage when exposed to the moon, based on the moon cycle(_moon)
	//18 - Matrix damaged, MRU corruption deals damage even on level 0(_mru)
	//19 - Matrix damaged, periodic damage in the nether(_nether)
	//20 - Matrix damaged, periodic damage in the overworld(_overworld)
	//21 - Matrix damaged, periodic damage when exposed to the rain(_rain)
	//22 - Matrix damaged, periodic damage when exposed to the sun(_sun)
	//23 - Matrix damaged, damage while in water(_water)
	//24 - Matrix damaged, periodic random damage(_wind)
	
	public CorruptionEffectEC3NBTBased(){}
	
	public CorruptionEffectEC3NBTBased setType(EnumCorruptionEffect e)
	{
		type = e;
		return this;
	}
	
	public CorruptionEffectEC3NBTBased setCost(int i)
	{
		cost = i;
		return this;
	}
	
	public CorruptionEffectEC3NBTBased setMeta(int i)
	{
		meta = i;
		return this;
	}
	
	public CorruptionEffectEC3NBTBased done()
	{
		CorruptionEffectLibrary.addEffect(this);
		return this;
	}
	
	@Override
	public void readFromNBTTagCompound(NBTTagCompound tag,int tagID)
	{
		if(tagID == -1)
		{
			type = EnumCorruptionEffect.values()[tag.getInteger("type")];
			cost = tag.getInteger("cost");
			meta = tag.getInteger("meta");
		}else
		{
			NBTTagCompound ttag = tag.getCompoundTag("effectTag_"+tagID);
			type = EnumCorruptionEffect.values()[ttag.getInteger("type")];
			cost = ttag.getInteger("cost");
			meta = ttag.getInteger("meta");
		}
	}

	@Override
	public NBTTagCompound writeToNBTTagCompound(NBTTagCompound tag, int tagID) 
	{
		if(tagID == -1)
		{
			tag.setInteger("type", type.ordinal());
			tag.setInteger("cost", cost);
			tag.setInteger("meta", meta);
		}else
		{
			NBTTagCompound ttag = new NBTTagCompound();
			ttag.setInteger("type", type.ordinal());
			ttag.setInteger("cost", cost);
			ttag.setInteger("meta", meta);
			tag.setTag("effectTag_"+tagID, ttag);
		}
		return tag;
	}

	@Override
	public EnumCorruptionEffect getType()
	{
		return type;
	}

	@Override
	public void onPlayerTick(EntityPlayer player)
	{
		if(!player.getEntityWorld().isRemote)
		{
			switch(this.meta)
			{
				case 0:
				{
					if(player.getEntityWorld().rand.nextFloat() <= 0.001F)
					{
						message(player,"ec3.effect.desc_"+meta);
						player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS,400,1,true,true));
					}
					break;
				}
				case 1:
				{
					if(player.getEntityWorld().rand.nextFloat() <= 0.001F)
					{
						message(player,"ec3.effect.desc_"+meta);
						player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS,400,1,true,true));
					}
					break;
				}
				case 2:
				{
					if(player.getEntityWorld().rand.nextFloat() <= 0.01F && player.isSwingInProgress)
					{
						message(player,"ec3.effect.desc_"+meta);
						player.attackEntityFrom(DamageSource.cactus, 3);
					}
					break;
				}
				case 3:
				{
					if(player.getEntityWorld().rand.nextFloat() <= 0.001F)
					{
						message(player,"ec3.effect.desc_"+meta);
						player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS,400,1,true,true));
					}
					break;
				}
				case 4:
				{
					if(player.fallDistance > 0)
					{
						player.fallDistance += 1;
					}break;
				}
				case 5:
				{
					if(player.getEntityWorld().rand.nextFloat() <= 0.001F && !player.isPotionActive(PotionRegistry.paradox))
					{
						message(player,"ec3.effect.desc_"+meta);
						player.addPotionEffect(new PotionEffect(PotionRegistry.paradox,2000,1,true,true));
					}break;
				}
				case 6:
				{
					if(player.getEntityWorld().rand.nextFloat() <= 0.0008F)
					{
						message(player,"ec3.effect.desc_"+meta);
					}break;
				}
				case 7:
				{
					if(player.getEntityWorld().rand.nextFloat() <= 0.001F)
					{
						message(player,"ec3.effect.desc_"+meta);
					}break;
				}
				case 8:
				{
					if(player.getEntityWorld().rand.nextFloat() <= 0.00005F)
					{
						List<ItemStack> playerItems = new ArrayList<ItemStack>();
						for(int i = 0; i < player.inventory.getSizeInventory(); ++i)
						{
							if(player.inventory.getStackInSlot(i) != null)
								playerItems.add(player.inventory.getStackInSlot(i).copy());
							else
								playerItems.add(null);
						}
						List<ItemStack> messedItems = new ArrayList<ItemStack>();
						while(!playerItems.isEmpty())
						{
							int randomInt = player.getEntityWorld().rand.nextInt(playerItems.size());
							messedItems.add(playerItems.get(randomInt));
							playerItems.remove(randomInt);
						}
						for(int i = 0; i < player.inventory.getSizeInventory(); ++i)
						{
							player.inventory.setInventorySlotContents(i, messedItems.get(i));
						}
						messedItems.clear();
						playerItems.clear();
						messedItems = null;
						playerItems = null;
						message(player,"ec3.effect.desc_"+meta);
					}break;
				}
				case 9:
				{
					if(player.getEntityWorld().rand.nextFloat() <= 0.00007F)
					{
						message(player,"ec3.effect.desc_"+meta);
						player.inventory.dropAllItems();
					}break;
				}
				case 10:
				{
					if(player.getEntityWorld().rand.nextFloat() <= 0.001F)
					{
						message(player,"ec3.effect.desc_"+meta);
						if(player.inventory.getCurrentItem()!=null)
						{
							player.inventory.getCurrentItem().useItemRightClick(player.getEntityWorld(), player, EnumHand.MAIN_HAND);
						}
					}break;
				}
				case 11:
				{
					if(player.getEntityWorld().rand.nextFloat() <= 0.03F && player.hurtTime > 0)
					{
						message(player,"ec3.effect.desc_"+meta);
						player.hurtTime = 0;
						player.hurtResistantTime = 0;
					}break;
				}
				case 12:
				{
					if(player.getEntityWorld().rand.nextFloat() <= 0.003F && player.dimension == 1)
					{
						message(player,"ec3.effect.desc_"+meta);
						player.attackEntityFrom(DamageSource.drown, 1);
					}break;
				}
				case 13:
				{
					if(player.getEntityWorld().rand.nextFloat() <= 0.0006F)
					{
						message(player,"ec3.effect.desc_"+meta);
						player.getEntityWorld().createExplosion(null, player.posX, player.posY, player.posZ, 3, true);
					}break;
				}
				case 14:
				{
					if(player.getEntityWorld().rand.nextFloat() <= 0.001F)
					{
						message(player,"ec3.effect.desc_"+meta);
						player.setFire(8);
					}break;
				}
				case 15:
				{
					if(player.getEntityWorld().rand.nextFloat() <= 0.0007F)
					{
						message(player,"ec3.effect.desc_"+meta);
						EntityLightningBolt bolt = new EntityLightningBolt(player.getEntityWorld(),player.posX,player.posY,player.posZ, false);
						player.getEntityWorld().spawnEntity(bolt);
						player.getEntityWorld().addWeatherEffect(bolt);
						player.onStruckByLightning(bolt);
					}break;
				}
				case 16:
				{
					if(player.getEntityWorld().rand.nextFloat() <= 0.01F)
					{
						ECUtils.getData(player).modifyOverhaulDamage(ECUtils.getData(player).getOverhaulDamage()+1);
					}break;
				}
				case 17:
				{
					if(player.getEntityWorld().rand.nextFloat() <= 0.01F && !player.getEntityWorld().isRaining() && !player.getEntityWorld().isDaytime() && player.getEntityWorld().isDaytime() && player.getEntityWorld().canBlockSeeSky(new BlockPos(MathHelper.floor(player.posX), MathHelper.floor(player.posY+player.eyeHeight), MathHelper.floor(player.posZ))))
					{
						int moonPhase = (int) (player.getEntityWorld().getWorldTime() / 24000L % 8L + 8L) % 8;
						int damage = 0;
						if(moonPhase == 0)
							damage = 5;
						if(moonPhase == 1 || moonPhase == 7)
							damage = 3;
						if(moonPhase == 2 || moonPhase == 6)
							damage = 2;
						if(moonPhase == 3 || moonPhase == 5)
							damage = 1;
						
						message(player,"ec3.effect.desc_"+meta);
						player.attackEntityFrom(DamageSource.magic, damage);
					}break;
				}
				case 18:
				{
					if(player.getEntityWorld().rand.nextFloat() <= 0.001F && player.isPotionActive(PotionRegistry.mruCorruptionPotion))
					{
						player.attackEntityFrom(DamageSource.magic, 4);
					}break;
				}
				case 19:
				{
					if(player.getEntityWorld().rand.nextFloat() <= 0.003F && player.dimension == -1)
					{
						message(player,"ec3.effect.desc_"+meta);
						player.attackEntityFrom(DamageSource.drown, 1);
					}break;
				}
				case 20:
				{
					if(player.getEntityWorld().rand.nextFloat() <= 0.003F && player.dimension == 0)
					{
						message(player,"ec3.effect.desc_"+meta);
						player.attackEntityFrom(DamageSource.drown, 1);
					}break;
				}
				case 21:
				{
					if(player.getEntityWorld().rand.nextFloat() <= 0.1F && player.getEntityWorld().isRaining() && player.getEntityWorld().isDaytime() && player.getEntityWorld().canBlockSeeSky(new BlockPos(MathHelper.floor(player.posX), MathHelper.floor(player.posY+player.eyeHeight), MathHelper.floor(player.posZ))))
					{
						//message(player,"ec3.effect.desc_"+meta);
						player.attackEntityFrom(DamageSource.wither, 1);
					}break;
				}
				case 22:
				{
					if(player.getEntityWorld().rand.nextFloat() <= 0.003F && player.getEntityWorld().isDaytime() && !player.getEntityWorld().isRaining() && player.getEntityWorld().canBlockSeeSky(new BlockPos(MathHelper.floor(player.posX), MathHelper.floor(player.posY+player.eyeHeight), MathHelper.floor(player.posZ))))
					{
						message(player,"ec3.effect.desc_"+meta);
						player.attackEntityFrom(DamageSource.wither, 1);
					}break;
				}
				case 23:
				{
					if(player.getEntityWorld().rand.nextFloat() <= 0.03F && player.isInsideOfMaterial(Material.WATER))
					{
						message(player,"ec3.effect.desc_"+meta);
						player.attackEntityFrom(DamageSource.wither, 3);
					}break;
				}
				case 24:
				{
					if(player.getEntityWorld().rand.nextFloat() <= 0.005F)
					{
						message(player,"ec3.effect.desc_"+meta);
						player.attackEntityFrom(DamageSource.wither, 1);
					}break;
				}
			}
		}
	}
	
	public static void message(EntityPlayer p, String message)
	{
		//p.addChatComponentMessage(new ChatComponentText(I18n.translateToLocal(message)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_RED)));
	}

	@Override
	public ResourceLocation getEffectIcon()
	{
		return meta < allLocs.length ? allLocs[meta] : null;
	}

	@Override
	public int getStickiness()
	{
		return cost;
	}

	@Override
	public ICorruptionEffect copy() {
		NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBTTagCompound(tag,-1);
		CorruptionEffectEC3NBTBased effect = new CorruptionEffectEC3NBTBased();
		effect.readFromNBTTagCompound(tag,-1);
		tag = null;
		return effect;
	}

	@Override
	public boolean canMultiply() {
		return false;
	}

	@Override
	public ArrayList<UnformedItemStack> cureItems() {
		return new ArrayList<UnformedItemStack>();
	}

	@Override
	public boolean effectEquals(ICorruptionEffect effect) {
		return effect.getClass() == this.getClass() ? (CorruptionEffectEC3NBTBased.class.cast(effect).cost == this.cost && CorruptionEffectEC3NBTBased.class.cast(effect).meta == this.meta && CorruptionEffectEC3NBTBased.class.cast(effect).type == this.type) : false;
	}

	@Override
	public String getLocalizedName() {
		return I18n.translateToLocal("ec3.effect.name_"+this.meta);
	}

	@Override
	public String getLocalizedDesc() {
		return I18n.translateToLocal("ec3.effect.desc_"+this.meta);
	}

}
