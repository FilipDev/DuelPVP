/*
 * If this plugin is released, it means that whoever bought this plugin gave permission to distribute it. You can use this plugin use the code in this plugin to help you and other fellow plugin devs.
 */

package org.thespherret.plugins.duelpvp.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.thespherret.plugins.duelpvp.Main;
import org.thespherret.plugins.duelpvp.utils.UUIDFetcher;

import java.io.IOException;

/**
 * Created by Administrator on 6/10/14.
 */
public class PlayerManager {

	Main main;

	public PlayerManager(Main main){
		this.main = main;
	}

	public void loadInventory(String player){
		ItemStack[] invMain = new ItemStack[0];
		ItemStack[] invArmor = new ItemStack[0];
		try {
			invMain = (ItemStack[]) main.playerData.get((UUIDFetcher.getUUIDOf(player) + ".inventoryMain"));
			invArmor = (ItemStack[]) main.playerData.get((UUIDFetcher.getUUIDOf(player) + ".inventoryArmor"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!(invMain == null || invArmor == null)){
			try {
				Bukkit.getPlayer((UUIDFetcher.getUUIDOf(player))).getInventory().setContents(invMain);
				Bukkit.getPlayer((UUIDFetcher.getUUIDOf(player))).getInventory().setArmorContents(invArmor);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void saveInventory(String player){
		try {
			main.playerData.set((UUIDFetcher.getUUIDOf(player)) + ".inventoryMain", Bukkit.getPlayer(player).getInventory().getContents());
			main.playerData.set((UUIDFetcher.getUUIDOf(player)) + ".inventoryArmor", Bukkit.getPlayer(player).getInventory().getArmorContents());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void revertPlayer(String player){
		Player player1 = Bukkit.getPlayer(player);
		ConfigurationSection data = main.playerData.getConfigurationSection(player);
		Location location = new Location(Bukkit.getWorld(data.getString("world")), data.getInt("x"), data.getInt("y"), data.getInt("z"), data.getInt("pitch"), data.getInt("yaw"));
		loadInventory(player);
		try {
			data.set(UUIDFetcher.getUUIDOf(player) + ".inventoryMain", null);
			data.set(UUIDFetcher.getUUIDOf(player) + ".inventoryArmor", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			main.playerData.save(main.playerData1.getFile());
		} catch (IOException e) {}
		player1.teleport(location);
		main.getAM().playersInArenas.remove(player);
	}

}
