package hamsteryds.darkarena.warlord.util;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
		new ScaleBalancer().runTaskTimer(DarkArena.instance, 0L, 10L);
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
			if (event.getClickedBlock() != null) {
				Block block = event.getClickedBlock();
				Location loc = block.getLocation();
				if (loc == pl.team.currentFlagLocation && pl.team.currentFlagLocation != pl.team.spawnLocation) {
					block.setType(Material.AIR);
					pl.team.spawnLocation.getBlock().setType(Material.BEACON);
					player.sendMessage("您成功夺回了旗帜");
					// TODO 夺回旗帜
				}
				if ((!pl.isCarryingFlag) && loc.distance(pl.enemy.currentFlagLocation) <= 1
						&& block.getType() == Material.BEACON) {
					pl.isCarryingFlag = true;
					block.setType(Material.AIR);
					player.sendMessage("您成功抢夺了旗帜");
					// TODO 抢夺旗帜
				}
				event.setCancelled(true);
			}
			if (event.getClickedBlock() != null && pl.isCarryingFlag) {
				Block block = event.getClickedBlock();
				Location loc = block.getLocation();
				if (pl.isCarryingFlag) {
					if (loc.distance(pl.team.currentFlagLocation) <= 1) {
						pl.team.currentFlags++;
						pl.team.currentFlagLocation.getBlock().setType(Material.AIR);
						pl.enemy.currentFlagLocation.getBlock().setType(Material.AIR);
						pl.isCarryingFlag=false;
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
						if (pl.team.currentFlags >= 3)
							WarlordManager.stopArena(this.arenaId);

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
				System.out.print("awa");
				Location death = player.getLocation().getBlock().getLocation();
				pl.enemy.currentFlagLocation = death;
				player.getLocation().getBlock().setType(Material.BEACON);
				pl.isCarryingFlag = false;
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
		}
	}

	public class ScaleBalancer extends BukkitRunnable {
		@Override
		public void run() {
			if (!isRunning) {
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
}
