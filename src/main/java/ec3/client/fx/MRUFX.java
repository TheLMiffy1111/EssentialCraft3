package ec3.client.fx;

import org.lwjgl.opengl.GL11;

import DummyCore.Utils.TessellatorWrapper;
import ec3.utils.common.ECUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class MRUFX extends Particle{

	private double mruPosX;
	private double mruPosY;
	private double mruPosZ;
	public float tickPos;
	private static final ResourceLocation particleTextures = new ResourceLocation("textures/particle/particles.png");
	private static final ResourceLocation ecparticleTextures = new ResourceLocation("essentialcraft","textures/special/particles.png");
	public MRUFX(World w, double x, double y,double z, double i, double j,double k) 
	{
		super(w, x, y, z, i, j,k);
		this.motionX = i;
		this.motionY = j;
		this.motionZ = k;
		this.mruPosX = this.posX = x;
		this.mruPosY = this.posY = y;
		this.mruPosZ = this.posZ = z;
		this.rand.nextFloat();
		this.particleScale = 0.5F;
		this.particleRed = 0;
		this.particleGreen = 0F;
		this.particleRed = 0.8F;
		this.particleMaxAge = (int)(Math.random() * 10.0D) + 40;
		this.canCollide = true;
		this.setParticleTextureIndex((int)(Math.random() * 8.0D));
	}

	public MRUFX(World w, double x, double y,double z, double i, double j,double k, double cR, double cG, double cB) 
	{
		this(w,x,y,z,i,j,k);
		this.particleRed = (float) cR;
		this.particleGreen = (float) cG;
		this.particleBlue = (float) cB;
	}

	public void renderParticle(VertexBuffer var1, Entity var2, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		TessellatorWrapper.getInstance().draw().begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
		Minecraft.getMinecraft().renderEngine.bindTexture(ecparticleTextures);
		GlStateManager.disableAlpha();
		boolean enabled = GL11.glIsEnabled(GL11.GL_BLEND);
		GlStateManager.enableBlend();
		if(ECUtils.canPlayerSeeMRU(Minecraft.getMinecraft().player))
		{
			float sc = this.particleScale;
			float cR = this.particleRed;
			float cG = this.particleGreen;
			float cB = this.particleBlue;
			float cA = this.particleAlpha;
			this.particleScale *= 1.5F;
			this.particleRed = 1;
			this.particleGreen = 0F;
			this.particleBlue = 1F;
			this.particleAlpha = 1F;
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			super.renderParticle(var1, var2, par2, par3, par4, par5, par6, par7);
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			this.particleScale = sc;
			this.particleRed =  cR;
			this.particleGreen =  cG;
			this.particleBlue =  cB;
			this.particleAlpha = cA;
			super.renderParticle(var1, var2, par2, par3, par4, par5, par6, par7);
		}
		TessellatorWrapper.getInstance().draw().begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
		Minecraft.getMinecraft().renderEngine.bindTexture(particleTextures);

		if(!enabled)
			GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
	}

	public int getBrightnessForRender(float p_70070_1_)
	{
		return 255;
	}

	/**
	 * Gets how bright this entity is.
	 */
	public float getBrightness(float p_70013_1_)
	{
		float f1 = super.getBrightnessForRender(p_70013_1_);
		float f2 = (float)this.particleAge / (float)this.particleMaxAge;
		f2 = f2 * f2 * f2 * f2;
		return f1 * (1.0F - f2) + f2;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate()
	{
		tickPos += 15+this.world.rand.nextFloat()*15;
		if(this.particleAge < this.particleMaxAge/2)
			this.setParticleTextureIndex(7 - this.particleAge * 8 / (this.particleMaxAge/2));
		else
			this.setParticleTextureIndex((this.particleAge-this.particleMaxAge/2) * 8 / (this.particleMaxAge/2));
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		float f = (float)this.particleAge / (float)this.particleMaxAge;
		this.posX = (this.mruPosX + this.motionX * (double)f) + Math.sin(Math.toRadians(tickPos + this.world.getWorldTime()*10))/10;
		this.posY = (this.mruPosY + this.motionY * (double)f) + Math.cos(Math.toRadians(tickPos + this.world.getWorldTime()*10))/10;
		this.posZ = (this.mruPosZ + this.motionZ * (double)f) - Math.sin(Math.toRadians(tickPos + this.world.getWorldTime()*10))/10;

		if (this.particleAge++ >= this.particleMaxAge)
		{
			this.setExpired();
		}
	}
}
