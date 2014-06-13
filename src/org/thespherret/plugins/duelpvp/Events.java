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
import org.thespherret.plugins.duelpvp.enums.Message;
import org.thespherret.plugins.duelpvp.enums.Error;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Events implements Listener {

	private Main main;
	private HashMap<String, Long> clickedSign = new HashMap<>();

    public Events(Main main){
		this.main = main;
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent e){
		Arena a;
		if ((a = main.getAM().getArena(e.getPlayer())) != null)
			if (a.hasStarted()){
				e.setCancelled(true);
				e.getPlayer().sendMessage(Error.CANNOT_TELEPORT_IN_ARENA.get());
			}
	}

	@EventHandler
	public void onCommandPreprocess(PlayerCommandPreprocessEvent e){
		if (!e.getPlayer().hasPermission("DuelPVP.admin"))
			if (main.getAM().getArena(e.getPlayer()) != null){
				List<String> allowedCommands = main.getConfig().getStringList("allowedcommands");
				if (!(allowedCommands.contains(e.getMessage().split(" ")[0].replaceFirst("/", "")))){
					e.setCancelled(true);
					e.getPlayer().sendMessage(Error.CANNOT_USE_COMMAND_IN_MATCH.get());
				}
			}
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e){
		final Player p = e.getPlayer();
		Arena a;
		if ((a = main.getAM().getArena(p)) != null)
			Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				@Override
				public void run() {
					main.getPM().revertPlayer(p);
				}
			}, 20L);
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e){
		Arena arena;
		if ((arena = main.getAM().getArena(e.getEntity().getPlayer())) != null){
			arena.setLoser(e.getEntity().getName());
			arena.endGame(EndReason.DEATH);
			e.getDrops().clear();
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		Arena arena;
		if ((arena = main.getAM().getArena(e.getPlayer())) != null && arena.hasStarted()){
			if (arena.hasStarted()){
				e.getPlayer().sendMessage(Error.CANNOT_MODIFY_BLOCKS.get());
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e){
		Arena arena;
		if ((arena = main.getAM().getArena(e.getPlayer())) != null && arena.hasStarted()){
			if (arena.hasStarted()){
				e.getPlayer().sendMessage(Error.CANNOT_MODIFY_BLOCKS.get());
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e){
		Arena arena;
		if ((arena = main.getAM().getArena(e.getPlayer())) != null){
			if (arena.hasStarted()){
				arena.setLoser(e.getPlayer().getName());
				arena.endGame(EndReason.DISCONNECT);
			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e){
		Block block;
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			if ((block = e.getClickedBlock()).getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN){
				Sign sign = (Sign) block.getState();
				String line = sign.getLine(0);
				Player p = e.getPlayer();
				if (line != null){
					Byte b = line.equals(ChatColor.BLUE + "Save Kit§f") ? 1 : line.equals(ChatColor.BLUE + "Load Kit§f") ? 2 : (byte) 0;
					if (b == 1 || b == 2 && sign.getLine(1) != null){
						Long lastClicked = clickedSign.get(p.getName());
						if (lastClicked == null || System.currentTimeMillis() - lastClicked > 1000){
							if (b == 1){
								if (saveKit(p, Integer.parseInt(sign.getLine(1))))
									p.sendMessage(Message.SAVED_KIT.getF(sign.getLine(1)));
								else
									p.sendMessage(Error.COULD_NOT_SAVE_KIT.get());
							}
							if (b == 2){
								if (main.getAM().getArena(p) != null){
									loadKit(p, Integer.parseInt(sign.getLine(1)));
									p.sendMessage(Message.LOADED_KIT.getF(sign.getLine(1)));
								}else{
									e.setCancelled(true);
									p.sendMessage(Error.LOAD_KIT_NOT_IN_MATCH.get());
								}
							}
						}else{
							e.setCancelled(true);
							p.sendMessage(Error.WAIT_BETWEEN_SIGN_CLICK.get());
						}
						clickedSign.put(p.getName(), System.currentTimeMillis());
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerSignEdit(SignChangeEvent e){
		String line = e.getLine(0);
		if (line.equals("Load Kit") || line.equals("Save Kit")){
			if (e.getPlayer().hasPermission("DuelPvP.admin")){
				try{
					Integer.parseInt(e.getLine(1));
					e.setLine(0, ChatColor.BLUE + line + "§f");
				}catch (NumberFormatException ex){
					e.getPlayer().sendMessage(Error.KIT_NUMBER_MUST_BE_SPECIFIED.get());
				}
			}
		}
	}

	public boolean saveKit(Player p, Integer kitNumber) {
		try {
			try {
				main.kits.set(p.getUniqueId().toString() + "." + kitNumber + ".main", p.getInventory().getContents());
				main.kits.set(p.getUniqueId().toString() + "." + kitNumber + ".armor", p.getInventory().getArmorContents());
			} catch (Exception e) {
				e.printStackTrace();
			}
			main.kits.save(main.kits1.getFile());
			return true;
		} catch (IOException e) {
			Bukkit.getConsoleSender().sendMessage(Error.COULD_NOT_SAVE_KIT.get());
			return false;
		}
	}

	public void loadKit(Player p, Integer kitNumber){
		p.getInventory().clear();
		try {
			p.getInventory().setContents((ItemStack[]) main.kits.get(p.getUniqueId().toString() + "." + kitNumber + ".main"));
			p.getInventory().setArmorContents((ItemStack[]) main.kits.get(p.getUniqueId().toString() + "." + kitNumber + ".armor"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
