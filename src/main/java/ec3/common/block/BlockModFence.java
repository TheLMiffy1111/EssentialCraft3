package ec3.common.block;

import DummyCore.Client.IModelRegisterer;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class BlockModFence extends BlockFence implements IModelRegisterer {

	public String textureName;

	public BlockModFence(Material p_i45394_1_, String texture)
	{
		super(p_i45394_1_, p_i45394_1_.getMaterialMapColor());
		textureName = texture;
	}

	public BlockModFence(Material p_i45394_1_) {
		super(p_i45394_1_, p_i45394_1_.getMaterialMapColor());
		textureName = "essentialcraft:fortifiedStone";
	}

	public BlockModFence() {
		super(Material.ROCK, Material.ROCK.getMaterialMapColor());
		textureName = "essentialcraft:fortifiedStone";
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:" + getRegistryName().getResourcePath(), "inventory"));
	}
}
