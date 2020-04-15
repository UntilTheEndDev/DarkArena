package HamsterYDS.DarkArena.arena.normal.util.team;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import HamsterYDS.DarkArena.arena.normal.runner.ArenaLoader;
import HamsterYDS.DarkArena.arena.normal.util.buildings.CentreCrystal;
import HamsterYDS.DarkArena.arena.normal.util.buildings.DefenseTower;

public class Team {
	public static final int maxPlayer=5;
	protected int players;
	protected Location spawnPoint;
	protected CentreCrystal centreCrystal;
	protected List<DefenseTower> defenseTowers;
	
	public Team(String name) {
		this.spawnPoint = ArenaLoader.loadLocation(name+".spawnPoint");
		this.centreCrystal = new CentreCrystal(name);
		
		List<DefenseTower> defenseTowers=new ArrayList<DefenseTower>();
		defenseTowers.add(new DefenseTower(name,1));
		defenseTowers.add(new DefenseTower(name,2));
		defenseTowers.add(new DefenseTower(name,3));
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
