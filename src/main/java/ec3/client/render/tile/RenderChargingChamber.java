package ec3.client.render.tile;

import DummyCore.Client.AdvancedModelLoader;
import DummyCore.Client.IModelCustom;
import DummyCore.Utils.DrawUtils;
import DummyCore.Utils.MiscUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import ec3.common.tile.TileChargingChamber;

@SideOnly(Side.CLIENT)
public class RenderChargingChamber extends TileEntitySpecialRenderer<TileChargingChamber> {

	public RenderChargingChamber() {}

	public void doRender(TileChargingChamber tile, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
		RenderHelper.disableStandardItemLighting();

		float rotation = tile.getWorld().getWorldTime() % 360;
		float upperIndex = tile.getWorld().getWorldTime() % 360;

		if(upperIndex < 180) {
			upperIndex = (180 - upperIndex);
		}
		else {
			upperIndex -= 180;
		}

		rotation = rotation + 360F/tile.getWorld().getWorldTime() % 360;

		GlStateManager.pushMatrix(); 
		DrawUtils.renderItemStack_Full(tile.getStackInSlot(1),tile.getPos().getX()+0.5D,tile.getPos().getY()+10D , tile.getPos().getZ()+0.5D, p_76986_2_, p_76986_4_, p_76986_6_, rotation,0F, 1, 1, 1, 0.5F, 0.65F+((float)upperIndex/500F),0.5F, false);
		GlStateManager.popMatrix();
		RenderHelper.enableStandardItemLighting();
	}

	@Override
	public void renderTileEntityAt(TileChargingChamber p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_, int destroyStage) {
		this.doRender(p_147500_1_, p_147500_2_, p_147500_4_, p_147500_6_, p_147500_8_, 0);
	}
}