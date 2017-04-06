package ec3.client.fx;

import org.lwjgl.opengl.GL11;

import DummyCore.Utils.TessellatorWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class SmokeFX extends ParticleSmokeNormal {
	private static final ResourceLocation particleTextures = new ResourceLocation("textures/particle/particles.png");
	private static final ResourceLocation ecparticleTextures = new ResourceLocation("essentialcraft","textures/special/particles.png");

	public SmokeFX(World w, double x, double y,	double z, double mX, double mY,	double mZ, float scale)
	{
		super(w, x, y, z, mX, mY,mZ, scale);
	}

	public SmokeFX(World w, double x, double y,	double z, double mX, double mY,	double mZ, float scale, double r, double g, double b)
	{
		super(w, x, y, z, mX, mY,mZ, scale);
		this.particleRed = (float) r;
		this.particleGreen = (float) g;
		this.particleBlue = (float) b;
	}

	public void renderParticle(VertexBuffer var1, Entity var2, float par2, float par3, float par4, float par5, float par6, float par7) {
		TessellatorWrapper.getInstance().draw().begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
		Minecraft.getMinecraft().renderEngine.bindTexture(ecparticleTextures);
		GlStateManager.pushMatrix();
		GlStateManager.disableAlpha();
		boolean enabled = GL11.glIsEnabled(GL11.GL_BLEND);
		GlStateManager.enableBlend();
		super.renderParticle(var1, var2, par2, par3, par4, par5, par6, par7);
		TessellatorWrapper.getInstance().draw().begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
		Minecraft.getMinecraft().renderEngine.bindTexture(particleTextures);
		if(!enabled)
			GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.popMatrix();
	}
}
