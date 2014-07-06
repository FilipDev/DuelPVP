package org.thespherret.plugins.duelpvp.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.Arena;
import org.thespherret.plugins.duelpvp.enums.EndReason;
import org.thespherret.plugins.duelpvp.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ArenaManager {

	Main main;
	private int matchDelay;

	public HashMap<String, Arena> activeArenas = new HashMap<>();

	public HashMap<String, String> playersInArenas = new HashMap<>();

	public ArenaManager(Main main)
	{
		this.main = main;
		this.matchDelay = main.getConfig().getInt("match-start-delay", 30);
	}

	public Arena getArena(String arenaName)
	{
		return activeArenas.get(arenaName);
	}

	public Arena getArena(Player player)
	{
		return activeArenas.get(playersInArenas.get(player.getName()));
	}

	public void addArena(Arena arena)
	{
		activeArenas.put(arena.getArenaName(), arena);
	}

	public void initArenas()
	{
		try{
			for (String arenaString : main.arenas.getConfigurationSection("arenas").getKeys(false)){
				Bukkit.getConsoleSender().sendMessage("Initializing arena " + arenaString + ".");
				addArena(new Arena(this, arenaString));
			}
		}catch (NullPointerException e){
			e.printStackTrace();
		}
	}

	public World getWorld(String arenaName)
	{
		return Bukkit.getWorld(main.arenas.getString("arenas." + arenaName + ".1.world"));
	}

	public Location getSpawnPoint(String arenaName, Integer label)
	{
		label++;
		return new Location(getWorld(arenaName), main.arenas.getDouble("arenas." + arenaName + "." + label + ".x"), main.arenas.getDouble("arenas." + arenaName + "." + label + ".y"), main.arenas.getDouble("arenas." + arenaName + "." + label + ".z"), main.arenas.getInt("arenas." + arenaName + "." + label + ".yaw"), main.arenas.getInt("arenas." + arenaName + "." + label + ".pitch"));
	}

	public void createArenaPoint(String arenaName, Integer locNumber, Location location)
	{
		main.arenas.set("arenas." + arenaName + "." + locNumber + ".world", location.getWorld().getName());
		main.arenas.set("arenas." + arenaName + "." + locNumber + ".x", location.getX());
		main.arenas.set("arenas." + arenaName + "." + locNumber + ".y", location.getY());
		main.arenas.set("arenas." + arenaName + "." + locNumber + ".z", location.getZ());
		main.arenas.set("arenas." + arenaName + "." + locNumber + ".yaw", location.getYaw());
		main.arenas.set("arenas." + arenaName + "." + locNumber + ".pitch", location.getPitch());
		try {
			if (!main.arenas1.getFormattedile().exists())
				if (main.arenas1.getFormattedile().createNewFile())
					main.arenas.save(main.arenas1.getFormattedile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		initArenas();
	}

	public void saveArenas()
	{
		try {
			for (Arena arena : activeArenas.values())
				if (arena.hasStarted())
					arena.endGame(EndReason.SERVER_CLOSE);
			main.arenas.save(main.arenas1.getFormattedile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Main getMain()
	{
		return this.main;
	}

	public Integer getMatchStartDelay()
	{
		return this.matchDelay;
	}

	public Arena getRandomArena()
	{
		ArrayList<String> unoccupiedArenas = new ArrayList<>();
		for (Arena arena : activeArenas.values())
			if (!arena.isOccupied() && arena.isEnabled())
				unoccupiedArenas.add(arena.getArenaName());
		if (unoccupiedArenas.isEmpty())
			return null;
		return getArena(unoccupiedArenas.get(new Random().nextInt(unoccupiedArenas.size())));
	}

}
