package ec3.common.block;

import DummyCore.Client.IModelRegisterer;
import ec3.common.tile.TileEmberForge;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockEmberForge extends BlockContainer implements IModelRegisterer {

	public BlockEmberForge(Material p_i45394_1_) {
		super(p_i45394_1_);
	}

	public BlockEmberForge() {
		super(Material.ROCK);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEmberForge();
	}

	public EnumBlockRenderType getRenderType(IBlockState s) {
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:emberForge"));
	}
}
