package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.Arena;
import org.thespherret.plugins.duelpvp.Request;
import org.thespherret.plugins.duelpvp.enums.Error;
import org.thespherret.plugins.duelpvp.enums.Message;
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
				if (!dueled.getName().equals(p.getName()) || true)
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
								int rtd = cm.getMain().getRM().getRequestTimeoutDelay();
								final Request request = new Request(cm.getMain().getRM(), arena, dueled.getUniqueId(), p.getUniqueId());
								cm.getMain().getRM().pendingRequests.add(request);
								dueled.sendMessage(Message.RECIEVED_DUEL_REQUEST.getFormatted(p.getName()));
								p.sendMessage(Message.REQUEST_SENT.getFormatted(dueled.getName()));
								p.sendMessage(Message.REQUEST_TIMEOUT.getFormatted(rtd));
								dueled.sendMessage(Message.REQUEST_TIMEOUT.getFormatted(rtd));
								Bukkit.getScheduler().scheduleSyncDelayedTask(cm.getMain(), new Runnable() {
									public void run() {
										if (cm.getMain().getRM().pendingRequests.contains(request))
											request.cancel();
									}
								}, cm.getMain().getRM().getRequestTimeoutDelay() * 20);
							}else
								p.sendMessage(ChatColor.RED + "Request send cancelled.");
						}
						else
							p.sendMessage(Error.ARENA_OCCUPIED.get());
					}
					else
						p.sendMessage(Error.ALREADY_HAS_DUEL_REQUEST.get());
				}
				else
					p.sendMessage(Error.CANNOT_DUEL_YOURSELF.get());
			}
			else
				p.sendMessage(Error.CANNOT_FIND_PLAYER.getFormatted(args[0]));
		}
		else
			p.sendMessage(Error.INCORRECT_USAGE.get());
		return true;
	}
}
