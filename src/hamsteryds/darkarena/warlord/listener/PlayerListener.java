package hamsteryds.darkarena.warlord.listener;

import java.util.HashMap;

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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import hamsteryds.darkarena.DarkArena;
import hamsteryds.darkarena.warlord.role.SkillEffecter;
import hamsteryds.darkarena.warlord.runner.WarlordManager;
import hamsteryds.darkarena.warlord.util.WarlordPlayer;

public class PlayerListener implements Listener {
	public static HashMap<String, String> rejoinDatas = new HashMap<String, String>();
	public static HashMap<String, WarlordPlayer> rejoinTeams = new HashMap<String, WarlordPlayer>();
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
		if (WarlordManager.players.get(this.arenaId).containsKey(player.getName())) {
			quitBeforeEnding(player, this.arenaId);
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (!WarlordManager.arenas.get(this.arenaId).isRunning)
			return;
		Player player = event.getPlayer();
		if (rejoinDatas.containsKey(player.getName())) {
			rejoinBeforeEnding(player, rejoinDatas.get(player.getName()));
		}
	}

	public static boolean rejoinBeforeEnding(Player player, String id) {
		if (id == null)
			return false;
		String name = player.getName();
		if (rejoinDatas.containsKey(name)) {
			WarlordManager.players.get(id).put(name, rejoinTeams.get(name));
			Location loc = rejoinTeams.get(name).team.spawnLocation;
			rejoinDatas.remove(name);
			rejoinTeams.remove(name);
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
		String name = player.getName();
		rejoinDatas.remove(name);
		rejoinTeams.remove(name);
		rejoinDatas.put(name, id);
		rejoinTeams.put(name, WarlordManager.players.get(id).get(name));
		WarlordManager.players.get(id).remove(name);
		new BukkitRunnable() {
			@Override
			public void run() {
				rejoinDatas.remove(name);
			}
		}.runTaskLaterAsynchronously(DarkArena.instance, 600 * 20L);
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		if (!WarlordManager.arenas.get(this.arenaId).isRunning)
			return;
		Player player = event.getPlayer();
		if (WarlordManager.players.get(this.arenaId).containsKey(player.getName())) {
			WarlordPlayer pl = WarlordManager.players.get(this.arenaId).get(player.getName());
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
		if (WarlordManager.players.get(this.arenaId).containsKey(player.getName())) {
			WarlordPlayer pl = WarlordManager.players.get(this.arenaId).get(player.getName());
			if (pl.isCarryingFlag) {
				Location death = player.getLocation().getBlock().getLocation();
				pl.enemy.currentFlagLocation = death;
				player.getLocation().getBlock().setType(Material.BEACON);
				pl.isCarryingFlag = false;

				this.announcePlayer(pl, "§6[战争领主]§r玩家" + pl.name + "掉落了敌方旗帜，30秒后自动传回大本营！",
						"§6[战争领主]§r玩家" + pl.name + "掉落了我方旗帜，30秒后自动传回大本营！");
				this.announcePlayer(pl,
						"§6[战争领主]§r坐标为 x:" + player.getLocation().getBlockX() + " y:" + player.getLocation().getBlockX()
								+ " z:" + player.getLocation().getBlockX(),
						"§6[战争领主]§r坐标为 x:" + player.getLocation().getBlockX() + " y:" + player.getLocation().getBlockX()
								+ " z:" + player.getLocation().getBlockX());

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
			// 击杀
			String killer = pl.attackTimeStamps.get(pl.attackTimeStamps.size() - 1);
			if (pl.attackTimeStamps.size() == 0)
				return;
			if (WarlordManager.players.get(this.arenaId).containsKey(killer)) {
				WarlordPlayer kpl = WarlordManager.players.get(this.arenaId).get(killer);
				kpl.kill++;
			}
			// 助攻
			String assist = "";
			for (String damager : pl.attackAmountStamps.keySet()) {
				if (damager.equalsIgnoreCase(killer))
					continue;
				if (pl.attackAmountStamps.get(damager) >= pl.attackAmountStamps.get(assist))
					assist = damager;
			}
			if (WarlordManager.players.get(this.arenaId).containsKey(assist))
				WarlordManager.players.get(this.arenaId).get(assist).assist++;
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
		if (WarlordManager.players.get(this.arenaId).containsKey(damager.getName())
				&& WarlordManager.players.get(this.arenaId).containsKey(damagee.getName())) {
			WarlordPlayer derpl = WarlordManager.players.get(this.arenaId).get(damager.getName());
			WarlordPlayer deepl = WarlordManager.players.get(this.arenaId).get(damagee.getName());
			if (derpl == deepl) {
				event.setCancelled(true);
			} else {
				// 时间戳记录
				deepl.health -= event.getDamage() * 200;
				deepl.attackTimeStamps.add(damager.getName());
				int currentATK = derpl.attackAmountStamps.containsKey(damager.getName())
						? deepl.attackAmountStamps.get(damager.getName())
						: 0;
				deepl.attackAmountStamps.remove(damager.getName());
				deepl.attackAmountStamps.put(damager.getName(), (int) (currentATK + event.getDamage() * 200));
				derpl.totalATK += event.getDamage() * 200;
			}
		}
	}

	@EventHandler
	public void onOtherATK(EntityDamageEvent event) {
		if (!WarlordManager.arenas.get(this.arenaId).isRunning)
			return;
		Entity damagee = event.getEntity();
		if (WarlordManager.players.get(this.arenaId).containsKey(damagee.getName())) {
			WarlordPlayer pl = WarlordManager.players.get(this.arenaId).get(damagee.getName());
			pl.health -= event.getDamage() * 200;
		}
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		if (!WarlordManager.arenas.get(this.arenaId).isRunning)
			return;
		if (!event.hasItem())
			return;
		ItemStack item = event.getItem();
		if (item.hasItemMeta())
			if (item.getItemMeta().hasDisplayName())
				SkillEffecter.effect(item, event.getPlayer());
	}

	public void announcePlayer(WarlordPlayer pl, String teamMessage, String enemyMessage) {
		for (String name : WarlordManager.players.get(this.arenaId).keySet()) {
			Player gamePlayer = Bukkit.getPlayer(name);
			if (WarlordManager.players.get(this.arenaId).get(name).enemy == pl.team)
				gamePlayer.sendMessage(enemyMessage);
			else
				gamePlayer.sendMessage(teamMessage);
		}
	}
}
