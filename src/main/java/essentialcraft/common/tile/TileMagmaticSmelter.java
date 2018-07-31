package essentialcraft.common.tile;

import java.util.List;

import DummyCore.Utils.MathUtils;
import essentialcraft.api.ApiCore;
import essentialcraft.api.OreSmeltingRecipe;
import essentialcraft.common.item.ItemsCore;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.oredict.OreDictionary;

public class TileMagmaticSmelter extends TileMRUGeneric {

	public int progressLevel, smeltingLevel;
	public FluidTank lavaTank = new FluidTank(8000);
	public static int cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;
	public static boolean generatesCorruption = false;
	public static int genCorruption = 2;
	public static int mruUsage = 50;
	public static int smeltMRUUsage = 30;
	public static int oreSmeltingTime = 400;
	public static int alloySmeltingTime = 40;

	public TileMagmaticSmelter() {
		super(cfgMaxMRU);
		setSlotsNum(8);
	}

	@Override
	public void update() {
		super.update();
		mruStorage.update(getPos(), getWorld(), getStackInSlot(0));
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			if(FluidUtil.getFluidContained(getStackInSlot(1)) != null && getStackInSlot(2).isEmpty()) {
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

			ItemStack ore = getStackInSlot(3);
			if(!ore.isEmpty() && !getWorld().isRemote) {
				int[] oreIds = OreDictionary.getOreIDs(ore);

				String oreName = "Unknown";
				if(oreIds.length > 0)
					oreName = OreDictionary.getOreName(oreIds[0]);
				int metadata = -1;
				for(int i = 0; i < OreSmeltingRecipe.RECIPES.size(); ++i) {
					OreSmeltingRecipe oreColor = OreSmeltingRecipe.RECIPES.get(i);
					if(oreName.equalsIgnoreCase(oreColor.oreName)) {
						metadata = i;
						break;
					}
				}
				if(metadata != -1) {
					if(getStackInSlot(4).isEmpty()) {
						if(mruStorage.getMRU() >= mruUsage && lavaTank != null && lavaTank.getFluid() != null && lavaTank.getFluid().getFluid() == FluidRegistry.LAVA && lavaTank.getFluidAmount() > 0) {
							mruStorage.extractMRU(mruUsage, true);
							if(!getWorld().isRemote && getWorld().rand.nextFloat() <= 0.1F)
								lavaTank.drain(1, true);
							if(getWorld().isRemote)
								getWorld().spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5D + MathUtils.randomDouble(getWorld().rand) / 2.2D, pos.getY(), pos.getZ() + 0.5D + MathUtils.randomDouble(getWorld().rand) / 2.2D, 0, -0.1D, 0);
							++progressLevel;
							if(generatesCorruption)
								ECUtils.randomIncreaseCorruptionAt(getWorld(), pos, getWorld().rand, (genCorruption));
							if(progressLevel >= oreSmeltingTime) {
								progressLevel = 0;
								decrStackSize(3, 1);
								int suggestedStackSize = OreSmeltingRecipe.RECIPES.get(metadata).dropAmount * 2;
								setInventorySlotContents(4, OreSmeltingRecipe.getAlloyStack(OreSmeltingRecipe.RECIPES.get(metadata), suggestedStackSize));
								if(getStackInSlot(7).isEmpty())
									setInventorySlotContents(7, new ItemStack(ItemsCore.magicalSlag,1,0));
								else if(getStackInSlot(7).getItem() == ItemsCore.magicalSlag && getStackInSlot(7).getCount() < 64) {
									ItemStack slagIS = getStackInSlot(7);
									slagIS.grow(1);
									setInventorySlotContents(7, slagIS);
								}
							}
						}
					}
					else if(getStackInSlot(4).getItem() == ItemsCore.magicalAlloy && OreSmeltingRecipe.getIndex(getStackInSlot(4)) == metadata && getStackInSlot(4).getCount()+2 <= getStackInSlot(4).getMaxStackSize() && getStackInSlot(4).getCount() + 4 <= getInventoryStackLimit()) {
						if(mruStorage.getMRU() >= mruUsage && lavaTank != null && lavaTank.getFluid() != null && lavaTank.getFluid().getFluid() == FluidRegistry.LAVA && lavaTank.getFluidAmount() > 0) {
							mruStorage.extractMRU(mruUsage, true);
							if(!getWorld().isRemote && getWorld().rand.nextFloat() <= 0.1F)
								lavaTank.drain(1, true);
							if(getWorld().isRemote)
								getWorld().spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5D + MathUtils.randomDouble(getWorld().rand) / 2.2D, pos.getY(), pos.getZ() + 0.5D + MathUtils.randomDouble(getWorld().rand) / 2.2D, 0, -0.1D, 0);
							++progressLevel;
							if(generatesCorruption)
								ECUtils.randomIncreaseCorruptionAt(getWorld(), pos, getWorld().rand, (genCorruption));
							if(progressLevel >= oreSmeltingTime) {
								progressLevel = 0;
								decrStackSize(3, 1);
								int suggestedStackSize = OreSmeltingRecipe.RECIPES.get(metadata).dropAmount * 2;
								ItemStack is = getStackInSlot(4);
								is.grow(suggestedStackSize);
								if(is.getCount() > is.getMaxStackSize())
									is.setCount(is.getMaxStackSize());
								setInventorySlotContents(4, is);
								if(getStackInSlot(7).isEmpty())
									setInventorySlotContents(7, new ItemStack(ItemsCore.magicalSlag, 1, 0));
								else if(getStackInSlot(7).getItem() == ItemsCore.magicalSlag && getStackInSlot(7).getCount() < 64) {
									ItemStack slagIS = getStackInSlot(7);
									slagIS.grow(1);
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
			if(!alloy.isEmpty() && getStackInSlot(5).getItem() == ItemsCore.magicalAlloy) {
				OreSmeltingRecipe oreColor = OreSmeltingRecipe.RECIPES.get(OreSmeltingRecipe.getIndex(alloy));
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
					if(getStackInSlot(6).isEmpty()) {
						if(mruStorage.getMRU() >= smeltMRUUsage) {
							mruStorage.extractMRU(smeltMRUUsage, true);
							if(getWorld().isRemote)
								getWorld().spawnParticle(EnumParticleTypes.FLAME, pos.getX()+0.5D+MathUtils.randomDouble(getWorld().rand)/2.2D, pos.getY(), pos.getZ()+0.5D+MathUtils.randomDouble(getWorld().rand)/2.2D, 0, -0.1D, 0);
							++smeltingLevel;
							if(generatesCorruption)
								ECUtils.randomIncreaseCorruptionAt(getWorld(), pos, getWorld().rand, (genCorruption));
							if(smeltingLevel >= alloySmeltingTime) {
								decrStackSize(5, 1);
								int suggestedStackSize = 2;
								ingotStk.setCount(suggestedStackSize);
								setInventorySlotContents(6, ingotStk);
								smeltingLevel = 0;
								if(getStackInSlot(7).isEmpty())
									setInventorySlotContents(7, new ItemStack(ItemsCore.magicalSlag,1,0));
								else if(getStackInSlot(7).getItem() == ItemsCore.magicalSlag && getStackInSlot(7).getCount() < 64) {
									ItemStack slagIS = getStackInSlot(7);
									slagIS.grow(1);
									setInventorySlotContents(7, slagIS);
								}
							}
						}
					}
					else if(getStackInSlot(6).isItemEqual(ingotStk) && getStackInSlot(6).getCount()+2 <= getStackInSlot(6).getMaxStackSize() && getStackInSlot(6).getCount() + 2 <= getInventoryStackLimit()) {
						if(mruStorage.getMRU() >= smeltMRUUsage) {
							mruStorage.extractMRU(smeltMRUUsage, true);
							if(getWorld().isRemote)
								getWorld().spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5D + MathUtils.randomDouble(getWorld().rand) / 2.2D, pos.getY(), pos.getZ() + 0.5D + MathUtils.randomDouble(getWorld().rand) / 2.2D, 0, -0.1D, 0);
							++smeltingLevel;
							if(generatesCorruption)
								ECUtils.randomIncreaseCorruptionAt(getWorld(), pos, getWorld().rand, (genCorruption));
							if(smeltingLevel >= alloySmeltingTime) {
								decrStackSize(5, 1);
								int suggestedStackSize = 2;
								ItemStack is = getStackInSlot(6);
								is.grow(suggestedStackSize);
								if(is.getCount() > is.getMaxStackSize())
									is.setCount(is.getMaxStackSize());
								setInventorySlotContents(6, is);
								smeltingLevel = 0;
								if(getStackInSlot(7).isEmpty())
									setInventorySlotContents(7, new ItemStack(ItemsCore.magicalSlag,1,0));
								else if(getStackInSlot(7).getItem() == ItemsCore.magicalSlag && getStackInSlot(7).getCount() < 64) {
									ItemStack slagIS = getStackInSlot(7);
									slagIS.grow(1);
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
		return stack.getItem().hasContainerItem(stack) ? stack.getItem().getContainerItem(stack) : ItemStack.EMPTY;
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

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.magmaticsmeltery";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.DEVICE_MAX_MRU_GENERIC).setMinValue(1).getInt();
			mruUsage = cfg.get(category, "MRUUsageOres", 50).setMinValue(0).setMaxValue(cfgMaxMRU).getInt();
			generatesCorruption = cfg.get(category, "GenerateCorruption", false).getBoolean();
			genCorruption = cfg.get(category, "MaxCorruptionGen", 3, "Max amount of corruption generated per tick").setMinValue(0).getInt();
			smeltMRUUsage = cfg.get(category, "MRUUsageAlloys", 30).setMinValue(0).setMaxValue(cfgMaxMRU).getInt();
			oreSmeltingTime = cfg.get(category, "TicksRequiredOres", 400).setMinValue(0).getInt();
			alloySmeltingTime = cfg.get(category, "TicksRequiredAlloys", 40).setMinValue(0).getInt();
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
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot == 0 ? isBoundGem(stack) :
			slot == 1 ? FluidUtil.getFluidContained(stack) != null && FluidUtil.getFluidContained(stack).getFluid() == FluidRegistry.LAVA :
				slot == 5 ? stack.getItem() == ItemsCore.magicalAlloy :
					slot == 3 ? stack.getItem() != ItemsCore.magicalAlloy :
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
