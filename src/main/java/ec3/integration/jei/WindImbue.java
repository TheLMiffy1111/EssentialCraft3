package ec3.integration.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import ec3.api.WindImbueRecipe;
import ec3.common.block.BlocksCore;
import ec3.common.item.ItemSoulStone;
import ec3.common.item.ItemsCore;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;

public class WindImbue {
	
	public static final String UID = "essentialcraft:windImbue";
	
	public static List<WindImbue.Wrapper> getRecipes() {
		ArrayList<WindImbue.Wrapper> ret = Lists.<WindImbue.Wrapper>newArrayList();
		for(WindImbueRecipe rec : WindImbueRecipe.recipes) {
			ret.add(new WindImbue.Wrapper(rec));
		}
		return ret;
	}
	
	public static class Wrapper extends BlankRecipeWrapper {
		
		private WindImbueRecipe rec;
		
		public Wrapper(WindImbueRecipe rec) {
			this.rec = rec;
		}
		
		@Override
		public List<ItemStack> getInputs() {
			return Lists.<ItemStack>newArrayList(rec.transforming, new ItemStack(BlocksCore.windRune));
		}

		@Override
		public List<ItemStack> getOutputs() {
			return Collections.singletonList(rec.result);
		}

		@Override
		public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
			minecraft.fontRendererObj.drawString(MathHelper.floor(rec.enderEnergy)+" ESPE", 0, 36, 0x000000, false);
			if(rec.result != null && rec.result.getItem() instanceof ItemSoulStone)
				minecraft.fontRendererObj.drawString("+Wind Relations", 0, 46, 0x81d17d, true);
		}

		@Override
		public void getIngredients(IIngredients paramIIngredients) {}
	}
	
	public static class Handler implements IRecipeHandler<WindImbue.Wrapper> {

		@Override
		public String getRecipeCategoryUid() {
			return UID;
		}

		@Override
		public String getRecipeCategoryUid(WindImbue.Wrapper arg0) {
			return UID;
		}

		@Override
		public Class<WindImbue.Wrapper> getRecipeClass() {
			return WindImbue.Wrapper.class;
		}

		@Override
		public IRecipeWrapper getRecipeWrapper(WindImbue.Wrapper arg0) {
			return arg0;
		}

		@Override
		public boolean isRecipeValid(WindImbue.Wrapper arg0) {
			return !arg0.getInputs().isEmpty() && !arg0.getOutputs().isEmpty();
		}
	}
	
	public static class Category extends BlankRecipeCategory<WindImbue.Wrapper> {

		private final IDrawable BG;
		
		public Category(IGuiHelper gh) {
			BG = gh.createDrawable(new ResourceLocation("essentialcraft:textures/gui/jei/wind_imbue.png"), 0, 0, 57, 60);
		}
		
		@Override
		public IDrawable getBackground() {
			return BG;
		}

		@Override
		public String getTitle() {
			return I18n.translateToLocal("jei.essentialcraft.recipe.windImbue");
		}

		@Override
		public String getUid() {
			return UID;
		}

		@Override
		public void setRecipe(IRecipeLayout arg0, WindImbue.Wrapper arg1, IIngredients arg2) {
			arg0.getItemStacks().init(0, true, 2, 0);
			arg0.getItemStacks().init(1, true, 20, 18);
			arg0.getItemStacks().init(2, false, 38, 0);
			
			arg0.getItemStacks().set(0, arg1.getInputs().get(0));
			arg0.getItemStacks().set(1, arg1.getInputs().get(1));
			arg0.getItemStacks().set(2, arg1.getOutputs().get(0));
		}
	}
}
