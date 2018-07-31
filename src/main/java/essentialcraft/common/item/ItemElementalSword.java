package essentialcraft.common.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.base.Predicates;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import DummyCore.Client.IModelRegisterer;
import essentialcraft.api.IMRUHandlerItem;
import essentialcraft.common.capabilities.mru.CapabilityMRUHandler;
import essentialcraft.common.capabilities.mru.MRUItemStorage;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemElementalSword extends ItemSword implements IModelRegisterer {

	public static String[] names = {"fire", "water", "earth", "air", "normal"};
	public static Capability<IMRUHandlerItem> MRU_HANDLER_ITEM_CAPABILITY = CapabilityMRUHandler.MRU_HANDLER_ITEM_CAPABILITY;
	public static int maxMRU = 5000;
	static Random rand = new Random(8192L);

	public ItemElementalSword() {
		super(ItemsCore.elemental);
		this.maxStackSize = 1;
		this.bFull3D = true;
		this.setMaxDamage(0);
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		if(attacker instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)attacker;
			String attrib = getPrimaryAttribute(stack);
			if(attrib.contains("Fire") ) {
				if(ECUtils.playerUseMRU(player, stack, 50)) {
					target.setFire(5);
				}
			}
			if(attrib.contains("Water")) {
				if(ECUtils.playerUseMRU(player, stack, 50)) {
					target.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS,40,0));
					target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS,40,0));
				}
			}
			if(attrib.contains("Earth")) {
				if(ECUtils.playerUseMRU(player, stack, 50)) {
					target.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS,40,0));
					target.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE,40,0));
				}
			}
			if(attrib.contains("Air")) {
				if(ECUtils.playerUseMRU(player, stack, 50)) {
					attacker.addPotionEffect(new PotionEffect(MobEffects.SPEED,50,0));
					attacker.addPotionEffect(new PotionEffect(MobEffects.STRENGTH,50,0));
				}
			}
			List<String> embers = getEmberEffects(stack);
			if(ECUtils.playerUseMRU(player, stack, 50)) {
				if(embers.contains("Slowness")) {
					target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS,60,0));
				}
				if(embers.contains("Greater Slowness")) {
					target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS,60,1));
				}
				if(embers.contains("Poison")) {
					target.addPotionEffect(new PotionEffect(MobEffects.POISON,60,0));
				}
				if(embers.contains("Greater Poison")) {
					target.addPotionEffect(new PotionEffect(MobEffects.POISON,60,1));
				}
				if(embers.contains("Damage Self")) {
					attacker.attackEntityFrom(DamageSource.causeMobDamage(attacker),2);
				}
				if(embers.contains("Lightning")) {
					EntityLightningBolt bold = new EntityLightningBolt(target.getEntityWorld(), target.posX, target.posY, target.posZ, false);
					target.getEntityWorld().addWeatherEffect(bold);
					if(!target.getEntityWorld().isRemote) {
						target.getEntityWorld().spawnEntity(bold);
					}
					attacker.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE,60,1));
				}
				if(embers.contains("Lifesteal")) {
					attacker.heal(1);
				}
				if(embers.contains("Greater Lifesteal")) {
					attacker.heal(3);
				}
				if(embers.contains("Weakness")) {
					target.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS,60,0));
				}
				if(embers.contains("Greater Weakness")) {
					target.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS,60,1));
				}
				if(embers.contains("Greater Damage Boost")) {
					attacker.addPotionEffect(new PotionEffect(MobEffects.STRENGTH,60,1));
				}
				if(embers.contains("Damage Boost")) {
					attacker.addPotionEffect(new PotionEffect(MobEffects.STRENGTH,60,0));
				}
				if(embers.contains("Greater Speed Boost")) {
					attacker.addPotionEffect(new PotionEffect(MobEffects.SPEED,60,1));
				}
				if(embers.contains("Speed Boost")) {
					attacker.addPotionEffect(new PotionEffect(MobEffects.SPEED,60,0));
				}
				if(embers.contains("Greater Hunger")) {
					target.addPotionEffect(new PotionEffect(MobEffects.HUNGER,60,1));
				}
			}
		}
		//target.attackEntityFrom(DamageSource.causeMobDamage(attacker),damage);
		return false;
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot s, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();
		String attrib = getPrimaryAttribute(stack);
		int damage = 0;
		if(attrib.contains("Fire")) {
			damage += 7;
		}
		if(attrib.contains("Water")) {
			damage += 3;
		}
		if(attrib.contains("Earth")) {
			damage += 8;
		}
		if(attrib.contains("Air")) {
			damage += 8;
		}
		if(attrib.contains("Combined")) {
			damage += 5;
		}
		List<String> embers = getEmberEffects(stack);
		for(int i = 0; i < 11; ++i) {
			if(embers.contains("Damage +"+i)) {
				damage += i;
			}
		}
		if(s == EntityEquipmentSlot.MAINHAND) {
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", damage, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4D, 0));
		}
		return multimap;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World player, List<String> list, ITooltipFlag tooltipFlag) {
		super.addInformation(stack, player, list, tooltipFlag);
		if(stack.getTagCompound() != null && stack.getTagCompound().hasKey("ember_0")) {
			list.add("Primal: "+stack.getTagCompound().getString("primary"));
			list.add("Second: "+stack.getTagCompound().getString("secondary"));
			List<String> l = getEmberEffects(stack);
			list.addAll(l);
		}
		list.add(stack.getCapability(MRU_HANDLER_ITEM_CAPABILITY, null).getMRU() + "/" + stack.getCapability(MRU_HANDLER_ITEM_CAPABILITY, null).getMaxMRU() + " MRU");
	}

	@Override
	public void getSubItems(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
		if(this.isInCreativeTab(creativeTabs)) {
			ItemStack min = new ItemStack(this, 1, 0);
			ItemStack max = new ItemStack(this, 1, 0);
			min.getCapability(MRU_HANDLER_ITEM_CAPABILITY, null).setMRU(0);
			max.getCapability(MRU_HANDLER_ITEM_CAPABILITY, null).setMRU(maxMRU);
			list.add(min);
			list.add(max);
		}
	}

	public static void setPrimaryAttribute(ItemStack s) {
		NBTTagCompound tag = s.getTagCompound();
		if(tag.hasKey("primary")) {
			return;
		}
		else if(tag.hasKey("focus_0")) {
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
			if(fire > water && fire > earth && fire > air) {
				tag.setString("primary", "Fire");
			}
			else if(water > fire && water > earth && water > air) {
				tag.setString("primary", "Water");
			}
			else if(earth > water && earth > fire && earth > air) {
				tag.setString("primary", "Earth");
			}
			else if(air > water && air > earth && air > fire) {
				tag.setString("primary", "Air");
			}
			else {
				tag.setString("primary", "Combined");
			}
			List<String> secondaryAttribs = new ArrayList<String>();
			if(fire != 0) {
				secondaryAttribs.add("Fire");
			}
			if(water != 0) {
				secondaryAttribs.add("Water");
			}
			if(earth != 0) {
				secondaryAttribs.add("Earth");
			}
			if(air != 0) {
				secondaryAttribs.add("Air");
			}
			if(!secondaryAttribs.isEmpty()) {
				tag.setString("secondary", secondaryAttribs.get(rand.nextInt(secondaryAttribs.size())));
			}
			else {
				tag.setString("secondary", getPrimaryAttribute(s));
			}
		}
		else {
			return;
		}
	}

	public static String getPrimaryAttribute(ItemStack s) {
		if(s.hasTagCompound())
			return s.getTagCompound().getString("primary");
		else
			return "combined";
	}

	public static String getSecondaryAttribute(ItemStack s) {
		if(s.hasTagCompound())
			return s.getTagCompound().getString("secondary");
		else
			return "combined";
	}

	public static String getA(ItemStack s, int pass) {
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

	public static List<String> getEmberEffects(ItemStack stack) {
		List<String> ret = new ArrayList<String>();
		if(stack.hasTagCompound()) {
			String allEmbers = stack.getTagCompound().getString("ember_0")+" "+stack.getTagCompound().getString("ember_1")+" "+stack.getTagCompound().getString("ember_2")+" "+stack.getTagCompound().getString("ember_3");
			allEmbers = allEmbers.toLowerCase();
			if(allEmbers.contains("acid") && allEmbers.contains("chaos")) {
				ret.add("Damage +4");
			}
			if(allEmbers.contains("acid") && allEmbers.contains("common")) {
				ret.add("Damage +2");
			}
			if(allEmbers.contains("acid") && allEmbers.contains("corruption")) {
				ret.add("Slowness");
			}
			if(allEmbers.contains("acid") && allEmbers.contains("crystal")) {
				ret.add("Damage +6");
			}
			if(allEmbers.contains("acid") && allEmbers.contains("divine")) {
				ret.add("Poison");
			}
			if(allEmbers.contains("acid") && allEmbers.contains("magic")) {
				ret.add("Greater Poison");
			}
			if(allEmbers.contains("acid") && allEmbers.contains("flame")) {
				ret.add("Damage +8");
			}
			if(allEmbers.contains("chaos") && allEmbers.contains("common")) {
				ret.add("Damage Self");
				ret.add("Damage +7");
			}
			if(allEmbers.contains("chaos") && allEmbers.contains("corruption")) {
				ret.add("Greater Slow");
			}
			if(allEmbers.contains("chaos") && allEmbers.contains("crystal")) {
				ret.add("Damage +5");
			}
			if(allEmbers.contains("chaos") && allEmbers.contains("divine")) {
				ret.add("Lightning");
			}
			if(allEmbers.contains("chaos") && allEmbers.contains("magic")) {
				ret.add("Damage +6");
			}
			if(allEmbers.contains("chaos") && allEmbers.contains("flame")) {
				ret.add("Damage +4");
			}
			if(allEmbers.contains("common") && allEmbers.contains("corruption")) {
				ret.add("Weakness");
			}
			if(allEmbers.contains("common") && allEmbers.contains("crystal")) {
				ret.add("Damage +3");
			}
			if(allEmbers.contains("common") && allEmbers.contains("divine")) {
				ret.add("Damage Boost");
			}
			if(allEmbers.contains("common") && allEmbers.contains("magic")) {
				ret.add("Speed Boost");
			}
			if(allEmbers.contains("common") && allEmbers.contains("flame")) {
				ret.add("Damage +11");
			}
			if(allEmbers.contains("corruption") && allEmbers.contains("crystal")) {
				ret.add("Lifesteal");
			}
			if(allEmbers.contains("corruption") && allEmbers.contains("divine")) {
				ret.add("Damage Self");
				ret.add("Greater Hunger");
			}
			if(allEmbers.contains("corruption") && allEmbers.contains("magic")) {
				ret.add("Greater Lifesteal");
			}
			if(allEmbers.contains("corruption") && allEmbers.contains("flame")) {
				ret.add("Damage +5");
			}
			if(allEmbers.contains("crystal") && allEmbers.contains("divine")) {
				ret.add("Damage +9");
			}
			if(allEmbers.contains("crystal") && allEmbers.contains("magic")) {
				ret.add("Damage +7");
			}
			if(allEmbers.contains("crystal") && allEmbers.contains("flame")) {
				ret.add("Greater Speed Boost");
			}
			if(allEmbers.contains("magic") && allEmbers.contains("flame")) {
				ret.add("Damage +6");
			}
		}
		return ret;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BLOCK;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		player.setActiveHand(hand);
		String attrib = getSecondaryAttribute(stack);
		if(attrib.contains("Fire")) {
			if(ECUtils.playerUseMRU(player, stack, 50)) {
				player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 50, 0));
				List<EntityLivingBase> l = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(player.posX-2, player.posY-1, player.posZ-2, player.posX+2, player.posY+3, player.posZ+2), Predicates.and(EntitySelectors.NOT_SPECTATING, e->e != player));
				if(!l.isEmpty()) {
					for(int i = 0; i < l.size(); ++i) {
						EntityLivingBase b = l.get(i);
						b.setFire(2);
					}
				}
			}
		}
		if(attrib.contains("Water")) {
			if(ECUtils.playerUseMRU(player, stack, 50)) {
				player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 40, 0));
			}
		}
		if(attrib.contains("Earth")) {
			if(ECUtils.playerUseMRU(player, stack, 50)) {
				player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 40, 0));
			}
		}
		if(attrib.contains("Air")) {
			if(ECUtils.playerUseMRU(player, stack, 50)) {
				player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 30, 0));
				player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 30, 0));
			}
		}
		return super.onItemRightClick(world, player, hand);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new MRUItemStorage(stack, maxMRU);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelLoader.setCustomMeshDefinition(this, new MeshDefinitionElementalSword());
		ArrayList<ModelResourceLocation> locations = new ArrayList<ModelResourceLocation>();
		for(int i = 0; i < names.length; i++) {
			for(int j = 0; j < names.length; j++) {
				locations.add(new ModelResourceLocation("essentialcraft:item/elementalsword", "bottom=" + names[i] + "," + "top=" + names[j]));
			}
		}
		ModelBakery.registerItemVariants(this, locations.toArray(new ModelResourceLocation[0]));
	}

	public static class MeshDefinitionElementalSword implements ItemMeshDefinition {
		@Override
		public ModelResourceLocation getModelLocation(ItemStack stack) {
			return new ModelResourceLocation("essentialcraft:item/elementalsword", "bottom=" + getA(stack, 0) + "," + "top=" + getA(stack, 1));
		}
	}
}
