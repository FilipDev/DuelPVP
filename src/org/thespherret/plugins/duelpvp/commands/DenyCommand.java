/*
 * If this plugin is released, it means that whoever bought this plugin gave permission to distribute it. You can use this plugin use the code in this plugin to help you and other fellow plugin devs.
 */

package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.Request;
import org.thespherret.plugins.duelpvp.enums.Error;
import org.thespherret.plugins.duelpvp.enums.Message;
import org.thespherret.plugins.duelpvp.managers.CommandManager;

/**
 * Created by Administrator on 6/14/14.
 */
public class DenyCommand implements Command {
	@Override
	public boolean execute(CommandManager cm, CommandSender sender, String[] args) {
		Player p = (Player) sender;
		Request request = cm.getMain().getRM().getRequest(p);
		if (request != null)
		{
			Player attacker = Bukkit.getPlayer(request.getAttackerString());
			if (attacker.isOnline())
			{
				p.sendMessage(Message.REQUEST_DENY.getF(attacker.getName()));
				attacker.sendMessage(Message.REQUEST_DENIED.getF(p.getName()));
			}
			cm.getMain().getRM().pendingRequests.remove(request);
			return true;
		}
		p.sendMessage(Error.NO_PENDING_DUEL_REQUEST.get());
		return true;
	}
}