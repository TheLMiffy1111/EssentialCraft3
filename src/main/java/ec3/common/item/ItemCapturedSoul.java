package ec3.common.item;

import java.util.List;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Client.ModelUtils;
import ec3.api.DemonTrade;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

public class ItemCapturedSoul extends Item implements IModelRegisterer {

	public ItemCapturedSoul() {
		setHasSubtypes(true);
		setMaxDamage(0);
	}

	public void getSubItems(Item itm, CreativeTabs tab, List<ItemStack> lst) {
		for(int i = 0; i < DemonTrade.allMobs.size(); ++i) {
			lst.add(new ItemStack(itm,1,i));
		}
	}

	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean par4) {
		super.addInformation(stack, player, list, par4);
		Class<? extends Entity> e = stack.getItemDamage() < DemonTrade.allMobs.size() ? DemonTrade.allMobs.get(stack.getItemDamage()) : null;
		String s = "entity." + EntityList.CLASS_TO_NAME.get(e) + ".name";
		list.add(I18n.translateToLocal(s));
	}

	@Override
	public void registerModels() {
		ModelUtils.setItemModelSingleIcon(this, "essentialcraft:item/soul");
	}
}
