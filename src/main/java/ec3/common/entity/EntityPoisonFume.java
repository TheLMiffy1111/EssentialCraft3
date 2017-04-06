package ec3.common.entity;

import java.util.List;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import DummyCore.Utils.MathUtils;
import ec3.common.item.BaublesModifier;
import ec3.common.item.ItemsCore;
import ec3.common.mod.EssentialCraftCore;
import ec3.utils.cfg.Config;
import ec3.utils.common.ECUtils;
import ec3.utils.common.RadiationManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityPoisonFume extends EntityMob {

	public static final DataParameter<Boolean> FLAG = EntityDataManager.<Boolean>createKey(EntityPoisonFume.class, DataSerializers.BOOLEAN);
	/** Random offset used in floating behaviour */
	private float heightOffset = 0.5F;
	/** ticks until heightOffset is randomized */
	private int heightOffsetUpdateTime;
	public double mX, mY, mZ;

	public EntityPoisonFume(World p_i1731_1_)
	{
		super(p_i1731_1_);
		this.isImmuneToFire = true;
		this.setSize(0.6F, 0.6F);
	}

	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
	}

	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_)
	{
		return false;
	}

	protected void entityInit()
	{
		super.entityInit();
		this.getDataManager().register(FLAG, false);
	}

	public int getBrightnessForRender(float p_70070_1_)
	{
		return 15728880;
	}

	/**
	 * Gets how bright this entity is.
	 */
	public float getBrightness(float p_70013_1_)
	{
		return 1.0F;
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	@SuppressWarnings("unchecked")
	public void onLivingUpdate()
	{
		if(!(this.dimension == Config.dimensionID && ECUtils.isEventActive("ec3.event.fumes")))
			this.setDead();

		if (!this.getEntityWorld().isRemote)
		{
			--this.heightOffsetUpdateTime;

			if (this.heightOffsetUpdateTime <= 0)
			{
				this.heightOffsetUpdateTime = 100;
				this.mX = MathUtils.randomDouble(this.getEntityWorld().rand);
				this.mY = MathUtils.randomDouble(this.getEntityWorld().rand);
				this.mZ = MathUtils.randomDouble(this.getEntityWorld().rand);
				this.setHeightOffset(0.5F + (float)this.rand.nextGaussian() * 3.0F);
			}
			this.motionX = mX/10;
			this.motionY = mY/10;
			this.motionZ = mZ/10;
			if(this.ticksExisted > 1000)
				this.setDead();
		}
		EssentialCraftCore.proxy.spawnParticle("fogFX", (float)posX, (float)posY+2, (float)posZ, 0.0F, 1.0F, 0.0F);
		List<EntityPlayer> players = this.getEntityWorld().<EntityPlayer>getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(posX-1, posY-1, posZ-1, posX+1, posY+1, posZ+1).expand(6, 3, 6));
		for(int i = 0; i < players.size(); ++i)
		{
			EntityPlayer p = players.get(i);
			boolean ignorePoison = false;
			IBaublesItemHandler b = BaublesApi.getBaublesHandler(p);
			if(b != null)
			{
				for(int i1 = 0; i1 < b.getSlots(); ++i1)
				{
					ItemStack is = b.getStackInSlot(i1);
					if((is != null && is.getItem() != null && is.getItem() instanceof BaublesModifier && is.getItemDamage() == 19) || p.capabilities.isCreativeMode)
						ignorePoison = true;
				}
			}
			if(!p.getEntityWorld().isRemote && !ignorePoison)
			{
				RadiationManager.increasePlayerRadiation(p, 10);
				p.addPotionEffect(new PotionEffect(MobEffects.POISON,200,1));
			}
		}
		super.onLivingUpdate();
	}

	/**
	 * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
	 */
	public boolean attackEntityAsMob(Entity p_70785_1_) {
		return false;
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	public void fall(float p_70069_1_, float s) {}

	/**
	 * Returns true if the entity is on fire. Used by render to add the fire effect on rendering.
	 */
	public boolean isBurning()
	{
		return false;
	}

	public boolean func_70845_n()
	{
		return this.getDataManager().get(FLAG);
	}

	public void func_70844_e(boolean p_70844_1_)
	{
		this.getDataManager().set(FLAG, p_70844_1_);
	}

	/**
	 * Checks to make sure the light is not too bright where the mob is spawning
	 */
	protected boolean isValidLightLevel()
	{
		return true;
	}

	public boolean getCanSpawnHere()
	{
		return this.dimension == Config.dimensionID && ECUtils.isEventActive("ec3.event.fumes");
	}

	public float getHeightOffset() {
		return heightOffset;
	}

	public void setHeightOffset(float heightOffset) {
		this.heightOffset = heightOffset;
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(ItemsCore.entityEgg,1,EntitiesCore.registeredEntities.indexOf(this.getClass()));
	}
}