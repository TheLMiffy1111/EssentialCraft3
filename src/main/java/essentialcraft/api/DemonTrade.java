package essentialcraft.api;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class DemonTrade {

	public static final List<DemonTrade> TRADES = Lists.newArrayList();
	public static final Set<EntityEntry> ALL_MOBS = Sets.newHashSet();
	public ItemStack desiredItem = ItemStack.EMPTY;
	public EntityEntry entityType;

	public DemonTrade(ItemStack is) {
		desiredItem = is;
		TRADES.add(this);
	}

	public DemonTrade(EntityEntry e) {
		entityType = e;
		ALL_MOBS.add(e);
		TRADES.add(this);
	}

	public DemonTrade(ResourceLocation e) {
		this(ForgeRegistries.ENTITIES.getValue(e));
	}

	public static void removeTrade(DemonTrade tra) {
		if(tra.entityType != null) {
			ALL_MOBS.remove(tra.entityType);
		}
		TRADES.remove(tra);
	}

	public static void removeTrade(ItemStack is) {
		DemonTrade toRemove = null;
		for(DemonTrade tra : TRADES) {
			if(ItemStack.areItemStacksEqual(tra.desiredItem, is)) {
				toRemove = tra;
				break;
			}
		}
		removeTrade(toRemove);
	}

	public static void removeTrade(EntityEntry e) {
		DemonTrade toRemove = null;
		for(DemonTrade tra : TRADES) {
			if(tra.entityType != null && tra.entityType.equals(e)) {
				toRemove = tra;
				break;
			}
		}
		removeTrade(toRemove);
	}

	public static void removeTrade(ResourceLocation e) {
		removeTrade(ForgeRegistries.ENTITIES.getValue(e));
	}
}
