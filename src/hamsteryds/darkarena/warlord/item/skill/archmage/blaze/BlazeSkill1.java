package hamsteryds.darkarena.warlord.item.skill.archmage.blaze;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import hamsteryds.darkarena.DarkArena;

public class BlazeSkill1 implements Listener {
	private Player player;
	private String arenaId;

	public BlazeSkill1(Player player, String arenaId) {
		this.player = player;
		this.arenaId = arenaId;
		Location loc = player.getLocation().add(0, 1.25, 0);
		Vector vec = player.getEyeLocation().getDirection().multiply(1);
		new BukkitRunnable() {
			int counter = 0;
			@Override
			public void run() {
				counter++;
				if (counter >= 500) {
					cancel();
					return;
				}
				loc.add(vec);
				PlayerBackCoordinate coordinate = new PlayerBackCoordinate(loc.clone());
				for (int angle = 0; angle < 360; angle++) {
					double radians = Math.toRadians(angle);
					double x = 0.3 * Math.cos(radians);
					double y = 0.3 * Math.sin(radians);
					Location loc = coordinate.newLocation(x, y);
					loc.getWorld().spawnParticle(Particle.DRIP_LAVA, loc, 1, 0, 0, 0, 0);
				}
				Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc, 0.3, 0.3, 0.3);
				if (!entities.isEmpty()) {
					for (Entity entity : entities) {
						if (entity instanceof Player) {
							if (entity.getEntityId() == player.getEntityId())
								continue;
							Player damagee = (Player) entity;
							double damage = 0.0;
							damage += 9 * (1 /* + add */) + Math.random() - Math.random();
							damage -= counter / 250.0;
							damagee.damage(damage > 0 ? damage : 0);
							callEvent(damagee, damage);
							damagee.sendMessage("§6[战争领主]§r您被" + player.getName() + "释放的火焰喷发攻击，血量减少了"
									+ String.format("%.2f", damage * 200));
							for (Entity passenger : damagee.getNearbyEntities(5, 5, 5)) {
								if (passenger instanceof Player) {
									if (passenger.getEntityId() == player.getEntityId()
											|| passenger.getEntityId() == damagee.getEntityId())
										continue;
									Player damagee2 = (Player) passenger;
									damage = 0;
									damage += 6 * (1 /* + add */) + Math.random() - Math.random();
									damage *= 1 - (damagee2.getLocation().distance(damagee.getLocation()) / 3);
									damagee2.damage(damage > 0 ? damage : 0);
									callEvent(damagee2, damage);
									damagee2.sendMessage("§6[战争领主]§r您被" + player.getName() + "释放的火焰喷发攻击，血量减少了"
											+ String.format("%.2f", damage * 200));
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
							if (entity.getEntityId() == player.getEntityId())
								continue;
							Player damagee = (Player) entity;
							double damage = 0;
							damage += 6 * (1 /* + add */) + Math.random() - Math.random();
							damage *= 1 - (damagee.getLocation().distance(loc) / 5);
							damage -= counter / 250.0;
							damagee.damage(damage > 0 ? damage : 0);
							callEvent(damagee, damage);
							damagee.sendMessage("§6[战争领主]§r您被" + player.getName() + "释放的火球术攻击，血量减少了"
									+ String.format("%.2f", damage * 200));
						}
					}
					loc.getWorld().spawnParticle(Particle.FLAME, loc, 3);
					cancel();
					return;
				}

			}

			private void callEvent(Player damagee, double damage) {
				EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(player, damagee, DamageCause.CUSTOM,
						damage);
				Bukkit.getPluginManager().callEvent(event);
			}

			// THANKS: 602723113
			class PlayerBackCoordinate {
				private Location zeroDot;
				private double rotateAngle;

				public PlayerBackCoordinate(Location playerLocation) {
					rotateAngle = playerLocation.getYaw();
					zeroDot = playerLocation.clone();
					zeroDot.setPitch(0);
					zeroDot.add(zeroDot.getDirection().multiply(-0.3));
				}

				public Location getZeroDot() {
					return zeroDot;
				}

				public Location newLocation(double x, double y) {
					return rotateLocationAboutPoint(zeroDot.clone().add(-x, y, 0), rotateAngle, zeroDot);
				}

				/**
				 * 在二维平面上利用给定的中心点逆时针旋转一个点
				 * 
				 * @param location
				 *            待旋转的点
				 * @param angle
				 *            旋转角度
				 * @param point
				 *            中心点
				 * @return {@link Location}
				 */
				public Location rotateLocationAboutPoint(Location location, double angle, Location point) {
					double radians = Math.toRadians(angle);
					double dx = location.getX() - point.getX();
					double dz = location.getZ() - point.getZ();
					double newX = dx * Math.cos(radians) - dz * Math.sin(radians) + point.getX();
					double newZ = dz * Math.cos(radians) + dx * Math.sin(radians) + point.getZ();
					return new Location(location.getWorld(), newX, location.getY(), newZ);
				}
			}
		}.runTaskTimer(DarkArena.instance, 0L, 1L);
	}
}
