package ec3.common.world.biome;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public class BiomeGenCorruption_Chaos extends Biome
{
    public BiomeGenCorruption_Chaos(BiomeProperties properties)
    {
        super(properties);
    }
    
    public int getGrassColorAtPos(BlockPos pos)
    {
    	return 0x770000;
    }
    
    public int getFoliageColorAtPos(BlockPos pos)
    {
    	return 0x770000;
    }
    
    public int getWaterColorMultiplier()
    {
    	return 0x770000;
    }
    
    public int getModdedBiomeGrassColor(int original)
    {
    	return 0x770000;
    }
    
    public int getModdedBiomeFoliageColor(int original)
    {
    	return 0x770000;
    }
}
