package essentialcraft.client.render.tile;

import DummyCore.Utils.DrawUtils;
import essentialcraft.common.tile.TileMonsterHarvester;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMonsterHarvester extends TileEntitySpecialRenderer<TileMonsterHarvester> {

	public void doRender(TileMonsterHarvester tile, double x, double y, double z, float partialTicks)
	{
		RenderHelper.disableStandardItemLighting();

		float rotation = (tile.getWorld().getWorldTime()+partialTicks) % 360;

		GlStateManager.pushMatrix();
		DrawUtils.renderItemStack_Full(tile.getStackInSlot(2), x, y, z, rotation,0F, 1, 1, 1, 0.5F, 0.95F,0.5F);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		DrawUtils.renderItemStack_Full(tile.getStackInSlot(1), x, y, z, rotation,0F, 1, 1, 1, 0.3F, 1.15F,0.3F);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		DrawUtils.renderItemStack_Full(tile.getStackInSlot(3), x, y, z, rotation,0F, 1, 1, 1, 0.7F, 1.15F,0.3F);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		DrawUtils.renderItemStack_Full(tile.getStackInSlot(4), x, y, z, rotation,0F, 1, 1, 1, 0.3F, 1.15F,0.7F);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		DrawUtils.renderItemStack_Full(tile.getStackInSlot(5), x, y, z, rotation,0F, 1, 1, 1, 0.7F, 1.15F,0.7F);
		GlStateManager.popMatrix();
		RenderHelper.enableStandardItemLighting();
	}

	@Override
	public void render(TileMonsterHarvester tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(tile.getBlockMetadata() == 0)
			this.doRender(tile, x, y, z, partialTicks);
	}
}