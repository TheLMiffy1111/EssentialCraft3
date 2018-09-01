package essentialcraft.common.entity;

import essentialcraft.common.item.ItemsCore;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class EntityArmorDestroyer extends EntityThrowable {

	public EntityArmorDestroyer(World w) {
		super(w);
	}

	public EntityArmorDestroyer(World p_i1774_1_, EntityLivingBase p_i1774_2_) {
		super(p_i1774_1_, p_i1774_2_);
	}

	@Override
	protected void onImpact(RayTraceResult p_70184_1_) {
		if(p_70184_1_.entityHit != null) {
			if(p_70184_1_.entityHit == this.getThrower())
				return;

			if(!(p_70184_1_.entityHit instanceof EntityPlayer))
				return;

			EntityPlayer p = (EntityPlayer) p_70184_1_.entityHit;

			if(p != null) {
				ItemStack c = p.getHeldItemMainhand();
				ItemStack c1 = p.getHeldItemOffhand();
				if(!c.isEmpty())
					c.damageItem(5, p);
				if(!c1.isEmpty())
					c1.damageItem(5, p);

				for(int j = 0; j < 4; ++j) {
					ItemStack a = p.inventory.armorInventory.get(j);

					if(!a.isEmpty())
						if(a.getItem() instanceof ISpecialArmor)
							((ISpecialArmor)a.getItem()).damageArmor(p, a, DamageSource.ANVIL, 5, j);
						else
							a.damageItem(5, p);
				}
			}
		}

		if(!this.getEntityWorld().isRemote) {
			this.setDead();
		}

		this.getEntityWorld().playSound(posX, posY, posZ, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 0.3F, 2, false);
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(ItemsCore.entityEgg,1,EntitiesCore.REGISTERED_ENTITIES.indexOf(ForgeRegistries.ENTITIES.getValue(EntityList.getKey(this.getClass()))));
	}
}
