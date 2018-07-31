package essentialcraft.common.registry;

import essentialcraft.api.WorldEventRegistry;
import essentialcraft.common.mod.EssentialCraftCore;
import essentialcraft.common.world.event.WorldEventDarkness;
import essentialcraft.common.world.event.WorldEventEarthquake;
import essentialcraft.common.world.event.WorldEventFumes;
import essentialcraft.common.world.event.WorldEventSunArray;
import essentialcraft.network.ECPacketDispatcher;
import essentialcraft.network.PacketNBT;
import essentialcraft.proxy.ClientProxy;
import essentialcraft.proxy.CommonProxy;
import essentialcraft.utils.common.ECEventHandler;
import essentialcraft.utils.common.PlayerTickHandler;
import essentialcraft.utils.common.PlayerTracker;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CoreRegistry {

	public static void register() {
		if(EssentialCraftCore.proxy != null)
			NetworkRegistry.INSTANCE.registerGuiHandler(EssentialCraftCore.core, EssentialCraftCore.proxy);
		else {
			Side s = FMLCommonHandler.instance().getEffectiveSide();
			if(s == Side.CLIENT)
				EssentialCraftCore.proxy = new ClientProxy();
			else
				EssentialCraftCore.proxy = new CommonProxy();

			NetworkRegistry.INSTANCE.registerGuiHandler(EssentialCraftCore.core, EssentialCraftCore.proxy);
		}
		EssentialCraftCore.network = NetworkRegistry.INSTANCE.newSimpleChannel("essentialcraft3");
		EssentialCraftCore.network.registerMessage(ECPacketDispatcher.class, PacketNBT.class, 0, Side.SERVER);
		EssentialCraftCore.network.registerMessage(ECPacketDispatcher.class, PacketNBT.class, 0, Side.CLIENT);
		MinecraftForge.EVENT_BUS.register(new PlayerTickHandler());
		MinecraftForge.EVENT_BUS.register(new ECEventHandler());
		MinecraftForge.TERRAIN_GEN_BUS.register(new ECEventHandler());
		MinecraftForge.EVENT_BUS.register(new PlayerTracker());

		WorldEventRegistry.registerWorldEvent(new WorldEventSunArray());
		WorldEventRegistry.registerWorldEvent(new WorldEventFumes());
		WorldEventRegistry.registerWorldEvent(new WorldEventDarkness());
		WorldEventRegistry.registerWorldEvent(new WorldEventEarthquake());
	}

}
