package org.thespherret.plugins.duelpvp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.thespherret.plugins.duelpvp.enums.EndReason;
import org.thespherret.plugins.duelpvp.enums.Error;
import org.thespherret.plugins.duelpvp.enums.Message;
import org.thespherret.plugins.duelpvp.events.ArenaEndEvent;
import org.thespherret.plugins.duelpvp.events.ArenaPreStartEvent;
import org.thespherret.plugins.duelpvp.managers.ArenaManager;

import java.io.IOException;
import java.util.HashMap;

public class Arena {

	ArenaManager am;

	private final String arenaName;
	private final Location[] spawns = new Location[2];
	private Player player1, player2;
	private HashMap<String, Boolean> requestsToEnd = new HashMap<>();
	private boolean enabled;

	private Countdown countdown;

	private final Main main;

	private Player winner, loser;

	private boolean started = false, occupied = false;

	public Arena(ArenaManager am, String arenaName)
	{
		this.enabled = am.getMain().arenas.getBoolean("arenas." + arenaName + ".enabled");
		this.arenaName = arenaName;
		this.spawns[0] = am.getSpawnPoint(arenaName, 0);
		this.spawns[1] = am.getSpawnPoint(arenaName, 1);
		this.am = am;
		this.main = am.getMain();
	}

	public String getArenaName()
	{
		return this.arenaName;
	}

	public void startGame()
	{
		this.saveAndClear(player1);
		this.saveAndClear(player2);
	}

	public void saveAndClear(Player p)
	{
		am.getMain().getPM().saveInventory(p);
		p.getInventory().clear();
	}

	public void addPlayers(Player player1, Player player2)
	{
		ArenaPreStartEvent preStartEvent = new ArenaPreStartEvent(this, player1, player2);
		Bukkit.getPluginManager().callEvent(preStartEvent);

		if (!preStartEvent.isCancelled())
		{
			this.player1 = player1;
			this.player2 = player2;
			this.requestsToEnd.put(player1.getName(), false);
			this.requestsToEnd.put(player2.getName(), false);
			for (Player p : new Player[]{player1, player2}){
				am.playersInArenas.put(p.getName(), getArenaName());
				try {
					main.playerData.set(p.getUniqueId().toString() + ".world", p.getLocation().getWorld().getName());
					main.playerData.set(p.getUniqueId().toString() + ".x", p.getLocation().getX());
					main.playerData.set(p.getUniqueId().toString() + ".y", p.getLocation().getY());
					main.playerData.set(p.getUniqueId().toString() + ".z", p.getLocation().getZ());
					main.playerData.set(p.getUniqueId().toString() + ".pitch", p.getLocation().getPitch());
					main.playerData.set(p.getUniqueId().toString() + ".yaw", p.getLocation().getYaw());
					am.lobbyTeleport(p);
					for (PotionEffect pe : p.getActivePotionEffects())
						p.removePotionEffect(pe.getType());
				} catch (Exception e) {
					e.printStackTrace();
				}
				p.sendMessage(Message.MATCH_STARTING.getFormatted(am.getMatchStartDelay()));
			}
			this.countdown = new Countdown(this);
			this.occupied = true;
			startGame();
		}
	}


	public void setLoser(Player player)
	{
		this.loser = player;
		this.winner = getOtherPlayer(player);
	}

	public Player getOtherPlayer(Player player)
	{
		if (player1.getName().equals(player.getName()))
			return player2;
		else if (player2.getName().equals(player.getName()))
			return player1;
		else
		{
			Bukkit.getConsoleSender().sendMessage(Error.CANNOT_FIND_PLAYER.getFormatted(arenaName));
			return null;
		}
	}

	public Integer toInt(String player)
	{
		if (getPlayers()[0].getName().equals(player))
			return 0;
		else
			return 1;
	}

