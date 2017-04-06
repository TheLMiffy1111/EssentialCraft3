package ec3.common.item;

import java.util.List;

import DummyCore.Client.IModelRegisterer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;

public class ItemDrop extends Item implements IModelRegisterer {
	public static String[] dropNames = new String[] {"fire", "water", "earth", "air", "elemental", "unknown"};

	public ItemDrop() {
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	public String getUnlocalizedName(ItemStack p_77667_1_) {
		return getUnlocalizedName()+dropNames[Math.min(p_77667_1_.getItemDamage(), dropNames.length-1)];
	}

	public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List<ItemStack> p_150895_3_) {
		for(int i = 0; i < 5; ++i) {
			p_150895_3_.add(new ItemStack(p_150895_1_, 1, i));
		}
	}

	@Override
	public void registerModels() {
		for(int i = 0; i < 5; i++)
			ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation("essentialcraft:item/drops", "type=" + dropNames[i]));
	}
}
