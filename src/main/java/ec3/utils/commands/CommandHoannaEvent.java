package ec3.utils.commands;

import java.util.Collections;
import java.util.List;

import ec3.api.IWorldEvent;
import ec3.api.WorldEventLibrary;
import ec3.utils.cfg.Config;
import ec3.utils.common.ECUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

public class CommandHoannaEvent extends CommandBase {

	@Override
	public String getName() {
		return "hoannaEvent";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/hoannaEvent <id|stop>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length > 0) {
			WorldServer world = server.worldServerForDimension(Config.dimensionID);
			IWorldEvent event = WorldEventLibrary.gettEffectByID(args[0]);
			if(event != null || args[0].equalsIgnoreCase("stop")) {
				if(WorldEventLibrary.currentEvent != null) {
					ECUtils.ec3WorldTag.setInteger("currentEventDuration", -1);
					WorldEventLibrary.currentEvent.onEventEnd(world);
					WorldEventLibrary.currentEvent = null;
					WorldEventLibrary.currentEventDuration = -1;
					ECUtils.ec3WorldTag.removeTag("currentEventDuration");
					ECUtils.ec3WorldTag.removeTag("currentEvent");
					ECUtils.requestCurrentEventSync();

					if(args[0].equalsIgnoreCase("stop")) {
						return;
					}
				}
				event.onEventBeginning(world);
				ECUtils.ec3WorldTag.setString("currentEvent", event.getEventID());
				ECUtils.ec3WorldTag.setInteger("currentEventDuration", event.getEventDuration(world));
				ECUtils.requestCurrentEventSync();
			}
			else {
				throw new CommandException("Cannot find event with name "+args[0]);
			}
		}
		else {
			throw new CommandException("Cannot find event");
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		return args.length == 1 ? getListOfStringsMatchingLastWord(args, WorldEventLibrary.getAllIDs()) : Collections.<String>emptyList();
	}
}