	public void toggleRequestEnd(Player player)
	{
		this.requestsToEnd.put(player.getName(), !this.requestsToEnd.get(player.getName()));

		if (this.requestsToEnd.get(player1.getName()) && this.requestsToEnd.get(player2.getName()))
			endGame(EndReason.END);
		else if (this.requestsToEnd.get(player.getName()))
		{
			player1.sendMessage(Message.END_MATCH_REQUEST.getFormatted(player.getName()));
			player2.sendMessage(Message.END_MATCH_REQUEST.getFormatted(player.getName()));
			getOtherPlayer(player).sendMessage(Message.END_MATCH_TIP.toString());
		}
		else
		{
			player1.sendMessage(Message.END_MATCH_REVOCATION.getFormatted(player.getName()));
			player2.sendMessage(Message.END_MATCH_REVOCATION.getFormatted(player.getName()));
		}
	}

	public Player getWinner()
	{
		return winner;
	}

	public Player getLoser()
	{
		return loser;
	}

	public void endGame(EndReason endReason)
	{
		this.started = false;
		switch (endReason)
		{
			case END:
				revertPlayer(player1);
				revertPlayer(player2);
				player1.sendMessage(Message.END_MATCH.toString());
				player2.sendMessage(Message.END_MATCH.toString());
				break;
			case SERVER_CLOSE:
				revertPlayer(player1);
				revertPlayer(player2);
				break;
			case DISABLE:
				revertPlayer(player1);
				revertPlayer(player2);
				break;
			case DEATH:
				revertPlayer(winner);
				winner.sendMessage(Message.END_MATCH_DEATH.getFormatted("won", loser.getName()));
				loser.sendMessage(Message.END_MATCH_DEATH.getFormatted("lost", winner.getName()));
				broadcastWon();
				break;
			case DISCONNECT:
				revertPlayer(winner);
				revertPlayer(loser);
				getWinner().sendMessage(Message.PARTNER_DISCONNECTED.toString());
				break;
			case CANCELLED:
				revertPlayer(winner);
				revertPlayer(loser);
				winner.sendMessage(ChatColor.RED + "Game start has been cancelled.");
				loser.sendMessage(ChatColor.RED + "Game start has been cancelled.");
				break;
		}
		Bukkit.getScheduler().cancelTask(countdown.getTaskID());
		ArenaEndEvent arenaEndEvent = new ArenaEndEvent(this, endReason, winner, loser);
		Bukkit.getPluginManager().callEvent(arenaEndEvent);
		this.winner = null;
		this.loser = null;
		this.player1 = null;
		this.player2 = null;
		this.occupied = false;
		this.requestsToEnd.clear();
	}

	public Player[] getPlayers()
	{
		return new Player[]{player1, player2};
	}

	public void revertPlayer(Player p)
	{
		am.getMain().getPM().revertPlayer(p);
	}

	public void gameStart()
	{
		Player[] tempPlayerArray = new Player[]{player1, player2};
		for (int x = 0; x <= 1; x++)
			am.teleportNoChecks(spawns[x], tempPlayerArray[x]);
		try {
			am.getMain().playerData.save(am.getMain().playerData1.getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Bukkit.broadcastMessage(Message.BROADCAST_MATCH_START.getFormatted(player1.getName(), player2.getName()));
		this.started = true;
	}

	public void broadcastWon()
	{
		for (Player player : Bukkit.getOnlinePlayers())
			if (!player.getName().equals(player1.getName()) && !player.getName().equals(player2.getName()))
				player.sendMessage(Main.PREFIX + ChatColor.LIGHT_PURPLE + this.getWinner() + " won a duel against " + this.getLoser() + "!");
	}

	public boolean hasStarted()
	{
		return started;
	}

	public boolean isOccupied()
	{
		return this.occupied;
	}

	public boolean isEnabled()
	{
		return this.enabled;
	}

	public boolean setEnabled(boolean b)
	{
		am.getMain().arenas.set("arenas." + arenaName + ".enabled", b);
		if (this.started)
			endGame(EndReason.DISABLE);
		try {
			am.getMain().arenas.save(am.getMain().arenas1.getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return !(this.enabled = b);
	}

	public boolean toggleEnabled()
	{
		return setEnabled(!am.getMain().arenas.getBoolean("arenas." + arenaName + ".enabled"));
	}

	public Location getSpawnPoint(Integer i)
	{
		return spawns[i];
	}

	public Countdown getCountdown()
	{
		return this.countdown;
	}
}