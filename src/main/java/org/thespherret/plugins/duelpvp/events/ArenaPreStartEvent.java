package org.thespherret.plugins.duelpvp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.thespherret.plugins.duelpvp.Arena;

public class ArenaPreStartEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private final Player player1, player2;
	private final Arena arena;
	private boolean cancelled;

	public ArenaPreStartEvent(Arena arena, Player player1, Player player2)
	{
		this.player1 = player1;
		this.player2 = player2;
		this.arena = arena;
	}

	public Arena getArena()
	{
		return arena;
	}

	public Player getPlayer1()
	{
		return player1;
	}

	public Player getPlayer2()
	{
		return player2;
	}

	public boolean isCancelled()
	{
		return cancelled;
	}

	public void setCancelled(boolean cancel)
	{
		cancelled = cancel;
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
