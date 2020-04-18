package hamsteryds.darkarena.warlord.util;

import hamsteryds.darkarena.warlord.util.WarlordTeam.TeamType;

public class WarlordPlayer {
	public int health;
	public int magicka;
	public int kill;
	public int death;
	public int assist;
	public String arenaId;
	public TeamType team;
	public WarlordPlayer(int health, int magicka, int kill, int death, int assist, String arenaId, TeamType team) {
		this.health = health;
		this.magicka = magicka;
		this.kill = kill;
		this.death = death;
		this.assist = assist;
		this.arenaId = arenaId;
		this.team = team;
	}
}
