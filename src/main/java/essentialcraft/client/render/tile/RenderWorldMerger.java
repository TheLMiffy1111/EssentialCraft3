package essentialcraft.client.render.tile;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import essentialcraft.client.render.RenderHandlerEC;
import essentialcraft.common.tile.TileWorldMerger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class RenderWorldMerger extends TileEntitySpecialRenderer<TileWorldMerger> {

	static final float HALF_SQRT_3 = 0.8660254F;
	static final float TWO_OVER_THREE = 0.6666667F;
	
	@Override
	public void render(TileWorldMerger te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		x += 0.5D;
		y += 1.5D;
		z += 0.5D;
		float index = te.innerRotation+partialTicks;
		RenderHelper.disableStandardItemLighting();
		float mru = te.progressLevel * 90;
		Random rand = new Random(432L);
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		GlStateManager.translate(x, y, z);
		Minecraft.getMinecraft().renderEngine.bindTexture(RenderHandlerEC.whitebox);
		GlStateManager.scale(0.0000050F*mru, 0.0000050F*mru, 0.0000050F*mru);
		for(int i = 0; i < 20; i++) {
			GlStateManager.rotate(rand.nextFloat() * 360F + 0.01F*index, 0F, 1F, 0F);
			float f0 = rand.nextFloat() * 20F + 5F;
			float f1 = rand.nextFloat() * 2F + 1F;
			GlStateManager.glBegin(GL11.GL_TRIANGLE_FAN);
			GlStateManager.color(0F, 1F, 1F, 0.5F);
			GlStateManager.glVertex3f(0, 0, 0);
			GlStateManager.color(1F, 0F, 1F, 0.5F);
			GlStateManager.glVertex3f(-HALF_SQRT_3*f1, f0, -f1/2F);
			GlStateManager.glVertex3f(HALF_SQRT_3*f1, f0, -f1/2F);
			GlStateManager.glVertex3f(0, f0, f1);
			GlStateManager.glVertex3f(-HALF_SQRT_3*f1, f0, -f1/2F);
			GlStateManager.glEnd();
		}
		GlStateManager.translate(0D, 20D, 0D);
		GlStateManager.scale(0.5D, 0.5D, 0.5D);
		GlStateManager.color(1, 0, 1, 0.5F);
		for(int i = 0; i < te.progressLevel/5 * 1000F/TileWorldMerger.requiredTicks; i++) {
			//GlStateManager.rotate(rand.nextFloat() * 360F, 1F, 0F, 0F);
			//GlStateManager.rotate(rand.nextFloat() * 360F, 0F, 1F, 0F);
			//GlStateManager.rotate(rand.nextFloat() * 360F, 0F, 0F, 1F);
			GlStateManager.rotate(rand.nextFloat() * 360F, 1F, 0F, 0F);
			GlStateManager.rotate(rand.nextFloat() * 360F, 0F, 1F, 0F);
			GlStateManager.rotate(rand.nextFloat() * 360F + 0.05F*index, 0F, 0F, 1F);
			float f0 = rand.nextFloat() * 20F + 5F;
			float f1 = rand.nextFloat() * 2F + 1F;
			GlStateManager.glBegin(GL11.GL_TRIANGLE_FAN);
			GlStateManager.glVertex3f(0, 0, 0);
			GlStateManager.glVertex3f(HALF_SQRT_3*f1, f0, f1/2F);
			GlStateManager.glVertex3f(-HALF_SQRT_3*f1, f0, f1/2F);
			GlStateManager.glVertex3f(0, f0, -f1);
			GlStateManager.glVertex3f(HALF_SQRT_3*f1, f0, f1/2F);
			GlStateManager.glEnd();
		}
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.popMatrix();
		RenderHelper.enableStandardItemLighting();
	}

	@Override
	public boolean isGlobalRenderer(TileWorldMerger te) {
		return true;
	}
}
