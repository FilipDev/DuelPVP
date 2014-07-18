package org.thespherret.plugins.duelpvp.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.thespherret.plugins.duelpvp.Main;

import java.io.IOException;
import java.util.List;

public class PlayerManager {

	final Main main;

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
		main.getAM().teleportNoChecks(location, p);
		main.getAM().playersInArenas.remove(p.getName());
	}


	public boolean saveKit(Player p, Integer kitNumber)
	{
		try {
			main.kits.set(p.getUniqueId().toString() + "." + kitNumber + ".main", p.getInventory().getContents().clone());
			main.kits.set(p.getUniqueId().toString() + "." + kitNumber + ".armor", p.getInventory().getArmorContents().clone());
			main.kits.save(main.kits1.getFile());
			return true;
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage(org.thespherret.plugins.duelpvp.enums.Error.COULD_NOT_SAVE_KIT.get());
			return false;
		}
	}

	public void loadKit(Player p, Integer kitNumber)
			throws Exception
	{
		ItemStack[] invContents, armContents;
		Object oInv, oArm;

		invContents = (oInv = main.kits.get(p.getUniqueId().toString() + "." + kitNumber + ".main")) instanceof ItemStack[] ? (ItemStack[]) oInv : (ItemStack[]) ((List) oInv).toArray(new ItemStack[4]);
		armContents = (oArm = main.kits.get(p.getUniqueId().toString() + "." + kitNumber + ".armor")) instanceof ItemStack[] ? (ItemStack[]) oArm : (ItemStack[]) ((List) oArm).toArray(new ItemStack[4]);

		p.getInventory().setContents(invContents);
		p.getInventory().setArmorContents(armContents);
		p.updateInventory();
	}

}
