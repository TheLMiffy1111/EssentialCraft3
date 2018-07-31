package essentialcraft.common.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import DummyCore.Client.ModelUtils;
import DummyCore.Utils.MiscUtils;
import essentialcraft.api.IShadeHandlerEntity;
import essentialcraft.common.entity.EntityShadowKnife;
import essentialcraft.utils.common.ECUtils;
import essentialcraft.utils.common.ShadeUtils;
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

public class ItemShadowKnife extends ItemSwordEC {

	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
		if(stack.getCount() >= 2 && (entityLiving instanceof IShadeHandlerEntity || entityLiving instanceof EntityPlayer && ECUtils.getData((EntityPlayer)entityLiving).getMatrixTypeID() == 4)) {
			if(!(entityLiving instanceof EntityPlayer && ((EntityPlayer)entityLiving).capabilities.isCreativeMode))
				stack.shrink(1);
			if(stack.getCount() <= 0) {
				EntityEquipmentSlot slot = entityLiving.swingingHand == EnumHand.MAIN_HAND ? EntityEquipmentSlot.MAINHAND : entityLiving.swingingHand == EnumHand.OFF_HAND ? EntityEquipmentSlot.OFFHAND : null;
				entityLiving.setItemStackToSlot(slot, ItemStack.EMPTY);
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
		if(!is.isEmpty()) {
			NBTTagCompound tag = MiscUtils.getStackTag(is);
			if(tag.getBoolean("active") != b) {
				tag.setBoolean("active", b);
			}
		}
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem) {
		toggleActivity(entityItem.getItem(),false);
		return super.onEntityItemUpdate(entityItem);
	}

	@Override
	public void onUpdate(ItemStack sword, World w, Entity e, int slotNum, boolean held) {
		if(e.ticksExisted % 20 == 0 && !w.isRemote && held) {
			sword.grow(1);
			if(sword.getCount() >= 32)
				sword.setCount(32);
		}
		if(e instanceof IShadeHandlerEntity)
			toggleActivity(sword,true);

		if(e instanceof EntityPlayer) {
			EntityPlayer p = (EntityPlayer)e;
			if(ECUtils.getData(p).getMatrixTypeID() == 4)
				toggleActivity(sword,true);
			else
				toggleActivity(sword,false);
		}
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot s, ItemStack stack) {
		Multimap<String, AttributeModifier> mp = HashMultimap.<String, AttributeModifier>create();
		if(MiscUtils.getStackTag(stack).getBoolean("active") && s == EntityEquipmentSlot.MAINHAND) {
			mp.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 12, 0));
			mp.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4D, 0));
		}
		return mp;
	}


	public boolean isItemTool(ItemStack p_77616_1_) {
		return true;
	}

	@Override
	public boolean hitEntity(ItemStack weapon, EntityLivingBase attacked, EntityLivingBase attacker) {
		if(attacker instanceof IShadeHandlerEntity) {
			if(attacked instanceof EntityPlayer) {
				EntityPlayer p = (EntityPlayer)attacked;
				ShadeUtils.attackPlayerWithShade(p, attacker, weapon);
			}
		}
		return false;
	}

	@Override
	public void registerModels() {
		ModelUtils.setItemModelNBTActive(this, "essentialcraft:item/shadeknife");
	}
}
