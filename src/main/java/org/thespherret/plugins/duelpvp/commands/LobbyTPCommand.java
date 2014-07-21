package org.thespherret.plugins.duelpvp.commands;

import org.thespherret.plugins.duelpvp.enums.Error;

public class LobbyTPCommand extends Command {

	public void execute()
	{
		if (cm.getMain().isPlayerAdmin(p))
			cm.getMain().getAM().lobbyTeleport(p);
		else
			p.sendMessage(Error.NO_COMMAND_PERMISSION.toString());
	}
}
