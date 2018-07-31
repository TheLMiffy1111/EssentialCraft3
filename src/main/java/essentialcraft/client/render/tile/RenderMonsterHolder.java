package essentialcraft.client.render.tile;

import java.util.List;

import org.lwjgl.opengl.GL11;

import DummyCore.Utils.Coord3D;
import DummyCore.Utils.DrawUtils;
import DummyCore.Utils.DummyDistance;
import DummyCore.Utils.TessellatorWrapper;
import essentialcraft.common.capabilities.mru.CapabilityMRUHandler;
import essentialcraft.common.tile.TileMonsterHolder;
import essentialcraft.utils.common.PlayerTickHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMonsterHolder extends TileEntitySpecialRenderer<TileMonsterHolder>
{

	public void doRender(TileMonsterHolder tile, double x, double y, double z, float partialTicks)
	{
		RenderHelper.disableStandardItemLighting();
		List<EntityLivingBase> lst = tile.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(tile.getPos().getX()-32, tile.getPos().getY()-32, tile.getPos().getZ()-32, tile.getPos().getX()+33, tile.getPos().getY()+33, tile.getPos().getZ()+33));
		if(!lst.isEmpty() && tile.getCapability(CapabilityMRUHandler.MRU_HANDLER_CAPABILITY, null).getMRU() > lst.size())
		{
			for(int i = 0; i < lst.size(); ++i)
			{
				EntityLivingBase e = lst.get(i);
				if(!(e instanceof EntityPlayer))
				{
					Coord3D tilePos = new Coord3D(tile.getPos().getX()+0.5D,tile.getPos().getY()+0.5D,tile.getPos().getZ()+0.5D);
					Coord3D mobPosition = new Coord3D(e.posX,e.posY,e.posZ);
					DummyDistance dist = new DummyDistance(tilePos,mobPosition);
					if(dist.getDistance() < 10)
					{
						GlStateManager.pushMatrix();
						double[] o = {e.posX-0.5D,e.posY+e.getEyeHeight()+0.5D,e.posZ-0.5D};
						float f21 = 0 + partialTicks;
						float f31 = MathHelper.sin(f21 * 0.2F) / 2.0F + 0.5F;
						f31 = (f31 * f31 + f31) * 0.2F;
						float f4;
						float f5;
						float f6;
						f4 = (float)(o[0] - tile.getPos().getX());
						f5 = (float)(o[1] - (f31 +tile.getPos().getY()+1.3F));
						f6 = (float)(o[2] - tile.getPos().getZ());
						GlStateManager.translate((float)x+0.5F, (float)y + 0.6F, (float)z+0.5F);
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
						tessellator.startDrawingWithColor(5);
						byte b0 = 8;

						for (int i1 = 0; i1 <= b0; ++i1)
						{
							float f11 = MathHelper.sin(i1 % b0 * (float)Math.PI * 2.0F / b0) * 0.75F * 0.1F;
							float f12 = MathHelper.cos(i1 % b0 * (float)Math.PI * 2.0F / b0) * 0.75F * 0.1F;
							float f13 = i1 % b0 * 1.0F / b0;
							tessellator.setColorRGBA_F(0.0F, 1.0F, 1.0F, 10F);
							tessellator.addVertexWithUV(f11, f12, 0.0D, f13, f10);
							tessellator.setColorRGBA_F(1.0F, 0.0F, 1.0F, 10F);
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
				}
			}
		}
		RenderHelper.enableStandardItemLighting();
	}

	@Override
	public void render(TileMonsterHolder tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(tile.getBlockMetadata() == 0)
			this.doRender(tile, x, y, z, partialTicks);
	}

	@Override
	public boolean isGlobalRenderer(TileMonsterHolder te) {
		return true;
	}
}