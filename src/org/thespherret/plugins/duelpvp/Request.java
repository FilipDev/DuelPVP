package org.thespherret.plugins.duelpvp;

import org.bukkit.Bukkit;
import org.thespherret.plugins.duelpvp.enums.Message;
import org.thespherret.plugins.duelpvp.managers.RequestManager;

public class Request {

	private String defenderString, attackerString;
	private Arena arena;
	private RequestManager rm;

	public Request(RequestManager rm, Arena arena, String defenderString, String attackerString)
	{
		this.rm = rm;
		this.arena = arena;
		this.attackerString = attackerString;
		this.defenderString = defenderString;
	}

	public Arena getArena()
	{
		return arena;
	}

	public String getDefenderString()
	{
		return defenderString;
	}

	public String getAttackerString()
	{
		return attackerString;
	}

	public void cancel()
	{
		rm.pendingRequests.remove(this);
		Bukkit.getPlayer(defenderString).sendMessage(Message.REQUEST_TIMEDOUT.get());
		Bukkit.getPlayer(attackerString).sendMessage(Message.REQUEST_TIMEDOUT.get());
	}
}
