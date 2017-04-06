package ec3.client.render.entity;

import ec3.common.entity.EntityMRUArrow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderMRUArrow extends Render<EntityMRUArrow> {

	public RenderMRUArrow(RenderManager renderManager) {
		super(renderManager);
	}

	private static final ResourceLocation arrowTextures = new ResourceLocation("textures/entity/arrow.png");
	
	@Override
	public void doRender(EntityMRUArrow p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {}

    protected ResourceLocation getEntityTexture(EntityArrow p_110775_1_) {
        return arrowTextures;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityMRUArrow p_110775_1_) {
        return this.getEntityTexture((EntityArrow)p_110775_1_);
    }
    
    public static class Factory implements IRenderFactory<EntityMRUArrow> {
    	@Override
    	public Render<? super EntityMRUArrow> createRenderFor(RenderManager manager) {
    		return new RenderMRUArrow(manager);
    	}
    }
}
