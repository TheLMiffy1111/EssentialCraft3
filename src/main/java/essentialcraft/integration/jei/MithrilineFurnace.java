package essentialcraft.integration.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import essentialcraft.api.MithrilineFurnaceRecipe;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;

public class MithrilineFurnace {

	public static final String UID = "essentialcraft:mithrilineFurnace";

	public static class Wrapper implements IRecipeWrapper {

		private MithrilineFurnaceRecipe rec;

		public Wrapper(MithrilineFurnaceRecipe rec) {
			this.rec = rec;
		}

		@Override
		public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
			minecraft.fontRenderer.drawString(MathHelper.floor(rec.energy)+" ESPE", 2, 19, 0x000000, false);
		}

		@Override
		public void getIngredients(IIngredients arg0) {
			ArrayList<ItemStack> get = Lists.<ItemStack>newArrayList(rec.input.getMatchingStacks());
			ArrayList<ItemStack> ret = Lists.<ItemStack>newArrayList();
			for(ItemStack stk : get) {
				ret.add(stk.copy());
			}
			for(ItemStack stk : ret) {
				stk.setCount(rec.stackSize);
			}
			arg0.setInputLists(ItemStack.class, Collections.<List<ItemStack>>singletonList(ret));
			arg0.setOutput(ItemStack.class, rec.result.copy());
		}
	}

	public static class Category implements IRecipeCategory<MithrilineFurnace.Wrapper> {

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

			arg0.getItemStacks().set(0, arg2.getInputs(ItemStack.class).get(0));
			arg0.getItemStacks().set(1, arg2.getOutputs(ItemStack.class).get(0));
		}

		@Override
		public String getModName() {
			return "essentialcraft";
		}
	}
}
