package ec3.integration.jei;

import javax.annotation.Nonnull;

import ec3.client.gui.GuiMagicianTable;
import ec3.client.gui.GuiMithrilineFurnace;
import ec3.client.gui.GuiRadiatingChamber;
import ec3.common.block.BlocksCore;
import ec3.common.item.ItemsCore;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

@JEIPlugin
public class EssentialCraftPlugin extends BlankModPlugin {
	
	public void register(IModRegistry registry) {
		
		registry.addRecipeCategories(new IRecipeCategory[] {
				new DemonTrading.Category(registry.getJeiHelpers().getGuiHelper()),
				new MagicianTable.Category(registry.getJeiHelpers().getGuiHelper()),
				new MithrilineFurnace.Category(registry.getJeiHelpers().getGuiHelper()),
				new RadiatingChamber.Category(registry.getJeiHelpers().getGuiHelper()),
				new WindImbue.Category(registry.getJeiHelpers().getGuiHelper()),
		});
		registry.addRecipeHandlers(new IRecipeHandler[] {
				new DemonTrading.Handler(),
				new MagicianTable.Handler(),
				new MithrilineFurnace.Handler(),
				new RadiatingChamber.Handler(),
				new WindImbue.Handler(),
		});
		
		registry.addRecipes(DemonTrading.getRecipes());
		registry.addRecipes(MagicianTable.getRecipes());
		registry.addRecipes(MithrilineFurnace.getRecipes());
		registry.addRecipes(RadiatingChamber.getRecipes());
		registry.addRecipes(WindImbue.getRecipes());
		
		registry.addRecipeCategoryCraftingItem(new ItemStack(BlocksCore.demonicPentacle), DemonTrading.UID);
		registry.addRecipeCategoryCraftingItem(new ItemStack(BlocksCore.magicianTable), MagicianTable.UID);
		registry.addRecipeCategoryCraftingItem(new ItemStack(BlocksCore.mithrilineFurnace), MithrilineFurnace.UID);
		registry.addRecipeCategoryCraftingItem(new ItemStack(BlocksCore.radiatingChamber), RadiatingChamber.UID);
		registry.addRecipeCategoryCraftingItem(new ItemStack(BlocksCore.windRune), WindImbue.UID);
		
		registry.addRecipeClickArea(GuiMagicianTable.class, 121, 4, 18, 18, MagicianTable.UID);
		registry.addRecipeClickArea(GuiMithrilineFurnace.class, 79, 44, 18, 18, MithrilineFurnace.UID);
		registry.addRecipeClickArea(GuiRadiatingChamber.class, 106, 22, 18, 18, RadiatingChamber.UID);
		
		registry.getJeiHelpers().getItemBlacklist().addItemToBlacklist(new ItemStack(ItemsCore.secret, 1, OreDictionary.WILDCARD_VALUE));
		registry.getJeiHelpers().getItemBlacklist().addItemToBlacklist(new ItemStack(BlocksCore.air));
		registry.getJeiHelpers().getItemBlacklist().addItemToBlacklist(new ItemStack(BlocksCore.water));
		registry.getJeiHelpers().getItemBlacklist().addItemToBlacklist(new ItemStack(BlocksCore.lava));
		registry.getJeiHelpers().getItemBlacklist().addItemToBlacklist(new ItemStack(BlocksCore.fire));
	}
}
