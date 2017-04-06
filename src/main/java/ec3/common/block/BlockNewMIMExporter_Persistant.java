package ec3.common.block;

import ec3.common.tile.TileNewMIMExportNode_Persistant;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockNewMIMExporter_Persistant extends BlockNewMIMExporter {

	public BlockNewMIMExporter_Persistant() {
		super();
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int metadata) {
		return new TileNewMIMExportNode_Persistant();
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:mimEjectorP", "inventory"));
	}
}
