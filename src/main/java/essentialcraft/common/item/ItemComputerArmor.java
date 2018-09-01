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
import essentialcraft.utils.common.ECUtils;
import net.minecraft.client.Minecraft;
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
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.items.IGoggles;
import thaumcraft.api.items.IRevealer;
import thaumcraft.api.items.IVisDiscountGear;

@DCASMCheck
@ExistenceCheck(classPath = {"thaumcraft.api.items.IVisDiscountGear", "thaumcraft.api.items.IRevealer","thaumcraft.api.items.IGoggles"})
public class ItemComputerArmor extends ItemArmor implements IVisDiscountGear, IRevealer, IGoggles, ISpecialArmor, IModelRegisterer {

	public static Capability<IMRUHandlerItem> MRU_HANDLER_ITEM_CAPABILITY = CapabilityMRUHandler.MRU_HANDLER_ITEM_CAPABILITY;
	public int maxMRU = 1000000;

	public ItemComputerArmor(ArmorMaterial material, int texture, int armorType) {
		super(material, texture, EntityEquipmentSlot.values()[5-armorType]);
		mat = material;
	}

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, EntityEquipmentSlot slot, String type) {
		switch(slot) {
		case LEGS: return "essentialcraft:textures/special/armor/computer_layer_2.png"; //2 should be the slot for legs
		default: return "essentialcraft:textures/special/armor/computer_layer_1.png";
		}
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		Multimap<String, AttributeModifier> mods = HashMultimap.<String, AttributeModifier>create();

		if(this == ItemsCore.computer_chestplate && slot == EntityEquipmentSlot.CHEST)
			mods.put(SharedMonsterAttributes.MAX_HEALTH.getName(), new AttributeModifier(UUID.fromString("1bca943c-3cf5-42cc-a3df-2ed994ae0000"), "hp", 40D, 0));

		if(this == ItemsCore.computer_leggings && slot == EntityEquipmentSlot.LEGS)
			mods.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(UUID.fromString("1bca943c-3cf5-42cc-a3df-2ed994ae0001"), "movespeed", 0.15D, 0));
		return mods;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World player, List<String> list, ITooltipFlag par4) {
		super.addInformation(stack, player, list, par4);

		list.add(stack.getCapability(MRU_HANDLER_ITEM_CAPABILITY, null).getMRU() + "/" + stack.getCapability(MRU_HANDLER_ITEM_CAPABILITY, null).getMaxMRU() + " MRU");

		switch(this.armorType) {
		case HEAD: {
			list.add(TextFormatting.DARK_PURPLE+I18n.translateToLocal("essentialcraft.txt.computer_helmet.props"));
			break;
		}
		case CHEST: {
			list.add(TextFormatting.DARK_PURPLE+I18n.translateToLocal("essentialcraft.txt.computer_chestplate.props"));
			break;
		}
		case LEGS: {
			list.add(TextFormatting.DARK_PURPLE+I18n.translateToLocal("essentialcraft.txt.computer_legs.props"));
			break;
		}
		case FEET: {
			list.add(TextFormatting.DARK_PURPLE+I18n.translateToLocal("essentialcraft.txt.computer_boots.props"));
			break;
		}
		default:
			break;
		}

		list.add(" ");

		list.add((hasFullset(Minecraft.getMinecraft().player) ? TextFormatting.GREEN : TextFormatting.RESET)+I18n.translateToLocal("essentialcraft.txt.fullset"));
		list.add(TextFormatting.ITALIC+I18n.translateToLocal("essentialcraft.txt.fullset.computer.props"));
		list.add(TextFormatting.ITALIC+I18n.translateToLocal("essentialcraft.txt.fullset.computer.props_2"));
	}

	public static boolean hasFullset(EntityPlayer p) {
		if(p == null)
			return false;

		for(int i = 0; i < 4; ++i)
			if(!(p.inventory.armorInventory.get(i).getItem() instanceof ItemComputerArmor))
				return false;

		return true;
	}

	@Override
	public void getSubItems(CreativeTabs par2CreativeTabs, NonNullList<ItemStack> list) {
		if(this.isInCreativeTab(par2CreativeTabs)) {
			ItemStack min = new ItemStack(this, 1, 0);
			ItemStack max = new ItemStack(this, 1, 0);
			min.getCapability(MRU_HANDLER_ITEM_CAPABILITY, null).setMRU(0);
			max.getCapability(MRU_HANDLER_ITEM_CAPABILITY, null).setMRU(maxMRU);
			list.add(min);
			list.add(max);
		}
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

	public static int[] discount = {18,25,12,15};

	@Override
	public int getVisDiscount(ItemStack stack, EntityPlayer player) {
		EntityEquipmentSlot type = ((ItemArmor)stack.getItem()).armorType;
		return discount[5-type.ordinal()];
	}

	@Override
	public ArmorProperties getProperties(EntityLivingBase player,ItemStack armor, DamageSource source, double damage, int slot) {
		if(armor.getItem() == ItemsCore.computer_chestplate && player instanceof EntityPlayer) {
			boolean hasFullSet = true;
			EntityPlayer p = (EntityPlayer) player;

			for(int i = 0; i < 4; ++i) {
				if(!(p.inventory.armorInventory.get(i).getItem() instanceof ItemComputerArmor)) {
					hasFullSet = false;
					break;
				}
			}

			if(source.getTrueSource() != null && hasFullSet) {
				float newDamage = (float) (damage/2);
				if(newDamage<0.5D)
					newDamage = 0;

				ECUtils.playSoundToAllNearby(player.posX, player.posY, player.posZ, "essentialcraft:sound.lightning_hit", 0.2F, 1F, 8, player.dimension);
				source.getTrueSource().attackEntityFrom(DamageSource.causeThornsDamage(player), newDamage);
			}
		}
		int mru = armor.getCapability(MRU_HANDLER_ITEM_CAPABILITY, null).getMRU();
		if(mru > 0) {
			ItemArmor aarmor = (ItemArmor)armor.getItem();
			return new ArmorProperties(0, aarmor.damageReduceAmount/10D, Integer.MAX_VALUE);
		}
		else
			return new ArmorProperties(0, 0, 1);
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		return mat.getDamageReductionAmount(armorType);
	}

	public ArmorMaterial mat;

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
		if(entity instanceof EntityPlayer) {
			EntityPlayer p = (EntityPlayer) entity;
			if(ECUtils.playerUseMRU(p, stack, damage*250)) {}
			else {}
		}
	}

	public String textureName = "";

	public ItemComputerArmor setTextureName(String name) {
		textureName = name;
		return this;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new MRUItemStorage(stack, maxMRU);
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/" + getRegistryName().getResourcePath(), "inventory"));
	}
}
