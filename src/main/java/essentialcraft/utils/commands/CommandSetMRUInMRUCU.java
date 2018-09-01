package essentialcraft.utils.commands;

import java.util.Collections;
import java.util.List;

import essentialcraft.api.IMRUHandler;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandSetMRUInMRUCU extends CommandBase {

	@Override
	public String getName() {
		return "setmruinmrucu";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/setmruinmrucu <x> <y> <z> <mruAmount>";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 3;
	}

	@Override
	public void execute(MinecraftServer s, ICommandSender sender, String[] args) throws CommandException {
		if(args.length < 4) {
			throw new WrongUsageException("Usage: /setmruinmrucu <x> <y> <z> <mruAmount>");
		}
		int var3 = parseInt(args[3], 0);
		BlockPos p = parseBlockPos(sender, args, 0, true);
		try {
			IMRUHandler mru = ECUtils.getClosestMRUCU(sender.getEntityWorld(), p, 16);
			mru.setMRU(var3);
			notifyCommandListener(sender, this, "Successfully set MRU in nearest MRUCU to "+var3);
		}
		catch(Exception e) {
			throw new CommandException("Could not find MRUCU", new Object[0]);
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		return args.length > 0 && args.length <= 3 ? getTabCompletionCoordinate(args, 0, pos) : Collections.<String>emptyList();
	}
}
