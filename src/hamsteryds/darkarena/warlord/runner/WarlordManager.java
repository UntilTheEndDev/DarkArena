package hamsteryds.darkarena.warlord.runner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import hamsteryds.darkarena.DarkArena;
import hamsteryds.darkarena.warlord.util.WarlordArena;
import hamsteryds.darkarena.warlord.util.WarlordPlayer;
import hamsteryds.darkarena.warlord.util.WarlordTeam;
import hamsteryds.darkarena.warlord.util.WarlordTeam.TeamType;

public class WarlordManager {
	public static HashMap<String, WarlordArena> arenas = new HashMap<String, WarlordArena>();
	public static HashMap<String, HashMap<String, WarlordPlayer>> players = new HashMap<String, HashMap<String, WarlordPlayer>>();
	public static HashMap<String, List<WarlordTeam>> teams = new HashMap<String, List<WarlordTeam>>();
	public static File arenaFile = new File(DarkArena.instance.getDataFolder(), "warlord.yml");
	public static YamlConfiguration arenaLoader = YamlConfiguration.loadConfiguration(arenaFile);

	public static void loadConfigWarlordArenas() {
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
		arenas.put(arenaId,
				new WarlordArena(arenaId, true, false, arenaLoader.getLong("arenas." + arenaId + ".maxPeriod"),
						arenaLoader.getInt("arenas." + arenaId + ".maxPlayer")));
		List<WarlordTeam> initTeams = new ArrayList<WarlordTeam>();
		initTeams.add(new WarlordTeam(loadLocation("arenas." + arenaId + ".spawnpoint.red"), 0, 0));
		initTeams.add(new WarlordTeam(loadLocation("arenas." + arenaId + ".spawnpoint.blue"), 0, 0));
		teams.put(arenaId, initTeams);
		players.put(arenaId, new HashMap<String, WarlordPlayer>());
	}

	public static Location loadLocation(String path) {
		World world = Bukkit.getWorld(arenaLoader.getString(path + ".world"));
		int x = arenaLoader.getInt(path + ".x");
		int y = arenaLoader.getInt(path + ".y");
		int z = arenaLoader.getInt(path + ".z");
		Location loc = new Location(world, x, y, z);
		return loc;
	}

	private static HashMap<String,Boolean> isCounting=new HashMap<String,Boolean>();
	
	public static boolean joinArena(Player player, String arenaId, TeamType teamType) {
		if (!arenas.get(arenaId).isWaiting)
			return false;
		int maxPlayers = arenas.get(arenaId).maxPlayer;
		HashMap<String, WarlordPlayer> playerMap = players.get(arenaId);
		if (playerMap.keySet().size() >= maxPlayers) {
			return false;
		}
		
		WarlordTeam team = (teamType == TeamType.RED) ? teams.get(arenaId).get(0) : teams.get(arenaId).get(1);
		players.get(arenaId).put(player.getName(), new WarlordPlayer((int) player.getHealth() * 200,
				player.getFoodLevel() * 10, 0, 0, 0, arenaId, player.getName(), team, 
				(teamType == TeamType.BLUE) ? teams.get(arenaId).get(0) : teams.get(arenaId).get(1), false));
		
		if (playerMap.keySet().size()*2 >= maxPlayers) {
			if(isCounting.containsKey(arenaId))
				if(isCounting.get(arenaId)) 
					return true;
			isCounting.remove(arenaId);
			isCounting.put(arenaId,true);
			new BukkitRunnable() {
				int cnt=10;
				@Override
				public void run() {
					cnt--;
					if(cnt<=0) {
						isCounting.remove(arenaId);
						startArena(arenaId);
						cancel();
						return;
					}
					for(String name:playerMap.keySet()) {
						Player player=Bukkit.getPlayer(name);
						player.sendMessage("比赛将在"+cnt+"秒后开始"); 
					}
				}
				
			}.runTaskTimer(DarkArena.instance,0L,20L);
		}
		
		return true;
	}

