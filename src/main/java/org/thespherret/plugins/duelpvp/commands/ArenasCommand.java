package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.Arena;
import org.thespherret.plugins.duelpvp.managers.CommandManager;

import java.util.HashMap;

public class ArenasCommand implements Command {

	@Override
	public boolean execute(CommandManager cm, Player p, String[] args)
	{
		p.sendMessage(ChatColor.DARK_GRAY + "===" + ChatColor.AQUA + "1v1 Arenas" + ChatColor.DARK_GRAY + "===");
		HashMap<Integer, String> stringGroups = new HashMap<>();
		int arenasPerLine = 1, line = 0, displayNumber = 1;
		for (Arena arena : cm.getMain().getAM().activeArenas.values()) {
			String curSt = stringGroups.get(line);
			if (curSt == null){
				stringGroups.put(line, displayNumber + ". " + (arena.isOccupied() ? ChatColor.RED : arena.isEnabled() ? ChatColor.GREEN : ChatColor.DARK_GRAY) + arena.getArenaName());
			}else
				stringGroups.put(line, curSt + ChatColor.RESET + ", " + displayNumber + ". " + (arena.isOccupied() ? ChatColor.RED : arena.isEnabled() ? ChatColor.GREEN : ChatColor.DARK_GRAY) + arena.getArenaName());
			if (arenasPerLine == 3){
				line++;
				arenasPerLine = 0;
			}
			arenasPerLine++;
			displayNumber++;
		}
		for (int z = 0; z <= stringGroups.size() - 1; z++){
			p.sendMessage(stringGroups.get(z));
			if (!(z == stringGroups.size() - 1))
				p.sendMessage("");
		}
		return true;
	}
}