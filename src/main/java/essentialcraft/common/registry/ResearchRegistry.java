package essentialcraft.common.registry;

import java.util.HashMap;
import java.util.Random;

import DummyCore.Utils.MiscUtils;
import essentialcraft.api.ApiCore;
import essentialcraft.api.CategoryEntry;
import essentialcraft.api.DiscoveryEntry;
import essentialcraft.api.MagicianTableUpgrades;
import essentialcraft.api.PageEntry;
import essentialcraft.api.StructureBlock;
import essentialcraft.api.StructureRecipe;
import essentialcraft.common.block.BlocksCore;
import essentialcraft.common.item.ItemBaublesResistance;
import essentialcraft.common.item.ItemGun;
import essentialcraft.common.item.ItemsCore;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ResearchRegistry {

	public static HashMap<String, Integer> stringIDS = new HashMap<String, Integer>();

	public static void registerBasicCategory()
	{
		ItemStack book = new ItemStack(ItemsCore.research_book);
		MiscUtils.getStackTag(book).setInteger("tier", 0);
		ItemStack book_t1 = new ItemStack(ItemsCore.research_book);
		MiscUtils.getStackTag(book_t1).setInteger("tier", 1);
		basic
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.book")
				.setReferal(book)
				.setDisplayStack(new ItemStack(ItemsCore.research_book,1,0))
				.apendPage(
						new PageEntry("essentialcraft.page.book_0")
						.setText(I18n.translateToLocal("essentialcraft.page.book_0.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.book_1")
						.setRecipe(ECUtils.findRecipeByIS(book,2))
						.setText(I18n.translateToLocal("essentialcraft.page.book_1.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.book_2")
						.setText(I18n.translateToLocal("essentialcraft.page.book_2.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.book_3")
						.setText(I18n.translateToLocal("essentialcraft.page.book_3.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.book_4")
						.setText(I18n.translateToLocal("essentialcraft.page.book_4.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.book_5")
						.setText(I18n.translateToLocal("essentialcraft.page.book_5.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.book_6")
						.setText(I18n.translateToLocal("essentialcraft.page.book_6.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.book_7")
						.setText(I18n.translateToLocal("essentialcraft.page.book_7.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.book_8")
						.setText(I18n.translateToLocal("essentialcraft.page.book_8.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.book_9")
						.setText(I18n.translateToLocal("essentialcraft.page.book_9.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.book_10")
						.setText(I18n.translateToLocal("essentialcraft.page.book_10.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.book_11")
						.setText(I18n.translateToLocal("essentialcraft.page.book_11.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.book_12")
						.setText(I18n.translateToLocal("essentialcraft.page.book_12.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.book_13")
						.setText(I18n.translateToLocal("essentialcraft.page.book_13.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.book_14")
						.setText(I18n.translateToLocal("essentialcraft.page.book_14.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.book_15")
						.setText(I18n.translateToLocal("essentialcraft.page.book_15.txt"))
						.setRecipe(
								new ShapedOreRecipe(null, book_t1, new Object[]{
										"EGE",
										"GBG",
										"EGE",
										'E',"coreMagic",
										'G',"gemElemental",
										'B',book
								}))
						)
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.wind")
				.setReferal(new ItemStack(ItemsCore.bottledWind,1,0),new ItemStack(ItemsCore.imprisonedWind,1,0),new ItemStack(ItemsCore.windKeeper,1,0),new ItemStack(ItemsCore.magicArmorItems[12],1,0),new ItemStack(ItemsCore.magicArmorItems[13],1,0),new ItemStack(ItemsCore.magicArmorItems[14],1,0),new ItemStack(ItemsCore.magicArmorItems[15],1,0),new ItemStack(ItemsCore.air_potion,1,0))
				.setDisplayStack(new ResourceLocation("essentialcraft","textures/special/wind_icon.png")
						)
				.apendPage(
						new PageEntry("essentialcraft.page.wind_0")
						.setText(I18n.translateToLocal("essentialcraft.page.wind_0.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.wind_1")
						.setImg(new ResourceLocation("essentialcraft","textures/special/bookicons/windtouch.png"))
						.setText(I18n.translateToLocal("essentialcraft.page.wind_1.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.wind_2")
						.setImg(new ResourceLocation("essentialcraft","textures/special/bookicons/windmages.png"))
						.setText(I18n.translateToLocal("essentialcraft.page.wind_2.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.wind_3")
						.setDisplayStacks(new ItemStack(ItemsCore.bottledWind,1,0),new ItemStack(ItemsCore.imprisonedWind,1,0),new ItemStack(ItemsCore.windKeeper,1,0))
						.setText(I18n.translateToLocal("essentialcraft.page.wind_3.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.wind_4")
						.setDisplayStacks(new ItemStack(ItemsCore.magicArmorItems[12],1,0),new ItemStack(ItemsCore.magicArmorItems[13],1,0),new ItemStack(ItemsCore.magicArmorItems[14],1,0),new ItemStack(ItemsCore.magicArmorItems[15],1,0))
						.setText(I18n.translateToLocal("essentialcraft.page.wind_4.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.wind_5")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.air_potion,1,0), 3))
						.setText(I18n.translateToLocal("essentialcraft.page.wind_5.txt"))
						)
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.drops")
				.setReferal(new ItemStack(ItemsCore.drops,1,0),new ItemStack(ItemsCore.drops,1,1),new ItemStack(ItemsCore.drops,1,2),new ItemStack(ItemsCore.drops,1,3),new ItemStack(ItemsCore.drops,1,4),new ItemStack(BlocksCore.compressed,1,0),new ItemStack(BlocksCore.compressed,1,1),new ItemStack(BlocksCore.compressed,1,2),new ItemStack(BlocksCore.compressed,1,3),new ItemStack(BlocksCore.compressed,1,4),new ItemStack(ItemsCore.weak_elemental_axe),new ItemStack(ItemsCore.weak_elemental_hoe),new ItemStack(ItemsCore.weak_elemental_pick),new ItemStack(ItemsCore.weak_elemental_shovel),new ItemStack(ItemsCore.weak_elemental_sword))
				.setDisplayStack(new ItemStack(ItemsCore.drops,1,4))

				.apendPage(
						new PageEntry("essentialcraft.page.drops_0")
						.setText(I18n.translateToLocal("essentialcraft.page.drops_0.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.drops_1")
						.setRecipe(new StructureRecipe(new ItemStack(ItemsCore.drops,1,0),
								new StructureBlock(Blocks.NETHERRACK, 0, 0, 0, 0),
								new StructureBlock(BlocksCore.lava, 0, 1, 0, 0),
								new StructureBlock(BlocksCore.lava, 0, -1, 0, 0),
								new StructureBlock(BlocksCore.lava, 0, 0, 0, 1),
								new StructureBlock(BlocksCore.lava, 0, 0, 0, -1)
								))
						.setText(I18n.translateToLocal("essentialcraft.page.drops_1.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.drops_2")
						.setRecipe(new StructureRecipe(new ItemStack(ItemsCore.drops,1,1),
								new StructureBlock(Blocks.ICE, 0, 0, 0, 0),
								new StructureBlock(BlocksCore.water, 0, 1, 0, 0),
								new StructureBlock(BlocksCore.water, 0, -1, 0, 0),
								new StructureBlock(BlocksCore.water, 0, 0, 0, 1),
								new StructureBlock(BlocksCore.water, 0, 0, 0, -1)
								))
						.setText(I18n.translateToLocal("essentialcraft.page.drops_2.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.drops_3")
						.setRecipe(new StructureRecipe(new ItemStack(ItemsCore.drops,1,2),
								new StructureBlock(Blocks.SLIME_BLOCK, 0, 0, 0, 0),
								new StructureBlock(Blocks.GRASS, 0, 1, 0, 0),
								new StructureBlock(Blocks.GRASS, 0, -1, 0, 0),
								new StructureBlock(Blocks.GRASS, 0, 0, 0, 1),
								new StructureBlock(Blocks.GRASS, 0, 0, 0, -1)
								))
						.setText(I18n.translateToLocal("essentialcraft.page.drops_3.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.drops_4")
						.setRecipe(new StructureRecipe(new ItemStack(ItemsCore.drops,1,3),
								new StructureBlock(Blocks.QUARTZ_BLOCK, 0, 0, 0, 0),
								new StructureBlock(Blocks.SAND, 0, 1, 0, 0),
								new StructureBlock(Blocks.SAND, 0, -1, 0, 0),
								new StructureBlock(Blocks.SAND, 0, 0, 0, 1),
								new StructureBlock(Blocks.SAND, 0, 0, 0, -1)
								))
						.setText(I18n.translateToLocal("essentialcraft.page.drops_4.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.drops_11")
						.setText(I18n.translateToLocal("essentialcraft.page.drops_11.txt"))
						.setDisplayStacks(new ItemStack(BlocksCore.oreDrops,1,0),new ItemStack(BlocksCore.oreDrops,1,1),new ItemStack(BlocksCore.oreDrops,1,2),new ItemStack(BlocksCore.oreDrops,1,3),new ItemStack(BlocksCore.oreDrops,1,4))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.drops_12")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.compressed,1,0), 3))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.drops_13")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.compressed,1,1), 3))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.drops_14")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.compressed,1,2), 3))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.drops_15")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.compressed,1,3), 3))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.drops_16")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.compressed,1,4), 3))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.drops_5")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.drops,1,4), 3))
						.setText(I18n.translateToLocal("essentialcraft.page.drops_5.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.drops_6")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.weak_elemental_axe,1,0), 2))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.drops_7")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.weak_elemental_hoe,1,0), 2))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.drops_8")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.weak_elemental_pick,1,0), 2))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.drops_9")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.weak_elemental_shovel,1,0), 2))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.drops_10")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.weak_elemental_sword,1,0), 3))
						)

				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.miscCrafts")
				.setReferal(new ItemStack(ItemsCore.genericItem,1,1),new ItemStack(ItemsCore.genericItem,1,21),new ItemStack(ItemsCore.genericItem,1,22),new ItemStack(ItemsCore.genericItem,1,23),new ItemStack(ItemsCore.genericItem,1,25),new ItemStack(ItemsCore.genericItem,1,26),new ItemStack(ItemsCore.genericItem,1,28),new ItemStack(ItemsCore.genericItem,1,29))
				.setDisplayStack(new ItemStack(ItemsCore.genericItem,1,1))
				.apendPage(
						new PageEntry("essentialcraft.page.miscCrafts_0")
						.setText(I18n.translateToLocal("essentialcraft.page.miscCrafts_0.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.miscCrafts_1")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.genericItem,1,1), 2))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.miscCrafts_2")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.genericItem,1,21), 2))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.miscCrafts_3")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.genericItem,1,22), 2))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.miscCrafts_4")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.genericItem,1,23), 2))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.miscCrafts_5")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.genericItem,1,25), 2))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.miscCrafts_6")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.genericItem,1,26), 2))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.miscCrafts_7")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.genericItem,1,28), 2))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.miscCrafts_8")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.genericItem,1,29), 2))
						)
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.mru")
				.setReferal(new ItemStack(ItemsCore.mruMover1))
				.setDisplayStack(new ResourceLocation("essentialcraft","textures/special/basical_knowledge_icon.png"))
				.apendPage(
						new PageEntry("essentialcraft.page.mru_0")
						.setText(I18n.translateToLocal("essentialcraft.page.mru_0.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.mru_1")
						.setText(I18n.translateToLocal("essentialcraft.page.mru_1.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.mru_2")
						.setText(I18n.translateToLocal("essentialcraft.page.mru_2.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.mru_3")
						.setImg(new ResourceLocation("essentialcraft","textures/special/bookicons/mru_lowest.png"))
						.setText(I18n.translateToLocal("essentialcraft.page.mru_3.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.mru_4")
						.setText(I18n.translateToLocal("essentialcraft.page.mru_4.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.mru_5")
						.setText(I18n.translateToLocal("essentialcraft.page.mru_5.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.mru_6")
						.setText(I18n.translateToLocal("essentialcraft.page.mru_6.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.mru_7")
						.setText(I18n.translateToLocal("essentialcraft.page.mru_7.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.mru_8")
						.setText(I18n.translateToLocal("essentialcraft.page.mru_8.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.mru_9")
						.setText(I18n.translateToLocal("essentialcraft.page.mru_9.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.mru_10")
						.setText(I18n.translateToLocal("essentialcraft.page.mru_10.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.mru_11")
						.setText(I18n.translateToLocal("essentialcraft.page.mru_11.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.mru_12")
						.setImg(new ResourceLocation("essentialcraft","textures/special/bookicons/mru_pressence.png"))
						.setText(I18n.translateToLocal("essentialcraft.page.mru_12.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.mru_13")
						.setText(I18n.translateToLocal("essentialcraft.page.mru_13.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.mru_14")
						.setText(I18n.translateToLocal("essentialcraft.page.mru_14.txt"))
						.setDisplayStacks(new ItemStack(BlocksCore.lightCorruption[0],1,7),new ItemStack(BlocksCore.lightCorruption[1],1,7),new ItemStack(BlocksCore.lightCorruption[3],1,7))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.mru_15")
						.setText(I18n.translateToLocal("essentialcraft.page.mru_15.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.mru_16")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.mruMover1), 3))
						)
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.soulStone")
				.setReferal(new ItemStack(ItemsCore.soulStone))
				.setDisplayStack(new ItemStack(ItemsCore.soulStone,1,0))
				.apendPage(
						new PageEntry("essentialcraft.page.soulStone_0")
						.setText(I18n.translateToLocal("essentialcraft.page.soulStone_0.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.soulStone_1")
						.setRecipe(new StructureRecipe(new ItemStack(ItemsCore.soulStone,1,0),
								new StructureBlock(Blocks.EMERALD_BLOCK, 0, 0, 0, 0),
								new StructureBlock(Blocks.SOUL_SAND, 0, 1, 0, 0),
								new StructureBlock(Blocks.SOUL_SAND, 0, -1, 0, 0),
								new StructureBlock(Blocks.SOUL_SAND, 0, 0, 0, 1),
								new StructureBlock(Blocks.SOUL_SAND, 0, 0, 0, -1)
								))
						.setText(I18n.translateToLocal("essentialcraft.page.soulStone_1.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.soulStone_2")
						.setImg(new ResourceLocation("essentialcraft","textures/special/bookicons/soulstone.png"))
						.setText(I18n.translateToLocal("essentialcraft.page.soulStone_2.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.soulStone_3")
						.setText(I18n.translateToLocal("essentialcraft.page.soulStone_3.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.soulStone_43")
						.setText(I18n.translateToLocal("essentialcraft.page.soulStone_4.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.soulStone_5")
						.setText(I18n.translateToLocal("essentialcraft.page.soulStone_5.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.soulStone_6")
						.setText(I18n.translateToLocal("essentialcraft.page.soulStone_6.txt"))
						)
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.decorations")
				.setReferal(new ItemStack(ItemsCore.magicalChisel))
				.setDisplayStack(new ItemStack(ItemsCore.magicalChisel))
				.apendPage(next("decorations").setText(I18n.translateToLocal("essentialcraft.page.decorations_0.txt")))
				.apendPage(next("decorations").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.magicalChisel), 2))
						.setDisplayStacks(new ItemStack(ItemsCore.magicalSlag),new ItemStack(BlocksCore.concrete),new ItemStack(BlocksCore.fortifiedStone),new ItemStack(BlocksCore.magicPlating),new ItemStack(BlocksCore.platingPale),new ItemStack(BlocksCore.voidStone),new ItemStack(BlocksCore.coldStone),new ItemStack(BlocksCore.invertedBlock),new ItemStack(BlocksCore.demonicPlating),new ItemStack(BlocksCore.mimic)))
				.apendPage(next("decorations").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.fancyBlocks.get(0),1,1), 2)))
				.apendPage(next("decorations").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.fancyBlocks.get(0),1,0), 2)))
				.apendPage(next("decorations").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.fancyBlocks.get(0),1,2), 2)))
				.apendPage(next("decorations").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.fancyBlocks.get(0),1,3), 2)))
				.apendPage(next("decorations").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.fancyBlocks.get(0),1,4), 2)))
				.apendPage(next("decorations").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.fancyBlocks.get(0),1,5), 2)))
				.apendPage(next("decorations").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.fancyBlocks.get(0),1,6), 2)))
				.apendPage(next("decorations").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.fancyBlocks.get(0),1,7), 2)))
				.apendPage(next("decorations").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.fancyBlocks.get(0),1,8), 2)))
				.apendPage(next("decorations").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.fancyBlocks.get(0),1,9), 2)))
				.apendPage(next("decorations").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.fancyBlocks.get(0),1,10), 2)))
				.apendPage(next("decorations").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.fancyBlocks.get(0),1,11), 2)))
				.apendPage(next("decorations").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.fancyBlocks.get(0),1,12), 2)))
				.apendPage(next("decorations").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.fancyBlocks.get(0),1,13), 2)))
				.apendPage(next("decorations").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.fancyBlocks.get(0),1,14), 2)))
				.apendPage(next("decorations").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.fancyBlocks.get(0),1,15), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.crystalLamp")
				.setReferal(new ItemStack(BlocksCore.crystalLamp,1,0),new ItemStack(BlocksCore.crystalLamp,1,1),new ItemStack(BlocksCore.crystalLamp,1,2),new ItemStack(BlocksCore.crystalLamp,1,3),new ItemStack(BlocksCore.crystalLamp,1,4),new ItemStack(BlocksCore.crystalLamp,1,5))
				.setDisplayStack(new ItemStack(BlocksCore.crystalLamp,1,4))
				.apendPage(next("crystalLamp").setText(I18n.translateToLocal("essentialcraft.page.crystalLamp_0.txt")))
				.apendPage(next("crystalLamp").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.crystalLamp,1,0), 2)))
				.apendPage(next("crystalLamp").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.crystalLamp,1,1), 2)))
				.apendPage(next("crystalLamp").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.crystalLamp,1,2), 2)))
				.apendPage(next("crystalLamp").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.crystalLamp,1,3), 2)))
				.apendPage(next("crystalLamp").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.crystalLamp,1,4), 2)))
				.apendPage(next("crystalLamp").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.crystalLamp,1,5), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.overworld")
				.setReferal(new ItemStack(ItemsCore.baublesCore,1,0),new ItemStack(ItemsCore.baublesCore,1,1),new ItemStack(ItemsCore.baublesCore,1,2),new ItemStack(ItemsCore.baublesCore,1,3),new ItemStack(ItemsCore.baublesCore,1,4),new ItemStack(ItemsCore.baublesCore,1,5),new ItemStack(ItemsCore.baublesCore,1,6),new ItemStack(ItemsCore.baublesCore,1,7),new ItemStack(ItemsCore.baublesCore,1,8),new ItemStack(ItemsCore.baublesCore,1,9),new ItemStack(ItemsCore.baublesCore,1,10),new ItemStack(ItemsCore.baublesCore,1,11),new ItemStack(ItemsCore.baublesCore,1,12),new ItemStack(ItemsCore.baublesCore,1,13),new ItemStack(ItemsCore.baublesCore,1,14),new ItemStack(ItemsCore.baublesCore,1,15),new ItemStack(ItemsCore.baublesCore,1,16),new ItemStack(ItemsCore.baublesCore,1,17),new ItemStack(ItemsCore.baublesCore,1,18),new ItemStack(ItemsCore.baublesCore,1,19),new ItemStack(ItemsCore.baublesCore,1,20),new ItemStack(ItemsCore.baublesCore,1,21),new ItemStack(ItemsCore.baublesCore,1,22),new ItemStack(ItemsCore.baublesCore,1,23),new ItemStack(ItemsCore.baublesCore,1,24),new ItemStack(ItemsCore.baublesCore,1,25),new ItemStack(ItemsCore.baublesCore,1,26),new ItemStack(ItemsCore.baublesCore,1,27),new ItemStack(ItemsCore.baublesCore,1,28),new ItemStack(ItemsCore.baublesCore,1,29),new ItemStack(ItemsCore.baublesCore,1,30),new ItemStack(ItemsCore.baublesCore,1,31))
				.setDisplayStack(new ItemStack(ItemsCore.baublesCore,1,14))

				.apendPage(next("overworld").setText(I18n.translateToLocal("essentialcraft.page.overworld_0.txt")))
				.apendPage(next("overworld").setText(I18n.translateToLocal("essentialcraft.page.overworld_1.txt")))
				.apendPage(next("overworld").setDisplayStacks(new ItemStack(ItemsCore.baublesCore,1,0),new ItemStack(ItemsCore.baublesCore,1,1),new ItemStack(ItemsCore.baublesCore,1,2),new ItemStack(ItemsCore.baublesCore,1,3),new ItemStack(ItemsCore.baublesCore,1,4),new ItemStack(ItemsCore.baublesCore,1,5),new ItemStack(ItemsCore.baublesCore,1,6),new ItemStack(ItemsCore.baublesCore,1,7),new ItemStack(ItemsCore.baublesCore,1,8),new ItemStack(ItemsCore.baublesCore,1,9),new ItemStack(ItemsCore.baublesCore,1,10),new ItemStack(ItemsCore.baublesCore,1,11),new ItemStack(ItemsCore.baublesCore,1,12),new ItemStack(ItemsCore.baublesCore,1,13),new ItemStack(ItemsCore.baublesCore,1,14),new ItemStack(ItemsCore.baublesCore,1,15),new ItemStack(ItemsCore.baublesCore,1,16),new ItemStack(ItemsCore.baublesCore,1,17),new ItemStack(ItemsCore.baublesCore,1,18),new ItemStack(ItemsCore.baublesCore,1,19),new ItemStack(ItemsCore.baublesCore,1,20),new ItemStack(ItemsCore.baublesCore,1,21),new ItemStack(ItemsCore.baublesCore,1,22),new ItemStack(ItemsCore.baublesCore,1,23),new ItemStack(ItemsCore.baublesCore,1,24),new ItemStack(ItemsCore.baublesCore,1,25),new ItemStack(ItemsCore.baublesCore,1,26),new ItemStack(ItemsCore.baublesCore,1,27),new ItemStack(ItemsCore.baublesCore,1,28),new ItemStack(ItemsCore.baublesCore,1,29),new ItemStack(ItemsCore.baublesCore,1,30),new ItemStack(ItemsCore.baublesCore,1,31)))
				)
		;
	}

	public static void registerMruCategory()
	{
		ItemStack book_t1 = new ItemStack(ItemsCore.research_book);
		MiscUtils.getStackTag(book_t1).setInteger("tier", 1);
		ItemStack book_t2 = new ItemStack(ItemsCore.research_book);
		MiscUtils.getStackTag(book_t2).setInteger("tier", 2);
		ItemStack pistol = new ItemStack(ItemsCore.pistol,1,0);
		ItemGun.createRandomGun(pistol);
		ItemStack rifle = new ItemStack(ItemsCore.rifle,1,0);
		ItemGun.createRandomGun(rifle);
		ItemStack sniper = new ItemStack(ItemsCore.sniper,1,0);
		ItemGun.createRandomGun(sniper);
		ItemStack gatling = new ItemStack(ItemsCore.gatling,1,0);
		ItemGun.createRandomGun(gatling);
		mru
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.boundGem")
				.setReferal(new ItemStack(ItemsCore.bound_gem,1,0))
				.setDisplayStack(new ItemStack(ItemsCore.bound_gem))
				.apendPage(
						new PageEntry("essentialcraft.page.boundGem_0")
						.setText(I18n.translateToLocal("essentialcraft.page.boundGem_0.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.boundGem_1")
						.setText(I18n.translateToLocal("essentialcraft.page.boundGem_1.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.boundGem_2")
						.setText(I18n.translateToLocal("essentialcraft.page.boundGem_2.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.boundGem_3")
						.setText(I18n.translateToLocal("essentialcraft.page.boundGem_3.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.boundGem_4")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.bound_gem), 2))
						.setText(I18n.translateToLocal("essentialcraft.page.boundGem_4.txt"))
						)
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.matrixDestructor")
				.setReferal(new ItemStack(BlocksCore.matrixAbsorber,1,0))
				.setDisplayStack(new ItemStack(BlocksCore.matrixAbsorber))
				.apendPage(
						new PageEntry("essentialcraft.page.matrixDestructor_0")
						.setText(I18n.translateToLocal("essentialcraft.page.matrixDestructor_0.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.matrixDestructor_1")
						.setText(I18n.translateToLocal("essentialcraft.page.matrixDestructor_1.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.matrixDestructor_2")
						.setText(I18n.translateToLocal("essentialcraft.page.matrixDestructor_2.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.matrixDestructor_3")
						.setText(I18n.translateToLocal("essentialcraft.page.matrixDestructor_3.txt"))
						.setDisplayStacks(new ItemStack(ItemsCore.soulStone,1,0))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.matrixDestructor_4")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.matrixAbsorber), 2))
						)

				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.radiatingChamber")
				.setReferal(new ItemStack(BlocksCore.radiatingChamber,1,0))
				.setDisplayStack(new ItemStack(BlocksCore.radiatingChamber))
				.apendPage(
						new PageEntry("essentialcraft.page.radiatingChamber_0")
						.setText(I18n.translateToLocal("essentialcraft.page.radiatingChamber_0.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.radiatingChamber_1")
						.setText(I18n.translateToLocal("essentialcraft.page.radiatingChamber_1.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.radiatingChamber_2")
						.setText(I18n.translateToLocal("essentialcraft.page.radiatingChamber_2.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.radiatingChamber_3")
						.setText(I18n.translateToLocal("essentialcraft.page.radiatingChamber_3.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.radiatingChamber_4")
						.setText(I18n.translateToLocal("essentialcraft.page.radiatingChamber_4.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.radiatingChamber_5")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.radiatingChamber), 2))
						)
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.miscRadiation")

				.setReferal(
						new ItemStack(ItemsCore.genericItem,1,3),
						new ItemStack(ItemsCore.genericItem,1,43),
						new ItemStack(ItemsCore.genericItem,1,44),
						new ItemStack(BlocksCore.fortifiedStone,1,0),
						new ItemStack(BlocksCore.fortifiedGlass,1,0),
						new ItemStack(ItemsCore.genericItem,1,24),
						new ItemStack(ItemsCore.genericItem,1,31),
						new ItemStack(ItemsCore.elemental_pick,1,0),
						new ItemStack(ItemsCore.elemental_axe,1,0),
						new ItemStack(ItemsCore.elemental_sword,1,0),
						new ItemStack(ItemsCore.elemental_hoe,1,0),
						new ItemStack(ItemsCore.elemental_shovel,1,0)
						)
				.setDisplayStack(new ItemStack(ItemsCore.genericItem,1,44))
				.apendPage(
						new PageEntry("essentialcraft.page.miscRadiation_0")
						.setText(I18n.translateToLocal("essentialcraft.page.miscRadiation_0.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.miscRadiation_1")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.genericItem,1,3), 5))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.miscRadiation_2")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.genericItem,1,43), 5))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.miscRadiation_3")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.genericItem,1,44), 5))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.miscRadiation_4")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.fortifiedStone,1,0), 5))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.miscRadiation_5")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.fortifiedGlass,1,0), 5))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.miscRadiation_6")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.genericItem,1,24), 2))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.miscRadiation_7")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.genericItem,1,31), 2))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.drops_6")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.elemental_axe,1,0), 2))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.drops_7")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.elemental_hoe,1,0), 2))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.drops_8")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.elemental_pick,1,0), 2))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.drops_9")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.elemental_shovel,1,0), 2))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.drops_10")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.elemental_sword,1,0), 3))
						)
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.magicianTable")
				.setReferal(new ItemStack(BlocksCore.magicianTable,1,0))
				.setDisplayStack(new ItemStack(BlocksCore.magicianTable,1,0))
				.apendPage(
						new PageEntry("essentialcraft.page.magicianTable_0")
						.setText(I18n.translateToLocal("essentialcraft.page.magicianTable_0.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.magicianTable_1")
						.setText(I18n.translateToLocal("essentialcraft.page.magicianTable_1.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.magicianTable_2")
						.setText(I18n.translateToLocal("essentialcraft.page.magicianTable_2.txt"))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.magicianTable_3")
						.setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.magicianTable), 2))
						)
				.apendPage(
						new PageEntry("essentialcraft.page.magicianTable_4")
						.setDisplayStacks(MagicianTableUpgrades.UPGRADE_STACKS.toArray(new ItemStack[MagicianTableUpgrades.UPGRADE_STACKS.size()]))
						.setText(I18n.translateToLocal("essentialcraft.page.magicianTable_4.txt"))
						)
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.magicianCrafts")
				.setReferal(
						new ItemStack(ItemsCore.genericItem,1,0),
						new ItemStack(ItemsCore.genericItem,1,5),
						new ItemStack(ItemsCore.genericItem,1,6),
						new ItemStack(ItemsCore.genericItem,1,4),
						new ItemStack(ItemsCore.genericItem,1,7),
						new ItemStack(ItemsCore.genericItem,1,8),
						new ItemStack(ItemsCore.genericItem,1,9),
						new ItemStack(ItemsCore.genericItem,1,10),
						new ItemStack(ItemsCore.genericItem,1,11),
						new ItemStack(ItemsCore.genericItem,1,12),
						new ItemStack(ItemsCore.genericItem,1,13),
						new ItemStack(ItemsCore.genericItem,1,14),
						new ItemStack(ItemsCore.genericItem,1,15),
						new ItemStack(ItemsCore.genericItem,1,16),
						new ItemStack(ItemsCore.genericItem,1,20),
						new ItemStack(ItemsCore.genericItem,1,34),
						new ItemStack(ItemsCore.genericItem,1,32),
						new ItemStack(ItemsCore.genericItem,1,27),
						new ItemStack(ItemsCore.genericItem,1,30),
						new ItemStack(ItemsCore.genericItem,1,33),
						new ItemStack(ItemsCore.genericItem,1,79)
						)
				.setDisplayStack(new ItemStack(ItemsCore.genericItem,1,14))
				.apendPage(
						next("magicianCrafts")
						.setText(I18n.translateToLocal("essentialcraft.page.magicianCrafts_0.txt"))
						)
				.apendPage(next("magicianCrafts").setRecipe(ECUtils.findRecipeByIS(generic(5), 6)))
				.apendPage(next("magicianCrafts").setRecipe(ECUtils.findRecipeByIS(generic(6), 6)))
				.apendPage(next("magicianCrafts").setRecipe(ECUtils.findRecipeByIS(generic(10), 6)))
				.apendPage(next("magicianCrafts").setRecipe(ECUtils.findRecipeByIS(generic(20), 6)))
				.apendPage(next("magicianCrafts").setRecipe(ECUtils.findRecipeByIS(generic(4), 6)))
				.apendPage(next("magicianCrafts").setRecipe(ECUtils.findRecipeByIS(generic(7), 6)))
				.apendPage(next("magicianCrafts").setRecipe(ECUtils.findRecipeByIS(generic(11), 6)))
				.apendPage(next("magicianCrafts").setRecipe(ECUtils.findRecipeByIS(generic(9), 6)))
				.apendPage(next("magicianCrafts").setRecipe(ECUtils.findRecipeByIS(generic(8), 6)))
				.apendPage(next("magicianCrafts").setRecipe(ECUtils.findRecipeByIS(generic(0), 6)))
				.apendPage(next("magicianCrafts").setRecipe(ECUtils.findRecipeByIS(generic(32), 6)))
				.apendPage(next("magicianCrafts").setRecipe(ECUtils.findRecipeByIS(generic(34), 6)))
				.apendPage(next("magicianCrafts").setRecipe(ECUtils.findRecipeByIS(generic(12), 6)))
				.apendPage(next("magicianCrafts").setRecipe(ECUtils.findRecipeByIS(generic(13), 6)))
				.apendPage(next("magicianCrafts").setRecipe(ECUtils.findRecipeByIS(generic(14), 6)))
				.apendPage(next("magicianCrafts").setRecipe(ECUtils.findRecipeByIS(generic(15), 6)))
				.apendPage(next("magicianCrafts").setRecipe(ECUtils.findRecipeByIS(generic(16), 6)))
				.apendPage(next("magicianCrafts").setRecipe(ECUtils.findRecipeByIS(generic(27), 2)))
				.apendPage(next("magicianCrafts").setRecipe(ECUtils.findRecipeByIS(generic(30), 2)))
				.apendPage(next("magicianCrafts").setRecipe(ECUtils.findRecipeByIS(generic(33), 2)))
				.apendPage(next("magicianCrafts").setRecipe(ECUtils.findRecipeByIS(generic(79), 6)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.paleItems")
				.setReferal(generic(38),generic(39),generic(40),generic(41),generic(42),new ItemStack(BlocksCore.platingPale),new ItemStack(BlocksCore.blockPale))
				.setDisplayStack(new ItemStack(BlocksCore.platingPale))
				.apendPage(next("paleItems").setText(I18n.translateToLocal("essentialcraft.page.paleItems_0.txt")))
				.apendPage(next("paleItems").setRecipe(ECUtils.findRecipeByIS(generic(39), 5)))
				.apendPage(next("paleItems").setRecipe(ECUtils.findRecipeByIS(generic(38), 5)))
				.apendPage(next("paleItems").setRecipe(ECUtils.findRecipeByIS(generic(40), 5)))
				.apendPage(next("paleItems").setRecipe(ECUtils.findRecipeByIS(generic(41), 6)))
				.apendPage(next("paleItems").setRecipe(ECUtils.findRecipeByIS(generic(42), 5)))
				.apendPage(next("paleItems").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.platingPale), 2)))
				.apendPage(next("paleItems").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.blockPale), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.magicMonocle")
				.setReferal(new ItemStack(ItemsCore.magicMonocle,1,0))
				.setDisplayStack(new ItemStack(ItemsCore.magicMonocle,1,0))
				.apendPage(next("magicMonocle").setText(I18n.translateToLocal("essentialcraft.page.magicMonocle_0.txt")))
				.apendPage(next("magicMonocle").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.magicMonocle), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.mrucumover2")
				.setReferal(new ItemStack(ItemsCore.mruMover_t2,1,0))
				.setDisplayStack(new ItemStack(ItemsCore.mruMover_t2,1,0))
				.apendPage(next("mrucumover2").setText(I18n.translateToLocal("essentialcraft.page.mrucumover2_0.txt")))
				.apendPage(next("mrucumover2").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.mruMover_t2), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.mrucuec")
				.setReferal(new ItemStack(BlocksCore.magicPlating,1,0),new ItemStack(BlocksCore.ecController,1,0))
				.setDisplayStack(new ItemStack(BlocksCore.ecController,1,0))
				.apendPage(next("mrucuec").setText(I18n.translateToLocal("essentialcraft.page.mrucuec_0.txt")))
				.apendPage(next("mrucuec").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.magicPlating,1,0), 2)))
				.apendPage(next("mrucuec").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.ecController,1,0), 2)))
				.apendPage(next("mrucuec").setText(I18n.translateToLocal("essentialcraft.page.mrucuec_3.txt")))
				.apendPage(next("mrucuec").setDisplayStacks(new ItemStack(BlocksCore.fortifiedStone),new ItemStack(BlocksCore.fortifiedGlass),new ItemStack(BlocksCore.magicPlating),new ItemStack(BlocksCore.platingPale),new ItemStack(BlocksCore.voidStone),new ItemStack(BlocksCore.voidGlass), new ItemStack(BlocksCore.invertedBlock,1,0)).setText(I18n.translateToLocal("essentialcraft.page.mrucuec_4.txt")))
				.apendPage(next("mrucuec").setImg(new ResourceLocation("essentialcraft","textures/special/bookicons/mrucuec.png")))
				.apendPage(next("mrucuec").setText(I18n.translateToLocal("essentialcraft.page.mrucuec_6.txt")))
				.apendPage(next("mrucuec").setImg(new ResourceLocation("essentialcraft","textures/special/bookicons/mrucuec_mru.png")))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.mrucuec.state")
				.setReferal(new ItemStack(BlocksCore.ecStateChecker))
				.setDisplayStack(new ItemStack(BlocksCore.ecStateChecker,1,0))
				.apendPage(next("mrucuec.state").setText(I18n.translateToLocal("essentialcraft.page.mrucuec.state_0.txt")))
				.apendPage(next("mrucuec.state").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.ecStateChecker), 2)))
				.apendPage(next("mrucuec.state").setText(I18n.translateToLocal("essentialcraft.page.mrucuec.state_2.txt")))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.mrucuec.in")
				.setReferal(new ItemStack(BlocksCore.ecAcceptor))
				.setDisplayStack(new ItemStack(BlocksCore.ecAcceptor,1,0))
				.apendPage(next("mrucuec.in").setText(I18n.translateToLocal("essentialcraft.page.mrucuec.in_0.txt")))
				.apendPage(next("mrucuec.in").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.ecAcceptor), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.mrucuec.out")
				.setReferal(new ItemStack(BlocksCore.ecEjector))
				.setDisplayStack(new ItemStack(BlocksCore.ecEjector,1,0))
				.apendPage(next("mrucuec.out").setText(I18n.translateToLocal("essentialcraft.page.mrucuec.out_0.txt")))
				.apendPage(next("mrucuec.out").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.ecEjector), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.mrucuec.storage")
				.setReferal(new ItemStack(BlocksCore.ecHoldingChamber))
				.setDisplayStack(new ItemStack(BlocksCore.ecHoldingChamber,1,0))
				.apendPage(next("mrucuec.storage").setText(I18n.translateToLocal("essentialcraft.page.mrucuec.storage_0.txt")))
				.apendPage(next("mrucuec.storage").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.ecHoldingChamber), 2)))
				.apendPage(next("mrucuec.storage").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.storage,1,2), 6)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.mrucuec.balancer")
				.setReferal(new ItemStack(BlocksCore.ecBalancer))
				.setDisplayStack(new ItemStack(BlocksCore.ecBalancer,1,0))
				.apendPage(next("mrucuec.balancer").setText(I18n.translateToLocal("essentialcraft.page.mrucuec.balancer_0.txt")))
				.apendPage(next("mrucuec.balancer").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.ecBalancer), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.mrucuec.redstone")
				.setReferal(new ItemStack(BlocksCore.ecRedstoneController))
				.setDisplayStack(new ItemStack(BlocksCore.ecRedstoneController,1,0))
				.apendPage(next("mrucuec.redstone").setText(I18n.translateToLocal("essentialcraft.page.mrucuec.redstone_0.txt")))
				.apendPage(next("mrucuec.redstone").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.ecRedstoneController), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.naturalFurnace")
				.setReferal(new ItemStack(BlocksCore.naturalFurnace))
				.setDisplayStack(new ItemStack(BlocksCore.naturalFurnace,1,0))
				.apendPage(next("naturalFurnace").setText(I18n.translateToLocal("essentialcraft.page.naturalFurnace_0.txt")))
				.apendPage(next("naturalFurnace").setText(I18n.translateToLocal("essentialcraft.page.naturalFurnace_1.txt")).setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.naturalFurnace), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.heatGenerator")
				.setReferal(new ItemStack(BlocksCore.heatGenerator))
				.setDisplayStack(new ItemStack(BlocksCore.heatGenerator,1,0))
				.apendPage(next("heatGenerator").setText(I18n.translateToLocal("essentialcraft.page.heatGenerator_0.txt")))
				.apendPage(next("heatGenerator").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.heatGenerator), 2)))
				.apendPage(next("heatGenerator").setRecipe(new StructureRecipe(
						new ItemStack(BlocksCore.heatGenerator),
						new StructureBlock(BlocksCore.heatGenerator, 0, 0, 0, 0),
						new StructureBlock(Blocks.NETHERRACK,0, 2, 0, 0),
						new StructureBlock(Blocks.NETHERRACK,0, -2, 0, 0),
						new StructureBlock(Blocks.NETHERRACK,0, 0, 0, 2),
						new StructureBlock(Blocks.NETHERRACK,0, 0, 0, -2),
						new StructureBlock(BlocksCore.air, 0,0, 0, -1),
						new StructureBlock(BlocksCore.air,0, 0, 0, 1),
						new StructureBlock(BlocksCore.air,0, -1, 0, 0),
						new StructureBlock(BlocksCore.air,0, 1, 0, 0)
						))
						.setText("5MRU/tick")
						)
				.apendPage(next("mrucuec.heatGenerator").setRecipe(new StructureRecipe(
						new ItemStack(BlocksCore.heatGenerator),
						new StructureBlock(BlocksCore.heatGenerator, 0, 0, 0, 0),
						new StructureBlock(BlocksCore.fire,0, 2, 0, 0),
						new StructureBlock(BlocksCore.fire,0, -2, 0, 0),
						new StructureBlock(BlocksCore.fire,0, 0, 0, 2),
						new StructureBlock(BlocksCore.fire,0, 0, 0, -2),
						new StructureBlock(BlocksCore.air,0, 0, 0, -1),
						new StructureBlock(BlocksCore.air,0,0, 0, 1),
						new StructureBlock(BlocksCore.air,0, -1, 0, 0),
						new StructureBlock(BlocksCore.air,0, 1, 0, 0)
						))
						.setText("8MRU/tick")
						)
				.apendPage(next("mrucuec.heatGenerator").setRecipe(new StructureRecipe(
						new ItemStack(BlocksCore.heatGenerator),
						new StructureBlock(BlocksCore.heatGenerator, 0, 0, 0, 0),
						new StructureBlock(BlocksCore.lava,0, 2, 0, 0),
						new StructureBlock(BlocksCore.lava,0, -2, 0, 0),
						new StructureBlock(BlocksCore.lava, 0,0, 0, 2),
						new StructureBlock(BlocksCore.lava,0, 0, 0, -2),
						new StructureBlock(BlocksCore.air,0, 0, 0, -1),
						new StructureBlock(BlocksCore.air,0, 0, 0, 1),
						new StructureBlock(BlocksCore.air,0, -1, 0, 0),
						new StructureBlock(BlocksCore.air,0, 1, 0, 0)
						))
						.setText("16MRU/tick")
						)
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.magicalMirrors")
				.setReferal(new ItemStack(BlocksCore.magicalMirror),new ItemStack(ItemsCore.controlRod))
				.setDisplayStack(new ItemStack(BlocksCore.magicalMirror,1,0))

				.apendPage(next("magicalMirrors").setText(I18n.translateToLocal("essentialcraft.page.magicalMirrors_0.txt")))
				.apendPage(next("magicalMirrors").setText(I18n.translateToLocal("essentialcraft.page.magicalMirrors_1.txt")))
				.apendPage(next("magicalMirrors").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.magicalMirror), 2)))
				.apendPage(next("magicalMirrors").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.controlRod), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.corruptionCleaner")

				.setReferal(new ItemStack(BlocksCore.corruptionCleaner))
				.setDisplayStack(new ItemStack(BlocksCore.corruptionCleaner,1,0))
				.apendPage(next("corruptionCleaner").setText(I18n.translateToLocal("essentialcraft.page.corruptionCleaner_0.txt")))
				.apendPage(next("corruptionCleaner").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.corruptionCleaner), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.rayTower")
				.setReferal(new ItemStack(BlocksCore.rayTower))
				.setDisplayStack(new ItemStack(BlocksCore.rayTower,1,0))
				.apendPage(next("rayTower").setText(I18n.translateToLocal("essentialcraft.page.rayTower_0.txt")))
				.apendPage(next("rayTower").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.rayTower), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.book_t2")
				.setDisplayStack(book_t2)
				.apendPage(next("book_t2").setText(I18n.translateToLocal("essentialcraft.page.book_t2_0.txt")))
				.apendPage(next("book_t2").setRecipe(ECUtils.findRecipeByIS(book_t2, 6)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.magicalActivator")

				.setDisplayStack(new ItemStack(BlocksCore.rightClicker))
				.setReferal(new ItemStack(BlocksCore.rightClicker))
				.apendPage(next("magicalActivator").setText(I18n.translateToLocal("essentialcraft.page.magicalActivator_0.txt")))
				.apendPage(next("magicalActivator").setText(I18n.translateToLocal("essentialcraft.page.magicalActivator_1.txt")))
				.apendPage(next("magicalActivator").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.rightClicker,1,0), 2)))
				.apendPage(next("magicalActivator").setText(I18n.translateToLocal("essentialcraft.page.magicalActivator_2.txt")))
				.apendPage(next("magicalActivator").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.rightClicker,1,1), 3)))
				.apendPage(next("magicalActivator").setText(I18n.translateToLocal("essentialcraft.page.magicalActivator_3.txt")))
				.apendPage(next("magicalActivator").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.rightClicker,1,2), 3)))
				.apendPage(next("magicalActivator").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.rightClicker,1,3), 3)))
				.apendPage(next("magicalActivator").setText(I18n.translateToLocal("essentialcraft.page.magicalActivator_4.txt")))
				.apendPage(next("magicalActivator").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.rightClicker,1,4), 3)))
				.apendPage(next("magicalActivator").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.rightClicker,1,5), 3)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.redstoneTransmitter")
				.setDisplayStack(new ItemStack(BlocksCore.redstoneTransmitter))
				.setReferal(new ItemStack(BlocksCore.redstoneTransmitter))

				.apendPage(next("redstoneTransmitter").setText(I18n.translateToLocal("essentialcraft.page.redstoneTransmitter_0.txt")))
				.apendPage(next("redstoneTransmitter").setText(I18n.translateToLocal("essentialcraft.page.redstoneTransmitter_1.txt")))
				.apendPage(next("redstoneTransmitter").setText(I18n.translateToLocal("essentialcraft.page.redstoneTransmitter_2.txt")))
				.apendPage(next("redstoneTransmitter").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.redstoneTransmitter), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.magicalHopper")
				.setDisplayStack(new ItemStack(BlocksCore.magicalHopper))
				.setReferal(new ItemStack(BlocksCore.magicalHopper))

				.apendPage(next("magicalHopper").setText(I18n.translateToLocal("essentialcraft.page.magicalHopper_0.txt")))
				.apendPage(next("magicalHopper").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.magicalHopper), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.metadataManager")
				.setDisplayStack(new ItemStack(BlocksCore.metadataManager))
				.setReferal(new ItemStack(BlocksCore.metadataManager))

				.apendPage(next("metadataManager").setText(I18n.translateToLocal("essentialcraft.page.metadataManager_0.txt")))
				.apendPage(next("metadataManager").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.metadataManager), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.blockBreaker")
				.setDisplayStack(new ItemStack(BlocksCore.blockBreaker))
				.setReferal(new ItemStack(BlocksCore.blockBreaker))

				.apendPage(next("blockBreaker").setText(I18n.translateToLocal("essentialcraft.page.blockBreaker_0.txt")))
				.apendPage(next("blockBreaker").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.blockBreaker), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.weaponMaker")
				.setDisplayStack(new ItemStack(BlocksCore.weaponMaker))
				.setReferal(new ItemStack(BlocksCore.weaponMaker))

				.apendPage(next("weaponMaker").setText(I18n.translateToLocal("essentialcraft.page.weaponMaker_0.txt")))
				.apendPage(next("weaponMaker").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.weaponMaker), 2)))
				.apendPage(next("weaponMaker").setText(I18n.translateToLocal("essentialcraft.page.weaponMaker_1.txt")))
				.apendPage(next("weaponMaker").setText(I18n.translateToLocal("essentialcraft.page.weaponMaker_2.txt")).setDisplayStacks(pistol))
				.apendPage(next("weaponMaker").setText(I18n.translateToLocal("essentialcraft.page.weaponMaker_3.txt")).setDisplayStacks(rifle))
				.apendPage(next("weaponMaker").setText(I18n.translateToLocal("essentialcraft.page.weaponMaker_4.txt")).setDisplayStacks(sniper))
				.apendPage(next("weaponMaker").setText(I18n.translateToLocal("essentialcraft.page.weaponMaker_5.txt")).setDisplayStacks(gatling))
				.apendPage(next("weaponMaker").setText(I18n.translateToLocal("essentialcraft.page.weaponMaker_6.txt")))
				.apendPage(next("weaponMaker").setText(I18n.translateToLocal("essentialcraft.page.weaponMaker_7.txt")).setDisplayStacks(new ItemStack(ItemsCore.genericItem,1,5),new ItemStack(ItemsCore.genericItem,1,39),new ItemStack(ItemsCore.genericItem,1,50),new ItemStack(ItemsCore.genericItem,1,35),new ItemStack(ItemsCore.genericItem,1,52)))
				.apendPage(next("weaponMaker").setText(I18n.translateToLocal("essentialcraft.page.weaponMaker_8.txt")))
				.apendPage(next("weaponMaker").setText(I18n.translateToLocal("essentialcraft.page.weaponMaker_9.txt")))
				.apendPage(next("weaponMaker").setText(I18n.translateToLocal("essentialcraft.page.weaponMaker_10.txt")))
				.apendPage(next("weaponMaker").setText(I18n.translateToLocal("essentialcraft.page.weaponMaker_11.txt")))
				.apendPage(next("weaponMaker").setText(I18n.translateToLocal("essentialcraft.page.weaponMaker_12.txt")))
				.apendPage(next("weaponMaker").setText(I18n.translateToLocal("essentialcraft.page.weaponMaker_13.txt")))
				.apendPage(next("weaponMaker").setText(I18n.translateToLocal("essentialcraft.page.weaponMaker_14.txt")))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.magmaticFurnace")
				.setDisplayStack(new ItemStack(BlocksCore.furnaceMagic))
				.setReferal(new ItemStack(BlocksCore.furnaceMagic))

				.apendPage(next("magmaticFurnace").setText(I18n.translateToLocal("essentialcraft.page.magmaticFurnace_0.txt")))
				.apendPage(next("magmaticFurnace").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.furnaceMagic,1,0), 2)))
				.apendPage(next("magmaticFurnace").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.furnaceMagic,1,4), 2)))
				.apendPage(next("magmaticFurnace").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.furnaceMagic,1,8), 2)))
				.apendPage(next("magmaticFurnace").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.furnaceMagic,1,12), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.magicalChest")
				.setDisplayStack(new ItemStack(BlocksCore.chest))
				.setReferal(new ItemStack(BlocksCore.chest,1,0),new ItemStack(BlocksCore.chest,1,1))

				.apendPage(next("magicalChest").setText(I18n.translateToLocal("essentialcraft.page.magicalChest_0.txt")))
				.apendPage(next("magicalChest").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.chest,1,0), 2)))
				.apendPage(next("magicalChest").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.chest,1,1), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.replanter")
				.setDisplayStack(new ItemStack(BlocksCore.device,1,0))
				.setReferal(new ItemStack(BlocksCore.device,1,0))

				.apendPage(next("replanter").setText(I18n.translateToLocal("essentialcraft.page.replanter_0.txt")))
				.apendPage(next("replanter").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.device,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.itemShuffler")
				.setDisplayStack(new ItemStack(BlocksCore.device,1,1))
				.setReferal(new ItemStack(BlocksCore.device,1,1))

				.apendPage(next("itemShuffler").setText(I18n.translateToLocal("essentialcraft.page.itemShuffler_0.txt")))
				.apendPage(next("itemShuffler").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.device,1,1), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.crafter")
				.setDisplayStack(new ItemStack(BlocksCore.device,1,2))
				.setReferal(new ItemStack(BlocksCore.device,1,2),new ItemStack(ItemsCore.craftingFrame,1,0))

				.apendPage(next("crafter").setText(I18n.translateToLocal("essentialcraft.page.crafter_0.txt")))
				.apendPage(next("crafter").setText(I18n.translateToLocal("essentialcraft.page.crafter_1.txt")))
				.apendPage(next("crafter").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.device,1,2), 2)))
				.apendPage(next("crafter").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.craftingFrame,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.breeder")
				.setDisplayStack(new ItemStack(BlocksCore.device,1,3))
				.setReferal(new ItemStack(BlocksCore.device,1,3))

				.apendPage(next("breeder").setText(I18n.translateToLocal("essentialcraft.page.breeder_0.txt")))
				.apendPage(next("breeder").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.device,1,3), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.shearingStation")
				.setDisplayStack(new ItemStack(BlocksCore.device,1,5))
				.setReferal(new ItemStack(BlocksCore.device,1,5))

				.apendPage(next("shearingStation").setText(I18n.translateToLocal("essentialcraft.page.shearingStation_0.txt")))
				.apendPage(next("shearingStation").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.device,1,5), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.separator")
				.setDisplayStack(new ItemStack(BlocksCore.device,1,6))
				.setReferal(new ItemStack(BlocksCore.device,1,6),new ItemStack(BlocksCore.device,1,6))

				.apendPage(next("separator").setText(I18n.translateToLocal("essentialcraft.page.separator_0.txt")))
				.apendPage(next("separator").setText(I18n.translateToLocal("essentialcraft.page.separator_1.txt")))
				.apendPage(next("separator").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.device,1,6), 2)))
				.apendPage(next("separator").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.device,1,7), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.advBreaker")
				.setDisplayStack(new ItemStack(BlocksCore.advBreaker,1,0))
				.setReferal(new ItemStack(BlocksCore.advBreaker,1,0),new ItemStack(ItemsCore.filter,1,0))

				.apendPage(next("advBreaker").setText(I18n.translateToLocal("essentialcraft.page.advBreaker_0.txt")))
				.apendPage(next("advBreaker").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.advBreaker,1,0), 2)))
				.apendPage(next("advBreaker").setText(I18n.translateToLocal("essentialcraft.page.advBreaker_1.txt")))
				.apendPage(next("advBreaker").setText(I18n.translateToLocal("essentialcraft.page.advBreaker_2.txt")))
				.apendPage(next("advBreaker").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.filter,1,0), 2)))
				.apendPage(next("advBreaker").setText(I18n.translateToLocal("essentialcraft.page.advBreaker_3.txt")))
				.apendPage(next("advBreaker").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.filter,1,2), 2)))
				.apendPage(next("advBreaker").setText(I18n.translateToLocal("essentialcraft.page.advBreaker_4.txt")))
				.apendPage(next("advBreaker").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.filter,1,1), 2)))
				.apendPage(next("advBreaker").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.filter,1,3), 2)))
				)
		.apendDiscovery(new DiscoveryEntry("essentialcraft.disc.mimic")
				.setDisplayStack(new ItemStack(BlocksCore.mimic,1,0))
				.setReferal(new ItemStack(BlocksCore.mimic,1,0))
				.apendPage(next("mimic").setText(I18n.translateToLocal("essentialcraft.page.mimic_0.txt")))
				.apendPage(next("mimic").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.mimic,1,0), 2)))
				)
		;
	}

	public static void registerEngineersCategory() {
		ItemStack book_t2 = new ItemStack(ItemsCore.research_book);
		MiscUtils.getStackTag(book_t2).setInteger("tier", 2);
		ItemStack book_t3 = new ItemStack(ItemsCore.research_book);
		MiscUtils.getStackTag(book_t3).setInteger("tier", 3);
		ItemStack crystal = new ItemStack(BlocksCore.elementalCrystal);
		MiscUtils.getStackTag(crystal).setFloat("size",100);
		MiscUtils.getStackTag(crystal).setFloat("fire",100);
		eng
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.crystals")
				.setReferal(crystal,new ItemStack(BlocksCore.crystalFormer),new ItemStack(BlocksCore.crystalController),new ItemStack(BlocksCore.crystalExtractor),new ItemStack(ItemsCore.elementalFuel,1,0),new ItemStack(ItemsCore.elementalFuel,1,1),new ItemStack(ItemsCore.elementalFuel,1,2),new ItemStack(ItemsCore.elementalFuel,1,3),new ItemStack(ItemsCore.fFocus),new ItemStack(ItemsCore.wFocus),new ItemStack(ItemsCore.eFocus),new ItemStack(ItemsCore.aFocus))
				.setDisplayStack(crystal)
				.apendPage(next("crystals").setText(I18n.translateToLocal("essentialcraft.page.crystals_0.txt")))
				.apendPage(next("crystals").setText(I18n.translateToLocal("essentialcraft.page.crystals_1.txt")))
				.apendPage(next("crystals").setDisplayStacks(crystal).setText(I18n.translateToLocal("essentialcraft.page.crystals_2.txt")))
				.apendPage(next("crystals").setText(I18n.translateToLocal("essentialcraft.page.crystals_3.txt")))
				.apendPage(next("crystals").setText(I18n.translateToLocal("essentialcraft.page.crystals_4.txt")))
				.apendPage(next("crystals").setText(I18n.translateToLocal("essentialcraft.page.crystals_5.txt")))
				.apendPage(next("crystals").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.crystalFormer), 2)))
				.apendPage(next("crystals").setText(I18n.translateToLocal("essentialcraft.page.crystals_7.txt")))
				.apendPage(next("crystals").setText(I18n.translateToLocal("essentialcraft.page.crystals_8.txt")))
				.apendPage(next("crystals").setText(I18n.translateToLocal("essentialcraft.page.crystals_9.txt")))
				.apendPage(next("crystals").setText(I18n.translateToLocal("essentialcraft.page.crystals_10.txt")))
				.apendPage(next("crystals").setText(I18n.translateToLocal("essentialcraft.page.crystals_11.txt")))
				.apendPage(next("crystals").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.crystalController), 2)))
				.apendPage(next("crystals").setText(I18n.translateToLocal("essentialcraft.page.crystals_13.txt")))
				.apendPage(next("crystals").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.crystalExtractor), 2)))
				.apendPage(next("crystals").setText(I18n.translateToLocal("essentialcraft.page.crystals_15.txt")))
				.apendPage(next("crystals").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.elementalFuel,1,0), 2)))
				.apendPage(next("crystals").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.elementalFuel,1,1), 2)))
				.apendPage(next("crystals").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.elementalFuel,1,2), 2)))
				.apendPage(next("crystals").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.elementalFuel,1,3), 2)))
				.apendPage(next("crystals").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.fFocus,1,0), 2)))
				.apendPage(next("crystals").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.wFocus,1,0), 2)))
				.apendPage(next("crystals").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.eFocus,1,0), 2)))
				.apendPage(next("crystals").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.aFocus,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.matrixSwitch")
				.setReferal(new ItemStack(ItemsCore.matrixProj,1,0),new ItemStack(ItemsCore.matrixProj,1,1),new ItemStack(ItemsCore.matrixProj,1,2),new ItemStack(ItemsCore.matrixProj,1,3),new ItemStack(ItemsCore.matrixProj,1,4))
				.setDisplayStack(new ItemStack(ItemsCore.matrixProj,1,3))
				.apendPage(next("matrixSwitch").setText(I18n.translateToLocal("essentialcraft.page.matrixSwitch_0.txt")))
				.apendPage(next("matrixSwitch").setText(I18n.translateToLocal("essentialcraft.page.matrixSwitch_1.txt")))
				.apendPage(next("matrixSwitch").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.matrixProj,1,0), 5)))
				.apendPage(next("matrixSwitch").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.matrixProj,1,1), 5)))
				.apendPage(next("matrixSwitch").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.matrixProj,1,2), 5)))
				.apendPage(next("matrixSwitch").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.matrixProj,1,3), 5)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.enderGen")
				.setReferal(new ItemStack(BlocksCore.enderGenerator,1,0))
				.setDisplayStack(new ItemStack(BlocksCore.enderGenerator,1,0))
				.apendPage(next("enderGen").setText(I18n.translateToLocal("essentialcraft.page.enderGen_0.txt")))
				.apendPage(next("enderGen").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.enderGenerator,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.coldDistillator")

				.setReferal(new ItemStack(BlocksCore.coldStone,1,0),new ItemStack(BlocksCore.coldDistillator,1,0))
				.setDisplayStack(new ItemStack(BlocksCore.coldDistillator,1,0))
				.apendPage(next("coldDistillator").setText(I18n.translateToLocal("essentialcraft.page.coldDistillator_0.txt")))
				.apendPage(next("coldDistillator").setText(I18n.translateToLocal("essentialcraft.page.coldDistillator_1.txt")))
				.apendPage(next("coldDistillator").setText(I18n.translateToLocal("essentialcraft.page.coldDistillator_2.txt")))
				.apendPage(next("coldDistillator").setText(I18n.translateToLocal("essentialcraft.page.coldDistillator_3.txt")).setDisplayStacks(new ItemStack(Blocks.SNOW_LAYER)))
				.apendPage(next("coldDistillator").setText(I18n.translateToLocal("essentialcraft.page.coldDistillator_4.txt")).setDisplayStacks(new ItemStack(Blocks.SNOW)))
				.apendPage(next("coldDistillator").setText(I18n.translateToLocal("essentialcraft.page.coldDistillator_5.txt")).setDisplayStacks(new ItemStack(Blocks.ICE)))
				.apendPage(next("coldDistillator").setText(I18n.translateToLocal("essentialcraft.page.coldDistillator_6.txt")).setDisplayStacks(new ItemStack(Blocks.PACKED_ICE)))
				.apendPage(next("coldDistillator").setText(I18n.translateToLocal("essentialcraft.page.coldDistillator_7.txt")).setDisplayStacks(new ItemStack(BlocksCore.coldStone)))
				.apendPage(next("coldDistillator").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.coldStone,1,0), 2)))
				.apendPage(next("coldDistillator").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.coldDistillator,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.solarAbsorber")
				.setReferal(new ItemStack(BlocksCore.solarPrism,1,0),new ItemStack(BlocksCore.sunRayAbsorber,1,0))
				.setDisplayStack(new ItemStack(BlocksCore.solarPrism,1,0))

				.apendPage(next("solarAbsorber").setText(I18n.translateToLocal("essentialcraft.page.solarAbsorber_0.txt")))
				.apendPage(next("solarAbsorber").setText(I18n.translateToLocal("essentialcraft.page.solarAbsorber_1.txt")))
				.apendPage(next("solarAbsorber").setText(I18n.translateToLocal("essentialcraft.page.solarAbsorber_2.txt")))
				.apendPage(next("solarAbsorber").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.solarPrism,1,0), 2)))
				.apendPage(next("solarAbsorber").setRecipe(new StructureRecipe(new ItemStack(BlocksCore.solarPrism,1,0),
						new StructureBlock(BlocksCore.solarPrism, 0, 0, 0, 0),
						new StructureBlock(BlocksCore.air, 0, 1, 0, 0),
						new StructureBlock(BlocksCore.air, 0, -1, 0, 0),
						new StructureBlock(BlocksCore.air, 0, 0, 0, 1),
						new StructureBlock(BlocksCore.air, 0, 0, 0, -1),
						new StructureBlock(BlocksCore.air, 0, 1, 0, 1),
						new StructureBlock(BlocksCore.air, 0, 1, 0, -1),
						new StructureBlock(BlocksCore.air, 0, -1, 0, 1),
						new StructureBlock(BlocksCore.air, 0, -1, 0, -1),
						new StructureBlock(BlocksCore.magicPlating, 0, 2, 0, 0),
						new StructureBlock(BlocksCore.magicPlating, 0, -2, 0, 0),
						new StructureBlock(BlocksCore.magicPlating, 0, 0, 0, 2),
						new StructureBlock(BlocksCore.magicPlating, 0, 0, 0, -2)
						)))
				.apendPage(next("solarAbsorber").setText(I18n.translateToLocal("essentialcraft.page.solarAbsorber_5.txt")))
				.apendPage(next("solarAbsorber").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.sunRayAbsorber,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.moonWell")

				.setReferal(new ItemStack(BlocksCore.moonWell,1,0))
				.setDisplayStack(new ItemStack(BlocksCore.moonWell,1,0))
				.apendPage(next("moonWell").setText(I18n.translateToLocal("essentialcraft.page.moonWell_0.txt")))
				.apendPage(next("moonWell").setText(I18n.translateToLocal("essentialcraft.page.moonWell_1.txt")))
				.apendPage(next("moonWell").setText(I18n.translateToLocal("essentialcraft.page.moonWell_2.txt")))
				.apendPage(next("moonWell").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.moonWell,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.reactor")

				.setReferal(new ItemStack(BlocksCore.reactor,1,0),new ItemStack(BlocksCore.reactorSupport,1,0))
				.setDisplayStack(new ItemStack(BlocksCore.reactor,1,0))
				.apendPage(next("reactor").setText(I18n.translateToLocal("essentialcraft.page.reactor_0.txt")))
				.apendPage(next("reactor").setImg(new ResourceLocation("essentialcraft","textures/special/bookicons/reactor.png")))
				.apendPage(next("reactor").setText(I18n.translateToLocal("essentialcraft.page.reactor_2.txt")))
				.apendPage(next("reactor").setRecipe(new StructureRecipe(new ItemStack(BlocksCore.reactor,1,0),
						new StructureBlock(BlocksCore.magicPlating, 0, 0, 0, 0),
						new StructureBlock(BlocksCore.magicPlating, 0, 0, 0, 1),
						new StructureBlock(BlocksCore.magicPlating, 0, 0, 0, -1),
						new StructureBlock(BlocksCore.magicPlating, 0, 1, 0, 0),
						new StructureBlock(BlocksCore.magicPlating, 0, 1, 0, 1),
						new StructureBlock(BlocksCore.magicPlating, 0, 1, 0, -1),
						new StructureBlock(BlocksCore.magicPlating, 0, 2, 0, 0),
						new StructureBlock(BlocksCore.magicPlating, 0, 2, 0, 1),
						new StructureBlock(BlocksCore.magicPlating, 0, 2, 0, -1),
						new StructureBlock(BlocksCore.magicPlating, 0, -1, 0, 0),
						new StructureBlock(BlocksCore.magicPlating, 0, -1, 0, 1),
						new StructureBlock(BlocksCore.magicPlating, 0, -1, 0, -1),
						new StructureBlock(BlocksCore.magicPlating, 0, -2, 0, 0),
						new StructureBlock(BlocksCore.magicPlating, 0, -2, 0, 1),
						new StructureBlock(BlocksCore.magicPlating, 0, -2, 0, -1)
						)))
				.apendPage(next("reactor").setRecipe(new StructureRecipe(new ItemStack(BlocksCore.reactor,1,0),
						new StructureBlock(BlocksCore.reactor, 0, 0, 0, 0),
						new StructureBlock(BlocksCore.air, 0, 0, 0, 1),
						new StructureBlock(BlocksCore.air, 0, 0, 0, -1),
						new StructureBlock(BlocksCore.reactorSupport, 0, 1, 0, 0),
						new StructureBlock(BlocksCore.air, 0, 1, 0, 1),
						new StructureBlock(BlocksCore.air, 0, 1, 0, -1),
						new StructureBlock(BlocksCore.air, 0, 2, 0, 0),
						new StructureBlock(BlocksCore.air, 0, 2, 0, 1),
						new StructureBlock(BlocksCore.air, 0, 2, 0, -1),
						new StructureBlock(BlocksCore.reactorSupport, 0, -1, 0, 0),
						new StructureBlock(BlocksCore.air, 0, -1, 0, 1),
						new StructureBlock(BlocksCore.air, 0, -1, 0, -1),
						new StructureBlock(BlocksCore.air, 0, -2, 0, 0),
						new StructureBlock(BlocksCore.air, 0, -2, 0, 1),
						new StructureBlock(BlocksCore.air, 0, -2, 0, -1)
						)))
				.apendPage(next("reactor").setRecipe(new StructureRecipe(new ItemStack(BlocksCore.reactor,1,0),
						new StructureBlock(BlocksCore.air, 0, 0, 0, 0),
						new StructureBlock(BlocksCore.air, 0, 0, 0, 1),
						new StructureBlock(BlocksCore.air, 0, 0, 0, -1),
						new StructureBlock(BlocksCore.reactorSupport, 0, 1, 0, 0),
						new StructureBlock(BlocksCore.air, 0, 1, 0, 1),
						new StructureBlock(BlocksCore.air, 0, 1, 0, -1),
						new StructureBlock(BlocksCore.air, 0, 2, 0, 0),
						new StructureBlock(BlocksCore.air, 0, 2, 0, 1),
						new StructureBlock(BlocksCore.air, 0, 2, 0, -1),
						new StructureBlock(BlocksCore.reactorSupport, 0, -1, 0, 0),
						new StructureBlock(BlocksCore.air, 0, -1, 0, 1),
						new StructureBlock(BlocksCore.air, 0, -1, 0, -1),
						new StructureBlock(BlocksCore.air, 0, -2, 0, 0),
						new StructureBlock(BlocksCore.air, 0, -2, 0, 1),
						new StructureBlock(BlocksCore.air, 0, -2, 0, -1)
						)))
				.apendPage(next("reactor").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.reactorSupport,1,0), 2)))
				.apendPage(next("reactor").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.reactor,1,0), 2)))
				.apendPage(next("reactor").setText(I18n.translateToLocal("essentialcraft.page.reactor_8.txt")))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.soulStorage")
				.setReferal(new ItemStack(BlocksCore.chargingChamber,1,0),new ItemStack(ItemsCore.storage,1,0),new ItemStack(ItemsCore.storage,1,1),new ItemStack(ItemsCore.storage,1,2),new ItemStack(ItemsCore.storage,1,3),new ItemStack(ItemsCore.storage,1,4))
				.setDisplayStack(new ItemStack(ItemsCore.storage,1,4))
				.apendPage(next("soulStorage").setText(I18n.translateToLocal("essentialcraft.page.soulStorage_0.txt")))
				.apendPage(next("soulStorage").setText(I18n.translateToLocal("essentialcraft.page.soulStorage_1.txt")).setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.storage,1,0), 6)))
				.apendPage(next("soulStorage").setText(I18n.translateToLocal("essentialcraft.page.soulStorage_2.txt")).setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.storage,1,1), 6)))
				.apendPage(next("soulStorage").setText(I18n.translateToLocal("essentialcraft.page.soulStorage_3.txt")).setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.storage,1,2), 6)))
				.apendPage(next("soulStorage").setText(I18n.translateToLocal("essentialcraft.page.soulStorage_4.txt")).setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.storage,1,3), 6)))
				.apendPage(next("soulStorage").setText(I18n.translateToLocal("essentialcraft.page.soulStorage_5.txt")).setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.storage,1,4), 6)))
				.apendPage(next("soulStorage").setText(I18n.translateToLocal("essentialcraft.page.soulStorage_6.txt")).setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.chargingChamber,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.magicalDigger")

				.setReferal(new ItemStack(ItemsCore.magicalDigger,1,0))
				.setDisplayStack(new ItemStack(ItemsCore.magicalDigger,1,0))
				.apendPage(next("magicalDigger").setText(I18n.translateToLocal("essentialcraft.page.magicalDigger_0.txt")))
				.apendPage(next("magicalDigger").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.magicalDigger,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.magicalLantern")

				.setReferal(new ItemStack(ItemsCore.magicalLantern,1,0))
				.setDisplayStack(new ItemStack(ItemsCore.magicalLantern,1,0))
				.apendPage(next("magicalLantern").setText(I18n.translateToLocal("essentialcraft.page.magicalLantern_0.txt")))
				.apendPage(next("magicalLantern").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.magicalLantern,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.magnetizingStaff")

				.setReferal(new ItemStack(ItemsCore.magnetizingStaff,1,0))
				.setDisplayStack(new ItemStack(ItemsCore.magnetizingStaff,1,0))
				.apendPage(next("magnetizingStaff").setText(I18n.translateToLocal("essentialcraft.page.magnetizingStaff_0.txt")))
				.apendPage(next("magnetizingStaff").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.magnetizingStaff,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.spawnerCollector")

				.setReferal(new ItemStack(ItemsCore.spawnerCollector,1,0))
				.setDisplayStack(new ItemStack(ItemsCore.spawnerCollector,1,0))
				.apendPage(next("spawnerCollector").setText(I18n.translateToLocal("essentialcraft.page.spawnerCollector_0.txt")))
				.apendPage(next("spawnerCollector").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.spawnerCollector,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.lifeStaff")

				.setReferal(new ItemStack(ItemsCore.staffOfLife,1,0))
				.setDisplayStack(new ItemStack(ItemsCore.staffOfLife,1,0))
				.apendPage(next("lifeStaff").setText(I18n.translateToLocal("essentialcraft.page.lifeStaff_0.txt")))
				.apendPage(next("lifeStaff").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.staffOfLife,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.holyMace")

				.setReferal(new ItemStack(ItemsCore.holyMace,1,0))
				.setDisplayStack(new ItemStack(ItemsCore.holyMace,1,0))
				.apendPage(next("holyMace").setText(I18n.translateToLocal("essentialcraft.page.holyMace_0.txt")))
				.apendPage(next("holyMace").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.holyMace,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.emeraldHeart")

				.setReferal(new ItemStack(ItemsCore.emeraldHeart,1,0))
				.setDisplayStack(new ItemStack(ItemsCore.emeraldHeart,1,0))
				.apendPage(next("emeraldHeart").setText(I18n.translateToLocal("essentialcraft.page.emeraldHeart_0.txt")))
				.apendPage(next("emeraldHeart").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.emeraldHeart,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.magicalWings")

				.setReferal(new ItemStack(ItemsCore.magicalWings,1,0))
				.setDisplayStack(new ItemStack(ItemsCore.magicalWings,1,0))
				.apendPage(next("magicalWings").setText(I18n.translateToLocal("essentialcraft.page.magicalWings_0.txt")))
				.apendPage(next("magicalWings").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.magicalWings,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.magicalPorkchop")

				.setReferal(new ItemStack(ItemsCore.magicalPorkchop,1,0))
				.setDisplayStack(new ItemStack(ItemsCore.magicalPorkchop,1,0))
				.apendPage(next("magicalPorkchop").setText(I18n.translateToLocal("essentialcraft.page.magicalPorkchop_0.txt")))
				.apendPage(next("magicalPorkchop").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.magicalPorkchop,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.magicalWater")

				.setReferal(new ItemStack(ItemsCore.magicWaterBottle,1,0))
				.setDisplayStack(new ItemStack(ItemsCore.magicWaterBottle,1,0))
				.apendPage(next("magicalWater").setText(I18n.translateToLocal("essentialcraft.page.magicalWater_0.txt")))
				.apendPage(next("magicalWater").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.magicWaterBottle,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.magicalShield")

				.setReferal(new ItemStack(ItemsCore.magicalShield,1,0),new ItemStack(ItemsCore.spikyShield,1,0))
				.setDisplayStack(new ItemStack(ItemsCore.magicalShield,1,0))
				.apendPage(next("magicalShield").setText(I18n.translateToLocal("essentialcraft.page.magicalShield_0.txt")))
				.apendPage(next("magicalShield").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.magicalShield,1,0), 2)))
				.apendPage(next("magicalShield").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.spikyShield,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.biomeWand")

				.setReferal(new ItemStack(ItemsCore.biomeWand,1,0))
				.setDisplayStack(new ItemStack(ItemsCore.biomeWand,1,0))
				.apendPage(next("biomeWand").setText(I18n.translateToLocal("essentialcraft.page.biomeWand_0.txt")))
				.apendPage(next("biomeWand").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.biomeWand,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.chaosFork")

				.setReferal(new ItemStack(ItemsCore.chaosFork,1,0))
				.setDisplayStack(new ItemStack(ItemsCore.chaosFork,1,0))
				.apendPage(next("chaosFork").setText(I18n.translateToLocal("essentialcraft.page.chaosFork_0.txt")))
				.apendPage(next("chaosFork").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.chaosFork,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.frozenMace")
				.setReferal(new ItemStack(ItemsCore.frozenMace,1,0))
				.setDisplayStack(new ItemStack(ItemsCore.frozenMace,1,0))
				.apendPage(next("frozenMace").setText(I18n.translateToLocal("essentialcraft.page.frozenMace_0.txt")))
				.apendPage(next("frozenMace").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.frozenMace,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.magmaticStaff")
				.setReferal(new ItemStack(ItemsCore.magmaticStaff,1,0))
				.setDisplayStack(new ItemStack(ItemsCore.magmaticStaff,1,0))
				.apendPage(next("magmaticStaff").setText(I18n.translateToLocal("essentialcraft.page.magmaticStaff_0.txt")))
				.apendPage(next("magmaticStaff").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.magmaticStaff,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.mhs")
				.setReferal(new ItemStack(ItemsCore.magicArmorItems[0],1,0),new ItemStack(ItemsCore.magicArmorItems[1],1,0),new ItemStack(ItemsCore.magicArmorItems[2],1,0),new ItemStack(ItemsCore.magicArmorItems[3],1,0))
				.setDisplayStack(new ItemStack(ItemsCore.magicArmorItems[0],1,0))
				.apendPage(next("mhs").setText(I18n.translateToLocal("essentialcraft.page.mhs_0.txt")))
				.apendPage(next("mhs").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.magicArmorItems[0],1,0), 2)))
				.apendPage(next("mhs").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.magicArmorItems[1],1,0), 2)))
				.apendPage(next("mhs").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.magicArmorItems[2],1,0), 2)))
				.apendPage(next("mhs").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.magicArmorItems[3],1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.elementalCharms")
				.setReferal(new ItemStack(ItemsCore.charm,1,0),new ItemStack(ItemsCore.charm,1,1),new ItemStack(ItemsCore.charm,1,2),new ItemStack(ItemsCore.charm,1,3),new ItemStack(ItemsCore.charm,1,4),new ItemStack(ItemsCore.charm,1,5),new ItemStack(ItemsCore.charm,1,6),new ItemStack(ItemsCore.charm,1,7),new ItemStack(ItemsCore.charm,1,8),new ItemStack(ItemsCore.charm,1,9))
				.setDisplayStack(new ItemStack(ItemsCore.charm,1,7))
				.apendPage(next("elementalCharms").setText(I18n.translateToLocal("essentialcraft.page.elementalCharms_0.txt")))
				.apendPage(next("elementalCharms").setText(I18n.translateToLocal("essentialcraft.page.elementalCharms_1.txt")).setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.charm,1,0), 2)))
				.apendPage(next("elementalCharms").setText(I18n.translateToLocal("essentialcraft.page.elementalCharms_2.txt")).setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.charm,1,1), 2)))
				.apendPage(next("elementalCharms").setText(I18n.translateToLocal("essentialcraft.page.elementalCharms_3.txt")).setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.charm,1,2), 2)))
				.apendPage(next("elementalCharms").setText(I18n.translateToLocal("essentialcraft.page.elementalCharms_4.txt")).setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.charm,1,3), 2)))
				.apendPage(next("elementalCharms").setText(I18n.translateToLocal("essentialcraft.page.elementalCharms_5.txt")).setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.charm,1,4), 2)))
				.apendPage(next("elementalCharms").setText(I18n.translateToLocal("essentialcraft.page.elementalCharms_6.txt")).setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.charm,1,5), 2)))
				.apendPage(next("elementalCharms").setText(I18n.translateToLocal("essentialcraft.page.elementalCharms_7.txt")).setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.charm,1,6), 2)))
				.apendPage(next("elementalCharms").setText(I18n.translateToLocal("essentialcraft.page.elementalCharms_8.txt")).setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.charm,1,7), 2)))
				.apendPage(next("elementalCharms").setText(I18n.translateToLocal("essentialcraft.page.elementalCharms_9.txt")).setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.charm,1,8), 2)))
				.apendPage(next("elementalCharms").setText(I18n.translateToLocal("essentialcraft.page.elementalCharms_10.txt")).setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.charm,1,9), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.mruCoil")
				.setReferal(new ItemStack(ItemsCore.playerList,1,0),new ItemStack(BlocksCore.mruCoil,1,0),new ItemStack(BlocksCore.mruCoilHardener,1,0))
				.setDisplayStack(new ItemStack(BlocksCore.mruCoil,1,0))
				.apendPage(next("mruCoil").setText(I18n.translateToLocal("essentialcraft.page.mruCoil_0.txt")))
				.apendPage(next("mruCoil").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.mruCoil,1,0), 2)))
				.apendPage(next("mruCoil").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.mruCoilHardener,1,0), 2)))
				.apendPage(next("mruCoil").setRecipe(new StructureRecipe(new ItemStack(BlocksCore.mruCoil,1,0),
						new StructureBlock(BlocksCore.magicPlating, 0, 0, 0, 0),
						new StructureBlock(BlocksCore.magicPlating, 0, 0, 0, 1),
						new StructureBlock(BlocksCore.magicPlating, 0, 0, 0, -1),
						new StructureBlock(BlocksCore.magicPlating, 0, 1, 0, 0),
						new StructureBlock(BlocksCore.magicPlating, 0, 1, 0, 1),
						new StructureBlock(BlocksCore.magicPlating, 0, 1, 0, -1),
						new StructureBlock(BlocksCore.magicPlating, 0, -1, 0, 0),
						new StructureBlock(BlocksCore.magicPlating, 0, -1, 0, 1),
						new StructureBlock(BlocksCore.magicPlating, 0, -1, 0, -1),
						new StructureBlock(BlocksCore.magicPlating, 0, -2, 0, 0),
						new StructureBlock(BlocksCore.magicPlating, 0, 2, 0, 0),
						new StructureBlock(BlocksCore.magicPlating, 0, 0, 0, 2),
						new StructureBlock(BlocksCore.magicPlating, 0, 0, 0, -2),
						new StructureBlock(BlocksCore.magicPlating, 0, 0, 0, 3),
						new StructureBlock(BlocksCore.magicPlating, 0, 0, 0, -3),
						new StructureBlock(BlocksCore.magicPlating, 0, -3, 0, 0),
						new StructureBlock(BlocksCore.magicPlating, 0, 3, 0, 0),
						new StructureBlock(BlocksCore.magicPlating, 0, -3, 0, 1),
						new StructureBlock(BlocksCore.magicPlating, 0, 3, 0, 1),
						new StructureBlock(BlocksCore.magicPlating, 0, -3, 0, -1),
						new StructureBlock(BlocksCore.magicPlating, 0, 3, 0, -1),
						new StructureBlock(BlocksCore.magicPlating, 0, 1, 0, 3),
						new StructureBlock(BlocksCore.magicPlating, 0, 1, 0, -3),
						new StructureBlock(BlocksCore.magicPlating, 0, -1, 0, 3),
						new StructureBlock(BlocksCore.magicPlating, 0, -1, 0, -3)
						)))
				.apendPage(next("mruCoil").setText(I18n.translateToLocal("essentialcraft.page.mruCoil_4.txt")))
				.apendPage(next("mruCoil").setText(I18n.translateToLocal("essentialcraft.page.mruCoil_5.txt")))
				.apendPage(next("mruCoil").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.playerList,1,0), 2)))
				.apendPage(next("mruCoil").setText(I18n.translateToLocal("essentialcraft.page.mruCoil_7.txt")))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.potionSpreader")
				.setReferal(new ItemStack(BlocksCore.potionSpreader,1,0))
				.setDisplayStack(new ItemStack(BlocksCore.potionSpreader,1,0))
				.apendPage(next("potionSpreader").setText(I18n.translateToLocal("essentialcraft.page.potionSpreader_0.txt")))
				.apendPage(next("potionSpreader").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.potionSpreader,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.magicalEnchanter")
				.setReferal(new ItemStack(BlocksCore.magicalEnchanter,1,0))
				.setDisplayStack(new ItemStack(BlocksCore.magicalEnchanter,1,0))
				.apendPage(next("magicalEnchanter").setText(I18n.translateToLocal("essentialcraft.page.magicalEnchanter_0.txt")))
				.apendPage(next("magicalEnchanter").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.magicalEnchanter,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.monsterHolder")
				.setReferal(new ItemStack(BlocksCore.monsterClinger,1,0))
				.setDisplayStack(new ItemStack(BlocksCore.monsterClinger,1,0))
				.apendPage(next("monsterHolder").setText(I18n.translateToLocal("essentialcraft.page.monsterHolder_0.txt")))
				.apendPage(next("monsterHolder").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.monsterClinger,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.magicalJukebox")
				.setReferal(new ItemStack(BlocksCore.magicalJukebox,1,0))
				.setDisplayStack(new ItemStack(BlocksCore.magicalJukebox,1,0))
				.apendPage(next("magicalJukebox").setText(I18n.translateToLocal("essentialcraft.page.magicalJukebox_0.txt")))
				.apendPage(next("magicalJukebox").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.magicalJukebox,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.magicalRepairer")
				.setReferal(new ItemStack(BlocksCore.magicalRepairer,1,0))
				.setDisplayStack(new ItemStack(BlocksCore.magicalRepairer,1,0))
				.apendPage(next("magicalRepairer").setText(I18n.translateToLocal("essentialcraft.page.magicalRepairer_0.txt")))
				.apendPage(next("magicalRepairer").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.magicalRepairer,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.monsterDuplicator")
				.setReferal(new ItemStack(BlocksCore.monsterHarvester,1,0))
				.setDisplayStack(new ItemStack(BlocksCore.monsterHarvester,1,0))
				.apendPage(next("monsterDuplicator").setText(I18n.translateToLocal("essentialcraft.page.monsterDuplicator_0.txt")))
				.apendPage(next("monsterDuplicator").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.monsterHarvester,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.magmaticSmeltery")
				.setReferal(new ItemStack(BlocksCore.magmaticSmeltery,1,0))
				.setDisplayStack(new ItemStack(BlocksCore.magmaticSmeltery,1,0))
				.apendPage(next("magmaticSmeltery").setText(I18n.translateToLocal("essentialcraft.page.magmaticSmeltery_0.txt")))
				.apendPage(next("magmaticSmeltery").setImg(new ResourceLocation("essentialcraft","textures/special/bookicons/smeltery.png")))
				.apendPage(next("magmaticSmeltery").setText(I18n.translateToLocal("essentialcraft.page.magmaticSmeltery_2.txt")))
				.apendPage(next("magmaticSmeltery").setText(I18n.translateToLocal("essentialcraft.page.magmaticSmeltery_3.txt")))
				.apendPage(next("magmaticSmeltery").setText(I18n.translateToLocal("essentialcraft.page.magmaticSmeltery_4.txt")))
				.apendPage(next("magmaticSmeltery").setText(I18n.translateToLocal("essentialcraft.page.magmaticSmeltery_5.txt")))
				.apendPage(next("magmaticSmeltery").setText(I18n.translateToLocal("essentialcraft.page.magmaticSmeltery_6.txt")))
				.apendPage(next("magmaticSmeltery").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.magmaticSmeltery,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.magicalQuarry")
				.setReferal(new ItemStack(BlocksCore.magicalQuarry,1,0),generic(18),generic(19),generic(17),generic(80),generic(81),generic(78),generic(77))
				.setDisplayStack(new ItemStack(BlocksCore.magicalQuarry,1,0))
				.apendPage(next("magicalQuarry").setText(I18n.translateToLocal("essentialcraft.page.magicalQuarry_0.txt")))
				.apendPage(next("magicalQuarry").setText(I18n.translateToLocal("essentialcraft.page.magicalQuarry_1.txt")))
				.apendPage(next("magicalQuarry").setText(I18n.translateToLocal("essentialcraft.page.magicalQuarry_2.txt")))
				.apendPage(next("magicalQuarry").setText(I18n.translateToLocal("essentialcraft.page.magicalQuarry_3.txt")))
				.apendPage(next("magicalQuarry").setText(I18n.translateToLocal("essentialcraft.page.magicalQuarry_4.txt")).setRecipe(ECUtils.findRecipeByIS(generic(18), 2)))
				.apendPage(next("magicalQuarry").setText(I18n.translateToLocal("essentialcraft.page.magicalQuarry_5.txt")).setRecipe(ECUtils.findRecipeByIS(generic(19), 2)))
				.apendPage(next("magicalQuarry").setText(I18n.translateToLocal("essentialcraft.page.magicalQuarry_6.txt")).setRecipe(ECUtils.findRecipeByIS(generic(17), 2)))
				.apendPage(next("magicalQuarry").setText(I18n.translateToLocal("essentialcraft.page.magicalQuarry_7.txt")).setRecipe(ECUtils.findRecipeByIS(generic(80), 2)))
				.apendPage(next("magicalQuarry").setText(I18n.translateToLocal("essentialcraft.page.magicalQuarry_8.txt")).setRecipe(ECUtils.findRecipeByIS(generic(81), 2)))
				.apendPage(next("magicalQuarry").setText(I18n.translateToLocal("essentialcraft.page.magicalQuarry_9.txt")).setRecipe(ECUtils.findRecipeByIS(generic(78), 2)))
				.apendPage(next("magicalQuarry").setText(I18n.translateToLocal("essentialcraft.page.magicalQuarry_10.txt")).setRecipe(ECUtils.findRecipeByIS(generic(77), 2)))
				.apendPage(next("magicalQuarry").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.magicalQuarry,1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.hoanna")
				.setReferal(new ItemStack(ItemsCore.windTablet,1,0))
				.setDisplayStack(new ItemStack(ItemsCore.windTablet,1,0))
				.apendPage(next("hoanna").setText(I18n.translateToLocal("essentialcraft.page.hoanna_0.txt")))
				.apendPage(next("hoanna").setText(I18n.translateToLocal("essentialcraft.page.hoanna_1.txt")))
				.apendPage(next("hoanna").setText(I18n.translateToLocal("essentialcraft.page.hoanna_2.txt")))
				.apendPage(next("hoanna").setText(I18n.translateToLocal("essentialcraft.page.hoanna_3.txt")))
				.apendPage(next("hoanna").setText(I18n.translateToLocal("essentialcraft.page.hoanna_4.txt")).setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.windTablet,1,0), 2)))
				.apendPage(next("hoanna").setText(I18n.translateToLocal("essentialcraft.page.hoanna_5.txt")))
				.apendPage(next("hoanna").setText(I18n.translateToLocal("essentialcraft.page.hoanna_6.txt")))
				.apendPage(next("hoanna").setText(I18n.translateToLocal("essentialcraft.page.hoanna_7.txt")))
				.apendPage(next("hoanna").setText(I18n.translateToLocal("essentialcraft.page.hoanna_8.txt")))
				.apendPage(next("hoanna").setText(I18n.translateToLocal("essentialcraft.page.hoanna_9.txt")))
				.apendPage(next("hoanna").setRecipe(ECUtils.findRecipeByIS(book_t3, 2)))
				)
		;
	}

	public static void registerHoannaCategory()
	{
		ItemStack lootable = new ItemStack(ItemsCore.bauble,1,0);
		ItemBaublesResistance.initRandomTag(lootable, new Random());
		hoanna
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.radiation")
				.setDisplayStack(new ResourceLocation("essentialcraft","textures/special/radiation_icon.png"))
				.apendPage(next("radiation").setText(I18n.translateToLocal("essentialcraft.page.radiation_0.txt")))
				.apendPage(next("radiation").setText(I18n.translateToLocal("essentialcraft.page.radiation_1.txt")))
				.apendPage(next("radiation").setText(I18n.translateToLocal("essentialcraft.page.radiation_2.txt")))
				.apendPage(next("radiation").setText(I18n.translateToLocal("essentialcraft.page.radiation_3.txt")))
				.apendPage(next("radiation").setText(I18n.translateToLocal("essentialcraft.page.radiation_4.txt")))
				.apendPage(next("radiation").setText(I18n.translateToLocal("essentialcraft.page.radiation_5.txt")))
				.apendPage(next("radiation").setText(I18n.translateToLocal("essentialcraft.page.radiation_6.txt")))
				.apendPage(next("radiation").setText(I18n.translateToLocal("essentialcraft.page.radiation_7.txt")))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.loot")
				.setDisplayStack(generic(37))
				.setReferal(generic(35),generic(36),generic(37),new ItemStack(ItemsCore.titanite,1,0),new ItemStack(ItemsCore.twinkling_titanite,1,0),new ItemStack(ItemsCore.ember,1,0),new ItemStack(ItemsCore.ember,1,1),new ItemStack(ItemsCore.ember,1,2),new ItemStack(ItemsCore.ember,1,3),new ItemStack(ItemsCore.ember,1,4),new ItemStack(ItemsCore.ember,1,5),new ItemStack(ItemsCore.ember,1,6),new ItemStack(ItemsCore.ember,1,7))
				.apendPage(next("loot").setText(I18n.translateToLocal("essentialcraft.page.loot_0.txt")))
				.apendPage(next("loot").setDisplayStacks(generic(35),generic(36),generic(37),new ItemStack(ItemsCore.titanite,1,0),new ItemStack(ItemsCore.twinkling_titanite,1,0),new ItemStack(ItemsCore.ember,1,0),new ItemStack(ItemsCore.ember,1,1),new ItemStack(ItemsCore.ember,1,2),new ItemStack(ItemsCore.ember,1,3),new ItemStack(ItemsCore.ember,1,4),new ItemStack(ItemsCore.ember,1,5),new ItemStack(ItemsCore.ember,1,6),new ItemStack(ItemsCore.ember,1,7),lootable))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.voidStone")
				.setDisplayStack(new ItemStack(BlocksCore.voidStone,1,0))
				.setReferal(new ItemStack(BlocksCore.voidStone,1,0),new ItemStack(BlocksCore.voidGlass,1,0))
				.apendPage(next("voidStone").setText(I18n.translateToLocal("essentialcraft.page.voidStone_0.txt")))
				.apendPage(next("voidStone").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.voidStone), 2)))
				.apendPage(next("voidStone").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.voidGlass), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.voidArmor")
				.setReferal(new ItemStack(ItemsCore.magicArmorItems[4],1,0),new ItemStack(ItemsCore.magicArmorItems[5],1,0),new ItemStack(ItemsCore.magicArmorItems[6],1,0),new ItemStack(ItemsCore.magicArmorItems[7],1,0))
				.setDisplayStack(new ItemStack(ItemsCore.magicArmorItems[5],1,0))
				.apendPage(next("voidArmor").setText(I18n.translateToLocal("essentialcraft.page.voidArmor_0.txt")))
				.apendPage(next("voidArmor").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.magicArmorItems[4],1,0), 2)))
				.apendPage(next("voidArmor").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.magicArmorItems[5],1,0), 2)))
				.apendPage(next("voidArmor").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.magicArmorItems[6],1,0), 2)))
				.apendPage(next("voidArmor").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.magicArmorItems[7],1,0), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.magicalTeleporter")
				.setReferal(new ItemStack(BlocksCore.magicalTeleporter))
				.setDisplayStack(new ItemStack(BlocksCore.magicalTeleporter))
				.apendPage(next("magicalTeleporter").setText(I18n.translateToLocal("essentialcraft.page.magicalTeleporter_0.txt")))
				.apendPage(next("magicalTeleporter").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.magicalTeleporter), 2)))
				.apendPage(next("magicalTeleporter").setText(I18n.translateToLocal("essentialcraft.page.magicalTeleporter_2.txt")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.magicalTeleporter),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, 2),
						new StructureBlock(BlocksCore.voidStone, 0, -1, 0, 2),
						new StructureBlock(BlocksCore.magicPlating, 0, 0, 0, 2),
						new StructureBlock(BlocksCore.voidStone, 0, 1, 0, 2),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, 2),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, 1),
						new StructureBlock(BlocksCore.voidStone, 0, -1, 0, 1),
						new StructureBlock(BlocksCore.voidStone, 0, 0, 0, 1),
						new StructureBlock(BlocksCore.voidStone, 0, 1, 0, 1),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, 1),
						new StructureBlock(BlocksCore.magicPlating, 0, -2, 0, 0),
						new StructureBlock(BlocksCore.voidStone, 0, -1, 0, 0),
						new StructureBlock(BlocksCore.magicalTeleporter, 0, 0, 0, 0),
						new StructureBlock(BlocksCore.voidStone, 0, 1, 0, 0),
						new StructureBlock(BlocksCore.magicPlating, 0, 2, 0, 0),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, -1),
						new StructureBlock(BlocksCore.voidStone, 0, -1, 0, -1),
						new StructureBlock(BlocksCore.voidStone, 0, 0, 0, -1),
						new StructureBlock(BlocksCore.voidStone, 0, 1, 0, -1),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, -1),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, -2),
						new StructureBlock(BlocksCore.voidStone, 0, -1, 0, -2),
						new StructureBlock(BlocksCore.magicPlating, 0, 0, 0, -2),
						new StructureBlock(BlocksCore.voidStone, 0, 1, 0, -2),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, -2)
						)
						))
				.apendPage(next("magicalTeleporter").setText(I18n.translateToLocal("essentialcraft.page.magicalTeleporter_3.txt")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.magicalTeleporter),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, 1),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, -1),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, 1),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, -1),
						new StructureBlock(BlocksCore.voidStone, 0, 1, 0, 2),
						new StructureBlock(BlocksCore.voidStone, 0, -1, 0, 2),
						new StructureBlock(BlocksCore.voidStone, 0, 1, 0, -2),
						new StructureBlock(BlocksCore.voidStone, 0, -1, 0, -2)
						)
						))
				.apendPage(next("magicalTeleporter").setText(I18n.translateToLocal("essentialcraft.page.magicalTeleporter_4.txt")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.magicalTeleporter),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, 1),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, -1),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, 1),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, -1),
						new StructureBlock(BlocksCore.voidStone, 0, 1, 0, 2),
						new StructureBlock(BlocksCore.voidStone, 0, -1, 0, 2),
						new StructureBlock(BlocksCore.voidStone, 0, 1, 0, -2),
						new StructureBlock(BlocksCore.voidStone, 0, -1, 0, -2)
						)
						))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.magicalFurnace")
				.setReferal(new ItemStack(BlocksCore.magicalFurnace))
				.setDisplayStack(new ItemStack(BlocksCore.magicalFurnace))
				.apendPage(next("magicalFurnace").setText(I18n.translateToLocal("essentialcraft.page.magicalFurnace_0.txt")))
				.apendPage(next("magicalFurnace").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.magicalFurnace), 2)))
				.apendPage(next("magicalFurnace").setText(I18n.translateToLocal("essentialcraft.page.magicalFurnace_2.txt")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.magicalFurnace),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, 2),
						new StructureBlock(BlocksCore.voidStone, 0, -1, 0, 2),
						new StructureBlock(BlocksCore.voidStone, 0, 0, 0, 2),
						new StructureBlock(BlocksCore.voidStone, 0, 1, 0, 2),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, 2),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, 1),
						new StructureBlock(BlocksCore.voidStone, 0, -1, 0, 1),
						new StructureBlock(BlocksCore.voidStone, 0, 0, 0, 1),
						new StructureBlock(BlocksCore.voidStone, 0, 1, 0, 1),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, 1),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, 0),
						new StructureBlock(BlocksCore.voidStone, 0, -1, 0, 0),
						new StructureBlock(BlocksCore.voidStone, 0, 0, 0, 0),
						new StructureBlock(BlocksCore.voidStone, 0, 1, 0, 0),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, 0),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, -1),
						new StructureBlock(BlocksCore.voidStone, 0, -1, 0, -1),
						new StructureBlock(BlocksCore.voidStone, 0, 0, 0, -1),
						new StructureBlock(BlocksCore.voidStone, 0, 1, 0, -1),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, -1),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, -2),
						new StructureBlock(BlocksCore.voidStone, 0, -1, 0, -2),
						new StructureBlock(BlocksCore.voidStone, 0, 0, 0, -2),
						new StructureBlock(BlocksCore.voidStone, 0, 1, 0, -2),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, -2)
						)
						))
				.apendPage(next("magicalFurnace").setText(I18n.translateToLocal("essentialcraft.page.magicalFurnace_3.txt")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.magicalFurnace),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, -2),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, 2),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, -2),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, 2),
						new StructureBlock(BlocksCore.magicalFurnace, 0, 0, 0, 0)
						)
						))
				.apendPage(next("magicalFurnace").setText(I18n.translateToLocal("essentialcraft.page.magicalFurnace_4.txt")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.magicalFurnace),
						new StructureBlock(BlocksCore.heatGenerator, 0, -2, 0, -2),
						new StructureBlock(BlocksCore.heatGenerator, 0, -2, 0, 2),
						new StructureBlock(BlocksCore.heatGenerator, 0, 2, 0, -2),
						new StructureBlock(BlocksCore.heatGenerator, 0, 2, 0, 2)
						)
						))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.emberForge")
				.setReferal(new ItemStack(BlocksCore.emberForge))
				.setDisplayStack(new ItemStack(BlocksCore.emberForge))
				.apendPage(next("emberForge").setText(I18n.translateToLocal("essentialcraft.page.emberForge_0.txt")))
				.apendPage(next("emberForge").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.emberForge), 2)))
				.apendPage(next("emberForge").setText(I18n.translateToLocal("essentialcraft.page.emberForge_2.txt")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.emberForge),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, 2),
						new StructureBlock(BlocksCore.voidStone, 0, -1, 0, 2),
						new StructureBlock(BlocksCore.voidStone, 0, 0, 0, 2),
						new StructureBlock(BlocksCore.voidStone, 0, 1, 0, 2),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, 2),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, 1),
						new StructureBlock(BlocksCore.voidStone, 0, -1, 0, 1),
						new StructureBlock(BlocksCore.voidStone, 0, 0, 0, 1),
						new StructureBlock(BlocksCore.voidStone, 0, 1, 0, 1),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, 1),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, 0),
						new StructureBlock(BlocksCore.voidStone, 0, -1, 0, 0),
						new StructureBlock(BlocksCore.voidStone, 0, 0, 0, 0),
						new StructureBlock(BlocksCore.voidStone, 0, 1, 0, 0),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, 0),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, -1),
						new StructureBlock(BlocksCore.voidStone, 0, -1, 0, -1),
						new StructureBlock(BlocksCore.voidStone, 0, 0, 0, -1),
						new StructureBlock(BlocksCore.voidStone, 0, 1, 0, -1),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, -1),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, -2),
						new StructureBlock(BlocksCore.voidStone, 0, -1, 0, -2),
						new StructureBlock(BlocksCore.voidStone, 0, 0, 0, -2),
						new StructureBlock(BlocksCore.voidStone, 0, 1, 0, -2),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, -2)
						)
						))
				.apendPage(next("emberForge").setText(I18n.translateToLocal("essentialcraft.page.emberForge_3.txt")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.emberForge),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, -2),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, 2),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, -2),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, 2),
						new StructureBlock(BlocksCore.platingPale, 0, 0, 0, -2),
						new StructureBlock(BlocksCore.platingPale, 0, 0, 0, 2),
						new StructureBlock(BlocksCore.platingPale, 0, 2, 0, 0),
						new StructureBlock(BlocksCore.platingPale, 0, -2, 0, 0),
						new StructureBlock(BlocksCore.emberForge, 0, 0, 0, 0)
						)
						))
				.apendPage(next("emberForge").setText(I18n.translateToLocal("essentialcraft.page.emberForge_4.txt")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.emberForge),
						new StructureBlock(BlocksCore.magicPlating, 0, -2, 0, -2),
						new StructureBlock(BlocksCore.magicPlating, 0, -2, 0, 2),
						new StructureBlock(BlocksCore.magicPlating, 0, 2, 0, -2),
						new StructureBlock(BlocksCore.magicPlating, 0, 2, 0, 2)
						)
						))
				)

		/*
			.apendDiscovery(
					new DiscoveryEntry("essentialcraft.disc.mim")

					.setDisplayStack(new ItemStack(BlocksCore.mim,1,0))
					.setReferal(new ItemStack(BlocksCore.mim,1,0),new ItemStack(BlocksCore.minEjector,1,0),new ItemStack(BlocksCore.minInjector,1,0),new ItemStack(ItemsCore.filter,1,0))
					.apendPage(next("mim").setText(I18n.translateToLocal("essentialcraft.page.mim_0.txt")))
					.apendPage(next("mim").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.filter,1,0), 2)))
					.apendPage(next("mim").setText(I18n.translateToLocal("essentialcraft.page.mim_2.txt")))
					.apendPage(next("mim").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.filter,1,1), 2)))
					.apendPage(next("mim").setText(I18n.translateToLocal("essentialcraft.page.mim_4.txt")))
					.apendPage(next("mim").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.minEjector,1,0), 2)))
					.apendPage(next("mim").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.minEjector,1,6), 2)))
					.apendPage(next("mim").setText(I18n.translateToLocal("essentialcraft.page.mim_7.txt")))
					.apendPage(next("mim").setText(I18n.translateToLocal("essentialcraft.page.mim_8.txt")))
					.apendPage(next("mim").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.minInjector,1,0), 2)))
					.apendPage(next("mim").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.minInjector,1,6), 2)))
					.apendPage(next("mim").setText(I18n.translateToLocal("essentialcraft.page.mim_11.txt")))
					.apendPage(next("mim").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.mim,1,0), 2)))
					.apendPage(next("mim").setText(I18n.translateToLocal("essentialcraft.page.mim_13.txt")))
			)
		 */
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.o8elisk")
				.setDisplayStack(new ItemStack(BlocksCore.darknessObelisk,1,0))
				.setReferal(new ItemStack(BlocksCore.darknessObelisk,1,0))

				.apendPage(next("o8elisk").setText(I18n.translateToLocal("essentialcraft.page.o8elisk_0.txt")))
				.apendPage(next("o8elisk").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.darknessObelisk), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.ultraHeat")
				.setDisplayStack(new ItemStack(BlocksCore.ultraHeatGen,1,0))
				.setReferal(new ItemStack(BlocksCore.ultraHeatGen,1,0))

				.apendPage(next("ultraHeat").setText(I18n.translateToLocal("essentialcraft.page.ultraHeat_0.txt")))
				.apendPage(next("ultraHeat").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.ultraHeatGen), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.ultraFlower")
				.setDisplayStack(new ItemStack(BlocksCore.ultraFlowerBurner,1,0))
				.setReferal(new ItemStack(BlocksCore.ultraFlowerBurner,1,0))

				.apendPage(next("ultraFlower").setText(I18n.translateToLocal("essentialcraft.page.ultraFlower_0.txt")))
				.apendPage(next("ultraFlower").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.ultraFlowerBurner), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.magicalBuilder")
				.setDisplayStack(new ItemStack(ItemsCore.magicalBuilder,1,0))
				.setReferal(new ItemStack(ItemsCore.magicalBuilder,1,0))

				.apendPage(next("magicalBuilder").setText(I18n.translateToLocal("essentialcraft.page.magicalBuilder_0.txt")))
				.apendPage(next("magicalBuilder").setText(I18n.translateToLocal("essentialcraft.page.magicalBuilder_1.txt")))
				.apendPage(next("magicalBuilder").setText(I18n.translateToLocal("essentialcraft.page.magicalBuilder_2.txt")))
				.apendPage(next("magicalBuilder").setText(I18n.translateToLocal("essentialcraft.page.magicalBuilder_3.txt")))
				.apendPage(next("magicalBuilder").setText(I18n.translateToLocal("essentialcraft.page.magicalBuilder_4.txt")))
				.apendPage(next("magicalBuilder").setText(I18n.translateToLocal("essentialcraft.page.magicalBuilder_5.txt")))
				.apendPage(next("magicalBuilder").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.magicalBuilder), 2)))
				)
		.apendDiscovery(new DiscoveryEntry("hologram")

				.setDisplayStack(new ItemStack(ItemsCore.genericItem,1,76))
				.setReferal(new ItemStack(ItemsCore.genericItem,1,70),new ItemStack(ItemsCore.genericItem,1,71),new ItemStack(ItemsCore.genericItem,1,72),new ItemStack(ItemsCore.genericItem,1,73),new ItemStack(BlocksCore.holopad,1,0))
				.apendPage(next("hologram").setText(I18n.translateToLocal("essentialcraft.page.hologram_0.txt")))
				.apendPage(next("hologram").setText(I18n.translateToLocal("essentialcraft.page.hologram_1.txt")).setDisplayStacks(new ItemStack(ItemsCore.genericItem,1,70),new ItemStack(ItemsCore.genericItem,1,71),new ItemStack(ItemsCore.genericItem,1,72),new ItemStack(ItemsCore.genericItem,1,73)))
				.apendPage(next("hologram").setText(I18n.translateToLocal("essentialcraft.page.hologram_2.txt")))
				.apendPage(next("hologram").setText(I18n.translateToLocal("essentialcraft.page.hologram_3.txt")).setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.holopad), 2)))
				)
		.apendDiscovery(new DiscoveryEntry("redSuit")

				.setDisplayStack(new ItemStack(ItemsCore.computer_helmet,1,0))
				.setReferal(new ItemStack(ItemsCore.computer_helmet,1,0),new ItemStack(ItemsCore.computer_chestplate,1,0),new ItemStack(ItemsCore.computer_leggings,1,0),new ItemStack(ItemsCore.computer_boots,1,0),new ItemStack(ItemsCore.computerBoard,1,0),gen(56),gen(57),gen(58),gen(59),gen(60),gen(61),gen(62),gen(63),gen(64),gen(65),gen(66),gen(67),gen(68),gen(69),gen(74),gen(75))
				.apendPage(next("redSuit").setText(I18n.translateToLocal("essentialcraft.page.redSuit_0.txt")))
				.apendPage(next("redSuit").setText(I18n.translateToLocal("essentialcraft.page.redSuit_1.txt")))
				.apendPage(next("redSuit").setText(I18n.translateToLocal("essentialcraft.page.redSuit_2.txt")))
				.apendPage(next("redSuit").setText(I18n.translateToLocal("essentialcraft.page.redSuit_3.txt")))
				.apendPage(next("redSuit").setText(I18n.translateToLocal("essentialcraft.page.redSuit_4.txt")))
				.apendPage(next("redSuit").setText(I18n.translateToLocal("essentialcraft.page.redSuit_5.txt")))
				.apendPage(next("redSuit").setText(I18n.translateToLocal("essentialcraft.page.redSuit_6.txt")))
				.apendPage(next("redSuit").setText(I18n.translateToLocal("essentialcraft.page.redSuit_7.txt")))

				.apendPage(next("redSuit").setRecipe(ECUtils.findRecipeByIS(gen(54), 6)))
				.apendPage(next("redSuit").setRecipe(ECUtils.findRecipeByIS(gen(53), 2)))
				.apendPage(next("redSuit").setRecipe(ECUtils.findRecipeByIS(gen(59), 2)))
				.apendPage(next("redSuit").setRecipe(ECUtils.findRecipeByIS(gen(74), 2)))
				.apendPage(next("redSuit").setRecipe(ECUtils.findRecipeByIS(gen(68), 2)))
				.apendPage(next("redSuit").setRecipe(ECUtils.findRecipeByIS(gen(69), 2)))

				.apendPage(next("redSuit").setRecipe(ECUtils.findRecipeByIS(gen(56), 2)))
				.apendPage(next("redSuit").setRecipe(ECUtils.findRecipeByIS(gen(60), 2)))
				.apendPage(next("redSuit").setRecipe(ECUtils.findRecipeByIS(gen(63), 2)))

				.apendPage(next("redSuit").setRecipe(ECUtils.findRecipeByIS(gen(65), 2)))
				.apendPage(next("redSuit").setRecipe(ECUtils.findRecipeByIS(gen(67), 2)))
				.apendPage(next("redSuit").setRecipe(ECUtils.findRecipeByIS(gen(66), 2)))
				.apendPage(next("redSuit").setRecipe(ECUtils.findRecipeByIS(gen(58), 2)))

				.apendPage(next("redSuit").setRecipe(ECUtils.findRecipeByIS(gen(75), 2)))
				.apendPage(next("redSuit").setRecipe(ECUtils.findRecipeByIS(gen(57), 2)))
				.apendPage(next("redSuit").setRecipe(ECUtils.findRecipeByIS(gen(64), 2)))
				.apendPage(next("redSuit").setRecipe(ECUtils.findRecipeByIS(gen(61), 2)))
				.apendPage(next("redSuit").setRecipe(ECUtils.findRecipeByIS(gen(62), 2)))

				.apendPage(next("redSuit").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.computer_helmet), 2)))
				.apendPage(next("redSuit").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.computer_chestplate), 2)))
				.apendPage(next("redSuit").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.computer_leggings), 2)))
				.apendPage(next("redSuit").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.computer_boots), 2)))
				.apendPage(next("redSuit").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.computerBoard), 2)))

				.apendPage(next("redSuit").setText(I18n.translateToLocal("essentialcraft.page.redSuit_?.txt")))
				)
		.apendDiscovery(
				new DiscoveryEntry("essentialcraft.disc.newMim")
				.setDisplayStack(new ItemStack(BlocksCore.newMim,1,0))
				.setReferal(new ItemStack(BlocksCore.newMim,1,0),new ItemStack(BlocksCore.mimEjector,1,0),new ItemStack(BlocksCore.mimInjector,1,0),new ItemStack(BlocksCore.mimEjectorP,1,0),new ItemStack(BlocksCore.mimInjectorP,1,0))
				.apendPage(next("newMim").setText(I18n.translateToLocal("essentialcraft.page.newMim_0.txt")))
				.apendPage(next("newMim").setText(I18n.translateToLocal("essentialcraft.page.newMim_1.txt")))
				.apendPage(next("newMim").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.newMim), 2)))
				.apendPage(next("newMim").setText(I18n.translateToLocal("essentialcraft.page.newMim_2.txt")))
				.apendPage(next("newMim").setText(I18n.translateToLocal("essentialcraft.page.newMim_3.txt")))
				.apendPage(next("newMim").setText(I18n.translateToLocal("essentialcraft.page.newMim_4.txt")))
				.apendPage(next("newMim").setText(I18n.translateToLocal("essentialcraft.page.newMim_5.txt")))
				.apendPage(next("newMim").setText(I18n.translateToLocal("essentialcraft.page.newMim_6.txt")))
				.apendPage(next("newMim").setText(I18n.translateToLocal("essentialcraft.page.newMim_7.txt")))
				.apendPage(next("newMim").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.mimEjector), 2)))
				.apendPage(next("newMim").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.mimInjector), 2)))
				.apendPage(next("newMim").setText(I18n.translateToLocal("essentialcraft.page.newMim_8.txt")))
				.apendPage(next("newMim").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.mimEjectorP), 2)))
				.apendPage(next("newMim").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.mimInjectorP), 2)))
				)
		.apendDiscovery(new DiscoveryEntry("newMimInv")
				.setDisplayStack(new ItemStack(BlocksCore.mimInvStorage))
				.setReferal(new ItemStack(BlocksCore.mimInvStorage),new ItemStack(ItemsCore.inventoryGem))
				.apendPage(next("newMimInv").setText(I18n.translateToLocal("essentialcraft.page.newMimInv_0.txt")))
				.apendPage(next("newMimInv").setText(I18n.translateToLocal("essentialcraft.page.newMimInv_1.txt")))
				.apendPage(next("newMimInv").setText(I18n.translateToLocal("essentialcraft.page.newMimInv_2.txt")))
				.apendPage(next("newMimInv").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.mimInvStorage), 2)))
				.apendPage(next("newMimInv").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.inventoryGem), 2)))
				)
		.apendDiscovery(new DiscoveryEntry("newMimCraft")
				.setDisplayStack(new ItemStack(BlocksCore.mimCrafter))
				.setReferal(new ItemStack(BlocksCore.mimCrafter))
				.apendPage(next("newMimCraft").setText(I18n.translateToLocal("essentialcraft.page.newMimCraft_0.txt")))
				.apendPage(next("newMimCraft").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.mimCrafter), 2)))
				)
		.apendDiscovery(new DiscoveryEntry("newMimScreen")
				.setDisplayStack(new ItemStack(BlocksCore.mimScreen))
				.setReferal(new ItemStack(BlocksCore.mimScreen))
				.apendPage(next("newMimScreen").setText(I18n.translateToLocal("essentialcraft.page.newMimScreen_0.txt")))
				.apendPage(next("newMimScreen").setText(I18n.translateToLocal("essentialcraft.page.newMimScreen_1.txt")))
				.apendPage(next("newMimScreen").setText(I18n.translateToLocal("essentialcraft.page.newMimScreen_2.txt")))
				.apendPage(next("newMimScreen").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.mimScreen), 2)))
				)
		;
	}

	public static ItemStack generic(int meta)
	{
		return new ItemStack(ItemsCore.genericItem,1,meta);
	}

	public static ItemStack gen(int meta)
	{
		return new ItemStack(ItemsCore.genericItem,1,meta);
	}

	public static void registerEnderCategory()
	{
		ender
		.apendDiscovery(
				new DiscoveryEntry("enderstar")

				.setDisplayStack(new ItemStack(ItemsCore.genericItem,1,48))
				.apendPage(next("enderstar").setText(I18n.translateToLocal("essentialcraft.page.enderstar_0.txt")))
				.apendPage(next("enderstar").setText(I18n.translateToLocal("essentialcraft.page.enderstar_1.txt")))
				.apendPage(next("enderstar").setText(I18n.translateToLocal("essentialcraft.page.enderstar_2.txt")))
				)
		.apendDiscovery(
				new DiscoveryEntry("mithriline")

				.setDisplayStack(new ItemStack(ItemsCore.genericItem,1,51))
				.setReferal(new ItemStack(ItemsCore.genericItem,1,51),new ItemStack(ItemsCore.genericItem,1,50),new ItemStack(ItemsCore.genericItem,1,49),new ItemStack(ItemsCore.genericItem,1,48),new ItemStack(BlocksCore.invertedBlock,1,0))
				.apendPage(next("mithriline").setText(I18n.translateToLocal("essentialcraft.page.mithriline_0.txt")))
				.apendPage(next("mithriline").setText(I18n.translateToLocal("essentialcraft.page.mithriline_1.txt")).setDisplayStacks(new ItemStack(BlocksCore.oreMithriline,1,0),new ItemStack(ItemsCore.genericItem,1,51)))
				.apendPage(next("mithriline").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.genericItem,1,50), 2)))
				.apendPage(next("mithriline").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.genericItem,1,49), 6)))
				.apendPage(next("mithriline").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.genericItem,1,48), 2)))
				.apendPage(next("mithriline").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.genericItem,1,50), 2)))
				.apendPage(next("mithriline").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.invertedBlock,1,0), 2)).setText(I18n.translateToLocal("essentialcraft.page.mithriline_2.txt")))
				.apendPage(next("mithriline").setText(I18n.translateToLocal("essentialcraft.page.mithriline_3.txt")))
				.apendPage(next("mithriline").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.mithrilineCrystal,1,0), 2)))
				.apendPage(next("mithriline").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.mithrilineCrystal,1,3), 2)))
				.apendPage(next("mithriline").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.mithrilineCrystal,1,6), 2)))
				.apendPage(next("mithriline").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.mithrilineCrystal,1,9), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("mithrilineFurnace")

				.setDisplayStack(new ItemStack(BlocksCore.mithrilineFurnace))
				.setReferal(new ItemStack(BlocksCore.mithrilineFurnace))
				.apendPage(next("mithrilineFurnace").setText(I18n.translateToLocal("essentialcraft.page.mithrilineFurnace_0.txt")))
				.apendPage(next("mithrilineFurnace").setText(I18n.translateToLocal("essentialcraft.page.mithrilineFurnace_1.txt")))
				.apendPage(next("mithrilineFurnace").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.mithrilineFurnace,1,0), 2)))
				.apendPage(next("mithrilineFurnace").setText(I18n.translateToLocal("essentialcraft.txt.layer_-1")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.mithrilineFurnace),
						new StructureBlock(BlocksCore.invertedBlock, 0, 1, 0, 0),
						new StructureBlock(BlocksCore.invertedBlock, 0, -1, 0, 0),
						new StructureBlock(BlocksCore.invertedBlock, 0, 0, 0, 1),
						new StructureBlock(BlocksCore.invertedBlock, 0, 0, 0, -1),
						new StructureBlock(BlocksCore.invertedBlock, 0, 1, 0, 1),
						new StructureBlock(BlocksCore.invertedBlock, 0, 1, 0, -1),
						new StructureBlock(BlocksCore.invertedBlock, 0, -1, 0, 1),
						new StructureBlock(BlocksCore.invertedBlock, 0, -1, 0, -1),
						new StructureBlock(BlocksCore.invertedBlock, 0, 2, 0, 0),
						new StructureBlock(BlocksCore.invertedBlock, 0, -2, 0, 0),
						new StructureBlock(BlocksCore.invertedBlock, 0, 0, 0, 2),
						new StructureBlock(BlocksCore.invertedBlock, 0, 0, 0, -2),
						new StructureBlock(BlocksCore.invertedBlock, 0, 2, 0, 2),
						new StructureBlock(BlocksCore.invertedBlock, 0, 2, 0, -2),
						new StructureBlock(BlocksCore.invertedBlock, 0, -2, 0, 2),
						new StructureBlock(BlocksCore.invertedBlock, 0, -2, 0, -2)
						)))
				.apendPage(next("mithrilineFurnace").setText(I18n.translateToLocal("essentialcraft.txt.layer_0")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.mithrilineFurnace),
						new StructureBlock(BlocksCore.mithrilineFurnace, 0, 0, 0, 0),
						new StructureBlock(BlocksCore.invertedBlock, 0, 2, 0, 0),
						new StructureBlock(BlocksCore.invertedBlock, 0, -2, 0, 0),
						new StructureBlock(BlocksCore.invertedBlock, 0, 0, 0, 2),
						new StructureBlock(BlocksCore.invertedBlock, 0, 0, 0, -2),
						new StructureBlock(BlocksCore.invertedBlock, 0, 2, 0, 2),
						new StructureBlock(BlocksCore.invertedBlock, 0, 2, 0, -2),
						new StructureBlock(BlocksCore.invertedBlock, 0, -2, 0, 2),
						new StructureBlock(BlocksCore.invertedBlock, 0, -2, 0, -2)
						)))
				.apendPage(next("mithrilineFurnace").setText(I18n.translateToLocal("essentialcraft.txt.layer_1")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.mithrilineFurnace),
						new StructureBlock(BlocksCore.invertedBlock, 0, 2, 0, 2),
						new StructureBlock(BlocksCore.invertedBlock, 0, 2, 0, -2),
						new StructureBlock(BlocksCore.invertedBlock, 0, -2, 0, 2),
						new StructureBlock(BlocksCore.invertedBlock, 0, -2, 0, -2)
						)))
				.apendPage(next("mithrilineFurnace").setText(I18n.translateToLocal("essentialcraft.txt.layer_2")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.mithrilineFurnace),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, 2, 0, 2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, 2, 0, -2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, -2, 0, 2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, -2, 0, -2)
						)))
				)
		.apendDiscovery(
				new DiscoveryEntry("windRune")

				.setDisplayStack(new ItemStack(BlocksCore.windRune))
				.setReferal(new ItemStack(BlocksCore.windRune))
				.apendPage(next("windRune").setText(I18n.translateToLocal("essentialcraft.page.windRune_0.txt")))
				.apendPage(next("windRune").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.windRune,1,0), 2)))
				.apendPage(next("windRune").setText(I18n.translateToLocal("essentialcraft.txt.layer_-1")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.windRune),
						new StructureBlock(BlocksCore.invertedBlock, 0, 0, 0, 0),
						new StructureBlock(BlocksCore.invertedBlock, 0, 1, 0, 0),
						new StructureBlock(BlocksCore.invertedBlock, 0, -1, 0, 0),
						new StructureBlock(BlocksCore.invertedBlock, 0, 0, 0, 1),
						new StructureBlock(BlocksCore.invertedBlock, 0, 0, 0, -1),
						new StructureBlock(BlocksCore.platingPale, 0, 1, 0, 1),
						new StructureBlock(BlocksCore.platingPale, 0, 2, 0, 1),
						new StructureBlock(BlocksCore.platingPale, 0, 1, 0, 2),
						new StructureBlock(BlocksCore.platingPale, 0, -1, 0, 1),
						new StructureBlock(BlocksCore.platingPale, 0, -2, 0, 1),
						new StructureBlock(BlocksCore.platingPale, 0, -1, 0, 2),
						new StructureBlock(BlocksCore.platingPale, 0, 1, 0, -1),
						new StructureBlock(BlocksCore.platingPale, 0, 2, 0, -1),
						new StructureBlock(BlocksCore.platingPale, 0, 1, 0, -2),
						new StructureBlock(BlocksCore.platingPale, 0, -1, 0, -1),
						new StructureBlock(BlocksCore.platingPale, 0, -2, 0, -1),
						new StructureBlock(BlocksCore.platingPale, 0, -1, 0, -2),
						new StructureBlock(BlocksCore.magicPlating, 0, 2, 0, 0),
						new StructureBlock(BlocksCore.magicPlating, 0, -2, 0, 0),
						new StructureBlock(BlocksCore.magicPlating, 0, 0, 0, 2),
						new StructureBlock(BlocksCore.magicPlating, 0, 0, 0, -2)
						)))
				.apendPage(next("windRune").setText(I18n.translateToLocal("essentialcraft.txt.layer_0")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.windRune),
						new StructureBlock(BlocksCore.windRune, 0, 0, 0, 0),
						new StructureBlock(BlocksCore.invertedBlock, 0, 1, 0, 1),
						new StructureBlock(BlocksCore.invertedBlock, 0, -1, 0, 1),
						new StructureBlock(BlocksCore.invertedBlock, 0, 1, 0, -1),
						new StructureBlock(BlocksCore.invertedBlock, 0, -1, 0, -1),
						new StructureBlock(BlocksCore.magicPlating, 0, 2, 0, 0),
						new StructureBlock(BlocksCore.magicPlating, 0, -2, 0, 0),
						new StructureBlock(BlocksCore.magicPlating, 0, 0, 0, 2),
						new StructureBlock(BlocksCore.magicPlating, 0, 0, 0, -2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, 2, 0, 2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, -2, 0, 2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, 2, 0, -2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, -2, 0, -2)
						)))
				.apendPage(next("windRune").setText(I18n.translateToLocal("essentialcraft.txt.layer_1")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.windRune),
						new StructureBlock(BlocksCore.invertedBlock, 0, 1, 0, 1),
						new StructureBlock(BlocksCore.invertedBlock, 0, -1, 0, 1),
						new StructureBlock(BlocksCore.invertedBlock, 0, 1, 0, -1),
						new StructureBlock(BlocksCore.invertedBlock, 0, -1, 0, -1)
						)))
				.apendPage(next("windRune").setText(I18n.translateToLocal("essentialcraft.txt.layer_2")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.windRune),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, 1, 0, 1),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, -1, 0, 1),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, 1, 0, -1),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, -1, 0, -1)
						)))
				.apendPage(next("windRune").setText(I18n.translateToLocal("essentialcraft.page.windRune_1.txt")))
				.apendPage(next("windRune").setText(I18n.translateToLocal("essentialcraft.page.windRune_2.txt")))
				.apendPage(next("windRune").setText(I18n.translateToLocal("essentialcraft.page.windRune_3.txt")))
				.apendPage(next("windRune").setText(I18n.translateToLocal("essentialcraft.page.windRune_4.txt")))
				.apendPage(next("windRune").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.wind_elemental_axe), 2)))
				.apendPage(next("windRune").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.wind_elemental_pick), 2)))
				.apendPage(next("windRune").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.wind_elemental_hoe), 2)))
				.apendPage(next("windRune").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.wind_elemental_shovel), 2)))
				.apendPage(next("windRune").setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.wind_elemental_sword), 2)))
				)
		.apendDiscovery(
				new DiscoveryEntry("playerPentacle")

				.setDisplayStack(new ItemStack(BlocksCore.playerPentacle))
				.setReferal(new ItemStack(BlocksCore.playerPentacle))
				.apendPage(next("playerPentacle").setText(I18n.translateToLocal("essentialcraft.page.playerPentacle_0.txt")))
				.apendPage(next("playerPentacle").setText(I18n.translateToLocal("essentialcraft.page.playerPentacle_1.txt")))
				.apendPage(next("playerPentacle").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.playerPentacle), 2)))
				.apendPage(next("playerPentacle").setText(I18n.translateToLocal("essentialcraft.txt.layer_-1")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.playerPentacle),
						new StructureBlock(BlocksCore.invertedBlock, 0, 1, 0, 0),
						new StructureBlock(BlocksCore.invertedBlock, 0, -1, 0, 0),
						new StructureBlock(BlocksCore.invertedBlock, 0, 0, 0, 1),
						new StructureBlock(BlocksCore.invertedBlock, 0, 0, 0, -1),
						new StructureBlock(BlocksCore.invertedBlock, 0, 0, 0, 0),
						new StructureBlock(BlocksCore.invertedBlock, 0, 2, 0, 1),
						new StructureBlock(BlocksCore.invertedBlock, 0, 2, 0, -1),
						new StructureBlock(BlocksCore.invertedBlock, 0, -2, 0, 1),
						new StructureBlock(BlocksCore.invertedBlock, 0, -2, 0, -1),
						new StructureBlock(BlocksCore.invertedBlock, 0, 1, 0, 2),
						new StructureBlock(BlocksCore.invertedBlock, 0, -1, 0, 2),
						new StructureBlock(BlocksCore.invertedBlock, 0, 1, 0, -2),
						new StructureBlock(BlocksCore.invertedBlock, 0, -1, 0, -2)
						)))
				.apendPage(next("playerPentacle").setText(I18n.translateToLocal("essentialcraft.txt.layer_0")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.playerPentacle),
						new StructureBlock(BlocksCore.playerPentacle, 0, 0, 0, 0),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, 2, 0, 1),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, 2, 0, -1),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, -2, 0, 1),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, -2, 0, -1),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, 1, 0, 2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, -1, 0, 2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, 1, 0, -2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, -1, 0, -2)
						)))

				.apendPage(next("playerPentacle").setText(I18n.translateToLocal("essentialcraft.page.playerPentacle_2.txt")))
				.apendPage(next("playerPentacle").setText(I18n.translateToLocal("essentialcraft.txt.layer_-1")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.playerPentacle),
						new StructureBlock(BlocksCore.invertedBlock, 0, 1, 0, 0),
						new StructureBlock(BlocksCore.invertedBlock, 0, -1, 0, 0),
						new StructureBlock(BlocksCore.invertedBlock, 0, 0, 0, 1),
						new StructureBlock(BlocksCore.invertedBlock, 0, 0, 0, -1),
						new StructureBlock(BlocksCore.invertedBlock, 0, 1, 0, 1),
						new StructureBlock(BlocksCore.invertedBlock, 0, -1, 0, 1),
						new StructureBlock(BlocksCore.invertedBlock, 0, 1, 0, -1),
						new StructureBlock(BlocksCore.invertedBlock, 0, -1, 0, -1),
						new StructureBlock(BlocksCore.invertedBlock, 0, 0, 0, 0),
						new StructureBlock(BlocksCore.invertedBlock, 0, 2, 0, 1),
						new StructureBlock(BlocksCore.invertedBlock, 0, 2, 0, -1),
						new StructureBlock(BlocksCore.invertedBlock, 0, -2, 0, 1),
						new StructureBlock(BlocksCore.invertedBlock, 0, -2, 0, -1),
						new StructureBlock(BlocksCore.invertedBlock, 0, 1, 0, 2),
						new StructureBlock(BlocksCore.invertedBlock, 0, -1, 0, 2),
						new StructureBlock(BlocksCore.invertedBlock, 0, 1, 0, -2),
						new StructureBlock(BlocksCore.invertedBlock, 0, -1, 0, -2)
						)))
				.apendPage(next("playerPentacle").setText(I18n.translateToLocal("essentialcraft.txt.layer_0")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.playerPentacle),
						new StructureBlock(BlocksCore.playerPentacle, 0, 0, 0, 0),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, 2, 0, 1),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, 2, 0, -1),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, -2, 0, 1),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, -2, 0, -1),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, 1, 0, 2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, -1, 0, 2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, 1, 0, -2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, -1, 0, -2),
						new StructureBlock(BlocksCore.blockPale, 0, 1, 0, 1),
						new StructureBlock(BlocksCore.blockPale, 0, -1, 0, 1),
						new StructureBlock(BlocksCore.blockPale, 0, 1, 0, -1),
						new StructureBlock(BlocksCore.blockPale, 0, -1, 0, -1)
						)))
				.apendPage(next("playerPentacle").setText(I18n.translateToLocal("essentialcraft.txt.layer_1")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.playerPentacle),
						new StructureBlock(BlocksCore.mithrilineCrystal, 3, 1, 0, 1),
						new StructureBlock(BlocksCore.mithrilineCrystal, 3, -1, 0, 1),
						new StructureBlock(BlocksCore.mithrilineCrystal, 3, 1, 0, -1),
						new StructureBlock(BlocksCore.mithrilineCrystal, 3, -1, 0, -1)
						)))

				.apendPage(next("playerPentacle").setText(I18n.translateToLocal("essentialcraft.page.playerPentacle_3.txt")))
				.apendPage(next("playerPentacle").setText(I18n.translateToLocal("essentialcraft.txt.layer_-1")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.playerPentacle),
						new StructureBlock(BlocksCore.invertedBlock, 0, 1, 0, 0),
						new StructureBlock(BlocksCore.invertedBlock, 0, -1, 0, 0),
						new StructureBlock(BlocksCore.invertedBlock, 0, 0, 0, 1),
						new StructureBlock(BlocksCore.invertedBlock, 0, 0, 0, -1),
						new StructureBlock(BlocksCore.invertedBlock, 0, 1, 0, 1),
						new StructureBlock(BlocksCore.invertedBlock, 0, -1, 0, 1),
						new StructureBlock(BlocksCore.invertedBlock, 0, 1, 0, -1),
						new StructureBlock(BlocksCore.invertedBlock, 0, -1, 0, -1),
						new StructureBlock(BlocksCore.invertedBlock, 0, 0, 0, 0),
						new StructureBlock(BlocksCore.invertedBlock, 0, 2, 0, 1),
						new StructureBlock(BlocksCore.invertedBlock, 0, 2, 0, -1),
						new StructureBlock(BlocksCore.invertedBlock, 0, -2, 0, 1),
						new StructureBlock(BlocksCore.invertedBlock, 0, -2, 0, -1),
						new StructureBlock(BlocksCore.invertedBlock, 0, 1, 0, 2),
						new StructureBlock(BlocksCore.invertedBlock, 0, -1, 0, 2),
						new StructureBlock(BlocksCore.invertedBlock, 0, 1, 0, -2),
						new StructureBlock(BlocksCore.invertedBlock, 0, -1, 0, -2)
						)))
				.apendPage(next("playerPentacle").setText(I18n.translateToLocal("essentialcraft.txt.layer_0")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.playerPentacle),
						new StructureBlock(BlocksCore.playerPentacle, 0, 0, 0, 0),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, 2, 0, 1),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, 2, 0, -1),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, -2, 0, 1),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, -2, 0, -1),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, 1, 0, 2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, -1, 0, 2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, 1, 0, -2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, -1, 0, -2),
						new StructureBlock(BlocksCore.blockPale, 0, 1, 0, 1),
						new StructureBlock(BlocksCore.blockPale, 0, -1, 0, 1),
						new StructureBlock(BlocksCore.blockPale, 0, 1, 0, -1),
						new StructureBlock(BlocksCore.blockPale, 0, -1, 0, -1),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, 2),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, 2),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, -2),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, -2)
						)))
				.apendPage(next("playerPentacle").setText(I18n.translateToLocal("essentialcraft.txt.layer_1")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.playerPentacle),
						new StructureBlock(BlocksCore.mithrilineCrystal, 3, 1, 0, 1),
						new StructureBlock(BlocksCore.mithrilineCrystal, 3, -1, 0, 1),
						new StructureBlock(BlocksCore.mithrilineCrystal, 3, 1, 0, -1),
						new StructureBlock(BlocksCore.mithrilineCrystal, 3, -1, 0, -1),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, 2),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, 2),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, -2),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, -2)
						)))
				.apendPage(next("playerPentacle").setText(I18n.translateToLocal("essentialcraft.txt.layer_2")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.playerPentacle),
						new StructureBlock(BlocksCore.mithrilineCrystal, 6, 2, 0, 2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 6, -2, 0, 2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 6, 2, 0, -2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 6, -2, 0, -2)
						)))

				.apendPage(next("playerPentacle").setText(I18n.translateToLocal("essentialcraft.page.playerPentacle_4.txt")))
				.apendPage(next("playerPentacle").setText(I18n.translateToLocal("essentialcraft.txt.layer_-1")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.playerPentacle),
						new StructureBlock(BlocksCore.invertedBlock, 0, 1, 0, 0),
						new StructureBlock(BlocksCore.invertedBlock, 0, -1, 0, 0),
						new StructureBlock(BlocksCore.invertedBlock, 0, 0, 0, 1),
						new StructureBlock(BlocksCore.invertedBlock, 0, 0, 0, -1),
						new StructureBlock(BlocksCore.invertedBlock, 0, 1, 0, 1),
						new StructureBlock(BlocksCore.invertedBlock, 0, -1, 0, 1),
						new StructureBlock(BlocksCore.invertedBlock, 0, 1, 0, -1),
						new StructureBlock(BlocksCore.invertedBlock, 0, -1, 0, -1),
						new StructureBlock(BlocksCore.invertedBlock, 0, 0, 0, 0),
						new StructureBlock(BlocksCore.invertedBlock, 0, 2, 0, 1),
						new StructureBlock(BlocksCore.invertedBlock, 0, 2, 0, -1),
						new StructureBlock(BlocksCore.invertedBlock, 0, -2, 0, 1),
						new StructureBlock(BlocksCore.invertedBlock, 0, -2, 0, -1),
						new StructureBlock(BlocksCore.invertedBlock, 0, 1, 0, 2),
						new StructureBlock(BlocksCore.invertedBlock, 0, -1, 0, 2),
						new StructureBlock(BlocksCore.invertedBlock, 0, 1, 0, -2),
						new StructureBlock(BlocksCore.invertedBlock, 0, -1, 0, -2),
						new StructureBlock(BlocksCore.demonicPlating, 0, 0, 0, 2),
						new StructureBlock(BlocksCore.demonicPlating, 0, 0, 0, -2),
						new StructureBlock(BlocksCore.demonicPlating, 0, 2, 0, 0),
						new StructureBlock(BlocksCore.demonicPlating, 0, -2, 0, 0)
						)))
				.apendPage(next("playerPentacle").setText(I18n.translateToLocal("essentialcraft.txt.layer_0")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.playerPentacle),
						new StructureBlock(BlocksCore.playerPentacle, 0, 0, 0, 0),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, 2, 0, 1),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, 2, 0, -1),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, -2, 0, 1),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, -2, 0, -1),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, 1, 0, 2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, -1, 0, 2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, 1, 0, -2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, -1, 0, -2),
						new StructureBlock(BlocksCore.blockPale, 0, 1, 0, 1),
						new StructureBlock(BlocksCore.blockPale, 0, -1, 0, 1),
						new StructureBlock(BlocksCore.blockPale, 0, 1, 0, -1),
						new StructureBlock(BlocksCore.blockPale, 0, -1, 0, -1),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, 2),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, 2),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, -2),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, -2)
						)))
				.apendPage(next("playerPentacle").setText(I18n.translateToLocal("essentialcraft.txt.layer_1")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.playerPentacle),
						new StructureBlock(BlocksCore.mithrilineCrystal, 3, 1, 0, 1),
						new StructureBlock(BlocksCore.mithrilineCrystal, 3, -1, 0, 1),
						new StructureBlock(BlocksCore.mithrilineCrystal, 3, 1, 0, -1),
						new StructureBlock(BlocksCore.mithrilineCrystal, 3, -1, 0, -1),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, 2),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, 2),
						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, -2),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, -2)
						)))
				.apendPage(next("playerPentacle").setText(I18n.translateToLocal("essentialcraft.txt.layer_2")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.playerPentacle),
						new StructureBlock(BlocksCore.mithrilineCrystal, 6, 2, 0, 2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 6, -2, 0, 2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 6, 2, 0, -2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 6, -2, 0, -2)
						)))
				.apendPage(next("playerPentacle").setText(I18n.translateToLocal("essentialcraft.txt.layer_3")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.playerPentacle),
						new StructureBlock(BlocksCore.mithrilineCrystal, 9, 2, 0, 0),
						new StructureBlock(BlocksCore.mithrilineCrystal, 9, -2, 0, 0),
						new StructureBlock(BlocksCore.mithrilineCrystal, 9, 0, 0, 2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 9, 0, 0, -2)
						)))
				)
		.apendDiscovery(
				new DiscoveryEntry("demonicPentacle")

				.setDisplayStack(new ItemStack(BlocksCore.demonicPentacle))
				.apendPage(next("demonicPentacle").setText(I18n.translateToLocal("essentialcraft.page.demonicPentacle_0.txt")))
				.apendPage(next("demonicPentacle").setText(I18n.translateToLocal("essentialcraft.page.demonicPentacle_1.txt")))
				.apendPage(next("demonicPentacle").setText(I18n.translateToLocal("essentialcraft.page.demonicPentacle_2.txt")))
				.apendPage(next("demonicPentacle").setRecipe(ECUtils.findRecipeByIS(new ItemStack(BlocksCore.demonicPentacle), 2)))
				.apendPage(next("demonicPentacle").setText(I18n.translateToLocal("essentialcraft.txt.layer_-1")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.demonicPentacle),
						new StructureBlock(BlocksCore.voidStone, 0, 0, 0, 0),

						new StructureBlock(BlocksCore.voidStone, 0, 2, 0, 0),
						new StructureBlock(BlocksCore.voidStone, 0, -2, 0, 0),
						new StructureBlock(BlocksCore.voidStone, 0, 0, 0, 2),
						new StructureBlock(BlocksCore.voidStone, 0, 0, 0, -2),

						new StructureBlock(BlocksCore.voidStone, 0, 3, 0, 1),
						new StructureBlock(BlocksCore.voidStone, 0, 3, 0, -1),
						new StructureBlock(BlocksCore.voidStone, 0, -3, 0, 1),
						new StructureBlock(BlocksCore.voidStone, 0, -3, 0, -1),
						new StructureBlock(BlocksCore.voidStone, 0, 1, 0, 3),
						new StructureBlock(BlocksCore.voidStone, 0, -1, 0, 3),
						new StructureBlock(BlocksCore.voidStone, 0, 1, 0, -3),
						new StructureBlock(BlocksCore.voidStone, 0, -1, 0, -3),

						new StructureBlock(BlocksCore.platingPale, 0, 1, 0, 0),
						new StructureBlock(BlocksCore.platingPale, 0, -1, 0, 0),
						new StructureBlock(BlocksCore.platingPale, 0, 0, 0, 1),
						new StructureBlock(BlocksCore.platingPale, 0, 0, 0, -1),

						new StructureBlock(BlocksCore.platingPale, 0, 2, 0, 1),
						new StructureBlock(BlocksCore.platingPale, 0, 2, 0, -1),
						new StructureBlock(BlocksCore.platingPale, 0, -2, 0, 1),
						new StructureBlock(BlocksCore.platingPale, 0, -2, 0, -1),
						new StructureBlock(BlocksCore.platingPale, 0, 1, 0, 2),
						new StructureBlock(BlocksCore.platingPale, 0, -1, 0, 2),
						new StructureBlock(BlocksCore.platingPale, 0, 1, 0, -2),
						new StructureBlock(BlocksCore.platingPale, 0, -1, 0, -2),

						new StructureBlock(BlocksCore.platingPale, 0, 3, 0, 2),
						new StructureBlock(BlocksCore.platingPale, 0, 3, 0, -2),
						new StructureBlock(BlocksCore.platingPale, 0, -3, 0, 2),
						new StructureBlock(BlocksCore.platingPale, 0, -3, 0, -2),
						new StructureBlock(BlocksCore.platingPale, 0, 2, 0, 3),
						new StructureBlock(BlocksCore.platingPale, 0, -2, 0, 3),
						new StructureBlock(BlocksCore.platingPale, 0, 2, 0, -3),
						new StructureBlock(BlocksCore.platingPale, 0, -2, 0, -3)
						)))
				.apendPage(next("demonicPentacle").setText(I18n.translateToLocal("essentialcraft.txt.layer_0")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.demonicPentacle),
						new StructureBlock(BlocksCore.demonicPentacle, 0, 0, 0, 0),

						new StructureBlock(BlocksCore.invertedBlock, 0, 2, 0, 2),
						new StructureBlock(BlocksCore.invertedBlock, 0, -2, 0, 2),
						new StructureBlock(BlocksCore.invertedBlock, 0, 2, 0, -2),
						new StructureBlock(BlocksCore.invertedBlock, 0, -2, 0, -2),

						new StructureBlock(Blocks.GLOWSTONE, 0, 3, 0, 0),
						new StructureBlock(Blocks.GLOWSTONE, 0, -3, 0, 0),
						new StructureBlock(Blocks.GLOWSTONE, 0, 0, 0, 3),
						new StructureBlock(Blocks.GLOWSTONE, 0, 0, 0, -3),

						new StructureBlock(BlocksCore.mithrilineCrystal, 3, 3, 0, 2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 3, 3, 0, -2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 3, -3, 0, 2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 3, -3, 0, -2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 3, 2, 0, 3),
						new StructureBlock(BlocksCore.mithrilineCrystal, 3, -2, 0, 3),
						new StructureBlock(BlocksCore.mithrilineCrystal, 3, 2, 0, -3),
						new StructureBlock(BlocksCore.mithrilineCrystal, 3, -2, 0, -3)
						)))
				.apendPage(next("demonicPentacle").setText(I18n.translateToLocal("essentialcraft.txt.layer_1")).setRecipe(new StructureRecipe(new ItemStack(BlocksCore.demonicPentacle),

						new StructureBlock(BlocksCore.mithrilineCrystal, 0, 2, 0, 2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, -2, 0, 2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, 2, 0, -2),
						new StructureBlock(BlocksCore.mithrilineCrystal, 0, -2, 0, -2),

						new StructureBlock(BlocksCore.mithrilineCrystal, 6, 3, 0, 0),
						new StructureBlock(BlocksCore.mithrilineCrystal, 6, -3, 0, 0),
						new StructureBlock(BlocksCore.mithrilineCrystal, 6, 0, 0, 3),
						new StructureBlock(BlocksCore.mithrilineCrystal, 6, 0, 0, -3)
						)))
				.apendPage(next("demonicPentacle").setText(I18n.translateToLocal("essentialcraft.page.demonicPentacle_3.txt")).setRecipe(ECUtils.findRecipeByIS(new ItemStack(ItemsCore.soulScriber), 2)))

				);
	}

	public static PageEntry next(String genID)
	{
		if(!stringIDS.containsKey(genID))
			stringIDS.put(genID, 0);
		int ptt = stringIDS.get(genID);
		stringIDS.put(genID, ptt+1);
		return new PageEntry("essentialcraft.page."+genID+"_"+ptt);
	}

	public static void init()
	{
		stringIDS.clear();
		ApiCore.CATEGORY_LIST.clear();
		basic.discoveries.clear();
		mru.discoveries.clear();
		eng.discoveries.clear();
		hoanna.discoveries.clear();
		shade.discoveries.clear();
		ender.discoveries.clear();

		registerBasicCategory();
		registerMruCategory();
		registerEngineersCategory();
		registerHoannaCategory();
		registerEnderCategory();

		mru.setDisplayStack(new ItemStack(ItemsCore.drops,1,4));
		eng.setDisplayStack(new ItemStack(ItemsCore.wFocus,1,0));
		hoanna.setDisplayStack(new ItemStack(ItemsCore.genericItem,1,37));
		shade.setDisplayStack(new ItemStack(BlocksCore.lightCorruption[3],1,6));
		ender.setDisplayStack(new ItemStack(BlocksCore.mithrilineCrystal,1,0));

		ApiCore.CATEGORY_LIST.add(basic);
		ApiCore.CATEGORY_LIST.add(mru);
		ApiCore.CATEGORY_LIST.add(eng);
		ApiCore.CATEGORY_LIST.add(hoanna);
		ApiCore.CATEGORY_LIST.add(shade);
		ApiCore.CATEGORY_LIST.add(ender);
	}

	public static final CategoryEntry basic = new CategoryEntry("essentialcraft.basic").setDisplayStack(new ResourceLocation("essentialcraft","textures/special/basical_knowledge_icon.png"));
	public static final CategoryEntry mru = new CategoryEntry("essentialcraft.mru").setTier(1);
	public static final CategoryEntry eng = new CategoryEntry("essentialcraft.eng").setTier(2);
	public static final CategoryEntry ender = new CategoryEntry("essentialcraft.ender").setTier(2);
	public static final CategoryEntry hoanna = new CategoryEntry("essentialcraft.hoanna").setTier(3).setSpecificTexture(new ResourceLocation("essentialcraft","textures/gui/research_book_hoanna.png")).setTextColor(0xcccccc);
	public static final CategoryEntry shade = new CategoryEntry("essentialcraft.shade").setTier(4);

}
