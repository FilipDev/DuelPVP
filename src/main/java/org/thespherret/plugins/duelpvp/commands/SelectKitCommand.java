package org.thespherret.plugins.duelpvp.commands;

import org.thespherret.plugins.duelpvp.enums.Error;

public class SelectKitCommand extends Command {

	public void execute()
	{
		if (args.length != 0)
			try{
				Integer kitNumber = Integer.parseInt(args[0]);
				cm.getMain().getPM().loadKit(p, kitNumber);
			}catch (Exception e){
				p.sendMessage(Error.DO_NOT_HAVE_KIT.toString());
			}
	}
}
