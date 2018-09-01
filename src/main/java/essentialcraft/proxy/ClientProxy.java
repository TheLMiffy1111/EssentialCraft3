package essentialcraft.proxy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Keyboard;

import DummyCore.Client.GuiCommon;
import DummyCore.Client.MainMenuRegistry;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.DummyPacketHandler;
import DummyCore.Utils.DummyPacketIMSG;
import DummyCore.Utils.MathUtils;
import essentialcraft.client.gui.GuiChargingChamber;
import essentialcraft.client.gui.GuiColdDistillator;
import essentialcraft.client.gui.GuiCorruptionCleaner;
import essentialcraft.client.gui.GuiCrafter;
import essentialcraft.client.gui.GuiCraftingFrame;
import essentialcraft.client.gui.GuiCrystalController;
import essentialcraft.client.gui.GuiCrystalExtractor;
import essentialcraft.client.gui.GuiCrystalFormer;
import essentialcraft.client.gui.GuiDarknessObelisk;
import essentialcraft.client.gui.GuiDemon;
import essentialcraft.client.gui.GuiEnderGenerator;
import essentialcraft.client.gui.GuiFilter;
import essentialcraft.client.gui.GuiFlowerBurner;
import essentialcraft.client.gui.GuiFurnaceMagic;
import essentialcraft.client.gui.GuiHeatGenerator;
import essentialcraft.client.gui.GuiMIM;
import essentialcraft.client.gui.GuiMIMCraftingManager;
import essentialcraft.client.gui.GuiMIMInventoryStorage;
import essentialcraft.client.gui.GuiMIMScreen;
import essentialcraft.client.gui.GuiMRUAcceptor;
import essentialcraft.client.gui.GuiMRUChunkLoader;
import essentialcraft.client.gui.GuiMRUCoil;
import essentialcraft.client.gui.GuiMRUDimTransciever;
import essentialcraft.client.gui.GuiMRUInfo;
import essentialcraft.client.gui.GuiMRUIntersector;
import essentialcraft.client.gui.GuiMagicalChest;
import essentialcraft.client.gui.GuiMagicalEnchanter;
import essentialcraft.client.gui.GuiMagicalFurnace;
import essentialcraft.client.gui.GuiMagicalJukebox;
import essentialcraft.client.gui.GuiMagicalQuarry;
import essentialcraft.client.gui.GuiMagicalRepairer;
import essentialcraft.client.gui.GuiMagicalTeleporter;
import essentialcraft.client.gui.GuiMagicianTable;
import essentialcraft.client.gui.GuiMagmaticSmeltery;
import essentialcraft.client.gui.GuiMainMenuEC;
import essentialcraft.client.gui.GuiMatrixAbsorber;
import essentialcraft.client.gui.GuiMithrilineFurnace;
import essentialcraft.client.gui.GuiMonsterHarvester;
import essentialcraft.client.gui.GuiMonsterHolder;
import essentialcraft.client.gui.GuiMoonWell;
import essentialcraft.client.gui.GuiPlayerPentacle;
import essentialcraft.client.gui.GuiPotionSpreader;
import essentialcraft.client.gui.GuiRadiatingChamber;
import essentialcraft.client.gui.GuiRayTower;
import essentialcraft.client.gui.GuiResearchBook;
import essentialcraft.client.gui.GuiRightClicker;
import essentialcraft.client.gui.GuiSunRayAbsorber;
import essentialcraft.client.gui.GuiUltraFlowerBurner;
import essentialcraft.client.gui.GuiUltraHeatGenerator;
import essentialcraft.client.gui.GuiWeaponBench;
import essentialcraft.client.gui.GuiWeatherController;
import essentialcraft.client.model.ModelArmorEC;
import essentialcraft.client.particle.ParticleCSpell;
import essentialcraft.client.particle.ParticleColoredFlame;
import essentialcraft.client.particle.ParticleFog;
import essentialcraft.client.particle.ParticleItem;
import essentialcraft.client.particle.ParticleMRU;
import essentialcraft.client.particle.ParticleSmokeEC;
import essentialcraft.client.render.RenderCloudsHoanna;
import essentialcraft.client.render.RenderHandlerEC;
import essentialcraft.client.render.RenderSkyHoanna;
import essentialcraft.client.render.entity.RenderDemon;
import essentialcraft.client.render.entity.RenderDivider;
import essentialcraft.client.render.entity.RenderHologram;
import essentialcraft.client.render.entity.RenderMRUArrow;
import essentialcraft.client.render.entity.RenderMRUPresence;
import essentialcraft.client.render.entity.RenderMRURay;
import essentialcraft.client.render.entity.RenderOrbitalStrike;
import essentialcraft.client.render.entity.RenderPlayerClone;
import essentialcraft.client.render.entity.RenderPoisonFume;
import essentialcraft.client.render.entity.RenderSolarBeam;
import essentialcraft.client.render.entity.RenderWindMage;
import essentialcraft.client.render.entity.ThrowableRenderFactory;
import essentialcraft.client.render.tile.RenderChargingChamber;
import essentialcraft.client.render.tile.RenderColdDistillator;
import essentialcraft.client.render.tile.RenderCorruptionCleaner;
import essentialcraft.client.render.tile.RenderCrystalController;
import essentialcraft.client.render.tile.RenderCrystalExtractor;
import essentialcraft.client.render.tile.RenderCrystalFormer;
import essentialcraft.client.render.tile.RenderDarknessObelisk;
import essentialcraft.client.render.tile.RenderDemonicPentacle;
import essentialcraft.client.render.tile.RenderElementalCrystal;
import essentialcraft.client.render.tile.RenderEnderGenerator;
import essentialcraft.client.render.tile.RenderMIM;
import essentialcraft.client.render.tile.RenderMRUCoil;
import essentialcraft.client.render.tile.RenderMRUCoilHardener;
import essentialcraft.client.render.tile.RenderMRULink;
import essentialcraft.client.render.tile.RenderMRUReactor;
import essentialcraft.client.render.tile.RenderMagicalChest;
import essentialcraft.client.render.tile.RenderMagicalDisplay;
import essentialcraft.client.render.tile.RenderMagicalEnchanter;
import essentialcraft.client.render.tile.RenderMagicalJukebox;
import essentialcraft.client.render.tile.RenderMagicalMirror;
import essentialcraft.client.render.tile.RenderMagicalQuarry;
import essentialcraft.client.render.tile.RenderMagicalRepairer;
import essentialcraft.client.render.tile.RenderMagicianTable;
import essentialcraft.client.render.tile.RenderMatrixAbsorber;
import essentialcraft.client.render.tile.RenderMithrilineCrystal;
import essentialcraft.client.render.tile.RenderMonsterHarvester;
import essentialcraft.client.render.tile.RenderMonsterHolder;
import essentialcraft.client.render.tile.RenderPlayerPentacle;
import essentialcraft.client.render.tile.RenderRadiatingChamber;
import essentialcraft.client.render.tile.RenderRayTower;
import essentialcraft.client.render.tile.RenderWeatherController;
import essentialcraft.client.render.tile.RenderWindRune;
import essentialcraft.client.render.tile.RenderWorldMerger;
import essentialcraft.common.entity.EntityArmorDestroyer;
import essentialcraft.common.entity.EntityDemon;
import essentialcraft.common.entity.EntityDivider;
import essentialcraft.common.entity.EntityDividerProjectile;
import essentialcraft.common.entity.EntityHologram;
import essentialcraft.common.entity.EntityMRUArrow;
import essentialcraft.common.entity.EntityMRUPresence;
import essentialcraft.common.entity.EntityMRURay;
import essentialcraft.common.entity.EntityOrbitalStrike;
import essentialcraft.common.entity.EntityPlayerClone;
import essentialcraft.common.entity.EntityPoisonFume;
import essentialcraft.common.entity.EntityShadowKnife;
import essentialcraft.common.entity.EntitySolarBeam;
import essentialcraft.common.entity.EntityWindMage;
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
import essentialcraft.common.inventory.ContainerMIMSimpleNode;
import essentialcraft.common.inventory.ContainerMRUAcceptor;
import essentialcraft.common.inventory.ContainerMRUChunkLoader;
import essentialcraft.common.inventory.ContainerMRUCoil;
import essentialcraft.common.inventory.ContainerMRUDimTransciever;
import essentialcraft.common.inventory.ContainerMRUInfo;
import essentialcraft.common.inventory.ContainerMRUIntersector;
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
import essentialcraft.common.item.ItemSecret;
import essentialcraft.common.item.ItemsCore;
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
import essentialcraft.common.tile.TileDemonicPentacle;
import essentialcraft.common.tile.TileElementalCrystal;
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
import essentialcraft.common.tile.TileSunRayAbsorber;
import essentialcraft.common.tile.TileUltraFlowerBurner;
import essentialcraft.common.tile.TileUltraHeatGenerator;
import essentialcraft.common.tile.TileWeaponMaker;
import essentialcraft.common.tile.TileWeatherController;
import essentialcraft.common.tile.TileWindRune;
import essentialcraft.common.tile.TileWorldMerger;
import essentialcraft.utils.cfg.Config;
import essentialcraft.utils.common.ECEventHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.TempCategory;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

