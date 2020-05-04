package hamsteryds.darkarena.warlord.item.skill.archmage;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Skill3{
	private Player player;
	private String arenaId;
	public Skill3(Player player,String arenaId) {
		this.player=player;
		this.arenaId=arenaId;
		player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,99999,3));
	}
}
