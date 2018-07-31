package essentialcraft.client.render.tile;

import org.lwjgl.opengl.GL11;

import DummyCore.Utils.DrawUtils;
import DummyCore.Utils.TessellatorWrapper;
import essentialcraft.common.tile.TileCorruptionCleaner;
import essentialcraft.utils.common.PlayerTickHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCorruptionCleaner extends TileEntitySpecialRenderer<TileCorruptionCleaner>
{
	public void doRender(TileCorruptionCleaner tile, double x, double y, double z, float partialTicks) {
		RenderHelper.disableStandardItemLighting();
		if(tile.cleared == null) {}
		else {
			GlStateManager.pushMatrix();
			float[] o = {tile.cleared.getX(),tile.cleared.getY()+1.45F,tile.cleared.getZ()};
			GlStateManager.popMatrix();

			float f21 = 0 + partialTicks;
			float f31 = MathHelper.sin(f21 * 0.2F) / 2.0F + 0.5F;
			f31 = (f31 * f31 + f31) * 0.2F;
			float f4;
			float f5;
			float f6;
			GlStateManager.pushMatrix();
			f4 = o[0] - tile.getPos().getX();
			f5 = (float)(o[1] - (double)(f31 + tile.getPos().getY()+1.3F));
			f6 = o[2] - tile.getPos().getZ();
			GlStateManager.translate((float)x+0.5F, (float)y + 0.5F, (float)z+0.5F);
			float f7 = MathHelper.sqrt(f4 * f4 + f6 * f6);
			float f8 = MathHelper.sqrt(f4 * f4 + f5 * f5 + f6 * f6);
			GlStateManager.rotate((float)-Math.atan2(f6, f4) * 180.0F / (float)Math.PI - 90.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate((float)-Math.atan2(f7, f5) * 180.0F / (float)Math.PI - 90.0F, 1.0F, 0.0F, 0.0F);
			TessellatorWrapper tessellator = TessellatorWrapper.getInstance();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
			GlStateManager.disableCull();
			DrawUtils.bindTexture("essentialcraft","textures/special/mru_beam.png");
			GlStateManager.shadeModel(GL11.GL_SMOOTH);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			GlStateManager.disableAlpha();
			float f9 = 1;
			float f10 = MathHelper.sqrt(f4 * f4 + f5 * f5 + f6 * f6) / 32.0F - (PlayerTickHandler.tickAmount + partialTicks) * 0.1F;
			GlStateManager.color(0.0F, 1.0F, 1.0F, 1F);
			tessellator.startDrawing(5);
			byte b0 = 8;

			for(int i = 0; i <= b0; ++i) {
				float f11 = MathHelper.sin(i % b0 * (float)Math.PI * 2.0F / b0) * 0.75F * 0.1F;
				float f12 = MathHelper.cos(i % b0 * (float)Math.PI * 2.0F / b0) * 0.75F * 0.1F;
				float f13 = i % b0 * 1.0F / b0;
				tessellator.addVertexWithUV(f11, f12, 0.0D, f13, f10);
				tessellator.addVertexWithUV(f11, f12, f8, f13, f9);
			}

			tessellator.draw();
			GlStateManager.enableCull();
			GlStateManager.disableBlend();
			GlStateManager.shadeModel(GL11.GL_FLAT);
			GlStateManager.enableAlpha();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.popMatrix();
		}
		RenderHelper.enableStandardItemLighting();
	}

	@Override
	public void render(TileCorruptionCleaner tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		this.doRender(tile, x, y, z, partialTicks);
	}

	@Override
	public boolean isGlobalRenderer(TileCorruptionCleaner te) {
		return true;
	}
}