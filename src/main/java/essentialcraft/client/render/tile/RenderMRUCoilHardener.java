package essentialcraft.client.render.tile;

import DummyCore.Client.AdvancedModelLoader;
import DummyCore.Client.IModelCustom;
import essentialcraft.common.tile.TileMRUCoilHardener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMRUCoilHardener extends TileEntitySpecialRenderer<TileMRUCoilHardener>
{
	public static final ResourceLocation textures = new ResourceLocation("essentialcraft:textures/models/mrucoilhardener.png");
	public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation("essentialcraft:models/block/mrucoilhardener.obj"));

	public void doRender(TileMRUCoilHardener tile, double x, double y, double z, float partialTicks)
	{
		RenderHelper.disableStandardItemLighting();
		GlStateManager.pushMatrix();
		if(tile.localLightning != null)
			tile.localLightning.render(x, y, z, partialTicks);
		GlStateManager.popMatrix();
		RenderHelper.enableStandardItemLighting();
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(TileEntity entity)
	{
		return textures;
	}

	@Override
	public void render(TileMRUCoilHardener tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(tile.getBlockMetadata() == 0)
			this.doRender(tile, x, y, z, partialTicks);
	}

	@Override
	public boolean isGlobalRenderer(TileMRUCoilHardener te) {
		return true;
	}
}