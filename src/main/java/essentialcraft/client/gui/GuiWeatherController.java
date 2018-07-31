package essentialcraft.client.gui;

import DummyCore.Client.GuiCommon;
import essentialcraft.client.gui.element.GuiBalanceState;
import essentialcraft.client.gui.element.GuiBoundGemState;
import essentialcraft.client.gui.element.GuiMRUState;
import essentialcraft.client.gui.element.GuiMRUStorage;
import essentialcraft.client.gui.element.GuiWeatherState;
import essentialcraft.common.tile.TileWeatherController;
import net.minecraft.inventory.Container;

public class GuiWeatherController extends GuiCommon {

	public GuiWeatherController(Container c, TileWeatherController tile) {
		super(c,tile);
		this.elementList.add(new GuiMRUStorage(7, 4, tile));
		this.elementList.add(new GuiBalanceState(25, 4, tile));
		this.elementList.add(new GuiBoundGemState(25, 22, tile, 0));
		this.elementList.add(new GuiWeatherState(25, 40, tile));
		this.elementList.add(new GuiMRUState(25, 58, tile, 0));
	}
}
