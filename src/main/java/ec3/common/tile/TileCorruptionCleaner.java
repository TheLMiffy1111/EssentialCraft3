package ec3.common.tile;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.config.Configuration;
import DummyCore.Utils.Coord3D;
import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.MathUtils;
import ec3.api.ApiCore;
import ec3.common.block.BlockCorruption_Light;
import ec3.utils.common.ECUtils;

public class TileCorruptionCleaner extends TileMRUGeneric {
	
	public Coord3D cleared;
	public int clearTime = 0;
	
	public static int maxRadius = 8;
	public static boolean removeBlock = false;
	public static int mruUsage = 20;
	public static int ticksRequired = 200;
	public static float cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;
	
	public TileCorruptionCleaner() {
		super();
		maxMRU = (int) cfgMaxMRU;
		setSlotsNum(1);
	}
	
	@Override
	public void update() {
		super.update();
		ECUtils.manage(this, 0);
		
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			if(cleared == null) {
				if(!getWorld().isRemote) {
					int offsetX = (int)(MathUtils.randomDouble(getWorld().rand)*maxRadius);
					int offsetY = (int)(MathUtils.randomDouble(getWorld().rand)*maxRadius);
					int offsetZ = (int)(MathUtils.randomDouble(getWorld().rand)*maxRadius);
					Block b = getWorld().getBlockState(pos.add(offsetX, offsetY, offsetZ)).getBlock();
					if(b instanceof BlockCorruption_Light) {
						cleared = new Coord3D(pos.getX()+offsetX, pos.getY()+offsetY, pos.getZ()+offsetZ);
						clearTime = ticksRequired;
					}
				}
			}
			else {
				if(!getWorld().isRemote) {
					BlockPos clearing = new BlockPos((int)cleared.x, (int)cleared.y, (int)cleared.z);
					Block b = getWorld().getBlockState(clearing).getBlock();
					if(!(b instanceof BlockCorruption_Light)) {
						cleared = null;
						clearTime = 0;
						return;
					}
					if(getMRU() - mruUsage > 0) {
						--clearTime;
						setMRU(getMRU()-mruUsage);
						if(clearTime <= 0) {
							int metadata = getWorld().getBlockState(clearing).getValue(BlockCorruption_Light.LEVEL);
							if(metadata == 0 || removeBlock)
								getWorld().setBlockToAir(clearing);
							else
								getWorld().setBlockState(clearing, b.getStateFromMeta(metadata-1), 3);
							cleared = null;
						}
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
				cleared = new Coord3D(Double.parseDouble(coordData[0].fieldValue), Double.parseDouble(coordData[1].fieldValue), Double.parseDouble(coordData[2].fieldValue));
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
			i.setString("coord", cleared.toString());
		}
		else {
			i.setString("coord", "null");
		}
		i.setInteger("clear", clearTime);
		return super.writeToNBT(i);
	}
	
	public static void setupConfig(Configuration cfg) {
		try {
			cfg.load();
			String[] cfgArrayString = cfg.getStringList("CorruptionCleanerSettings", "tileentities", new String[] {
					"Max MRU:" + ApiCore.DEVICE_MAX_MRU_GENERIC,
					"MRU Usage per Tick:20",
					"Required time to destroy corruption:200",
					"Should remove corruption blocks instead of reducing their growth:false",
					"A radius in which the device will detect corruption blocks:8"
			}, "");
			String dataString = "";
			
			for(int i = 0; i < cfgArrayString.length; ++i)
				dataString += "||" + cfgArrayString[i];
			
			DummyData[] data = DataStorage.parseData(dataString);
			
			maxRadius = Integer.parseInt(data[4].fieldValue);
			removeBlock = Boolean.parseBoolean(data[3].fieldValue);
			mruUsage = Integer.parseInt(data[1].fieldValue);
			ticksRequired = Integer.parseInt(data[2].fieldValue);
			cfgMaxMRU = Float.parseFloat(data[0].fieldValue);
			
			cfg.save();
		}
		catch(Exception e) {
			return;
		}
	}
	
	@Override
	public int[] getOutputSlots() {
		return new int[0];
	}
}
