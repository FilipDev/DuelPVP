package org.thespherret.plugins.duelpvp;

import org.bukkit.Bukkit;
import org.thespherret.plugins.duelpvp.enums.Message;
import org.thespherret.plugins.duelpvp.managers.RequestManager;

import java.util.UUID;

public class Request {

	private UUID defenderUUID, attackerUUID;
	private Arena arena;
	private RequestManager rm;

	public Request(RequestManager rm, Arena arena, UUID defenderUUID, UUID attackerUUID)
	{
		this.rm = rm;
		this.arena = arena;
		this.attackerUUID = attackerUUID;
		this.defenderUUID = defenderUUID;
	}

	public Arena getArena()
	{
		return arena;
	}

	public UUID getDefenderUUID()
	{
		return defenderUUID;
	}

	public UUID getAttackerUUID()
	{
		return attackerUUID;
	}

	public void cancel()
	{
		rm.pendingRequests.remove(this);
		Bukkit.getPlayer(defenderUUID).sendMessage(Message.REQUEST_TIMEDOUT.get());
		Bukkit.getPlayer(attackerUUID).sendMessage(Message.REQUEST_TIMEDOUT.get());
	}
}
