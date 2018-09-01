package essentialcraft.api;

import java.util.List;

import com.google.common.collect.Lists;

import DummyCore.Utils.IngredientUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class WindImbueRecipe {

	public ItemStack result = ItemStack.EMPTY;
	public Ingredient input = Ingredient.EMPTY;
	public int enderEnergy;

	public static final List<WindImbueRecipe> RECIPES = Lists.newArrayList();

	public static WindImbueRecipe getRecipeByInput(ItemStack input) {
		if(input.isEmpty()) {
			return null;
		}
		for(WindImbueRecipe rec : RECIPES) {
			if(rec.input.apply(input)) {
				return rec;
			}
		}
		return null;
	}

	public static WindImbueRecipe getRecipeByResult(ItemStack r) {
		if(r.isEmpty()) {
			return null;
		}
		for(WindImbueRecipe rec : RECIPES) {
			if(r.isItemEqual(rec.result)) {
				return rec;
			}
		}
		return null;
	}

	public WindImbueRecipe(Ingredient input, ItemStack result, int enderEnergy) {
		this.input = input;
		this.result = result;
		this.enderEnergy = enderEnergy;
		RECIPES.add(this);
	}

	public WindImbueRecipe(ItemStack input, ItemStack result, int enderEnergy) {
		this.input = Ingredient.fromStacks(input);
		this.result = result;
		this.enderEnergy = enderEnergy;
		RECIPES.add(this);
	}

	public WindImbueRecipe(Object input, ItemStack result, int enderEnergy) {
		this.input = IngredientUtils.getIngredient(input);
		this.result = result;
		this.enderEnergy = enderEnergy;
		RECIPES.add(this);
	}

	public static void removeRecipe(WindImbueRecipe rec) {
		RECIPES.remove(rec);
	}

	public static void removeRecipeByInput(ItemStack c) {
		removeRecipe(getRecipeByInput(c));
	}

	public static void removeRecipeByResult(ItemStack r) {
		removeRecipe(getRecipeByResult(r));
	}

	public static void removeRecipe(ItemStack s, ItemStack r) {
		WindImbueRecipe toRemove = null;
		for(WindImbueRecipe rec : RECIPES) {
			if(rec.input.apply(s) && rec.result.isItemEqual(r)) {
				toRemove = rec;
				break;
			}
		}
		removeRecipe(toRemove);
	}
}
