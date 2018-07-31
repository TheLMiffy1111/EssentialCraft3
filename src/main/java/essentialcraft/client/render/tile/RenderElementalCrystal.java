package essentialcraft.client.render.tile;

import DummyCore.Utils.MathUtils;
import essentialcraft.client.model.ModelElementalCrystal;
import essentialcraft.common.tile.TileElementalCrystal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderElementalCrystal extends TileEntitySpecialRenderer<TileElementalCrystal> {
	public static final ResourceLocation textures = new ResourceLocation("essentialcraft:textures/models/mcrystaltex.png");
	public static final ResourceLocation neutral = new ResourceLocation("essentialcraft:textures/models/mcrystaltex.png");
	public static final ResourceLocation fire = new ResourceLocation("essentialcraft:textures/models/fcrystaltex.png");
	public static final ResourceLocation water = new ResourceLocation("essentialcraft:textures/models/wcrystaltex.png");
	public static final ResourceLocation earth = new ResourceLocation("essentialcraft:textures/models/ecrystaltex.png");
	public static final ResourceLocation air = new ResourceLocation("essentialcraft:textures/models/acrystaltex.png");
	public static final ModelElementalCrystal crystal = new ModelElementalCrystal();

	public void doRender(TileElementalCrystal crystal_tile, double x, double y, double z, float partialTicks) {
		int metadata = crystal_tile.getBlockMetadata();

		GlStateManager.pushMatrix();
		float scale = MathUtils.getPercentage((int)crystal_tile.size, 100)/100F;

		if(metadata == 1) {
			GlStateManager.translate((float)x+0.5F, (float)y+1.4F-(1.0F-scale)*1.4F, (float)z+0.5F);
			GlStateManager.scale(scale, scale, scale);
			GlStateManager.rotate(180, 1, 0, 0);
		}

		if(metadata == 0) {
			GlStateManager.translate((float)x+0.5F, (float)y-0.4F+(1.0F-scale)*1.4F, (float)z+0.5F);
			GlStateManager.scale(scale, scale, scale);
			GlStateManager.rotate(0, 1, 0, 0);
		}

		if(metadata == 2) {
			GlStateManager.translate((float)x+0.5F, (float)y+0.5F, (float)z-0.5F+(1.0F-scale)*1.5F);
			GlStateManager.scale(scale, scale, scale);
			GlStateManager.rotate(90, 1, 0, 0);
		}

		if(metadata == 4) {
			GlStateManager.translate((float)x-0.5F+(1.0F-scale)*1.5F, (float)y+0.5F, (float)z+0.5F);
			GlStateManager.scale(scale, scale, scale);
			GlStateManager.rotate(90, 1, 0, 0);
			GlStateManager.rotate(270, 0, 0, 1);
		}

		if(metadata == 3) {
			GlStateManager.translate((float)x+0.5F, (float)y+0.5F, (float)z+1.5F-(1.0F-scale)*1.5F);
			GlStateManager.scale(scale, scale, scale);
			GlStateManager.rotate(-90, 1, 0, 0);

		}

		if(metadata == 5) {
			GlStateManager.translate((float)x+1.5F-(1.0F-scale)*1.5F, (float)y+0.5F, (float)z+0.5F);
			GlStateManager.scale(scale, scale, scale);
			GlStateManager.rotate(90, 1, 0, 0);
			GlStateManager.rotate(90, 0, 0, 1);
		}

		this.bindTexture(textures);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableCull();
		GlStateManager.disableBlend();
		GlStateManager.color(1, 1, 1, 0.5F);
		GlStateManager.enableBlend();
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		crystal.renderModel(0.0625F);

		this.bindTexture(fire);
		GlStateManager.color(1, 1, 1, (float)(crystal_tile.fire/100));
		crystal.renderModel(0.0625F);

		this.bindTexture(water);
		GlStateManager.color(1, 1, 1, (float)(crystal_tile.water/100));
		crystal.renderModel(0.0625F);

		this.bindTexture(earth);
		GlStateManager.color(1, 1, 1, (float)(crystal_tile.earth/100));
		crystal.renderModel(0.0625F);

		this.bindTexture(air);
		GlStateManager.color(1, 1, 1, (float)(crystal_tile.air/100));
		crystal.renderModel(0.0625F);

		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
		RenderHelper.enableStandardItemLighting();
	}

	@Override
	public void render(TileElementalCrystal tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		this.doRender(tile, x, y, z, partialTicks);
	}
}