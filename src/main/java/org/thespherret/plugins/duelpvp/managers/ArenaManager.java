package org.thespherret.plugins.duelpvp.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.Arena;
import org.thespherret.plugins.duelpvp.Main;
import org.thespherret.plugins.duelpvp.enums.EndReason;
import org.thespherret.plugins.duelpvp.utils.NMSandOBC;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ArenaManager {

	final Main main;
	private final int matchDelay;

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
				this.addArena(new Arena(this, arenaString));
			}
		}catch (NullPointerException ignored){}
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
			if (!main.arenas1.getFile().exists())
				if (main.arenas1.getFile().createNewFile())
					main.arenas.save(main.arenas1.getFile());
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
			main.arenas.save(main.arenas1.getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Main getMain()
	{
		return this.main;
	}

	public void lobbyTeleport(Player p)
	{
		this.teleportNoChecks(new Location(Bukkit.getWorld(main.getConfig().getString("lobby.world")), main.getConfig().getDouble("lobby.x"), main.getConfig().getDouble("lobby.y"), main.getConfig().getDouble("lobby.z"), main.getConfig().getInt("lobby.yaw"), main.getConfig().getInt("lobby.pitch")), p);
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

	public void teleportNoChecks(Location loc, Player p)
	{
		try {
			Object craftPlayer = NMSandOBC.getOBCClass("entity.CraftEntity").cast(p);
			Object entityPlayer = craftPlayer.getClass().getMethod("getHandle").invoke(craftPlayer);

			entityPlayer.getClass().getMethod("setPositionRotation", double.class, double.class, double.class, float.class, float.class).invoke(entityPlayer, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());

		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
