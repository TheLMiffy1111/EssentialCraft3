package ec3.common.item;

import DummyCore.Client.IModelRegisterer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ItemElementalFocus extends Item implements IModelRegisterer {

	public String textureName = "";

	public ItemElementalFocus() {
		super();
		setMaxDamage(3000);
		setMaxStackSize(1);
	}

	public ItemElementalFocus setTextureName(String name) {
		textureName = name;
		return this;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/" + getRegistryName().getResourcePath(), "inventory"));
	}
}
