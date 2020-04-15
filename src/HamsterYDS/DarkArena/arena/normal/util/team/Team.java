package HamsterYDS.DarkArena.arena.normal.util.team;

import java.util.List;

import org.bukkit.Location;

import HamsterYDS.DarkArena.arena.normal.util.buildings.CentreCrystal;
import HamsterYDS.DarkArena.arena.normal.util.buildings.DefenseTower;

public class Team {
	public static final int maxPlayer=5;
	protected int players;
	protected Location spawnPoint;
	protected CentreCrystal centreCrystal;
	protected List<DefenseTower> defenseTowers;
	
	public Team(int players, Location spawnPoint, CentreCrystal centreCrystal, List<DefenseTower> defenseTowers) {
		this.players=players;
		this.spawnPoint = spawnPoint;
		this.centreCrystal = centreCrystal;
		this.defenseTowers = defenseTowers;
	}
	public int getPlayers() {
		return players;
	}
	public Location getSpawnPoint() {
		return spawnPoint;
	}
	public CentreCrystal getCentreCrystal() {
		return centreCrystal;
	}
	public List<DefenseTower> getDefenseTowers() {
		return defenseTowers;
	}
}
