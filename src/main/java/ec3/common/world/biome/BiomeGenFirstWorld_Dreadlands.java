package ec3.common.world.biome;

import ec3.common.block.BlocksCore;
import ec3.common.world.WorldGenDreadCacti;
import net.minecraft.world.biome.Biome;

public class BiomeGenFirstWorld_Dreadlands extends Biome
{
	public int grassColor = 16777215;
	public int waterColor = 16777215;
	public int leavesColor = 16777215;
	
	public BiomeGenFirstWorld_Dreadlands setGrassColor(int i)
	{
		grassColor = i;
		return this;
	}
	
	public BiomeGenFirstWorld_Dreadlands setWaterColor(int i)
	{
		waterColor = i;
		return this;
	}
	
	public BiomeGenFirstWorld_Dreadlands setLeavesColor(int i)
	{
		leavesColor = i;
		return this;
	}
	
    public BiomeGenFirstWorld_Dreadlands(BiomeProperties par1)
    {
        super(par1);
        this.topBlock = BlocksCore.dreadDirt.getDefaultState();
        this.fillerBlock = BlocksCore.dreadDirt.getDefaultState();
        this.theBiomeDecorator.treesPerChunk = -999;
        this.theBiomeDecorator.deadBushPerChunk = 2;
        this.theBiomeDecorator.reedsPerChunk = -999;
        this.theBiomeDecorator.cactiPerChunk = -999;
        this.theBiomeDecorator.cactusGen = new WorldGenDreadCacti();
        
        this.spawnableCreatureList.clear();
    }
    
    public int getBiomeGrassColor()
    {
    	return grassColor;
    }
    
    public int getBiomeFoliageColor()
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
