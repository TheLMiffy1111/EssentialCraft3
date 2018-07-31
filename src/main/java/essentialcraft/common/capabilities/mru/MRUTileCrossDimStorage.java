package essentialcraft.common.capabilities.mru;

import DummyCore.Utils.MiscUtils;
import essentialcraft.api.IMRUHandler;
import essentialcraft.common.item.ItemBoundGem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MRUTileCrossDimStorage extends MRUTileStorage {

	public MRUTileCrossDimStorage() {
		super();
	}

	public MRUTileCrossDimStorage(int maxMRU) {
		super(maxMRU);
	}

	@Override
	public void mruIn(BlockPos pos, World world, ItemStack boundGem) {
		if(boundGem.getItem() instanceof ItemBoundGem && boundGem.getTagCompound() != null) {
			int[] o = ItemBoundGem.getCoords(boundGem);
			BlockPos o1 = new BlockPos(o[0],o[1],o[2]);
			if(getDimension(boundGem) == world.provider.getDimension()) {
				if(!pos.equals(o1) && world.getTileEntity(o1) != null && world.getTileEntity(o1).hasCapability(CapabilityMRUHandler.MRU_HANDLER_CAPABILITY, null)) {
					IMRUHandler other = world.getTileEntity(o1).getCapability(CapabilityMRUHandler.MRU_HANDLER_CAPABILITY, null);
					if(this.getMRU() < this.getMaxMRU()) {
						int req = this.getMaxMRU() - this.getMRU();
						int extracted = other.extractMRU(req, true);
						this.setBalance((other.getBalance()*extracted+this.getBalance()*this.getMRU())/(extracted+this.getMRU()));
						this.addMRU(extracted, true);
					}
				}
			}
			else if(!world.isRemote) {
				World worldOther = world.getMinecraftServer().getWorld(getDimension(boundGem));
				if(worldOther != null && worldOther.getTileEntity(o1) != null && worldOther.getTileEntity(o1).hasCapability(CapabilityMRUHandler.MRU_HANDLER_CAPABILITY, null)) {
					IMRUHandler other = worldOther.getTileEntity(o1).getCapability(CapabilityMRUHandler.MRU_HANDLER_CAPABILITY, null);
					if(this.getMRU() < this.getMaxMRU()) {
						int req = this.getMaxMRU() - this.getMRU();
						int extracted = other.extractMRU(req, true);
						this.setBalance((other.getBalance()*extracted+this.getBalance()*this.getMRU())/(extracted+this.getMRU()));
						this.addMRU(extracted, true);
					}
				}
			}
		}
	}

	@Override
	public void spawnMRUParticles(BlockPos pos, World world, ItemStack boundGem) {
		if(world.isRemote) {
			if(boundGem.getItem() instanceof ItemBoundGem && boundGem.getTagCompound() != null) {
				if(getDimension(boundGem) == world.provider.getDimension()) {
					int[] o = ItemBoundGem.getCoords(boundGem);
					BlockPos pos1 = new BlockPos(o[0], o[1], o[2]);
					doSpawnMRUParticles(pos, pos1, world);
				}
			}
		}
	}

	static int getDimension(ItemStack stack) {
		return MiscUtils.getStackTag(stack).getInteger("dim");
	}
}
