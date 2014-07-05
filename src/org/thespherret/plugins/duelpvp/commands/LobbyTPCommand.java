package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.managers.CommandManager;
import org.thespherret.plugins.duelpvp.enums.Error;

public class LobbyTPCommand implements Command {

	@Override
	public boolean execute(CommandManager cm, Player p, String[] args)
	{
		if (p.hasPermission("DuelPVP.admin")){
			Location loc = new Location(Bukkit.getWorld(cm.getMain().getConfig().getString("lobby.world")), cm.getMain().getConfig().getDouble("lobby.x"), cm.getMain().getConfig().getDouble("lobby.y"), cm.getMain().getConfig().getDouble("lobby.z"), cm.getMain().getConfig().getInt("lobby.yaw"), cm.getMain().getConfig().getInt("lobby.pitch"));
			p.teleport(loc);
		}else
			p.sendMessage(Error.NO_COMMAND_PERMISSION.get());
		return true;
	}
}
