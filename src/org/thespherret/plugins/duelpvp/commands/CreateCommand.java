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
	public boolean execute(CommandManager cm, Player p, String[] args)
	{
		if (p.hasPermission("DuelPvP.admin")){
			if (args.length < 1)
				return false;
			String arena = args[0];
			if (arena.equalsIgnoreCase("lobby"))
			{
				cm.getMain().getConfig().set("lobby.world", p.getLocation().getWorld().getName());
				cm.getMain().getConfig().set("lobby.x", p.getLocation().getX());
				cm.getMain().getConfig().set("lobby.y", p.getLocation().getY());
				cm.getMain().getConfig().set("lobby.z", p.getLocation().getZ());
				cm.getMain().getConfig().set("lobby.yaw", p.getLocation().getYaw());
				cm.getMain().getConfig().set("lobby.pitch", p.getLocation().getPitch());
				p.sendMessage(Message.SET_LOBBY_POS.get());
			}else{
				if (args.length == 2){
					int pos;
					if ((pos = Integer.parseInt(args[1])) <= 2){
						p.sendMessage(Message.SET_ARENA_POS.getF(pos + "", arena));
						cm.getMain().getAM().createArenaPoint(arena, Integer.valueOf(pos), p.getLocation());
						if (cm.getMain().arenas.get("arenas." + arena + ".enabled") == null)
							cm.getMain().arenas.set("arenas." + arena + ".enabled", false);
					}else
						p.sendMessage(Error.TOO_MANY_SPAWN_POINTS.get());
				}else
					p.sendMessage(Error.INCORRECT_USAGE.get());
			}
		}else
			p.sendMessage(Error.NO_COMMAND_PERMISSION.get());
		return true;
	}
}
