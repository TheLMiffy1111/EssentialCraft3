package ec3.client.render.tile;

import java.util.ArrayList;
import java.util.List;

import DummyCore.Utils.DrawUtils;
import ec3.common.tile.TileMagicalDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMagicalDisplay extends TileEntitySpecialRenderer<TileMagicalDisplay> {

	public RenderMagicalDisplay() {}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
	 * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
	 * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(TileMagicalDisplay display, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
		RenderHelper.disableStandardItemLighting();
		GlStateManager.pushMatrix();

		int metadata = display.getBlockMetadata();
		ItemStack displayed = display.getStackInSlot(0);
		float rotationX = metadata == 3 ? 0 : metadata == 2 ? 180 : metadata == 4 ? 90 : 270;
		float offsetX = metadata == 1 || metadata == 0 ? 0.7F : metadata == 3 || metadata == 2 ? 0.5F : metadata == 4 ? 0.96F : 0.04F;
		float offsetY = metadata == 1 ? 0.04F : metadata == 0 ? 0.96F : 0.4F;
		float offsetZ = metadata == 1 || metadata == 0 ? 0.5F : metadata == 4 || metadata == 5 ? 0.5F : metadata == 2 ? 0.96F : 0.04F;
		float rotationZ = metadata == 1 || metadata == 0 ? 90 : 0;
		float scaleIndex = display.type == 0 ? 1.5F : display.type == 1 ? 1F : 0.75F;

		if(displayed != null) {
			GlStateManager.scale(scaleIndex, scaleIndex, scaleIndex);
			if(display.type == 0 || metadata == 0 || metadata == 1) {
				DrawUtils.renderItemStack_Full(displayed, display.getPos().getX(), display.getPos().getY(), display.getPos().getZ(), p_76986_2_/scaleIndex, p_76986_4_/scaleIndex, p_76986_6_/scaleIndex, rotationX, rotationZ, 1, 1, 1, offsetX/scaleIndex, offsetY/scaleIndex, offsetZ/scaleIndex, false);
			}
			if(display.type == 1 && metadata != 0 && metadata != 1) {
				offsetY = metadata == 1 ? 0.04F : metadata == 0 ? 0.96F : 0.6F;
				DrawUtils.renderItemStack_Full(displayed, display.getPos().getX(), display.getPos().getY(), display.getPos().getZ(), p_76986_2_/scaleIndex, p_76986_4_/scaleIndex, p_76986_6_/scaleIndex, rotationX, rotationZ, 1, 1, 1, offsetX/scaleIndex, offsetY/scaleIndex, offsetZ/scaleIndex, false);
				FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
				GlStateManager.translate(p_76986_2_, p_76986_4_, p_76986_6_);
				String drawedName = displayed.getDisplayName();
				GlStateManager.rotate(180, 1, 0, 0);
				//GlStateManager.rotate(180, 0, 1, 0);
				switch(metadata) {
				case 2: {
					GlStateManager.translate(1-((float)drawedName.length()/160F), -0.4F, -0.949F);
					break;
				}
				case 3: {
					GlStateManager.translate(((float)drawedName.length()/160F), -0.4F, -0.051F);
					break;
				}
				case 4: {
					GlStateManager.translate(0.949F, -0.4F, -((float)drawedName.length()/160F));
					break;
				}
				case 5: {
					GlStateManager.translate(0.051F, -0.4F, -1+((float)drawedName.length()/160F));
					break;
				}
				}

				GlStateManager.rotate(rotationX, 0, 1, 0);

				float s = 0.06F / (drawedName.length()/2);

				GlStateManager.scale(s, s, s);
				renderer.drawString(drawedName, 0, 0, 0xffffff);
			}
			if(display.type == 2 && metadata != 0 && metadata != 1) {
				offsetX = metadata == 3 ? 0.25F : metadata == 2 ? 0.75F : metadata == 4 ? 0.94F : 0.05F;
				offsetY = 0.7F;
				offsetZ = metadata == 4 ? 0.25F : metadata == 5 ? 0.75F : metadata == 2 ? 0.95F : 0.04F;
				DrawUtils.renderItemStack_Full(displayed, display.getPos().getX(), display.getPos().getY(), display.getPos().getZ(), p_76986_2_/scaleIndex, p_76986_4_/scaleIndex, p_76986_6_/scaleIndex, rotationX, rotationZ, 1, 1, 1, offsetX/scaleIndex, offsetY/scaleIndex, offsetZ/scaleIndex, false);
				FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
				GlStateManager.translate(p_76986_2_, p_76986_4_, p_76986_6_);
				String drawedName = displayed.getDisplayName();
				GlStateManager.rotate(180, 1, 0, 0);
				//GlStateManager.rotate(180, 0, 1, 0);
				switch(metadata) {
				case 2: {
					GlStateManager.translate(-0.4F+1-((float)drawedName.length()/160F), -0.8F, -0.949F);
					break;
				}
				case 3: {
					GlStateManager.translate(0.4F+((float)drawedName.length()/160F), -0.8F, -0.051F);
					break;
				}
				case 4: {
					GlStateManager.translate(0.949F, -0.8F, -0.4F-((float)drawedName.length()/160F));
					break;
				}
				case 5: {
					GlStateManager.translate(0.051F, -0.8F, 0.4F-1+((float)drawedName.length()/160F));
					break;
				}
				}

				GlStateManager.rotate(rotationX, 0, 1, 0);

				float s = 0.03F / (drawedName.length()/2);
				GlStateManager.pushMatrix();
				GlStateManager.scale(s, s, s);
				renderer.drawString(drawedName, 0, 0, 0xffffff);
				GlStateManager.popMatrix();
				List<String> displaySt = new ArrayList<String>();
				displayed.getItem().addInformation(displayed, Minecraft.getMinecraft().player, displaySt, false);

				int longestStr = 1;

				for(int i = 0; i < displaySt.size(); ++i) {
					String st = displaySt.get(i);
					if(longestStr < st.length())
						longestStr = st.length();
				}
				GlStateManager.pushMatrix();
				GlStateManager.translate(-0.4F, 0.15F, 0);

				s = 0.08F / (longestStr/2F);
				GlStateManager.scale(s, s, s);

				for(int i = 0; i < displaySt.size(); ++i) {
					GlStateManager.translate(0, 10, 0);
					renderer.drawString(displaySt.get(i), 0, 0, 0xffffff);
				}

				GlStateManager.popMatrix();
			}
		}


		GlStateManager.popMatrix();
		RenderHelper.enableStandardItemLighting();
	}

	@Override
	public void renderTileEntityAt(TileMagicalDisplay p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_, int destroyStage) {
		this.doRender(p_147500_1_, p_147500_2_, p_147500_4_, p_147500_6_, p_147500_8_, 0);
	}
}