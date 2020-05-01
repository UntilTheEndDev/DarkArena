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
		this.runTaskTimer(DarkArena.instance,0L,20L);
		DarkArena.instance.getServer().getPluginManager().registerEvents(this,DarkArena.instance);
	}

	@Override
	public void run() {
		for(UUID uuid:WarlordManager.players.get(arenaId).keySet()) {
			Player player=Bukkit.getPlayer(uuid);
			WarlordTeam team=this.pointTeamFlag.contains(uuid)
					?WarlordManager.players.get(arenaId).get(uuid).team
					:WarlordManager.players.get(arenaId).get(uuid).enemy;
			if(team.whoIsCarrying!=null)
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
		if (item.getType()==Material.COMPASS){
			if(this.pointTeamFlag.contains(event.getPlayer().getUniqueId()))
				this.pointTeamFlag.remove(event.getPlayer().getUniqueId());
			else this.pointTeamFlag.add(event.getPlayer().getUniqueId());
		}
	}
}