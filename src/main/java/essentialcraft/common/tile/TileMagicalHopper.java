package essentialcraft.common.tile;

import java.util.List;

import essentialcraft.common.inventory.InventoryMagicFilter;
import essentialcraft.common.item.ItemFilter;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.config.Configuration;

public class TileMagicalHopper extends TileMRUGeneric {
	public static int itemHopRadius = 3;
	public static int itemDelay = 20;

	public int delay = 0;

	public EnumFacing getRotation() {
		int metadata = this.getBlockMetadata();
		metadata %= 6;
		return EnumFacing.getFront(metadata);
	}

	public TileMagicalHopper() {
		super(0);
		setSlotsNum(1);
		slot0IsBoundGem = false;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		return super.writeToNBT(nbt);
	}

	@Override
	public void update() {
		super.update();
		if(delay <= 0 && getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			EnumFacing r = getRotation();
			AxisAlignedBB teleportBB = new AxisAlignedBB(pos.offset(r));
			delay = itemDelay;
			List<EntityItem> items = getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos).grow(itemHopRadius, itemHopRadius, itemHopRadius));
			List<EntityItem> doNotTouch = getWorld().getEntitiesWithinAABB(EntityItem.class, teleportBB);

			for(int i = 0; i < items.size(); ++i) {
				EntityItem item = items.get(i);
				if(canTeleport(item) && !doNotTouch.contains(item))
					item.setPositionAndRotation(pos.getX()+0.5D+r.getFrontOffsetX(), pos.getY()+0.5D+r.getFrontOffsetY(), pos.getZ()+0.5D+r.getFrontOffsetZ(), 0, 0);
			}
		}
		else {
			--delay;
		}
	}

	public boolean canTeleport(EntityItem item) {
		if(item.getItem().isEmpty())
			return false;

		if(getStackInSlot(0).isEmpty() || !(getStackInSlot(0).getItem() instanceof ItemFilter))
			return true;

		return ECUtils.canFilterAcceptItem(new InventoryMagicFilter(getStackInSlot(0)), item.getItem(), getStackInSlot(0));
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.magicalhopper";
			itemHopRadius = cfg.get(category, "Radius", 3).setMinValue(0).getInt();
			itemDelay = cfg.get(category, "Delay", 20).setMinValue(1).getInt();
		}
		catch(Exception e) {
			return;
		}
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return stack.getItem() instanceof ItemFilter;
	}

	@Override
	public int[] getOutputSlots() {
		return new int[0];
	}
}
