package essentialcraft.common.tile;

import essentialcraft.api.ApiCore;
import essentialcraft.api.MagicianTableRecipe;
import essentialcraft.api.MagicianTableRecipes;
import essentialcraft.api.MagicianTableUpgrades;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;

public class TileMagicianTable extends TileMRUGeneric {

	public double progressLevel, progressRequired, speedFactor = 1, mruConsume = 1;
	public int upgrade = -1;
	public MagicianTableRecipe currentRecipe;

	public static int cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;
	public static boolean generatesCorruption = true;
	public static int genCorruption = 1;
	public static double mruUsage = 1;

	public TileMagicianTable() {
		super(cfgMaxMRU);
		setSlotsNum(7);
	}

	@Override
	public void update() {
		if(upgrade == -1)
			speedFactor = 1D;
		else
			speedFactor = MagicianTableUpgrades.UPGRADE_EFFICIENCIES.getDouble(upgrade);
		if(speedFactor != 1)
			mruConsume = speedFactor * mruUsage;
		else
			mruConsume = mruUsage;
		super.update();
		mruStorage.update(getPos(), getWorld(), getStackInSlot(0));
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			ItemStack[] craftMatrix = new ItemStack[5];
			craftMatrix[0] = getStackInSlot(1);
			craftMatrix[1] = getStackInSlot(2);
			craftMatrix[2] = getStackInSlot(3);
			craftMatrix[3] = getStackInSlot(4);
			craftMatrix[4] = getStackInSlot(5);
			MagicianTableRecipe rec = MagicianTableRecipes.getRecipeByInput(craftMatrix);
			if(currentRecipe == null && rec != null && progressRequired == rec.mruRequired && progressLevel != 0) {
				if(canFunction(rec)) {
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
				double mruReq = mruConsume;
				if(mruStorage.getMRU() >= (int)mruReq && progressLevel < progressRequired) {
					progressLevel += speedFactor;
					if(generatesCorruption)
						ECUtils.randomIncreaseCorruptionAt(getWorld(), pos, getWorld().rand, (genCorruption));
					mruStorage.extractMRU((int)mruReq, true);
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
		progressLevel = i.getDouble("progressLevel");
		progressRequired = i.getDouble("progressRequired");
		speedFactor = i.getDouble("speedFactor");
		mruConsume = i.getDouble("mruConsume");
		upgrade = i.getInteger("upgrade");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound i) {
		super.writeToNBT(i);
		i.setDouble("progressLevel", progressLevel);
		i.setDouble("progressRequired", progressRequired);
		i.setDouble("speedFactor", speedFactor);
		i.setDouble("mruConsume", mruConsume);
		i.setInteger("upgrade", upgrade);
		return i;
	}

	public boolean canFunction(MagicianTableRecipe rec) {
		ItemStack result = rec.result;
		if(!result.isEmpty()) {
			if(getStackInSlot(6).isEmpty())
				return true;
			else {
				if(getStackInSlot(6).isItemEqual(result)) {
					if(getStackInSlot(6).getCount() + result.getCount() <= getInventoryStackLimit() && getStackInSlot(6).getCount() + result.getCount() <= getStackInSlot(6).getMaxStackSize()) {
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
			if(getStackInSlot(6).isEmpty()) {
				ItemStack copied = stk.copy();
				if(copied.getCount() == 0)
					copied.setCount(1);
				setInventorySlotContents(6, copied);
			}
			else if(getStackInSlot(6).getItem() == stk.getItem())
				setInventorySlotContents(6, new ItemStack(stk.getItem(),stk.getCount()+getStackInSlot(6).getCount(),stk.getItemDamage()));
			for(int i = 1; i < 6; ++i) {
				decrStackSize(i, 1);
			}
		}
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.magiciantable";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.DEVICE_MAX_MRU_GENERIC).setMinValue(1).getInt();
			mruUsage = cfg.get(category, "MRUUsageModifier", 1D).setMinValue(0D).getDouble();
			generatesCorruption = cfg.get(category, "GenerateCorruption", true).getBoolean();
			genCorruption = cfg.get(category, "MaxCorruptionGen", 1, "Max amount of corruption generated per tick").setMinValue(0).getInt();
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
