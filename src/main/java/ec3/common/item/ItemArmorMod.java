package ec3.common.item;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.DCASMCheck;
import DummyCore.Utils.MiscUtils;/*
import thaumcraft.api.IGoggles;
import thaumcraft.api.IRepairable;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.nodes.IRevealer;*/
import ec3.api.IItemRequiresMRU;
import ec3.common.mod.EssentialCraftCore;
import ec3.utils.common.ECUtils;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@DCASMCheck
//@ExistenceCheck(classPath = {"thaumcraft.api.IRepairable","thaumcraft.api.IVisDiscountGear","thaumcraft.api.nodes.IRevealer","thaumcraft.api.IGoggles"})
public class ItemArmorMod extends ItemArmor implements /*IRepairable, IVisDiscountGear, IRevealer, IGoggles,*/ ISpecialArmor, IItemRequiresMRU, IModelRegisterer {
	public String armorTexture = "";
	public String desc = "";
	public int aType;
	public ArmorMaterial mat;
	public ItemArmorMod(ArmorMaterial p_i45325_1_, int p_i45325_2_, int p_i45325_3_, int it) {
		super(p_i45325_1_, p_i45325_2_, EntityEquipmentSlot.values()[5-p_i45325_3_]);
		aType = it;
		mat = p_i45325_1_;
	}

	public ItemArmorMod setArmorTexture(String path)
	{
		armorTexture = path;
		return this;
	}

