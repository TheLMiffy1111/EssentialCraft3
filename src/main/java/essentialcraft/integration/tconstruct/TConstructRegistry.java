package essentialcraft.integration.tconstruct;

import org.apache.logging.log4j.LogManager;

import net.minecraftforge.fml.common.Loader;

public class TConstructRegistry {

	public static void register() {
		if(Loader.isModLoaded("tconstruct")) {
			try {
				ModMRUReinforced.register();
				LogManager.getLogger().trace("Successfully registered TConstruct integration!");
			}
			catch(Exception e) {
				LogManager.getLogger().error("Unable to add TConstruct Integration.", e);
			}
		}
	}
}
