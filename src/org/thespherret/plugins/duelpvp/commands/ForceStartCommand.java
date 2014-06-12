/*
 * If this plugin is released, it means that whoever bought this plugin gave permission to distribute it. You can use this plugin use the code in this plugin to help you and other fellow plugin devs.
 */

package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.Arena;
import org.thespherret.plugins.duelpvp.enums.Message;
import org.thespherret.plugins.duelpvp.enums.Error;
import org.thespherret.plugins.duelpvp.managers.CommandManager;

public class ForceStartCommand implements Command {

	@Override
	public boolean execute(CommandManager cm, CommandSender sender, String[] args) {
		if (sender.hasPermission("DuelPVP.force")){
			Arena a;
			if ((a = cm.getMain().getAM().getArena((Player) sender)) != null){
				if (!a.hasStarted()){
					a.gameStart();
					Bukkit.getScheduler().cancelTask(a.getScheduledTask());
					sender.sendMessage(Message.FORCE_STARTED_MATCH.get());
				}else
					sender.sendMessage(Error.ARENA_NOT_STARTED.get());
			}else
				sender.sendMessage(Error.NOT_IN_ARENA.get());
		}
		return true;
	}
}
