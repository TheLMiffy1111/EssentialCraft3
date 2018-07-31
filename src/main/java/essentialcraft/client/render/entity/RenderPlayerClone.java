package essentialcraft.client.render.entity;

import java.util.UUID;

import org.lwjgl.opengl.GL11;

import essentialcraft.common.entity.EntityPlayerClone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPlayerClone extends RenderBiped<EntityPlayerClone> {
	private static ResourceLocation textures;
	private ModelBiped model;

	public RenderPlayerClone() {
		this(Minecraft.getMinecraft().getRenderManager());
	}

	public RenderPlayerClone(RenderManager rm) {
		super(rm, new ModelBiped(0,0,64,64), 0.5F);
		this.model = (ModelBiped)super.mainModel;
		this.addLayer(new LayerBipedArmor(this));
	}

	@Override
	protected void preRenderCallback(EntityPlayerClone entity, float partialTicks) {
		float s = 1.0F;
		GlStateManager.scale(s, s, s);

		GlStateManager.disableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GlStateManager.color(1, 1, 1, 0.2F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityPlayerClone entity) {
		return textures;
	}

	@Override
	public void doRender(EntityPlayerClone entity, double x, double y, double z, float entityYaw, float partialTicks) {
		textures = DefaultPlayerSkin.getDefaultSkinLegacy();
		UUID playerId = entity.getClonedPlayer();
		if(playerId != null) {
			textures = Minecraft.getMinecraft().getConnection().getPlayerInfo(playerId).getLocationSkin();
		}

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	public static class Factory implements IRenderFactory<EntityPlayerClone> {
		@Override
		public Render<? super EntityPlayerClone> createRenderFor(RenderManager manager) {
			return new RenderPlayerClone(manager);
		}
	}
}
