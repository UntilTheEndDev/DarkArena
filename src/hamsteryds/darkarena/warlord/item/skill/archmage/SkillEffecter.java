package hamsteryds.darkarena.warlord.item.skill.archmage;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import hamsteryds.darkarena.DarkArena;
import hamsteryds.darkarena.warlord.WarlordManager;
import hamsteryds.darkarena.warlord.item.skill.NormalSkill;
import hamsteryds.darkarena.warlord.item.skill.archmage.blaze.BlazeNormalAttack;
import hamsteryds.darkarena.warlord.item.skill.archmage.blaze.BlazeSkill1;
import hamsteryds.darkarena.warlord.item.skill.archmage.blaze.BlazeSkill4;

public class SkillEffecter implements Listener {
	private String arenaId;
	public static HashMap<String, NormalSkill> skills = new HashMap<String, NormalSkill>();

	public SkillEffecter(String arenaId) {
		this.arenaId = arenaId;
		skills.put("火球术", new NormalSkill("BlazeNormalAttack", 30, 0));
		skills.put("火焰喷发", new NormalSkill("BlazeSkill1", 80, 20));
		skills.put("时空断裂", new NormalSkill("Skill2", 50, 40));
		skills.put("奥术护盾", new NormalSkill("Skill3", 50, 30));
		skills.put("炼狱", new NormalSkill("BlazeSkill4", 0, 60));
		Bukkit.getServer().getPluginManager().registerEvents(this, DarkArena.instance);
		new BukkitRunnable() {

			@Override
			public void run() {
				if (!WarlordManager.arenas.get(arenaId).isRunning) {
					cancel();
					unregister();
				}
			}

		}.runTaskTimer(DarkArena.instance, 0L, 10L);
	}

	public void unregister() {
		HandlerList.unregisterAll(this);
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		if (!WarlordManager.arenas.get(this.arenaId).isRunning)
			return;
		if (!event.hasItem())
			return;
		event.setCancelled(true);
		ItemStack item = event.getItem();
		if (item.hasItemMeta())
			if (item.getItemMeta().hasDisplayName()) {
				String message = effect(item, event.getPlayer(), event.getPlayer().getInventory().getHeldItemSlot());
				if (!message.equalsIgnoreCase(""))
					event.getPlayer().sendMessage(message);
			}
	}

	public String effect(ItemStack item, Player player, int slot) {
		Material material = item.getType();
		if (material == Material.SULPHUR)
			return "§6[战争领主]§r技能冷却中……还剩下" + item.getAmount() + "秒";
		String itemName = item.getItemMeta().getDisplayName();
		if (!skills.containsKey(itemName))
			return "";
		NormalSkill skill = skills.get(itemName);
		if (WarlordManager.players.get(this.arenaId).get(player.getUniqueId()).magicka < skill.minusPP)
			return "§6[战争领主]§r魔法值不足……需要魔法值：" + skill.minusPP + "点";
		WarlordManager.players.get(this.arenaId).get(player.getUniqueId()).magicka -= skill.minusPP;
		setCooldown(player.getInventory(), slot, skill.cooldown);
		switch (skill.name) {
		case "BlazeNormalAttack":
			new BlazeNormalAttack(player, this.arenaId);
			break;
		case "BlazeSkill1":
			new BlazeSkill1(player, this.arenaId);
			break;
		case "Skill2":
			new Skill2(player, this.arenaId);
			break;
		case "Skill3":
			new Skill3(player, this.arenaId);
			break;
		case "BlazeSkill4":
			new BlazeSkill4(player, this.arenaId);
			break;
		default:
			break;
		}

		return "";
	}

	private static void setCooldown(PlayerInventory inv, int slot, int cd) {
		if (cd == 0)
			return;
		ItemStack item = inv.getItem(slot);
		Material material = item.getType();
		new BukkitRunnable() {
			int counter = 1;

			@Override
			public void run() {
				item.setType(Material.SULPHUR);
				item.setAmount(cd - counter + 1);
				inv.setItem(slot, item);
				if (counter >= cd) {

					item.setType(material);
					inv.setItem(slot, item);
					cancel();
					return;
				}
				counter++;
			}
		}.runTaskTimer(DarkArena.instance, 0L, 20L);
	}
}
