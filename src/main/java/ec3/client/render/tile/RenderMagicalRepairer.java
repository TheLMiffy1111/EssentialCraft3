package ec3.client.render.tile;

import DummyCore.Client.AdvancedModelLoader;
import DummyCore.Client.IModelCustom;
import DummyCore.Utils.DrawUtils;
import DummyCore.Utils.MiscUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import ec3.common.tile.TileMagicalRepairer;

@SideOnly(Side.CLIENT)
public class RenderMagicalRepairer extends TileEntitySpecialRenderer<TileMagicalRepairer> {

	public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation("essentialcraft:models/block/MagicalRepairer.obj"));
	public static final ResourceLocation textures = new ResourceLocation("essentialcraft:textures/models/magicalRepairer.png");

	public RenderMagicalRepairer() {}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
	 * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
	 * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(TileMagicalRepairer tile, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
		RenderHelper.disableStandardItemLighting();
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(p_76986_2_,  p_76986_4_,  p_76986_6_);
			Minecraft.getMinecraft().renderEngine.bindTexture(textures);
			model.renderAll();
			GlStateManager.popMatrix();
		}

		float rotation = tile.getWorld().getWorldTime() % 360;

		GlStateManager.pushMatrix(); 
		DrawUtils.renderItemStack_Full(tile.getStackInSlot(1),tile.getPos().getX()+0.5D,tile.getPos().getY()+10D , tile.getPos().getZ()+0.5D, p_76986_2_, p_76986_4_, p_76986_6_, rotation,0F, 1, 1, 1, 0.5F, 0.65F,0.5F, false);
		GlStateManager.popMatrix();
		RenderHelper.enableStandardItemLighting();
	}

	@Override
	public void renderTileEntityAt(TileMagicalRepairer p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_, int destroyStage) {
		this.doRender(p_147500_1_, p_147500_2_, p_147500_4_, p_147500_6_, p_147500_8_, 0);
	}
}