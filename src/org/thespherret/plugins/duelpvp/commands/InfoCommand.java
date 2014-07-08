package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.Main;
import org.thespherret.plugins.duelpvp.managers.CommandManager;

import java.io.IOException;

public class InfoCommand implements Command {

	@Override
	public boolean execute(CommandManager cm, Player p, String[] args)
	{
		if (args[0].equalsIgnoreCase("reload") && cm.getMain().isPlayerAdmin(p)){
			p.sendMessage(ChatColor.GREEN + "Reloading config.");
			cm.getMain().reloadConfig();
			p.sendMessage(ChatColor.GREEN + "Reloaded config.");
			try {
				p.sendMessage(ChatColor.GREEN + "Reloading messages.");
				Main.messages.load(Main.messages1.getFile());
				p.sendMessage(ChatColor.GREEN + "Reloaded messages.");
				p.sendMessage(ChatColor.GREEN + "Reloading arenas.");
				cm.getMain().arenas.load(cm.getMain().arenas1.getFile());
				p.sendMessage(ChatColor.GREEN + "Reloaded arenas.");
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}
