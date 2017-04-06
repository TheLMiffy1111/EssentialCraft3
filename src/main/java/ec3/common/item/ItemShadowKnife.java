package ec3.common.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import DummyCore.Client.ModelUtils;
import DummyCore.Utils.MiscUtils;
import ec3.api.IShadeCreature;
import ec3.common.entity.EntityShadowKnife;
import ec3.utils.common.ECUtils;
import ec3.utils.common.ShadeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemShadowKnife extends ItemSword_Mod {

	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
		if(stack.stackSize >= 2 && (entityLiving instanceof IShadeCreature || (entityLiving instanceof EntityPlayer && ECUtils.getData(EntityPlayer.class.cast(entityLiving)).getMatrixTypeID() == 4))) {
			if(!(entityLiving instanceof EntityPlayer && EntityPlayer.class.cast(entityLiving).capabilities.isCreativeMode))
				--stack.stackSize;
			if(stack.stackSize <= 0) {
				EntityEquipmentSlot slot = entityLiving.swingingHand == EnumHand.MAIN_HAND ? EntityEquipmentSlot.MAINHAND : entityLiving.swingingHand == EnumHand.OFF_HAND ? EntityEquipmentSlot.OFFHAND : null;
				entityLiving.setItemStackToSlot(slot, null);
			}
			EntityShadowKnife knife = new EntityShadowKnife(entityLiving.getEntityWorld(), entityLiving);
			knife.setHeadingFromThrower(entityLiving, entityLiving.rotationPitch, entityLiving.rotationYaw, 0.0F, 1.5F, 1.0F);
			if(!entityLiving.getEntityWorld().isRemote)
				entityLiving.getEntityWorld().spawnEntity(knife);
		}
		return false;
	}

	public ItemShadowKnife() {
		super(ItemsCore.shade);
	}

	public void toggleActivity(ItemStack is, boolean b) {
		if(is != null) {
			NBTTagCompound tag = MiscUtils.getStackTag(is);
			if(tag.getBoolean("active") != b) {
				tag.setBoolean("active", b);
			}
		}
	}

	public boolean onEntityItemUpdate(EntityItem entityItem) {
		toggleActivity(entityItem.getEntityItem(),false);
		return super.onEntityItemUpdate(entityItem);
	}

	public void onUpdate(ItemStack sword, World w, Entity e, int slotNum, boolean held) {
		if(e.ticksExisted % 20 == 0 && !w.isRemote && held) {
			sword.stackSize += 1;
			if(sword.stackSize >= 32)
				sword.stackSize = 32;
		}
		if(e instanceof IShadeCreature)
			toggleActivity(sword,true);

		if(e instanceof EntityPlayer) {
			EntityPlayer p = EntityPlayer.class.cast(e);
			if(ECUtils.getData(p).getMatrixTypeID() == 4)
				toggleActivity(sword,true);
			else
				toggleActivity(sword,false);
		}
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot s, ItemStack stack) {
		Multimap<String, AttributeModifier> mp = HashMultimap.<String, AttributeModifier>create();
		if(MiscUtils.getStackTag(stack).getBoolean("active") && s == EntityEquipmentSlot.MAINHAND)
			mp.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 12, 0));
		return mp;
	}


	public boolean isItemTool(ItemStack p_77616_1_) {
		return true;
	}

	public boolean hitEntity(ItemStack weapon, EntityLivingBase attacked, EntityLivingBase attacker) {
		if(attacker instanceof IShadeCreature) {
			if(attacked instanceof EntityPlayer) {
				EntityPlayer p = EntityPlayer.class.cast(attacked);
				ShadeUtils.attackPlayerWithShade(p, attacker, weapon);
			}
		}
		return false;
	}

	@Override
	public void registerModels() {
		ModelUtils.setItemModelNBTActive(this, "essentialcraft:item/shadeKnife");
	}
}
