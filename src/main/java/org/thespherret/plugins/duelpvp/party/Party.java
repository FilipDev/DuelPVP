package org.thespherret.plugins.duelpvp.party;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.Main;
import org.thespherret.plugins.duelpvp.enums.Message;

import java.util.ArrayList;
import java.util.UUID;

public class Party {

	private final Main main;

	private final String name;
	private final ArrayList<UUID> playersInParty = new ArrayList<>();
	private UUID owner;

	public Party(Main main, String name, Player owner)
	{
		this.main = main;
		this.name = name;
		this.owner = owner.getUniqueId();
		addPlayer(owner);
	}

	public void addPlayer(Player player)
	{
		this.playersInParty.add(player.getUniqueId());
		main.getPAM().playersInParties.put(player.getUniqueId(), getName());
	}

	public void setOwner(Player player)
	{
		this.owner = player.getUniqueId();
	}

	public void removePlayer(UUID uuid)
	{
		this.playersInParty.remove(uuid);
		main.getPAM().playersInParties.remove(uuid);
	}

	public void kick(UUID uuid)
	{
		removePlayer(uuid);
		Player player;
		if ((player = Bukkit.getPlayer(uuid)) != null)
			player.sendMessage(Message.KICKED_FROM_PARTY.toString());
	}

	public boolean containsPlayer(Player player)
	{
		return getMembers().contains(player.getUniqueId());
	}

	public UUID getOwner()
	{
		return this.owner;
	}

	public String getName()
	{
		return this.name;
	}

	public boolean isOwner(UUID uuid)
	{
		return uuid != null && uuid.equals(getOwner());
	}

	public ArrayList<Player> getOnlineMembers()
	{
		ArrayList<Player> members = new ArrayList<>();
		for (UUID uuid : playersInParty){
			Player temp;
			if ((temp = Bukkit.getPlayer(uuid)) != null)
				members.add(temp);
		}
		return members;
	}

	public ArrayList<UUID> getMembers()
	{
		return this.playersInParty;
	}

	public void teleport(Location loc)
	{
		for (Player player : getOnlineMembers())
			player.teleport(loc);
	}

	public void broadcast(String message)
	{
		chat(null, message);
	}

	public void disband()
	{
		for (UUID uuid : getMembers())
			kick(uuid);
		main.getPAM().parties.remove(getName());
	}

	public void chat(Player player, String message)
	{
		for (Player player1 : getOnlineMembers())
			player1.sendMessage(format(player, message));
	}

	private String format(Player player, String message)
	{
		String name = (player == null) ? "" : player.getName();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(ChatColor.RED).append("(")
				.append(isOwner((player == null) ? null : player.getUniqueId()) ? ChatColor.WHITE : ChatColor.GRAY)
				.append(name)
				.append(ChatColor.RED)
				.append(")")
				.append(" ")
				.append(message);
		return stringBuilder.toString();
	}

}
