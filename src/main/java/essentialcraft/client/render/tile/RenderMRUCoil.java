package essentialcraft.client.render.tile;

import essentialcraft.client.model.ModelFloatingCube;
import essentialcraft.common.tile.TileMRUCoil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMRUCoil extends TileEntitySpecialRenderer<TileMRUCoil>
{
	private static final ResourceLocation enderCrystalTextures = new ResourceLocation("essentialcraft:textures/entities/raycrystal.png");
	private ModelFloatingCube model;

	public RenderMRUCoil()
	{
		this.model = new ModelFloatingCube(0.0F, true);
	}

	public void doRender(TileMRUCoil tile, double x, double y, double z, float partialTicks)
	{
		RenderHelper.disableStandardItemLighting();
		float f2 = tile.innerRotation + partialTicks;
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x+0.5F, (float)y+0.6F, (float)z+0.5F);
		this.bindTexture(enderCrystalTextures);
		float f3 = MathHelper.sin(f2 * 0.2F) / 2.0F + 0.5F;
		f3 += f3 * f3;
		GlStateManager.scale(0.2F, 0.2F, 0.2F);
		this.model.render(tile, 0.0F, f2 * 3.0F, 0.35F, 0.0F, 0.0F, 0.0625F);
		if(tile.localLightning != null)
			tile.localLightning.render(x, y, z, partialTicks);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		if(tile.monsterLightning != null)
			tile.monsterLightning.render(x, y, z, partialTicks);
		GlStateManager.popMatrix();

		RenderHelper.enableStandardItemLighting();
	}

	@Override
	public void render(TileMRUCoil tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		this.doRender(tile, x, y, z, partialTicks);
	}

	@Override
	public boolean isGlobalRenderer(TileMRUCoil te) {
		return true;
	}
}