package org.thespherret.plugins.duelpvp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.thespherret.plugins.duelpvp.Arena;
import org.thespherret.plugins.duelpvp.enums.EndReason;

public class ArenaEndEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private final Player winner, loser;
	private final EndReason reason;
	private final Arena arena;

	public ArenaEndEvent(Arena arena, EndReason endReason, Player winner, Player loser)
	{
		this.winner = winner;
		this.reason = endReason;
		this.loser = loser;
		this.arena = arena;
	}

	public Arena getArena()
	{
		return arena;
	}
	public EndReason getReason()
	{
		return reason;
	}

	public Player getWinner()
	{
		return winner;
	}

	public Player getLoser()
	{
		return loser;
	}

	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}
}
