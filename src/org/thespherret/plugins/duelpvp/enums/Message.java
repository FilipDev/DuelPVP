/*
 * If this plugin is released, it means that whoever bought this plugin gave permission to distribute it. You can use this plugin use the code in this plugin to help you and other fellow plugin devs.
 */

package org.thespherret.plugins.duelpvp.enums;

import org.bukkit.ChatColor;
import org.thespherret.plugins.duelpvp.Main;

public enum Message {

	INITIALIZING(ChatColor.WHITE + "Initializing plugin."),
	MATCH_STARTING(ChatColor.AQUA + "Match starting in %v seconds."),
	BROADCAST_MATCH_START(ChatColor.LIGHT_PURPLE + "Game started with %v and %v!"),
	END_MATCH_REQUEST(ChatColor.DARK_GRAY + "%v requests to end the match."),
	END_MATCH_REVOCATION(ChatColor.DARK_GRAY + "%v revoked their request to end the match."),
	END_MATCH_TIP(ChatColor.GRAY + "Type " + ChatColor.RED + "/end" + ChatColor.GRAY + " to end it."),
	END_MATCH(ChatColor.LIGHT_PURPLE + "Both players have agreed to end the game."),
	END_MATCH_DEATH("You have %v the duel against %v."),
	FORCE_STARTED_MATCH("You have force started the match."),
	SET_LOBBY_POS(ChatColor.GRAY + "Set lobby position."),
	SET_ARENA_POS(ChatColor.GRAY + "Set spawn position " + ChatColor.RED + "%v" + ChatColor.GRAY + " in arena " + ChatColor.GOLD + "%v."),
	PARTNER_DISCONNECTED(ChatColor.RED + "Duel partner has disconnected."),
	SAVED_KIT(ChatColor.AQUA + "Saved kit %v."),
	LOADED_KIT(ChatColor.AQUA + "Loaded kit %v."),
	REQUEST_TIMEOUT(ChatColor.BLUE + "Duel request times out in %v seconds."),
	REQUEST_SENT(ChatColor.GREEN + "Duel request sent to %v"),
	RECIEVED_DUEL_REQUEST(ChatColor.BLUE + "%v challenges you to a duel! Type /accept or /deny!"),
	REQUEST_ACCEPT(ChatColor.AQUA + "Accepted duel request from %v."),
	REQUEST_ACCEPTED(ChatColor.AQUA + "%v has accepted the duel request!"),
	REQUEST_DENY(ChatColor.RED + "Denied duel request from %v"),
	REQUEST_DENIED(ChatColor.RED + "%v denied your duel request!"),
	REQUEST_TIMEDOUT(ChatColor.BLUE + "Duel request timed out."),
	;

	private String s;

	Message(String s)
	{
		this.s = s;
	}

	public String get()
	{
		return Main.prefix + s;
	}

	public String getF(String... strings)
	{
		String s1 = get();
		for (String s2 : strings)
			s1 = s1.replaceFirst("%v", s2);
		return s1;
	}

}