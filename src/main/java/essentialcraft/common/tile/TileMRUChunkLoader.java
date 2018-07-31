package essentialcraft.common.tile;

import java.util.Set;

import com.google.common.collect.Sets;

import DummyCore.Utils.DummyChunkLoader;
import DummyCore.Utils.DummyChunkLoader.IChunkLoader;
import essentialcraft.api.ApiCore;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.config.Configuration;

public class TileMRUChunkLoader extends TileMRUGeneric implements IChunkLoader {

	public static int cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC*10;
	public static int mruUsage = 5;

	public boolean getChunks = true;
	public DummyChunkLoader loader = new DummyChunkLoader(this);

	public TileMRUChunkLoader() {
		super(ApiCore.DEVICE_MAX_MRU_GENERIC);
		setSlotsNum(1);
	}

	@Override
	public void update() {
		super.update();
		mruStorage.update(getPos(), getWorld(), getStackInSlot(0));

		if(!getWorld().isRemote) {
			loader.tick();
			if(canOperate()) {
				mruStorage.extractMRU(mruUsage, true);
			}
		}
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return isBoundGem(stack);
	}

	@Override
	public int[] getOutputSlots() {
		return new int[0];
	}

	@Override
	public boolean canOperate() {
		return mruStorage.getMRU() >= mruUsage;
	}

	@Override
	public void invalidate() {
		super.invalidate();
		if(!this.getWorld().isRemote)
			loader.invalidate();
	}

	@Override
	public DummyChunkLoader getChunkLoader() {
		return loader;
	}

	@Override
	public Set<ChunkPos> getChunks() {
		return Sets.<ChunkPos>newHashSet(new ChunkPos(getPos()));
	}

	@Override
	public void readFromNBT(NBTTagCompound i) {
		loader.read(i);
		super.readFromNBT(i);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound i) {
		loader.write(i);
		return super.writeToNBT(i);
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.mruchunkloader";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.DEVICE_MAX_MRU_GENERIC*10).setMinValue(1).getInt();
			mruUsage = cfg.get(category, "MRUUsage", 5).setMinValue(0).setMaxValue(cfgMaxMRU).getInt();
		}
		catch(Exception e) {
			return;
		}
	}
}
