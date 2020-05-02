package hamsteryds.darkarena.warlord.task;

import java.util.UUID;

import org.bukkit.scheduler.BukkitRunnable;

import hamsteryds.darkarena.DarkArena;
import hamsteryds.darkarena.warlord.WarlordManager;
import hamsteryds.darkarena.warlord.util.WarlordPlayer;

public class MagickaImprover extends BukkitRunnable{
	String arenaId;

	public MagickaImprover(String arenaId) {
		this.arenaId = arenaId;
		this.runTaskTimer(DarkArena.instance, 0L, 20L);
	}

	@Override
	public void run() {
		if (!WarlordManager.arenas.get(arenaId).isRunning) {
			cancel();
			return;
		}
		for (UUID uuid : WarlordManager.players.get(arenaId).keySet()) {
			WarlordPlayer pl = WarlordManager.players.get(arenaId).get(uuid);
			if(pl.magicka+10<=pl.maxMagicka)
				pl.magicka+=10;
		}
	}
}
