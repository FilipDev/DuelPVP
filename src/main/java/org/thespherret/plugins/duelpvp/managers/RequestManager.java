package org.thespherret.plugins.duelpvp.managers;

import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.Main;
import org.thespherret.plugins.duelpvp.Request;

import java.util.ArrayList;

public class RequestManager {

	public final ArrayList<Request> pendingRequests = new ArrayList<>();
	private final int requestTimeoutDelay;

	Main main;

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
}
