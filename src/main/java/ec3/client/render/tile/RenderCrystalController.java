package ec3.client.render.tile;

import DummyCore.Client.AdvancedModelLoader;
import DummyCore.Client.IModelCustom;
import ec3.common.tile.TileCrystalController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderCrystalController extends TileEntitySpecialRenderer<TileCrystalController> {
	public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation("essentialcraft:models/block/CrystalController.obj"));
	public static final ResourceLocation textures = new ResourceLocation("essentialcraft:textures/models/crystalController.png");

	@Override
	public void renderTileEntityAt(TileCrystalController te, double x, double y, double z, float partialTicks, int destroyStage) {
		RenderHelper.disableStandardItemLighting();
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		Minecraft.getMinecraft().renderEngine.bindTexture(textures);
		model.renderAll();
		GlStateManager.popMatrix();
		RenderHelper.enableStandardItemLighting();
	}
}
