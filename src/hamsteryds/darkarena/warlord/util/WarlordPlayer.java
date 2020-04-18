package hamsteryds.darkarena.warlord.util;

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
	}
}
