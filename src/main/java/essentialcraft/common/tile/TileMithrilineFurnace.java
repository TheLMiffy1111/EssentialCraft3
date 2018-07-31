package essentialcraft.common.tile;

import DummyCore.Utils.MathUtils;
import DummyCore.Utils.MiscUtils;
import DummyCore.Utils.Notifier;
import DummyCore.Utils.TileStatTracker;
import essentialcraft.api.IESPEHandler;
import essentialcraft.api.MithrilineFurnaceRecipe;
import essentialcraft.api.MithrilineFurnaceRecipes;
import essentialcraft.common.block.BlockMithrilineCrystal;
import essentialcraft.common.block.BlocksCore;
import essentialcraft.common.capabilities.espe.CapabilityESPEHandler;
import essentialcraft.common.capabilities.espe.ESPEStorage;
import essentialcraft.common.mod.EssentialCraftCore;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public class TileMithrilineFurnace extends TileEntity implements ISidedInventory, ITickable {

	public static double maxEnergy = 10000D;
	public double progress;
	public double reqProgress;
	private TileStatTracker tracker;
	public int syncTick = 10;
	public ItemStack[] items = {ItemStack.EMPTY, ItemStack.EMPTY};
	public boolean requestSync = true;
	protected ESPEStorage espeStorage = new ESPEStorage(maxEnergy, 0);
	protected static Vec3i[] possiblePowerSources = {
			new Vec3i(0, 1, 0),
			new Vec3i(1, 0, 1), new Vec3i(-1, 0, 1), new Vec3i(1, 0, -1), new Vec3i(-1, 0, -1),
			new Vec3i(2, 1, 0), new Vec3i(-2, 1, 0), new Vec3i(0, 1, 2), new Vec3i(0, 1, -2),
			new Vec3i(2, 2, 2), new Vec3i(-2, 2, 2), new Vec3i(2, 2, -2), new Vec3i(-2, 2, -2)
	};

	public TileMithrilineFurnace() {
		super();
		tracker = new TileStatTracker(this);
	}

	@Override
	public void readFromNBT(NBTTagCompound i) {
		super.readFromNBT(i);
		MiscUtils.loadInventory(this, i);
		espeStorage.readFromNBT(i);
		progress = i.getDouble("progress");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound i) {
		super.writeToNBT(i);
		MiscUtils.saveInventory(this, i);
		espeStorage.writeToNBT(i);
		i.setDouble("progress", progress);
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
			if(espeStorage.getESPE() < maxEnergy)
				for(int i = 0; i < possiblePowerSources.length; ++i) {
					Vec3i c = possiblePowerSources[i];
					Block b = getWorld().getBlockState(pos.add(c)).getBlock();
					if(b instanceof BlockMithrilineCrystal) {
						TileEntity c_tile = getWorld().getTileEntity(pos.add(c));
						if(c_tile != null && c_tile.hasCapability(CapabilityESPEHandler.ESPE_HANDLER_CAPABILITY, null)) {
							IESPEHandler otherStorage = c_tile.getCapability(CapabilityESPEHandler.ESPE_HANDLER_CAPABILITY, null);
							double req = espeStorage.getMaxESPE() - espeStorage.getESPE();
							double extracted = otherStorage.extractESPE(req, true);
							espeStorage.addESPE(extracted, true);

							if(getWorld().isRemote && c_tile instanceof TileMithrilineCrystal) {
								int movement = (int)(getWorld().getWorldTime() % 60);
								if(movement > 30)
									movement = 60 - movement;
								getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+c.getX()+0.5F, pos.getY()+c.getY()+movement/30D, pos.getZ()+c.getZ()+0.5F, -1, 1, 0);
								getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+c.getX()+0.5F, pos.getY()+c.getY()+2+movement/30D, pos.getZ()+c.getZ()+0.5F, -1, 1, 0);
							}
						}
					}
					if(espeStorage.getESPE() >= maxEnergy) {
						break;
					}
				}

			if(!getStackInSlot(0).isEmpty()) {
				MithrilineFurnaceRecipe rec = MithrilineFurnaceRecipes.findRecipeByComponent(getStackInSlot(0));
				if(rec != null && getStackInSlot(0).getCount() >= rec.requiredRecipeSize) {
					reqProgress = rec.enderStarPulsesRequired;
					if(getStackInSlot(1).isEmpty() || getStackInSlot(1).isItemEqual(rec.result) && getStackInSlot(1).getCount()+rec.result.getCount() <= getStackInSlot(1).getMaxStackSize()) {
						double req = reqProgress - progress;
						progress += espeStorage.extractESPE(req, true);
						if(progress >= reqProgress) {
							decrStackSize(0, rec.requiredRecipeSize);
							if(getStackInSlot(1).isEmpty())
								setInventorySlotContents(1, rec.result.copy());
							else
								getStackInSlot(1).grow(rec.result.getCount());

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

			if(getWorld().isRemote) {
				for(int i = 0; i < 10; ++i)
					EssentialCraftCore.proxy.FlameFX(pos.getX()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.4F, pos.getY()+0.2F + MathUtils.randomFloat(getWorld().rand)*0.6F, pos.getZ()+0.5F + MathUtils.randomFloat(getWorld().rand)*0.4F, 0, 0.01F, 0, 0D, 1D, 0F, 1F);
			}
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
			String category = "tileentities.mithrilinefurnace";
			maxEnergy = cfg.get(category, "MaxESPE", 10000D).setMinValue(Double.MIN_NORMAL).getDouble();
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
		if(!items[par1].isEmpty()) {
			ItemStack itemstack;

			if(items[par1].getCount() <= par2) {
				itemstack = items[par1];
				items[par1] = ItemStack.EMPTY;
				return itemstack;
			}
			else {
				itemstack = items[par1].splitStack(par2);

				if(items[par1].getCount() == 0)
					items[par1] = ItemStack.EMPTY;

				return itemstack;
			}
		}
		else
			return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStackFromSlot(int par1) {
		if(!items[par1].isEmpty()) {
			ItemStack itemstack = items[par1];
			items[par1] = ItemStack.EMPTY;
			return itemstack;
		}
		else
			return ItemStack.EMPTY;
	}

	@Override
	public void setInventorySlotContents(int par1, ItemStack stack) {
		items[par1] = stack;

		if(!stack.isEmpty() && stack.getCount() > getInventoryStackLimit()) {
			stack.setCount(getInventoryStackLimit());
		}
	}

	@Override
	public String getName() {
		return "essentialcraft.container.mithrilineFurnace";
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
		return getWorld().getTileEntity(getPos()) == this && player.dimension == getWorld().provider.getDimension() && getPos().distanceSqToCenter(player.posX, player.posY, player.posZ) <= 64D;
	}

	@Override
	public void openInventory(EntityPlayer p) {}

	@Override
	public void closeInventory(EntityPlayer p) {}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot == 0;
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
			setInventorySlotContents(i, ItemStack.EMPTY);
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
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityESPEHandler.ESPE_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)itemHandler :
			capability == CapabilityESPEHandler.ESPE_HANDLER_CAPABILITY ? (T)espeStorage :
				super.getCapability(capability, facing);
	}

	@Override
	public boolean isEmpty() {
		boolean ret = true;
		for(ItemStack stk : items) {
			ret &= stk.isEmpty();
		}
		return ret;
	}
}
