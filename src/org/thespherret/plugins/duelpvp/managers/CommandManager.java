/*
 * If this plugin is released, it means that whoever bought this plugin gave permission to distribute it. You can use this plugin use the code in this plugin to help you and other fellow plugin devs.
 */

package org.thespherret.plugins.duelpvp.managers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.thespherret.plugins.duelpvp.Main;
import org.thespherret.plugins.duelpvp.commands.*;

import java.util.HashMap;

/**
 * Created by Administrator on 6/10/14.
 */
public class CommandManager implements CommandExecutor {

	Main main;
	HashMap<String, org.thespherret.plugins.duelpvp.commands.Command> commandMap = new HashMap<>();

	public CommandManager(Main main){
		this.main = main;
		commandMap.put("arenas", new ArenasCommand());
		commandMap.put("create", new CreateCommand());
		commandMap.put("duel", new DuelCommand());
		commandMap.put("end", new EndCommand());
		commandMap.put("duelpvp", new InfoCommand());
		commandMap.put("arenatp", new ArenaTPCommand());
		commandMap.put("forcestart", new ForceStartCommand());
		commandMap.put("arenatoggle", new DisableCommand());
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		return commandMap.get(command.getName()).execute(this, commandSender, strings);
	}

	public Main getMain(){
		return this.main;
	}
}
