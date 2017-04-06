package ec3.common.tile;

import DummyCore.Utils.Coord3D;
import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.MathUtils;
import DummyCore.Utils.MiscUtils;
import DummyCore.Utils.Notifier;
import DummyCore.Utils.TileStatTracker;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import ec3.api.MithrilineFurnaceRecipe;
import ec3.api.MithrilineFurnaceRecipes;
import ec3.common.block.BlockMithrilineCrystal;
import ec3.common.block.BlocksCore;
import ec3.common.mod.EssentialCraftCore;
import ec3.utils.common.ECUtils;

public class TileMithrilineFurnace extends TileEntity implements ISidedInventory, ITickable {
	
	public static float maxEnergy = 10000F;
	public float energy;
	public float progress;
	public float reqProgress;
	private TileStatTracker tracker;
	public int syncTick = 10;
	public ItemStack[] items = new ItemStack[2];
	public boolean requestSync = true;

	public TileMithrilineFurnace() {
		super();
		tracker = new TileStatTracker(this);
	}
	
	@Override
    public void readFromNBT(NBTTagCompound i) {
		super.readFromNBT(i);
		MiscUtils.loadInventory(this, i);
		energy = i.getFloat("energy");
		progress = i.getFloat("progress");
    }
	
	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound i) {
    	super.writeToNBT(i);
    	MiscUtils.saveInventory(this, i);
    	i.setFloat("energy", energy);
    	i.setFloat("progress", progress);
    	return i;
    }
	
	@Override
	public void update() {
		boolean correct = isStructureCorrect();
		if(syncTick == 0) {
			if(tracker == null)
				Notifier.notifyCustomMod("EssentialCraft", "[WARNING][SEVERE]TileEntity " + this + " at pos " + pos.getX() + "," + pos.getY() + "," + pos.getZ() + " tries to sync itself, but has no TileTracker attached to it! SEND THIS MESSAGE TO THE DEVELOPER OF THE MOD!");
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
		
		if(correct) {
			Coord3D[] possiblePowerSources = new Coord3D[] {
					new Coord3D(0, 1, 0),
					new Coord3D(1, 0, 1), new Coord3D(-1, 0, 1), new Coord3D(1, 0, -1), new Coord3D(-1, 0, -1),
					new Coord3D(2, 1, 0), new Coord3D(-2, 1, 0), new Coord3D(0, 1, 2), new Coord3D(0, 1, -2),
					new Coord3D(2, 2, 2), new Coord3D(-2, 2, 2), new Coord3D(2, 2, -2), new Coord3D(-2, 2, -2)
					
			};
			if(energy < maxEnergy)
				for(int i = 0; i < possiblePowerSources.length; ++i) {
					Coord3D c = possiblePowerSources[i];
					int dX = MathHelper.floor(c.x);
					int dY = MathHelper.floor(c.y);
					int dZ = MathHelper.floor(c.z);
					Block b = getWorld().getBlockState(pos.add(dX, dY, dZ)).getBlock();
					if(b instanceof BlockMithrilineCrystal) {
						TileEntity c_tile = getWorld().getTileEntity(pos.add(dX, dY, dZ));
						if(c_tile != null && c_tile instanceof TileMithrilineCrystal) {
							TileMithrilineCrystal crystal = (TileMithrilineCrystal) c_tile;
							float c_energy = crystal.energy;
							float movement = getWorld().getWorldTime() % 60;
							if(movement > 30)
								movement = 60F - movement;
							
							if(energy + c_energy <= maxEnergy) {
								energy += c_energy;
								crystal.energy = 0;
							}
							else {
								float energyReq = maxEnergy - energy;
								if(c_energy >= energyReq) {
									energy = maxEnergy;
									crystal.energy -= energyReq;
								}
							}
							getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+dX+0.5F, pos.getY()+dY+movement/30, pos.getZ()+dZ+0.5F, -1, 1, 0);
							getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+dX+0.5F, pos.getY()+dY+2+movement/30, pos.getZ()+dZ+0.5F, -1, 1, 0);
						}
					}
				}
			
			if(getStackInSlot(0) != null) {
				MithrilineFurnaceRecipe rec = MithrilineFurnaceRecipes.findRecipeByComponent(getStackInSlot(0));
				if(rec != null && getStackInSlot(0).stackSize >= rec.requiredRecipeSize) {
					reqProgress = rec.enderStarPulsesRequired;
					if(getStackInSlot(1) == null || (getStackInSlot(1).isItemEqual(rec.result) && getStackInSlot(1).stackSize+rec.result.stackSize <= getStackInSlot(1).getMaxStackSize())) {
						if(energy >= reqProgress) {
							progress = reqProgress;
							energy -= reqProgress;
						}
						else {
							progress += energy;
							energy = 0;
						}
						if(progress >= reqProgress) {
							decrStackSize(0, rec.requiredRecipeSize);
							if(getStackInSlot(1) == null)
								setInventorySlotContents(1, rec.result.copy());
							else
								getStackInSlot(1).stackSize += rec.result.stackSize;
							
							progress = 0;
						}
					}
				}
				else {
					progress = 0;
					reqProgress = 0;
				}
			}
			else {
				progress = 0;
				reqProgress = 0;
			}
			
			for(int i = 0; i < 10; ++i)
				EssentialCraftCore.proxy.FlameFX(pos.getX()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.4F, pos.getY()+0.2F + MathUtils.randomFloat(getWorld().rand)*0.6F, pos.getZ()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.4F, 0, 0.01F, 0, 0D, 1D, 0F, 1F);
		}
	}
	
	public boolean isStructureCorrect() {
		boolean hasPlatformBelow = 
				getWorld().getBlockState(pos.add(-1, -1, 0)).getBlock() == BlocksCore.invertedBlock &&
				getWorld().getBlockState(pos.add(1, -1, 0)).getBlock() == BlocksCore.invertedBlock &&
				getWorld().getBlockState(pos.add(0, -1, -1)).getBlock() == BlocksCore.invertedBlock &&
				getWorld().getBlockState(pos.add(0, -1, 1)).getBlock() == BlocksCore.invertedBlock &&
				getWorld().getBlockState(pos.add(1, -1, 1)).getBlock() == BlocksCore.invertedBlock &&
				getWorld().getBlockState(pos.add(1, -1, -1)).getBlock() == BlocksCore.invertedBlock &&
				getWorld().getBlockState(pos.add(-1, -1, 1)).getBlock() == BlocksCore.invertedBlock &&
				getWorld().getBlockState(pos.add(-1, -1, -1)).getBlock() == BlocksCore.invertedBlock &&
				getWorld().getBlockState(pos.add(-2, -1, 0)).getBlock() == BlocksCore.invertedBlock &&
				getWorld().getBlockState(pos.add(2, -1, 0)).getBlock() == BlocksCore.invertedBlock &&
				getWorld().getBlockState(pos.add(0, -1, -2)).getBlock() == BlocksCore.invertedBlock &&
				getWorld().getBlockState(pos.add(0, -1, 2)).getBlock() == BlocksCore.invertedBlock;
		
		boolean hasGenericOutline = 
				getWorld().getBlockState(pos.add(-2, 0, 0)).getBlock() == BlocksCore.invertedBlock &&
				getWorld().getBlockState(pos.add(2, 0, 0)).getBlock() == BlocksCore.invertedBlock &&
				getWorld().getBlockState(pos.add(0, 0, -2)).getBlock() == BlocksCore.invertedBlock &&
				getWorld().getBlockState(pos.add(0, 0, 2)).getBlock() == BlocksCore.invertedBlock &&
				getWorld().getBlockState(pos.add(-2, 0, 2)).getBlock() == BlocksCore.invertedBlock &&
				getWorld().getBlockState(pos.add(2, 0, -2)).getBlock() == BlocksCore.invertedBlock &&
				getWorld().getBlockState(pos.add(-2, 0, -2)).getBlock() == BlocksCore.invertedBlock &&
				getWorld().getBlockState(pos.add(2, 0, 2)).getBlock() == BlocksCore.invertedBlock &&
				getWorld().getBlockState(pos.add(-2, 1, 2)).getBlock() == BlocksCore.invertedBlock &&
				getWorld().getBlockState(pos.add(2, 1, -2)).getBlock() == BlocksCore.invertedBlock &&
				getWorld().getBlockState(pos.add(-2, 1, -2)).getBlock() == BlocksCore.invertedBlock &&
				getWorld().getBlockState(pos.add(2, 1, 2)).getBlock() == BlocksCore.invertedBlock;
		
		return hasPlatformBelow && hasGenericOutline;
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
			String[] cfgArrayString = cfg.getStringList("MithrilineFurnaceSettings", "tileentities", new String[] {
					"Maximum enderstar pulse stored:10000"
			}, "");
			String dataString = "";
			
			for(int i = 0; i < cfgArrayString.length; ++i)
				dataString += "||" + cfgArrayString[i];
			
			DummyData[] data = DataStorage.parseData(dataString);
			
			maxEnergy = Float.parseFloat(data[0].fieldValue);
			
			cfg.save();
		}
		catch(Exception e) {
			return;
		}
	}
	
	
	@Override
	public int getSizeInventory() {
		return items.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int par1) {
		return items[par1];
	}
	
	@Override
	public ItemStack decrStackSize(int par1, int par2) {
		if(items[par1] != null) {
			ItemStack itemstack;
			
			if(items[par1].stackSize <= par2) {
				itemstack = items[par1];
				items[par1] = null;
				return itemstack;
			}
			else {
				itemstack = items[par1].splitStack(par2);
				
				if(items[par1].stackSize == 0)
					items[par1] = null;
				
				return itemstack;
			}
		}
		else
			return null;
	}
	
	@Override
	public ItemStack removeStackFromSlot(int par1) {
		if(items[par1] != null) {
			ItemStack itemstack = items[par1];
			items[par1] = null;
			return itemstack;
		}
		else
			return null;
	}
	
	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
		items[par1] = par2ItemStack;
		
		if(par2ItemStack != null && par2ItemStack.stackSize > getInventoryStackLimit()) {
			par2ItemStack.stackSize = getInventoryStackLimit();
		}
	}
	
	@Override
	public String getName() {
		return "ec3.container.mithrilineFurnace";
	}
	
	@Override
	public boolean hasCustomName() {
		return false;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return getWorld().getTileEntity(pos) == this && player.dimension == getWorld().provider.getDimension();
	}
	
	@Override
	public void openInventory(EntityPlayer p) {}
	
	@Override
	public void closeInventory(EntityPlayer p) {}
	
	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return p_94041_1_ == 0;
	}
	
	@Override
	public int[] getSlotsForFace(EnumFacing p_94128_1_) {
		return new int[] {0, 1};
	}
	
	@Override
	public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, EnumFacing p_102007_3_) {
		return isItemValidForSlot(p_102007_1_, p_102007_2_);
	}
	
	@Override
	public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, EnumFacing p_102008_3_) {
		return p_102008_1_ == 1;
	}
	
	@Override
	public void clear() {
	    for(int i = 0; i < getSizeInventory(); i++)
	        setInventorySlotContents(i, null);
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {}

	@Override
	public int getFieldCount() {
		return 0;
	}

	public IItemHandler itemHandler = new SidedInvWrapper(this, EnumFacing.DOWN);

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)itemHandler : super.getCapability(capability, facing);
	}
}
