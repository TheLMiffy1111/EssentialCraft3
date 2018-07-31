package essentialcraft.common.capabilities.mru;

import DummyCore.Utils.MathUtils;
import essentialcraft.api.IMRUHandler;
import essentialcraft.api.IWorldUpdatable;
import essentialcraft.common.item.ItemBoundGem;
import essentialcraft.common.mod.EssentialCraftCore;
import essentialcraft.common.tile.TileMRUReactor;
import essentialcraft.common.tile.TileRayTower;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MRUTileStorage extends MRUStorage implements IWorldUpdatable<ItemStack> {

	protected int range = 16;

	public MRUTileStorage() {
		super();
	}

	public MRUTileStorage(int maxMRU) {
		super(maxMRU);
	}

	public int getRange() {
		return this.range;
	}

	public MRUTileStorage setRange(int range) {
		this.range = range;
		return this;
	}

	@Override
	public void update(BlockPos pos, World world, ItemStack boundGem) {
		this.spawnMRUParticles(pos, world, boundGem);
		this.mruIn(pos, world, boundGem);
	}

	public void mruIn(BlockPos pos, World world, ItemStack boundGem) {
		if(!world.isRemote && boundGem.getItem() instanceof ItemBoundGem && boundGem.getTagCompound() != null) {
			int[] o = ItemBoundGem.getCoords(boundGem);
			if(MathUtils.getDifference(pos.getX(), o[0]) <= range && MathUtils.getDifference(pos.getY(), o[1]) <= range && MathUtils.getDifference(pos.getZ(), o[2]) <= range) {
				BlockPos o1 = new BlockPos(o[0], o[1], o[2]);
				if(!pos.equals(o1) && world.getTileEntity(o1) != null && world.getTileEntity(o1).hasCapability(CapabilityMRUHandler.MRU_HANDLER_CAPABILITY, null)) {
					IMRUHandler other = world.getTileEntity(o1).getCapability(CapabilityMRUHandler.MRU_HANDLER_CAPABILITY, null);
					if(this.getMRU() < this.getMaxMRU()) {
						int req = this.getMaxMRU() - this.getMRU();
						int extracted = other.extractMRU(req, true);
						if(extracted+this.getMRU() > 0)
							this.setBalance((other.getBalance()*extracted+this.getBalance()*this.getMRU())/(extracted+this.getMRU()));
						this.addMRU(extracted, true);
					}
				}
			}
		}
	}

	public void spawnMRUParticles(BlockPos pos, World world, ItemStack boundGem) {
		if(world.isRemote) {
			if(boundGem.getItem() instanceof ItemBoundGem && boundGem.getTagCompound() != null) {
				int[] o = ItemBoundGem.getCoords(boundGem);
				if(MathUtils.getDifference(pos.getX(), o[0]) <= range && MathUtils.getDifference(pos.getY(), o[1]) <= range && MathUtils.getDifference(pos.getZ(), o[2]) <= range) {
					BlockPos pos1 = new BlockPos(o[0], o[1], o[2]);
					doSpawnMRUParticles(pos, pos1, world);
				}
			}
		}
	}

	public void doSpawnMRUParticles(BlockPos posThis, BlockPos posOther, World world) {
		if(world.getTileEntity(posOther) != null && world.getTileEntity(posOther).hasCapability(CapabilityMRUHandler.MRU_HANDLER_CAPABILITY, null)) {
			IMRUHandler other = world.getTileEntity(posOther).getCapability(CapabilityMRUHandler.MRU_HANDLER_CAPABILITY, null);
			float balance = other.getBalance();
			float colorRRender = 0.0F;
			float colorGRender = 1.0F;
			float colorBRender = 1.0F;

			float colorRNormal = 0.0F;
			float colorGNormal = 1.0F;
			float colorBNormal = 1.0F;

			float colorRChaos = 1.0F;
			float colorGChaos = 0.0F;
			float colorBChaos = 0.0F;

			float colorRFrozen = 0.0F;
			float colorGFrozen = 0.0F;
			float colorBFrozen = 1.0F;
			if(balance!=1.0F) {
				if(balance<1.0F) {
					float diff = balance;
					if(diff < 0.01F)
						diff = 0.0F;
					colorRRender = colorRNormal*diff + colorRFrozen*(1.0F-diff);
					colorGRender = colorGNormal*diff + colorGFrozen*(1.0F-diff);
					colorBRender = colorBNormal*diff + colorBFrozen*(1.0F-diff);
				}
				if(balance>1.0F) {
					float diff = 2.0F-balance;
					if(diff < 0.01F)
						diff = 0.0F;
					colorRRender = colorRNormal*diff + colorRChaos*(1.0F-diff);
					colorGRender = colorGNormal*diff + colorGChaos*(1.0F-diff);
					colorBRender = colorBNormal*diff + colorBChaos*(1.0F-diff);
				}
			}
			//will change
			if(world.getTileEntity(posThis) instanceof TileRayTower) {
				if(world.getTileEntity(posOther) instanceof TileRayTower)
					EssentialCraftCore.proxy.MRUFX(posOther.getX()+0.5D, posOther.getY()+1.85D, posOther.getZ()+0.5D, posThis.getX()-posOther.getX(), posThis.getY()-posOther.getY()+0.25D, posThis.getZ()-posOther.getZ(),colorRRender,colorGRender,colorBRender);
				else if(world.getTileEntity(posOther) instanceof TileMRUReactor)
					EssentialCraftCore.proxy.MRUFX(posOther.getX()+0.5D, posOther.getY()+1.1D, posOther.getZ()+0.5D, posThis.getX()-posOther.getX(), posThis.getY()-posOther.getY()+0.8D, posThis.getZ()-posOther.getZ(),colorRRender,colorGRender,colorBRender);
				else
					EssentialCraftCore.proxy.MRUFX(posOther.getX()+0.5D, posOther.getY()+0.5D, posOther.getZ()+0.5D, posThis.getX()-posOther.getX(), posThis.getY()-posOther.getY()+1.5D, posThis.getZ()-posOther.getZ(),colorRRender,colorGRender,colorBRender);
			}
			else {
				if(world.getTileEntity(posOther) instanceof TileRayTower)
					EssentialCraftCore.proxy.MRUFX(posOther.getX()+0.5D, posOther.getY()+1.85D, posOther.getZ()+0.5D, posThis.getX()-posOther.getX(), posThis.getY()-posOther.getY()-1.5D, posThis.getZ()-posOther.getZ(),colorRRender,colorGRender,colorBRender);
				else if(world.getTileEntity(posOther) instanceof TileMRUReactor)
					EssentialCraftCore.proxy.MRUFX(posOther.getX()+0.5D, posOther.getY()+1.1D, posOther.getZ()+0.5D, posThis.getX()-posOther.getX(), posThis.getY()-posOther.getY()-0.6D, posThis.getZ()-posOther.getZ(),colorRRender,colorGRender,colorBRender);
				else
					EssentialCraftCore.proxy.MRUFX(posOther.getX()+0.5D, posOther.getY()+0.5D, posOther.getZ()+0.5D, posThis.getX()-posOther.getX(), posThis.getY()-posOther.getY(), posThis.getZ()-posOther.getZ(),colorRRender,colorGRender,colorBRender);
			}
		}
	}
}
