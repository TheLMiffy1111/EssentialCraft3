package ec3.common.tile;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.MathUtils;
import ec3.api.ApiCore;
import ec3.api.OreSmeltingRecipe;
import ec3.common.item.ItemsCore;
import ec3.utils.common.ECUtils;

public class TileFurnaceMagic extends TileMRUGeneric {
	
	public int progressLevel, smeltingLevel;
	
	public static float cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;
	public static boolean generatesCorruption = false;
	public static int genCorruption = 2;
	public static int mruUsage = 25;
	public static int smeltingTime = 400;
	
	public TileFurnaceMagic() {
		super();
		maxMRU = (int)cfgMaxMRU;
		setSlotsNum(3);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound i) {
		super.readFromNBT(i);
		progressLevel = i.getInteger("progress");
		smeltingLevel = i.getInteger("smelting");
    }
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound i) {
		super.writeToNBT(i);
		i.setInteger("progress", progressLevel);
		i.setInteger("smelting", smeltingLevel);
		return i;
	}
	
	@Override
	public void update() {
		int usage = mruUsage;
		int time = smeltingTime/(getBlockMetadata()/4 + 1);
		super.update();
		ECUtils.manage(this, 0);
		
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			ItemStack ore = getStackInSlot(1);
			if(ore != null) {
				int[] oreIds = OreDictionary.getOreIDs(ore);
				
				String oreName = "Unknown";
				if(oreIds.length > 0)
					oreName = OreDictionary.getOreName(oreIds[0]);
				int metadata = -1;
				for(int i = 0; i < OreSmeltingRecipe.values().length; ++i) {
					OreSmeltingRecipe oreColor = OreSmeltingRecipe.values()[i];
					if(oreName.equalsIgnoreCase(oreColor.oreName)) {
						metadata = i;
						break;
					}
				}
				if(metadata != -1) {
					if(getStackInSlot(2) == null) {
						if(getMRU() >= usage) {
							setMRU(getMRU() - usage);
							getWorld().spawnParticle(EnumParticleTypes.FLAME, pos.getX()+0.5D+MathUtils.randomDouble(getWorld().rand)/2.2D, pos.getY(), pos.getZ()+0.5D+MathUtils.randomDouble(getWorld().rand)/2.2D, 0, -0.1D, 0);
							++progressLevel;
							
			    			if(generatesCorruption)
			    				ECUtils.increaseCorruptionAt(getWorld(), pos.getX(), pos.getY(), pos.getZ(), getWorld().rand.nextInt(genCorruption));
							
			    			if(progressLevel >= time && !getWorld().isRemote) {
								decrStackSize(1, 1);
								int suggestedStackSize = OreSmeltingRecipe.values()[metadata].dropAmount;
								setInventorySlotContents(2, new ItemStack(ItemsCore.magicalAlloy,suggestedStackSize,metadata));
								progressLevel = 0;
								syncTick = 0;
							}
						}
					}
					else if(getStackInSlot(2).getItem() == ItemsCore.magicalAlloy && getStackInSlot(2).getItemDamage() == metadata && getStackInSlot(2).stackSize+1 <= getStackInSlot(2).getMaxStackSize() && getStackInSlot(2).stackSize + 1 <= getInventoryStackLimit()) {
						if(getMRU() >= usage) {
							setMRU(getMRU() - usage);
							
							getWorld().spawnParticle(EnumParticleTypes.FLAME, pos.getX()+0.5D + MathUtils.randomDouble(getWorld().rand)/2.2D, pos.getY(), pos.getZ()+0.5D + MathUtils.randomDouble(getWorld().rand)/2.2D, 0, -0.1D, 0);
							++progressLevel;
							
			    			if(generatesCorruption)
			    				ECUtils.increaseCorruptionAt(getWorld(), pos.getX(), pos.getY(), pos.getZ(), getWorld().rand.nextInt(genCorruption));
			    			
							if(progressLevel >= time && !getWorld().isRemote) {
								decrStackSize(1, 1);
								int suggestedStackSize = OreSmeltingRecipe.values()[metadata].dropAmount;
								
								ItemStack is = getStackInSlot(2);
								is.stackSize += suggestedStackSize;
								if(is.stackSize > is.getMaxStackSize())
									is.stackSize = is.getMaxStackSize();
								setInventorySlotContents(2, is);
								progressLevel = 0;
								syncTick = 0;
							}
						}
					}
				}
				else
					progressLevel = 0;
			}
			else
				progressLevel = 0;
			
			ItemStack alloy = getStackInSlot(1);
			if(alloy != null && getStackInSlot(1).getItem() == ItemsCore.magicalAlloy) {
				OreSmeltingRecipe oreColor = OreSmeltingRecipe.values()[alloy.getItemDamage()];
				String oreName = oreColor.oreName;
				String outputName = oreColor.outputName;
				String suggestedIngotName;
				if(outputName.isEmpty())
					suggestedIngotName = "ingot"+oreName.substring(3);
				else
					suggestedIngotName = outputName;
				List<ItemStack> oreLst = OreDictionary.getOres(suggestedIngotName);
				
				if(oreLst != null && !oreLst.isEmpty()) {
					ItemStack ingotStk = oreLst.get(0).copy();
					if(getStackInSlot(2) == null) {
						if(getMRU() >= usage) {
							setMRU(getMRU()-usage);
							getWorld().spawnParticle(EnumParticleTypes.FLAME, pos.getX()+0.5D+MathUtils.randomDouble(getWorld().rand)/2.2D, pos.getY(), pos.getZ()+0.5D+MathUtils.randomDouble(getWorld().rand)/2.2D, 0, -0.1D, 0);
							++smeltingLevel;
							
			    			if(generatesCorruption)
			    				ECUtils.increaseCorruptionAt(getWorld(), pos.getX(), pos.getY(), pos.getZ(), getWorld().rand.nextInt(genCorruption));
							
			    			if(smeltingLevel >= time && !getWorld().isRemote) {
								decrStackSize(1, 1);
								int suggestedStackSize = 2;
								ingotStk.stackSize = suggestedStackSize;
								setInventorySlotContents(2, ingotStk);
								smeltingLevel = 0;
								syncTick = 0;
							}
						}
					}
					else if(getStackInSlot(2).isItemEqual(ingotStk) && getStackInSlot(2).stackSize + 2 <= getStackInSlot(2).getMaxStackSize() && getStackInSlot(2).stackSize + 2 <= getInventoryStackLimit()) {
						if(getMRU() >= usage) {
							setMRU(getMRU() - usage);
							getWorld().spawnParticle(EnumParticleTypes.FLAME, pos.getX()+0.5D + MathUtils.randomDouble(getWorld().rand)/2.2D, pos.getY(), pos.getZ()+0.5D + MathUtils.randomDouble(getWorld().rand)/2.2D, 0, -0.1D, 0);
							++smeltingLevel;
			    			if(generatesCorruption)
			    				ECUtils.increaseCorruptionAt(getWorld(), pos.getX(), pos.getY(), pos.getZ(), getWorld().rand.nextInt(genCorruption));
							if(smeltingLevel >= time && !getWorld().isRemote)
								decrStackSize(1, 1);
								int suggestedStackSize = 2;
								ItemStack is = getStackInSlot(2);
								is.stackSize += suggestedStackSize;
								if(is.stackSize > is.getMaxStackSize())
									is.stackSize = is.getMaxStackSize();
								setInventorySlotContents(2, is);
								smeltingLevel = 0;
								syncTick = 0;
							}
						}
					}
				else
					smeltingLevel = 0;
				}
			else
				smeltingLevel = 0;
		}
	}
	
	public static void setupConfig(Configuration cfg) {
		try {
			cfg.load();
			String[] cfgArrayString = cfg.getStringList("FurnaceMagicSettings", "tileentities", new String[] {
					"Max MRU:" + ApiCore.DEVICE_MAX_MRU_GENERIC,
					"MRU Usage:25",
					"Can this device actually generate corruption:false",
					"The amount of corruption generated each tick(do not set to 0!):3",
					"Ticks required to smelt:400"
			}, "");
			String dataString = "";
			
			for(int i = 0; i < cfgArrayString.length; ++i)
				dataString += "||" + cfgArrayString[i];
			
			DummyData[] data = DataStorage.parseData(dataString);
			
			mruUsage = Integer.parseInt(data[1].fieldValue);
			cfgMaxMRU = Float.parseFloat(data[0].fieldValue);
			generatesCorruption = Boolean.parseBoolean(data[2].fieldValue);
			genCorruption = Integer.parseInt(data[3].fieldValue);
			smeltingTime = Integer.parseInt(data[4].fieldValue);
			
			cfg.save();
		}
		catch(Exception e) {
			return;
		}
	}
	
	@Override
	public int[] getOutputSlots() {
		return new int[] {2};
	}
	
	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return p_94041_1_ == 0 ? isBoundGem(p_94041_2_) : p_94041_1_ == 1;
	}
}
