package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.managers.CommandManager;

public abstract class Command {

	CommandManager cm;
	String[] args;
	Player p;

	public void execute(CommandManager cm, Player p, String[] args)
	{
		this.cm = cm;
		this.p = p;
		this.args = args;
		execute();
	}

	public abstract void execute();

}