package ec3.client.render.entity;

import org.lwjgl.opengl.GL11;

import DummyCore.Client.AdvancedModelLoader;
import DummyCore.Client.IModelCustom;
import DummyCore.Utils.DrawUtils;
import DummyCore.Utils.MiscUtils;
import ec3.common.entity.EntityDivider;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderDivider extends Render<EntityDivider> {
	
	public RenderDivider(RenderManager renderManager) {
		super(renderManager);
	}

	public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation("essentialcraft","models/block/sphere.obj"));

	@Override
	public void doRender(EntityDivider e, double x, double y, double z, float partial, float zero) {
		
		GlStateManager.pushMatrix();
		
		GlStateManager.translate(x, y-3, z);
		GlStateManager.scale(3, 3, 3);
		
		RenderHelper.disableStandardItemLighting();
		
    	GlStateManager.disableAlpha();
    	GlStateManager.enableBlend();
    	GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
    	
    	DrawUtils.bindTexture("minecraft", "textures/entity/beacon_beam.png");
    	
    	GlStateManager.color(1, 0, 1, 0.2F);
		
		model.renderAll();
		
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		
		RenderHelper.enableStandardItemLighting();
		
		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityDivider e) {
		return null;
	}
	
	public static class Factory implements IRenderFactory<EntityDivider> {
		@Override
		public Render<? super EntityDivider> createRenderFor(RenderManager manager) {
			return new RenderDivider(manager);
		}
	}
}
