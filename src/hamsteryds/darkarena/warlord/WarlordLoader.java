package hamsteryds.darkarena.warlord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import hamsteryds.darkarena.DarkArena;
import hamsteryds.darkarena.warlord.util.WarlordArena;
import hamsteryds.darkarena.warlord.util.WarlordPlayer;
import hamsteryds.darkarena.warlord.util.WarlordTeam;

public class WarlordLoader extends WarlordManager{
	public static void loadConfigWarlordArenas() {
		DarkArena.instance.saveResource("warlord.yml", false);
		hubLocation = loadLocation("hub");
		for (String path : arenaLoader.getKeys(true)) {
			if (path.startsWith("arenas.")) {
				String tmp = path.replace("arenas.", "");
				if (tmp.contains("."))
					continue;
				loadConfigWarlordArena(tmp);
			}
		}
	}

	public static void loadConfigWarlordArena(String arenaId) {
		arenas.remove(arenaId);
		players.remove(arenaId);
		teams.remove(arenaId);
		arenas.put(arenaId, new WarlordArena(arenaId, arenaLoader.getLong("arenas." + arenaId + ".maxPeriod"),
				arenaLoader.getInt("arenas." + arenaId + ".maxPlayer")));
		List<WarlordTeam> initTeams = new ArrayList<WarlordTeam>();
		initTeams.add(new WarlordTeam(loadLocation("arenas." + arenaId + ".spawnpoint.red"), 0));
		initTeams.add(new WarlordTeam(loadLocation("arenas." + arenaId + ".spawnpoint.blue"), 0));
		teams.put(arenaId, initTeams);
		players.put(arenaId, new HashMap<UUID, WarlordPlayer>());
	}

	public static Location loadLocation(String path) {
		World world = Bukkit.getWorld(arenaLoader.getString(path + ".world"));
		int x = arenaLoader.getInt(path + ".x");
		int y = arenaLoader.getInt(path + ".y");
		int z = arenaLoader.getInt(path + ".z");
		Location loc = new Location(world, x, y, z);
		return loc;
	}
}
