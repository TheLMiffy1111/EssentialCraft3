package essentialcraft.common.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.MathUtils;
import DummyCore.Utils.MiscUtils;
import DummyCore.Utils.WeightedRandomChestContent;
import essentialcraft.common.item.ItemBaublesResistance;
import essentialcraft.common.item.ItemsCore;
import essentialcraft.common.mod.EssentialCraftCore;
import essentialcraft.common.registry.LootTableRegistry;
import essentialcraft.common.registry.SoundRegistry;
import essentialcraft.common.world.gen.structure.StructureOldCatacombs;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class EntityHologram extends EntityLiving {
	public static final double RANGE = 24;
	public static final DataParameter<String> DATA = EntityDataManager.<String>createKey(EntityHologram.class, DataSerializers.STRING);
	public int attackID = -1;
	public int attackTimer;
	public int restingTime;
	public int prevAttackID = -1;
	public int damage = 1;
	public double basePosX, basePosY, basePosZ;
	public List<UUID> players = new ArrayList<UUID>();
	public final BossInfoServer bossInfo = new BossInfoServer(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS);

	@Override
	public void fall(float distance, float damageMultiplier) {}

	@Override
	protected ResourceLocation getLootTable() {
		if(prevAttackID == -1) {
			prevAttackID = this.getEntityWorld().rand.nextInt(4);
		}
		switch(prevAttackID) {
		case 0: return LootTableRegistry.ENTITY_HOLOGRAM_ADDITION;
		case 1: return LootTableRegistry.ENTITY_HOLOGRAM_SUBTRACTION;
		case 2: return LootTableRegistry.ENTITY_HOLOGRAM_MULTIPLICATION;
		case 3: return LootTableRegistry.ENTITY_HOLOGRAM_DIVISION;
		default: return null;
		}
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if(!this.getEntityWorld().isRemote) {
			for(int i = 0; i < this.players.size(); ++i) {
				EntityPlayer p = MiscUtils.getPlayerFromUUID(this.players.get(i));
				boolean addBig = true;
				for(int j = 0; j < 4; ++j) {
					if(!p.inventory.armorInventory.get(j).isEmpty()) {
						addBig = false;
					}
				}
				if(addBig) {
					//Add the advancement here
				}
			}
		}
		EssentialCraftCore.proxy.stopSound("hologram");
		World w = this.getEntityWorld();
		w.setBlockState(this.getPosition(), Blocks.CHEST.getDefaultState());
		TileEntityChest chest = (TileEntityChest)w.getTileEntity(this.getPosition());
		if(chest != null) {
			chest.setLootTable(LootTableRegistry.CHEST_HOLOGRAM, w.rand.nextLong());
		}
	}

	public EntityHologram(World w) {
		super(w);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(400.0D);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.getDataManager().register(DATA, "||null:null");
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return null;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource s) {
		return null;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundRegistry.entityHologramShutdown;
	}

	public void dwWrite() {
		if(!this.getEntityWorld().isRemote) {
			this.getDataManager().set(DATA, "||aID:"+attackID+"||aTi:"+attackTimer+"||rTi:"+restingTime);
		}
	}

	public void dwRead() {
		if(this.getEntityWorld().isRemote) {
			String str = this.getDataManager().get(DATA);
			if(str != null && !str.isEmpty() && !str.equals("||null:null")) {
				try {
					DummyData[] genDat = DataStorage.parseData(str);
					attackID = Integer.parseInt(genDat[0].fieldValue);
					attackTimer = Integer.parseInt(genDat[1].fieldValue);
					restingTime = Integer.parseInt(genDat[2].fieldValue);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onUpdate() {
		if(this.ticksExisted == 1) {
			this.basePosX = posX;
			this.basePosY = posY;
			this.basePosZ = posZ;
		}
		if(this.posX != this.basePosX || this.posY != this.basePosY || this.posZ != this.basePosZ) {
			this.setPositionAndRotation(basePosX, basePosY, basePosZ, rotationYaw, rotationPitch);
		}
		if(this.deathTime != 0) {
			EssentialCraftCore.proxy.stopSound("hologram");
		}
		else if(!this.isDead) {
			EssentialCraftCore.proxy.startRecord("hologram", "essentialcraft:records.hologram", getPosition());
		}
		dwWrite();
		if(this.motionY < 0.002) {
			this.motionY = 0.002;
		}
		super.onUpdate();
		dwRead();
		if(this.isBurning()) {
			this.extinguish();
		}
		if(!this.getActivePotionEffects().isEmpty()) {
			this.clearActivePotions();
		}
		if(!this.getEntityWorld().isRemote) {
			if(restingTime == 0 && attackID == -1) {
				int rndID = this.getEntityWorld().rand.nextInt(4);
				attackID = rndID;
				attackTimer = 100;
				ECUtils.playSoundToAllNearby(posX, posY, posZ, "essentialcraft:sound.mob.hologram.stop", 5, 2F, 16, this.dimension);
				damage = 1;
			}
			if(attackTimer != 0 && attackID != -1) {
				--attackTimer;
				if(attackID == 0) {
					if(attackTimer == 20) {
						int hMax = 3 - MathHelper.floor(getHealth()/getMaxHealth() * 3);
						for(int i = 0; i < players.size(); ++i) {
							EntityPlayer p = MiscUtils.getPlayerFromUUID(players.get(i));
							if(p != null) {
								for(int j = 0; j < 1 + hMax; ++j) {
									EntityPlayerClone clone = new EntityPlayerClone(p.getEntityWorld());
									clone.setClonedPlayer(players.get(i));
									clone.setPositionAndRotation(p.posX+MathUtils.randomDouble(rand)*6, p.posY, p.posZ+MathUtils.randomDouble(rand)*6, p.rotationYaw, p.rotationPitch);
									clone.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, !p.getHeldItemMainhand().isEmpty() ? p.getHeldItemMainhand().copy() : ItemStack.EMPTY);
									clone.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, p.getHeldItemOffhand().isEmpty() ? p.getHeldItemOffhand().copy() : ItemStack.EMPTY);
									clone.setItemStackToSlot(EntityEquipmentSlot.HEAD, p.inventory.armorInventory.get(3).isEmpty() ? p.inventory.armorInventory.get(3).copy() : ItemStack.EMPTY);
									clone.setItemStackToSlot(EntityEquipmentSlot.CHEST, p.inventory.armorInventory.get(2).isEmpty() ? p.inventory.armorInventory.get(2).copy() : ItemStack.EMPTY);
									clone.setItemStackToSlot(EntityEquipmentSlot.LEGS, p.inventory.armorInventory.get(1).isEmpty() ? p.inventory.armorInventory.get(1).copy() : ItemStack.EMPTY);
									clone.setItemStackToSlot(EntityEquipmentSlot.FEET, p.inventory.armorInventory.get(0).isEmpty() ? p.inventory.armorInventory.get(0).copy() : ItemStack.EMPTY);

									this.getEntityWorld().spawnEntity(clone);
								}
							}
						}
					}
				}
				if(attackID == 1) {
					for(int i = 0; i < players.size(); ++i) {
						if(players.get(i) == null) {
							continue;
						}
						this.faceEntity(MiscUtils.getPlayerFromUUID(players.get(i)), 360F, 180F);
						EntityArmorDestroyer destr = new EntityArmorDestroyer(this.getEntityWorld(),this);
						destr.setHeadingFromThrower(this, this.rotationPitch, this.rotationYaw, 0.0F, 1.5F, 0.5F);
						this.rotationYaw = this.getEntityWorld().rand.nextFloat()*360;
						this.rotationPitch = 90-this.getEntityWorld().rand.nextFloat()*180;
						this.getEntityWorld().spawnEntity(destr);
					}
				}
				if(attackID == 2) {
					if(this.attackTimer % 10 == 0) {
						if(this.players.size() > 0) {
							int i = this.getEntityWorld().rand.nextInt(this.players.size());
							EntityPlayer p = MiscUtils.getPlayerFromUUID(players.get(i));
							if(p != null) {
								EntityOrbitalStrike strike = new EntityOrbitalStrike(getEntityWorld(), p.posX, p.posY, p.posZ, damage, 3 - (2 - this.getHealth()/this.getMaxHealth()*2), this);
								this.getEntityWorld().spawnEntity(strike);
							}
							damage *= 2;
						}
					}
				}
				if(attackID == 3) {
					if(this.attackTimer % 20 == 0) {
						for(int i = 0; i < 1 + 5 - MathHelper.floor(this.getHealth()/this.getMaxHealth()*5); ++i) {
							if(this.players.size() > 0) {
								int i1 = this.getEntityWorld().rand.nextInt(this.players.size());
								EntityPlayer p = MiscUtils.getPlayerFromUUID(players.get(i1));
								if(p != null) {
									EntityDivider d = new EntityDivider(getEntityWorld(), p.posX, p.posY, p.posZ, damage, 2, this);
									this.getEntityWorld().spawnEntity(d);
								}
							}
						}
					}
				}
			}
			if(attackTimer == 0 && attackID != -1) {
				prevAttackID = attackID;
				attackID = -1;
				restingTime = 100 - MathHelper.floor(80-this.getHealth()/this.getMaxHealth()*80);
				ECUtils.playSoundToAllNearby(posX, posY, posZ, "essentialcraft:sound.mob.hologram.stop", 5, 0.01F, 16, this.dimension);
			}
			if(restingTime > 0) {
				--restingTime;
			}
		}
		else {
			EntityPlayer p = EssentialCraftCore.proxy.getClientPlayer();
			if(p != null && !p.isCreative() && !p.isSpectator() && p.capabilities.isFlying && p.getDistanceToEntity(this) <= RANGE && p.dimension == this.dimension) {
				p.capabilities.isFlying = false;
			}
		}

		if(!this.getEntityWorld().isRemote && this.ticksExisted % 10 == 0) {
			MinecraftServer server = getEntityWorld().getMinecraftServer();
			PlayerList manager = server.getPlayerList();
			for(int i = 0; i < players.size(); ++i) {
				EntityPlayer p = MiscUtils.getPlayerFromUUID(players.get(i));
				if(p == null || p.isDead) {
					players.remove(i);
					--i;
				}
			}
			for(int i = 0; i < manager.getCurrentPlayerCount(); ++i) {
				EntityPlayerMP player = manager.getPlayers().get(i);
				if(player == null) {
					continue;
				}
				if(this.players.contains(MiscUtils.getUUIDFromPlayer(player))) {
					if(player.isDead) {
						players.remove(MiscUtils.getUUIDFromPlayer(player));
						continue;
					}
					if(this.dimension != player.dimension) {
						manager.changePlayerDimension(player, this.dimension);
					}
					double distance = player.getDistanceToEntity(this);
					if(distance > RANGE) {
						player.setPositionAndRotation(posX, posY, posZ, player.rotationYaw, player.rotationPitch);
						ECUtils.changePlayerPositionOnClient(player);
						player.attackEntityFrom(DamageSource.causeMobDamage(this), 5);
						ECUtils.playSoundToAllNearby(posX, posY, posZ, "random.anvil_break", 1, 0.01F, 8, this.dimension);
					}
					if(player.capabilities.isFlying) {
						player.capabilities.isFlying = false;
					}
				}
				else {
					if(this.dimension != player.dimension) {
						continue;
					}
					double distance = player.getDistanceToEntity(this);
					if(distance <= RANGE) {
						this.players.add(MiscUtils.getUUIDFromPlayer(player));
					}
				}
			}
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		tag.setInteger("attackID", attackID);
		tag.setInteger("attackTimer", attackTimer);
		tag.setInteger("restingTime", restingTime);
		tag.setInteger("prevAttackID", prevAttackID);
		tag.setInteger("listSize", players.size());
		tag.setInteger("damage", damage);
		tag.setDouble("basePosX", basePosX);
		tag.setDouble("basePosY", basePosY);
		tag.setDouble("basePosZ", basePosZ);
		for(int i = 0; i < players.size(); ++i) {
			tag.setUniqueId("player_"+i, players.get(i));
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		attackID = tag.getInteger("attackID");
		attackTimer = tag.getInteger("attackTimer");
		restingTime = tag.getInteger("restingTime");
		prevAttackID = tag.getInteger("prevAttackID");
		damage = tag.getInteger("damage");
		basePosX = tag.getDouble("basePosX");
		basePosY = tag.getDouble("basePosY");
		basePosZ = tag.getDouble("basePosZ");
		for(int i = 0; i < tag.getInteger("listSize"); ++i) {
			players.add(tag.getUniqueId("player_"+i));
		}
	}

	@Override
	public void applyEntityCollision(Entity e) {}

	@Override
	protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier) {}

	@Override
	protected void collideWithEntity(Entity e) {}

	@Override
	public boolean attackEntityFrom(DamageSource src, float f) {
		if(src == null) {
			return false;
		}
		if(src.getTrueSource() == null) {
			return false;
		}
		if(!(src.getTrueSource() instanceof EntityPlayer)) {
			return false;
		}
		if(src.getTrueSource() instanceof FakePlayer) {
			return false;
		}
		if(!((EntityPlayer)src.getTrueSource()).isCreative() && this.attackID != -1) {
			return false;
		}
		damage += f;
		if(f > 40 || damage > 40) {
			this.restingTime = 1;
		}
		if(src.isProjectile()) {
			f /= 4;
		}
		if(src.isProjectile()) {
			ECUtils.playSoundToAllNearby(posX, posY, posZ, "essentialcraft:sound.mob.hologram.damage.projectile", 5, this.getEntityWorld().rand.nextFloat()*2, 16, this.dimension);
		}
		else {
			ECUtils.playSoundToAllNearby(posX, posY, posZ, "essentialcraft:sound.mob.hologram.damage.melee", 0.3F, this.getEntityWorld().rand.nextFloat()*2, 16, this.dimension);
		}
		return super.attackEntityFrom(src, f);
	}

	@Override
	public boolean isNonBoss() {
		return false;
	}

	@Override
	protected void updateAITasks() {
		super.updateAITasks();
		bossInfo.setPercent(getHealth() / getMaxHealth());
	}

	@Override
	public void addTrackingPlayer(EntityPlayerMP player) {
		super.addTrackingPlayer(player);
		bossInfo.addPlayer(player);
	}

	@Override
	public void removeTrackingPlayer(EntityPlayerMP player) {
		super.removeTrackingPlayer(player);
		bossInfo.removePlayer(player);
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(ItemsCore.entityEgg, 1, EntitiesCore.REGISTERED_ENTITIES.indexOf(ForgeRegistries.ENTITIES.getValue(EntityList.getKey(this.getClass()))));
	}
}
