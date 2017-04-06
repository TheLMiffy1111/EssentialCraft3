package ec3.common.world.biome;

import java.util.Random;

import ec3.common.world.WorldGenDreadCacti;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class BiomeGenFirstWorld_Desert extends Biome
{
	public int grassColor = 16777215;
	public int waterColor = 16777215;
	public int leavesColor = 16777215;
	
	public BiomeGenFirstWorld_Desert setGrassColor(int i)
	{
		grassColor = i;
		return this;
	}
	
	public BiomeGenFirstWorld_Desert setWaterColor(int i)
	{
		waterColor = i;
		return this;
	}
	
	public BiomeGenFirstWorld_Desert setLeavesColor(int i)
	{
		leavesColor = i;
		return this;
	}
	
    public BiomeGenFirstWorld_Desert(BiomeProperties par1)
    {
        super(par1);
        this.topBlock = Blocks.SAND.getDefaultState();
        this.fillerBlock = Blocks.SAND.getDefaultState();
        this.theBiomeDecorator.treesPerChunk = -999;
        this.theBiomeDecorator.deadBushPerChunk = 2;
        this.theBiomeDecorator.reedsPerChunk = 50;
        this.theBiomeDecorator.cactiPerChunk = 10;
        this.theBiomeDecorator.cactusGen = new WorldGenDreadCacti();
        this.spawnableCreatureList.clear();
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
    
    public void decorate(World p_76728_1_, Random p_76728_2_, BlockPos p_76728_3_)
    {
    	this.theBiomeDecorator.cactusGen = new WorldGenDreadCacti();
        this.theBiomeDecorator.decorate(p_76728_1_, p_76728_2_, this, p_76728_3_);
    }
}
