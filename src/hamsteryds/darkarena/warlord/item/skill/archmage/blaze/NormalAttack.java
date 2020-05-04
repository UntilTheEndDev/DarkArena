package hamsteryds.darkarena.warlord.item.skill.archmage.blaze;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import hamsteryds.darkarena.DarkArena;
import hamsteryds.darkarena.warlord.stat.StatsManager;
import hamsteryds.darkarena.warlord.stat.gui.ArchmageSkill;

public class NormalAttack {
	private String arenaId;
	private Player player;

	public NormalAttack(Player player, String arenaId) {
		this.player = player;
		this.arenaId = arenaId;
		Location loc = player.getLocation().add(0, 1.25, 0);
		Vector vec = player.getEyeLocation().getDirection().multiply(1);
		int level = StatsManager.playerDatas.get(player.getUniqueId()).archmage.sk1Level;
		double add = level==0?0:ArchmageSkill.sk1Figures.get(level).get("{blaze}");
		new BukkitRunnable() {
			int counter = 0;

			@Override
			public void run() {
				loc.add(vec);
				loc.getWorld().spawnParticle(Particle.LAVA, loc, 1);
				Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc, 0.2, 0.2, 0.2);
				if (!entities.isEmpty()) {
					for (Entity entity : entities) {
						if (entity instanceof Player) {
							if (entity.getEntityId()==player.getEntityId())
								continue;
							Player damagee = (Player) entity;
							double damage = 0.0;
							damage += 4 * (1 + add) + Math.random() - Math.random();
							damage -= counter / 500.0;
							damagee.damage(damage);
							callEvent(damagee, damage);
							damagee.sendMessage("§6[战争领主]§r您被" + player.getName() + "释放的火球术攻击，血量减少了" + damage * 200);
							for (Entity passenger : damagee.getNearbyEntities(3, 3, 3)) {
								if (passenger instanceof Player) {
									if (passenger.getEntityId()==player.getEntityId()||
											passenger.getEntityId()==damagee.getEntityId())
										continue;
									Player damagee2 = (Player) passenger;
									damage *= 1 - (damagee2.getLocation().distance(damagee.getLocation()) / 3);
									damagee2.damage(damage);
									callEvent(damagee2, damage);
									damagee2.sendMessage(
											"§6[战争领主]§r您被" + player.getName() + "释放的火球术攻击，血量减少了" + damage * 200);
								}
							}
							loc.getWorld().spawnParticle(Particle.FLAME, loc, 3);
							cancel();
							return;
						}
					}
				}
				if (loc.getBlock().getType() != Material.AIR) {
					for (Entity entity : loc.getWorld().getNearbyEntities(loc, 5, 5, 5)) {
						if (entity instanceof Player) {
							if (entity.getEntityId()==player.getEntityId())
								continue;
							Player damagee = (Player) entity;
							double damage = 0;
							damage += 4 * (1 + add) + Math.random() - Math.random();
							damage *= 1 - (damagee.getLocation().distance(loc) / 5);
							damagee.damage(damage);
							callEvent(damagee, damage);
							damagee.sendMessage("§6[战争领主]§r您被" + player.getName() + "释放的火球术攻击，血量减少了" + damage * 200);
						}
					}
					loc.getWorld().spawnParticle(Particle.FLAME, loc, 3);
					cancel();
					return;
				}
				if (counter++ >= 1000) {
					cancel();
					return;
				}
			}

			private void callEvent(Player damagee, double damage) {
				EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(player, damagee, DamageCause.CUSTOM,
						damage);
				Bukkit.getPluginManager().callEvent(event);
			}
		}.runTaskTimer(DarkArena.instance, 0L, 1L);
	}
}
