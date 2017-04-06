package ec3.network.proxy;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Keyboard;

import DummyCore.Client.GuiCommon;
import DummyCore.Client.MainMenuRegistry;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.DummyPacketHandler;
import DummyCore.Utils.DummyPacketIMSG;
import DummyCore.Utils.MathUtils;
import ec3.client.fx.CSpellFX;
import ec3.client.fx.ColoredFlameFX;
import ec3.client.fx.FogFX;
import ec3.client.fx.ItemFX;
import ec3.client.fx.MRUFX;
import ec3.client.fx.SmokeFX;
import ec3.client.gui.GuiChargingChamber;
import ec3.client.gui.GuiColdDistillator;
import ec3.client.gui.GuiCorruptionCleaner;
import ec3.client.gui.GuiCrafter;
import ec3.client.gui.GuiCraftingFrame;
import ec3.client.gui.GuiCrystalController;
import ec3.client.gui.GuiCrystalExtractor;
import ec3.client.gui.GuiCrystalFormer;
import ec3.client.gui.GuiDarknessObelisk;
import ec3.client.gui.GuiDemon;
import ec3.client.gui.GuiEnderGenerator;
import ec3.client.gui.GuiFilter;
import ec3.client.gui.GuiFlowerBurner;
import ec3.client.gui.GuiFurnaceMagic;
import ec3.client.gui.GuiHeatGenerator;
import ec3.client.gui.GuiMIMCraftingManager;
import ec3.client.gui.GuiMIMInventoryStorage;
import ec3.client.gui.GuiMRUAcceptor;
import ec3.client.gui.GuiMRUChunkLoader;
import ec3.client.gui.GuiMRUCoil;
import ec3.client.gui.GuiMRUDimTransciever;
import ec3.client.gui.GuiMRUInfo;
import ec3.client.gui.GuiMagicalChest;
import ec3.client.gui.GuiMagicalEnchanter;
import ec3.client.gui.GuiMagicalFurnace;
import ec3.client.gui.GuiMagicalJukebox;
import ec3.client.gui.GuiMagicalQuarry;
import ec3.client.gui.GuiMagicalRepairer;
import ec3.client.gui.GuiMagicalTeleporter;
import ec3.client.gui.GuiMagicianTable;
import ec3.client.gui.GuiMagmaticSmeltery;
import ec3.client.gui.GuiMainMenuEC3;
import ec3.client.gui.GuiMatrixAbsorber;
import ec3.client.gui.GuiMithrilineFurnace;
import ec3.client.gui.GuiMonsterHarvester;
import ec3.client.gui.GuiMonsterHolder;
import ec3.client.gui.GuiMoonWell;
import ec3.client.gui.GuiNewMIM;
import ec3.client.gui.GuiNewMIMScreen;
import ec3.client.gui.GuiPlayerPentacle;
import ec3.client.gui.GuiPotionSpreader;
import ec3.client.gui.GuiRadiatingChamber;
import ec3.client.gui.GuiRayTower;
import ec3.client.gui.GuiResearchBook;
import ec3.client.gui.GuiRightClicker;
import ec3.client.gui.GuiSunRayAbsorber;
import ec3.client.gui.GuiUltraFlowerBurner;
import ec3.client.gui.GuiUltraHeatGenerator;
import ec3.client.gui.GuiWeaponBench;
import ec3.client.model.ModelArmorEC3;
import ec3.client.render.ClientRenderHandler;
import ec3.client.render.RenderCloudsFirstWorld;
import ec3.client.render.RenderHandlerEC3;
import ec3.client.render.RenderSkyFirstWorld;
import ec3.client.render.entity.RenderDemon;
import ec3.client.render.entity.RenderDivider;
import ec3.client.render.entity.RenderHologram;
import ec3.client.render.entity.RenderMRUArrow;
import ec3.client.render.entity.RenderMRUPresence;
import ec3.client.render.entity.RenderMRURay;
import ec3.client.render.entity.RenderOrbitalStrike;
import ec3.client.render.entity.RenderPlayerClone;
import ec3.client.render.entity.RenderPoisonFume;
import ec3.client.render.entity.RenderSolarBeam;
import ec3.client.render.entity.RenderWindMage;
import ec3.client.render.entity.ThrowableRenderFactory;
import ec3.client.render.tile.RenderChargingChamber;
import ec3.client.render.tile.RenderColdDistillator;
import ec3.client.render.tile.RenderCorruptionCleaner;
import ec3.client.render.tile.RenderCrystalController;
import ec3.client.render.tile.RenderCrystalExtractor;
import ec3.client.render.tile.RenderCrystalFormer;
import ec3.client.render.tile.RenderDarknessObelisk;
import ec3.client.render.tile.RenderDemonicPentacle;
import ec3.client.render.tile.RenderElementalCrystal;
import ec3.client.render.tile.RenderEnderGenerator;
import ec3.client.render.tile.RenderMRUCoil;
import ec3.client.render.tile.RenderMRUCoilHardener;
import ec3.client.render.tile.RenderMRULink;
import ec3.client.render.tile.RenderMRUReactor;
import ec3.client.render.tile.RenderMagicalChest;
import ec3.client.render.tile.RenderMagicalDisplay;
import ec3.client.render.tile.RenderMagicalEnchanter;
import ec3.client.render.tile.RenderMagicalJukebox;
import ec3.client.render.tile.RenderMagicalMirror;
import ec3.client.render.tile.RenderMagicalQuarry;
import ec3.client.render.tile.RenderMagicalRepairer;
import ec3.client.render.tile.RenderMagicianTable;
import ec3.client.render.tile.RenderMatrixAbsorber;
import ec3.client.render.tile.RenderMithrilineCrystal;
import ec3.client.render.tile.RenderMonsterHarvester;
import ec3.client.render.tile.RenderMonsterHolder;
import ec3.client.render.tile.RenderNewMIM;
import ec3.client.render.tile.RenderPlayerPentacle;
import ec3.client.render.tile.RenderPotionSpreader;
import ec3.client.render.tile.RenderRadiatingChamber;
import ec3.client.render.tile.RenderRayTower;
import ec3.client.render.tile.RenderWindRune;
import ec3.common.entity.EntityArmorDestroyer;
import ec3.common.entity.EntityDemon;
import ec3.common.entity.EntityDivider;
import ec3.common.entity.EntityDividerProjectile;
import ec3.common.entity.EntityHologram;
import ec3.common.entity.EntityMRUArrow;
import ec3.common.entity.EntityMRUPresence;
import ec3.common.entity.EntityMRURay;
import ec3.common.entity.EntityOrbitalStrike;
import ec3.common.entity.EntityPlayerClone;
import ec3.common.entity.EntityPoisonFume;
import ec3.common.entity.EntityShadowKnife;
import ec3.common.entity.EntitySolarBeam;
import ec3.common.entity.EntityWindMage;
import ec3.common.inventory.ContainerChargingChamber;
import ec3.common.inventory.ContainerColdDistillator;
import ec3.common.inventory.ContainerCorruptionCleaner;
import ec3.common.inventory.ContainerCrafter;
import ec3.common.inventory.ContainerCraftingFrame;
import ec3.common.inventory.ContainerCrystalController;
import ec3.common.inventory.ContainerCrystalExtractor;
import ec3.common.inventory.ContainerCrystalFormer;
import ec3.common.inventory.ContainerDarknessObelisk;
import ec3.common.inventory.ContainerDemon;
import ec3.common.inventory.ContainerEnderGenerator;
import ec3.common.inventory.ContainerFilter;
import ec3.common.inventory.ContainerFlowerBurner;
import ec3.common.inventory.ContainerFurnaceMagic;
import ec3.common.inventory.ContainerHeatGenerator;
import ec3.common.inventory.ContainerMRUAcceptor;
import ec3.common.inventory.ContainerMRUChunkLoader;
import ec3.common.inventory.ContainerMRUCoil;
import ec3.common.inventory.ContainerMRUDimTransciever;
import ec3.common.inventory.ContainerMRUInfo;
import ec3.common.inventory.ContainerMagicalEnchanter;
import ec3.common.inventory.ContainerMagicalFurnace;
import ec3.common.inventory.ContainerMagicalHopper;
import ec3.common.inventory.ContainerMagicalJukebox;
import ec3.common.inventory.ContainerMagicalQuarry;
import ec3.common.inventory.ContainerMagicalRepairer;
import ec3.common.inventory.ContainerMagicalTeleporter;
import ec3.common.inventory.ContainerMagicianTable;
import ec3.common.inventory.ContainerMagmaticSmeltery;
import ec3.common.inventory.ContainerMatrixAbsorber;
import ec3.common.inventory.ContainerMithrilineFurnace;
import ec3.common.inventory.ContainerMonsterHarvester;
import ec3.common.inventory.ContainerMonsterHolder;
import ec3.common.inventory.ContainerMoonWell;
import ec3.common.inventory.ContainerNewMIM;
import ec3.common.inventory.ContainerNewMIMSimpleNode;
import ec3.common.inventory.ContainerPotionSpreader;
import ec3.common.inventory.ContainerRadiatingChamber;
import ec3.common.inventory.ContainerRayTower;
import ec3.common.inventory.ContainerRedstoneTransmitter;
import ec3.common.inventory.ContainerRightClicker;
import ec3.common.inventory.ContainerSunRayAbsorber;
import ec3.common.inventory.ContainerUltraFlowerBurner;
import ec3.common.inventory.ContainerUltraHeatGenerator;
import ec3.common.inventory.ContainerWeaponBench;
import ec3.common.inventory.InventoryCraftingFrame;
import ec3.common.inventory.InventoryMagicFilter;
import ec3.common.item.ItemSecret;
import ec3.common.item.ItemsCore;
import ec3.common.tile.TileAdvancedBlockBreaker;
import ec3.common.tile.TileAnimalSeparator;
import ec3.common.tile.TileChargingChamber;
import ec3.common.tile.TileColdDistillator;
import ec3.common.tile.TileCorruptionCleaner;
import ec3.common.tile.TileCrafter;
import ec3.common.tile.TileCrystalController;
import ec3.common.tile.TileCrystalExtractor;
import ec3.common.tile.TileCrystalFormer;
import ec3.common.tile.TileDarknessObelisk;
import ec3.common.tile.TileDemonicPentacle;
import ec3.common.tile.TileElementalCrystal;
import ec3.common.tile.TileEnderGenerator;
import ec3.common.tile.TileFlowerBurner;
import ec3.common.tile.TileFurnaceMagic;
import ec3.common.tile.TileHeatGenerator;
import ec3.common.tile.TileMRUChunkLoader;
import ec3.common.tile.TileMRUCoil;
import ec3.common.tile.TileMRUCoil_Hardener;
import ec3.common.tile.TileMRUDimensionalTransciever;
import ec3.common.tile.TileMRUReactor;
import ec3.common.tile.TileMagicalChest;
import ec3.common.tile.TileMagicalDisplay;
import ec3.common.tile.TileMagicalEnchanter;
import ec3.common.tile.TileMagicalFurnace;
import ec3.common.tile.TileMagicalHopper;
import ec3.common.tile.TileMagicalJukebox;
import ec3.common.tile.TileMagicalMirror;
import ec3.common.tile.TileMagicalQuarry;
import ec3.common.tile.TileMagicalRepairer;
import ec3.common.tile.TileMagicalTeleporter;
import ec3.common.tile.TileMagicianTable;
import ec3.common.tile.TileMagmaticSmelter;
import ec3.common.tile.TileMatrixAbsorber;
import ec3.common.tile.TileMithrilineCrystal;
import ec3.common.tile.TileMithrilineFurnace;
import ec3.common.tile.TileMonsterHarvester;
import ec3.common.tile.TileMonsterHolder;
import ec3.common.tile.TileMoonWell;
import ec3.common.tile.TileNewMIM;
import ec3.common.tile.TileNewMIMCraftingManager;
import ec3.common.tile.TileNewMIMExportNode;
import ec3.common.tile.TileNewMIMImportNode;
import ec3.common.tile.TileNewMIMInventoryStorage;
import ec3.common.tile.TileNewMIMScreen;
import ec3.common.tile.TilePlayerPentacle;
import ec3.common.tile.TilePotionSpreader;
import ec3.common.tile.TileRadiatingChamber;
import ec3.common.tile.TileRayTower;
import ec3.common.tile.TileRedstoneTransmitter;
import ec3.common.tile.TileRightClicker;
import ec3.common.tile.TileSunRayAbsorber;
import ec3.common.tile.TileUltraFlowerBurner;
import ec3.common.tile.TileUltraHeatGenerator;
import ec3.common.tile.TileWeaponMaker;
import ec3.common.tile.TileWindRune;
import ec3.common.tile.TileecAcceptor;
import ec3.common.tile.TileecStateChecker;
import ec3.utils.cfg.Config;
import ec3.utils.common.ECEventHandler;
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
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy {

	public static final List<Pair<String, ISound>> playingMusic = new ArrayList<Pair<String, ISound>>();

	public void firstMovement(FMLPreInitializationEvent event) {
		super.firstMovement(event);
		OBJLoader.INSTANCE.addDomain("essentialcraft");
		//ModelLoaderRegistry.registerLoader(ModelGun_LoaderBased.LoaderGun.INSTANCE);
	}

	public boolean listHasKey(String key)
	{
		for(int i = 0; i < playingMusic.size(); ++i)
		{
			if(playingMusic.get(i).getLeft().equals(key))
				return true;
		}

		return false;
	}

	public int positionOf(String key)
	{
		for(int i = 0; i < playingMusic.size(); ++i)
		{
			if(playingMusic.get(i).getLeft().equals(key))
				return i;
		}

		return 0;
	}

	@Override
	public void stopSound(String soundID)
	{
		if(listHasKey(soundID))
		{
			Minecraft.getMinecraft().getSoundHandler().stopSound(playingMusic.get(positionOf(soundID)).getRight());
			playingMusic.remove(soundID);
		}
	}

	@Override
	public void startSound(String soundID, String soundName)
	{	
		if(!listHasKey(soundID))
		{
			PositionedSoundRecord s = PositionedSoundRecord.getMusicRecord(SoundEvent.REGISTRY.getObject(new ResourceLocation(soundName)));
			playingMusic.add(Pair.<String, ISound>of(soundID, s));

			Minecraft.getMinecraft().getSoundHandler().playSound(s);
		}
	}

	@Override
	public void startRecord(String soundID, String soundName, BlockPos pos)
	{	
		if(!listHasKey(soundID))
		{
			PositionedSoundRecord s = PositionedSoundRecord.getRecordSoundRecord(SoundEvent.REGISTRY.getObject(new ResourceLocation(soundName)), pos.getX()+0.5F, pos.getY()+0.5F, pos.getZ()+0.5F);
			playingMusic.add(Pair.<String, ISound>of(soundID, s));

			Minecraft.getMinecraft().getSoundHandler().playSound(s);
		}
	}

	ResourceLocation villagerSkin = new ResourceLocation("essentialcraft","textures/entities/magician.png");
	@SuppressWarnings("unchecked")
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,int x, int y, int z) 
	{
		if(ID == Config.guiID[0])
		{
			TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
			if(tile == null)
			{
				//Item:filter
				if(x == 0 && y == -1 && z == 0)
				{
					InventoryMagicFilter inventory = new InventoryMagicFilter(player.getHeldItemMainhand());
					return new GuiFilter(new ContainerFilter(player, inventory), inventory);
				}
				//Item: Crafting Frame
				if(x == 0 && y == -2 && z == 0)
				{
					InventoryCraftingFrame inventory = new InventoryCraftingFrame(player.getHeldItemMainhand());
					return new GuiCraftingFrame(new ContainerCraftingFrame(player, inventory), inventory);
				}
			}
			if(tile instanceof TileRayTower)
			{
				return new GuiRayTower(new ContainerRayTower(player.inventory, tile), tile);
			}
			if(tile instanceof TileecAcceptor)
			{
				return new GuiMRUAcceptor(new ContainerMRUAcceptor(player.inventory,tile), tile);
			}
			if(tile instanceof TileecStateChecker)
			{
				return new GuiMRUInfo(new ContainerMRUInfo(player.inventory,tile), tile);
			}
			if(tile instanceof TileMoonWell)
			{
				return new GuiMoonWell(new ContainerMoonWell(player.inventory, tile),tile);
			}
			if(tile instanceof TileSunRayAbsorber)
			{
				return new GuiSunRayAbsorber(new ContainerSunRayAbsorber(player.inventory, tile),tile);
			}
			if(tile instanceof TileColdDistillator)
			{
				return new GuiColdDistillator(new ContainerColdDistillator(player.inventory, tile),tile);
			}
			if(tile instanceof TileFlowerBurner)
			{
				return new GuiFlowerBurner(new ContainerFlowerBurner(player.inventory, tile),tile);
			}
			if(tile instanceof TileHeatGenerator)
			{
				return new GuiHeatGenerator(new ContainerHeatGenerator(player.inventory, tile),tile);
			}
			if(tile instanceof TileEnderGenerator)
			{
				return new GuiEnderGenerator(new ContainerEnderGenerator(player.inventory, tile),tile);
			}
			if(tile instanceof TileMagicianTable)
			{
				return new GuiMagicianTable(new ContainerMagicianTable(player.inventory, tile),tile);
			}
			if(tile instanceof TileMagicalQuarry)
			{
				return new GuiMagicalQuarry(new ContainerMagicalQuarry(player.inventory, tile),tile);
			}
			if(tile instanceof TileMonsterHolder)
			{
				return new GuiMonsterHolder(new ContainerMonsterHolder(player.inventory, tile),tile);
			}
			if(tile instanceof TilePotionSpreader)
			{
				return new GuiPotionSpreader(new ContainerPotionSpreader(player.inventory, tile),tile);
			}
			if(tile instanceof TileMagicalEnchanter)
			{
				return new GuiMagicalEnchanter(new ContainerMagicalEnchanter(player.inventory, tile),tile);
			}
			if(tile instanceof TileMonsterHarvester)
			{
				return new GuiMonsterHarvester(new ContainerMonsterHarvester(player.inventory, tile),tile);
			}
			if(tile instanceof TileMagicalRepairer)
			{
				return new GuiMagicalRepairer(new ContainerMagicalRepairer(player.inventory, tile),tile);
			}
			if(tile instanceof TileMatrixAbsorber)
			{
				return new GuiMatrixAbsorber(new ContainerMatrixAbsorber(player.inventory, tile),tile);
			}
			if(tile instanceof TileRadiatingChamber)
			{
				return new GuiRadiatingChamber(new ContainerRadiatingChamber(player.inventory, tile),tile);
			}
			if(tile instanceof TileMagmaticSmelter)
			{
				return new GuiMagmaticSmeltery(new ContainerMagmaticSmeltery(player.inventory, tile),tile);
			}
			if(tile instanceof TileMagicalJukebox)
			{
				return new GuiMagicalJukebox(new ContainerMagicalJukebox(player.inventory, tile),tile);
			}
			if(tile instanceof TileCrystalFormer)
			{
				return new GuiCrystalFormer(new ContainerCrystalFormer(player.inventory, tile),tile);
			}
			if(tile instanceof TileCrystalController)
			{
				return new GuiCrystalController(new ContainerCrystalController(player.inventory, tile),tile);
			}
			if(tile instanceof TileCrystalExtractor)
			{
				return new GuiCrystalExtractor(new ContainerCrystalExtractor(player.inventory, tile),tile);
			}
			if(tile instanceof TileChargingChamber)
			{
				return new GuiChargingChamber(new ContainerChargingChamber(player.inventory, tile),tile);
			}
			if(tile instanceof TileMagicalTeleporter)
			{
				return new GuiMagicalTeleporter(new ContainerMagicalTeleporter(player.inventory, tile),tile);
			}
			if(tile instanceof TileMagicalFurnace)
			{
				return new GuiMagicalFurnace(new ContainerMagicalFurnace(player.inventory, tile),tile);
			}
			if(tile instanceof TileMRUCoil)
			{
				return new GuiMRUCoil(new ContainerMRUCoil(player.inventory, tile),tile);
			}
			if(tile instanceof TileCorruptionCleaner)
			{
				return new GuiCorruptionCleaner(new ContainerCorruptionCleaner(player.inventory, tile),tile);
			}
			if(tile instanceof TileDarknessObelisk)
			{
				return new GuiDarknessObelisk(new ContainerDarknessObelisk(player.inventory, tile),tile);
			}
			if(tile instanceof TileUltraHeatGenerator)
			{
				return new GuiUltraHeatGenerator(new ContainerUltraHeatGenerator(player.inventory, tile),tile);
			}
			if(tile instanceof TileUltraFlowerBurner)
			{
				return new GuiUltraFlowerBurner(new ContainerUltraFlowerBurner(player.inventory, tile),tile);
			}
			if(tile instanceof TileMithrilineFurnace)
			{
				return new GuiMithrilineFurnace(new ContainerMithrilineFurnace(player.inventory, tile),tile);
			}
			if(tile instanceof TileRightClicker)
			{
				return new GuiRightClicker(new ContainerRightClicker(player.inventory, tile),tile);
			}
			if(tile instanceof TileRedstoneTransmitter)
			{
				return new GuiCommon(new ContainerRedstoneTransmitter(player.inventory, tile),tile);
			}
			if(tile instanceof TileMagicalHopper)
			{
				return new GuiCommon(new ContainerMagicalHopper(player.inventory, tile),tile);
			}
			if(tile instanceof TileWeaponMaker)
			{
				return new GuiWeaponBench(new ContainerWeaponBench(player.inventory, tile),tile);
			}
			if(tile instanceof TileFurnaceMagic)
			{
				return new GuiFurnaceMagic(new ContainerFurnaceMagic(player.inventory, tile),tile);
			}
			if(tile instanceof TilePlayerPentacle)
			{
				return new GuiPlayerPentacle(tile);
			}
			if(tile instanceof TileMagicalChest)
			{
				return new GuiMagicalChest(player.inventory, (TileMagicalChest) tile);
			}
			if(tile instanceof TileNewMIMInventoryStorage)
			{
				return new GuiMIMInventoryStorage(player.inventory, (TileNewMIMInventoryStorage) tile);
			}
			if(tile instanceof TileNewMIM)
			{
				return new GuiNewMIM(new ContainerNewMIM(player.inventory, tile),tile);
			}
			if(tile instanceof TileNewMIMScreen)
			{
				return new GuiNewMIMScreen((TileNewMIMScreen) tile, player);
			}
			if(tile instanceof TileNewMIMCraftingManager)
			{
				return new GuiMIMCraftingManager(player.inventory, (TileNewMIMCraftingManager) tile);
			}
			if(tile instanceof TileNewMIMExportNode || tile instanceof TileNewMIMImportNode || tile instanceof TileAdvancedBlockBreaker)
			{
				return new GuiCommon(new ContainerNewMIMSimpleNode(player.inventory, tile),tile);
			}
			if(tile instanceof TileCrafter)
			{
				return new GuiCrafter(new ContainerCrafter(player.inventory, (TileCrafter) tile),(TileCrafter) tile);
			}
			if(tile instanceof TileAnimalSeparator)
			{
				return new GuiRayTower(new ContainerRayTower(player.inventory, tile),tile);
			}
			if(tile instanceof TileMRUChunkLoader)
			{
				return new GuiMRUChunkLoader(new ContainerMRUChunkLoader(player.inventory, tile), tile);
			}
			if(tile instanceof TileMRUDimensionalTransciever)
			{
				return new GuiMRUDimTransciever(new ContainerMRUDimTransciever(player.inventory, tile), tile);
			}
		}
		if(ID == Config.guiID[1])
		{
			List<EntityDemon> demons = world.getEntitiesWithinAABB(EntityDemon.class, new AxisAlignedBB(x-1, y-1, z-1, x+1, y+1, z+1));
			if(!demons.isEmpty())
			{
				return new GuiDemon(new ContainerDemon(player, demons.get(0)));
			}
		}
		return null;
	}

	@Override
	public void openBookGUIForPlayer()
	{
		Minecraft.getMinecraft().displayGuiScreen(new GuiResearchBook());
	}

	@Override
	public void openPentacleGUIForPlayer(TileEntity tile)
	{
		Minecraft.getMinecraft().displayGuiScreen(new GuiPlayerPentacle(tile));
	}

	@Override
	public void registerRenderInformation() {
		MainMenuRegistry.registerNewGui(GuiMainMenuEC3.class, "[EC] Magical Menu", "For EssentialCraft Fans ;)");
		RenderingRegistry.registerEntityRenderingHandler(EntityMRUPresence.class, new RenderMRUPresence.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityMRUArrow.class, new RenderMRUArrow.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntitySolarBeam.class, new RenderSolarBeam.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityWindMage.class, new RenderWindMage.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityPoisonFume.class, new RenderPoisonFume.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityShadowKnife.class, new ThrowableRenderFactory(ItemsCore.shadeKnife));
		RenderingRegistry.registerEntityRenderingHandler(EntityArmorDestroyer.class, new ThrowableRenderFactory(ItemsCore.magicalSlag));
		RenderingRegistry.registerEntityRenderingHandler(EntityDividerProjectile.class, new ThrowableRenderFactory(ItemsCore.magicalSlag));
		RenderingRegistry.registerEntityRenderingHandler(EntityMRURay.class, new RenderMRURay.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityDemon.class, new RenderDemon.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityHologram.class, new RenderHologram.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityPlayerClone.class, new RenderPlayerClone.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityOrbitalStrike.class, new RenderOrbitalStrike.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityDivider.class, new RenderDivider.Factory());
		MinecraftForge.EVENT_BUS.register(new ClientRenderHandler());
		MinecraftForge.EVENT_BUS.register(new RenderHandlerEC3());

		kbArmorBoost = new KeyBinding("ComputerArmorBoost", Keyboard.KEY_Z, "key.categories.gameplay");
		ClientRegistry.registerKeyBinding(kbArmorBoost);
		kbArmorVision = new KeyBinding("ComputerArmorNightVision", Keyboard.KEY_X, "key.categories.gameplay");
		ClientRegistry.registerKeyBinding(kbArmorVision);
	}

	@Override
	public void registerTileEntitySpecialRenderer() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileRayTower.class, new RenderRayTower());
		ClientRegistry.bindTileEntitySpecialRenderer(TileecAcceptor.class, new RenderMRULink());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMagicianTable.class, new RenderMagicianTable());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMagicalQuarry.class, new RenderMagicalQuarry());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMonsterHolder.class, new RenderMonsterHolder());
		ClientRegistry.bindTileEntitySpecialRenderer(TilePotionSpreader.class, new RenderPotionSpreader());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMagicalEnchanter.class, new RenderMagicalEnchanter());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMonsterHarvester.class, new RenderMonsterHarvester());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMagicalRepairer.class, new RenderMagicalRepairer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMatrixAbsorber.class, new RenderMatrixAbsorber());
		ClientRegistry.bindTileEntitySpecialRenderer(TileRadiatingChamber.class, new RenderRadiatingChamber());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMagicalJukebox.class, new RenderMagicalJukebox());
		ClientRegistry.bindTileEntitySpecialRenderer(TileElementalCrystal.class, new RenderElementalCrystal());
		ClientRegistry.bindTileEntitySpecialRenderer(TileCrystalFormer.class, new RenderCrystalFormer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileChargingChamber.class, new RenderChargingChamber());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMRUCoil_Hardener.class, new RenderMRUCoilHardener());
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
		ClientRegistry.bindTileEntitySpecialRenderer(TileNewMIM.class, new RenderNewMIM());
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
		if(str.contains("consSpellParticle")) {
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
				Minecraft.getMinecraft().effectRenderer.addEffect(new MRUFX(Minecraft.getMinecraft().world, x, y, z, i, j, k));
			if(name.equals("cSpellFX"))
				Minecraft.getMinecraft().effectRenderer.addEffect(new CSpellFX(Minecraft.getMinecraft().world, x, y, z, i, j, k));
			if(name.equals("fogFX"))
				Minecraft.getMinecraft().effectRenderer.addEffect(new FogFX(Minecraft.getMinecraft().world, x, y, z, i, j, k));
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
					return (wrld.getWorldTime() % 24000 >= 14000 && wrld.getWorldTime() % 24000 <= 16000) && (player.rotationPitch <= -42 && player.rotationPitch >= -65) && (playerLst.size() == 1) && (!wrld.isRaining() && (biome.getTempCategory() == TempCategory.WARM || biome.getTempCategory() == TempCategory.MEDIUM));
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
				Minecraft.getMinecraft().effectRenderer.addEffect(new ItemFX(Minecraft.getMinecraft().world, ds[0], ds[1], ds[2], 1, 0, 1, ds[3], ds[4], ds[5]));
		}
	}

	@Override
	public void FlameFX(double... ds) {
		if(true && doParticle()) {
			if(ds.length == 10)
				Minecraft.getMinecraft().effectRenderer.addEffect(
						new ColoredFlameFX(Minecraft.getMinecraft().world, ds[0], ds[1], ds[2], ds[3], ds[4], ds[5], ds[6], ds[7], ds[8], ds[9]));
		}
	}

	@Override
	public void SmokeFX(double... ds) {
		if(true && doParticle()) {
			if(ds.length == 7)
				Minecraft.getMinecraft().effectRenderer.addEffect(new SmokeFX(Minecraft.getMinecraft().world, ds[0], ds[1], ds[2], ds[3], ds[4], ds[5], (float)ds[6]));
			if(ds.length == 10)
				Minecraft.getMinecraft().effectRenderer.addEffect(new SmokeFX(Minecraft.getMinecraft().world, ds[0], ds[1], ds[2], ds[3], ds[4], ds[5], (float)ds[6], ds[7], ds[8], ds[9]));
		}
	}

	@Override
	public void MRUFX(double... ds) {
		if(true && doParticle()) {
			if(ds.length == 6)
				Minecraft.getMinecraft().effectRenderer.addEffect(new MRUFX(Minecraft.getMinecraft().world, ds[0], ds[1], ds[2], ds[3], ds[4], ds[5]));
			if(ds.length == 9)
				Minecraft.getMinecraft().effectRenderer.addEffect(new MRUFX(Minecraft.getMinecraft().world, ds[0], ds[1], ds[2], ds[3], ds[4], ds[5], ds[6], ds[7], ds[8]));
		}
	}

	@Override
	public void wingsAction(EntityPlayer e, ItemStack s)
	{
		if(GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump) && Minecraft.getMinecraft().inGameHasFocus)
		{
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
	public void handlePositionChangePacket(DummyData[] packetData)
	{
		double sX = Double.parseDouble(packetData[1].fieldValue);
		double sY = Double.parseDouble(packetData[2].fieldValue);
		double sZ = Double.parseDouble(packetData[3].fieldValue);
		float yaw = Float.parseFloat(packetData[4].fieldValue);
		float pitch = Float.parseFloat(packetData[5].fieldValue);
		EntityPlayer player = Minecraft.getMinecraft().player;
		player.setPositionAndRotation(sX, sY, sZ,yaw,pitch);
		player.rotationYawHead = player.rotationYaw;
	}

	public void handleSoundPlay(DummyData[] packetData)
	{
		double sX = Double.parseDouble(packetData[1].fieldValue);
		double sY = Double.parseDouble(packetData[2].fieldValue);
		double sZ = Double.parseDouble(packetData[3].fieldValue);
		float volume = Float.parseFloat(packetData[4].fieldValue);
		float pitch = Float.parseFloat(packetData[5].fieldValue);
		String sound = packetData[6].fieldValue;
		EntityPlayer player = Minecraft.getMinecraft().player;
		player.getEntityWorld().playSound(sX, sY, sZ, SoundEvent.REGISTRY.getObject(new ResourceLocation(sound)), SoundCategory.MASTER, volume, pitch, false);
	}

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

	@SideOnly(Side.CLIENT)
	private static IRenderHandler skyedRenderer = new RenderSkyFirstWorld();

	@SideOnly(Side.CLIENT)
	private static IRenderHandler cloudedRenderer = new RenderCloudsFirstWorld();
	public static TextureAtlasSprite fogIcon;

	private static final ModelArmorEC3 chest = new ModelArmorEC3(1.0F);
	private static final ModelArmorEC3 chest1 = new ModelArmorEC3(0.75F);
	private static final ModelArmorEC3 legs = new ModelArmorEC3(0.5F);

	public static KeyBinding kbArmorBoost;
	public static KeyBinding kbArmorVision;
}
