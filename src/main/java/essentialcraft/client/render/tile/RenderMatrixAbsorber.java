package essentialcraft.client.render.tile;

import DummyCore.Utils.DrawUtils;
import essentialcraft.common.tile.TileMatrixAbsorber;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMatrixAbsorber extends TileEntitySpecialRenderer<TileMatrixAbsorber> {

	public void doRender(TileMatrixAbsorber tile, double x, double y, double z, float partialTicks) {
		RenderHelper.disableStandardItemLighting();

		float rotation = (tile.getWorld().getWorldTime()+partialTicks) % 360;
		float upperIndex = (tile.getWorld().getWorldTime()+partialTicks) % 360;

		if(upperIndex < 180) {
			upperIndex = 180 - upperIndex;
		}
		else {
			upperIndex -= 180;
		}

		rotation = rotation + 360F/(tile.getWorld().getWorldTime()+partialTicks) % 360;

		GlStateManager.pushMatrix();
		DrawUtils.renderItemStack_Full(tile.getStackInSlot(0), x, y, z, rotation,0F, 1, 1, 1, 0.5F, 0.25F+upperIndex/500F, 0.5F);
		GlStateManager.popMatrix();
		RenderHelper.enableStandardItemLighting();
	}

	@Override
	public void render(TileMatrixAbsorber tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(tile.getBlockMetadata() == 0)
			this.doRender(tile, x, y, z, partialTicks);
	}
}