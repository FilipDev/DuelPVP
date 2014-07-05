/*
 * If this plugin is released, it means that whoever bought this plugin gave permission to distribute it. You can use this plugin use the code in this plugin to help you and other fellow plugin devs.
 */

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
	;

	public String get()
	{
		return ChatColor.translateAlternateColorCodes('&', Main.PREFIX + Main.messages.getString(this.name()));
	}

	public String getF(Object... strings)
	{
		String s1 = get();
		for (Object s2 : strings)
			s1 = s1.replaceFirst("%v", s2.toString());
		return s1;
	}

}