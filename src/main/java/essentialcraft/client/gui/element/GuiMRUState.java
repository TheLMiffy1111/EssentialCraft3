package essentialcraft.client.gui.element;

import essentialcraft.api.IMRUDisplay;
import essentialcraft.api.IMRUHandler;
import essentialcraft.common.capabilities.mru.CapabilityMRUHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GuiMRUState extends GuiTextElement {

	public IMRUHandler tile;
	public int mru;

	public GuiMRUState(int i, int j, IMRUHandler t, int mruToSearch) {
		super(i,j);
		tile = t;
	}

	public GuiMRUState(int i, int j, TileEntity t, int mruToSearch) {
		super(i,j);
		if(t.hasCapability(CapabilityMRUHandler.MRU_HANDLER_CAPABILITY, null)) {
			tile = t.getCapability(CapabilityMRUHandler.MRU_HANDLER_CAPABILITY, null);
		}
		else if(t instanceof IMRUDisplay) {
			tile = ((IMRUDisplay)t).getMRUHandler();
		}
		else {
			throw new IllegalArgumentException("Tile does not handle MRU");
		}
	}

	@Override
	public ResourceLocation getElementTexture() {
		return super.getElementTexture();
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
		this.drawText(posX,posY);
	}

	@Override
	public int getX() {
		return super.getX();
	}

	@Override
	public int getY() {
		return super.getY();
	}

	@Override
	public void drawText(int posX, int posY) {
		Minecraft.getMinecraft().fontRenderer.drawString(""+this.tile.getMRU()+"/"+this.tile.getMaxMRU()+" MRU", posX+2, posY+5, 0xffffff, true);
	}

}
