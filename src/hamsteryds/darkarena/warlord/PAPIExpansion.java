package hamsteryds.darkarena.warlord;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import hamsteryds.darkarena.warlord.stat.StatsManager;
import hamsteryds.darkarena.warlord.stat.StatsManager.Player$1;
import hamsteryds.darkarena.warlord.util.WarlordArena;
import hamsteryds.darkarena.warlord.util.WarlordPlayer;
import hamsteryds.darkarena.warlord.util.WarlordTeam;
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
		Player$1 data = StatsManager.playerDatas.get(player.getUniqueId());
		if (identifier.equalsIgnoreCase("生涯总场数")) {
			return String.valueOf(data.totalMatch);
		} else if (identifier.equalsIgnoreCase("生涯总胜场")) {
			return String.valueOf(data.totalVictory);
		} else if (identifier.equalsIgnoreCase("生涯总输出")) {
			return String.valueOf(data.totalATK);
		} else if (identifier.equalsIgnoreCase("生涯总治疗")) {
			return String.valueOf(data.totalCure);
		} else if (identifier.equalsIgnoreCase("生涯输出MVP数")) {
			return String.valueOf(data.totalATKMVP);
		} else if (identifier.equalsIgnoreCase("生涯治疗MVP数")) {
			return String.valueOf(data.totalCureMVP);
		}
		
		String arenaId = findArena(player.getUniqueId());
		if (arenaId == null)
			return "";
		WarlordPlayer pl = WarlordManager.players.get(arenaId).get(player.getUniqueId());

		if (identifier.equalsIgnoreCase("局内击杀数")) {
			return String.valueOf(pl.kill);
		} else if (identifier.equalsIgnoreCase("局内助攻数")) {
			return String.valueOf(pl.assist);
		} else if (identifier.equalsIgnoreCase("局内死亡数")) {
			return String.valueOf(pl.death);
		} else if (identifier.equalsIgnoreCase("局内队伍分数")) {
			return String.valueOf(pl.team.currentScore);
		} else if (identifier.equalsIgnoreCase("局内结束倒计时")) {
			return String.valueOf(WarlordManager.arenas.get(arenaId).lastTime);
		} else if (identifier.equalsIgnoreCase("局内总输出")) {
			return String.valueOf(pl.totalATK);
		} else if (identifier.equalsIgnoreCase("局内总治疗")) {
			return String.valueOf(pl.totalCure);
		} else if (identifier.equalsIgnoreCase("魔法值")) {
			return String.valueOf(pl.magicka);
		} else if (identifier.equalsIgnoreCase("生命值")) {
			return String.valueOf(pl.health);
		} else if (identifier.equalsIgnoreCase("同队旗帜状态")) {
			return getFlagState(WarlordManager.arenas.get(arenaId),pl.team);
		} else if (identifier.equalsIgnoreCase("敌队旗帜状态")) {
			return getFlagState(WarlordManager.arenas.get(arenaId),pl.enemy);
		} else
			return "";
	}
	public String getFlagState(WarlordArena arena, WarlordTeam team) {
		if(arena.isWaiting)
			return "等待生成中……";
		if(team.isRespawningFlag)
			return "重新生成中……";
		if(team.currentFlagLocation.getBlock().getType()!=Material.BEACON)
			return "被抢夺";
		if(team.currentFlagLocation.equals(team.spawnLocation))
			return "安全";
		if(!team.currentFlagLocation.equals(team.spawnLocation))
			return "已掉落";
		return "未知错误-无法检测旗帜状态";
	}
	public String findArena(UUID uuid) {
		for (String arenaId : WarlordManager.players.keySet())
			if (WarlordManager.players.get(arenaId).containsKey(uuid))
				return arenaId;
		return null;
	}
}
