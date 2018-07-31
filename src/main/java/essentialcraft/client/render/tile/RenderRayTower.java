package essentialcraft.client.render.tile;

import essentialcraft.client.model.ModelFloatingCube;
import essentialcraft.common.tile.TileRayTower;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderRayTower extends TileEntitySpecialRenderer<TileRayTower> {

	private static final ResourceLocation enderCrystalTextures = new ResourceLocation("essentialcraft:textures/entities/raycrystal.png");
	private ModelFloatingCube model;

	public RenderRayTower() {
		this.model = new ModelFloatingCube(0.0F, true);
	}

	public void doRender(TileRayTower tile, double x, double y, double z, float partialTicks) {
		RenderHelper.disableStandardItemLighting();
		float f2 = tile.innerRotation + partialTicks;
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x+0.5F, (float)y+1.6F, (float)z+0.5F);
		this.bindTexture(enderCrystalTextures);
		float f3 = MathHelper.sin(f2 * 0.2F) / 2.0F + 0.5F;
		f3 += f3 * f3;
		GlStateManager.scale(0.4F, 0.4F, 0.4F);
		this.model.render(tile, 0.0F, f2 * 3.0F, 0.35F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.popMatrix();
		RenderHelper.enableStandardItemLighting();
	}

	@Override
	public void render(TileRayTower tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(tile.getBlockMetadata() == 0)
			this.doRender(tile, x, y, z, partialTicks);
	}
}