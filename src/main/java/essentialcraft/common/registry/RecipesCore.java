package essentialcraft.common.registry;

import java.util.HashMap;

import com.google.common.collect.Maps;

import DummyCore.Registries.RecipeRegistry;
import DummyCore.Utils.IngredientUtils;
import DummyCore.Utils.MiscUtils;
import essentialcraft.api.DemonTrade;
import essentialcraft.api.MagicianTableRecipes;
import essentialcraft.api.MithrilineFurnaceRecipes;
import essentialcraft.api.RadiatingChamberRecipes;
import essentialcraft.api.WindImbueRecipe;
import essentialcraft.common.block.BlocksCore;
import essentialcraft.common.item.ItemsCore;
import essentialcraft.utils.common.RecipeArmorDyes;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public class RecipesCore {
	public boolean hasGregTech = false;
	private Class<?> GT_Class;
	public static HashMap<Block,ItemStack> fancyBlockRecipes = Maps.<Block,ItemStack>newHashMap();

	public static void main() {
		registerFancyBlockCrafts();
		ForgeRegistries.RECIPES.register(new RecipeArmorDyes().setRegistryName("essentialcraft:armor_dyed"));
		registerMagicianTable();
		registerRadiatingChamber();
		registerMithrilineFurnace();
		registerWindRecipes();
		registerDemonTrades();
	}

	private static void registerRadiatingChamber() {
		//RadiatingChamberRecipes
		RadiatingChamberRecipes.addRecipe(new Object[] {new ItemStack(Blocks.STONE), "ingotIron"}, new ItemStack(BlocksCore.fortifiedStone, 4, 0), 10, Float.MAX_VALUE, -Float.MAX_VALUE);
		RadiatingChamberRecipes.addRecipe(new Object[] {"blockGlass", "ingotIron"}, new ItemStack(BlocksCore.fortifiedGlass, 4, 0), 10, Float.MAX_VALUE, -Float.MAX_VALUE);
		RadiatingChamberRecipes.addRecipe(new Object[] {"dustRedstone", new ItemStack(Items.BLAZE_POWDER)}, new ItemStack(ItemsCore.genericItem, 1, 3), 100, Float.MAX_VALUE, -Float.MAX_VALUE);
		RadiatingChamberRecipes.addRecipe(new Object[] {new ItemStack(ItemsCore.soulStone, 1, 0)}, new ItemStack(ItemsCore.matrixProj, 1, 0), 1000, Float.MAX_VALUE, -Float.MAX_VALUE);
		RadiatingChamberRecipes.addRecipe(new Object[] {new ItemStack(ItemsCore.matrixProj, 1, 0)}, new ItemStack(ItemsCore.matrixProj, 1, 1), 10000, Float.MAX_VALUE, 1.5F);
		RadiatingChamberRecipes.addRecipe(new Object[] {new ItemStack(ItemsCore.matrixProj, 1, 0)}, new ItemStack(ItemsCore.matrixProj, 1, 2), 10000, 0.5F, -Float.MAX_VALUE);
		RadiatingChamberRecipes.addRecipe(new Object[] {new ItemStack(ItemsCore.matrixProj, 1, 0)}, new ItemStack(ItemsCore.matrixProj, 1, 3), 20000, 1.5F, 0.5F);
		RadiatingChamberRecipes.addRecipe(new Object[] {"gemLapis", "dustGlowstone"}, new ItemStack(ItemsCore.genericItem, 1, 38), 250, Float.MAX_VALUE, -Float.MAX_VALUE, 20F);
		RadiatingChamberRecipes.addRecipe(new Object[] {"blockLapis", "ingotGold"}, new ItemStack(ItemsCore.genericItem, 4, 39), 100, Float.MAX_VALUE, -Float.MAX_VALUE, 80F);
		RadiatingChamberRecipes.addRecipe(new Object[] {new ItemStack(BlocksCore.blockPale, 1, 0),"gemDiamond"}, new ItemStack(ItemsCore.genericItem,4,40), 250, Float.MAX_VALUE, -Float.MAX_VALUE, 120F);
		RadiatingChamberRecipes.addRecipe(new Object[] {new ItemStack(BlocksCore.blockPale, 1, 0),"gemEmerald"}, new ItemStack(ItemsCore.genericItem,4,40), 300, Float.MAX_VALUE, -Float.MAX_VALUE, 100F);
		RadiatingChamberRecipes.addRecipe(new Object[] {new ItemStack(Blocks.STONE), "gemElemental"}, new ItemStack(ItemsCore.genericItem,1,42), 100, Float.MAX_VALUE, -Float.MAX_VALUE, 10F);
		RadiatingChamberRecipes.addRecipe(new Object[] {"ingotIron", new ItemStack(ItemsCore.genericItem, 1, 3)}, new ItemStack(ItemsCore.genericItem,1,43), 1000, Float.MAX_VALUE, -Float.MAX_VALUE, 1F);
		RadiatingChamberRecipes.addRecipe(new Object[] {"gemDiamond", "gemEmerald"}, new ItemStack(ItemsCore.genericItem, 1, 44), 100, Float.MAX_VALUE, -Float.MAX_VALUE, 50F);
	}

	private static void registerMagicianTable() {
		//MagicianTableRecipes
		MagicianTableRecipes.addRecipe(new Object[] {"plateFortified",new ItemStack(ItemsCore.genericItem,1,79),"plateEnder","plateEnder",new ItemStack(ItemsCore.genericItem,1,79)},new ItemStack(ItemsCore.genericItem,1,0), 10000);
		MagicianTableRecipes.addRecipe(new Object[] {"plateFortified","plateEnder",new ItemStack(ItemsCore.genericItem,1,79),new ItemStack(ItemsCore.genericItem,1,79),"plateEnder"},new ItemStack(ItemsCore.genericItem,1,0), 10000);
		MagicianTableRecipes.addRecipe(new Object[] {"essentialcraft:gemEnderPearl","ingotGoldMagical","ingotGoldMagical","ingotGoldMagical","ingotGoldMagical"},new ItemStack(ItemsCore.genericItem,1,4), 5000);
		MagicianTableRecipes.addRecipe(new Object[] {"ingotIron"},new ItemStack(ItemsCore.genericItem,1,5), 50);
		MagicianTableRecipes.addRecipe(new Object[] {IngredientUtils.getIngredientNBT(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER))},new ItemStack(ItemsCore.genericItem,1,6), 250);
		MagicianTableRecipes.addRecipe(new Object[] {"stoneFortified"},new ItemStack(ItemsCore.genericItem,1,7), 10);
		MagicianTableRecipes.addRecipe(new Object[] {"plateFortified","essentialcraft:gemEnderPearl","essentialcraft:gemEnderPearl","essentialcraft:gemEnderPearl","essentialcraft:gemEnderPearl"},new ItemStack(ItemsCore.genericItem,1,8), 1000);
		MagicianTableRecipes.addRecipe(new Object[] {"plateFortified","blockGlass","blockGlass","blockGlass","blockGlass"},new ItemStack(ItemsCore.genericItem,1,9), 1000);
		MagicianTableRecipes.addRecipe(new Object[] {"ingotGold","nuggetGold","nuggetGold","nuggetGold","nuggetGold"},new ItemStack(ItemsCore.genericItem,1,10), 250);
		MagicianTableRecipes.addRecipe(new Object[] {"plateFortified","dustRedstone","dustRedstone","dustRedstone","dustRedstone"},new ItemStack(ItemsCore.genericItem,1,11), 1000);
		MagicianTableRecipes.addRecipe(new Object[] {"gemQuartz"},new ItemStack(ItemsCore.genericItem,1,12), 10);
		MagicianTableRecipes.addRecipe(new Object[] {new ItemStack(ItemsCore.genericItem,1,12)},new ItemStack(ItemsCore.genericItem,1,13), 100);
		MagicianTableRecipes.addRecipe(new Object[] {new ItemStack(ItemsCore.genericItem,1,13)},new ItemStack(ItemsCore.genericItem,1,14), 200);
		MagicianTableRecipes.addRecipe(new Object[] {new ItemStack(ItemsCore.genericItem,1,14)},new ItemStack(ItemsCore.genericItem,1,15), 500);
		MagicianTableRecipes.addRecipe(new Object[] {new ItemStack(ItemsCore.genericItem,1,15)},new ItemStack(ItemsCore.genericItem,1,16), 1000);
		MagicianTableRecipes.addRecipe(new Object[] {"dustMagic"},new ItemStack(ItemsCore.genericItem,1,20), 3000);
		MagicianTableRecipes.addRecipe(new Object[] {"plateGlass","plateDiamond","plateEmerald","plateEmerald","plateDiamond"},new ItemStack(ItemsCore.genericItem,1,32), 10000);
		MagicianTableRecipes.addRecipe(new Object[] {"plateGlass","plateEmerald","plateDiamond","plateDiamond","plateEmerald"},new ItemStack(ItemsCore.genericItem,1,32), 10000);
		MagicianTableRecipes.addRecipe(new Object[] {"plateFortified","ingotThaumium","ingotThaumium","ingotThaumium","ingotThaumium"},new ItemStack(ItemsCore.genericItem,1,34), 100);
		MagicianTableRecipes.addRecipe(new Object[] {"plateFortified","ingotPale","ingotPale","ingotPale","ingotPale"},new ItemStack(ItemsCore.genericItem,1,41), 100);
		MagicianTableRecipes.addRecipe(new Object[] {"gemQuartz",new ItemStack(ItemsCore.genericItem,1,12),new ItemStack(ItemsCore.genericItem,1,12),new ItemStack(ItemsCore.genericItem,1,12),new ItemStack(ItemsCore.genericItem,1,12)},new ItemStack(ItemsCore.storage,1,0), 100);
		MagicianTableRecipes.addRecipe(new Object[] {"gemEmerald",new ItemStack(ItemsCore.genericItem,1,13),new ItemStack(ItemsCore.genericItem,1,13),new ItemStack(ItemsCore.genericItem,1,13),new ItemStack(ItemsCore.genericItem,1,13)},new ItemStack(ItemsCore.storage,1,1), 500);
		MagicianTableRecipes.addRecipe(new Object[] {"essentialcraft:gemEnderPearl",new ItemStack(ItemsCore.genericItem,1,14),new ItemStack(ItemsCore.genericItem,1,14),new ItemStack(ItemsCore.genericItem,1,14),new ItemStack(ItemsCore.genericItem,1,14)},new ItemStack(ItemsCore.storage,1,2), 100);
		MagicianTableRecipes.addRecipe(new Object[] {"gemDiamond",new ItemStack(ItemsCore.genericItem,1,15),new ItemStack(ItemsCore.genericItem,1,15),new ItemStack(ItemsCore.genericItem,1,15),new ItemStack(ItemsCore.genericItem,1,15)},new ItemStack(ItemsCore.storage,1,3), 250);
		MagicianTableRecipes.addRecipe(new Object[] {"gemNetherStar",new ItemStack(ItemsCore.genericItem,1,16),new ItemStack(ItemsCore.genericItem,1,16),new ItemStack(ItemsCore.genericItem,1,16),new ItemStack(ItemsCore.genericItem,1,16)},new ItemStack(ItemsCore.storage,1,4), 500);
		MagicianTableRecipes.addRecipe(new Object[] {"plateFortified","ingotMithrilineMetal","ingotMithrilineMetal","ingotMithrilineMetal","ingotMithrilineMetal"},new ItemStack(ItemsCore.genericItem,1,49), 400);
		MagicianTableRecipes.addRecipe(new Object[] {"plateFortified","ingotDemonic","ingotDemonic","ingotDemonic","ingotDemonic"},new ItemStack(ItemsCore.genericItem,4,54), 2000);
		MagicianTableRecipes.addRecipe(new Object[] {"plateFortified",Items.BLAZE_POWDER,Items.BLAZE_POWDER,Items.BLAZE_POWDER,Items.BLAZE_POWDER},new ItemStack(ItemsCore.genericItem,1,79), 500);
		ItemStack book_t1 = new ItemStack(ItemsCore.research_book);
		MiscUtils.getStackTag(book_t1).setInteger("tier", 1);
		ItemStack book_t2 = new ItemStack(ItemsCore.research_book);
		MiscUtils.getStackTag(book_t2).setInteger("tier", 2);
		MagicianTableRecipes.addRecipe(new Object[] {IngredientUtils.getIngredientNBT(book_t1)},book_t2, 100);
		MagicianTableRecipes.addRecipe(new Object[] {"stoneVoid"},new ItemStack(ItemsCore.genericItem,1,35), 1000);
	}

	private static void registerMithrilineFurnace() {
		//MithrilineFurnaceRecipes
		MithrilineFurnaceRecipes.addRecipe("dustMithriline", new ItemStack(ItemsCore.genericItem,1,50), 60,1);
		MithrilineFurnaceRecipes.addRecipe("gemResonant", new ItemStack(ItemsCore.genericItem,1,48), 120,1);
		MithrilineFurnaceRecipes.addRecipe("gemMithrilineCrystal", new ItemStack(ItemsCore.genericItem,1,47), 240,1);
		MithrilineFurnaceRecipes.addRecipe("gemFading", new ItemStack(ItemsCore.genericItem,8,46), 480,1);
		MithrilineFurnaceRecipes.addRecipe("dustGlowstone", new ItemStack(ItemsCore.genericItem,1,51), 32,1);
		MithrilineFurnaceRecipes.addRecipe("ingotIron", new ItemStack(Items.GOLD_INGOT), 64,8);
		MithrilineFurnaceRecipes.addRecipe("ingotGold", new ItemStack(Items.IRON_INGOT,8,0), 64,1);
		MithrilineFurnaceRecipes.addRecipe("gemDiamond", new ItemStack(Items.EMERALD), 512,2);
		MithrilineFurnaceRecipes.addRecipe("gemEmerald", new ItemStack(Items.DIAMOND,2,0), 512,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.PLANKS,1,0), new ItemStack(Blocks.PLANKS,1,1), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.PLANKS,1,1), new ItemStack(Blocks.PLANKS,1,2), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.PLANKS,1,2), new ItemStack(Blocks.PLANKS,1,3), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.PLANKS,1,3), new ItemStack(Blocks.PLANKS,1,4), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.PLANKS,1,4), new ItemStack(Blocks.PLANKS,1,5), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.PLANKS,1,5), new ItemStack(Blocks.PLANKS,1,0), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.SAPLING,1,0), new ItemStack(Blocks.SAPLING,1,1), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.SAPLING,1,1), new ItemStack(Blocks.SAPLING,1,2), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.SAPLING,1,2), new ItemStack(Blocks.SAPLING,1,3), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.SAPLING,1,3), new ItemStack(Blocks.SAPLING,1,4), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.SAPLING,1,4), new ItemStack(Blocks.SAPLING,1,5), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.SAPLING,1,5), new ItemStack(Blocks.SAPLING,1,0), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.LOG,1,0), new ItemStack(Blocks.LOG,1,1), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.LOG,1,1), new ItemStack(Blocks.LOG,1,2), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.LOG,1,2), new ItemStack(Blocks.LOG,1,3), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.LOG,1,3), new ItemStack(Blocks.LOG2,1,0), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.LOG2,1,0), new ItemStack(Blocks.LOG2,1,1), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.LOG2,1,1), new ItemStack(Blocks.LOG,1,0), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.LEAVES,1,0), new ItemStack(Blocks.LEAVES,1,1), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.LEAVES,1,1), new ItemStack(Blocks.LEAVES,1,2), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.LEAVES,1,2), new ItemStack(Blocks.LEAVES,1,3), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.LEAVES,1,3), new ItemStack(Blocks.LEAVES2,1,0), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.LEAVES2,1,0), new ItemStack(Blocks.LEAVES2,1,1), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.LEAVES2,1,1), new ItemStack(Blocks.LEAVES,1,0), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.MELON_BLOCK,1,0), new ItemStack(Blocks.PUMPKIN,1,0), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.PUMPKIN,1,0), new ItemStack(Blocks.MELON_BLOCK,1,0), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.YELLOW_FLOWER,1,0), new ItemStack(Blocks.RED_FLOWER,1,0), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.RED_FLOWER,1,0), new ItemStack(Blocks.RED_FLOWER,1,1), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.RED_FLOWER,1,1), new ItemStack(Blocks.RED_FLOWER,1,2), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.RED_FLOWER,1,2), new ItemStack(Blocks.RED_FLOWER,1,3), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.RED_FLOWER,1,3), new ItemStack(Blocks.RED_FLOWER,1,4), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.RED_FLOWER,1,4), new ItemStack(Blocks.RED_FLOWER,1,5), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.RED_FLOWER,1,5), new ItemStack(Blocks.RED_FLOWER,1,6), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.RED_FLOWER,1,6), new ItemStack(Blocks.RED_FLOWER,1,7), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.RED_FLOWER,1,7), new ItemStack(Blocks.RED_FLOWER,1,8), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.RED_FLOWER,1,8), new ItemStack(Blocks.YELLOW_FLOWER,1,0), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.RED_MUSHROOM,1,0), new ItemStack(Blocks.BROWN_MUSHROOM,1,0), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.BROWN_MUSHROOM,1,0), new ItemStack(Blocks.RED_MUSHROOM,1,0), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.ENDER_PEARL,1,0), new ItemStack(Items.BLAZE_ROD,2,0), 128,3);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.BLAZE_ROD,1,0), new ItemStack(Items.ENDER_PEARL,3,0), 128,2);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.REDSTONE,1,0), new ItemStack(Items.GHAST_TEAR,1,0), 1024,64);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.GHAST_TEAR,1,0), new ItemStack(Items.REDSTONE,64,0), 1024,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.CLAY_BALL,1,0), new ItemStack(Items.GUNPOWDER,1,0), 32,12);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.GUNPOWDER,1,0), new ItemStack(Items.CLAY_BALL,12,0), 32,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.PORKCHOP,1,0), new ItemStack(Items.BEEF,1,0), 16,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.BEEF,1,0), new ItemStack(Items.CHICKEN,1,0), 16,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.CHICKEN,1,0), new ItemStack(Items.FISH,1,0), 16,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.FISH,1,0), new ItemStack(Items.ROTTEN_FLESH,2,0), 16,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.ROTTEN_FLESH,1,0), new ItemStack(Items.RABBIT,1,0), 16,2);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.RABBIT,1,0), new ItemStack(Items.MUTTON,1,0), 16,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.MUTTON,1,0), new ItemStack(Items.PORKCHOP,1,0), 16,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.COOKED_PORKCHOP,1,0), new ItemStack(Items.COOKED_BEEF,1,0), 16,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.COOKED_BEEF,1,0), new ItemStack(Items.COOKED_CHICKEN,1,0), 16,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.COOKED_CHICKEN,1,0), new ItemStack(Items.COOKED_FISH,1,0), 16,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.COOKED_FISH,1,0), new ItemStack(Items.COOKED_FISH,1,1), 16,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.COOKED_FISH,1,1), new ItemStack(Items.COOKED_RABBIT,1,0), 16,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.COOKED_RABBIT,1,0), new ItemStack(Items.COOKED_MUTTON,1,0), 16,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.COOKED_MUTTON,1,0), new ItemStack(Items.COOKED_PORKCHOP,1,0), 16,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.RECORD_13,1,0), new ItemStack(Items.RECORD_CAT,1,0), 256,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.RECORD_CAT,1,0), new ItemStack(Items.RECORD_BLOCKS,1,0), 256,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.RECORD_BLOCKS,1,0), new ItemStack(Items.RECORD_CHIRP,1,0), 256,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.RECORD_CHIRP,1,0), new ItemStack(Items.RECORD_FAR,1,0), 256,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.RECORD_FAR,1,0), new ItemStack(Items.RECORD_MALL,1,0), 256,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.RECORD_MALL,1,0), new ItemStack(Items.RECORD_MELLOHI,1,0), 256,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.RECORD_MELLOHI,1,0), new ItemStack(Items.RECORD_STAL,1,0), 256,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.RECORD_STAL,1,0), new ItemStack(Items.RECORD_STRAD,1,0), 256,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.RECORD_STRAD,1,0), new ItemStack(Items.RECORD_WARD,1,0), 256,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.RECORD_WARD,1,0), new ItemStack(Items.RECORD_11,1,0), 256,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.RECORD_11,1,0), new ItemStack(Items.RECORD_WAIT,1,0), 256,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.RECORD_WAIT,1,0), new ItemStack(Items.RECORD_13,1,0), 256,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.SUGAR,1,0), new ItemStack(Items.SLIME_BALL,4,0), 16,3);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.SLIME_BALL,1,0), new ItemStack(Items.SUGAR,3,0), 16,4);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.EGG,1,0), new ItemStack(Items.BONE,2,0), 48,9);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.BONE,1,0), new ItemStack(Items.EGG,9,0), 48,2);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.SKULL,1,0), new ItemStack(Items.SKULL,1,1), 64,3);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.SKULL,1,1), new ItemStack(Items.SKULL,3,2), 64,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.SKULL,1,2), new ItemStack(Items.SKULL,1,3), 64,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.SKULL,1,3), new ItemStack(Items.SKULL,1,4), 64,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.SKULL,1,4), new ItemStack(Items.SKULL,1,0), 64,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.WHEAT,1,0), new ItemStack(Items.LEATHER,1,0), 128,3);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.LEATHER,1,0), new ItemStack(Items.WHEAT,3,0), 128,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.FISH,1,1), new ItemStack(Items.FISH,1,2), 24,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.FISH,1,2), new ItemStack(Items.FISH,1,3), 24,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.FISH,1,3), new ItemStack(Items.FISH,1,1), 24,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.REEDS,1,0), new ItemStack(Items.FEATHER,2,0), 64,3);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.FEATHER,1,0), new ItemStack(Items.REEDS,3,0), 64,2);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.COAL,1,0), new ItemStack(Items.COAL,1,1), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Items.COAL,1,1), new ItemStack(Items.COAL,1,0), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.ICE,1,0), new ItemStack(Blocks.GRASS,1,0), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.GRASS,1,0), new ItemStack(Blocks.DIRT,1,0), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.DIRT,1,0), new ItemStack(Blocks.DIRT,1,2), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.DIRT,1,2), new ItemStack(Blocks.GLASS,1,0), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.GLASS,1,0), new ItemStack(Blocks.ICE,1,0), 1,1);
	}

	private static void registerWindRecipes() {
		//wind recipes
		new WindImbueRecipe(new ItemStack(ItemsCore.soulStone,1,0),new ItemStack(ItemsCore.soulStone,1,0),40000);
		new WindImbueRecipe(new ItemStack(Items.DIAMOND,1,0),new ItemStack(ItemsCore.genericItem,1,55),10000);
		new WindImbueRecipe(IngredientUtils.getIngredientNBT(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER)),new ItemStack(ItemsCore.air_potion,1,0),250);
	}

	private static void registerDemonTrades() {
		//demon trades
		new DemonTrade(new ResourceLocation("minecraft:villager"));
		new DemonTrade(new ResourceLocation("minecraft:enderman"));
		new DemonTrade(new ResourceLocation("essentialcraft:entitywindmage"));
		new DemonTrade(new ItemStack(Blocks.DIAMOND_BLOCK, 2, 0));
		new DemonTrade(new ItemStack(Blocks.BEDROCK, 1, 0));
		new DemonTrade(new ItemStack(Blocks.IRON_BLOCK, 8, 0));
		new DemonTrade(new ItemStack(Blocks.TNT, 64, 0));
		new DemonTrade(new ItemStack(Blocks.ENCHANTING_TABLE, 16, 0));
		new DemonTrade(new ItemStack(Blocks.BOOKSHELF, 64, 0));
		new DemonTrade(new ItemStack(Blocks.END_PORTAL_FRAME, 3, 0));
		new DemonTrade(new ItemStack(Blocks.DRAGON_EGG, 1, 0));
		new DemonTrade(new ItemStack(Blocks.BEACON, 1, 0));
		new DemonTrade(new ItemStack(Blocks.COMMAND_BLOCK, 1, 0));
		new DemonTrade(new ItemStack(Blocks.CHAIN_COMMAND_BLOCK, 1, 0));
		new DemonTrade(new ItemStack(Blocks.REPEATING_COMMAND_BLOCK, 1, 0));
		new DemonTrade(new ItemStack(Items.GOLDEN_APPLE, 1, 1));
		new DemonTrade(new ItemStack(Items.FILLED_MAP, 1, OreDictionary.WILDCARD_VALUE));
		new DemonTrade(new ItemStack(Items.ENDER_EYE, 32, 0));
		new DemonTrade(new ItemStack(Items.SKULL, 1, 3));
		new DemonTrade(new ItemStack(Items.NETHER_STAR, 1, 0));
		new DemonTrade(new ItemStack(Items.RECORD_11, 1, 0));
		new DemonTrade(new ItemStack(BlocksCore.magicalEnchanter, 1, 0));
		new DemonTrade(new ItemStack(BlocksCore.magicalRepairer, 1, 0));
		new DemonTrade(new ItemStack(BlocksCore.heatGenerator, 3, 0));
		new DemonTrade(new ItemStack(BlocksCore.magicalTeleporter, 2, 0));
		new DemonTrade(new ItemStack(BlocksCore.mruCoil, 1, 0));
		new DemonTrade(new ItemStack(BlocksCore.ultraHeatGen, 1, 0));
		new DemonTrade(new ItemStack(BlocksCore.darknessObelisk, 1, 0));
		new DemonTrade(new ItemStack(BlocksCore.mithrilineCrystal, 12, 0));
		new DemonTrade(new ItemStack(BlocksCore.mithrilineCrystal, 6, 3));
		new DemonTrade(new ItemStack(BlocksCore.mithrilineCrystal, 3, 6));
		new DemonTrade(new ItemStack(BlocksCore.compressed, 32, 4));
		new DemonTrade(new ItemStack(ItemsCore.emeraldHeart, 1, 0));
		new DemonTrade(new ItemStack(ItemsCore.spikyShield, 1, 0));
		new DemonTrade(new ItemStack(ItemsCore.magicalShield, 1, 0));
		new DemonTrade(new ItemStack(ItemsCore.magicWaterBottle, 1, 0));
		new DemonTrade(new ItemStack(ItemsCore.wind_elemental_pick, 1, 0));
		new DemonTrade(new ItemStack(ItemsCore.wind_elemental_axe, 1, 0));
		new DemonTrade(new ItemStack(ItemsCore.wind_elemental_hoe, 1, 0));
		new DemonTrade(new ItemStack(ItemsCore.wind_elemental_shovel, 1, 0));
		new DemonTrade(new ItemStack(ItemsCore.wind_elemental_sword, 1, 0));
		new DemonTrade(new ItemStack(ItemsCore.wind_chestplate, 1, 0));
		new DemonTrade(new ItemStack(ItemsCore.wind_helmet, 1, 0));
		new DemonTrade(new ItemStack(ItemsCore.wind_boots, 1, 0));
		new DemonTrade(new ItemStack(ItemsCore.wind_leggings, 1, 0));
		new DemonTrade(new ItemStack(ItemsCore.magicalPorkchop, 1, 0));
		new DemonTrade(new ItemStack(ItemsCore.chaosFork, 1, 0));
		new DemonTrade(new ItemStack(ItemsCore.research_book, 1, 0));
		new DemonTrade(new ItemStack(ItemsCore.record_everlastingSummer, 1, 0));
		new DemonTrade(new ItemStack(ItemsCore.pistol, 1, OreDictionary.WILDCARD_VALUE));
		new DemonTrade(new ItemStack(ItemsCore.rifle, 1, OreDictionary.WILDCARD_VALUE));
		new DemonTrade(new ItemStack(ItemsCore.sniper, 1, OreDictionary.WILDCARD_VALUE));
		new DemonTrade(new ItemStack(ItemsCore.gatling, 1, OreDictionary.WILDCARD_VALUE));
	}

	//Keeping this here because it's dynamic
	public static void registerFancyBlockCrafts() {
		for(Block fancy : BlocksCore.fancyBlocks) {
			ItemStack createdFrom = fancyBlockRecipes.get(fancy);

			RecipeRegistry.addShapelessOreRecipe(new ItemStack(fancy, 4, 1), new Object[] {
					createdFrom, new ItemStack(ItemsCore.magicalChisel, 1, OreDictionary.WILDCARD_VALUE)
			});

			RecipeRegistry.addShapedOreRecipe(new ItemStack(fancy, 4, 2), new Object[] {
					"##",
					"##",
					'#', new ItemStack(fancy, 1, 1)
			});
			RecipeRegistry.addShapedOreRecipe(new ItemStack(fancy, 4, 7), new Object[] {
					" # ",
					"# #",
					" # ",
					'#', new ItemStack(fancy, 1, 2)
			});
			RecipeRegistry.addShapedOreRecipe(new ItemStack(fancy, 4, 0), new Object[] {
					"# #",
					"   ",
					"# #",
					'#', new ItemStack(fancy, 1, 1)
			});
			RecipeRegistry.addShapedOreRecipe(new ItemStack(fancy, 9, 3), new Object[] {
					"###",
					"###",
					"###",
					'#', new ItemStack(fancy, 1, 1)
			});
			RecipeRegistry.addShapedOreRecipe(new ItemStack(fancy, 8, 4), new Object[] {
					"###",
					"# #",
					"###",
					'#', new ItemStack(fancy, 1, 2)
			});
			RecipeRegistry.addShapedOreRecipe(new ItemStack(fancy, 4, 5), new Object[] {
					"##",
					"##",
					'#', new ItemStack(fancy, 1, 7)
			});
			RecipeRegistry.addShapedOreRecipe(new ItemStack(fancy, 9, 6), new Object[] {
					"###",
					"@$@",
					"###",
					'#', new ItemStack(fancy, 1, 1),
					'@', new ItemStack(fancy, 1, 3),
					'$', new ItemStack(fancy, 1, 0)
			});
			RecipeRegistry.addShapedOreRecipe(new ItemStack(fancy, 4, 8), new Object[] {
					" # ",
					"#@#",
					" # ",
					'#', new ItemStack(fancy, 1, 0),
					'@', new ItemStack(fancy, 1, 3)
			});
			RecipeRegistry.addShapedOreRecipe(new ItemStack(fancy, 8, 9), new Object[] {
					"###",
					"#@#",
					"###",
					'#', new ItemStack(fancy, 1, 8),
					'@', "dustRedstone"
			});
			RecipeRegistry.addShapedOreRecipe(new ItemStack(fancy, 8, 10), new Object[] {
					"###",
					"#@#",
					"###",
					'#', new ItemStack(fancy, 1, 1),
					'@', new ItemStack(ItemsCore.genericItem, 1, 3)
			});
			RecipeRegistry.addShapedOreRecipe(new ItemStack(fancy, 8, 11), new Object[] {
					"###",
					"#@#",
					"###",
					'#', new ItemStack(fancy, 1, 1),
					'@', new ItemStack(ItemsCore.genericItem, 1, 12)
			});
			RecipeRegistry.addShapedOreRecipe(new ItemStack(fancy, 8, 12), new Object[] {
					"###",
					"#@#",
					"###",
					'#', new ItemStack(fancy, 1, 1),
					'@', "ingotIron"
			});
			RecipeRegistry.addShapedOreRecipe(new ItemStack(fancy, 8, 13), new Object[] {
					"###",
					"#@#",
					"###",
					'#', new ItemStack(fancy, 1, 1),
					'@', "leather"
			});
			RecipeRegistry.addShapedOreRecipe(new ItemStack(fancy, 4, 14), new Object[] {
					"##",
					"##",
					'#', new ItemStack(fancy, 1, 13)
			});
			RecipeRegistry.addShapedOreRecipe(new ItemStack(fancy, 4, 15), new Object[] {
					"##",
					"##",
					'#', new ItemStack(fancy, 1, 12)
			});
		}
	}
}

