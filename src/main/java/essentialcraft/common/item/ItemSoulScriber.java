package essentialcraft.common.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import essentialcraft.api.DemonTrade;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ItemSoulScriber extends ItemSwordEC {

	public ItemSoulScriber() {
		super(ToolMaterial.WOOD);
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot s,ItemStack stack)
	{
		Multimap<String, AttributeModifier> mp = HashMultimap.<String, AttributeModifier>create();
		if(s == EntityEquipmentSlot.MAINHAND) {
			mp.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 1, 0));
			mp.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4D, 0));
		}
		return mp;
	}

	@Override
	public boolean hitEntity(ItemStack weapon, EntityLivingBase attacked, EntityLivingBase attacker) {
		if(MathHelper.floor(attacked.getHealth()) <= 2) {
			boolean createSoul = DemonTrade.allMobs.contains(EntityRegistry.getEntry(attacked.getClass()));
			if(createSoul && attacker instanceof EntityPlayer) {
				ItemStack soul = new ItemStack(ItemsCore.soul,1,DemonTrade.allMobs.indexOf(EntityRegistry.getEntry(attacked.getClass())));
				EntityItem ei = new EntityItem(attacked.getEntityWorld(),attacked.posX,attacked.posY,attacked.posZ,soul);
				if(!attacked.getEntityWorld().isRemote)
					attacked.getEntityWorld().spawnEntity(ei);

				attacked.setDead();
			}
		}
		return false;
	}
}
