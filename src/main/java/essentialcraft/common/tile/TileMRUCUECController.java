package essentialcraft.common.tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import DummyCore.Utils.BlockPosition;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.MiscUtils;
import essentialcraft.api.EnumStructureType;
import essentialcraft.api.IMRUDisplay;
import essentialcraft.api.IMRUHandler;
import essentialcraft.api.IMRUHandlerEntity;
import essentialcraft.api.IStructurePiece;
import essentialcraft.common.capabilities.mru.CapabilityMRUHandler;
import essentialcraft.common.capabilities.mru.MRUTileStorage;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.config.Configuration;

public class TileMRUCUECController extends TileEntity implements IMRUDisplay, ITickable {

	//============================Variables================================//
	public int syncTick;
	public int structureCheckTick;
	public MRUTileStorage mruStorage = new MRUTileStorage();
	public float resistance;
	public BlockPos upperCoord;
	public BlockPos lowerCoord;

	public boolean isCorrect;

	public List<BlockPosition> blocksInStructure = new ArrayList<BlockPosition>();

	public static int cfgMaxMRU = 60000;
	public static int cfgMRUPerStorage = 100000;
	//===========================Functions=================================//

	@Override
	public void update() {
		//Retrying structure checks. Basically, every 10 seconds the structure will re-initialize//
		if(structureCheckTick == 0) {
			isCorrect = checkStructure();
			structureCheckTick = 200;
		}
		else
			--structureCheckTick;

		//Sending the sync packets to the CLIENT.
		if(syncTick == 0) {
			if(!getWorld().isRemote)
				MiscUtils.sendPacketToAllAround(getWorld(), getUpdatePacket(), pos.getX(), pos.getY(), pos.getZ(), getWorld().provider.getDimension(), 16);
			syncTick = 30;
		}
		else
			--syncTick;
	}

	public IMRUHandlerEntity getMRUCU() {
		if(isCorrect) {
			List<Entity> eList = getWorld().getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(lowerCoord, upperCoord), e->e.hasCapability(CapabilityMRUHandler.MRU_HANDLER_ENTITY_CAPABILITY, null));
			if(eList != null && !eList.isEmpty())
				return eList.get(0).getCapability(CapabilityMRUHandler.MRU_HANDLER_ENTITY_CAPABILITY, null);
		}
		return null;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		writeToNBT(nbttagcompound);
		return new SPacketUpdateTileEntity(pos, -10, nbttagcompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		if(pkt.getTileEntityType() == -10)
			readFromNBT(pkt.getNbtCompound());
	}


	@Override
	public void readFromNBT(NBTTagCompound i) {
		super.readFromNBT(i);
		mruStorage.readFromNBT(i);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound i) {
		super.writeToNBT(i);
		mruStorage.writeToNBT(i);
		return i;
	}

