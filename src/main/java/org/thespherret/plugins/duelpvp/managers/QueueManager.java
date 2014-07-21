package org.thespherret.plugins.duelpvp.managers;

import org.bukkit.entity.Player;
import org.thespherret.plugins.duelpvp.Main;
import org.thespherret.plugins.duelpvp.Request;

public class QueueManager {

	Main main;

	Player player1;

	public QueueManager(Main main)
	{
		this.main = main;
	}

	public Request createRequest(Player player)
	{
		if (this.player1 == null){
			this.player1 = player;
			return null;
		}else{
			this.player1 = null;
			return new Request(main.getRM(), main.getAM().getRandomArena(), player.getUniqueId(), this.player1.getUniqueId());
		}
	}

}
