package HamsterYDS.DarkArena.arena.normal.util;

import java.util.HashMap;

import HamsterYDS.DarkArena.arena.normal.util.team.Team;
import HamsterYDS.DarkArena.arena.normal.util.team.TeamPlayer;

public class Arena {
	protected String name;
	protected Team red;
	protected Team blue;
	protected HashMap<String,TeamPlayer> players=new HashMap<String,TeamPlayer>();
	public Arena(String name,Team red,Team blue,HashMap<String,TeamPlayer> players) {
		this.name=name;
		this.red=red;
		this.blue=blue; 
		this.players=players;
	}
	public String getName() {
		return name;
	}
	public Team getRed() {
		return red;
	}
	public Team getBlue() {
		return blue;
	}
	public HashMap<String, TeamPlayer> getPlayers() {
		return players;
	}
}
