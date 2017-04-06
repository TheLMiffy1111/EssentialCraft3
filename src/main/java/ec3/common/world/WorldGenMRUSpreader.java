package ec3.common.world;

import java.util.Random;

import DummyCore.Utils.WeightedRandomChestContent;
import ec3.common.block.BlocksCore;
import ec3.common.item.ItemsCore;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenMRUSpreader extends WorldGenerator{
	
	
	public WorldGenMRUSpreader()
	{
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

	@Override
	public boolean generate(World w, Random r,BlockPos p)
	{
		int x = p.getX();
		int y = p.getY();
		int z = p.getZ();
		for(int dx = 0; dx < 3; ++dx)
		{
			for(int dz = 0; dz < 3; ++dz)
			{
				for(int dy = 0; dy < 7; ++dy)
				{
					BlockPos dp = new BlockPos(dx,dy,dz);
					if(dy == 0)
					{
						if(dx == 1 && dz == 1)
						{
							w.setBlockState(new BlockPos(x+dx, y, z+dz), BlocksCore.magicPlating.getDefaultState(),3);
							if(w.isAirBlock(new BlockPos(x+dx, y-1, z+dz)))
								w.setBlockState(new BlockPos(x+dx, y-1, z+dz), BlocksCore.levitator.getDefaultState(),3);
						}else
						{
							w.setBlockState(new BlockPos(x+dx, y-1, z+dz), BlocksCore.fortifiedStone.getDefaultState(),3);
						}
					}else
					{
						if((dx == 0 || dx == 2) && (dz == 0 || dz == 2) && dy < 5)
						{
							w.setBlockState(dp, BlocksCore.fence[2].getDefaultState(),3);
						}else
						{
							if((dx == 1) && (dz == 1) && dy < 6)
							{
								w.setBlockState(dp, BlocksCore.fence[1].getDefaultState(),3);
							}else
							{
								if((dx == 1) && (dz == 1) && dy == 6)
								{
									w.setBlockState(dp, BlocksCore.spreader.getDefaultState(),3);
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

}
