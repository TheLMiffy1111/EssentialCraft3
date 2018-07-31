package essentialcraft.client.render.tile;

import DummyCore.Client.AdvancedModelLoader;
import DummyCore.Client.IModelCustom;
import essentialcraft.common.tile.TileDemonicPentacle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDemonicPentacle extends TileEntitySpecialRenderer<TileDemonicPentacle>
{
	public static final ResourceLocation rune = new ResourceLocation("essentialcraft:textures/models/demonicpentacle.png");
	public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation("essentialcraft:models/block/rune.obj"));

	public void doRender(TileDemonicPentacle p, double x, double y, double z, float partialTicks)
	{
		RenderHelper.disableStandardItemLighting();

		GlStateManager.pushMatrix();
		Minecraft.getMinecraft().renderEngine.bindTexture(rune);
		GlStateManager.translate(x+0.5F, y-0.2F, z+0.5F);

		if(p.tier == -1) {
			GlStateManager.popMatrix();
			return;
		}

		float movement = Minecraft.getMinecraft().world.getTotalWorldTime()%60F+partialTicks;

		if(movement > 30)
			movement = 30 - movement+30F;

		float c = movement/30F;
		if(c < 0.02F)c = 0.02F;
		if(c > 0.8F)c= 0.8F;

		if(p.tier == 0)
		{
			GlStateManager.color(c, 0, 0);
		}

		GlStateManager.rotate(Minecraft.getMinecraft().world.getTotalWorldTime()%360F, 0, 1, 0);
		model.renderAll();
		GlStateManager.popMatrix();
		RenderHelper.enableStandardItemLighting();
	}

	@Override
	public void render(TileDemonicPentacle tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		this.doRender(tile, x, y, z, partialTicks);
	}
}