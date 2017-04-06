package ec3.utils.common;

import DummyCore.Utils.MiscUtils;
import DummyCore.Utils.ScheduledServerAction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class ServerToClientSyncAction extends ScheduledServerAction {

	EntityPlayer p;
	TileEntity t;
	
	public ServerToClientSyncAction(EntityPlayer requester, TileEntity tile)  {
		super(20);
		p = requester;
		t = tile;
	}

	@Override
	public void execute() {
		if(t != null && p != null && t.getWorld() != null)
			if(t.getWorld().isBlockLoaded(t.getPos()))
				if(t.getWorld().getTileEntity(t.getPos()) == t)
					MiscUtils.sendPacketToPlayer(t.getWorld(), t.getUpdatePacket(), p);
	}
}
