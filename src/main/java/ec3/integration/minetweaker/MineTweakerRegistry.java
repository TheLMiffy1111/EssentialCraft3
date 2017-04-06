package ec3.integration.minetweaker;

import org.apache.logging.log4j.LogManager;

import net.minecraftforge.fml.common.Loader;

public class MineTweakerRegistry {
	public static void register() {
		if(Loader.isModLoaded("MineTweaker3")) {
			try {
				minetweaker.MineTweakerAPI.registerClass(DemonTrading.class);
				minetweaker.MineTweakerAPI.registerClass(MagicianTable.class);
				minetweaker.MineTweakerAPI.registerClass(MithrilineFurnace.class);
				minetweaker.MineTweakerAPI.registerClass(RadiatingChamber.class);
				minetweaker.MineTweakerAPI.registerClass(WindImbue.class);
				LogManager.getLogger().trace("Successfully registered MineTweaker integration!");
			}
			catch(Exception e) {
				LogManager.getLogger().error("Unable to add MineTweaker Integration.");
			}
		}
	}
}
