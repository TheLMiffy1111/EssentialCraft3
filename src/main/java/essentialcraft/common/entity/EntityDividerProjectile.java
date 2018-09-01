package essentialcraft.common.entity;

import essentialcraft.common.item.ItemsCore;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class EntityDividerProjectile extends EntityThrowable {

	public EntityDividerProjectile(World w) {
		super(w);
	}

	public EntityDividerProjectile(World p_i1774_1_, EntityLivingBase p_i1774_2_) {
		super(p_i1774_1_, p_i1774_2_);
	}

	@Override
	protected void onImpact(RayTraceResult p_70184_1_) {
		if (p_70184_1_.typeOfHit == Type.BLOCK) {
			EntityDivider div = new EntityDivider(this.getEntityWorld(),this.posX,this.posY,this.posZ,0,2,this.getThrower());
			if(!this.getEntityWorld().isRemote)
				this.getEntityWorld().spawnEntity(div);

			this.setDead();
		}
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(ItemsCore.entityEgg,1,EntitiesCore.REGISTERED_ENTITIES.indexOf(ForgeRegistries.ENTITIES.getValue(EntityList.getKey(this.getClass()))));
	}
}
