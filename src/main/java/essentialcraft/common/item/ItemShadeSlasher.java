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
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemShadeSlasher extends ItemSwordEC {

	public ItemShadeSlasher() {
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
		if(e instanceof EntityLivingBase && !w.isRemote && held)
			((EntityLivingBase)e).addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE,3,3,true,true));
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
			mp.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 32, 0));
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
				if(attacker.getEntityWorld().rand.nextFloat() <= 0.6F)
				{
					//knockback
					float i = 3F;
					attacked.addVelocity(-MathHelper.sin(attacker.rotationYaw * (float)Math.PI / 180.0F) * i * 0.5F, 0.1D, MathHelper.cos(attacker.rotationYaw * (float)Math.PI / 180.0F) * i * 0.5F);
				}
				if(attacker.getEntityWorld().rand.nextFloat() <= 0.01F)
				{
					//instagib
					attacker.addPotionEffect(new PotionEffect(MobEffects.STRENGTH,20,20,true,true));
				}
			}
		}
		return false;
	}

	@Override
	public void registerModels() {
		ModelUtils.setItemModelNBTActive(this, "essentialcraft:item/shadeslasher");
	}
}
