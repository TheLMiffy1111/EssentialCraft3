package ec3.common.world;

import java.util.Random;

import DummyCore.Utils.MathUtils;
import DummyCore.Utils.WeightedRandomChestContent;
import ec3.common.block.BlocksCore;
import ec3.common.item.ItemBaublesWearable;
import ec3.common.item.ItemsCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockVine;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenOldCatacombs extends WorldGenerator{

	public static final WeightedRandomChestContent[] generatedItems = new WeightedRandomChestContent[] {
		new WeightedRandomChestContent(ItemsCore.titanite, 0, 8, 32, 20),
		new WeightedRandomChestContent(ItemsCore.twinkling_titanite, 0, 2, 16, 10),
		new WeightedRandomChestContent(ItemsCore.genericItem, 5, 1, 16, 15),
		new WeightedRandomChestContent(ItemsCore.genericItem, 6, 1, 16, 15),
		new WeightedRandomChestContent(ItemsCore.genericItem, 7, 1, 16, 15),
		new WeightedRandomChestContent(ItemsCore.genericItem, 8, 1, 16, 15), 
		new WeightedRandomChestContent(ItemsCore.genericItem, 9, 1, 16, 15),
		new WeightedRandomChestContent(ItemsCore.genericItem, 10, 1, 16, 15),
		new WeightedRandomChestContent(ItemsCore.genericItem, 11, 1, 16, 15),
		new WeightedRandomChestContent(ItemsCore.genericItem, 20, 1, 12, 10),
		new WeightedRandomChestContent(ItemsCore.genericItem, 3, 1, 16, 15),
		new WeightedRandomChestContent(ItemsCore.genericItem, 35, 1, 1, 6),
		new WeightedRandomChestContent(ItemsCore.genericItem, 36, 1, 1, 6),
		new WeightedRandomChestContent(ItemsCore.genericItem, 37, 1, 1, 6),
		new WeightedRandomChestContent(ItemsCore.magicalSlag, 0, 1, 16, 70),
		new WeightedRandomChestContent(ItemsCore.ember, 0, 1, 16, 10),
		new WeightedRandomChestContent(ItemsCore.ember, 1, 1, 16, 10),
		new WeightedRandomChestContent(ItemsCore.ember, 2, 1, 16, 10),
		new WeightedRandomChestContent(ItemsCore.ember, 3, 1, 16, 10),
		new WeightedRandomChestContent(ItemsCore.ember, 4, 1, 16, 10),
		new WeightedRandomChestContent(ItemsCore.ember, 5, 1, 16, 10),
		new WeightedRandomChestContent(ItemsCore.ember, 7, 1, 16, 10),
		new WeightedRandomChestContent(ItemsCore.ember, 6, 1, 16, 10),
		new WeightedRandomChestContent(ItemsCore.bauble, 0, 1, 1, 15)
	};
	
	public int maxSizeTries = 100;
	public int corridorMinLength = 32;
	public int corridorMaxLength = 64;
	
	public float lootRoomChance = 0.003F;
	public float destroyedRoomChance = 0.012F;
	public float greenRoomChance = 0.012F;
	public float weirdRoomChance = 0.003F;
	public float experimentRoomChance = 0.003F;
	
	public Block blockToGenerateOf;
	
	public boolean isFirstTry = true;
	
	public EnumFacing cameFrom;
	@Override
	public boolean generate(World w, Random r, BlockPos p)
	{
		int x = p.getX();
		int y = p.getY();
		int z = p.getZ();
		
		int lengthGenned = r.nextInt(corridorMaxLength-corridorMinLength)+corridorMinLength;		
		EnumFacing[] genTry = new EnumFacing[4];
		if(this.cameFrom != EnumFacing.SOUTH)
			genTry[0] = EnumFacing.NORTH;
		if(this.cameFrom != EnumFacing.NORTH)
			genTry[1] = EnumFacing.SOUTH;
		if(this.cameFrom != EnumFacing.EAST)
			genTry[2] = EnumFacing.WEST;
		if(this.cameFrom != EnumFacing.WEST)
			genTry[3] = EnumFacing.EAST;
		if(isFirstTry && maxSizeTries > 0)
		{
			for(int i = 0; i < 4; ++i)
			{
				if(genTry[i] != null)
					if(generateCorridor(w,x,y,z,lengthGenned,genTry[i],r.nextFloat()<=greenRoomChance))
					{
						WorldGenOldCatacombs catacombs = new WorldGenOldCatacombs();
						catacombs.maxSizeTries = this.maxSizeTries/4;
						catacombs.cameFrom = genTry[i];
						catacombs.generate(w, r, new BlockPos(x+EnumFacing.getFront(i+2).getFrontOffsetX()*10, y, z+EnumFacing.getFront(i+2).getFrontOffsetZ()*10));
					}
			}
		}
		else
		{
			if(w.rand.nextFloat() < 0.03F)
				generateWayUp(w,x,y,z);
			if(w.getBlockState(p).getBlock() != Blocks.CHEST)
			{
				w.setBlockState(p, Blocks.CHEST.getDefaultState(), 3);
				w.setBlockState(p.add(0, -1, 0), BlocksCore.voidStone.getDefaultState());
				TileEntityChest chest = (TileEntityChest)w.getTileEntity(p);
	            if (chest != null)
	            {
	                WeightedRandomChestContent.generateChestContents(w.rand, generatedItems, chest, w.rand.nextInt(12)+6);
	                IInventory inv = chest;
	                for(int i = 0; i < inv.getSizeInventory(); ++i)
	                {
	                	ItemStack stk = inv.getStackInSlot(i);
	                	if(stk != null && stk.getItem() instanceof ItemBaublesWearable)
	                	{
	                		ItemBaublesWearable.initRandomTag(stk, w.rand);
	                	}
	                }
	            }
			}
		}
		return false;
	}
	
	public boolean generateCorridor(World w, int x, int y, int z, int length, EnumFacing direction, boolean green)
	{
		int generatedLength = 0;
		Block generateFrom = this.getBlockToGenFrom();
		boolean gen = true;
		while(gen)
		{
			if(w.rand.nextFloat() < this.destroyedRoomChance)
				generateBrokenPath(w,x,y,z);
			if(direction.getFrontOffsetX() >= 0 && direction.getFrontOffsetZ() >= 0)
			{
				for(int dx = x-direction.getFrontOffsetZ()*2; dx <= x+direction.getFrontOffsetZ()*2; ++dx)
				{
					for(int dy = -2; dy <= 2; ++dy)
					{
						for(int dz = z-direction.getFrontOffsetX()*2; dz <= z+direction.getFrontOffsetX()*2; ++dz)
						{
							Block b = w.getBlockState(new BlockPos(dx, y+dy, dz)).getBlock();
							if(b != Blocks.CHEST && b != Blocks.AIR && !(b instanceof BlockBush) && !(b instanceof BlockVine) && !(b instanceof BlockLeaves) && (b != BlocksCore.concrete) && (b != BlocksCore.root))
							{
								w.setBlockState(new BlockPos(dx, y+dy, dz), generateFrom.getStateFromMeta(0), 3);
							}
						}
					}
				}
				for(int dx = x-direction.getFrontOffsetZ(); dx <= x+direction.getFrontOffsetZ(); ++dx)
				{
					for(int dy = -1; dy <= 1; ++dy)
					{
						for(int dz = z-direction.getFrontOffsetX(); dz <= z+direction.getFrontOffsetX(); ++dz)
						{
							Block b = w.getBlockState(new BlockPos(dx, y+dy, dz)).getBlock();
							if(b != Blocks.CHEST && b != Blocks.AIR && !(b instanceof BlockBush) && !(b instanceof BlockVine) && !(b instanceof BlockLeaves) && (b != BlocksCore.concrete) && (b != BlocksCore.root))
								w.setBlockState(new BlockPos(dx, y+dy, dz), Blocks.AIR.getDefaultState(), 3);
						}
					}
				}
			}
			if(direction.getFrontOffsetX() < 0 && direction.getFrontOffsetZ() >= 0)
			{
				for(int dx = x-direction.getFrontOffsetZ()*2; dx <= x+direction.getFrontOffsetZ()*2; ++dx)
				{
					for(int dy = -2; dy <= 2; ++dy)
					{
						for(int dz = z+direction.getFrontOffsetX()*2; dz <= z-direction.getFrontOffsetX()*2; ++dz)
						{
							Block b = w.getBlockState(new BlockPos(dx, y+dy, dz)).getBlock();
							if(b != Blocks.CHEST && b != Blocks.AIR && !(b instanceof BlockBush) && !(b instanceof BlockVine) && !(b instanceof BlockLeaves) && (b != BlocksCore.concrete) && (b != BlocksCore.root))
								w.setBlockState(new BlockPos(dx, y+dy, dz), generateFrom.getStateFromMeta(0), 3);
						}
					}
				}
				for(int dx = x-direction.getFrontOffsetZ(); dx <= x+direction.getFrontOffsetZ(); ++dx)
				{
					for(int dy = -1; dy <= 1; ++dy)
					{
						for(int dz = z+direction.getFrontOffsetX(); dz <= z-direction.getFrontOffsetX(); ++dz)
						{
							Block b = w.getBlockState(new BlockPos(dx, y+dy, dz)).getBlock();
							if(b != Blocks.CHEST && b != Blocks.AIR && !(b instanceof BlockBush) && !(b instanceof BlockVine) && !(b instanceof BlockLeaves) && (b != BlocksCore.concrete) && (b != BlocksCore.root))
								w.setBlockState(new BlockPos(dx, y+dy, dz), Blocks.AIR.getDefaultState(), 3);
						}
					}
				}
			}
			if(direction.getFrontOffsetX() >= 0 && direction.getFrontOffsetZ() < 0)
			{
				for(int dx = x+direction.getFrontOffsetZ()*2; dx <= x-direction.getFrontOffsetZ()*2; ++dx)
				{
					for(int dy = -2; dy <= 2; ++dy)
					{
						for(int dz = z-direction.getFrontOffsetX()*2; dz <= z+direction.getFrontOffsetX()*2; ++dz)
						{
							Block b = w.getBlockState(new BlockPos(dx, y+dy, dz)).getBlock();
							if(b != Blocks.CHEST && b != Blocks.AIR && !(b instanceof BlockBush) && !(b instanceof BlockVine) && !(b instanceof BlockLeaves) && (b != BlocksCore.concrete) && (b != BlocksCore.root))
								if(!w.isAirBlock(new BlockPos(dx, y+dy, dz)))
									w.setBlockState(new BlockPos(dx, y+dy, dz), generateFrom.getStateFromMeta(0), 3);
						}
					}
				}
				for(int dx = x+direction.getFrontOffsetZ(); dx <= x-direction.getFrontOffsetZ(); ++dx)
				{
					for(int dy = -1; dy <= 1; ++dy)
					{
						for(int dz = z-direction.getFrontOffsetX(); dz <= z+direction.getFrontOffsetX(); ++dz)
						{
							Block b = w.getBlockState(new BlockPos(dx, y+dy, dz)).getBlock();
							if(b != Blocks.CHEST && b != Blocks.AIR && !(b instanceof BlockBush) && !(b instanceof BlockVine) && !(b instanceof BlockLeaves) && (b != BlocksCore.concrete) && (b != BlocksCore.root))
								w.setBlockState(new BlockPos(dx, y+dy, dz), Blocks.AIR.getDefaultState(), 3);
						}
					}
				}
			}
			if(direction.getFrontOffsetX() < 0 && direction.getFrontOffsetZ() < 0)
			{
				for(int dx = x+direction.getFrontOffsetZ()*2; dx <= x-direction.getFrontOffsetZ()*2; ++dx)
				{
					for(int dy = -2; dy <= 2; ++dy)
					{
						for(int dz = z+direction.getFrontOffsetX()*2; dz <= z-direction.getFrontOffsetX()*2; ++dz)
						{
							Block b = w.getBlockState(new BlockPos(dx, y+dy, dz)).getBlock();
							if(b != Blocks.CHEST && b != Blocks.AIR && !(b instanceof BlockBush) && !(b instanceof BlockVine) && !(b instanceof BlockLeaves) && (b != BlocksCore.concrete) && (b != BlocksCore.root))
								if(!w.isAirBlock(new BlockPos(dx, y+dy, dz)))
									w.setBlockState(new BlockPos(dx, y+dy, dz), generateFrom.getStateFromMeta(0), 3);
						}
					}
				}
				for(int dx = x+direction.getFrontOffsetZ(); dx <= x-direction.getFrontOffsetZ(); ++dx)
				{
					for(int dy = -1; dy <= 1; ++dy)
					{
						for(int dz = z+direction.getFrontOffsetX(); dz <= z-direction.getFrontOffsetX(); ++dz)
						{
							Block b = w.getBlockState(new BlockPos(dx, y+dy, dz)).getBlock();
							if(b != Blocks.CHEST && b != Blocks.AIR && !(b instanceof BlockBush) && !(b instanceof BlockVine) && !(b instanceof BlockLeaves) && (b != BlocksCore.concrete) && (b != BlocksCore.root))
								w.setBlockState(new BlockPos(dx, y+dy, dz), Blocks.AIR.getDefaultState(), 3);
						}
					}
				}
			}
			++generatedLength;
			if(generatedLength >= length)
			{
				gen = false;
				return true;
			}
			x += direction.getFrontOffsetX();
			z += direction.getFrontOffsetZ();
			if(w.rand.nextFloat() < this.greenRoomChance)
				generateGrownPath(w,x,y,z);
		}
		return false;
	}
	
	public Block getBlockToGenFrom()
	{
		return this.blockToGenerateOf == null ? BlocksCore.fortifiedStone : blockToGenerateOf;
	}
	
	public void generateBrokenPath(World w, int x, int y, int z)
	{
		for(int i = -2; i <= 2; ++i)
		{
			for(int j = -2; j <= 2; ++j)
			{
				for(int k = -2; k <= 2; ++k)
				{
					BlockPos p = new BlockPos(x+i,y+j,z+k);
					int tryInt = j+3;
					if(w.rand.nextInt(tryInt) == 0)
						w.setBlockState(p, BlocksCore.concrete.getDefaultState());
				}
			}
		}
	}
	
	public void generateGrownPath(World w, int x, int y, int z)
	{
		Vec3d rootVec = new Vec3d(MathUtils.randomDouble(w.rand)*3, -6, MathUtils.randomDouble(w.rand)*3);
		for(int vi = 0; vi <= 6; ++vi)
		{
			w.setBlockState(new BlockPos((int)(x+rootVec.xCoord/vi), (int)(y-2-rootVec.yCoord/vi), (int)(z+rootVec.zCoord/vi)), BlocksCore.root.getDefaultState());
		}
		for(int i = -3; i <= 3; ++i)
		{
			for(int j = -3; j <= 3; ++j)
			{
				for(int k = -3; k <= 3; ++k)
				{
					if(w.rand.nextInt(3) == 0)
					{
						BlockPos p = new BlockPos(x+i, y+j, z+k);
						Block b = w.getBlockState(p).getBlock();
						if(b != Blocks.CHEST && b != Blocks.AIR && !(b instanceof BlockBush) && !(b instanceof BlockVine) && !(b instanceof BlockLeaves) && (b != BlocksCore.concrete) && (b != BlocksCore.root))
							w.setBlockState(p, Blocks.LEAVES.getStateFromMeta(0));
					}
				}
			}
		}
	}
	
	public boolean generateWayUp(World w, int x, int y, int z)
	{
		int maxY = y;
		while(maxY < 256)
		{
			++maxY;
			boolean isAir = true;
			for(int i = 0; i < 10; ++i)
			{
				if(!w.isAirBlock(new BlockPos(x, maxY+i, z)))
					isAir = false;
			}
			if(isAir)
				break;
		}
		
		for(int dx = -2; dx <= 2; ++dx)
		{
			for(int dy = y+3; dy < maxY; ++dy)
			{
				for(int dz = -2; dz <= 2; ++dz)
				{
					w.setBlockState(new BlockPos(x+dx, dy, z+dz), getBlockToGenFrom().getStateFromMeta(0), 3);
				}
			}
		}
		
		for(int dx = -1; dx <= 1; ++dx)
		{
			for(int dy = y+3; dy < maxY; ++dy)
			{
				for(int dz = -1; dz <= 1; ++dz)
				{
					w.setBlockState(new BlockPos(x+dx, dy, z+dz), Blocks.AIR.getDefaultState(), 3);
				}
			}
		}
		
		for(int dy = y-3; dy < maxY; ++dy)
		{
			w.setBlockState(new BlockPos(x-1, dy, z), Blocks.LADDER.getStateFromMeta(5), 3);
		}
		return true;
	}

}
