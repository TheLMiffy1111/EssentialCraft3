package essentialcraft.client.render.tile;

import DummyCore.Utils.DrawUtils;
import essentialcraft.common.tile.TileChargingChamber;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderChargingChamber extends TileEntitySpecialRenderer<TileChargingChamber> {

	public void doRender(TileChargingChamber tile, double x, double y, double z, float partialTicks) {
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
		DrawUtils.renderItemStack_Full(tile.getStackInSlot(1), x, y, z, rotation, 0F, 1, 1, 1, 0.5F, 0.65F+upperIndex/500F,0.5F);
		GlStateManager.popMatrix();
		RenderHelper.enableStandardItemLighting();
	}

	@Override
	public void render(TileChargingChamber tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		this.doRender(tile, x, y, z, partialTicks);
	}
}