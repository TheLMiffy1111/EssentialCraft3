package ec3.common.world.dim;

import ec3.common.registry.BiomeRegistry;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.storage.WorldInfo;

public class WorldChunkManager_FirstWorld extends BiomeProvider {
	
    public WorldChunkManager_FirstWorld(WorldInfo info)
    {
    	super(info);
    }
    
    public WorldChunkManager_FirstWorld()
    {
    	super();
    }

    public Biome[] getBiomeGenAt(Biome[] p_76931_1_, int p_76931_2_, int p_76931_3_, int p_76931_4_, int p_76931_5_, boolean p_76931_6_)
    {
    	Biome[] bgb = super.getBiomes(p_76931_1_, p_76931_2_, p_76931_3_, p_76931_4_, p_76931_5_, p_76931_6_);
    	for(int i = 0; i < bgb.length; ++i) {
    		Biome biome = bgb[i];
    		if(biome != null) {
	    		int id = Biome.getIdForBiome(biome);
	    		int newId = id%BiomeRegistry.firstWorldBiomeArray.length;
	    		bgb[i] = BiomeRegistry.firstWorldBiomeArray[newId];
    		}
    	}
    	return bgb;
    }
}
