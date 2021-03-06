package hamsteryds.darkarena.warlord.util;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import hamsteryds.darkarena.DarkArena;
import hamsteryds.darkarena.warlord.WarlordManager;
import hamsteryds.darkarena.warlord.listener.PlayerListener;
import hamsteryds.darkarena.warlord.listener.TeamListener;
import hamsteryds.darkarena.warlord.util.WarlordTeam.TeamType;

public class WarlordArena implements Listener {
	public int waitPeriod;
	public String arenaId;
	public boolean isWaiting;
	public boolean isRunning;
	public boolean isTicking;
	public long lastTime;
	public int maxPlayer;

	public WarlordArena(String arenaId, long lastTime, int maxPlayer) {
		this.arenaId = arenaId;
		this.isWaiting = true;
		this.isRunning = false;
		this.isTicking = false;
		this.lastTime = lastTime;
		this.maxPlayer = maxPlayer;
		this.waitPeriod=WarlordManager.arenaLoader.getInt("arenas."+this.arenaId+".waitPeriod");
		new TeamListener(this.arenaId);
		new PlayerListener(this.arenaId);
	}

	public String addPlayer(Player player, TeamType teamType) {
		if (!isWaiting)
			return "STARTED";
		HashMap<UUID, WarlordPlayer> players = WarlordManager.players.get(this.arenaId);
		if (players.keySet().size() >= this.maxPlayer)
			return "ARENAFULL";
		List<WarlordTeam> teams = WarlordManager.teams.get(this.arenaId);
		WarlordTeam team = (teamType == TeamType.RED) ? teams.get(0) : teams.get(1);
		WarlordTeam enemy = (teamType == TeamType.BLUE) ? teams.get(0) : teams.get(1);
		if (team.currentPlayers * 2 > this.maxPlayer) {
			WarlordTeam tmp = enemy;
			enemy = team;
			team = tmp;
			return "SUCCESSFULLY JOIN ANOTHER TEAM";
		}
		WarlordManager.players.get(this.arenaId).put(player.getUniqueId(), new WarlordPlayer((int) player.getHealth() * 200,
				player.getFoodLevel() * 10, 200, 0, 0, 0, this.arenaId, player.getName(), team, enemy, false));

		if (players.keySet().size() * 2 >= this.maxPlayer && (!this.isTicking)) {
			this.isTicking = true;
			new BukkitRunnable() {
				int cnt = waitPeriod;

				@Override
				public void run() {
					if (cnt <= 0) {
						WarlordManager.startArena(arenaId);
						cancel();
					}else {
						if (cnt % 30 == 0 || 
						   (cnt % 10 == 0 && cnt <= 30) || 
						   (cnt % 1 == 0 && cnt <= 10))
							for (UUID uuid : players.keySet()) {
								OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
								if(player.isOnline())
									((Player)player).sendMessage("§6[战争领主]§r比赛将在" + cnt + "秒后开始");
							}
					}
					cnt--;
				}

			}.runTaskTimer(DarkArena.instance, 0L, 20L);
		}
		return "SUCCESSFULLY JOIN CORRECT TEAM";
	}
}
