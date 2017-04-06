package ec3.client.render.entity;

import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ec3.common.entity.EntityPlayerClone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.UUID;

import org.lwjgl.opengl.GL11;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import DummyCore.Utils.MiscUtils;

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

	protected void preRenderCallback(EntityPlayerClone p_77041_1_, float p_77041_2_) {
		float s = 1.0F;
		GlStateManager.scale(s, s, s);

		GlStateManager.disableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GlStateManager.color(1, 1, 1, 0.2F);
	}

	protected ResourceLocation getEntityTexture(EntityPlayerClone p_110775_1_) {
		return textures;
	}

	public void doRender(EntityPlayerClone p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
		textures = DefaultPlayerSkin.getDefaultSkinLegacy();
		UUID playerId = p_76986_1_.getClonedPlayer();
		if(playerId != null) {
			textures = Minecraft.getMinecraft().getConnection().getPlayerInfo(playerId).getLocationSkin();
		}

		super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}

	public static class Factory implements IRenderFactory<EntityPlayerClone> {
		@Override
		public Render<? super EntityPlayerClone> createRenderFor(RenderManager manager) {
			return new RenderPlayerClone(manager);
		}
	}
}
