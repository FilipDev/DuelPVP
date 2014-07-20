package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.enums.Error;
import org.thespherret.plugins.duelpvp.managers.CommandManager;

public class SelectKitCommand implements Command {

	@Override
	public boolean execute(CommandManager cm, Player p, String[] args) {
		if (args.length != 0){

			try{
				Integer kitNumber = Integer.parseInt(args[0]);
				cm.getMain().getPM().loadKit(p, kitNumber);
			}catch (Exception e){
				p.sendMessage(Error.DO_NOT_HAVE_KIT.toString());
			}
		}

		return true;
	}
}
