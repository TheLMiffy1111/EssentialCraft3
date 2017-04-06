package ec3.client.render.entity;

import org.lwjgl.opengl.GL11;

import ec3.common.entity.EntityOrbitalStrike;
import DummyCore.Utils.DrawUtils;
import DummyCore.Utils.MiscUtils;
import DummyCore.Utils.TessellatorWrapper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraft.util.ResourceLocation;

public class RenderOrbitalStrike extends Render<EntityOrbitalStrike> {

	public RenderOrbitalStrike(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntityOrbitalStrike e, double screenX, double screenY, double screenZ, float rotationYaw, float rotationPitch) {
		GlStateManager.pushMatrix();

		EntityOrbitalStrike eos = e;

		//GL11.glScaled(eos.delay/3, 1, eos.delay/3);

		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
		GlStateManager.disableFog();
		GlStateManager.disableLighting();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		DrawUtils.bindTexture("minecraft", "textures/entity/beacon_beam.png");
		GlStateManager.translate(-0.5F, 0, -0.5F);
		TileEntityBeaconRenderer.renderBeamSegment(screenX, screenY, screenZ, 0, 1, e.getEntityWorld().getTotalWorldTime(), 0, (int)(255D-e.posY), new float[] {1,0,1}, 0.2D, eos.delay/6);
		GlStateManager.enableLighting();
		GlStateManager.enableFog();
		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityOrbitalStrike e) {
		return new ResourceLocation("textures/entity/beacon_beam.png");
	}

	public static class Factory implements IRenderFactory<EntityOrbitalStrike> {
		@Override
		public Render<? super EntityOrbitalStrike> createRenderFor(RenderManager manager) {
			return new RenderOrbitalStrike(manager);
		}
	}
}
