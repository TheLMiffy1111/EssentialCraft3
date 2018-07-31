package essentialcraft.client.render.tile;

import DummyCore.Client.AdvancedModelLoader;
import DummyCore.Client.IModelCustom;
import DummyCore.Utils.Lightning;
import essentialcraft.common.tile.TileMRUReactor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMRUReactor extends TileEntitySpecialRenderer<TileMRUReactor>
{

	public static final ResourceLocation textures = new ResourceLocation("essentialcraft:textures/models/flowerburner.png");
	public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation("essentialcraft:models/block/mrureactor_btm.obj"));

	public static final ResourceLocation stextures = new ResourceLocation("essentialcraft:textures/models/sphere.png");
	public static final IModelCustom smodel = AdvancedModelLoader.loadModel(new ResourceLocation("essentialcraft:models/block/sphere.obj"));

	public void doRender(TileMRUReactor tile, double x, double y, double z, float partialTicks)
	{
		RenderHelper.disableStandardItemLighting();

		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x+0.5F, (float)y, (float)z+0.5F);
		this.bindTexture(stextures);
		if(!tile.isStructureCorrect)
		{
			GlStateManager.color(0.4F, 0.4F, 0.4F);
			GlStateManager.scale(0.55F, 0.55F, 0.55F);
			smodel.renderAll();
		}else
		{
			float wTime = 0F;
			GlStateManager.translate(0, 0.5F+wTime, 0);
			GlStateManager.scale(0.55F, 0.55F, 0.55F);
			smodel.renderAll();
		}

		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		if(tile.isStructureCorrect())
			for(int i = 0; i < tile.lightnings.size(); ++i)
			{
				Lightning l = tile.lightnings.get(i);
				l.render(x, y, z, partialTicks);
			}
		GlStateManager.popMatrix();

		RenderHelper.enableStandardItemLighting();
	}

	@Override
	public void render(TileMRUReactor tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		this.doRender(tile, x, y, z, partialTicks);
	}
}