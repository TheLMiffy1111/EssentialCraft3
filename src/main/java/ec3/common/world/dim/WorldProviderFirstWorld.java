package ec3.common.world.dim;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ec3.common.mod.EssentialCraftCore;
import ec3.common.registry.DimensionRegistry;
import ec3.utils.cfg.Config;
import ec3.utils.common.ECUtils;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.client.IRenderHandler;

public class WorldProviderFirstWorld extends WorldProvider {

	@Override
	public DimensionType getDimensionType() {
		return DimensionRegistry.firstWorld;
	}

	protected void createBiomeProvider()
	{
		this.biomeProvider = new WorldChunkManager_FirstWorld(this.world.getWorldInfo());
		this.doesWaterVaporize = false;
		this.hasNoSky = false;
		this.setDimension(Config.dimensionID);
	}

	public void generateLightBrightnessTable()
	{
		float f = 0.0F;

		for (int i = 0; i <= 15; ++i)
		{
			float f1;
			if(!ECUtils.isEventActive("ec3.event.darkness"))
				f1 = 1.0F - (float)i / 15.0F;
			else
				f1 = 1.9F - (float)i / 15.0F;
			this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
		}
	}

	@SideOnly(Side.CLIENT)
	public IRenderHandler getSkyRenderer()
	{
		return (IRenderHandler)EssentialCraftCore.proxy.getRenderer(0);
	}

	@SideOnly(Side.CLIENT)
	public IRenderHandler getCloudRenderer()
	{
		return (IRenderHandler)EssentialCraftCore.proxy.getRenderer(1);
	}

	public boolean isSurfaceWorld()
	{
		return true;
	}

	public IChunkGenerator createChunkGenerator()
	{
		return new ChunkProviderFirstWorld(this.world, this.world.getSeed(), true, this.world.getWorldInfo().getGeneratorOptions());
	}
}
