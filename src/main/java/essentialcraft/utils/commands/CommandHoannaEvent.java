package essentialcraft.utils.commands;

import java.util.Collections;
import java.util.List;

import essentialcraft.api.IWorldEvent;
import essentialcraft.api.WorldEventRegistry;
import essentialcraft.utils.cfg.Config;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

public class CommandHoannaEvent extends CommandBase {

	@Override
	public String getName() {
		return "hoannaevent";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/hoannaevent <id|stop>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length > 0) {
			WorldServer world = server.getWorld(Config.dimensionID);
			IWorldEvent event = WorldEventRegistry.getEventByID(args[0]);
			if(event != null || args[0].equalsIgnoreCase("stop")) {
				if(WorldEventRegistry.currentEvent != null) {
					ECUtils.ec3WorldTag.setInteger("currentEventDuration", -1);
					WorldEventRegistry.currentEvent.onEventEnd(world);
					WorldEventRegistry.currentEvent = null;
					WorldEventRegistry.currentEventDuration = -1;
					ECUtils.ec3WorldTag.removeTag("currentEventDuration");
					ECUtils.ec3WorldTag.removeTag("currentEvent");
					ECUtils.requestCurrentEventSync();

					if(args[0].equalsIgnoreCase("stop")) {
						notifyCommandListener(sender, this, "Sucessfully stopped Hoanna event");
						return;
					}
				}
				event.onEventBeginning(world);
				ECUtils.ec3WorldTag.setString("currentEvent", event.getEventID());
				ECUtils.ec3WorldTag.setInteger("currentEventDuration", event.getEventDuration(world));
				ECUtils.requestCurrentEventSync();
				notifyCommandListener(sender, this, "Sucessfully set Hoanna event to "+args[0]);
			}
			else {
				throw new CommandException("Cannot find event with name "+args[0]);
			}
		}
		else {
			throw new WrongUsageException("Usage: /hoannaevent <id|stop>");
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		return args.length == 1 ? getListOfStringsMatchingLastWord(args, WorldEventRegistry.getAllIDs()) : Collections.<String>emptyList();
	}
}
