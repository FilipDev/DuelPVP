package org.thespherret.plugins.duelpvp.enums;

import org.bukkit.ChatColor;

public enum Error {

	CANNOT_DUEL_YOURSELF("Cannot duel yourself."),
	KIT_NUMBER_MUST_BE_SPECIFIED("Kit number must be specified on the second line."),
	DO_NOT_HAVE_KIT("You do not have this kit."),
	WAIT_BETWEEN_SIGN_CLICK("Wait one second before clicking the sign again."),
	COULD_NOT_SAVE_KIT("Could not save kit."),
	LOAD_KIT_NOT_IN_MATCH("Must be in match to use kit signs!"),
	ALREADY_HAS_DUEL_REQUEST("Player has a duel request."),
	NO_PENDING_DUEL_REQUEST("No pending duel request."),
	TOO_MANY_SPAWN_POINTS("Cannot create more than 2 spawn points in a duel arena."),
	NO_COMMAND_PERMISSION("No permission to run this command."),
	CANNOT_MODIFY_BLOCKS("You cannot modify blocks while in match."),
	NOT_IN_STARTED_ARENA("You are not in a started match."),
	NOT_IN_ARENA("Not in an arena."),
	CANNOT_FIND_PLAYER("Cannot find player %v."),
	INCORRECT_USAGE("Incorrect usage."),
	ARENA_NOT_STARTED("Arena not started."),
	SPAWN_POINT_NOT_SET("Spawn point not set."),
	ARENA_OCCUPIED("All arenas are currently occupied."),
	CANNOT_TELEPORT_IN_ARENA("Cannot teleport when inside a started arena."),
	CANNOT_USE_COMMAND_IN_MATCH("Cannot use this command while in a match."),
	CONSOLE_CANNOT_SEND("Console cannot use DuelPVP commands."),
	;

	private String error;

	Error(String error)
	{
		this.error = error;
	}

	public String toString()
	{
		return ChatColor.RED + "Error: " + ChatColor.DARK_RED + this.error;
	}

	public String getFormatted(String... strings)
	{
		String s1 = toString();
		for (String s2 : strings)
			s1 = s1.replaceFirst("%v", ChatColor.RED + s2 + ChatColor.DARK_RED);
		return s1;
	}
}
