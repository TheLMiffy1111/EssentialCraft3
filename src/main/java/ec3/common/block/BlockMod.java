package ec3.common.block;

import DummyCore.Client.IModelRegisterer;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.client.model.ModelLoader;

public class BlockMod extends Block implements IModelRegisterer {

	public String textureName = "";

	public BlockMod(Material p_i45394_1_) {
		super(p_i45394_1_);
	}

	public BlockMod setBlockTextureName(String name) {
		textureName = name;
		return this;
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	public Block setSoundType(SoundType s) {
		return super.setSoundType(s);
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:" + getRegistryName().getResourcePath(), "inventory"));
	}
}
