package hamsteryds.darkarena.warlord.listener;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import hamsteryds.darkarena.DarkArena;
import hamsteryds.darkarena.warlord.WarlordManager;
import hamsteryds.darkarena.warlord.util.WarlordArena;
import hamsteryds.darkarena.warlord.util.WarlordPlayer;

public class TeamListener implements Listener {
	public Listener instance;
	public String arenaId;

	public TeamListener(String arenaId) {
		this.instance = this;
		this.arenaId = arenaId;
		Bukkit.getServer().getPluginManager().registerEvents(this, DarkArena.instance);
	}

	public void unregister() {
		HandlerList.unregisterAll(this);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId(); 
		if (WarlordManager.players.get(this.arenaId).containsKey(uuid)) {
			WarlordArena arena = WarlordManager.arenas.get(this.arenaId);
			WarlordPlayer pl = WarlordManager.players.get(this.arenaId).get(uuid);
			event.setCancelled(true);
			if (!arena.isRunning)
				return;
			if (player.getLocation().getWorld() != pl.team.spawnLocation.getWorld())
				return;
			if (event.getClickedBlock() != null) {
				Block block = event.getClickedBlock();
				Location loc = block.getLocation();
				if (loc.equals(pl.team.currentFlagLocation) && block.getType() == Material.BEACON
						&& (!pl.team.currentFlagLocation.equals(pl.team.spawnLocation))) {
					block.setType(Material.AIR);
					pl.team.spawnLocation.getBlock().setType(Material.BEACON);
					pl.team.currentFlagLocation = pl.team.spawnLocation;
					player.sendMessage("§6[战争领主]§r您成功夺回了旗帜");
					this.announcePlayer(pl, "§6[战争领主]§r" + pl.name + "夺回了旗帜！", "§6[战争领主]§r旗帜被对方玩家" + pl.name + "夺回！");
				}

				if ((!pl.isCarryingFlag) && loc.equals(pl.enemy.currentFlagLocation)
						&& block.getType() == Material.BEACON) {
					pl.isCarryingFlag = true;
					block.setType(Material.AIR);
					player.sendMessage("§6[战争领主]§r您成功抢夺了旗帜");
					this.announcePlayer(pl, "§6[战争领主]§r" + pl.name + "拿到了敌对的旗帜，快去掩护他！",
							"§6[战争领主]§r" + pl.name + "夺取了你队的旗帜，快去拦截！");
				}
			}
			if (event.getClickedBlock() != null && pl.isCarryingFlag) {
				Block block = event.getClickedBlock();
				Location loc = block.getLocation();
				if (pl.isCarryingFlag) {
					if (loc.equals(pl.team.currentFlagLocation)) {
						pl.team.currentScore += 250;

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
						if (pl.team.currentScore >= 1000) {
							WarlordManager.stopArena(arenaId);
							return;
						}
						this.announcePlayer(pl, "§6[战争领主]§r" + pl.name + "夺取并带回了敌方的战旗，为战队赢得了250分！",
								"§6[战争领主]§r" + pl.name + "抢占你方旗帜成功，对方战队赢得了250分！");
						this.announcePlayer(pl, "§6[战争领主]§r旗帜将在10秒钟后重新生成！",
								"§6[战争领主]§r旗帜将在10秒钟后重新生成！");

					}
				}
			}
		}
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
