package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.Location;
import org.thespherret.plugins.duelpvp.Arena;
import org.thespherret.plugins.duelpvp.enums.Error;
import org.thespherret.plugins.duelpvp.managers.ArenaManager;

public class ArenaTPCommand extends Command {

	public void execute()
	{
		if (cm.getMain().isPlayerAdmin(p)){
			if (args.length == 2){
				ArenaManager am = cm.getMain().getAM();
				Arena a;
				if ((a = am.getArena(args[0])) != null){
					try{
						Location loc = am.getSpawnPoint(a.getArenaName(), Integer.parseInt(args[1]) - 1);
						p.teleport(loc);
					}catch (NumberFormatException | NullPointerException e){
						p.sendMessage(Error.SPAWN_POINT_NOT_SET.toString());
					}
				}
			}else
				p.sendMessage(Error.INCORRECT_USAGE.toString());
		}else
			p.sendMessage(Error.NO_COMMAND_PERMISSION.toString());
	}
}
