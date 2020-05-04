package hamsteryds.darkarena.warlord.item.skill.archmage;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import hamsteryds.darkarena.DarkArena;

public class NormalAttack{
	private String arenaId;
	private Player player;
	public NormalAttack(Player player,String arenaId) {
		this.player=player;
		this.arenaId=arenaId;
		Location loc=player.getLocation();
		new BukkitRunnable() {
			int counter = 0;

			@Override
			public void run() {
				//延展
			}
		}.runTaskTimer(DarkArena.instance, 0L, 5L);
	}
}
