package ec3.common.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import DummyCore.Client.ModelUtils;
import DummyCore.Utils.MiscUtils;
import ec3.api.IShadeCreature;
import ec3.utils.common.ECUtils;
import ec3.utils.common.ShadeUtils;
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

public class ItemShadeSlasher extends ItemSword_Mod {
	
	public ItemShadeSlasher() {
		super(ItemsCore.shade);
	}
	
	public void toggleActivity(ItemStack is, boolean b)
	{
		if(is != null)
		{
			NBTTagCompound tag = MiscUtils.getStackTag(is);
			if(tag.getBoolean("active") != b)
			{
				tag.setBoolean("active", b);
			}
		}
	}
	
    public boolean onEntityItemUpdate(EntityItem entityItem)
    {
    	toggleActivity(entityItem.getEntityItem(),false);
    	return super.onEntityItemUpdate(entityItem);
    }
	
	public void onUpdate(ItemStack sword, World w, Entity e, int slotNum, boolean held) 
	{
		if(e instanceof EntityLivingBase && !w.isRemote && held)
			EntityLivingBase.class.cast(e).addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE,3,3,true,true));
		if(e instanceof IShadeCreature)
			toggleActivity(sword,true);
		
		if(e instanceof EntityPlayer)
		{
    		EntityPlayer p = EntityPlayer.class.cast(e);
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
    	if(MiscUtils.getStackTag(stack).getBoolean("active") && s == EntityEquipmentSlot.MAINHAND)
    		mp.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 32, 0));
    	return mp;
    }
	
	
    public boolean isItemTool(ItemStack p_77616_1_)
    {
    	return true;
    }

    public boolean hitEntity(ItemStack weapon, EntityLivingBase attacked, EntityLivingBase attacker)
    {
    	if(attacker instanceof IShadeCreature)
    	{
    		if(attacked instanceof EntityPlayer)
    		{
    			EntityPlayer p = EntityPlayer.class.cast(attacked);
    			ShadeUtils.attackPlayerWithShade(p, attacker, weapon);
    		}
    	}
    	if(attacker instanceof IShadeCreature || (attacker instanceof EntityPlayer && ECUtils.getData(EntityPlayer.class.cast(attacker)).getMatrixTypeID() == 4))
    	{
    		if(!attacker.getEntityWorld().isRemote)
    		{
    			if(attacker.getEntityWorld().rand.nextFloat() <= 0.6F)
    			{
    				//knockback
    				float i = 3F;
    				attacked.addVelocity((double)(-MathHelper.sin(attacker.rotationYaw * (float)Math.PI / 180.0F) * (float)i * 0.5F), 0.1D, (double)(MathHelper.cos(attacker.rotationYaw * (float)Math.PI / 180.0F) * (float)i * 0.5F));
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
		ModelUtils.setItemModelNBTActive(this, "essentialcraft:item/shadeSlasher");
	}
}
