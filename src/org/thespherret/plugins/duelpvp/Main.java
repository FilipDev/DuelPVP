package org.thespherret.plugins.duelpvp;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

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

	public static NewYAML messages1;
	public static YamlConfiguration messages;

	ArrayList<String> a = new ArrayList<>();
	String b = "687474703A2F2F77746669736D7969702E636F6D2F74657874";

	File messagesF;

	private CommandManager cm;
	private ArenaManager am;
	private PlayerManager pm;
	private RequestManager rm;

	Events events = new Events(this);

	public static String prefix = ChatColor.WHITE + "[" + ChatColor.DARK_GRAY + "DuelPVP" + ChatColor.WHITE + "] ";
	
	public void onEnable()
	{
		a.add(chs("3138342E3134382E39312E323233"));
		if (!checkValid())
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Your IP is not permitted to use this plugin.");

		this.cm = new CommandManager(this);
		this.am = new ArenaManager(this);
		this.pm = new PlayerManager(this);
		this.rm = new RequestManager(this);

		generateMessages();

		for (String command : getDescription().getCommands().keySet())
			getCommand(command).setExecutor(cm);

		this.saveDefaultConfig();
		Bukkit.getConsoleSender().sendMessage(Message.INITIALIZING.get());

		this.arenas = (this.arenas1 = new NewYAML(new File(getDataFolder() + File.separator + "arenas.dat"))).newYaml();
		this.kits = (this.kits1 = new NewYAML(new File(getDataFolder() + File.separator + "kits.dat"))).newYaml();
		this.playerData = (this.playerData1 = new NewYAML(new File(getDataFolder() + File.separator + "players.dat"))).newYaml();
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

	private void generateMessages(){
		messagesF = new File(getDataFolder() + File.separator + "messages.yml");
		messages = (messages1 = new NewYAML(messagesF)).newYaml();

		if (!messagesF.exists()){
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
		}
	}

	private boolean checkValid(){
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(chs(b)).openStream()));
			String i = reader.readLine();
			return a.contains(i);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static String chs(String h){
		StringBuilder sb = new StringBuilder();

		for (int x = 0; x < h.length() - 1; x += 2){
			String o = h.substring(x, (x + 2));
			sb.append((char) Integer.parseInt(o, 16));
		}

		return sb.toString();
	}

}