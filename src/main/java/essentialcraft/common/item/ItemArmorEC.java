package essentialcraft.common.item;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.DCASMCheck;
import DummyCore.Utils.ExistenceCheck;
import essentialcraft.api.IMRUHandlerItem;
import essentialcraft.common.capabilities.mru.CapabilityMRUHandler;
import essentialcraft.common.capabilities.mru.MRUItemStorage;
import essentialcraft.common.mod.EssentialCraftCore;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.items.IGoggles;
import thaumcraft.api.items.IRevealer;
import thaumcraft.api.items.IVisDiscountGear;

@DCASMCheck
@ExistenceCheck(classPath = {"thaumcraft.api.items.IVisDiscountGear", "thaumcraft.api.items.IRevealer", "thaumcraft.api.items.IGoggles"})
public class ItemArmorEC extends ItemArmor implements IVisDiscountGear, IRevealer, IGoggles, ISpecialArmor, IModelRegisterer {
	public static Capability<IMRUHandlerItem> MRU_HANDLER_ITEM_CAPABILITY = CapabilityMRUHandler.MRU_HANDLER_ITEM_CAPABILITY;

	public String armorTexture = "";
	public String desc = "";
	public int aType;
	public ArmorMaterial mat;
	public int maxMRU = 5000;
	public ItemArmorEC(ArmorMaterial p_i45325_1_, int p_i45325_2_, int p_i45325_3_, int it) {
		super(p_i45325_1_, p_i45325_2_, EntityEquipmentSlot.values()[5-p_i45325_3_]);
		aType = it;
		mat = p_i45325_1_;
	}

	public ItemArmorEC setArmorTexture(String path)
	{
		armorTexture = path;
		return this;
	}

	public ItemArmorEC setDescription(String desc) {
		this.desc = desc;
		return this;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag par4)
	{
		super.addInformation(stack, world, list, par4);
		if(!desc.isEmpty())
			list.add(desc);
		if(this.aType == 1)
		{
			list.add(stack.getCapability(MRU_HANDLER_ITEM_CAPABILITY, null).getMRU() + "/" + stack.getCapability(MRU_HANDLER_ITEM_CAPABILITY, null).getMaxMRU() + " MRU");
		}
	}

	@Override
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
	public void getSubItems(CreativeTabs par2CreativeTabs, NonNullList<ItemStack> list) {
		if(this.aType != 1)
			super.getSubItems(par2CreativeTabs, list);
		else if(this.isInCreativeTab(par2CreativeTabs)) {
			ItemStack min = new ItemStack(this, 1, 0);
			ItemStack max = new ItemStack(this, 1, 0);
			min.getCapability(MRU_HANDLER_ITEM_CAPABILITY, null).setMRU(0);
			max.getCapability(MRU_HANDLER_ITEM_CAPABILITY, null).setMRU(maxMRU);
			list.add(min);
			list.add(max);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default)  {
		ModelBiped armorModel = null;
		if(!itemStack.isEmpty()) {
			if(itemStack.getItem() instanceof ItemArmorEC) {
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
				armorModel.rightArmPose = !entityLiving.getHeldItem(EnumHand.MAIN_HAND).isEmpty() ? ModelBiped.ArmPose.ITEM : ModelBiped.ArmPose.EMPTY;
				armorModel.leftArmPose = !entityLiving.getHeldItem(EnumHand.OFF_HAND).isEmpty() ? ModelBiped.ArmPose.ITEM : ModelBiped.ArmPose.EMPTY;
				if(entityLiving instanceof EntityPlayer) {
					armorModel.rightArmPose =((EntityPlayer)entityLiving).getItemInUseCount() > 2 ? ModelBiped.ArmPose.BOW_AND_ARROW : ModelBiped.ArmPose.EMPTY;
				}
			}
		}
		return armorModel;
	}

	@Override
	public boolean showIngamePopups(ItemStack itemstack, EntityLivingBase player) {
		EntityEquipmentSlot type = ((ItemArmor)itemstack.getItem()).armorType;
		return type == EntityEquipmentSlot.HEAD;
	}

	@Override
	public boolean showNodes(ItemStack itemstack, EntityLivingBase player) {
		EntityEquipmentSlot type = ((ItemArmor)itemstack.getItem()).armorType;
		return type == EntityEquipmentSlot.HEAD;
	}

	@Override
	public int getVisDiscount(ItemStack stack, EntityPlayer player) {
		EntityEquipmentSlot type = ((ItemArmor)stack.getItem()).armorType;

		return discount[aType][5-type.ordinal()];
	}

	public static int[][] discount = {{5,5,3,2},{8,10,7,5},{10,15,8,7},{2,3,2,1}};

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
			int mru = armor.getCapability(MRU_HANDLER_ITEM_CAPABILITY, null).getMRU();
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
		if(this.aType == 1 && entity instanceof EntityPlayer) {
			EntityPlayer p = (EntityPlayer) entity;
			if(ECUtils.playerUseMRU(p, stack, damage*800)) {}
			else {}
		}
		else {
			stack.damageItem(damage, entity);
		}
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		if(this.aType != 1) {
			return super.initCapabilities(stack, nbt);
		}
		return new MRUItemStorage(stack, maxMRU);
	}

	@Override
	public void registerModels() {
		if(!Loader.isModLoaded("codechickenlib"))
			ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/" + getRegistryName().getResourcePath(), "inventory"));
		else
			ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:armor", "inventory"));
	}
}
