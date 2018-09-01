package essentialcraft.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.World;

public class WorldEventRegistry {

	public static final List<IWorldEvent> EVENTS = new ArrayList<IWorldEvent>();

	public static IWorldEvent currentEvent = null;

	public static int currentEventDuration = -1;

	public static void registerWorldEvent(IWorldEvent event) {
		EVENTS.add(event);
	}

	public static IWorldEvent selectRandomEvent(World w) {
		IWorldEvent event = EVENTS.get(w.rand.nextInt(EVENTS.size()));
		if(w.rand.nextFloat() <= event.getEventProbability(w) && event.possibleToApply(w) && event.getEventDuration(w) > 0) {
			return event;
		}
		return null;
	}

	public static IWorldEvent getEventByID(String id) {
		for(IWorldEvent event : EVENTS) {
			if(event.getEventID().equals(id)) {
				return event;
			}
		}
		return null;
	}

	public static String[] getAllIDs() {
		String[] ret = new String[EVENTS.size()];
		for(int i = 0; i < ret.length; i++) {
			ret[i] = EVENTS.get(i).getEventID();
		}
		return ret;
	}
}
