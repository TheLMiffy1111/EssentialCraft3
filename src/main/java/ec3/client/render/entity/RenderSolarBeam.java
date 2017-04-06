package ec3.client.render.entity;

import org.lwjgl.opengl.GL11;

import DummyCore.Utils.TessellatorWrapper;
import ec3.common.entity.EntitySolarBeam;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraft.util.ResourceLocation;

public class RenderSolarBeam extends Render<EntitySolarBeam> {

	public RenderSolarBeam(RenderManager renderManager) {
		super(renderManager);
	}

	private static final ResourceLocation field_147523_b = new ResourceLocation("textures/entity/beacon_beam.png");

	@Override
	public void doRender(EntitySolarBeam p_76986_1_, double p_147500_2_,
			double p_147500_4_, double p_147500_6_, float p_147500_8_,
			float p_76986_9_) {
		RenderHelper.disableStandardItemLighting();
		float f1 = 1.0F;
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);

		if (f1 > 0.0F)
		{
			TessellatorWrapper tessellator = TessellatorWrapper.getInstance();
			this.bindTexture(field_147523_b);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
			GlStateManager.disableLighting();
			GlStateManager.disableCull();
			GlStateManager.disableBlend();
			GlStateManager.depthMask(true);
			OpenGlHelper.glBlendFunc(770, 1, 1, 0);
			float f2 = (float)p_76986_1_.getEntityWorld().getTotalWorldTime() + p_147500_8_;
			float f3 = -f2 * 0.2F - (float)MathHelper.floor(-f2 * 0.1F);
			byte b0 = 1;
			double d3 = (double)f2 * 0.025D * (1.0D - (double)(b0 & 1) * 2.5D);
			tessellator.startDrawingQuadsWithColor();
			tessellator.setColorRGBA_F(255, 255, 0, 11);
			double d5 = (double)b0 * 0.2D;
			double d7 = 0.5D + Math.cos(d3 + 2.356194490192345D) * d5;
			double d9 = 0.5D + Math.sin(d3 + 2.356194490192345D) * d5;
			double d11 = 0.5D + Math.cos(d3 + (Math.PI / 4D)) * d5;
			double d13 = 0.5D + Math.sin(d3 + (Math.PI / 4D)) * d5;
			double d15 = 0.5D + Math.cos(d3 + 3.9269908169872414D) * d5;
			double d17 = 0.5D + Math.sin(d3 + 3.9269908169872414D) * d5;
			double d19 = 0.5D + Math.cos(d3 + 5.497787143782138D) * d5;
			double d21 = 0.5D + Math.sin(d3 + 5.497787143782138D) * d5;
			double d23 = (double)(256.0F * f1);
			double d25 = 0.0D;
			double d27 = 1.0D;
			double d28 = (double)(-1.0F + f3);
			double d29 = (double)(256.0F * f1) * (0.5D / d5) + d28;
			tessellator.addVertexWithUV(p_147500_2_ + d7, p_147500_4_ + d23, p_147500_6_ + d9, d27, d29);
			tessellator.addVertexWithUV(p_147500_2_ + d7, p_147500_4_, p_147500_6_ + d9, d27, d28);
			tessellator.addVertexWithUV(p_147500_2_ + d11, p_147500_4_, p_147500_6_ + d13, d25, d28);
			tessellator.addVertexWithUV(p_147500_2_ + d11, p_147500_4_ + d23, p_147500_6_ + d13, d25, d29);
			tessellator.addVertexWithUV(p_147500_2_ + d19, p_147500_4_ + d23, p_147500_6_ + d21, d27, d29);
			tessellator.addVertexWithUV(p_147500_2_ + d19, p_147500_4_, p_147500_6_ + d21, d27, d28);
			tessellator.addVertexWithUV(p_147500_2_ + d15, p_147500_4_, p_147500_6_ + d17, d25, d28);
			tessellator.addVertexWithUV(p_147500_2_ + d15, p_147500_4_ + d23, p_147500_6_ + d17, d25, d29);
			tessellator.addVertexWithUV(p_147500_2_ + d11, p_147500_4_ + d23, p_147500_6_ + d13, d27, d29);
			tessellator.addVertexWithUV(p_147500_2_ + d11, p_147500_4_, p_147500_6_ + d13, d27, d28);
			tessellator.addVertexWithUV(p_147500_2_ + d19, p_147500_4_, p_147500_6_ + d21, d25, d28);
			tessellator.addVertexWithUV(p_147500_2_ + d19, p_147500_4_ + d23, p_147500_6_ + d21, d25, d29);
			tessellator.addVertexWithUV(p_147500_2_ + d15, p_147500_4_ + d23, p_147500_6_ + d17, d27, d29);
			tessellator.addVertexWithUV(p_147500_2_ + d15, p_147500_4_, p_147500_6_ + d17, d27, d28);
			tessellator.addVertexWithUV(p_147500_2_ + d7, p_147500_4_, p_147500_6_ + d9, d25, d28);
			tessellator.addVertexWithUV(p_147500_2_ + d7, p_147500_4_ + d23, p_147500_6_ + d9, d25, d29);
			tessellator.draw();
			GlStateManager.enableBlend();
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GlStateManager.depthMask(false);
			GlStateManager.color(1, 1, 100F/255F, 64F/255F);
			tessellator.startDrawingQuads();
			double d30 = 0.2D;
			double d4 = 0.2D;
			double d6 = 0.8D;
			double d8 = 0.2D;
			double d10 = 0.2D;
			double d12 = 0.8D;
			double d14 = 0.8D;
			double d16 = 0.8D;
			double d18 = (double)(256.0F * f1);
			double d20 = 0.0D;
			double d22 = 1.0D;
			double d24 = (double)(-1.0F + f3);
			double d26 = (double)(256.0F * f1 * f1);
			tessellator.addVertexWithUV(p_147500_2_ + d30, p_147500_4_ + d18, p_147500_6_ + d4, d22, d26);
			tessellator.addVertexWithUV(p_147500_2_ + d30, p_147500_4_, p_147500_6_ + d4, d22, d24);
			tessellator.addVertexWithUV(p_147500_2_ + d6, p_147500_4_, p_147500_6_ + d8, d20, d24);
			tessellator.addVertexWithUV(p_147500_2_ + d6, p_147500_4_ + d18, p_147500_6_ + d8, d20, d26);
			tessellator.addVertexWithUV(p_147500_2_ + d14, p_147500_4_ + d18, p_147500_6_ + d16, d22, d26);
			tessellator.addVertexWithUV(p_147500_2_ + d14, p_147500_4_, p_147500_6_ + d16, d22, d24);
			tessellator.addVertexWithUV(p_147500_2_ + d10, p_147500_4_, p_147500_6_ + d12, d20, d24);
			tessellator.addVertexWithUV(p_147500_2_ + d10, p_147500_4_ + d18, p_147500_6_ + d12, d20, d26);
			tessellator.addVertexWithUV(p_147500_2_ + d6, p_147500_4_ + d18, p_147500_6_ + d8, d22, d26);
			tessellator.addVertexWithUV(p_147500_2_ + d6, p_147500_4_, p_147500_6_ + d8, d22, d24);
			tessellator.addVertexWithUV(p_147500_2_ + d14, p_147500_4_, p_147500_6_ + d16, d20, d24);
			tessellator.addVertexWithUV(p_147500_2_ + d14, p_147500_4_ + d18, p_147500_6_ + d16, d20, d26);
			tessellator.addVertexWithUV(p_147500_2_ + d10, p_147500_4_ + d18, p_147500_6_ + d12, d22, d26);
			tessellator.addVertexWithUV(p_147500_2_ + d10, p_147500_4_, p_147500_6_ + d12, d22, d24);
			tessellator.addVertexWithUV(p_147500_2_ + d30, p_147500_4_, p_147500_6_ + d4, d20, d24);
			tessellator.addVertexWithUV(p_147500_2_ + d30, p_147500_4_ + d18, p_147500_6_ + d4, d20, d26);
			tessellator.draw();
			GlStateManager.enableLighting();
			GlStateManager.enableTexture2D();
			GlStateManager.depthMask(true);
		}
		RenderHelper.enableStandardItemLighting();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntitySolarBeam p_110775_1_) {
		return field_147523_b;
	}

	public static class Factory implements IRenderFactory<EntitySolarBeam> {
		@Override
		public Render<? super EntitySolarBeam> createRenderFor(RenderManager manager) {
			return new RenderSolarBeam(manager);
		}
	}
}
