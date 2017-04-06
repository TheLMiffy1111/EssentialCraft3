package ec3.common.item;

import DummyCore.Client.IModelRegisterer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;

public class ItemMagicalChisel extends Item implements IModelRegisterer {

	public ItemStack getContainerItem(ItemStack itemStack)
	{
		if(!hasContainerItem(itemStack))
		{
			return null;
		}
		return new ItemStack(itemStack.getItem(),itemStack.stackSize,itemStack.getItemDamage()+1);
	}

	public boolean hasContainerItem(ItemStack stack)
	{
		return stack.getItemDamage() < stack.getMaxDamage();
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/magicalChisel", "inventory"));
	}
}
