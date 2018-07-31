package essentialcraft.utils.cfg;

import DummyCore.Utils.IDummyConfig;
import essentialcraft.api.OreSmeltingRecipe;
import essentialcraft.common.registry.TileRegistry;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.config.Configuration;

public class Config implements IDummyConfig {

	public static Config instance;

	public Config() {
		instance = this;
	}

	//GUIs
	public void loadGUIs() {
		guiID[0] = config.get("gui", "GenericGUIID", 7321).getInt();
		guiID[1] = config.get("gui", "DemonGUIID", 7322).getInt();
	}

	public void loadMisc() {
		enablePersonalityShatter = config.get("misc", "EnablePersonalityShatter", true).getBoolean();
		renderStructuresFromAbove = config.get("misc", "RenderStructuresFromAbove", true).getBoolean();
		dimensionID = config.get("misc", "HoannaID", 53).getInt();
		String[] cfgCustomOreParsing = config.get("misc", "CustomMagmaticAlloys", new String[0], "Allows to add custom ores to Magmatic Alloys, where this is an array list, where first part is the ore name in OreDictionary, int after : is the color, int after | is the amount of drops you get from the ore and String after ? is the OreDictionary name of the result.").getStringList();
		for(String s : cfgCustomOreParsing) {
			int index_0 = s.indexOf(":");
			int index_1 = s.indexOf("|");
			int index_2 = s.indexOf("?");
			if(index_0 == -1 || index_1 == -1 || index_2 == -1)
				continue;
			String oredOreName = s.substring(0, index_0);
			int oreColor = Integer.parseInt(s.substring(index_0+1, index_1));
			int oreOutput = Integer.parseInt(s.substring(index_1+1, index_2));
			String oredResultName = s.substring(index_2+1, s.length());

			OreSmeltingRecipe.addRecipe(oredOreName,oredResultName,oreColor,oreOutput);
		}
		oreGenAttempts = config.get("misc", "OreGenAttempts", 4, "The amount of tries to generate the elemental ore cluster in a chunk. Set to 0 to disable worldgen.").setMinValue(0).getInt();
		eMRUCUGenAttempts = config.get("misc", "ElderMRUCUGenAttempts", 1, "The amount of tries to generate the Elder MRUCU Structure in a chunk. Set to 0 to disable worldgen.").setMinValue(0).getInt();
		allowPaleItemsInOtherRecipes = config.get("misc", "AllowPaleItemsInOtherRecipes", true).getBoolean();
		allowHologramInOtherDimensions = config.get("mobs", "AllowHologramInOtherDimensions", false, "Is the hologram boss allowed to spawn in the overworld/nether/anything or only Hoanna").getBoolean();
	}

	public static int genericBlockIDS = 1200;
	public static int blocksCount = 0;
	public static int genericItemIDS = 13200;
	public static int itemsCount = 0;
	public static int dimensionID = 53;
	public static int[] guiID = new int[48];
	public static boolean isCorruptionAllowed;
	public static boolean renderMRUPresenceWithoutMonocle;
	public static boolean enableHardcoreCrafts;
	public static boolean renderAdvancedBlockFX;
	public static int magicianID;
	public static boolean enablePersonalityShatter = true;
	public static Configuration config;

	public static String[] data_addedOresNames;
	public static int[] data_addedOreColors;
	public static int[] data_addedOreAmount;
	public static int oreGenAttempts;
	public static int eMRUCUGenAttempts;

	public static boolean allowPaleItemsInOtherRecipes;

	public static boolean renderStructuresFromAbove;
	public static boolean allowHologramInOtherDimensions;

	@Override
	public void load(Configuration config) {
		Config.config = config;
		this.loadGUIs();
		this.loadMisc();
		this.loadTiles();
		config.save();
	}

	public void loadTiles() {
		for(Class<? extends TileEntity> tile : TileRegistry.CONFIG_DEPENDANT) {
			try {
				if(tile.getMethod("setupConfig", Configuration.class) != null) {
					tile.getMethod("setupConfig", Configuration.class).invoke(null, Config.config);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}
}
