package org.thespherret.plugins.duelpvp.managers;

import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.Main;
import org.thespherret.plugins.duelpvp.Request;

import java.util.ArrayList;

public class RequestManager {

	public ArrayList<Request> pendingRequests = new ArrayList<>();
	private int requestTimeoutDelay;

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
			if (request0.getDefenderString().equals(player.getName())){
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
