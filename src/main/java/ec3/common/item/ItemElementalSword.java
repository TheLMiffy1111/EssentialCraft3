package ec3.common.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.MiscUtils;
import ec3.api.IItemRequiresMRU;
import ec3.utils.common.ECUtils;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemElementalSword extends ItemSword implements IItemRequiresMRU, IModelRegisterer /*ItemStoresMRUInNBT*/ {

	public static String[] names = new String[] {"fire", "water", "earth", "air", "normal"};
	public static int maxMRU = 5000;
	static Random rand = new Random(8192L);

	public Item setMaxMRU(int max)
	{
		maxMRU = max;
		return this;
	}

	public ItemElementalSword() {
		super(ItemsCore.elemental);
		this.setMaxMRU(5000);
		this.maxStackSize = 1;
		this.bFull3D = true;
		this.setMaxDamage(0);
	}

	@Override
	public boolean setMRU(ItemStack stack, int amount) {
		if(MiscUtils.getStackTag(stack).getInteger("mru")+amount >= 0 && MiscUtils.getStackTag(stack).getInteger("mru")+amount<=MiscUtils.getStackTag(stack).getInteger("maxMRU"))
		{
			MiscUtils.getStackTag(stack).setInteger("mru", MiscUtils.getStackTag(stack).getInteger("mru")+amount);
			return true;
		}
		return false;
	}

	@Override
	public int getMRU(ItemStack stack) {
		return MiscUtils.getStackTag(stack).getInteger("mru");
	}

	public boolean isItemTool(ItemStack p_77616_1_)
	{
		return true;
	}

	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase)
	{
		if(par3EntityLivingBase instanceof EntityPlayer) {
			String attrib = getPrimaryAttribute(par1ItemStack);
			if(attrib.contains("Fire") )
			{
				if((ECUtils.tryToDecreaseMRUInStorage((EntityPlayer) par3EntityLivingBase, -50) || this.setMRU(par1ItemStack, -50)))
				{
					par2EntityLivingBase.setFire(5);
				}
			}
			if(attrib.contains("Water"))
			{
				if((ECUtils.tryToDecreaseMRUInStorage((EntityPlayer) par3EntityLivingBase, -50) || this.setMRU(par1ItemStack, -50)))
				{
					par2EntityLivingBase.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS,40,0));
					par2EntityLivingBase.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS,40,0));
				}
			}
			if(attrib.contains("Earth"))
			{
				if((ECUtils.tryToDecreaseMRUInStorage((EntityPlayer) par3EntityLivingBase, -50) || this.setMRU(par1ItemStack, -50)))
				{
					par2EntityLivingBase.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS,10,0));
				}
			}
			if(attrib.contains("Air"))
			{
				if((ECUtils.tryToDecreaseMRUInStorage((EntityPlayer) par3EntityLivingBase, -50) || this.setMRU(par1ItemStack, -50)))
				{
					par3EntityLivingBase.addPotionEffect(new PotionEffect(MobEffects.SPEED,50,0));
					par3EntityLivingBase.addPotionEffect(new PotionEffect(MobEffects.STRENGTH,50,0));
				}
			}
			List<String> embers = getEmberEffects(par1ItemStack);
			if((ECUtils.tryToDecreaseMRUInStorage((EntityPlayer) par3EntityLivingBase, -50) || this.setMRU(par1ItemStack, -50)))
			{
				for(int i = 0; i < 11; ++i)
				{
					if(embers.contains("Damage +"+i)) {
					}
				}
				if(embers.contains("Slowness"))
				{
					par2EntityLivingBase.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS,60,0));
				}
				if(embers.contains("Greater Slowness"))
				{
					par2EntityLivingBase.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS,60,1));
				}
				if(embers.contains("Poison"))
				{
					par2EntityLivingBase.addPotionEffect(new PotionEffect(MobEffects.POISON,60,0));
				}
				if(embers.contains("Greater Poison"))
				{
					par2EntityLivingBase.addPotionEffect(new PotionEffect(MobEffects.POISON,60,1));
				}
				if(embers.contains("Damage Self"))
				{
					par3EntityLivingBase.attackEntityFrom(DamageSource.causeMobDamage(par3EntityLivingBase),2);
				}
				if(embers.contains("Lightning"))
				{
					EntityLightningBolt bold = new EntityLightningBolt(par2EntityLivingBase.getEntityWorld(), par2EntityLivingBase.posX, par2EntityLivingBase.posY, par2EntityLivingBase.posZ, false);
					par2EntityLivingBase.getEntityWorld().addWeatherEffect(bold);
					if(!par2EntityLivingBase.getEntityWorld().isRemote)
						par2EntityLivingBase.getEntityWorld().spawnEntity(bold);
					par3EntityLivingBase.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE,60,1));
				}
				if(embers.contains("Lifesteal"))
				{
					par3EntityLivingBase.heal(1);
				}
				if(embers.contains("Greater Lifesteal"))
				{
					par3EntityLivingBase.heal(3);
				}
				if(embers.contains("Weakness"))
				{
					par2EntityLivingBase.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS,60,0));
				}
				if(embers.contains("Greater Weakness"))
				{
					par2EntityLivingBase.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS,60,1));
				}
				if(embers.contains("Greater Damage Boost"))
				{
					par3EntityLivingBase.addPotionEffect(new PotionEffect(MobEffects.STRENGTH,60,1));
				}
				if(embers.contains("Damage Boost"))
				{
					par3EntityLivingBase.addPotionEffect(new PotionEffect(MobEffects.STRENGTH,60,0));
				}
				if(embers.contains("Greater Speed Boost"))
				{
					par3EntityLivingBase.addPotionEffect(new PotionEffect(MobEffects.SPEED,60,1));
				}
				if(embers.contains("Speed Boost"))
				{
					par3EntityLivingBase.addPotionEffect(new PotionEffect(MobEffects.SPEED,60,0));
				}
				if(embers.contains("Greater Hunger"))
				{
					par2EntityLivingBase.addPotionEffect(new PotionEffect(MobEffects.HUNGER,60,1));
				}
			}
		}
		//par2EntityLivingBase.attackEntityFrom(DamageSource.causeMobDamage(par3EntityLivingBase),damage);
		return false;
	}


	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int indexInInventory, boolean isCurrentItem)
	{
		ECUtils.initMRUTag(itemStack, maxMRU);
	}

	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot s, ItemStack stack)
	{
		Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();
		String attrib = getPrimaryAttribute(stack);
		int damage = 0;
		if(attrib.contains("Fire") )
		{
			damage += 7;
		}
		if(attrib.contains("Water"))
		{
			damage += 3;
		}
		if(attrib.contains("Earth"))
		{
			damage += 8;
		}
		if(attrib.contains("Air"))
		{
			damage += 8;
		}
		List<String> embers = getEmberEffects(stack);
		for(int i = 0; i < 11; ++i)
		{
			if(embers.contains("Damage +"+i))
				damage += i;
		}
		if(s == EntityEquipmentSlot.MAINHAND)
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", damage, 0));
		return multimap;
	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List, boolean par4) 
	{
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
		if(par1ItemStack.getTagCompound() != null && par1ItemStack.getTagCompound().hasKey("ember_0"))
		{
			List l = getEmberEffects(par1ItemStack);
			par3List.addAll(l);
			par3List.add("Primal Attribute: "+par1ItemStack.getTagCompound().getString("primary"));
			par3List.add("Second Attribute: "+par1ItemStack.getTagCompound().getString("secondary"));
		}
		par3List.add(ECUtils.getStackTag(par1ItemStack).getInteger("mru") + "/" + ECUtils.getStackTag(par1ItemStack).getInteger("maxMRU") + " MRU");
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
	{
		for(int var4 = 0; var4 < 1; ++var4)
		{
			ItemStack min = new ItemStack(par1, 1, 0);
			ECUtils.initMRUTag(min, maxMRU);
			ItemStack max = new ItemStack(par1, 1, 0);
			ECUtils.initMRUTag(max, maxMRU);
			ECUtils.getStackTag(max).setInteger("mru", ECUtils.getStackTag(max).getInteger("maxMRU"));
			par3List.add(min);
			par3List.add(max);
		}
	}

	@Override
	public int getMaxMRU(ItemStack stack) {
		return this.maxMRU;
	}

	public static void setPrimaryAttribute(ItemStack s)
	{
		NBTTagCompound tag = s.getTagCompound();
		if(tag.hasKey("primary"))
		{
			return;
		}else
		{
			if(tag.hasKey("focus_0"))
			{
				String s_0 = tag.getString("focus_0");
				String s_1 = tag.getString("focus_1");
				String s_2 = tag.getString("focus_2");
				String s_3 = tag.getString("focus_3");
				s_0 = s_0.toLowerCase();
				s_1 = s_1.toLowerCase();
				s_2 = s_2.toLowerCase();
				s_3 = s_3.toLowerCase();
				int fire = 0,water = 0,earth = 0,air = 0;
				if(s_0.toLowerCase().contains("ffocus"))
					++fire;
				if(s_1.toLowerCase().contains("ffocus"))
					++fire;
				if(s_2.toLowerCase().contains("ffocus"))
					++fire;
				if(s_3.toLowerCase().contains("ffocus"))
					++fire;
				if(s_0.toLowerCase().contains("wfocus"))
					++water;
				if(s_1.toLowerCase().contains("wfocus"))
					++water;
				if(s_2.toLowerCase().contains("wfocus"))
					++water;
				if(s_3.toLowerCase().contains("wfocus"))
					++water;
				if(s_0.toLowerCase().contains("efocus"))
					++earth;
				if(s_1.toLowerCase().contains("efocus"))
					++earth;
				if(s_2.toLowerCase().contains("efocus"))
					++earth;
				if(s_3.toLowerCase().contains("efocus"))
					++earth;
				if(s_0.toLowerCase().contains("afocus"))
					++air;
				if(s_1.toLowerCase().contains("afocus"))
					++air;
				if(s_2.toLowerCase().contains("afocus"))
					++air;
				if(s_3.toLowerCase().contains("afocus"))
					++air;
				if(fire > water && fire > earth && fire > air)
				{
					tag.setString("primary", "Fire");
				}else
					if(water > fire && water > earth && water > air)
					{
						tag.setString("primary", "Water");
					}else
						if(earth > water && earth > fire && earth > air)
						{
							tag.setString("primary", "Earth");
						}else
							if(air > water && air > earth && air > fire)
							{
								tag.setString("primary", "Air");
							}else
								tag.setString("primary", "Combined");
				List<String> secondaryAttribs = new ArrayList<String>();
				if(fire != 0)
					secondaryAttribs.add("Fire");
				if(water != 0)
					secondaryAttribs.add("Water");
				if(earth != 0)
					secondaryAttribs.add("Earth");
				if(air != 0)
					secondaryAttribs.add("Air");
				if(!secondaryAttribs.isEmpty())
					tag.setString("secondary", secondaryAttribs.get(rand.nextInt(secondaryAttribs.size())));
				else
					tag.setString("secondary",getPrimaryAttribute(s));

			}else
			{
				return;
			}
		}
	}

	public static String getPrimaryAttribute(ItemStack s)
	{
		if(s.hasTagCompound())
			return s.getTagCompound().getString("primary");
		else
			return "combined";	
	}

	public static String getSecondaryAttribute(ItemStack s)
	{
		if(s.hasTagCompound())
			return s.getTagCompound().getString("secondary");
		else
			return "combined";	
	}

	public static String getA(ItemStack s, int pass)
	{
		String a = "";
		if(pass == 0)
			a = getPrimaryAttribute(s);
		else
			a = getSecondaryAttribute(s);
		if(a.contains("Fire"))
			return "fire";
		if(a.contains("Water"))
			return "water";
		if(a.contains("Earth"))
			return "earth";
		if(a.contains("Air"))
			return "air";
		return "normal";
	}

	public static List<String> getEmberEffects(ItemStack par1ItemStack)
	{
		List<String> ret = new ArrayList<String>();
		String allEmbers = par1ItemStack.getTagCompound().getString("ember_0")+" "+par1ItemStack.getTagCompound().getString("ember_1")+" "+par1ItemStack.getTagCompound().getString("ember_2")+" "+par1ItemStack.getTagCompound().getString("ember_3");
		allEmbers = allEmbers.toLowerCase();
		if(allEmbers.contains("acid") && allEmbers.contains("chaos"))
		{
			ret.add("Damage +4");
		}
		if(allEmbers.contains("acid") && allEmbers.contains("common"))
		{
			ret.add("Damage +2");
		}
		if(allEmbers.contains("acid") && allEmbers.contains("corruption"))
		{
			ret.add("Slowness");
		}
		if(allEmbers.contains("acid") && allEmbers.contains("crystal"))
		{
			ret.add("Damage +6");
		}
		if(allEmbers.contains("acid") && allEmbers.contains("divine"))
		{
			ret.add("Poison");
		}
		if(allEmbers.contains("acid") && allEmbers.contains("magic"))
		{
			ret.add("Greater Poison");
		}
		if(allEmbers.contains("acid") && allEmbers.contains("flame"))
		{
			ret.add("Damage +8");
		}
		if(allEmbers.contains("chaos") && allEmbers.contains("common"))
		{
			ret.add("Damage Self");
			ret.add("Damage +7");
		}
		if(allEmbers.contains("chaos") && allEmbers.contains("corruption"))
		{
			ret.add("Greater Slow");
		}
		if(allEmbers.contains("chaos") && allEmbers.contains("crystal"))
		{
			ret.add("Damage +5");
		}
		if(allEmbers.contains("chaos") && allEmbers.contains("divine"))
		{
			ret.add("Lightning");
		}
		if(allEmbers.contains("chaos") && allEmbers.contains("magic"))
		{
			ret.add("Damage +6");
		}
		if(allEmbers.contains("chaos") && allEmbers.contains("flame"))
		{
			ret.add("Damage +4");
		}
		if(allEmbers.contains("common") && allEmbers.contains("corruption"))
		{
			ret.add("Weakness");
		}
		if(allEmbers.contains("common") && allEmbers.contains("crystal"))
		{
			ret.add("Damage +3");
		}
		if(allEmbers.contains("common") && allEmbers.contains("divine"))
		{
			ret.add("Damage Boost");
		}
		if(allEmbers.contains("common") && allEmbers.contains("magic"))
		{
			ret.add("Speed Boost");
		}
		if(allEmbers.contains("common") && allEmbers.contains("flame"))
		{
			ret.add("Damage +11");
		}
		if(allEmbers.contains("corruption") && allEmbers.contains("crystal"))
		{
			ret.add("Lifesteal");
		}
		if(allEmbers.contains("corruption") && allEmbers.contains("divine"))
		{
			ret.add("Damage Self");
			ret.add("Greater Hunger");
		}
		if(allEmbers.contains("corruption") && allEmbers.contains("magic"))
		{
			ret.add("Greater Lifesteal");
		}
		if(allEmbers.contains("corruption") && allEmbers.contains("flame"))
		{
			ret.add("Damage +5");
		}
		if(allEmbers.contains("crystal") && allEmbers.contains("divine"))
		{
			ret.add("Damage +9");
		}
		if(allEmbers.contains("crystal") && allEmbers.contains("magic"))
		{
			ret.add("Damage +7");
		}
		if(allEmbers.contains("crystal") && allEmbers.contains("flame"))
		{
			ret.add("Greater Speed Boost");
		}
		if(allEmbers.contains("magic") && allEmbers.contains("flame"))
		{
			ret.add("Damage +6");
		}
		return ret;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack)
	{
		return EnumAction.BLOCK;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack)
	{
		return 72000;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, EnumHand hand)
	{
		par3EntityPlayer.setActiveHand(hand);;
		String attrib = getSecondaryAttribute(par1ItemStack);
		if(attrib.contains("Fire"))
		{
			if((ECUtils.tryToDecreaseMRUInStorage((EntityPlayer) par3EntityPlayer, -50) || this.setMRU(par1ItemStack, -50)))
			{
				par3EntityPlayer.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE,50,0));
				List<EntityLivingBase> l = par2World.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(par3EntityPlayer.posX-2, par3EntityPlayer.posY-1, par3EntityPlayer.posZ-2, par3EntityPlayer.posX+2, par3EntityPlayer.posY+3, par3EntityPlayer.posZ+2));
				if(!l.isEmpty())
				{
					for(int i = 0; i < l.size(); ++i)
					{
						EntityLivingBase b = l.get(i);
						b.setFire(2);
					}
				}
			}
		}
		if(attrib.contains("Water"))
		{
			if((ECUtils.tryToDecreaseMRUInStorage((EntityPlayer) par3EntityPlayer, -50) || this.setMRU(par1ItemStack, -50)))
			{
				par3EntityPlayer.addPotionEffect(new PotionEffect(MobEffects.REGENERATION,40,0));
			}
		}
		if(attrib.contains("Earth"))
		{
			if((ECUtils.tryToDecreaseMRUInStorage((EntityPlayer) par3EntityPlayer, -50) || this.setMRU(par1ItemStack, -50)))
			{
				par3EntityPlayer.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE,40,0));
			}
		}
		if(attrib.contains("Air"))
		{
			if((ECUtils.tryToDecreaseMRUInStorage((EntityPlayer) par3EntityPlayer, -50) || this.setMRU(par1ItemStack, -50)))
			{
				par3EntityPlayer.addPotionEffect(new PotionEffect(MobEffects.SPEED,30,0));
				par3EntityPlayer.addPotionEffect(new PotionEffect(MobEffects.STRENGTH,30,0));
			}
		}
		return new ActionResult(EnumActionResult.PASS, par1ItemStack);
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomMeshDefinition(this, new MeshDefinitionElementalSword());
		ArrayList<ModelResourceLocation> locations = new ArrayList<ModelResourceLocation>();
		for(int i = 0; i < names.length; i++) {
			for(int j = 0; j < names.length; j++) {
				locations.add(new ModelResourceLocation("essentialcraft:item/elementalSword", "bottom=" + names[i] + "," + "top=" + names[j]));
			}
		}
		ModelBakery.registerItemVariants(this, locations.toArray(new ModelResourceLocation[0]));
	}

	public static class MeshDefinitionElementalSword implements ItemMeshDefinition {
		@Override
		public ModelResourceLocation getModelLocation(ItemStack stack) {
			return new ModelResourceLocation("essentialcraft:item/elementalSword", "bottom=" + getA(stack, 0) + "," + "top=" + getA(stack, 1));
		}
	}
}
