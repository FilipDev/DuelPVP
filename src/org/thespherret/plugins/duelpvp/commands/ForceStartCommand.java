package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.Arena;
import org.thespherret.plugins.duelpvp.enums.Message;
import org.thespherret.plugins.duelpvp.enums.Error;
import org.thespherret.plugins.duelpvp.managers.CommandManager;

public class ForceStartCommand implements Command {

	@Override
	public boolean execute(CommandManager cm, Player p, String[] args)
	{
		if (p.hasPermission("DuelPVP.force")){
			Arena a;
			if ((a = cm.getMain().getAM().getArena(p)) != null){
				if (!a.hasStarted()){
					a.gameStart();
					Bukkit.getScheduler().cancelTask(a.getScheduledTask());
					p.sendMessage(Message.FORCE_STARTED_MATCH.get());
				}else
					p.sendMessage(Error.ARENA_NOT_STARTED.get());
			}else
				p.sendMessage(Error.NOT_IN_ARENA.get());
		}
		return true;
	}
}
