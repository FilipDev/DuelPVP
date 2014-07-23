package org.thespherret.plugins.duelpvp.managers;

import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.Main;
import org.thespherret.plugins.duelpvp.Request;

import java.util.ArrayList;

public class QueueManager {

	final Main main;

	ArrayList<String> players = new ArrayList<>();

	public QueueManager(Main main)
	{
		this.main = main;
	}

	public Request createRequest(Player player)
	{
		/*if (this.player1 == null)
		{
			this.player1 = player;
			return null;
		}
		else
		{
			this.player1 = null;
			return new Request(main.getRM(), main.getAM().getRandomArena(), player.getUniqueId(), this.player1.getUniqueId());
		}*/
		return null;
	}

}
