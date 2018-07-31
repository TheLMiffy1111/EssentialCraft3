package essentialcraft.client.gui.element;

import DummyCore.Client.GuiElement;
import DummyCore.Utils.DrawUtils;
import DummyCore.Utils.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class GuiFluidTank extends GuiElement{

	private ResourceLocation rec = new ResourceLocation("essentialcraft","textures/gui/mrustorage.png");

	public int x;
	public int y;
	public IFluidTankProperties tank;

	public GuiFluidTank(int i, int j, IFluidHandler t) {
		x = i;
		y = j;
		tank = t.getTankProperties()[0];
	}

	public GuiFluidTank(int i, int j, IFluidTankProperties t) {
		x = i;
		y = j;
		tank = t;
	}

	public GuiFluidTank(int i, int j, TileEntity t) {
		x = i;
		y = j;
		if(t.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
			tank = t.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).getTankProperties()[0];
		}
		else {
			throw new IllegalArgumentException("Tile does not handle fluids");
		}
	}

	@Override
	public ResourceLocation getElementTexture() {
		return rec;
	}

	@Override
	public void draw(int posX, int posY, int mouseX, int mouseY) {
		this.drawTexturedModalRect(posX, posY, 0, 0, 18, 54);
		this.drawTexturedModalRect(posX, posY+53, 0, 71, 18, 1);
		if(tank != null) {
			FluidStack fStk = tank.getContents();
			if(fStk != null && fStk.amount > 0) {
				TextureAtlasSprite icon = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fStk.getFluid().getFlowing().toString());
				int scale = MathUtils.pixelatedTextureSize(fStk.amount, tank.getCapacity(), 52);
				DrawUtils.drawTexture(posX+1, posY+1+52-scale, icon, 16, scale, 1);
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