	public static void quitArena(Player player, String arenaId) {
		players.get(arenaId).remove(player.getName());
	}

	public static boolean startArena(String arenaId) {
		if (!arenas.get(arenaId).isWaiting)
			return false;
		int maxPlayers = arenas.get(arenaId).maxPlayer;
		HashMap<String, WarlordPlayer> playerMap = players.get(arenaId);
		arenas.get(arenaId).isWaiting = false;
		arenas.get(arenaId).isRunning = true;
		for (String name : playerMap.keySet()) {
			Player player = Bukkit.getPlayer(name);
			if (player == null)
				playerMap.remove(name);
			else {
				player.teleport(playerMap.get(name).team.spawnLocation);
				player.sendMessage("比赛开始"); 
			}
		}
		new ScaleBalancer(arenaId).runTaskTimer(DarkArena.instance, 0L, 10L);
		teams.get(arenaId).get(0).currentFlagLocation.getBlock().setType(Material.BEACON);
		teams.get(arenaId).get(1).currentFlagLocation.getBlock().setType(Material.BEACON);
		new BukkitRunnable() {
			int cnt = 0;

			@Override
			public void run() {
				for(String name: players.get(arenaId).keySet()) {
					Player player=Bukkit.getPlayer(name);
					PlayerInventory inv=player.getInventory();
					player.setBedSpawnLocation(players.get(arenaId).get(name).enemy.currentFlagLocation); 
				}
				if (arenas.get(arenaId).isRunning)
					if (cnt++ >= arenas.get(arenaId).lastTime) {
						stopArena(arenaId);
						cancel();
					}
			}

		}.runTaskTimerAsynchronously(DarkArena.instance, 0L, 20L);
		return true;
	}

	public static class ScaleBalancer extends BukkitRunnable {
		String arenaId;
		public ScaleBalancer(String arenaId) {
			this.arenaId=arenaId;
		}
		@Override
		public void run() {
			if (!arenas.get(arenaId).isRunning) {
				cancel();
				return;
			}
			for (String name : WarlordManager.players.get(arenaId).keySet()) {
				WarlordPlayer pl = WarlordManager.players.get(arenaId).get(name);
				Player player = Bukkit.getPlayer(name);
				player.setHealth(pl.health / 200);
				player.setFoodLevel(pl.magicka / 10);
			}
		}
	}
	public static void stopArena(String arenaId) {
		arenas.get(arenaId).unRegEvents();
		List<WarlordTeam> currentTeams = teams.get(arenaId);
		HashMap<String, WarlordPlayer> currentPlayers = players.get(arenaId);
		List<String> winners = new ArrayList<String>();
		List<String> losers = new ArrayList<String>();
		int flag1 = currentTeams.get(0).currentFlags;
		int flag2 = currentTeams.get(1).currentFlags;
		int score1 = currentTeams.get(0).currentScore;
		int score2 = currentTeams.get(1).currentScore;
		for (String name : currentPlayers.keySet()) {
			WarlordPlayer pl = currentPlayers.get(name);
			if (pl.team == currentTeams.get(0))
				winners.add(name);
			if (pl.team == currentTeams.get(1))
				losers.add(name);
		}
		boolean flag = false;
		if (flag1 == flag2)
			if (score1 < score2)
				flag = true;
		if (flag1 < flag2)
			flag = true;
		if (flag) {
			List<String> tmp = new ArrayList<String>(winners);
			winners.clear();
			winners.addAll(losers);
			losers.clear();
			losers.addAll(tmp);
		}
		prideWinner(losers);
		prideLoser(winners);

		loadConfigWarlordArena(arenaId);
	}

	public static void prideLoser(List<String> winners) {
		for(String name:winners) {
			Player player=Bukkit.getPlayer(name);
			player.sendMessage("您的队伍获胜！");
		}
	}

	public static void prideWinner(List<String> losers) {
		for(String name:losers) {
			Player player=Bukkit.getPlayer(name);
			player.sendMessage("您的队伍失败！");
		}
	}
}
