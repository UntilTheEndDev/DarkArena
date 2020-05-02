package hamsteryds.darkarena.warlord;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import hamsteryds.darkarena.DarkArena;
import hamsteryds.darkarena.warlord.task.CompassTargeter;
import hamsteryds.darkarena.warlord.task.FlagGlowingSetter;
import hamsteryds.darkarena.warlord.task.LastTimeCounter;
import hamsteryds.darkarena.warlord.task.ScaleBalancer;
import hamsteryds.darkarena.warlord.util.WarlordArena;
import hamsteryds.darkarena.warlord.util.WarlordPlayer;
import hamsteryds.darkarena.warlord.util.WarlordTeam;

public class WarlordManager {
	public static HashMap<String, WarlordArena> arenas = new HashMap<String, WarlordArena>();
	public static HashMap<String, HashMap<UUID, WarlordPlayer>> players = new HashMap<String, HashMap<UUID, WarlordPlayer>>();
	public static HashMap<String, List<WarlordTeam>> teams = new HashMap<String, List<WarlordTeam>>();
	public static File arenaFile = new File(DarkArena.instance.getDataFolder(), "warlord.yml");
	public static YamlConfiguration arenaLoader = YamlConfiguration.loadConfiguration(arenaFile);

	public static void loadConfigWarlordArenas() {
		DarkArena.instance.saveResource("warlord.yml", false);
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

	private static HashMap<String, Boolean> isCounting = new HashMap<String, Boolean>();

	public static void quitArena(Player player, String arenaId) {
		players.get(arenaId).remove(player.getUniqueId());
	}

	public static boolean startArena(String arenaId) {
		if (!arenas.get(arenaId).isWaiting)
			return false;
		int maxPlayers = arenas.get(arenaId).maxPlayer;
		HashMap<UUID, WarlordPlayer> playerMap = players.get(arenaId);
		arenas.get(arenaId).isWaiting = false;
		arenas.get(arenaId).isRunning = true;
		for (UUID uuid : playerMap.keySet()) {
			Player player = Bukkit.getPlayer(uuid);
			if (player == null)
				playerMap.remove(uuid);
			else {
				player.teleport(playerMap.get(uuid).team.spawnLocation);
				player.sendMessage("§6[战争领主]§r比赛开始");
			}
		}
		teams.get(arenaId).get(0).currentFlagLocation.getBlock().setType(Material.BEACON);
		teams.get(arenaId).get(1).currentFlagLocation.getBlock().setType(Material.BEACON);
		new FlagGlowingSetter(arenaId);
		new LastTimeCounter(arenaId);
		new ScaleBalancer(arenaId);
		new CompassTargeter(arenaId);
		return true;
	}

	public static void stopArena(String arenaId) {
		List<WarlordTeam> currentTeams = teams.get(arenaId);
		HashMap<UUID, WarlordPlayer> currentPlayers = players.get(arenaId);
		List<UUID> winners = new ArrayList<UUID>();
		List<UUID> losers = new ArrayList<UUID>();
		int score1 = currentTeams.get(0).currentScore;
		int score2 = currentTeams.get(1).currentScore;
		UUID ATKMVP=null, CureMVP=null;
		int atk = 0, cure = 0;
		for (UUID uuid : currentPlayers.keySet()) {
			WarlordPlayer pl = currentPlayers.get(uuid);
			if (pl.totalATK > atk) {
				atk = pl.totalATK;
				ATKMVP = uuid;
			}
			if (pl.totalCure > cure) {
				cure = pl.totalCure;
				CureMVP = uuid;
			}
			StatsManager.playerDatas.get(uuid).totalMatch++;
			StatsManager.playerDatas.get(uuid).totalATK += pl.totalATK;
			StatsManager.playerDatas.get(uuid).totalCure += pl.totalCure;
			if (pl.team == currentTeams.get(0))
				winners.add(uuid);
			if (pl.team == currentTeams.get(1))
				losers.add(uuid);
		}
		if (ATKMVP != null)
			StatsManager.playerDatas.get(ATKMVP).totalATKMVP++;
		if (CureMVP != null)
			StatsManager.playerDatas.get(CureMVP).totalCureMVP++;
		boolean flag = false;
		if (score1 < score2)
			flag = true;
		if (flag) {
			List<UUID> tmp = new ArrayList<UUID>(winners);
			winners.clear();
			winners.addAll(losers);
			losers.clear();
			losers.addAll(tmp);
		}
		prideWinner(winners);
		prideLoser(losers);

		loadConfigWarlordArena(arenaId);
	}

	public static void prideLoser(List<UUID> losers) {
		for (UUID uuid : losers) {
			Player player = Bukkit.getPlayer(uuid);
			player.sendMessage("§6[战争领主]§r您的队伍失败！");
		}
	}

	public static void prideWinner(List<UUID> winners) {
		for (UUID uuid : winners) {
			StatsManager.playerDatas.get(uuid).totalVictory++;
			Player player = Bukkit.getPlayer(uuid);
			player.sendMessage("§6[战争领主]§r您的队伍获胜！");
		}
	}
}
