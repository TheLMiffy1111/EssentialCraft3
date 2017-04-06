package ec3.common.tile;

import java.util.List;
import java.util.Random;

import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.config.Configuration;
import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import ec3.api.ApiCore;
import ec3.utils.common.ECUtils;

public class TileMagicalEnchanter extends TileMRUGeneric {
	
	List<EnchantmentData> enchants;
	public int progressLevel = -1;
	public int field_145926_a;
	public float field_145933_i;
	public float field_145931_j;
	public float field_145932_k;
	public float field_145929_l;
	public float field_145930_m;
	public float field_145927_n;
	public float field_145928_o;
	public float field_145925_p;
	public float field_145924_q;
	private static Random field_145923_r = new Random();
		
	public static float cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;
	public static boolean generatesCorruption = false;
	public static int genCorruption = 2;
	public static int mruUsage = 100;
	public static int maxEnchantmentLevel = 60;
	
	public TileMagicalEnchanter() {
		super();
		maxMRU = (int)cfgMaxMRU;
		setSlotsNum(3);
	}
	
	@Override
	public void update() {
		super.update();
		ECUtils.manage(this, 0);
		
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0)
			tryEnchant();
		
		field_145927_n = field_145930_m;
		field_145925_p = field_145928_o;
		EntityPlayer entityplayer = getWorld().getClosestPlayer((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), 3.0D, false);
		
		if(entityplayer != null) {
			double d0 = entityplayer.posX - (double)((float)pos.getX() + 0.5F);
			double d1 = entityplayer.posZ - (double)((float)pos.getZ() + 0.5F);
			field_145924_q = (float)Math.atan2(d1, d0);
			field_145930_m += 0.1F;
			
			if(field_145930_m < 0.5F || field_145923_r.nextInt(40) == 0) {
				float f1 = field_145932_k;
				
				do {
					field_145932_k += (float)(field_145923_r.nextInt(4) - field_145923_r.nextInt(4));
				}
				while(f1 == field_145932_k);
			}
		}
		else {
			field_145924_q += 0.02F;
			field_145930_m -= 0.1F;
		}
		
		while (field_145928_o >= (float)Math.PI) {
			field_145928_o -= ((float)Math.PI * 2F);
		}
		
		while (field_145928_o < -(float)Math.PI) {
			field_145928_o += ((float)Math.PI * 2F);
		}
		
		while (field_145924_q >= (float)Math.PI) {
			field_145924_q -= ((float)Math.PI * 2F);
		}
		
		while (field_145924_q < -(float)Math.PI) {
			field_145924_q += ((float)Math.PI * 2F);
		}
		
		float f2;
		
		for (f2 = field_145924_q - field_145928_o; f2 >= (float)Math.PI; f2 -= ((float)Math.PI * 2F)) {}
		
		while(f2 < -(float)Math.PI) {
			f2 += ((float)Math.PI * 2F);
		}
		
		field_145928_o += f2 * 0.4F;
		
		if(field_145930_m < 0.0F)
			field_145930_m = 0.0F;
		
		if(field_145930_m > 1.0F)
			field_145930_m = 1.0F;
		
		++field_145926_a;
		field_145931_j = field_145933_i;
		float f = (field_145932_k - field_145933_i) * 0.4F;
		float f3 = 0.2F;
		
		if(f < -f3)
			f = -f3;
		
		if(f > f3)
			f = f3;
		
		field_145929_l += (f - field_145929_l) * 0.9F;
		field_145933_i += field_145929_l;
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		AxisAlignedBB bb = INFINITE_EXTENT_AABB;
		return bb;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound i) {
		progressLevel = i.getInteger("work");
		super.readFromNBT(i);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound i) {
		i.setInteger("work", progressLevel);
		return super.writeToNBT(i);
	}
	
	public void tryEnchant() {
		if(canItemBeEnchanted() && !getWorld().isRemote) {
			if(setMRU(getMRU() - mruUsage)) {
				if(generatesCorruption)
					ECUtils.increaseCorruptionAt(getWorld(), pos.getX(), pos.getY(), pos.getZ(), getWorld().rand.nextInt(genCorruption));
				++progressLevel;
				if(progressLevel >= getRequiredTimeToAct()) {
					enchant();
					progressLevel = -1;
				}
			}
		}
		if(!canItemBeEnchanted()) {
			progressLevel = -1;
			enchants = null;
		}
	}
	
	public void enchant() {
    	List<EnchantmentData> enchants = getEnchantmentsForStack(getStackInSlot(1));
    	ItemStack enchanted = getStackInSlot(1).copy();
    	enchanted.stackSize = 1;
    	decrStackSize(1, 1);
    	for(int m = 0; m < enchants.size(); ++m) {
    		EnchantmentData d = enchants.get(m);
    		if(d != null) {
    			if(enchanted.getItem() == Items.BOOK) {
    				enchanted = new ItemStack(Items.ENCHANTED_BOOK, 1, 0);
    			}
    			enchanted.addEnchantment(d.enchantmentobj, d.enchantmentLevel);
    		}
    	}
    	setInventorySlotContents(2, enchanted);
    	enchants = null;
	}
	
	@SuppressWarnings("unchecked")
	public List<EnchantmentData> getEnchantmentsForStack(ItemStack stack) {
		if(enchants == null)
			enchants = EnchantmentHelper.buildEnchantmentList(getWorld().rand, stack, getMaxPower(), false);
		return enchants;
	}
	
	public int getRequiredTimeToAct() {
		return getMaxPower()*10;
	}
	
	public int getRequiredMRU() {
		return getMaxPower()*1000;
	}
	
	public int getMaxPower() {
		int l = 0;
		for(int x = -2; x <= 2; ++x) {
			for(int y = 0; y <= 2; ++y) {
				for(int z = -2; z <= 2; ++z) {
					if(x != 0 || y != 0 || z != 0) {
						l += ForgeHooks.getEnchantPower(getWorld(), pos);
					}
				}
			}
		}
		if(l > maxEnchantmentLevel)
			l = maxEnchantmentLevel;
		return l;
	}
	
	public boolean canItemBeEnchanted() {
		try {
			ItemStack s = getStackInSlot(1);
			if(s != null && getMaxPower() > 0 && getMRU() > mruUsage && getStackInSlot(2) == null) {
				if(s.isItemEnchantable() && getEnchantmentsForStack(s) != null && !getEnchantmentsForStack(s).isEmpty()) {
					return true;
				}
			}
			return false;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	public static void setupConfig(Configuration cfg) {
		try {
			cfg.load();
			String[] cfgArrayString = cfg.getStringList("MagicalEnchanterSettings", "tileentities", new String[]{
					"Max MRU:" + ApiCore.DEVICE_MAX_MRU_GENERIC,
					"MRU Usage:100",
					"Max level of enchantment:60",
					"Can this device actually generate corruption:false",
					"The amount of corruption generated each tick(do not set to 0!):2"
			}, "");
			String dataString = "";
			
			for(int i = 0; i < cfgArrayString.length; ++i)
				dataString += "||" + cfgArrayString[i];
			
			DummyData[] data = DataStorage.parseData(dataString);
			
			mruUsage = Integer.parseInt(data[1].fieldValue);
			maxEnchantmentLevel = Integer.parseInt(data[2].fieldValue);
			cfgMaxMRU = Float.parseFloat(data[0].fieldValue);
			generatesCorruption = Boolean.parseBoolean(data[3].fieldValue);
			genCorruption = Integer.parseInt(data[4].fieldValue);
			
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
}
