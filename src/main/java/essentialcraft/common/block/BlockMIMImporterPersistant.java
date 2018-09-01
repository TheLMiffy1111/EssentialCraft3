package essentialcraft.common.block;

import essentialcraft.common.tile.TileMIMImportNodePersistant;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockMIMImporterPersistant extends BlockMIMImporter {

	public BlockMIMImporterPersistant()
	{
		super();
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileMIMImportNodePersistant();
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:miminjectorp", "inventory"));
	}
}
