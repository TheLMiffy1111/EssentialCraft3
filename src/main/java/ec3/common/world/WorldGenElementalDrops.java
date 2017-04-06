package ec3.common.world;

import java.util.Random;

import ec3.common.block.BlocksCore;
import ec3.utils.cfg.Config;
import net.minecraft.block.Block;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenElementalDrops extends WorldGenerator {

	public Block minableReplacable;
	public boolean isEnd;
	public boolean isNether;
	public int generationSteps;
	public Random rnd;
	public int x,y,z;
	public World w;

	public WorldGenElementalDrops selectValidAABBGen() {
		int startX = x;
		int startZ = z;
		if(!w.isBlockLoaded(new BlockPos(x+generationSteps/2, y, z))) {
			while(!w.isBlockLoaded(new BlockPos(x+generationSteps/2-1, y, z)) && x+generationSteps/2-1 >= startX) {
				--x;
			}
		}
		if(!w.isBlockLoaded(new BlockPos(x-generationSteps/2, y, z))) {
			while(!w.isBlockLoaded(new BlockPos(x-generationSteps/2+1, y, z)) && x-generationSteps/2+1 <= startX) {
				++x;
			}
		}
		if(!w.isBlockLoaded(new BlockPos(x, y, z+generationSteps/2))) {
			while(!w.isBlockLoaded(new BlockPos(x, y, z+generationSteps/2-1)) && z+generationSteps/2-1 >= startZ) {
				--z;
			}
		}
		if(!w.isBlockLoaded(new BlockPos(x, y, z-generationSteps/2))) {
			while(!w.isBlockLoaded(new BlockPos(x, y, z-generationSteps/2+1)) && z-generationSteps/2+1 <= startZ) {
				++z;
			}
		}
		return this;
	}

	public WorldGenElementalDrops(World w, int type, int x, int y, int z) {
		rnd = w.rand;

		if(type == 0) {
			isEnd = false;
			isNether = false;
			minableReplacable = Blocks.STONE;
		}

		if(type == 1) {
			isEnd = true;
			isNether = false;
			minableReplacable = Blocks.END_STONE;
		}

		if(type == -1) {
			isEnd = false;
			isNether = true;
			minableReplacable = Blocks.NETHERRACK;
		}

		generationSteps = 16;
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	@Override
	public boolean generate(World w, Random rnd, BlockPos pos) {
		int meta = 0;

		if(isNether)
			meta += 5;
		if(isEnd)
			meta += 10;

		int[] selection = new int[] {rnd.nextInt(6), rnd.nextInt(6), rnd.nextInt(6)};
		for(int i = 0; i < selection.length; i++) {
			switch(selection[i]) {
			case 0:
				new WorldGenMinable(BlocksCore.oreDrops.getStateFromMeta(meta),16,BlockMatcher.forBlock(minableReplacable)).generate(w, rnd, new BlockPos(x, y, z));
				break;
			case 1:
				new WorldGenMinable(BlocksCore.oreDrops.getStateFromMeta(meta+1),8,BlockMatcher.forBlock(minableReplacable)).generate(w, rnd, new BlockPos(x+rnd.nextInt(6)-rnd.nextInt(6), y+rnd.nextInt(6)-rnd.nextInt(6), z+rnd.nextInt(6)-rnd.nextInt(6)));
				break;
			case 2:
				new WorldGenMinable(BlocksCore.oreDrops.getStateFromMeta(meta+2),8,BlockMatcher.forBlock(minableReplacable)).generate(w, rnd, new BlockPos(x+rnd.nextInt(6)-rnd.nextInt(6), y+rnd.nextInt(6)-rnd.nextInt(6), z+rnd.nextInt(6)-rnd.nextInt(6)));
				break;
			case 3:
				new WorldGenMinable(BlocksCore.oreDrops.getStateFromMeta(meta+3),8,BlockMatcher.forBlock(minableReplacable)).generate(w, rnd, new BlockPos(x+rnd.nextInt(6)-rnd.nextInt(6), y+rnd.nextInt(6)-rnd.nextInt(6), z+rnd.nextInt(6)-rnd.nextInt(6)));
				break;
			case 4:
				new WorldGenMinable(BlocksCore.oreDrops.getStateFromMeta(meta+4),8,BlockMatcher.forBlock(minableReplacable)).generate(w, rnd, new BlockPos(x+rnd.nextInt(6)-rnd.nextInt(6), y+rnd.nextInt(6)-rnd.nextInt(6), z+rnd.nextInt(6)-rnd.nextInt(6)));
				break;
			case 5:
				new WorldGenMinable(BlocksCore.oreMithriline.getStateFromMeta(meta/5),8,BlockMatcher.forBlock(minableReplacable)).generate(w, rnd, new BlockPos(x+rnd.nextInt(6)-rnd.nextInt(6), y+rnd.nextInt(6)-rnd.nextInt(6), z+rnd.nextInt(6)-rnd.nextInt(6)));
				break;
			}
		}

		return false;
	}

	public static void handleGeneration(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		for(int i = 0; i < Config.oreGenAttempts; ++i) {
			int x = chunkX*16 + random.nextInt(16);
			int y = random.nextInt(world.getHeight());
			int z = chunkZ*16 + random.nextInt(16);
			int type = world.provider.isSurfaceWorld() ? 0 : world.provider.doesWaterVaporize() ? -1 : 1;
			new WorldGenElementalDrops(world,type,x,y,z).selectValidAABBGen().generate(world, random, new BlockPos(x,y,z));
		}
	}
}
