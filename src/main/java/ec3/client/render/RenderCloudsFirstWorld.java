package ec3.client.render;

import org.lwjgl.opengl.GL11;

import DummyCore.Utils.TessellatorWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.IRenderHandler;

public class RenderCloudsFirstWorld extends IRenderHandler{

	private static final ResourceLocation locationCloudsPng = new ResourceLocation("textures/environment/clouds.png");
	private int cloudTickCounter;

	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		if(world.rand.nextInt(10)==0)
			++cloudTickCounter;
		for(int layer = 0; layer < 3; ++layer) {
			GlStateManager.pushMatrix();
			GlStateManager.disableCull();
			float f1 = (float)(mc.getRenderViewEntity().lastTickPosY + (mc.getRenderViewEntity().posY - mc.getRenderViewEntity().lastTickPosY) * (double)partialTicks);
			Tessellator tessellator = Tessellator.getInstance();
			VertexBuffer vertexbuffer = tessellator.getBuffer();
			float f2 = 12.0F;
			float f3 = 4.0F;
			double d0 = (double)((float)this.cloudTickCounter + partialTicks);
			double d1 = (mc.getRenderViewEntity().prevPosX + (mc.getRenderViewEntity().posX - mc.getRenderViewEntity().prevPosX) * (double)partialTicks + d0* layer*layer * 0.03D) / (double)f2 ;
			double d2 = (mc.getRenderViewEntity().prevPosZ + (mc.getRenderViewEntity().posZ - mc.getRenderViewEntity().prevPosZ) * (double)partialTicks) / (double)f2* layer*layer + 0.33D ;
			float f4 = world.provider.getCloudHeight() - f1 + 0.33F + layer*36;
			int i = MathHelper.floor(d1 / 2048.0D);
			int j = MathHelper.floor(d2 / 2048.0D);
			d1 -= (double)(i * 2048);
			d2 -= (double)(j * 2048);
			mc.renderEngine.bindTexture(locationCloudsPng);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			Vec3d vec3d = new Vec3d((1F*(layer+1)/3F), (1F*(layer+1)/3F), (1F*(layer+1)/3F));
			if(world.isRaining())
				vec3d = new Vec3d(0.3D, 0.3D, 0.3D);
			if(world.isRaining() && world.isThundering())
				vec3d = new Vec3d(0.1D, 0.1D, 0.1D);
			float f5 = (float)vec3d.xCoord;
			float f6 = (float)vec3d.yCoord;
			float f7 = (float)vec3d.zCoord;
			float f8;
			float f9;
			float f10;

			if (mc.gameSettings.anaglyph) {
				f8 = (f5 * 30.0F + f6 * 59.0F + f7 * 11.0F) / 100.0F;
				f9 = (f5 * 30.0F + f6 * 70.0F) / 100.0F;
				f10 = (f5 * 30.0F + f7 * 70.0F) / 100.0F;
				f5 = f8;
				f6 = f9;
				f7 = f10;
			}

			f8 = (float)(d1 * 0.0D);
			f9 = (float)(d2 * 0.0D);
			f10 = 0.00390625F;
			f8 = (float)MathHelper.floor(d1) * f10;
			f9 = (float)MathHelper.floor(d2) * f10;
			float f11 = (float)(d1 - (double)MathHelper.floor(d1));
			float f12 = (float)(d2 - (double)MathHelper.floor(d2));
			byte b0 = 8;
			byte b1 = 4;
			float f13 = 9.765625E-4F;
			GlStateManager.scale(f2, 1.0F, f2);

			for (int k = 0; k < 2; ++k) {
				if (k == 0) {
					GlStateManager.colorMask(false, false, false, false);
				}
				else if (mc.gameSettings.anaglyph) {
					if (EntityRenderer.anaglyphField == 0) {
						GlStateManager.colorMask(false, true, true, true);
					}
					else
					{
						GlStateManager.colorMask(true, false, false, true);
					}
				}
				else
				{
					GlStateManager.colorMask(true, true, true, true);
				}

				for (int l = -b1 + 1; l <= b1; ++l) {
					for (int i1 = -b1 + 1; i1 <= b1; ++i1) {
						vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
						float f14 = (float)(l * b0);
						float f15 = (float)(i1 * b0);
						float f16 = f14 - f11;
						float f17 = f15 - f12;

						if (f4 > -f3 - 1.0F) {
							vertexbuffer.pos((double)(f16 + 0.0F), (double)(f4 + 0.0F), (double)(f17 + (float)b0)).tex((double)((f14 + 0.0F) * f10 + f8), (double)((f15 + (float)b0) * f10 + f9)).color(f5 * 0.7F, f6 * 0.7F, f7 * 0.7F, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
							vertexbuffer.pos((double)(f16 + (float)b0), (double)(f4 + 0.0F), (double)(f17 + (float)b0)).tex((double)((f14 + (float)b0) * f10 + f8), (double)((f15 + (float)b0) * f10 + f9)).color(f5 * 0.7F, f6 * 0.7F, f7 * 0.7F, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
							vertexbuffer.pos((double)(f16 + (float)b0), (double)(f4 + 0.0F), (double)(f17 + 0.0F)).tex((double)((f14 + (float)b0) * f10 + f8), (double)((f15 + 0.0F) * f10 + f9)).color(f5 * 0.7F, f6 * 0.7F, f7 * 0.7F, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
							vertexbuffer.pos((double)(f16 + 0.0F), (double)(f4 + 0.0F), (double)(f17 + 0.0F)).tex((double)((f14 + 0.0F) * f10 + f8), (double)((f15 + 0.0F) * f10 + f9)).color(f5 * 0.7F, f6 * 0.7F, f7 * 0.7F, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
						}

						if (f4 <= f3 + 1.0F) {
							vertexbuffer.pos((double)(f16 + 0.0F), (double)(f4 + f3 - f13), (double)(f17 + (float)b0)).tex((double)((f14 + 0.0F) * f10 + f8), (double)((f15 + (float)b0) * f10 + f9)).color(f5, f6, f7, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
							vertexbuffer.pos((double)(f16 + (float)b0), (double)(f4 + f3 - f13), (double)(f17 + (float)b0)).tex((double)((f14 + (float)b0) * f10 + f8), (double)((f15 + (float)b0) * f10 + f9)).color(f5, f6, f7, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
							vertexbuffer.pos((double)(f16 + (float)b0), (double)(f4 + f3 - f13), (double)(f17 + 0.0F)).tex((double)((f14 + (float)b0) * f10 + f8), (double)((f15 + 0.0F) * f10 + f9)).color(f5, f6, f7, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
							vertexbuffer.pos((double)(f16 + 0.0F), (double)(f4 + f3 - f13), (double)(f17 + 0.0F)).tex((double)((f14 + 0.0F) * f10 + f8), (double)((f15 + 0.0F) * f10 + f9)).color(f5, f6, f7, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
						}

						int j1;

						if (l > -1) {
							for (j1 = 0; j1 < b0; ++j1) {
								vertexbuffer.pos((double)(f16 + (float)j1 + 0.0F), (double)(f4 + 0.0F), (double)(f17 + (float)b0)).tex((double)((f14 + (float)j1 + 0.5F) * f10 + f8), (double)((f15 + (float)b0) * f10 + f9)).color(f5 * 0.9F, f6 * 0.9F, f7 * 0.9F, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
								vertexbuffer.pos((double)(f16 + (float)j1 + 0.0F), (double)(f4 + f3), (double)(f17 + (float)b0)).tex((double)((f14 + (float)j1 + 0.5F) * f10 + f8), (double)((f15 + (float)b0) * f10 + f9)).color(f5 * 0.9F, f6 * 0.9F, f7 * 0.9F, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
								vertexbuffer.pos((double)(f16 + (float)j1 + 0.0F), (double)(f4 + f3), (double)(f17 + 0.0F)).tex((double)((f14 + (float)j1 + 0.5F) * f10 + f8), (double)((f15 + 0.0F) * f10 + f9)).color(f5 * 0.9F, f6 * 0.9F, f7 * 0.9F, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
								vertexbuffer.pos((double)(f16 + (float)j1 + 0.0F), (double)(f4 + 0.0F), (double)(f17 + 0.0F)).tex((double)((f14 + (float)j1 + 0.5F) * f10 + f8), (double)((f15 + 0.0F) * f10 + f9)).color(f5 * 0.9F, f6 * 0.9F, f7 * 0.9F, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
							}
						}

						if (l <= 1) {
							for (j1 = 0; j1 < b0; ++j1) {
								vertexbuffer.pos((double)(f16 + (float)j1 + 1.0F - f13), (double)(f4 + 0.0F), (double)(f17 + (float)b0)).tex((double)((f14 + (float)j1 + 0.5F) * f10 + f8), (double)((f15 + (float)b0) * f10 + f9)).color(f5 * 0.9F, f6 * 0.9F, f7 * 0.9F, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
								vertexbuffer.pos((double)(f16 + (float)j1 + 1.0F - f13), (double)(f4 + f3), (double)(f17 + (float)b0)).tex((double)((f14 + (float)j1 + 0.5F) * f10 + f8), (double)((f15 + (float)b0) * f10 + f9)).color(f5 * 0.9F, f6 * 0.9F, f7 * 0.9F, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
								vertexbuffer.pos((double)(f16 + (float)j1 + 1.0F - f13), (double)(f4 + f3), (double)(f17 + 0.0F)).tex((double)((f14 + (float)j1 + 0.5F) * f10 + f8), (double)((f15 + 0.0F) * f10 + f9)).color(f5 * 0.9F, f6 * 0.9F, f7 * 0.9F, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
								vertexbuffer.pos((double)(f16 + (float)j1 + 1.0F - f13), (double)(f4 + 0.0F), (double)(f17 + 0.0F)).tex((double)((f14 + (float)j1 + 0.5F) * f10 + f8), (double)((f15 + 0.0F) * f10 + f9)).color(f5 * 0.9F, f6 * 0.9F, f7 * 0.9F, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
							}
						}

						if (i1 > -1) {
							for (j1 = 0; j1 < b0; ++j1) {
								vertexbuffer.pos((double)(f16 + 0.0F), (double)(f4 + f3), (double)(f17 + (float)j1 + 0.0F)).tex((double)((f14 + 0.0F) * f10 + f8), (double)((f15 + (float)j1 + 0.5F) * f10 + f9)).color(f5 * 0.8F, f6 * 0.8F, f7 * 0.8F, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
								vertexbuffer.pos((double)(f16 + (float)b0), (double)(f4 + f3), (double)(f17 + (float)j1 + 0.0F)).tex((double)((f14 + (float)b0) * f10 + f8), (double)((f15 + (float)j1 + 0.5F) * f10 + f9)).color(f5 * 0.8F, f6 * 0.8F, f7 * 0.8F, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
								vertexbuffer.pos((double)(f16 + (float)b0), (double)(f4 + 0.0F), (double)(f17 + (float)j1 + 0.0F)).tex((double)((f14 + (float)b0) * f10 + f8), (double)((f15 + (float)j1 + 0.5F) * f10 + f9)).color(f5 * 0.8F, f6 * 0.8F, f7 * 0.8F, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
								vertexbuffer.pos((double)(f16 + 0.0F), (double)(f4 + 0.0F), (double)(f17 + (float)j1 + 0.0F)).tex((double)((f14 + 0.0F) * f10 + f8), (double)((f15 + (float)j1 + 0.5F) * f10 + f9)).color(f5 * 0.8F, f6 * 0.8F, f7 * 0.8F, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
							}
						}

						if (i1 <= 1) {
							for (j1 = 0; j1 < b0; ++j1) {
								vertexbuffer.pos((double)(f16 + 0.0F), (double)(f4 + f3), (double)(f17 + (float)j1 + 1.0F - f13)).tex((double)((f14 + 0.0F) * f10 + f8), (double)((f15 + (float)j1 + 0.5F) * f10 + f9)).color(f5 * 0.8F, f6 * 0.8F, f7 * 0.8F, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
								vertexbuffer.pos((double)(f16 + (float)b0), (double)(f4 + f3), (double)(f17 + (float)j1 + 1.0F - f13)).tex((double)((f14 + (float)b0) * f10 + f8), (double)((f15 + (float)j1 + 0.5F) * f10 + f9)).color(f5 * 0.8F, f6 * 0.8F, f7 * 0.8F, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
								vertexbuffer.pos((double)(f16 + (float)b0), (double)(f4 + 0.0F), (double)(f17 + (float)j1 + 1.0F - f13)).tex((double)((f14 + (float)b0) * f10 + f8), (double)((f15 + (float)j1 + 0.5F) * f10 + f9)).color(f5 * 0.8F, f6 * 0.8F, f7 * 0.8F, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
								vertexbuffer.pos((double)(f16 + 0.0F), (double)(f4 + 0.0F), (double)(f17 + (float)j1 + 1.0F - f13)).tex((double)((f14 + 0.0F) * f10 + f8), (double)((f15 + (float)j1 + 0.5F) * f10 + f9)).color(f5 * 0.8F, f6 * 0.8F, f7 * 0.8F, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
							}
						}

						tessellator.draw();
					}
				}
			}

			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.disableBlend();
			GlStateManager.enableCull();
			GlStateManager.popMatrix();
		}
	}
}
