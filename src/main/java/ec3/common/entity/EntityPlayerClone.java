package ec3.common.entity;

import java.util.UUID;

import com.google.common.base.Optional;

import ec3.common.item.ItemsCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class EntityPlayerClone extends EntityZombie {

	public EntityPlayer playerToAttack = null;
	protected UUID clonedPlayer;
	private boolean firstTick = true;
	public static final DataParameter<Optional<UUID>> CLONED = EntityDataManager.<Optional<UUID>>createKey(EntityPlayerClone.class, DataSerializers.OPTIONAL_UNIQUE_ID);

	public EntityPlayerClone(World w) {
		super(w);
		this.inventoryHandsDropChances[0] = 0;
		this.inventoryHandsDropChances[1] = 0;
		this.inventoryArmorDropChances[0] = 0;
		this.inventoryArmorDropChances[1] = 0;
		this.inventoryArmorDropChances[2] = 0;
		this.inventoryArmorDropChances[3] = 0;
	}

	public Entity findPlayerToAttack() {
		playerToAttack = this.getEntityWorld().getNearestAttackablePlayer(this, 16, 16);
		return playerToAttack;
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(2.0D);
	}

	protected void dropEquipment(boolean p_82160_1_, int p_82160_2_) {}

	@Override
	public void onUpdate() {
		if(!isPotionActive(MobEffects.SPEED))
			addPotionEffect(new PotionEffect(MobEffects.SPEED,200,3,true,true));

		if(deathTime > 0)
			setDead();

		super.onUpdate();
		if(ticksExisted % 200 == 0)
			setDead();

		firstTick = false;
	}

	protected SoundEvent getAmbientSound() {
		return (SoundEvent)null;
	}

	protected SoundEvent getHurtSound() {
		return (SoundEvent)null;
	}

	protected SoundEvent getDeathSound() {
		return (SoundEvent)null;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return (ResourceLocation)null;
	}

	public UUID getClonedPlayer() {
		return dataManager.get(CLONED).orNull();
	}

	public void setClonedPlayer(UUID clonedPlayer) {
		if(this != null && !isDead)
			dataManager.set(CLONED, Optional.<UUID>fromNullable(clonedPlayer));
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(CLONED, Optional.<UUID>absent());
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		if(clonedPlayer != null)
			compound.setUniqueId("cloned", clonedPlayer);
		else
			compound.removeTag("cloned");
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		clonedPlayer = compound.getUniqueId("cloned");
	}

	public void updateClonedPlayer() {
		if(!getEntityWorld().isRemote) {
			setClonedPlayer(getClonedPlayer());
		}
		if(getEntityWorld().isRemote && !firstTick) {
			clonedPlayer = getClonedPlayer();
		}
	}

	@Override
	public String getName() {
		if(hasCustomName()) {
			return getCustomNameTag();
		}
		else {
			String s = EntityList.getEntityString(this);

			if(s == null) {
				s = "generic";
			}

			return I18n.translateToLocal("entity." + s + ".name");
		}
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		extinguish();
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(ItemsCore.entityEgg,1,EntitiesCore.registeredEntities.indexOf(this.getClass()));
	}
}
