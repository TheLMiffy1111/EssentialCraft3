package essentialcraft.integration.jei;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import DummyCore.Utils.DrawUtils;
import DummyCore.Utils.MathUtils;
import essentialcraft.api.MagicianTableRecipe;
import essentialcraft.common.mod.EssentialCraftCore;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public class MagicianTable {

	public static final String UID = "essentialcraft:magicianTable";

	public static class Wrapper implements IRecipeWrapper {

		private MagicianTableRecipe rec;

		public Wrapper(MagicianTableRecipe rec) {
			this.rec = rec;
		}

		@Override
		public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
			int percentageScaled = MathUtils.pixelatedTextureSize(rec.mruRequired, 5000, 72);
			TextureAtlasSprite icon = (TextureAtlasSprite)EssentialCraftCore.proxy.getClientIcon("mru");
			DrawUtils.drawTexture(1, -1+74-percentageScaled, icon, 16, percentageScaled-2, 0);

			minecraft.fontRenderer.drawString(rec.mruRequired+" MRU", 20, 59, 0xFFFFFF, true);
			minecraft.fontRenderer.drawString(rec.mruRequired/20/60+"Min "+(rec.mruRequired/20-rec.mruRequired/20/60*60)+"Sec", 80, 59, 0xFFFFFF, true);
		}

		@Override
		public void getIngredients(IIngredients arg0) {
			ArrayList<List<ItemStack>> ret = Lists.<List<ItemStack>>newArrayList();
			for(Ingredient ing : rec.requiredItems) {
				if(ing == Ingredient.EMPTY)
					ret.add(Collections.emptyList());
				else
					ret.add(Arrays.asList(ing.getMatchingStacks()));
			}
			arg0.setInputLists(ItemStack.class, ret);
			arg0.setOutput(ItemStack.class, rec.result);
		}
	}

	public static class Category implements IRecipeCategory<MagicianTable.Wrapper> {

		private final IDrawable BG;

		public Category(IGuiHelper gh) {
			BG = gh.createDrawable(new ResourceLocation("essentialcraft:textures/gui/jei/magician_table.png"), 0, 0, 162, 72);
		}

		@Override
		public IDrawable getBackground() {
			return BG;
		}

		@Override
		public String getTitle() {
			return I18n.translateToLocal("jei.essentialcraft.recipe.magicianTable");
		}

		@Override
		public String getUid() {
			return UID;
		}

		@Override
		public void setRecipe(IRecipeLayout arg0, MagicianTable.Wrapper arg1, IIngredients arg2) {
			arg0.getItemStacks().init(0, true, 36, 18);
			arg0.getItemStacks().init(1, true, 18, 0);
			arg0.getItemStacks().init(2, true, 54, 0);
			arg0.getItemStacks().init(3, true, 18, 36);
			arg0.getItemStacks().init(4, true, 54, 36);
			arg0.getItemStacks().init(5, false, 108, 18);

			for(int i = 0; i < 5; i++) {
				if(arg2.getInputs(ItemStack.class).size()>i)
					arg0.getItemStacks().set(i, arg2.getInputs(ItemStack.class).get(i));
			}
			arg0.getItemStacks().set(5, arg2.getOutputs(ItemStack.class).get(0));
		}

		@Override
		public String getModName() {
			return "essentialcraft";
		}
	}
}
