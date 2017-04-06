package ec3.common.registry;

import DummyCore.Utils.DummyPortalHandler;
import ec3.common.block.BlocksCore;
import ec3.common.world.dim.PortalGeneratorFirstWorld;
import ec3.common.world.dim.WorldProviderFirstWorld;
import ec3.utils.cfg.Config;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class DimensionRegistry {
	public static DimensionType firstWorld;
	public static void registerDimensionMagic() {
		firstWorld = DimensionType.register("First World", "_firstWorld", Config.dimensionID, WorldProviderFirstWorld.class, false);
		DimensionManager.registerDimension(Config.dimensionID,firstWorld);
		DummyPortalHandler.registerPortal(BlocksCore.portal, Config.dimensionID, 0, 200, false, PortalGeneratorFirstWorld.INSTANCE, new ResourceLocation("essentialcraft:blocks/portal"), 0xFFFFFF);
	}
}
