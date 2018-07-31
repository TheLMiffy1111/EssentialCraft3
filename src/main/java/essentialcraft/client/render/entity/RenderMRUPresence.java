package essentialcraft.client.render.entity;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import essentialcraft.client.render.RenderHandlerEC;
import essentialcraft.common.entity.EntityMRUPresence;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderMRUPresence extends Render<EntityMRUPresence> {

	static final float HALF_SQRT_3 = 0.8660254F;

	public RenderMRUPresence(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntityMRUPresence entity, double x, double y, double z, float entityYaw, float partialTicks) {
		if(ECUtils.canPlayerSeeMRU(Minecraft.getMinecraft().player)) {
			float index = entity.renderIndex;
			float stability = entity.mruStorage.getBalance();
			float colorRRender = 0F;
			float colorGRender = 1F;
			float colorBRender = 1F;

			float colorRNormal = 0F;
			float colorGNormal = 1F;
			float colorBNormal = 1F;

			float colorRChaos = 1F;
			float colorGChaos = 0F;
			float colorBChaos = 0F;

			float colorRFrozen = 0F;
			float colorGFrozen = 0F;
			float colorBFrozen = 1F;

			int mru = entity.mruStorage.getMRU();
			if(stability!=1F) {
				if(stability<1F) {
					float diff = stability;
					if(diff < 0.01F) {
						diff = 0F;
					}
					colorRRender = colorRNormal*diff + colorRFrozen*(1F-diff);
					colorGRender = colorGNormal*diff + colorGFrozen*(1F-diff);
					colorBRender = colorBNormal*diff + colorBFrozen*(1F-diff);
				}
				if(stability>1F) {
					float diff = 2F-stability;
					if(diff < 0.01F) {
						diff = 0F;
					}
					colorRRender = colorRNormal*diff + colorRChaos*(1F-diff);
					colorGRender = colorGNormal*diff + colorGChaos*(1F-diff);
					colorBRender = colorBNormal*diff + colorBChaos*(1F-diff);
				}
			}
			Random rand = new Random(432L);

			GlStateManager.pushMatrix();
			GlStateManager.depthFunc(GL11.GL_ALWAYS);
			GlStateManager.enableBlend();
			GlStateManager.disableAlpha();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.shadeModel(GL11.GL_SMOOTH);
			GlStateManager.color(colorRRender, colorGRender, colorBRender, 0.5F);

			GlStateManager.translate(x, y, z);

			Minecraft.getMinecraft().renderEngine.bindTexture(RenderHandlerEC.whitebox);
			GlStateManager.scale(0.0000075F*mru, 0.0000075F*mru, 0.0000075F*mru);
			for(int i = 0; i < entity.mruStorage.getMRU()/25; ++i) {
				//GlStateManager.rotate(rand.nextFloat() * 360F, 1F, 0F, 0F);
				//GlStateManager.rotate(rand.nextFloat() * 360F, 0F, 1F, 0F);
				//GlStateManager.rotate(rand.nextFloat() * 360F, 0F, 0F, 1F);
				GlStateManager.rotate(rand.nextFloat() * 360F, 1F, 0F, 0F);
				GlStateManager.rotate(rand.nextFloat() * 360F, 0F, 1F, 0F);
				GlStateManager.rotate(rand.nextFloat() * 360F + index * 90F, 0F, 0F, 1F);
				float f0 = rand.nextFloat() * 20F + 5F;
				float f1 = rand.nextFloat() * 2F + 1F;
				GlStateManager.glBegin(GL11.GL_TRIANGLE_FAN);
				GlStateManager.glVertex3f(0, 0, 0);
				GlStateManager.glVertex3f(-HALF_SQRT_3*f1, f0, -f1/2F);
				GlStateManager.glVertex3f(HALF_SQRT_3*f1, f0, -f1/2F);
				GlStateManager.glVertex3f(0, f0, f1);
				GlStateManager.glVertex3f(-HALF_SQRT_3*f1, f0, -f1/2F);
				GlStateManager.glEnd();
			}

			GlStateManager.color(1F, 1F, 1F, 1F);
			GlStateManager.shadeModel(GL11.GL_FLAT);
			GlStateManager.disableBlend();
			GlStateManager.enableAlpha();
			GlStateManager.depthFunc(GL11.GL_LEQUAL);
			GlStateManager.popMatrix();
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityMRUPresence entity) {
		return RenderHandlerEC.whitebox;
	}

	public static class Factory implements IRenderFactory<EntityMRUPresence> {
		@Override
		public Render<? super EntityMRUPresence> createRenderFor(RenderManager manager) {
			return new RenderMRUPresence(manager);
		}
	}
}
