package ec3.client.gui.element;

import ec3.api.ITEHasMRU;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GuiBalanceState extends GuiTextElement{

	public TileEntity tile;

	public GuiBalanceState(int i, int j, TileEntity t)
	{
		super(i,j);
		tile = t;
	}

	@Override
	public ResourceLocation getElementTexture() {
		return super.getElementTexture();
	}

	@Override
	public void draw(int posX, int posY, int mouseX, int mouseY) {
		super.draw(posX, posY, mouseX, mouseY);
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
		float balance = ((ITEHasMRU)tile).getBalance();
		String str = Float.toString( ((ITEHasMRU)tile).getBalance());
		if(str.length() > 6)
			str = str.substring(0, 6);

		for(int i = str.length()-1; i > 0; --i)
		{
			if(i > 2)
			{
				char c = str.charAt(i);
				if(c == '0')
				{
					str = str.substring(0, i);
				}
			}
		}
		String balanceType = "Pure";
		int color = 0x00ffff;
		if(balance < 1)
		{
			balanceType = "Frozen";
			color = 0x0000ff;
		}
		if(balance > 1)
		{
			balanceType = "Chaos";
			color = 0xff0000;
		}

		Minecraft.getMinecraft().fontRendererObj.drawString(balanceType+": "+str, posX+2, posY+5, color, true);
	}

}