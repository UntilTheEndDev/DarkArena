package HamsterYDS.DarkArena.arena.normal.util.buildings;

import org.bukkit.Location;

public class CentreCrystal {
	public static final int originHP=5000;
	public static final int originATK=800;
	public static final int originATKCD=3;
	public static final int originRECOVER=50;
	public static final int originRECOVERCD=5;
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
	public CentreCrystal(Location loc) {
		this.loc=loc;
		this.hp = this.originHP;
		this.atk = this.originATK;
		this.atkcd = this.originATKCD;
//		CentreCrystal instance=this;
//		new BukkitRunnable() {
//			@Override
//			public void run() {
//				if(hp<=originHP-originRECOVER) 
//					hp+=originRECOVER;
//				else 
//					hp=originHP;
//			}
//		}.runTaskTimerAsynchronously(DarkArena.instance,0L,originRECOVERCD);
	}
}
