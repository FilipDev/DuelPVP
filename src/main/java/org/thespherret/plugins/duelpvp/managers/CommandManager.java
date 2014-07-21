package org.thespherret.plugins.duelpvp.managers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.Main;
import org.thespherret.plugins.duelpvp.commands.*;
import org.thespherret.plugins.duelpvp.enums.Error;

import java.util.HashMap;

public class CommandManager implements CommandExecutor {

	final Main main;
	final HashMap<String, org.thespherret.plugins.duelpvp.commands.Command> commandMap = new HashMap<>();

	public CommandManager(Main main)
	{
		this.main = main;
		commandMap.put("arenas", new ArenasCommand());
		commandMap.put("create", new CreateCommand());
		commandMap.put("duel", new DuelCommand());
		commandMap.put("end", new EndCommand());
		commandMap.put("accept", new AcceptCommand());
		commandMap.put("deny", new DenyCommand());
		commandMap.put("duelpvp", new InfoCommand());
		commandMap.put("arenatp", new ArenaTPCommand());
		commandMap.put("lobbytp", new LobbyTPCommand());
		commandMap.put("forcestart", new ForceStartCommand());
		commandMap.put("arenatoggle", new ArenaToggleCommand());
		commandMap.put("selectkit", new SelectKitCommand());
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
	{
		if (commandSender instanceof Player)
		{
			commandMap.get(command.getName()).execute(this, (Player) commandSender, strings);
			return true;
		}
		else
		{
			Bukkit.getConsoleSender().sendMessage(Error.CONSOLE_CANNOT_SEND.toString());
			return false;
		}
	}

	public Main getMain()
	{
		return this.main;
	}
}
