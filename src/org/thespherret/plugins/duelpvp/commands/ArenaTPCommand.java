package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.Arena;
import org.thespherret.plugins.duelpvp.managers.ArenaManager;
import org.thespherret.plugins.duelpvp.managers.CommandManager;
import org.thespherret.plugins.duelpvp.enums.Error;

public class ArenaTPCommand implements Command {

	@Override
	public boolean execute(CommandManager cm, Player p, String[] args)
	{
		if (p.hasPermission("DuelPVP.admin")){
			if (args.length == 2){
				ArenaManager am = cm.getMain().getAM();
				Arena a;
				if ((a = am.getArena(args[0])) != null){
					try{
						Location loc = am.getSpawnPoint(a.getArenaName(), Integer.parseInt(args[1]) - 1);
						p.teleport(loc);
					}catch (NullPointerException e){
						p.sendMessage(Error.SPAWN_POINT_NOT_SET.get());
					}
				}
			}else
				p.sendMessage(Error.INCORRECT_USAGE.get());
		}else
			p.sendMessage(Error.NO_COMMAND_PERMISSION.get());
		return true;
	}
}
