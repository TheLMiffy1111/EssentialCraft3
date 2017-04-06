package ec3.common.item;

import DummyCore.Client.IModelRegisterer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemAxe;
import net.minecraftforge.client.model.ModelLoader;

public class ItemAxe_Mod extends ItemAxe implements IModelRegisterer {

	public String textureName = "";

	public ItemAxe_Mod(ToolMaterial p_i45347_1_) {
		super(p_i45347_1_, p_i45347_1_.getDamageVsEntity(), p_i45347_1_.getEfficiencyOnProperMaterial());
	}

	public ItemAxe_Mod setTextureName(String name) {
		textureName = name;
		return this;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/" + getRegistryName().getResourcePath(), "inventory"));
	}
}
