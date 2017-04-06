package ec3.common.tile;

import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.config.Configuration;
import ec3.api.ApiCore;
import ec3.api.IHotBlock;
import ec3.common.item.ItemsCore;
import ec3.common.mod.EssentialCraftCore;

public class TileHeatGenerator extends TileMRUGeneric {
	
	public int currentBurnTime, currentMaxBurnTime;
	public static float cfgMaxMRU = ApiCore.GENERATOR_MAX_MRU_GENERIC;
	public static float cfgBalance = -1F;
	public static float mruGenerated = 20;
	
	public TileHeatGenerator() {
		super();
		balance = cfgBalance;
		maxMRU = (int)cfgMaxMRU;
		slot0IsBoundGem = false;
		setSlotsNum(2);
	}
	
	public boolean canGenerateMRU() {
		return false;
	}
	
	@Override
	public void update() {
		float mruGen = mruGenerated;
		super.update();
		if(balance == -1) {
			balance = getWorld().rand.nextFloat()*2;
		}
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			if(currentBurnTime > 0) {
				--currentBurnTime;
				if(!getWorld().isRemote) {
					float mruFactor = 1.0F;
					Block[] b = new Block[4];
					b[0] = getWorld().getBlockState(pos.east(2)).getBlock();
					b[1] = getWorld().getBlockState(pos.west(2)).getBlock();
					b[2] = getWorld().getBlockState(pos.south(2)).getBlock();
					b[3] = getWorld().getBlockState(pos.north(2)).getBlock();
					int[] ox = new int[]{2, -2, 0, 0};
					int[] oz = new int[]{0, 0, 2, -2};
					for(int i = 0; i < 4; ++i) {
						if(b[i] == Blocks.AIR)
							mruFactor *= 0;
						else if(b[i] == Blocks.NETHERRACK)
							mruFactor *= 0.75F;
						else if(b[i] == Blocks.LAVA)
							mruFactor *= 0.95F;
						else if(b[i] == Blocks.FIRE)
							mruFactor *= 0.7F;
						else if(b[i] instanceof IHotBlock)
							mruFactor *= (((IHotBlock)b[i]).getHeatModifier(getWorld(), pos.getX()+ox[i], pos.getY(), pos.getZ()+oz[i]));
						else
							mruFactor *= 0.5F;
					}
					
					mruGen *= mruFactor;
					if(mruGen >= 1) {
						setMRU((int)(getMRU() + mruGen));
						if(getMRU() > getMaxMRU())
							setMRU(getMaxMRU());
					}
				}
			}
			
			if(!getWorld().isRemote) {
				if(getStackInSlot(0) != null) {
					if(currentBurnTime == 0 && getMRU() < getMaxMRU()) {
						currentMaxBurnTime = currentBurnTime = TileEntityFurnace.getItemBurnTime(getStackInSlot(0));
						
						if(currentBurnTime > 0) {
							if(getStackInSlot(0) != null) {
								if(getStackInSlot(1) == null || getStackInSlot(1).stackSize < getInventoryStackLimit()) {
									if(getStackInSlot(1) != null && getStackInSlot(1).getItem() == ItemsCore.magicalSlag) {
										ItemStack stk = getStackInSlot(1);
										++stk.stackSize;
										setInventorySlotContents(1, stk);
									}
									if(getStackInSlot(1) == null) {
										ItemStack stk = new ItemStack(ItemsCore.magicalSlag,1,0);
										setInventorySlotContents(1, stk);
									}
								}
								if(getStackInSlot(0).stackSize == 0) {
									setInventorySlotContents(0, getStackInSlot(0).getItem().getContainerItem(getStackInSlot(0)));
								}
								decrStackSize(0, 1);
							}
						}
					}
				}
			}
		}
		for(int i = 2; i < 6; ++i) {
			EnumFacing rotation = EnumFacing.getFront(i);
			float rotXAdv = rotation.getFrontOffsetX()-0.5F;
			float rotZAdv = rotation.getFrontOffsetZ()-0.5F;
			EssentialCraftCore.proxy.FlameFX(pos.getX()+0.725F + rotXAdv/2.2F, pos.getY()+0.4F, pos.getZ()+0.725F + rotZAdv/2.2F, 0, 0F, 0, 0.8D, 0.5D, 0.5F, 0.5F);
			EssentialCraftCore.proxy.FlameFX(pos.getX()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.2F, pos.getY()+0.65F, pos.getZ()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.2F, 0, 0.01F, 0, 0.8D, 0.5D, 0.5F, 1F);
		}
		EssentialCraftCore.proxy.SmokeFX(pos.getX()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.05F, pos.getY()+0.8F, pos.getZ()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.05F, 0, 0, 0, 1);
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
			cfg.load();
			String[] cfgArrayString = cfg.getStringList("HeatGeneratorSettings", "tileentities", new String[] {
					"Max MRU:"+ApiCore.GENERATOR_MAX_MRU_GENERIC,
					"Default balance(-1 is random):-1.0",
					"Max MRU generated per tick:20"
			}, "");
			String dataString = "";
			
			for(int i = 0; i < cfgArrayString.length; ++i)
				dataString += "||" + cfgArrayString[i];
			
			DummyData[] data = DataStorage.parseData(dataString);
			
			cfgMaxMRU = Float.parseFloat(data[0].fieldValue);
			cfgBalance = Float.parseFloat(data[1].fieldValue);
			mruGenerated = Float.parseFloat(data[2].fieldValue);
			
			cfg.save();
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
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return p_94041_1_ == 0;
	}
}
