/*
 * If this plugin is released, it means that whoever bought this plugin gave permission to distribute it. You can use this plugin use the code in this plugin to help you and other fellow plugin devs.
 */

package org.thespherret.plugins.duelpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.Arena;
import org.thespherret.plugins.duelpvp.enums.Message;
import org.thespherret.plugins.duelpvp.enums.Error;
import org.thespherret.plugins.duelpvp.managers.CommandManager;
import org.thespherret.plugins.duelpvp.Main;
import org.thespherret.plugins.duelpvp.Request;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;

public class DuelCommand implements Command {
	@Override
	public boolean execute(final CommandManager cm, CommandSender sender, String[] args)
	{
		Player p = (Player) sender;
		if (args.length == 1)
		{
			Player dueled;
			if ((dueled = Bukkit.getPlayer(args[0])) != null)
			{
				if (!dueled.getName().equals(p.getName()))
				{
					Request request0 = cm.getMain().getRM().getRequest(p);
					if ((request0 == null) && (cm.getMain().getAM().getArena(dueled) == null))
					{
						Arena arena = cm.getMain().getAM().getRandomArena();
						if (arena != null)
						{
							int rtd = cm.getMain().getRM().getRequestTimeoutDelay();
							final Request request = new Request(cm.getMain().getRM(), arena, dueled.getName(), p.getName());
							cm.getMain().getRM().pendingRequests.add(request);
							dueled.sendMessage( Message.RECIEVED_DUEL_REQUEST.getF(p.getName()));
							p.sendMessage(Message.REQUEST_SENT.getF(dueled.getName()));
							p.sendMessage(Message.REQUEST_TIMEOUT.getF(rtd + ""));
							dueled.sendMessage(Message.REQUEST_TIMEOUT.getF(rtd + ""));
							Bukkit.getScheduler().scheduleSyncDelayedTask(cm.getMain(), new Runnable()
							{
								public void run()
								{
									try{
										for (Request r : cm.getMain().getRM().pendingRequests)
											if (r.equals(request))
												request.cancel();
									}catch (ConcurrentModificationException e){}
								}
							}, cm.getMain().getRM().getRequestTimeoutDelay() * 20);
						}
						else
							p.sendMessage(Error.ARENA_OCCUPIED.get());
					}
					else
						p.sendMessage(Error.ALREADY_HAS_DUEL_REQUEST.get());
				}
				else
					p.sendMessage(Error.CANNOT_DUEL_YOURSELF.get());
			}
			else
				p.sendMessage(Error.CANNOT_FIND_PLAYER.getF(args[0]));
		}
		else
			p.sendMessage(Error.INCORRECT_USAGE.get());
		return true;
	}
}
