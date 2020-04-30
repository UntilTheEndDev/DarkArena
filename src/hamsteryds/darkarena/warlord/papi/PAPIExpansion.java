package hamsteryds.darkarena.warlord.papi;

import java.util.UUID;

import org.bukkit.entity.Player;

import hamsteryds.darkarena.warlord.runner.WarlordManager;
import hamsteryds.darkarena.warlord.util.WarlordPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PAPIExpansion extends PlaceholderExpansion {

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return "南外丶仓鼠";
	}

	@Override
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return "warlord";
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return "1.0";
	}

	public String onPlaceholderRequest(Player player, String identifier) {
		if (player == null)
			return "";
		String arenaId = findArena(player.getUniqueId());
		if(arenaId==null) return "";
		WarlordPlayer pl=WarlordManager.players.get(arenaId).get(player.getUniqueId());
		if (identifier.equalsIgnoreCase("局内击杀数")) {
			return String.valueOf(pl.kill);
		} else if(identifier.equalsIgnoreCase("局内助攻数")) {
			return String.valueOf(pl.assist);
		} else if(identifier.equalsIgnoreCase("局内死亡数")) {
			return String.valueOf(pl.death);
		} else if(identifier.equalsIgnoreCase("局内队伍分数")) {
			return String.valueOf(pl.team.currentScore);
		} else if(identifier.equalsIgnoreCase("局内结束倒计时")) {
			return String.valueOf(WarlordManager.arenas.get(arenaId).lastTime);
		} else if(identifier.equalsIgnoreCase("局内总输出")) {
			return String.valueOf(pl.totalATK);
		} else if(identifier.equalsIgnoreCase("局内总治疗")) {
			return String.valueOf(pl.totalCure);
		} else if(identifier.equalsIgnoreCase("魔法值")) {
			return String.valueOf(pl.magicka);
		} else if(identifier.equalsIgnoreCase("生命值")) {
			return String.valueOf(pl.health);
		} else
			return "";
	}
	public String findArena(UUID uuid) {
		for (String arenaId : WarlordManager.players.keySet())
			if (WarlordManager.players.get(arenaId).containsKey(uuid))
				return arenaId;
		return null;
	}
}
