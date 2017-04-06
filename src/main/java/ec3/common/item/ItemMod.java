package ec3.common.item;

import DummyCore.Client.IModelRegisterer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ItemMod extends Item implements IModelRegisterer {
	
	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/" + getRegistryName().getResourcePath(), "inventory"));
	}
}
