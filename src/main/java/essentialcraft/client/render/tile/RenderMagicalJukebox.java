package essentialcraft.client.render.tile;

import DummyCore.Utils.DrawUtils;
import essentialcraft.common.item.ItemsCore;
import essentialcraft.common.tile.TileMagicalJukebox;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMagicalJukebox extends TileEntitySpecialRenderer<TileMagicalJukebox> {

	public void doRender(TileMagicalJukebox tile, double x, double y, double z, float partialTicks)
	{
		float[][] coloring = {{1,0,0},{1,0,1},{0,0,1},{0,1,1},{0,1,0},{1,1,0},{1,1,1}};

		float rotation = 90;
		int currentSupposedTimingColor = (int) (tile.getWorld().getWorldTime() % (7*30));
		currentSupposedTimingColor/= 30;
		ItemStack is = tile.getStackInSlot(1);

		float upperIndex = (tile.getWorld().getWorldTime()+partialTicks) % 40;

		if(upperIndex < 20)
		{
			upperIndex = 20 - upperIndex;
		}else
		{
			upperIndex -= 20;
		}

		float upperIndex1 = (tile.getWorld().getWorldTime()+partialTicks) % 30;

		if(upperIndex1 < 15)
		{
			upperIndex1 = 15 - upperIndex1;
		}else
		{
			upperIndex1 -= 15;
		}

		if(is.getItem() == ItemsCore.record_secret && tile.recordCooldownTime > 0)
		{
			GlStateManager.color(coloring[currentSupposedTimingColor][0], coloring[currentSupposedTimingColor][1], coloring[currentSupposedTimingColor][2]);
		}

		RenderHelper.disableStandardItemLighting();

		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x+0.5F, (float)y, (float)z+0.5F);

		if(is.getItem() == ItemsCore.record_secret && tile.recordCooldownTime > 0)
		{
			GlStateManager.scale(1.0F-upperIndex/60F , 1.0F-upperIndex1/40F, 1.0F-(20-upperIndex)/60F);
		}
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		if(is.getItem() == ItemsCore.record_secret && tile.recordCooldownTime > 0)
			DrawUtils.renderItemStack_Full(tile.getStackInSlot(1), x, y, z, rotation,0F, 1, 1, 1, 0.5F, 0.65F-upperIndex1/40F,0.5F);
		else
			DrawUtils.renderItemStack_Full(tile.getStackInSlot(1), x, y, z, rotation,0F, 1, 1, 1, 0.5F, 0.65F,0.5F);
		GlStateManager.popMatrix();
		RenderHelper.enableStandardItemLighting();
	}

	@Override
	public void render(TileMagicalJukebox tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(tile.getBlockMetadata() == 0)
			this.doRender(tile, x, y, z, partialTicks);
	}
}