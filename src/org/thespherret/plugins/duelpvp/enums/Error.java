/*
 * If this plugin is released, it means that whoever bought this plugin gave permission to distribute it. You can use this plugin use the code in this plugin to help you and other fellow plugin devs.
 */

package org.thespherret.plugins.duelpvp.enums;

import org.bukkit.ChatColor;

public enum Error {

	CANNOT_DUEL_YOURSELF("You cannot duel yourself."),
	KIT_NUMBER_MUST_BE_SPECIFIED("Kit number must be specified on the second line."),
	WAIT_BETWEEN_SIGN_CLICK("Wait one second before clicking the sign again."),
	COULD_NOT_SAVE_KIT("Could not save kit."),
	LOAD_KIT_NOT_IN_MATCH("You must be in a match to use kit signs!"),
	ALREADY_HAS_DUEL_REQUEST("Player already has a duel request."),
	NO_PENDING_DUEL_REQUEST("No pending duel request."),
	TOO_MANY_SPAWN_POINTS("You may not create more than 2 spawn points in a duel arena."),
	NO_COMMAND_PERMISSION("You do not have permission to run this command."),
	CANNOT_MODIFY_BLOCKS("You cannot modify blocks while in match."),
	NOT_IN_STARTED_ARENA("You are not in a started match."),
	NOT_IN_ARENA("You are not in an arena."),
	CANNOT_FIND_PLAYER("Cannot find player %v."),
	INCORRECT_USAGE("Incorrect usage."),
	ARENA_NOT_STARTED("Arena not started."),
	SPAWN_POINT_NOT_SET("Spawn point not set."),
	ARENA_OCCUPIED("All arenas are currently occupied."),
	CANNOT_TELEPORT_IN_ARENA("You cannot teleport when inside a started arena."),
	;

	private String s;

	Error(String s){
		this.s = s;
	}

	public String get(){
		return ChatColor.RED + "Error: " + ChatColor.DARK_RED + this.s;
	}

	public String getF(String... strings){
		String s1 = get();
		for (String s2 : strings)
			s1 = s1.replaceFirst("%v", ChatColor.RED + s2 + ChatColor.DARK_RED);
		return s1;
	}
}
