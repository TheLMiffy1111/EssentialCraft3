package essentialcraft.common.tile;

import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.MathUtils;
import essentialcraft.api.ApiCore;
import essentialcraft.common.block.BlockCorruption;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.config.Configuration;

public class TileCorruptionCleaner extends TileMRUGeneric {

	public BlockPos cleared;
	public int clearTime = 0;

	public static int maxRadius = 8;
	public static boolean removeBlock = false;
	public static int mruUsage = 20;
	public static int ticksRequired = 200;
	public static int cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;

	public TileCorruptionCleaner() {
		super(cfgMaxMRU);
		setSlotsNum(1);
	}

	@Override
	public void update() {
		super.update();
		mruStorage.update(getPos(), getWorld(), getStackInSlot(0));

		if(!getWorld().isRemote && getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			if(cleared == null) {
				int offsetX = (int)(MathUtils.randomDouble(getWorld().rand)*maxRadius);
				int offsetY = (int)(MathUtils.randomDouble(getWorld().rand)*maxRadius);
				int offsetZ = (int)(MathUtils.randomDouble(getWorld().rand)*maxRadius);
				Block b = getWorld().getBlockState(pos.add(offsetX, offsetY, offsetZ)).getBlock();
				if(b instanceof BlockCorruption) {
					cleared = pos.add(offsetX, offsetY, offsetZ);
					clearTime = ticksRequired;
				}
			}
			else {
				Block b = getWorld().getBlockState(cleared).getBlock();
				if(!(b instanceof BlockCorruption)) {
					cleared = null;
					clearTime = 0;
					return;
				}
				if(mruStorage.getMRU() >= mruUsage) {
					--clearTime;
					mruStorage.extractMRU(mruUsage, true);
					if(clearTime <= 0) {
						int metadata = getWorld().getBlockState(cleared).getValue(BlockCorruption.LEVEL);
						if(metadata == 0 || removeBlock)
							getWorld().setBlockToAir(cleared);
						else
							getWorld().setBlockState(cleared, b.getStateFromMeta(metadata-1), 2);
						cleared = null;
					}
				}
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound i) {
		if(i.hasKey("coord")) {
			String str = i.getString("coord");
			if(!str.equals("null")) {
				DummyData[] coordData = DataStorage.parseData(i.getString("coord"));
				cleared = new BlockPos(Integer.parseInt(coordData[0].fieldValue), Integer.parseInt(coordData[1].fieldValue), Integer.parseInt(coordData[2].fieldValue));
			}
			else {
				cleared = null;
			}
		}
		clearTime = i.getInteger("clear");
		super.readFromNBT(i);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound i) {
		if(cleared != null) {
			i.setString("coord", "||x:" + cleared.getX() + "||y:" + cleared.getY() + "||z:" + cleared.getZ());
		}
		else {
			i.setString("coord", "null");
		}
		i.setInteger("clear", clearTime);
		return super.writeToNBT(i);
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.corruptioncleaner";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.DEVICE_MAX_MRU_GENERIC).setMinValue(1).getInt();
			mruUsage = cfg.get(category, "MRUUsage", 20, "Usage per tick").setMinValue(0).setMaxValue(cfgMaxMRU).getInt();
			ticksRequired = cfg.get(category, "TicksRequired", 200, "Time required to destroy corruption").setMinValue(0).getInt();
			removeBlock = cfg.get(category, "RemoveBlock", false, "Should remove corruption blocks instead of reducing their growth").getBoolean();
			maxRadius = cfg.get(category, "MaxRadius", 8).setMinValue(0).getInt();
		}
		catch(Exception e) {
			return;
		}
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(-maxRadius, -maxRadius, -maxRadius, 1+maxRadius, 1+maxRadius, 1+maxRadius);
	}

	@Override
	public int[] getOutputSlots() {
		return new int[0];
	}
}
