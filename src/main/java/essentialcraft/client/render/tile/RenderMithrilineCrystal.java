package essentialcraft.client.render.tile;

import org.lwjgl.opengl.GL11;

import DummyCore.Client.AdvancedModelLoader;
import DummyCore.Client.IModelCustom;
import essentialcraft.common.tile.TileMithrilineCrystal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMithrilineCrystal extends TileEntitySpecialRenderer<TileMithrilineCrystal>
{
	public static final ResourceLocation textures_mithriline = new ResourceLocation("essentialcraft:textures/models/mithrilinecrystal.png");
	public static final ResourceLocation textures_pale = new ResourceLocation("essentialcraft:textures/models/palecrystal.png");
	public static final ResourceLocation textures_void = new ResourceLocation("essentialcraft:textures/models/voidcrystal.png");
	public static final ResourceLocation textures_demonic = new ResourceLocation("essentialcraft:textures/models/demoniccrystal.png");
	public static final ResourceLocation textures_shade = new ResourceLocation("essentialcraft:textures/models/shadecrystal.png");
	public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation("essentialcraft:models/block/mithrilinecrystal.obj"));

	public void doRender(TileMithrilineCrystal tile, double x, double y, double z, float partialTicks)
	{
		int meta = tile.getBlockMetadata();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();
		GlStateManager.disableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1, 1, 1, 0.9F);
		float time = (tile.getWorld().getWorldTime()+partialTicks)%45*8;
		float movement = (tile.getWorld().getWorldTime()+partialTicks)%60;

		if(movement > 30)
			movement = 30 - movement+30F;

		GlStateManager.translate((float)x+0.5F, (float)y+movement/30, (float)z+0.5F);
		GlStateManager.rotate(time, 0, 1, 0);
		GlStateManager.scale(2, 2, 2);
		this.bindTexture(meta == 0 ? textures_mithriline : meta == 3 ? textures_pale : meta == 6 ? textures_void : meta == 9 ? textures_demonic : textures_shade);
		model.renderAll();
		GlStateManager.enableLighting();
		GlStateManager.enableAlpha();
		GlStateManager.popMatrix();

		RenderHelper.enableStandardItemLighting();
	}

	@Override
	public void render(TileMithrilineCrystal tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(tile.getBlockMetadata()%3 == 0)
			this.doRender(tile, x, y, z, partialTicks);
	}
}