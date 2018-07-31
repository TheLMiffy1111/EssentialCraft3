package essentialcraft.client.render.tile;

import essentialcraft.common.tile.TileCrystalFormer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCrystalFormer extends TileEntitySpecialRenderer<TileCrystalFormer>
{
	public void doRender(TileCrystalFormer tile, double x, double y, double z, float partialTicks)
	{
		RenderHelper.disableStandardItemLighting();

		float scale = 0.5F;

		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x+0.8F, (float)y+1.1F, (float)z+0.5F);
		GlStateManager.scale(scale, scale, scale);
		GlStateManager.rotate(180, 1, 0, 0);
		this.bindTexture(RenderElementalCrystal.neutral);
		RenderElementalCrystal.crystal.renderModel(0.0625F);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x+0.2F, (float)y+1.1F, (float)z+0.5F);
		GlStateManager.scale(scale, scale, scale);
		GlStateManager.rotate(180, 1, 0, 0);
		this.bindTexture(RenderElementalCrystal.neutral);
		RenderElementalCrystal.crystal.renderModel(0.0625F);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x+0.5F, (float)y+1.1F, (float)z+0.8F);
		GlStateManager.scale(scale, scale, scale);
		GlStateManager.rotate(180, 1, 0, 0);
		this.bindTexture(RenderElementalCrystal.neutral);
		RenderElementalCrystal.crystal.renderModel(0.0625F);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x+0.5F, (float)y+1.1F, (float)z+0.2F);
		GlStateManager.scale(scale, scale, scale);
		GlStateManager.rotate(180, 1, 0, 0);
		this.bindTexture(RenderElementalCrystal.neutral);
		RenderElementalCrystal.crystal.renderModel(0.0625F);
		GlStateManager.popMatrix();

		RenderHelper.enableStandardItemLighting();
	}

	@Override
	public void render(TileCrystalFormer tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(tile.getBlockMetadata() == 0)
			this.doRender(tile, x, y, z, partialTicks);
	}
}