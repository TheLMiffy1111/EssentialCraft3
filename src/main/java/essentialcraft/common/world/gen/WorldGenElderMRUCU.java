package essentialcraft.common.world.gen;

import java.util.Random;

import DummyCore.Utils.Coord3D;
import DummyCore.Utils.MathUtils;
import essentialcraft.utils.cfg.Config;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.DungeonHooks;

public class WorldGenElderMRUCU extends WorldGenerator {
	public int type;

	public static void handleGeneration(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		for(int i = 0; i < Config.eMRUCUGenAttempts; ++i) {
			int rndX = chunkX*16 + random.nextInt(16) + 8;
			int rndY = random.nextInt(128);
			int rndZ = chunkZ*16 + random.nextInt(16) + 8;
			new WorldGenElderMRUCU(random.nextInt(4)).generate(world, random, new BlockPos(rndX, rndY, rndZ));
		}
	}

	public WorldGenElderMRUCU(int type) {
		this.type = type;
	}

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		if(type == 0 && canGenerateAt(pos.getX(), pos.getZ(), pos.getX()+4, pos.getZ()+4, pos.getY(), true, world)) {
			for(int x = 0; x <= 4; ++x) {
				for(int z = 0; z <= 4; ++z) {
					world.setBlockState(pos.add(x, 0, z), Blocks.STONEBRICK.getDefaultState(), 2);
					if(x == 0 || x == 4 || z == 0 || z == 4) {
						world.setBlockState(pos.add(x, 1, z), Blocks.COBBLESTONE_WALL.getDefaultState(), 2);
					}
					if(x == 2 && z == 2) {
						world.setBlockState(pos.add(x, 1, z), Blocks.STONEBRICK.getStateFromMeta(3), 2);
						ECUtils.createMRUCUAt(world, new Coord3D(pos.getX()+x+0.5F,pos.getY()+1.5F,pos.getZ()+z+0.5F), 5000+rand.nextInt(5000), MathUtils.randomFloat(rand)+1.0F, false, false);
					}
				}
			}
		}
		if(type == 1 && canGenerateAt(pos.getX(), pos.getZ(), pos.getX()+8, pos.getZ()+8, pos.getY(), true, world)) {
			for(int x = 0; x <= 8; ++x) {
				for(int z = 0; z <= 8; ++z) {
					world.setBlockState(pos.add(x, 0, z), Blocks.STONEBRICK.getStateFromMeta(rand.nextInt(3)), 2);
					if(x == 0 || x == 8 || z == 0 || z == 8) {
						world.setBlockState(pos.add(x, 1, z), Blocks.COBBLESTONE_WALL.getDefaultState(), 2);
					}
					if(x == 4 && z == 4) {
						world.setBlockState(pos.add(x, 1, z), Blocks.STONEBRICK.getStateFromMeta(3), 2);
						ECUtils.createMRUCUAt(world, new Coord3D(pos.getX()+x+0.5F,pos.getY()+1.5F,pos.getZ()+z+0.5F), 5000+rand.nextInt(5000), MathUtils.randomFloat(rand)+1.0F, false, false);
					}
					if(x == 1 && z == 1 || x == 7 && z == 1 || x == 7 && z == 7 || x == 1 && z == 7) {
						world.setBlockState(pos.add(x, 1, z), Blocks.CHEST.getStateFromMeta(4), 2);
						TileEntityChest tileentitychest = (TileEntityChest)world.getTileEntity(pos.add(x, 1, z));
						if(tileentitychest != null) {
							tileentitychest.setLootTable(LootTableList.CHESTS_SIMPLE_DUNGEON, rand.nextLong());
						}
					}
				}
			}
		}
		if(type == 2 && canGenerateAt(pos.getX(), pos.getZ(), pos.getX()+8, pos.getZ()+8, pos.getY(), true, world)) {
			for(int x = 0; x <= 8; ++x) {
				for(int z = 0; z <= 8; ++z) {
					world.setBlockState(pos.add(x, 0, z), Blocks.STONEBRICK.getStateFromMeta(rand.nextInt(3)), 2);
					world.setBlockState(pos.add(x, 1, z), Blocks.STONEBRICK.getStateFromMeta(rand.nextInt(3)), 2);
					if(x == 0 || x == 8 || z == 0 || z == 8) {
						world.setBlockState(pos.add(x, 2, z), Blocks.COBBLESTONE_WALL.getDefaultState(), 2);
					}
					if(x == 0 && z == 0) {
						world.setBlockState(pos.add(x, 2, z), Blocks.STONEBRICK.getStateFromMeta(3), 2);
						ECUtils.createMRUCUAt(world, new Coord3D(pos.getX()+x+0.5F, pos.getY()+1.5F, pos.getZ()+z+0.5F), 5000+rand.nextInt(5000), MathUtils.randomFloat(rand)+1.0F, false, false);
					}
					if(x != 7 && x != 1 && z != 7 && z != 1 && (x == 2 || x == 6 || z == 2 || z == 6)) {
						world.setBlockState(pos.add(x, 2, z), Blocks.COBBLESTONE_WALL.getDefaultState(), 2);
					}
					if(x != 8 && x != 0 && z != 8 && z != 0 && (x == 1 || x == 7 || z == 1 || z == 7)) {
						world.setBlockState(pos.add(x, 1, z), Blocks.LAVA.getDefaultState(), 2);
					}
				}
			}
		}
		if(type == 3 && canGenerateAt(pos.getX(), pos.getZ(), pos.getX()+8, pos.getZ()+8, pos.getY(), false, world)) {
			for(int x = 0; x <= 8; ++x) {
				for(int z = 0; z <= 8; ++z) {
					world.setBlockState(pos.add(x, 0, z), Blocks.STONEBRICK.getStateFromMeta(rand.nextInt(3)), 2);
					world.setBlockState(pos.add(x, 4, z), Blocks.STONEBRICK.getStateFromMeta(rand.nextInt(3)), 2);
					if(x == 0 || x == 8 || z == 0 || z == 8) {
						world.setBlockState(pos.add(x, 1, z), Blocks.IRON_BARS.getDefaultState(), 2);
						world.setBlockState(pos.add(x, 2, z), Blocks.STONEBRICK.getStateFromMeta(rand.nextInt(3)), 2);
						world.setBlockState(pos.add(x, 3, z), Blocks.IRON_BARS.getDefaultState(), 2);
					}
					if(x == 0 && z == 0 || x == 8 && z == 0 || x == 8 && z == 8 || x == 0 && z == 8) {
						world.setBlockState(pos.add(x, 1, z), Blocks.STONEBRICK.getStateFromMeta(rand.nextInt(3)), 2);
						world.setBlockState(pos.add(x, 3, z), Blocks.STONEBRICK.getStateFromMeta(rand.nextInt(3)), 2);
					}
					if(x == 4 && z == 4) {
						world.setBlockState(pos.add(x, 1, z), Blocks.STONEBRICK.getStateFromMeta(3), 2);
						ECUtils.createMRUCUAt(world, new Coord3D(pos.getX()+x+0.5F, pos.getY()+1.5F, pos.getZ()+z+0.5F), 5000+rand.nextInt(5000), MathUtils.randomFloat(rand)+1.0F, false, false);
					}
					if(x == 1 && z == 1 || x == 7 && z == 1 || x == 7 && z == 7 || x == 1 && z == 7) {
						world.setBlockState(pos.add(x, 1, z), Blocks.CHEST.getStateFromMeta(rand.nextInt(4)), 2);
						TileEntityChest tileentitychest = (TileEntityChest)world.getTileEntity(pos.add(x, 1, z));
						if(tileentitychest != null) {
							tileentitychest.setLootTable(LootTableList.CHESTS_SIMPLE_DUNGEON, rand.nextLong());
						}
					}
					if(x == 3 && z == 4 || x == 5 && z == 4 || x == 4 && z == 5 || x == 4 && z == 3) {
						world.setBlockState(pos.add(x, 1, z), Blocks.MOB_SPAWNER.getDefaultState(), 2);
						TileEntityMobSpawner tileentitymobspawner = (TileEntityMobSpawner)world.getTileEntity(pos.add(x, 1, z));
						if(tileentitymobspawner != null) {
							tileentitymobspawner.getSpawnerBaseLogic().setEntityId(DungeonHooks.getRandomDungeonMob(rand));
						}
					}
				}
			}
		}

		return false;
	}

	public boolean canGenerateAt(int minX, int minZ, int maxX, int maxZ, int y, boolean requiresSolidBlocks, World w) {
		for(int dx = minX; dx <= maxX; ++dx) {
			for(int dz = minZ; dz <= maxZ; ++dz) {
				BlockPos dp = new BlockPos(dx,y,dz);
				Block b = w.getBlockState(dp).getBlock();
				if(!b.isAir(w.getBlockState(dp), w, dp) && w.getBlockState(dp).isBlockNormalCube()) {
					if(requiresSolidBlocks) {
						if(w.getBlockState(dp.up()).isBlockNormalCube() && !w.getBlockState(dp.up()).getBlock().isAir(w.getBlockState(dp), w, dp.up())) {
							return false;
						}
					}
					else {
						if(!w.getBlockState(dp).getBlock().isReplaceableOreGen(w.getBlockState(dp), w, dp, BlockMatcher.forBlock(Blocks.STONE))) {
							return false;
						}
					}
				}
				else {
					return false;
				}
			}
		}
		return true;
	}
}
