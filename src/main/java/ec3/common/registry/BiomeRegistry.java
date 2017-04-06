package ec3.common.registry;

import org.apache.logging.log4j.LogManager;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ec3.common.world.biome.BiomeGenCorruption_Chaos;
import ec3.common.world.biome.BiomeGenCorruption_Frozen;
import ec3.common.world.biome.BiomeGenCorruption_Magic;
import ec3.common.world.biome.BiomeGenCorruption_Shadow;
import ec3.common.world.biome.BiomeGenFirstWorld_Desert;
import ec3.common.world.biome.BiomeGenFirstWorld_Dreadlands;
import ec3.utils.cfg.Config;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.event.terraingen.WorldTypeEvent;

public class BiomeRegistry {
	
	public BiomeRegistry() {
		//MinecraftForge.TERRAIN_GEN_BUS.register(this);
	}
	
	@SubscribeEvent
	public void manageBiomeGen(WorldTypeEvent.InitBiomeGens event) {
	}
	
	public static void register() {
		chaosCorruption = new BiomeGenCorruption_Chaos(new BiomeProperties("Corrupted Land")).setRegistryName("essentialcraft:chaosCorruption");
		frozenCorruption = new BiomeGenCorruption_Frozen(new BiomeProperties("Corrupted Land")).setRegistryName("essentialcraft:frozenCorruption");
		shadowCorruption = new BiomeGenCorruption_Shadow(new BiomeProperties("Corrupted Land")).setRegistryName("essentialcraft:shadowCorruption");
		magicCorruption = new BiomeGenCorruption_Magic(new BiomeProperties("Corrupted Land")).setRegistryName("essentialcraft:magicCorruption");
		dreadlands = new BiomeGenFirstWorld_Dreadlands(new BiomeProperties("dreadlands").setRainDisabled().setTemperature(2F).setRainfall(0F).setBaseHeight(0.125F).setHeightVariation(0.126F))
				.setGrassColor(0x889688).setWaterColor(0x889688).setLeavesColor(0x889688).setRegistryName("essentialcraft:dreadlands");
		desert = new BiomeGenFirstWorld_Desert(new BiomeProperties("desert").setRainDisabled().setTemperature(2F).setRainfall(0F).setBaseHeight(0.125F).setHeightVariation(0.05F))
				.setGrassColor(16421912).setLeavesColor(16421912).setWaterColor(16421912).setRegistryName("essentialcraft:desert");
		
		GameRegistry.register(chaosCorruption);
		GameRegistry.register(frozenCorruption);
		GameRegistry.register(shadowCorruption);
		GameRegistry.register(magicCorruption);
		GameRegistry.register(dreadlands);
		GameRegistry.register(desert);
		
		BiomeManager.removeSpawnBiome(chaosCorruption);
		BiomeManager.removeSpawnBiome(frozenCorruption);
		BiomeManager.removeSpawnBiome(shadowCorruption);
		BiomeManager.removeSpawnBiome(magicCorruption);
		
		registerFirstWorldBiomes();
	}
	
	public static void registerFirstWorldBiomes() {
		firstWorldBiomeArray[0] = Biomes.OCEAN;
		firstWorldBiomeArray[1] = magicCorruption;
		firstWorldBiomeArray[2] = Biomes.BEACH;
		firstWorldBiomeArray[3] = Biomes.RIVER;
		firstWorldBiomeArray[4] = dreadlands;
		firstWorldBiomeArray[5] = Biomes.FOREST;
		firstWorldBiomeArray[6] = Biomes.EXTREME_HILLS;
		firstWorldBiomeArray[7] = chaosCorruption;
		firstWorldBiomeArray[8] = desert;
		firstWorldBiomeArray[9] = frozenCorruption;
	}
	
    public static Biome getBiome(int p_150568_0_)
    {
        if (p_150568_0_ >= 0 && p_150568_0_ <= firstWorldBiomeArray.length)
        {
            return firstWorldBiomeArray[p_150568_0_];
        }
        else
        {
            LogManager.getLogger().warn("Biome ID is out of bounds: " + p_150568_0_ + ", defaulting to 0 (Ocean)");
            return firstWorldBiomeArray[0];
        }
    }
	
	public static Biome chaosCorruption;
	public static Biome frozenCorruption;
	public static Biome shadowCorruption;
	public static Biome magicCorruption;
	public static Biome dreadlands;
	public static Biome desert;
	
	public static Biome[] firstWorldBiomeArray = new Biome[10];
}
