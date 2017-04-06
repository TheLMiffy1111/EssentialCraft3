package ec3.common.item;

import DummyCore.Client.IModelRegisterer;
import ec3.api.IItemAllowsSeeingMRUCU;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ItemMonocle extends Item implements IItemAllowsSeeingMRUCU, IModelRegisterer {

	public ItemMonocle() {
		super();
		this.maxStackSize = 1;
		this.setMaxDamage(16);
	}

	@Override
	public boolean isFull3D()
	{
		return true;
	}

	@Override
	public int getItemEnchantability()
	{
		return 10;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/magicMonocle", "inventory"));
	}
}
