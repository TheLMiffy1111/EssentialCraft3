package essentialcraft.common.inventory;

import DummyCore.Utils.ContainerInventory;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;

public class ContainerWeatherController extends ContainerInventory {

	public ContainerWeatherController(InventoryPlayer invPlayer, TileEntity tile) {
		super(invPlayer, tile);
	}

	@Override
	public void setupSlots() {
		addSlotToContainer(new SlotBoundEssence(inv, 0, 108, 23));
		addSlotToContainer(new SlotGeneric(inv, 1, 135, 23));
		addSlotToContainer(new SlotGeneric(inv, 2, 153, 23));
		this.setupPlayerInventory();
	}
}
