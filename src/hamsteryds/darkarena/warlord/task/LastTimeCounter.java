package hamsteryds.darkarena.warlord.task;

import org.bukkit.scheduler.BukkitRunnable;

import hamsteryds.darkarena.DarkArena;
import hamsteryds.darkarena.warlord.WarlordManager;

public class LastTimeCounter extends BukkitRunnable {
	String arenaId;

	public LastTimeCounter(String arenaId) {
		this.arenaId = arenaId;
		this.runTaskTimer(DarkArena.instance, 0L, 20L);
	}

	@Override
	public void run() {
		if (WarlordManager.arenas.get(arenaId).isRunning)
			if (WarlordManager.arenas.get(arenaId).lastTime-- <= 0) {
				WarlordManager.stopArena(arenaId);
				cancel();
			}
		if(!WarlordManager.arenas.get(arenaId).isRunning) {
			cancel();
		}
	}
}
