package essentialcraft.common.item;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Client.ModelUtils;
import DummyCore.Utils.MiscUtils;
import essentialcraft.api.DemonTrade;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ItemCapturedSoul extends Item implements IModelRegisterer {

	public ItemCapturedSoul() {
		setHasSubtypes(true);
		setMaxDamage(0);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> lst) {
		if(this.isInCreativeTab(tab)) {
			for(EntityEntry e : DemonTrade.ALL_MOBS) {
				ItemStack stack = new ItemStack(this, 1, 0);
				MiscUtils.getStackTag(stack).setString("entity", e.getRegistryName().toString());
				lst.add(stack);
			}
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String s = "";
		if(stack.getTagCompound() != null) {
			NBTTagCompound tag = MiscUtils.getStackTag(stack);
			if(tag.hasKey("entity")) {
				EntityEntry e = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(tag.getString("entity")));
				if(e != null) {
					s = "entity." + e.getName() + ".name";
				}
			}
		}
		return super.getItemStackDisplayName(stack) + " - " + I18n.translateToLocal(s);
	}

	@Override
	public void registerModels() {
		ModelUtils.setItemModelSingleIcon(this, "essentialcraft:item/soul");
	}
}
