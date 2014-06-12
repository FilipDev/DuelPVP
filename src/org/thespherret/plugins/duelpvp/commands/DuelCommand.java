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
import java.util.Random;

public class DuelCommand implements Command {
	@Override
	public boolean execute(final CommandManager cm, CommandSender sender, String[] args) {
		Player p = (Player) sender;
		if (args.length == 1)
		{
			if (args[0].equalsIgnoreCase("accept"))
			{
				Request request = cm.getMain().getRM().getRequest(p);
				if (request != null)
				{
					Player attacker = Bukkit.getPlayer(request.getAttackerString());
					if (attacker.isOnline())
					{
						p.sendMessage(Main.prefix + Message.REQUEST_ACCEPTED.getF(attacker.getName()));
						attacker.sendMessage(Main.prefix + Message.REQUEST_ACCEPT.get());
						request.getArena().addPlayers(p.getName(), attacker.getName());
					}
					else
						p.sendMessage(Main.prefix + Message.PARTNER_DISCONNECTED.get());
					cm.getMain().getRM().pendingRequests.remove(request);
					return true;
				}
				p.sendMessage(Main.prefix + Error.NO_PENDING_DUEL_REQUEST.get());
				return true;
			}
			if (args[0].equalsIgnoreCase("deny"))
			{
				Request request = null;
				for (Request request0 : cm.getMain().getRM().pendingRequests) {
					if (request0.getDefenderString().equals(p.getName()))
					{
						request = request0;
						break;
					}
				}
				if (request != null)
				{
					Player attacker = Bukkit.getPlayer(request.getAttackerString());
					if (attacker.isOnline())
					{
						p.sendMessage(Main.prefix + Message.REQUEST_DENIED.getF(attacker.getName()));
						attacker.sendMessage(Main.prefix + Message.REQUEST_DENY.get());
					}
					cm.getMain().getRM().pendingRequests.remove(request);
					return true;
				}
				p.sendMessage(Main.prefix + Error.NO_PENDING_DUEL_REQUEST.get());
				return true;
			}
			Player dueled;
			if ((dueled = Bukkit.getPlayer(args[0])) != null)
			{
				if (!dueled.getName().equals(p.getName()))
				{
					Request request0 = null;
					for (Request request1 : cm.getMain().getRM().pendingRequests) {
						if (request1.getDefenderString().equals(dueled.getName()))
						{
							request0 = request1;
							break;
						}
					}
					if ((request0 == null) && (cm.getMain().getAM().getArena(dueled) == null))
					{
						ArrayList<String> unoccupiedArenas = new ArrayList();
						for (Arena arena : cm.getMain().getAM().activeArenas.values()) 
							if (!arena.isOccupied())
								unoccupiedArenas.add(arena.getArenaName());
						Arena arena = cm.getMain().getAM().getArena(unoccupiedArenas.get(new Random().nextInt(unoccupiedArenas.size())));
						if (arena != null)
						{
							int rtd = cm.getMain().getRM().getRequestTimeoutDelay();
							final Request request = new Request(cm.getMain().getRM(), arena, dueled.getName(), p.getName());
							cm.getMain().getRM().pendingRequests.add(request);
							dueled.sendMessage(Main.prefix + Message.RECIEVED_DUEL_REQUEST.getF(p.getName()));
							p.sendMessage(Main.prefix + Message.REQUEST_SENT.getF(dueled.getName()));
							p.sendMessage(Main.prefix + Message.REQUEST_TIMEOUT.getF(rtd + ""));
							dueled.sendMessage(Main.prefix + Message.REQUEST_TIMEOUT.getF(rtd + ""));
							Bukkit.getScheduler().scheduleSyncDelayedTask(cm.getMain(), new Runnable()
							{
								public void run()
								{
									for (Request r : cm.getMain().getRM().pendingRequests)
										if (r.equals(request))
											request.cancel();
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
				p.sendMessage(Error.CANNOT_FIND_PLAYER.get());
		}
		return true;
	}
}
