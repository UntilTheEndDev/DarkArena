package HamsterYDS.DarkArena.arena.normal.runner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import HamsterYDS.DarkArena.DarkArena;
import HamsterYDS.DarkArena.arena.normal.util.Arena;
import HamsterYDS.DarkArena.arena.normal.util.team.Team;
import HamsterYDS.DarkArena.arena.normal.util.team.TeamPlayer;

public class ArenaLoader {
	public static final File arenaFile=new File(DarkArena.instance.getDataFolder(),"arenas.yml");
	public static final YamlConfiguration arenaConfig=YamlConfiguration.loadConfiguration(arenaFile);
	public static List<ArenaRunner> runningGames=new ArrayList<ArenaRunner>();
	public static boolean runNormalArena(String name,HashMap<String,TeamPlayer> teamPlayers) {
		if(arenaConfig.getBoolean(name+".running"))
			return false;
		arenaConfig.set(name+".running",false);
		Arena arena=new Arena(name, new Team(name+".team.red"), new Team(name+".team.blue"), teamPlayers); 
		runningGames.add(new ArenaRunner(arena));
		return true;
	} 
	public static Location loadLocation(String string) {
		String toString=arenaConfig.getString(string);
		try {
	        StringBuilder world = new StringBuilder();
	        StringBuilder x = new StringBuilder();
	        StringBuilder y = new StringBuilder();
	        StringBuilder z = new StringBuilder();
	        int tot = 0;
	        for (int i = 0; i < toString.toCharArray().length; i++) {
	            char ch = toString.toCharArray()[i];
	            if (ch == '-') {
	                tot++;
	                if (toString.toCharArray()[i + 1] == '-')
	                    tot--;
	                continue;
	            }
	            if (tot == 0) world.append(ch);
	            if (tot == 1) x.append(ch);
	            if (tot == 2) y.append(ch);
	            if (tot == 3) z.append(ch);
	        }
	        return new Location(Bukkit.getWorld(world.toString()), Integer.parseInt(x.toString()), Integer.parseInt(y.toString()), Integer.parseInt(z.toString()));
	    } catch (Exception e) {
	        return null;
	    }
	}
}
