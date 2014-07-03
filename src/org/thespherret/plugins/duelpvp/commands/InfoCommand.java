/*
 * If this plugin is released, it means that whoever bought this plugin gave permission to distribute it. You can use this plugin use the code in this plugin to help you and other fellow plugin devs.
 */

package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.managers.CommandManager;
import org.thespherret.plugins.duelpvp.Main;

import java.io.IOException;

/**
 * Created by Administrator on 6/10/14.
 */
public class InfoCommand implements Command {
	@Override
	public boolean execute(CommandManager cm, Player p, String[] args)
	{
		if (args.length < 1);
			//p.sendMessage(ChatColor.DARK_GRAY + "DuelPVP" + ChatColor.WHITE + " is a plugin developed by a member of QubeCraft's community! It was made with the community's expectations in mind to provide the best experience to everyone that's either into pvp or not so much.");
		else if (args[0].equalsIgnoreCase("reload") && p.hasPermission("duelpvp.admin")){
			p.sendMessage(ChatColor.GREEN + "Reloading config.");
			cm.getMain().reloadConfig();
			p.sendMessage(ChatColor.GREEN + "Reloaded config.");
			try {
				p.sendMessage(ChatColor.GREEN + "Reloading messages.");
				Main.messages.load(Main.messages1.getFile());
				p.sendMessage(ChatColor.GREEN + "Reloaded messages.");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}
