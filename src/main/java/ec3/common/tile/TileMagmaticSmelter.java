package ec3.common.tile;

import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.oredict.OreDictionary;
import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.MathUtils;
import ec3.api.ApiCore;
import ec3.api.OreSmeltingRecipe;
import ec3.common.item.ItemsCore;
import ec3.utils.common.ECUtils;

public class TileMagmaticSmelter extends TileMRUGeneric implements IFluidTank {
	
	public int progressLevel, smeltingLevel;
	public FluidTank lavaTank = new FluidTank(8000);
	public static float cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;
	public static boolean generatesCorruption = false;
	public static int genCorruption = 2;
	public static int mruUsage = 50;
	public static int smeltMRUUsage = 30;
	public static int oreSmeltingTime = 400;
	public static int alloySmeltingTime = 40;
	
	public TileMagmaticSmelter() {
		super();
		maxMRU = (int)cfgMaxMRU;
		setSlotsNum(8);
	}
	
	@Override
	public void update() {
		super.update();
		ECUtils.manage(this, 0);
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			if(!getWorld().isRemote) {
				if(FluidUtil.getFluidContained(getStackInSlot(1)) != null && getStackInSlot(2) == null) {
					if(lavaTank.getFluid() == null) {
						lavaTank.fill(FluidUtil.getFluidContained(getStackInSlot(1)), true);
						setInventorySlotContents(2,consumeItem(getStackInSlot(1)));
						decrStackSize(1, 1);
					}
					else if(lavaTank.getFluidAmount() != 8000 && lavaTank.getFluid().isFluidEqual(FluidUtil.getFluidContained(getStackInSlot(1)))) {
						lavaTank.fill(FluidUtil.getFluidContained(getStackInSlot(1)), true);
						setInventorySlotContents(2,consumeItem(getStackInSlot(1)));
						decrStackSize(1, 1);
					}
				}
			}
			
			ItemStack ore = getStackInSlot(3);
			if(ore != null && !getWorld().isRemote) {
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
					if(getStackInSlot(4) == null) {
						if(getMRU() >= mruUsage && lavaTank != null && lavaTank.getFluid() != null && lavaTank.getFluid().getFluid() == FluidRegistry.LAVA && lavaTank.getFluidAmount() > 0) {
							setMRU(getMRU() - mruUsage);
							if(getWorld().rand.nextFloat() <= 0.1F)
								drain(1, true);
							getWorld().spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5D + MathUtils.randomDouble(getWorld().rand) / 2.2D, pos.getY(), pos.getZ() + 0.5D + MathUtils.randomDouble(getWorld().rand) / 2.2D, 0, -0.1D, 0);
							++progressLevel;
			    			if(generatesCorruption)
			    				ECUtils.increaseCorruptionAt(getWorld(), pos.getX(), pos.getY(), pos.getZ(), getWorld().rand.nextInt(genCorruption));
							if(progressLevel >= oreSmeltingTime) {
								decrStackSize(3, 1);
								int suggestedStackSize = OreSmeltingRecipe.values()[metadata].dropAmount;
								if(getWorld().rand.nextFloat() <= 1)
									suggestedStackSize = OreSmeltingRecipe.values()[metadata].dropAmount * 2;
								setInventorySlotContents(4, new ItemStack(ItemsCore.magicalAlloy, suggestedStackSize, metadata));
								progressLevel = 0;
								if(getStackInSlot(7) == null)
									setInventorySlotContents(7, new ItemStack(ItemsCore.magicalSlag,1,0));
								else if(getStackInSlot(7).getItem() == ItemsCore.magicalSlag && getStackInSlot(7).stackSize < 64) {
									ItemStack slagIS = getStackInSlot(7);
									slagIS.stackSize += 1;
									setInventorySlotContents(7, slagIS);
								}
							}
						}
					}
					else if(getStackInSlot(4).getItem() == ItemsCore.magicalAlloy && getStackInSlot(4).getItemDamage() == metadata && getStackInSlot(4).stackSize+2 <= getStackInSlot(4).getMaxStackSize() && getStackInSlot(4).stackSize + 4 <= getInventoryStackLimit())
					{
						if(getMRU() >= mruUsage && lavaTank != null && lavaTank.getFluid() != null && lavaTank.getFluid().getFluid() == FluidRegistry.LAVA && lavaTank.getFluidAmount() > 0)
						{
							setMRU(getMRU() - mruUsage);
							if(getWorld().rand.nextFloat() <= 0.1F)
								drain(1, true);
							getWorld().spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5D + MathUtils.randomDouble(getWorld().rand) / 2.2D, pos.getY(), pos.getZ() + 0.5D + MathUtils.randomDouble(getWorld().rand) / 2.2D, 0, -0.1D, 0);
							++progressLevel;
			    			if(generatesCorruption)
			    				ECUtils.increaseCorruptionAt(getWorld(), pos.getX(), pos.getY(), pos.getZ(), getWorld().rand.nextInt(genCorruption));
							if(progressLevel >= oreSmeltingTime) {
								decrStackSize(3, 1);
								int suggestedStackSize = OreSmeltingRecipe.values()[metadata].dropAmount;
								if(getWorld().rand.nextFloat() <= 1)
									suggestedStackSize = OreSmeltingRecipe.values()[metadata].dropAmount * 2;
								ItemStack is = getStackInSlot(4);
								is.stackSize += suggestedStackSize;
								if(is.stackSize > is.getMaxStackSize())
									is.stackSize = is.getMaxStackSize();
								setInventorySlotContents(4, is);
								progressLevel = 0;
								if(getStackInSlot(7) == null)
									setInventorySlotContents(7, new ItemStack(ItemsCore.magicalSlag, 1, 0));
								else if(getStackInSlot(7).getItem() == ItemsCore.magicalSlag && getStackInSlot(7).stackSize < 64) {
									ItemStack slagIS = getStackInSlot(7);
									slagIS.stackSize += 1;
									setInventorySlotContents(7, slagIS);
								}
							}
						}
					}
				}
				else
					progressLevel = 0;
			}
			else
				progressLevel = 0;
			
			ItemStack alloy = getStackInSlot(5);
			if(alloy != null && getStackInSlot(5).getItem() == ItemsCore.magicalAlloy) {
				OreSmeltingRecipe oreColor = OreSmeltingRecipe.values()[alloy.getItemDamage()];
				String oreName = oreColor.oreName;
				String outputName = oreColor.outputName;
				String suggestedIngotName;
				if(outputName.isEmpty())
					suggestedIngotName = "ingot" + oreName.substring(3);
				else
					suggestedIngotName = outputName;
				List<ItemStack> oreLst = OreDictionary.getOres(suggestedIngotName);
				
				if(oreLst != null && !oreLst.isEmpty() && !getWorld().isRemote) {
					ItemStack ingotStk = oreLst.get(0).copy();
					if(getStackInSlot(6) == null) {
						if(getMRU() >= smeltMRUUsage) {
							setMRU(getMRU() - smeltMRUUsage);
							getWorld().spawnParticle(EnumParticleTypes.FLAME, pos.getX()+0.5D+MathUtils.randomDouble(getWorld().rand)/2.2D, pos.getY(), pos.getZ()+0.5D+MathUtils.randomDouble(getWorld().rand)/2.2D, 0, -0.1D, 0);
							++smeltingLevel;
			    			if(generatesCorruption)
			    				ECUtils.increaseCorruptionAt(getWorld(), pos.getX(), pos.getY(), pos.getZ(), getWorld().rand.nextInt(genCorruption));
							if(smeltingLevel >= alloySmeltingTime) {
								decrStackSize(5, 1);
								int suggestedStackSize = 2;
								ingotStk.stackSize = suggestedStackSize;
								setInventorySlotContents(6, ingotStk);
								smeltingLevel = 0;
								if(getStackInSlot(7) == null)
									setInventorySlotContents(7, new ItemStack(ItemsCore.magicalSlag,1,0));
								else if(getStackInSlot(7).getItem() == ItemsCore.magicalSlag && getStackInSlot(7).stackSize < 64) {
									ItemStack slagIS = getStackInSlot(7);
									slagIS.stackSize += 1;
									setInventorySlotContents(7, slagIS);
								}
							}
						}
					}
					else if(getStackInSlot(6).isItemEqual(ingotStk) && getStackInSlot(6).stackSize+2 <= getStackInSlot(6).getMaxStackSize() && getStackInSlot(6).stackSize + 2 <= getInventoryStackLimit()) {
						if(getMRU() >= smeltMRUUsage) {
							setMRU(getMRU() - smeltMRUUsage);
							getWorld().spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5D + MathUtils.randomDouble(getWorld().rand) / 2.2D, pos.getY(), pos.getZ() + 0.5D + MathUtils.randomDouble(getWorld().rand) / 2.2D, 0, -0.1D, 0);
							++smeltingLevel;
			    			if(generatesCorruption)
			    				ECUtils.increaseCorruptionAt(getWorld(), pos.getX(), pos.getY(), pos.getZ(), getWorld().rand.nextInt(genCorruption));
							if(smeltingLevel >= alloySmeltingTime) {
								decrStackSize(5, 1);
								int suggestedStackSize = 2;
								ItemStack is = getStackInSlot(6);
								is.stackSize += suggestedStackSize;
								if(is.stackSize > is.getMaxStackSize())
									is.stackSize = is.getMaxStackSize();
								setInventorySlotContents(6, is);
								smeltingLevel = 0;
								if(getStackInSlot(7) == null)
									setInventorySlotContents(7, new ItemStack(ItemsCore.magicalSlag,1,0));
								else if(getStackInSlot(7).getItem() == ItemsCore.magicalSlag && getStackInSlot(7).stackSize < 64) {
									ItemStack slagIS = getStackInSlot(7);
									slagIS.stackSize += 1;
									setInventorySlotContents(7, slagIS);
								}
							}
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
	
	public static ItemStack consumeItem(ItemStack stack) {
		return stack.getItem().hasContainerItem(stack) ? stack.getItem().getContainerItem(stack) : null;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound i) {
		super.readFromNBT(i);
		lavaTank.readFromNBT(i);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound i) {
		super.writeToNBT(i);
		lavaTank.writeToNBT(i);
		return i;
	}
	
	@Override
	public FluidStack getFluid() {
		return lavaTank.getFluid();
	}
	
	@Override
	public int getFluidAmount() {
		return lavaTank.getFluidAmount();
	}
	
	@Override
	public int getCapacity() {
		return lavaTank.getCapacity();
	}
	
	@Override
	public FluidTankInfo getInfo() {
		return lavaTank.getInfo();
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		return lavaTank.fill(resource, doFill);
	}
	
	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		return lavaTank.drain(maxDrain, doDrain);
	}
	
	public static void setupConfig(Configuration cfg) {
		try {
			cfg.load();
			String[] cfgArrayString = cfg.getStringList("MagmaticSmelterySettings", "tileentities", new String[] {
					"Max MRU:"+ApiCore.DEVICE_MAX_MRU_GENERIC,
					"MRU Usage when smelting Ores:50",
					"Can this device actually generate corruption:false",
					"The amount of corruption generated each tick(do not set to 0!):3",
					"MRU Usage when smelting Alloys:30",
					"Ticks required to smelt an ore into Alloy:400",
					"Ticks required to smelt an alloy:40",
			}, "");
			String dataString = "";
			
			for(int i = 0; i < cfgArrayString.length; ++i)
				dataString += "||" + cfgArrayString[i];
			
			DummyData[] data = DataStorage.parseData(dataString);
			
			mruUsage = Integer.parseInt(data[1].fieldValue);
			cfgMaxMRU = Float.parseFloat(data[0].fieldValue);
			generatesCorruption = Boolean.parseBoolean(data[2].fieldValue);
			genCorruption = Integer.parseInt(data[3].fieldValue);
			smeltMRUUsage = Integer.parseInt(data[4].fieldValue);
			oreSmeltingTime = Integer.parseInt(data[5].fieldValue);
			alloySmeltingTime = Integer.parseInt(data[6].fieldValue);
			
			cfg.save();
		}
		catch(Exception e) {
			return;
		}
	}
	
	@Override
	public int[] getOutputSlots() {
		return new int[] {2, 4, 6, 7};
	}
	
	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return p_94041_1_ == 0 ? isBoundGem(p_94041_2_) :
			p_94041_1_ == 1 ? FluidUtil.getFluidContained(p_94041_2_) != null && FluidUtil.getFluidContained(p_94041_2_).getFluid() == FluidRegistry.LAVA :
			p_94041_1_ == 5 ? p_94041_2_.getItem() == ItemsCore.magicalAlloy :
			p_94041_1_ == 3 ? p_94041_2_.getItem() != ItemsCore.magicalAlloy :
			false;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? (T)lavaTank : super.getCapability(capability, facing);
	}
}
