package ec3.common.registry;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import ec3.api.WorldEventLibrary;
import ec3.common.block.BlocksCore;
import ec3.common.item.ItemsCore;
import ec3.common.mod.EssentialCraftCore;
import ec3.common.world.event.WorldEvent_Darkness;
import ec3.common.world.event.WorldEvent_Earthquake;
import ec3.common.world.event.WorldEvent_Fumes;
import ec3.common.world.event.WorldEvent_SunArray;
import ec3.network.EC3PacketDispatcher;
import ec3.network.PacketNBT;
import ec3.proxy.ClientProxy;
import ec3.proxy.CommonProxy;
import ec3.utils.cfg.Config;
import ec3.utils.common.ECEventHandler;
import ec3.utils.common.PlayerTickHandler;
import ec3.utils.common.PlayerTracker;

public class CoreRegistry {
	
	public static CoreRegistry instance;
	
	public static void register() {
		Config.instance = new Config();
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
		EssentialCraftCore.network.registerMessage(EC3PacketDispatcher.class, PacketNBT.class, 0, Side.SERVER);
		EssentialCraftCore.network.registerMessage(EC3PacketDispatcher.class, PacketNBT.class, 0, Side.CLIENT);
		MinecraftForge.EVENT_BUS.register(new PlayerTickHandler());
		MinecraftForge.EVENT_BUS.register(new ECEventHandler());
		MinecraftForge.TERRAIN_GEN_BUS.register(new ECEventHandler());
		MinecraftForge.EVENT_BUS.register(new PlayerTracker());
		
		WorldEventLibrary.registerWorldEvent(new WorldEvent_SunArray());
		WorldEventLibrary.registerWorldEvent(new WorldEvent_Fumes());
		WorldEventLibrary.registerWorldEvent(new WorldEvent_Darkness());
		WorldEventLibrary.registerWorldEvent(new WorldEvent_Earthquake());
	}

}
