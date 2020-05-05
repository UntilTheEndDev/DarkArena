package hamsteryds.darkarena.warlord.listener;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import hamsteryds.darkarena.DarkArena;
import hamsteryds.darkarena.warlord.WarlordManager;
import hamsteryds.darkarena.warlord.util.WarlordPlayer;

public class PlayerListener implements Listener {
	public static HashMap<UUID, String> rejoinDatas = new HashMap<UUID, String>();
	public static HashMap<UUID, WarlordPlayer> rejoinTeams = new HashMap<UUID, WarlordPlayer>();
	public Listener instance;
	public String arenaId;

	public PlayerListener(String arenaId) {
		this.instance = this;
		this.arenaId = arenaId;
		Bukkit.getServer().getPluginManager().registerEvents(this, DarkArena.instance);
	}

	public void unregister() {
		HandlerList.unregisterAll(this);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if (!WarlordManager.arenas.get(this.arenaId).isRunning)
			return;
		Player player = event.getPlayer();
		if (WarlordManager.players.get(this.arenaId).containsKey(player.getUniqueId())) {
			quitBeforeEnding(player, this.arenaId);
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (!WarlordManager.arenas.get(this.arenaId).isRunning)
			return;
		Player player = event.getPlayer();
		if (rejoinDatas.containsKey(player.getUniqueId())) {
			rejoinBeforeEnding(player, rejoinDatas.get(player.getUniqueId()));
		}
	}

	public static boolean rejoinBeforeEnding(Player player, String id) {
		if (id == null)
			return false;
		UUID uuid = player.getUniqueId();
		if (rejoinDatas.containsKey(uuid)) {
			WarlordManager.players.get(id).put(uuid, rejoinTeams.get(uuid));
			Location loc = rejoinTeams.get(uuid).team.spawnLocation;
			rejoinDatas.remove(uuid);
			rejoinTeams.remove(uuid);
			new BukkitRunnable() {
				@Override
				public void run() {
					player.teleport(loc);
				}
			}.runTaskLater(DarkArena.instance, 2L);
		}
		return true;
	}

	public static void quitBeforeEnding(Player player, String id) {
		UUID uuid = player.getUniqueId();
		rejoinDatas.remove(uuid);
		rejoinTeams.remove(uuid);
		rejoinDatas.put(uuid, id);
		rejoinTeams.put(uuid, WarlordManager.players.get(id).get(uuid));
		WarlordManager.players.get(id).remove(uuid);
		new BukkitRunnable() {
			@Override
			public void run() {
				rejoinDatas.remove(uuid);
			}
		}.runTaskLaterAsynchronously(DarkArena.instance, 600 * 20L);
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		if (!WarlordManager.arenas.get(this.arenaId).isRunning)
			return;
		Player player = event.getPlayer();
		if (WarlordManager.players.get(this.arenaId).containsKey(player.getUniqueId())) {
			WarlordPlayer pl = WarlordManager.players.get(this.arenaId).get(player.getUniqueId());
			new BukkitRunnable() {
				@Override
				public void run() {
					player.teleport(pl.team.spawnLocation);
				}
			}.runTaskLater(DarkArena.instance, 15L);
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if (!WarlordManager.arenas.get(this.arenaId).isRunning)
			return;
		Player player = event.getEntity();
		if (WarlordManager.players.get(this.arenaId).containsKey(player.getUniqueId())) {
			WarlordPlayer pl = WarlordManager.players.get(this.arenaId).get(player.getUniqueId());
			if (pl.isCarryingFlag) {
				Location death = player.getLocation().getBlock().getLocation();
				pl.enemy.currentFlagLocation = death;
				player.getLocation().getBlock().setType(Material.BEACON);
				pl.isCarryingFlag = false;
				pl.enemy.whoIsCarrying = null;

				this.announcePlayer(pl, "§6[战争领主]§r玩家" + pl.name + "掉落了敌方旗帜，30秒后自动传回大本营！",
						"§6[战争领主]§r玩家" + pl.name + "掉落了我方旗帜，30秒后自动传回大本营！");
				this.announcePlayer(pl,
						"§6[战争领主]§r坐标为 x:" + player.getLocation().getBlockX() + " y:" + player.getLocation().getBlockY()
								+ " z:" + player.getLocation().getBlockZ(),
						"§6[战争领主]§r坐标为 x:" + player.getLocation().getBlockX() + " y:" + player.getLocation().getBlockY()
								+ " z:" + player.getLocation().getBlockZ());

				new BukkitRunnable() {
					@Override
					public void run() {
						if (pl.enemy.currentFlagLocation.equals(death)) {
							pl.enemy.currentFlagLocation = pl.enemy.spawnLocation;
							death.getBlock().setType(Material.AIR);
							pl.enemy.currentFlagLocation.getBlock().setType(Material.BEACON);
							announcePlayer(pl, "§6[战争领主]§r敌方旗帜无人捡起，已经自动传回！", "§6[战争领主]§r我方旗帜已经自动传回！");
						}
					}
				}.runTaskLater(DarkArena.instance, 600L);
			}
			try {
				// 击杀
				UUID killer = pl.attackTimeStamps.get(pl.attackTimeStamps.size() - 1);
				if (pl.attackTimeStamps.size() == 0)
					return;
				if (WarlordManager.players.get(this.arenaId).containsKey(killer)) {
					WarlordPlayer kpl = WarlordManager.players.get(this.arenaId).get(killer);
					kpl.kill++;
				}
				// 助攻
				UUID assist = killer;
				for (UUID damager : pl.attackAmountStamps.keySet()) {
					if (damager.equals(killer))
						continue;
					if (pl.attackAmountStamps.get(damager) >= pl.attackAmountStamps.get(assist))
						assist = damager;
				}
				if ((!assist.equals(killer)) && WarlordManager.players.get(this.arenaId).containsKey(assist))
					WarlordManager.players.get(this.arenaId).get(assist).assist++;
			} catch (Exception e) {

			}
			// 击杀数据清空结算
			pl.death++;
			pl.enemy.currentScore += 5;
			if (pl.enemy.currentScore >= 1000)
				WarlordManager.stopArena(arenaId);
			pl.attackAmountStamps.clear();
			pl.attackTimeStamps.clear();

		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onHumanATK(EntityDamageByEntityEvent event) {
		if (!WarlordManager.arenas.get(this.arenaId).isRunning)
			return;
		Entity damager = event.getDamager();
		Entity damagee = event.getEntity();
		if (WarlordManager.players.get(this.arenaId).containsKey(damager.getUniqueId())
				&& WarlordManager.players.get(this.arenaId).containsKey(damagee.getUniqueId())) {
			WarlordPlayer derpl = WarlordManager.players.get(this.arenaId).get(damager.getUniqueId());
			WarlordPlayer deepl = WarlordManager.players.get(this.arenaId).get(damagee.getUniqueId());
			if (derpl.team == deepl.team) {
				event.setCancelled(true);
			} else {
				// 时间戳记录
				deepl.health -= event.getDamage() * 200;
				deepl.attackTimeStamps.add(damager.getUniqueId());
				int currentATK = derpl.attackAmountStamps.containsKey(damager.getUniqueId())
						? deepl.attackAmountStamps.get(damager.getUniqueId())
						: 0;
				deepl.attackAmountStamps.remove(damager.getUniqueId());
				deepl.attackAmountStamps.put(damager.getUniqueId(), (int) (currentATK + event.getDamage() * 200));
				derpl.totalATK += event.getDamage() * 200;
			}
		}
	}

	@EventHandler
	public void onOtherATK(EntityDamageEvent event) {
		if (!WarlordManager.arenas.get(this.arenaId).isRunning)
			return;
		Entity damagee = event.getEntity();
		if (WarlordManager.players.get(this.arenaId).containsKey(damagee.getUniqueId())) {
			WarlordPlayer pl = WarlordManager.players.get(this.arenaId).get(damagee.getUniqueId());
			pl.health -= event.getDamage() * 200;
		}
	}

	@EventHandler
	public void onInventoryEvent(InventoryClickEvent event) {
		if (WarlordManager.players.get(this.arenaId).containsKey(event.getWhoClicked().getUniqueId()))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onThrow(PlayerDropItemEvent event) {
		if (WarlordManager.players.get(this.arenaId).containsKey(event.getPlayer().getUniqueId()))
			event.setCancelled(true);
	}

	public void announcePlayer(WarlordPlayer pl, String teamMessage, String enemyMessage) {
		for (UUID uuid : WarlordManager.players.get(this.arenaId).keySet()) {
			Player gamePlayer = Bukkit.getPlayer(uuid);
			if (WarlordManager.players.get(this.arenaId).get(uuid).enemy == pl.team)
				gamePlayer.sendMessage(enemyMessage);
			else
				gamePlayer.sendMessage(teamMessage);
		}
	}
}
