package ec3.common.item;

import java.util.List;

import DummyCore.Client.IModelRegisterer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;

public class ItemEmber extends Item implements IModelRegisterer {
	public static String[] unlocalisedName = new String[] {"acidic", "chaos", "common", "corrupted", "crystal", "divine", "magical", "flame", "unknown"};

	public ItemEmber() {
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	public String getUnlocalizedName(ItemStack p_77667_1_) {
		return getUnlocalizedName()+unlocalisedName[Math.min(p_77667_1_.getItemDamage(), unlocalisedName.length-1)];
	}

	public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List<ItemStack> p_150895_3_) {
		for(int i = 0; i < unlocalisedName.length-1; ++i) {
			p_150895_3_.add(new ItemStack(p_150895_1_, 1, i));
		}
	}

	@Override
	public void registerModels() {
		for(int i = 0; i < unlocalisedName.length-1; i++) {
			ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation("essentialcraft:item/ember", "type=" + unlocalisedName[i]));
		}
	}
}
