package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.Arena;
import org.thespherret.plugins.duelpvp.enums.Error;
import org.thespherret.plugins.duelpvp.managers.CommandManager;

public class EndCommand implements Command {

	@Override
	public boolean execute(CommandManager cm, Player p, String[] args)
	{
		Arena arena;
		if (((arena = cm.getMain().getAM().getArena(p)) != null) && (arena.hasStarted()))
			arena.toggleRequestEnd(p);
		else
			p.sendMessage(Error.NOT_IN_STARTED_ARENA.get());
		return true;
	}
}
