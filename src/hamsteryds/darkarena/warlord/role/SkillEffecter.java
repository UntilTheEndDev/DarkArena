package hamsteryds.darkarena.warlord.role;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import hamsteryds.darkarena.DarkArena;

public class SkillEffecter {
	public static void effect(ItemStack item, Player player) {
		Material material = item.getType();
		if (material == Material.SULPHUR)
			return;
		String itemName = item.getItemMeta().getDisplayName();
		
		if (itemName.contains("时空断裂")) {
			setCooldowning(item, 20);
			Location loc = player.getLocation().clone();
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
						player.sendMessage("已经传送回原地并恢复大量血量！");
						cancel();
						return;
					}
				}
			}.runTaskTimer(DarkArena.instance, 0L, 5L);
		}
		
		if(itemName.contains("奥术护盾")) {
			setCooldowning(item, 20);
		}
	}

	private static void setCooldowning(ItemStack item, int cd) {
		Material material = item.getType();
		new BukkitRunnable() {
			int counter = 0;

			@Override
			public void run() {
				item.setType(Material.SULPHUR);
				item.setAmount(cd - counter);
				if (counter++ >= cd) {
					item.setType(material);
					cancel();
					return;
				}
			}
		}.runTaskTimer(DarkArena.instance, 0L, 20L);
	}
}
