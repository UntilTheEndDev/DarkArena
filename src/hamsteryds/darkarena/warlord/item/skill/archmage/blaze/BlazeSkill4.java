package hamsteryds.darkarena.warlord.item.skill.archmage.blaze;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import hamsteryds.darkarena.DarkArena;

public class BlazeSkill4 implements Listener {
	private Player player;
	private String arenaId;

	public BlazeSkill4(Player player, String arenaId) {
		this.player = player;
		this.arenaId = arenaId;
		Bukkit.getPluginManager().registerEvents(this, DarkArena.instance);
		new BukkitRunnable() {
			@Override
			public void run() {
				unregister();
			}
		}.runTaskLater(DarkArena.instance, 400L);
	}

	public void unregister() {
		HandlerList.unregisterAll(this);
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onAttack(EntityDamageByEntityEvent event) {
		Entity damager = event.getDamager();
		if (damager.getUniqueId().equals(player.getUniqueId())) {
			event.setDamage(event.getDamage() * (1 + 0.5));
		}
	}
}
