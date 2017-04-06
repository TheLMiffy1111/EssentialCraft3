package ec3.common.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import ec3.common.mod.EssentialCraftCore;
import ec3.utils.cfg.Config;

public class EntitiesCore {
	public static final List<Class<? extends Entity>> registeredEntities = new ArrayList<Class<? extends Entity>>(); 
	
	public static void registerEntities() {
		registerEntity(EntityMRUPresence.class, 64, 1, true);
		registerEntity(EntityMRUArrow.class, 64, 1, true);
		registerEntity(EntitySolarBeam.class, 64, 1, true);
		registerEntity(EntityWindMage.class, 64, 1, true);
		registerEntity(EntityPoisonFume.class, 64, 1, true);
		registerEntity(EntityShadowKnife.class, 32, 1, true);
		registerEntity(EntityMRURay.class, 128, 1, true);
		registerEntity(EntityDemon.class, 32, 1, true);
		registerEntity(EntityHologram.class, 32, 1, true);
		registerEntity(EntityPlayerClone.class, 32, 1, true);
		registerEntity(EntityOrbitalStrike.class, 32, 1, true);
		registerEntity(EntityDivider.class, 32, 1, true);
		registerEntity(EntityArmorDestroyer.class, 32, 1, true);
		registerEntity(EntityDividerProjectile.class, 32, 1, true);
		
		EntityRegistry.addSpawn(EntityWindMage.class, 2, 1, 6, EnumCreatureType.MONSTER, biomesToSpawn());
		EntityRegistry.addSpawn(EntityPoisonFume.class, 100, 8, 16, EnumCreatureType.MONSTER, biomesToSpawn());
	}
	
	public static void registerEntity(Class<? extends Entity> entityClass, int trackingRange, int tickDelay, boolean trackRotation) {
		EntityRegistry.registerModEntity(entityClass, entityClass.getName(), nextID(), EssentialCraftCore.core, trackingRange, tickDelay, trackRotation);
		registeredEntities.add(entityClass);
	}
	
	public static int id = -1;
	
	private static int nextID() {
		return ++id;
	}
	
	public static int nextEntityID(int defaultID)
	{
		if(Config.autoFindEID)
			return defaultID;
		for(int i = defaultID; i < 512; ++i)
		{
			if(EntityList.getClassFromID(i) == null)
				return i;
		}
		return defaultID;
	}
	
	public static Biome[] biomesToSpawn() {
		List<Biome> spawnLst = new ArrayList<Biome>();
		for(Biome biome : Biome.REGISTRY)
		{
			if(biome != null &&
					biome != Biomes.HELL &&
					biome != Biomes.SKY &&
					biome.getSpawnableList(EnumCreatureType.MONSTER) != null &&
					!biome.getSpawnableList(EnumCreatureType.MONSTER).isEmpty() &&
					!BiomeDictionary.isBiomeOfType(biome, Type.END)  &&
					!BiomeDictionary.isBiomeOfType(biome, Type.NETHER)) {
				spawnLst.add(biome);
			}
		}
		Biome[] biomesArray = new Biome[spawnLst.size()];
		for(int i = 0; i < spawnLst.size(); ++i) {
			biomesArray[i] = spawnLst.get(i);
		}
		return biomesArray;
	}
}
