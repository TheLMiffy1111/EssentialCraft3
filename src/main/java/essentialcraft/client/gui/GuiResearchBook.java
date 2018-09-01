package essentialcraft.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import DummyCore.Utils.DrawUtils;
import DummyCore.Utils.IngredientUtils;
import DummyCore.Utils.MathUtils;
import DummyCore.Utils.MiscUtils;
import DummyCore.Utils.Notifier;
import essentialcraft.api.ApiCore;
import essentialcraft.api.CategoryEntry;
import essentialcraft.api.DiscoveryEntry;
import essentialcraft.api.MagicianTableRecipe;
import essentialcraft.api.PageEntry;
import essentialcraft.api.RadiatingChamberRecipe;
import essentialcraft.api.StructureBlock;
import essentialcraft.api.StructureRecipe;
import essentialcraft.common.block.BlocksCore;
import essentialcraft.common.mod.EssentialCraftCore;
import essentialcraft.utils.cfg.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class GuiResearchBook extends GuiScreen {

	protected static RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
	public int currentDepth;
	public static int currentPage;
	public static CategoryEntry currentCategory;
	public static DiscoveryEntry currentDiscovery;
	public static int currentDiscoveryPage;
	public static List<Object[]> hoveringText = new ArrayList<Object[]>();
	public static List<Object[]> prevState = new ArrayList<Object[]>();
	public static final int DISCOVERIES_PER_PAGE = 48;
	public NBTTagCompound bookTag;
	public boolean isLeftMouseKeyPressed = false;
	public boolean isRightMouseKeyPressed = false;
	public static final ResourceLocation DEFAULT_BOOK_TEXTURE = new ResourceLocation("essentialcraft", "textures/gui/research_book_generic.png");
	public static float ticksOpened;
	public static int ticksBeforePressing;
	public String numberString = "";
	public int pressDelay;

	public GuiResearchBook() {
		super();
	}

	@Override
	public void updateScreen()  {
		++ticksOpened;
		--ticksBeforePressing;
		--pressDelay;
		if(!numberString.isEmpty() && pressDelay <= 0) {
			int buttonID = -1;
			try {
				buttonID = Integer.parseInt(numberString);
			}
			catch(NumberFormatException e) {
				Notifier.notifyCustomMod("EssentialCraft", "[ERROR]"+numberString+" is not a valid number!");
			}
			if(buttonID > -1) {
				buttonID -= 1;
				if(currentCategory != null && currentDiscovery == null) {
					buttonID += 3;
				}
				if(buttonID >= 0) {
					if(buttonList.size() > buttonID) {
						GuiButton button = buttonList.get(buttonID);
						if(button.enabled) {
							this.actionPerformed(button);
						}
					}
				}
			}
			numberString = "";
		}
	}

	@Override
	protected void keyTyped(char typed, int keyID) {
		try {
			super.keyTyped(typed, keyID);
			if(keyID == Keyboard.KEY_BACK && currentCategory != null) {
				if(this.buttonList.size() > 0) {
					GuiButton button = this.buttonList.get(0);
					if(button.enabled) {
						this.actionPerformed(button);
					}
				}
			}
			if(typed == '0' || typed == '1' || typed == '2' || typed == '3' || typed == '4' || typed == '5' || typed == '6' || typed == '7' || typed == '8' || typed == '9' || typed == '0') {
				numberString += typed;
				pressDelay = 20;
			}
			if(keyID == Keyboard.KEY_APOSTROPHE) {
				pressDelay = 0;
			}
			if(currentCategory != null && (keyID == Keyboard.KEY_RIGHT || keyID == Keyboard.KEY_LEFT)) {
				int press = keyID == Keyboard.KEY_RIGHT ? 2 : 1;
				if(this.buttonList.size() > press) {
					GuiButton button = this.buttonList.get(press);
					if(button.enabled) {
						this.actionPerformed(button);
					}
				}
			}
		}
		catch(Exception e) {}
	}

	@Override
	public void initGui()  {
		isLeftMouseKeyPressed = Mouse.isButtonDown(0);
		isRightMouseKeyPressed = Mouse.isButtonDown(1);
		this.buttonList.clear();
		this.labelList.clear();
		bookTag = this.mc.player.getHeldItemMainhand().getTagCompound();
		if(currentCategory == null) {
			initCategories();
		}
		if(currentCategory != null && currentDiscovery == null) {
			initDiscoveries();
		}
		if(currentCategory != null && currentDiscovery != null) {
			initPage();
		}
		ticksBeforePressing = 1;
	}

	@Override
	public void drawBackground(int tint) {
		GlStateManager.color(1, 1, 1);
		int k = (this.width - 256) / 2;
		int l = (this.height - 168) / 2;
		if(currentCategory == null) {
			this.mc.renderEngine.bindTexture(DEFAULT_BOOK_TEXTURE);
		}
		if(currentCategory != null && currentDiscovery == null && currentCategory.specificBookTextures != null) {
			this.mc.renderEngine.bindTexture(currentCategory.specificBookTextures);
		}
		if(currentCategory != null && currentDiscovery == null && currentCategory.specificBookTextures == null) {
			this.mc.renderEngine.bindTexture(DEFAULT_BOOK_TEXTURE);
		}
		if(currentCategory != null && currentDiscovery != null) {
			if(currentCategory == null || currentCategory.specificBookTextures == null) {
				this.mc.renderEngine.bindTexture(DEFAULT_BOOK_TEXTURE);
			}
			else {
				this.mc.renderEngine.bindTexture(currentCategory.specificBookTextures);
			}
		}
		this.drawTexturedModalRect(k, l, 0, 0, 256, 180);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		int k = (this.width - 256) / 2;
		int l = (this.height - 168) / 2;
		int dWheel = Mouse.getDWheel();
		if(currentDiscovery != null) {
			if(dWheel < 0) {
				if(this.buttonList.size() > 1) {
					GuiButton button = this.buttonList.get(2);
					if(button.enabled) {
						this.actionPerformed(button);
					}
				}
			}
			if(dWheel > 0) {
				if(this.buttonList.size() > 1) {
					GuiButton button = this.buttonList.get(1);
					if(button.enabled) {
						this.actionPerformed(button);
					}
				}
			}
		}
		else if(currentCategory != null) {
			if(dWheel < 0) {
				if(this.buttonList.size() > 1) {
					GuiButton button = this.buttonList.get(2);
					if(button.enabled) {
						this.actionPerformed(button);
					}
				}
			}
			if(dWheel > 0) {
				if(this.buttonList.size() > 1) {
					GuiButton button = this.buttonList.get(1);
					if(button.enabled) {
						this.actionPerformed(button);
					}
				}
			}
		}
		if(isRightMouseKeyPressed && !Mouse.isButtonDown(1)) {
			isRightMouseKeyPressed = false;
		}
		if(!isRightMouseKeyPressed && Mouse.isButtonDown(1)) {
			isRightMouseKeyPressed = true;
			if(!prevState.isEmpty()) {
				Object[] tryArray = prevState.get(prevState.size()-1);
				currentPage = Integer.parseInt(tryArray[1].toString());
				currentDiscoveryPage = Integer.parseInt(tryArray[2].toString());
				currentDiscovery = (DiscoveryEntry) tryArray[0];
				prevState.remove(prevState.size()-1);
				this.initGui();
			}
		}
		if(isLeftMouseKeyPressed && !Mouse.isButtonDown(0)) {
			isLeftMouseKeyPressed = false;
		}
		hoveringText.clear();
		drawBackground(0);
		if(currentCategory == null) {
			drawCategories(mouseX, mouseY);
		}
		if(currentCategory != null && currentDiscovery == null) {
			drawDiscoveries(mouseX, mouseY);
		}
		if(currentCategory != null && currentDiscovery != null) {
			drawPage(mouseX, mouseY);
		}
		drawAllText();
		if(!this.numberString.isEmpty()) {
			GlStateManager.translate(0, 0, 500);
			GlStateManager.color(1/(5F-pressDelay), 1/(5F-pressDelay), 1/(5F-pressDelay));
			this.drawCenteredString(fontRenderer, numberString, k+128, l+172, 0xffffff);
			GlStateManager.color(1, 1, 1);
			GlStateManager.translate(0, 0, -500);
		}
		RenderHelper.enableStandardItemLighting();
	}

	public void drawAllText() {
		for(int i = 0; i < hoveringText.size(); ++i) {
			Object[] drawable = (Object[])hoveringText.get(i);
			List<String> listToDraw = (List<String>) drawable[0];
			int x = Integer.parseInt(drawable[1].toString());
			int y = Integer.parseInt(drawable[2].toString());
			FontRenderer renderer = (FontRenderer)drawable[3];
			this.drawHoveringText(listToDraw, x, y, renderer);
		}
	}

	public void initDiscoveries() {
		currentPage = 0;
		int k = (this.width - 256) / 2;
		int l = (this.height - 168) / 2;
		GuiButtonNoSound back = new GuiButtonNoSound(0, k+236, l+7, 14, 18, "");
		this.buttonList.add(back);
		GuiButtonNoSound page_left = new GuiButtonNoSound(1,k+7,l+158,24,13,"");
		GuiButtonNoSound page_right = new GuiButtonNoSound(2,k+227,l+158,24,13,"");
		int discAmount = currentCategory.discoveries.size();
		if(discAmount - 48*(currentDiscoveryPage+1) <= 0) {
			page_left.enabled = false;
		}
		if(discAmount - 48*(currentDiscoveryPage+1) > 0) {
			page_right.enabled = true;
		}
		else {
			page_right.enabled = false;
		}

		this.buttonList.add(page_left);
		this.buttonList.add(page_right);
		for(int i = 48*currentDiscoveryPage; i < discAmount - 48*currentDiscoveryPage; ++i) {
			int dx = k + 22*(i/6) + 12;
			if(i >= 24) dx += 40;
			int dy = l + 22*(i%6) + 22;
			GuiButtonNoSound btnAdd = new GuiButtonNoSound(i + 3, dx, dy, 20, 20, "");
			this.buttonList.add(btnAdd);
		}
	}

	public void initCategories() {
		if(bookTag == null) {
			bookTag = new NBTTagCompound();
			bookTag.setInteger("tier", 3);
		}
		currentDiscoveryPage = 0;
		int k = (this.width - 256) / 2 + 128;
		int l = (this.height - 168) / 2;
		if(ApiCore.CATEGORY_LIST != null) {
			for(int i = 0; i < ApiCore.CATEGORY_LIST.size(); ++i) {
				CategoryEntry cat = ApiCore.CATEGORY_LIST.get(i);
				if(cat != null) {
					GuiButtonNoSound added = new GuiButtonNoSound(i, k + 30*(i/5) + 8, l + 30*(i%5) + 28, 20, 20, "");
					added.enabled = false;
					int reqTier = cat.reqTier;
					int tier = bookTag.getInteger("tier");
					if(tier >= reqTier) {
						added.enabled = true;
					}
					this.buttonList.add(added);
				}
			}
		}
	}

	public void initPage() {
		int k = (this.width - 256) / 2;
		int l = (this.height - 168) / 2;
		GuiButtonNoSound back = new GuiButtonNoSound(0, k+236, l+7, 14, 18, "");
		this.buttonList.add(back);
		GuiButtonNoSound page_left = new GuiButtonNoSound(1,k+7,l+158,24,13,"");
		GuiButtonNoSound page_right = new GuiButtonNoSound(2,k+227,l+158,24,13,"");
		int pagesMax = currentDiscovery.pages.size();
		if(currentPage <= 0) {
			page_left.enabled = false;
		}
		if(currentPage + 2 >= pagesMax) {
			page_right.enabled = false;
		}
		this.buttonList.add(page_left);
		this.buttonList.add(page_right);
	}

	public void drawPage(int mouseX, int mouseZ) {
		int pagesMax = currentDiscovery.pages.size();
		for(int ik = 0; ik < this.buttonList.size(); ++ik) {
			GlStateManager.color(1, 1, 1);
			GuiButton btn = this.buttonList.get(ik);
			boolean hover = mouseX >= btn.x && mouseZ >= btn.y && mouseX < btn.x + btn.width && mouseZ < btn.y + btn.height;
			int id = btn.id;
			if(id == 0) {
				if(currentCategory == null || currentCategory.specificBookTextures == null) {
					this.mc.renderEngine.bindTexture(DEFAULT_BOOK_TEXTURE);
				}
				else {
					this.mc.renderEngine.bindTexture(currentCategory.specificBookTextures);
				}
				if(hover) {
					GlStateManager.color(1, 0.8F, 1);
				}
				this.drawTexturedModalRect(btn.x, btn.y, 49, 238, 14, 18);
			}
			if(id == 1) {
				if(currentCategory == null || currentCategory.specificBookTextures == null) {
					this.mc.renderEngine.bindTexture(DEFAULT_BOOK_TEXTURE);
				}
				else {
					this.mc.renderEngine.bindTexture(currentCategory.specificBookTextures);
				}
				if(hover && btn.enabled) {
					GlStateManager.color(1, 0.8F, 1);
				}
				if(!btn.enabled) {
					GlStateManager.color(0.3F, 0.3F, 0.3F);
				}
				this.drawTexturedModalRect(btn.x, btn.y, 0, 243, 24, 13);
			}
			if(id == 2) {
				if(currentCategory == null || currentCategory.specificBookTextures == null) {
					this.mc.renderEngine.bindTexture(DEFAULT_BOOK_TEXTURE);
				}
				else {
					this.mc.renderEngine.bindTexture(currentCategory.specificBookTextures);
				}
				if(hover && btn.enabled) {
					GlStateManager.color(1, 0.8F, 1);
				}
				if(!btn.enabled) {
					GlStateManager.color(0.3F, 0.3F, 0.3F);
				}
				this.drawTexturedModalRect(btn.x, btn.y, 25, 243, 24, 13);
			}
		}
		//Text Draw
		for(int ik = 0; ik < this.buttonList.size(); ++ik) {
			GlStateManager.color(1, 1, 1);
			GuiButton btn  = this.buttonList.get(ik);
			boolean hover = mouseX >= btn.x && mouseZ >= btn.y && mouseX < btn.x + btn.width && mouseZ < btn.y + btn.height;
			int id = btn.id;
			if(id == 0) {
				if(hover) {
					List<String> catStr = new ArrayList<String>();
					catStr.add(I18n.translateToLocal("essentialcraft.text.button.back"));
					this.addHoveringText(catStr, mouseX, mouseZ);
				}
			}
		}

		this.drawPage_0(mouseX, mouseZ);
		if(currentPage+1 < pagesMax) {
			this.drawPage_1(mouseX, mouseZ);
		}
	}

	public void drawPage_0(int mouseX, int mouseY) {
		PageEntry page = currentDiscovery.pages.get(currentPage);
		int k = (this.width - 256) / 2;
		int l = (this.height - 168) / 2;
		if(currentPage == 0) {
			String added = "";
			if(page.pageTitle == null || page.pageTitle.isEmpty()) {
				if(currentDiscovery.name == null || currentDiscovery.name.isEmpty()) {
					added = TextFormatting.BOLD+I18n.translateToLocal("ec3book.discovery_"+currentDiscovery.id+".name");
				}
				else {
					added = currentDiscovery.name;
				}
			}
			else {
				added = page.pageTitle;
			}
			this.fontRenderer.drawStringWithShadow(added, k+6, l+10, 0xaa88ff);
		}
		else if(page.pageTitle != null && !page.pageTitle.isEmpty()) {
			this.fontRenderer.drawStringWithShadow(page.pageTitle, k+6, l+10, 0xffffff);
		}

		if(page.pageImgLink != null) {
			GlStateManager.color(1, 1, 1);
			GlStateManager.disableLighting();
			this.mc.renderEngine.bindTexture(page.pageImgLink);
			drawScaledCustomSizeModalRect(k+16, l+10, 0, 0, 256, 256, 100, 100, 256, 256);
			l += 86;
		}

		if(page.displayedItems != null) {
			for(int i = 0; i < page.displayedItems.length; ++i) {
				ItemStack is = page.displayedItems[i];
				if(!is.isEmpty()) {
					this.drawIS(is, k + 10 + i%4*20, l + 10 + i/4 * 20, mouseX, mouseY, 0);
				}
			}

			for(int i = 0; i < page.displayedItems.length; ++i) {
				ItemStack is = page.displayedItems[i];
				if(!is.isEmpty()) {
					this.drawIS(is, k + 10 + i%4*20, l + 10 + i/4 * 20, mouseX, mouseY, 1);
				}
			}

			l += page.displayedItems.length/4 * 20;
		}
		if(page.pageRecipe != null) {
			if(page.displayedItems != null) {
				l += page.displayedItems.length/4 * 20;
			}
			l += this.drawRecipe(mouseX, mouseY, k, l, page.pageRecipe);
		}
		if(page.pageText != null && !page.pageText.isEmpty()) {
			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.disableLighting();
			this.fontRenderer.setUnicodeFlag(true);
			this.fontRenderer.drawSplitString(page.pageText, k+12, l+25, 110, currentCategory.textColor);
			this.fontRenderer.setUnicodeFlag(false);
			GlStateManager.enableLighting();
			GlStateManager.disableBlend();
			RenderHelper.disableStandardItemLighting();
			RenderHelper.enableGUIStandardItemLighting();
		}
	}

	public void drawPage_1(int mouseX, int mouseY) {
		if(currentDiscovery.pages.size() > currentPage+1) {
			PageEntry page = currentDiscovery.pages.get(currentPage+1);
			int k = (this.width - 256) / 2 + 128;
			int l = (this.height - 168) / 2;

			if(page.pageTitle != null && !page.pageTitle.isEmpty()) {
				this.fontRenderer.drawStringWithShadow(page.pageTitle, k+6, l+10, 0xffffff);
			}
			if(page.pageImgLink != null) {
				GlStateManager.disableLighting();
				GlStateManager.color(1, 1, 1);
				this.mc.renderEngine.bindTexture(page.pageImgLink);
				drawScaledCustomSizeModalRect(k+16, l+10, 0, 0, 256, 256, 100, 100, 256, 256);
				l += 86;
			}

			if(page.displayedItems != null) {
				for(int i = 0; i < page.displayedItems.length; ++i) {
					ItemStack is = page.displayedItems[i];
					if(!is.isEmpty()) {
						this.drawIS(is, k + 10 + i%4*20, l + 10 + i/4 * 20, mouseX, mouseY, 0);
					}
				}

				for(int i = 0; i < page.displayedItems.length; ++i) {
					ItemStack is = page.displayedItems[i];
					if(!is.isEmpty()) {
						this.drawIS(is, k + 10 + i%4*20, l + 10 + i/4 * 20, mouseX, mouseY, 1);
					}
				}

				l += page.displayedItems.length/4 * 20;
			}
			if(page.pageRecipe != null) {
				if(page.displayedItems != null) {
					l += page.displayedItems.length/4 * 20;
				}
				l += this.drawRecipe(mouseX, mouseY, k, l, page.pageRecipe);
			}
			if(page.pageText != null && !page.pageText.isEmpty()) {
				List<String> display = parse(page.pageText);
				for(int i = 0; i < display.size(); ++i) {
					RenderHelper.enableStandardItemLighting();
					GlStateManager.enableBlend();
					GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					GlStateManager.disableLighting();
					this.fontRenderer.setUnicodeFlag(true);
					this.fontRenderer.drawSplitString(page.pageText, k+12, l+25, 110, currentCategory.textColor);
					this.fontRenderer.setUnicodeFlag(false);
					GlStateManager.enableLighting();
					GlStateManager.disableBlend();
					RenderHelper.disableStandardItemLighting();
					RenderHelper.enableGUIStandardItemLighting();
				}
			}
		}
	}

	public int drawRecipe(int mouseX, int mouseZ, int k, int l, IRecipe toDraw) {
		//2
		if(toDraw instanceof ShapedOreRecipe) {
			return drawShapedOreRecipe(mouseX,mouseZ,k,l,(ShapedOreRecipe) toDraw);
		}
		//3
		if(toDraw instanceof ShapelessOreRecipe) {
			return drawShapelessOreRecipe(mouseX,mouseZ,k,l,(ShapelessOreRecipe) toDraw);
		}
		//5
		if(toDraw instanceof RadiatingChamberRecipe) {
			return drawRadiatingChamberRecipe(mouseX,mouseZ,k,l,(RadiatingChamberRecipe) toDraw);
		}
		//6
		if(toDraw instanceof MagicianTableRecipe) {
			return drawMagicianTableRecipe(mouseX,mouseZ,k,l,(MagicianTableRecipe) toDraw);
		}
		//?7?
		if(toDraw instanceof StructureRecipe) {
			return drawStructureRecipe(mouseX,mouseZ,k,l,(StructureRecipe) toDraw);
		}
		return 0;
	}

	public int drawMagicianTableRecipe(int mouseX, int mouseZ, int k, int l, MagicianTableRecipe toDraw) {
		this.fontRenderer.drawString(I18n.translateToLocal("essentialcraft.txt.magicianRecipe"), k+24, l+12, 0x222222);
		this.fontRenderer.drawString(I18n.translateToLocal("MRU Required: "+toDraw.mruRequired), k+26, l+83, 0x222222);

		GlStateManager.disableLighting();
		RenderHelper.disableStandardItemLighting();
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.color(1, 1, 1);
		DrawUtils.bindTexture("essentialcraft", "textures/gui/mrustorage.png");
		DrawUtils.drawTexturedModalRect(k+7, l+20, 0, 0, 18, 72,1);
		int percentageScaled = MathUtils.pixelatedTextureSize(toDraw.mruRequired, 5000, 72);
		TextureAtlasSprite icon = (TextureAtlasSprite)EssentialCraftCore.proxy.getClientIcon("mru");
		DrawUtils.drawTexture(k+8, l-1+74-percentageScaled+20, icon, 16, percentageScaled-2, 2);

		this.drawSlotInRecipe(k, l, 13, 8);
		this.drawSlotInRecipe(k, l, 13+36, 8);
		this.drawSlotInRecipe(k, l, 13, 8+36);
		this.drawSlotInRecipe(k, l, 13+36, 8+36);
		this.drawSlotInRecipe(k, l, 13+18, 8+18);
		this.drawSlotInRecipe(k, l, 13+74, 8+18);

		if(toDraw.requiredItems.length > 0) {
			this.drawIS(IngredientUtils.getStackToDraw(toDraw.requiredItems[0], System.currentTimeMillis()/50), k+26+18, l+25+18, mouseX, mouseZ, 0);
		}
		if(toDraw.requiredItems.length > 1) {
			this.drawIS(IngredientUtils.getStackToDraw(toDraw.requiredItems[1], System.currentTimeMillis()/50), k+26, l+25, mouseX, mouseZ, 0);
		}
		if(toDraw.requiredItems.length > 2) {
			this.drawIS(IngredientUtils.getStackToDraw(toDraw.requiredItems[2], System.currentTimeMillis()/50), k+26+36, l+25, mouseX, mouseZ, 0);
		}
		if(toDraw.requiredItems.length > 3) {
			this.drawIS(IngredientUtils.getStackToDraw(toDraw.requiredItems[3], System.currentTimeMillis()/50), k+26, l+25+36, mouseX, mouseZ, 0);
		}
		if(toDraw.requiredItems.length > 4) {
			this.drawIS(IngredientUtils.getStackToDraw(toDraw.requiredItems[4], System.currentTimeMillis()/50), k+26+36, l+25+36, mouseX, mouseZ, 0);
		}

		this.drawIS(toDraw.result, k+26+74, l+25+18, mouseX, mouseZ, 0);

		if(toDraw.requiredItems.length > 0) {
			this.drawIS(IngredientUtils.getStackToDraw(toDraw.requiredItems[0], System.currentTimeMillis()/50), k+26+18, l+25+18, mouseX, mouseZ, 1);
		}
		if(toDraw.requiredItems.length > 1) {
			this.drawIS(IngredientUtils.getStackToDraw(toDraw.requiredItems[1], System.currentTimeMillis()/50), k+26, l+25, mouseX, mouseZ, 1);
		}
		if(toDraw.requiredItems.length > 2) {
			this.drawIS(IngredientUtils.getStackToDraw(toDraw.requiredItems[2], System.currentTimeMillis()/50), k+26+36, l+25, mouseX, mouseZ, 1);
		}
		if(toDraw.requiredItems.length > 3) {
			this.drawIS(IngredientUtils.getStackToDraw(toDraw.requiredItems[3], System.currentTimeMillis()/50), k+26, l+25+36, mouseX, mouseZ, 1);
		}
		if(toDraw.requiredItems.length > 4) {
			this.drawIS(IngredientUtils.getStackToDraw(toDraw.requiredItems[4], System.currentTimeMillis()/50), k+26+36, l+25+36, mouseX, mouseZ, 1);
		}

		this.drawIS(toDraw.result, k+26+74, l+25+18, mouseX, mouseZ, 1);
		return 80;
	}

	public int drawRadiatingChamberRecipe(int mouseX, int mouseZ, int k, int l, RadiatingChamberRecipe toDraw) {
		this.fontRenderer.drawString(I18n.translateToLocal("essentialcraft.txt.radiatingRecipe"), k+8, l+12, 0x222222);
		this.fontRenderer.drawString(I18n.translateToLocal("MRU Required: "+toDraw.mruRequired), k+26, l+83, 0x222222);
		TextFormatting addeddCF = TextFormatting.RESET;
		float upperBalance = toDraw.upperBalanceLine;
		if(upperBalance > 2.0F) {
			upperBalance = 2.0F;
		}
		if(upperBalance > 1.0F) {
			addeddCF = TextFormatting.RED;
		}
		else if(upperBalance < 1.0F) {
			addeddCF = TextFormatting.BLUE;
		}
		else {
			addeddCF = TextFormatting.AQUA;
		}
		String balanceUpper = Float.toString(upperBalance);
		if(balanceUpper.length() > 4) {
			balanceUpper = balanceUpper.substring(0, 4);
		}
		this.fontRenderer.drawString(I18n.translateToLocal(I18n.translateToLocal("essentialcraft.txt.format.upperBalance")+addeddCF+balanceUpper), k+44, l+32, 0x222222);

		float lowerBalance = toDraw.lowerBalanceLine;
		if(lowerBalance < 0.0F) {
			lowerBalance = 0.0F;
		}
		if(lowerBalance > 1.0F) {
			addeddCF = TextFormatting.RED;
		}
		else if(lowerBalance < 1.0F) {
			addeddCF = TextFormatting.BLUE;
		}
		else {
			addeddCF = TextFormatting.AQUA;
		}
		String balanceLower = Float.toString(lowerBalance);
		if(balanceLower.length() > 4)
			balanceLower = balanceLower.substring(0, 4);
		this.fontRenderer.drawString(I18n.translateToLocal(I18n.translateToLocal("essentialcraft.txt.format.lowerBalance")+addeddCF+balanceLower), k+44, l+32+36, 0x222222);

		this.fontRenderer.drawString("MRU/t "+(int)toDraw.costModifier, k+44+18, l+32+18, 0x222222);

		GlStateManager.disableLighting();
		RenderHelper.disableStandardItemLighting();
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.color(1, 1, 1);
		DrawUtils.bindTexture("essentialcraft", "textures/gui/mrustorage.png");
		DrawUtils.drawTexturedModalRect(k+7, l+20, 0, 0, 18, 72,1);
		int percentageScaled = MathUtils.pixelatedTextureSize((int) (toDraw.mruRequired*toDraw.costModifier), 5000, 72);
		TextureAtlasSprite icon = (TextureAtlasSprite) EssentialCraftCore.proxy.getClientIcon("mru");
		DrawUtils.drawTexture(k+8, l-1+74-percentageScaled+20, icon, 16, percentageScaled-2, 2);
		int positionY = 8;
		this.drawSlotInRecipe(k, l, 13, 4+positionY);
		this.drawSlotInRecipe(k, l, 13+18, 22+positionY);
		this.drawSlotInRecipe(k, l, 13, 40+positionY);

		if(toDraw.recipeItems.length > 0) {
			this.drawIS(IngredientUtils.getStackToDraw(toDraw.recipeItems[0], System.currentTimeMillis()/50), k+26, l+21+positionY, mouseX, mouseZ, 0);
		}
		if(toDraw.recipeItems.length > 1) {
			this.drawIS(IngredientUtils.getStackToDraw(toDraw.recipeItems[1], System.currentTimeMillis()/50), k+26, l+21+36+positionY, mouseX, mouseZ, 0);
		}
		this.drawIS(toDraw.result, k+26+18, l+21+18+positionY, mouseX, mouseZ, 0);

		if(toDraw.recipeItems.length > 0) {
			this.drawIS(IngredientUtils.getStackToDraw(toDraw.recipeItems[0], System.currentTimeMillis()/50), k+26, l+21+positionY, mouseX, mouseZ, 1);
		}
		if(toDraw.recipeItems.length > 1) {
			this.drawIS(IngredientUtils.getStackToDraw(toDraw.recipeItems[1], System.currentTimeMillis()/50), k+26, l+21+36+positionY, mouseX, mouseZ, 1);
		}
		this.drawIS(toDraw.result, k+26+18, l+21+18+positionY, mouseX, mouseZ, 1);
		return 90;
	}

	public int drawStructureRecipe(int mouseX, int mouseZ, int k, int l, StructureRecipe toDraw) {
		try {
			this.fontRenderer.drawString(I18n.translateToLocal("essentialcraft.txt.structure"), k+8, l+12, 0x222222);
			l += 5;
			StructureRecipe recipe = toDraw;
			int highestStructureBlk = 0;
			for(StructureBlock blk : recipe.structure) {
				if(blk.y > highestStructureBlk) {
					highestStructureBlk = blk.y;
				}
			}
			for(StructureBlock blk : recipe.structure) {
				if(!Config.renderStructuresFromAbove) {
					this.drawSB(blk, k+52+blk.x*12-blk.z*12, l+32+highestStructureBlk*20-blk.y*20+blk.z*12+blk.x*12, mouseX, mouseZ, 0);
				}
				else {
					this.drawSB(blk, k+52+blk.x*12, l+32+highestStructureBlk*20+blk.z*12, mouseX, mouseZ, 0);
				}
			}
			this.drawIS(recipe.referal, k+52, l+144, mouseX, mouseZ, 0);

			for(StructureBlock blk : recipe.structure) {
				if(!Config.renderStructuresFromAbove) {
					this.drawSB(blk, k+52+blk.x*12-blk.z*12, l+32+highestStructureBlk*20-blk.y*20+blk.z*12+blk.x*12, mouseX, mouseZ, 1);
				}
				else {
					this.drawSB(blk, k+52+blk.x*12, l+32+highestStructureBlk*20+blk.z*12, mouseX, mouseZ, 1);
				}
			}

			this.drawIS(recipe.referal, k+52, l+144, mouseX, mouseZ, 1);

			return 60+highestStructureBlk*20;
		}
		catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	//Taken from JEI's CraftingGridHelper
	public int getCraftingIndex(int i, int width, int height) {
		int index;
		if(width == 1) {
			if(height == 3) {
				index = i*3+1;
			}
			else if (height == 2) {
				index = i*3+1;
			}
			else {
				index = 4;
			}
		}
		else if(height == 1) {
			index = i+3;
		}
		else if(width == 2) {
			index = i;
			if(i > 1) {
				index++;
				if(i > 3) {
					index++;
				}
			}
		}
		else if(height == 2) {
			index = i+3;
		}
		else {
			index = i;
		}
		return index;
	}

	public int drawShapedOreRecipe(int mouseX, int mouseZ, int k, int l, ShapedOreRecipe toDraw) {
		this.fontRenderer.drawString(I18n.translateToLocal("essentialcraft.txt.shapedRecipe"), k+8, l+12, 0x222222);
		ShapedOreRecipe recipe = toDraw;
		for(int i = 0; i < 9; ++i) {
			drawSlotInRecipe(k,l+6,i%3*18,i/3*18);
		}
		drawSlotInRecipe(k,l+6,80,1*18);
		DrawUtils.bindTexture("minecraft", "textures/gui/container/crafting_table.png");

		GlStateManager.color(1, 1, 1);
		this.drawTexturedModalRect(k+78-10, l+23+18, 90, 35, 22, 15);

		Random rnd = new Random(System.currentTimeMillis()/1000);

		int[] drawingID = new int[9];
		for(int i = 0; i < recipe.getWidth()*recipe.getHeight(); ++i) {
			int j = getCraftingIndex(i, recipe.getWidth(), recipe.getHeight());
			Ingredient drawable = toDraw.getIngredients().get(i);
			ItemStack needToDraw = ItemStack.EMPTY;
			if(drawable.getMatchingStacks().length > 0) {
				drawingID[i] = rnd.nextInt(drawable.getMatchingStacks().length);
				needToDraw = drawable.getMatchingStacks()[drawingID[i]];
			}
			if(!needToDraw.isEmpty()) {
				this.drawIS(needToDraw, k + 13 + j%3*18, l + 23 + j/3 * 18, mouseX, mouseZ, 0);
			}
		}

		this.drawIS(recipe.getRecipeOutput(), k + 93, l + 41, mouseX, mouseZ, 0);

		for(int i = 0; i < recipe.getRecipeHeight()*recipe.getRecipeWidth(); ++i) {
			int j = getCraftingIndex(i, recipe.getRecipeWidth(), recipe.getRecipeHeight());
			Ingredient drawable = recipe.getIngredients().get(i);
			ItemStack needToDraw = ItemStack.EMPTY;
			if(drawable.getMatchingStacks().length > 0) {
				needToDraw = drawable.getMatchingStacks()[drawingID[i]];
			}
			if(!needToDraw.isEmpty()) {
				this.drawIS(needToDraw, k + 13 + j%3*18, l + 23 + j/3 * 18, mouseX, mouseZ, 1);
			}
		}

		this.drawIS(recipe.getRecipeOutput(), k + 93, l + 41, mouseX, mouseZ, 1);

		return 56;
	}

	public int drawShapelessOreRecipe(int mouseX, int mouseZ, int k, int l, ShapelessOreRecipe toDraw) {
		this.fontRenderer.drawString(I18n.translateToLocal("essentialcraft.txt.shapelessRecipe"), k+8, l+12, 0x222222);
		NonNullList<Ingredient> input = toDraw.getIngredients();

		int width, height;
		if(input.size() > 4) {
			width = height = 3;
		}
		else if(input.size() > 1) {
			width = height = 2;
		}
		else {
			width = height = 1;
		}

		for(int i = 0; i < 9; ++i) {
			drawSlotInRecipe(k,l+6,i%3*18,i/3*18);
		}
		drawSlotInRecipe(k,l+6,80,1*18);

		DrawUtils.bindTexture("minecraft", "textures/gui/container/crafting_table.png");

		GlStateManager.color(1, 1, 1);
		this.drawTexturedModalRect(k+78-10, l+23+18, 90, 35, 22, 15);

		Random rnd = new Random(System.currentTimeMillis()/1000);

		int[] drawingID = new int[9];

		for(int i = 0; i < input.size(); ++i) {
			int j = getCraftingIndex(i, width, height);
			Ingredient drawable = input.get(i);
			ItemStack needToDraw = ItemStack.EMPTY;
			if(drawable.getMatchingStacks().length > 0) {
				drawingID[i] = rnd.nextInt(drawable.getMatchingStacks().length);
				needToDraw = drawable.getMatchingStacks()[drawingID[i]];
			}
			if(!needToDraw.isEmpty()) {
				this.drawIS(needToDraw, k + 13 + j%3*18, l + 23 + j/3 * 18, mouseX, mouseZ, 0);
			}
		}

		this.drawIS(toDraw.getRecipeOutput(), k + 93, l + 41, mouseX, mouseZ, 0);

		for(int i = 0; i < input.size(); ++i) {
			int j = getCraftingIndex(i, width, height);
			Ingredient drawable = input.get(i);
			ItemStack needToDraw = ItemStack.EMPTY;
			if(drawable.getMatchingStacks().length > 0) {
				needToDraw = drawable.getMatchingStacks()[drawingID[i]];
			}
			if(!needToDraw.isEmpty()) {
				this.drawIS(needToDraw, k + 13 + j%3*18, l + 23 + j/3 * 18, mouseX, mouseZ, 1);
			}
		}

		this.drawIS(toDraw.getRecipeOutput(), k + 93, l + 41, mouseX, mouseZ, 1);

		return 56;
	}

	public void drawSlotInRecipe(int k, int l, int defaultX, int defaultY)
	{
		GlStateManager.color(1, 1, 1);
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.enableRescaleNormal();
		this.drawGradientRect(k+12+defaultX, l+16+defaultY, k+12+18+defaultX, l+16+18+defaultY, 0x88ffaaff, 0x88886688);
		this.drawGradientRect(k+12+defaultX, l+16+defaultY, k+12+1+defaultX, l+16+18+defaultY, 0xff660066, 0xff330033);
		this.drawGradientRect(k+12+defaultX, l+16+17+defaultY, k+12+18+defaultX, l+16+18+defaultY, 0xff330033, 0xff110011);
		this.drawGradientRect(k+12+17+defaultX, l+16+defaultY, k+12+18+defaultX, l+16+18+defaultY, 0xff990099, 0xff110011);
		this.drawGradientRect(k+12+defaultX, l+16+defaultY, k+12+18+defaultX, l+16+1+defaultY, 0xff660066, 0xff990099);
	}

	public List<String> parse(String s) {
		List<String> rtLst = new ArrayList<String>();
		int maxSymbols = 24;
		int cycle = 1;
		String added = "";
		for(int i = 0; i < s.length(); ++i) {
			if(i+1 < s.length()) {
				String substr = s.substring(i,i+1);
				if(substr.equals("|")) {
					rtLst.add(added);
					rtLst.add("");
					added = "";
					++i;
				}
				added += s.substring(i, i+1);
			}
			else {
				added += s.substring(s.length()-1);
			}
			if(added.length() > maxSymbols || added.length() + maxSymbols*(cycle-1) == s.length() || i == s.length()-1) {
				int index = added.lastIndexOf(" ");
				if(index == -1) index = 24;
				if(index >= added.length()) index = added.length()-1;
				if(i == s.length()-1)index = added.length()-1;
				String add = added.substring(0, index+1);
				rtLst.add(add);
				String nxtCycle = added.substring(index+1);
				added = ""+nxtCycle;
				++cycle;
			}
		}
		return rtLst;
	}

	public void drawDiscoveries(int mouseX, int mouseZ) {
		int k = (this.width - 256) / 2;
		int l = (this.height - 168) / 2;
		this.fontRenderer.drawStringWithShadow(TextFormatting.GOLD+" "+TextFormatting.BOLD + I18n.translateToLocal("essentialcraft.txt.book.startup"), k+6, l+10, 0xffffff);
		CategoryEntry cat = currentCategory;
		if(cat.name != null && !cat.name.isEmpty()) {
			this.fontRenderer.drawStringWithShadow(TextFormatting.GOLD+" "+cat.name, k+6+128, l+10, 0xFFFFFF);
		}
		else {
			this.fontRenderer.drawStringWithShadow(TextFormatting.GOLD+" "+I18n.translateToLocal("ec3book.category_"+currentCategory.id+".name"), k+6+128, l+10, 0xffffff);
		}
		RenderHelper.enableGUIStandardItemLighting();
		for(int ik = 0; ik < this.buttonList.size(); ++ik) {
			GlStateManager.color(1, 1, 1);
			GuiButton btn  = this.buttonList.get(ik);
			boolean hover = mouseX >= btn.x && mouseZ >= btn.y && mouseX < btn.x + btn.width && mouseZ < btn.y + btn.height;
			int id = btn.id;
			if(id == 0) {
				if(currentCategory == null || currentCategory.specificBookTextures == null) {
					this.mc.renderEngine.bindTexture(DEFAULT_BOOK_TEXTURE);
				}
				else {
					this.mc.renderEngine.bindTexture(currentCategory.specificBookTextures);
				}
				if(hover) {
					GlStateManager.color(1, 0.8F, 1);
				}
				this.drawTexturedModalRect(btn.x, btn.y, 49, 238, 14, 18);
			}
			if(id == 1) {
				if(currentCategory == null || currentCategory.specificBookTextures == null) {
					this.mc.renderEngine.bindTexture(DEFAULT_BOOK_TEXTURE);
				}
				else {
					this.mc.renderEngine.bindTexture(currentCategory.specificBookTextures);
				}
				if(hover && btn.enabled) {
					GlStateManager.color(1, 0.8F, 1);
				}
				if(!btn.enabled) {
					GlStateManager.color(0.3F, 0.3F, 0.3F);
				}
				this.drawTexturedModalRect(btn.x, btn.y, 0, 243, 24, 13);
			}
			if(id == 2) {
				if(currentCategory == null || currentCategory.specificBookTextures == null) {
					this.mc.renderEngine.bindTexture(DEFAULT_BOOK_TEXTURE);
				}
				else {
					this.mc.renderEngine.bindTexture(currentCategory.specificBookTextures);
				}
				if(hover && btn.enabled) {
					GlStateManager.color(1, 0.8F, 1);
				}
				if(!btn.enabled) {
					GlStateManager.color(0.3F, 0.3F, 0.3F);
				}
				this.drawTexturedModalRect(btn.x, btn.y, 25, 243, 24, 13);
			}
			if(id > 2) {
				DiscoveryEntry disc = cat.discoveries.get(48*currentDiscoveryPage + id - 3);
				GlStateManager.color(1, 1, 1);
				if(currentCategory == null || currentCategory.specificBookTextures == null) {
					this.mc.renderEngine.bindTexture(DEFAULT_BOOK_TEXTURE);
				}
				else {
					this.mc.renderEngine.bindTexture(currentCategory.specificBookTextures);
				}
				if(disc.isNew) {
					GlStateManager.color(1, 1, 0);
				}
				if(!hover) {
					this.drawTexturedModalRect(btn.x, btn.y, 0, 222, 20, 20);
				}
				else {
					this.drawTexturedModalRect(btn.x, btn.y, 28, 222, 20, 20);
				}
				GlStateManager.color(1, 1, 1);
				if(!disc.displayStack.isEmpty()) {
					itemRender.renderItemAndEffectIntoGUI(disc.displayStack, btn.x+2, btn.y+2);
				}
				else if(disc.displayTexture != null) {
					this.mc.renderEngine.bindTexture(disc.displayTexture);
					drawModalRectWithCustomSizedTexture(btn.x+2, btn.y+2, 0, 0, 16, 16, 16, 16);
				}

				if(isCtrlKeyDown()) {
					GlStateManager.translate(0, 0, 500);
					GlStateManager.color(1, 1, 1);
					this.drawString(fontRenderer, btn.id-2+"", btn.x+15, btn.y+14, 0xffffff);
					GlStateManager.color(1, 1, 1);
					GlStateManager.translate(0, 0, -500);
				}
			}
		}
		RenderHelper.disableStandardItemLighting();
		//Text Draw
		for(int ik = 0; ik < this.buttonList.size(); ++ik) {
			GlStateManager.color(1, 1, 1);
			GuiButton btn  = this.buttonList.get(ik);
			boolean hover = mouseX >= btn.x && mouseZ >= btn.y && mouseX < btn.x + btn.width && mouseZ < btn.y + btn.height;
			int id = btn.id;
			if(id == 0) {
				if(hover) {
					List<String> catStr = new ArrayList<String>();
					catStr.add(I18n.translateToLocal("essentialcraft.text.button.back"));
					this.addHoveringText(catStr, mouseX, mouseZ);
				}
			}
			if(id > 2) {
				if(hover) {
					DiscoveryEntry disc = cat.discoveries.get(48*currentDiscoveryPage + id - 3);
					GlStateManager.color(1, 1, 1);
					List<String> discStr = new ArrayList<String>();
					if(disc.name == null || disc.name.isEmpty()) {
						discStr.add(TextFormatting.BOLD+I18n.translateToLocal("ec3book.discovery_"+disc.id+".name"));
					}
					else {
						discStr.add(disc.name);
					}
					if(disc.shortDescription == null || disc.shortDescription.isEmpty()) {
						discStr.add(TextFormatting.ITALIC+I18n.translateToLocal("ec3book.discovery_"+disc.id+".desc"));
					}
					else {
						discStr.add(disc.shortDescription);
					}
					if(disc.isNew) {
						discStr.add(TextFormatting.GOLD+"New");
					}
					discStr.add(I18n.translateToLocal("essentialcraft.txt.contains")+disc.pages.size()+I18n.translateToLocal("essentialcraft.txt.pages"));
					this.addHoveringText(discStr, mouseX, mouseZ);
				}
			}
		}
	}

	public void drawCategories(int mouseX, int mouseZ) {
		int k = (this.width - 256) / 2;
		int l = (this.height - 168) / 2;
		this.fontRenderer.drawStringWithShadow(TextFormatting.GOLD+" "+TextFormatting.BOLD+I18n.translateToLocal("essentialcraft.txt.book.startup"), k+6, l+10, 0xffffff);
		this.fontRenderer.drawStringWithShadow(TextFormatting.GOLD+" "+TextFormatting.BOLD+I18n.translateToLocal("essentialcraft.txt.book.categories"), k+134, l+10, 0xffffff);
		this.fontRenderer.drawStringWithShadow(TextFormatting.GOLD+I18n.translateToLocal("essentialcraft.txt.book.containedKnowledge"), k+16, l+25, 0xffffff);
		int tier = bookTag.getInteger("tier");
		for(int i = 0; i <= tier; ++i) {
			this.fontRenderer.drawStringWithShadow(TextFormatting.GRAY+"-"+TextFormatting.ITALIC+I18n.translateToLocal("essentialcraft.txt.book.tier_"+i), k+16, l+35+i*10, 0xffffff);
		}
		this.fontRenderer.drawStringWithShadow(TextFormatting.GOLD+I18n.translateToLocal("essentialcraft.txt.book.edition"), k+16, l+90, 0xffffff);
		this.fontRenderer.drawString(TextFormatting.DARK_GREEN+FMLCommonHandler.instance().findContainerFor(EssentialCraftCore.core).getDisplayVersion()+"r", k+16, l+100, 0xffffff);
		k += 128;
		this.mc.renderEngine.bindTexture(DEFAULT_BOOK_TEXTURE);
		GlStateManager.color(1, 1, 1);
		RenderHelper.enableGUIStandardItemLighting();
		for(int ik = 0; ik < this.buttonList.size(); ++ik) {
			GuiButton btn = this.buttonList.get(ik);
			boolean hover = mouseX >= btn.x && mouseZ >= btn.y && mouseX < btn.x + btn.width && mouseZ < btn.y + btn.height;
			CategoryEntry cat = ApiCore.CATEGORY_LIST.get(ik);
			int reqTier = cat.reqTier;
			if(tier >= reqTier) {
				this.mc.renderEngine.bindTexture(DEFAULT_BOOK_TEXTURE);
				if(!hover) {
					this.drawTexturedModalRect(btn.x, btn.y, 0, 222, 20, 20);
				}
				else {
					this.drawTexturedModalRect(btn.x, btn.y, 28, 222, 20, 20);
				}
				if(!cat.displayStack.isEmpty()) {
					itemRender.renderItemAndEffectIntoGUI(cat.displayStack, btn.x+2, btn.y+2);
				}
				else if(cat.displayTexture != null) {
					this.mc.renderEngine.bindTexture(cat.displayTexture);
					drawModalRectWithCustomSizedTexture(btn.x+2, btn.y+2, 0, 0, 16, 16, 16, 16);
				}
				if(isCtrlKeyDown()) {
					GlStateManager.translate(0, 0, 500);
					this.drawString(fontRenderer, btn.id+1+"", btn.x+15, btn.y+14, 0xffffff);
					GlStateManager.color(1, 1, 1);
					GlStateManager.translate(0, 0, -500);
				}
			}
		}
		RenderHelper.disableStandardItemLighting();
		//Text Overlay
		for(int ik = 0; ik < this.buttonList.size(); ++ik) {
			GuiButton btn  = this.buttonList.get(ik);
			boolean hover = mouseX >= btn.x && mouseZ >= btn.y && mouseX < btn.x + btn.width && mouseZ < btn.y + btn.height;
			CategoryEntry cat = ApiCore.CATEGORY_LIST.get(ik);
			int reqTier = cat.reqTier;
			if(tier >= reqTier) {
				if(hover) {
					List<String> catStr = new ArrayList<String>();
					if(cat.name == null || cat.name.isEmpty()) {
						catStr.add(TextFormatting.BOLD+I18n.translateToLocal("ec3book.category_"+cat.id+".name"));
					}
					else {
						catStr.add(cat.name);
					}
					if(cat.shortDescription == null || cat.shortDescription.isEmpty()) {
						catStr.add(TextFormatting.ITALIC+I18n.translateToLocal("ec3book.category_"+cat.id+".desc"));
					}
					else {
						catStr.add(cat.shortDescription);
					}
					catStr.add(I18n.translateToLocal("essentialcraft.txt.contains")+cat.discoveries.size()+I18n.translateToLocal("essentialcraft.txt.entries"));
					this.addHoveringText(catStr, mouseX, mouseZ);
				}
			}
		}
	}

	public void drawIS(ItemStack toDraw, int pX, int pZ, int mX, int mZ, int phase) {
		if(!toDraw.isEmpty()) {
			toDraw = MathUtils.<ItemStack>randomElement(MiscUtils.getSubItemsToDraw(toDraw), new Random(System.currentTimeMillis()/1000));
			if(phase == 0) {
				RenderHelper.enableGUIStandardItemLighting();
				itemRender.renderItemAndEffectIntoGUI(toDraw, pX, pZ);
				RenderHelper.disableStandardItemLighting();
				if(toDraw.getCount() > 1) {
					GlStateManager.translate(0, 0, 500);
					fontRenderer.drawStringWithShadow(toDraw.getCount()+"", pX+10, pZ+10, 0xFFFFFF);
					GlStateManager.translate(0, 0, -500);
				}
			}
			else {
				boolean hover = mX >= pX && mZ >= pZ && mX < pX + 16 && mZ < pZ + 16;
				if(hover) {
					List<String> catStr = toDraw.getTooltip(this.mc.player, ITooltipFlag.TooltipFlags.NORMAL);
					DiscoveryEntry ds = ApiCore.findDiscoveryByIS(toDraw);
					if(ds != null && ds != currentDiscovery) {
						catStr.add(TextFormatting.ITALIC + I18n.translateToLocal("essentialcraft.txt.is.press"));
						if(Mouse.isButtonDown(0) && !isLeftMouseKeyPressed) {
							prevState.add(new Object[]{currentDiscovery,currentPage,currentDiscoveryPage});
							isLeftMouseKeyPressed = true;
							currentPage = 0;
							currentDiscoveryPage = 0;
							currentDiscovery = ds;
							if(ds != null) {
								f:for(int i = 0; i < ds.pages.size(); ++i) {
									PageEntry entry = ds.pages.get(i);
									if(entry != null) {
										if(entry.displayedItems != null && entry.displayedItems.length > 0) {
											for(ItemStack is : entry.displayedItems) {
												if(toDraw.isItemEqual(is)) {
													currentPage = i - i%2;
													break f;
												}
											}
										}
										if(entry.pageRecipe != null) {
											ItemStack result = entry.pageRecipe.getRecipeOutput();
											if(result.isItemEqual(toDraw)) {
												currentPage = i - i%2;
												break f;
											}
										}
									}
								}
							}
							initGui();
						}
					}
					this.addHoveringText(catStr, mX, mZ);
				}
			}
		}
	}

	public void drawSB(StructureBlock drawable, int pX, int pZ, int mX, int mZ, int phase) {
		ItemStack toDraw;
		if(drawable.blk == Blocks.AIR || Item.getItemFromBlock(drawable.blk) == null) {
			//Handle stuff that can't be drawn
			if(drawable.blk == Blocks.WATER) {
				toDraw = new ItemStack(BlocksCore.water);
			}
			else if(drawable.blk == Blocks.LAVA) {
				toDraw = new ItemStack(BlocksCore.lava);
			}
			else if(drawable.blk == Blocks.FIRE) {
				toDraw = new ItemStack(BlocksCore.fire);
			}
			else if(drawable.blk == Blocks.AIR) {
				toDraw = new ItemStack(BlocksCore.air);
			}
			else {
				toDraw = ItemStack.EMPTY;
			}
		}
		else {
			toDraw = new ItemStack(drawable.blk, 1, drawable.metadata);
		}
		if(phase == 0) {
			RenderHelper.enableGUIStandardItemLighting();
			itemRender.renderItemAndEffectIntoGUI(toDraw, pX, pZ);
			RenderHelper.disableStandardItemLighting();
		}
		else {
			boolean hover = mX >= pX && mZ >= pZ && mX < pX + 16 && mZ < pZ + 16;
			if(hover) {
				List<String> catStr = toDraw.getTooltip(this.mc.player, ITooltipFlag.TooltipFlags.NORMAL);
				catStr.add(I18n.translateToLocal("essentialcraft.txt.relativePosition"));
				catStr.add("x: "+drawable.x);
				catStr.add("y: "+drawable.y);
				catStr.add("z: "+drawable.z);
				if(ApiCore.findDiscoveryByIS(toDraw) != null) {
					catStr.add(TextFormatting.ITALIC + I18n.translateToLocal("essentialcraft.txt.is.press"));
					if(Mouse.isButtonDown(0) && !this.isLeftMouseKeyPressed) {
						prevState.add(new Object[]{currentDiscovery,currentPage,currentDiscoveryPage});
						isLeftMouseKeyPressed = true;
						DiscoveryEntry switchTo = ApiCore.findDiscoveryByIS(toDraw);
						currentPage = 0;
						currentDiscoveryPage = 0;
						currentDiscovery = switchTo;
						if(switchTo != null) {
							f:for(int i = 0; i < switchTo.pages.size(); ++i) {
								PageEntry entry = switchTo.pages.get(i);
								if(entry != null) {
									if(entry.displayedItems != null && entry.displayedItems.length > 0) {
										for(ItemStack is : entry.displayedItems) {
											if(toDraw.isItemEqual(is)) {
												currentPage = i - i%2;
												break f;
											}
										}
									}
									if(entry.pageRecipe != null) {
										ItemStack result = entry.pageRecipe.getRecipeOutput();
										if(result.isItemEqual(toDraw)) {
											currentPage = i - i%2;
											break f;
										}
									}
								}
							}
						}
					}
				}
				this.addHoveringText(catStr, mX, mZ);
			}
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		try {
			super.mouseClicked(mouseX, mouseY, mouseButton);
		}
		catch(Exception e) {}
	}

	@Override
	protected void actionPerformed(GuiButton b) {
		if(ticksBeforePressing > 0) {
			return;
		}
		if(currentCategory == null) {
			CategoryEntry cat = ApiCore.CATEGORY_LIST.get(b.id);
			currentCategory = cat;
			initGui();
			return;
		}
		if(currentCategory != null && currentDiscovery == null) {
			if(b.id == 0) {
				currentCategory = null;
				initGui();
				return;
			}
			if(b.id == 1) {
				++currentDiscoveryPage;
				initGui();
				return;
			}
			if(b.id == 2) {
				--currentDiscoveryPage;
				initGui();
				return;
			}
			if(b.id > 2) {
				DiscoveryEntry disc = currentCategory.discoveries.get(48*currentDiscoveryPage + b.id - 3);
				currentDiscovery = disc;
				initGui();
				return;
			}
		}
		if(currentCategory != null && currentDiscovery != null) {
			if(b.id == 0) {
				currentDiscovery = null;
				initGui();
				return;
			}
			if(b.id == 1) {
				currentPage -= 2;
				initGui();
				return;
			}
			if(b.id == 2) {
				currentPage += 2;
				initGui();
				return;
			}
		}
	}

	@Override
	protected void renderToolTip(ItemStack stack, int x, int y) {
		List<String> list = stack.getTooltip(this.mc.player, this.mc.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);

		for(int k = 0; k < list.size(); ++k) {
			if(k == 0) {
				list.set(k, stack.getRarity().rarityColor + list.get(k));
			}
			else {
				list.set(k, TextFormatting.GRAY + list.get(k));
			}
		}

		FontRenderer font = stack.getItem().getFontRenderer(stack);
		drawHoveringText(list, x, y, font == null ? fontRenderer : font);
	}

	protected void addHoveringText(List<String> list, int x, int y) {
		hoveringText.add(new Object[] {list, x, y, fontRenderer});
	}

	@Override
	protected void drawHoveringText(List<String> list, int x, int y, FontRenderer font) {
		GlStateManager.disableLighting();
		if(!list.isEmpty()) {
			GlStateManager.disableRescaleNormal();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();

			int k = 0;
			for(String s : list) {
				int l = font.getStringWidth(s);
				if(l > k) {
					k = l;
				}
			}

			int j2 = x + 12;
			int k2 = y - 12;
			int i1 = 8;

			if(list.size() > 1) {
				i1 += 2 + (list.size() - 1) * 10;
			}

			if(j2 + k > this.width) {
				j2 -= 28 + k;
			}

			if(k2 + i1 + 6 > this.height) {
				k2 = this.height - i1 - 6;
			}

			this.zLevel = 600.0F;
			itemRender.zLevel = 600.0F;
			int j1 = 0xF0100008;
			this.drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
			this.drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
			this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
			this.drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
			this.drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
			int k1 = 0x505000FF;
			int l1 = (k1 & 0x00FEFEFE) >> 1 | k1 & 0xFF000000;
			this.drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
			this.drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
			this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
			this.drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

			for(int i2 = 0; i2 < list.size(); ++i2) {
				String s1 = list.get(i2);
				font.drawStringWithShadow(s1, j2, k2, -1);

				if(i2 == 0) {
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

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
