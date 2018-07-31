package essentialcraft.common.tile;

import java.util.ArrayList;

import essentialcraft.common.inventory.InventoryMagicFilter;
import essentialcraft.common.item.ItemFilter;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class TileMIMExportNode extends TileMRUGeneric {
	static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

	public TileMIMExportNode() {
		super(0);
		setSlotsNum(1);
		slot0IsBoundGem = false;
	}

	public EnumFacing getRotation() {
		int metadata = this.getBlockMetadata();
		metadata %= 6;
		return EnumFacing.getFront(metadata);
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return stack.getItem() instanceof ItemFilter;
	}

	@Override
	public int[] getOutputSlots() {
		return new int[0];
	}

	public IItemHandler getConnectedInventory() {
		EnumFacing side = getRotation();
		if(getWorld().getTileEntity(pos.offset(side)) != null) {
			TileEntity tile = getWorld().getTileEntity(pos.offset(side));
			if(tile.hasCapability(ITEM_HANDLER_CAPABILITY, side.getOpposite()))
				return tile.getCapability(ITEM_HANDLER_CAPABILITY, side.getOpposite());
		}

		return null;
	}

	public IItemHandler getConnectedInventoryNonSided() {
		EnumFacing side = getRotation();
		if(getWorld().getTileEntity(pos.offset(side)) != null) {
			TileEntity tile = getWorld().getTileEntity(pos.offset(side));
			if(tile.hasCapability(ITEM_HANDLER_CAPABILITY, null))
				return tile.getCapability(ITEM_HANDLER_CAPABILITY, null);
		}

		return null;
	}

	public void exportAllPossibleItems(TileMIM parent) {
		if(getWorld().isBlockIndirectlyGettingPowered(pos) > 0)
			return;

		IItemHandler inv = getConnectedInventory();
		if(inv == null) {
			IItemHandler iinv = getConnectedInventoryNonSided();
		}
		ArrayList<ItemStack> itemsToExport = parent.getAllItems();
		int slots = inv.getSlots();

		if(slots <= 0)
			return;

		for(int i = 0; i < itemsToExport.size(); ++i) {
			for(int j = 0; j < slots; ++j) {
				if(inv.insertItem(j, itemsToExport.get(i), true).getCount() < itemsToExport.get(i).getCount()) {
					if(inv.getStackInSlot(j).isEmpty() || ItemHandlerHelper.canItemStacksStack(inv.getStackInSlot(j), itemsToExport.get(i))) {
						if(getStackInSlot(0).isEmpty() || !(getStackInSlot(0).getItem() instanceof ItemFilter)) {
							ItemStack copied = itemsToExport.get(i).copy();
							int original = copied.getCount();
							int remaining = inv.insertItem(j, copied, true).getCount();
							copied.setCount(original-remaining);
							if(parent.retrieveItemStackFromSystem(copied, false, true) == 0)
								inv.insertItem(j, copied, false);
						}
						else {
							ItemStack copied = itemsToExport.get(i).copy();
							if(ECUtils.canFilterAcceptItem(new InventoryMagicFilter(getStackInSlot(0)), copied, getStackInSlot(0))) {
								int original = copied.getCount();
								int remaining = inv.insertItem(j, copied, true).getCount();
								copied.setCount(original-remaining);
								if(parent.retrieveItemStackFromSystem(copied, false, true) == 0)
									inv.insertItem(j, copied, false);
							}
						}
					}
				}
			}
		}
	}
}
