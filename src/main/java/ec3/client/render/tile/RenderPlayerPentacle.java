package ec3.client.render.tile;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import DummyCore.Client.AdvancedModelLoader;
import DummyCore.Client.IModelCustom;
import ec3.common.tile.TilePlayerPentacle;

@SideOnly(Side.CLIENT)
public class RenderPlayerPentacle extends TileEntitySpecialRenderer
{
	public static final ResourceLocation rune = new ResourceLocation("essentialcraft:textures/models/pentacle.png");
	public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation("essentialcraft:models/block/Rune.obj"));

	public RenderPlayerPentacle()
	{
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
	 * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
	 * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(TileEntity tile, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
	{
		RenderHelper.disableStandardItemLighting();
		TilePlayerPentacle p = (TilePlayerPentacle)tile;

		GlStateManager.pushMatrix();
		Minecraft.getMinecraft().renderEngine.bindTexture(rune);
		GlStateManager.translate(p_76986_2_+0.5F, p_76986_4_-0.2F, p_76986_6_+0.5F);

		if(p.tier == -1)
		{
			GlStateManager.color(0.2F, 0.2F, 0.2F);
		}
		if(p.tier == 0)
		{
			GlStateManager.color(0F, 1F, 0F);
		}
		if(p.tier == 1)
		{
			GlStateManager.color(0F, 0F, 1F);
		}
		if(p.tier == 2)
		{
			GlStateManager.color(0.5F, 0F, 0.5F);
		}
		if(p.tier == 3)
		{
			GlStateManager.color(1F, 0F, 0F);
		}

		if(p.tier != -1)
			GlStateManager.rotate(tile.getWorld().getWorldTime()%360, 0, 1, 0);
		model.renderPart("pPlane1");
		GlStateManager.popMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_, int destroyStage) {
		if(p_147500_1_.getBlockMetadata() == 0)
			this.doRender((TileEntity) p_147500_1_, p_147500_2_, p_147500_4_, p_147500_6_, p_147500_8_, 0);
	}
}