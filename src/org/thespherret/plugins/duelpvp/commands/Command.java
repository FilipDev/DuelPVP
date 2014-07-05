package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.managers.CommandManager;

public interface Command {

	public boolean execute(CommandManager cm, Player p, String[] args);

}
