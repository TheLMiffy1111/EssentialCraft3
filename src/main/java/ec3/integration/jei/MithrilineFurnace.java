package ec3.integration.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import ec3.api.MithrilineFurnaceRecipe;
import ec3.api.MithrilineFurnaceRecipes;
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

public class MithrilineFurnace {

	public static final String UID = "essentialcraft:mithrilineFurnace";
	
	public static List<MithrilineFurnace.Wrapper> getRecipes() {
		ArrayList<MithrilineFurnace.Wrapper> ret = Lists.<MithrilineFurnace.Wrapper>newArrayList();
		for(MithrilineFurnaceRecipe rec : MithrilineFurnaceRecipes.allRegisteredRecipes) {
			ret.add(new MithrilineFurnace.Wrapper(rec));
		}
		return ret;
	}
	
	public static class Wrapper extends BlankRecipeWrapper {
		
		private MithrilineFurnaceRecipe rec;
		
		public Wrapper(MithrilineFurnaceRecipe rec) {
			this.rec = rec;
		}
		
		@Override
		public List<List<ItemStack>> getInputs() {
			ArrayList<ItemStack> get = Lists.<ItemStack>newArrayList(rec.smelted.possibleStacks);
			ArrayList<ItemStack> ret = Lists.<ItemStack>newArrayList();
			for(ItemStack stk : get) {
				ret.add(stk.copy());
			}
			for(ItemStack stk : ret) {
				stk.stackSize = rec.requiredRecipeSize;
			}
			return Collections.<List<ItemStack>>singletonList(ret);
		}

		@Override
		public List<ItemStack> getOutputs() {
			return Collections.<ItemStack>singletonList(rec.result);
		}

		@Override
		public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
			minecraft.fontRendererObj.drawString(MathHelper.floor(rec.enderStarPulsesRequired)+" ESPE", 2, 19, 0x000000, false);
		}

		@Override
		public void getIngredients(IIngredients arg0) {}
	}
	
	public static class Handler implements IRecipeHandler<MithrilineFurnace.Wrapper> {

		@Override
		public String getRecipeCategoryUid() {
			return UID;
		}

		@Override
		public String getRecipeCategoryUid(MithrilineFurnace.Wrapper arg0) {
			return UID;
		}

		@Override
		public Class<MithrilineFurnace.Wrapper> getRecipeClass() {
			return MithrilineFurnace.Wrapper.class;
		}

		@Override
		public MithrilineFurnace.Wrapper getRecipeWrapper(MithrilineFurnace.Wrapper arg0) {
			return arg0;
		}

		@Override
		public boolean isRecipeValid(MithrilineFurnace.Wrapper arg0) {
			return !arg0.getInputs().get(0).isEmpty() && !arg0.getOutputs().isEmpty();
		}
	}
	
	public static class Category extends BlankRecipeCategory<MithrilineFurnace.Wrapper> {

		private final IDrawable BG;
		
		public Category(IGuiHelper gh) {
			BG = gh.createDrawable(new ResourceLocation("essentialcraft:textures/gui/jei/mithriline_furnace.png"), 0, 0, 57, 30);
		}
		
		@Override
		public IDrawable getBackground() {
			return BG;
		}

		@Override
		public String getTitle() {
			return I18n.translateToLocal("jei.essentialcraft.recipe.mithrilineFurnace");
		}

		@Override
		public String getUid() {
			return UID;
		}

		@Override
		public void setRecipe(IRecipeLayout arg0, MithrilineFurnace.Wrapper arg1, IIngredients arg2) {
			arg0.getItemStacks().init(0, true, 2, 0);
			arg0.getItemStacks().init(1, false, 38, 0);
			
			arg0.getItemStacks().set(0, arg1.getInputs().get(0));
			arg0.getItemStacks().set(1, arg1.getOutputs().get(0));
		}
	}
}
