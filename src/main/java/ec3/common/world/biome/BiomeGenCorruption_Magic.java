package ec3.common.world.biome;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;

public class BiomeGenCorruption_Magic extends Biome
{
    public BiomeGenCorruption_Magic(BiomeProperties par1)
    {
		super(par1);
	}

	public int getGrassColorAtPos(BlockPos pos)
    {
    	return 0xff00ff;
    }
    
    public int getFoliageColorAtPos(BlockPos pos)
    {
    	return 0xff00ff;
    }
    
    public int getWaterColorMultiplier()
    {
    	return 0xff00ff;
    }
    
    public int getModdedBiomeGrassColor(int original)
    {
    	return 0xff00ff;
    }
    
    public int getModdedBiomeFoliageColor(int original)
    {
    	return 0xff00ff;
    }
}
