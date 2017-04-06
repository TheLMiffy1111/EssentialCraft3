package ec3.common.block;

import java.util.ArrayList;
import java.util.List;

import DummyCore.Blocks.BlocksRegistry;
import ec3.common.item.ItemBlockElementalCrystal;
import ec3.common.item.ItemBlockFancy;
import ec3.common.item.ItemBlockGeneric;
import ec3.common.item.ItemBlockMeta;
import ec3.common.item.ItemBlockMithrilineCrystal;
import ec3.common.item.ItemBlockRDNS;
import ec3.common.item.ItemsCore;
import ec3.common.mod.EssentialCraftCore;
import ec3.common.registry.RecipeRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class BlocksCore {

	public static void loadBlocks() {
		drops = new BlockDrops(Material.CLOTH).setUnlocalizedName("essentialcraft.drops");
		registerBlockSimple(new ItemBlockMeta(drops), "Drops");
		magicPlating = new BlockConnectedTextures(Material.ROCK).setBlockTextureName("essentialcraft:magicPlatingBlock").setTexturePath("magicPlating").setHardness(3.0F).setResistance(15.0F).setUnlocalizedName("essentialcraft.magicPlatingBlock");
		registerBlockSimple(magicPlating, "magicPlating");
		fortifiedGlass = new BlockConnectedTextures(Material.GLASS).setBlockTextureName("essentialcraft:transparentGlass").setTexturePath("fortifiedGlass").setHardness(3.0F).setResistance(15.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.fortifiedGlass");
		registerBlockSimple(fortifiedGlass, "fortifiedGlass");
		ecController = new BlockecController(Material.ROCK).setHardness(3.0F).setResistance(15.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.ecController");
		registerBlockSimple(ecController, "ecController");
		ecAcceptor = new BlockecAcceptor(Material.ROCK).setHardness(3.0F).setResistance(15.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.ecAcceptor");
		registerBlockSimple(ecAcceptor, "ecAcceptor");
		ecBalancer = new BlockecBalancer(Material.ROCK).setHardness(3.0F).setResistance(15.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.ecBalancer");
		registerBlockSimple(ecBalancer, "ecBalancer");
		ecEjector = new BlockecEjector(Material.ROCK).setHardness(3.0F).setResistance(15.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.ecEjector");
		registerBlockSimple(ecEjector, "ecEjector");
		ecHoldingChamber = new BlockecHoldingChamber(Material.ROCK).setHardness(3.0F).setResistance(15.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.ecHoldingChamber");
		registerBlockSimple(ecHoldingChamber, "ecHoldingChamber");
		ecStateChecker = new BlockecStateChecker(Material.ROCK).setHardness(3.0F).setResistance(15.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.ecStateChecker");
		registerBlockSimple(ecStateChecker, "ecStateChecker");
		ecRedstoneController = new BlockecRedstoneController(Material.ROCK).setHardness(3.0F).setResistance(15.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.ecRedstoneController");
		registerBlockSimple(ecRedstoneController, "ecRedstoneController");
		rayTower = new BlockRayTower().setHardness(1.0F).setResistance(1.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.rayTower");
		registerBlockSimple(rayTower, "rayTower");
		solarPrism = new BlockSolarPrism(Material.ROCK).setHardness(1.0F).setResistance(1.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.solarPrism");
		registerBlockSimple(solarPrism, "solarPrism");
		sunRayAbsorber = new BlockSunRayAbsorber(Material.ROCK).setHardness(1.0F).setResistance(1.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.sunRayAbsorber");
		registerBlockSimple(sunRayAbsorber, "sunRayAbsorber");
		coldStone = new BlockColdStone().setHardness(1.0F).setResistance(1.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.coldStone");
		registerBlockSimple(coldStone, "coldStone");
		String[] corruptionNames = new String[]{"chaos", "frozen", "magic", "shade"};
		coldDistillator = new BlockColdDistillator(Material.ROCK).setHardness(1.0F).setResistance(1.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.coldDistillator");
		registerBlockSimple(coldDistillator, "coldDistillator");
		naturalFurnace = new BlockFlowerBurner(Material.ROCK).setHardness(1.0F).setResistance(1.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.naturalFurnace");
		registerBlockSimple(naturalFurnace, "naturalFurnace");
		heatGenerator = new BlockHeatGenerator(Material.ROCK).setHardness(1.0F).setResistance(1.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.heatGenerator");
		registerBlockSimple(heatGenerator, "heatGenerator");
		enderGenerator = new BlockEnderGenerator(Material.ROCK).setHardness(1.0F).setResistance(1.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.enderGenerator");
		registerBlockSimple(enderGenerator, "enderGenerator");
		for(int i = 0; i < 4; ++i) {
			lightCorruption[i] = new BlockCorruption_Light(Material.CIRCUITS).setBlockTextureName("essentialcraft:"+corruptionNames[i]).setHardness(1.0F).setResistance(1.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.corruption."+corruptionNames[i]);
			registerBlockSimple(lightCorruption[i], corruptionNames[i]);
		}
		moonWell = new BlockMoonWell(Material.ROCK).setHardness(1.0F).setResistance(1.0F).setLightOpacity(0).setUnlocalizedName("essentialcraft.moonWell");
		registerBlockSimple(moonWell, "moonWell");
		magicianTable = registerBlockSimple(BlockMagicianTable.class, Material.ROCK, magicianTable, "magicianTable", "magicPlatingBlock", 1, 1, 0);
		fortifiedStone = new BlockConnectedTextures(Material.ROCK).setBlockTextureName("essentialcraft:fortifiedStone").setTexturePath("fortifiedStone").setHardness(3.0F).setResistance(15.0F).setUnlocalizedName("essentialcraft.fortifiedStone");
		registerBlockSimple(fortifiedStone, "fortifiedStone");
		magicalQuarry = registerBlockSimple(BlockMagicalQuarry.class, Material.ROCK, magicalQuarry, "magicalQuarry", "magicPlatingBlock", 1, 1, 0);
		monsterClinger = registerBlockSimple(BlockMonsterHolder.class, Material.ROCK, monsterClinger, "monsterClinger", "magicPlatingBlock", 1, 1, 0);
		potionSpreader = registerBlockSimple(BlockPotionSpreader.class, Material.ROCK, potionSpreader, "potionSpreader", "magicPlatingBlock", 1, 1, 0);
		magicalEnchanter = registerBlockSimple(BlockMagicalEnchanter.class, Material.ROCK, magicalEnchanter, "magicalEnchanter", "magicPlatingBlock", 1, 1, 0);
		monsterHarvester = registerBlockSimple(BlockMonsterHarvester.class, Material.ROCK, monsterHarvester, "monsterHarvester", "magicPlatingBlock", 1, 1, 0);
		magicalRepairer = registerBlockSimple(BlockMagicalRepairer.class, Material.ROCK, magicalRepairer, "magicalRepairer", "magicPlatingBlock", 1, 1, 0);
		matrixAbsorber = registerBlockSimple(BlockMatrixAbsorber.class, Material.ROCK, matrixAbsorber, "matrixAbsorber", "magicPlatingBlock", 1, 1, 0);
		radiatingChamber = registerBlockSimple(BlockRadiatingChamber.class, Material.ROCK, radiatingChamber, "radiatingChamber", "magicPlatingBlock", 1, 1, 0);
		magmaticSmeltery = registerBlockSimple(BlockMagmaticSmeltery.class, Material.ROCK, magmaticSmeltery, "magmaticSmeltery", "magicPlatingBlock", 1, 1, 0);
		magicalJukebox = registerBlockSimple(BlockMagicalJukebox.class, Material.ROCK, magicalJukebox, "magicalJukebox", "magicPlatingBlock", 1, 1, 0);
		elementalCrystal = new BlockElementalCrystal(Material.ROCK).setHardness(3.0F).setResistance(15.0F).setUnlocalizedName("essentialcraft.elementalCrystal");
		registerBlockSimple(elementalCrystal, "elementalCrystal", ItemBlockElementalCrystal.class);
		crystalFormer = registerBlockSimple(BlockCrystalFormer.class, Material.ROCK, crystalFormer, "crystalFormer", "magicPlatingBlock", 1, 1, 0);
		crystalController = registerBlockSimple(BlockCrystalController.class, Material.ROCK, crystalController, "crystalController", "magicPlatingBlock", 1, 1, 0);
		crystalExtractor = registerBlockSimple(BlockCrystalExtractor.class, Material.ROCK, crystalExtractor, "crystalExtractor", "magicPlatingBlock", 1, 1, 0);
		chargingChamber = registerBlockSimple(BlockChargingChamber.class, Material.ROCK ,chargingChamber, "chargingChamber", "magicPlatingBlock", 1, 1, 0);
		voidStone = new BlockConnectedTextures(Material.ROCK).setBlockTextureName("essentialcraft:voidStone").setTexturePath("voidStone").setHardness(8.0F).setResistance(150.0F).setUnlocalizedName("essentialcraft.voidStone");
		registerBlockSimple(voidStone, "voidStone");
		voidGlass = new BlockConnectedTextures(Material.GLASS).setBlockTextureName("essentialcraft:voidGlass").setTexturePath("voidGlass").setHardness(8.0F).setResistance(150.0F).setUnlocalizedName("essentialcraft.voidGlass");
		registerBlockSimple(voidGlass, "voidGlass");

		concrete = registerBlockSimple(BlockMod.class, Material.ROCK, concrete, "concrete", "firstWorld/burnedConcrete", 1, 1, 0);
		cacti = registerBlockSimple(BlockDreadCacti.class, Material.CACTUS, cacti, "cacti", "firstWorld/cactus", 1, 1, 0);
		dreadDirt = registerBlockSimple(BlockMod.class, Material.GROUND, dreadDirt, "dreadDirt", "firstWorld/dreadDirt", 1, 1, 0);
		((BlockMod)dreadDirt).setSoundType(SoundType.GROUND);
		flowerGreen = registerBlockSimple(BlockModFlower.class, Material.GRASS, flowerGreen, "flowerGreen", "firstWorld/flower_green", 1, 1, 0);
		fruit = registerBlockSimple(BlockMagicalFruit.class, Material.GRASS, fruit, "fruit", "firstWorld/magicalFruit", 1, 1, 0);
		root = registerBlockSimple(BlockMod.class, Material.WOOD,root, "root", "firstWorld/root", 1, 1, 0);
		tallGrass = registerBlockSimple(BlockModTallGrass.class, Material.GRASS, tallGrass, "tallGrass", "firstWorld/tallGrass", 1, 1, 0);

		magicalTeleporter = registerBlockSimple(BlockMagicalTeleporter.class, Material.ROCK, magicalTeleporter, "magicalTeleporter", "magicPlatingBlock", 1, 1, 0);
		magicalFurnace = registerBlockSimple(BlockMagicalFurnace.class, Material.ROCK, magicalFurnace, "magicalFurnace", "fortifiedStone", 1, 1, 0);
		emberForge = registerBlockSimple(BlockEmberForge.class, Material.ROCK, emberForge, "emberForge", "magicPlatingBlock", 1, 1, 0);
		levitator = registerBlockSimple(BlockMRULevitator.class, Material.ROCK, levitator, "levitator", "fortifiedStone",1,100,0);
		spreader = registerBlockSimple(BlockMRUSpreader.class, Material.ROCK, spreader, "spreader", "fortifiedStone",1 ,100 ,0);

		fence[0] = new BlockModFence(Material.ROCK, "essentialcraft:voidStone").setHardness(8.0F).setResistance(150.0F).setUnlocalizedName("essentialcraft.voidFence");
		registerBlockSimple(fence[0], "voidFence");
		fence[1] = new BlockModFence(Material.ROCK, "essentialcraft:magicPlatingBlock").setHardness(3.0F).setResistance(15.0F).setUnlocalizedName("essentialcraft.magicFence");
		registerBlockSimple(fence[1], "magicFence");
		fence[2] = new BlockModFence(Material.ROCK, "essentialcraft:fortifiedStone").setHardness(3.0F).setResistance(15.0F).setUnlocalizedName("essentialcraft.fFence");
		registerBlockSimple(fence[2], "fFence");

		torch = registerBlockSimple(BlockMagicLight.class, Material.CIRCUITS,torch, "torch", "firstWorld/root", 0, 0, 1);
		torch.setLightLevel(1.0F);

		blockPale = registerBlockSimple(BlockMod.class, Material.ROCK, blockPale, "blockPale", "paleBlock", 2, 100, 0);
		((BlockMod)blockPale).setSoundType(SoundType.METAL);
		platingPale = new BlockConnectedTextures(Material.ROCK).setBlockTextureName("essentialcraft:palePlatingBlock").setTexturePath("palePlating").setHardness(3.0F).setResistance(100.0F).setUnlocalizedName("essentialcraft.platingPale");
		registerBlockSimple(platingPale, "platingPale");
		mruCoilHardener = registerBlockSimple(BlockMRUCoil_Hardener.class, Material.ROCK, mruCoilHardener, "mruCoilHardener", "fortifiedStone", 1, 100, 0);
		mruCoil = registerBlockSimple(BlockMRUCoil_Coil.class, Material.ROCK, mruCoil, "mruCoil", "fortifiedStone", 1, 100, 0);
		corruptionCleaner = registerBlockSimple(BlockCorruptionCleaner.class, Material.ROCK, corruptionCleaner, "corruptionCleaner", "fortifiedStone", 1, 100, 0);
		reactorSupport = new BlockReactorSupport().setUnlocalizedName("essentialcraft.reactorSupport").setHardness(1).setResistance(10).setLightOpacity(0);
		registerBlockSimple(reactorSupport, "reactorSupport");
		reactor = registerBlockSimple(BlockMRUReactor.class, Material.ROCK,reactor, "reactor", "fortifiedStone", 1, 10, 0);
		air = new BlockMod(Material.ROCK).setBlockTextureName("minecraft:cauldron_top").setHardness(-1).setResistance(-1).setLightLevel(0).setUnlocalizedName("air");
		registerBlockSimple(air, "air");
		darknessObelisk = registerBlockSimple(BlockDarknessObelisk.class, Material.ROCK, darknessObelisk, "darknessObelisk", "voidStone", 1, 1, 0);

		ultraHeatGen = registerBlockSimple(BlockUltraHeatGenerator.class, Material.ROCK, ultraHeatGen, "ultraHeatGen", "voidStone", 1, 1, 0);
		ultraFlowerBurner = registerBlockSimple(BlockUltraFlowerBurner.class, Material.ROCK, ultraFlowerBurner, "ultraFlowerBurner", "voidStone", 1, 1, 0);

		magicalMirror = registerBlockSimple(BlockMagicalMirror.class, Material.ROCK, magicalMirror, "magicalMirror", "magicPlatingBlock", 1, 1, 0);
		magicalDisplay = registerBlockSimple(BlockMagicalDisplay.class, Material.ROCK, magicalDisplay, "magicalDisplay", "magicalDisplay", 1, 1, 0);
		portal = (BlockPortal)registerBlockSimple(BlockPortal.class, Material.ROCK, portal, "portal", "portal",-1,-1,1);
		oreDrops = registerBlockSimple(BlockDropsOre.class, Material.ROCK, oreDrops, "oreDrops", "fortifiedStone",1,1,1);

		invertedBlock = new BlockConnectedTextures(Material.ROCK).setBlockTextureName("essentialcraft:invertedPlatingBlock").setTexturePath("invertedPlating").setHardness(4.0F).setResistance(100.0F).setUnlocalizedName("essentialcraft.mithrilinePlating");
		registerBlockSimple(invertedBlock, "invertedPlating");
		mithrilineCrystal = new BlockMithrilineCrystal().setUnlocalizedName("essentialcraft.mithrilineCrystal").setHardness(1).setResistance(1).setLightOpacity(1);
		registerBlockSimple(mithrilineCrystal, "mithrilineCrystal", ItemBlockMithrilineCrystal.class);
		mithrilineFurnace = registerBlockSimple(BlockMithrilineFurnace.class, Material.ROCK, mithrilineFurnace, "mithrilineFurnace", "invertedPlatingBlock",1,1,1);

		demonicPlating = new BlockConnectedTextures(Material.ROCK).setBlockTextureName("essentialcraft:demonicPlatingBlock").setTexturePath("demonicPlating").setHardness(3.0F).setResistance(100.0F).setUnlocalizedName("essentialcraft.demonicPlating");
		registerBlockSimple(demonicPlating, "demonicPlating");
		playerPentacle = registerBlockSimple(BlockPlayerPentacle.class, Material.ROCK, playerPentacle, "playerPentacle", "playerPentacle",0,0,0);
		windRune = registerBlockSimple(BlockWindRune.class, Material.ROCK, windRune, "windRune", "windRune",3,10,0);
		rightClicker = registerBlockSimple(BlockRightClicker.class, Material.ROCK, rightClicker, "rightClicker", "fortifiedStone", 1, 1, 15);
		redstoneTransmitter = registerBlockSimple(BlockRedstoneTransmitter.class, Material.ROCK,redstoneTransmitter, "redstoneTransmitter", "fortifiedStone",0,0,0);
		magicalHopper = registerBlockSimple(BlockMagicalHopper.class, Material.ROCK, magicalHopper, "magicalHopper", "fortifiedStone", 1, 1, 15);
		metadataManager = registerBlockSimple(BlockMetadataManager.class, Material.ROCK, metadataManager, "metadataManager", "fortifiedStone", 1, 1, 15);
		blockBreaker = registerBlockSimple(BlockBlockBreaker.class, Material.ROCK, blockBreaker, "blockBreaker", "fortifiedStone", 1, 1, 15);
		compressed = new BlockCompressedDrops().setUnlocalizedName("essentialcraft.compressed").setHardness(0.4F).setResistance(1).setLightOpacity(15);
		registerBlockSimple(new ItemBlockMeta(compressed), "compressed");
		demonicPentacle = registerBlockSimple(BlockDemonicPentacle.class, Material.ROCK, demonicPentacle, "demonicPentacle", "demonicPentacle",0,0,0);
		weaponMaker = registerBlockSimple(BlockWeaponMaker.class, Material.ROCK, weaponMaker, "weaponMaker", "fortifiedStone", 1, 1, 15);
		furnaceMagic = new BlockFurnaceMagic().setUnlocalizedName("essentialcraft.furnaceMagic").setHardness(1).setResistance(1).setLightOpacity(15);
		registerBlockSimple(furnaceMagic, "furnaceMagic");
		holopad = registerBlockSimple(BlockHologramSpawner.class, Material.ROCK, holopad, "holopad", "holopad", 1, 1, 15);
		chest = registerBlockSimple(BlockEC3Chest.class, Material.ROCK, chest, "chest", "null", 1, 1, 0);
		mimInvStorage = registerBlockSimple(BlockNewMIMInventoryStorage.class, Material.ROCK,mimInvStorage, "mimInvStorage", "null", 1, 1, 15);
		newMim = new BlockNewMIM(Material.ROCK).setResistance(1).setHardness(1).setLightOpacity(0).setUnlocalizedName("essentialcraft.newMim");
		registerBlockSimple(newMim, "newMim");
		mimScreen = registerBlockSimple(BlockMIMScreen.class, Material.ROCK, mimScreen, "mimScreen", "null", 1, 1, 15);
		mimCrafter = registerBlockSimple(BlockNewMIMCraftingManager.class, Material.ROCK, mimCrafter, "mimCrafter", "null", 1, 1, 15);
		mimEjector = registerBlockSimple(BlockNewMIMExporter.class, Material.ROCK, mimEjector, "mimEjector", "mimNode_Ejection", 1, 1, 0);
		mimInjector = registerBlockSimple(BlockNewMIMImporter.class, Material.ROCK, mimInjector, "mimInjector", "mimNode_Injection", 1, 1, 0);
		device = new BlockRedstoneDeviceNotSided().setHardness(1).setResistance(1).setUnlocalizedName("essentialcraft.device");
		registerBlockSimple(device, "device", ItemBlockRDNS.class);
		advBreaker = registerBlockSimple(BlockAdvBlockBreaker.class, Material.ROCK,advBreaker, "advBreaker", "fortifiedStone", 1, 1, 0);
		mimEjectorP = registerBlockSimple(BlockNewMIMExporter_Persistant.class, Material.ROCK, mimEjectorP, "mimEjectorP", "mimNode_EjectionP", 1, 1, 0);
		mimInjectorP = registerBlockSimple(BlockNewMIMImporter_Persistant.class, Material.ROCK, mimInjectorP, "mimInjectorP", "mimNode_InjectionP", 1, 1, 0);
	
		oreMithriline = new BlockMithrilineOre().setUnlocalizedName("essentialcraft.oreMithriline").setHardness(0.4F).setResistance(1).setLightOpacity(15);
		registerBlockSimple(oreMithriline, "oreMithriline");
		crystalLamp = new BlockCrystalLamp().setUnlocalizedName("essentialcraft.crystalLamp").setHardness(0.4F).setResistance(1).setLightOpacity(15);
		registerBlockSimple(new ItemBlockMeta(crystalLamp), "crystalLamp");
		mimic = new BlockMimic().setUnlocalizedName("essentialcraft.mimic").setResistance(15).setHardness(3).setLightOpacity(15);
		registerBlockSimple(mimic, "mimic");
		
		water = new BlockMod(Material.WATER).setHardness(-1).setResistance(-1).setLightLevel(0).setUnlocalizedName("water");
		registerBlockSimple(water, "water");
		lava = new BlockMod(Material.LAVA).setHardness(-1).setResistance(-1).setLightLevel(0).setUnlocalizedName("lava");
		registerBlockSimple(lava, "lava");
		fire = new BlockMod(Material.FIRE).setHardness(-1).setResistance(-1).setLightLevel(0).setUnlocalizedName("fire");
		registerBlockSimple(fire, "fire");

		chunkLoader = registerBlockSimple(BlockChunkLoader.class,Material.ROCK,chunkLoader,"chunkLoader","chunkLoader",1,1,0);
		dimTransciever = registerBlockSimple(BlockDimensionalTransciever.class,Material.ROCK,dimTransciever,"dimTransciever","dimTransciever",1,1,0);
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

	public static Block registerBlockSimple(Class<? extends Block> c, Material m, Block b, String name, String texture, float hardness, float resistance, int opacity) {
		try {
			b = c.getConstructor().newInstance().setUnlocalizedName("essentialcraft."+name).setResistance(resistance).setHardness(hardness).setLightOpacity(opacity);
			BlocksRegistry.registerBlock(new ItemBlockGeneric(b), name, EssentialCraftCore.class);
			EssentialCraftCore.proxy.handleBlockRegister(b);
			return b;
		}
		catch (Exception e) {
			try {
				if(c == BlockMod.class) {
					b = new BlockMod(m).setBlockTextureName("essentialcraft:"+texture).setUnlocalizedName("essentialcraft."+name).setResistance(resistance).setHardness(hardness).setLightOpacity(opacity);
					BlocksRegistry.registerBlock(new ItemBlockGeneric(b), name, EssentialCraftCore.class);
					EssentialCraftCore.proxy.handleBlockRegister(b);
					return b;
				}
				else {
					c.getConstructor(Material.class).setAccessible(true);
					b = c.getConstructor(Material.class).newInstance(m).setUnlocalizedName("essentialcraft."+name).setResistance(resistance).setHardness(hardness).setLightOpacity(opacity);
					BlocksRegistry.registerBlock(new ItemBlockGeneric(b), name, EssentialCraftCore.class);
					EssentialCraftCore.proxy.handleBlockRegister(b);
					return b;
				}
			}
			catch (Exception e1) {
				e1.printStackTrace();
				return null;
			}
		}
	}

	public static Block registerBlockSimple(Block b, String name) {
		BlocksRegistry.registerBlock(new ItemBlockGeneric(b), name, EssentialCraftCore.class);
		EssentialCraftCore.proxy.handleBlockRegister(b);
		return b;
	}

	public static Block registerBlockSimple(Block b, String name, Class<? extends ItemBlock> ib) {
		try {
			ib.getConstructor(Block.class).setAccessible(true);
			BlocksRegistry.registerBlock(ib.getConstructor(Block.class).newInstance(b), name, EssentialCraftCore.class);
			EssentialCraftCore.proxy.handleBlockRegister(b);
			return b;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ItemBlock registerBlockSimple(ItemBlock ib, String name) {
		BlocksRegistry.registerBlock(ib, name, EssentialCraftCore.class);
		EssentialCraftCore.proxy.handleBlockRegister(ib.block);
		return ib;
	}

	public static void createFancyBlock(Material m, String name, String texture, float hardness, float resistance, ItemStack createdFrom) {
		Block fancy = new BlockFancy(m).setModelName("essentialcraft:fancyBlocks/"+texture).setUnlocalizedName("essentialcraft.fancyBlock."+name).setResistance(resistance).setHardness(hardness);
		BlocksRegistry.registerBlock(new ItemBlockFancy(fancy), "fancyBlock."+name, EssentialCraftCore.class);
		EssentialCraftCore.proxy.handleBlockRegister(fancy);
		fancyBlocks.add(fancy);
		RecipeRegistry.fancyBlockRecipes.put(fancy, createdFrom);
	}
	
	public static Block registerFancyBlock(Block fancy, String name, ItemStack createdFrom) {
		BlocksRegistry.registerBlock(new ItemBlockFancy(fancy), "fancyBlock."+name, EssentialCraftCore.class);
		EssentialCraftCore.proxy.handleBlockRegister(fancy);
		fancyBlocks.add(fancy);
		RecipeRegistry.fancyBlockRecipes.put(fancy, createdFrom);
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
	public static Block cacti;
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
	public static BlockPortal portal;
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
	
	public static Block water;
	public static Block lava;
	public static Block fire;

	public static List<Block> fancyBlocks = new ArrayList<Block>();

	public static Block[] lightCorruption = new Block[4];
}
