package ec3.common.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import ec3.api.DemonTrade;

public class ItemSoulScriber extends ItemSword_Mod {

	public ItemSoulScriber() {
		super(ToolMaterial.WOOD);
		setTextureName("essentialcraft:tools/soulScriber");
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot s,ItemStack stack)
	{
		Multimap<String, AttributeModifier> mp = HashMultimap.<String, AttributeModifier>create();
		if(s == EntityEquipmentSlot.MAINHAND)
			mp.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 1, 0));
		return mp;
	}

	public boolean hitEntity(ItemStack weapon, EntityLivingBase attacked, EntityLivingBase attacker)
	{
		if(MathHelper.floor(attacked.getHealth()) <= 2)
		{
			boolean createSoul = DemonTrade.allMobs.contains(attacked.getClass());
			if(createSoul && attacker instanceof EntityPlayer)
			{
				ItemStack soul = new ItemStack(ItemsCore.soul,1,DemonTrade.allMobs.indexOf(attacked.getClass()));
				EntityItem ei = new EntityItem(attacked.getEntityWorld(),attacked.posX,attacked.posY,attacked.posZ,soul);
				if(!attacked.getEntityWorld().isRemote)
					attacked.getEntityWorld().spawnEntity(ei);

				attacked.setDead();
			}
		}
		return false;
	}
}
