package ec3.integration.waila;

import net.minecraftforge.fml.common.event.FMLInterModComms;

public class WailaInitializer {
	
	public static void sendIMC() {
		FMLInterModComms.sendMessage("Waila", "register", "ec3.integration.waila.WailaDataProvider.callbackRegister");	
	}
}
