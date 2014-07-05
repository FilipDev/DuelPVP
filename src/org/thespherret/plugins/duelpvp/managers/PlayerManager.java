/*
 * If this plugin is released, it means that whoever bought this plugin gave permission to distribute it. You can use this plugin use the code in this plugin to help you and other fellow plugin devs.
 */

package org.thespherret.plugins.duelpvp.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.thespherret.plugins.duelpvp.Main;

import java.io.IOException;

public class PlayerManager {

	Main main;

	public PlayerManager(Main main)
	{
		this.main = main;
	}

	public void loadInventory(Player p)
	{
		ItemStack[] invMain = new ItemStack[0], invArmor = new ItemStack[0];
		try {
			invMain = (ItemStack[]) main.playerData.get(p.getUniqueId().toString() + ".inventoryMain");
			invArmor = (ItemStack[]) main.playerData.get(p.getUniqueId().toString() + ".inventoryArmor");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!(invMain == null || invArmor == null)){
			try {
				p.getInventory().setContents(invMain);
				p.getInventory().setArmorContents(invArmor);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void saveInventory(Player p)
	{
		try {
			main.playerData.set(p.getUniqueId().toString() + ".inventoryMain", p.getInventory().getContents());
			main.playerData.set(p.getUniqueId().toString() + ".inventoryArmor", p.getInventory().getArmorContents());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void revertPlayer(Player p)
	{
		ConfigurationSection data = null;
		try {
			data = main.playerData.getConfigurationSection(p.getUniqueId().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Location location = new Location(Bukkit.getWorld(data.getString("world", "world")), data.getDouble("x"), data.getDouble("y"), data.getDouble("z"), data.getInt("pitch"), data.getInt("yaw"));
		loadInventory(p);
		try {
			data.set(p.getUniqueId().toString() + ".inventoryMain", null);
			data.set(p.getUniqueId().toString() + ".inventoryArmor", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			main.playerData.save(main.playerData1.getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		p.teleport(location, PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
		main.getAM().playersInArenas.remove(p.getName());
	}

}
