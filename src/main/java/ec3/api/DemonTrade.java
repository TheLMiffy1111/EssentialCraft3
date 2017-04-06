package ec3.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;

public class DemonTrade
{
	public static final List<DemonTrade> trades = new ArrayList<DemonTrade>();
	public static final List<Class<? extends Entity>> allMobs = new ArrayList<Class<? extends Entity>>();
	public ItemStack desiredItem;
	public Class<? extends Entity> entityType;
	
	public DemonTrade(ItemStack is) {
		desiredItem = is;
		trades.add(this);
	}
	
	public DemonTrade(Class<? extends Entity> e) {
		entityType = e;
		allMobs.add(e);
		trades.add(this);
	}
	
	public DemonTrade(String e) {
		this(EntityList.NAME_TO_CLASS.get(e));
	}
	
	public static void removeTrade(DemonTrade tra) {
		if(tra.entityType != null)
			allMobs.remove(tra.entityType);
		trades.remove(tra);
	}
	
	public static void removeTrade(ItemStack is) {
		for(DemonTrade tra : trades) {
			if(tra.desiredItem.isItemEqual(is)) {
				removeTrade(tra);
				return;
			}
		}
	}
	
	public static void removeTrade(Class<? extends Entity> e) {
		for(DemonTrade tra : trades) {
			if(tra.entityType != null && tra.entityType.equals(e)) {
				removeTrade(tra);
				return;
			}
		}
	}
	
	public static void removeTrade(String e) {
		removeTrade(EntityList.NAME_TO_CLASS.get(e));
	}
}
