package ec3.common.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ec3.common.item.ItemBaublesWearable;
import ec3.common.item.ItemsCore;
import ec3.common.mod.EssentialCraftCore;
import ec3.common.registry.AchievementRegistry;
import ec3.common.registry.SoundRegistry;
import ec3.common.world.WorldGenOldCatacombs;
import ec3.utils.common.ECUtils;
import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.MathUtils;
import DummyCore.Utils.MiscUtils;
import DummyCore.Utils.WeightedRandomChestContent;
import net.minecraft.entity.Entity;
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
import net.minecraft.stats.AchievementList;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.AchievementEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class EntityHologram extends EntityLiving {
	public static final double RANGE = 24;
	public static final DataParameter<String> DATA = EntityDataManager.<String>createKey(EntityHologram.class, DataSerializers.STRING);
	public int attackID = -1;
	public int attackTimer;
	public int restingTime;
	public int prevAttackID = -1;
	public int damage = 1;
	public double basePosX, basePosY, basePosZ;
	public List<String> players = new ArrayList<String>();
	public final BossInfoServer bossInfo = new BossInfoServer(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS);

	@Override
	public void fall(float distance, float damageMultiplier) {}

	protected void dropFewItems(boolean playerkill, int fortune) {
		if(!this.getEntityWorld().isRemote) {
			for(int i = 0; i < this.players.size(); ++i) {
				EntityPlayer p = MiscUtils.getPlayerFromUUID(this.players.get(i));
				if(p != null)
					p.addStat(AchievementRegistry.achievementList.get("hologram"));

				boolean addBig = true;

				for(int j = 0; j < 4; ++j) {
					if(p.inventory.armorInventory[j] != null)
						addBig = false;
				}

				if(addBig)
					p.addStat(AchievementRegistry.achievementList.get("hologramBig"));
			}
		}

		EssentialCraftCore.proxy.stopSound("hologram");

		int meta = 76;

		if(prevAttackID == -1)
			prevAttackID = this.getEntityWorld().rand.nextInt(4);

		if(prevAttackID == 0)
			meta = 70;

		if(prevAttackID == 1)
			meta = 73;

		if(prevAttackID == 2)
			meta = 72;

		if(prevAttackID == 3)
			meta = 71;

		this.entityDropItem(new ItemStack(ItemsCore.genericItem,1,meta), 0);

		if(this.getEntityWorld().rand.nextDouble() < 0.1D)
			this.entityDropItem(new ItemStack(ItemsCore.orbitalRemote,1,0), 0);

		if(this.getEntityWorld().rand.nextDouble() < 0.1D)
			this.entityDropItem(new ItemStack(ItemsCore.dividingGun,1,0), 0);

		if(this.getEntityWorld().rand.nextDouble() < 0.3D)
			this.entityDropItem(new ItemStack(ItemsCore.record_robocalypse,1,0), 0);

		World w = this.getEntityWorld();

		w.setBlockState(getPosition(), Blocks.CHEST.getDefaultState());
		TileEntityChest chest = (TileEntityChest)w.getTileEntity(getPosition());
		if(chest != null) {
			WeightedRandomChestContent.generateChestContents(w.rand, WorldGenOldCatacombs.generatedItems, chest, w.rand.nextInt(26)+18);
			IInventory inv = chest;
			for(int i = 0; i < inv.getSizeInventory(); ++i) {
				ItemStack stk = inv.getStackInSlot(i);
				if(stk != null && stk.getItem() instanceof ItemBaublesWearable) {
					ItemBaublesWearable.initRandomTag(stk, w.rand);
				}
			}
		}
	}

	public EntityHologram(World w) {
		super(w);
	}

	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(400.0D);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
	}

	@Override
	protected void entityInit() 
	{
		super.entityInit();
		this.getDataManager().register(DATA, "||null:null");
	}

	protected SoundEvent getAmbientSound()
	{
		return null;
	}

	protected SoundEvent getHurtSound()
	{
		return null;
	}

	protected SoundEvent getDeathSound()
	{
		return SoundRegistry.entityHologramShutdown;
	}

	public void dwWrite()
	{
		if(!this.getEntityWorld().isRemote)
			this.getDataManager().set(DATA, "||aID:"+attackID+"||aTi:"+attackTimer+"||rTi:"+restingTime);
	}

	public void dwRead()
	{
		if(this.getEntityWorld().isRemote)
		{
			String str = this.getDataManager().get(DATA);
			if(str != null && !str.isEmpty() && !str.equals("||null:null"))
			{
				try
				{
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

		if(this.posX != this.basePosX || this.posY != this.basePosY || this.posZ != this.basePosZ)
			this.setPositionAndRotation(basePosX, basePosY, basePosZ, rotationYaw, rotationPitch);

		//EssentialCraftCore.proxy.stopSound("hologram");
		if(this.deathTime != 0)
			EssentialCraftCore.proxy.stopSound("hologram");
		else if(!this.isDead)
			EssentialCraftCore.proxy.startRecord("hologram", "essentialcraft:records.hologram", getPosition());

		dwWrite();

		if(this.motionY < 0.002)
			this.motionY = 0.002;

		super.onUpdate();

		dwRead();

		if(this.isBurning())
			this.extinguish();

		if(!this.getActivePotionEffects().isEmpty())
			this.clearActivePotions();

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
									clone.setClonedPlayer(UUID.fromString(players.get(i)));
									clone.setPositionAndRotation(p.posX+MathUtils.randomDouble(rand)*6, p.posY, p.posZ+MathUtils.randomDouble(rand)*6, p.rotationYaw, p.rotationPitch);
									clone.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, p.getHeldItemMainhand() != null ? p.getHeldItemMainhand().copy() : null);
									clone.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, p.getHeldItemOffhand() != null ? p.getHeldItemOffhand().copy() : null);
									clone.setItemStackToSlot(EntityEquipmentSlot.HEAD, p.inventory.armorInventory[3] != null ? p.inventory.armorInventory[3].copy() : null);
									clone.setItemStackToSlot(EntityEquipmentSlot.CHEST, p.inventory.armorInventory[2] != null ? p.inventory.armorInventory[2].copy() : null);
									clone.setItemStackToSlot(EntityEquipmentSlot.LEGS, p.inventory.armorInventory[1] != null ? p.inventory.armorInventory[1].copy() : null);
									clone.setItemStackToSlot(EntityEquipmentSlot.FEET, p.inventory.armorInventory[0] != null ? p.inventory.armorInventory[0].copy() : null);

									this.getEntityWorld().spawnEntity(clone);
								}
							}
						}
					}
				}
				if(attackID == 1) {
					for(int i = 0; i < players.size(); ++i) {
						if(players.get(i) == null)
							continue;

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
						for(int i = 0; i < 1 + (5 - MathHelper.floor(this.getHealth()/this.getMaxHealth()*5)); ++i) {
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
				restingTime = 100 - MathHelper.floor(80-(this.getHealth()/this.getMaxHealth()*80));
				ECUtils.playSoundToAllNearby(posX, posY, posZ, "essentialcraft:sound.mob.hologram.stop", 5, 0.01F, 16, this.dimension);
			}
			if(restingTime > 0)
				--restingTime;
		}
		else {
			EntityPlayer p = EssentialCraftCore.proxy.getClientPlayer();
			if(p != null && p.capabilities.isFlying && p.getDistanceToEntity(this) <= RANGE && p.dimension == this.dimension) {
				p.capabilities.isFlying = false;
			}
		}

		if(!this.getEntityWorld().isRemote && this.ticksExisted % 10 == 0) {
			MinecraftServer server = getEntityWorld().getMinecraftServer();
			PlayerList manager = server.getPlayerList();
			for(int i = 0; i < players.size(); ++i) {
				EntityPlayer p = MiscUtils.getPlayerFromUUID(players.get(i));
				if(p == null || p.isDead)
					players.remove(i);
			}
			for(int i = 0; i < manager.getCurrentPlayerCount(); ++i) {
				EntityPlayerMP player = EntityPlayerMP.class.cast(manager.getPlayers().get(i));

				if(player == null)
					continue;

				if(this.players.contains(MiscUtils.getUUIDFromPlayer(player).toString())) {
					if(player.isDead) {
						players.remove(i);
						continue;
					}

					if(this.dimension != player.dimension)
						manager.changePlayerDimension(player, this.dimension);

					double distance = player.getDistanceToEntity(this);
					if(distance > RANGE) {
						player.setPositionAndRotation(posX, posY, posZ, player.rotationYaw, player.rotationPitch);
						ECUtils.changePlayerPositionOnClient(player);
						player.attackEntityFrom(DamageSource.causeMobDamage(this), 5);
						ECUtils.playSoundToAllNearby(posX, posY, posZ, "random.anvil_break", 1, 0.01F, 8, this.dimension);
					}

					if(player.capabilities.isFlying)
						player.capabilities.isFlying = false;

				}
				else {
					if(this.dimension != player.dimension)
						continue;

					double distance = player.getDistanceToEntity(this);
					if(distance <= RANGE) {
						this.players.add(MiscUtils.getUUIDFromPlayer(player).toString());
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
		for(int i = 0; i < players.size(); ++i)
			tag.setString("player_"+i, players.get(i));
	}

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
		for(int i = 0; i < tag.getInteger("listSize"); ++i)
			players.add(tag.getString("player_"+i));
	}

	@Override
	public void applyEntityCollision(Entity e) {}

	@Override
	protected void dropEquipment(boolean p_82160_1_, int p_82160_2_) {}

	@Override
	protected void collideWithEntity(Entity e) {}

	public boolean attackEntityFrom(DamageSource src, float f) {
		if(this.attackID != -1)
			return false;

		if(src == null)
			return false;

		if(src.getSourceOfDamage() == null)
			return false;

		if(!(src.getSourceOfDamage() instanceof EntityPlayer))
			return false;

		if(src.getSourceOfDamage() instanceof FakePlayer)
			return false;

		damage += f;

		if(f > 40 || damage > 40)
			this.restingTime = 1;

		if(src.isProjectile())
			f /= 4;

		if(src.isProjectile())
			ECUtils.playSoundToAllNearby(posX, posY, posZ, "essentialcraft:sound.mob.hologram.damage.projectile", 5, this.getEntityWorld().rand.nextFloat()*2, 16, this.dimension);
		else
			ECUtils.playSoundToAllNearby(posX, posY, posZ, "essentialcraft:sound.mob.hologram.damage.melee", 0.3F, this.getEntityWorld().rand.nextFloat()*2, 16, this.dimension);

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
		return new ItemStack(ItemsCore.entityEgg,1,EntitiesCore.registeredEntities.indexOf(this.getClass()));
	}
}
