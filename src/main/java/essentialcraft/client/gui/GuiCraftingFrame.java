package essentialcraft.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DummyCore.Utils.DrawUtils;
import DummyCore.Utils.MiscUtils;
import essentialcraft.common.inventory.InventoryCraftingFrame;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class GuiCraftingFrame extends GuiContainer{

	public InventoryCraftingFrame crafter;

	public GuiCraftingFrame(Container c, InventoryCraftingFrame inv) {
		super(c);
		crafter = inv;
	}

	@Override
	protected boolean checkHotbarKeys(int slot)
	{
		return false;
	}

	@Override
	public void initGui()
	{
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.buttonList.add(new GuiButton(0, k+6, l+6, 20, 20, ""));
		super.initGui();
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		MiscUtils.handleButtonPress(par1GuiButton.id, this.getClass(), par1GuiButton.getClass(), this.mc.player, 0, 0, 0);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
			int p_146976_2_, int p_146976_3_) {
		DrawUtils.bindTexture("minecraft", "textures/gui/container/crafting_table.png");
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	@Override
	public void drawScreen(int mX, int mY, float partialTicks)
	{
		this.drawDefaultBackground();
		if(!this.crafter.filterStack.isItemEqual(mc.player.getHeldItemMainhand()))
			this.crafter.filterStack = mc.player.getHeldItemMainhand();
		if(!ItemStack.areItemStackTagsEqual(this.crafter.filterStack, mc.player.getHeldItemMainhand()))
			this.crafter.filterStack = mc.player.getHeldItemMainhand();

		super.drawScreen(mX, mY, partialTicks);
		for (int ik = 0; ik < this.buttonList.size(); ++ik)
		{
			RenderHelper.disableStandardItemLighting();
			GlStateManager.color(1, 1, 1);
			GuiButton btn  = this.buttonList.get(ik);
			boolean hover = mX >= btn.x && mY >= btn.y && mX < btn.x + btn.width && mY < btn.y + btn.height;
			int id = btn.id;
			if(id == 0)
			{
				DrawUtils.bindTexture("essentialcraft", "textures/gui/guiFilterButtons.png");
				if(MiscUtils.getStackTag(this.crafter.filterStack).getBoolean("ignoreOreDict"))
				{
					this.drawTexturedModalRect(btn.x, btn.y, 20, 40, 20, 20);
				}else
				{
					this.drawTexturedModalRect(btn.x, btn.y, 0, 40, 20, 20);
				}
			}
			if(hover)
			{
				if(id == 0)
				{
					List<String> drawedLst = new ArrayList<String>();
					if(MiscUtils.getStackTag(this.crafter.filterStack).getBoolean("ignoreOreDict"))
					{
						drawedLst.add("Ore Dictionary: Ignored");
					}else
					{
						drawedLst.add("Ore Dictionary: Not Ignored");
					}
					drawHoveringText(drawedLst, mX, mY, fontRenderer);

				}
			}
		}
		this.renderHoveredToolTip(mX, mY);
	}

	@Override
	protected void drawHoveringText(List<String> list, int x, int y, FontRenderer font)
	{
		GlStateManager.disableLighting();
		if (!list.isEmpty())
		{
			GlStateManager.disableRescaleNormal();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			int k = 0;
			Iterator<String> iterator = list.iterator();

			while (iterator.hasNext())
			{
				String s = iterator.next();
				int l = font.getStringWidth(s);

				if (l > k)
				{
					k = l;
				}
			}

			int j2 = x + 12;
			int k2 = y - 12;
			int i1 = 8;

			if (list.size() > 1)
			{
				i1 += 2 + (list.size() - 1) * 10;
			}

			if (j2 + k > this.width)
			{
				j2 -= 28 + k;
			}

			if (k2 + i1 + 6 > this.height)
			{
				k2 = this.height - i1 - 6;
			}

			this.zLevel = 600.0F;
			itemRender.zLevel = 600.0F;
			int j1 = -267386872;
			this.drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
			this.drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
			this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
			this.drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
			this.drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
			int k1 = 1347420415;
			int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
			this.drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
			this.drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
			this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
			this.drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

			for (int i2 = 0; i2 < list.size(); ++i2)
			{
				String s1 = list.get(i2);
				font.drawStringWithShadow(s1, j2, k2, -1);

				if (i2 == 0)
				{
					k2 += 2;
				}

				k2 += 10;
			}

			this.zLevel = 0.0F;
			itemRender.zLevel = 0.0F;
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableRescaleNormal();
		}
		GlStateManager.enableLighting();
		GlStateManager.color(1, 1, 1);
	}

}
