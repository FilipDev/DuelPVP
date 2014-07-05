package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.managers.CommandManager;
import org.thespherret.plugins.duelpvp.Main;

import java.io.IOException;

public class InfoCommand implements Command {

	@Override
	public boolean execute(CommandManager cm, Player p, String[] args)
	{
		if (args[0].equalsIgnoreCase("reload") && p.hasPermission("duelpvp.admin")){
			p.sendMessage(ChatColor.GREEN + "Reloading config.");
			cm.getMain().reloadConfig();
			p.sendMessage(ChatColor.GREEN + "Reloaded config.");
			try {
				p.sendMessage(ChatColor.GREEN + "Reloading messages.");
				Main.messages.load(Main.messages1.getFile());
				p.sendMessage(ChatColor.GREEN + "Reloaded messages.");
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}
