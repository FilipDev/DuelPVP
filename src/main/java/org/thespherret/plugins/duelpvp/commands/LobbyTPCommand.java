package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.enums.Error;
import org.thespherret.plugins.duelpvp.managers.CommandManager;

public class LobbyTPCommand implements Command {

	@Override
	public boolean execute(CommandManager cm, Player p, String[] args)
	{
		if (cm.getMain().isPlayerAdmin(p))
			cm.getMain().getAM().lobbyTeleport(p);
		else
			p.sendMessage(Error.NO_COMMAND_PERMISSION.toString());
		return true;
	}
}
