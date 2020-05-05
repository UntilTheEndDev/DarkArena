package hamsteryds.darkarena.warlord.task;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import hamsteryds.darkarena.DarkArena;
import hamsteryds.darkarena.warlord.WarlordManager;
import hamsteryds.darkarena.warlord.util.WarlordTeam;

public class CompassTargeter extends BukkitRunnable implements Listener {
	private String arenaId;
	public HashSet<UUID> pointTeamFlag = new HashSet<UUID>();

	public CompassTargeter(String arenaId) {
		this.arenaId = arenaId;
		for (UUID uuid : WarlordManager.players.get(arenaId).keySet())
			pointTeamFlag.add(uuid);
		this.runTaskTimer(DarkArena.instance, 0L, 20L);
		Bukkit.getServer().getPluginManager().registerEvents(this, DarkArena.instance);
	}

	@Override
	public void run() {
		for (UUID uuid : WarlordManager.players.get(arenaId).keySet()) {
			Player player = Bukkit.getPlayer(uuid);
			WarlordTeam team = this.pointTeamFlag.contains(uuid) ? WarlordManager.players.get(arenaId).get(uuid).team
					: WarlordManager.players.get(arenaId).get(uuid).enemy;
			if (team.whoIsCarrying != null)
				player.setCompassTarget(Bukkit.getPlayer(team.whoIsCarrying).getLocation());
			else
				player.setCompassTarget(team.currentFlagLocation);
		}
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		if (!WarlordManager.arenas.get(this.arenaId).isRunning)
			return;
		if (!event.hasItem())
			return;
		ItemStack item = event.getItem();
		if (item.getType() == Material.COMPASS
				&& WarlordManager.players.get(this.arenaId).containsKey(event.getPlayer().getUniqueId())) {
			Player player = event.getPlayer();
			if (this.pointTeamFlag.contains(player.getUniqueId()))
				this.pointTeamFlag.remove(player.getUniqueId());
			else
				this.pointTeamFlag.add(player.getUniqueId());
			player.sendMessage("§6[战争领主]§r已经切换指南针模式");
		}
	}
}
