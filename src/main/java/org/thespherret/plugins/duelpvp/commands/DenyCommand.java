package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.Request;
import org.thespherret.plugins.duelpvp.enums.Error;
import org.thespherret.plugins.duelpvp.enums.Message;
import org.thespherret.plugins.duelpvp.managers.CommandManager;

public class DenyCommand implements Command {

	@Override
	public boolean execute(CommandManager cm, Player p, String[] args) {
		Request request = cm.getMain().getRM().getRequest(p);
		if (request != null) {
			Player attacker = Bukkit.getPlayer(request.getAttackerUUID());
			if (!(attacker == null)) {
				p.sendMessage(Message.REQUEST_DENY.getFormatted(attacker.getName()));
				attacker.sendMessage(Message.REQUEST_DENIED.getFormatted(p.getName()));
			}
			cm.getMain().getRM().pendingRequests.remove(request);
			return true;
		}
		p.sendMessage(Error.NO_PENDING_DUEL_REQUEST.toString());
		return true;
	}
}
