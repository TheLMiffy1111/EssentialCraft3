package ec3.client.render.entity;

import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import ec3.client.model.ModelDemon;
import ec3.common.entity.EntityDemon;

@SideOnly(Side.CLIENT)
public class RenderDemon extends RenderLiving<EntityDemon>
{
    private static final ResourceLocation endermanEyesTexture = new ResourceLocation("essentialcraft","textures/entities/demon_eyes.png");
    private static final ResourceLocation endermanTextures = new ResourceLocation("essentialcraft","textures/entities/demon.png");
    /** The model of the enderman */
    private ModelDemon endermanModel;
    public RenderDemon()
    {
        super(Minecraft.getMinecraft().getRenderManager(), new ModelDemon(1,0,64,32), 0.5F);
        this.endermanModel = (ModelDemon)super.mainModel;
    }
    
    public RenderDemon(RenderManager rm)
    {
        super(rm, new ModelDemon(1,0,64,32), 0.5F);
        this.endermanModel = (ModelDemon)super.mainModel;
    }

    protected void preRenderCallback(EntityDemon p_77041_1_, float p_77041_2_) 
    {
    	float s = 1.4F;
    	GlStateManager.scale(s, s, s);
    }
    
    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(EntityDemon p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
    	//p_76986_1_.setInvisible(true);

        super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityDemon p_110775_1_)
    {
        return endermanTextures;
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityDemon p_77032_1_, int p_77032_2_, float p_77032_3_)
    {
        if (p_77032_2_ != 0)
        {
            return -1;
        }
        else
        {
            this.bindTexture(endermanEyesTexture);
            float f1 = 1.0F;
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();
            GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
            GlStateManager.disableLighting();

            if (p_77032_1_.isInvisible())
            {
                GlStateManager.depthMask(false);
            }
            else
            {
                GlStateManager.depthMask(true);
            }

            char c0 = 61680;
            int j = c0 % 65536;
            int k = c0 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
            GlStateManager.enableLighting();
            GlStateManager.color(1.0F, 1.0F, 1.0F, f1);
            return 1;
        }
    }

    protected void renderEquippedItems(EntityDemon p_77029_1_, float p_77029_2_)
    {
        this.renderEquippedItems(p_77029_1_, p_77029_2_);
    }
    
    public static class Factory implements IRenderFactory<EntityDemon> {
    	@Override
    	public Render<? super EntityDemon> createRenderFor(RenderManager manager) {
    		return new RenderDemon(manager);
    	}
    }
}