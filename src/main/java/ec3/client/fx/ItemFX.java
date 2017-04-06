package ec3.client.fx;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import DummyCore.Utils.TessellatorWrapper;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ItemFX extends Particle{

	public double red,green,blue;

	public ItemFX(World w, double x, double y,double z, double r, double g, double b, double mX, double mY, double mZ)
	{
		super(w, x, y, z, 0, 0, 0);
		red = r;
		green = g;
		blue = b;
		this.motionX = mX/20;
		this.motionY = mY/20;
		this.motionZ = mZ/20;
		this.particleMaxAge = 25;
	}

	public void renderParticle(VertexBuffer var1, Entity var2, float x, float y, float z, float u1, float u2, float u3)
	{
		TessellatorWrapper var3 = TessellatorWrapper.getInstance();
		this.canCollide = true;
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		float f11 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)x - interpPosX);
		float f12 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)x - interpPosY);
		float f13 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)x - interpPosZ);
		var3.draw();

		Random var6 = new Random((long) (this.posX*100+this.posY*100+this.posZ*100));
		GlStateManager.disableTexture2D();
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GlStateManager.pushMatrix();
		GlStateManager.translate(f11, f12, f13);
		int mru = 200;
		GlStateManager.scale(0.0000075F*mru, 0.0000075F*mru, 0.0000075F*mru);
		for (int var7 = 0; var7 < 100; ++var7)
		{
			var3.draw().begin(6, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
			GlStateManager.rotate(var6.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(var6.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(var6.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(var6.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(var6.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(var6.nextFloat() * 360.0F + 1 * 90.0F, 0.0F, 0.0F, 1.0F);
			float var8 = var6.nextFloat() * 20.0F + 5.0F + 1 * 10.0F;
			float var9 = var6.nextFloat() * 2.0F + 1.0F + 1 * 2.0F;
			var1.pos(0.0D, 0.0D, 0.0D).color((float)red, (float)green, (float)blue, 1F).endVertex();
			var1.pos(-0.866D * (double)var9, (double)var8, (double)(-0.5F * var9)).color((float)red, (float)green, (float)blue, 1F).endVertex();
			var1.pos(0.866D * (double)var9, (double)var8, (double)(-0.5F * var9)).color((float)red, (float)green, (float)blue, 1F).endVertex();
			var1.pos(0.0D, (double)var8, (double)(1.0F * var9)).color((float)red, (float)green, (float)blue, 1F).endVertex();
			var1.pos(-0.866D * (double)var9, (double)var8, (double)(-0.5F * var9)).color((float)red, (float)green, (float)blue, 1F).endVertex();
		}

		var3.draw().begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
		GlStateManager.popMatrix();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableBlend();
		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.enableTexture2D();
	}
}
