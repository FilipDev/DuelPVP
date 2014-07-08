package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.Request;
import org.thespherret.plugins.duelpvp.enums.Error;
import org.thespherret.plugins.duelpvp.enums.Message;
import org.thespherret.plugins.duelpvp.managers.CommandManager;

public class AcceptCommand implements Command {

	@Override
	public boolean execute(CommandManager cm, Player p, String[] args){
		Request request = cm.getMain().getRM().getRequest(p);
		if (request != null){
			Player attacker = Bukkit.getPlayer(request.getAttackerUUID());
			if (attacker.isOnline()){
				p.sendMessage(Message.REQUEST_ACCEPT.getFormatted(attacker.getName()));
				attacker.sendMessage(Message.REQUEST_ACCEPTED.getFormatted(p.getName()));
				request.getArena().addPlayers(p, attacker);
			}
			else
				p.sendMessage(Message.PARTNER_DISCONNECTED.get());
			cm.getMain().getRM().pendingRequests.remove(request);
			return true;
		}
		p.sendMessage(Error.NO_PENDING_DUEL_REQUEST.get());
		return true;
	}
}
