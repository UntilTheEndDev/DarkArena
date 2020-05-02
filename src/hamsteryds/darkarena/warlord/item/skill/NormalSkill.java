package hamsteryds.darkarena.warlord.item.skill;

public class NormalSkill {
	public String name;
	public int minusPP;
	public int cooldown;
	public NormalSkill(String name, int minusPP, int cooldown) {
		this.name = name;
		this.minusPP = minusPP;
		this.cooldown = cooldown;
	}
	
}
