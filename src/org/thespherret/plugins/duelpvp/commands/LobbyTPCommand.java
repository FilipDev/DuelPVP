/*
 * If this plugin is released, it means that whoever bought this plugin gave permission to distribute it. You can use this plugin use the code in this plugin to help you and other fellow plugin devs.
 */

package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.managers.CommandManager;
import org.thespherret.plugins.duelpvp.enums.Error;

public class LobbyTPCommand implements Command {
	@Override
	public boolean execute(CommandManager cm, Player p, String[] args)
	{
		if (p.hasPermission("DuelPVP.admin")){
			Location loc = new Location(Bukkit.getWorld(cm.getMain().getConfig().getString("lobby.world")), cm.getMain().getConfig().getInt("lobby.x"), cm.getMain().getConfig().getInt("lobby.y"), cm.getMain().getConfig().getInt("lobby.z"));
			p.teleport(loc);
		}else
			p.sendMessage(Error.NO_COMMAND_PERMISSION.get());
		return true;
	}
}
