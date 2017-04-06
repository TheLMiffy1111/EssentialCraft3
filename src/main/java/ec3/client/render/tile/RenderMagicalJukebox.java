package ec3.client.render.tile;

import DummyCore.Client.AdvancedModelLoader;
import DummyCore.Client.IModelCustom;
import DummyCore.Utils.DrawUtils;
import DummyCore.Utils.MiscUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import ec3.common.item.ItemsCore;
import ec3.common.tile.TileMagicalJukebox;

@SideOnly(Side.CLIENT)
public class RenderMagicalJukebox extends TileEntitySpecialRenderer {

	public RenderMagicalJukebox()
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
		float[][] coloring = new float[][]{{1,0,0},{1,0,1},{0,0,1},{0,1,1},{0,1,0},{1,1,0},{1,1,1}};


		TileMagicalJukebox tile = (TileMagicalJukebox) p_76986_1_;
		float rotation = 90;
		int currentSupposedTimingColor = (int) (tile.getWorld().getWorldTime() % (7*30));
		currentSupposedTimingColor/= 30;
		ItemStack is = tile.getStackInSlot(1);

		float upperIndex = tile.getWorld().getWorldTime() % 40;

		if(upperIndex < 20)
		{
			upperIndex = (20 - upperIndex);
		}else
		{
			upperIndex -= 20;
		}

		float upperIndex1 = tile.getWorld().getWorldTime() % 30;

		if(upperIndex1 < 15)
		{
			upperIndex1 = (15 - upperIndex1);
		}else
		{
			upperIndex1 -= 15;
		}

		if(is != null && is.getItem() == ItemsCore.record_secret && tile.recordCooldownTime > 0)
		{
			GlStateManager.color(coloring[currentSupposedTimingColor][0], coloring[currentSupposedTimingColor][1], coloring[currentSupposedTimingColor][2]);
		}

		RenderHelper.disableStandardItemLighting();

		GlStateManager.pushMatrix();
		GlStateManager.translate((float)p_76986_2_+0.5F, (float)p_76986_4_, (float)p_76986_6_+0.5F);

		if(is != null && is.getItem() == ItemsCore.record_secret && tile.recordCooldownTime > 0)
		{
			GlStateManager.scale(1.0F-(upperIndex/60F) , 1.0F-((upperIndex1)/40F), 1.0F-((20-upperIndex)/60F));
		}
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix(); 
		if(is != null && is.getItem() == ItemsCore.record_secret && tile.recordCooldownTime > 0)
			DrawUtils.renderItemStack_Full(tile.getStackInSlot(1),p_76986_1_.getPos().getX()+0.5D,p_76986_1_.getPos().getY()+10D , p_76986_1_.getPos().getZ()+0.5D, p_76986_2_, p_76986_4_, p_76986_6_, rotation,0F, 1, 1, 1, 0.5F, 0.65F-(upperIndex1)/40F,0.5F, false);
		else
			DrawUtils.renderItemStack_Full(tile.getStackInSlot(1),p_76986_1_.getPos().getX()+0.5D,p_76986_1_.getPos().getY()+10D , p_76986_1_.getPos().getZ()+0.5D, p_76986_2_, p_76986_4_, p_76986_6_, rotation,0F, 1, 1, 1, 0.5F, 0.65F,0.5F, false);
		GlStateManager.popMatrix();
		RenderHelper.enableStandardItemLighting();
	}

	@Override
	public void renderTileEntityAt(TileEntity p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_, int destroyStage) {
		if(p_147500_1_.getBlockMetadata() == 0)
			this.doRender((TileEntity) p_147500_1_, p_147500_2_, p_147500_4_, p_147500_6_, p_147500_8_, 0);
	}
}