package ec3.utils.commands;

import ec3.utils.common.ECUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandClearCorruptionEffects extends CommandBase {

	@Override
	public String getName() {
		return "clearCorruptionEffects";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/clearCorruptionEffects <player>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		EntityPlayer p = args.length == 0 ? getCommandSenderAsPlayer(sender) : getPlayer(server, sender, args[0]);
		ECUtils.getData(p).getEffects().clear();
	}
}
