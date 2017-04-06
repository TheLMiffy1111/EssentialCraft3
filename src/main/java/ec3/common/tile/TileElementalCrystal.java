package ec3.common.tile;

import java.util.Random;

import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.MiscUtils;
import DummyCore.Utils.Notifier;
import DummyCore.Utils.TileStatTracker;
import ec3.common.mod.EssentialCraftCore;
import ec3.utils.common.ECUtils;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.config.Configuration;

public class TileElementalCrystal extends TileEntity implements ITickable {
	public int syncTick = 10;
	public float size,fire,water,earth,air;
	private TileStatTracker tracker;
	public boolean requestSync = true;
	
	public static float mutatuinChance = 0.001F;
	public static float growthModifier = 1.0F;
	
	public TileElementalCrystal() {
		super();
		tracker = new TileStatTracker(this);
	}
	
	@Override
    public void readFromNBT(NBTTagCompound i) {
		super.readFromNBT(i);
		size = i.getFloat("size");
		fire = i.getFloat("fire");
		water = i.getFloat("water");
		earth = i.getFloat("earth");
		air = i.getFloat("air");
    }
	
	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound i)  {
    	super.writeToNBT(i);
    	i.setFloat("size", size);
    	i.setFloat("fire", fire);
    	i.setFloat("water", water);
    	i.setFloat("earth", earth);
    	i.setFloat("air", air);
    	return i;
    }
    
	public float getElementByNum(int num) {
		if(num == 0)
			return fire;
		if(num == 1)
			return water;
		if(num == 2)
			return earth;
		if(num == 3)
			return air;
		return -1;
	}
	
	public void setElementByNum(int num, float amount) {
		if(num == 0)
			fire += amount;
		if(num == 1)
			water += amount;
		if(num == 2)
			earth += amount;
		if(num == 3)
			air += amount;
	}
	
	public void randomlyMutate() {
		Random r = getWorld().rand;
		if(r.nextFloat() <= mutatuinChance)
			mutate(r.nextInt(4), r.nextInt(3)-r.nextInt(3));
	}
	
	public boolean mutate(int element, int amount)  {
		if(getElementByNum(element) + amount <= 100 && getElementByNum(element) + amount >= 0)
			setElementByNum(element, amount);
		return false;
	}
	
	public int getDominant() {
		if(fire > water && fire > earth && fire > air)
			return 0;
		if(water > fire && water > earth && water > air)
			return 1;
		if(earth > water && earth > fire && earth > air)
			return 2;
		if(air > fire && air > earth && air > water)
			return 3;
		return -1;
	}
	
	public void update() {
		int metadata = this.getBlockMetadata();
		
		if(metadata == 1) {
			Block b = getWorld().getBlockState(pos.down()).getBlock();
			if(!b.isBlockSolid(getWorld(), pos.down(), EnumFacing.UP)) {
				getWorld().getBlockState(pos).getBlock().dropBlockAsItem(getWorld(), pos, getWorld().getBlockState(pos), 0);
				getWorld().setBlockToAir(pos);
			}
		}
		
		if(metadata == 0) {
			Block b = getWorld().getBlockState(pos.up()).getBlock();
			if(!b.isBlockSolid(getWorld(), pos.up(), EnumFacing.DOWN)) {
				getWorld().getBlockState(pos).getBlock().dropBlockAsItem(getWorld(), pos, getWorld().getBlockState(pos), 0);
				getWorld().setBlockToAir(pos);
			}
		}
		
		if(metadata == 3) {
			Block b = getWorld().getBlockState(pos.north()).getBlock();
			if(!b.isBlockSolid(getWorld(), pos.north(), EnumFacing.SOUTH)) {
				getWorld().getBlockState(pos).getBlock().dropBlockAsItem(getWorld(), pos, getWorld().getBlockState(pos), 0);
				getWorld().setBlockToAir(pos);
			}
		}
		
		if(metadata == 2) {
			Block b = getWorld().getBlockState(pos.south()).getBlock();
			if(!b.isBlockSolid(getWorld(), pos.south(), EnumFacing.NORTH)) {
				getWorld().getBlockState(pos).getBlock().dropBlockAsItem(getWorld(), pos, getWorld().getBlockState(pos), 0);
				getWorld().setBlockToAir(pos);
			}
		}
		
		if(metadata == 5) {
			Block b = getWorld().getBlockState(pos.west()).getBlock();
			if(!b.isBlockSolid(getWorld(), pos.west(), EnumFacing.EAST)) {
				getWorld().getBlockState(pos).getBlock().dropBlockAsItem(getWorld(), pos, getWorld().getBlockState(pos), 0);
				getWorld().setBlockToAir(pos);
			}
		}
		
		if(metadata == 4) {
			Block b = getWorld().getBlockState(pos.east()).getBlock();
			if(!b.isBlockSolid(getWorld(), pos.east(), EnumFacing.WEST)) {
				getWorld().getBlockState(pos).getBlock().dropBlockAsItem(getWorld(), pos, getWorld().getBlockState(pos), 0);
				getWorld().setBlockToAir(pos);
			}
		}
		
		if(size < 100) {
			getWorld().spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, pos.getX()+getWorld().rand.nextFloat(),pos.getY()+1,pos.getZ()+getWorld().rand.nextFloat(), 0, 0, 0);
			if(!getWorld().isRemote) {
	    		size += 0.002F*growthModifier;
	    			randomlyMutate();
			}
		}
		
		//Sending the sync packets to the CLIENT. 
		if(syncTick == 0) {
			if(tracker == null)
				Notifier.notifyCustomMod("EssentialCraft", "[WARNING][SEVERE]TileEntity " + this + " at pos " + pos.getX() + "," + pos.getY() + ","  + pos.getZ() + " tries to sync itself, but has no TileTracker attached to it! SEND THIS MESSAGE TO THE DEVELOPER OF THE MOD!");
			else if(!getWorld().isRemote && tracker.tileNeedsSyncing()) {
				MiscUtils.sendPacketToAllAround(getWorld(), getUpdatePacket(), pos.getX(), pos.getY(), pos.getZ(), getWorld().provider.getDimension(), 32);
			}
			syncTick = 60;
		}
		else
			--syncTick;
		
		if(requestSync && getWorld().isRemote) {
			requestSync = false;
			ECUtils.requestScheduledTileSync(this, EssentialCraftCore.proxy.getClientPlayer());
		}
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		writeToNBT(nbttagcompound);
		return new SPacketUpdateTileEntity(pos, -10, nbttagcompound);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		if(pkt.getTileEntityType() == -10)
			readFromNBT(pkt.getNbtCompound());
	}
	
	public static void setupConfig(Configuration cfg) {
		try {
			cfg.load();
			String[] cfgArrayString = cfg.getStringList("ElementalCrystalSettings", "tileentities", new String[] {
					"Chance to mutate per tick:0.001",
					"Growth per tick modifier(crystal grows at 0.2% per tick):1.0"
			}, "");
			String dataString="";
			
			for(int i = 0; i < cfgArrayString.length; ++i)
				dataString += "||" + cfgArrayString[i];
			
			DummyData[] data = DataStorage.parseData(dataString);
			
			mutatuinChance = Float.parseFloat(data[0].fieldValue);
			growthModifier = Float.parseFloat(data[1].fieldValue);
			
			cfg.save();
		}
		catch(Exception e) {
			return;
		}
	}
}
