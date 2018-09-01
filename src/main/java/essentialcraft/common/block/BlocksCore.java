package essentialcraft.common.block;

import java.util.ArrayList;
import java.util.List;

import DummyCore.Registries.BlockRegistry;
import essentialcraft.common.item.ItemBlockElementalCrystal;
import essentialcraft.common.item.ItemBlockFancy;
import essentialcraft.common.item.ItemBlockGeneric;
import essentialcraft.common.item.ItemBlockMeta;
import essentialcraft.common.item.ItemBlockMithrilineCrystal;
import essentialcraft.common.item.ItemBlockRDNS;
import essentialcraft.common.item.ItemsCore;
import essentialcraft.common.mod.EssentialCraftCore;
import essentialcraft.common.registry.RecipesCore;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class BlocksCore {

	public static void loadBlocks() {
		drops = new BlockDrops().setUnlocalizedName("essentialcraft.drops");
		registerBlockSimple(new ItemBlockMeta(drops), "Drops");
		magicPlating = new BlockDyeable(Material.ROCK, MapColor.PURPLE).setHardness(3.0F).setResistance(15.0F).setUnlocalizedName("essentialcraft.magicPlatingBlock");
		registerBlockSimple(magicPlating, "magicPlating");
		fortifiedGlass = new BlockDyeable(Material.GLASS).setHardness(3.0F).setResistance(15.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.fortifiedGlass");
		registerBlockSimple(fortifiedGlass, "fortifiedGlass");
		ecController = new BlockMRUCUECController().setHardness(3.0F).setResistance(15.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.ecController");
		registerBlockSimple(ecController, "ecController");
		ecAcceptor = new BlockMRUCUECAcceptor().setHardness(3.0F).setResistance(15.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.ecAcceptor");
		registerBlockSimple(ecAcceptor, "ecAcceptor");
		ecBalancer = new BlockMRUCUECBalancer().setHardness(3.0F).setResistance(15.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.ecBalancer");
		registerBlockSimple(ecBalancer, "ecBalancer");
		ecEjector = new BlockMRUCUECEjector().setHardness(3.0F).setResistance(15.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.ecEjector");
		registerBlockSimple(ecEjector, "ecEjector");
		ecHoldingChamber = new BlockMRUCUECHoldingChamber().setHardness(3.0F).setResistance(15.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.ecHoldingChamber");
		registerBlockSimple(ecHoldingChamber, "ecHoldingChamber");
		ecStateChecker = new BlockMRUCUECStateChecker().setHardness(3.0F).setResistance(15.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.ecStateChecker");
		registerBlockSimple(ecStateChecker, "ecStateChecker");
		ecRedstoneController = new BlockMRUCUECRedstoneController().setHardness(3.0F).setResistance(15.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.ecRedstoneController");
		registerBlockSimple(ecRedstoneController, "ecRedstoneController");
		rayTower = new BlockRayTower().setHardness(1.0F).setResistance(1.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.rayTower");
		registerBlockSimple(rayTower, "rayTower");
		solarPrism = new BlockSolarPrism().setHardness(1.0F).setResistance(1.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.solarPrism");
		registerBlockSimple(solarPrism, "solarPrism");
		sunRayAbsorber = new BlockSunRayAbsorber().setHardness(1.0F).setResistance(1.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.sunRayAbsorber");
		registerBlockSimple(sunRayAbsorber, "sunRayAbsorber");
		coldStone = new BlockColdStone().setHardness(1.0F).setResistance(1.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.coldStone");
		registerBlockSimple(coldStone, "coldStone");
		coldDistillator = new BlockColdDistillator().setHardness(1.0F).setResistance(1.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.coldDistillator");
		registerBlockSimple(coldDistillator, "coldDistillator");
		naturalFurnace = new BlockFlowerBurner().setHardness(1.0F).setResistance(1.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.naturalFurnace");
		registerBlockSimple(naturalFurnace, "naturalFurnace");
		heatGenerator = new BlockHeatGenerator().setHardness(1.0F).setResistance(1.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.heatGenerator");
		registerBlockSimple(heatGenerator, "heatGenerator");
		enderGenerator = new BlockEnderGenerator().setHardness(1.0F).setResistance(1.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.enderGenerator");
		registerBlockSimple(enderGenerator, "enderGenerator");
		String[] corruptionNames = {"chaos", "frozen", "magic", "shade"};
		for(int i = 0; i < 4; ++i) {
			lightCorruption[i] = new BlockCorruption().setBlockTextureName("essentialcraft:"+corruptionNames[i]).setHardness(1.0F).setResistance(1.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.corruption."+corruptionNames[i]);
			registerBlockSimple(lightCorruption[i], corruptionNames[i]);
		}
		moonWell = new BlockMoonWell().setHardness(1.0F).setResistance(1.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.moonWell");
		registerBlockSimple(moonWell, "moonWell");
		magicianTable = registerBlockSimple(new BlockMagicianTable(), "magicianTable", 1, 1, 0);
		fortifiedStone = new BlockDyeable(Material.ROCK).setHardness(3.0F).setResistance(15.0F).setUnlocalizedName("essentialcraft.fortifiedStone");
		registerBlockSimple(fortifiedStone, "fortifiedStone");
		magicalQuarry = registerBlockSimple(new BlockMagicalQuarry(), "magicalQuarry", 1, 1, 0);
		monsterClinger = registerBlockSimple(new BlockMonsterHolder(), "monsterClinger", 1, 1, 0);
		potionSpreader = registerBlockSimple(new BlockPotionSpreader(), "potionSpreader", 1, 1, 0);
		magicalEnchanter = registerBlockSimple(new BlockMagicalEnchanter(), "magicalEnchanter", 1, 1, 0);
		monsterHarvester = registerBlockSimple(new BlockMonsterHarvester(), "monsterHarvester", 1, 1, 0);
		magicalRepairer = registerBlockSimple(new BlockMagicalRepairer(), "magicalRepairer", 1, 1, 0);
		matrixAbsorber = registerBlockSimple(new BlockMatrixAbsorber(), "matrixAbsorber", 1, 1, 0);
		radiatingChamber = registerBlockSimple(new BlockRadiatingChamber(), "radiatingChamber", 1, 1, 0);
		magmaticSmeltery = registerBlockSimple(new BlockMagmaticSmeltery(), "magmaticSmeltery", 1, 1, 0);
		magicalJukebox = registerBlockSimple(new BlockMagicalJukebox(), "magicalJukebox", 1, 1, 0);
		elementalCrystal = new BlockElementalCrystal().setHardness(3.0F).setResistance(15.0F).setUnlocalizedName("essentialcraft.elementalCrystal");
		registerBlockSimple(elementalCrystal, "elementalCrystal", ItemBlockElementalCrystal.class);
		crystalFormer = registerBlockSimple(new BlockCrystalFormer(), "crystalFormer", 1, 1, 0);
		crystalController = registerBlockSimple(new BlockCrystalController(), "crystalController", 1, 1, 0);
		crystalExtractor = registerBlockSimple(new BlockCrystalExtractor(), "crystalExtractor", 1, 1, 0);
		chargingChamber = registerBlockSimple(new BlockChargingChamber(), "chargingChamber", 1, 1, 0);
		voidStone = new BlockDyeable(Material.ROCK, MapColor.BLACK).setHardness(8.0F).setResistance(150.0F).setUnlocalizedName("essentialcraft.voidStone");
		registerBlockSimple(voidStone, "voidStone");
		voidGlass = new BlockDyeable(Material.GLASS, MapColor.BLACK).setHardness(8.0F).setResistance(150.0F).setUnlocalizedName("essentialcraft.voidGlass");
		registerBlockSimple(voidGlass, "voidGlass");

		concrete = registerBlockSimple(new BlockEC(Material.ROCK), "concrete", 1, 1, 0);
		cacti = registerBlockSimple(new BlockDreadCactus(), "cacti", 1, 1, 0);
		dreadDirt = registerBlockSimple(new BlockEC(Material.GROUND), "dreadDirt", 1, 1, 0).setSoundType(SoundType.GROUND);
		flowerGreen = registerBlockSimple(new BlockHoannaFlower(), "flowerGreen", 1, 1, 0);
		fruit = registerBlockSimple(new BlockMagicalFruit(), "fruit", 1, 1, 0);
		root = registerBlockSimple(new BlockEC(Material.WOOD), "root", 1, 1, 0);
		tallGrass = registerBlockSimple(new BlockHoannaTallGrass(), "tallGrass", 1, 1, 0);

		magicalTeleporter = registerBlockSimple(new BlockMagicalTeleporter(), "magicalTeleporter", 1, 1, 0);
		magicalFurnace = registerBlockSimple(new BlockMagicalFurnace(), "magicalFurnace", 1, 1, 0);
		emberForge = registerBlockSimple(new BlockEmberForge(), "emberForge", 1, 1, 0);
		levitator = registerBlockSimple(new BlockMRULevitator(), "levitator", 1, 100, 0);
		spreader = registerBlockSimple(new BlockMRUSpreader(), "spreader", 1, 100, 0);

		fence[0] = new BlockFenceEC(Material.ROCK, MapColor.BLACK).setHardness(8.0F).setResistance(150.0F).setUnlocalizedName("essentialcraft.voidFence");
		registerBlockSimple(fence[0], "voidFence");
		fence[1] = new BlockFenceEC(Material.ROCK, MapColor.PURPLE).setHardness(3.0F).setResistance(15.0F).setUnlocalizedName("essentialcraft.magicFence");
		registerBlockSimple(fence[1], "magicFence");
		fence[2] = new BlockFenceEC(Material.ROCK).setHardness(3.0F).setResistance(15.0F).setUnlocalizedName("essentialcraft.fFence");
		registerBlockSimple(fence[2], "fFence");

		torch = registerBlockSimple(new BlockMagicLight(), "torch", 0, 0, 1);
		torch.setLightLevel(1.0F);

		blockPale = registerBlockSimple(new BlockEC(Material.ROCK, MapColor.LAPIS), "blockPale", 2, 100, 0).setSoundType(SoundType.METAL);
		platingPale = new BlockDyeable(Material.ROCK, MapColor.LAPIS).setHardness(3.0F).setResistance(100.0F).setUnlocalizedName("essentialcraft.platingPale");
		registerBlockSimple(platingPale, "platingPale");
		mruCoilHardener = registerBlockSimple(new BlockMRUCoilHardener(), "mruCoilHardener", 1, 100, 0);
		mruCoil = registerBlockSimple(new BlockMRUCoil(), "mruCoil", 1, 100, 0);
		corruptionCleaner = registerBlockSimple(new BlockCorruptionCleaner(), "corruptionCleaner", 1, 100, 0);
		reactorSupport = new BlockReactorSupport().setUnlocalizedName("essentialcraft.reactorSupport").setHardness(1).setResistance(10).setLightOpacity(0);
		registerBlockSimple(reactorSupport, "reactorSupport");
		reactor = registerBlockSimple(new BlockMRUReactor(), "reactor", 1, 10, 0);
		air = new BlockEC(Material.ROCK).setHardness(-1).setResistance(-1).setLightLevel(0).setUnlocalizedName("air");
		registerBlockSimple(air, "air");
		darknessObelisk = registerBlockSimple(new BlockDarknessObelisk(), "darknessObelisk", 1, 1, 0);

		ultraHeatGen = registerBlockSimple(new BlockUltraHeatGenerator(), "ultraHeatGen", 1, 1, 0);
		ultraFlowerBurner = registerBlockSimple(new BlockUltraFlowerBurner(), "ultraFlowerBurner", 1, 1, 0);

		magicalMirror = registerBlockSimple(new BlockMagicalMirror(), "magicalMirror", 1, 1, 0);
		magicalDisplay = registerBlockSimple(new BlockMagicalDisplay(), "magicalDisplay", 1, 1, 0);
		portal = registerBlockSimple(new BlockHoannaPortal(), "portal", -1, -1, 1);
		oreDrops = registerBlockSimple(new BlockDropsOre(), "oreDrops", 1, 1, 1);

		invertedBlock = new BlockDyeable(Material.ROCK, MapColor.GREEN).setHardness(4.0F).setResistance(100.0F).setUnlocalizedName("essentialcraft.mithrilinePlating");
		registerBlockSimple(invertedBlock, "invertedPlating");
		mithrilineCrystal = new BlockMithrilineCrystal().setUnlocalizedName("essentialcraft.mithrilineCrystal").setHardness(1).setResistance(1).setLightOpacity(1);
		registerBlockSimple(mithrilineCrystal, "mithrilineCrystal", ItemBlockMithrilineCrystal.class);
		mithrilineFurnace = registerBlockSimple(new BlockMithrilineFurnace(), "mithrilineFurnace",1,1,1);

		demonicPlating = new BlockDyeable(Material.ROCK, MapColor.RED).setHardness(3.0F).setResistance(100.0F).setUnlocalizedName("essentialcraft.demonicPlating");
		registerBlockSimple(demonicPlating, "demonicPlating");
		playerPentacle = registerBlockSimple(new BlockPlayerPentacle(), "playerPentacle", 0, 0, 0);
		windRune = registerBlockSimple(new BlockWindRune(), "windRune", 3, 10, 0);
		rightClicker = registerBlockSimple(new BlockRightClicker(), "rightClicker", 1, 1, 15);
		redstoneTransmitter = registerBlockSimple(new BlockRedstoneTransmitter(), "redstoneTransmitter", 0, 0, 0);
		magicalHopper = registerBlockSimple(new BlockMagicalHopper(), "magicalHopper", 1, 1, 15);
		metadataManager = registerBlockSimple(new BlockMetadataManager(), "metadataManager",1, 1, 15);
		blockBreaker = registerBlockSimple(new BlockBlockBreaker(), "blockBreaker", 1, 1, 15);
		compressed = new BlockCompressedDrops().setUnlocalizedName("essentialcraft.compressed").setHardness(0.4F).setResistance(1).setLightOpacity(15);
		registerBlockSimple(new ItemBlockMeta(compressed), "compressed");
		demonicPentacle = registerBlockSimple(new BlockDemonicPentacle(), "demonicPentacle", 0, 0, 0);
		weaponMaker = registerBlockSimple(new BlockWeaponMaker(), "weaponMaker", 1, 1, 15);
		furnaceMagic = new BlockFurnaceMagic().setUnlocalizedName("essentialcraft.furnaceMagic").setHardness(1).setResistance(1).setLightOpacity(15);
		registerBlockSimple(furnaceMagic, "furnaceMagic");
		holopad = registerBlockSimple(new BlockHologramSpawner(), "holopad", 1, 1, 15);
		chest = registerBlockSimple(new BlockChestEC(), "chest", 1, 1, 0);
		mimInvStorage = registerBlockSimple(new BlockMIMInventoryStorage(), "mimInvStorage", 1, 1, 15);
		newMim = new BlockMIM().setResistance(1).setHardness(1).setLightOpacity(0).setUnlocalizedName("essentialcraft.newMim");
		registerBlockSimple(newMim, "newMim");
		mimScreen = registerBlockSimple(new BlockMIMScreen(), "mimScreen", 1, 1, 15);
		mimCrafter = registerBlockSimple(new BlockMIMCraftingManager(), "mimCrafter", 1, 1, 15);
		mimEjector = registerBlockSimple(new BlockMIMExporter(), "mimEjector", 1, 1, 0);
		mimInjector = registerBlockSimple(new BlockMIMImporter(), "mimInjector", 1, 1, 0);
		device = new BlockRedstoneDeviceNotSided().setHardness(1).setResistance(1).setUnlocalizedName("essentialcraft.device");
		registerBlockSimple(device, "device", ItemBlockRDNS.class);
		advBreaker = registerBlockSimple(new BlockAdvBlockBreaker(), "advBreaker", 1, 1, 0);
		mimEjectorP = registerBlockSimple(new BlockMIMExporterPersistant(), "mimEjectorP", 1, 1, 0);
		mimInjectorP = registerBlockSimple(new BlockMIMImporterPersistant(), "mimInjectorP", 1, 1, 0);

		oreMithriline = new BlockMithrilineOre().setUnlocalizedName("essentialcraft.oreMithriline").setHardness(0.4F).setResistance(1).setLightOpacity(15);
		registerBlockSimple(oreMithriline, "oreMithriline");
		crystalLamp = new BlockCrystalLamp().setUnlocalizedName("essentialcraft.crystalLamp").setHardness(0.4F).setResistance(1).setLightOpacity(15);
		registerBlockSimple(new ItemBlockMeta(crystalLamp), "crystalLamp");
		mimic = new BlockMimic().setUnlocalizedName("essentialcraft.mimic").setResistance(15).setHardness(3).setLightOpacity(15);
		registerBlockSimple(mimic, "mimic");

		water = new BlockEC(Material.WATER).setHardness(-1).setResistance(-1).setLightLevel(0).setUnlocalizedName("water");
		registerBlockSimple(water, "water");
		lava = new BlockEC(Material.LAVA).setHardness(-1).setResistance(-1).setLightLevel(0).setUnlocalizedName("lava");
		registerBlockSimple(lava, "lava");
		fire = new BlockEC(Material.FIRE).setHardness(-1).setResistance(-1).setLightLevel(0).setUnlocalizedName("fire");
		registerBlockSimple(fire, "fire");

		chunkLoader = registerBlockSimple(new BlockChunkLoader(), "chunkLoader", 1, 1, 0);
		dimTransciever = registerBlockSimple(new BlockDimensionalTransciever(), "dimTransciever", 1, 1, 0);

		intersector = registerBlockSimple(new BlockMRUIntersector(), "intersector", 1, 1, 15);
		worldMerger = registerBlockSimple(new BlockWorldMerger(), "worldMerger", 1, 1, 0);
		weatherController = registerBlockSimple(new BlockWeatherController(), "weatherController", 1, 1, 15);
	}

	public static void postInitLoad() {
		createFancyBlock(Material.ROCK, "mru", "mru", 1, 100, new ItemStack(ItemsCore.magicalSlag));
		createFancyBlock(Material.ROCK, "concrete", "concrete", 1, 5, new ItemStack(concrete));
		createFancyBlock(Material.ROCK, "fortifiedStone", "fortifiedStone", 1.5F, 8, new ItemStack(fortifiedStone));
		createFancyBlock(Material.GLASS, "coldStone", "coldStone", 0.7F, 1, new ItemStack(coldStone));
		createFancyBlock(Material.ROCK, "magicPlating", "magicPlating", 2F, 8, new ItemStack(magicPlating));
		createFancyBlock(Material.ROCK, "palePlating", "palePlating", 2F, 8,new ItemStack(platingPale));
		createFancyBlock(Material.ROCK, "voidStone", "voidStone", 3F, 28, new ItemStack(voidStone));
		createFancyBlock(Material.ROCK, "mithrilinePlating", "mithrilinePlating", 3F, 100, new ItemStack(invertedBlock));
		createFancyBlock(Material.ROCK, "demonicPlating", "demonicPlating", 3F, 100, new ItemStack(demonicPlating));

		Block mimicFancy = new BlockFancyMimic().setUnlocalizedName("essentialcraft.fancyBlock.mimic").setResistance(15).setHardness(3).setLightOpacity(15);
		registerFancyBlock(mimicFancy, "mimic", new ItemStack(mimic));
	}

	public static <T extends Block> T registerBlockSimple(T c, String name, float hardness, float resistance, int opacity) {
		try {
			c.setUnlocalizedName("essentialcraft."+name).setResistance(resistance).setHardness(hardness).setLightOpacity(opacity);
			BlockRegistry.registerBlock(new ItemBlockGeneric(c), name, EssentialCraftCore.class);
			EssentialCraftCore.proxy.handleBlockRegister(c);
			return c;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T extends Block> T registerBlockSimple(Block b, String name) {
		BlockRegistry.registerBlock(new ItemBlockGeneric(b), name, EssentialCraftCore.class);
		EssentialCraftCore.proxy.handleBlockRegister(b);
		return (T)b;
	}

	public static <T extends Block> T registerBlockSimple(Block b, String name, Class<? extends ItemBlock> ib) {
		try {
			ib.getConstructor(Block.class).setAccessible(true);
			BlockRegistry.registerBlock(ib.getConstructor(Block.class).newInstance(b), name, EssentialCraftCore.class);
			EssentialCraftCore.proxy.handleBlockRegister(b);
			return (T)b;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ItemBlock registerBlockSimple(ItemBlock ib, String name) {
		BlockRegistry.registerBlock(ib, name, EssentialCraftCore.class);
		EssentialCraftCore.proxy.handleBlockRegister(ib.getBlock());
		return ib;
	}

	public static void createFancyBlock(Material m, String name, String texture, float hardness, float resistance, ItemStack createdFrom) {
		Block fancy = new BlockFancy(m).setUnlocalizedName("essentialcraft.fancyBlock."+name).setResistance(resistance).setHardness(hardness);
		BlockRegistry.registerBlock(new ItemBlockFancy(fancy), "fancyBlock."+name, EssentialCraftCore.class);
		EssentialCraftCore.proxy.handleBlockRegister(fancy);
		fancyBlocks.add(fancy);
		RecipesCore.fancyBlockRecipes.put(fancy, createdFrom);
	}

	public static Block registerFancyBlock(Block fancy, String name, ItemStack createdFrom) {
		BlockRegistry.registerBlock(new ItemBlockFancy(fancy), "fancyBlock."+name, EssentialCraftCore.class);
		EssentialCraftCore.proxy.handleBlockRegister(fancy);
		fancyBlocks.add(fancy);
		RecipesCore.fancyBlockRecipes.put(fancy, createdFrom);
		return fancy;
	}

	public static Block drops;
	public static Block magicPlating;
	public static Block fortifiedGlass;
	public static Block ecController;
	public static Block ecAcceptor;
	public static Block ecBalancer;
	public static Block ecEjector;
	public static Block ecHoldingChamber;
	public static Block ecStateChecker;
	public static Block ecRedstoneController;
	public static Block rayTower;
	public static Block moonWell;
	public static Block solarPrism;
	public static Block sunRayAbsorber;
	public static Block coldStone;
	public static Block coldDistillator;
	public static Block naturalFurnace;
	public static Block heatGenerator;
	public static Block enderGenerator;
	public static Block magicianTable;
	public static Block fortifiedStone;
	public static Block magicalQuarry;
	public static Block monsterClinger;
	public static Block potionSpreader;
	public static Block magicalEnchanter;
	public static Block monsterHarvester;
	public static Block magicalRepairer;
	public static Block matrixAbsorber;
	public static Block radiatingChamber;
	public static Block magmaticSmeltery;
	public static Block magicalJukebox;
	public static Block elementalCrystal;
	public static Block crystalFormer;
	public static Block crystalController;
	public static Block crystalExtractor;
	public static Block chargingChamber;
	public static Block voidStone;
	public static Block voidGlass;

	public static Block concrete;
	public static BlockDreadCactus cacti;
	public static Block dreadDirt;
	public static Block flowerGreen;
	public static Block fruit;
	public static Block root;
	public static Block tallGrass;
	public static Block thorns;

	public static Block magicalTeleporter;
	public static Block magicalFurnace;
	public static Block emberForge;

	public static Block levitator;
	public static Block spreader;
	public static Block[] fence = new Block[3];
	public static Block torch;

	public static Block blockPale;

	public static Block platingPale;
	public static Block mruCoilHardener;
	public static Block mruCoil;
	public static Block corruptionCleaner;
	public static Block reactorSupport;
	public static Block reactor;

	public static Block air;

	//public static Block minEjector;
	//public static Block minInjector;
	//public static Block mim;
	public static Block darknessObelisk;
	public static Block ultraHeatGen;
	public static Block ultraFlowerBurner;
	public static Block magicalMirror;
	public static Block magicalDisplay;
	public static BlockHoannaPortal portal;
	public static Block oreDrops;
	public static Block invertedBlock;
	public static Block mithrilineCrystal;
	public static Block mithrilineFurnace;
	public static Block demonicPlating;
	public static Block playerPentacle;
	public static Block windRune;
	public static Block rightClicker;
	public static Block redstoneTransmitter;
	public static Block magicalHopper;
	public static Block metadataManager;
	public static Block blockBreaker;
	public static Block compressed;
	public static Block demonicPentacle;
	public static Block weaponMaker;
	public static Block furnaceMagic;

	public static Block holopad;
	public static Block chest;
	public static Block mimInvStorage;
	public static Block newMim;
	public static Block mimScreen;
	public static Block mimCrafter;
	public static Block mimEjector;
	public static Block mimInjector;
	public static Block device;
	public static Block advBreaker;
	public static Block mimEjectorP;
	public static Block mimInjectorP;

	public static Block oreMithriline;
	public static Block crystalLamp;
	public static Block mimic;
	public static Block chunkLoader;
	public static Block dimTransciever;

	public static Block intersector;
	public static Block worldMerger;
	public static Block weatherController;

	public static Block water;
	public static Block lava;
	public static Block fire;

	public static List<Block> fancyBlocks = new ArrayList<Block>();

	public static Block[] lightCorruption = new Block[4];
}
