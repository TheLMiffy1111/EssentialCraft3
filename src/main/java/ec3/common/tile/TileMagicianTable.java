package ec3.common.tile;

import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import ec3.api.ApiCore;
import ec3.api.MagicianTableRecipe;
import ec3.api.MagicianTableRecipes;
import ec3.api.MagicianTableUpgrades;
import ec3.utils.common.ECUtils;

public class TileMagicianTable extends TileMRUGeneric {
	
	public float progressLevel, progressRequired, speedFactor = 1, mruConsume = 1;
	public int upgrade = -1;
	public MagicianTableRecipe currentRecipe;
	
	public static float cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;
	public static boolean generatesCorruption = true;
	public static int genCorruption = 1;
	public static float mruUsage = 1;
	
	public TileMagicianTable() {
		super();
		maxMRU = (int)cfgMaxMRU;
		setSlotsNum(7);
	}
	
	public boolean canGenerateMRU() {
		return false;
	}
	
	@Override
	public void update() {
		if(upgrade == -1)
			speedFactor = 1F;
		else
			speedFactor = MagicianTableUpgrades.upgradeEfficency.get(upgrade);
		if(speedFactor != 1)
			mruConsume = speedFactor * 2 * mruUsage;
		else
			mruConsume = 1 * mruUsage;
		super.update();
		ECUtils.manage(this, 0);
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			ItemStack[] craftMatrix = new ItemStack[5];
			craftMatrix[0] = getStackInSlot(1);
			craftMatrix[1] = getStackInSlot(2);
			craftMatrix[2] = getStackInSlot(3);
			craftMatrix[3] = getStackInSlot(4);
			craftMatrix[4] = getStackInSlot(5);
			MagicianTableRecipe rec = MagicianTableRecipes.getRecipeByCP(craftMatrix);
			if(currentRecipe == null && rec != null && progressRequired == rec.mruRequired && progressLevel != 0) {
				if(canFunction(rec))
				{
					progressRequired = rec.mruRequired;
					currentRecipe = rec;
				}
			}
			if(currentRecipe == null && rec != null && progressRequired == 0 && progressLevel == 0) {
				if(canFunction(rec)) {
					progressRequired = rec.mruRequired;
					currentRecipe = rec;
				}
			}
			if(currentRecipe != null && rec == null) {
				progressRequired = 0;
				progressLevel = 0;
				currentRecipe = null;
				return;
			}
			if(currentRecipe != null && rec != null && progressRequired != 0) {
				if(!canFunction(rec)) {
					progressRequired = 0;
					progressLevel = 0;
					currentRecipe = null;
					return;
				}
				float mruReq = mruConsume;
				if(getMRU() >= mruReq && progressLevel < progressRequired) {
					progressLevel += speedFactor;
					if(generatesCorruption)
						ECUtils.increaseCorruptionAt(getWorld(), pos.getX(), pos.getY(), pos.getZ(), getWorld().rand.nextInt(genCorruption));
					setMRU((int)(getMRU() - mruReq));
					if(progressLevel >= progressRequired) {
						progressRequired = 0;
						progressLevel = 0;
						craft();
						currentRecipe = null;
					}
				}
			}
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound i) {
		super.readFromNBT(i);
		progressLevel = i.getFloat("progressLevel");
		progressRequired = i.getFloat("progressRequired");
		speedFactor = i.getFloat("speedFactor");
		mruConsume = i.getFloat("mruConsume");
		upgrade = i.getInteger("upgrade");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound i) {
		super.writeToNBT(i);
		i.setFloat("progressLevel", progressLevel);
		i.setFloat("progressRequired", progressRequired);
		i.setFloat("speedFactor", speedFactor);
		i.setFloat("mruConsume", mruConsume);
		i.setInteger("upgrade", upgrade);
		return i;
	}
	
	public boolean canFunction(MagicianTableRecipe rec){
		ItemStack result = rec.result;
		if(result != null) {
			if(getStackInSlot(6) == null)
				return true;
			else {
				if(getStackInSlot(6).isItemEqual(result)) {
					if(getStackInSlot(6).stackSize + result.stackSize <= getInventoryStackLimit() && getStackInSlot(6).stackSize + result.stackSize <= getStackInSlot(6).getMaxStackSize()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void craft() {
		if(canFunction(currentRecipe)) {
			ItemStack stk = currentRecipe.result;
			if(getStackInSlot(6) == null) {
				ItemStack copied = stk.copy();
				if(copied.stackSize == 0)
					copied.stackSize = 1;
				setInventorySlotContents(6, copied);
			}
			else if(getStackInSlot(6).getItem() == stk.getItem())
				setInventorySlotContents(6, new ItemStack(stk.getItem(),stk.stackSize+getStackInSlot(6).stackSize,stk.getItemDamage()));
			for(int i = 1; i < 6; ++i) {
				decrStackSize(i, 1);
			}
		}
	}
	
	public static void setupConfig(Configuration cfg) {
		try {
			cfg.load();
			String[] cfgArrayString = cfg.getStringList("MagicianTableSettings", "tileentities", new String[]{
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
		return new int[] {6};
	}
}
