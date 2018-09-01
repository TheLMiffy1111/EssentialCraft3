package essentialcraft.common.block;

import essentialcraft.common.tile.TileMIMExportNodePersistant;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockMIMExporterPersistant extends BlockMIMExporter {

	public BlockMIMExporterPersistant() {
		super();
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileMIMExportNodePersistant();
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:mimejectorp", "inventory"));
	}
}
