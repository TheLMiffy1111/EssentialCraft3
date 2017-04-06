package ec3.common.registry;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import DummyCore.Utils.MiscUtils;
import DummyCore.Utils.UnformedItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ec3.api.DemonTrade;
import ec3.api.MagicianTableRecipes;
import ec3.api.MithrilineFurnaceRecipes;
import ec3.api.RadiatingChamberRecipes;
import ec3.api.WindImbueRecipe;
import ec3.common.block.BlocksCore;
import ec3.common.entity.EntityWindMage;
import ec3.common.item.ItemsCore;
import ec3.utils.common.RecipeArmorDyesHandler;

public class RecipeRegistry {
	public boolean hasGregTech = false;
	private Class<?> GT_Class;
	public static HashMap<Block,ItemStack> fancyBlockRecipes = Maps.<Block,ItemStack>newHashMap();

	@SuppressWarnings("unchecked")
	public static void main() {
		registerDictionary();
		registerRecipes();
		registerMagicianTable();
		registerRadiatingChamber();
		registerMithrilineFurnace();
		registerWindRecipes();
		registerDemonTrades();
		CraftingManager.getInstance().getRecipeList().add(new RecipeArmorDyesHandler());
	}

	private static void registerDictionary() {
		OreDictionaryRegistry.register();
	}

