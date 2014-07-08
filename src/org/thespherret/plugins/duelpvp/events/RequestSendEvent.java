package org.thespherret.plugins.duelpvp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.thespherret.plugins.duelpvp.Arena;

public class RequestSendEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private Player attacker, defender;
	private Arena arena;
	private boolean cancelled;

	public RequestSendEvent(Arena arena, Player attacker, Player defender) {
		this.attacker = attacker;
		this.defender = defender;
		this.arena = arena;
	}

	public Arena getArena() {
		return arena;
	}

	public Player getAttacker() {
		return attacker;
	}

	public Player getDefender() {
		return defender;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
