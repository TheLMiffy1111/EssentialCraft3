package ec3.integration.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import DummyCore.Utils.DrawUtils;
import DummyCore.Utils.MathUtils;
import DummyCore.Utils.UnformedItemStack;
import ec3.api.MagicianTableRecipe;
import ec3.api.MagicianTableRecipes;
import ec3.common.mod.EssentialCraftCore;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.IRecipeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public class MagicianTable {
	
	public static final String UID = "essentialcraft:magicianTable";
	
	public static List<MagicianTable.Wrapper> getRecipes() {
		ArrayList<MagicianTable.Wrapper> ret = Lists.<MagicianTable.Wrapper>newArrayList();
		for(MagicianTableRecipe rec : MagicianTableRecipes.recipes.values()) {
			ret.add(new MagicianTable.Wrapper(rec));
		}
		return ret;
	}
	
	public static class Wrapper extends BlankRecipeWrapper {
		
		private MagicianTableRecipe rec;
		
		public Wrapper(MagicianTableRecipe rec) {
			this.rec = rec;
		}

		@Override
		public List<List<ItemStack>> getInputs() {
			ArrayList<List<ItemStack>> ret = Lists.<List<ItemStack>>newArrayList();
			for(UnformedItemStack stk : rec.requiredItems) {
				if(stk == null)
					ret.add(null);
				else
					ret.add(stk.possibleStacks);
			}
			return ret;
		}

		@Override
		public List<ItemStack> getOutputs() {
			return Collections.<ItemStack>singletonList(rec.result);
		}

		@Override
		public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
			int percentageScaled = MathUtils.pixelatedTextureSize(rec.mruRequired, 5000, 72);
			TextureAtlasSprite icon = (TextureAtlasSprite)EssentialCraftCore.proxy.getClientIcon("mru");
			DrawUtils.drawTexture(1, -1+(74-percentageScaled), icon, 16, percentageScaled-2, 0);
			
			minecraft.fontRendererObj.drawString(rec.mruRequired+" MRU", 20, 59, 0xFFFFFF, true);
			minecraft.fontRendererObj.drawString(rec.mruRequired/20/60+"Min "+(rec.mruRequired/20-((rec.mruRequired/20/60)*60))+"Sec", 80, 59, 0xFFFFFF, true);
		}

		@Override
		public void getIngredients(IIngredients arg0) {}
	}
	
	public static class Handler implements IRecipeHandler<MagicianTable.Wrapper> {

		@Override
		public String getRecipeCategoryUid() {
			return UID;
		}

		@Override
		public String getRecipeCategoryUid(MagicianTable.Wrapper arg0) {
			return UID;
		}

		@Override
		public Class<MagicianTable.Wrapper> getRecipeClass() {
			return MagicianTable.Wrapper.class;
		}

		@Override
		public MagicianTable.Wrapper getRecipeWrapper(MagicianTable.Wrapper arg0) {
			return arg0;
		}

		@Override
		public boolean isRecipeValid(MagicianTable.Wrapper arg0) {
			return !arg0.getInputs().isEmpty() && !arg0.getOutputs().isEmpty();
		}
	}
	
	public static class Category extends BlankRecipeCategory<MagicianTable.Wrapper> {

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
			
			for(int i = 0; i < 5; i++)
				arg0.getItemStacks().set(i, arg1.getInputs().get(i));
			arg0.getItemStacks().set(5, arg1.getOutputs().get(0));
		}
	}
}
