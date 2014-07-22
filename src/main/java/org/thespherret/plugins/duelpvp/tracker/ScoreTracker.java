package org.thespherret.plugins.duelpvp.tracker;

import code.husky.mysql.MySQL;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.thespherret.plugins.duelpvp.Main;
import org.thespherret.plugins.duelpvp.events.ArenaEndEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ScoreTracker implements Listener {

	final Main main;
	private MySQL mySQL;
	Connection c;

	public ScoreTracker(Main main) throws SQLException
	{
		this.main = main;
		this.setupMySQL();
	}

	@EventHandler
	public void onArenaEnd(ArenaEndEvent e)
	{
		Player p = e.getWinner();
		System.out.println(p.getName() + " has won");
	}

	public void setupMySQL() throws SQLException
	{
		//this.mySQL = new MySQL(main, main.getConfig().getString("host"), main.getConfig().getString("port"), main.getConfig().getString("name"), main.getConfig().getString("user"), main.getConfig().getString("pass"));
		//this.mySQL = new MySQL(main, "thespherret.org", "3306", "spherre2_new", "spherre2_testu", "testpass");
		this.mySQL = new MySQL(main, "localhost", "3306", "test", "test", "password");
		this.c = this.mySQL.openConnection();
		Statement statement = c.createStatement();
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS 'winlosslog' ('name' VARCHAR(32), 'wins' INT, 'losses' INT);");
		statement.close();
	}

	public void setLosses(Player p) throws SQLException
	{
		if (!this.mySQL.checkConnection())
			this.mySQL.openConnection();
		String name = p.getName().toLowerCase();

		int wins = getWins(p);
		Statement statement = c.createStatement();
		if (wins == 0)
			statement.executeUpdate("INSERT INTO 'winlosslog' ('name', 'losses') VALUES ('" + name + "', '" + 1 + "');");
		else
			statement.executeUpdate("UPDATE 'winlosslog' SET 'losses' = " + wins + 1 + "WHERE 'name' = " + name);
		statement.close();
	}

	public void setWins(Player p) throws SQLException
	{
		if (!this.mySQL.checkConnection())
			this.mySQL.openConnection();
		String name = p.getName().toLowerCase();

		int wins = getWins(p);
		Statement statement = c.createStatement();
		if (wins == 0)
			statement.executeUpdate("INSERT INTO 'winlosslog' ('name', 'wins') VALUES ('" + name + "', '" + 1 + "');");
		else
			statement.executeUpdate("UPDATE 'winlosslog' SET 'wins' = " + wins + 1 + "WHERE 'name' = " + name);
		statement.close();
	}

	public int getWins(Player p) throws SQLException
	{
		if (!this.mySQL.checkConnection())
			this.mySQL.openConnection();
		String name = p.getName().toLowerCase();
		Statement statement = c.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT wins FROM 'winlosslog' WHERE name = " + name + ";");
		statement.close();

		if (!resultSet.next())
			return 0;

		return resultSet.getInt("wins");
	}

	public int getLosses(Player p) throws SQLException
	{
		if (!this.mySQL.checkConnection())
			this.mySQL.openConnection();
		String name = p.getName().toLowerCase();
		Statement statement = c.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT losses FROM winlosslog WHERE name = " + name);
		statement.close();

		if (!resultSet.next())
			return 0;

		return resultSet.getInt("losses");
	}

	public void closeMySQL()
	{
		this.mySQL.closeConnection();
	}

}
