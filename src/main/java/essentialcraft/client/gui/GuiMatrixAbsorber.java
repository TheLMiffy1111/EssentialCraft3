package essentialcraft.client.gui;

import DummyCore.Client.GuiCommon;
import essentialcraft.api.IMRUHandler;
import essentialcraft.client.gui.element.GuiBalanceState;
import essentialcraft.client.gui.element.GuiMRUGenerated;
import essentialcraft.client.gui.element.GuiMRUState;
import essentialcraft.client.gui.element.GuiMRUStorage;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

public class GuiMatrixAbsorber extends GuiCommon{

	public GuiMatrixAbsorber(Container c, TileEntity tile) {
		super(c,tile);
		this.elementList.add(new GuiMRUStorage(7, 4, (IMRUHandler) tile));
		this.elementList.add(new GuiMRUState(25, 58, (IMRUHandler) tile, 0));
		this.elementList.add(new GuiBalanceState(25, 22, tile));
		this.elementList.add(new GuiMRUGenerated(25, 40,tile,"matrixAbsorber"));
	}



}