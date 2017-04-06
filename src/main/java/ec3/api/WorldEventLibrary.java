package ec3.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.World;

public class WorldEventLibrary {
	
	public static final List<IWorldEvent> registeredEventList = new ArrayList<IWorldEvent>();
	
	public static IWorldEvent currentEvent = null;
	
	public static int currentEventDuration = -1;
	
	public static void registerWorldEvent(IWorldEvent event) {
		registeredEventList.add(event);
	}
	
	public static IWorldEvent selectRandomEffect(World w) {
		for(IWorldEvent event : registeredEventList) {
			if(w.rand.nextFloat() <= event.getEventProbability(w) && event.possibleToApply(w) && event.getEventDuration(w) > 0) {
				return event;
			}
		}
		return null;
	}
	
	public static IWorldEvent gettEffectByID(String id) {
		for(IWorldEvent event : registeredEventList) {
			if(event.getEventID().equals(id)) {
				return event;
			}
		}
		return null;
	}

	public static String[] getAllIDs() {
		String[] ret = new String[registeredEventList.size()];
		for(int i = 0; i < ret.length; i++) {
			ret[i] = registeredEventList.get(i).getEventID();
		}
		return ret;
	}
}
