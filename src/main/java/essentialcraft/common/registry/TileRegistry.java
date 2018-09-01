package essentialcraft.common.registry;

import java.util.ArrayList;
import java.util.List;

import essentialcraft.common.tile.TileAdvancedBlockBreaker;
import essentialcraft.common.tile.TileAnimalSeparator;
import essentialcraft.common.tile.TileChargingChamber;
import essentialcraft.common.tile.TileColdDistillator;
import essentialcraft.common.tile.TileCorruption;
import essentialcraft.common.tile.TileCorruptionCleaner;
import essentialcraft.common.tile.TileCrafter;
import essentialcraft.common.tile.TileCreativeESPESource;
import essentialcraft.common.tile.TileCreativeMRUSource;
import essentialcraft.common.tile.TileCrystalController;
import essentialcraft.common.tile.TileCrystalExtractor;
import essentialcraft.common.tile.TileCrystalFormer;
import essentialcraft.common.tile.TileDarknessObelisk;
import essentialcraft.common.tile.TileDemonicPentacle;
import essentialcraft.common.tile.TileElementalCrystal;
import essentialcraft.common.tile.TileEmberForge;
import essentialcraft.common.tile.TileEnderGenerator;
import essentialcraft.common.tile.TileFlowerBurner;
import essentialcraft.common.tile.TileFurnaceMagic;
import essentialcraft.common.tile.TileHeatGenerator;
import essentialcraft.common.tile.TileMIM;
import essentialcraft.common.tile.TileMIMCraftingManager;
import essentialcraft.common.tile.TileMIMExportNode;
import essentialcraft.common.tile.TileMIMExportNodePersistant;
import essentialcraft.common.tile.TileMIMImportNode;
import essentialcraft.common.tile.TileMIMImportNodePersistant;
import essentialcraft.common.tile.TileMIMInventoryStorage;
import essentialcraft.common.tile.TileMIMScreen;
import essentialcraft.common.tile.TileMRUCUECAcceptor;
import essentialcraft.common.tile.TileMRUCUECBalancer;
import essentialcraft.common.tile.TileMRUCUECController;
import essentialcraft.common.tile.TileMRUCUECEjector;
import essentialcraft.common.tile.TileMRUCUECHoldingChamber;
import essentialcraft.common.tile.TileMRUCUECRedstoneController;
import essentialcraft.common.tile.TileMRUCUECStateChecker;
import essentialcraft.common.tile.TileMRUChunkLoader;
import essentialcraft.common.tile.TileMRUCoil;
import essentialcraft.common.tile.TileMRUCoilHardener;
import essentialcraft.common.tile.TileMRUDimensionalTransciever;
import essentialcraft.common.tile.TileMRUIntersector;
import essentialcraft.common.tile.TileMRUReactor;
import essentialcraft.common.tile.TileMagicalChest;
import essentialcraft.common.tile.TileMagicalDisplay;
import essentialcraft.common.tile.TileMagicalEnchanter;
import essentialcraft.common.tile.TileMagicalFurnace;
import essentialcraft.common.tile.TileMagicalHopper;
import essentialcraft.common.tile.TileMagicalJukebox;
import essentialcraft.common.tile.TileMagicalMirror;
import essentialcraft.common.tile.TileMagicalQuarry;
import essentialcraft.common.tile.TileMagicalRepairer;
import essentialcraft.common.tile.TileMagicalTeleporter;
import essentialcraft.common.tile.TileMagicianTable;
import essentialcraft.common.tile.TileMagmaticSmelter;
import essentialcraft.common.tile.TileMatrixAbsorber;
import essentialcraft.common.tile.TileMimic;
import essentialcraft.common.tile.TileMithrilineCrystal;
import essentialcraft.common.tile.TileMithrilineFurnace;
import essentialcraft.common.tile.TileMonsterHarvester;
import essentialcraft.common.tile.TileMonsterHolder;
import essentialcraft.common.tile.TileMoonWell;
import essentialcraft.common.tile.TilePlayerPentacle;
import essentialcraft.common.tile.TilePotionSpreader;
import essentialcraft.common.tile.TileRadiatingChamber;
import essentialcraft.common.tile.TileRayTower;
import essentialcraft.common.tile.TileRedstoneTransmitter;
import essentialcraft.common.tile.TileRightClicker;
import essentialcraft.common.tile.TileSolarPrism;
import essentialcraft.common.tile.TileSunRayAbsorber;
import essentialcraft.common.tile.TileUltraFlowerBurner;
import essentialcraft.common.tile.TileUltraHeatGenerator;
import essentialcraft.common.tile.TileWeaponMaker;
import essentialcraft.common.tile.TileWeatherController;
import essentialcraft.common.tile.TileWindRune;
import essentialcraft.common.tile.TileWorldMerger;
import essentialcraft.utils.cfg.Config;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileRegistry {

	public static final List<Class<? extends TileEntity>> CONFIG_DEPENDANT = new ArrayList<Class<? extends TileEntity>>();

	public static void register() {
		addTileToMapping(TileMRUCUECController.class);
		addTileToMapping(TileMRUCUECAcceptor.class);
		addTileToMapping(TileMRUCUECBalancer.class);
		addTileToMapping(TileMRUCUECEjector.class);
		addTileToMapping(TileMRUCUECHoldingChamber.class);
		addTileToMapping(TileMRUCUECRedstoneController.class);
		addTileToMapping(TileMRUCUECStateChecker.class);
		addTileToMapping(TileRayTower.class);
		addTileToMapping(TileCorruption.class);
		addTileToMapping(TileMoonWell.class);
		addTileToMapping(TileSolarPrism.class);
		addTileToMapping(TileSunRayAbsorber.class);
		addTileToMapping(TileColdDistillator.class);
		addTileToMapping(TileFlowerBurner.class);
		addTileToMapping(TileHeatGenerator.class);
		addTileToMapping(TileEnderGenerator.class);
		addTileToMapping(TileMagicianTable.class);
		addTileToMapping(TileMagicalQuarry.class);
		addTileToMapping(TileMonsterHolder.class);
		addTileToMapping(TilePotionSpreader.class);
		addTileToMapping(TileMagicalEnchanter.class);
		addTileToMapping(TileMonsterHarvester.class);
		addTileToMapping(TileMagicalRepairer.class);
		addTileToMapping(TileMatrixAbsorber.class);
		addTileToMapping(TileRadiatingChamber.class);
		addTileToMapping(TileMagmaticSmelter.class);
		addTileToMapping(TileMagicalJukebox.class);
		addTileToMapping(TileElementalCrystal.class);
		addTileToMapping(TileCrystalFormer.class);
		addTileToMapping(TileCrystalController.class);
		addTileToMapping(TileCrystalExtractor.class);
		addTileToMapping(TileChargingChamber.class);
		addTileToMapping(TileMagicalTeleporter.class);
		addTileToMapping(TileMagicalFurnace.class);
		addTileToMapping(TileEmberForge.class);
		addTileToMapping(TileMRUCoilHardener.class);
		addTileToMapping(TileMRUCoil.class);
		addTileToMapping(TileCorruptionCleaner.class);
		addTileToMapping(TileMRUReactor.class);
		addTileToMapping(TileDarknessObelisk.class);
		addTileToMapping(TileUltraHeatGenerator.class);
		addTileToMapping(TileUltraFlowerBurner.class);
		addTileToMapping(TileMagicalMirror.class);
		addTileToMapping(TileMagicalDisplay.class);
		addTileToMapping(TileMithrilineCrystal.class);
		addTileToMapping(TileMithrilineFurnace.class);
		addTileToMapping(TilePlayerPentacle.class);
		addTileToMapping(TileWindRune.class);
		addTileToMapping(TileRightClicker.class);
		addTileToMapping(TileRedstoneTransmitter.class);
		addTileToMapping(TileMagicalHopper.class);
		addTileToMapping(TileDemonicPentacle.class);
		addTileToMapping(TileWeaponMaker.class);
		addTileToMapping(TileFurnaceMagic.class);
		addTileToMapping(TileMagicalChest.class);
		addTileToMapping(TileMIMInventoryStorage.class);
		addTileToMapping(TileMIM.class);
		addTileToMapping(TileMIMScreen.class);
		addTileToMapping(TileMIMCraftingManager.class);
		addTileToMapping(TileMIMExportNode.class);
		addTileToMapping(TileMIMImportNode.class);
		addTileToMapping(TileAdvancedBlockBreaker.class);
		addTileToMapping(TileMIMExportNodePersistant.class);
		addTileToMapping(TileMIMImportNodePersistant.class);
		addTileToMapping(TileCrafter.class);
		addTileToMapping(TileCreativeMRUSource.class);
		addTileToMapping(TileAnimalSeparator.class);
		addTileToMapping(TileMimic.class);
		addTileToMapping(TileMRUChunkLoader.class);
		addTileToMapping(TileMRUDimensionalTransciever.class);
		addTileToMapping(TileMRUIntersector.class);
		addTileToMapping(TileWorldMerger.class);
		addTileToMapping(TileWeatherController.class);
		addTileToMapping(TileCreativeESPESource.class);

		Config.config.save();
	}

	public static void addTileToMapping(Class<? extends TileEntity> tile) {
		GameRegistry.registerTileEntity(tile, new ResourceLocation("essentialcraft:"+tile.getCanonicalName()));
		try {
			if(tile.getMethod("setupConfig", Configuration.class) != null) {
				CONFIG_DEPENDANT.add(tile);
				tile.getMethod("setupConfig", Configuration.class).invoke(null, Config.config);
			}
		}
		catch(NoSuchMethodException e) {
			return;
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
	}
}
