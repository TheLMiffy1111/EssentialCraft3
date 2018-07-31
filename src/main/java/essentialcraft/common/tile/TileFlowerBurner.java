package essentialcraft.common.tile;

import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.MathUtils;
import essentialcraft.api.ApiCore;
import essentialcraft.common.mod.EssentialCraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

public class TileFlowerBurner extends TileMRUGeneric {

	public static int cfgMaxMRU = ApiCore.GENERATOR_MAX_MRU_GENERIC;
	public static float cfgBalance = -1F;
	public static int mruGenerated = 10;
	public static int flowerBurnTime = 600;
	public static int saplingBurnTime = 900;
	public static int tallgrassBurnTime = 150;
	public static boolean createDeadBush = true;

	public BlockPos burnedFlower;
	public int burnTime = 0;
	private boolean firstTick = true;

	public TileFlowerBurner() {
		super(cfgMaxMRU);
		slot0IsBoundGem = false;
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
			if(!getWorld().isRemote) {
				if(burnedFlower == null && mruStorage.getMRU() < mruStorage.getMaxMRU()) {
					int offsetX = (int)(MathUtils.randomDouble(getWorld().rand)*8);
					int offsetZ = (int)(MathUtils.randomDouble(getWorld().rand)*8);
					IBlockState state = getWorld().getBlockState(pos.add(offsetX, 0, offsetZ));
					Block b = state.getBlock();
					if(b != Blocks.AIR && Item.getItemFromBlock(b) != null) {
						int[] ids = OreDictionary.getOreIDs(new ItemStack(b, 1, b.damageDropped(state)));
						String name = "";
						if(ids != null && ids.length > 0) {
							for(int i = 0; i < ids.length; ++i) {
								int oreDictID = ids[i];
								String n = OreDictionary.getOreName(oreDictID);
								if(n != null && !n.isEmpty()) {
									name = n;
									break;
								}
							}
						}
						if(name.toLowerCase().contains("flower") || b instanceof BlockFlower) {
							burnedFlower = pos.add(offsetX, 0, offsetZ);
							burnTime = flowerBurnTime;
						}
						if(name.toLowerCase().contains("sapling") || b instanceof BlockSapling) {
							burnedFlower = pos.add(offsetX, 0, offsetZ);
							burnTime = saplingBurnTime;
						}
						if(name.toLowerCase().contains("tallgrass") || b instanceof BlockTallGrass) {
							burnedFlower = pos.add(offsetX, 0, offsetZ);
							burnTime = tallgrassBurnTime;
						}
					}
				}
				else if(burnedFlower != null) {
					--burnTime;
					mruStorage.addMRU(mruGenerated, true);
					if(burnTime <= 0) {
						if(createDeadBush) {
							Block blk = getWorld().getBlockState(burnedFlower.down()).getBlock();
							if(blk == Blocks.GRASS) {
								getWorld().setBlockState(burnedFlower.down(), Blocks.DIRT.getDefaultState(), 3);
							}
							getWorld().setBlockState(burnedFlower, Blocks.DEADBUSH.getDefaultState(), 3);
						}
						else {
							getWorld().setBlockState(burnedFlower, Blocks.AIR.getDefaultState(), 3);
						}
						burnedFlower = null;
					}
				}
			}
			if(world.isRemote && burnedFlower != null && burnTime > 0) {
				getWorld().spawnParticle(EnumParticleTypes.FLAME, burnedFlower.getX()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.3F, burnedFlower.getY()+0.1F + getWorld().rand.nextFloat()/2, burnedFlower.getZ()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.3F, 0, 0, 0);
				for(int t = 0; t < 200; ++t)
					EssentialCraftCore.proxy.SmokeFX(burnedFlower.getX()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.3F, burnedFlower.getY()+0.1F + getWorld().rand.nextFloat()/2, burnedFlower.getZ()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.3F, 0, 0, 0, 1);
			}
		}
		if(world.isRemote) {
			EssentialCraftCore.proxy.FlameFX(pos.getX()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.4F, pos.getY()+0.1F, pos.getZ()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.4F, 0, 0.01F, 0, 1D, 0.5D, 1, 1);
			EssentialCraftCore.proxy.FlameFX(pos.getX()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.2F, pos.getY()+0.2F, pos.getZ()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.2F, 0, 0.01F, 0, 1D, 0.5D, 1, 1);
			for(int i = 0; i < 10; ++i)
				EssentialCraftCore.proxy.SmokeFX(pos.getX()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.1F, pos.getY()+0.6F, pos.getZ()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.1F, 0, 0, 0,1);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound i) {
		if(i.hasKey("coord")) {
			DummyData[] coordData = DataStorage.parseData(i.getString("coord"));
			burnedFlower = new BlockPos(Integer.parseInt(coordData[0].fieldValue), Integer.parseInt(coordData[1].fieldValue), Integer.parseInt(coordData[2].fieldValue));
		}
		else {
			burnedFlower = null;
		}
		burnTime = i.getInteger("burn");
		super.readFromNBT(i);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound i) {
		if(burnedFlower != null) {
			i.setString("coord", "||x:" + burnedFlower.getX() + "||y:" + burnedFlower.getY() + "||z:" + burnedFlower.getZ());
		}
		i.setInteger("burn", burnTime);
		return super.writeToNBT(i);
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.naturalfurnace";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.GENERATOR_MAX_MRU_GENERIC).setMinValue(1).getInt();
			cfgBalance = (float)cfg.get(category, "Balance", -1D, "Default balance (-1 is random)").setMinValue(-1D).setMaxValue(2D).getDouble();
			mruGenerated = cfg.get(category, "MRUGenerated", 10, "MRU generated per tick").setMinValue(0).getInt();
			flowerBurnTime = cfg.get(category, "TicksRequiredFlower", 600).setMinValue(0).getInt();
			saplingBurnTime = cfg.get(category, "TicksRequiredSapling", 900).setMinValue(0).getInt();
			tallgrassBurnTime = cfg.get(category, "TicksRequiredGrass", 150).setMinValue(0).getInt();
			createDeadBush = cfg.get(category, "LeaveDeadBush", true).getBoolean();
		}
		catch(Exception e) {
			return;
		}
	}

	@Override
	public int[] getOutputSlots() {
		return new int[0];
	}
}
