package essentialcraft.common.tile;

import java.util.List;

import com.google.common.collect.Lists;

import DummyCore.Utils.MathUtils;
import DummyCore.Utils.MiscUtils;
import essentialcraft.api.ApiCore;
import essentialcraft.common.item.ItemCollectedMonsterSpawner;
import essentialcraft.common.item.ItemsCore;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class TileDarknessObelisk extends TileMRUGeneric {
	public static int cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;
	public static boolean generatesCorruption = false;
	public static int genCorruption = 30;
	public static int mruUsage = 500;
	public static double worldSpawnChance = 0.1D;
	public static int mobSpanwerDelay = 100;
	public static boolean enableMobSpawners = true;
	public static int mobSpawnerRadius = 3, worldSpawnerRadius = 8;

	private NBTTagCompound prevTag = null;
	private List<WeightedSpawnerEntity> potentialSpawns = Lists.<WeightedSpawnerEntity>newArrayList();

	public TileDarknessObelisk() {
		super(cfgMaxMRU);
		setSlotsNum(2);
	}

	@Override
	public void update() {
		mruStorage.update(getPos(), getWorld(), getStackInSlot(0));
		if(getWorld().isBlockIndirectlyGettingPowered(getPos()) == 0) {
			if(getStackInSlot(1).isEmpty() || !(getStackInSlot(1).getItem() instanceof ItemCollectedMonsterSpawner)) {
				Biome biome = getWorld().getBiome(getPos());
				List<Biome.SpawnListEntry> l = biome.getSpawnableList(EnumCreatureType.MONSTER);
				if(l != null && !l.isEmpty() && getWorld().rand.nextDouble() < worldSpawnChance && mruStorage.getMRU() >= mruUsage) {
					if(!getWorld().isRemote) {
						Biome.SpawnListEntry spawnlistentry = null;
						IEntityLivingData ientitylivingdata = null;
						int rndOffsetX = (int)(getPos().getX() + MathUtils.randomDouble(getWorld().rand) * worldSpawnerRadius);
						int rndOffsetY = (int)(getPos().getY() + MathUtils.randomDouble(getWorld().rand) * worldSpawnerRadius);
						int rndOffsetZ = (int)(getPos().getZ() + MathUtils.randomDouble(getWorld().rand) * worldSpawnerRadius);
						BlockPos rndPos = new BlockPos(rndOffsetX, rndOffsetY, rndOffsetZ);
						if(WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, getWorld(), rndPos)) {
							WorldServer wrld = (WorldServer)getWorld();
							spawnlistentry = wrld.getSpawnListEntryForTypeAt(EnumCreatureType.MONSTER, rndPos);
							if(spawnlistentry != null) {
								EntityLiving entityliving;

								try {
									entityliving = spawnlistentry.newInstance(world);
									entityliving.setLocationAndAngles((double)rndOffsetX+0.5F, rndOffsetY, rndOffsetZ+0.5D, wrld.rand.nextFloat()*360.0F, 0.0F);
									Result canSpawn = ForgeEventFactory.canEntitySpawn(entityliving, wrld, rndOffsetX+0.5F, rndOffsetY, rndOffsetZ+0.5F, false);
									if(canSpawn == Result.ALLOW || canSpawn == Result.DEFAULT && entityliving.getCanSpawnHere()) {
										wrld.spawnEntity(entityliving);
										mruStorage.extractMRU(mruUsage, true);
										if(generatesCorruption)
											ECUtils.randomIncreaseCorruptionAt(getWorld(), getPos(), getWorld().rand, genCorruption);
										if(!ForgeEventFactory.doSpecialSpawn(entityliving, wrld, rndOffsetX+0.5F, rndOffsetY, rndOffsetZ+0.5F)) {
											ientitylivingdata = entityliving.onInitialSpawn(getWorld().getDifficultyForLocation(rndPos), ientitylivingdata);
										}
									}
								}
								catch (Exception exception) {
									exception.printStackTrace();
									return;
								}
							}
						}
					}
				}
			}
			else if(getStackInSlot(1).getItem() instanceof ItemCollectedMonsterSpawner && enableMobSpawners) {
				if(innerRotation % mobSpanwerDelay == 0 && mruStorage.getMRU() >= mruUsage) {
					NBTTagCompound tag = MiscUtils.getStackTag(getStackInSlot(1));
					if(tag.hasKey("monsterSpawner")) {
						NBTTagCompound spTag = tag.getCompoundTag("monsterSpawner");
						NBTTagCompound mobTag = getMobTag(spTag);
						if(mobTag != null && mobTag.hasKey("id")) {
							String id = mobTag.getString("id");
							innerRotation = 0;
							int metadata = getStackInSlot(1).getItemDamage();
							Entity base = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(id)).newInstance(getWorld());
							if(base != null && base instanceof EntityLiving) {
								EntityLiving entityliving = (EntityLiving) base;
								int rndOffsetX = (int)(pos.getX() + MathUtils.randomDouble(getWorld().rand)*mobSpawnerRadius);
								int rndOffsetY = (int)(pos.getY() + MathUtils.randomDouble(getWorld().rand));
								int rndOffsetZ = (int)(pos.getZ() + MathUtils.randomDouble(getWorld().rand)*mobSpawnerRadius);
								entityliving.setLocationAndAngles(rndOffsetX+0.5D, rndOffsetY, rndOffsetZ+0.5D, getWorld().rand.nextFloat()*360.0F, 0.0F);
								if(entityliving.getCanSpawnHere()) {
									if(!getWorld().isRemote)
										getWorld().spawnEntity(entityliving);

									getWorld().playEvent(2004, pos, 0);

									if(entityliving != null)
										entityliving.spawnExplosionParticle();
									mruStorage.extractMRU(mruUsage, true);
									if(generatesCorruption)
										ECUtils.randomIncreaseCorruptionAt(getWorld(), getPos(), getWorld().rand, genCorruption);
								}
							}
						}
					}
				}
			}
		}
		super.update();

		//The following code is to make the secret record acquirable
		List<EntityPlayer> playerlist = getWorld().getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos).grow(8));
		if(!playerlist.isEmpty()) {
			if(world.rand.nextInt(10000) == 0) {
				EntityItem ei = new EntityItem(world, pos.getX()+0.5, pos.getY()+1, pos.getZ()+0.5, new ItemStack(ItemsCore.secret,1,0));
				getWorld().spawnEntity(ei);
			}
		}
	}

	public NBTTagCompound getMobTag(NBTTagCompound nbt) {
		if(prevTag == null || !prevTag.equals(nbt)) {
			prevTag = nbt;

			if(nbt.hasKey("SpawnPotentials", 9)) {
				NBTTagList nbttaglist = nbt.getTagList("SpawnPotentials", 10);

				for(int i = 0; i < nbttaglist.tagCount(); ++i) {
					this.potentialSpawns.add(new WeightedSpawnerEntity(nbttaglist.getCompoundTagAt(i)));
				}
			}
		}

		if(nbt.hasKey("SpawnData", 10)) {
			return nbt.getCompoundTag("SpawnData");
		}
		else if(!potentialSpawns.isEmpty()) {
			return WeightedRandom.getRandomItem(getWorld().rand, potentialSpawns).getNbt();
		}
		return null;
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.darknessobelisk";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.DEVICE_MAX_MRU_GENERIC).setMinValue(1).getInt();
			mruUsage = cfg.get(category, "MRUUsage", 500).setMinValue(0).setMaxValue(cfgMaxMRU).getInt();
			mobSpanwerDelay = cfg.get(category, "SpawnerDelay", 100).setMinValue(1).getInt();
			generatesCorruption = cfg.get(category, "GenerateCorruption", false).getBoolean();
			genCorruption = cfg.get(category, "MaxCorruptionGen", 30, "Max amount of corruption generated per tick").setMinValue(0).getInt();
			worldSpawnChance = cfg.get(category, "SpawnChance", 0.1D, "Chance per tick to try spawning a mob without a spawner").setMinValue(0D).setMaxValue(1D).getDouble();
			enableMobSpawners = cfg.get(category, "EnableSpawners", true).getBoolean();
			mobSpawnerRadius = cfg.get(category, "SpawnerRadius", 3).setMinValue(0).getInt();
			worldSpawnerRadius = cfg.get(category, "NoSpawnerRadius", 8).setMinValue(0).getInt();
		}
		catch(Exception e) {
			return;
		}
	}

	@Override
	public int[] getOutputSlots() {
		return new int[0];
	}
}
