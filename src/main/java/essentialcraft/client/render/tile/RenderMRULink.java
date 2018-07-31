package essentialcraft.client.render.tile;

import essentialcraft.common.tile.TileMRUCUECAcceptor;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMRULink extends TileEntitySpecialRenderer<TileMRUCUECAcceptor>
{
	private static final ResourceLocation enderDragonCrystalBeamTextures = new ResourceLocation("textures/entity/endercrystal/endercrystal_beam.png");

	public void doRender(TileMRUCUECAcceptor tile, double x, double y, double z, float partialTicks)
	{
	}

	@Override
	public void render(TileMRUCUECAcceptor tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(tile.getBlockMetadata() == 0)
			this.doRender(tile, x, y, z, partialTicks);
	}
}