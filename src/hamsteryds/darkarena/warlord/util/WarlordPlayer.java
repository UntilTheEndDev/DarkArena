package hamsteryds.darkarena.warlord.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WarlordPlayer {
	public int health;
	public int magicka;
	public int kill;
	public int death;
	public int assist;
	public String arenaId;
	public String name;
	public WarlordTeam team;
	public WarlordTeam enemy;
	public boolean isCarryingFlag;

	public HashMap<String, Integer> attackAmountStamps;
	public List<String> attackTimeStamps;
	public int totalATK;
	public int totalCure;

	public WarlordPlayer(int health, int magicka, int kill, int death, int assist, String arenaId, String name,
			WarlordTeam team, WarlordTeam enemy, boolean isCarryingFlag) {
		this.health = health;
		this.magicka = magicka;
		this.kill = kill;
		this.death = death;
		this.assist = assist;
		this.arenaId = arenaId;
		this.name = name;
		this.team = team;
		this.enemy = enemy;
		this.isCarryingFlag = isCarryingFlag;
		this.attackAmountStamps = new HashMap<String, Integer>();
		this.attackTimeStamps = new ArrayList<String>();
		this.totalATK = 0;
		this.totalCure = 0;
	}
}
