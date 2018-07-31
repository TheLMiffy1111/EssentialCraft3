package essentialcraft.client.render.tile;

import DummyCore.Client.AdvancedModelLoader;
import DummyCore.Client.IModelCustom;
import essentialcraft.common.tile.TileMIM;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMIM extends TileEntitySpecialRenderer<TileMIM>
{
	public static final ResourceLocation textures = new ResourceLocation("essentialcraft:textures/blocks/mimcube.png");
	public static final ResourceLocation vtextures = new ResourceLocation("essentialcraft:textures/blocks/voidstone.png");
	public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation("essentialcraft:models/block/mim.obj"));

	public void doRender(TileMIM t, double x, double y, double z, float partialTicks)
	{
		RenderHelper.disableStandardItemLighting();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x+0.5F, (float)y, (float)z+0.5F);
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
		this.bindTexture(vtextures);
		model.renderPart("Cube.001_Cube.002");
		model.renderPart("Cube_Cube.001");

		GlStateManager.rotate(t.innerRotation+partialTicks, 0, 1, 0);

		this.bindTexture(textures);
		model.renderPart("Cube.002_Cube.003");

		GlStateManager.popMatrix();
		RenderHelper.enableStandardItemLighting();
	}

	@Override
	public void render(TileMIM tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		this.doRender(tile, x, y, z, partialTicks);
	}
}