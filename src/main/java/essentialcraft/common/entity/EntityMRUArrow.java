package essentialcraft.common.entity;

import DummyCore.Utils.MathUtils;
import essentialcraft.common.item.ItemsCore;
import essentialcraft.common.mod.EssentialCraftCore;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class EntityMRUArrow extends EntityArrow {

	public EntityMRUArrow(World p_i1753_1_)
	{
		super(p_i1753_1_);
	}

	public EntityMRUArrow(World p_i1756_1_, EntityLivingBase p_i1756_2_,float p_i1756_3_)
	{
		super(p_i1756_1_, p_i1756_2_);
		this.pickupStatus = PickupStatus.DISALLOWED;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if(this.ticksExisted > 60)
			this.setDead();
		//this.motionX *= 0.9F;
		//this.motionY *= 0.9F;
		//this.motionZ *= 0.9F;
		for(int i = 0; i < 2; ++i)
		{
			EssentialCraftCore.proxy.spawnParticle("cSpellFX",(float)posX+MathUtils.randomFloat(rand)/10, (float)posY+MathUtils.randomFloat(rand)/10, (float)posZ+MathUtils.randomFloat(rand)/10, motionX*10, motionY*10, motionZ*10);
		}
	}

	@Override
	protected ItemStack getArrowStack() {
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(ItemsCore.entityEgg,1,EntitiesCore.REGISTERED_ENTITIES.indexOf(ForgeRegistries.ENTITIES.getValue(EntityList.getKey(this.getClass()))));
	}
}
