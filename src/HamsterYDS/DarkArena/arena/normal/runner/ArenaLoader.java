package HamsterYDS.DarkArena.arena.normal.runner;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import HamsterYDS.DarkArena.DarkArena;
import HamsterYDS.DarkArena.arena.normal.util.Arena;
import HamsterYDS.DarkArena.arena.normal.util.buildings.CentreCrystal;
import HamsterYDS.DarkArena.arena.normal.util.team.Team;
import HamsterYDS.DarkArena.arena.normal.util.team.TeamPlayer;

public class ArenaLoader {
	public static final File arenaFile=new File(DarkArena.instance.getDataFolder(),"arenas.yml");
	public static final YamlConfiguration arenaConfig=YamlConfiguration.loadConfiguration(arenaFile);
	public static boolean runNormalArena(String name,HashMap<String,TeamPlayer> teamPlayers) {
		if(arenaConfig.getBoolean(name+".running"))
			return false;
		arenaConfig.set(name+".running",false);
		
		Team red=new Team(5, loadLocation(name+".team.red.spawnPoint"), 
						  new CentreCrystal(loadLocation(name+".team.red.crystalPoint")), 
						  null);
		Team blue=new Team(5, loadLocation(name+".team.blue.spawnPoint"), 
				          new CentreCrystal(loadLocation(name+".team.blue.crystalPoint")), 
				          null);
		
		Arena arena=new Arena(name, red, blue, teamPlayers); 
		return true;
	}
	private static Location loadLocation(String string) {
		// TODO Auto-generated method stub
		return null;
	}
}
