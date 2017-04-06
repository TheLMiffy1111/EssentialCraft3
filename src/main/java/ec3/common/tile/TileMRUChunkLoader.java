package ec3.common.tile;

import java.util.HashSet;
import java.util.Set;

import ec3.common.mod.EssentialCraftCore;
import ec3.utils.common.ECUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;

public class TileMRUChunkLoader extends TileMRUGeneric {

	public Ticket chunkTicket;
	public Set<ChunkPos> chunkSet = new HashSet<ChunkPos>();
	public boolean getChunks = true;
	
	public TileMRUChunkLoader() {
		setMaxMRU(5000F);
		setSlotsNum(1);
	}

	@Override
	public void update() {
		super.update();
		ECUtils.manage(this, 0);

		if(!getWorld().isRemote) {
			manage();
		}
		
		if(getMRU() >= 5) {
			try {

			}
			catch(Exception e) {
				return;
			}
		}
	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return isBoundGem(p_94041_2_);
	}

	@Override
	public int[] getOutputSlots() {
		return new int[0];
	}

	public void manage() {
		if(getChunks) {
			getChunks = false;
			chunkSet.add(new ChunkPos(pos));
		}
		
		if(chunkTicket != null && (!canOperate() || chunkTicket.world != getWorld()))
			release();

		sortChunks();

		if(canOperate() && chunkTicket == null) {
			Ticket ticket = ForgeChunkManager.requestTicket(EssentialCraftCore.core, getWorld(), Type.NORMAL);

			if(ticket != null) {
				ticket.getModData().setInteger("xCoord", getPos().getX());
				ticket.getModData().setInteger("yCoord", getPos().getY());
				ticket.getModData().setInteger("zCoord", getPos().getZ());

				forceChunks(ticket);
			}
		}
		
		if(canOperate()) {
			setMRU(getMRU() - 5);
		}
	}

	public boolean canOperate() {
		return getMRU() >= 5;
	}

	@Override
	public void invalidate() {
		super.invalidate();
		if(!this.getWorld().isRemote)
			release();
	}

	public void setTicket(Ticket t) {
		if(chunkTicket != t && chunkTicket != null && chunkTicket.world == getWorld()) {
			for(ChunkPos chunk : chunkTicket.getChunkList()) {
				if(ForgeChunkManager.getPersistentChunksFor(getWorld()).keys().contains(chunk)) {
					ForgeChunkManager.unforceChunk(chunkTicket, chunk);
				}
			}

			ForgeChunkManager.releaseTicket(chunkTicket);
		}

		chunkTicket = t;
	}

	public void release() {
		setTicket(null);
	}

	public void sortChunks() {
		if(chunkTicket != null) {
			for(ChunkPos chunk : chunkTicket.getChunkList()) {
				if(!chunkSet.contains(chunk)) {
					if(ForgeChunkManager.getPersistentChunksFor(getWorld()).keys().contains(chunk)) {
						ForgeChunkManager.unforceChunk(chunkTicket, chunk);
					}
				}
			}

			for(ChunkPos chunk : chunkSet) {
				if(!chunkTicket.getChunkList().contains(chunk)) {
					ForgeChunkManager.forceChunk(chunkTicket, chunk);
				}
			}
		}
	}

	public void forceChunks(Ticket ticket) {
		setTicket(ticket);

		for(ChunkPos chunk : chunkSet) {
			ForgeChunkManager.forceChunk(chunkTicket, chunk);
		}
	}
}
