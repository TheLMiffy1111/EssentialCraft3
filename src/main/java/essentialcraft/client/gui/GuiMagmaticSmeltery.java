package essentialcraft.client.gui;

import DummyCore.Client.GuiCommon;
import essentialcraft.client.gui.element.GuiBalanceState;
import essentialcraft.client.gui.element.GuiBoundGemState;
import essentialcraft.client.gui.element.GuiFluidTank;
import essentialcraft.client.gui.element.GuiMRUState;
import essentialcraft.client.gui.element.GuiMRUStorage;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

public class GuiMagmaticSmeltery extends GuiCommon{

	public GuiMagmaticSmeltery(Container c, TileEntity tile) {
		super(c,tile);
		this.elementList.add(new GuiMRUStorage(7, 4, tile));
		this.elementList.add(new GuiBalanceState(43, 40, tile));
		this.elementList.add(new GuiBoundGemState(43, 4, tile, 0));
		this.elementList.add(new GuiMRUState(25, 58, tile, 0));
		this.elementList.add(new GuiFluidTank(25, 4, tile));
	}
}
