package ec3.common.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.config.Configuration;
import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import ec3.api.ApiCore;
import ec3.api.RadiatingChamberRecipe;
import ec3.api.RadiatingChamberRecipes;
import ec3.utils.common.ECUtils;

public class TileRadiatingChamber extends TileMRUGeneric {
	
	public int progressLevel;
	public RadiatingChamberRecipe currentRecipe;
	public static float cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;
	public static boolean generatesCorruption = true;
	public static int genCorruption = 1;
	public static float mruUsage = 1F;
	
	public TileRadiatingChamber() {
		super();
		maxMRU = (int)cfgMaxMRU;
		setSlotsNum(4);
	}
	
	@Override
	public void update() {
		super.update();
		ECUtils.manage(this, 0);
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			ItemStack[] craftMatrix = new ItemStack[2];
			craftMatrix[0] = getStackInSlot(1);
			craftMatrix[1] = getStackInSlot(2);
			RadiatingChamberRecipe rec = RadiatingChamberRecipes.getRecipeByCPAndBalance(craftMatrix, getBalance());
			if(currentRecipe == null && rec != null && progressLevel != 0) {
				if(canFunction(rec))
					currentRecipe = rec;
			}
			if(currentRecipe == null && rec != null && progressLevel == 0) {
				if(canFunction(rec))
					currentRecipe = rec;
			}
			if(currentRecipe != null && rec == null) {
				progressLevel = 0;
				currentRecipe = null;
				return;
			}
			if(currentRecipe != null && rec != null) {
				if(!canFunction(rec)) {
					progressLevel = 0;
					currentRecipe = null;
					return;
				}
				int mruReq = (int)(mruUsage * currentRecipe.costModifier);
				if(getMRU() >= mruReq && progressLevel < currentRecipe.mruRequired) {
					progressLevel += 1;
					if(generatesCorruption)
						ECUtils.increaseCorruptionAt(getWorld(), pos.getX(), pos.getY(), pos.getZ(), getWorld().rand.nextInt(genCorruption));
					setMRU(getMRU() - mruReq);
					if(progressLevel >= currentRecipe.mruRequired) {
						progressLevel = 0;
						craft();
						currentRecipe = null;
					}
				}
			}
		}
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		AxisAlignedBB bb = INFINITE_EXTENT_AABB;
		return bb;
	}
	
	public boolean canFunction(RadiatingChamberRecipe rec) {
		ItemStack result = rec.result;
		if(result != null) {
			if(getStackInSlot(3) == null)
				return true;
			else if(getStackInSlot(3).isItemEqual(result)) {
				if(getStackInSlot(3).stackSize+result.stackSize <= getInventoryStackLimit() && getStackInSlot(3).stackSize+result.stackSize <= getStackInSlot(3).getMaxStackSize())
					return true;
			}
		}
		return false;
	}
	
	public void craft()
	{
		if(canFunction(currentRecipe)) {
			ItemStack stk = currentRecipe.result.copy();
			
			if(getStackInSlot(3) == null)
				setInventorySlotContents(3, stk.copy());
			else if (getStackInSlot(3).getItem() == stk.getItem())
				setInventorySlotContents(3, new ItemStack(stk.getItem(),stk.stackSize+getStackInSlot(3).stackSize,stk.getItemDamage()));
			for(int i = 1; i < 3; ++i) {
				decrStackSize(i, 1);
			}
		}
	}
	
	public static void setupConfig(Configuration cfg) {
		try {
			cfg.load();
			String[] cfgArrayString = cfg.getStringList("RadiatingChamberSettings", "tileentities", new String[] {
					"Max MRU:" + ApiCore.DEVICE_MAX_MRU_GENERIC,
					"MRU Usage Modifier:1.0",
					"Can this device actually generate corruption:true",
					"The amount of corruption generated each tick(do not set to 0!):1"
			}, "");
			String dataString = "";
			
			for(int i = 0; i < cfgArrayString.length; ++i)
				dataString += "||" + cfgArrayString[i];
			
			DummyData[] data = DataStorage.parseData(dataString);
			
			mruUsage = Float.parseFloat(data[1].fieldValue);
			cfgMaxMRU = Float.parseFloat(data[0].fieldValue);
			generatesCorruption = Boolean.parseBoolean(data[2].fieldValue);
			genCorruption = Integer.parseInt(data[3].fieldValue);
			
			cfg.save();
		}
		catch(Exception e) {
			return;
		}
	}
	
	@Override
	public int[] getOutputSlots() {
		return new int[] {3};
	}
}
