package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.managers.CommandManager;
import org.thespherret.plugins.duelpvp.enums.Error;

public class ArenaToggleCommand implements Command {

	@Override
	public boolean execute(CommandManager cm, Player p, String[] args)
	{
		if (p.hasPermission("DuelPVP.admin")){
			if (args.length == 1){
				boolean enabled = cm.getMain().getAM().getArena(args[0]).toggleEnabled();
				p.sendMessage(ChatColor.GRAY + "Arena " + ChatColor.RED + args[0] + ChatColor.GRAY + " has been: " + (enabled ? ChatColor.RED + "Disabled" : ChatColor.GREEN + "Enabled") + ".");
			}else
				p.sendMessage(Error.INCORRECT_USAGE.get());
		}else
			p.sendMessage(Error.NO_COMMAND_PERMISSION.get());
		return true;
	}
}
