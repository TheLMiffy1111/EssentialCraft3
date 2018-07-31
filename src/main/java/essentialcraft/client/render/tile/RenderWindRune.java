package essentialcraft.client.render.tile;

import DummyCore.Client.AdvancedModelLoader;
import DummyCore.Client.IModelCustom;
import essentialcraft.common.tile.TileWindRune;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderWindRune extends TileEntitySpecialRenderer<TileWindRune>
{
	public static final ResourceLocation rune = new ResourceLocation("essentialcraft:textures/models/windrune.png");
	public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation("essentialcraft:models/block/rune.obj"));

	public void doRender(TileWindRune p, double x, double y, double z, float partialTicks) {
		RenderHelper.disableStandardItemLighting();

		GlStateManager.pushMatrix();
		Minecraft.getMinecraft().renderEngine.bindTexture(rune);
		GlStateManager.translate(x+0.5F, y-0.2F, z+0.5F);

		if(p.tier == -1) {
			GlStateManager.popMatrix();
			return;
		}

		float movement = (p.getWorld().getWorldTime()+partialTicks)%60+partialTicks;

		if(movement > 30)
			movement = 60F - movement;

		float c = movement/30F;
		if(c < 0.2F)c = 0.2F;

		if(p.tier == 0) {
			GlStateManager.color(c, c, c);
		}

		GlStateManager.rotate(45, 0, 1, 0);

		model.renderPart("pPlane1");
		GlStateManager.popMatrix();
	}

	@Override
	public void render(TileWindRune tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		this.doRender(tile, x, y, z, partialTicks);
	}

	@Override
	public boolean isGlobalRenderer(TileWindRune te) {
		return true;
	}
}