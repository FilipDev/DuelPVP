/*
 * If this plugin is released, it means that whoever bought this plugin gave permission to distribute it. You can use this plugin use the code in this plugin to help you and other fellow plugin devs.
 */

package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.Arena;
import org.thespherret.plugins.duelpvp.enums.Error;
import org.thespherret.plugins.duelpvp.managers.CommandManager;

/**
 * Created by Administrator on 6/10/14.
 */
public class EndCommand implements Command {
	@Override
	public boolean execute(CommandManager cm, Player p, String[] args)
	{
		Arena arena;
		if (((arena = cm.getMain().getAM().getArena(p)) != null) && (arena.hasStarted())){
			arena.toggleRequestEnd(p.getName());
		}
		else
			p.sendMessage(Error.NOT_IN_STARTED_ARENA.get());
		return true;
	}
}
