package HamsterYDS.DarkArena.arena.normal.util.buildings;

import org.bukkit.Location;

public class DefenseTower {
	public static final int originHP=3000;
	public static final int originATK=500;
	public static final int originATKCD=3;
	protected Location loc;
	protected int hp;
	protected int atk;
	protected int atkcd;
	public Location getLoc() {
		return loc;
	}
	public void setHp(Location loc) {
		this.loc = loc;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public int getAtk() {
		return atk;
	}
	public void setAtk(int atk) {
		this.atk = atk;
	}
	public int getAtkcd() {
		return atkcd;
	}
	public void setAtkcd(int atkcd) {
		this.atkcd = atkcd;
	}
	public DefenseTower(Location loc, int hp, int atk, int atkcd) {
		this.loc=loc;
		this.hp = hp;
		this.atk = atk;
		this.atkcd = atkcd;
	}
}
