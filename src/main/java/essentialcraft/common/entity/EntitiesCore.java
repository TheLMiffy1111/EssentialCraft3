package essentialcraft.common.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.StreamSupport;

import essentialcraft.common.mod.EssentialCraftCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class EntitiesCore {
	public static final List<EntityEntry> REGISTERED_ENTITIES = new ArrayList<EntityEntry>();

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
		ResourceLocation rl = new ResourceLocation("essentialcraft:"+entityClass.getSimpleName().toLowerCase(Locale.US));
		EntityRegistry.registerModEntity(rl, entityClass, entityClass.getName(), nextID(), EssentialCraftCore.core, trackingRange, tickDelay, trackRotation);
		REGISTERED_ENTITIES.add(ForgeRegistries.ENTITIES.getValue(rl));
	}

	public static int id = -1;

	private static int nextID() {
		return ++id;
	}

	public static Biome[] biomesToSpawn() {
		return StreamSupport.<Biome>stream(Biome.REGISTRY.spliterator(), false).filter(biome->biome != null &&
				biome != Biomes.HELL &&
				biome != Biomes.SKY &&
				biome.getSpawnableList(EnumCreatureType.MONSTER) != null &&
				!biome.getSpawnableList(EnumCreatureType.MONSTER).isEmpty() &&
				!BiomeDictionary.hasType(biome, Type.END) &&
				!BiomeDictionary.hasType(biome, Type.NETHER)).<Biome>toArray(size->new Biome[size]);
	}

	public static void fixMappings() {

	}
}
