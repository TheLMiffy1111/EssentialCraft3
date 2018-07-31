package essentialcraft.common.item;

import java.util.Locale;

import DummyCore.Client.IModelRegisterer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;

public class ItemEssentialFuel extends Item implements IModelRegisterer {

	public String[] name = {"Fiery", "Watery", "Earthen", "Windy", "Unknown"};

	public ItemEssentialFuel() {
		super();
		setMaxDamage(0);
		maxStackSize = 16;
		bFull3D = false;
		setHasSubtypes(true);
	}

	@Override
	public void getSubItems(CreativeTabs par2CreativeTabs, NonNullList<ItemStack> list) {
		if(this.isInCreativeTab(par2CreativeTabs))
			for(int var4 = 0; var4 < 4; ++var4) {
				ItemStack min = new ItemStack(this, 1, var4);
				list.add(min);
			}
	}

	@Override
	public String getUnlocalizedName(ItemStack p_77667_1_) {
		return getUnlocalizedName()+name[Math.min(p_77667_1_.getItemDamage(), name.length-1)];
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.RARE;
	}

	@Override
	public void registerModels() {
		for(int i = 0; i < name.length-1; i++) {
			ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation("essentialcraft:item/elementalfuel", "type=" + name[i].toLowerCase(Locale.ENGLISH)));
		}
	}
}
