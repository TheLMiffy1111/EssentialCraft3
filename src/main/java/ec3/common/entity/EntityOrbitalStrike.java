package ec3.common.entity;

import java.util.List;

import ec3.common.item.ItemsCore;
import ec3.utils.common.ECUtils;
import DummyCore.Utils.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityOrbitalStrike extends Entity {

	public static final DataParameter<String> DATA = EntityDataManager.<String>createKey(EntityOrbitalStrike.class, DataSerializers.STRING);

	public EntityOrbitalStrike(World w) {
		super(w);
		this.setSize(0.3F, 0.3F);
	}

	public EntityOrbitalStrike(World w, double x, double y, double z) {
		this(w);
		this.setPositionAndRotation(x, y, z, 0, 0);
	}

	public EntityOrbitalStrike(World w, double x, double y, double z, double damage, double delay, EntityLivingBase base) {
		this(w,x,y,z);
		this.damage = damage;
		this.delay = delay;
	}

	public EntityLivingBase attacker;
	public double delay = 3;
	public double damage = 1;

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

		if(!this.getEntityWorld().isRemote)
			this.getDataManager().set(DATA, String.valueOf(delay));

		if(this.ticksExisted == 3)
			ECUtils.playSoundToAllNearby(posX, posY, posZ, "essentialcraft:sound.orbital_strike", 1, 1F, 16,this.dimension);

		if(delay <= 0 && !this.isDead)
		{
			List<EntityLivingBase> allEntities = this.getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX-0.5D, posY-0.5D, posZ-0.5D, posX+0.5D, posY+0.5D, posZ+0.5D).expand(2, 2, 2));
			for(int i = 0; i < allEntities.size(); ++i)
			{

				EntityLivingBase elb = allEntities.get(i);

				if(elb == null)
					continue;

				if(elb.isDead)
					continue;

				if(elb == this.attacker)
					continue;

				elb.attackEntityFrom(new DamageSource("orbitalStrike"){

					public Entity getEntity()
					{
						return attacker;
					}

				}
				.setDamageIsAbsolute(), (float) damage);
			}
			this.setDead();

			for(int i = 0; i < 3; ++i)
				this.getEntityWorld().playSound(posX, posY, posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1, this.rand.nextFloat()*2, false);

			for(int i = 0; i < 20; ++i)
				this.getEntityWorld().spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX+MathUtils.randomDouble(rand), posY+MathUtils.randomDouble(rand), posZ+MathUtils.randomDouble(rand), 0, 0, 0);

			if(this.getEntityWorld().getGameRules().getBoolean("mobGriefing"))
				for(int dx = -2; dx <= 2; ++dx)
				{
					int x = MathHelper.floor(posX) + dx;
					for(int dy = -2; dy <= 2; ++dy)
					{
						int y = MathHelper.floor(posY) + dy;
						for(int dz = -2; dz <= 2; ++dz)
						{
							int z = MathHelper.floor(posZ) + dz;
							IBlockState b = this.getEntityWorld().getBlockState(new BlockPos(x, y, z));
							if(!this.getEntityWorld().isAirBlock(new BlockPos(x, y, z)))
							{
								if(b.getMaterial() == Material.WATER || b.getMaterial() == Material.ICE || b.getMaterial() == Material.SNOW)
								{
									if(!this.getEntityWorld().isRemote)
										this.getEntityWorld().setBlockState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState(),2);

									getEntityWorld().playSound((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (getEntityWorld().rand.nextFloat() - getEntityWorld().rand.nextFloat()) * 0.8F, false);

									for (int l = 0; l < 8; ++l)
									{
										getEntityWorld().spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double)x + Math.random(), (double)y + Math.random(), (double)z + Math.random(), 0.0D, 0.0D, 0.0D);
									}

									continue;
								}
								ItemStack is = new ItemStack(b.getBlock(),1,b.getBlock().getMetaFromState(b));
								ItemStack result = FurnaceRecipes.instance().getSmeltingResult(is);
								if(result != null)
								{
									if(result.getItem() instanceof ItemBlock)
									{
										Block setTo = ItemBlock.class.cast(result.getItem()).block;
										if(setTo != null && !this.getEntityWorld().isRemote)
											this.getEntityWorld().setBlockState(new BlockPos(x, y, z), setTo.getStateFromMeta(result.getItemDamage()),2);
									}else
									{
										if(!this.getEntityWorld().isRemote)
											this.getEntityWorld().setBlockState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState(),2);

										EntityItem itm = new EntityItem(this.getEntityWorld(),x,y,z,result.copy());

										if(!this.getEntityWorld().isRemote)
											this.getEntityWorld().spawnEntity(itm);
									}
								}

								is = null;
								result = null;
							}
						}	
					}
				}
		}

		if(this.getEntityWorld().isRemote)
		{
			try
			{
				this.delay = Double.parseDouble(this.getDataManager().get(DATA));
			}catch(Exception e)
			{

			}
		}
	}
	
	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(ItemsCore.entityEgg,1,EntitiesCore.registeredEntities.indexOf(this.getClass()));
	}
}
