package hamsteryds.darkarena.warlord.util;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import hamsteryds.darkarena.DarkArena;
import hamsteryds.darkarena.warlord.runner.WarlordManager;

public class WarlordArena implements Listener {
	public String arenaId;
	public boolean isWaiting;
	public boolean isRunning;
	public long lastTime;
	public int maxPlayer;

	public WarlordArena(String arenaId, boolean isWaiting, boolean isRunning, long lastTime, int maxPlayer) {
		this.arenaId = arenaId;
		this.isWaiting = isWaiting;
		this.isRunning = isRunning;
		this.lastTime = lastTime;
		this.maxPlayer = maxPlayer;
		Bukkit.getServer().getPluginManager().registerEvents(this, DarkArena.instance);
	}

	public void unRegEvents() {
		HandlerList.unregisterAll(this);
	}

	public static HashMap<String, String> rejoinDatas = new HashMap<String, String>();
	public static HashMap<String, WarlordPlayer> rejoinTeams = new HashMap<String, WarlordPlayer>();

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (WarlordManager.players.get(this.arenaId).containsKey(player.getName())) {
			rejoinDatas.remove(player.getName());
			rejoinDatas.put(player.getName(), this.arenaId);

			rejoinTeams.remove(player.getName());
			rejoinTeams.put(player.getName(), WarlordManager.players.get(this.arenaId).get(player.getName()));

			WarlordManager.players.get(this.arenaId).remove(player.getName());
			new BukkitRunnable() {
				@Override
				public void run() {
					rejoinDatas.remove(player.getName());
				}
			}.runTaskLaterAsynchronously(DarkArena.instance, 600 * 20L);
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (rejoinDatas.containsKey(player.getName())) {
			WarlordManager.players.get(this.arenaId).put(player.getName(), rejoinTeams.get(player.getName()));
			Location loc = rejoinTeams.get(player.getName()).team.spawnLocation;
			rejoinDatas.remove(player.getName());
			rejoinTeams.remove(player.getName());
			new BukkitRunnable() {
				@Override
				public void run() {
					player.teleport(loc);
				}
			}.runTaskLater(DarkArena.instance, 2L);
		}
	}

