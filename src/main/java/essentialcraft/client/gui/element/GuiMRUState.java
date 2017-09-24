package essentialcraft.client.gui.element;

import essentialcraft.api.IMRUHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GuiMRUState extends GuiTextElement {

	public IMRUHandler tile;
	public int mru;

	public GuiMRUState(int i, int j, IMRUHandler t, int mruToSearch) {
		super(i,j);
		tile = t;
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
		drawText(posX,posY);
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