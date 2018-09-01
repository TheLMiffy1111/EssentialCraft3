package essentialcraft.common.tile;

import java.lang.ref.WeakReference;
import java.util.List;

import DummyCore.Utils.MathUtils;
import essentialcraft.api.ApiCore;
import essentialcraft.api.IMRUHandlerItem;
import essentialcraft.common.block.BlockRightClicker;
import essentialcraft.common.capabilities.mru.CapabilityMRUHandler;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBlockSpecial;
import net.minecraft.item.ItemRedstone;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class TileRightClicker extends TileMRUGeneric {

	public static int cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;

	public boolean wasPowered = false;
	public boolean firstTick = true;
	public int rotation = 0;
	public FakePlayer fakePlayer;
	public IBlockState mimickedBlock = null;
	public ItemStack prevMimic = ItemStack.EMPTY;

	public EnumFacing getRotation() {
		return EnumFacing.getFront(rotation);
	}

	public void finishClick(int slot, boolean setupAll) {
		int cycle = setupAll ? 9 : 1;
		for(int i = 0; i < cycle; ++i) {
			if(!fakePlayer.getHeldItemMainhand().isEmpty()) {
				ItemStack setted = fakePlayer.inventory.getCurrentItem().copy();
				if(setted.hasCapability(CapabilityMRUHandler.MRU_HANDLER_ITEM_CAPABILITY, null)) {
					IMRUHandlerItem iReq = setted.getCapability(CapabilityMRUHandler.MRU_HANDLER_ITEM_CAPABILITY, null);
					int current = iReq.getMRU();
					int thM = mruStorage.getMRU();
					int thMM = mruStorage.getMaxMRU();
					if(thM < thMM) {
						int diff = thM - thMM;
						if(diff <= current) {
							iReq.extractMRU(diff, true);
							mruStorage.addMRU(diff, true);
						}
						else {
							iReq.extractMRU(current, true);
							mruStorage.addMRU(current, true);
						}
					}
				}
				setInventorySlotContents(1+i, setted);
				if(setted.getCount() <= 0)
					setInventorySlotContents(1+i, ItemStack.EMPTY);
			}
			else
				setInventorySlotContents(1+i, ItemStack.EMPTY);

			fakePlayer.inventory.setInventorySlotContents(fakePlayer.inventory.currentItem, ItemStack.EMPTY);
			fakePlayer.inventory.currentItem += 1;
		}
		fakePlayer = null;
	}

	public boolean canAct() {
		return true;
	}

	public void setupFake(int slot, boolean setupAll) {
		EnumFacing r = getRotation();
		fakePlayer = new WeakReference<FakePlayer>(FakePlayerFactory.get((WorldServer)getWorld(), ECUtils.EC3FakePlayerProfile)).get();
		fakePlayer.inventory.currentItem = 0;
		if(setupAll) {
			for(int i = 0; i < 9; ++i) {
				fakePlayer.inventory.setInventorySlotContents(fakePlayer.inventory.currentItem+i, getStackInSlot(1+i).isEmpty() ? ItemStack.EMPTY : getStackInSlot(1+i).copy());
				if(i + 1 != slot)
					setInventorySlotContents(i+1, ItemStack.EMPTY);
			}
		}

		if(!getStackInSlot(slot).isEmpty()) {
			ItemStack setted = getStackInSlot(slot).copy();
			if(setted.hasCapability(CapabilityMRUHandler.MRU_HANDLER_ITEM_CAPABILITY, null)) {
				IMRUHandlerItem iReq = setted.getCapability(CapabilityMRUHandler.MRU_HANDLER_ITEM_CAPABILITY, null);
				int current = iReq.getMRU();
				int max = iReq.getMaxMRU();
				int thM = mruStorage.getMRU();
				if(current < max) {
					int diff = current - max;
					if(diff <= thM) {
						iReq.addMRU(diff, true);
						mruStorage.extractMRU(diff, true);
					}
					else {
						iReq.addMRU(thM, true);
						mruStorage.extractMRU(thM, true);
					}
				}
			}
			fakePlayer.inventory.setInventorySlotContents(fakePlayer.inventory.currentItem, setted);
			setInventorySlotContents(slot, ItemStack.EMPTY);
			float rotation = 0F;

			if(rotation == 2)
				rotation = 0;
			if(rotation == 3)
				rotation = 90;
			if(rotation == 4)
				rotation = 180;
			if(rotation == 5)
				rotation = 270;
			fakePlayer.setPositionAndRotation(pos.getX()+0.5D+r.getFrontOffsetX(), pos.getY()+0.5D+r.getFrontOffsetY(), pos.getZ()+0.5D+r.getFrontOffsetZ(), rotation, rotation == 0 ? -90 : rotation == 1 ? 90 : 0);
		}
	}

	public boolean rightClick(boolean sneak) {
		EnumFacing faceDir = getRotation();
		int dx = faceDir.getFrontOffsetX();
		int dy = faceDir.getFrontOffsetY();
		int dz = faceDir.getFrontOffsetZ();
		int x = pos.getX() + dx;
		int y = pos.getY() + dy;
		int z = pos.getZ() + dz;
		BlockPos p = pos.offset(faceDir);
		fakePlayer.setPosition(x + 0.5, y + 0.5 - fakePlayer.eyeHeight, z + 0.5);
		fakePlayer.rotationPitch = faceDir.getFrontOffsetY() * -90;
		fakePlayer.setSneaking(sneak);

		switch(faceDir) {
		case NORTH:
			fakePlayer.rotationYaw = 180;
			break;
		case SOUTH:
			fakePlayer.rotationYaw = 0;
			break;
		case WEST:
			fakePlayer.rotationYaw = 90;
			break;
		case EAST:
			fakePlayer.rotationYaw = -90;
		default:
			break;
		}

		try {
			PlayerInteractEvent event = new PlayerInteractEvent.RightClickEmpty(fakePlayer, EnumHand.MAIN_HAND);
			MinecraftForge.EVENT_BUS.post(event);
			if(event.isCanceled())
				return false;

			Block block = getWorld().getBlockState(p).getBlock();
			List<EntityLivingBase> detectedEntities = getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(p));

			Entity entity = detectedEntities.isEmpty() ? null : detectedEntities.get(getWorld().rand.nextInt(detectedEntities.size()));

			ItemStack stack = fakePlayer.getHeldItemMainhand();
			if(stack.isEmpty())
				stack = new ItemStack(Items.STICK,1,0);
			if(stack.getItem().itemInteractionForEntity(stack, fakePlayer, (EntityLivingBase)entity, EnumHand.MAIN_HAND))
				return true;
			if(entity instanceof EntityAnimal && ((EntityAnimal)entity).processInteract(fakePlayer, EnumHand.MAIN_HAND))
				return true;
			if(stack.getItem().onItemUseFirst(fakePlayer, getWorld(), p, faceDir, dx, dy, dz, EnumHand.MAIN_HAND) == EnumActionResult.SUCCESS)
				return true;
			if(!getWorld().isAirBlock(p) && block.onBlockActivated(getWorld(), p, getWorld().getBlockState(p), fakePlayer, EnumHand.MAIN_HAND, faceDir, dx, dy, dz))
				return true;

			boolean isGoingToShift = false;
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof ItemBlockSpecial || stack.getItem() instanceof ItemRedstone)
					isGoingToShift = true;
				int useX = isGoingToShift ? pos.getX() : x;
				int useY = isGoingToShift ? pos.getY() : y;
				int useZ = isGoingToShift ? pos.getZ() : z;
				if(stack.getItem().onItemUse(fakePlayer, getWorld(), new BlockPos(useX, useY, useZ), EnumHand.MAIN_HAND, faceDir, dx, dy, dz) == EnumActionResult.SUCCESS)
					return true;
			}

			ItemStack copy = stack.copy();
			fakePlayer.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack.getItem().onItemRightClick(getWorld(), fakePlayer, EnumHand.MAIN_HAND).getResult());
			if(!copy.isItemEqual(stack))
				return true;

			return false;
		}
		catch (Throwable e) {
			return true;
		}
	}

	public TileRightClicker() {
		super(cfgMaxMRU);
		setSlotsNum(11);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		rotation = nbt.getInteger("rotation");
		wasPowered = nbt.getBoolean("powered");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("rotation", rotation);
		nbt.setBoolean("powered", wasPowered);
		return nbt;
	}

	@Override
	public void update() {
		super.update();

		if(firstTick) {
			getWorld().markBlockRangeForRenderUpdate(pos.add(-1, -1, -1), pos.add(1, 1, 1));
			firstTick = false;
		}

		mruStorage.update(getPos(), getWorld(), getStackInSlot(0));

		if(getWorld().isBlockIndirectlyGettingPowered(pos) > 0 && !wasPowered && canAct()) {
			if(getBlockMetadata() <= 1) {
				if(!getWorld().isRemote) {
					setupFake(1, false);
					rightClick(getBlockMetadata()%2 == 1);
					finishClick(1, false);
				}
			}
			if(getBlockMetadata() == 2 || getBlockMetadata() == 3) {
				if(!getWorld().isRemote) {
					for(int i = 0; i < 9; ++i) {
						setupFake(1 + i, false);
						rightClick(getBlockMetadata()%2 == 1);
						finishClick(1 + i, false);
					}
				}
			}
			if(getBlockMetadata() == 4 || getBlockMetadata() == 5) {
				if(!getWorld().isRemote) {
					for(int i = 0; i < 9; ++i) {
						setupFake(1 + i, true);
						rightClick(getBlockMetadata()%2 == 1);
						finishClick(1 + i, true);
					}
				}
			}
			wasPowered = true;
		}
		if(wasPowered && getWorld().isBlockIndirectlyGettingPowered(pos) == 0)
			wasPowered = false;

		if(wasPowered)
			getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+0.5D + MathUtils.randomDouble(getWorld().rand)/1.5D, pos.getY()+0.5D + MathUtils.randomDouble(getWorld().rand)/1.5D, pos.getZ()+0.5D + MathUtils.randomDouble(getWorld().rand)/1.5D, 1, 0, 0);

		manageMimic();
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		if(pkt.getTileEntityType() == -10) {
			readFromNBT(pkt.getNbtCompound());
		}
	}

	@Override
	public int[] getOutputSlots() {
		return getBlockMetadata() <= 1 ? new int[] {1} : new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9};
	}

	@Override
	public int[] getSlotsForFace(EnumFacing facing) {
		return getBlockMetadata() <= 1 ? new int[] {0, 1, 10} : new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
	}

	public IBlockState getState() {
		return mimickedBlock;
	}

	public void manageMimic() {
		ItemStack stk = getStackInSlot(10);

		if(prevMimic == stk)
			return;

		if(!stk.isEmpty() && stk.getItem() instanceof ItemBlock && !(Block.getBlockFromItem(stk.getItem()) instanceof BlockRightClicker)) {
			IBlockState state = Block.getBlockFromItem(stk.getItem()).getStateFromMeta(stk.getItemDamage());
			if(isValidBlock(state)) {
				mimickedBlock = state;
			}
			else
				mimickedBlock = null;
		}
		else
			mimickedBlock = null;

		prevMimic = stk;
		getWorld().markBlockRangeForRenderUpdate(pos.add(-1, -1, -1), pos.add(1, 1, 1));
	}

	public static boolean isValidBlock(IBlockState state) {
		return state.getRenderType() == EnumBlockRenderType.MODEL && state.getMaterial() != Material.AIR;
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.magicalactivator";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.DEVICE_MAX_MRU_GENERIC).setMinValue(1).getInt();
		}
		catch(Exception e) {
			return;
		}
	}
}
