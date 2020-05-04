package hamsteryds.darkarena.warlord.item.skill.archmage;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import hamsteryds.darkarena.DarkArena;
import hamsteryds.darkarena.warlord.WarlordManager;
import hamsteryds.darkarena.warlord.item.ItemFactory;
import hamsteryds.darkarena.warlord.item.skill.NormalSkill;
import hamsteryds.darkarena.warlord.item.skill.archmage.blaze.NormalAttack;

public class SkillEffecter implements Listener {
	private String arenaId;
	public static HashMap<String, NormalSkill> skills = new HashMap<String, NormalSkill>();

	public SkillEffecter(String arenaId) {
		this.arenaId = arenaId;
		skills.put("时空断裂", new NormalSkill("Skill2", 50, 40));
		skills.put("奥术护盾", new NormalSkill("Skill3", 50, 30));
		skills.put("火球术", new NormalSkill("NormalAttack", 30, 0));
		skills.put("炼狱", new NormalSkill("BlazeSkill4", 40, 60));
		Bukkit.getServer().getPluginManager().registerEvents(this, DarkArena.instance);
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		if (!WarlordManager.arenas.get(this.arenaId).isRunning)
			return;
		if (!event.hasItem())
			return;
		ItemStack item = event.getItem();
		if (item.hasItemMeta())
			if (item.getItemMeta().hasDisplayName()) {
				String message = effect(item, event.getPlayer());
				if (!message.equalsIgnoreCase(""))
					event.getPlayer().sendMessage(message);
			}
	}

	public String effect(ItemStack item, Player player) {
		Material material = item.getType();
		if (material == ItemFactory.fromLegacy(Material.SULPHUR))
			return "§6[战争领主]§r技能冷却中……还剩下" + item.getAmount() + "秒";
		String itemName = item.getItemMeta().getDisplayName();
		if (!skills.containsKey(itemName))
			return "";
		NormalSkill skill = skills.get(itemName);
		if (WarlordManager.players.get(this.arenaId).get(player.getUniqueId()).magicka < skill.minusPP)
			return "§6[战争领主]§r魔法值不足……需要魔法值：" + skill.minusPP + "点";
		setCooldown(item, skill.cooldown);
		switch (skill.name) {
		case "Skill2":
			new Skill2(player, this.arenaId);
			break;
		case "Skill3":
			new Skill3(player, this.arenaId);
			break;
		case "NormalAttack":
			new NormalAttack(player, this.arenaId);
			break;
		default:
			break;
		}

		return "";
	}

	private static void setCooldown(ItemStack item, int cd) {
		Material material = item.getType();
		new BukkitRunnable() {
			int counter = 1;

			@Override
			public void run() {
				item.setType(Material.SULPHUR);
				item.setAmount(cd - counter + 1);
				if (counter++ >= cd) {
					item.setType(material);
					cancel();
					return;
				}
			}
		}.runTaskTimer(DarkArena.instance, 0L, 20L);
	}
}
