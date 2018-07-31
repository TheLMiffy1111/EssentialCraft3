package essentialcraft.common.tile;

import java.util.List;

import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.MathUtils;
import essentialcraft.api.ApiCore;
import essentialcraft.common.mod.EssentialCraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

public class TileUltraFlowerBurner extends TileMRUGeneric {

	public static int cfgMaxMRU = ApiCore.GENERATOR_MAX_MRU_GENERIC*10;
	public static float cfgBalance = -1F;
	public static int flowerBurnTime = 600;
	public static int mruGeneratedFlower = 60;
	public static int leavesBurnTime = 300;
	public static int mruGeneratedLeaves = 70;
	public static int tallgrassBurnTime = 100;
	public static int mruGeneratedTallgrass = 20;
	public static int logBurnTime = 2400;
	public static int mruGeneratedLog = 100;

	public BlockPos burnedFlower;
	public int burnTime = 0, mruProduced = 0;

	private boolean firstTick = true;

	public TileUltraFlowerBurner() {
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
				if(getWorld().getWorldTime()%80 == 0) {
					List<EntityItem> sapplings = getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX()-16, pos.getY()-0.5D, pos.getZ()-16, pos.getX()+17, pos.getY()+1.5D, pos.getZ()+17));
					if(!sapplings.isEmpty()) {
						for(int i = 0; i < sapplings.size(); ++i) {
							EntityItem sappling = sapplings.get(i);
							if(!sappling.isDead) {
								ItemStack sStk = sappling.getItem();
								if(!sStk.isEmpty()) {
									int[] ids = OreDictionary.getOreIDs(sStk);
									String name = "";
									if(ids != null && ids.length > 0) {
										for(int i1 = 0; i1 < ids.length; ++i1) {
											int oreDictID = ids[i1];
											String n = OreDictionary.getOreName(oreDictID);
											if(n != null && !n.isEmpty()) {
												name = n;
												break;
											}
										}
									}
									if(name.contains("sapling")) {
										int pBIDX = MathHelper.floor(sappling.posX);
										int pBIDY = MathHelper.floor(sappling.posY);
										int pBIDZ = MathHelper.floor(sappling.posZ);
										Block b = getWorld().getBlockState(new BlockPos(pBIDX, pBIDY, pBIDZ)).getBlock();
										if(getWorld().isAirBlock(new BlockPos(pBIDX, pBIDY, pBIDZ))) {
											Block sBlk = Block.getBlockFromItem(sStk.getItem());
											if(sBlk != null) {
												getWorld().setBlockState(new BlockPos(pBIDX, pBIDY, pBIDZ), sBlk.getStateFromMeta(sStk.getItemDamage()), 3);
												sappling.setDead();
											}
										}
									}
								}
							}
						}
					}
				}
				if(burnedFlower == null && mruStorage.getMRU() < mruStorage.getMaxMRU()) {
					int offsetX = (int)(MathUtils.randomDouble(getWorld().rand)*16);
					int offsetZ = (int)(MathUtils.randomDouble(getWorld().rand)*16);
					y:
						for(int y = 32; y >= 0; --y) {
							IBlockState state = getWorld().getBlockState(pos.add(offsetX, y, offsetZ));
							Block b = state.getBlock();
							int[] ids = b != Blocks.AIR && Item.getItemFromBlock(b) != null ? OreDictionary.getOreIDs(new ItemStack(b, 1, b.damageDropped(state))) : null;
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
								burnedFlower = pos.add(offsetX, y, offsetZ);
								burnTime = 600;
								mruProduced = 60;
							}
							if(name.toLowerCase().contains("leaves") || b.isLeaves(getWorld().getBlockState(pos.add(offsetX, y, offsetZ)), getWorld(), pos.add(offsetX, y, offsetZ))) {
								burnedFlower = pos.add(offsetX, y, offsetZ);
								burnTime = 300;
								mruProduced = 70;
							}
							if(name.toLowerCase().contains("tallgrass") || b instanceof BlockTallGrass || b instanceof BlockDoublePlant) {
								burnedFlower = pos.add(offsetX, y, offsetZ);
								burnTime = 100;
								mruProduced = 20;
							}
							if(name.toLowerCase().contains("logWood") || b instanceof BlockLog) {
								burnedFlower = pos.add(offsetX, y, offsetZ);
								burnTime = 2400;
								mruProduced = 100;
							}
							if(mruProduced > 0)
								break y;
						}
				}
				else if(burnedFlower != null) {
					--burnTime;
					int mruGenerated = mruProduced;
					mruStorage.addMRU(mruGenerated, true);
					if(burnTime <= 0) {
						if(getWorld().getBlockState(burnedFlower).getBlock().isLeaves(getWorld().getBlockState(burnedFlower), getWorld(), burnedFlower)) {
							Item droppedSapling = getWorld().getBlockState(burnedFlower).getBlock().getItemDropped(getWorld().getBlockState(burnedFlower), getWorld().rand, 0);
							if(droppedSapling != null) {
								if(getWorld().rand.nextFloat() < 0.05F) {
									ItemStack saplingStk = new ItemStack(droppedSapling,1,getWorld().getBlockState(burnedFlower).getBlock().damageDropped(getWorld().getBlockState(burnedFlower)));
									EntityItem sapling = new EntityItem(getWorld(), burnedFlower.getX()+0.5D, burnedFlower.getY()+0.5D, burnedFlower.getZ()+0.5D, saplingStk);
									getWorld().spawnEntity(sapling);
								}
							}
						}
						getWorld().setBlockToAir(burnedFlower);
						burnedFlower = null;
						mruProduced = 0;
					}
				}
			}
			if(world.isRemote && burnedFlower != null && burnTime > 0) {
				EssentialCraftCore.proxy.FlameFX(burnedFlower.getX()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.3F, burnedFlower.getY()+0.1F + getWorld().rand.nextFloat()/2, burnedFlower.getZ()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.3F, 0, 0, 0, 1, 1, 1, 1);
				EssentialCraftCore.proxy.FlameFX(burnedFlower.getX()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.3F, burnedFlower.getY()+0.1F + getWorld().rand.nextFloat()/2, burnedFlower.getZ()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.3F, (pos.getX()-0.5D-burnedFlower.getX()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.3F)/20, (pos.getY()-burnedFlower.getY()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.3F)/20, (pos.getZ()-0.5D-burnedFlower.getZ()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.3F)/20, 1, 1, 1, 1);
				--burnTime;
				if(burnTime <= 0) {
					for(int t = 0; t < 600; ++t)
						EssentialCraftCore.proxy.SmokeFX(burnedFlower.getX()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.3F, burnedFlower.getY()+0.1F + getWorld().rand.nextFloat()/2, burnedFlower.getZ()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.3F, 0, 0, 0,1);
				}
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
		burnTime = i.getInteger("burn");
		mruProduced = i.getInteger("genMRU");
		super.readFromNBT(i);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound i) {
		if(burnedFlower != null) {
			i.setString("coord", "||x:" + burnedFlower.getX() + "||y:" + burnedFlower.getY() + "||z:" + burnedFlower.getZ());
		}
		i.setInteger("burn", burnTime);
		i.setInteger("genMRU", mruProduced);
		return super.writeToNBT(i);
	}

	@Override
	public int[] getOutputSlots() {
		return new int[0];
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.ultranaturalfurnace";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.GENERATOR_MAX_MRU_GENERIC).setMinValue(1).getInt();
			cfgBalance = (float)cfg.get(category, "Balance", -1D, "Default balance (-1 is random)").setMinValue(-1D).setMaxValue(2D).getDouble();
			flowerBurnTime = cfg.get(category, "TicksRequiredFlower", 600).setMinValue(0).getInt();
			mruGeneratedFlower = cfg.get(category, "MRUGeneratedFlower", 60).setMinValue(0).getInt();
			leavesBurnTime = cfg.get(category, "TicksRequiredLeaves", 300).setMinValue(0).getInt();
			mruGeneratedLeaves = cfg.get(category, "MRUGeneratedLeaves", 70).setMinValue(0).getInt();
			tallgrassBurnTime = cfg.get(category, "TicksRequiredGrass", 100).setMinValue(0).getInt();
			mruGeneratedTallgrass = cfg.get(category, "MRUGeneratedGrass", 20).setMinValue(0).getInt();
			logBurnTime = cfg.get(category, "TicksRequiredLog", 2400).setMinValue(0).getInt();
			mruGeneratedLog = cfg.get(category, "MRUGeneratedLog", 100).setMinValue(0).getInt();
		}
		catch(Exception e) {
			return;
		}
	}
}
