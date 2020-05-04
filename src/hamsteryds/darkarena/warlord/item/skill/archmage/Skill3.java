package hamsteryds.darkarena.warlord.item.skill.archmage;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import hamsteryds.darkarena.DarkArena;

public class Skill3{
	private Player player;
	private String arenaId;
	public Skill3(Player player,String arenaId) {
		this.player=player;
		this.arenaId=arenaId;
		Location loc=player.getLocation();
		new BukkitRunnable() {
			int counter = 0;

			@Override
			public void run() {
				loc.getWorld().spawnParticle(Particle.CLOUD, loc, 10);
				counter++;
				if (counter >= 4 * 5) {
					loc.getWorld().spawnParticle(Particle.END_ROD, loc, 2);
					loc.getWorld().spawnParticle(Particle.END_ROD, player.getLocation(), 2);
					player.teleport(loc);
					player.setHealth((player.getHealth() + player.getMaxHealth() * 0.25) >= player.getMaxHealth()
							? player.getMaxHealth()
							: (player.getHealth() + player.getMaxHealth() * 0.25));
					player.sendMessage("§6[战争领主]§r已经传送回原地并恢复大量血量！");
					cancel();
					return;
				}
			}
		}.runTaskTimer(DarkArena.instance, 0L, 5L);
	}
}
