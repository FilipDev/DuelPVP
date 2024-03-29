package org.thespherret.plugins.duelpvp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.thespherret.plugins.duelpvp.enums.EndReason;
import org.thespherret.plugins.duelpvp.enums.Error;
import org.thespherret.plugins.duelpvp.enums.Message;

import java.util.HashMap;
import java.util.List;

public class Events implements Listener {

	private final Main main;
	private final HashMap<String, Long> clickedSign = new HashMap<>();

    public Events(Main main)
    {
		this.main = main;
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent e)
	{
		if (main.getAM().getArena(e.getPlayer()) != null)
			if (!e.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL))
			{
				e.setCancelled(true);
				e.getPlayer().sendMessage(Error.CANNOT_TELEPORT_IN_ARENA.toString());
			}
	}

	@EventHandler
	public void onCommandPreprocess(PlayerCommandPreprocessEvent e)
	{
		if (!main.isPlayerAdmin(e.getPlayer()))
			if (main.getAM().getArena(e.getPlayer()) != null)
			{
				List<String> allowedCommands = main.getConfig().getStringList("allowedcommands");
				if (!(allowedCommands.contains(e.getMessage().split(" ")[0].replaceFirst("/", ""))))
				{
					e.setCancelled(true);
					e.getPlayer().sendMessage(Error.CANNOT_USE_COMMAND_IN_MATCH.toString());
				}
			}
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e)
	{
		final Player p = e.getPlayer();
		if (main.getAM().getArena(p) != null)
			Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				@Override
				public void run() {
					main.getPM().revertPlayer(p);
				}
			}, 20L);
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e)
	{
		Arena arena;
		if ((arena = main.getAM().getArena(e.getEntity().getPlayer())) != null)
		{
			arena.setLoser(e.getEntity());
			arena.endGame(EndReason.DEATH);
			e.getDrops().clear();
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e)
	{
		if (main.getAM().getArena(e.getPlayer()) != null)
		{
			e.getPlayer().sendMessage(Error.CANNOT_MODIFY_BLOCKS.toString());
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e)
	{
		if (main.getAM().getArena(e.getPlayer()) != null)
		{
			e.getPlayer().sendMessage(Error.CANNOT_MODIFY_BLOCKS.toString());
			e.setCancelled(true);
		}

	}

	@EventHandler
	public void onDropItem(PlayerDropItemEvent e)
	{
		Arena a;
		if ((a = main.getAM().getArena(e.getPlayer())) != null)
		{
			if (a.isOccupied() && !a.hasStarted())
				e.setCancelled(true);
			else
				e.getItemDrop().setItemStack(new ItemStack(Material.AIR));
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		Arena arena;
		if ((arena = main.getAM().getArena(e.getPlayer())) != null)
		{
			arena.setLoser(e.getPlayer());
			arena.endGame(EndReason.DISCONNECT);
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		Block block;
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			if ((block = e.getClickedBlock()).getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN)
			{
				Sign sign = (Sign) block.getState();
				String line = sign.getLine(0);
				Player p = e.getPlayer();
				if (line != null)
				{
					Byte b = line.equals(ChatColor.BLUE + "Save Kit§f") ? 1 : line.equals(ChatColor.BLUE + "Load Kit§f") ? 2 : (byte) 0;
					if (b == 1 || b == 2 && sign.getLine(1) != null)
					{
						Long lastClicked = clickedSign.get(p.getName());
						if (lastClicked == null || System.currentTimeMillis() - lastClicked > 1000)
						{
							if (b == 1)
							{
								if (main.getPM().saveKit(p, Integer.parseInt(sign.getLine(1))))
									p.sendMessage(Message.SAVED_KIT.getFormatted(sign.getLine(1)));
								else
									p.sendMessage(Error.COULD_NOT_SAVE_KIT.toString());
							}
							else
							{
								if (main.getAM().getArena(p) != null || p.isOp())
								{
									try {
										main.getPM().loadKit(p, Integer.parseInt(sign.getLine(1)));
										p.sendMessage(Message.LOADED_KIT.getFormatted(sign.getLine(1)));
									} catch (Exception e1) {
										e1.printStackTrace();
										p.sendMessage(Error.DO_NOT_HAVE_KIT.toString());
									}
								}
								else
								{
									e.setCancelled(true);
									p.sendMessage(Error.LOAD_KIT_NOT_IN_MATCH.toString());
								}
							}
						}
						else
						{
							e.setCancelled(true);
							p.sendMessage(Error.WAIT_BETWEEN_SIGN_CLICK.toString());
						}
						e.setCancelled(true);
						clickedSign.put(p.getName(), System.currentTimeMillis());
					}
				}
			}
		}
		Arena a;
		if ((a = main.getAM().getArena(e.getPlayer())) != null)
			if (a.hasStarted())
				e.setCancelled(true);
	}

	@EventHandler
	public void onPlayerSignEdit(SignChangeEvent e)
	{
		String line = e.getLine(0);
		if (equalsAny(line, "Load Kit", "Save Kit", ChatColor.BLUE + "Load Kit§f", ChatColor.BLUE + "Save Kit§f"))
			if (main.isPlayerAdmin(e.getPlayer()))
				if (e.getLine(1).equals("\\d+"))
					e.setLine(0, ChatColor.BLUE + line + "§f");
				else
					e.getPlayer().sendMessage(Error.KIT_NUMBER_MUST_BE_SPECIFIED.toString());
			else
				e.setCancelled(true);
	}

	public boolean equalsAny(String s1, String... s2)
	{
		for (String testingString : s2)
			if (s1.equals(testingString))
				return true;
		return false;
	}
}
