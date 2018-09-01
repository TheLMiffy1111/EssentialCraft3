package essentialcraft.integration.crafttweaker;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import essentialcraft.api.DemonTrade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.essentialcraft.DemonTrade")
public class DemonTrading {

	public static List<IAction> addActions = Lists.newArrayList();
	public static List<IAction> removeActions = Lists.newArrayList();

	@ZenMethod
	public static void add(IItemStack input) {
		if(input == null) {
			CraftTweakerAPI.logError("Cannot turn "+input+" into a Demon Trade");
			return;
		}
		addActions.add(new ActionAddDemonTrade(CraftTweakerMC.getItemStack(input)));
	}

	@ZenMethod
	public static void add(String input) {
		if(input == null) {
			CraftTweakerAPI.logError("Cannot turn "+input+" into a Demon Trade");
			return;
		}
		addActions.add(new ActionAddEntityDemonTrade(input));
	}

	@ZenMethod
	public static void remove(IItemStack input) {
		if(input == null) {
			CraftTweakerAPI.logError("Input cannot be null");
			return;
		}
		removeActions.add(new ActionRemoveDemonTrade(input));
	}

	@ZenMethod
	public static void remove(String input) {
		if(input == null) {
			CraftTweakerAPI.logError("Input cannot be null");
			return;
		}
		removeActions.add(new ActionRemoveEntityDemonTrade(new ResourceLocation(input)));
	}

	private static class ActionAddDemonTrade implements IAction {
		DemonTrade rec;
		ItemStack input;

		public ActionAddDemonTrade(ItemStack input) {
			this.input = input;
		}

		@Override
		public void apply() {
			rec = new DemonTrade(input);
		}

		@Override
		public String describe() {
			return "Adding Demon Trade for "+input.getDisplayName();
		}
	}

	private static class ActionAddEntityDemonTrade implements IAction {
		DemonTrade rec;
		String input;

		public ActionAddEntityDemonTrade(String input) {
			this.input = input;
		}

		@Override
		public void apply() {
			boolean flag = true;
			for(EntityEntry e : DemonTrade.ALL_MOBS) {
				if(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(input)).equals(e)) {
					flag = false;
					break;
				}
			}
			if(flag)
				rec = new DemonTrade(new ResourceLocation(input));
			else
				CraftTweakerAPI.logWarning("Demon Trade already exists for "+rec);
		}

		@Override
		public String describe() {
			return "Adding Demon Trade for "+input;
		}
	}

	private static class ActionRemoveDemonTrade implements IAction {
		IItemStack input;

		public ActionRemoveDemonTrade(IItemStack input) {
			this.input = input;
		}

		@Override
		public void apply() {
			ArrayList<DemonTrade> toRemove = new ArrayList<DemonTrade>();
			DemonTrade.TRADES.stream().
			filter(entry->input.matches(CraftTweakerMC.getIItemStack(entry.desiredItem))).
			forEach(entry->toRemove.add(entry));

			if(toRemove.isEmpty())
				CraftTweakerAPI.logWarning("No recipe for "+input.toString());
			else {
				for(DemonTrade entry : toRemove) {
					DemonTrade.removeTrade(entry);
				}
			}
		}

		@Override
		public String describe() {
			return "Removing Demon Trades for"+input.toString();
		}
	}

	private static class ActionRemoveEntityDemonTrade implements IAction {
		ResourceLocation input;

		public ActionRemoveEntityDemonTrade(ResourceLocation input) {
			this.input = input;
		}

		@Override
		public void apply() {
			DemonTrade.removeTrade(input);
		}

		@Override
		public String describe() {
			return "Removing Demon Trade for "+input;
		}
	}
}
