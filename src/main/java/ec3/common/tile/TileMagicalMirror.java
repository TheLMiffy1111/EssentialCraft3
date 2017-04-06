package ec3.common.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import ec3.api.ApiCore;
import ec3.utils.common.ECUtils;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.config.Configuration;
import DummyCore.Utils.Coord3D;
import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.MathUtils;
import DummyCore.Utils.UnformedItemStack;

public class TileMagicalMirror extends TileMRUGeneric {
	
	public Coord3D inventoryPos;
	public int transferTime = 0;
	
	public static float cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;
	public static float cfgMaxDistance = 8;
	public ItemStack transferingStack;
	
	public boolean pulsing;
	
	public TileMagicalMirror() {
		super();
		setMaxMRU(cfgMaxMRU);
	}
	
	@Override
	public void update() {
		if(getWorld().isRemote) {
			if(pulsing && transferTime < 60)
				++transferTime;
		}
		super.update();
		if(inventoryPos != null) {
			
		}
		TileEntity tile = getWorld().getTileEntity(pos.down());
		if(tile != null && tile instanceof IInventory)
		{
			IInventory assembler = (IInventory)tile;
			if(inventoryPos != null)
			{
				TileEntity tile1 = getWorld().getTileEntity(new BlockPos(MathHelper.floor(inventoryPos.x), MathHelper.floor(inventoryPos.y), MathHelper.floor(inventoryPos.z)));
				if(assembler != null && tile1 != null && tile1 instanceof IInventory)
				{
					IInventory inv = (IInventory) tile1;
					CycleF:
						for(int w = 0; w < assembler.getSizeInventory(); ++w) {
							if(assembler.getStackInSlot(w) != null) {
								ItemStack is = assembler.getStackInSlot(w);
								for(int i = 0; i < inv.getSizeInventory(); ++i) {
									ItemStack is1 = inv.getStackInSlot(i);
									if(is1 == null || (is1.isItemEqual(is) && is1.stackSize + 1 < is1.getMaxStackSize() + 1)) {
										pulsing = true;
										syncTick = 0;
										if(!getWorld().isRemote && transferTime <= 60)
											++transferTime;
										transferingStack = assembler.getStackInSlot(w);
										double sX = pos.getX()+0.5D + MathUtils.randomDouble(getWorld().rand)/6;
										double sY = pos.getY()+0.5D + MathUtils.randomDouble(getWorld().rand)/6;
										double sZ = pos.getZ()+0.5D + MathUtils.randomDouble(getWorld().rand)/6;
										double dX = inventoryPos.x + MathUtils.randomDouble(getWorld().rand)/6;
										double dY = inventoryPos.y + MathUtils.randomDouble(getWorld().rand)/6;
										double dZ = inventoryPos.z + MathUtils.randomDouble(getWorld().rand)/6;
										if(transferTime < 20) {
											dX = sX;
											dY = sY;
											dZ = sZ;
											sY -= 1;
										}
										if(getWorld().getWorldTime()%5 == 0)
										ECUtils.spawnItemFX(sX, sY, sZ, dX, dY, dZ);
										if(transferTime >= 60) {
											ItemStack set = is.copy();
											set.stackSize = 1;
											if(inv.getStackInSlot(i) == null)
												inv.setInventorySlotContents(i, set);
											else
												++inv.getStackInSlot(i).stackSize;
											assembler.decrStackSize(w, 1);
											transferingStack = null;
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
			inventoryPos = new Coord3D(Double.parseDouble(coordData[0].fieldValue),Double.parseDouble(coordData[1].fieldValue),Double.parseDouble(coordData[2].fieldValue));
		}
		else
			inventoryPos = null;
		transferTime = i.getInteger("transferTime");
		pulsing = i.getBoolean("pulse");
		if(i.hasKey("transferingStack")) {
			NBTTagCompound tag = i.getCompoundTag("transferingStack");
			ItemStack is = ItemStack.loadItemStackFromNBT(tag);
			if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
				is.stackSize = 1;
			transferingStack = is;
		}
		else {
			transferingStack = null;
		}
		super.readFromNBT(i);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound i) {
		if(inventoryPos != null)
			i.setString("coord", inventoryPos.toString());
		i.setInteger("transferTime", transferTime);
		i.setBoolean("pulse", pulsing);
		if(transferingStack != null) {
			NBTTagCompound tag = new NBTTagCompound();
			transferingStack.writeToNBT(tag);
			i.setTag("transferingStack", tag);
		}
		return super.writeToNBT(i);
	}
	
	public static void setupConfig(Configuration cfg) {
		try {
			cfg.load();
			String[] cfgArrayString = cfg.getStringList("MagicalMirrorSettings", "tileentities", new String[] {
					"Max MRU:"+ApiCore.DEVICE_MAX_MRU_GENERIC,
					"Max range of item transfering:8.0"
			}, "");
			String dataString = "";
			
			for(int i = 0; i < cfgArrayString.length; ++i)
				dataString += "||" + cfgArrayString[i];
			
			DummyData[] data = DataStorage.parseData(dataString);
			
			cfgMaxMRU = Float.parseFloat(data[0].fieldValue);
			cfgMaxDistance = Float.parseFloat(data[1].fieldValue);
			
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
