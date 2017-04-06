package ec3.client.gui.element;

import DummyCore.Client.GuiElement;
import DummyCore.Utils.DrawUtils;
import DummyCore.Utils.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

public class GuiFluidTank extends GuiElement{
	private ResourceLocation rec = new ResourceLocation("essentialcraft","textures/gui/mruStorage.png");
	
	public int x;
	public int y;
	public IFluidTank tile;
	
	public GuiFluidTank(int i, int j, IFluidTank t)
	{
		x = i;
		y = j;
		tile = t;
	}

	@Override
	public ResourceLocation getElementTexture() {
		return rec;
	}

	@Override
	public void draw(int posX, int posY, int mouseX, int mouseY) {
		this.drawTexturedModalRect(posX, posY, 0, 0, 18, 54);
		this.drawTexturedModalRect(posX, posY+53, 0, 71, 18, 1);
		if(tile != null)
		{
			FluidStack fStk = tile.getFluid();
			if(fStk != null && fStk.amount > 0)
			{
				TextureAtlasSprite icon = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fStk.getFluid().getFlowing().toString());
				int scale = MathUtils.pixelatedTextureSize(fStk.amount, tile.getCapacity(), 52);
				DrawUtils.drawTexture(posX+1, posY+1+(52-scale), icon, 16, scale, 1);
			}
		}
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

}