	private static void registerRecipes() {
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemsCore.drops,4,4), new Object[]{
				"shardFire","shardWater","shardEarth","shardAir"
		}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemsCore.mruMover1,1,0), new Object[]{
				"shardElemental","stickWood"
		}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemsCore.magicalChisel,1,0), new Object[]{
				"shardElemental","shardElemental","shardElemental","shardElemental","stickWood"
		}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemsCore.bound_gem,1,0), new Object[]{
				"shardElemental","gemQuartz"
		}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemsCore.genericItem,1,33), new Object[]{
				"ingotThaumium","ingotThaumium","ingotThaumium","ingotThaumium","ingotThaumium","ingotThaumium","ingotThaumium","ingotThaumium",new ItemStack(ItemsCore.genericItem,1,23)
		}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemsCore.playerList,1,0), new Object[]{
				Items.PAPER,new ItemStack(ItemsCore.bound_gem,1,0)
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,1,50), new Object[]{
				" I ",
				"ISI",
				" I ",
				'I',"dustMithriline",
				'S',"ingotThaumium"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,1,48), new Object[]{
				" I ",
				"ISI",
				" I ",
				'S',"dustMithriline",
				'I',"ingotMithriline"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.holopad), new Object[]{
				"TVT",
				"TET",
				"THT",
				'T',ItemsCore.twinkling_titanite,
				'V',gen(36),
				'H',gen(59),
				'E',new ItemStack(ItemsCore.storage,1,4)
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.blockPale,1,0), new Object[]{
				"ISI",
				"SES",
				"ISI",
				'I',getItemByNameEC3("genericItem",39),
				'S',Blocks.LAPIS_BLOCK,
				'E',getItemByNameEC3("genericItem",38)
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.furnaceMagic,1,0), new Object[]{
				"III",
				"IEI",
				"III",
				'I',new ItemStack(ItemsCore.genericItem,1,7),
				'E',"rodHeat"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.furnaceMagic,1,4), new Object[]{
				"III",
				"IEI",
				"III",
				'I',new ItemStack(ItemsCore.genericItem,1,34),
				'E',new ItemStack(BlocksCore.furnaceMagic,1,0)
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.furnaceMagic,1,8), new Object[]{
				"III",
				"IEI",
				"III",
				'I',new ItemStack(ItemsCore.genericItem,1,41),
				'E',new ItemStack(BlocksCore.furnaceMagic,1,4)
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.furnaceMagic,1,12), new Object[]{
				"III",
				"IEI",
				"III",
				'I',new ItemStack(ItemsCore.genericItem,1,35),
				'E',new ItemStack(BlocksCore.furnaceMagic,1,8)
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,1,1), new Object[]{
				"ISI",
				"SES",
				"ISI",
				'I',"ingotIron",
				'S',"stone",
				'E',"shardElemental"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.mruMover_t2,1,0), new Object[]{
				" SI",
				" ES",
				"E  ",
				'I',"gemDiamond",
				'S',"plateGlass",
				'E',"ingotMagic"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,1,21), new Object[]{
				"ISI",
				" E ",
				"ISI",
				'I',"ingotIron",
				'S',"gemDiamond",
				'E',"shardElemental"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,1,21), new Object[]{
				"I I",
				"SES",
				"I I",
				'I',"ingotIron",
				'S',"gemDiamond",
				'E',"shardElemental"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,1,22), new Object[]{
				"ISI",
				" E ",
				"ISI",
				'I',"ingotIron",
				'S',"gemEmerald",
				'E',"shardElemental"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,1,22), new Object[]{
				"I I",
				"SES",
				"I I",
				'I',"ingotIron",
				'S',"gemEmerald",
				'E',"shardElemental"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,1,23), new Object[]{
				"ISI",
				"SES",
				"ISI",
				'I',"ingotIron",
				'S',"shardElemental",
				'E',"ec3:gemEnderPearl"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,3,24), new Object[]{
				"ISI",
				"S S",
				"ISI",
				'I',getItemByNameEC3("fortifiedGlass",0),
				'S',getItemByNameEC3("fortifiedStone",0)
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,1,25), new Object[]{
				"ISI",
				"ISI",
				"ISI",
				'I',"shardFire",
				'S',Items.IRON_INGOT
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,4,26), new Object[]{
				"ISI",
				"SIS",
				"ISI",
				'I',"ingotIron",
				'S',"shardElemental"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,1,27), new Object[]{
				"I I",
				" S ",
				"I I",
				'I',"frameMagic",
				'S',"magicWater"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,8,28), new Object[]{
				"SPS",
				"EGE",
				"GGG",
				'G',"ec3:ingotGold",
				'E',"gemEmerald",
				'P',"ec3:gemEnderPearl",
				'S',"shardElemental"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,8,29), new Object[]{
				"GGG",
				"EGE",
				"SPS",
				'G',"ec3:ingotGold",
				'E',"ec3:gemEnderPearl",
				'P',"ec3:gemEnderPearl",
				'S',"shardElemental"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,1,30), new Object[]{
				" I ",
				" S ",
				" I ",
				'I',"ingotThaumium",
				'S',"elementalCore"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,1,31), new Object[]{
				" O ",
				"OMO",
				" O ",
				'O',"obsidian",
				'M',"frameMagic"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.matrixAbsorber,1,0), new Object[]{
				"SAS",
				"HEH",
				"SLS",
				'S',"frameIron",
				'A',"mruCatcher",
				'L',"mruLink",
				'E',"elementalCore",
				'H',"rodHeat"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.radiatingChamber,1,0), new Object[]{
				"DAD",
				"ECE",
				"DHD",
				'D',"plateDiamond",
				'A',"mruCatcher",
				'H',"rodHeat",
				'E',getItemByNameEC3("genericItem",23),
				'C',"elementalCore"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.controlRod,1,0), new Object[]{
				"DII",
				" CI",
				"I D",
				'I',"ingotIron",
				'C',"elementalCore",
				'D',"waterMagic"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.magicalMirror,3,0), new Object[]{
				" P ",
				"PGP",
				" P ",
				'P',"plateMagic",
				'G',"plateGlass"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.filter,1,0), new Object[]{
				" P ",
				"PGP",
				" P ",
				'P',"plateFortified",
				'G',Blocks.CRAFTING_TABLE
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.filter,1,1), new Object[]{
				" P ",
				"PGP",
				" P ",
				'P',"plateVoid",
				'G',ItemsCore.filter
		}));
		addRecipe(new ItemStack(ItemsCore.magicalBuilder,1,0),new Object[]{
				new ItemStack(BlocksCore.rightClicker,1,OreDictionary.WILDCARD_VALUE),"plateVoid","voidCore",
				new ItemStack(ItemsCore.filter,1,0),new ItemStack(ItemsCore.magicalDigger,1,0),"plateVoid",
				"plateMagic",new ItemStack(ItemsCore.filter,1,0),new ItemStack(BlocksCore.rightClicker,1,OreDictionary.WILDCARD_VALUE)
		});
		GameRegistry.addShapelessRecipe(new ItemStack(ItemsCore.magicalBuilder,1,1), new ItemStack(ItemsCore.magicalBuilder,1,0));
		GameRegistry.addShapelessRecipe(new ItemStack(ItemsCore.magicalBuilder,1,2), new ItemStack(ItemsCore.magicalBuilder,1,1));
		GameRegistry.addShapelessRecipe(new ItemStack(ItemsCore.magicalBuilder,1,3), new ItemStack(ItemsCore.magicalBuilder,1,2));
		GameRegistry.addShapelessRecipe(new ItemStack(ItemsCore.magicalBuilder,1,4), new ItemStack(ItemsCore.magicalBuilder,1,3));
		GameRegistry.addShapelessRecipe(new ItemStack(ItemsCore.magicalBuilder,1,0), new ItemStack(ItemsCore.magicalBuilder,1,4));

		addRecipe(new ItemStack(BlocksCore.ecStateChecker,1,0),new Object[]{
				"frameMagic","screenMagic","frameMagic",
				"mruCatcher","elementalCore","conversionMatrix",
				"plateMagic","mruLink","plateMagic",
		});
		addRecipe(new ItemStack(BlocksCore.mithrilineCrystal,1,0),new Object[]{
				"dustMithriline","enderEye","dustMithriline",
				"dustMithriline","gemMithriline","dustMithriline",
				"dustMithriline","ec3:gemEnderPearl","dustMithriline",
		});
		addRecipe(new ItemStack(BlocksCore.mithrilineCrystal,1,3),new Object[]{
				"dustFading","gemFading","dustFading",
				"dustFading",new ItemStack(BlocksCore.mithrilineCrystal,1,0),"dustFading",
				"dustFading","gemFading","dustFading",
		});
		addRecipe(new ItemStack(BlocksCore.mithrilineCrystal,1,6),new Object[]{
				"plateVoid","voidCore","plateVoid",
				"plateVoid",new ItemStack(BlocksCore.mithrilineCrystal,1,3),"plateVoid",
				"plateVoid","voidMRU","plateVoid",
		});
		addRecipe(new ItemStack(BlocksCore.mithrilineCrystal,1,9),new Object[]{
				"ingotDemonic","demonicCore","ingotDemonic",
				"ingotDemonic",new ItemStack(BlocksCore.mithrilineCrystal,1,6),"ingotDemonic",
				"ingotDemonic","demonicCore","ingotDemonic",
		});
		addRecipe(new ItemStack(BlocksCore.ecBalancer,1,0),new Object[]{
				"plateMagic","alloysMagical","plateMagic",
				"conversionMatrix","elementalCore","conversionMatrix",
				"plateMagic","mruCatcher","plateMagic",
		});
		addRecipe(new ItemStack(BlocksCore.ecRedstoneController,1,0),new Object[]{
				"plateMagic","plateRedstone","plateMagic",
				"conversionMatrix","elementalCore","conversionMatrix",
				"plateMagic","plateRedstone","plateMagic",
		});
		addRecipe(new ItemStack(ItemsCore.genericItem,1,53),new Object[]{
				"plateDemonic","shardElemental","plateDemonic",
				"shardElemental","resonatingCrystal","shardElemental",
				"plateDemonic","shardElemental","plateDemonic",
		});
		addRecipe(new ItemStack(BlocksCore.ecHoldingChamber,1,0),new Object[]{
				"frameMagic","mruCatcher","frameMagic",
				getItemByNameEC3("storage",2),"plateGlass",getItemByNameEC3("storage",2),
				"plateMagic","mruLink","plateMagic",
		});
		addRecipe(new ItemStack(BlocksCore.rayTower,4,0),new Object[]{
				"magicWater","conversionMatrix","magicWater",
				"plateFortified","mruCatcher","plateFortified",
				new ItemStack(BlocksCore.fortifiedStone,1,0),"screenMagic",new ItemStack(BlocksCore.fortifiedStone,1,0),
		});

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlocksCore.magicPlating,16,0), new Object[]{
				getItemByNameEC3("genericItem",34),getItemByNameEC3("genericItem",34),getItemByNameEC3("genericItem",34),getItemByNameEC3("genericItem",34)
		}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlocksCore.platingPale,16,0), new Object[]{
				getItemByNameEC3("genericItem",41),getItemByNameEC3("genericItem",41),getItemByNameEC3("genericItem",41),getItemByNameEC3("genericItem",41)
		}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlocksCore.invertedBlock,16,0), new Object[]{
				getItemByNameEC3("genericItem",49),getItemByNameEC3("genericItem",49),getItemByNameEC3("genericItem",49),getItemByNameEC3("genericItem",49)
		}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlocksCore.demonicPlating,16,0), new Object[]{
				getItemByNameEC3("genericItem",54),getItemByNameEC3("genericItem",54),getItemByNameEC3("genericItem",54),getItemByNameEC3("genericItem",54)
		}));
		addRecipe(new ItemStack(BlocksCore.potionSpreader,1,0),new Object[]{
				"worldInteractor","frameMagic","worldInteractor",
				"mruCatcher","elementalCore","screenMagic",
				"plateMagic","conversionMatrix","plateMagic",
		});
		addRecipe(new ItemStack(BlocksCore.magicalEnchanter,1,0),new Object[]{
				"screenMagic",new ItemStack(Items.ENCHANTED_BOOK,1,0),"mruCatcher",
				"worldInteractor",new ItemStack(Blocks.ENCHANTING_TABLE,1,0),"conversionMatrix",
				"plateMagic","elementalCore","plateMagic",
		});
		addRecipe(new ItemStack(BlocksCore.monsterHarvester,1,0),new Object[]{
				"plateMagic","screenMagic","mruCatcher",
				new ItemStack(ItemsCore.staffOfLife,1,0),"elementalCore","conversionMatrix",
				"worldInteractor","frameMagic","worldInteractor",
		});
		addRecipe(new ItemStack(BlocksCore.magicalRepairer,1,0),new Object[]{
				"frameMagic","screenMagic","mruCatcher",
				"conversionMatrix","elementalCore","conversionMatrix",
				"plateMagic","mruLink","worldInteractor",
		});

		addRecipe(new ItemStack(BlocksCore.ecAcceptor,1,0),new Object[]{
				"frameMagic","mruCatcher","frameMagic",
				"screenMagic","elementalCore","conversionMatrix",
				"plateMagic","mruLink","plateMagic",
		});
		addRecipe(new ItemStack(BlocksCore.solarPrism,1,0),new Object[]{
				"alloysMagical",getItemByNameEC3("genericItem",32),"alloysMagical",
				getItemByNameEC3("genericItem",32),getItemByNameEC3("genericItem",32),getItemByNameEC3("genericItem",32),
				"alloysMagical",getItemByNameEC3("genericItem",32),"alloysMagical",
		});
		addRecipe(new ItemStack(BlocksCore.ecController,1,0),new Object[]{
				"frameMagic","plateMagic","frameMagic",
				"conversionMatrix","elementalCore","worldInteractor",
				"mruLink","mruLink","mruLink",
		});
		addRecipe(new ItemStack(BlocksCore.ecEjector,1,0),new Object[]{
				"frameMagic","mruLink","frameMagic",
				"conversionMatrix","elementalCore","conversionMatrix",
				"plateMagic","mruLink","plateMagic",
		});
		addRecipe(new ItemStack(BlocksCore.coldStone,1,0),new Object[]{
				new ItemStack(Blocks.ICE,1,0),new ItemStack(Blocks.SNOW,1,0),new ItemStack(Blocks.ICE,1,0),
				new ItemStack(Blocks.SNOW,1,0),new ItemStack(Blocks.GLOWSTONE,1,0),new ItemStack(Blocks.SNOW,1,0),
				new ItemStack(Blocks.ICE,1,0),new ItemStack(Blocks.SNOW,1,0),new ItemStack(Blocks.ICE,1,0),
		});
		addRecipe(new ItemStack(BlocksCore.coldDistillator,1,0),new Object[]{
				"screenMagic","mruCatcher","conversionMatrix",
				new ItemStack(BlocksCore.coldStone),"elementalCore",new ItemStack(BlocksCore.coldStone),
				"plateMagic",new ItemStack(ItemsCore.matrixProj,1,2),"worldInteractor",
		});
		addRecipe(new ItemStack(BlocksCore.magmaticSmeltery,1,0),new Object[]{
				"mruCatcher","screenMagic","alloysMagical",
				"conversionMatrix","elementalCore","conversionMatrix",
				"rodHeat","worldInteractor","rodHeat",
		});
		addRecipe(new ItemStack(BlocksCore.magicalJukebox,1,0),new Object[]{
				"plateMagic","mruCatcher","worldInteractor",
				"elementalCore",new ItemStack(Blocks.JUKEBOX,1,0),"shardElemental",
				"plateRedstone","screenMagic","plateRedstone",
		});
		addRecipe(new ItemStack(BlocksCore.crystalFormer,1,0),new Object[]{
				"screenMagic","mruCatcher","plateMagic",
				"dustCrystal","elementalCore","dustCrystal",
				"plateObsidian","conversionMatrix","plateObsidian",
		});
		addRecipe(new ItemStack(BlocksCore.crystalController,1,0),new Object[]{
				"screenMagic","mruCatcher","plateMagic",
				"dustCrystal","elementalCore","dustCrystal",
				"plateFortified","conversionMatrix","plateFortified",
		});
		addRecipe(new ItemStack(BlocksCore.naturalFurnace,1,0),new Object[]{
				"frameMagic","screenMagic","mruCatcher",
				"rodHeat","elementalCore","rodHeat",
				"plateMagic","worldInteractor","conversionMatrix",
		});
		addRecipe(new ItemStack(BlocksCore.heatGenerator,1,0),new Object[]{
				"plateMagic","screenMagic","conversionMatrix",
				"rodHeat","elementalCore","rodHeat",
				"frameMagic","worldInteractor","mruCatcher",
		});
		addRecipe(new ItemStack(BlocksCore.enderGenerator,1,0),new Object[]{
				"frameMagic","screenMagic","mruCatcher",
				"conversionMatrix","elementalCore","conversionMatrix",
				"plateMagic","worldInteractor","plateEnder",
		});
		addRecipe(new ItemStack(BlocksCore.magicianTable,1,0),new Object[]{
				"frameIron","mruCatcher","frameIron",
				"plateEmerald","elementalCore","plateEmerald",
				"plateObsidian","frameIron","plateObsidian",
		});
		addRecipe(new ItemStack(BlocksCore.magicalQuarry,1,0),new Object[]{
				"plateFortified","frameMagic","plateFortified",
				"screenMagic",new ItemStack(ItemsCore.magicalDigger,1,0),"mruCatcher",
				"worldInteractor","elementalCore","worldInteractor",
		});
		addRecipe(new ItemStack(BlocksCore.monsterClinger,1,0),new Object[]{
				"worldInteractor","elementalCore","worldInteractor",
				"screenMagic","conversionMatrix","mruCatcher",
				"frameMagic","plateFortified","frameMagic",
		});
		addRecipe(new ItemStack(BlocksCore.crystalExtractor,1,0),new Object[]{
				"screenMagic","dustCrystal","alloysMagical",
				"conversionMatrix","elementalCore","conversionMatrix",
				"plateFortified","dustCrystal","plateFortified",
		});
		addRecipe(new ItemStack(BlocksCore.chargingChamber,1,0),new Object[]{
				"elementalCore","mruCatcher","frameMagic",
				"screenMagic",new ItemStack(ItemsCore.storage,1,3),"conversionMatrix",
				"plateMagic","mruLink","plateMagic",
		});
		addRecipe(new ItemStack(BlocksCore.voidGlass,32,0),new Object[]{
				"titanite","frameMagic","titanite",
				"frameMagic","ttitanite","frameMagic",
				"titanite","frameMagic","titanite",
		});
		//Computer Guy's recipes
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,1,69),new Object[]{
				"#R#",
				"LQL",
				"#R#",
				'#',"plateDemonic",
				'R',"dustRedstone",
				'L',"gemLapis",
				'Q',"gemQuartz"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,1,68),new Object[]{
				"#@#",
				"@C@",
				"#@#",
				'#',"plateDemonic",
				'@',gen(47),
				'C',gen(55)
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,1,74),new Object[]{
				"#@#",
				"@C@",
				"#@#",
				'#',"plateDemonic",
				'@',gen(48),
				'C',gen(55)
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,1,67),new Object[]{
				"#@#",
				"@C@",
				"#@#",
				'#',"plateDemonic",
				'@',gen(44),
				'C',gen(68)
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,1,66),new Object[]{
				"#@#",
				"@C@",
				"#@#",
				'#',"plateDemonic",
				'@',gen(47),
				'C',gen(74)
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,2,59),new Object[]{
				"#C#",
				"@#@",
				"#C#",
				'#',"plateDemonic",
				'@',gen(0),
				'C',gen(55)
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,1,65),new Object[]{
				"#@#",
				"@C@",
				"#@#",
				'#',gen(41),
				'@',"demonicCore",
				'C',gen(37)
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,1,63),new Object[]{
				"#@#",
				"ACA",
				"#@#",
				'#',gen(59),
				'@',gen(74),
				'C',gen(9),
				'A',gen(32)
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,1,64),new Object[]{
				"#@#",
				"@C@",
				"#@#",
				'#',gen(74),
				'@',gen(59),
				'C',gen(75)
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,1,60),new Object[]{
				"#@#",
				"@@@",
				"#@#",
				'#',gen(74),
				'@',gen(59)
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,1,62),new Object[]{
				"#@#",
				"$#$",
				"P#P",
				'#',gen(68),
				'@',gen(67),
				'$',gen(60),
				'P',gen(59)
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,1,56),new Object[]{
				"#+#",
				"/C*",
				"#-#",
				'#',gen(59),
				'C',gen(69),
				'+',gen(70),
				'-',gen(73),
				'*',gen(72),
				'/',gen(71)
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,1,61),new Object[]{
				"#@#",
				"@C@",
				"#@#",
				'#',gen(67),
				'@',gen(68),
				'C',gen(59)
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,1,58),new Object[]{
				"#@#",
				"@C@",
				"#@#",
				'#',gen(66),
				'@',gen(67),
				'C',gen(65)
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,1,57),new Object[]{
				"#@#",
				"@C@",
				"#@#",
				'#',gen(68),
				'@',gen(65),
				'C',new ItemStack(ItemsCore.storage,1,4)
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.genericItem,1,75),new Object[]{
				"#C#",
				"767",
				"#C#",
				'#',"plateDemonic",
				'C',"demonicCore",
				'7',gen(74),
				'6',gen(60)
		}));

		//Armor

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.computer_helmet,1,0),new Object[]{
				"FCF",
				"SLS",
				"EOE",
				'F',gen(58),
				'C',gen(57),
				'S',gen(64),
				'L',gen(63),
				'E',gen(59),
				'O',gen(60)
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.computer_chestplate,1,0),new Object[]{
				"R*S",
				"FCF",
				"ETE",
				'R',gen(61),
				'*',gen(56),
				'S',gen(60),
				'F',gen(58),
				'C',gen(57),
				'E',gen(59),
				'T',gen(62)
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.computer_leggings,1,0),new Object[]{
				"FRF",
				"TCT",
				"ESE",
				'R',gen(61),
				'S',gen(60),
				'F',gen(58),
				'C',gen(57),
				'E',gen(59),
				'T',gen(62)
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.computer_boots,1,0),new Object[]{
				"FSF",
				"ECE",
				"SFS",
				'S',gen(60),
				'F',gen(58),
				'C',gen(57),
				'E',gen(59)
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.computerBoard,1,0),new Object[]{
				"SCS",
				"EEE",
				"RTR",
				'R',gen(61),
				'S',gen(60),
				'C',gen(57),
				'E',gen(59),
				'T',gen(62)
		}));
		//4.5 recipes
		addRecipe(new ItemStack(BlocksCore.mithrilineFurnace,1,0),new Object[]{
				"dustMithriline","gemMithriline","dustMithriline",
				"plateEmerald",Blocks.FURNACE,"plateEmerald",
				"ingotMithriline","rodHeat","ingotMithriline",
		});
		addRecipe(new ItemStack(BlocksCore.playerPentacle,1,0),new Object[]{
				"dustMithriline",ItemsCore.matrixProj,"dustMithriline",
				ItemsCore.matrixProj,ItemsCore.soulStone,ItemsCore.matrixProj,
				"dustMithriline",ItemsCore.matrixProj,"dustMithriline",
		});
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.redstoneTransmitter,2,0), new Object[]{
				"   ",
				"RTR",
				"SSS",
				'S',BlocksCore.fortifiedStone,
				'R',"dustRedstone",
				'T',Blocks.REDSTONE_TORCH
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.magicalHopper,1,0), new Object[]{
				"SHS",
				"SCS",
				"SDS",
				'S',BlocksCore.fortifiedStone,
				'H',Blocks.HOPPER,
				'C',Blocks.CHEST,
				'D',Blocks.DROPPER
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.rightClicker,1,0), new Object[]{
				"SSS",
				"DID",
				"SPS",
				'S',BlocksCore.mimic,
				'P',Blocks.PISTON,
				'I',Blocks.DISPENSER,
				'D',Blocks.DROPPER
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.windRune,1,0), new Object[]{
				"MPM",
				"PEP",
				"MPM",
				'M',BlocksCore.invertedBlock,
				'E',"elementalCore",
				'P',"plateMithriline"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.metadataManager,1,0), new Object[]{
				"SSS",
				"SBR",
				"SSS",
				'S',BlocksCore.fortifiedStone,
				'R',"dustRedstone",
				'B',ItemsCore.bound_gem
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.blockBreaker,1,0), new Object[]{
				"SSS",
				"SRP",
				"SSS",
				'S',BlocksCore.fortifiedStone,
				'R',"dustRedstone",
				'P',ItemsCore.weak_elemental_pick
		}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlocksCore.rightClicker,1,1), new Object[]{
				new ItemStack(BlocksCore.rightClicker,1,0),new ItemStack(Items.SLIME_BALL,1,0)
		}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlocksCore.rightClicker,1,2), new Object[]{
				new ItemStack(BlocksCore.rightClicker,1,0),new ItemStack(Blocks.CHEST,1,0)
		}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlocksCore.rightClicker,1,3), new Object[]{
				new ItemStack(BlocksCore.rightClicker,1,2),new ItemStack(Items.SLIME_BALL,1,0)
		}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlocksCore.rightClicker,1,4), new Object[]{
				new ItemStack(BlocksCore.rightClicker,1,2),new ItemStack(Items.REDSTONE,1,0)
		}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlocksCore.rightClicker,1,5), new Object[]{
				new ItemStack(BlocksCore.rightClicker,1,4),new ItemStack(Items.SLIME_BALL,1,0)
		}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlocksCore.compressed,1,0), new Object[]{
				new ItemStack(ItemsCore.drops,1,0),
				new ItemStack(ItemsCore.drops,1,0),
				new ItemStack(ItemsCore.drops,1,0),
				new ItemStack(ItemsCore.drops,1,0),
				new ItemStack(ItemsCore.drops,1,0),
				new ItemStack(ItemsCore.drops,1,0),
				new ItemStack(ItemsCore.drops,1,0),
				new ItemStack(ItemsCore.drops,1,0),
				new ItemStack(ItemsCore.drops,1,0),
		}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlocksCore.compressed,1,1), new Object[]{
				new ItemStack(ItemsCore.drops,1,1),
				new ItemStack(ItemsCore.drops,1,1),
				new ItemStack(ItemsCore.drops,1,1),
				new ItemStack(ItemsCore.drops,1,1),
				new ItemStack(ItemsCore.drops,1,1),
				new ItemStack(ItemsCore.drops,1,1),
				new ItemStack(ItemsCore.drops,1,1),
				new ItemStack(ItemsCore.drops,1,1),
				new ItemStack(ItemsCore.drops,1,1),
		}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlocksCore.compressed,1,2), new Object[]{
				new ItemStack(ItemsCore.drops,1,2),
				new ItemStack(ItemsCore.drops,1,2),
				new ItemStack(ItemsCore.drops,1,2),
				new ItemStack(ItemsCore.drops,1,2),
				new ItemStack(ItemsCore.drops,1,2),
				new ItemStack(ItemsCore.drops,1,2),
				new ItemStack(ItemsCore.drops,1,2),
				new ItemStack(ItemsCore.drops,1,2),
				new ItemStack(ItemsCore.drops,1,2),
		}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlocksCore.compressed,1,3), new Object[]{
				new ItemStack(ItemsCore.drops,1,3),
				new ItemStack(ItemsCore.drops,1,3),
				new ItemStack(ItemsCore.drops,1,3),
				new ItemStack(ItemsCore.drops,1,3),
				new ItemStack(ItemsCore.drops,1,3),
				new ItemStack(ItemsCore.drops,1,3),
				new ItemStack(ItemsCore.drops,1,3),
				new ItemStack(ItemsCore.drops,1,3),
				new ItemStack(ItemsCore.drops,1,3),
		}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlocksCore.compressed,1,4), new Object[]{
				new ItemStack(ItemsCore.drops,1,4),
				new ItemStack(ItemsCore.drops,1,4),
				new ItemStack(ItemsCore.drops,1,4),
				new ItemStack(ItemsCore.drops,1,4),
				new ItemStack(ItemsCore.drops,1,4),
				new ItemStack(ItemsCore.drops,1,4),
				new ItemStack(ItemsCore.drops,1,4),
				new ItemStack(ItemsCore.drops,1,4),
				new ItemStack(ItemsCore.drops,1,4),
		}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlocksCore.compressed,1,5), new Object[]{
				new ItemStack(ItemsCore.genericItem,1,51),
				new ItemStack(ItemsCore.genericItem,1,51),
				new ItemStack(ItemsCore.genericItem,1,51),
				new ItemStack(ItemsCore.genericItem,1,51),
				new ItemStack(ItemsCore.genericItem,1,51),
				new ItemStack(ItemsCore.genericItem,1,51),
				new ItemStack(ItemsCore.genericItem,1,51),
				new ItemStack(ItemsCore.genericItem,1,51),
				new ItemStack(ItemsCore.genericItem,1,51),
		}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemsCore.drops,9,0), new Object[]{
				new ItemStack(BlocksCore.compressed,1,0)
		}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemsCore.drops,9,1), new Object[]{
				new ItemStack(BlocksCore.compressed,1,1)
		}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemsCore.drops,9,2), new Object[]{
				new ItemStack(BlocksCore.compressed,1,2)
		}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemsCore.drops,9,3), new Object[]{
				new ItemStack(BlocksCore.compressed,1,3)
		}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemsCore.drops,9,4), new Object[]{
				new ItemStack(BlocksCore.compressed,1,4)
		}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemsCore.genericItem,9,51), new Object[]{
				new ItemStack(BlocksCore.compressed,1,5)
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.demonicPentacle,1,0), new Object[]{
				"FVF",
				"VCV",
				"FVF",
				'F',"focusFire",
				'V',"plateVoid",
				'C',"voidCore"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.weaponMaker,1,0), new Object[]{
				"SIS",
				"SCS",
				"SSS",
				'S',BlocksCore.fortifiedStone,
				'I',Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE,
				'C',Blocks.CRAFTING_TABLE
		}));
		GameRegistry.addShapelessRecipe(new ItemStack(BlocksCore.weaponMaker,1,1), new ItemStack(BlocksCore.weaponMaker,1,0));
		GameRegistry.addShapelessRecipe(new ItemStack(BlocksCore.weaponMaker,1,2), new ItemStack(BlocksCore.weaponMaker,1,1));
		GameRegistry.addShapelessRecipe(new ItemStack(BlocksCore.weaponMaker,1,3), new ItemStack(BlocksCore.weaponMaker,1,2));
		GameRegistry.addShapelessRecipe(new ItemStack(BlocksCore.weaponMaker,1,0), new ItemStack(BlocksCore.weaponMaker,1,3));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.wind_elemental_hoe,1,0), new Object[]{
				"DD ",
				" S ",
				" S ",
				'D',"gemWind",
				'S',"stickWood"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.wind_elemental_axe,1,0), new Object[]{
				"DD ",
				"DS ",
				" S ",
				'D',"gemWind",
				'S',"stickWood"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.wind_elemental_pick,1,0), new Object[]{
				"DDD",
				" S ",
				" S ",
				'D',"gemWind",
				'S',"stickWood"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.wind_elemental_shovel,1,0), new Object[]{
				" D ",
				" S ",
				" S ",
				'D',"gemWind",
				'S',"stickWood"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.wind_elemental_sword,1,0), new Object[]{
				" D ",
				" D ",
				" S ",
				'D',"gemWind",
				'S',"stickWood"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.soulScriber,1,0), new Object[]{
				"N",
				"Q",
				"S",
				'N',Items.NETHERBRICK,
				'S',"stickWood",
				'Q',Items.QUARTZ
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.weak_elemental_hoe,1,0), new Object[]{
				"DD ",
				" S ",
				" S ",
				'D',"blockShardElemental",
				'S',"stickWood"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.weak_elemental_axe,1,0), new Object[]{
				"DD ",
				"DS ",
				" S ",
				'D',"blockShardElemental",
				'S',"stickWood"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.weak_elemental_pick,1,0), new Object[]{
				"DDD",
				" S ",
				" S ",
				'D',"blockShardElemental",
				'S',"stickWood"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.weak_elemental_shovel,1,0), new Object[]{
				" D ",
				" S ",
				" S ",
				'D',"blockShardElemental",
				'S',"stickWood"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.weak_elemental_sword,1,0), new Object[]{
				" D ",
				" D ",
				" S ",
				'D',"blockShardElemental",
				'S',"stickWood"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.wind_helmet,1,0), new Object[]{
				"DDD",
				"D D",
				'D',"gemWind"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.wind_chestplate,1,0), new Object[]{
				"D D",
				"DDD",
				"DDD",
				'D',"gemWind"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.wind_leggings,1,0), new Object[]{
				"DDD",
				"DDD",
				"D D",
				'D',"gemWind"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.wind_boots,1,0), new Object[]{
				"D D",
				"D D",
				'D',"gemWind"
		}));

		//4.6 Recipes
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.chest,1,0), new Object[]{
				"###",
				"# #",
				"###",
				'#',new ItemStack(BlocksCore.magicPlating,1,OreDictionary.WILDCARD_VALUE)
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.chest,1,1), new Object[]{
				"###",
				"# #",
				"###",
				'#',new ItemStack(BlocksCore.voidStone,1,OreDictionary.WILDCARD_VALUE)
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.device,1,0), new Object[]{
				"#@#",
				"#R#",
				"###",
				'#',new ItemStack(BlocksCore.fortifiedStone,1,OreDictionary.WILDCARD_VALUE),
				'R',"dustRedstone",
				'@',ItemsCore.weak_elemental_hoe
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.device,1,1), new Object[]{
				"#@#",
				"#R#",
				"###",
				'#',new ItemStack(BlocksCore.fortifiedStone,1,OreDictionary.WILDCARD_VALUE),
				'R',"dustRedstone",
				'@',Items.WATER_BUCKET
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.device,1,2), new Object[]{
				"#@#",
				"#R#",
				"#@#",
				'#',new ItemStack(BlocksCore.fortifiedStone,1,OreDictionary.WILDCARD_VALUE),
				'R',"dustRedstone",
				'@',Blocks.CRAFTING_TABLE
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.device,1,3), new Object[]{
				"#@#",
				"#R#",
				"###",
				'#',new ItemStack(BlocksCore.fortifiedStone,1,OreDictionary.WILDCARD_VALUE),
				'R',"dustRedstone",
				'@',Blocks.HAY_BLOCK
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.device,1,5), new Object[]{
				"#@#",
				"#R#",
				"###",
				'#',new ItemStack(BlocksCore.fortifiedStone,1,OreDictionary.WILDCARD_VALUE),
				'R',"dustRedstone",
				'@',Items.SHEARS
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.device,1,6), new Object[]{
				"#@#",
				"#R#",
				"###",
				'#',new ItemStack(BlocksCore.fortifiedStone,1,OreDictionary.WILDCARD_VALUE),
				'R',"dustRedstone",
				'@',Items.GOLDEN_APPLE
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.device,1,7), new Object[]{
				"#@#",
				"#R#",
				"###",
				'#',new ItemStack(BlocksCore.fortifiedStone,1,OreDictionary.WILDCARD_VALUE),
				'R',"dustRedstone",
				'@',Items.APPLE
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.advBreaker,1,0), new Object[]{
				" R ",
				"RBR",
				" C ",
				'B',BlocksCore.blockBreaker,
				'R',"dustRedstone",
				'C',Blocks.CHEST
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.craftingFrame,1,0), new Object[]{
				"#@#",
				"@#@",
				"#@#",
				'#',Blocks.CRAFTING_TABLE,
				'@',"plateFortified"
		}));

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemsCore.inventoryGem,1,0),new Object[]{
				"shardElemental",gen(12)
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.filter,1,2), new Object[]{
				" @ ",
				"@#@",
				" @ ",
				'#',Blocks.CRAFTING_TABLE,
				'@',"plateObsidian"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.filter,1,3), new Object[]{
				" @ ",
				"@#@",
				" @ ",
				'#',new ItemStack(ItemsCore.filter,1,2),
				'@',"plateVoid"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.mimEjector), new Object[]{
				" @ ",
				"#C#",
				"#E#",
				'#',gen(34),
				'@',"ec3:gemEnderPearl",
				'C',"elementalCore",
				'E',"plateEnder"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.mimInjector), new Object[]{
				" @ ",
				"#C#",
				"#E#",
				'#',gen(34),
				'@',gen(23),
				'C',"elementalCore",
				'E',"plateEnder"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.mimEjectorP), new Object[]{
				" @ ",
				"#C#",
				"#E#",
				'#',"plateMithriline",
				'@',"ec3:gemEnderPearl",
				'C',"elementalCore",
				'E',"plateEnder"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.mimInjectorP), new Object[]{
				" @ ",
				"#C#",
				"#E#",
				'#',"plateMithriline",
				'@',gen(23),
				'C',"elementalCore",
				'E',"plateEnder"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.newMim), new Object[]{
				"VPV",
				"CRC",
				"PMP",
				'P',"plateVoid",
				'V',new ItemStack(BlocksCore.chest,1,1),
				'C',"voidCore",
				'R',"voidMRU",
				'M',"redSoulMatter"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.mimScreen), new Object[]{
				"DDD",
				"PCP",
				"PMP",
				'P',"plateMagic",
				'D',"screenMagic",
				'C',"elementalCore",
				'M',"mruCatcher"
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.mimInvStorage), new Object[]{
				"PPP",
				"SCS",
				"PPP",
				'P',"plateMagic",
				'C',"elementalCore",
				'S',BlocksCore.chest
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.mimCrafter), new Object[]{
				"PPP",
				"SCS",
				"PPP",
				'P',"plateMagic",
				'C',"elementalCore",
				'S',Blocks.CRAFTING_TABLE
		}));


		addRecipe(new ItemStack(BlocksCore.voidStone,16,0),new Object[]{
				"titanite","plateObsidian","titanite",
				"plateObsidian","ttitanite","plateObsidian",
				"titanite","plateObsidian","titanite",
		});
		addRecipe(new ItemStack(BlocksCore.sunRayAbsorber,1,0),new Object[]{
				"screenMagic",new ItemStack(ItemsCore.genericItem,1,32),"mruCatcher",
				"worldInteractor","elementalCore","conversionMatrix",
				"plateMagic",new ItemStack(ItemsCore.matrixProj,1,1),"plateMagic",
		});
		addRecipe(new ItemStack(BlocksCore.moonWell,1,0),new Object[]{
				"screenMagic",new ItemStack(BlocksCore.elementalCrystal,1,0),"mruCatcher",
				"worldInteractor","elementalCore","conversionMatrix",
				"plateMagic",new ItemStack(ItemsCore.matrixProj,1,3),"plateMagic",
		});
		//assembler Recipes
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.elemental_hoe,1,0), new Object[]{
				"DD ",
				" S ",
				" S ",
				'D',"resonatingCrystal",
				'S',"stickWood"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.elemental_axe,1,0), new Object[]{
				"DD ",
				"DS ",
				" S ",
				'D',"resonatingCrystal",
				'S',"stickWood"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.elemental_pick,1,0), new Object[]{
				"DDD",
				" S ",
				" S ",
				'D',"resonatingCrystal",
				'S',"stickWood"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.elemental_shovel,1,0), new Object[]{
				" D ",
				" S ",
				" S ",
				'D',"resonatingCrystal",
				'S',"stickWood"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.elemental_sword,1,0), new Object[]{
				" D ",
				" D ",
				" S ",
				'D',"resonatingCrystal",
				'S',"stickWood"
		}));

		addRecipe(new ItemStack(ItemsCore.biomeWand,1,0),new Object[]{
				"redSoulMatter","focusFire","focusAir",
				"plateEmerald","ingotThaumium","focusWater",
				"focusEarth","plateEmerald","redSoulMatter",
		});

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.magicMonocle,1,0), new Object[]{
				" DS",
				" D ",
				" D ",
				'D',new ItemStack(ItemsCore.genericItem,1,10),
				'S',"plateGlass"
		}));

		addRecipe(new ItemStack(ItemsCore.genericItem,1,17),new Object[]{
				"alloysMagical","plateGlass","alloysMagical",
				"plateEnder",Blocks.CHEST,"plateEnder",
				"alloysMagical","plateGlass","alloysMagical",
		});

		addRecipe(new ItemStack(ItemsCore.genericItem,3,18),new Object[]{
				"alloysMagical","plateRedstone","alloysMagical",
				"plateRedstone",Items.CAKE,"plateRedstone",
				"alloysMagical","plateRedstone","alloysMagical",
		});

		addRecipe(new ItemStack(ItemsCore.genericItem,1,80),new Object[]{
				"alloysMagical","plateDiamond","alloysMagical",
				"plateEmerald",Items.GOLDEN_APPLE,"plateEmerald",
				"alloysMagical","plateDiamond","alloysMagical",
		});

		addRecipe(new ItemStack(ItemsCore.genericItem,1,81),new Object[]{
				"alloysMagical","plateObsidian","alloysMagical",
				"plateObsidian",Items.ENDER_EYE,"plateObsidian",
				"alloysMagical","plateObsidian","alloysMagical",
		});

		addRecipe(new ItemStack(ItemsCore.genericItem,3,78),new Object[]{
				"alloysMagical","platePale","alloysMagical",
				"platePale","gemPale","platePale",
				"alloysMagical","platePale","alloysMagical",
		});

		addRecipe(new ItemStack(ItemsCore.genericItem,1,77),new Object[]{
				"alloysMagical",gen(79),"alloysMagical",
				gen(79),Items.LAVA_BUCKET,gen(79),
				"alloysMagical",gen(79),"alloysMagical",
		});

		addRecipe(new ItemStack(ItemsCore.genericItem,3,19),new Object[]{
				"alloysMagical","plateEnder","alloysMagical",
				"plateEnder","dustCrystal","plateEnder",
				"alloysMagical","plateEnder","alloysMagical",
		});

		addRecipe(new ItemStack(ItemsCore.spawnerCollector,1,0),new Object[]{
				"dustCrystal","focusWater","focusWater",
				"ingotMagic","redSoulMatter","focusWater",
				"plateMagic","ingotMagic","dustCrystal",
		});

		addRecipe(new ItemStack(ItemsCore.magicalDigger,1,0),new Object[]{
				Blocks.TNT,"focusEarth","redSoulMatter",
				"focusFire",ItemsCore.elemental_pick,"focusEarth",
				"plateMagic","focusFire",Blocks.TNT,
		});

		addRecipe(new ItemStack(ItemsCore.staffOfLife,1,0),new Object[]{
				ItemsCore.elemental_hoe,"focusEarth",new ItemStack(ItemsCore.genericItem,1,4),
				"focusEarth","redSoulMatter","focusEarth",
				"plateMagic","focusEarth",ItemsCore.elemental_hoe,
		});

		addRecipe(new ItemStack(ItemsCore.emeraldHeart,1,0),new Object[]{
				"focusEarth","gemEmerald","focusEarth",
				"magicWater","redSoulMatter","magicWater",
				"focusWater",Items.APPLE,"focusWater",
		});
		addRecipe(new ItemStack(ItemsCore.magicalShield,1,0),new Object[]{
				"plateObsidian","alloysMagical","plateObsidian",
				"focusEarth","redSoulMatter","focusEarth",
				"dustCrystal","plateObsidian","dustCrystal",
		});
		addRecipe(new ItemStack(ItemsCore.spikyShield,1,0),new Object[]{
				ItemsCore.elemental_sword,"gemNetherStar",ItemsCore.elemental_sword,
				"alloysMagical",ItemsCore.magicalShield,"alloysMagical",
				"alloysMagical","matterOfEternity","alloysMagical",
		});
		addRecipe(new ItemStack(ItemsCore.magicWaterBottle,1,0),new Object[]{
				"focusAir","magicWater","focusEarth",
				"plateDiamond","redSoulMatter","plateDiamond",
				"focusWater","magicWater","focusWater",
		});
		addRecipe(new ItemStack(ItemsCore.magicalPorkchop,1,0),new Object[]{
				"focusEarth",Items.PORKCHOP,"focusEarth",
				"dustCrystal","redSoulMatter","dustCrystal",
				"focusWater","dustCrystal","focusWater",
		});
		addRecipe(new ItemStack(ItemsCore.magicalWings,1,0),new Object[]{
				"plateMagic","focusAir","focusAir",
				"plateMagic",Items.FEATHER,Items.FEATHER,
				"redSoulMatter",Items.FEATHER,Items.FEATHER,
		});
		addRecipe(new ItemStack(ItemsCore.holyMace,1,0),new Object[]{
				"redSoulMatter","focusAir",new ItemStack(ItemsCore.genericItem,1,4),
				"ingotMagic",ItemsCore.elemental_sword,"focusAir",
				"focusEarth","ingotMagic","redSoulMatter",
		});
		addRecipe(new ItemStack(ItemsCore.chaosFork,1,0),new Object[]{
				"ingotMagic","focusFire","matterOfEternity",
				"ingotMagic",ItemsCore.elemental_sword,"focusFire",
				new ItemStack(ItemsCore.matrixProj,1,1),"ingotMagic","ingotMagic",
		});
		addRecipe(new ItemStack(ItemsCore.frozenMace,1,0),new Object[]{
				"ingotMagic","focusWater","matterOfEternity",
				"ingotMagic",ItemsCore.elemental_sword,"focusWater",
				new ItemStack(ItemsCore.matrixProj,1,2),"ingotMagic","ingotMagic",
		});

		addRecipe(new ItemStack(ItemsCore.windTablet,1,0),new Object[]{
				"focusAir","plateFortified","focusAir",
				"plateFortified",ItemsCore.windKeeper,"plateFortified",
				"focusAir","plateFortified","focusAir",
		});

		addRecipe(new ItemStack(BlocksCore.magicalTeleporter,1,0),new Object[]{
				"screenMagic","voidMRU","worldInteractor",
				"plateMagic","voidCore","plateMagic",
				"plateVoid","matterOfEternity","plateVoid",
		});

		addRecipe(new ItemStack(BlocksCore.darknessObelisk,1,0),new Object[]{
				"plateVoid","voidMRU","plateVoid",
				"plateVoid","matterOfEternity","plateVoid",
				"plateVoid","voidMRU","plateVoid",
		});
		addRecipe(new ItemStack(BlocksCore.ultraHeatGen,1,0),new Object[]{
				"plateVoid","voidMRU","plateVoid",
				"voidCore",new ItemStack(BlocksCore.heatGenerator),"voidCore",
				"plateVoid","voidMRU","plateVoid",
		});
		addRecipe(new ItemStack(BlocksCore.ultraFlowerBurner,1,0),new Object[]{
				"plateVoid","voidMRU","plateVoid",
				"voidCore",new ItemStack(BlocksCore.naturalFurnace),"voidCore",
				"plateVoid","voidMRU","plateVoid",
		});

		addRecipe(new ItemStack(BlocksCore.magicalFurnace,1,0),new Object[]{
				"screenMagic","voidMRU","worldInteractor",
				"plateFortified","voidCore","plateFortified",
				"plateVoid","matterOfEternity","plateVoid",
		});

		addRecipe(new ItemStack(BlocksCore.emberForge,1,0),new Object[]{
				"screenMagic","voidMRU","plateEnder",
				"plateFortified","voidCore","plateFortified",
				"plateVoid","matterOfEternity","plateVoid",
		});

		addRecipe(new ItemStack(ItemsCore.magicArmorItems[0],1,0),new Object[]{
				"alloysMagical","worldInteractor","alloysMagical",
				"plateMagic","plateGlass","plateMagic",
				"dustCrystal","dustCrystal","dustCrystal",
		});

		addRecipe(new ItemStack(ItemsCore.magicArmorItems[1],1,0),new Object[]{
				"worldInteractor","dustCrystal","worldInteractor",
				"plateMagic","alloysMagical","plateMagic",
				"plateMagic","alloysMagical","plateMagic",
		});

		addRecipe(new ItemStack(ItemsCore.magicArmorItems[2],1,0),new Object[]{
				"alloysMagical","worldInteractor","alloysMagical",
				"plateMagic","dustCrystal","plateMagic",
				"plateMagic","dustCrystal","plateMagic",
		});

		addRecipe(new ItemStack(ItemsCore.magicArmorItems[3],1,0),new Object[]{
				"dustCrystal","worldInteractor","dustCrystal",
				"plateMagic","dustCrystal","plateMagic",
				"alloysMagical","dustCrystal","alloysMagical",
		});

		addRecipe(new ItemStack(ItemsCore.magicArmorItems[4],1,0),new Object[]{
				"gemNetherStar","voidMRU","gemNetherStar",
				"voidCore",new ItemStack(ItemsCore.magicArmorItems[0],1,0),"voidCore",
				"plateVoid","matterOfEternity","plateVoid",
		});

		addRecipe(new ItemStack(ItemsCore.magicArmorItems[5],1,0),new Object[]{
				"voidMRU","gemNetherStar","voidMRU",
				"voidCore",new ItemStack(ItemsCore.magicArmorItems[1],1,0),"voidCore",
				"plateVoid","matterOfEternity","plateVoid",
		});

		addRecipe(new ItemStack(ItemsCore.magicArmorItems[6],1,0),new Object[]{
				"gemNetherStar","voidMRU","gemNetherStar",
				"voidCore",new ItemStack(ItemsCore.magicArmorItems[2],1,0),"voidCore",
				"plateVoid","matterOfEternity","plateVoid",
		});

		addRecipe(new ItemStack(ItemsCore.magicArmorItems[7],1,0),new Object[]{
				"gemNetherStar","voidMRU","gemNetherStar",
				"voidCore",new ItemStack(ItemsCore.magicArmorItems[3],1,0),"voidCore",
				"plateVoid","matterOfEternity","plateVoid",
		});

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.fence[0],16,0), new Object[]{
				"   ",
				"DDD",
				"DDD",
				'D',BlocksCore.voidStone
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.fence[1],16,0), new Object[]{
				"   ",
				"DDD",
				"DDD",
				'D',BlocksCore.magicPlating
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.fence[2],16,0), new Object[]{
				"   ",
				"DDD",
				"DDD",
				'D',BlocksCore.fortifiedStone
		}));

		addRecipe(new ItemStack(ItemsCore.magmaticStaff,1,0),new Object[]{
				"focusFire","focusFire",BlocksCore.magmaticSmeltery,
				"dustCrystal","matterOfEternity","focusFire",
				"plateMagic","dustCrystal","focusFire",
		});

		addRecipe(new ItemStack(ItemsCore.magicalLantern,1,0),new Object[]{
				"focusAir","focusFire","redSoulMatter",
				ItemsCore.magicalSlag,"plateMagic","focusFire",
				"plateMagic",ItemsCore.magicalSlag,"focusAir",
		});

		addRecipe(new ItemStack(ItemsCore.magnetizingStaff,1,0),new Object[]{
				"focusAir","orbGold","darkSoulMatter",
				ItemsCore.magicalSlag,"plateMagic","orbGold",
				"plateMagic",ItemsCore.magicalSlag,"focusAir",
		});

		addRecipe(new ItemStack(BlocksCore.mruCoilHardener,3,0),new Object[]{
				"plateMagic","elementalCore","plateMagic",
				"plateMagic","magnet","plateMagic",
				"plateMagic","mruLink","plateMagic",
		});

		addRecipe(new ItemStack(BlocksCore.mruCoil,1,0),new Object[]{
				"worldInteractor","resonatingCrystal","screenMagic",
				"plateMagic","magnet","plateMagic",
				"mruLink",new ItemStack(ItemsCore.matrixProj,1,3),"mruLink",
		});

		addRecipe(new ItemStack(BlocksCore.corruptionCleaner,1,0),new Object[]{
				"screenMagic","elementalCore","plateFortified",
				"plateRedstone","resonatingCrystal","plateEnder",
				"plateFortified","worldInteractor","plateFortified",
		});

		addRecipe(new ItemStack(BlocksCore.reactorSupport,2,0),new Object[]{
				"magnet","resonatingCrystal","magnet",
				"plateMagic","elementalCore","plateMagic",
				"plateMagic","plateEnder","plateMagic",
		});

		addRecipe(new ItemStack(BlocksCore.reactor,1,0),new Object[]{
				new ItemStack(ItemsCore.matrixProj,1,3),"gemNetherStar",new ItemStack(ItemsCore.matrixProj,1,3),
				"resonatingCrystal","magnet","resonatingCrystal",
				"plateMagic","plateMagic","plateMagic",
		});

		ItemStack book = new ItemStack(ItemsCore.research_book);
		MiscUtils.getStackTag(book).setInteger("tier", 0);

		addRecipe(book,new Object[]{
				"dyeRed","itemBook","itemFeather",
				"dyeGreen","itemBook","dyeBlack",
				"dyeBlue","itemBook","dyeBlack",
		});

		ItemStack book_t1 = new ItemStack(ItemsCore.research_book);
		MiscUtils.getStackTag(book_t1).setInteger("tier", 1);

		addRecipe(book_t1,new Object[]{
				"elementalCore","shardElemental","elementalCore",
				"shardElemental",book,"shardElemental",
				"elementalCore","shardElemental","elementalCore",
		});

		ItemStack book_t2 = new ItemStack(ItemsCore.research_book);
		MiscUtils.getStackTag(book_t2).setInteger("tier", 2);

		ItemStack book_t3 = new ItemStack(ItemsCore.research_book);
		MiscUtils.getStackTag(book_t3).setInteger("tier", 3);

		addRecipe(book_t3,new Object[]{
				"plateVoid","resonatingCrystal","plateVoid",
				"resonatingCrystal",book_t2,"resonatingCrystal",
				"plateVoid","resonatingCrystal","plateVoid",
		});

		registerEFuelCrafts();
		registerCharmsCraft();

		addRecipe(new ItemStack(BlocksCore.crystalLamp,1,0), new Object[] {
				"cobblestone","shardFire","cobblestone",
				"shardFire",Blocks.TORCH,"shardFire",
				"cobblestone","shardFire","cobblestone",
		});
		addRecipe(new ItemStack(BlocksCore.crystalLamp,1,1), new Object[] {
				"cobblestone","shardWater","cobblestone",
				"shardWater",Blocks.TORCH,"shardWater",
				"cobblestone","shardWater","cobblestone",
		});
		addRecipe(new ItemStack(BlocksCore.crystalLamp,1,2), new Object[] {
				"cobblestone","shardEarth","cobblestone",
				"shardEarth",Blocks.TORCH,"shardEarth",
				"cobblestone","shardEarth","cobblestone",
		});
		addRecipe(new ItemStack(BlocksCore.crystalLamp,1,3), new Object[] {
				"cobblestone","shardAir","cobblestone",
				"shardAir",Blocks.TORCH,"shardAir",
				"cobblestone","shardAir","cobblestone",
		});
		addRecipe(new ItemStack(BlocksCore.crystalLamp,1,4), new Object[] {
				"cobblestone","shardElemental","cobblestone",
				"shardElemental",Blocks.TORCH,"shardElemental",
				"cobblestone","shardElemental","cobblestone",
		});
		addRecipe(new ItemStack(BlocksCore.crystalLamp,1,5), new Object[] {
				"cobblestone","dustMithriline","cobblestone",
				"dustMithriline",Blocks.TORCH,"dustMithriline",
				"cobblestone","dustMithriline","cobblestone",
		});

		registerFancyBlockCrafts();

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlocksCore.mimic,4,0), new Object[]{
				"DSD",
				"SGS",
				"DSD",
				'D',BlocksCore.fortifiedStone,
				'S',ItemsCore.magicalSlag,
				'G',BlocksCore.fortifiedGlass,
		}));
	}

	public static void addRecipe(ItemStack output, Object... recipe) {
		GameRegistry.addRecipe(new ShapedOreRecipe(output, new Object[]{
				"123",
				"456",
				"789",
				'1',recipe[0],
				'2',recipe[1],
				'3',recipe[2],
				'4',recipe[3],
				'5',recipe[4],
				'6',recipe[5],
				'7',recipe[6],
				'8',recipe[7],
				'9',recipe[8],
		}));
	}

	private static void registerRadiatingChamber() {
		//RadiatingChamberRecipes
		RadiatingChamberRecipes.addRecipeIS(new ItemStack[]{new ItemStack(Blocks.STONE),new ItemStack(Items.IRON_INGOT)}, getItemByNameEC3("fortifiedStone",0), 10, new float[]{Float.MAX_VALUE,-Float.MAX_VALUE},4);
		RadiatingChamberRecipes.addRecipeIS(new ItemStack[]{new ItemStack(Blocks.GLASS),new ItemStack(Items.IRON_INGOT)}, getItemByNameEC3("fortifiedGlass",0), 10, new float[]{Float.MAX_VALUE,-Float.MAX_VALUE},4);
		RadiatingChamberRecipes.addRecipeIS(new ItemStack[]{new ItemStack(Items.REDSTONE),new ItemStack(Items.BLAZE_POWDER)}, getItemByNameEC3("genericItem",3), 100, new float[]{Float.MAX_VALUE,-Float.MAX_VALUE},1);
		RadiatingChamberRecipes.addRecipeIS(new ItemStack[]{getItemByNameEC3("soulStone",0),null}, getItemByNameEC3("matrixProj",0), 1000, new float[]{Float.MAX_VALUE,-Float.MAX_VALUE},1);
		RadiatingChamberRecipes.addRecipeIS(new ItemStack[]{getItemByNameEC3("matrixProj",0),null}, getItemByNameEC3("matrixProj",1), 10000, new float[]{Float.MAX_VALUE,1.5F},1);
		RadiatingChamberRecipes.addRecipeIS(new ItemStack[]{getItemByNameEC3("matrixProj",0),null}, getItemByNameEC3("matrixProj",2), 10000, new float[]{0.5F,-Float.MAX_VALUE},1);
		RadiatingChamberRecipes.addRecipeIS(new ItemStack[]{getItemByNameEC3("matrixProj",0),null}, getItemByNameEC3("matrixProj",3), 20000, new float[]{1.5F,0.5F},1);
		RadiatingChamberRecipes.addRecipeIS(new ItemStack[]{new ItemStack(Items.DYE,1,4),new ItemStack(Items.GLOWSTONE_DUST)}, getItemByNameEC3("genericItem",38), 250, new float[]{Float.MAX_VALUE,-Float.MAX_VALUE},20,1);
		RadiatingChamberRecipes.addRecipeIS(new ItemStack[]{new ItemStack(Blocks.LAPIS_BLOCK,1,0),new ItemStack(Items.GOLD_INGOT)}, getItemByNameEC3("genericItem",39), 100, new float[]{Float.MAX_VALUE,-Float.MAX_VALUE},80,4);
		RadiatingChamberRecipes.addRecipeIS(new ItemStack[]{new ItemStack(BlocksCore.blockPale,1,0),new ItemStack(Items.DIAMOND)}, getItemByNameEC3("genericItem",40), 250, new float[]{Float.MAX_VALUE,-Float.MAX_VALUE},120,4);
		RadiatingChamberRecipes.addRecipeIS(new ItemStack[]{new ItemStack(BlocksCore.blockPale,1,0),new ItemStack(Items.EMERALD)}, getItemByNameEC3("genericItem",40), 300, new float[]{Float.MAX_VALUE,-Float.MAX_VALUE},100,4);
		RadiatingChamberRecipes.addRecipeIS(new ItemStack[]{new ItemStack(Blocks.STONE),new ItemStack(ItemsCore.drops,1,4)}, getItemByNameEC3("genericItem",42), 100, new float[]{Float.MAX_VALUE,-Float.MAX_VALUE},10,1);
		RadiatingChamberRecipes.addRecipeIS(new ItemStack[]{new ItemStack(Items.IRON_INGOT),new ItemStack(ItemsCore.genericItem,1,3)}, getItemByNameEC3("genericItem",43), 1000, new float[]{Float.MAX_VALUE,-Float.MAX_VALUE},1,1);
		RadiatingChamberRecipes.addRecipeIS(new ItemStack[]{new ItemStack(Items.DIAMOND),new ItemStack(Items.EMERALD)}, getItemByNameEC3("genericItem",44), 100, new float[]{Float.MAX_VALUE,-Float.MAX_VALUE},50,1);
	}

	private static void registerMagicianTable() {
		//MagicianTableRecipes
		MagicianTableRecipes.addRecipeIS(new ItemStack[]{getItemByNameEC3("genericItem",7),getItemByNameEC3("genericItem",79),getItemByNameEC3("genericItem",8),getItemByNameEC3("genericItem",8),getItemByNameEC3("genericItem",79)},getItemByNameEC3("genericItem",0), 10000);
		MagicianTableRecipes.addRecipeIS(new ItemStack[]{getItemByNameEC3("genericItem",7),getItemByNameEC3("genericItem",8),getItemByNameEC3("genericItem",79),getItemByNameEC3("genericItem",79),getItemByNameEC3("genericItem",8)},getItemByNameEC3("genericItem",0), 10000);
		MagicianTableRecipes.addRecipeIS(new ItemStack[]{new ItemStack(Items.ENDER_PEARL),getItemByNameEC3("genericItem",10),getItemByNameEC3("genericItem",10),getItemByNameEC3("genericItem",10),getItemByNameEC3("genericItem",10)},getItemByNameEC3("genericItem",4), 5000);
		MagicianTableRecipes.addRecipeIS(new ItemStack[]{new ItemStack(Items.IRON_INGOT),null,null,null,null},getItemByNameEC3("genericItem",5), 50);
		MagicianTableRecipes.addRecipeIS(new ItemStack[]{new ItemStack(Items.POTIONITEM),null,null,null,null},getItemByNameEC3("genericItem",6), 250);
		MagicianTableRecipes.addRecipeIS(new ItemStack[]{getItemByNameEC3("fortifiedStone",OreDictionary.WILDCARD_VALUE),null,null,null,null},getItemByNameEC3("genericItem",7), 10);
		MagicianTableRecipes.addRecipeIS(new ItemStack[]{getItemByNameEC3("genericItem",7),new ItemStack(Items.ENDER_PEARL),new ItemStack(Items.ENDER_PEARL),new ItemStack(Items.ENDER_PEARL),new ItemStack(Items.ENDER_PEARL)},getItemByNameEC3("genericItem",8), 1000);
		MagicianTableRecipes.addRecipeIS(new ItemStack[]{getItemByNameEC3("genericItem",7),new ItemStack(Blocks.GLASS),new ItemStack(Blocks.GLASS),new ItemStack(Blocks.GLASS),new ItemStack(Blocks.GLASS)},getItemByNameEC3("genericItem",9), 1000);
		MagicianTableRecipes.addRecipeIS(new ItemStack[]{new ItemStack(Items.GOLD_INGOT),new ItemStack(Items.GOLD_NUGGET),new ItemStack(Items.GOLD_NUGGET),new ItemStack(Items.GOLD_NUGGET),new ItemStack(Items.GOLD_NUGGET)},getItemByNameEC3("genericItem",10), 250);
		MagicianTableRecipes.addRecipeIS(new ItemStack[]{getItemByNameEC3("genericItem",7),new ItemStack(Items.REDSTONE),new ItemStack(Items.REDSTONE),new ItemStack(Items.REDSTONE),new ItemStack(Items.REDSTONE)},getItemByNameEC3("genericItem",11), 1000);
		MagicianTableRecipes.addRecipeIS(new ItemStack[]{new ItemStack(Items.QUARTZ),null,null,null,null},getItemByNameEC3("genericItem",12), 10);
		MagicianTableRecipes.addRecipeIS(new ItemStack[]{getItemByNameEC3("genericItem",12),null,null,null,null},getItemByNameEC3("genericItem",13), 100);
		MagicianTableRecipes.addRecipeIS(new ItemStack[]{getItemByNameEC3("genericItem",13),null,null,null,null},getItemByNameEC3("genericItem",14), 200);
		MagicianTableRecipes.addRecipeIS(new ItemStack[]{getItemByNameEC3("genericItem",14),null,null,null,null},getItemByNameEC3("genericItem",15), 500);
		MagicianTableRecipes.addRecipeIS(new ItemStack[]{getItemByNameEC3("genericItem",15),null,null,null,null},getItemByNameEC3("genericItem",16), 1000);
		MagicianTableRecipes.addRecipeIS(new ItemStack[]{getItemByNameEC3("genericItem",3),null,null,null,null},getItemByNameEC3("genericItem",20), 3000);
		MagicianTableRecipes.addRecipeIS(new ItemStack[]{getItemByNameEC3("genericItem",9),getItemByNameEC3("genericItem",21),getItemByNameEC3("genericItem",22),getItemByNameEC3("genericItem",22),getItemByNameEC3("genericItem",21)},getItemByNameEC3("genericItem",32), 10000);
		MagicianTableRecipes.addRecipeIS(new ItemStack[]{getItemByNameEC3("genericItem",9),getItemByNameEC3("genericItem",22),getItemByNameEC3("genericItem",21),getItemByNameEC3("genericItem",21),getItemByNameEC3("genericItem",22)},getItemByNameEC3("genericItem",32), 10000);
		MagicianTableRecipes.addRecipeIS(new UnformedItemStack[]{new UnformedItemStack(getItemByNameEC3("genericItem",7)),new UnformedItemStack("ingotThaumium"),new UnformedItemStack("ingotThaumium"),new UnformedItemStack("ingotThaumium"),new UnformedItemStack("ingotThaumium")},getItemByNameEC3("genericItem",34), 100);
		MagicianTableRecipes.addRecipeIS(new ItemStack[]{getItemByNameEC3("genericItem",7),getItemByNameEC3("genericItem",39),getItemByNameEC3("genericItem",39),getItemByNameEC3("genericItem",39),getItemByNameEC3("genericItem",39)},getItemByNameEC3("genericItem",41), 100);
		MagicianTableRecipes.addRecipeIS(new ItemStack[]{new ItemStack(Items.QUARTZ),getItemByNameEC3("genericItem",12),getItemByNameEC3("genericItem",12),getItemByNameEC3("genericItem",12),getItemByNameEC3("genericItem",12)},getItemByNameEC3("storage",0), 100);
		MagicianTableRecipes.addRecipeIS(new ItemStack[]{new ItemStack(Items.EMERALD),getItemByNameEC3("genericItem",13),getItemByNameEC3("genericItem",13),getItemByNameEC3("genericItem",13),getItemByNameEC3("genericItem",13)},getItemByNameEC3("storage",1), 500);
		MagicianTableRecipes.addRecipeIS(new ItemStack[]{new ItemStack(Items.ENDER_PEARL),getItemByNameEC3("genericItem",14),getItemByNameEC3("genericItem",14),getItemByNameEC3("genericItem",14),getItemByNameEC3("genericItem",14)},getItemByNameEC3("storage",2), 100);
		MagicianTableRecipes.addRecipeIS(new ItemStack[]{new ItemStack(Items.DIAMOND),getItemByNameEC3("genericItem",15),getItemByNameEC3("genericItem",15),getItemByNameEC3("genericItem",15),getItemByNameEC3("genericItem",15)},getItemByNameEC3("storage",3), 250);
		MagicianTableRecipes.addRecipeIS(new ItemStack[]{new ItemStack(Items.NETHER_STAR),getItemByNameEC3("genericItem",16),getItemByNameEC3("genericItem",16),getItemByNameEC3("genericItem",16),getItemByNameEC3("genericItem",16)},getItemByNameEC3("storage",4), 500);
		MagicianTableRecipes.addRecipeIS(new UnformedItemStack[]{new UnformedItemStack(getItemByNameEC3("genericItem",7)),new UnformedItemStack("ingotMithriline"),new UnformedItemStack("ingotMithriline"),new UnformedItemStack("ingotMithriline"),new UnformedItemStack("ingotMithriline")},getItemByNameEC3("genericItem",49), 400);
		MagicianTableRecipes.addRecipeIS(new UnformedItemStack[]{new UnformedItemStack(getItemByNameEC3("genericItem",7)),new UnformedItemStack("ingotDemonic"),new UnformedItemStack("ingotDemonic"),new UnformedItemStack("ingotDemonic"),new UnformedItemStack("ingotDemonic")},new ItemStack(ItemsCore.genericItem,4,54), 2000);
		MagicianTableRecipes.addRecipeIS(new ItemStack[]{getItemByNameEC3("genericItem",7),new ItemStack(Items.BLAZE_POWDER),new ItemStack(Items.BLAZE_POWDER),new ItemStack(Items.BLAZE_POWDER),new ItemStack(Items.BLAZE_POWDER)},getItemByNameEC3("genericItem",79), 500);
		ItemStack book_t1 = new ItemStack(ItemsCore.research_book);
		MiscUtils.getStackTag(book_t1).setInteger("tier", 1);
		ItemStack book_t2 = new ItemStack(ItemsCore.research_book);
		MiscUtils.getStackTag(book_t2).setInteger("tier", 2);
		MagicianTableRecipes.addRecipeIS(new ItemStack[]{book_t1,null,null,null,null},book_t2, 100);
		MagicianTableRecipes.addRecipeIS(new ItemStack[]{new ItemStack(BlocksCore.voidStone),null,null,null,null},getItemByNameEC3("genericItem",35), 1000);
	}

	private static void registerMithrilineFurnace() {
		//MithrilineFurnaceRecipes
		MithrilineFurnaceRecipes.addRecipe("dustMithriline", getItemByNameEC3("genericItem",50), 60,1);
		MithrilineFurnaceRecipes.addRecipe("gemResonant", getItemByNameEC3("genericItem",48), 120,1);
		MithrilineFurnaceRecipes.addRecipe("gemMithriline", getItemByNameEC3("genericItem",47), 240,1);
		MithrilineFurnaceRecipes.addRecipe(getItemByNameEC3("genericItem",47), new ItemStack(ItemsCore.genericItem,8,46), 480,1);
		MithrilineFurnaceRecipes.addRecipe("dustGlowstone", getItemByNameEC3("genericItem",51), 32,1);
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
		//MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.FIRE,1,0), new ItemStack(Blocks.COAL_BLOCK,1,0), 512,1);
		//MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.COAL_BLOCK,1,0), new ItemStack(Blocks.FIRE,1,0), 512,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.ICE,1,0), new ItemStack(Blocks.GRASS,1,0), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.GRASS,1,0), new ItemStack(Blocks.DIRT,1,0), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.DIRT,1,0), new ItemStack(Blocks.DIRT,1,2), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.DIRT,1,2), new ItemStack(Blocks.GLASS,1,0), 1,1);
		MithrilineFurnaceRecipes.addRecipe(new ItemStack(Blocks.GLASS,1,0), new ItemStack(Blocks.ICE,1,0), 1,1);
	}

	private static void registerWindRecipes() {
		new WindImbueRecipe(new ItemStack(ItemsCore.soulStone,1,0),new ItemStack(ItemsCore.soulStone,1,0),40000);
		new WindImbueRecipe(new ItemStack(Items.DIAMOND,1,0),new ItemStack(ItemsCore.genericItem,1,55),10000);
		new WindImbueRecipe(new ItemStack(Items.POTIONITEM,1,0),new ItemStack(ItemsCore.air_potion,1,0),250);
		//wind recipes
	}

	private static void registerDemonTrades() {
		//demon trades
		new DemonTrade(EntityVillager.class);
		new DemonTrade(EntityEnderman.class);
		new DemonTrade(EntityWindMage.class);
		new DemonTrade(new ItemStack(Blocks.DIAMOND_BLOCK,2,0));
		new DemonTrade(new ItemStack(Blocks.BEDROCK,1,0));
		new DemonTrade(new ItemStack(Blocks.IRON_BLOCK,8,0));
		new DemonTrade(new ItemStack(Blocks.TNT,64,0));
		new DemonTrade(new ItemStack(Blocks.ENCHANTING_TABLE,16,0));
		new DemonTrade(new ItemStack(Blocks.BOOKSHELF,64,0));
		new DemonTrade(new ItemStack(Blocks.END_PORTAL_FRAME,3,0));
		new DemonTrade(new ItemStack(Blocks.DRAGON_EGG,1,0));
		new DemonTrade(new ItemStack(Blocks.BEACON,1,0));
		new DemonTrade(new ItemStack(Blocks.COMMAND_BLOCK,1,0));
		new DemonTrade(new ItemStack(Blocks.CHAIN_COMMAND_BLOCK,1,0));
		new DemonTrade(new ItemStack(Blocks.REPEATING_COMMAND_BLOCK,1,0));
		new DemonTrade(new ItemStack(Items.GOLDEN_APPLE,1,1));
		new DemonTrade(new ItemStack(Items.MAP,1,OreDictionary.WILDCARD_VALUE));
		new DemonTrade(new ItemStack(Items.ENDER_EYE,32,0));
		new DemonTrade(new ItemStack(Items.SKULL,1,3));
		new DemonTrade(new ItemStack(Items.NETHER_STAR,1,0));
		new DemonTrade(new ItemStack(Items.RECORD_11,1,0));
		new DemonTrade(new ItemStack(BlocksCore.magicalEnchanter,1,0));
		new DemonTrade(new ItemStack(BlocksCore.magicalRepairer,1,0));
		new DemonTrade(new ItemStack(BlocksCore.heatGenerator,3,0));
		new DemonTrade(new ItemStack(BlocksCore.magicalTeleporter,2,0));
		new DemonTrade(new ItemStack(BlocksCore.mruCoil,1,0));
		new DemonTrade(new ItemStack(BlocksCore.ultraHeatGen,1,0));
		new DemonTrade(new ItemStack(BlocksCore.darknessObelisk,1,0));
		new DemonTrade(new ItemStack(BlocksCore.mithrilineCrystal,12,0));
		new DemonTrade(new ItemStack(BlocksCore.mithrilineCrystal,6,3));
		new DemonTrade(new ItemStack(BlocksCore.mithrilineCrystal,3,6));
		new DemonTrade(new ItemStack(BlocksCore.compressed,32,4));
		new DemonTrade(new ItemStack(ItemsCore.emeraldHeart,1,0));
		new DemonTrade(new ItemStack(ItemsCore.spikyShield,1,0));
		new DemonTrade(new ItemStack(ItemsCore.magicalShield,1,0));
		new DemonTrade(new ItemStack(ItemsCore.magicWaterBottle,1,0));
		new DemonTrade(new ItemStack(ItemsCore.wind_elemental_pick,1,0));
		new DemonTrade(new ItemStack(ItemsCore.wind_elemental_axe,1,0));
		new DemonTrade(new ItemStack(ItemsCore.wind_elemental_hoe,1,0));
		new DemonTrade(new ItemStack(ItemsCore.wind_elemental_shovel,1,0));
		new DemonTrade(new ItemStack(ItemsCore.wind_elemental_sword,1,0));
		new DemonTrade(new ItemStack(ItemsCore.wind_chestplate,1,0));
		new DemonTrade(new ItemStack(ItemsCore.wind_helmet,1,0));
		new DemonTrade(new ItemStack(ItemsCore.wind_boots,1,0));
		new DemonTrade(new ItemStack(ItemsCore.wind_leggings,1,0));
		new DemonTrade(new ItemStack(ItemsCore.magicalPorkchop,1,0));
		new DemonTrade(new ItemStack(ItemsCore.chaosFork,1,0));
		new DemonTrade(new ItemStack(ItemsCore.research_book,1,0));
		new DemonTrade(new ItemStack(ItemsCore.record_everlastingSummer,1,0));
		new DemonTrade(new ItemStack(ItemsCore.pistol,1,0));
		new DemonTrade(new ItemStack(ItemsCore.rifle,1,0));
		new DemonTrade(new ItemStack(ItemsCore.sniper,1,0));
		new DemonTrade(new ItemStack(ItemsCore.gatling,1,0));
	}

	public static ItemStack getItemByNameEC3(String itemName, int metadata) {
		Class<BlocksCore> blocks = BlocksCore.class;
		try {
			Field block = blocks.getDeclaredField(itemName);
			ItemStack is = new ItemStack((Block)block.get(null),1,metadata);
			return is;
		}
		catch (Exception e) {
			try {
				Class<ItemsCore> items = ItemsCore.class;
				Field item = items.getDeclaredField(itemName);
				ItemStack is = new ItemStack((Item)item.get(null),1,metadata);
				return is;
			}
			catch (Exception e1) {
				e.printStackTrace();
				e1.printStackTrace();
				return null;
			}
		}
	}

	private static void registerEFuelCrafts() {
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.elementalFuel,1,0), new Object[]{
				" E ","ECE"," E ", 'E', new ItemStack(ItemsCore.essence,1,0),'C',Items.COAL
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.elementalFuel,4,0), new Object[]{
				" E ","ECE"," E ", 'E', new ItemStack(ItemsCore.essence,1,4),'C',Items.COAL
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.elementalFuel,8,0), new Object[]{
				" E ","ECE"," E ", 'E', new ItemStack(ItemsCore.essence,1,8),'C',Items.COAL
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.elementalFuel,16,0), new Object[]{
				" E ","ECE"," E ", 'E', new ItemStack(ItemsCore.essence,1,12),'C',Items.COAL
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.elementalFuel,1,1), new Object[]{
				" E ","ECE"," E ", 'E', new ItemStack(ItemsCore.essence,1,1),'C',Items.SNOWBALL
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.elementalFuel,4,1), new Object[]{
				" E ","ECE"," E ", 'E', new ItemStack(ItemsCore.essence,1,5),'C',Items.SNOWBALL
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.elementalFuel,8,1), new Object[]{
				" E ","ECE"," E ", 'E', new ItemStack(ItemsCore.essence,1,9),'C',Items.SNOWBALL
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.elementalFuel,16,1), new Object[]{
				" E ","ECE"," E ", 'E', new ItemStack(ItemsCore.essence,1,13),'C',Items.SNOWBALL
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.elementalFuel,1,2), new Object[]{
				" E ","ECE"," E ", 'E', new ItemStack(ItemsCore.essence,1,2),'C',Items.WHEAT_SEEDS
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.elementalFuel,4,2), new Object[]{
				" E ","ECE"," E ", 'E', new ItemStack(ItemsCore.essence,1,6),'C',Items.WHEAT_SEEDS
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.elementalFuel,8,2), new Object[]{
				" E ","ECE"," E ", 'E', new ItemStack(ItemsCore.essence,1,10),'C',Items.WHEAT_SEEDS
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.elementalFuel,16,2), new Object[]{
				" E ","ECE"," E ", 'E', new ItemStack(ItemsCore.essence,1,14),'C',Items.WHEAT_SEEDS
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.elementalFuel,1,3), new Object[]{
				" E ","ECE"," E ", 'E', new ItemStack(ItemsCore.essence,1,3)
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.elementalFuel,4,3), new Object[]{
				" E ","ECE"," E ", 'E', new ItemStack(ItemsCore.essence,1,7)
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.elementalFuel,8,3), new Object[]{
				" E ","ECE"," E ", 'E', new ItemStack(ItemsCore.essence,1,11)
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.elementalFuel,16,3), new Object[]{
				" E ","ECE"," E ", 'E', new ItemStack(ItemsCore.essence,1,15)
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.fFocus,1,0), new Object[]{
				"GIG","IEI","GIG", 'E', new ItemStack(ItemsCore.elementalFuel,1,0),'G',Items.GOLD_INGOT,'I',Items.IRON_INGOT
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.wFocus,1,0), new Object[]{
				"GIG","IEI","GIG", 'E', new ItemStack(ItemsCore.elementalFuel,1,1),'G',Items.GOLD_INGOT,'I',Items.IRON_INGOT
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.eFocus,1,0), new Object[]{
				"GIG","IEI","GIG", 'E', new ItemStack(ItemsCore.elementalFuel,1,2),'G',Items.GOLD_INGOT,'I',Items.IRON_INGOT
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.aFocus,1,0), new Object[]{
				"GIG","IEI","GIG", 'E', new ItemStack(ItemsCore.elementalFuel,1,3),'G',Items.GOLD_INGOT,'I',Items.IRON_INGOT
		}));
	}

	private static void registerCharmsCraft() {
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.charm,1,0), new Object[]{
				"SGS","FRF","@G@",
				'F',ItemsCore.fFocus,'W',ItemsCore.wFocus,'E',ItemsCore.eFocus,'A',ItemsCore.aFocus,
				'S',Items.STRING,'G',getItemByNameEC3("genericItem",10),'R',new ItemStack(ItemsCore.storage,1,3),'@',"magicWater"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.charm,1,1), new Object[]{
				"SGS","WRW","@G@",
				'F',ItemsCore.fFocus,'W',ItemsCore.wFocus,'E',ItemsCore.eFocus,'A',ItemsCore.aFocus,
				'S',Items.STRING,'G',getItemByNameEC3("genericItem",10),'R',new ItemStack(ItemsCore.storage,1,3),'@',"magicWater"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.charm,1,2), new Object[]{
				"SGS","ERE","@G@",
				'F',ItemsCore.fFocus,'W',ItemsCore.wFocus,'E',ItemsCore.eFocus,'A',ItemsCore.aFocus,
				'S',Items.STRING,'G',getItemByNameEC3("genericItem",10),'R',new ItemStack(ItemsCore.storage,1,3),'@',"magicWater"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.charm,1,3), new Object[]{
				"SGS","ARA","@G@",
				'F',ItemsCore.fFocus,'W',ItemsCore.wFocus,'E',ItemsCore.eFocus,'A',ItemsCore.aFocus,
				'S',Items.STRING,'G',getItemByNameEC3("genericItem",10),'R',new ItemStack(ItemsCore.storage,1,3),'@',"magicWater"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.charm,1,4), new Object[]{
				"SGS","FRA","@G@",
				'F',ItemsCore.fFocus,'W',ItemsCore.wFocus,'E',ItemsCore.eFocus,'A',ItemsCore.aFocus,
				'S',Items.STRING,'G',getItemByNameEC3("genericItem",10),'R',new ItemStack(ItemsCore.storage,1,3),'@',"magicWater"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.charm,1,4), new Object[]{
				"SGS","ARF","@G@",
				'F',ItemsCore.fFocus,'W',ItemsCore.wFocus,'E',ItemsCore.eFocus,'A',ItemsCore.aFocus,
				'S',Items.STRING,'G',getItemByNameEC3("genericItem",10),'R',new ItemStack(ItemsCore.storage,1,3),'@',"magicWater"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.charm,1,5), new Object[]{
				"SGS","FRE","@G@",
				'F',ItemsCore.fFocus,'W',ItemsCore.wFocus,'E',ItemsCore.eFocus,'A',ItemsCore.aFocus,
				'S',Items.STRING,'G',getItemByNameEC3("genericItem",10),'R',new ItemStack(ItemsCore.storage,1,3),'@',"magicWater"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.charm,1,5), new Object[]{
				"SGS","ERF","@G@",
				'F',ItemsCore.fFocus,'W',ItemsCore.wFocus,'E',ItemsCore.eFocus,'A',ItemsCore.aFocus,
				'S',Items.STRING,'G',getItemByNameEC3("genericItem",10),'R',new ItemStack(ItemsCore.storage,1,3),'@',"magicWater"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.charm,1,6), new Object[]{
				"SGS","FRW","@G@",
				'F',ItemsCore.fFocus,'W',ItemsCore.wFocus,'E',ItemsCore.eFocus,'A',ItemsCore.aFocus,
				'S',Items.STRING,'G',getItemByNameEC3("genericItem",10),'R',new ItemStack(ItemsCore.storage,1,3),'@',"magicWater"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.charm,1,6), new Object[]{
				"SGS","WRF","@G@",
				'F',ItemsCore.fFocus,'W',ItemsCore.wFocus,'E',ItemsCore.eFocus,'A',ItemsCore.aFocus,
				'S',Items.STRING,'G',getItemByNameEC3("genericItem",10),'R',new ItemStack(ItemsCore.storage,1,3),'@',"magicWater"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.charm,1,7), new Object[]{
				"SGS","ERW","@G@",
				'F',ItemsCore.fFocus,'W',ItemsCore.wFocus,'E',ItemsCore.eFocus,'A',ItemsCore.aFocus,
				'S',Items.STRING,'G',getItemByNameEC3("genericItem",10),'R',new ItemStack(ItemsCore.storage,1,3),'@',"magicWater"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.charm,1,7), new Object[]{
				"SGS","WRE","@G@",
				'F',ItemsCore.fFocus,'W',ItemsCore.wFocus,'E',ItemsCore.eFocus,'A',ItemsCore.aFocus,
				'S',Items.STRING,'G',getItemByNameEC3("genericItem",10),'R',new ItemStack(ItemsCore.storage,1,3),'@',"magicWater"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.charm,1,8), new Object[]{
				"SGS","WRA","@G@",
				'F',ItemsCore.fFocus,'W',ItemsCore.wFocus,'E',ItemsCore.eFocus,'A',ItemsCore.aFocus,
				'S',Items.STRING,'G',getItemByNameEC3("genericItem",10),'R',new ItemStack(ItemsCore.storage,1,3),'@',"magicWater"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.charm,1,8), new Object[]{
				"SGS","ARW","@G@",
				'F',ItemsCore.fFocus,'W',ItemsCore.wFocus,'E',ItemsCore.eFocus,'A',ItemsCore.aFocus,
				'S',Items.STRING,'G',getItemByNameEC3("genericItem",10),'R',new ItemStack(ItemsCore.storage,1,3),'@',"magicWater"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.charm,1,9), new Object[]{
				"SGS","ERA","@G@",
				'F',ItemsCore.fFocus,'W',ItemsCore.wFocus,'E',ItemsCore.eFocus,'A',ItemsCore.aFocus,
				'S',Items.STRING,'G',getItemByNameEC3("genericItem",10),'R',new ItemStack(ItemsCore.storage,1,3),'@',"magicWater"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemsCore.charm,1,9), new Object[]{
				"SGS","ARE","@G@",
				'F',ItemsCore.fFocus,'W',ItemsCore.wFocus,'E',ItemsCore.eFocus,'A',ItemsCore.aFocus,
				'S',Items.STRING,'G',getItemByNameEC3("genericItem",10),'R',new ItemStack(ItemsCore.storage,1,3),'@',"magicWater"
		}));
	}

	public static void registerFancyBlockCrafts() {
		for(Block fancy : BlocksCore.fancyBlocks) {
			ItemStack createdFrom = fancyBlockRecipes.get(fancy);

			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(fancy, 4, 1), new Object[] {
					createdFrom, new ItemStack(ItemsCore.magicalChisel, 1, OreDictionary.WILDCARD_VALUE)
			}));

			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(fancy, 4, 2), new Object[] {
					"## ",
					"## ",
					"   ",
					'#', new ItemStack(fancy, 1, 1)
			}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(fancy, 4, 7), new Object[] {
					" # ",
					"# #",
					" # ",
					'#', new ItemStack(fancy, 1, 2)
			}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(fancy, 4, 0), new Object[] {
					"# #",
					"   ",
					"# #",
					'#', new ItemStack(fancy, 1, 1)
			}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(fancy, 9, 3), new Object[] {
					"###",
					"###",
					"###",
					'#', new ItemStack(fancy, 1, 1)
			}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(fancy, 8, 4), new Object[] {
					"###",
					"# #",
					"###",
					'#', new ItemStack(fancy, 1, 2)
			}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(fancy, 4, 5), new Object[] {
					"## ",
					"## ",
					"   ",
					'#', new ItemStack(fancy, 1, 7)
			}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(fancy, 9, 6), new Object[] {
					"###",
					"@$@",
					"###",
					'#', new ItemStack(fancy, 1, 1),
					'@', new ItemStack(fancy, 1, 3),
					'$', new ItemStack(fancy, 1, 0)
			}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(fancy, 4, 8), new Object[] {
					" # ",
					"#@#",
					" # ",
					'#', new ItemStack(fancy, 1, 0),
					'@', new ItemStack(fancy, 1, 3)
			}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(fancy, 8, 9), new Object[] {
					"###",
					"#@#",
					"###",
					'#', new ItemStack(fancy, 1, 8),
					'@', new ItemStack(Items.REDSTONE, 1, 0)
			}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(fancy, 8, 10), new Object[] {
					"###",
					"#@#",
					"###",
					'#', new ItemStack(fancy, 1, 1),
					'@', new ItemStack(ItemsCore.genericItem, 1, 3)
			}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(fancy, 8, 11), new Object[] {
					"###",
					"#@#",
					"###",
					'#', new ItemStack(fancy, 1, 1),
					'@', new ItemStack(ItemsCore.genericItem, 1, 12)
			}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(fancy, 8, 12), new Object[] {
					"###",
					"#@#",
					"###",
					'#', new ItemStack(fancy, 1, 1),
					'@', new ItemStack(Items.IRON_INGOT)
			}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(fancy, 8, 13), new Object[] {
					"###",
					"#@#",
					"###",
					'#', new ItemStack(fancy, 1, 1),
					'@', new ItemStack(Items.LEATHER)
			}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(fancy, 4, 14), new Object[] {
					"## ",
					"## ",
					"   ",
					'#', new ItemStack(fancy, 1, 13)
			}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(fancy, 4, 15), new Object[] {
					"## ",
					"## ",
					"   ",
					'#', new ItemStack(fancy, 1, 12)
			}));
		}
	}

	public static ItemStack gen(int meta) {
		return new ItemStack(ItemsCore.genericItem,1,meta);
	}
}

