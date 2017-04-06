package ec3.client.render.tile;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import DummyCore.Client.AdvancedModelLoader;
import DummyCore.Client.IModelCustom;
import ec3.common.tile.TileMagicalEnchanter;

@SideOnly(Side.CLIENT)
public class RenderMagicalEnchanter extends TileEntitySpecialRenderer
{
	public static final ResourceLocation bookTextures = new ResourceLocation("textures/entity/enchanting_table_book.png");
	public static final ModelBook book = new ModelBook();

	public RenderMagicalEnchanter()
	{
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
	 * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
	 * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(TileEntity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
	{
		RenderHelper.disableStandardItemLighting();
		TileMagicalEnchanter p_147500_1_ = (TileMagicalEnchanter) p_76986_1_;

		GlStateManager.pushMatrix();
		GlStateManager.translate((float)p_76986_2_ + 0.5F, (float)p_76986_4_ + 0.6F, (float)p_76986_6_ + 0.5F);
		float f1 = (float)p_147500_1_.field_145926_a + p_76986_8_;
		GlStateManager.translate(0.0F, 0.1F + MathHelper.sin(f1 * 0.1F) * 0.01F, 0.0F);
		float f2;

		for (f2 = p_147500_1_.field_145928_o - p_147500_1_.field_145925_p; f2 >= (float)Math.PI; f2 -= ((float)Math.PI * 2F))
		{

		}

		while (f2 < -(float)Math.PI)
		{
			f2 += ((float)Math.PI * 2F);
		}

		float f3 = p_147500_1_.field_145925_p + f2 * p_76986_8_;
		GlStateManager.rotate(-f3 * 180.0F / (float)Math.PI, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(80.0F, 0.0F, 0.0F, 1.0F);
		this.bindTexture(bookTextures);
		float f4 = p_147500_1_.field_145931_j + (p_147500_1_.field_145933_i - p_147500_1_.field_145931_j) * p_76986_8_ + 0.25F;
		float f5 = p_147500_1_.field_145931_j + (p_147500_1_.field_145933_i - p_147500_1_.field_145931_j) * p_76986_8_ + 0.75F;
		f4 = (f4 - (float)MathHelper.fastFloor((double)f4)) * 1.6F - 0.3F;
		f5 = (f5 - (float)MathHelper.fastFloor((double)f5)) * 1.6F - 0.3F;

		if (f4 < 0.0F)
		{
			f4 = 0.0F;
		}

		if (f5 < 0.0F)
		{
			f5 = 0.0F;
		}

		if (f4 > 1.0F)
		{
			f4 = 1.0F;
		}

		if (f5 > 1.0F)
		{
			f5 = 1.0F;
		}

		float f6 = p_147500_1_.field_145927_n + (p_147500_1_.field_145930_m - p_147500_1_.field_145927_n) * p_76986_8_;
		GlStateManager.enableCull();
		book.render((Entity)null, f1, f4, f5, f6, 0.0F, 0.0625F);
		GlStateManager.popMatrix();
		RenderHelper.enableStandardItemLighting();
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(TileEntity p_110775_1_)
	{
		return bookTextures;
	}

	@Override
	public void renderTileEntityAt(TileEntity p_147500_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, int destroyStage) {
		if(p_147500_1_.getBlockMetadata() == 0)
			this.doRender((TileEntity) p_147500_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, 0);
	}
}