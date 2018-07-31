package essentialcraft.client.render.tile;

import DummyCore.Client.AdvancedModelLoader;
import DummyCore.Client.IModelCustom;
import essentialcraft.common.tile.TilePlayerPentacle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPlayerPentacle extends TileEntitySpecialRenderer<TilePlayerPentacle>
{
	public static final ResourceLocation rune = new ResourceLocation("essentialcraft:textures/models/pentacle.png");
	public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation("essentialcraft:models/block/rune.obj"));

	public void doRender(TilePlayerPentacle p, double x, double y, double z, float partialTicks)
	{
		RenderHelper.disableStandardItemLighting();

		GlStateManager.pushMatrix();
		Minecraft.getMinecraft().renderEngine.bindTexture(rune);
		GlStateManager.translate(x+0.5F, y-0.2F, z+0.5F);

		if(p.tier == -1)
		{
			GlStateManager.color(0.2F, 0.2F, 0.2F);
		}
		if(p.tier == 0)
		{
			GlStateManager.color(0F, 1F, 0F);
		}
		if(p.tier == 1)
		{
			GlStateManager.color(0F, 0F, 1F);
		}
		if(p.tier == 2)
		{
			GlStateManager.color(0.5F, 0F, 0.5F);
		}
		if(p.tier == 3)
		{
			GlStateManager.color(1F, 0F, 0F);
		}

		if(p.tier != -1)
			GlStateManager.rotate(p.getWorld().getTotalWorldTime()%360, 0, 1, 0);
		model.renderPart("pPlane1");
		GlStateManager.popMatrix();
	}

	@Override
	public void render(TilePlayerPentacle tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(tile.getBlockMetadata() == 0)
			this.doRender(tile, x, y, z, partialTicks);
	}
}