package ec3.client.render.entity;

import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import ec3.common.entity.EntityWindMage;

@SideOnly(Side.CLIENT)
public class RenderWindMage extends RenderLiving<EntityWindMage> {
	private static final ResourceLocation apprenticeTextures = new ResourceLocation("essentialcraft","textures/entities/windMage_apprentice.png");
	private static final ResourceLocation normalTextures = new ResourceLocation("essentialcraft","textures/entities/windMage.png");
	private static final ResourceLocation archmageTextures = new ResourceLocation("essentialcraft","textures/entities/windMage_archmage.png");

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

	protected ResourceLocation getEntityTexture(EntityWindMage p_110775_1_) {
		switch (p_110775_1_.getType()) {
		case 1:
			return normalTextures;
		case 2:
			return archmageTextures;
		default:
			return apprenticeTextures;
		}
	}

	protected void preRenderCallback(EntityWindMage p_77041_1_, float p_77041_2_) {
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