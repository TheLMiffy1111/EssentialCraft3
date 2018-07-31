package essentialcraft.client.gui.element;

import essentialcraft.common.capabilities.mru.CapabilityMRUHandler;
import essentialcraft.common.tile.TileMagicalEnchanter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

public class GuiEnchantmentState extends GuiTextElement {

	public TileMagicalEnchanter tile;

	public GuiEnchantmentState(int i, int j, TileMagicalEnchanter t) {
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
		this.drawTexturedModalRect(posX+17+80, posY, 1, 0, 17, 18);
		this.drawTexturedModalRect(posX+17+97, posY, 0, 0, 17, 18);
		this.drawTexturedModalRect(posX+17+111, posY, 1, 0, 17, 18);
		this.drawText(posX, posY);
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
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		if(tile.getStackInSlot(1).isEmpty()) {
			fontRenderer.drawStringWithShadow("Nothing To Enchant!", posX+4, posY+5, 0xFFFF00);
		}
		else if(!tile.getStackInSlot(2).isEmpty()) {
			fontRenderer.drawStringWithShadow("Output Must Be Empty!", posX+4, posY+5, 0xFF0000);
		}
		else if(tile.getCapability(CapabilityMRUHandler.MRU_HANDLER_CAPABILITY, null).getMRU() < 100) {
			fontRenderer.drawStringWithShadow("No MRU!", posX+4, posY+5, 0xFF0000);
		}
		else if(!tile.getStackInSlot(1).isItemEnchantable()) {
			fontRenderer.drawStringWithShadow("Item Can't Be Enchanted!", posX+4, posY+5, 0xFF0000);
		}
		else {
			try {
				if(tile.getEnchantmentsForStack(tile.getStackInSlot(1)) == null || tile.getEnchantmentsForStack(tile.getStackInSlot(1)).isEmpty()) {
					fontRenderer.drawStringWithShadow("Item Can't Be Enchanted!", posX+4, posY+5, 0xFF0000);
				}
				else if(tile.getMaxPower() <= 0) {
					fontRenderer.drawStringWithShadow("No Bookshelves!", posX+4, posY+5, 0xFF0000);
				}
				else {
					fontRenderer.drawStringWithShadow("Enchanting Item...", posX+4, posY+5, 0x00FF00);
				}
			}
			catch(Exception e) {
				fontRenderer.drawStringWithShadow("Item Can't Be Enchanted!", posX+4, posY+5, 0xFF0000);
				return;
			}
		}
		fontRenderer.drawStringWithShadow(tile.getMaxPower()+"", posX+122, posY+5, 0xFFFFFF);
	}
}
