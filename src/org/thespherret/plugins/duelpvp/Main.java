package org.thespherret.plugins.duelpvp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.thespherret.plugins.duelpvp.enums.Message;
import org.thespherret.plugins.duelpvp.managers.ArenaManager;
import org.thespherret.plugins.duelpvp.managers.CommandManager;
import org.thespherret.plugins.duelpvp.managers.PlayerManager;
import org.thespherret.plugins.duelpvp.managers.RequestManager;
import org.thespherret.plugins.duelpvp.tracker.ScoreTracker;
import org.thespherret.plugins.duelpvp.utils.NewYAML;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main extends JavaPlugin {

	public NewYAML arenas1, playerData1, kits1;
	public YamlConfiguration arenas, playerData, kits;

	ScoreTracker scoreTracker;

	public static NewYAML messages1;
	public static YamlConfiguration messages;

	File messagesFile;

	private CommandManager cm;
	private ArenaManager am;
	private PlayerManager pm;
	private RequestManager rm;

	public final Events events = new Events(this);

	public final static String PREFIX = ChatColor.WHITE + "[" + ChatColor.DARK_GRAY + "DuelPVP" + ChatColor.WHITE + "] ";
	
	public void onEnable()
	{
		this.cm = new CommandManager(this);
		this.am = new ArenaManager(this);
		this.pm = new PlayerManager(this);
		this.rm = new RequestManager(this);

		generateMessages();
		this.saveDefaultConfig();

		for (String command : getDescription().getCommands().keySet())
			getCommand(command).setExecutor(cm);

		Bukkit.getConsoleSender().sendMessage(Message.INITIALIZING.get());

		this.arenas = (this.arenas1 = new NewYAML(new File(getDataFolder() + File.separator + "arenas.dat"))).newYaml();
		this.kits = (this.kits1 = new NewYAML(new File(getDataFolder() + File.separator + "kits.dat"))).newYaml();
		this.playerData = (this.playerData1 = new NewYAML(new File(getDataFolder() + File.separator + "players.dat"))).newYaml();
		this.am.initArenas();
		getServer().getPluginManager().registerEvents(events, this);

		if (getConfig().getBoolean("tracker.shoulduse")){
			try{
				this.scoreTracker = new ScoreTracker(this);
				getServer().getPluginManager().registerEvents(this.scoreTracker, this);
				Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.GREEN + "Successfully connected to the MySQL database!");
			}catch (Exception e){
				Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.RED + "Unsuccessfully connected to the MySQL database!");
				this.scoreTracker = null;
			}
		}
	}
	
	public void onDisable()
	{
		this.saveConfig();
		this.getAM().saveArenas();
		try {
			this.playerData.save(this.playerData1.getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public CommandManager getCM()
	{
		return this.cm;
	}

	public ArenaManager getAM()
	{
		return this.am;
	}

	public PlayerManager getPM()
	{
		return this.pm;
	}

	public RequestManager getRM()
	{
		return this.rm;
	}

	private void generateMessages(){
		messagesFile = new File(getDataFolder() + File.separator + "messages.yml");

		if (!messagesFile.exists()){
			messages = (messages1 = new NewYAML(messagesFile)).newYaml();
			BufferedReader input = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("messages.yml")));
			String line;
			try {
				while ((line = input.readLine()) != null)
					messages.set(line.split(": ")[0], line.split(": ")[1]);
				messages.save(messages1.getFile());
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else
			messages = (messages1 = new NewYAML(messagesFile)).newYaml1();
	}

	public boolean isPlayerAdmin(Player p)
	{
		return p.hasPermission("DuelPVP.Admin");
	}
}