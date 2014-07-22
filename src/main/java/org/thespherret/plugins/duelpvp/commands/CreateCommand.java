package org.thespherret.plugins.duelpvp.commands;

import org.thespherret.plugins.duelpvp.enums.Error;
import org.thespherret.plugins.duelpvp.enums.Message;

public class CreateCommand extends Command {

	public void execute()
	{
		if (cm.getMain().isPlayerAdmin(p))
		{
			if (args.length >= 1)
			{
				String arena = args[0];
				if (arena.equalsIgnoreCase("lobby"))
				{
					cm.getMain().getConfig().set("lobby.world", p.getLocation().getWorld().getName());
					cm.getMain().getConfig().set("lobby.x", p.getLocation().getX());
					cm.getMain().getConfig().set("lobby.y", p.getLocation().getY());
					cm.getMain().getConfig().set("lobby.z", p.getLocation().getZ());
					cm.getMain().getConfig().set("lobby.yaw", p.getLocation().getYaw());
					cm.getMain().getConfig().set("lobby.pitch", p.getLocation().getPitch());
					p.sendMessage(Message.SET_LOBBY_POS.toString());
				}
				else
				{
					if (args.length == 2)
					{
						int pos;
						if ((pos = Integer.parseInt(args[1])) <= 2)
						{
							p.sendMessage(Message.SET_ARENA_POS.getFormatted(pos, arena));
							cm.getMain().getAM().createArenaPoint(arena, pos, p.getLocation());
							if (cm.getMain().arenas.get("arenas." + arena + ".enabled") == null)
								cm.getMain().arenas.set("arenas." + arena + ".enabled", false);
						}
						else
							p.sendMessage(Error.TOO_MANY_SPAWN_POINTS.toString());
					}
					else
						p.sendMessage(Error.INCORRECT_USAGE.toString());
				}
			}
		}
		else
			p.sendMessage(Error.NO_COMMAND_PERMISSION.toString());
	}
}
