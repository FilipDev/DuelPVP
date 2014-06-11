/*
 * If this plugin is released, it means that whoever bought this plugin gave permission to distribute it. You can use this plugin use the code in this plugin to help you and other fellow plugin devs.
 */

package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.Arena;
import org.thespherret.plugins.duelpvp.managers.CommandManager;

public class ArenasCommand implements Command {
	@Override
	public boolean execute(CommandManager cm, CommandSender sender, String[] args) {
		Player p = (Player) sender;
		p.sendMessage(ChatColor.WHITE + "===" + ChatColor.GRAY + "Current Arenas" + ChatColor.WHITE + "===");
		for (Arena arena : cm.getMain().getAM().activeArenas.values()) {
			p.sendMessage((arena.isOccupied() ? ChatColor.RED : ChatColor.WHITE) + arena.getArenaName());
		}
		return true;
	}
}
