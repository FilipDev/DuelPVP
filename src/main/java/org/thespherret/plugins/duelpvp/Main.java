package org.thespherret.plugins.duelpvp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.thespherret.plugins.duelpvp.enums.Message;
import org.thespherret.plugins.duelpvp.managers.*;
import org.thespherret.plugins.duelpvp.utils.NewYAML;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main extends JavaPlugin {

	public NewYAML arenas1, playerData1, kits1, messages1;
	public YamlConfiguration arenas, playerData, kits, messages;

	private CommandManager cm;
	private ArenaManager am;
	private PlayerManager pm;
	private QueueManager qm;
	private RequestManager rm;
	private PartyManager pam;

	private static String pluginName;

	public static Main getMain()
	{
		return ((Main) Bukkit.getPluginManager().getPlugin(Main.pluginName));
	}

	public final Events events = new Events(this);

	public static String PREFIX;

	public void onEnable()
	{
		Main.pluginName = getDescription().getName();
		Main.PREFIX = ChatColor.WHITE + "[" + ChatColor.DARK_GRAY + Main.pluginName + ChatColor.WHITE + "] ";

		this.cm = new CommandManager(this);
		this.am = new ArenaManager(this);
		this.pm = new PlayerManager(this);
		this.rm = new RequestManager(this);
		this.pam = new PartyManager(this);
		this.qm = new QueueManager(this);

		generateMessages();
		this.saveDefaultConfig();

		Bukkit.getConsoleSender().sendMessage(Message.INITIALIZING.toString());

		for (String command : getDescription().getCommands().keySet())
			getCommand(command).setExecutor(cm);

		this.arenas = (this.arenas1 = new NewYAML(new File(getDataFolder(), "arenas.dat"))).newYaml();
		this.kits = (this.kits1 = new NewYAML(new File(getDataFolder(), "kits.dat"))).newYaml();
		this.playerData = (this.playerData1 = new NewYAML(new File(getDataFolder(), "players.dat"))).newYaml();
		this.am.initArenas();
		getServer().getPluginManager().registerEvents(events, this);
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

	public QueueManager getQM()
	{
		return this.qm;
	}

	public PartyManager getPAM()
	{
		return this.pam;
	}

	private void generateMessages()
	{
		File messagesFile = new File(getDataFolder(), "messages.yml");

		if (!messagesFile.exists())
		{
			messages = (messages1 = new NewYAML(messagesFile)).newYaml();
			BufferedReader input = new BufferedReader(new InputStreamReader(getClassLoader().getResourceAsStream("messages.yml")));
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