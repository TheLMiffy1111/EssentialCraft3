package ec3.integration.jei;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import DummyCore.Utils.DrawUtils;
import DummyCore.Utils.MathUtils;
import ec3.api.RadiatingChamberRecipe;
import ec3.api.RadiatingChamberRecipes;
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

public class RadiatingChamber {
	
	public static final String UID = "essentialcraft:radiatingChamber";
	
	public static List<RadiatingChamber.Wrapper> getRecipes() {
		ArrayList<RadiatingChamber.Wrapper> ret = Lists.<RadiatingChamber.Wrapper>newArrayList();
		for(List<RadiatingChamberRecipe> list : RadiatingChamberRecipes.recipes.values()) {
			for(RadiatingChamberRecipe rec : list) {
				ret.add(new RadiatingChamber.Wrapper(rec));
			}
		}
		return ret;
	}
	
	public static class Wrapper extends BlankRecipeWrapper {
		
		private RadiatingChamberRecipe rec;
		
		public Wrapper(RadiatingChamberRecipe rec) {
			this.rec = rec;
		}

		@Override
		public List<ItemStack> getInputs() {
			return Arrays.<ItemStack>asList(rec.recipeItems);
		}

		@Override
		public List<ItemStack> getOutputs() {
			return Collections.<ItemStack>singletonList(rec.result);
		}

		@Override
		public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
			int percentageScaled = MathUtils.pixelatedTextureSize((int)(rec.mruRequired*rec.costModifier), 5000, 72);
			TextureAtlasSprite icon = (TextureAtlasSprite)EssentialCraftCore.proxy.getClientIcon("mru");
			DrawUtils.drawTexture(1, -1+(74-percentageScaled), icon, 16, percentageScaled-2, 0);
			
			minecraft.fontRendererObj.drawString((int)(rec.mruRequired*rec.costModifier)+" MRU", 20, 59, 0xFFFFFF, true);
			minecraft.fontRendererObj.drawString(rec.mruRequired/20/60+"Min "+(rec.mruRequired/20-((rec.mruRequired/20/60)*60))+"Sec", 80, 59, 0xFFFFFF, true);
		
			float upperBalance = Math.min(rec.upperBalanceLine, 2F);
			float lowerBalance = Math.max(rec.lowerBalanceLine, 0F);
			
			minecraft.fontRendererObj.drawString("Upper Balance: "+upperBalance, 56, 5, 0xFFFFFF, true);
			minecraft.fontRendererObj.drawString("MRU/Tick: "+(int)rec.costModifier, 56, 23, 0xFFFFFF, true);
			minecraft.fontRendererObj.drawString("Lower Balance: "+lowerBalance, 56, 41, 0xFFFFFF, true);
		}

		@Override
		public void getIngredients(IIngredients arg0) {}
	}
	
	public static class Handler implements IRecipeHandler<RadiatingChamber.Wrapper> {

		@Override
		public String getRecipeCategoryUid() {
			return UID;
		}

		@Override
		public String getRecipeCategoryUid(RadiatingChamber.Wrapper arg0) {
			return UID;
		}

		@Override
		public Class<RadiatingChamber.Wrapper> getRecipeClass() {
			return RadiatingChamber.Wrapper.class;
		}

		@Override
		public RadiatingChamber.Wrapper getRecipeWrapper(RadiatingChamber.Wrapper arg0) {
			return arg0;
		}

		@Override
		public boolean isRecipeValid(RadiatingChamber.Wrapper arg0) {
			return !arg0.getInputs().isEmpty() && !arg0.getOutputs().isEmpty();
		}
	}
	
	public static class Category extends BlankRecipeCategory<RadiatingChamber.Wrapper> {

		private final IDrawable BG;
		
		public Category(IGuiHelper gh) {
			BG = gh.createDrawable(new ResourceLocation("essentialcraft:textures/gui/jei/radiating_chamber.png"), 0, 0, 162, 72);
		}
		
		@Override
		public IDrawable getBackground() {
			return BG;
		}

		@Override
		public String getTitle() {
			return I18n.translateToLocal("jei.essentialcraft.recipe.radiatingChamber");
		}

		@Override
		public String getUid() {
			return UID;
		}

		@Override
		public void setRecipe(IRecipeLayout arg0, RadiatingChamber.Wrapper arg1, IIngredients arg2) {
			arg0.getItemStacks().init(0, true, 18, 0);
			arg0.getItemStacks().init(1, true, 18, 36);
			arg0.getItemStacks().init(2, false, 36, 18);
			
			arg0.getItemStacks().set(0, arg1.getInputs().get(0));
			arg0.getItemStacks().set(1, arg1.getInputs().get(1));
			arg0.getItemStacks().set(2, arg1.getOutputs().get(0));
		}
	}
}
