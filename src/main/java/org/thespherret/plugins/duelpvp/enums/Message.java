package org.thespherret.plugins.duelpvp.enums;

import org.bukkit.ChatColor;
import org.thespherret.plugins.duelpvp.Main;

public enum Message {

	INITIALIZING,
	MATCH_STARTING,
	BROADCAST_MATCH_START,
	END_MATCH_REQUEST,
	END_MATCH_REVOCATION,
	END_MATCH_TIP,
	END_MATCH,
	END_MATCH_DEATH,
	FORCE_STARTED_MATCH,
	SET_LOBBY_POS,
	SET_ARENA_POS,
	PARTNER_DISCONNECTED,
	SAVED_KIT,
	LOADED_KIT,
	REQUEST_TIMEOUT,
	REQUEST_SENT,
	RECIEVED_DUEL_REQUEST,
	REQUEST_ACCEPT,
	REQUEST_ACCEPTED,
	REQUEST_DENY,
	REQUEST_DENIED,
	REQUEST_TIMEDOUT,
	ADDED_TO_QUEUE,
	KICKED_FROM_PARTY,
	;

	private String message;

	private Message()
	{
		this.message = ChatColor.translateAlternateColorCodes('&', Main.PREFIX + Main.getMain().messages.getString(name()));
	}

	@Override
	public String toString()
	{
		return message;
	}

	public String getFormatted(Object... strings)
	{
		String s1 = toString();
		for (Object s2 : strings)
			s1 = s1.replaceFirst("%v", s2.toString());
		return s1;
	}

}