	@EventHandler
	public void onChangeWorld(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		if (WarlordManager.players.get(this.arenaId).containsKey(player.getName())) {
			rejoinDatas.remove(player.getName());
			rejoinDatas.put(player.getName(), this.arenaId);

			rejoinTeams.remove(player.getName());
			rejoinTeams.put(player.getName(), WarlordManager.players.get(this.arenaId).get(player.getName()));

			WarlordManager.players.get(this.arenaId).remove(player.getName());
			new BukkitRunnable() {
				@Override
				public void run() {
					rejoinDatas.remove(player.getName());
				}
			}.runTaskLaterAsynchronously(DarkArena.instance, 600 * 20L);
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (WarlordManager.players.get(this.arenaId).containsKey(player.getName())) {
			if (this.isWaiting || (!this.isRunning)) {
				event.setCancelled(true);
				return;
			}

			WarlordPlayer pl = WarlordManager.players.get(this.arenaId).get(player.getName());
			if (player.getLocation().getWorld() != pl.team.spawnLocation.getWorld()){
				WarlordManager.quitArena(player, arenaId);
				return;
			}
			if (event.getClickedBlock() != null) {
				Block block = event.getClickedBlock();
				Location loc = block.getLocation();
				if (loc == pl.team.currentFlagLocation && pl.team.currentFlagLocation != pl.team.spawnLocation) {
					block.setType(Material.AIR);
					pl.team.spawnLocation.getBlock().setType(Material.BEACON);
					player.sendMessage("您成功夺回了旗帜");
					for(String name:WarlordManager.players.get(this.arenaId).keySet()) {
						Player gamePlayer=Bukkit.getPlayer(name);
						if(WarlordManager.players.get(this.arenaId).get(name).enemy==pl.team) {
							gamePlayer.sendMessage("旗帜被对方玩家"+pl.name+"夺回！");
						}else {
							gamePlayer.sendMessage(pl.name+"夺回了旗帜！");
						}
					}
					// TODO 夺回旗帜
				}
				if ((!pl.isCarryingFlag) && loc.distance(pl.enemy.currentFlagLocation) <= 1
						&& block.getType() == Material.BEACON) {
					pl.isCarryingFlag = true;
					block.setType(Material.AIR);
					player.sendMessage("您成功抢夺了旗帜");
					// TODO 抢夺旗帜
					for(String name:WarlordManager.players.get(this.arenaId).keySet()) {
						Player gamePlayer=Bukkit.getPlayer(name);
						if(WarlordManager.players.get(this.arenaId).get(name).enemy==pl.team) {
							gamePlayer.sendMessage(pl.name+"夺取了你队的旗帜，快去拦截！");
						}else {
							gamePlayer.sendMessage(pl.name+"拿到了敌对的旗帜，快去掩护他！");
						}
					}
				}
				event.setCancelled(true);
			}
			if (event.getClickedBlock() != null && pl.isCarryingFlag) {
				Block block = event.getClickedBlock();
				Location loc = block.getLocation();
				if (pl.isCarryingFlag) {
					if (loc.distance(pl.team.currentFlagLocation) <= 1) {
						pl.team.currentScore+=250;
						pl.team.currentFlagLocation.getBlock().setType(Material.AIR);
						pl.enemy.currentFlagLocation.getBlock().setType(Material.AIR);
						pl.isCarryingFlag = false;
						new BukkitRunnable() {
							@Override
							public void run() {
								pl.team.currentFlagLocation = pl.team.spawnLocation;
								pl.enemy.currentFlagLocation = pl.enemy.spawnLocation;
								pl.team.currentFlagLocation.getBlock().setType(Material.BEACON);
								pl.enemy.currentFlagLocation.getBlock().setType(Material.BEACON);
							}

						}.runTaskLater(DarkArena.instance, 200L);
						player.sendMessage("您成功为队伍赢得1分");
						for(String name:WarlordManager.players.get(this.arenaId).keySet()) {
							Player gamePlayer=Bukkit.getPlayer(name);
							gamePlayer.sendMessage(pl.name+"夺取并带回了敌方的战旗，为战队赢得了一分！");
							gamePlayer.sendMessage("旗帜将在10秒钟后重新生成！");
						}
						// TODO 抢夺旗帜成功
					}
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if (WarlordManager.players.get(this.arenaId).containsKey(player.getName())) {
			WarlordPlayer pl = WarlordManager.players.get(this.arenaId).get(player.getName());
			if (pl.isCarryingFlag) {
				Location death = player.getLocation().getBlock().getLocation();
				pl.enemy.currentFlagLocation = death;
				player.getLocation().getBlock().setType(Material.BEACON);
				pl.isCarryingFlag = false;
				
				for(String name:WarlordManager.players.get(this.arenaId).keySet()) {
					Player gamePlayer=Bukkit.getPlayer(name);
					gamePlayer.sendMessage("玩家"+pl.name+"掉落了旗帜！");
				}
				new BukkitRunnable() {
					@Override
					public void run() {
						if (pl.enemy.currentFlagLocation == death) {
							pl.enemy.currentFlagLocation = pl.enemy.spawnLocation;
							death.getBlock().setType(Material.AIR);
							pl.enemy.currentFlagLocation.getBlock().setType(Material.BEACON);
							// TODO 自动回家
						}
					}
				}.runTaskLater(DarkArena.instance, 400L);
				return;
			}
			pl.death++;
			String killer = "";
			if (WarlordManager.players.get(this.arenaId)
					.containsKey(pl.attackTimeStamps.get(pl.attackTimeStamps.size() - 1))) {
				WarlordManager.players.get(this.arenaId)
						.get(pl.attackTimeStamps.get(pl.attackTimeStamps.size() - 1)).kill++;
				WarlordManager.players.get(this.arenaId)
				.get(pl.attackTimeStamps.get(pl.attackTimeStamps.size() - 1)).team.currentScore+=5;
				killer = pl.attackTimeStamps.get(pl.attackTimeStamps.size() - 1);
			}
			int highestAmount = -1;
			String assist = "";
			for (String damager : pl.attackAmountStamps.keySet()) {
				if (damager.equalsIgnoreCase(killer))
					continue;
				if (pl.attackAmountStamps.get(damager) >= highestAmount) {
					assist = damager;
					highestAmount = pl.attackAmountStamps.get(damager);
				}
			}
			if (WarlordManager.players.get(this.arenaId).containsKey(assist))
				WarlordManager.players.get(this.arenaId).get(assist).assist++;
			pl.attackAmountStamps.clear();
			pl.attackTimeStamps.clear();
		}
	}

	@EventHandler
	public void onATK(EntityDamageByEntityEvent event) {
		Entity damager = event.getDamager();
		Entity damagee = event.getEntity();
		if (WarlordManager.players.get(this.arenaId).containsKey(damager.getName())
				&& WarlordManager.players.get(this.arenaId).containsKey(damagee.getName())) {
			event.setCancelled(true);
			if (WarlordManager.players.get(this.arenaId).get(damager.getName()).team == WarlordManager.players
					.get(this.arenaId).get(damagee.getName()).team)
				return;
			WarlordPlayer pl = WarlordManager.players.get(this.arenaId).get(damagee.getName());
			pl.health -= event.getDamage() * 200;
			pl.attackTimeStamps.add(damager.getName());
			int currentATK = pl.attackAmountStamps.containsKey(damager.getName())
					? pl.attackAmountStamps.get(damager.getName())
					: 0;
			pl.attackAmountStamps.remove(damager.getName());
			pl.attackAmountStamps.put(damager.getName(), (int) (currentATK + event.getDamage() * 200));
			WarlordManager.players.get(this.arenaId).get(damager.getName()).totalATK += event.getDamage() * 200;
		}
	}

}
