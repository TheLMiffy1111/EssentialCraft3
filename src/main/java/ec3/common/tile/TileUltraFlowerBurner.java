package ec3.common.tile;

import java.util.List;

import DummyCore.Utils.Coord3D;
import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.oredict.OreDictionary;
import ec3.api.ApiCore;
import ec3.common.mod.EssentialCraftCore;

public class TileUltraFlowerBurner extends TileMRUGeneric {
	
	public Coord3D burnedFlower;
	public int burnTime = 0, mruProduced = 0;
	
	public TileUltraFlowerBurner() {
		super();
		balance = -1;
		maxMRU = (int)ApiCore.GENERATOR_MAX_MRU_GENERIC*10;
		slot0IsBoundGem = false;
	}
	
	public boolean canGenerateMRU() {
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void update() {
		super.update();
		if(balance == -1) {
			balance = getWorld().rand.nextFloat()*2;
		}
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			if(!getWorld().isRemote) {
				if(getWorld().getWorldTime()%80 == 0) {
					List<EntityItem> sapplings = getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX()-16, pos.getY()-0.5D, pos.getZ()-16, pos.getX()+17, pos.getY()+1.5D, pos.getZ()+17));
					if(!sapplings.isEmpty()) {
						for(int i = 0; i < sapplings.size(); ++i) {
							EntityItem sappling = sapplings.get(i);
							if(!sappling.isDead) {
								ItemStack sStk = sappling.getEntityItem();
								if(sStk != null) {
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
									if(name.contains("treeSapling")) {
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
				if(burnedFlower == null && getMRU() < getMaxMRU()) {
					int offsetX = (int)(MathUtils.randomDouble(getWorld().rand)*16);
					int offsetZ = (int)(MathUtils.randomDouble(getWorld().rand)*16);
					y:
						for(int y = 32; y >= 0; --y) {
							Block b = getWorld().getBlockState(pos.add(offsetX, y, offsetZ)).getBlock();
							int[] ids = Item.getItemFromBlock(b) != null ? OreDictionary.getOreIDs(new ItemStack(b,1,0)) : null;
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
								burnedFlower = new Coord3D(pos.getX()+offsetX, pos.getY()+y, pos.getZ()+offsetZ);
								burnTime = 600;
								mruProduced = 60;
							}
							if(name.toLowerCase().contains("leaves") || b.isLeaves(getWorld().getBlockState(pos.add(offsetX, y, offsetZ)), getWorld(), pos.add(offsetX, y, offsetZ))) {
								burnedFlower = new Coord3D(pos.getX()+offsetX, pos.getY()+y, pos.getZ()+offsetZ);
								burnTime = 300;
								mruProduced = 70;
							}
							if(name.toLowerCase().contains("tallgrass") || b instanceof BlockTallGrass || b instanceof BlockDoublePlant) {
								burnedFlower = new Coord3D(pos.getX()+offsetX, pos.getY()+y, pos.getZ()+offsetZ);
								burnTime = 100;
								mruProduced = 20;
							}
							if(name.toLowerCase().contains("logWood") || b instanceof BlockLog) {
								burnedFlower = new Coord3D(pos.getX()+offsetX, pos.getY()+y, pos.getZ()+offsetZ);
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
					setMRU((int)(getMRU() + mruGenerated));
					if(getMRU() > getMaxMRU())
						setMRU(getMaxMRU());
					if(burnTime <= 0) {
						BlockPos burning = new BlockPos((int)burnedFlower.x, (int)burnedFlower.y, (int)burnedFlower.z);
						if(getWorld().getBlockState(burning).getBlock().isLeaves(getWorld().getBlockState(burning), getWorld(), burning)) {
							Item droppedSapling = getWorld().getBlockState(burning).getBlock().getItemDropped(getWorld().getBlockState(burning), getWorld().rand, 0);
							if(droppedSapling != null) {
								if(getWorld().rand.nextFloat() < 0.05F) {
									ItemStack saplingStk = new ItemStack(droppedSapling,1,getWorld().getBlockState(burning).getBlock().damageDropped(getWorld().getBlockState(burning)));
									EntityItem sapling = new EntityItem(getWorld(), (int)burnedFlower.x+0.5D, (int)burnedFlower.y+0.5D, (int)burnedFlower.z+0.5D, saplingStk);
									getWorld().spawnEntity(sapling);
								}
							}
						}
						getWorld().setBlockToAir(burning);
						burnedFlower = null;
						mruProduced = 0;
					}
				}
			}
			if(burnedFlower != null && burnTime > 0) {
				EssentialCraftCore.proxy.FlameFX(burnedFlower.x+0.5F + MathUtils.randomFloat(getWorld().rand)*0.3F, burnedFlower.y+0.1F + getWorld().rand.nextFloat()/2, burnedFlower.z+0.5F + MathUtils.randomFloat(getWorld().rand)*0.3F, 0, 0, 0, 1, 1, 1, 1);
				EssentialCraftCore.proxy.FlameFX(burnedFlower.x+0.5F + MathUtils.randomFloat(getWorld().rand)*0.3F, burnedFlower.y+0.1F + getWorld().rand.nextFloat()/2, burnedFlower.z+0.5F + MathUtils.randomFloat(getWorld().rand)*0.3F, ((pos.getX()-0.5D)-burnedFlower.x+0.5F + MathUtils.randomFloat(getWorld().rand)*0.3F)/20, (pos.getY()-burnedFlower.y+0.5F + MathUtils.randomFloat(getWorld().rand)*0.3F)/20, ((pos.getZ()-0.5D)-burnedFlower.z+0.5F + MathUtils.randomFloat(getWorld().rand)*0.3F)/20, 1, 1, 1, 1);
				--burnTime;
				if(burnTime <= 0) {
					for(int t = 0; t < 600; ++t)
						EssentialCraftCore.proxy.SmokeFX(burnedFlower.x+0.5F + MathUtils.randomFloat(getWorld().rand)*0.3F, burnedFlower.y+0.1F + getWorld().rand.nextFloat()/2, burnedFlower.z+0.5F + MathUtils.randomFloat(getWorld().rand)*0.3F, 0, 0, 0,1);
				}
			}
		}
		EssentialCraftCore.proxy.FlameFX(pos.getX()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.4F, pos.getY()+0.1F, pos.getZ()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.4F, 0, 0.01F, 0, 1D, 0.5D, 1, 1);
		EssentialCraftCore.proxy.FlameFX(pos.getX()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.2F, pos.getY()+0.2F, pos.getZ()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.2F, 0, 0.01F, 0, 1D, 0.5D, 1, 1);
		for(int i = 0; i < 10; ++i)
			EssentialCraftCore.proxy.SmokeFX(pos.getX()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.1F, pos.getY()+0.6F, pos.getZ()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.1F, 0, 0, 0,1);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound i) {
		if(i.hasKey("coord")) {
			DummyData[] coordData = DataStorage.parseData(i.getString("coord"));
			burnedFlower = new Coord3D(Double.parseDouble(coordData[0].fieldValue),Double.parseDouble(coordData[1].fieldValue),Double.parseDouble(coordData[2].fieldValue));
		}
		burnTime = i.getInteger("burn");
		mruProduced = i.getInteger("genMRU");
		super.readFromNBT(i);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound i) {
		if(burnedFlower != null)
			i.setString("coord", burnedFlower.toString());
		i.setInteger("burn", burnTime);
		i.setInteger("genMRU", mruProduced);
		return super.writeToNBT(i);
	}
	
	@Override
	public int[] getOutputSlots() {
		return new int[0];
	}
}
