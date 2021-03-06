package essentialcraft.client.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.io.Charsets;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import DummyCore.Utils.IMainMenu;
import DummyCore.Utils.TessellatorWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class GuiMainMenuEC extends GuiMainMenu implements IMainMenu {
	private int panoramaTimer;
	private static final ResourceLocation[] titlePanoramaPaths = {
			new ResourceLocation("essentialcraft","textures/gui/mainmenu/panorama_0.png"),
			new ResourceLocation("essentialcraft","textures/gui/mainmenu/panorama_1.png"),
			new ResourceLocation("essentialcraft","textures/gui/mainmenu/panorama_2.png"),
			new ResourceLocation("essentialcraft","textures/gui/mainmenu/panorama_3.png"),
			new ResourceLocation("essentialcraft","textures/gui/mainmenu/panorama_4.png"),
			new ResourceLocation("essentialcraft","textures/gui/mainmenu/panorama_5.png")
	};
	private ResourceLocation field_110351_G;
	private DynamicTexture viewportTexture;
	private static final ResourceLocation minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");
	private float updateCounter;
	private String splashText;
	private static final Random rand = new Random(); private static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");
	private int field_92024_r;
	private int field_92022_t;
	private int field_92021_u;
	private int field_92020_v;
	private int field_92019_w;
	private String field_92025_p;
	private String field_146972_A;
	public static ResourceLocation buttonTextures = new ResourceLocation("essentialcraft","textures/gui/widgets.png");

	@Override
	public void initGui()
	{
		super.initGui();
		BufferedReader bufferedreader = null;

		try
		{
			ArrayList<String> arraylist = new ArrayList<String>();
			bufferedreader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(), Charsets.UTF_8));
			String s;

			while ((s = bufferedreader.readLine()) != null)
			{
				s = s.trim();

				if (!s.isEmpty())
				{
					arraylist.add(s);
				}
			}

			if (!arraylist.isEmpty())
			{
				do
				{
					this.splashText = arraylist.get(rand.nextInt(arraylist.size()));
				}
				while (this.splashText.hashCode() == 125780783);
			}
		}
		catch (IOException ioexception1)
		{

		}
		finally
		{
			if (bufferedreader != null)
			{
				try
				{
					bufferedreader.close();
				}
				catch (IOException ioexception)
				{

				}
			}
		}
		this.updateCounter = rand.nextFloat();
		this.viewportTexture = new DynamicTexture(256, 256);
		this.field_110351_G = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
	}

	@Override
	public void updateScreen()
	{
		++this.panoramaTimer;
	}


	/**
	 * Draws the main menu panorama
	 */
	private void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder BufferBuilder = tessellator.getBuffer();
		GlStateManager.matrixMode(5889);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
		GlStateManager.matrixMode(5888);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.disableCull();
		GlStateManager.depthMask(false);
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		int i = 8;

		for (int j = 0; j < 64; ++j)
		{
			GlStateManager.pushMatrix();
			float f = (j % 8 / 8.0F - 0.5F) / 64.0F;
			float f1 = (j / 8 / 8.0F - 0.5F) / 64.0F;
			float f2 = 0.0F;
			GlStateManager.translate(f, f1, 0.0F);
			GlStateManager.rotate(MathHelper.sin((this.panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(-(this.panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);

			for (int k = 0; k < 6; ++k)
			{
				GlStateManager.pushMatrix();

				if (k == 1)
				{
					GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (k == 2)
				{
					GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				}

				if (k == 3)
				{
					GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (k == 4)
				{
					GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
				}

				if (k == 5)
				{
					GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
				}

				this.mc.getTextureManager().bindTexture(titlePanoramaPaths[k]);
				BufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				int l = 255 / (j + 1);
				float f3 = 0.0F;
				BufferBuilder.pos(-1.0D, -1.0D, 1.0D).tex(0.0D, 0.0D).color(255, 255, 255, l).endVertex();
				BufferBuilder.pos(1.0D, -1.0D, 1.0D).tex(1.0D, 0.0D).color(255, 255, 255, l).endVertex();
				BufferBuilder.pos(1.0D, 1.0D, 1.0D).tex(1.0D, 1.0D).color(255, 255, 255, l).endVertex();
				BufferBuilder.pos(-1.0D, 1.0D, 1.0D).tex(0.0D, 1.0D).color(255, 255, 255, l).endVertex();
				tessellator.draw();
				GlStateManager.popMatrix();
			}

			GlStateManager.popMatrix();
			GlStateManager.colorMask(true, true, true, false);
		}

		BufferBuilder.setTranslation(0.0D, 0.0D, 0.0D);
		GlStateManager.colorMask(true, true, true, true);
		GlStateManager.matrixMode(5889);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(5888);
		GlStateManager.popMatrix();
		GlStateManager.depthMask(true);
		GlStateManager.enableCull();
		GlStateManager.enableDepth();
	}

	/**
	 * Rotate and blurs the skybox view in the main menu
	 */
	private void rotateAndBlurSkybox(float p_73968_1_)
	{
		this.mc.getTextureManager().bindTexture(this.field_110351_G);
		GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GlStateManager.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.colorMask(true, true, true, false);
		TessellatorWrapper tessellator = TessellatorWrapper.getInstance();
		BufferBuilder BufferBuilder = Tessellator.getInstance().getBuffer();
		tessellator.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
		GlStateManager.disableAlpha();

		for(int i = 0; i < 3; ++i) {
			float f = 1.0F / (i + 1);
			int k = this.width;
			int l = this.height;
			float f1 = (i - 1) / 256.0F;
			BufferBuilder.pos(k, l, this.zLevel).tex(0.0F + f1, 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
			BufferBuilder.pos(k, 0.0D, this.zLevel).tex(1.0F + f1, 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
			BufferBuilder.pos(0.0D, 0.0D, this.zLevel).tex(1.0F + f1, 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
			BufferBuilder.pos(0.0D, l, this.zLevel).tex(0.0F + f1, 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
		}

		tessellator.draw();
		GlStateManager.enableAlpha();
		GlStateManager.colorMask(true, true, true, true);
	}

	private void renderSkybox(int mouseX, int mouseY, float partialTicks) {
		this.mc.getFramebuffer().unbindFramebuffer();
		GlStateManager.viewport(0, 0, 256, 256);
		this.drawPanorama(mouseX, mouseY, partialTicks);
		this.rotateAndBlurSkybox(partialTicks);
		this.rotateAndBlurSkybox(partialTicks);
		this.rotateAndBlurSkybox(partialTicks);
		this.rotateAndBlurSkybox(partialTicks);
		this.rotateAndBlurSkybox(partialTicks);
		this.rotateAndBlurSkybox(partialTicks);
		this.rotateAndBlurSkybox(partialTicks);
		this.mc.getFramebuffer().bindFramebuffer(true);
		GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
		float f = 120.0F / (this.width > this.height ? this.width : this.height);
		float f1 = this.height * f / 256.0F;
		float f2 = this.width * f / 256.0F;
		int i = this.width;
		int j = this.height;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder BufferBuilder = tessellator.getBuffer();
		BufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		BufferBuilder.pos(0.0D, j, this.zLevel).tex(0.5F - f1, 0.5F + f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		BufferBuilder.pos(i, j, this.zLevel).tex(0.5F - f1, 0.5F - f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		BufferBuilder.pos(i, 0.0D, this.zLevel).tex(0.5F + f1, 0.5F - f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		BufferBuilder.pos(0.0D, 0.0D, this.zLevel).tex(0.5F + f1, 0.5F + f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		tessellator.draw();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		GlStateManager.disableAlpha();
		this.renderSkybox(mouseX, mouseY, partialTicks);
		GlStateManager.enableAlpha();
		int i = 274;
		int j = this.width / 2 - 137;
		int k = 30;
		this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
		this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
		this.mc.getTextureManager().bindTexture(minecraftTitleTextures);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		if (this.updateCounter < 1.0E-4D)
		{
			this.drawTexturedModalRect(j + 0, 30, 0, 0, 99, 44);
			this.drawTexturedModalRect(j + 99, 30, 129, 0, 27, 44);
			this.drawTexturedModalRect(j + 99 + 26, 30, 126, 0, 3, 44);
			this.drawTexturedModalRect(j + 99 + 26 + 3, 30, 99, 0, 26, 44);
			this.drawTexturedModalRect(j + 155, 30, 0, 45, 155, 44);
		}
		else
		{
			this.drawTexturedModalRect(j + 0, 30, 0, 0, 155, 44);
			this.drawTexturedModalRect(j + 155, 30, 0, 45, 155, 44);
		}

		this.splashText = net.minecraftforge.client.ForgeHooksClient.renderMainMenu(this, this.fontRenderer, this.width, this.height, this.splashText);

		GlStateManager.pushMatrix();
		GlStateManager.translate(this.width / 2 + 90, 70.0F, 0.0F);
		GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
		float f = 1.8F - MathHelper.abs(MathHelper.sin(Minecraft.getSystemTime() % 1000L / 1000.0F * ((float)Math.PI * 2F)) * 0.1F);
		f = f * 100.0F / (this.fontRenderer.getStringWidth(this.splashText) + 32);
		GlStateManager.scale(f, f, f);
		this.drawCenteredString(this.fontRenderer, this.splashText, 0, -8, -256);
		GlStateManager.popMatrix();
		String s = "Minecraft 1.10.2";

		if (this.mc.isDemo())
		{
			s = s + " Demo";
		}
		else
		{
			s = s + ("release".equalsIgnoreCase(this.mc.getVersionType()) ? "" : "/" + this.mc.getVersionType());
		}

		java.util.List<String> brandings = com.google.common.collect.Lists.reverse(net.minecraftforge.fml.common.FMLCommonHandler.instance().getBrandings(true));
		for (int brdline = 0; brdline < brandings.size(); brdline++)
		{
			String brd = brandings.get(brdline);
			if (!com.google.common.base.Strings.isNullOrEmpty(brd))
			{
				this.drawString(this.fontRenderer, brd, 2, this.height - ( 10 + brdline * (this.fontRenderer.FONT_HEIGHT + 1)), 16777215);
			}
		}
		String s1 = "Copyright Mojang AB. Do not distribute!";
		this.drawString(this.fontRenderer, "Copyright Mojang AB. Do not distribute!", this.width - this.fontRenderer.getStringWidth("Copyright Mojang AB. Do not distribute!") - 2, this.height - 10, -1);

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public void drawButton(GuiButton button,Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_)
	{
		if (button.enabled)
		{
			FontRenderer fontrenderer = p_146112_1_.fontRenderer;
			p_146112_1_.getTextureManager().bindTexture(buttonTextures);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			boolean field_146123_n = p_146112_2_ >= button.x && p_146112_3_ >= button.y && p_146112_2_ < button.x + button.width && p_146112_3_ < button.y + button.height;
			int k = 1;
			if (!button.enabled)
			{
				k = 0;
			}
			else if(field_146123_n)
			{
				k = 2;
			}
			GlStateManager.enableBlend();
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			this.drawTexturedModalRect(button.x, button.y, 0, 46 + k * 20, button.width / 2, button.height);
			this.drawTexturedModalRect(button.x + button.width / 2, button.y, 200 - button.width / 2, 46 + k * 20, button.width / 2, button.height);
			int l = 14737632;

			if (button.packedFGColour != 0)
			{
				l = button.packedFGColour;
			}
			else if (!button.enabled)
			{
				l = 10526880;
			}
			else
			{
				l = 16777120;
			}

			this.drawCenteredString(fontrenderer, button.displayString, button.x + button.width / 2, button.y + (button.height - 8) / 2, l);
		}
	}
}
