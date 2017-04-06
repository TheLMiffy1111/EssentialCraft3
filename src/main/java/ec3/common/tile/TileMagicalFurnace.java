package ec3.common.tile;

import java.util.ArrayList;
import java.util.List;

import ec3.api.ApiCore;
import ec3.common.block.BlocksCore;
import ec3.common.item.ItemsCore;
import ec3.common.mod.EssentialCraftCore;
import ec3.utils.common.ECUtils;
import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.MathUtils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.config.Configuration;

public class TileMagicalFurnace extends TileMRUGeneric {
	public int progressLevel;
	public static float cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC*10;
	public static int mruUsage = 25;
	public static int smeltingTime = 20;
	public static float chanceToDoubleOutput = 0.3F;
	public static float chanceToDoubleSlags = 0.1F;
	
	public TileMagicalFurnace() {
		super();
		setSlotsNum(1);
	}
		
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		progressLevel = par1NBTTagCompound.getInteger("progress");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("progress", progressLevel);
		return par1NBTTagCompound;
	}
	
	@Override
	public void update() {
		maxMRU = (int)cfgMaxMRU;
		super.update();
		spawnParticles();
		ECUtils.manage(this, 0);
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0)
			smelt();
	}
    
	public boolean isStructureCorrect() {
		boolean flag = true;
		for(int x = -2; x <= 2; ++x) {
			for(int z = -2; z <= 2; ++z) {
				flag = getWorld().getBlockState(pos.add(x, -1, z)).getBlock() == BlocksCore.voidStone;
				if(!flag)
					return false;
			}
		}
		flag = 
				getWorld().getBlockState(pos.add(2, 0, 2)).getBlock() == BlocksCore.voidStone &&
				getWorld().getBlockState(pos.add(-2, 0, 2)).getBlock() == BlocksCore.voidStone &&
				getWorld().getBlockState(pos.add(2, 0, -2)).getBlock() == BlocksCore.voidStone &&
				getWorld().getBlockState(pos.add(-2, 0, -2)).getBlock() == BlocksCore.voidStone &&
				getWorld().getBlockState(pos.add(2, 1, 2)).getBlock() == BlocksCore.heatGenerator &&
				getWorld().getBlockState(pos.add(-2, 1, 2)).getBlock() == BlocksCore.heatGenerator &&
				getWorld().getBlockState(pos.add(2, 1, -2)).getBlock() == BlocksCore.heatGenerator &&
				getWorld().getBlockState(pos.add(-2, 1, -2)).getBlock() == BlocksCore.heatGenerator;
		return flag;
	}
	
	public void smelt() {
		ItemStack smeltingStack = getSmeltingStack();
		EntityItem smeltingItem = getSmeltingItem();
		if(isStructureCorrect() && getMRU() > 0 && smeltingStack != null && smeltingItem != null) {
			if(progressLevel == 0)
				getWorld().playSound(null, pos, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F, 1);
			ItemStack mainSmelting = smeltingStack.copy();
			++progressLevel;
			setMRU(getMRU() - mruUsage);
			if(progressLevel >= smeltingTime) {
				progressLevel = 0;
				getWorld().playSound(null, pos, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 1.0F, 1);
				ItemStack s = FurnaceRecipes.instance().getSmeltingResult(mainSmelting);
				s.stackSize += 1;
				if(!getWorld().isRemote) {
					--smeltingItem.getEntityItem().stackSize;
					if(smeltingItem.getEntityItem().stackSize <= 0) {
						smeltingItem.setPosition(0, 0, 0);
						smeltingItem.setDead();
					}
				}
				if(!getWorld().isRemote && getWorld().rand.nextFloat() <= chanceToDoubleOutput) {
					EntityItem smelted = new EntityItem(getWorld(), pos.getX()-1.5, pos.getY()+2.15, pos.getZ()-1.5, s.copy());
					getWorld().spawnEntity(smelted);
					smelted.motionX = 0;
					smelted.motionY = 0;
					smelted.motionZ = 0;
				}
				EntityItem smelted = new EntityItem(getWorld(),pos.getX()+2.5, pos.getY()+2.15, pos.getZ()+2.5, s.copy());
				if(!getWorld().isRemote)
					getWorld().spawnEntity(smelted);
				smelted.motionX = 0;
				smelted.motionY = 0;
				smelted.motionZ = 0;
				if(!getWorld().isRemote && getWorld().rand.nextFloat() <= chanceToDoubleSlags) {
					EntityItem slag = new EntityItem(getWorld(),pos.getX()-1.5, pos.getY()+2.15, pos.getZ()+2.5, new ItemStack(ItemsCore.magicalSlag));
					getWorld().spawnEntity(slag);
					smelted.motionX = 0;
					smelted.motionY = 0;
					smelted.motionZ = 0;
				}
				EntityItem slag = new EntityItem(getWorld(),pos.getX()+2.5,pos.getY()+2.15,pos.getZ()-1.5,new ItemStack(ItemsCore.magicalSlag));
				if(!getWorld().isRemote)
					getWorld().spawnEntity(slag);
				smelted.motionX = 0;
				smelted.motionY = 0;
				smelted.motionZ = 0;
				s.stackSize = 0;
				s = null;
			}
		}
		else
			progressLevel = 0;
	}
	
	@SuppressWarnings("unchecked")
	public EntityItem getSmeltingItem() {
		EntityItem ret = null;
		List<EntityItem> l = getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX()+1, pos.getY()+2, pos.getZ()+1));
		if(!l.isEmpty()) {
			for(int i = 0; i < l.size(); ++i) {
				EntityItem item = l.get(i);
				if(FurnaceRecipes.instance().getSmeltingResult(item.getEntityItem()) != null)
					return item;
			}
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public ItemStack getSmeltingStack() {
		ItemStack ret = null;
		List<EntityItem> l = getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX()+1, pos.getY()+2, pos.getZ()+1));
		if(!l.isEmpty()) {
			List<ItemStack> canBeSmelted = new ArrayList<ItemStack>();
			for(int i = 0; i < l.size(); ++i) {
				EntityItem item = l.get(i);
				if(FurnaceRecipes.instance().getSmeltingResult(item.getEntityItem()) != null)
					canBeSmelted.add(item.getEntityItem());
			}
			if(!canBeSmelted.isEmpty())
				ret = canBeSmelted.get(0);
		}
		return ret;
	}
	
	public void spawnParticles() {
		if(isStructureCorrect()) {
			/*for(int i = 0; i < 100; ++i) */ {
				EssentialCraftCore.proxy.spawnParticle("cSpellFX", pos.getX()+0.5F + MathUtils.randomFloat(getWorld().rand)*3, pos.getY(), pos.getZ()+0.5F + MathUtils.randomFloat(getWorld().rand)*3, 0,2, 0);
			}
		}
	}
	
	public static void setupConfig(Configuration cfg) {
		try {
			cfg.load();
			String[] cfgArrayString = cfg.getStringList("MagicalFurnaceSettings", "tileentities", new String[] {
					"Max MRU:" + ApiCore.DEVICE_MAX_MRU_GENERIC*10,
					"MRU Usage:25",
					"Ticks required to smelt 1 item:20",
					"Chance to double the outcome:0.3",
					"Chance to double slags outcome:0.1"
			}, "");
			String dataString = "";
			
			for(int i = 0; i < cfgArrayString.length; ++i)
				dataString += "||" + cfgArrayString[i];
			
			DummyData[] data = DataStorage.parseData(dataString);
			
			mruUsage = Integer.parseInt(data[1].fieldValue);
			smeltingTime = Integer.parseInt(data[2].fieldValue);
			cfgMaxMRU = Float.parseFloat(data[0].fieldValue);
			chanceToDoubleOutput = Float.parseFloat(data[3].fieldValue);
			chanceToDoubleSlags = Float.parseFloat(data[4].fieldValue);
			
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
