package org.thespherret.plugins.duelpvp;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.thespherret.plugins.duelpvp.enums.Message;
import org.thespherret.plugins.duelpvp.managers.ArenaManager;
import org.thespherret.plugins.duelpvp.managers.CommandManager;
import org.thespherret.plugins.duelpvp.managers.PlayerManager;
import org.thespherret.plugins.duelpvp.managers.RequestManager;
import org.thespherret.plugins.duelpvp.utils.NewYAML;

public class Main extends JavaPlugin {

	public NewYAML arenas1, playerData1, kits1;
	public YamlConfiguration arenas, playerData, kits;

	private CommandManager cm;
	private ArenaManager am;
	private PlayerManager pm;
	private RequestManager rm;

	Events events = new Events(this);

	public static String prefix = ChatColor.WHITE + "[" + ChatColor.DARK_GRAY + "DuelPVP" + ChatColor.WHITE + "] ";
	
	public void onEnable(){
		this.cm = new CommandManager(this);
		this.am = new ArenaManager(this);
		this.pm = new PlayerManager(this);
		this.rm = new RequestManager(this);

		getCommand("arenas").setExecutor(cm);
		getCommand("create").setExecutor(cm);
		getCommand("duel").setExecutor(cm);
		getCommand("end").setExecutor(cm);
		getCommand("duelpvp").setExecutor(cm);
		getCommand("arenatp").setExecutor(cm);

		this.saveDefaultConfig();
		Bukkit.getConsoleSender().sendMessage(Message.INITIALIZING.get());
		this.arenas = (this.arenas1 = new NewYAML(new File(getDataFolder() + File.separator + "arenas.dat"))).newYaml();
		this.kits = (this.kits1 = new NewYAML(new File(getDataFolder() + File.separator + "kits.dat"))).newYaml();
		this.playerData = (this.playerData1 = new NewYAML(new File(getDataFolder() + File.separator + "players.dat"))).newYaml();
		this.am.initArenas();
		getServer().getPluginManager().registerEvents(events, this);
	}
	
	public void onDisable(){
		this.saveConfig();
		this.getAM().saveArenas();
		try {
			this.playerData.save(this.playerData1.getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public CommandManager getCM(){
		return this.cm;
	}

	public ArenaManager getAM(){
		return this.am;
	}

	public PlayerManager getPM(){
		return this.pm;
	}

	public RequestManager getRM(){
		return this.rm;
	}
}