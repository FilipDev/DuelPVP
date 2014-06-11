/*
 * If this plugin is released, it means that whoever bought this plugin gave permission to distribute it. You can use this plugin use the code in this plugin to help you and other fellow plugin devs.
 */

package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.enums.Message;
import org.thespherret.plugins.duelpvp.enums.Error;
import org.thespherret.plugins.duelpvp.managers.CommandManager;

public class CreateCommand implements Command {
	@Override
	public boolean execute(CommandManager cm, CommandSender sender, String[] args) {
		Player p = (Player) sender;

		if (p.hasPermission("DuelPvP.admin") && args.length == 2){
			String arena;
			String position;
			try
			{
				arena = args[0];
				position = args[1];
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
			int pos;
			if ((pos = Integer.parseInt(position)) <= 2)
			{
				if (arena.equalsIgnoreCase("lobby"))
				{
					cm.getMain().getConfig().set("lobby..world", p.getLocation().getWorld().getName());
					cm.getMain().getConfig().set("lobby." + pos + ".x", Integer.valueOf(p.getLocation().getBlockX()));
					cm.getMain().getConfig().set("lobby." + pos + ".y", Integer.valueOf(p.getLocation().getBlockY()));
					cm.getMain().getConfig().set("lobby." + pos + ".z", Integer.valueOf(p.getLocation().getBlockZ()));
					p.sendMessage(Message.SET_LOBBY_POS.getF(position));
				}
				else
				{
					p.sendMessage(Message.SET_ARENA_POS.getF(position, arena));
					cm.getMain().getAM().createArenaPoint(arena, Integer.valueOf(pos), p.getLocation());
				}
			}
			else
				p.sendMessage(Error.TOO_MANY_SPAWN_POINTS.get());
		}else
			p.sendMessage(Error.NO_COMMAND_PERMISSION.get());
		return true;
	}
}
