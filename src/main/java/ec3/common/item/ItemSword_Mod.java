package ec3.common.item;

import DummyCore.Client.IModelRegisterer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemSword;
import net.minecraftforge.client.model.ModelLoader;

public class ItemSword_Mod extends ItemSword implements IModelRegisterer {
	
	public String textureName = "";
	
	public ItemSword_Mod(ToolMaterial p_i45347_1_) {
		super(p_i45347_1_);
	}
	
	public ItemSword_Mod setTextureName(String name) {
		textureName = name;
		return this;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/" + getRegistryName().getResourcePath(), "inventory"));
	}
}
