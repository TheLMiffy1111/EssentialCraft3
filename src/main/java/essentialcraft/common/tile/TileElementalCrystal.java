package essentialcraft.common.tile;

import java.util.Random;

import DummyCore.Utils.MiscUtils;
import DummyCore.Utils.Notifier;
import DummyCore.Utils.TileStatTracker;
import essentialcraft.common.mod.EssentialCraftCore;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.config.Configuration;

public class TileElementalCrystal extends TileEntity implements ITickable {
	public int syncTick = 10;
	public double size,fire,water,earth,air;
	private TileStatTracker tracker;
	public boolean requestSync = true;

	public static double mutationChance = 0.001D;
	public static double growthModifier = 1.0D;

	public TileElementalCrystal() {
		super();
		tracker = new TileStatTracker(this);
	}

	@Override
	public void readFromNBT(NBTTagCompound i) {
		super.readFromNBT(i);
		size = i.getDouble("size");
		fire = i.getDouble("fire");
		water = i.getDouble("water");
		earth = i.getDouble("earth");
		air = i.getDouble("air");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound i)  {
		super.writeToNBT(i);
		i.setDouble("size", size);
		i.setDouble("fire", fire);
		i.setDouble("water", water);
		i.setDouble("earth", earth);
		i.setDouble("air", air);
		return i;
	}

	public double getElementByNum(int num) {
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

	public void setElementByNum(int num, double amount) {
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
		if(r.nextDouble() <= mutationChance)
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

	@Override
	public void update() {
		int metadata = this.getBlockMetadata();

		if(metadata == 1) {
			IBlockState b = getWorld().getBlockState(pos.down());
			if(!b.isSideSolid(getWorld(), pos.down(), EnumFacing.UP)) {
				getWorld().getBlockState(pos).getBlock().dropBlockAsItem(getWorld(), pos, getWorld().getBlockState(pos), 0);
				getWorld().setBlockToAir(pos);
			}
		}

		if(metadata == 0) {
			IBlockState b = getWorld().getBlockState(pos.up());
			if(!b.isSideSolid(getWorld(), pos.up(), EnumFacing.DOWN)) {
				getWorld().getBlockState(pos).getBlock().dropBlockAsItem(getWorld(), pos, getWorld().getBlockState(pos), 0);
				getWorld().setBlockToAir(pos);
			}
		}

		if(metadata == 3) {
			IBlockState b = getWorld().getBlockState(pos.north());
			if(!b.isSideSolid(getWorld(), pos.north(), EnumFacing.SOUTH)) {
				getWorld().getBlockState(pos).getBlock().dropBlockAsItem(getWorld(), pos, getWorld().getBlockState(pos), 0);
				getWorld().setBlockToAir(pos);
			}
		}

		if(metadata == 2) {
			IBlockState b = getWorld().getBlockState(pos.south());
			if(!b.isSideSolid(getWorld(), pos.south(), EnumFacing.NORTH)) {
				getWorld().getBlockState(pos).getBlock().dropBlockAsItem(getWorld(), pos, getWorld().getBlockState(pos), 0);
				getWorld().setBlockToAir(pos);
			}
		}

		if(metadata == 5) {
			IBlockState b = getWorld().getBlockState(pos.west());
			if(!b.isSideSolid(getWorld(), pos.west(), EnumFacing.EAST)) {
				getWorld().getBlockState(pos).getBlock().dropBlockAsItem(getWorld(), pos, getWorld().getBlockState(pos), 0);
				getWorld().setBlockToAir(pos);
			}
		}

		if(metadata == 4) {
			IBlockState b = getWorld().getBlockState(pos.east());
			if(!b.isSideSolid(getWorld(), pos.east(), EnumFacing.WEST)) {
				getWorld().getBlockState(pos).getBlock().dropBlockAsItem(getWorld(), pos, getWorld().getBlockState(pos), 0);
				getWorld().setBlockToAir(pos);
			}
		}

		if(size < 100) {
			getWorld().spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, pos.getX()+getWorld().rand.nextFloat(),pos.getY()+1,pos.getZ()+getWorld().rand.nextFloat(), 0, 0, 0);
			if(!getWorld().isRemote) {
				size += 0.002D*growthModifier;
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
			String category = "tileentities.elementalcrystal";
			mutationChance = cfg.get(category, "MutationChance", 0.001D, "Chance to mutate per tick").setMinValue(0D).setMaxValue(1D).getDouble();
			growthModifier = cfg.get(category, "GrowthModifier", 1D).setMinValue(0).getDouble();
		}
		catch(Exception e) {
			return;
		}
	}
}
