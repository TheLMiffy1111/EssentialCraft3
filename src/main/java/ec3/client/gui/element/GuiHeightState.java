package ec3.client.gui.element;

import java.util.Random;

import DummyCore.Client.TextureUtils;
import DummyCore.Utils.DrawUtils;
import DummyCore.Utils.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GuiHeightState extends GuiTextField{
	
	public TileEntity tile;
	public int mru;
	
	public GuiHeightState(int i, int j, TileEntity t)
	{
		super(i,j);
		tile = t;
	}

	@Override
	public ResourceLocation getElementTexture() {
		// TODO Auto-generated method stub
		return super.getElementTexture();
	}

	@Override
	public void draw(int posX, int posY, int mouseX, int mouseY) {
		this.drawTexturedModalRect(posX-18, posY-1, 0, 0, 18, 18);
		this.drawTexturedModalRect(posX, posY, 0, 1, 18, 17);
		this.drawTexturedModalRect(posX, posY-16, 0, 1, 18, 16);
		this.drawTexturedModalRect(posX, posY-16-16, 0, 0, 18, 17);
		GlStateManager.color(0, 0.6F, 0);
		DrawUtils.drawScaledTexturedRect(posX+1, posY, TextureUtils.fromBlock(Blocks.GRASS), 16, 1, 1);
		GlStateManager.color(1, 1, 1);
		DrawUtils.drawScaledTexturedRect(posX+1, posY+1, TextureUtils.fromBlock(Blocks.DIRT), 16, 2, 1);
		DrawUtils.drawScaledTexturedRect(posX+1, posY+3, TextureUtils.fromBlock(Blocks.STONE), 16, 11, 1);
		DrawUtils.drawScaledTexturedRect(posX+1, posY+15, TextureUtils.fromBlock(Blocks.BEDROCK), 16, 1, 1);
		Random rnd = new Random(143535645L);
		for(int i = 0; i < 10; ++i)
		{
			DrawUtils.drawScaledTexturedRect(posX+1+rnd.nextInt(15), posY+3+rnd.nextInt(11), TextureUtils.fromBlock(Blocks.GRAVEL), 2, 2, 2);
		}
		for(int i = 0; i < 2; ++i)
		{
			DrawUtils.drawScaledTexturedRect(posX+1+rnd.nextInt(15), posY+9+rnd.nextInt(11), TextureUtils.fromBlock(Blocks.DIAMOND_BLOCK), 1, 1, 2);
		}
		for(int i = 0; i < 12; ++i)
		{
			DrawUtils.drawScaledTexturedRect(posX+1+rnd.nextInt(15), posY+3+rnd.nextInt(11), TextureUtils.fromBlock(Blocks.COAL_BLOCK), 1, 1, 2);
		}
		for(int i = 0; i < 6; ++i)
		{
			DrawUtils.drawScaledTexturedRect(posX+1+rnd.nextInt(15), posY+3+rnd.nextInt(11), TextureUtils.fromBlock(Blocks.STAINED_HARDENED_CLAY, 8), 1, 1, 2);
		}
		for(int i = 0; i < 4; ++i)
		{
			DrawUtils.drawScaledTexturedRect(posX+1+rnd.nextInt(15), posY+6+rnd.nextInt(8), TextureUtils.fromBlock(Blocks.GOLD_BLOCK), 1, 1, 2);
		}
		for(int i = 0; i < 8; ++i)
		{
			DrawUtils.drawScaledTexturedRect(posX+1+rnd.nextInt(15), posY+12+rnd.nextInt(2), TextureUtils.fromBlock(Blocks.LAVA), 1, 1, 2);
		}
		for(int i = 0; i < 8; ++i)
		{
			DrawUtils.drawScaledTexturedRect(posX+1+rnd.nextInt(15), posY+14, TextureUtils.fromBlock(Blocks.BEDROCK), 1, 1, 2);
		}
		rnd = null;
		int pos = MathUtils.pixelatedTextureSize(tile.getPos().getY(), 256, 50);
		if(pos > 45)pos = 45;
		GlStateManager.color(0, 1, 0);
		DrawUtils.drawScaledTexturedRect(posX+1, posY+14-pos, TextureUtils.fromBlock(Blocks.EMERALD_BLOCK), 16, 1, 2);
		GlStateManager.color(1,1,1);
		drawText(posX,posY);
	}

	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return super.getX();
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return super.getY();
	}

	@Override
	public void drawText(int posX, int posY) {
		Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(tile.getPos().getY()+"", posX-15-Integer.toString(tile.getPos().getY()).length(), posY+5, 0xffffff);
	}

}
