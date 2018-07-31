package essentialcraft.proxy;

import java.util.List;

import DummyCore.Utils.DummyData;
import essentialcraft.common.entity.EntityDemon;
import essentialcraft.common.inventory.ContainerChargingChamber;
import essentialcraft.common.inventory.ContainerColdDistillator;
import essentialcraft.common.inventory.ContainerCorruptionCleaner;
import essentialcraft.common.inventory.ContainerCrafter;
import essentialcraft.common.inventory.ContainerCraftingFrame;
import essentialcraft.common.inventory.ContainerCrystalController;
import essentialcraft.common.inventory.ContainerCrystalExtractor;
import essentialcraft.common.inventory.ContainerCrystalFormer;
import essentialcraft.common.inventory.ContainerDarknessObelisk;
import essentialcraft.common.inventory.ContainerDemon;
import essentialcraft.common.inventory.ContainerEnderGenerator;
import essentialcraft.common.inventory.ContainerFilter;
import essentialcraft.common.inventory.ContainerFlowerBurner;
import essentialcraft.common.inventory.ContainerFurnaceMagic;
import essentialcraft.common.inventory.ContainerHeatGenerator;
import essentialcraft.common.inventory.ContainerMIM;
import essentialcraft.common.inventory.ContainerMIMCraftingManager;
import essentialcraft.common.inventory.ContainerMIMInventoryStorage;
import essentialcraft.common.inventory.ContainerMIMScreen;
import essentialcraft.common.inventory.ContainerMIMSimpleNode;
import essentialcraft.common.inventory.ContainerMRUAcceptor;
import essentialcraft.common.inventory.ContainerMRUChunkLoader;
import essentialcraft.common.inventory.ContainerMRUCoil;
import essentialcraft.common.inventory.ContainerMRUDimTransciever;
import essentialcraft.common.inventory.ContainerMRUInfo;
import essentialcraft.common.inventory.ContainerMRUIntersector;
import essentialcraft.common.inventory.ContainerMagicalChest;
import essentialcraft.common.inventory.ContainerMagicalEnchanter;
import essentialcraft.common.inventory.ContainerMagicalFurnace;
import essentialcraft.common.inventory.ContainerMagicalHopper;
import essentialcraft.common.inventory.ContainerMagicalJukebox;
import essentialcraft.common.inventory.ContainerMagicalQuarry;
import essentialcraft.common.inventory.ContainerMagicalRepairer;
import essentialcraft.common.inventory.ContainerMagicalTeleporter;
import essentialcraft.common.inventory.ContainerMagicianTable;
import essentialcraft.common.inventory.ContainerMagmaticSmeltery;
import essentialcraft.common.inventory.ContainerMatrixAbsorber;
import essentialcraft.common.inventory.ContainerMithrilineFurnace;
import essentialcraft.common.inventory.ContainerMonsterHarvester;
import essentialcraft.common.inventory.ContainerMonsterHolder;
import essentialcraft.common.inventory.ContainerMoonWell;
import essentialcraft.common.inventory.ContainerPlayerPentacle;
import essentialcraft.common.inventory.ContainerPotionSpreader;
import essentialcraft.common.inventory.ContainerRadiatingChamber;
import essentialcraft.common.inventory.ContainerRayTower;
import essentialcraft.common.inventory.ContainerRedstoneTransmitter;
import essentialcraft.common.inventory.ContainerRightClicker;
import essentialcraft.common.inventory.ContainerSunRayAbsorber;
import essentialcraft.common.inventory.ContainerUltraFlowerBurner;
import essentialcraft.common.inventory.ContainerUltraHeatGenerator;
import essentialcraft.common.inventory.ContainerWeaponBench;
import essentialcraft.common.inventory.ContainerWeatherController;
import essentialcraft.common.inventory.InventoryCraftingFrame;
import essentialcraft.common.inventory.InventoryMagicFilter;
import essentialcraft.common.tile.TileAdvancedBlockBreaker;
import essentialcraft.common.tile.TileAnimalSeparator;
import essentialcraft.common.tile.TileChargingChamber;
import essentialcraft.common.tile.TileColdDistillator;
import essentialcraft.common.tile.TileCorruptionCleaner;
import essentialcraft.common.tile.TileCrafter;
import essentialcraft.common.tile.TileCrystalController;
import essentialcraft.common.tile.TileCrystalExtractor;
import essentialcraft.common.tile.TileCrystalFormer;
import essentialcraft.common.tile.TileDarknessObelisk;
import essentialcraft.common.tile.TileEnderGenerator;
import essentialcraft.common.tile.TileFlowerBurner;
import essentialcraft.common.tile.TileFurnaceMagic;
import essentialcraft.common.tile.TileHeatGenerator;
import essentialcraft.common.tile.TileMIM;
import essentialcraft.common.tile.TileMIMCraftingManager;
import essentialcraft.common.tile.TileMIMExportNode;
import essentialcraft.common.tile.TileMIMImportNode;
import essentialcraft.common.tile.TileMIMInventoryStorage;
import essentialcraft.common.tile.TileMIMScreen;
import essentialcraft.common.tile.TileMRUCUECAcceptor;
import essentialcraft.common.tile.TileMRUCUECStateChecker;
import essentialcraft.common.tile.TileMRUChunkLoader;
import essentialcraft.common.tile.TileMRUCoil;
import essentialcraft.common.tile.TileMRUDimensionalTransciever;
import essentialcraft.common.tile.TileMRUIntersector;
import essentialcraft.common.tile.TileMagicalChest;
import essentialcraft.common.tile.TileMagicalEnchanter;
import essentialcraft.common.tile.TileMagicalFurnace;
import essentialcraft.common.tile.TileMagicalHopper;
import essentialcraft.common.tile.TileMagicalJukebox;
import essentialcraft.common.tile.TileMagicalQuarry;
import essentialcraft.common.tile.TileMagicalRepairer;
import essentialcraft.common.tile.TileMagicalTeleporter;
import essentialcraft.common.tile.TileMagicianTable;
import essentialcraft.common.tile.TileMagmaticSmelter;
import essentialcraft.common.tile.TileMatrixAbsorber;
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
import essentialcraft.common.tile.TileSunRayAbsorber;
import essentialcraft.common.tile.TileUltraFlowerBurner;
import essentialcraft.common.tile.TileUltraHeatGenerator;
import essentialcraft.common.tile.TileWeaponMaker;
import essentialcraft.common.tile.TileWeatherController;
import essentialcraft.common.tile.TileWorldMerger;
import essentialcraft.utils.cfg.Config;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler {

	public void firstMovement(FMLPreInitializationEvent event) {}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == Config.guiID[0]) {
			TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
			if(tile == null) {
				//Item:filter
				if(x == 0 && y == -1 && z == 0) {
					InventoryMagicFilter inventory = new InventoryMagicFilter(player.getHeldItemMainhand());
					return new ContainerFilter(player, inventory);
				}
				//Item: Crafting Frame
				if(x == 0 && y == -2 && z == 0) {
					InventoryCraftingFrame inventory = new InventoryCraftingFrame(player.getHeldItemMainhand());
					return new ContainerCraftingFrame(player, inventory);
				}
			}
			if(tile instanceof TileRayTower) {
				return new ContainerRayTower(player.inventory, tile);
			}
			if(tile instanceof TileMRUCUECAcceptor) {
				return new ContainerMRUAcceptor(player.inventory, tile);
			}
			if(tile instanceof TileMRUCUECStateChecker) {
				return new ContainerMRUInfo(player.inventory);
			}
			if(tile instanceof TileMoonWell) {
				return new ContainerMoonWell(player.inventory, tile);
			}
			if(tile instanceof TileSunRayAbsorber) {
				return new ContainerSunRayAbsorber(player.inventory, tile);
			}
			if(tile instanceof TileColdDistillator) {
				return new ContainerColdDistillator(player.inventory, tile);
			}
			if(tile instanceof TileFlowerBurner) {
				return new ContainerFlowerBurner(player.inventory, tile);
			}
			if(tile instanceof TileHeatGenerator) {
				return new ContainerHeatGenerator(player.inventory, tile);
			}
			if(tile instanceof TileEnderGenerator) {
				return new ContainerEnderGenerator(player.inventory, tile);
			}
			if(tile instanceof TileMagicianTable) {
				return new ContainerMagicianTable(player.inventory, tile);
			}
			if(tile instanceof TileMagicalQuarry) {
				return new ContainerMagicalQuarry(player.inventory, tile);
			}
			if(tile instanceof TileMonsterHolder) {
				return new ContainerMonsterHolder(player.inventory, tile);
			}
			if(tile instanceof TilePotionSpreader) {
				return new ContainerPotionSpreader(player.inventory, tile);
			}
			if(tile instanceof TileMagicalEnchanter) {
				return new ContainerMagicalEnchanter(player.inventory, tile);
			}
			if(tile instanceof TileMonsterHarvester) {
				return new ContainerMonsterHarvester(player.inventory, tile);
			}
			if(tile instanceof TileMagicalRepairer) {
				return new ContainerMagicalRepairer(player.inventory, tile);
			}
			if(tile instanceof TileMatrixAbsorber) {
				return new ContainerMatrixAbsorber(player.inventory, tile);
			}
			if(tile instanceof TileRadiatingChamber) {
				return new ContainerRadiatingChamber(player.inventory, tile);
			}
			if(tile instanceof TileMagmaticSmelter) {
				return new ContainerMagmaticSmeltery(player.inventory, tile);
			}
			if(tile instanceof TileMagicalJukebox) {
				return new ContainerMagicalJukebox(player.inventory, tile);
			}
			if(tile instanceof TileCrystalFormer) {
				return new ContainerCrystalFormer(player.inventory, tile);
			}
			if(tile instanceof TileCrystalController) {
				return new ContainerCrystalController(player.inventory, tile);
			}
			if(tile instanceof TileCrystalExtractor) {
				return new ContainerCrystalExtractor(player.inventory, tile);
			}
			if(tile instanceof TileChargingChamber) {
				return new ContainerChargingChamber(player.inventory, tile);
			}
			if(tile instanceof TileMagicalTeleporter) {
				return new ContainerMagicalTeleporter(player.inventory, tile);
			}
			if(tile instanceof TileMagicalFurnace) {
				return new ContainerMagicalFurnace(player.inventory, tile);
			}
			if(tile instanceof TileMRUCoil) {
				return new ContainerMRUCoil(player.inventory, tile);
			}
			if(tile instanceof TileCorruptionCleaner) {
				return new ContainerCorruptionCleaner(player.inventory, tile);
			}
			if(tile instanceof TileDarknessObelisk) {
				return new ContainerDarknessObelisk(player.inventory, tile);
			}
			if(tile instanceof TileUltraHeatGenerator) {
				return new ContainerUltraHeatGenerator(player.inventory, tile);
			}
			if(tile instanceof TileUltraFlowerBurner) {
				return new ContainerUltraFlowerBurner(player.inventory, tile);
			}
			if(tile instanceof TileMithrilineFurnace) {
				return new ContainerMithrilineFurnace(player.inventory, tile);
			}
			if(tile instanceof TileRightClicker) {
				return new ContainerRightClicker(player.inventory, tile);
			}
			if(tile instanceof TileRedstoneTransmitter) {
				return new ContainerRedstoneTransmitter(player.inventory, tile);
			}
			if(tile instanceof TileMagicalHopper) {
				return new ContainerMagicalHopper(player.inventory, tile);
			}
			if(tile instanceof TileWeaponMaker) {
				return new ContainerWeaponBench(player.inventory, tile);
			}
			if(tile instanceof TileFurnaceMagic) {
				return new ContainerFurnaceMagic(player.inventory, tile);
			}
			if(tile instanceof TileMagicalChest) {
				return new ContainerMagicalChest(player.inventory, tile);
			}
			if(tile instanceof TileMIMInventoryStorage) {
				return new ContainerMIMInventoryStorage(player.inventory, tile);
			}
			if(tile instanceof TileMIM) {
				return new ContainerMIM(player.inventory, tile);
			}
			if(tile instanceof TileMIMScreen) {
				return new ContainerMIMScreen(player.inventory, tile);
			}
			if(tile instanceof TileMIMCraftingManager) {
				return new ContainerMIMCraftingManager(player.inventory, tile);
			}
			if(tile instanceof TileMIMExportNode || tile instanceof TileMIMImportNode || tile instanceof TileAdvancedBlockBreaker) {
				return new ContainerMIMSimpleNode(player.inventory, tile);
			}
			if(tile instanceof TileCrafter) {
				return new ContainerCrafter(player.inventory, tile);
			}
			if(tile instanceof TileAnimalSeparator) {
				return new ContainerRayTower(player.inventory, tile);
			}
			if(tile instanceof TileMRUChunkLoader) {
				return new ContainerMRUChunkLoader(player.inventory, tile);
			}
			if(tile instanceof TileMRUDimensionalTransciever) {
				return new ContainerMRUDimTransciever(player.inventory, tile);
			}
			if(tile instanceof TilePlayerPentacle) {
				return new ContainerPlayerPentacle();
			}
			if(tile instanceof TileMRUIntersector) {
				return new ContainerMRUIntersector(player.inventory, tile);
			}
			if(tile instanceof TileWorldMerger) {
				return new ContainerRayTower(player.inventory, tile);
			}
			if(tile instanceof TileWeatherController) {
				return new ContainerWeatherController(player.inventory, tile);
			}
		}
		if(ID == Config.guiID[1]) {
			List<EntityDemon> demons = world.getEntitiesWithinAABB(EntityDemon.class, new AxisAlignedBB(x-1, y-1, z-1, x+1, y+1, z+1));
			if(!demons.isEmpty()) {
				return new ContainerDemon(player, demons.get(0));
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	public void openBookGUIForPlayer() {}

	public void registerRenderInformation() {}

	public void registerTileEntitySpecialRenderer() {}

	public void handleBlockRegister(Block b) {}

	public void handleItemRegister(Item i) {}

	public World getClientWorld() {
		return null;
	}

	public Object getClientIcon(String iconName) {
		return null;
	}

	public void spawnParticle(String name, float x, float y, float z, double i, double j, double k) {}

	public boolean itemHasEffect(ItemStack stk) {
		return false;
	}

	public ModelBiped getClientModel(int id) {
		return null;
	}

	public Object getRenderer(int index) {
		return null;
	}

	public EntityPlayer getClientPlayer() {
		return null;
	}

	public void registerTexture(ResourceLocation rl) {}

	public void ItemFX(double... ds) {}

	public void FlameFX(double... ds) {}

	public void SmokeFX(double... ds) {}

	public void MRUFX(double... ds) {}

	public void wingsAction(EntityPlayer e, ItemStack s) {}

	public void handlePositionChangePacket(DummyData[] packetData) {}

	public void handleSoundPlay(DummyData[] packetData) {}

	public void stopSound(String soundID) {}

	public void startSound(String soundID, String soundName) {}

	public void startRecord(String soundID, String soundName, BlockPos pos) {}

	public void preInit() {}
}
