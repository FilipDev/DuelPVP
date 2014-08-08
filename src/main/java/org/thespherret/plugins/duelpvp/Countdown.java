package org.thespherret.plugins.duelpvp;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.thespherret.plugins.duelpvp.enums.EndReason;
import org.thespherret.plugins.duelpvp.enums.Message;
import org.thespherret.plugins.duelpvp.events.ArenaStartEvent;

import java.util.Arrays;
import java.util.List;

public class Countdown extends BukkitRunnable {

	private Arena arena;
	private int secondsLeft;

	private int taskID;

	public static List<Integer> tellTimes = Arrays.asList(15, 10, 5, 4, 3, 2, 1);

	public Countdown(Arena arena)
	{
		this.secondsLeft = arena.am.getMatchStartDelay();
		this.arena = arena;
		this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(arena.am.getMain(), this, 20, 20);
	}

	public synchronized int getSecondsLeft()
	{
		return secondsLeft;
	}

	public synchronized void setSecondsLeft(int secondsLeft)
	{
		this.secondsLeft = secondsLeft;
	}

	public int getTaskID()
	{
		return this.taskID;
	}

	@Override
	public void run()
	{
		Player[] players = arena.getPlayers();


			if (tellTimes.contains(secondsLeft))
			{
				if (secondsLeft == 0)
				{
					ArenaStartEvent arenaStartEvent = new ArenaStartEvent(arena, players[0], players[1]);
					Bukkit.getPluginManager().callEvent(arenaStartEvent);
					if (!arenaStartEvent.isCancelled())
						arena.gameStart();
					else
						arena.endGame(EndReason.CANCELLED);
					Bukkit.getScheduler().cancelTask(getTaskID());
				}
				else
					for (Player player : players)
					{
						if (!player.isOnline())
						{
							arena.setLoser(player);
							arena.endGame(EndReason.DISCONNECT);
						}
						else
							player.sendMessage(Message.MATCH_STARTING.getFormatted(getSecondsLeft()));
					}
		}
		setSecondsLeft(getSecondsLeft() - 1);
	}

}
