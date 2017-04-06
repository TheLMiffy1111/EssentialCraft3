package ec3.common.item;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.DCASMCheck;
import DummyCore.Utils.MiscUtils;
/*import thaumcraft.api.IGoggles;
import thaumcraft.api.IRepairable;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.nodes.IRevealer;*/
import ec3.api.IItemRequiresMRU;
import ec3.utils.common.ECUtils;
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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ISpecialArmor;

@DCASMCheck
//@ExistenceCheck(classPath = {"thaumcraft.api.IRepairable","thaumcraft.api.IVisDiscountGear","thaumcraft.api.nodes.IRevealer","thaumcraft.api.IGoggles"})
public class ItemComputerArmor extends ItemArmor implements /*IRepairable, IVisDiscountGear, IRevealer, IGoggles, */ISpecialArmor, IItemRequiresMRU, IModelRegisterer {
	public ItemComputerArmor(ArmorMaterial material, int texture, int armorType) {
		super(material, texture, EntityEquipmentSlot.values()[5-armorType]);
		mat = material;
	}

	@Override 
	public String getArmorTexture(ItemStack itemstack, Entity entity, EntityEquipmentSlot slot, String type)
	{ 
		switch(slot)
		{ 
		case LEGS: return "essentialcraft:textures/special/armor/computer_layer_2.png"; //2 should be the slot for legs
		default: return "essentialcraft:textures/special/armor/computer_layer_1.png"; 
		}
	}

	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
	{
		Multimap<String, AttributeModifier> mods = HashMultimap.<String, AttributeModifier>create();

		if(this == ItemsCore.computer_chestplate && slot == EntityEquipmentSlot.CHEST)
			mods.put(SharedMonsterAttributes.MAX_HEALTH.getName(), new AttributeModifier(UUID.fromString("1bca943c-3cf5-42cc-a3df-2ed994ae0000"), "hp", 40D, 0));

		if(this == ItemsCore.computer_leggings && slot == EntityEquipmentSlot.LEGS)
			mods.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(UUID.fromString("1bca943c-3cf5-42cc-a3df-2ed994ae0001"), "movespeed", 0.15D, 0));
		return mods;
	}

	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean par4)
	{
		super.addInformation(stack, player, list, par4);
		/*if(EssentialCraftCore.clazzExists("thaumcraft.api.IVisDiscountGear"))
	    	list.add((new StringBuilder()).append(TextFormatting.DARK_PURPLE).append(I18n.translateToLocal("tc.visdiscount")).append(": ").append(getVisDiscount(stack, player, null)).append("%").toString());*/

		list.add(this.getMRU(stack) + "/" + this.getMaxMRU(stack) + " MRU");

		switch(this.armorType)
		{
		case HEAD:
		{
			list.add(TextFormatting.DARK_PURPLE+I18n.translateToLocal("ec3.txt.computer_helmet.props"));
			break;
		}
		case CHEST:
		{
			list.add(TextFormatting.DARK_PURPLE+I18n.translateToLocal("ec3.txt.computer_chestplate.props"));
			break;
		}
		case LEGS:
		{
			list.add(TextFormatting.DARK_PURPLE+I18n.translateToLocal("ec3.txt.computer_legs.props"));
			break;
		}
		case FEET:
		{
			list.add(TextFormatting.DARK_PURPLE+I18n.translateToLocal("ec3.txt.computer_boots.props"));
			break;
		}
		default:
			break;
		}

		list.add(" ");

		list.add((hasFullset(player) ? TextFormatting.GREEN : TextFormatting.RESET)+I18n.translateToLocal("ec3.txt.fullset"));
		list.add(TextFormatting.ITALIC+I18n.translateToLocal("ec3.txt.fullset.computer.props"));
		list.add(TextFormatting.ITALIC+I18n.translateToLocal("ec3.txt.fullset.computer.props_2"));
	}

	public static boolean hasFullset(EntityPlayer p)
	{
		if(p == null)
			return false;

		for(int i = 0; i < 4; ++i)
			if(p.inventory.armorInventory[i] == null || !(p.inventory.armorInventory[i].getItem() instanceof ItemComputerArmor))
				return false;

		return true;
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
	{
		for (int var4 = 0; var4 < 1; ++var4)
		{
			ItemStack min = new ItemStack(par1, 1, 0);
			ECUtils.initMRUTag(min, 1000000);
			ItemStack max = new ItemStack(par1, 1, 0);
			ECUtils.initMRUTag(max, 1000000);
			ECUtils.getStackTag(max).setInteger("mru", 1000000);
			par3List.add(min);
			par3List.add(max);
		}
	}

	/*public boolean showIngamePopups(ItemStack itemstack, EntityLivingBase player) {
		int type = ((ItemArmor)itemstack.getItem()).armorType;
		return type == 0;
	}

	public boolean showNodes(ItemStack itemstack, EntityLivingBase player) {
		int type = ((ItemArmor)itemstack.getItem()).armorType;
		return type == 0;
	}

	public static int[] discount = new int[]{18,25,12,15};

	public int getVisDiscount(ItemStack stack, EntityPlayer player,
			Aspect aspect) {
		int type = ((ItemArmor)stack.getItem()).armorType;

		return discount[type];
	}*/

	@Override
	public ArmorProperties getProperties(EntityLivingBase player,ItemStack armor, DamageSource source, double damage, int slot) 
	{
		if(armor.getItem() == ItemsCore.computer_chestplate && player instanceof EntityPlayer)
		{
			boolean hasFullSet = true;
			EntityPlayer p = (EntityPlayer) player;

			for(int i = 0; i < 4; ++i)
			{
				if(p.inventory.armorInventory[i] == null || !(p.inventory.armorInventory[i].getItem() instanceof ItemComputerArmor))
				{
					hasFullSet = false;
					break;
				}
			}

			if(source.getSourceOfDamage() != null && hasFullSet)
			{
				float newDamage = (float) (damage/2);
				if(newDamage<0.5D)
					newDamage = 0;

				ECUtils.playSoundToAllNearby(player.posX, player.posY, player.posZ, "essentialcraft:sound.lightning_hit", 0.2F, 1F, 8, player.dimension);
				source.getSourceOfDamage().attackEntityFrom(DamageSource.causeThornsDamage(player), newDamage);
			}
		}
		int mru = getMRU(armor);
		if(mru > 0)
		{
			ItemArmor aarmor = (ItemArmor)armor.getItem();
			return new ArmorProperties(0, aarmor.damageReduceAmount/10D, Integer.MAX_VALUE);
		}else
			return new ArmorProperties(0,0,1);
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		return mat.getDamageReductionAmount(armorType);
	}

	public ArmorMaterial mat;

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack,
			DamageSource source, int damage, int slot) {

		if(entity instanceof EntityPlayer)
		{
			EntityPlayer p = (EntityPlayer) entity;
			if(ECUtils.tryToDecreaseMRUInStorage(p, -damage*250) || this.setMRU(stack, -damage*250)) {}
			else {}
		}
	}

	@Override
	public boolean setMRU(ItemStack stack, int amount) {
		if(getMRU(stack)+amount >= 0 && getMRU(stack)+amount<=getMaxMRU(stack))
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

	@Override
	public int getMaxMRU(ItemStack stack) {
		return 1000000;
	}

	public String textureName = "";

	public ItemComputerArmor setTextureName(String name) {
		textureName = name;
		return this;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/" + getRegistryName().getResourcePath(), "inventory"));
	}
}
