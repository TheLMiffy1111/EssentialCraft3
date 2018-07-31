package essentialcraft.client.gui.element;

import essentialcraft.common.capabilities.mru.CapabilityMRUHandler;
import essentialcraft.common.item.ItemsCore;
import essentialcraft.common.tile.TileWeatherController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class GuiWeatherState extends GuiTextElement {

	public TileWeatherController tile;

	public GuiWeatherState(int i, int j, TileWeatherController t) {
		super(i,j);
		tile = t;
	}

	@Override
	public void draw(int posX, int posY, int mouseX, int mouseY) {
		this.drawTexturedModalRect(posX, posY, 0, 0, 17, 18);
		this.drawTexturedModalRect(posX+17, posY, 1, 0, 16, 18);
		this.drawTexturedModalRect(posX+17+16, posY, 1, 0, 16, 18);
		this.drawTexturedModalRect(posX+17+32, posY, 1, 0, 16, 18);
		this.drawTexturedModalRect(posX+17+48, posY, 1, 0, 16, 18);
		this.drawTexturedModalRect(posX+17+64, posY, 1, 0, 16, 18);
		this.drawTexturedModalRect(posX+17+80, posY, 1, 0, 16, 18);
		this.drawTexturedModalRect(posX+17+96, posY, 1, 0, 16, 18);
		this.drawTexturedModalRect(posX+17+111, posY, 1, 0, 17, 18);
		this.drawText(posX, posY);
	}

	@Override
	public void drawText(int posX, int posY) {
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		if(tile.getStackInSlot(1).isEmpty()) {
			fontRenderer.drawStringWithShadow("No Catalyst!", posX+4, posY+5, 0xFFFF00);
		}
		else if(tile.getStackInSlot(2).getCount() >= tile.getStackInSlot(2).getMaxStackSize()) {
			fontRenderer.drawStringWithShadow("Bottle Storage Full!", posX+4, posY+5, 0xFFFF00);
		}
		else if(tile.getCapability(CapabilityMRUHandler.MRU_HANDLER_CAPABILITY, null).getMRU() < 100) {
			fontRenderer.drawStringWithShadow("No MRU!", posX+4, posY+5, 0xFF0000);
		}
		else if(tile.getStackInSlot(1).getItem() == ItemsCore.clearing_catalyst) {
			if(tile.getWorld().isRaining()) {
				fontRenderer.drawStringWithShadow("Clearing Skies...", posX+4, posY+5, 0x00FF00);
			}
			else {
				fontRenderer.drawStringWithShadow("Already Clear!", posX+4, posY+5, 0xFF0000);
			}
		}
		else if(tile.getStackInSlot(1).getItem() == ItemsCore.raining_catalyst) {
			if(!tile.getWorld().isRaining() || tile.getWorld().isThundering()) {
				fontRenderer.drawStringWithShadow("Making Rain...", posX+4, posY+5, 0x00FF00);
			}
			else {
				fontRenderer.drawStringWithShadow("Already Raining!", posX+4, posY+5, 0xFF0000);
			}
		}
		else if(tile.getStackInSlot(1).getItem() == ItemsCore.thundering_catalyst) {
			if(!tile.getWorld().isThundering()) {
				fontRenderer.drawStringWithShadow("Creating Thunder...", posX+4, posY+5, 0x00FF00);
			}
			else {
				fontRenderer.drawStringWithShadow("Already Thundering!", posX+4, posY+5, 0xFF0000);
			}
		}
		else {
			fontRenderer.drawStringWithShadow("Invalid Item!", posX+4, posY+5, 0xFF0000);
		}
	}
}
