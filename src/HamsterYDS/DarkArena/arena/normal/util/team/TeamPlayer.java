package HamsterYDS.DarkArena.arena.normal.util.team;

import HamsterYDS.DarkArena.roles.PlayerRole;
import HamsterYDS.DarkArena.stats.PlayerStat;

public class TeamPlayer {
	protected Team team;
	protected int economy;
	protected int hp;
	protected int maxhp;
	protected int pp;
	protected int maxpp;
	protected PlayerRole role;
	protected PlayerStat stat;
	protected int deaths;
	protected int kills;
	protected int assists;
	public TeamPlayer(Team team, int hp, int maxhp, int pp, int maxpp, PlayerRole role, PlayerStat stat) {
		this.team = team;
		this.economy = 0;
		this.hp = hp;//TODO
		this.maxhp = maxhp;//TODO
		this.pp = pp;//TODO
		this.maxpp = maxpp;//TODO
		this.role = role;//TODO
		this.stat = stat;//TODO
		this.deaths = 0;
		this.kills = 0;
		this.assists = 0;
	}
	public Team getTeam() {
		return team;
	}
	public int getEconomy() {
		return economy;
	}
	public int getHp() {
		return hp;
	}
	public int getMaxhp() {
		return maxhp;
	}
	public int getPp() {
		return pp;
	}
	public int getMaxpp() {
		return maxpp;
	}
	public PlayerRole getRole() {
		return role;
	}
	public PlayerStat getStat() {
		return stat;
	}
	public int getDeaths() {
		return deaths;
	}
	public int getKills() {
		return kills;
	}
	public int getAssists() {
		return assists;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public void setEconomy(int economy) {
		this.economy = economy;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public void setMaxhp(int maxhp) {
		this.maxhp = maxhp;
	}
	public void setPp(int pp) {
		this.pp = pp;
	}
	public void setMaxpp(int maxpp) {
		this.maxpp = maxpp;
	}
	public void setRole(PlayerRole role) {
		this.role = role;
	}
	public void setStat(PlayerStat stat) {
		this.stat = stat;
	}
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}
	public void setKills(int kills) {
		this.kills = kills;
	}
	public void setAssists(int assists) {
		this.assists = assists;
	}
	
}
