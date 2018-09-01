package essentialcraft.common.world.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

import DummyCore.Utils.WeightedEnum;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.DungeonHooks;
import net.minecraftforge.oredict.OreDictionary;

public class WorldGenMerge {

	public EnumGenerationType generate;

	public WorldGenMerge(Random rand) {
		generate = WeightedRandom.getRandomItem(rand, EnumGenerationType.getWeightedList()).getEnumType();
	}

	public boolean generate(World world, Random rand, BlockPos pos) {
		if(this.generate != null) {
			if(this.generate == EnumGenerationType.ORES) {
				Stream<IBlockState> stream0 = Stream.<String>of(OreDictionary.getOreNames()).filter(str->str.startsWith("ore")).<ItemStack>flatMap(str->OreDictionary.getOres(str).stream()).filter(stack->stack.getItem() instanceof ItemBlock).<IBlockState>map(stack->Block.getBlockFromItem(stack.getItem()).getStateFromMeta(stack.getItemDamage()));
				Stream<IBlockState> stream1 = Stream.<IBlockState>of(
						Blocks.DIRT.getDefaultState(),
						Blocks.GRAVEL.getDefaultState(),
						Blocks.STONE.getDefaultState(),
						Blocks.STONE.getStateFromMeta(1),
						Blocks.STONE.getStateFromMeta(3),
						Blocks.STONE.getStateFromMeta(5),
						Blocks.BONE_BLOCK.getDefaultState(),
						Blocks.OBSIDIAN.getDefaultState(),
						Blocks.NETHERRACK.getDefaultState(),
						Blocks.SOUL_SAND.getDefaultState(),
						Blocks.MAGMA.getDefaultState(),
						Blocks.GLOWSTONE.getDefaultState(),
						Blocks.END_STONE.getDefaultState()
						);
				Stream<IBlockState> stream2 = Stream.<String>of().map(str->{
					String[] split = str.split("@");
					ResourceLocation rl = new ResourceLocation(split[0]);
					Block block = Block.REGISTRY.getObject(rl);
					if(split.length == 2) {
						return block.getStateFromMeta(Integer.parseInt(split[1]));
					}
					return block.getDefaultState();
				});
				List<IBlockState> states = Stream.<Stream<IBlockState>>of(stream0, stream1, stream2).<IBlockState>flatMap(stream->stream).distinct().filter(state->true).collect(Collectors.toList());
				for(int x = -4; x <= 4; x++) {
					for(int y = -4; y <= 4; y++) {
						for(int z = -4; z <= 4; z++) {
							world.setBlockState(pos.add(x, y, z), states.get(rand.nextInt(states.size())), 2);
						}
					}
				}
			}
			if(this.generate == EnumGenerationType.DUNGEON) {
				genDungeon(world, rand, pos, false);
			}
			if(this.generate == EnumGenerationType.DUNGEON_LOOT) {
				genDungeon(world, rand, pos, true);
			}
			if(this.generate == EnumGenerationType.HOUSE) {
				genHouse(world, rand, pos, true);
			}
			if(this.generate == EnumGenerationType.BIOME) {
				Biome rndBiome = Biome.REGISTRY.getRandomObject(rand);
				for(int x = -4; x <= 4; x++) {
					for(int z = -4; z <= 4; z++) {
						world.setBlockState(pos.add(x, 0, z), rndBiome.topBlock, 2);
						Chunk chunk = world.getChunkFromBlockCoords(pos.add(x, 0, z));
						byte[] biome = chunk.getBiomeArray();
						int index = (pos.getZ()+z & 0xF) << 4 | pos.getX()+x & 0xF;
						int cbiome = biome[index];
						if(!world.isRemote) {
							cbiome = Biome.getIdForBiome(rndBiome) & 0xFF;
							biome[index] = ((byte)cbiome);
							chunk.setBiomeArray(biome);
						}
						world.markBlockRangeForRenderUpdate(pos.add(x, 0, z), pos.add(x, 0, z));
					}
				}
				rndBiome.getRandomWorldGenForGrass(rand).generate(world, rand, pos.up());
				rndBiome.getRandomTreeFeature(rand).generate(world, rand, pos.up());
			}
			return true;
		}
		return false;
	}

