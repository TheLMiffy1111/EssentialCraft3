package essentialcraft.common.item;

import DummyCore.Client.IModelRegisterer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;

public class ItemEmber extends Item implements IModelRegisterer {
	public static String[] unlocalisedName = {"acidic", "chaos", "common", "corrupted", "crystal", "divine", "magical", "flame", "unknown"};

	public ItemEmber() {
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return getUnlocalizedName()+unlocalisedName[Math.min(stack.getItemDamage(), unlocalisedName.length-1)];
	}

	@Override
	public void getSubItems(CreativeTabs p_150895_2_, NonNullList<ItemStack> p_150895_3_) {
		if(this.isInCreativeTab(p_150895_2_))
			for(int i = 0; i < unlocalisedName.length-1; ++i) {
				p_150895_3_.add(new ItemStack(this, 1, i));
			}
	}

	@Override
	public void registerModels() {
		for(int i = 0; i < unlocalisedName.length-1; i++) {
			ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation("essentialcraft:item/ember", "type=" + unlocalisedName[i]));
		}
	}
}
