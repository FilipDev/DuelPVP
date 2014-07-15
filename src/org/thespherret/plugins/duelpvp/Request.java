package org.thespherret.plugins.duelpvp;

import org.bukkit.Bukkit;
import org.thespherret.plugins.duelpvp.enums.Message;
import org.thespherret.plugins.duelpvp.managers.RequestManager;

import java.util.UUID;

public class Request {

	private UUID defenderUUID, attackerUUID;
	private Arena arena;
	private RequestManager rm;
	private boolean cancelled;

	public Request(RequestManager rm, Arena arena, UUID defenderUUID, UUID attackerUUID)
	{
		this.rm = rm;
		this.arena = arena;
		this.attackerUUID = attackerUUID;
		this.defenderUUID = defenderUUID;
		this.cancelled = false;
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
		setCancelled(true);
		Bukkit.getPlayer(defenderUUID).sendMessage(Message.REQUEST_TIMEDOUT.get());
		Bukkit.getPlayer(attackerUUID).sendMessage(Message.REQUEST_TIMEDOUT.get());
	}

	private void setCancelled(boolean b)
	{
		this.cancelled = b;
	}

	public boolean isCancelled()
	{
		return this.cancelled;
	}
}
