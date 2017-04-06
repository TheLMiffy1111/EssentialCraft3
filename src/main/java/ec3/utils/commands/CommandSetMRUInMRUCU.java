package ec3.utils.commands;

import java.util.Collections;
import java.util.List;

import DummyCore.Utils.Coord3D;
import ec3.common.entity.EntityMRUPresence;
import ec3.utils.common.ECUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandSetMRUInMRUCU extends CommandBase 
{
	public String getName()
	{
		return "setMRUInMRUCU";
	}

	public String getUsage(ICommandSender par1ICommandSender)
	{
		return "/setMRUInMRUCU <x> <y> <z> <mruAmount>";
	}

	/**
	 * Return the required permission level for this command.
	 */
	public int getRequiredPermissionLevel()
	{
		return 3;
	}

	public void execute(MinecraftServer s, ICommandSender par1ICommandSender, String[] par2ArrayOfStr) throws CommandException {
		int var3 = parseInt(par2ArrayOfStr[3], 0);
		BlockPos p = parseBlockPos(par1ICommandSender, par2ArrayOfStr, 0, true);
		int x = p.getX();
		int y = p.getY();
		int z = p.getZ();
		try {
			EntityMRUPresence mru = (EntityMRUPresence)ECUtils.getClosestMRUCU(par1ICommandSender.getEntityWorld(), new Coord3D(x,y,z), 16);
			mru.setMRU(var3);
		}
		catch(Exception e) {
			throw new CommandException("Could not find MRUCU", new Object[0]);
		}
	}

	public List<String> getTabCompletions(MinecraftServer server, ICommandSender par1ICommandSender, String[] par2ArrayOfStr, BlockPos pos) {
		return par2ArrayOfStr.length > 0 && par2ArrayOfStr.length <= 3 ? getTabCompletionCoordinate(par2ArrayOfStr, 0, pos) : Collections.<String>emptyList();
	}
}
