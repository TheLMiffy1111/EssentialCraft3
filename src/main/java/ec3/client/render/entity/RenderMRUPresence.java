package ec3.client.render.entity;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import org.lwjgl.opengl.GL11;

import DummyCore.Utils.TessellatorWrapper;
import ec3.client.render.RenderHandlerEC3;
import ec3.common.entity.EntityMRUPresence;
import ec3.utils.common.ECUtils;

public class RenderMRUPresence extends Render<EntityMRUPresence> {

	public RenderMRUPresence(RenderManager renderManager) {
		super(renderManager);
	}

	public void doActualRender(EntityMRUPresence par1Entity, double par2, double par4, double par6, float par8, float par9) {
		TessellatorWrapper var3 = TessellatorWrapper.getInstance();
		float var4 = par1Entity.renderIndex;
		float var5 = 0.0F;
		float stability = par1Entity.getBalance();
		float colorRRender = 0.0F;
		float colorGRender = 1.0F;
		float colorBRender = 1.0F;

		float colorRNormal = 0.0F;
		float colorGNormal = 1.0F;
		float colorBNormal = 1.0F;

		float colorRChaos = 1.0F;
		float colorGChaos = 0.0F;
		float colorBChaos = 0.0F;

		float colorRFrozen = 0.0F;
		float colorGFrozen = 0.0F;
		float colorBFrozen = 1.0F;

		int mru = par1Entity.getMRU();
		if(stability!=1.0F)
		{
			if(stability<1.0F)
			{
				float diff = stability;
				if(diff < 0.01F)
					diff = 0.0F;
				colorRRender = (colorRNormal*diff) + (colorRFrozen*(1.0F-diff));
				colorGRender = (colorGNormal*diff) + (colorGFrozen*(1.0F-diff));
				colorBRender = (colorBNormal*diff) + (colorBFrozen*(1.0F-diff));
			}
			if(stability>1.0F)
			{
				float diff = 2.0F-stability;
				if(diff < 0.01F)
					diff = 0.0F;
				colorRRender = (colorRNormal*diff) + (colorRChaos*(1.0F-diff));
				colorGRender = (colorGNormal*diff) + (colorGChaos*(1.0F-diff));
				colorBRender = (colorBNormal*diff) + (colorBChaos*(1.0F-diff));
			}
		}
		Random var6 = new Random(432L);


		GlStateManager.pushMatrix();

		GlStateManager.depthMask(false);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GlStateManager.disableAlpha();

		GlStateManager.translate(par2, par4, par6);

		Minecraft.getMinecraft().renderEngine.bindTexture(RenderHandlerEC3.whitebox);
		GlStateManager.scale(0.0000075F*mru, 0.0000075F*mru, 0.0000075F*mru);
		for(int var7 = 0; (float)var7 < (par1Entity.getMRU())/50; ++var7) {
			GlStateManager.rotate(var6.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(var6.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(var6.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(var6.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(var6.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(var6.nextFloat() * 360.0F + var4 * 90.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.color(colorRRender, colorGRender, colorBRender, 1);
			var3.startDrawing(6);
			float var8 = var6.nextFloat() * 20.0F + 5.0F + var5 * 10.0F;
			float var9 = var6.nextFloat() * 2.0F + 1.0F + var5 * 2.0F;
			var3.addVertex(0.0D, 0.0D, 0.0D);
			var3.addVertexWithUV(-0.866D * (double)var9, (double)var8, (double)(-0.5F * var9),0,0);
			var3.addVertexWithUV(0.866D * (double)var9, (double)var8, (double)(-0.5F * var9),0,1);
			var3.addVertexWithUV(0.0D, (double)var8, (double)(1.0F * var9),1,1);
			var3.addVertexWithUV(-0.866D * (double)var9, (double)var8, (double)(-0.5F * var9),1,0);
			var3.draw();
		}

		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.depthMask(true);

		GlStateManager.popMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}

	public void doRender(EntityMRUPresence par1Entity, double par2, double par4, double par6, float par8, float par9) {
		if(ECUtils.canPlayerSeeMRU(Minecraft.getMinecraft().player)) {
			doActualRender(par1Entity, par2, par4, par6, par8, par9);
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityMRUPresence entity) {
		return RenderHandlerEC3.whitebox;
	}

	public static class Factory implements IRenderFactory<EntityMRUPresence> {
		@Override
		public Render<? super EntityMRUPresence> createRenderFor(RenderManager manager) {
			return new RenderMRUPresence(manager);
		}
	}
}
