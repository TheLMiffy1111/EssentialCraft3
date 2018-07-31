package essentialcraft.client.render.tile;

import org.lwjgl.opengl.GL11;

import DummyCore.Utils.DrawUtils;
import essentialcraft.common.item.ItemsCore;
import essentialcraft.common.tile.TileWeatherController;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class RenderWeatherController extends TileEntitySpecialRenderer<TileWeatherController> {

	@Override
	public void render(TileWeatherController te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		int id = te.getStackInSlot(1).getItem() == ItemsCore.clearing_catalyst ? 0 :
			te.getStackInSlot(1).getItem() == ItemsCore.raining_catalyst ? 1 :
				te.getStackInSlot(1).getItem() == ItemsCore.thundering_catalyst ? 2 : -1;
		if(id != -1) {
			GlStateManager.pushMatrix();
			GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
			GlStateManager.disableFog();
			GlStateManager.disableLighting();
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
			DrawUtils.bindTexture("minecraft", "textures/entity/beacon_beam.png");
			GlStateManager.translate(-0.5F, 0, -0.5F);
			float[] colors = id == 0 ? new float[] {127/255F, 170/255F, 1F} : id == 1 ? new float[] {102/255F, 113/255F, 137/255F} : id == 2 ? new float[] {43/255F, 46/255F, 52/255F} : new float[3];
			double beamRad = (double)te.progressLevel/te.requiredTicks/5;
			double glowRad = (double)te.progressLevel/te.requiredTicks/2;
			TileEntityBeaconRenderer.renderBeamSegment(x+0.5D, y, z+0.5D, 0, 1, te.getWorld().getTotalWorldTime(), 0, 255-te.getPos().getY(), colors, beamRad, glowRad);
			GlStateManager.enableLighting();
			GlStateManager.enableFog();
			GlStateManager.popMatrix();
		}
	}

	@Override
	public boolean isGlobalRenderer(TileWeatherController te) {
		return true;
	}
}