	/**
	 * Checking the shape of the structure;
	 * @return - false, if the structure is incorrect, true otherwise
	 */
	public boolean checkStructure() {
		resistance = 0F;
		mruStorage.setMaxMRU(cfgMaxMRU);
		blocksInStructure.clear(); //Clearing the list of blocks to reinitialize it
		//Base variables setup//
		int minX = 0;
		int minY = 0;
		int minZ = 0;
		int maxX = 0;
		int maxY = 0;
		int maxZ = 0;
		int checkInt0 = 0;
		Collection<Block> allowedBlocks = ECUtils.STRUCTURE_TO_BLOCKS_MAP.get(EnumStructureType.MRUCUEC); //Getting the list of allowed blocks in the structure
		//Trying to find the whole shape of a structure//
		while(allowedBlocks.contains(getWorld().getBlockState(pos.add(checkInt0, 0, 0)).getBlock())) {
			++checkInt0;
		}
		--checkInt0;
		if(checkInt0 > 0)
			maxX = checkInt0;
		checkInt0 = 0;
		while(allowedBlocks.contains(getWorld().getBlockState(pos.add(checkInt0, 0, 0)).getBlock())) {
			--checkInt0;
		}
		++checkInt0;
		if(checkInt0 < 0)
			minX = checkInt0;
		if(maxX == 0) {
			checkInt0 = 0;
			while(allowedBlocks.contains(getWorld().getBlockState(pos.add(0, 0, checkInt0)).getBlock())) {
				++checkInt0;
			}
			--checkInt0;
			if(checkInt0 > 0)
				maxZ = checkInt0;
		}
		if(minX == 0) {
			checkInt0 = 0;
			while(allowedBlocks.contains(getWorld().getBlockState(pos.add(0, 0, checkInt0)).getBlock())) {
				--checkInt0;
			}
			++checkInt0;
			if(checkInt0 < 0)
				minZ = checkInt0;
		}
		if(maxX == 0 && maxZ != 0) {
			checkInt0 = 0;
			while(allowedBlocks.contains(getWorld().getBlockState(pos.add(checkInt0, 0, maxZ)).getBlock())) {
				++checkInt0;
			}
			--checkInt0;
			if(checkInt0 > 0)
				maxX = checkInt0;
		}
		if(minX == 0 && maxZ != 0) {
			checkInt0 = 0;
			while(allowedBlocks.contains(getWorld().getBlockState(pos.add(checkInt0, 0, maxZ)).getBlock())) {
				--checkInt0;
			}
			++checkInt0;
			if(checkInt0 < 0)
				minX = checkInt0;
		}
		if(maxX == 0 && minZ != 0) {
			checkInt0 = 0;
			while(allowedBlocks.contains(getWorld().getBlockState(pos.add(checkInt0, 0, minZ)).getBlock())) {
				++checkInt0;
			}
			--checkInt0;
			if(checkInt0 > 0)
				maxX = checkInt0;
		}
		if(minX == 0 && minZ != 0) {
			checkInt0 = 0;
			while(allowedBlocks.contains(getWorld().getBlockState(pos.add(checkInt0, 0, minZ)).getBlock())) {
				--checkInt0;
			}
			++checkInt0;
			if(checkInt0 < 0)
				minX = checkInt0;
		}
		if(maxZ == 0 && maxX != 0) {
			checkInt0 = 0;
			while(allowedBlocks.contains(getWorld().getBlockState(pos.add(maxX, 0, checkInt0)).getBlock())) {
				++checkInt0;
			}
			--checkInt0;
			if(checkInt0 > 0)
				maxZ = checkInt0;
		}
		if(minZ == 0 && maxX != 0) {
			checkInt0 = 0;
			while(allowedBlocks.contains(getWorld().getBlockState(pos.add(maxX, 0, checkInt0)).getBlock())) {
				--checkInt0;
			}
			++checkInt0;
			if(checkInt0 < 0)
				minZ = checkInt0;
		}
		if(maxZ == 0 && minX != 0) {
			checkInt0 = 0;
			while(allowedBlocks.contains(getWorld().getBlockState(pos.add(minX, 0, checkInt0)).getBlock())) {
				++checkInt0;
			}
			--checkInt0;
			if(checkInt0 > 0)
				maxZ = checkInt0;
		}
		if(minZ == 0 && minX != 0) {
			checkInt0 = 0;
			while(allowedBlocks.contains(getWorld().getBlockState(pos.add(minX, 0, checkInt0)).getBlock())) {
				--checkInt0;
			}
			++checkInt0;
			if(checkInt0 < 0)
				minZ = checkInt0;
		}
		if(maxY == 0 && maxX != 0 && maxZ != 0) {
			checkInt0 = 0;
			while(allowedBlocks.contains(getWorld().getBlockState(pos.add(maxX, checkInt0, maxZ)).getBlock())) {
				++checkInt0;
			}
			--checkInt0;
			if(checkInt0 > 0)
				maxY = checkInt0;
		}
		if(maxY == 0 && minX != 0 && maxZ != 0) {
			checkInt0 = 0;
			while(allowedBlocks.contains(getWorld().getBlockState(pos.add(minX, checkInt0, maxZ)).getBlock())) {
				++checkInt0;
			}
			--checkInt0;
			if(checkInt0 > 0)
				maxY = checkInt0;
		}
		if(maxY == 0 && maxX != 0 && minZ != 0) {
			checkInt0 = 0;
			while(allowedBlocks.contains(getWorld().getBlockState(pos.add(maxX, checkInt0, minZ)).getBlock())) {
				++checkInt0;
			}
			--checkInt0;
			if(checkInt0 > 0)
				maxY = checkInt0;
		}
		if(maxY == 0 && minX != 0 && minZ != 0) {
			checkInt0 = 0;
			while(allowedBlocks.contains(getWorld().getBlockState(pos.add(minX, checkInt0, minZ)).getBlock())) {
				++checkInt0;
			}
			--checkInt0;
			if(checkInt0 > 0)
				maxY = checkInt0;
		}
		if(minY == 0 && maxX != 0 && minZ != 0) {
			checkInt0 = 0;
			while(allowedBlocks.contains(getWorld().getBlockState(pos.add(maxX, checkInt0, minZ)).getBlock())) {
				--checkInt0;
			}
			++checkInt0;
			minY = checkInt0;
		}
		if(minY == 0 && minX != 0 && minZ != 0) {
			checkInt0 = 0;
			while(allowedBlocks.contains(getWorld().getBlockState(pos.add(minX, checkInt0, minZ)).getBlock())) {
				--checkInt0;
			}
			++checkInt0;
			if(checkInt0 < 0)
				minY = checkInt0;
		}
		if(minY == 0 && minX != 0 && maxZ != 0) {
			checkInt0 = 0;
			while(allowedBlocks.contains(getWorld().getBlockState(pos.add(maxX, checkInt0, minZ)).getBlock())) {
				--checkInt0;
			}
			++checkInt0;
			if(checkInt0 < 0)
				minY = checkInt0;
		}
		if(minY == 0 && maxX != 0 && maxZ != 0) {
			checkInt0 = 0;
			while(allowedBlocks.contains(getWorld().getBlockState(pos.add(minX, checkInt0, minZ)).getBlock())) {
				--checkInt0;
			}
			++checkInt0;
			if(checkInt0 < 0)
				minY = checkInt0;
		}
		//Checking for the cuboid shape//
		if(minX == 0 && maxX == 0 || minY == 0 && maxY == 0 || minZ == 0 && maxZ == 0)
			return false;
		else {
			lowerCoord = pos.add(minX, minY, minZ);
			upperCoord = pos.add(maxX, maxY, maxZ);
			for(int x = minX; x <= maxX; ++x) {
				for(int y = minY; y <= maxY; ++y) {
					for(int z = minZ; z <= maxZ; ++z) {
						if(z == minZ || z == maxZ || x == minX || x == maxX || y == minY || y == maxY) {
							BlockPos cp = new BlockPos(pos.add(x, y, z));
							if(allowedBlocks.contains(getWorld().getBlockState(cp).getBlock())) {
								blocksInStructure.add(new BlockPosition(getWorld(), pos.getX()+x, pos.getY()+y, pos.getZ()+z));
								int meta = getWorld().getBlockState(cp).getBlock().getMetaFromState(getWorld().getBlockState(cp));
								if(ECUtils.IGNORE_META.containsKey(getWorld().getBlockState(cp).getBlock().getUnlocalizedName()) && ECUtils.IGNORE_META.get(getWorld().getBlockState(cp).getBlock().getUnlocalizedName()))
									meta = -1;
								DummyData dt = new DummyData(getWorld().getBlockState(cp).getBlock().getUnlocalizedName(),meta);
								if(ECUtils.MRU_RESISTANCES.containsKey(dt.toString()))
									resistance += ECUtils.MRU_RESISTANCES.get(dt.toString());
								else
									resistance += 1F;
								if(getWorld().getTileEntity(cp) != null && getWorld().getTileEntity(cp) instanceof IStructurePiece) {
									IStructurePiece piece = (IStructurePiece) getWorld().getTileEntity(cp);
									piece.setStructureController(this, EnumStructureType.MRUCUEC);
									if(getWorld().getTileEntity(cp) instanceof TileMRUCUECHoldingChamber)
										mruStorage.setMaxMRU(mruStorage.getMaxMRU()+cfgMRUPerStorage);
								}
							}
							else
								return false;
						}
					}
				}
			}
		}
		return true;
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.enrichmentchamber";
			cfgMaxMRU = cfg.get(category, "MaxMRU", 60000).setMinValue(1).getInt();
			cfgMRUPerStorage = cfg.get(category, "MRUPerStorage", 100000).setMinValue(0).getInt();
		}
		catch(Exception e) {
			return;
		}
	}

	@Override
	public IMRUHandler getMRUHandler() {
		return mruStorage;
	}
}
