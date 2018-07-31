package essentialcraft.integration.bloodmagic;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;

import essentialcraft.common.item.ItemsCore;
import essentialcraft.common.mod.EssentialCraftCore;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

public class BloodMagicRegistry {

	public static void register() {
		if(Loader.isModLoaded("bloodmagic")) {
			if(EssentialCraftCore.clazzExists("WayofTime.bloodmagic.api.BloodMagicAPI")) {
				try {
					Class<?> classAltarRecipe = Class.forName("WayofTime.bloodmagic.api.registry.AltarRecipeRegistry$AltarRecipe");
					Class<? extends Enum> classEnumAltarTier = (Class<? extends Enum>)Class.forName("WayofTime.bloodmagic.api.altar.EnumAltarTier");
					Enum<? extends Enum> ONE = Enum.valueOf(classEnumAltarTier, "ONE");
					Constructor<?> constructorAltarRecipe = classAltarRecipe.getConstructor(ItemStack.class, ItemStack.class, classEnumAltarTier, Integer.TYPE, Integer.TYPE, Integer.TYPE);
					Class<?> classAltarRecipeRegistry = Class.forName("WayofTime.bloodmagic.api.registry.AltarRecipeRegistry");
					Method registerRecipe = classAltarRecipeRegistry.getMethod("registerRecipe", classAltarRecipe);

					ItemStack emptySoulStone = new ItemStack(ItemsCore.soulStone, 1, 0);
					ItemStack filledSoulStone = new ItemStack(ItemsCore.soulStone, 1, 1);

					registerRecipe.invoke(null, constructorAltarRecipe.newInstance(emptySoulStone, filledSoulStone, ONE, 250, 2 ,1));
					LogManager.getLogger().trace("Successfully registered Blood Magic integration!");
				}
				catch(Exception e) {
					LogManager.getLogger().error("Unable to add Blood Magic Integration.", e);
				}
			}
			if(EssentialCraftCore.clazzExists("WayofTime.bloodmagic.apibutnotreally.BloodMagicAPI")) {
				try {
					Class<?> classAltarRecipe = Class.forName("WayofTime.bloodmagic.apibutnotreally.registry.AltarRecipeRegistry$AltarRecipe");
					Class<? extends Enum> classEnumAltarTier = (Class<? extends Enum>)Class.forName("WayofTime.bloodmagic.apibutnotreally.altar.EnumAltarTier");
					Enum<? extends Enum> ONE = Enum.valueOf(classEnumAltarTier, "ONE");
					Constructor<?> constructorAltarRecipe = classAltarRecipe.getConstructor(ItemStack.class, ItemStack.class, classEnumAltarTier, Integer.TYPE, Integer.TYPE, Integer.TYPE);
					Class<?> classAltarRecipeRegistry = Class.forName("WayofTime.bloodmagic.apibutnotreally.registry.AltarRecipeRegistry");
					Method registerRecipe = classAltarRecipeRegistry.getMethod("registerRecipe", classAltarRecipe);

					ItemStack emptySoulStone = new ItemStack(ItemsCore.soulStone, 1, 0);
					ItemStack filledSoulStone = new ItemStack(ItemsCore.soulStone, 1, 1);

					registerRecipe.invoke(null, constructorAltarRecipe.newInstance(emptySoulStone, filledSoulStone, ONE, 250, 2 ,1));
					LogManager.getLogger().trace("Successfully registered Blood Magic integration!");
				}
				catch(Exception e) {
					LogManager.getLogger().error("Unable to add Blood Magic Integration.", e);
				}
			}
		}
	}
}
