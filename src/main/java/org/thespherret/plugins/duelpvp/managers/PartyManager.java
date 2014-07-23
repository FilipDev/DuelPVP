package org.thespherret.plugins.duelpvp.managers;

import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.Main;
import org.thespherret.plugins.duelpvp.party.Party;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Administrator on 7/23/14.
 */
public class PartyManager {

	private final Main main;

	public final HashMap<String, Party> parties = new HashMap<>();
	public final HashMap<UUID, String> playersInParties = new HashMap<>();

	public PartyManager(Main main)
	{
		this.main = main;
	}

	public Party getParty(String name)
	{
		return parties.get(name);
	}

	public Party getParty(Player player)
	{
		return parties.get(playersInParties.get(player.getUniqueId()));
	}

	public void createParty(Party party)
	{
		if (parties.containsKey(party.getName()))
			main.unsafe.throwException(new Exception("Party already exists."));
		this.parties.put(party.getName(), party);
		this.playersInParties.put(party.getOwner(), party.getName());
	}

}