public class ClientProxy extends CommonProxy {

	public static List<Pair<String, ISound>> playingMusic = new CopyOnWriteArrayList<Pair<String, ISound>>();

	@Override
	public void firstMovement(FMLPreInitializationEvent event) {
		super.firstMovement(event);
		OBJLoader.INSTANCE.addDomain("essentialcraft");
		//ModelLoaderRegistry.registerLoader(ModelGun_LoaderBased.LoaderGun.INSTANCE);
	}

	public boolean listHasKey(String key) {
		for(int i = 0; i < playingMusic.size(); ++i) {
			if(playingMusic.get(i).getLeft().equals(key)) {
				return true;
			}
		}

		return false;
	}

	public int positionOf(String key) {
		for(int i = 0; i < playingMusic.size(); ++i) {
			if(playingMusic.get(i).getLeft().equals(key)) {
				return i;
			}
		}

		return 0;
	}

	@Override
	public void stopSound(String soundID) {
		if(listHasKey(soundID)) {
			int pos = positionOf(soundID);
			Minecraft.getMinecraft().getSoundHandler().stopSound(playingMusic.get(pos).getRight());
			playingMusic.remove(pos);
		}
	}

	@Override
	public void startSound(String soundID, String soundName) {
		if(!listHasKey(soundID)) {
			PositionedSoundRecord s = PositionedSoundRecord.getMusicRecord(SoundEvent.REGISTRY.getObject(new ResourceLocation(soundName)));
			playingMusic.add(Pair.<String, ISound>of(soundID, s));

			Minecraft.getMinecraft().getSoundHandler().playSound(s);
		}
	}

