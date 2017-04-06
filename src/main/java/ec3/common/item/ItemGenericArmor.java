package ec3.common.item;

import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import DummyCore.Client.IModelRegisterer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.model.ModelLoader;

public class ItemGenericArmor extends ItemArmor implements IItemColor, IModelRegisterer {

	public ItemGenericArmor(ArmorMaterial material, int renderIndex, int type)
	{
		super(material, renderIndex, EntityEquipmentSlot.values()[5-type]);
	}

	public ItemGenericArmor(ArmorMaterial material, int renderIndex, EntityEquipmentSlot type)
	{
		super(material, renderIndex, type);
	}

	@Override 
	public String getArmorTexture(ItemStack itemstack, Entity entity, EntityEquipmentSlot slot, String type)
	{ 
		switch(slot)
		{ 
		case LEGS: return "essentialcraft:textures/special/armor/wind_layer_2.png"; //2 should be the slot for legs
		default: return "essentialcraft:textures/special/armor/wind_layer_1.png"; 
		}
	}

	public int getColorFromItemstack(ItemStack stack, int renderPass)
	{
		int j = this.getColor(stack);

		if (j < 0)
		{
			j = 0xffffff;
		}

		return j;
	}

	public boolean hasColor(ItemStack stk)
	{
		return (!stk.hasTagCompound() ? false : (!stk.getTagCompound().hasKey("display", 10) ? false : stk.getTagCompound().getCompoundTag("display").hasKey("color", 3)));
	}

	public int getColor(ItemStack p_82814_1_)
	{
		NBTTagCompound nbttagcompound = p_82814_1_.getTagCompound();
		if (nbttagcompound == null)
		{
			return 0xffffff;
		}
		else
		{
			NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");
			return nbttagcompound1 == null ? 0xffffff : (nbttagcompound1.hasKey("color", 3) ? nbttagcompound1.getInteger("color") : 0xffffff);
		}
	}

	public void removeColor(ItemStack stk)
	{
		NBTTagCompound nbttagcompound = stk.getTagCompound();

		if (nbttagcompound != null)
		{
			NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

			if (nbttagcompound1.hasKey("color"))
			{
				nbttagcompound1.removeTag("color");
			}
		}
	}

	@Override
	public void setColor(ItemStack stk, int newColor)
	{
		NBTTagCompound nbttagcompound = stk.getTagCompound();

		if (nbttagcompound == null)
		{
			nbttagcompound = new NBTTagCompound();
			stk.setTagCompound(nbttagcompound);
		}

		NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

		if (!nbttagcompound.hasKey("display", 10))
		{
			nbttagcompound.setTag("display", nbttagcompound1);
		}

		nbttagcompound1.setInteger("color", newColor);
	}

	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot, ItemStack s)
	{
		Multimap<String, AttributeModifier> mods = HashMultimap.<String, AttributeModifier>create();

		if(this == ItemsCore.wind_chestplate && equipmentSlot == EntityEquipmentSlot.CHEST)
			mods.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(UUID.fromString("1bca943c-3cf5-42cc-a3df-2ed994ae0000"), "mSpeed", 0.075D, 0));

		return mods;
	}

	@Override
	public boolean hasOverlay(ItemStack stack) {
		return hasColor(stack);
	}

	public static String textureName = "";

	public ItemGenericArmor setTextureName(String name) {
		textureName = name;
		return this;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/" + getRegistryName().getResourcePath(), "inventory"));
	}
}
