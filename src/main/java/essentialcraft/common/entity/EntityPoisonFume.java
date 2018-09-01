package essentialcraft.common.entity;

import java.util.List;

import DummyCore.Utils.MathUtils;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import essentialcraft.common.item.ItemBaublesSpecial;
import essentialcraft.common.item.ItemsCore;
import essentialcraft.common.mod.EssentialCraftCore;
import essentialcraft.utils.cfg.Config;
import essentialcraft.utils.common.ECUtils;
import essentialcraft.utils.common.RadiationManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class EntityPoisonFume extends EntityMob {

	private float heightOffset = 0.5F;
	private int heightOffsetUpdateTime;
	public double mX, mY, mZ;

	public EntityPoisonFume(World p_i1731_1_) {
		super(p_i1731_1_);
		this.isImmuneToFire = true;
		this.setSize(0.6F, 0.6F);
	}

	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float amount) {
		return false;
	}

	@Override
	public int getBrightnessForRender() {
		return 15728880;
	}

	@Override
	public float getBrightness() {
		return 1.0F;
	}

	@Override
	public void onLivingUpdate() {
		if(!(this.dimension == Config.dimensionID && ECUtils.isEventActive("essentialcraft.event.fumes")))
			this.setDead();

		if(!this.getEntityWorld().isRemote) {
			--this.heightOffsetUpdateTime;

			if(this.heightOffsetUpdateTime <= 0) {
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
		List<EntityPlayer> players = this.getEntityWorld().<EntityPlayer>getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(posX-1, posY-1, posZ-1, posX+1, posY+1, posZ+1).grow(6, 3, 6));
		for(int i = 0; i < players.size(); ++i) {
			EntityPlayer p = players.get(i);
			boolean ignorePoison = false;
			IBaublesItemHandler b = BaublesApi.getBaublesHandler(p);
			if(b != null) {
				for(int i1 = 0; i1 < b.getSlots(); ++i1) {
					ItemStack is = b.getStackInSlot(i1);
					if(is.getItem() instanceof ItemBaublesSpecial && is.getItemDamage() == 19 || p.capabilities.isCreativeMode)
						ignorePoison = true;
				}
			}
			if(!p.getEntityWorld().isRemote && !ignorePoison) {
				RadiationManager.increasePlayerRadiation(p, 10);
				p.addPotionEffect(new PotionEffect(MobEffects.POISON,200,1));
			}
		}
		super.onLivingUpdate();
	}

	@Override
	public boolean attackEntityAsMob(Entity p_70785_1_) {
		return false;
	}

	@Override
	public void fall(float p_70069_1_, float s) {}

	@Override
	public boolean isBurning() {
		return false;
	}

	@Override
	protected boolean isValidLightLevel() {
		return true;
	}

	@Override
	public boolean getCanSpawnHere() {
		return this.dimension == Config.dimensionID && ECUtils.isEventActive("essentialcraft.event.fumes");
	}

	public float getHeightOffset() {
		return heightOffset;
	}

	public void setHeightOffset(float heightOffset) {
		this.heightOffset = heightOffset;
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(ItemsCore.entityEgg,1,EntitiesCore.REGISTERED_ENTITIES.indexOf(ForgeRegistries.ENTITIES.getValue(EntityList.getKey(this.getClass()))));
	}
}