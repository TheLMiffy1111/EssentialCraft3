package essentialcraft.integration.jei;

import java.util.Arrays;
import java.util.Collections;

import essentialcraft.api.WindImbueRecipe;
import essentialcraft.common.block.BlocksCore;
import essentialcraft.common.item.ItemSoulStone;
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

public class WindImbue {

	public static final String UID = "essentialcraft:windImbue";

	public static class Wrapper implements IRecipeWrapper {

		private WindImbueRecipe rec;

		public Wrapper(WindImbueRecipe rec) {
			this.rec = rec;
		}

		@Override
		public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
			minecraft.fontRenderer.drawString(MathHelper.floor(rec.enderEnergy)+" ESPE", 0, 36, 0x000000, false);
			if(rec.result.getItem() instanceof ItemSoulStone)
				minecraft.fontRenderer.drawString("+Wind Relations", 0, 46, 0x81d17d, true);
		}

		@Override
		public void getIngredients(IIngredients paramIIngredients) {
			paramIIngredients.setInputLists(ItemStack.class, Arrays.asList(Arrays.asList(rec.input.getMatchingStacks()), Collections.singletonList(new ItemStack(BlocksCore.windRune))));
			paramIIngredients.setOutput(ItemStack.class, rec.result);
		}
	}

	public static class Category implements IRecipeCategory<WindImbue.Wrapper> {

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

			arg0.getItemStacks().set(0, arg2.getInputs(ItemStack.class).get(0));
			arg0.getItemStacks().set(1, arg2.getInputs(ItemStack.class).get(1));
			arg0.getItemStacks().set(2, arg2.getOutputs(ItemStack.class).get(0));
		}

		@Override
		public String getModName() {
			return "essentialcraft";
		}
	}
}
