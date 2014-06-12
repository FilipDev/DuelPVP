

package org.thespherret.plugins.duelpvp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.enums.EndReason;
import org.thespherret.plugins.duelpvp.enums.Message;
import org.thespherret.plugins.duelpvp.enums.Error;
import org.thespherret.plugins.duelpvp.managers.ArenaManager;

import java.io.IOException;
import java.util.HashMap;

public class Arena implements Runnable {

	ArenaManager am;

	private String arenaName;
	private World world;
	private Location[] spawns = new Location[2];
	private String[] players = new String[2];
	private HashMap<String, Boolean> requestsToEnd = new HashMap<>();
	private int id;

	private String winner, loser;

	private boolean started = false;
	private boolean occupied = false;
	private int secondsLeft;

	public Arena(ArenaManager am, String arenaName){
		this.arenaName = arenaName;
		this.secondsLeft = am.getMatchStartDelay();
		this.world = am.getWorld(arenaName);
		this.spawns[0] = am.getSpawnPoint(arenaName, 0);
		this.spawns[1] = am.getSpawnPoint(arenaName, 1);
		this.am = am;
	}

	public String getArenaName(){
		return arenaName;
	}

	public void startGame(){
		for (int x = 0; x <= 1; x++){
			am.getMain().getPM().saveInventory(Bukkit.getPlayer(players[x]));
			Bukkit.getPlayer(players[x]).getInventory().clear();
		}
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(am.getMain(), this, 20, 20);
	}

