package ec3.client.render;

import org.lwjgl.opengl.GL11;

import DummyCore.Utils.TessellatorWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ec3.common.item.ItemGun;

public class ClientRenderHandler {

	ResourceLocation loc = new ResourceLocation("essentialcraft","textures/hud/sniper_scope.png");

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClientRenderTick(RenderGameOverlayEvent.Pre event)
	{
		if(event.getType() != ElementType.ALL)
		{
			EntityPlayer p = Minecraft.getMinecraft().player;
			if(p.getHeldItemMainhand() != null && p.getHeldItemMainhand().getItem() instanceof ItemGun && p.isSneaking() && p.getHeldItemMainhand().getTagCompound() != null && p.getHeldItemMainhand().getTagCompound().hasKey("scope") && ItemGun.class.cast(p.getHeldItemMainhand().getItem()).gunType.equalsIgnoreCase("sniper"))
			{
				if(event.getType() == ElementType.CROSSHAIRS)
				{
					Minecraft mc = Minecraft.getMinecraft();
					ScaledResolution scaledresolution = new ScaledResolution(mc);
					int k = scaledresolution.getScaledWidth();
					int l = scaledresolution.getScaledHeight();
					if(k < l) k = l;
					if(k > l) k = l;

					Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
					GlStateManager.depthMask(false);

					TessellatorWrapper tessellator = TessellatorWrapper.getInstance();
					tessellator.startDrawingQuads();
					tessellator.addVertexWithUV(scaledresolution.getScaledWidth()/2-scaledresolution.getScaledWidth()/4, (double)l, -90.0D, 0.0D, 1.0D);
					tessellator.addVertexWithUV((double)k+scaledresolution.getScaledWidth()/2-scaledresolution.getScaledWidth()/4, (double)l, -90.0D, 1.0D, 1.0D);
					tessellator.addVertexWithUV((double)k+scaledresolution.getScaledWidth()/2-scaledresolution.getScaledWidth()/4, 0.0D, -90.0D, 1.0D, 0.0D);
					tessellator.addVertexWithUV(scaledresolution.getScaledWidth()/2-scaledresolution.getScaledWidth()/4, 0.0D, -90.0D, 0.0D, 0.0D);
					tessellator.draw();
					Minecraft.getMinecraft().getTextureManager().bindTexture(RenderHandlerEC3.whitebox);

					GlStateManager.color(0, 0, 0);

					tessellator.startDrawingQuads();
					tessellator.addVertexWithUV(0, (double)l, -90.0D, 0.0D, 1.0D);
					tessellator.addVertexWithUV(scaledresolution.getScaledWidth()/2-scaledresolution.getScaledWidth()/4, (double)l, -90.0D, 1.0D, 1.0D);
					tessellator.addVertexWithUV(scaledresolution.getScaledWidth()/2-scaledresolution.getScaledWidth()/4, 0.0D, -90.0D, 1.0D, 0.0D);
					tessellator.addVertexWithUV(0, 0.0D, -90.0D, 0.0D, 0.0D);
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.addVertexWithUV((double)k+scaledresolution.getScaledWidth()/2-scaledresolution.getScaledWidth()/4, (double)l, -90.0D, 0.0D, 1.0D);
					tessellator.addVertexWithUV(scaledresolution.getScaledWidth(), (double)l, -90.0D, 1.0D, 1.0D);
					tessellator.addVertexWithUV(scaledresolution.getScaledWidth(), 0.0D, -90.0D, 1.0D, 0.0D);
					tessellator.addVertexWithUV((double)k+scaledresolution.getScaledWidth()/2-scaledresolution.getScaledWidth()/4, 0.0D, -90.0D, 0.0D, 0.0D);
					tessellator.draw();

					GlStateManager.color(1, 1, 1);

					GlStateManager.depthMask(true);

				}
				event.setCanceled(true);
			}
		}
	}

	public static void renderImage(ResourceLocation image, int scaledResX, int scaledResY, float opacity, float r, float g, float b)
	{
		GlStateManager.disableDepth();;
		GlStateManager.depthMask(false);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GlStateManager.color(r, g, b, opacity);
		GlStateManager.disableAlpha();
		Minecraft.getMinecraft().getTextureManager().bindTexture(image);

		GlStateManager.depthMask(true);
		GlStateManager.enableDepth();
		GlStateManager.enableAlpha();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}
}
