package org.thespherret.plugins.duelpvp.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.Main;
import org.thespherret.plugins.duelpvp.Request;
import org.thespherret.plugins.duelpvp.enums.Message;

import java.util.ArrayList;

public class RequestManager {

	public final ArrayList<Request> pendingRequests = new ArrayList<>();
	private final int requestTimeoutDelay;

	final Main main;

	public RequestManager(Main main)
	{
		this.main = main;
		this.requestTimeoutDelay = main.getConfig().getInt("request-timeout-delay");
	}

	public Request getRequest(Player player)
	{
		Request request = null;
		for (Request request0 : pendingRequests)
			if (request0.getDefenderUUID().equals(player.getUniqueId())){
				request = request0;
				break;
			}
		return request;
	}

	public int getRequestTimeoutDelay()
	{
		return requestTimeoutDelay;
	}

	public void sendRequest(final Request request)
	{
		Player sender = Bukkit.getPlayer(request.getAttackerUUID());
		Player reciever = Bukkit.getPlayer(request.getDefenderUUID());
		pendingRequests.add(request);
		reciever.sendMessage(Message.RECIEVED_DUEL_REQUEST.getFormatted(sender.getName()));
		sender.sendMessage(Message.REQUEST_SENT.getFormatted(reciever.getName()));
		sender.sendMessage(Message.REQUEST_TIMEOUT.getFormatted(getRequestTimeoutDelay()));
		reciever.sendMessage(Message.REQUEST_TIMEOUT.getFormatted(getRequestTimeoutDelay()));
		Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
			public void run() {
				if (pendingRequests.contains(request))
					request.cancel();
			}
		}, getRequestTimeoutDelay() * 20);
	}
}
