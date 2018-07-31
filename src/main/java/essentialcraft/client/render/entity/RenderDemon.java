package essentialcraft.client.render.entity;

import org.lwjgl.opengl.GL11;

import essentialcraft.client.model.ModelDemon;
import essentialcraft.common.entity.EntityDemon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

	@Override
	protected void preRenderCallback(EntityDemon entity, float partialTicks)
	{
		float s = 1.4F;
		GlStateManager.scale(s, s, s);
	}

	@Override
	public void doRender(EntityDemon entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(EntityDemon entity) {
		return endermanTextures;
	}

	protected int shouldRenderPass(EntityDemon partialTicks, int p_77032_2_, float p_77032_3_)
	{
		if(p_77032_2_ != 0)
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

			if (partialTicks.isInvisible())
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
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
			GlStateManager.enableLighting();
			GlStateManager.color(1.0F, 1.0F, 1.0F, f1);
			return 1;
		}
	}

	public static class Factory implements IRenderFactory<EntityDemon> {
		@Override
		public Render<? super EntityDemon> createRenderFor(RenderManager manager) {
			return new RenderDemon(manager);
		}
	}
}