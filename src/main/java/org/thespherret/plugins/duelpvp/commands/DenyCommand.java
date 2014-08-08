package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.Request;
import org.thespherret.plugins.duelpvp.enums.Error;
import org.thespherret.plugins.duelpvp.enums.Message;

public class DenyCommand extends Command {

	public void execute()
	{
		Request request = cm.getMain().getRM().getRequest(p);
		if (request != null)
		{
			Player attacker = Bukkit.getPlayer(request.getAttackerUUID());
			if (!(attacker == null))
			{
				p.sendMessage(Message.REQUEST_DENY.getFormatted(attacker.getName()));
				attacker.sendMessage(Message.REQUEST_DENIED.getFormatted(p.getName()));
			}
			cm.getMain().getRM().pendingRequests.remove(request);
		}
		else
			p.sendMessage(Error.NO_PENDING_DUEL_REQUEST.toString());
	}
}
