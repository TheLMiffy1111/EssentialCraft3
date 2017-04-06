package ec3.client.render.tile;

import DummyCore.Client.AdvancedModelLoader;
import DummyCore.Client.IModelCustom;
import DummyCore.Utils.DrawUtils;
import DummyCore.Utils.MiscUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import org.lwjgl.opengl.GL11;

import ec3.common.tile.TileMagicalMirror;

@SideOnly(Side.CLIENT)
public class RenderMagicalMirror extends TileEntitySpecialRenderer
{
	public static final ResourceLocation textures = new ResourceLocation("essentialcraft:textures/models/armTextures.png");
	public static final ResourceLocation glass = new ResourceLocation("essentialcraft:textures/models/mirror.png");
	public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation("essentialcraft:models/block/Mirror.obj"));

	public RenderMagicalMirror()
	{
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
	 * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
	 * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(TileEntity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
	{
		TileMagicalMirror tile = (TileMagicalMirror) p_76986_1_;
		RenderHelper.disableStandardItemLighting();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)p_76986_2_+0.5F, (float)p_76986_4_-0.25F, (float)p_76986_6_+0.5F);
		float timeIndex = Minecraft.getMinecraft().world.getWorldTime()%120;
		float yIndex = 1.0F;
		if(timeIndex <= 60)
			yIndex = timeIndex/240F;
		else
			yIndex = 0.5F-timeIndex/240F;
		GlStateManager.translate(0, yIndex-0.25F, 0);
		if(tile.inventoryPos != null)
		{
			double d0 = tile.inventoryPos.x - (tile.getPos().getX()+0.5F);
			double d1 = tile.inventoryPos.y - (tile.getPos().getY()+0.5F+yIndex);
			double d2 = tile.inventoryPos.z - (tile.getPos().getZ()+0.5F);
			double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
			float f = -(float)(Math.atan2(d2, d0) * 180.0D / Math.PI)-90;
			float f1 = -(float)(-(Math.atan2(d1, d3) * 180.0D / Math.PI));

			GlStateManager.rotate(f, 0, 1, 0);
			GlStateManager.rotate(f1, 1, 0, 0);
		}


		this.bindTexture(textures);
		model.renderPart("pCube2");
		this.bindTexture(glass);
		if(tile.pulsing)
		{
			timeIndex = Minecraft.getMinecraft().world.getWorldTime()%20;
			float colorIndex = 1.0F;
			if(timeIndex <= 10)
			{
				colorIndex = 1.0F - timeIndex/10;
			}else
			{
				colorIndex = (timeIndex-10)/10;
			}
			GlStateManager.color(1, colorIndex, 1);
		}
		model.renderPart("pPlane1");
		GlStateManager.rotate(180, 0, 1, 0);
		model.renderPart("pPlane1");
		GlStateManager.color(1, 1, 1);
		GlStateManager.popMatrix();
		RenderHelper.enableStandardItemLighting();

		if(tile.transferingStack != null)
		{
			if(tile.transferTime < 20)
			{
				DrawUtils.renderItemStack_Full(tile.transferingStack, tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), p_76986_2_, p_76986_4_, p_76986_6_, tile.getWorld().getWorldTime()%360, 0, 1, 1, 1, 0.5F, -0.3F+((float)tile.transferTime/20F), 0.5F, false);
			}else
			{
				Vec3d vec = new Vec3d(tile.inventoryPos.x+0.5F - (tile.getPos().getX()+0.5F), tile.inventoryPos.y - (tile.getPos().getY()+0.5F), tile.inventoryPos.z - (tile.getPos().getZ()+0.5F));

				DrawUtils.renderItemStack_Full(tile.transferingStack, tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), p_76986_2_, p_76986_4_, p_76986_6_, tile.getWorld().getWorldTime()%360, 0, 1, 1, 1, 0.5F+((float)vec.xCoord*(tile.transferTime-20F)/40), 0.5F+((float)vec.yCoord*(tile.transferTime-20F)/40), 0.5F+((float)vec.zCoord*(tile.transferTime-20F)/40), false);
			}
		}
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(TileEntity p_110775_1_)
	{
		return textures;
	}

	@Override
	public void renderTileEntityAt(TileEntity p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_, int destroyStage) {
		if(p_147500_1_.getBlockMetadata() == 0)
			this.doRender((TileEntity) p_147500_1_, p_147500_2_, p_147500_4_, p_147500_6_, p_147500_8_, 0);
	}
}