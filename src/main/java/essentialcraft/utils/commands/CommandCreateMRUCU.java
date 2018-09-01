package essentialcraft.utils.commands;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import essentialcraft.common.entity.EntityMRUPresence;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandCreateMRUCU extends CommandBase
{
	@Override
	public String getName() {
		return "createmrucu";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/createmrucu <x> <y> <z> <mruAmount> <balance>";
	}

	/**
	 * Return the required permission level for this command.
	 */
	@Override
	public int getRequiredPermissionLevel() {
		return 3;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length < 5) {
			throw new WrongUsageException("Usage: /createmrucu <x> <y> <z> <mruAmount> <balance>");
		}
		int var3 = parseInt(args[3], 0);
		double var4 = parseDouble(args[4], 0, 2);
		BlockPos p = parseBlockPos(sender, args, 0, true);
		int x = p.getX();
		int y = p.getY();
		int z = p.getZ();
		EntityMRUPresence mru = new EntityMRUPresence(sender.getEntityWorld());
		mru.setPositionAndRotation(x, y, z, 0, 0);
		mru.mruStorage.setMRU(var3);
		mru.mruStorage.setBalance((float)var4);
		sender.getEntityWorld().spawnEntity(mru);
		notifyCommandListener(sender, this, "Sucessfully created MRUCU");
	}

	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
		return args.length > 0 && args.length <= 3 ? getTabCompletionCoordinate(args, 1, pos) : Collections.<String>emptyList();
	}
}
