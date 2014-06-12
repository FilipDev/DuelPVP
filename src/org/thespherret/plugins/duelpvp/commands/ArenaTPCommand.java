/*
 * If this plugin is released, it means that whoever bought this plugin gave permission to distribute it. You can use this plugin use the code in this plugin to help you and other fellow plugin devs.
 */

package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.managers.ArenaManager;
import org.thespherret.plugins.duelpvp.managers.CommandManager;
import org.thespherret.plugins.duelpvp.enums.Error;

/**
 * Created by Administrator on 6/11/14.
 */
public class ArenaTPCommand implements Command {
	@Override
	public boolean execute(CommandManager cm, CommandSender sender, String[] args) {
		if (sender.hasPermission("DuelPVP.admin")){
			Player p = (Player) sender;
			ArenaManager am = cm.getMain().getAM();
			try{
				Location loc = am.getSpawnPoint(args[0], Integer.parseInt(args[1]) - 1);
				p.teleport(loc);
			}catch (NullPointerException e){
				sender.sendMessage(Error.SPAWN_POINT_NOT_SET.get());
			}catch (ArrayIndexOutOfBoundsException e){
				sender.sendMessage(Error.INCORRECT_USAGE.get());
			}
		}else
			sender.sendMessage(Error.NO_COMMAND_PERMISSION.get());
		return true;
	}
}
