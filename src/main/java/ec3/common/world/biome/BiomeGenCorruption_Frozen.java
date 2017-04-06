package ec3.common.world.biome;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;

public class BiomeGenCorruption_Frozen extends Biome
{
    public BiomeGenCorruption_Frozen(BiomeProperties par1)
    {
        super(par1);
    }

	public int getGrassColorAtPos(BlockPos pos)
    {
    	return 0x0077ff;
    }
    
    public int getFoliageColorAtPos(BlockPos pos)
    {
    	return 0x0077ff;
    }
    
    public int getWaterColorMultiplier()
    {
    	return 0x0077ff;
    }
    
    public int getModdedBiomeGrassColor(int original)
    {
    	return 0x0077ff;
    }
    
    public int getModdedBiomeFoliageColor(int original)
    {
    	return 0x0077ff;
    }
}
