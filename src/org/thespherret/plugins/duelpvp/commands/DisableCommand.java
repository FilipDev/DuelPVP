/*
 * If this plugin is released, it means that whoever bought this plugin gave permission to distribute it. You can use this plugin use the code in this plugin to help you and other fellow plugin devs.
 */

package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.thespherret.plugins.duelpvp.managers.CommandManager;
import org.thespherret.plugins.duelpvp.enums.Error;

public class DisableCommand implements Command {
	@Override
	public boolean execute(CommandManager cm, CommandSender sender, String[] args) {
		if (sender.hasPermission("DuelPVP.admin")){
			if (args.length == 1){
				boolean disabled = cm.getMain().arenas.getBoolean("arenas." + args[0] + ".disabled");
				cm.getMain().arenas.set("arenas." + args[0] + ".disabled", !disabled);
				sender.sendMessage(ChatColor.GRAY + "Arena " + ChatColor.RED + "%v" + ChatColor.GRAY + " has been: " + (disabled ? ChatColor.RED + "Disabled" : ChatColor.GREEN + "Enabled") + ".");
			}else
				sender.sendMessage(Error.INCORRECT_USAGE.get());
		}else
			sender.sendMessage(Error.NO_COMMAND_PERMISSION.get());
		return true;
	}
}