	public ItemArmorMod setDescription(String desc) {
		this.desc = desc;
		return this;
	}

	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean par4)
	{
		super.addInformation(stack, player, list, par4);
		//list.add((new StringBuilder()).append(TextFormatting.DARK_PURPLE).append(I18n.translateToLocal("tc.visdiscount")).append(": ").append(getVisDiscount(stack, player, null)).append("%").toString());
		if(!desc.isEmpty())
			list.add(desc);
		if(this.aType == 1)
		{
			list.add(this.getMRU(stack) + "/" + this.getMaxMRU(stack) + " MRU");
		}	
	}

	public Multimap<String,AttributeModifier> getAttributeModifiers(EntityEquipmentSlot s, ItemStack stack)
	{
		Multimap<String,AttributeModifier> mods = HashMultimap.<String,AttributeModifier>create();

		if(this == ItemsCore.magicArmorItems[5] && s == EntityEquipmentSlot.CHEST)
			mods.put(SharedMonsterAttributes.MAX_HEALTH.getName(), new AttributeModifier(UUID.fromString("1bca943c-3cf5-42cc-a3df-2ed994ae0000"), "hp", 20D, 0));

		if(this == ItemsCore.magicArmorItems[7] && s == EntityEquipmentSlot.FEET)
			mods.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(UUID.fromString("1bca943c-3cf5-42cc-a3df-2ed994ae0000"), "movespeed", 0.075D, 0));

		if(this == ItemsCore.magicArmorItems[9] && s == EntityEquipmentSlot.CHEST)
			mods.put(SharedMonsterAttributes.MAX_HEALTH.getName(), new AttributeModifier(UUID.fromString("1bca943c-3cf5-42cc-a3df-2ed994ae0000"), "hp", 30D, 0));

		if(this == ItemsCore.magicArmorItems[11] && s == EntityEquipmentSlot.FEET)
			mods.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(UUID.fromString("1bca943c-3cf5-42cc-a3df-2ed994ae0000"), "movespeed", 0.1D, 0));

		return mods;
	}

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, EntityEquipmentSlot slot, String type) { 
		switch(slot) { 
		case LEGS: return "essentialcraft:textures/special/armor/"+armorTexture+"_1.png"; //2 should be the slot for legs
		default: return "essentialcraft:textures/special/armor/"+armorTexture+"_0.png"; 
		}
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
	{
		if(this.aType != 1)
			super.getSubItems(par1, par2CreativeTabs, par3List);
		else
			for (int var4 = 0; var4 < 1; ++var4)
			{
				ItemStack min = new ItemStack(par1, 1, 0);
				ECUtils.initMRUTag(min, 5000);
				ItemStack max = new ItemStack(par1, 1, 0);
				ECUtils.initMRUTag(max, 5000);
				ECUtils.getStackTag(max).setInteger("mru", 5000);
				par3List.add(min);
				par3List.add(max);
			}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default)  {
		ModelBiped armorModel = null;
		if(itemStack != null) { 
			if(itemStack.getItem() instanceof ItemArmorMod) { 
				GlStateManager.enableBlend();
				OpenGlHelper.glBlendFunc(770, 771, 1, 0);
				EntityEquipmentSlot type = ((ItemArmor)itemStack.getItem()).armorType;
				if(type == EntityEquipmentSlot.LEGS) {
					armorModel = EssentialCraftCore.proxy.getClientModel(0);
				}
				else if(type == EntityEquipmentSlot.CHEST) {
					armorModel = EssentialCraftCore.proxy.getClientModel(2);
				}
				else { 
					armorModel = EssentialCraftCore.proxy.getClientModel(1); 
				}
			}
			if(armorModel != null) {
				armorModel.bipedHead.showModel = armorSlot == EntityEquipmentSlot.HEAD;
				armorModel.bipedHeadwear.showModel = armorSlot == EntityEquipmentSlot.HEAD;
				armorModel.bipedBody.showModel = armorSlot == EntityEquipmentSlot.CHEST || armorSlot == EntityEquipmentSlot.LEGS;
				armorModel.bipedRightArm.showModel = armorSlot == EntityEquipmentSlot.CHEST;
				armorModel.bipedLeftArm.showModel = armorSlot == EntityEquipmentSlot.CHEST;
				armorModel.bipedRightLeg.showModel = armorSlot == EntityEquipmentSlot.LEGS || armorSlot == EntityEquipmentSlot.FEET; 
				armorModel.bipedLeftLeg.showModel = armorSlot == EntityEquipmentSlot.LEGS || armorSlot == EntityEquipmentSlot.FEET;
				armorModel.isSneak = entityLiving.isSneaking(); 
				armorModel.isRiding = entityLiving.isRiding(); 
				armorModel.isChild = entityLiving.isChild();
				armorModel.rightArmPose = entityLiving.getHeldItem(EnumHand.MAIN_HAND) != null ? ModelBiped.ArmPose.ITEM : ModelBiped.ArmPose.EMPTY; 
				armorModel.leftArmPose = entityLiving.getHeldItem(EnumHand.OFF_HAND) != null ? ModelBiped.ArmPose.ITEM : ModelBiped.ArmPose.EMPTY; 
				if(entityLiving instanceof EntityPlayer) { 
					armorModel.rightArmPose =((EntityPlayer)entityLiving).getItemInUseCount() > 2 ? ModelBiped.ArmPose.BOW_AND_ARROW : ModelBiped.ArmPose.EMPTY;
				}
			}
		}
		return armorModel;
	}

	/*
	@Override
	public boolean showIngamePopups(ItemStack itemstack, EntityLivingBase player) {
		int type = ((ItemArmor)itemstack.getItem()).armorType;
		return type == 0;
	}

	@Override
	public boolean showNodes(ItemStack itemstack, EntityLivingBase player) {
		int type = ((ItemArmor)itemstack.getItem()).armorType;
		return type == 0;
	}

	@Override
	public int getVisDiscount(ItemStack stack, EntityPlayer player,
			Aspect aspect) {
		int type = ((ItemArmor)stack.getItem()).armorType;

		return discount[aType][type];
	}

	public static int[][] discount = new int[][]{{5,5,3,2},{8,10,7,5},{10,15,8,7},{2,3,2,1}};
	 */

	@Override
	public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
		if(this.aType != 1) {
			if(!source.isUnblockable()) {
				ItemArmor aarmor = (ItemArmor)armor.getItem();
				return new ArmorProperties(0, aarmor.damageReduceAmount / 25D, aarmor.getMaxDamage() + 1 - armor.getItemDamage());
			}
			else
				return new ArmorProperties(0,0,armor.getMaxDamage() + 1 - armor.getItemDamage());
		}
		else {
			int mru = getMRU(armor);
			if(mru > 0) {
				ItemArmor aarmor = (ItemArmor)armor.getItem();
				return new ArmorProperties(0, aarmor.damageReduceAmount / 20D, aarmor.getMaxDamage() + 1 - armor.getItemDamage());
			}
			else
				return new ArmorProperties(0,0,armor.getMaxDamage() + 1 - armor.getItemDamage());
		}
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		return mat.getDamageReductionAmount(armorType);
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
		if(this.aType != 1) {
			stack.damageItem(damage, entity);
		}
		else {
			if(entity instanceof EntityPlayer) {
				EntityPlayer p = (EntityPlayer) entity;
				if(ECUtils.tryToDecreaseMRUInStorage(p, -damage*800) || this.setMRU(stack, -damage*800)) {}
				else {}
			}
		}
	}

	@Override
	public boolean setMRU(ItemStack stack, int amount) {
		if(getMRU(stack)+amount >= 0 && getMRU(stack)+amount<=getMaxMRU(stack)) {
			MiscUtils.getStackTag(stack).setInteger("mru", MiscUtils.getStackTag(stack).getInteger("mru")+amount);
			return true;
		}
		return false;
	}

	@Override
	public int getMRU(ItemStack stack) {
		return MiscUtils.getStackTag(stack).getInteger("mru");
	}

	@Override
	public int getMaxMRU(ItemStack stack) {
		return this.aType == 1 ? 5000 : 1;
	}

	@Override
	public void registerModels() {
		if(!Loader.isModLoaded("codechickenlib") && !Loader.isModLoaded("CodeChickenLib"))
			ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/" + getRegistryName().getResourcePath(), "inventory"));
		else
			ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:armor", "inventory"));
	}
}
