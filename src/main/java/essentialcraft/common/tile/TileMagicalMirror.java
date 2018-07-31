package essentialcraft.common.tile;

import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.MathUtils;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileMagicalMirror extends TileMRUGeneric {
	static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

	public BlockPos inventoryPos;
	public int transferTime = 0;

	public static double cfgMaxDistance = 8;
	public ItemStack transferingStack = ItemStack.EMPTY;

	public boolean pulsing;

	public TileMagicalMirror() {
		super(0);
	}

	@Override
	public void update() {
		super.update();
		TileEntity tile = getWorld().getTileEntity(pos.down());
		if(tile != null && tile.hasCapability(ITEM_HANDLER_CAPABILITY, EnumFacing.UP)) {
			IItemHandler invBelow = tile.getCapability(ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
			if(inventoryPos != null) {
				TileEntity tile1 = getWorld().getTileEntity(inventoryPos);
				if(invBelow != null && tile1 != null && tile1.hasCapability(ITEM_HANDLER_CAPABILITY, null)) {
					IItemHandler inv = tile1.getCapability(ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
					CycleF:
						for(int w = 0; w < invBelow.getSlots(); ++w) {
							if(!invBelow.getStackInSlot(w).isEmpty()) {
								ItemStack is = invBelow.getStackInSlot(w);
								for(int i = 0; i < inv.getSlots(); ++i) {
									ItemStack is1 = inv.getStackInSlot(i);
									if(is1.isEmpty() || is1.isItemEqual(is) && is1.getCount() + 1 <= is1.getMaxStackSize()) {
										pulsing = true;
										syncTick = 0;
										++transferTime;
										transferingStack = invBelow.getStackInSlot(w);
										if(getWorld().isRemote) {
											double sX = pos.getX()+0.5D + MathUtils.randomDouble(getWorld().rand)/6;
											double sY = pos.getY()+0.5D + MathUtils.randomDouble(getWorld().rand)/6;
											double sZ = pos.getZ()+0.5D + MathUtils.randomDouble(getWorld().rand)/6;
											double dX = inventoryPos.getX() + MathUtils.randomDouble(getWorld().rand)/6;
											double dY = inventoryPos.getY() + MathUtils.randomDouble(getWorld().rand)/6;
											double dZ = inventoryPos.getZ() + MathUtils.randomDouble(getWorld().rand)/6;
											if(transferTime < 20) {
												dX = sX;
												dY = sY;
												dZ = sZ;
												sY -= 1;
											}
											if(getWorld().getWorldTime()%5 == 0)
												ECUtils.spawnItemFX(sX, sY, sZ, dX, dY, dZ);
										}
										if(transferTime >= 60) {
											ItemStack set = invBelow.extractItem(w, 1, false);
											inv.insertItem(i, set, false);
											transferingStack = ItemStack.EMPTY;
											transferTime = 0;
											pulsing = false;
										}
										break CycleF;
									}
								}
							}
						}
				}
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound i) {
		if(i.hasKey("coord")) {
			DummyData[] coordData = DataStorage.parseData(i.getString("coord"));
			inventoryPos = new BlockPos(Integer.parseInt(coordData[0].fieldValue), Integer.parseInt(coordData[1].fieldValue), Integer.parseInt(coordData[2].fieldValue));
		}
		else
			inventoryPos = null;
		transferTime = i.getInteger("transferTime");
		pulsing = i.getBoolean("pulse");
		if(i.hasKey("transferingStack")) {
			NBTTagCompound tag = i.getCompoundTag("transferingStack");
			ItemStack is = new ItemStack(tag);
			if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
				is.setCount(1);
			transferingStack = is;
		}
		else {
			transferingStack = ItemStack.EMPTY;
		}
		super.readFromNBT(i);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound i) {
		if(inventoryPos != null) {
			i.setString("coord", "||x:" + inventoryPos.getX() + "||y:" + inventoryPos.getY() + "||z:" + inventoryPos.getZ());
		}
		i.setInteger("transferTime", transferTime);
		i.setBoolean("pulse", pulsing);
		if(!transferingStack.isEmpty()) {
			NBTTagCompound tag = new NBTTagCompound();
			transferingStack.writeToNBT(tag);
			i.setTag("transferingStack", tag);
		}
		return super.writeToNBT(i);
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.magicalmirror";
			cfgMaxDistance = cfg.get(category, "Radius", 8D).setMinValue(0D).getDouble();
		}
		catch(Exception e) {
			return;
		}
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}

	@Override
	public int[] getOutputSlots() {
		return new int[0];
	}
}
