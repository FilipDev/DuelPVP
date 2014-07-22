package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.Bukkit;
import org.thespherret.plugins.duelpvp.Arena;
import org.thespherret.plugins.duelpvp.enums.Error;
import org.thespherret.plugins.duelpvp.enums.Message;

public class ForceStartCommand extends Command {

	public void execute()
	{
		if (p.hasPermission("DuelPVP.Force"))
		{
			Arena a;
			if ((a = cm.getMain().getAM().getArena(p)) != null)
			{
				if (!a.hasStarted())
				{
					a.gameStart();
					Bukkit.getScheduler().cancelTask(a.getScheduledTask());
					p.sendMessage(Message.FORCE_STARTED_MATCH.toString());
				}
				else
					p.sendMessage(Error.ARENA_NOT_STARTED.toString());
			}
			else
				p.sendMessage(Error.NOT_IN_ARENA.toString());
		}
	}
}
