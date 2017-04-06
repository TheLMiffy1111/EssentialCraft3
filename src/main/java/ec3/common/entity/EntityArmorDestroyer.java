package ec3.common.entity;

import ec3.common.item.ItemsCore;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;

public class EntityArmorDestroyer extends EntityThrowable {

	public EntityArmorDestroyer(World w) {
		super(w);
	}

	public EntityArmorDestroyer(World p_i1774_1_, EntityLivingBase p_i1774_2_) {
		super(p_i1774_1_, p_i1774_2_);
	}

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
				if(c != null)
					c.damageItem(5, p);
				if(c1 != null)
					c1.damageItem(5, p);

				for(int j = 0; j < 4; ++j) {
					ItemStack a = p.inventory.armorInventory[j];

					if(a != null)
						if(a.getItem() instanceof ISpecialArmor)
							ISpecialArmor.class.cast(a.getItem()).damageArmor(p, a, DamageSource.anvil, 5, j);
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
		return new ItemStack(ItemsCore.entityEgg,1,EntitiesCore.registeredEntities.indexOf(this.getClass()));
	}
}
