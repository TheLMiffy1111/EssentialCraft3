package essentialcraft.client.render.entity;

import essentialcraft.common.entity.EntityPoisonFume;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPoisonFume extends RenderLiving<EntityPoisonFume> {
	private static final ResourceLocation villagerTextures = new ResourceLocation("essentialcraft","textures/entities/windMage_apprentice.png");

	public RenderPoisonFume() {
		super(Minecraft.getMinecraft().getRenderManager(), new ModelBiped(0.0F), 0.5F);
	}

	public RenderPoisonFume(RenderManager rm) {
		super(rm, new ModelBiped(0.0F), 0.5F);
	}

	@Override
	public void doRender(EntityPoisonFume entity, double x, double y, double z, float entityYaw, float partialTicks) {}

	@Override
	protected ResourceLocation getEntityTexture(EntityPoisonFume entity) {
		return villagerTextures;
	}

	@Override
	protected void preRenderCallback(EntityPoisonFume entity, float partialTicks) {
		float f1 = 0.9375F;

		GlStateManager.scale(f1, f1, f1);
	}

	public static class Factory implements IRenderFactory<EntityPoisonFume> {
		@Override
		public Render<? super EntityPoisonFume> createRenderFor(RenderManager manager) {
			return new RenderPoisonFume(manager);
		}
	}
}