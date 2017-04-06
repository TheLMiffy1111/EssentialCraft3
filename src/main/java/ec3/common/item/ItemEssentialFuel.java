package ec3.common.item;

import java.util.List;
import java.util.Locale;

import DummyCore.Client.IModelRegisterer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;

public class ItemEssentialFuel extends Item implements IModelRegisterer {

	public String[] name = new String[] {"Fiery", "Watery", "Earthen", "Windy", "Unknown"};

	public ItemEssentialFuel() {
		super();
		setMaxDamage(0);
		maxStackSize = 16;
		bFull3D = false;
		setHasSubtypes(true);
	}

	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		for(int var4 = 0; var4 < 4; ++var4) {
			ItemStack min = new ItemStack(par1, 1, var4);
			par3List.add(min);
		}
	}

	public String getUnlocalizedName(ItemStack p_77667_1_) {
		return getUnlocalizedName()+name[Math.min(p_77667_1_.getItemDamage(), name.length-1)];
	}

	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.RARE;
	}

	@Override
	public void registerModels() {
		for(int i = 0; i < name.length-1; i++) {
			ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation("essentialcraft:item/elementalFuel", "type=" + name[i].toLowerCase(Locale.ENGLISH)));
		}
	}
}
