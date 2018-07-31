package essentialcraft.common.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import DummyCore.Client.ModelUtils;
import DummyCore.Utils.MiscUtils;
import essentialcraft.api.IShadeHandlerEntity;
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
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemShadeSword extends ItemSwordEC {

	public ItemShadeSword() {
		super(ItemsCore.shade);
	}

	public void toggleActivity(ItemStack is, boolean b)
	{
		if(!is.isEmpty())
		{
			NBTTagCompound tag = MiscUtils.getStackTag(is);
			if(tag.getBoolean("active") != b)
			{
				tag.setBoolean("active", b);
			}
		}
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
	{
		toggleActivity(entityItem.getItem(),false);
		return super.onEntityItemUpdate(entityItem);
	}

	@Override
	public void onUpdate(ItemStack sword, World w, Entity e, int slotNum, boolean held)
	{
		if(e instanceof IShadeHandlerEntity)
			toggleActivity(sword,true);

		if(e instanceof EntityPlayer)
		{
			EntityPlayer p = (EntityPlayer)e;
			if(ECUtils.getData(p).getMatrixTypeID() == 4)
				toggleActivity(sword,true);
			else
				toggleActivity(sword,false);
		}
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot s, ItemStack stack)
	{
		Multimap<String, AttributeModifier> mp = HashMultimap.<String, AttributeModifier>create();
		if(MiscUtils.getStackTag(stack).getBoolean("active") && s == EntityEquipmentSlot.MAINHAND) {
			mp.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 16, 0));
			mp.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4D, 0));
		}
		return mp;
	}


	public boolean isItemTool(ItemStack p_77616_1_)
	{
		return true;
	}

	@Override
	public boolean hitEntity(ItemStack weapon, EntityLivingBase attacked, EntityLivingBase attacker)
	{
		if(attacker instanceof IShadeHandlerEntity)
		{
			if(attacked instanceof EntityPlayer)
			{
				EntityPlayer p = (EntityPlayer)attacked;
				ShadeUtils.attackPlayerWithShade(p, attacker, weapon);
			}
		}
		if(attacker instanceof IShadeHandlerEntity || attacker instanceof EntityPlayer && ECUtils.getData((EntityPlayer)attacker).getMatrixTypeID() == 4)
		{
			if(!attacker.getEntityWorld().isRemote)
			{
				if(attacker.getEntityWorld().rand.nextFloat() <= 0.33F)
				{
					//swap
					Vec3d offsetVec = new Vec3d(attacker.posX-attacked.posX, attacked.posY-attacker.posY, attacker.posZ-attacked.posZ);
					float newYaw = 0;
					if(attacker.rotationYawHead >= 180)
						newYaw = attacker.rotationYawHead - 180;
					else
						newYaw = attacker.rotationYawHead + 180;
					attacker.setPositionAndRotation(attacked.posX-offsetVec.x, attacker.posY, attacked.posZ-offsetVec.z, newYaw, attacker.rotationPitch);
					attacker.rotationYawHead = newYaw;
					if(attacker instanceof EntityPlayer)
						ECUtils.changePlayerPositionOnClient((EntityPlayer)attacker);
				}
				if(attacker.getEntityWorld().rand.nextDouble() <= 0.1D)
				{
					//crit
					MiscUtils.damageEntityIgnoreEvent(attacked, DamageSource.causeMobDamage(attacker), 16);
					attacked.hurtResistantTime = 0;
					attacked.hurtTime = 0;
				}
			}
		}
		return false;
	}

	@Override
	public void registerModels() {
		ModelUtils.setItemModelNBTActive(this, "essentialcraft:item/shadesword");
	}
}
