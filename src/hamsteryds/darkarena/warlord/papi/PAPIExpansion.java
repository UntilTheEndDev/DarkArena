package hamsteryds.darkarena.warlord.papi;

import org.bukkit.entity.Player;

import hamsteryds.darkarena.warlord.runner.WarlordManager;
import hamsteryds.darkarena.warlord.util.WarlordArena;
import hamsteryds.darkarena.warlord.util.WarlordPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PAPIExpansion extends PlaceholderExpansion {

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	public String onPlaceholderRequest(Player player, String identifier) {
		if (player == null)
			return "";
		WarlordPlayer pl = findPlayer(player.getName());
		WarlordArena arena = findArena(player.getName());
		if(pl==null||arena==null) return "";
		if (identifier.equalsIgnoreCase("plKill")) {
			return String.valueOf(pl.kill);
		} else if(identifier.equalsIgnoreCase("plAssist")) {
			return String.valueOf(pl.assist);
		} else if(identifier.equalsIgnoreCase("plDeath")) {
			return String.valueOf(pl.death);
		} else if(identifier.equalsIgnoreCase("teamScore")) {
			return String.valueOf(pl.team.currentScore);
		} else if(identifier.equalsIgnoreCase("arenaLast")) {
			return String.valueOf(arena.lastTime);
		} else if(identifier.equalsIgnoreCase("plTotalATK")) {
			return String.valueOf(pl.totalATK);
		} else if(identifier.equalsIgnoreCase("plTotalCure")) {
			return String.valueOf(pl.totalCure);
		} else if(identifier.equalsIgnoreCase("plPP")) {
			return String.valueOf(pl.magicka);
		} else if(identifier.equalsIgnoreCase("plHP")) {
			return String.valueOf(pl.health);
		} else
			return "";
	}

	public WarlordPlayer findPlayer(String name) {
		for (String arenaId : WarlordManager.players.keySet())
			if (WarlordManager.players.get(arenaId).containsKey(name))
				return WarlordManager.players.get(arenaId).get(name);
		return null;
	}
	public WarlordArena findArena(String name) {
		for (String arenaId : WarlordManager.players.keySet())
			if (WarlordManager.players.get(arenaId).containsKey(name))
				return WarlordManager.arenas.get(arenaId);
		return null;
	}
}
