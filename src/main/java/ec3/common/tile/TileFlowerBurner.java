package ec3.common.tile;

import DummyCore.Utils.Coord3D;
import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import ec3.api.ApiCore;
import ec3.common.mod.EssentialCraftCore;

public class TileFlowerBurner extends TileMRUGeneric {

	public Coord3D burnedFlower;
	public int burnTime = 0;

	public static float cfgMaxMRU =  ApiCore.GENERATOR_MAX_MRU_GENERIC;
	public static float cfgBalance = -1F;
	public static float mruGenerated = 10;
	public static int flowerBurnTime = 600;
	public static int saplingBurnTime = 900;
	public static int tallgrassBurnTime = 150;
	public static boolean createDeadBush = true;

	public TileFlowerBurner() {
		super();
		balance = cfgBalance;
		maxMRU = (int)cfgMaxMRU;
		slot0IsBoundGem = false;
	}

	public boolean canGenerateMRU() {
		return false;
	}

	@Override
	public void update() {
		super.update();
		if(balance == -1)
			balance = getWorld().rand.nextFloat()*2;
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			if(!getWorld().isRemote) {
				if(burnedFlower == null && getMRU() < getMaxMRU()) {
					int offsetX = (int)(MathUtils.randomDouble(getWorld().rand)*8);
					int offsetZ = (int)(MathUtils.randomDouble(getWorld().rand)*8);
					Block b = getWorld().getBlockState(pos.add(offsetX, 0, offsetZ)).getBlock();
					if(Item.getItemFromBlock(b) != null) {
						int[] ids = OreDictionary.getOreIDs(new ItemStack(b, 1, 0));
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
							burnedFlower = new Coord3D(pos.getX()+offsetX, pos.getY(), pos.getZ()+offsetZ);
							burnTime = flowerBurnTime;
						}
						if(name.toLowerCase().contains("sapling") || b instanceof BlockSapling) {
							burnedFlower = new Coord3D(pos.getX()+offsetX, pos.getY(), pos.getZ()+offsetZ);
							burnTime = saplingBurnTime;
						}
						if(name.toLowerCase().contains("tallgrass") || b instanceof BlockTallGrass) {
							burnedFlower = new Coord3D(pos.getX()+offsetX, pos.getY(), pos.getZ()+offsetZ);
							burnTime = tallgrassBurnTime;
						}
					}
				}
				else {
					if(burnedFlower != null) {
						--burnTime;
						setMRU((int)(getMRU() + mruGenerated));
						if(getMRU() > getMaxMRU())
							setMRU(getMaxMRU());
						if(burnTime <= 0) {
							if(createDeadBush)
								getWorld().setBlockState(new BlockPos((int)burnedFlower.x, (int)burnedFlower.y, (int)burnedFlower.z), Blocks.DEADBUSH.getStateFromMeta(0), 3);
							else
								getWorld().setBlockState(new BlockPos((int)burnedFlower.x, (int)burnedFlower.y, (int)burnedFlower.z), Blocks.AIR.getDefaultState(), 3);
							burnedFlower = null;
						}
					}
				}
			}
			if(burnedFlower != null && burnTime > 0) {
				getWorld().spawnParticle(EnumParticleTypes.FLAME, burnedFlower.x+0.5F + MathUtils.randomFloat(getWorld().rand)*0.3F, burnedFlower.y+0.1F + getWorld().rand.nextFloat()/2, burnedFlower.z+0.5F + MathUtils.randomFloat(getWorld().rand)*0.3F, 0, 0, 0);
				--burnTime;
				for(int t = 0; t < 200; ++t)
					EssentialCraftCore.proxy.SmokeFX(burnedFlower.x+0.5F + MathUtils.randomFloat(getWorld().rand)*0.3F, burnedFlower.y+0.1F + getWorld().rand.nextFloat()/2, burnedFlower.z+0.5F + MathUtils.randomFloat(getWorld().rand)*0.3F, 0, 0, 0, 1);
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
			burnedFlower = new Coord3D(Double.parseDouble(coordData[0].fieldValue), Double.parseDouble(coordData[1].fieldValue), Double.parseDouble(coordData[2].fieldValue));
		}
		burnTime = i.getInteger("burn");
		super.readFromNBT(i);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound i) {
		if(burnedFlower != null)
			i.setString("coord", burnedFlower.toString());
		i.setInteger("burn", burnTime);
		return super.writeToNBT(i);
	}

	public static void setupConfig(Configuration cfg) {
		try {
			cfg.load();
			String[] cfgArrayString = cfg.getStringList("NatureFurnaceSettings", "tileentities", new String[] {
					"Max MRU:"+ApiCore.GENERATOR_MAX_MRU_GENERIC,
					"Default balance(-1 is random):-1.0",
					"MRU generated per tick:10",
					"Time required to burn a flower:600",
					"Time required to burn a sapling:900",
					"Time required to burn grass:150",
					"Should leave Dead Bushes:true"
			}, "");
			String dataString = "";

			for(int i = 0; i < cfgArrayString.length; ++i)
				dataString += "||" + cfgArrayString[i];

			DummyData[] data = DataStorage.parseData(dataString);

			cfgMaxMRU = Float.parseFloat(data[0].fieldValue);
			cfgBalance = Float.parseFloat(data[1].fieldValue);
			mruGenerated = Float.parseFloat(data[2].fieldValue);
			flowerBurnTime = Integer.parseInt(data[3].fieldValue);
			saplingBurnTime = Integer.parseInt(data[4].fieldValue);
			tallgrassBurnTime = Integer.parseInt(data[5].fieldValue);
			createDeadBush = Boolean.parseBoolean(data[6].fieldValue);

			cfg.save();
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
