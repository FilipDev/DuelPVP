package org.thespherret.plugins.duelpvp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.thespherret.plugins.duelpvp.Arena;

public class ArenaEndEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private Player winner, loser;
	private Arena arena;

	public ArenaEndEvent(Arena arena, Player winner, Player loser) {
		this.winner = winner;
		this.loser = loser;
		this.arena = arena;
	}

	public Arena getArena() {
		return arena;
	}

	public Player getWinner() {
		return winner;
	}

	public Player getLoser() {
		return loser;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
