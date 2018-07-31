package essentialcraft.client.render.tile;

import DummyCore.Client.AdvancedModelLoader;
import DummyCore.Client.IModelCustom;
import essentialcraft.api.MagicianTableUpgrades;
import essentialcraft.common.tile.TileMagicianTable;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMagicianTable extends TileEntitySpecialRenderer<TileMagicianTable>
{
	public static final IModelCustom cube = AdvancedModelLoader.loadModel(new ResourceLocation("essentialcraft:models/block/cube.obj"));

	public void doRender(TileMagicianTable table, double x, double y, double z, float partialTicks)
	{
		RenderHelper.disableStandardItemLighting();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x+0.5F, (float)y, (float)z+0.5F);
		if(table.upgrade != -1)
		{
			this.bindTexture(MagicianTableUpgrades.UPGRADE_TEXTURES.get(table.upgrade));
			float scale = 0.99F;
			GlStateManager.translate(0, 0.005F, 0);
			GlStateManager.scale(scale, scale, scale);
			cube.renderAll();
		}
		GlStateManager.popMatrix();
		RenderHelper.enableStandardItemLighting();
	}

	@Override
	public void render(TileMagicianTable tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(tile.getBlockMetadata() == 0)
			this.doRender(tile, x, y, z, partialTicks);
	}
}