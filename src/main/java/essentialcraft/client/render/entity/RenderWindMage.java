package essentialcraft.client.render.entity;

import essentialcraft.common.entity.EntityWindMage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderWindMage extends RenderLiving<EntityWindMage> {
	private static final ResourceLocation APPRENTICE_TEXTURES = new ResourceLocation("essentialcraft","textures/entities/windMage_apprentice.png");
	private static final ResourceLocation NORMAL_TEXTURES = new ResourceLocation("essentialcraft","textures/entities/windMage.png");
	private static final ResourceLocation ARCHMAGE_TEXTURES = new ResourceLocation("essentialcraft","textures/entities/windMage_archmage.png");

	protected ModelBiped villagerModel;

	public RenderWindMage() {
		super(Minecraft.getMinecraft().getRenderManager(), new ModelBiped(0.0F), 0.5F);
		this.villagerModel = (ModelBiped)this.mainModel;
		this.addLayer(new LayerBipedArmor(this));
	}

	public RenderWindMage(RenderManager rm) {
		super(rm, new ModelBiped(0.0F), 0.5F);
		this.villagerModel = (ModelBiped)this.mainModel;
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityWindMage entity) {
		switch(entity.getType()) {
		case 0:
		default:
			return APPRENTICE_TEXTURES;
		case 1:
			return NORMAL_TEXTURES;
		case 2:
			return ARCHMAGE_TEXTURES;
		}
	}

	@Override
	protected void preRenderCallback(EntityWindMage entity, float partialTicks) {
		float f1 = 0.9375F;
		GlStateManager.scale(f1, f1, f1);
	}

	public static class Factory implements IRenderFactory<EntityWindMage> {
		@Override
		public Render<? super EntityWindMage> createRenderFor(RenderManager manager) {
			return new RenderWindMage(manager);
		}
	}
}