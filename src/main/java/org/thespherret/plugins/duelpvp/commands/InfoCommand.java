package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.thespherret.plugins.duelpvp.enums.Error;

import java.io.IOException;

public class InfoCommand extends Command {

	public void execute()
	{
		if (args.length == 1)
		{
			if (args[0].equalsIgnoreCase("reload") && cm.getMain().isPlayerAdmin(p))
			{
				p.sendMessage(ChatColor.GREEN + "Reloading config.");
				cm.getMain().reloadConfig();
				p.sendMessage(ChatColor.GREEN + "Reloaded config.");
				try {
					p.sendMessage(ChatColor.GREEN + "Reloading messages.");
					cm.getMain().messages.load(cm.getMain().messages1.getFile());
					p.sendMessage(ChatColor.GREEN + "Reloaded messages.");
					p.sendMessage(ChatColor.GREEN + "Reloading arenas.");
					cm.getMain().arenas.load(cm.getMain().arenas1.getFile());
					p.sendMessage(ChatColor.GREEN + "Reloaded arenas.");
				} catch (IOException | InvalidConfigurationException e) {
					e.printStackTrace();
				}
			}
		}
		else
			p.sendMessage(Error.INCORRECT_USAGE.toString());
	}
}
