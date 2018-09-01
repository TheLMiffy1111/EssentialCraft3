package essentialcraft.integration.jei;

import essentialcraft.api.DemonTrade;
import essentialcraft.api.MagicianTableRecipe;
import essentialcraft.api.MagicianTableRecipes;
import essentialcraft.api.MithrilineFurnaceRecipe;
import essentialcraft.api.MithrilineFurnaceRecipes;
import essentialcraft.api.RadiatingChamberRecipe;
import essentialcraft.api.RadiatingChamberRecipes;
import essentialcraft.api.WindImbueRecipe;
import essentialcraft.client.gui.GuiMagicianTable;
import essentialcraft.client.gui.GuiMithrilineFurnace;
import essentialcraft.client.gui.GuiRadiatingChamber;
import essentialcraft.common.block.BlocksCore;
import essentialcraft.common.item.ItemsCore;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

@JEIPlugin
public class EssentialCraftPlugin implements IModPlugin {

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new IRecipeCategory[] {
				new DemonTrading.Category(registry.getJeiHelpers().getGuiHelper()),
				new MagicianTable.Category(registry.getJeiHelpers().getGuiHelper()),
				new MithrilineFurnace.Category(registry.getJeiHelpers().getGuiHelper()),
				new RadiatingChamber.Category(registry.getJeiHelpers().getGuiHelper()),
				new WindImbue.Category(registry.getJeiHelpers().getGuiHelper()),
		});
	}

	@Override
	public void register(IModRegistry registry) {
		registry.handleRecipes(DemonTrade.class, DemonTrading.Wrapper::new, DemonTrading.UID);
		registry.handleRecipes(MagicianTableRecipe.class, MagicianTable.Wrapper::new, MagicianTable.UID);
		registry.handleRecipes(MithrilineFurnaceRecipe.class, MithrilineFurnace.Wrapper::new, MithrilineFurnace.UID);
		registry.handleRecipes(RadiatingChamberRecipe.class, RadiatingChamber.Wrapper::new, RadiatingChamber.UID);
		registry.handleRecipes(WindImbueRecipe.class, WindImbue.Wrapper::new, WindImbue.UID);

		registry.addRecipes(DemonTrade.TRADES, DemonTrading.UID);
		registry.addRecipes(MagicianTableRecipes.RECIPES, MagicianTable.UID);
		registry.addRecipes(MithrilineFurnaceRecipes.RECIPES, MithrilineFurnace.UID);
		registry.addRecipes(RadiatingChamberRecipes.RECIPES, RadiatingChamber.UID);
		registry.addRecipes(WindImbueRecipe.RECIPES, WindImbue.UID);

		registry.addRecipeCatalyst(new ItemStack(BlocksCore.demonicPentacle), DemonTrading.UID);
		registry.addRecipeCatalyst(new ItemStack(BlocksCore.magicianTable), MagicianTable.UID);
		registry.addRecipeCatalyst(new ItemStack(BlocksCore.mithrilineFurnace), MithrilineFurnace.UID);
		registry.addRecipeCatalyst(new ItemStack(BlocksCore.radiatingChamber), RadiatingChamber.UID);
		registry.addRecipeCatalyst(new ItemStack(BlocksCore.windRune), WindImbue.UID);

		registry.addRecipeClickArea(GuiMagicianTable.class, 121, 4, 18, 18, MagicianTable.UID);
		registry.addRecipeClickArea(GuiMithrilineFurnace.class, 79, 44, 18, 18, MithrilineFurnace.UID);
		registry.addRecipeClickArea(GuiRadiatingChamber.class, 106, 22, 18, 18, RadiatingChamber.UID);

		registry.getJeiHelpers().getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ItemsCore.secret, 1, OreDictionary.WILDCARD_VALUE));
		registry.getJeiHelpers().getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(BlocksCore.air));
		registry.getJeiHelpers().getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(BlocksCore.water));
		registry.getJeiHelpers().getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(BlocksCore.lava));
		registry.getJeiHelpers().getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(BlocksCore.fire));
	}
}
