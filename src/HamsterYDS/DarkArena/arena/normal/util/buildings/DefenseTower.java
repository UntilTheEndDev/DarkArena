package HamsterYDS.DarkArena.arena.normal.util.buildings;

import org.bukkit.Location;

import HamsterYDS.DarkArena.arena.normal.runner.ArenaLoader;

public class DefenseTower {
	public static final int originHP=3000;
	public static final int originATK=500;
	public static final int originATKCD=3;
	protected Location loc;
	protected int hp;
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
	public DefenseTower(String name,int index) {
		this.loc=ArenaLoader.loadLocation(name+".defensetowerPoint"+index);
		this.hp = this.originHP;
	}
}
