package essentialcraft.client.gui;

import DummyCore.Client.GuiCommon;
import essentialcraft.client.gui.element.GuiBalanceState;
import essentialcraft.client.gui.element.GuiBoundGemState;
import essentialcraft.client.gui.element.GuiEnchantmentState;
import essentialcraft.client.gui.element.GuiMRUState;
import essentialcraft.client.gui.element.GuiMRUStorage;
import essentialcraft.common.tile.TileMagicalEnchanter;
import net.minecraft.inventory.Container;

public class GuiMagicalEnchanter extends GuiCommon{

	public GuiMagicalEnchanter(Container c, TileMagicalEnchanter tile) {
		super(c,tile);
		this.elementList.add(new GuiMRUStorage(7, 4, tile));
		this.elementList.add(new GuiBalanceState(25, 4, tile));
		this.elementList.add(new GuiBoundGemState(90, 4, tile, 0));
		this.elementList.add(new GuiMRUState(25, 58, tile, 0));
		this.elementList.add(new GuiEnchantmentState(25, 40, tile));
	}
}
