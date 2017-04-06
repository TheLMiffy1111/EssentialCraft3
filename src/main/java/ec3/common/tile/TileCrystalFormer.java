package ec3.common.tile;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.MiscUtils;
import ec3.api.ApiCore;
import ec3.common.block.BlocksCore;
import ec3.utils.common.ECUtils;

public class TileCrystalFormer extends TileMRUGeneric {
	
	public int progressLevel;
	public static float cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;
	public static int mruUsage = 100;
	public static int requiredTime = 1000;
	public static boolean generatesCorruption = true;
	public static int genCorruption = 2;
	
	public TileCrystalFormer() {
		super();
		maxMRU = (int)cfgMaxMRU;
		setSlotsNum(8);
	}
	
	@Override
	public void update() {
		super.update();
		ECUtils.manage(this, 0);
		
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0)
			doWork();
    	spawnParticles();
	}
	
	public void doWork()  {
		if(canWork()) {
			if(getMRU() > mruUsage) {
				if(!getWorld().isRemote) {
					if(setMRU(getMRU() - mruUsage))
						++progressLevel;
					if(generatesCorruption)
						ECUtils.increaseCorruptionAt(getWorld(), pos.getX(), pos.getY(), pos.getZ(), getWorld().rand.nextInt(genCorruption));
					if(progressLevel >= requiredTime) {
						progressLevel = 0;
						createItem();
					}	
				}
			}
		}
	}
	
	public void createItem()  {
		ItemStack b = new ItemStack(Items.BUCKET, 1, 0);
		setInventorySlotContents(2, b);
		setInventorySlotContents(3, b);
		setInventorySlotContents(4, b);
		decrStackSize(5, 1);
		decrStackSize(6, 1);
		decrStackSize(7, 1);
		ItemStack crystal = new ItemStack(BlocksCore.elementalCrystal,1,0);
		MiscUtils.getStackTag(crystal).setFloat("size", 1);
		MiscUtils.getStackTag(crystal).setFloat("fire", 0);
		MiscUtils.getStackTag(crystal).setFloat("water", 0);
		MiscUtils.getStackTag(crystal).setFloat("earth", 0);
		MiscUtils.getStackTag(crystal).setFloat("air", 0);
		setInventorySlotContents(1, crystal);
	}
	
	public boolean canWork() {
		ItemStack[] s = new ItemStack[7];
		for(int i = 1; i < 8; ++i) {
			s[i-1] = getStackInSlot(i);
		}
		if(s[0] == null) {
			if(s[1] != null && s[2] != null && s[3] != null && s[4] != null && s[5] != null && s[6] != null) {
				if(s[1].getItem() == Items.WATER_BUCKET && s[2].getItem() == Items.WATER_BUCKET && s[3].getItem() == Items.WATER_BUCKET && isGlassBlock(s[4]) && isGlassBlock(s[5]) && s[6].getItem() == Items.DIAMOND) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isGlassBlock(ItemStack is) {
		if(is == null)
			return false;
		
		if(is.getItem() == Item.getItemFromBlock(Blocks.GLASS) || is.getItem() == Item.getItemFromBlock(Blocks.STAINED_GLASS))
			return true;
		
		if(OreDictionary.getOreIDs(is) != null && OreDictionary.getOreIDs(is).length > 0) {
			for(int i = 0; i < OreDictionary.getOreIDs(is).length; ++i) {
				String name = OreDictionary.getOreName(OreDictionary.getOreIDs(is)[i]);
				if(name.equals("blockGlass"))
					return true;
			}
		}
		
		return false;
	}
	
	public void spawnParticles() {
		if(canWork() && getMRU() > 0) {
			for(int o = 0; o < 10; ++o) {
				getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+0.1D + getWorld().rand.nextDouble()/1.1D, pos.getY() + ((float)o/10), pos.getZ()+0.1D + getWorld().rand.nextDouble()/1.1D, -1.0D, 1.0D, 1.0D);
			}
		}
	}
	
	public static void setupConfig(Configuration cfg) {
		try {
			cfg.load();
			String[] cfgArrayString = cfg.getStringList("CrystalFormerSettings", "tileentities", new String[] {
					"Max MRU:" + ApiCore.DEVICE_MAX_MRU_GENERIC,
					"MRU Usage:100",
					"Ticks required to create a crystal:1000",
					"Can this device actually generate corruption:true",
					"The amount of corruption generated each tick(do not set to 0!):2"
			}, "");
			String dataString = "";
			
			for(int i = 0; i < cfgArrayString.length; ++i)
				dataString += "||" + cfgArrayString[i];
	    	
			DummyData[] data = DataStorage.parseData(dataString);
			
			mruUsage = Integer.parseInt(data[1].fieldValue);
			requiredTime = Integer.parseInt(data[2].fieldValue);
			cfgMaxMRU = Float.parseFloat(data[0].fieldValue);
			generatesCorruption = Boolean.parseBoolean(data[3].fieldValue);
			genCorruption = Integer.parseInt(data[4].fieldValue);
			
			cfg.save();
		}
		catch(Exception e) {
			return;
		}
	}
	
	@Override
	public int[] getOutputSlots() {
		return new int[] {1, 2, 3, 4};
	}
	
	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return p_94041_1_ == 0 ? isBoundGem(p_94041_2_) : p_94041_1_ >= 2 && p_94041_1_ <= 4 ? p_94041_2_.getItem() == Items.WATER_BUCKET : p_94041_1_ >= 5 && p_94041_1_ <= 6 ? isGlassBlock(p_94041_2_) : p_94041_1_ == 7 ? p_94041_2_.getItem() == Items.DIAMOND : false;
	}
}