	public void addPlayers(String player1, String player2){
		this.players[0] = player1;
		this.players[1] = player2;
		this.requestsToEnd.put(player1, false);
		this.requestsToEnd.put(player2, false);
		Main main = am.getMain();
		setSecondsLeft(am.getMatchStartDelay());
		for (String playerS : players){
			Player p = Bukkit.getPlayer(playerS);
			am.playersInArenas.put(playerS, getArenaName());
			try {
				main.playerData.set(p.getUniqueId().toString() + ".world", p.getLocation().getWorld().getName());
				main.playerData.set(p.getUniqueId().toString() + ".x", p.getLocation().getBlockX());
				main.playerData.set(p.getUniqueId().toString() + ".y", p.getLocation().getBlockY());
				main.playerData.set(p.getUniqueId().toString() + ".z", p.getLocation().getBlockZ());
				main.playerData.set(p.getUniqueId().toString() + ".pitch", p.getLocation().getPitch());
				main.playerData.set(p.getUniqueId().toString() + ".yaw", p.getLocation().getYaw());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.occupied = true;
		for (String player : players)
			Bukkit.getPlayer(player).sendMessage(Message.MATCH_STARTING.getF(am.getMatchStartDelay() + ""));
		try{
			Bukkit.getPlayer(players[0]).teleport(new Location(Bukkit.getWorld(main.getConfig().getString("lobby.world")), main.getConfig().getInt("lobby." + ".1.x"), main.getConfig().getInt("lobby.1.y"), main.getConfig().getInt("lobby..1.z")));
			Bukkit.getPlayer(players[1]).teleport(new Location(Bukkit.getWorld(main.getConfig().getString("lobby.world")), main.getConfig().getInt("lobby." + ".2.x"), main.getConfig().getInt("lobby.2.y"), main.getConfig().getInt("lobby..2.z")));
		}catch (NullPointerException e){}
		startGame();
	}

	public void setLoser(String player){
		String player1 = getPlayers()[0], player2 = getPlayers()[1];
		if (player1.equals(player)){
			this.winner = player2;
			this.loser = player1;
		}else if (player2.equals(player)){
			this.winner = player1;
			this.loser = player2;
		}else{
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error: " + ChatColor.DARK_RED + " Cannot find player in arena " + getArenaName());
		}
	}

	public String getOtherPlayer(String player){
		String player1 = getPlayers()[0], player2 = getPlayers()[1];
		if (player1.equals(player)){
			return player2;
		}else if (player2.equals(player)){
			return player1;
		}else{
			Bukkit.getConsoleSender().sendMessage(Error.CANNOT_FIND_PLAYER.getF(arenaName));
			return null;
		}
	}

	public void toggleRequestEnd(String player){
		this.requestsToEnd.put(player, !this.requestsToEnd.get(player));
		if (this.requestsToEnd.get(players[0]) && this.requestsToEnd.get(players[1])){
			endGame(EndReason.END);
			return;
		}
		if (this.requestsToEnd.get(player)){
			Bukkit.getPlayer(players[0]).sendMessage(Message.END_MATCH_REQUEST.getF(player));
			Bukkit.getPlayer(players[1]).sendMessage(Message.END_MATCH_REQUEST.getF(player));
			Bukkit.getPlayer(getOtherPlayer(player)).sendMessage(Message.END_MATCH_TIP.get());
		}else{
			Bukkit.getPlayer(players[0]).sendMessage(Message.END_MATCH_REVOCATION.getF(player));
			Bukkit.getPlayer(players[1]).sendMessage(Message.END_MATCH_REVOCATION.getF(player));
		}
	}

	public Integer getScheduledTask(){
		return this.id;
	}

	public String getWinner(){
		return winner;
	}

	public String getLoser(){
		return loser;
	}

	public synchronized int getSecondsLeft(){
		return secondsLeft;
	}

	public synchronized void setSecondsLeft(int secondsLeft){
		this.secondsLeft = secondsLeft;
	}

	@Override
	public void run() {
		Player player;
		for (int x = 0; x < players.length; x++){
			player = Bukkit.getPlayer(players[x]);
			if (!player.isOnline()){
				this.setLoser(player.getName());
				endGame(EndReason.DISCONNECT);
			}
			if (getSecondsLeft() == 15 || getSecondsLeft() == 10 || getSecondsLeft() <= 5){
				if (getSecondsLeft() == 0){
					if (!this.started){
						gameStart();
						Bukkit.getScheduler().cancelTask(id);
					}
				}
				else
					player.sendMessage(Message.MATCH_STARTING.getF(getSecondsLeft() + ""));
			}
		}
		setSecondsLeft(getSecondsLeft() - 1);
	}

	public void endGame(EndReason endReason){
		if (endReason == EndReason.END){
			am.getMain().getPM().revertPlayer(Bukkit.getPlayer(players[0]));
			am.getMain().getPM().revertPlayer(Bukkit.getPlayer(players[1]));
			Bukkit.getPlayer(players[0]).sendMessage(Message.END_MATCH.get());
			Bukkit.getPlayer(players[1]).sendMessage(Message.END_MATCH.get());
		}
		if (endReason == EndReason.DISABLE){
			am.getMain().getPM().revertPlayer(Bukkit.getPlayer(players[0]));
			am.getMain().getPM().revertPlayer(Bukkit.getPlayer(players[1]));
		}
		if (endReason == EndReason.DEATH){
			am.getMain().getPM().revertPlayer(Bukkit.getPlayer(this.winner));
			Bukkit.getPlayer(winner).sendMessage(Message.END_MATCH_DEATH.getF("won", loser));
			Bukkit.getPlayer(loser).sendMessage(Message.END_MATCH_DEATH.getF("lost", winner));
			broadcastWon();
		}
		if (endReason == EndReason.DISCONNECT){
			am.getMain().getPM().revertPlayer(Bukkit.getPlayer(this.winner));
			am.getMain().getPM().revertPlayer(Bukkit.getPlayer(this.loser));
			Bukkit.getPlayer(this.getWinner()).sendMessage(Message.PARTNER_DISCONNECTED.get());
		}
		this.winner = null;
		this.loser = null;
		this.started = false;
		this.players = new String[2];
		this.occupied = false;
		this.requestsToEnd.clear();
	}

	public String[] getPlayers(){
		return players;
	}

	public void gameStart(){
		this.started = true;
		for (int x = 0; x <= 1; x++)
			Bukkit.getPlayer(players[x]).teleport(spawns[x]);
		try {
			am.getMain().playerData.save(am.getMain().playerData1.getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Bukkit.broadcastMessage(Message.BROADCAST_MATCH_START.getF(players[0], players[1]));
	}

	public void broadcastWon(){
		for (Player player : Bukkit.getOnlinePlayers())
			if (!player.getName().equals(players[0]) && !player.getName().equals(players[1]))
				player.sendMessage( ChatColor.LIGHT_PURPLE + " " + this.getWinner() + " has won a duel against " + this.getLoser() + "!");
	}

	public boolean hasStarted(){
		return this.started;
	}

	public boolean isOccupied(){
		return this.occupied;
	}

	public Location getSpawnPoint(Integer i){
		return spawns[i];
	}

}