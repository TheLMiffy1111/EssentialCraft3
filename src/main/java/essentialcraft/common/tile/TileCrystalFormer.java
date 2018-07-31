package essentialcraft.common.tile;

import DummyCore.Utils.MiscUtils;
import essentialcraft.api.ApiCore;
import essentialcraft.common.block.BlocksCore;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

public class TileCrystalFormer extends TileMRUGeneric {

	public int progressLevel;
	public static int cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;
	public static int mruUsage = 100;
	public static int requiredTime = 1000;
	public static boolean generatesCorruption = true;
	public static int genCorruption = 2;

	public TileCrystalFormer() {
		super(cfgMaxMRU);
		setSlotsNum(8);
	}

	@Override
	public void update() {
		super.update();
		mruStorage.update(getPos(), getWorld(), getStackInSlot(0));

		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0)
			doWork();
		spawnParticles();
	}

	public void doWork()  {
		if(canWork()) {
			if(mruStorage.getMRU() >= mruUsage) {
				mruStorage.extractMRU(mruUsage, true);
				++progressLevel;
				if(generatesCorruption)
					ECUtils.randomIncreaseCorruptionAt(getWorld(), pos, getWorld().rand, genCorruption);
				if(progressLevel >= requiredTime) {
					progressLevel = 0;
					createItem();
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
		if(s[0].isEmpty()) {
			if(s[1].getItem() == Items.WATER_BUCKET && s[2].getItem() == Items.WATER_BUCKET && s[3].getItem() == Items.WATER_BUCKET && isGlassBlock(s[4]) && isGlassBlock(s[5]) && s[6].getItem() == Items.DIAMOND) {
				return true;
			}
		}
		return false;
	}

	public boolean isGlassBlock(ItemStack is) {
		if(is.isEmpty())
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
		if(world.isRemote && canWork() && mruStorage.getMRU() > 0) {
			for(int o = 0; o < 10; ++o) {
				getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+0.1D + getWorld().rand.nextDouble()/1.1D, pos.getY() + (float)o/10, pos.getZ()+0.1D + getWorld().rand.nextDouble()/1.1D, -1.0D, 1.0D, 1.0D);
			}
		}
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.crystalformer";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.DEVICE_MAX_MRU_GENERIC).setMinValue(1).getInt();
			mruUsage = cfg.get(category, "MRUUsage", 100).setMinValue(0).setMaxValue(cfgMaxMRU).getInt();
			requiredTime = cfg.get(category, "TicksRequired", 1000, "Ticks required to create a crystal").setMinValue(0).getInt();
			generatesCorruption = cfg.get(category, "GenerateCorruption", true).getBoolean();
			genCorruption = cfg.get(category, "MaxCorruptionGen", 2, "Max amount of corruption generated per tick").setMinValue(0).getInt();
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
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot == 0 ? isBoundGem(stack) : slot >= 2 && slot <= 4 ? stack.getItem() == Items.WATER_BUCKET : slot >= 5 && slot <= 6 ? isGlassBlock(stack) : slot == 7 ? stack.getItem() == Items.DIAMOND : false;
	}
}
