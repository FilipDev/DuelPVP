/*
 * If this plugin is released, it means that whoever bought this plugin gave permission to distribute it. You can use this plugin use the code in this plugin to help you and other fellow plugin devs.
 */

package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.managers.CommandManager;
import org.thespherret.plugins.duelpvp.Main;

/**
 * Created by Administrator on 6/10/14.
 */
public class InfoCommand implements Command {
	@Override
	public boolean execute(CommandManager cm, CommandSender sender, String[] args) {
		Player player = (Player) sender;
		if (args.length < 1)
			player.sendMessage(ChatColor.DARK_GRAY + "DuelPVP" + ChatColor.WHITE + " is a plugin developed by a member of QubeCraft's community! It was made with the community's expectations in mind to provide the best experience to everyone that's either into pvp or not so much.");
		else if (args[0].equalsIgnoreCase("reload") && player.hasPermission("duelpvp.admin")){
			player.sendMessage(Main.prefix + ChatColor.GREEN + " Reloading config.");
			cm.getMain().reloadConfig();
		}
		return true;
	}
}
