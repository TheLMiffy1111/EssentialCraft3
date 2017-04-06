package ec3.common.world.dim;

import java.util.Random;

import ec3.common.world.WorldGenOldCatacombs;
import ec3.common.world.structure.MapGenModernShafts;
import ec3.common.world.structure.MapGenTown;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderOverworld;

public class ChunkProviderFirstWorld extends ChunkProviderOverworld {

	public Random rand;
	public World world;
	public MapGenTown town = new MapGenTown();
	public MapGenModernShafts shafts = new MapGenModernShafts();
	public static ChunkProviderFirstWorld instance;

	public static MapGenTown getGen()
	{
		return instance.town;
	}

	public ChunkProviderFirstWorld(World p_i2006_1_, long p_i2006_2_, boolean p_i2006_4_, String p_i2006_5_) {
		super(p_i2006_1_, p_i2006_2_, false, p_i2006_5_);
		instance = this;
		try
		{
			rand = new Random(p_i2006_2_);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return;
		}
		this.world = p_i2006_1_;
	}

	public void populate(int p_73153_2_, int p_73153_3_)
	{
		instance = this;
		super.populate(p_73153_2_, p_73153_3_);
		int k = p_73153_2_ * 16;
		int l = p_73153_3_ * 16;

		try
		{
			this.town.generateStructure(this.world, this.rand, new ChunkPos(p_73153_2_, p_73153_3_));
			this.shafts.generateStructure(this.world, this.rand, new ChunkPos(p_73153_2_, p_73153_3_));
		}
		catch(Exception e)
		{
			return;
		}

		int y = this.rand.nextInt(256);
		if(y > 6 && y < 32 && this.rand.nextFloat() < 0.1F)
		{
			int l1 = k + this.rand.nextInt(16) + 8;
			int j2 = l + this.rand.nextInt(16) + 8;
			(new WorldGenOldCatacombs()).generate(this.world, this.rand, new BlockPos(l1, y, j2));
		}

	}

	public Chunk provideChunk(int p_73154_1_, int p_73154_2_)
	{
		instance = this;
		this.town.generate(this.world, p_73154_1_, p_73154_2_, new ChunkPrimer());
		this.shafts.generate(this.world, p_73154_1_, p_73154_2_, new ChunkPrimer());
		return super.provideChunk(p_73154_1_, p_73154_2_);
	}

	public void recreateStructures(Chunk chunk, int p_82695_1_, int p_82695_2_)
	{
		this.town.generate(this.world, p_82695_1_, p_82695_2_, new ChunkPrimer());
		this.shafts.generate(this.world, p_82695_1_, p_82695_2_, new ChunkPrimer());
	}
}
