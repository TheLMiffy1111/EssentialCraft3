package essentialcraft.common.item;

import java.util.List;
import java.util.Locale;

import DummyCore.Client.IModelRegisterer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemEssence extends Item implements IModelRegisterer {
	public static String[] dropNames = {"Fire", "Water", "Earth", "Air"};

	public ItemEssence() {
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return getUnlocalizedName()+dropNames[stack.getItemDamage()%4];
	}

	@Override
	public void getSubItems(CreativeTabs p_150895_2_, NonNullList<ItemStack> p_150895_3_) {
		if(this.isInCreativeTab(p_150895_2_))
			for(int var4 = 0; var4 < 16; ++var4) {
				ItemStack min = new ItemStack(this, 1, var4);
				p_150895_3_.add(min);
			}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World player, List<String> list, ITooltipFlag par4) {
		int t = stack.getItemDamage()/4;
		if(t == 0)
			list.add("Rarity: \247f"+"Common");
		if(t == 1)
			list.add("Rarity: \247e"+"Uncommon");
		if(t == 2)
			list.add("Rarity: \247b"+"Rare");
		if(t == 3)
			list.add("Rarity: \247d"+"Exceptional");
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		int t = stack.getItemDamage()/4;
		if(t == 1)
			return EnumRarity.UNCOMMON;
		if(t == 2)
			return EnumRarity.RARE;
		if(t == 3)
			return EnumRarity.EPIC;
		return EnumRarity.COMMON;
	}

	@Override
	public void registerModels() {
		for(int i = 0; i < 16; i++) {
			ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation("essentialcraft:item/essence", "type=" + dropNames[i%4].toLowerCase(Locale.ENGLISH)));
		}
	}
}
