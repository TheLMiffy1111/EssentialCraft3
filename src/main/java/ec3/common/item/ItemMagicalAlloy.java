package ec3.common.item;

import java.util.List;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Client.ModelUtils;
import ec3.api.OreSmeltingRecipe;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.oredict.OreDictionary;

public class ItemMagicalAlloy extends Item implements IItemColor, IModelRegisterer {

	public ItemMagicalAlloy() {
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List<ItemStack> p_150895_3_) {
		for(int i = 0; i < OreSmeltingRecipe.values().length; ++i) {
			p_150895_3_.add(new ItemStack(p_150895_1_, 1, i));
		}
	}

	public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List<String> p_77624_3_, boolean p_77624_4_) {
		if(p_77624_1_.getItemDamage() >= OreSmeltingRecipe.values().length)
			return;
		OreSmeltingRecipe ore = OreSmeltingRecipe.values()[p_77624_1_.getItemDamage()];
		List<ItemStack> oreLst = OreDictionary.getOres(ore.oreName);
		if(oreLst != null && !oreLst.isEmpty())
			p_77624_3_.add(oreLst.get(0).getDisplayName());
		else	
			p_77624_3_.add(I18n.translateToLocal("tile."+OreSmeltingRecipe.values()[p_77624_1_.getItemDamage()].oreName+".name"));
	}

	public int getColorFromItemstack(ItemStack p_82790_1_, int p_82790_2_) {
		if(p_82790_2_ == 0 || p_82790_1_.getItemDamage() >= OreSmeltingRecipe.values().length)
			return 16777215;
		return OreSmeltingRecipe.values()[p_82790_1_.getItemDamage()].color;
	}

	@Override
	public void registerModels() {
		ModelUtils.setItemModelSingleIcon(this, "essentialcraft:item/magicalAlloy");
	}
}
