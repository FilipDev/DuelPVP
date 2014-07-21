package org.thespherret.plugins.duelpvp.commands;

import org.thespherret.plugins.duelpvp.Arena;
import org.thespherret.plugins.duelpvp.enums.Error;

public class EndCommand extends Command {

	public void execute()
	{
		Arena arena;
		if (((arena = cm.getMain().getAM().getArena(p)) != null) && (arena.hasStarted()))
			arena.toggleRequestEnd(p);
		else
			p.sendMessage(Error.NOT_IN_STARTED_ARENA.toString());
	}
}
