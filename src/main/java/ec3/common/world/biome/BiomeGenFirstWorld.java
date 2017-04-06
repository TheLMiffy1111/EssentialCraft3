package ec3.common.world.biome;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public class BiomeGenFirstWorld extends Biome
{
	public int grassColor = 16777215;
	public int waterColor = 16777215;
	public int leavesColor = 16777215;
	
	public BiomeGenFirstWorld setGrassColor(int i)
	{
		grassColor = i;
		return this;
	}
	
	public BiomeGenFirstWorld setWaterColor(int i)
	{
		waterColor = i;
		return this;
	}
	
	public BiomeGenFirstWorld setLeavesColor(int i)
	{
		leavesColor = i;
		return this;
	}
	
    public BiomeGenFirstWorld(BiomeProperties par1)
    {
        super(par1);
    }
    
    public int getGrassColorAtPos(BlockPos pos)
    {
    	return grassColor;
    }
    
    public int getFoliageColorAtPos(BlockPos pos)
    {
    	return leavesColor;
    }
    
    public int getWaterColorMultiplier()
    {
    	return waterColor;
    }
    
    public int getModdedBiomeGrassColor(int original)
    {
    	return grassColor;
    }
    
    public int getModdedBiomeFoliageColor(int original)
    {
    	return leavesColor;
    }
}
