package ec3.common.tile;

import DummyCore.Utils.MiscUtils;
import DummyCore.Utils.Notifier;
import DummyCore.Utils.TileStatTracker;
import ec3.common.mod.EssentialCraftCore;
import ec3.utils.common.ECUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileMimic extends TileEntity implements ITickable {
	
	private IBlockState mimicState;
	private TileStatTracker tracker;
	public int innerRotation;
	public int syncTick = 10;
	public boolean requestSync = true;
	public boolean firstTick = true;

	public TileMimic() {
		super();
		tracker = new TileStatTracker(this);
	}

	@Override
	public void update() {
		++innerRotation;
		if(syncTick == 0) {
			if(tracker == null)
				Notifier.notifyCustomMod("EssentialCraft", "[WARNING][SEVERE]TileEntity " + this + " at pos " + pos.getX() + "," + pos.getY() + ","  + pos.getZ() + " tries to sync itself, but has no TileTracker attached to it! SEND THIS MESSAGE TO THE DEVELOPER OF THE MOD!");
			else if(!getWorld().isRemote && tracker.tileNeedsSyncing()) {
				MiscUtils.sendPacketToAllAround(getWorld(), getUpdatePacket(), pos.getX(), pos.getY(), pos.getZ(), getWorld().provider.getDimension(), 32);
			}
			syncTick = 60;
		}
		else
			--syncTick;

		if(requestSync && getWorld().isRemote) {
			requestSync = false;
			ECUtils.requestScheduledTileSync(this, EssentialCraftCore.proxy.getClientPlayer());
		}

		if(firstTick) {
			getWorld().markBlockRangeForRenderUpdate(pos.getX(), pos.getY(), pos.getZ(), pos.getX()+1, pos.getY()+1, pos.getZ()+1);
			firstTick = false;
		}
	}

	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		writeToNBT(nbttagcompound);
		return new SPacketUpdateTileEntity(pos, -10, nbttagcompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		if(pkt.getTileEntityType() == -10) {
			readFromNBT(pkt.getNbtCompound());
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		Block b = Block.getBlockFromName(compound.getString("mimic"));
		if(b != null) {
			mimicState = b.getStateFromMeta(compound.getInteger("mimicMeta"));
		}
		super.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		if(mimicState != null) {
			compound.setString("mimic", Block.REGISTRY.getNameForObject(mimicState.getBlock()).toString());
			compound.setInteger("mimicMeta", mimicState.getBlock().getMetaFromState(mimicState));
		}
		else {
			compound.setString("mimic", "");
			compound.setInteger("mimicMeta", 0);
		}
		return super.writeToNBT(compound);
	}

	public IBlockState getState() {
		return mimicState;
	}

	public void setState(IBlockState mimicState) {
		this.mimicState = mimicState;
	}
}
