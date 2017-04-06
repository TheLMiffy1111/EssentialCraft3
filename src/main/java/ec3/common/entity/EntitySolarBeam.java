package ec3.common.entity;

import java.util.List;

import DummyCore.Utils.MiscUtils;
import ec3.common.item.ItemsCore;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntitySolarBeam extends EntityWeatherEffect {
	
	public int beamLiveTime = 20;

	public EntitySolarBeam(World p_i1702_1_) {
		super(p_i1702_1_);
		this.ignoreFrustumCheck = true;
		this.setSize(0.3F, 0.3F);
	}
	
	public EntitySolarBeam(World p_i1703_1_, double p_i1703_2_, double p_i1703_4_, double p_i1703_6_)
    {
        super(p_i1703_1_);
        this.setLocationAndAngles(p_i1703_2_, p_i1703_4_, p_i1703_6_, 0.0F, 0.0F);
        this.beamLiveTime = 20;
        if (!p_i1703_1_.isRemote && p_i1703_1_.getGameRules().getBoolean("doFireTick") && p_i1703_1_.isAreaLoaded(new BlockPos(MathHelper.floor(p_i1703_2_), MathHelper.floor(p_i1703_4_), MathHelper.floor(p_i1703_6_)), 10))
        {
            int i = MathHelper.floor(p_i1703_2_);
            int j = MathHelper.floor(p_i1703_4_);
            int k = MathHelper.floor(p_i1703_6_);

            if (p_i1703_1_.getBlockState(new BlockPos(i, j, k)).getMaterial() == Material.AIR && Blocks.FIRE.canPlaceBlockAt(p_i1703_1_, new BlockPos(i, j, k)))
            {
                p_i1703_1_.setBlockState(new BlockPos(i, j, k), Blocks.FIRE.getDefaultState());
            }

            for (i = 0; i < 32; ++i)
            {
                j = MathHelper.floor(p_i1703_2_) + this.rand.nextInt(13) - 1;
                k = MathHelper.floor(p_i1703_4_) + this.rand.nextInt(13) - 1;
                int l = MathHelper.floor(p_i1703_6_) + this.rand.nextInt(13) - 1;

                if (p_i1703_1_.getBlockState(new BlockPos(j, k, l)).getMaterial() == Material.AIR && Blocks.FIRE.canPlaceBlockAt(p_i1703_1_, new BlockPos(j, k, l)))
                {
                    p_i1703_1_.setBlockState(new BlockPos(j, k, l), Blocks.FIRE.getDefaultState());
                }
            }
        }
    }

    public void onUpdate()
    {
        super.onUpdate();
        if(--beamLiveTime <= 0)
        	this.setDead();
        if((beamLiveTime%5) == 0)
        	this.getEntityWorld().playSound(null, this.posX, this.posY, this.posZ, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.WEATHER, 10.0F, 2F);
        double d0 = 6.0D;
        List<?> list = this.getEntityWorld().getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(this.posX - d0, this.posY - d0, this.posZ - d0, this.posX + d0, this.posY + 128.0D + d0, this.posZ + d0));

        for (int l = 0; l < list.size(); ++l)
        {
            Entity entity = (Entity)list.get(l);
            entity.setFire(5);
            entity.attackEntityFrom(DamageSource.onFire, 3.0F);
        }
    }
	
	@Override
	protected void entityInit() {}

	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {}

	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {}
	
	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(ItemsCore.entityEgg,1,EntitiesCore.registeredEntities.indexOf(this.getClass()));
	}
}
