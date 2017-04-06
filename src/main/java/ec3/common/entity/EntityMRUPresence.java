package ec3.common.entity;

import java.util.List;

import DummyCore.Utils.DummyData;
import ec3.api.IMRUPressence;
import ec3.common.block.BlockCorruption_Light;
import ec3.common.block.BlocksCore;
import ec3.common.item.ItemsCore;
import ec3.common.registry.SoundRegistry;
import ec3.utils.cfg.Config;
import ec3.utils.common.ECUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityMRUPresence extends EntityLivingBase implements IMRUPressence {

	public int mruAmount;
	public float balance = 1.0F;
	public float renderIndex;
	public boolean canStay = true;
	public boolean flag = false;
	public int tickTimer;
	public boolean firstTick = true;
	public static final DataParameter<Float> BALANCE = EntityDataManager.<Float>createKey(EntityMRUPresence.class, DataSerializers.FLOAT);
	public static final DataParameter<Integer> MRU = EntityDataManager.<Integer>createKey(EntityMRUPresence.class, DataSerializers.VARINT);
	public static final DataParameter<Boolean> STAY = EntityDataManager.<Boolean>createKey(EntityMRUPresence.class, DataSerializers.BOOLEAN);
	public static final DataParameter<Boolean> FLAG = EntityDataManager.<Boolean>createKey(EntityMRUPresence.class, DataSerializers.BOOLEAN);

	public EntityMRUPresence(World par1World) {
		super(par1World);
		this.setBalance(1.0F);
		this.setSize(0.3F, 0.3F);
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();

		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(BALANCE, 0F);
		this.dataManager.register(MRU, 1);
		this.dataManager.register(STAY, true);
		this.dataManager.register(FLAG, false);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
		if(!this.getEntityWorld().isRemote)
			this.setMRU(var1.getInteger("mru"));
		this.updateMRU();
		if(!this.getEntityWorld().isRemote)
			this.setBalance(var1.getFloat("Balance"));
		this.updateBalance();
		if(!this.getEntityWorld().isRemote)
			this.setAlwaysStay(var1.getBoolean("stay"));
		this.updateStay();
		if(!this.getEntityWorld().isRemote)
			this.setFlag(var1.getBoolean("flag"));
		this.updateFlag();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
		var1.setInteger("mru", this.getMRU());
		var1.setFloat("Balance", this.getBalance());
		var1.setBoolean("stay", this.canAlwaysStay());
		var1.setBoolean("flag", this.getFlag());
	}

	@Override
	protected boolean canTriggerWalking() { return false; }

	@Override
	protected void updateFallState(double par1, boolean par3, IBlockState par4, BlockPos par5) {}

	@Override
	public void setFire(int par1) {}

	@Override
	protected void setOnFireFromLava() {}

	@SuppressWarnings("unchecked")
	public void merge() {
		if(!this.isDead) {
			List<EntityMRUPresence> l = this.getEntityWorld().getEntitiesWithinAABB(EntityMRUPresence.class, new AxisAlignedBB(posX-0.5D, posY-0.5D, posZ-0.5D, posX+0.5D, posY+0.5D, posZ+0.5D));
			if(!l.isEmpty()) {
				for(int i = 0; i < l.size(); ++i) {
					EntityMRUPresence presence = l.get(i);
					if(presence != this && !presence.isDead) {
						presence.setDead();
						this.setMRU(this.getMRU()/2+presence.getMRU()/2);
						this.setBalance((this.getBalance()+presence.getBalance())/2);
					}
				}
			}
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		this.motionY = 0;
		this.motionX = 0;
		this.motionZ = 0;
		this.noClip = true;
		this.ignoreFrustumCheck = true;
		this.merge();
		if(!this.getEntityWorld().isRemote && !this.isDead) {
			if(tickTimer <= 0) {
				tickTimer = 20;
				float diff = 0F;
				Block id = BlocksCore.lightCorruption[3];
				if(getBalance() < 1F) {
					id = BlocksCore.lightCorruption[1];
					diff = 1F-getBalance();
				}
				if(getBalance() > 1F) {
					id = BlocksCore.lightCorruption[0];
					diff = getBalance()-1F;
				}
				float mainMRUState = (diff*getMRU()/60000F)*10F;
				Vec3d vec = new Vec3d(1, 0, 0);
				//Spinning the vec right 'round...
				vec.rotatePitch(this.getEntityWorld().rand.nextFloat()*360);
				vec.rotateYaw(this.getEntityWorld().rand.nextFloat()*360);
				vec.rotatePitch(this.getEntityWorld().rand.nextFloat()*360);
				vec.rotateYaw(this.getEntityWorld().rand.nextFloat()*360);
				if(!this.getFlag()) {
					for(int i = 0; i < mainMRUState; ++i) {
						//From now on even me, @Modbder, is unsure of the things, happening here. No idea, how and why this works, but it does.
						//It shouldn't
						Vec3d vc = new Vec3d(vec.xCoord*i, vec.yCoord*i, vec.zCoord*i);
						Vec3d vc1 = new Vec3d(vec.xCoord*(i+1), vec.yCoord*(i+1), vec.zCoord*(i+1));
						Block blk = this.getEntityWorld().getBlockState(new BlockPos((int)(vc.xCoord+posX),(int)(vc.yCoord+posY),(int)(vc.zCoord+posZ))).getBlock();
						Block blk1 = this.getEntityWorld().getBlockState(new BlockPos((int)(vc1.xCoord+posX),(int)(vc1.yCoord+posY),(int)(vc1.zCoord+posZ))).getBlock();
						int meta = blk1.getMetaFromState(this.getEntityWorld().getBlockState(new BlockPos((int)(vc1.xCoord+posX),(int)(vc1.yCoord+posY),(int)(vc1.zCoord+posZ))));
						float resistance = 1F;
						if(ECUtils.ignoreMeta.containsKey(blk1.getUnlocalizedName()) && ECUtils.ignoreMeta.get(blk1.getUnlocalizedName())) {
							meta = -1;
						}
						DummyData dt = new DummyData(blk1.getUnlocalizedName(),meta);
						if(ECUtils.mruResistance.containsKey(dt.toString())) {
							resistance = ECUtils.mruResistance.get(dt.toString());
						}
						else {
							resistance = 1F;
						}
						dt = null;
						if(Config.isCorruptionAllowed) {
							if(!(blk1 instanceof BlockCorruption_Light) && !(blk instanceof BlockCorruption_Light) && blk1 != Blocks.AIR && blk == Blocks.AIR) {
								if(!this.getEntityWorld().isRemote && this.getEntityWorld().rand.nextInt((int) (1000*resistance)) <= mainMRUState) {
									this.getEntityWorld().setBlockState(new BlockPos((int)(vc.xCoord+posX),(int)(vc.yCoord+posY),(int)(vc.zCoord+posZ)), id.getStateFromMeta(0), 3);
									break;
								}
							}
							if(blk instanceof BlockCorruption_Light) {
								int metadata = blk.getMetaFromState(this.getEntityWorld().getBlockState(new BlockPos((int)(vc.xCoord+posX),(int)(vc.yCoord+posY),(int)(vc.zCoord+posZ))));
								if(metadata < 7 && this.getEntityWorld().rand.nextInt((int) (1000*resistance)) <= mainMRUState)
								{
									this.getEntityWorld().setBlockState(new BlockPos((int)(vc.xCoord+posX),(int)(vc.yCoord+posY),(int)(vc.zCoord+posZ)), blk.getStateFromMeta(metadata+1), 3);
								}
							}
						}
					}
					List<EntityPlayer> players = this.getEntityWorld().getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(posX-0.5D, posY-0.5D, posZ-0.5D, posX+0.5D, posY+0.5D, posZ+0.5D).expand(12, 12, 12));
					for(int i = 0; i < players.size(); ++i) {
						EntityPlayer player = players.get(i);
						Vec3d playerCheck = new Vec3d(player.posX-this.posX, player.posY-this.posY, player.posZ-this.posZ);
						float resistance = 1F;
						for(double j = 0; j < playerCheck.lengthVector(); j += 0.5D) {
							double checkIndexX = playerCheck.xCoord / playerCheck.lengthVector() * j;
							double checkIndexY = playerCheck.yCoord / playerCheck.lengthVector() * j;
							double checkIndexZ = playerCheck.zCoord / playerCheck.lengthVector() * j;
							int dX = MathHelper.floor(posX+checkIndexX);
							int dY = MathHelper.floor(posY+checkIndexY);
							int dZ = MathHelper.floor(posZ+checkIndexZ);
							Block b = this.getEntityWorld().getBlockState(new BlockPos(dX, dY, dZ)).getBlock();
							int meta = b.getMetaFromState(this.getEntityWorld().getBlockState(new BlockPos(dX, dY, dZ)));

							if(ECUtils.ignoreMeta.containsKey(b.getUnlocalizedName()) && ECUtils.ignoreMeta.get(b.getUnlocalizedName()))
							{
								meta = -1;
							}
							DummyData dt = new DummyData(b.getUnlocalizedName(),meta);
							if(ECUtils.mruResistance.containsKey(dt.toString()))
							{
								if(resistance < ECUtils.mruResistance.get(dt.toString()))
									resistance = ECUtils.mruResistance.get(dt.toString());
							}else
							{
								if(resistance < 1)
									resistance = 1F;
							}
							dt = null;
						}
						playerCheck = null;
						if(this.getEntityWorld().rand.nextInt(MathHelper.floor(resistance)) == 0)
						{
							float genResistance = ECUtils.getGenResistance(0, player);
							if(genResistance >= 1.0F)genResistance = 0.99F;
							float matrixDamage = 4 * ((this.getMRU() / 10000) / (10-(genResistance*10)));
							if(matrixDamage >= 1)
							{
								ECUtils.getData(player).modifyOverhaulDamage(ECUtils.getData(player).getOverhaulDamage()+MathHelper.floor(matrixDamage));
							}
						}
					}
					players.clear();
					players = null;

				}
				else {
					this.setFlag(false);
				}
				vec = null;
			}
			else {
				--tickTimer;
			}
		}
		else {
			if(this.getEntityWorld().rand.nextFloat() < 0.025F)
				this.getEntityWorld().playSound(posX, posY, posZ, SoundRegistry.entityMRUCUNoise, SoundCategory.BLOCKS, (float)this.getMRU()/60000F, 0.1F+this.getEntityWorld().rand.nextFloat(), false);
		}
		if(this.getMRU() > 20000) {
			this.setMRU(20000);
		}

		renderIndex += 0.001F*this.balance;
		if(renderIndex>=1F)renderIndex=0F;
		this.updateMRU();
		this.updateBalance();
		this.updateStay();
		this.updateFlag();

		firstTick = false;
	}

	public void updateMRU() {
		if(!this.getEntityWorld().isRemote) {
			this.setMRU(this.getMRU());
		}
		if(this.getEntityWorld().isRemote) {
			if(!firstTick)
				this.mruAmount = this.getMRU();
		}
	}

	public void setMRU(int i) {
		if(this != null && !this.isDead)
			this.dataManager.set(MRU, i);
	}

	public int getMRU() {
		return this.dataManager.get(MRU);
	}

	public void updateBalance() {
		if(!this.getEntityWorld().isRemote) {
			this.setBalance(this.getBalance());
		}
		if(this.getEntityWorld().isRemote && !firstTick) {
			this.balance = this.getBalance();
		}
	}

	public float getBalance() {
		return ((float)this.dataManager.get(BALANCE));
	}

	public void setBalance(float f) {
		if(this != null && !this.isDead)
			this.dataManager.set(BALANCE, f);;
	}

	public boolean canAlwaysStay() {
		return this.dataManager.get(STAY);
	}

	public void setAlwaysStay(boolean b) {
		if(this != null && !this.isDead)
			this.dataManager.set(STAY, b);
	}

	public void updateStay() {
		if(!this.getEntityWorld().isRemote) {
			this.setAlwaysStay(this.canAlwaysStay());
		}
		if(this.getEntityWorld().isRemote && !firstTick) {
			this.canStay = this.canAlwaysStay();
		}
	}

	public void setFlag(boolean b) {
		if(this != null && !this.isDead)
			this.dataManager.set(FLAG, b);
	}

	public boolean getFlag() {
		return this.dataManager.get(FLAG);
	}

	public void updateFlag() {
		if(!this.getEntityWorld().isRemote) {
			this.setFlag(this.getFlag());
		}
		if(this.getEntityWorld().isRemote && !firstTick) {
			this.flag = this.getFlag();
		}
	}

	@Override
	public void fall(float par1, float par2) {}

	@Override
	protected void dealFireDamage(int par1) {}

	@Override
	public boolean handleWaterMovement() { return false; }

	@Override
	public boolean isInsideOfMaterial(Material par1Material) { return false; }

	@Override
	public void moveRelative(float par1, float par2, float par3) {}

	@Override
	public int getBrightnessForRender(float par1) { return 1; }

	@Override
	public float getBrightness(float par1) { return 1F; }

	@Override
	public void applyEntityCollision(Entity par1Entity) {}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) { return false; }

	@Override
	public boolean isEntityInsideOpaqueBlock() { return false; }

	@Override
	public float getCollisionBorderSize() { return 0F; }

	@Override
	public void onStruckByLightning(EntityLightningBolt par1EntityLightningBolt) {}

	@Override
	public ItemStack getHeldItem(EnumHand hand) { return null; }

	@Override
	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slot) { return null; }

	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slot, ItemStack p_70062_2_) {}

	@Override
	public Iterable<ItemStack> getArmorInventoryList() { return null; }

	@Override
	public EnumHandSide getPrimaryHand() { return EnumHandSide.RIGHT; }
	
	//Required to remove MRUCUs
	@Override
	public void onKillCommand() { setDead(); }

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(ItemsCore.entityEgg,1,EntitiesCore.registeredEntities.indexOf(this.getClass()));
	}
}