	@Override
	public void startRecord(String soundID, String soundName, BlockPos pos) {
		if(!listHasKey(soundID)) {
			PositionedSoundRecord s = PositionedSoundRecord.getRecordSoundRecord(SoundEvent.REGISTRY.getObject(new ResourceLocation(soundName)), pos.getX()+0.5F, pos.getY()+0.5F, pos.getZ()+0.5F);
			playingMusic.add(Pair.<String, ISound>of(soundID, s));

			Minecraft.getMinecraft().getSoundHandler().playSound(s);
		}
	}

	ResourceLocation villagerSkin = new ResourceLocation("essentialcraft","textures/entities/magician.png");

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == Config.guiID[0]) {
			TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
			if(tile == null) {
				//Item:filter
				if(x == 0 && y == -1 && z == 0) {
					InventoryMagicFilter inventory = new InventoryMagicFilter(player.getHeldItemMainhand());
					return new GuiFilter(new ContainerFilter(player, inventory), inventory);
				}
				//Item: Crafting Frame
				if(x == 0 && y == -2 && z == 0) {
					InventoryCraftingFrame inventory = new InventoryCraftingFrame(player.getHeldItemMainhand());
					return new GuiCraftingFrame(new ContainerCraftingFrame(player, inventory), inventory);
				}
			}
			if(tile instanceof TileRayTower) {
				return new GuiRayTower(new ContainerRayTower(player.inventory, tile), tile);
			}
			if(tile instanceof TileMRUCUECAcceptor) {
				return new GuiMRUAcceptor(new ContainerMRUAcceptor(player.inventory, tile), tile);
			}
			if(tile instanceof TileMRUCUECStateChecker) {
				return new GuiMRUInfo(new ContainerMRUInfo(player.inventory), tile);
			}
			if(tile instanceof TileMoonWell) {
				return new GuiMoonWell(new ContainerMoonWell(player.inventory, tile), tile);
			}
			if(tile instanceof TileSunRayAbsorber) {
				return new GuiSunRayAbsorber(new ContainerSunRayAbsorber(player.inventory, tile), tile);
			}
			if(tile instanceof TileColdDistillator) {
				return new GuiColdDistillator(new ContainerColdDistillator(player.inventory, tile), tile);
			}
			if(tile instanceof TileFlowerBurner) {
				return new GuiFlowerBurner(new ContainerFlowerBurner(player.inventory, tile), tile);
			}
			if(tile instanceof TileHeatGenerator) {
				return new GuiHeatGenerator(new ContainerHeatGenerator(player.inventory, tile), tile);
			}
			if(tile instanceof TileEnderGenerator) {
				return new GuiEnderGenerator(new ContainerEnderGenerator(player.inventory, tile), tile);
			}
			if(tile instanceof TileMagicianTable) {
				return new GuiMagicianTable(new ContainerMagicianTable(player.inventory, tile), (TileMagicianTable)tile);
			}
			if(tile instanceof TileMagicalQuarry) {
				return new GuiMagicalQuarry(new ContainerMagicalQuarry(player.inventory, tile), tile);
			}
			if(tile instanceof TileMonsterHolder) {
				return new GuiMonsterHolder(new ContainerMonsterHolder(player.inventory, tile), tile);
			}
			if(tile instanceof TilePotionSpreader) {
				return new GuiPotionSpreader(new ContainerPotionSpreader(player.inventory, tile), tile);
			}
			if(tile instanceof TileMagicalEnchanter) {
				return new GuiMagicalEnchanter(new ContainerMagicalEnchanter(player.inventory, tile), (TileMagicalEnchanter)tile);
			}
			if(tile instanceof TileMonsterHarvester) {
				return new GuiMonsterHarvester(new ContainerMonsterHarvester(player.inventory, tile), tile);
			}
			if(tile instanceof TileMagicalRepairer) {
				return new GuiMagicalRepairer(new ContainerMagicalRepairer(player.inventory, tile), tile);
			}
			if(tile instanceof TileMatrixAbsorber) {
				return new GuiMatrixAbsorber(new ContainerMatrixAbsorber(player.inventory, tile), tile);
			}
			if(tile instanceof TileRadiatingChamber) {
				return new GuiRadiatingChamber(new ContainerRadiatingChamber(player.inventory, tile), tile);
			}
			if(tile instanceof TileMagmaticSmelter) {
				return new GuiMagmaticSmeltery(new ContainerMagmaticSmeltery(player.inventory, tile), tile);
			}
			if(tile instanceof TileMagicalJukebox) {
				return new GuiMagicalJukebox(new ContainerMagicalJukebox(player.inventory, tile), tile);
			}
			if(tile instanceof TileCrystalFormer) {
				return new GuiCrystalFormer(new ContainerCrystalFormer(player.inventory, tile), tile);
			}
			if(tile instanceof TileCrystalController) {
				return new GuiCrystalController(new ContainerCrystalController(player.inventory, tile), tile);
			}
			if(tile instanceof TileCrystalExtractor) {
				return new GuiCrystalExtractor(new ContainerCrystalExtractor(player.inventory, tile), tile);
			}
			if(tile instanceof TileChargingChamber) {
				return new GuiChargingChamber(new ContainerChargingChamber(player.inventory, tile), tile);
			}
			if(tile instanceof TileMagicalTeleporter) {
				return new GuiMagicalTeleporter(new ContainerMagicalTeleporter(player.inventory, tile), tile);
			}
			if(tile instanceof TileMagicalFurnace) {
				return new GuiMagicalFurnace(new ContainerMagicalFurnace(player.inventory, tile), tile);
			}
			if(tile instanceof TileMRUCoil) {
				return new GuiMRUCoil(new ContainerMRUCoil(player.inventory, tile), tile);
			}
			if(tile instanceof TileCorruptionCleaner) {
				return new GuiCorruptionCleaner(new ContainerCorruptionCleaner(player.inventory, tile), tile);
			}
			if(tile instanceof TileDarknessObelisk) {
				return new GuiDarknessObelisk(new ContainerDarknessObelisk(player.inventory, tile), tile);
			}
			if(tile instanceof TileUltraHeatGenerator) {
				return new GuiUltraHeatGenerator(new ContainerUltraHeatGenerator(player.inventory, tile), tile);
			}
			if(tile instanceof TileUltraFlowerBurner) {
				return new GuiUltraFlowerBurner(new ContainerUltraFlowerBurner(player.inventory, tile), tile);
			}
			if(tile instanceof TileMithrilineFurnace) {
				return new GuiMithrilineFurnace(new ContainerMithrilineFurnace(player.inventory, tile), (TileMithrilineFurnace)tile);
			}
			if(tile instanceof TileRightClicker) {
				return new GuiRightClicker(new ContainerRightClicker(player.inventory, tile), tile);
			}
			if(tile instanceof TileRedstoneTransmitter) {
				return new GuiCommon(new ContainerRedstoneTransmitter(player.inventory, tile), tile);
			}
			if(tile instanceof TileMagicalHopper) {
				return new GuiCommon(new ContainerMagicalHopper(player.inventory, tile), tile);
			}
			if(tile instanceof TileWeaponMaker) {
				return new GuiWeaponBench(new ContainerWeaponBench(player.inventory, tile), tile);
			}
			if(tile instanceof TileFurnaceMagic) {
				return new GuiFurnaceMagic(new ContainerFurnaceMagic(player.inventory, tile), (TileFurnaceMagic)tile);
			}
			if(tile instanceof TilePlayerPentacle) {
				return new GuiPlayerPentacle(tile);
			}
			if(tile instanceof TileMagicalChest) {
				return new GuiMagicalChest(player.inventory, (TileMagicalChest)tile);
			}
			if(tile instanceof TileMIMInventoryStorage) {
				return new GuiMIMInventoryStorage(player.inventory, (TileMIMInventoryStorage)tile);
			}
			if(tile instanceof TileMIM) {
				return new GuiMIM(new ContainerMIM(player.inventory, tile), tile);
			}
			if(tile instanceof TileMIMScreen) {
				return new GuiMIMScreen((TileMIMScreen) tile, player);
			}
			if(tile instanceof TileMIMCraftingManager) {
				return new GuiMIMCraftingManager(player.inventory, (TileMIMCraftingManager) tile);
			}
			if(tile instanceof TileMIMExportNode || tile instanceof TileMIMImportNode || tile instanceof TileAdvancedBlockBreaker) {
				return new GuiCommon(new ContainerMIMSimpleNode(player.inventory, tile), tile);
			}
			if(tile instanceof TileCrafter) {
				return new GuiCrafter(new ContainerCrafter(player.inventory, tile),(TileCrafter) tile);
			}
			if(tile instanceof TileAnimalSeparator) {
				return new GuiRayTower(new ContainerRayTower(player.inventory, tile), tile);
			}
			if(tile instanceof TileMRUChunkLoader) {
				return new GuiMRUChunkLoader(new ContainerMRUChunkLoader(player.inventory, tile), tile);
			}
			if(tile instanceof TileMRUDimensionalTransciever) {
				return new GuiMRUDimTransciever(new ContainerMRUDimTransciever(player.inventory, tile), tile);
			}
			if(tile instanceof TileMRUIntersector) {
				return new GuiMRUIntersector(new ContainerMRUIntersector(player.inventory, tile), tile);
			}
			if(tile instanceof TileWorldMerger) {
				return new GuiRayTower(new ContainerRayTower(player.inventory, tile), tile);
			}
			if(tile instanceof TileWeatherController) {
				return new GuiWeatherController(new ContainerWeatherController(player.inventory, tile), (TileWeatherController)tile);
			}
		}
		if(ID == Config.guiID[1]) {
			List<EntityDemon> demons = world.getEntitiesWithinAABB(EntityDemon.class, new AxisAlignedBB(x-1, y-1, z-1, x+1, y+1, z+1));
			if(!demons.isEmpty()) {
				return new GuiDemon(new ContainerDemon(player, demons.get(0)));
			}
		}
		return null;
	}

	@Override
	public void openBookGUIForPlayer() {
		Minecraft.getMinecraft().displayGuiScreen(new GuiResearchBook());
	}

	@Override
	public void registerRenderInformation() {
		MainMenuRegistry.registerNewGui(GuiMainMenuEC.class, "[EC] Magical Menu", "For EssentialCraft Fans ;)");
		RenderingRegistry.registerEntityRenderingHandler(EntityMRUPresence.class, new RenderMRUPresence.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityMRUArrow.class, new RenderMRUArrow.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntitySolarBeam.class, new RenderSolarBeam.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityWindMage.class, new RenderWindMage.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityPoisonFume.class, new RenderPoisonFume.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityShadowKnife.class, new ThrowableRenderFactory<EntityShadowKnife>(ItemsCore.shadeKnife));
		RenderingRegistry.registerEntityRenderingHandler(EntityArmorDestroyer.class, new ThrowableRenderFactory<EntityArmorDestroyer>(ItemsCore.magicalSlag));
		RenderingRegistry.registerEntityRenderingHandler(EntityDividerProjectile.class, new ThrowableRenderFactory<EntityDividerProjectile>(ItemsCore.magicalSlag));
		RenderingRegistry.registerEntityRenderingHandler(EntityMRURay.class, new RenderMRURay.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityDemon.class, new RenderDemon.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityHologram.class, new RenderHologram.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityPlayerClone.class, new RenderPlayerClone.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityOrbitalStrike.class, new RenderOrbitalStrike.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityDivider.class, new RenderDivider.Factory());
		MinecraftForge.EVENT_BUS.register(new RenderHandlerEC());

		kbArmorBoost = new KeyBinding("ComputerArmorBoost", Keyboard.KEY_Z, "key.categories.gameplay");
		ClientRegistry.registerKeyBinding(kbArmorBoost);
		kbArmorVision = new KeyBinding("ComputerArmorNightVision", Keyboard.KEY_X, "key.categories.gameplay");
		ClientRegistry.registerKeyBinding(kbArmorVision);
	}

	@Override
	public void registerTileEntitySpecialRenderer() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileRayTower.class, new RenderRayTower());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMRUCUECAcceptor.class, new RenderMRULink());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMagicianTable.class, new RenderMagicianTable());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMagicalQuarry.class, new RenderMagicalQuarry());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMonsterHolder.class, new RenderMonsterHolder());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMagicalEnchanter.class, new RenderMagicalEnchanter());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMonsterHarvester.class, new RenderMonsterHarvester());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMagicalRepairer.class, new RenderMagicalRepairer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMatrixAbsorber.class, new RenderMatrixAbsorber());
		ClientRegistry.bindTileEntitySpecialRenderer(TileRadiatingChamber.class, new RenderRadiatingChamber());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMagicalJukebox.class, new RenderMagicalJukebox());
		ClientRegistry.bindTileEntitySpecialRenderer(TileElementalCrystal.class, new RenderElementalCrystal());
		ClientRegistry.bindTileEntitySpecialRenderer(TileCrystalFormer.class, new RenderCrystalFormer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileChargingChamber.class, new RenderChargingChamber());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMRUCoilHardener.class, new RenderMRUCoilHardener());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMRUCoil.class, new RenderMRUCoil());
		ClientRegistry.bindTileEntitySpecialRenderer(TileCorruptionCleaner.class, new RenderCorruptionCleaner());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMRUReactor.class, new RenderMRUReactor());
		ClientRegistry.bindTileEntitySpecialRenderer(TileDarknessObelisk.class, new RenderDarknessObelisk());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMagicalMirror.class, new RenderMagicalMirror());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMagicalDisplay.class, new RenderMagicalDisplay());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMithrilineCrystal.class, new RenderMithrilineCrystal());
		ClientRegistry.bindTileEntitySpecialRenderer(TilePlayerPentacle.class, new RenderPlayerPentacle());
		ClientRegistry.bindTileEntitySpecialRenderer(TileWindRune.class, new RenderWindRune());
		ClientRegistry.bindTileEntitySpecialRenderer(TileDemonicPentacle.class, new RenderDemonicPentacle());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMagicalChest.class, new RenderMagicalChest());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMIM.class, new RenderMIM());
		ClientRegistry.bindTileEntitySpecialRenderer(TileWorldMerger.class, new RenderWorldMerger());
		ClientRegistry.bindTileEntitySpecialRenderer(TileWeatherController.class, new RenderWeatherController());
		{
			ClientRegistry.bindTileEntitySpecialRenderer(TileEnderGenerator.class, new RenderEnderGenerator());
			ClientRegistry.bindTileEntitySpecialRenderer(TileColdDistillator.class, new RenderColdDistillator());
			ClientRegistry.bindTileEntitySpecialRenderer(TileCrystalController.class, new RenderCrystalController());
			ClientRegistry.bindTileEntitySpecialRenderer(TileCrystalExtractor.class, new RenderCrystalExtractor());
		}
	}

	@Override
	public void handleBlockRegister(Block b) {

	}

	@Override
	public void handleItemRegister(Item i) {

	}

	@Override
	public World getClientWorld() {
		return Minecraft.getMinecraft().world;
	}

	@Override
	public Object getClientIcon(String str) {
		if(str.equals("mru"))
			return mruIcon;
		if(str.equals("chaosIcon"))
			return chaosIcon;
		if(str.equals("frozenIcon"))
			return frozenIcon;
		if(str.equals("mruParticleIcon"))
			return mruParticleIcon;
		if(str.equals("particle_fogFX"))
			return fogIcon;
		if(str.startsWith("consSpellParticle")) {
			int index = str.indexOf('_');
			if(index != -1) {
				int arrayNum = Integer.parseInt(str.substring(index+1));
				return c_spell_particle_array[arrayNum];
			}
		}
		return null;
	}

	@Override
	public void spawnParticle(String name, float x, float y, float z, double i, double j, double k) {
		if(true && doParticle()) {
			if(name.equals("mruFX"))
				Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleMRU(Minecraft.getMinecraft().world, x, y, z, i, j, k));
			if(name.equals("cSpellFX"))
				Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleCSpell(Minecraft.getMinecraft().world, x, y, z, i, j, k));
			if(name.equals("fogFX"))
				Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleFog(Minecraft.getMinecraft().world, x, y, z, i, j, k));
		}
	}

	@Override
	public boolean itemHasEffect(ItemStack stk) {
		try {
			if(stk.getItem() instanceof ItemSecret) {
				int metadata = stk.getItemDamage();
				switch(metadata) {
				case 0:
					EntityPlayer player = Minecraft.getMinecraft().player;
					World wrld = Minecraft.getMinecraft().world;
					List<EntityPlayer> playerLst = wrld.<EntityPlayer>getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(player.posX-10, player.posY-10, player.posZ-10, player.posX+10, player.posY+10, player.posZ+10));
					Biome biome = wrld.getBiome(player.getPosition());
					return wrld.getWorldTime() % 24000 >= 14000 && wrld.getWorldTime() % 24000 <= 16000 && player.rotationPitch <= -42 && player.rotationPitch >= -65 && playerLst.size() == 1 && !wrld.isRaining() && (biome.getTempCategory() == TempCategory.WARM || biome.getTempCategory() == TempCategory.MEDIUM);
				}
			}
		}
		catch(Exception e) {}
		return false;
	}

	@Override
	public ModelBiped getClientModel(int id) {
		switch (id) {
		case 0:
			return chest;
		case 1:
			return legs;
		case 2:
			return chest1;
		default: break;
		}
		return chest;
	}

	@Override
	public Object getRenderer(int index) {
		if(index == 0)
			return skyedRenderer;
		else
			return cloudedRenderer;
	}

	@Override
	public EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().player;
	}

	@Override
	public void ItemFX(double... ds) {
		if(true && doParticle()) {
			if(ds.length == 6)
				Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleItem(Minecraft.getMinecraft().world, ds[0], ds[1], ds[2], 1, 0, 1, ds[3], ds[4], ds[5]));
		}
	}

	@Override
	public void FlameFX(double... ds) {
		if(true && doParticle()) {
			if(ds.length == 10)
				Minecraft.getMinecraft().effectRenderer.addEffect(
						new ParticleColoredFlame(Minecraft.getMinecraft().world, ds[0], ds[1], ds[2], ds[3], ds[4], ds[5], ds[6], ds[7], ds[8], ds[9]));
		}
	}

	@Override
	public void SmokeFX(double... ds) {
		if(true && doParticle()) {
			if(ds.length == 7)
				Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleSmokeEC(Minecraft.getMinecraft().world, ds[0], ds[1], ds[2], ds[3], ds[4], ds[5], (float)ds[6]));
			if(ds.length == 10)
				Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleSmokeEC(Minecraft.getMinecraft().world, ds[0], ds[1], ds[2], ds[3], ds[4], ds[5], (float)ds[6], ds[7], ds[8], ds[9]));
		}
	}

	@Override
	public void MRUFX(double... ds) {
		if(true && doParticle()) {
			if(ds.length == 6)
				Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleMRU(Minecraft.getMinecraft().world, ds[0], ds[1], ds[2], ds[3], ds[4], ds[5]));
			if(ds.length == 9)
				Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleMRU(Minecraft.getMinecraft().world, ds[0], ds[1], ds[2], ds[3], ds[4], ds[5], ds[6], ds[7], ds[8]));
		}
	}

	@Override
	public void wingsAction(EntityPlayer e, ItemStack s) {
		if(GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump) && Minecraft.getMinecraft().inGameHasFocus) {
			e.getEntityWorld().spawnParticle(EnumParticleTypes.REDSTONE, e.posX+MathUtils.randomDouble(e.getEntityWorld().rand)/2, e.posY-1+MathUtils.randomDouble(e.getEntityWorld().rand), e.posZ+MathUtils.randomDouble(e.getEntityWorld().rand)/2, 0, 1, 1);
			e.motionY += 0.1F;
			e.fallDistance = 0F;
			double pX = e.posX;
			double pY = e.posY;
			double pZ = e.posZ;
			String dataString = new String();
			dataString += "||mod:EC3.Item.Wings";
			dataString += "||x:"+pX+"||y:"+pY+"||z:"+pZ;
			dataString += "||playername:"+e.getGameProfile().getId();
			DummyPacketIMSG pkt = new DummyPacketIMSG(dataString);
			DummyPacketHandler.sendToServer(pkt);
		}
	}

	@Override
	public void handlePositionChangePacket(DummyData[] packetData) {
		double sX = Double.parseDouble(packetData[1].fieldValue);
		double sY = Double.parseDouble(packetData[2].fieldValue);
		double sZ = Double.parseDouble(packetData[3].fieldValue);
		float yaw = Float.parseFloat(packetData[4].fieldValue);
		float pitch = Float.parseFloat(packetData[5].fieldValue);
		EntityPlayer player = Minecraft.getMinecraft().player;
		player.setPositionAndRotation(sX, sY, sZ,yaw,pitch);
		player.rotationYawHead = player.rotationYaw;
	}

	@Override
	public void handleSoundPlay(DummyData[] packetData) {
		double sX = Double.parseDouble(packetData[1].fieldValue);
		double sY = Double.parseDouble(packetData[2].fieldValue);
		double sZ = Double.parseDouble(packetData[3].fieldValue);
		float volume = Float.parseFloat(packetData[4].fieldValue);
		float pitch = Float.parseFloat(packetData[5].fieldValue);
		String sound = packetData[6].fieldValue;
		EntityPlayer player = Minecraft.getMinecraft().player;
		player.getEntityWorld().playSound(sX, sY, sZ, SoundEvent.REGISTRY.getObject(new ResourceLocation(sound)), SoundCategory.MASTER, volume, pitch, false);
	}

	@Override
	public void registerTexture(ResourceLocation rl) {
		ECEventHandler.textures.add(rl);
	}

	private boolean doParticle() {
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
			return false;

		float chance = 1F;
		if(Minecraft.getMinecraft().gameSettings.particleSetting == 1)
			chance = 0.6F;
		else if(Minecraft.getMinecraft().gameSettings.particleSetting == 2)
			chance = 0.2F;

		return chance == 1F || Math.random() < chance;
	}

	public static TextureAtlasSprite mruIcon;
	public static TextureAtlasSprite mruParticleIcon;
	public static TextureAtlasSprite[] c_spell_particle_array = new TextureAtlasSprite[4];
	public static TextureAtlasSprite chaosIcon;
	public static TextureAtlasSprite frozenIcon;
	public static TextureAtlasSprite fogIcon;

	private static IRenderHandler skyedRenderer = new RenderSkyHoanna();
	private static IRenderHandler cloudedRenderer = new RenderCloudsHoanna();

	private static final ModelArmorEC chest = new ModelArmorEC(1.0F);
	private static final ModelArmorEC chest1 = new ModelArmorEC(0.75F);
	private static final ModelArmorEC legs = new ModelArmorEC(0.5F);

	public static KeyBinding kbArmorBoost;
	public static KeyBinding kbArmorVision;
}
