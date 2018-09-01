package essentialcraft.common.entity;

import java.util.ArrayList;
import java.util.List;

import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.MathUtils;
import essentialcraft.common.item.ItemsCore;
import essentialcraft.utils.common.ECUtils;
import essentialcraft.utils.common.ShadeUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class EntityMRURay extends Entity {

	public static final DataParameter<String> DATA = EntityDataManager.<String>createKey(EntityMRURay.class, DataSerializers.STRING);
	public float balance;
	public float damage;
	public EntityLivingBase shootingEntity;
	public double pX,pY,pZ;
	List<EntityLivingBase> hitEntities = new ArrayList<EntityLivingBase>();

	public EntityMRURay(World w)
	{
		super(w);
	}

	@Override
	public void onEntityUpdate()
	{
		if(this.ticksExisted >= 60)
		{
			this.setDead();
		}

		if(!this.getEntityWorld().isRemote)
		{
			this.getDataManager().set(DATA, "||x:"+pX+"||y:"+pY+"||z:"+pZ+"||b:"+(double)balance);
		}

		if(this.getEntityWorld().isRemote)
		{
			String dataStr = this.getDataManager().get(DATA);
			if(dataStr != null && !dataStr.isEmpty())
			{
				DummyData[] posData = DataStorage.parseData(dataStr);
				pX = Double.parseDouble(posData[0].fieldValue);
				pY = Double.parseDouble(posData[1].fieldValue);
				pZ = Double.parseDouble(posData[2].fieldValue);
				balance = (float) Double.parseDouble(posData[3].fieldValue);
			}
		}

	}

	public EntityMRURay(World w, EntityLivingBase base)
	{
		super(w);
		this.rotationYaw = base.rotationYawHead;
		this.rotationPitch = base.rotationPitch;
		this.shootingEntity = base;
		pX = this.shootingEntity.posX;
		pY = this.shootingEntity.posY+this.shootingEntity.getEyeHeight();
		pZ = this.shootingEntity.posZ;
		this.posX = base.posX;
		this.posY = base.posY+this.shootingEntity.getEyeHeight();
		this.posZ = base.posZ;
	}

	public EntityMRURay(World w, EntityLivingBase base, float damage, float offset, float balance)
	{

		super(w);
		this.posX = base.posX;
		this.posY = base.posY+base.getEyeHeight();
		this.posZ = base.posZ;
		float rY = base.rotationYaw;
		float rP = base.rotationPitch;
		if(!w.isRemote)
		{
			base.rotationYaw = (float) (base.rotationYawHead + MathUtils.randomDouble(w.rand)*offset);
			base.rotationPitch = (float) (base.rotationPitch + MathUtils.randomDouble(w.rand)*offset);
		}
		this.damage = damage;
		this.balance = balance;
		this.shootingEntity = base;
		pX = this.shootingEntity.posX;
		pY = this.shootingEntity.posY+this.shootingEntity.getEyeHeight();
		pZ = this.shootingEntity.posZ;
		if(!this.getEntityWorld().isRemote)
		{
			this.shoot(rY,rP);
		}
	}

	public DamageSource causeMRUDamage(final EntityLivingBase attacker, EntityLivingBase attacked)
	{
		if(attacked instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)attacked;
			if(!player.getEntityWorld().isRemote && this.getEntityWorld().getMinecraftServer().isPVPEnabled())
			{
				if(attacker instanceof EntityPlayer)
				{
					EntityPlayer attackerPlayer = (EntityPlayer) attacker;
					if(attackerPlayer.getTeam() == null || player == null || !player.getTeam().isSameTeam(attackerPlayer.getTeam()))
						if(!this.getEntityWorld().getGameRules().getBoolean("essentialcraft:weaponMatrixDamage"))
							ECUtils.getData(player).modifyOverhaulDamage(ECUtils.getData(player).getOverhaulDamage() + MathHelper.floor(this.damage*100));
				}else
					if(!this.getEntityWorld().getGameRules().getBoolean("essentialcraft:weaponMatrixDamage"))
						ECUtils.getData(player).modifyOverhaulDamage(ECUtils.getData(player).getOverhaulDamage() + MathHelper.floor(this.damage*100));
			}
			if(this.balance == 4)
			{
				ShadeUtils.attackPlayerWithShade(player, attacker, ItemStack.EMPTY);
			}
		}
		return new DamageSource("mru")
		{

			@Override
			public Entity getImmediateSource()
			{
				return attacker;
			}
		}
		.setProjectile();
	}

	public void shoot(float f, float f1)
	{
		Vec3d vec = this.shootingEntity.getLookVec();
		for(int i = 0; i < 128; ++i)
		{
			float vX = (float) (vec.x*i/2F+this.posX);
			float vY = (float) (vec.y*i/2F+this.posY);
			float vZ = (float) (vec.z*i/2F+this.posZ);
			int bVX = MathHelper.floor(vX);
			int bVY = MathHelper.floor(vY);
			int bVZ = MathHelper.floor(vZ);
			double d = 0.5D;
			AxisAlignedBB aabb = new AxisAlignedBB(vX-d, vY-d, vZ-d, vX+d, vY+d, vZ+d);
			List<EntityLivingBase> entities = this.getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, aabb);
			for(int j = 0; j < entities.size(); ++j)
			{
				EntityLivingBase base = entities.get(j);
				if(base != shootingEntity && !base.isDead && !hitEntities.contains(base))
				{
					base.attackEntityFrom(causeMRUDamage(this.shootingEntity, base), this.damage);
					this.hitEntities.add(base);
				}
			}
			IBlockState b = this.getEntityWorld().getBlockState(new BlockPos(bVX, bVY, bVZ));
			if(b.isNormalCube() || i == 127)
			{
				this.setPositionAndRotation(vX, vY, vZ, 0, 0);
				break;
			}
		}
		this.shootingEntity.rotationYaw = f;
		this.shootingEntity.rotationPitch = f1;
	}

	@Override
	protected void entityInit()
	{
		this.getDataManager().register(DATA, "");
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tag)
	{
		pX = tag.getDouble("pX");
		pY = tag.getDouble("pY");
		pZ = tag.getDouble("pZ");
		balance = tag.getFloat("balance");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tag)
	{
		tag.setDouble("pX", pX);
		tag.setDouble("pY", pY);
		tag.setDouble("pZ", pZ);
		tag.setFloat("balance", balance);
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(ItemsCore.entityEgg,1,EntitiesCore.REGISTERED_ENTITIES.indexOf(ForgeRegistries.ENTITIES.getValue(EntityList.getKey(this.getClass()))));
	}
}
