package essentialcraft.common.tile;

import DummyCore.Utils.MathUtils;
import essentialcraft.api.ApiCore;
import essentialcraft.api.IHotBlock;
import essentialcraft.common.item.ItemsCore;
import essentialcraft.common.mod.EssentialCraftCore;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.config.Configuration;

public class TileHeatGenerator extends TileMRUGeneric {

	public static int cfgMaxMRU = ApiCore.GENERATOR_MAX_MRU_GENERIC;
	public static float cfgBalance = -1F;
	public static int mruGenerated = 20;

	public int currentBurnTime, currentMaxBurnTime;
	private boolean firstTick = true;

	public TileHeatGenerator() {
		super(cfgMaxMRU);
		slot0IsBoundGem = false;
		setSlotsNum(2);
	}

	@Override
	public void update() {
		if(firstTick) {
			if(cfgBalance < 0) {
				mruStorage.setBalance(getWorld().rand.nextFloat()*2);
			}
			else {
				mruStorage.setBalance(cfgBalance);
			}
		}
		super.update();
		firstTick = false;
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			if(currentBurnTime > 0) {
				--currentBurnTime;
				double mruGen = mruGenerated;
				double mruFactor = 1D;
				Block[] b = new Block[4];
				b[0] = getWorld().getBlockState(pos.east(2)).getBlock();
				b[1] = getWorld().getBlockState(pos.west(2)).getBlock();
				b[2] = getWorld().getBlockState(pos.south(2)).getBlock();
				b[3] = getWorld().getBlockState(pos.north(2)).getBlock();
				int[] ox = {2, -2, 0, 0};
				int[] oz = {0, 0, 2, -2};
				for(int i = 0; i < 4; ++i) {
					if(b[i] == Blocks.AIR)
						mruFactor *= 0;
					else if(b[i] == Blocks.NETHERRACK)
						mruFactor *= 0.75D;
					else if(b[i] == Blocks.LAVA)
						mruFactor *= 0.95D;
					else if(b[i] == Blocks.FIRE)
						mruFactor *= 0.7D;
					else if(b[i] instanceof IHotBlock)
						mruFactor *= ((IHotBlock)b[i]).getHeatModifier(getWorld(), pos.add(ox[i], 0, oz[i]));
					else
						mruFactor *= 0.5D;
				}

				mruGen *= mruFactor;
				if(mruGen >= 1) {
					mruStorage.addMRU((int)mruGen, true);
				}
			}

			if(!getStackInSlot(0).isEmpty()) {
				if(currentBurnTime == 0 && mruStorage.getMRU() < mruStorage.getMaxMRU()) {
					currentMaxBurnTime = currentBurnTime = TileEntityFurnace.getItemBurnTime(getStackInSlot(0));

					if(currentBurnTime > 0) {
						if(!getStackInSlot(0).isEmpty()) {
							if(getStackInSlot(1).isEmpty() || getStackInSlot(1).getCount() < getInventoryStackLimit()) {
								if(getStackInSlot(1).getItem() == ItemsCore.magicalSlag) {
									ItemStack stk = getStackInSlot(1);
									stk.grow(1);
									setInventorySlotContents(1, stk);
								}
								if(getStackInSlot(1).isEmpty()) {
									ItemStack stk = new ItemStack(ItemsCore.magicalSlag,1,0);
									setInventorySlotContents(1, stk);
								}
							}
							if(getStackInSlot(0).getCount() == 0) {
								setInventorySlotContents(0, getStackInSlot(0).getItem().getContainerItem(getStackInSlot(0)));
							}
							decrStackSize(0, 1);
						}
					}
				}
			}
		}
		if(getWorld().isRemote) {
			for(int i = 2; i < 6; ++i) {
				EnumFacing rotation = EnumFacing.getFront(i);
				float rotXAdv = rotation.getFrontOffsetX()-0.5F;
				float rotZAdv = rotation.getFrontOffsetZ()-0.5F;
				EssentialCraftCore.proxy.FlameFX(pos.getX()+0.725F + rotXAdv/2.2F, pos.getY()+0.4F, pos.getZ()+0.725F + rotZAdv/2.2F, 0, 0F, 0, 0.8D, 0.5D, 0.5F, 0.5F);
				EssentialCraftCore.proxy.FlameFX(pos.getX()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.2F, pos.getY()+0.65F, pos.getZ()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.2F, 0, 0.01F, 0, 0.8D, 0.5D, 0.5F, 1F);
			}
			EssentialCraftCore.proxy.SmokeFX(pos.getX()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.05F, pos.getY()+0.8F, pos.getZ()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.05F, 0, 0, 0, 1);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound i) {
		currentBurnTime = i.getInteger("burn");
		currentMaxBurnTime = i.getInteger("burnMax");
		super.readFromNBT(i);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound i) {
		i.setInteger("burn", currentBurnTime);
		i.setInteger("burnMax", currentMaxBurnTime);
		return super.writeToNBT(i);
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.heatgenerator";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.GENERATOR_MAX_MRU_GENERIC).setMinValue(1).getInt();
			cfgBalance = (float)cfg.get(category, "Balance", -1D, "Default balance (-1 is random)").setMinValue(-1D).setMinValue(2D).getDouble();
			mruGenerated = cfg.get(category, "MRUGenerated", 20, "Max MRU generated per tick").setMinValue(0).getInt();
		}
		catch(Exception e) {
			return;
		}
	}

	@Override
	public int[] getOutputSlots() {
		return new int[] {1};
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot == 0;
	}
}
