package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.Request;
import org.thespherret.plugins.duelpvp.enums.Error;
import org.thespherret.plugins.duelpvp.enums.Message;

public class AcceptCommand extends Command {

	public void execute()
	{
		Request request = cm.getMain().getRM().getRequest(p);
		if (request != null){
			Player attacker = Bukkit.getPlayer(request.getAttackerUUID());
			if (!(attacker == null)){
				p.sendMessage(Message.REQUEST_ACCEPT.getFormatted(attacker.getName()));
				attacker.sendMessage(Message.REQUEST_ACCEPTED.getFormatted(p.getName()));
				request.getArena().addPlayers(p, attacker);
			}
			else
				p.sendMessage(Message.PARTNER_DISCONNECTED.toString());
			cm.getMain().getRM().pendingRequests.remove(request);
		}else
			p.sendMessage(Error.NO_PENDING_DUEL_REQUEST.toString());
	}
}
