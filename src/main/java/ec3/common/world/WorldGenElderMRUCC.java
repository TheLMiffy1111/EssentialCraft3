package ec3.common.world;

import java.util.Random;

import DummyCore.Utils.Coord3D;
import DummyCore.Utils.MathUtils;
import ec3.utils.cfg.Config;
import ec3.utils.common.ECUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.DungeonHooks;

public class WorldGenElderMRUCC extends WorldGenerator {
	public int type;
	
	public static void handleGeneration(Random random, int chunkX, int chunkZ, World world,IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		for(int i = 0; i < Config.eMRUCUGenAttempts; ++i)
		{
			int rndX = chunkX*16 + random.nextInt(16);
			int rndY = random.nextInt(128);
			int rndZ = chunkZ*16 + random.nextInt(16);
			new WorldGenElderMRUCC(random.nextInt(4)).generate(world, random, new BlockPos(rndX, rndY, rndZ));
		}
	}
	
	public WorldGenElderMRUCC(int i)
	{
		type = i;
	}
	@Override
	public boolean generate(World var1, Random var2, BlockPos var3) {
		if(type == 0 && canGenerateAt(var3.getX()-2,var3.getZ()-2,var3.getX()+2,var3.getZ()+2,var3.getY(),true,var1))
		{
			for(int x = -2; x<= 2; ++x)
			{
				for(int z = -2; z<= 2; ++z)
				{
					var1.setBlockState(var3.add(x, 0, z), Block.getBlockFromName("minecraft:stonebrick").getStateFromMeta(0), 3);
					if((x == -2 || x == 2)||(z == -2 || z == 2))
					{
						var1.setBlockState(var3.add(x, 1, z), Block.getBlockFromName("minecraft:cobblestone_wall").getStateFromMeta(0), 3);
					}
					if(x == 0 && z == 0)
					{
						var1.setBlockState(var3.add(x, 1, z), Block.getBlockFromName("minecraft:stonebrick").getStateFromMeta(3), 3);
						ECUtils.createMRUCUAt(var1, new Coord3D(var3.getX()+x+0.5F,var3.getY()+1.5F,var3.getZ()+z+0.5F), 5000+var2.nextInt(5000), MathUtils.randomFloat(var2)+1.0F, false, false);
					}
				}
			}
		}
		if(type == 1 && canGenerateAt(var3.getX()-4,var3.getZ()-4,var3.getX()+4,var3.getZ()+4,var3.getY(),true,var1))
		{
			for(int x = -4; x<= 4; ++x)
			{
				for(int z = -4; z<= 4; ++z)
				{
					var1.setBlockState(var3.add(x, 0, z), Block.getBlockFromName("minecraft:stonebrick").getStateFromMeta(var2.nextInt(3)), 3);
					if((x == -4 || x == 4)||(z == -4 || z == 4))
					{
						var1.setBlockState(var3.add(x, 1, z), Block.getBlockFromName("minecraft:cobblestone_wall").getStateFromMeta(0), 3);
					}
					if(x == 0 && z == 0)
					{
						var1.setBlockState(var3.add(x, 1, z), Block.getBlockFromName("minecraft:stonebrick").getStateFromMeta(3), 3);
						ECUtils.createMRUCUAt(var1, new Coord3D(var3.getX()+x+0.5F,var3.getY()+1.5F,var3.getZ()+z+0.5F), 5000+var2.nextInt(5000), MathUtils.randomFloat(var2)+1.0F, false, false);
					}
					if((x == -3 && z == -3) || (x == 3 && z == -3) || (x == 3 && z == 3) || (x == -3 && z == 3))
					{
						var1.setBlockState(var3.add(x, 1, z), Block.getBlockFromName("minecraft:chest").getStateFromMeta(4), 3);
                        TileEntityChest tileentitychest = (TileEntityChest)var1.getTileEntity(var3.add(x, 1, z));

                        if (tileentitychest != null)
                        {
                            tileentitychest.setLootTable(LootTableList.CHESTS_SIMPLE_DUNGEON, var2.nextLong());
                        }
					}
				}
			}
		}
		if(type == 2 && canGenerateAt(var3.getX()-4,var3.getZ()-4,var3.getX()+4,var3.getZ()+4,var3.getY(),true,var1))
		{
			for(int x = -4; x<= 4; ++x)
			{
				for(int z = -4; z<= 4; ++z)
				{
					var1.setBlockState(var3.add(x, 0, z), Block.getBlockFromName("minecraft:stonebrick").getStateFromMeta(var2.nextInt(3)), 3);
					var1.setBlockState(var3.add(x, 1, z), Block.getBlockFromName("minecraft:stonebrick").getStateFromMeta(var2.nextInt(3)), 3);
					if((x == -4 || x == 4)||(z == -4 || z == 4))
					{
						var1.setBlockState(var3.add(x, 2, z), Block.getBlockFromName("minecraft:cobblestone_wall").getStateFromMeta(0), 3);
					}
					if(x == 0 && z == 0)
					{
						var1.setBlockState(var3.add(x, 2, z), Block.getBlockFromName("minecraft:stonebrick").getStateFromMeta(3), 3);
						ECUtils.createMRUCUAt(var1, new Coord3D(var3.getX()+x+0.5F,var3.getY()+1.5F,var3.getZ()+z+0.5F), 5000+var2.nextInt(5000), MathUtils.randomFloat(var2)+1.0F, false, false);
					}
					if(x != 3 && x != -3 && z != 3 && z != -3 && ((x == -2 || x == 2)||(z == -2 || z == 2)))
					{
						var1.setBlockState(var3.add(x, 2, z), Block.getBlockFromName("minecraft:cobblestone_wall").getStateFromMeta(0), 3);
					}
					if(x != 4 && x != -4 && z != 4 && z != -4 && ((x == -3 || x == 3)||(z == -3 || z == 3)))
					{
						var1.setBlockState(var3.add(x, 1, z), Block.getBlockFromName("minecraft:lava").getStateFromMeta(0), 3);
					}
				}
			}
		}
		if(type == 3 && canGenerateAt(var3.getX()-4,var3.getZ()-4,var3.getX()+4,var3.getZ()+4,var3.getY(),false,var1))
		{
			for(int x = -4; x<= 4; ++x)
			{
				for(int z = -4; z<= 4; ++z)
				{
					var1.setBlockState(var3.add(x, 0, z), Block.getBlockFromName("minecraft:stonebrick").getStateFromMeta(var2.nextInt(3)), 3);
					var1.setBlockState(var3.add(x, 4, z), Block.getBlockFromName("minecraft:stonebrick").getStateFromMeta(var2.nextInt(3)), 3);
					if((x == -4 || x == 4)||(z == -4 || z == 4))
					{
						var1.setBlockState(var3.add(x, 1, z), Block.getBlockFromName("minecraft:iron_bars").getStateFromMeta(0), 3);
						var1.setBlockState(var3.add(x, 2, z), Block.getBlockFromName("minecraft:stonebrick").getStateFromMeta(var2.nextInt(3)), 3);
						var1.setBlockState(var3.add(x, 3, z), Block.getBlockFromName("minecraft:iron_bars").getStateFromMeta(0), 3);
					}
					if((x == -4 && z == -4) || (x == 4 && z == -4) || (x == 4 && z == 4) || (x == -4 && z == 4))
					{
						var1.setBlockState(var3.add(x, 1, z), Block.getBlockFromName("minecraft:stonebrick").getStateFromMeta(var2.nextInt(3)), 3);
						var1.setBlockState(var3.add(x, 3, z), Block.getBlockFromName("minecraft:stonebrick").getStateFromMeta(var2.nextInt(3)), 3);
					}
					if(x == 0 && z == 0)
					{
						var1.setBlockState(var3.add(x, 1, z), Block.getBlockFromName("minecraft:stonebrick").getStateFromMeta(3), 3);
						ECUtils.createMRUCUAt(var1, new Coord3D(var3.getX()+x+0.5F,var3.getY()+1.5F,var3.getZ()+z+0.5F), 5000+var2.nextInt(5000), MathUtils.randomFloat(var2)+1.0F, false, false);
					}
					if((x == -3 && z == -3) || (x == 3 && z == -3) || (x == 3 && z == 3) || (x == -3 && z == 3))
					{
						var1.setBlockState(var3.add(x, 1, z), Block.getBlockFromName("minecraft:chest").getStateFromMeta(var2.nextInt(4)), 3);
                        TileEntityChest tileentitychest = (TileEntityChest)var1.getTileEntity(new BlockPos(var3.getX()+x, var3.getY()+1, var3.getZ()+z));

                        if (tileentitychest != null)
                        {
                        	tileentitychest.setLootTable(LootTableList.CHESTS_SIMPLE_DUNGEON, var2.nextLong());
                        }
					}
					if((x == -1 && z == 0) || (x == 1 && z == 0) || (x == 0 && z == 1) || (x == 0 && z == -1))
					{
						var1.setBlockState(var3.add(x, 1, z), Block.getBlockFromName("minecraft:mob_spawner").getStateFromMeta(0), 3);
                        TileEntityMobSpawner tileentitymobspawner = (TileEntityMobSpawner)var1.getTileEntity(new BlockPos(var3.getX()+x, var3.getY()+1, var3.getZ()+z));

                        if (tileentitymobspawner != null)
                        {
                        	tileentitymobspawner.getSpawnerBaseLogic().setEntityName(DungeonHooks.getRandomDungeonMob(var2));
                        }
					}
				}
			}
		}
		
		return false;
	}

	public boolean canGenerateAt(int minX, int minZ, int maxX, int maxZ, int y, boolean requiresSolidBlocks, World w)
	{
		for(int dx = minX; dx <= maxX; ++dx)
		{
			for(int dz = minZ; dz <= maxZ; ++dz)
			{
				BlockPos dp = new BlockPos(dx,y,dz);
				Block b = w.getBlockState(dp).getBlock();
				if(!b.isAir(w.getBlockState(dp), w, dp) && b.isBlockNormalCube(w.getBlockState(dp)))
				{
					if(requiresSolidBlocks)
					{
						if(!w.getBlockState(dp.add(0, 1, 0)).getBlock().isBlockNormalCube(w.getBlockState(dp)) || w.getBlockState(dp.up()).getBlock().isAir(w.getBlockState(dp), w, dp.up())) {}
						else
							return false;
					}
					else {
						if(w.getBlockState(dp).getBlock().isReplaceableOreGen(w.getBlockState(dp), w, dp, BlockMatcher.forBlock(Blocks.STONE))){}
						else
							return false;
					}
						
				}else
					return false;
			}
		}
		return true;
	}
}
