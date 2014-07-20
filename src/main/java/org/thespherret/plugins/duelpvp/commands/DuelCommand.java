package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.Arena;
import org.thespherret.plugins.duelpvp.Request;
import org.thespherret.plugins.duelpvp.enums.Error;
import org.thespherret.plugins.duelpvp.events.RequestSendEvent;
import org.thespherret.plugins.duelpvp.managers.CommandManager;

public class DuelCommand implements Command {

	@Override
	public boolean execute(final CommandManager cm, Player p, String[] args)
	{
		if (args.length != 0)
		{
			Player dueled;
			if ((dueled = Bukkit.getPlayer(args[0])) != null)
			{
				if (!dueled.getName().equals(p.getName()))
				{
					Request request0 = cm.getMain().getRM().getRequest(p);
					Request request01 = cm.getMain().getRM().getRequest(dueled);
					if ((request0 == null) && (request01 == null) && (cm.getMain().getAM().getArena(dueled) == null))
					{
						Arena arena;
						if (args.length == 1 && p.hasPermission("DuelPVP.SelectArena"))
							arena = cm.getMain().getAM().getRandomArena();
						else
							arena = cm.getMain().getAM().getArena(args[1]);
						if (arena != null)
						{
							RequestSendEvent requestSendEvent = new RequestSendEvent(arena, p, dueled);
							Bukkit.getPluginManager().callEvent(requestSendEvent);
							if (!requestSendEvent.isCancelled()){
								Request request = new Request(cm.getMain().getRM(), arena, dueled.getUniqueId(), p.getUniqueId());
								cm.getMain().getRM().sendRequest(request);
							}else
								p.sendMessage(ChatColor.RED + "Request send cancelled.");
						}
						else
							p.sendMessage(Error.ARENA_OCCUPIED.toString());
					}
					else
						p.sendMessage(Error.ALREADY_HAS_DUEL_REQUEST.toString());
				}
				else
					p.sendMessage(Error.CANNOT_DUEL_YOURSELF.toString());
			}
			else
				p.sendMessage(Error.CANNOT_FIND_PLAYER.getFormatted(args[0]));
		}
		else
			p.sendMessage(Error.INCORRECT_USAGE.toString());
		return true;
	}
}
