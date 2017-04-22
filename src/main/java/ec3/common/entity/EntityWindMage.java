package ec3.common.entity;

import com.google.common.base.Predicate;

import ec3.common.item.ItemsCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityWindMage extends EntityMob implements IRangedAttackMob {
	
	public static final DataParameter<Byte> TYPE = EntityDataManager.<Byte>createKey(EntityWindMage.class, DataSerializers.BYTE);
    private EntityAIAttackRanged aiArrowAttack = new EntityAIAttackRanged(this, 1.0D, 20, 60, 15.0F);
    private EntityAIAttackMelee aiAttackOnCollide = new EntityAIAttackMelee(this, 1.2D, false);
    public EntityWindMage(World p_i1741_1_)
    {
        super(p_i1741_1_);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, (Predicate)null));

        if (p_i1741_1_ != null && !p_i1741_1_.isRemote)
        {
            this.setCombatTask();
        }
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    protected void entityInit()
    {
        super.entityInit();
        this.getDataManager().register(TYPE, new Byte((byte)0));
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return true;
    }


    protected SoundEvent getHurtSound()
    {
        return SoundEvents.ENTITY_VILLAGER_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_VILLAGER_DEATH;
    }

    public boolean attackEntityAsMob(Entity p_70652_1_)
    {
        if (super.attackEntityAsMob(p_70652_1_))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEFINED;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
    }

    /**
     * Handles updating while being ridden by an entity
     */
    public void updateRidden()
    {
        super.updateRidden();

        if (this.getRidingEntity() instanceof EntityCreature)
        {
            EntityCreature entitycreature = (EntityCreature)this.getRidingEntity();
            this.renderYawOffset = entitycreature.renderYawOffset;
        }
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource p_70645_1_)
    {
        super.onDeath(p_70645_1_);
    }

    protected Item getDropItem()
    {
        return ItemsCore.bottledWind;
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
    {
    	if(this.getType() == 0)
    		this.dropItem(getDropItem(), 1);
    	if(this.getType() == 1)
    		this.dropItem(ItemsCore.imprisonedWind, 1);
    	if(this.getType() == 2)
    	{
    		this.dropItem(ItemsCore.windKeeper, 1);
    		if(this.getEntityWorld().rand.nextFloat() < 0.1F)
    		{
    			int i = this.getEntityWorld().rand.nextInt(4);
    			switch(i)
    			{
    				case 0:
    				{
    					this.dropItem(ItemsCore.magicArmorItems[12], 1);
    					return;
    				}
    				case 1:
    				{
    					this.dropItem(ItemsCore.magicArmorItems[13], 1);
    					return;
    				}
    				case 2:
    				{
    					this.dropItem(ItemsCore.magicArmorItems[14], 1);
    					return;
    				}
    				case 3:
    				{
    					this.dropItem(ItemsCore.magicArmorItems[15], 1);
    					return;
    				}
    			}
    		}
    	}
    }


    /**
     * sets this entity's combat AI.
     */
    public void setCombatTask()
    {
        this.tasks.removeTask(this.aiAttackOnCollide);
        this.tasks.removeTask(this.aiArrowAttack);
        this.tasks.addTask(4, this.aiArrowAttack);
    }

    /**
     * Attack the specified entity using a ranged attack.
     */
    public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_)
    {
        EntityMRUArrow entityarrow = new EntityMRUArrow(this.getEntityWorld(), this, 1.6F);
        double d0 = p_82196_1_.posX - this.posX;
        double d1 = p_82196_1_.getEntityBoundingBox().minY + (double)(p_82196_1_.height / 3.0F) - entityarrow.posY;
        double d2 = p_82196_1_.posZ - this.posZ;
        double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
        entityarrow.setThrowableHeading(d0, d1 + d3 * 0.2D, d2, 1.6F, (float)(14 - this.getEntityWorld().getDifficulty().getDifficultyId() * 4));
        entityarrow.setDamage((this.getType()+1)*3);
        this.getEntityWorld().spawnEntity(entityarrow);
    }

    public int getType()
    {
        return this.getDataManager().get(TYPE);
    }

    public void setType(int p_82201_1_)
    {
        this.getDataManager().set(TYPE, Byte.valueOf((byte)p_82201_1_));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound p_70037_1_)
    {
        super.readEntityFromNBT(p_70037_1_);

        if (p_70037_1_.hasKey("Type", 99))
        {
            byte b0 = p_70037_1_.getByte("Type");
            this.setType(b0);
        }

        this.setCombatTask();
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound p_70014_1_)
    {
        super.writeEntityToNBT(p_70014_1_);
        p_70014_1_.setByte("Type", (byte)this.getType());
    }
    
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData p_110161_1_)
    {
        p_110161_1_ = super.onInitialSpawn(difficulty, p_110161_1_);
        this.setType(this.getEntityWorld().rand.nextInt(3));
        return p_110161_1_;
    }

    /**
     * Returns the Y Offset of this entity.
     */
    public double getYOffset()
    {
        return super.getYOffset() - 0.5D;
    }
	
	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(ItemsCore.entityEgg,1,EntitiesCore.registeredEntities.indexOf(this.getClass()));
	}
}