	public void genDungeon(World world, Random rand, BlockPos pos, boolean generateLoot) {
		int sizeX = rand.nextInt(2) + 2;
		int sizeZ = rand.nextInt(2) + 2;
		for(int x = -sizeX-1; x <= sizeX+1; x++) {
			for(int y = 4; y >= -1; y--) {
				for(int z = -sizeZ-1; z <= sizeZ+1; z++) {
					if(x != -sizeX-1 && y != -1 && z != -sizeZ-1 && x != sizeX+1 && y != 4 && z != sizeZ+1) {
						world.setBlockToAir(pos.add(x, y, z));
					}
					else if(y == -1 && rand.nextInt(4) != 0) {
						world.setBlockState(pos.add(x, y, z), Blocks.MOSSY_COBBLESTONE.getDefaultState(), 2);
					}
					else {
						world.setBlockState(pos.add(x, y, z), Blocks.COBBLESTONE.getDefaultState(), 2);
					}
				}
			}
		}
		for(int i = 0; i < 12; i++) {
			int x = rand.nextInt(sizeX * 2 + 1) - sizeX;
			int z = rand.nextInt(sizeZ * 2 + 1) - sizeZ;
			if(world.isAirBlock(pos.add(x, 0, z))) {
				int sideCount = 0;
				if(world.getBlockState(pos.add(x-1, 0, z)).getMaterial().isSolid()) {
					sideCount++;
				}
				if(world.getBlockState(pos.add(x+1, 0, z)).getMaterial().isSolid()) {
					sideCount++;
				}
				if(world.getBlockState(pos.add(x, 0, z-1)).getMaterial().isSolid()) {
					sideCount++;
				}
				if(world.getBlockState(pos.add(x, 0, z+1)).getMaterial().isSolid()) {
					sideCount++;
				}
				if(sideCount == 1) {
					if(!generateLoot) {
						break;
					}
					world.setBlockState(pos.add(x, 0, z), Blocks.CHEST.getDefaultState(), 2);
					TileEntityChest tileentitychest = (TileEntityChest)world.getTileEntity(pos.add(x, 0, z));
					if(tileentitychest != null) {
						tileentitychest.setLootTable(LootTableList.CHESTS_SIMPLE_DUNGEON, rand.nextLong());
					}
					break;
				}
			}
		}
		world.setBlockState(pos, Blocks.MOB_SPAWNER.getDefaultState(), 2);
		TileEntityMobSpawner tileentitymobspawner = (TileEntityMobSpawner)world.getTileEntity(pos);
		if(tileentitymobspawner != null) {
			tileentitymobspawner.getSpawnerBaseLogic().setEntityId(pickMobSpawner(rand));
		}
	}

	public void genHouse(World world, Random rand, BlockPos pos, boolean generateLoot) {
		IBlockState state = Blocks.PLANKS.getStateFromMeta(rand.nextInt(6));
		if(rand.nextBoolean()) {
			state = Blocks.STONEBRICK.getDefaultState();
		}
		int sizeX = rand.nextInt(2) + 2;
		int sizeZ = rand.nextInt(2) + 2;
		for(int x = -sizeX-1; x <= sizeX+1; x++) {
			for(int y = 4; y >= -1; y--) {
				for(int z = -sizeZ-1; z <= sizeZ+1; z++) {
					if(x != -sizeX-1 && y != -1 && z != -sizeZ-1 && x != sizeX+1 && y != 4 && z != sizeZ+1) {
						world.setBlockToAir(pos.add(x, y, z));
					}
					else if(y == -1 && rand.nextInt(4) != 0) {
						world.setBlockState(pos.add(x, y, z), state, 2);
					}
					else {
						world.setBlockState(pos.add(x, y, z), state, 2);
					}
				}
			}
		}
		for(int i = 0; i < 12; i++) {
			int x = rand.nextInt(sizeX * 2 + 1) - sizeX;
			int z = rand.nextInt(sizeZ * 2 + 1) - sizeZ;
			if(world.isAirBlock(pos.add(x, 0, z))) {
				int sideCount = 0;
				if(world.getBlockState(pos.add(x-1, 0, z)).getMaterial().isSolid()) {
					sideCount++;
				}
				if(world.getBlockState(pos.add(x+1, 0, z)).getMaterial().isSolid()) {
					sideCount++;
				}
				if(world.getBlockState(pos.add(x, 0, z-1)).getMaterial().isSolid()) {
					sideCount++;
				}
				if(world.getBlockState(pos.add(x, 0, z+1)).getMaterial().isSolid()) {
					sideCount++;
				}
				if(sideCount == 1) {
					if(generateLoot) {
						world.setBlockState(pos.add(x, 0, z), Blocks.CHEST.getDefaultState(), 2);
						TileEntityChest tileentitychest = (TileEntityChest)world.getTileEntity(pos.add(x, 0, z));
						if(tileentitychest != null) {
							tileentitychest.setLootTable(LootTableList.CHESTS_STRONGHOLD_CORRIDOR, rand.nextLong());
						}
					}
					if(rand.nextInt(5) == 0) {
						break;
					}
				}
			}
		}
	}

	private ResourceLocation pickMobSpawner(Random rand) {
		return DungeonHooks.getRandomDungeonMob(rand);
	}

	public static enum EnumGenerationType {

		ORES(10),
		BIOME(4),
		DUNGEON(6),
		HOUSE(4),
		DUNGEON_LOOT(4),
		//PLAYER(1),
		//PIRATES(1),
		//ANCIENT(2),
		;

		private final int weight;

		EnumGenerationType(int weight) {
			this.weight = weight;
		}

		public static List<WeightedEnum<EnumGenerationType>> getWeightedList() {
			ArrayList<WeightedEnum<EnumGenerationType>> ret = Lists.<WeightedEnum<EnumGenerationType>>newArrayList();
			for(EnumGenerationType type : values()) {
				ret.add(new WeightedEnum<EnumGenerationType>(type.weight, type));
			}
			return ret;
		}
	}
}
