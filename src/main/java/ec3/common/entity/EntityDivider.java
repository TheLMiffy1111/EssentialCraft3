package ec3.common.entity;

import java.util.List;

import ec3.common.item.ItemsCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityDivider extends Entity {
	
	public static final DataParameter<String> DATA = EntityDataManager.<String>createKey(EntityDivider.class, DataSerializers.STRING);
	
	public EntityDivider(World w) {
		super(w);
		this.setSize(0.3F, 0.3F);
	}
	
	public EntityDivider(World w, double x, double y, double z) {
		this(w);
		this.setPositionAndRotation(x, y, z, 0, 0);
	}
	
	public EntityDivider(World w, double x, double y, double z, double damage, double delay, EntityLivingBase base) {
		this(w,x,y,z);
		this.damage = damage;
		this.delay = delay;
	}
	
	public EntityLivingBase attacker;
	public double delay = 2;
	public double damage = 1;
	public static final double RANGE = 3;
	@Override
	protected void entityInit() 
	{
		this.getDataManager().register(DATA, "||null:null");
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tag) {
		delay = tag.getDouble("delay");
		damage = tag.getDouble("damage");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tag) {
		tag.setDouble("delay", delay);
		tag.setDouble("damage", damage);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onUpdate()
	{
		delay -= 0.05D;
		
		if(this.ticksExisted % 10 == 0)
			this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
		
		this.getEntityWorld().spawnParticle(EnumParticleTypes.REDSTONE, posX, posY, posZ, 1, 0, 1);
		
		if(delay <= 0 && !this.isDead)
		{
			List<EntityLivingBase> allEntities = this.getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX-0.5D, posY-0.5D, posZ-0.5D, posX+0.5D, posY+0.5D, posZ+0.5D).expand(3, 3, 3));
			for(int i = 0; i < allEntities.size(); ++i)
			{
				
				EntityLivingBase elb = allEntities.get(i);
				
				if(elb == null)
					continue;
				
				if(elb.isDead)
					continue;
				
				if(elb == this.attacker)
					continue;
				
				if(elb instanceof EntityHologram)
					continue;
				
				if(elb.getDistanceToEntity(this) > 3)
					continue;
				
				if(this.getEntityWorld().isRemote)
					return;
				
				elb.setHealth(elb.getHealth()/2);
				elb.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS,200,4,true,true));
				elb.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS,200,4,true,true));
				elb.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE,200,4,true,true));
				elb.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS,100,0,true,true));
				
			}
			this.setDead();

		}
	}
	
	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(ItemsCore.entityEgg,1,EntitiesCore.registeredEntities.indexOf(this.getClass()));
	}
}
