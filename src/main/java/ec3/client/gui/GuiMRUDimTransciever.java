package ec3.client.gui;

import DummyCore.Client.GuiCommon;
import ec3.api.ITEHasMRU;
import ec3.client.gui.element.GuiBalanceState;
import ec3.client.gui.element.GuiBoundGemStateDimTransciever;
import ec3.client.gui.element.GuiMRUState;
import ec3.client.gui.element.GuiMRUStorage;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

public class GuiMRUDimTransciever extends GuiCommon {

	public GuiMRUDimTransciever(Container c, TileEntity tile) {
		super(c,tile);
		this.elementList.add(new GuiMRUStorage(7, 4, (ITEHasMRU) tile));
		this.elementList.add(new GuiBoundGemStateDimTransciever(88, 4, tile,0));
		this.elementList.add(new GuiBalanceState(46, 31, tile));
		this.elementList.add(new GuiMRUState(25, 58, (ITEHasMRU)tile,0));
	}
}
