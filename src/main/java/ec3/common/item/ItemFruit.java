package ec3.common.item;

import DummyCore.Client.IModelRegisterer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemFood;
import net.minecraftforge.client.model.ModelLoader;

public class ItemFruit extends ItemFood implements IModelRegisterer {

	public ItemFruit(int amount, float saturation, boolean isWolfFood) {
		super(amount, saturation, isWolfFood);
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/fruit_Item", "inventory"));
	}
}
