package essentialcraft.client.render.tile;

import essentialcraft.common.tile.TileMagicalChest;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMagicalChest extends TileEntitySpecialRenderer<TileMagicalChest>
{
	public static final ResourceLocation magicalTextures = new ResourceLocation("essentialcraft:textures/blocks/chests/magical.png");
	public static final ResourceLocation voidTextures = new ResourceLocation("essentialcraft:textures/blocks/chests/void.png");
	public static final ModelChest chest = new ModelChest();
	public static final ModelChest inventoryChest = new ModelChest();

	public void doRender(TileMagicalChest tile, double x, double y, double z, float partialTicks)
	{
		RenderHelper.disableStandardItemLighting();
		GlStateManager.pushMatrix();

		GlStateManager.enableRescaleNormal();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.translate((float)x, (float)y + 1.0F, (float)z + 1.0F);
		GlStateManager.scale(1.0F, -1.0F, -1.0F);
		GlStateManager.translate(0.5F, 0.5F, 0.5F);
		GlStateManager.rotate((float)tile.rotation*90+180, 0.0F, 1.0F, 0.0F);
		GlStateManager.translate(-0.5F, -0.5F, -0.5F);

		float f1 = tile.prevLidAngle + (tile.lidAngle - tile.prevLidAngle) * partialTicks;
		chest.chestLid.rotateAngleX = -(f1 * (float)Math.PI / 2.0F);
		this.bindTexture(tile.getBlockMetadata() == 0 ? magicalTextures : voidTextures);
		chest.renderAll();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void render(TileMagicalChest tile, double x,double y, double z, float partialTicks, int destroyStage, float alpha) {
		this.doRender(tile, x, y, z, partialTicks);
	}
}