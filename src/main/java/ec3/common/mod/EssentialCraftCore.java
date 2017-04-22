package ec3.common.mod;


import java.util.Arrays;

import net.minecraft.command.CommandHandler;
import net.minecraft.server.MinecraftServer;
import DummyCore.Core.Core;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ec3.common.block.BlocksCore;
import ec3.common.entity.EntitiesCore;
import ec3.common.item.ItemsCore;
import ec3.common.registry.AchievementRegistry;
import ec3.common.registry.BiomeRegistry;
import ec3.common.registry.CERegistry;
import ec3.common.registry.CoreRegistry;
import ec3.common.registry.DimensionRegistry;
import ec3.common.registry.EnchantRegistry;
import ec3.common.registry.GunInitialization;
import ec3.common.registry.PotionRegistry;
import ec3.common.registry.RecipeRegistry;
import ec3.common.registry.ResearchRegistry;
import ec3.common.registry.SoundRegistry;
import ec3.common.registry.StructureRegistry;
import ec3.common.registry.TileRegistry;
import ec3.common.registry.VillagersRegistry;
import ec3.common.world.WorldGenManager;
import ec3.integration.bloodmagic.BloodMagicRegistry;
import ec3.integration.minetweaker.MineTweakerRegistry;
import ec3.integration.rotarycraft.RCLoadingHandler;
import ec3.integration.tconstruct.TConstructRegistry;
import ec3.integration.versionChecker.Check;
import ec3.integration.waila.WailaInitializer;
import ec3.proxy.CommonProxy;
import ec3.utils.cfg.Config;
import ec3.utils.commands.CommandClearCorruptionEffects;
import ec3.utils.commands.CommandCreateMRUCU;
import ec3.utils.commands.CommandHoannaEvent;
import ec3.utils.commands.CommandSetBalanceInMRUCU;
import ec3.utils.commands.CommandSetMRUInMRUCU;
import ec3.utils.commands.CommandSetUBMRU;
import ec3.utils.common.ECEventHandler;
import ec3.utils.common.ECUtils;

@Mod(
		modid = EssentialCraftCore.modid,
		name = "EssentialCraftIVUnofficial",
		version = EssentialCraftCore.version,
		dependencies = "required-after:dummycore@[2.3,);required-after:Baubles",
		guiFactory = "ec3.client.gui.ModConfigGuiHandler"
		)
public class EssentialCraftCore {

	//============================================CORE VARS==================================================//
	@Instance(EssentialCraftCore.modid)
	public static EssentialCraftCore core;
	@SidedProxy(clientSide = "ec3.proxy.ClientProxy", serverSide = "ec3.proxy.CommonProxy", modId = EssentialCraftCore.modid)
	public static CommonProxy proxy;
	public static Config cfg = new Config();
	public static final String version = "4.7.1102.6";
	public static final String modid = "essentialcraft";
	public static ModMetadata metadata;
	public static SimpleNetworkWrapper network;
	public static final boolean Unofficial = true; 
	//============================================CORE FUNCTIONS=============================================//
	//============================================CORE MOD===================================================//
	@EventHandler
	public void serverStart(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandSetMRUInMRUCU());
		event.registerServerCommand(new CommandSetBalanceInMRUCU());
		event.registerServerCommand(new CommandCreateMRUCU());
		event.registerServerCommand(new CommandSetUBMRU());
		event.registerServerCommand(new CommandClearCorruptionEffects());
		event.registerServerCommand(new CommandHoannaEvent());
	}

	@EventHandler
	public void firstMovement(FMLPreInitializationEvent event) {
		metadata = event.getModMetadata();

		core = this;
		try {
			Core.registerModAbsolute(getClass(), "EssentialCraft", event.getModConfigurationDirectory().getAbsolutePath(), cfg);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		proxy.firstMovement(event);

		ForgeChunkManager.setForcedChunkLoadingCallback(core, ECUtils.getTHObj());
		Check.checkerCommit();
		WailaInitializer.sendIMC();
		RCLoadingHandler.runPreInitChecks();

		CoreRegistry.register();
		SoundRegistry.init();

		BlocksCore.loadBlocks();
		ItemsCore.loadItems();
		BlocksCore.postInitLoad();
		TileRegistry.register();
		
		EntitiesCore.registerEntities();
		proxy.registerRenderInformation();
		
		AchievementRegistry.register();
		PotionRegistry.registerPotions();
		
		DimensionRegistry.registerDimensionMagic();
		BiomeRegistry.register();
		
		TConstructRegistry.register();
	}

	@EventHandler
	public void secondMovement(FMLInitializationEvent event) {
		GunInitialization.register();
		RecipeRegistry.main();

		EnchantRegistry.register();
		VillagersRegistry.register();

		StructureRegistry.register();
		proxy.registerTileEntitySpecialRenderer();

		CERegistry.register();
		MineTweakerRegistry.register();
		ECEventHandler.shouldLoadLoot = true;
	}

	public static boolean clazzExists(String clazzName) {
		try {
			Class<?> clazz = Class.forName(clazzName);
			return clazz != null;
		}
		catch(Exception e) {
			return false;
		}
	}

	@EventHandler
	public void thirdMovement(FMLPostInitializationEvent event) {
		BloodMagicRegistry.register();
		GameRegistry.registerWorldGenerator(new WorldGenManager(), 16);
		ResearchRegistry.init();

		metadata.autogenerated = false;
		metadata.modId = modid;
		metadata.version = version;
		metadata.name="Essential Craft 4 Unofficial";
		metadata.credits="Author: Modbder, TheLMiffy1111, mrAppleXZ";
		metadata.authorList = Arrays.<String>asList(new String[]{"Modbder", "TheLMiffy1111", "mrAppleXZ"});
		metadata.description = "Essential Craft 4 is a modified version of Essential Craft 3, which is a huge magic-themed mod, that adds lots of end-game content.";
		metadata.url = "http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2286105-1-7-10-forge-open-source-dummythinking-mods";
		metadata.logoFile = "assets/essentialcraft/textures/special/logo.png";
	}
}
