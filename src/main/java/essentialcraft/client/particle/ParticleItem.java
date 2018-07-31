package essentialcraft.client.particle;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ParticleItem extends Particle {

	static final float HALF_SQRT_3 = 0.8660254F;
	public double red,green,blue;

	public ParticleItem(World w, double x, double y,double z, double r, double g, double b, double mX, double mY, double mZ)
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

	@Override
	public void renderParticle(BufferBuilder var1, Entity var2, float x, float y, float z, float u1, float u2, float u3) {
		this.canCollide = true;
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		float f11 = (float)(this.prevPosX + (this.posX - this.prevPosX) * x - interpPosX);
		float f12 = (float)(this.prevPosY + (this.posY - this.prevPosY) * x - interpPosY);
		float f13 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * x - interpPosZ);

		Random var6 = new Random((long) (this.posX*100+this.posY*100+this.posZ*100));
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.pushMatrix();
		GlStateManager.translate(f11, f12, f13);
		int mru = 200;
		GlStateManager.scale(0.0000075F*mru, 0.0000075F*mru, 0.0000075F*mru);
		GlStateManager.color((float)red, (float)green, (float)blue, 1F);
		for(int var7 = 0; var7 < 100; ++var7) {
			//GlStateManager.rotate(var6.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			//GlStateManager.rotate(var6.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			//GlStateManager.rotate(var6.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(var6.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(var6.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(var6.nextFloat() * 360.0F + 1 * 90.0F, 0.0F, 0.0F, 1.0F);
			float var8 = var6.nextFloat() * 20F + 15F;
			float var9 = var6.nextFloat() * 2F + 3F;
			GlStateManager.glBegin(GL11.GL_TRIANGLE_FAN);
			GlStateManager.glVertex3f(0, 0, 0);
			GlStateManager.glVertex3f(-HALF_SQRT_3*var9, var8, -var9/2F);
			GlStateManager.glVertex3f(HALF_SQRT_3*var9, var8, -var9/2F);
			GlStateManager.glVertex3f(0, var8, var9);
			GlStateManager.glVertex3f(-HALF_SQRT_3*var9, var8, -var9/2F);
			GlStateManager.glEnd();
		}

		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.popMatrix();
		GlStateManager.shadeModel(GL11.GL_FLAT);
	}
}
