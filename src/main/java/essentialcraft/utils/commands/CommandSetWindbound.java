package essentialcraft.utils.commands;

import java.util.Collections;
import java.util.List;

import essentialcraft.utils.common.ECUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandSetWindbound extends CommandBase {

	@Override
	public String getName() {
		return "setwindbound";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/setwindbound <player> <true|false>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length < 1) {
			throw new WrongUsageException("Usage: /setwindbound <player> <true|false>");
		}
		boolean var3 = parseBoolean(args.length == 1 ? args[0] : args[1]);
		EntityPlayerMP player = args.length == 1 ? getCommandSenderAsPlayer(sender) : getPlayer(server, sender, args[0]);
		ECUtils.getData(player).modifyWindbound(var3);
		notifyCommandListener(sender, this, "Successfully set "+player.getName()+"'s Windbound to "+var3);
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.<String>emptyList();
	}

	@Override
	public boolean isUsernameIndex(String[] s, int par1) {
		return par1 == 0;
	}
}
