package essentialcraft.utils.common;

import java.util.ArrayList;

import essentialcraft.common.item.ItemGenericArmor;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry.Impl;

public class RecipeArmorDyes extends Impl<IRecipe> implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting invCrafting, World world) {
		ItemStack itemstack = ItemStack.EMPTY;
		ArrayList<ItemStack> arraylist = new ArrayList<ItemStack>();

		for(int i = 0; i < invCrafting.getSizeInventory(); ++i) {
			ItemStack itemstack1 = invCrafting.getStackInSlot(i);

			if(!itemstack1.isEmpty()) {
				if(itemstack1.getItem() instanceof ItemGenericArmor) {
					if(!itemstack.isEmpty()) {
						return false;
					}

					itemstack = itemstack1;
				}
				else {
					if(itemstack1.getItem() != Items.DYE) {
						return false;
					}

					arraylist.add(itemstack1);
				}
			}
		}

		return !itemstack.isEmpty() && !arraylist.isEmpty();
	}

	/**
	 * Returns an Item that is the result of this recipe
	 */
	@Override
	public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
		ItemStack itemstack = ItemStack.EMPTY;
		int[] aint = new int[3];
		int i = 0;
		int j = 0;
		ItemGenericArmor itemarmor = null;
		int k;
		int l;
		float f;
		float f1;
		int l1;

		for(k = 0; k < p_77572_1_.getSizeInventory(); ++k) {
			ItemStack itemstack1 = p_77572_1_.getStackInSlot(k);

			if(!itemstack1.isEmpty()) {
				if(itemstack1.getItem() instanceof ItemGenericArmor) {
					itemarmor = (ItemGenericArmor)itemstack1.getItem();

					if(!itemstack.isEmpty()) {
						return ItemStack.EMPTY;
					}

					itemstack = itemstack1.copy();
					itemstack.setCount(1);

					if(itemarmor.hasColor(itemstack1)) {
						l = itemarmor.getColor(itemstack);
						f = (l >> 16 & 255) / 255.0F;
						f1 = (l >> 8 & 255) / 255.0F;
						float f2 = (l & 255) / 255.0F;
						i = (int)(i + Math.max(f, Math.max(f1, f2)) * 255.0F);
						aint[0] = (int)(aint[0] + f * 255.0F);
						aint[1] = (int)(aint[1] + f1 * 255.0F);
						aint[2] = (int)(aint[2] + f2 * 255.0F);
						++j;
					}
				}
				else {
					if(itemstack1.getItem() != Items.DYE) {
						return ItemStack.EMPTY;
					}

					float[] afloat = EntitySheep.getDyeRgb(EnumDyeColor.byDyeDamage(itemstack1.getItemDamage()));
					int j1 = (int)(afloat[0] * 255.0F);
					int k1 = (int)(afloat[1] * 255.0F);
					l1 = (int)(afloat[2] * 255.0F);
					i += Math.max(j1, Math.max(k1, l1));
					aint[0] += j1;
					aint[1] += k1;
					aint[2] += l1;
					++j;
				}
			}
		}

		if(itemarmor == null) {
			return ItemStack.EMPTY;
		}
		else {
			k = aint[0] / j;
			int i1 = aint[1] / j;
			l = aint[2] / j;
			f = (float)i / (float)j;
			f1 = Math.max(k, Math.max(i1, l));
			k = (int)(k * f / f1);
			i1 = (int)(i1 * f / f1);
			l = (int)(l * f / f1);
			l1 = (k << 8) + i1;
			l1 = (l1 << 8) + l;
			itemarmor.setColor(itemstack, l1);
			return itemstack;
		}
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canFit(int width, int height){
		return width*height>=2;
	}
}
