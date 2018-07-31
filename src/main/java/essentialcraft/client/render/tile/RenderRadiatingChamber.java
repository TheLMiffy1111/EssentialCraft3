package essentialcraft.client.render.tile;

import DummyCore.Utils.DrawUtils;
import essentialcraft.common.tile.TileRadiatingChamber;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderRadiatingChamber extends TileEntitySpecialRenderer<TileRadiatingChamber> {

	public void doRender(TileRadiatingChamber tile, double x, double y, double z, float partialTicks) {
		RenderHelper.disableStandardItemLighting();

		GlStateManager.pushMatrix();

		DrawUtils.renderItemStack_Full(tile.getStackInSlot(1), x, y, z, 0F, 90F, 1, 1, 1, 0.5F, 0.875F, 0.5F);
		GlStateManager.popMatrix();
		RenderHelper.enableStandardItemLighting();
	}

	@Override
	public void render(TileRadiatingChamber tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		this.doRender(tile, x, y, z, partialTicks);
	}
}