package essentialcraft.client.render.tile;

import DummyCore.Client.AdvancedModelLoader;
import DummyCore.Client.IModelCustom;
import DummyCore.Utils.DrawUtils;
import essentialcraft.common.tile.TileMagicalRepairer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMagicalRepairer extends TileEntitySpecialRenderer<TileMagicalRepairer> {

	public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation("essentialcraft:models/block/magicalrepairer.obj"));
	public static final ResourceLocation textures = new ResourceLocation("essentialcraft:textures/models/magicalrepairer.png");

	public void doRender(TileMagicalRepairer tile, double x, double y, double z, float partialTicks) {
		RenderHelper.disableStandardItemLighting();
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(x,  y,  z);
			Minecraft.getMinecraft().renderEngine.bindTexture(textures);
			model.renderAll();
			GlStateManager.popMatrix();
		}

		float rotation = (tile.getWorld().getWorldTime()+partialTicks) % 360;

		GlStateManager.pushMatrix();
		DrawUtils.renderItemStack_Full(tile.getStackInSlot(1), x, y, z, rotation,0F, 1, 1, 1, 0.5F, 0.65F, 0.5F);
		GlStateManager.popMatrix();
		RenderHelper.enableStandardItemLighting();
	}

	@Override
	public void render(TileMagicalRepairer tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		this.doRender(tile, x, y, z, partialTicks);
	}
}