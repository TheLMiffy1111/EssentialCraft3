package ec3.common.world;

import java.util.Random;

import DummyCore.Utils.WeightedRandomChestContent;
import ec3.common.block.BlocksCore;
import ec3.common.item.ItemsCore;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenDestroyedHouse extends WorldGenerator{
	
	public int floorsAmount, rad;
	
	public WorldGenDestroyedHouse(int i)
	{
		floorsAmount = i;
	}
	
	public WorldGenDestroyedHouse(int i, int j)
	{
		floorsAmount = i;
		rad = j;
	}

	public static final WeightedRandomChestContent[] generatedItems = new WeightedRandomChestContent[] {
		new WeightedRandomChestContent(ItemsCore.titanite, 0, 8, 64, 20),
		new WeightedRandomChestContent(ItemsCore.twinkling_titanite, 0, 2, 16, 10),
		new WeightedRandomChestContent(ItemsCore.genericItem, 5, 1, 64, 15),
		new WeightedRandomChestContent(ItemsCore.genericItem, 6, 1, 64, 15),
		new WeightedRandomChestContent(ItemsCore.genericItem, 7, 1, 64, 15),
		new WeightedRandomChestContent(ItemsCore.genericItem, 8, 1, 64, 15), 
		new WeightedRandomChestContent(ItemsCore.genericItem, 9, 1, 64, 15),
		new WeightedRandomChestContent(ItemsCore.genericItem, 10, 1, 64, 15),
		new WeightedRandomChestContent(ItemsCore.genericItem, 11, 1, 64, 15),
		new WeightedRandomChestContent(ItemsCore.genericItem, 20, 1, 12, 10),
		new WeightedRandomChestContent(ItemsCore.genericItem, 3, 1, 64, 15),
		new WeightedRandomChestContent(ItemsCore.genericItem, 35, 1, 1, 6),
		new WeightedRandomChestContent(ItemsCore.genericItem, 36, 1, 1, 6),
		new WeightedRandomChestContent(ItemsCore.genericItem, 37, 1, 1, 6)
	};
	
	public int getGroundToGenerate(World w, int x, int y, int z)
	{
		while(y > 5)
		{
			BlockPos p = new BlockPos(x,y,z);
			if(!w.isAirBlock(p) && w.getBlockState(p).getBlock() != Blocks.WATER)
			{
				if(w.getBlockState(p).getBlock() != BlocksCore.concrete && w.getBlockState(p).getBlock() != BlocksCore.fortifiedStone)
				{
					break;
				}else
				{
					return -1;
				}
			}
			--y;
		}
		if(y == 5)
			return -1;
		return y;
	}

	@Override
	public boolean generate(World w, Random r,BlockPos p)
	{
		int genY = getGroundToGenerate(w,p.getX(),p.getY(),p.getZ());
		if(genY != -1)
		{
			int y = genY;
			if(rad == 0)
				rad = r.nextInt(6)+3;
			for(int i = 0; i < floorsAmount+1; ++i)
			{
				generateFloor(w,r,p.getX(),y+5*i,p.getZ(),i,rad);
			}
			return true;
		}
		return false;
	}
	
	public void generateFloor(World w, Random r,int x, int y, int z,int floorNum, int size)
	{
		for(int dx = -size; dx <= size; ++dx)
		{
			for(int dz = -size; dz <= size; ++dz)
			{
				if(floorNum == 0)
				{
					if(((dx == -size || dx == size) && (dz == -size || dz == size)) || (dx == 0 && dz == 0))
					{
						w.setBlockState(new BlockPos(x+dx, y-1, z+dz), BlocksCore.levitator.getDefaultState(),3);
					}
				}
				for(int dy = 0; dy < 5; ++dy)
				{
					BlockPos p = new BlockPos(x+dx, y+dy, z+dz);
					if(w.getBlockState(p) != Blocks.WATER)
						w.setBlockState(p, Blocks.AIR.getDefaultState(),3);
					int tryInt = dy+1;
					if(w.rand.nextInt(tryInt) == 0)
						w.setBlockState(p, BlocksCore.concrete.getDefaultState(),3);
					if(dy == 0 || dy == 4)
					{
						w.setBlockState(p, BlocksCore.fortifiedStone.getDefaultState(),3);
					}
					if(dx == -size || dx == size)
					{
						w.setBlockState(p, BlocksCore.fortifiedStone.getDefaultState(),3);
						if(dy > 0 && dy < 4 && dz > -size+1 && dz < size-1)
						{
							w.setBlockState(p, BlocksCore.fortifiedGlass.getDefaultState(),3);
						}
					}
					if(dz == -size || dz == size)
					{
						w.setBlockState(p, BlocksCore.fortifiedStone.getDefaultState(),3);
						if(dy > 0 && dy < 4 && dx > -size+1 && dx < size-1)
						{
							w.setBlockState(p, BlocksCore.fortifiedGlass.getDefaultState(),3);
						}
					}
					if(floorsAmount == 0)floorsAmount = 1;
					if(r.nextInt(floorsAmount*10)<floorNum)
					{
		                ECExplosion explosion = new ECExplosion(w,null,x+dx, y+dy, z+dz, 3+(floorNum/3));
		                explosion.doExplosionA();
		                explosion.doExplosionB(true);
					}
				}
			}
		}
	}

}
