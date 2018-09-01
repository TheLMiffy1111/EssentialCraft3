package essentialcraft.common.entity;

import java.util.Collections;
import java.util.List;

import DummyCore.Utils.DummyData;
import essentialcraft.common.block.BlockCorruption;
import essentialcraft.common.block.BlocksCore;
import essentialcraft.common.capabilities.mru.CapabilityMRUHandler;
import essentialcraft.common.capabilities.mru.MRUEntityStorage;
import essentialcraft.common.item.ItemsCore;
import essentialcraft.common.registry.SoundRegistry;
import essentialcraft.utils.cfg.Config;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
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
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class EntityMRUPresence extends EntityLivingBase {

	public float renderIndex;
	public int tickTimer;
	public boolean firstTick = true;
	public MRUEntityStorage mruStorage = new MRUEntityStorage(20000);

	public static final DataParameter<NBTTagCompound> MRU_STORAGE = EntityDataManager.<NBTTagCompound>createKey(EntityMRUPresence.class, DataSerializers.COMPOUND_TAG);

	public EntityMRUPresence(World world) {
		super(world);
		this.setSize(0.3F, 0.3F);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();

		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(MRU_STORAGE, new NBTTagCompound());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
		mruStorage.readFromNBT(var1);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
		mruStorage.writeToNBT(var1);
	}

	@Override
	protected boolean canTriggerWalking() { return false; }

	@Override
	protected void updateFallState(double par1, boolean par3, IBlockState par4, BlockPos par5) {}

	@Override
	public void setFire(int par1) {}

	@Override
	protected void setOnFireFromLava() {}

	public void merge() {
		if(!this.isDead) {
			List<EntityMRUPresence> l = this.getEntityWorld().getEntitiesWithinAABB(EntityMRUPresence.class, new AxisAlignedBB(posX-0.5D, posY-0.5D, posZ-0.5D, posX+0.5D, posY+0.5D, posZ+0.5D),
					entity->entity != this && entity.mruStorage.getMRU() <= this.mruStorage.getMRU());
			if(!l.isEmpty()) {
				for(int i = 0; i < l.size(); ++i) {
					EntityMRUPresence presence = l.get(i);
					if(!presence.isDead) {
						presence.setDead();
						mruStorage.setBalance((mruStorage.getBalance()*mruStorage.getMRU()+presence.mruStorage.getBalance()*presence.mruStorage.getMRU())/(mruStorage.getMRU()+presence.mruStorage.getMRU()));
						mruStorage.addMRU(presence.mruStorage.getMRU(), true);
					}
				}
			}
		}
	}

	@Override
	public void onEntityUpdate() {
		updateMRUStorage();
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
				if(mruStorage.getBalance() < 1F) {
					id = BlocksCore.lightCorruption[1];
					diff = 1F-mruStorage.getBalance();
				}
				if(mruStorage.getBalance() > 1F) {
					id = BlocksCore.lightCorruption[0];
					diff = mruStorage.getBalance()-1F;
				}
				float mainMRUState = diff*mruStorage.getMRU()/6000F;
				Vec3d vec = new Vec3d(1, 0, 0);

				vec = vec.rotatePitch(this.getEntityWorld().rand.nextFloat()*360);
				vec = vec.rotateYaw(this.getEntityWorld().rand.nextFloat()*360);
				if(!mruStorage.getFlag()) {
					for(int i = 0; i < mainMRUState; ++i) {
						Vec3d vc = new Vec3d(vec.x*i, vec.y*i, vec.z*i);
						Vec3d vc1 = new Vec3d(vec.x*(i+1), vec.y*(i+1), vec.z*(i+1));
						Block blk = this.getEntityWorld().getBlockState(new BlockPos((int)(vc.x+posX),(int)(vc.y+posY),(int)(vc.z+posZ))).getBlock();
						Block blk1 = this.getEntityWorld().getBlockState(new BlockPos((int)(vc1.x+posX),(int)(vc1.y+posY),(int)(vc1.z+posZ))).getBlock();
						int meta = blk1.getMetaFromState(this.getEntityWorld().getBlockState(new BlockPos((int)(vc1.x+posX),(int)(vc1.y+posY),(int)(vc1.z+posZ))));
						float resistance = 1F;
						if(ECUtils.IGNORE_META.containsKey(blk1.getUnlocalizedName()) && ECUtils.IGNORE_META.get(blk1.getUnlocalizedName())) {
							meta = -1;
						}
						DummyData dt = new DummyData(blk1.getUnlocalizedName(),meta);
						if(ECUtils.MRU_RESISTANCES.containsKey(dt.toString())) {
							resistance = ECUtils.MRU_RESISTANCES.get(dt.toString());
						}
						else {
							resistance = 1F;
						}
						if(Config.isCorruptionAllowed) {
							if(!(blk1 instanceof BlockCorruption) && !(blk instanceof BlockCorruption) && blk1 != Blocks.AIR && blk == Blocks.AIR) {
								if(!this.getEntityWorld().isRemote && this.getEntityWorld().rand.nextInt((int) (1000*resistance)) <= mainMRUState) {
									this.getEntityWorld().setBlockState(new BlockPos((int)(vc.x+posX),(int)(vc.y+posY),(int)(vc.z+posZ)), id.getStateFromMeta(0), 3);
									break;
								}
							}
							if(blk instanceof BlockCorruption) {
								int metadata = blk.getMetaFromState(this.getEntityWorld().getBlockState(new BlockPos((int)(vc.x+posX),(int)(vc.y+posY),(int)(vc.z+posZ))));
								if(metadata < 7 && this.getEntityWorld().rand.nextInt((int) (1000*resistance)) <= mainMRUState) {
									this.getEntityWorld().setBlockState(new BlockPos((int)(vc.x+posX),(int)(vc.y+posY),(int)(vc.z+posZ)), blk.getStateFromMeta(metadata+1), 3);
								}
							}
						}
					}
					List<EntityPlayer> players = this.getEntityWorld().getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(posX-0.5D, posY-0.5D, posZ-0.5D, posX+0.5D, posY+0.5D, posZ+0.5D).grow(12, 12, 12));
					for(int i = 0; i < players.size(); ++i) {
						EntityPlayer player = players.get(i);
						Vec3d playerCheck = new Vec3d(player.posX-this.posX, player.posY-this.posY, player.posZ-this.posZ);
						float resistance = 1F;
						for(double j = 0; j < playerCheck.lengthVector(); j += 0.5D) {
							double checkIndexX = playerCheck.x / playerCheck.lengthVector() * j;
							double checkIndexY = playerCheck.y / playerCheck.lengthVector() * j;
							double checkIndexZ = playerCheck.z / playerCheck.lengthVector() * j;
							int dX = MathHelper.floor(posX+checkIndexX);
							int dY = MathHelper.floor(posY+checkIndexY);
							int dZ = MathHelper.floor(posZ+checkIndexZ);
							Block b = this.getEntityWorld().getBlockState(new BlockPos(dX, dY, dZ)).getBlock();
							int meta = b.getMetaFromState(this.getEntityWorld().getBlockState(new BlockPos(dX, dY, dZ)));

							if(ECUtils.IGNORE_META.containsKey(b.getUnlocalizedName()) && ECUtils.IGNORE_META.get(b.getUnlocalizedName())) {
								meta = -1;
							}
							DummyData dt = new DummyData(b.getUnlocalizedName(),meta);
							if(ECUtils.MRU_RESISTANCES.containsKey(dt.toString())) {
								if(resistance < ECUtils.MRU_RESISTANCES.get(dt.toString()))
									resistance = ECUtils.MRU_RESISTANCES.get(dt.toString());
							}
							else {
								if(resistance < 1)
									resistance = 1F;
							}
						}
						if(this.getEntityWorld().rand.nextInt(MathHelper.floor(resistance)) == 0) {
							float genResistance = ECUtils.getGenResistance(0, player);
							if(genResistance >= 1.0F)genResistance = 0.99F;
							float matrixDamage = 4 * (mruStorage.getMRU() / 10000 / (10-genResistance*10));
							if(matrixDamage >= 1)
							{
								ECUtils.getData(player).modifyOverhaulDamage(ECUtils.getData(player).getOverhaulDamage()+MathHelper.floor(matrixDamage));
							}
						}
					}
					players.clear();

				}
				else {
					mruStorage.setFlag(false);
				}
			}
			else {
				--tickTimer;
			}
		}
		else {
			if(this.getEntityWorld().rand.nextFloat() < 0.025F)
				this.getEntityWorld().playSound(posX, posY, posZ, SoundRegistry.entityMRUCUNoise, SoundCategory.BLOCKS, mruStorage.getMRU()/60000F, 0.1F+this.getEntityWorld().rand.nextFloat(), false);
		}

		renderIndex += 0.001F*mruStorage.getBalance();
		if(renderIndex>4F)
			renderIndex=0F;

		firstTick = false;
	}

	protected void updateMRUStorage() {
		if(!this.world.isRemote) {
			this.dataManager.set(MRU_STORAGE, this.mruStorage.writeToNBT(new NBTTagCompound()));
		}
		else {
			this.mruStorage.readFromNBT(this.dataManager.get(MRU_STORAGE));
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
	public void moveRelative(float par1, float par2, float par3, float par4) {}

	@Override
	public int getBrightnessForRender() { return 1; }

	@Override
	public float getBrightness() { return 1F; }

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
	public ItemStack getHeldItem(EnumHand hand) { return ItemStack.EMPTY; }

	@Override
	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slot) { return ItemStack.EMPTY; }

	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slot, ItemStack p_70062_2_) {}

	@Override
	public Iterable<ItemStack> getArmorInventoryList() { return Collections.emptyList(); }

	@Override
	public EnumHandSide getPrimaryHand() { return EnumHandSide.RIGHT; }

	//Required to remove MRUCUs
	@Override
	public void onKillCommand() { setDead(); }

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(ItemsCore.entityEgg,1,EntitiesCore.REGISTERED_ENTITIES.indexOf(ForgeRegistries.ENTITIES.getValue(EntityList.getKey(this.getClass()))));
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityMRUHandler.MRU_HANDLER_ENTITY_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == CapabilityMRUHandler.MRU_HANDLER_ENTITY_CAPABILITY ? (T)mruStorage :super.getCapability(capability, facing);
	}
}
